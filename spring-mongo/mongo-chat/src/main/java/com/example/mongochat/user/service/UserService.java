package com.example.mongochat.user.service;


import com.sidepr.mono.sns.global.error.exception.NotFoundException;
import com.sidepr.mono.sns.global.fileuploader.FileUploader;
import com.sidepr.mono.sns.user.domain.Follow;
import com.sidepr.mono.sns.user.domain.User;
import com.sidepr.mono.sns.user.dto.UserPasswordChangeRequest;
import com.sidepr.mono.sns.user.dto.UserResponse;
import com.sidepr.mono.sns.user.dto.UserSignupRequest;
import com.sidepr.mono.sns.user.dto.UserUpdateRequest;
import com.sidepr.mono.sns.user.exception.DuplicateUserException;
import com.sidepr.mono.sns.user.exception.NotFoundUserException;
import com.sidepr.mono.sns.user.exception.NotValidUserRelationException;
import com.sidepr.mono.sns.user.repository.FollowRepository;
import com.sidepr.mono.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import static com.sidepr.mono.sns.global.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FileUploader uploader;

    @Value("${file.user}")
    private String DIRECTORY;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    @Transactional(readOnly = true)
    public User login(String email, String password) {

        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RESOURCE_ERROR));
        user.login(passwordEncoder, password);
        return user;
    }

    @Transactional
    public Long signup(UserSignupRequest form){
        if((!isDuplicateUser(form) && !form.isDifferentPassword())){
            form.setEncodedPassword(passwordEncoder.encode(form.getPassword()));
        }
        return userRepository.save(form.toEntity()).getId();
    }

    @Transactional
    public void followUser(Long myUserId, String nickname) {
        User user = findActiveUser(myUserId);
        User followingUser = findActiveUserByNickname(nickname);
        isValidUserRelation(user, followingUser);

        followRepository.save(
                Follow.builder()
                        .followed(user)
                        .following(followingUser)
                        .build()
        );
    }

    @Transactional
    public void unFollowUser(Long myUserId, String nickname) {
        User user = findActiveUser(myUserId);
        User unFollowingUser = findActiveUserByNickname(nickname);
        isValidUserRelation(user, unFollowingUser);

        followRepository.delete(
                findFollowRelationByFollowedAndFollower(unFollowingUser, user)
        );
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(Long id) {
        return UserResponse.of(findActiveUser(id));
    }

    @Transactional(readOnly = true)
    public UserResponse findUserByNickname(String nickname, Long myUserId) {
        User lookUpUser = findActiveUserByNicknameOrUserId(nickname, myUserId);
        UserResponse looUpUserResponse = UserResponse.of(lookUpUser);
        setUserResponseFollowInformation(myUserId, lookUpUser, looUpUserResponse);
        return looUpUserResponse;
    }

    private Follow findFollowRelationByFollowedAndFollower(User followed, User following){
        return followRepository.findByFollowedAndFollower(followed, following)
                .orElseThrow(() -> new NotValidUserRelationException(NOT_VALID_REQUEST_ERROR));
    }

    private User findActiveUserByNicknameOrUserId(String nickname, Long myUserId){
        if(!StringUtils.hasText(nickname)){
            return findActiveUser(myUserId);
        }
        return findActiveUserByNickname(nickname);
    }

    private void setUserResponseFollowInformation(Long myUserId, User lookUpUser, UserResponse looUpUserResponse) {
        if(Objects.equals(looUpUserResponse.getUserId(), myUserId)) {
            looUpUserResponse.setIsMyAccount(true);
        } else {
            looUpUserResponse.setIsMyAccount(false);
            looUpUserResponse.setIsFollowed(
                followRepository.existsByFollowedAndFollower(
                        findActiveUser(myUserId),
                        lookUpUser
                )
            );
        }
    }

    @Transactional(readOnly = true)
    public User findActiveUser(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
    }

    @Transactional(readOnly = true)
    public User findActiveUserByNickname(String nickname) {
        return userRepository.findByNicknameAndIsDeletedFalse(nickname)
                .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
    }

    @Transactional(readOnly = true)
    public boolean isValidEmail(String email){
        if(StringUtils.hasText(email)){
            return !userRepository.existsByEmailAndIsDeletedFalse(email);
        }
        return false;
    }

    @Transactional
    public Long update(Long id, UserUpdateRequest userUpdateRequest, MultipartFile file) throws IOException {
        User user = findActiveUser(id);
        userUpdateRequest.changeProfileImage(user.getProfileImage());

        if(!file.isEmpty()){
            userUpdateRequest.changeProfileImage(uploader.upload(file, getDayFormatDirectoryName()));
        }
        user.updateUserInfo(userUpdateRequest);

        return user.getId();
    }

    @Transactional
    public Long delete(Long id) {
        User user = findActiveUser(id);
        user.updateUserDeleted();

        return id;
    }

    @Transactional
    public Long updatePassword(Long id, UserPasswordChangeRequest userPasswordChangeRequest) {
        User user = findActiveUser(id);
        user.checkPassword(passwordEncoder, userPasswordChangeRequest.getNowPassword());
        if(!userPasswordChangeRequest.isDifferentPassword()){
            user.updateUserPasswordInfo(passwordEncoder, userPasswordChangeRequest);
        }
        return user.getId();
    }


    private boolean isDuplicateUser(UserSignupRequest form) {
        if (userRepository.existsByEmailAndIsDeletedFalse(form.getEmail())) {
            throw new DuplicateUserException(CONFLICT_VALUE_ERROR);
        }
        return false;
    }

    private void isValidUserRelation(User user, User followingUser) {
        if(user == followingUser) throw new NotValidUserRelationException(NOT_VALID_REQUEST_ERROR);
    }

    private String getDayFormatDirectoryName() {
        return DIRECTORY + dateTimeFormatter.format(LocalDateTime.now());
    }
}