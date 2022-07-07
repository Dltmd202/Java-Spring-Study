package com.sidepr.mono.sns.user.service;



import com.sidepr.mono.sns.user.exception.DuplicateUserException;
import com.sidepr.mono.sns.user.exception.NotFoundUserException;
import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.global.error.exception.NotFoundException;
import com.sidepr.mono.sns.global.fileuploader.FileUploader;
import com.sidepr.mono.sns.user.domain.User;
import com.sidepr.mono.sns.user.dto.UserPasswordChangeRequest;
import com.sidepr.mono.sns.user.dto.UserResponse;
import com.sidepr.mono.sns.user.dto.UserSignupRequest;
import com.sidepr.mono.sns.user.dto.UserUpdateRequest;
import com.sidepr.mono.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FileUploader uploader;
    private static final String DIRECTORY = "file/";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/");


    @Transactional(readOnly = true)
    public User login(String email, String password) {

        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
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

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {

        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public UserResponse findUser(Long id) {
        return UserResponse.of(findActiveUser(id));
    }

    @Transactional(readOnly = true)
    public User findActiveUser(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
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

    private String getDayFormatDirectoryName() {
        return DIRECTORY + dateTimeFormatter.format(LocalDateTime.now());
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
            throw new DuplicateUserException(ErrorCode.CONFLICT_VALUE_ERROR);
        }
        return false;
    }
}
