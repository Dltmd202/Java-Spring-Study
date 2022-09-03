package com.example.mongochat.user.api;


import com.example.mongochat.configures.JwtTokenConfigure;
import com.example.mongochat.global.error.exception.UnauthorizedException;
import com.example.mongochat.global.utils.ApiUtils;
import com.example.mongochat.user.domain.User;
import com.example.mongochat.user.dto.*;
import com.example.mongochat.user.security.Jwt;
import com.example.mongochat.user.security.JwtAuthentication;
import com.example.mongochat.user.security.JwtAuthenticationToken;
import com.example.mongochat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

    //TODO status 코드
    private final Jwt jwt;
    private final UserService userService;
    private final JwtTokenConfigure jwtTokenConfigure;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<ApiUtils.ApiResult<Long>> signup(@Validated @RequestBody UserSignupRequest form){
        Long userId = userService.signup(form);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiUtils.success(userId));
    }

    @GetMapping
    public ResponseEntity<ApiUtils.ApiResult<UserResponse>> get(
            @RequestParam(value = "nickname", defaultValue = "") String nickname,
            @AuthenticationPrincipal JwtAuthentication token
    ){
        return ResponseEntity.ok(ApiUtils.success(
                userService.findUserByNickname(
                        nickname,
                        token.getId()
                )));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiUtils.ApiResult<UserToken>> login(
            @Valid @RequestBody UserLoginRequest request
    ){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials())
            );

            User user = (User) authentication.getDetails();
            String token = user.newJwt(
                    jwt,
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toArray(String[]::new)
            );
            HttpHeaders httpHeaders = putJwtToAuthenticationHeader(token);
            return ResponseEntity
                    .ok()
                    .headers(httpHeaders)
                    .body(
                            ApiUtils.success(
                            new UserToken(user.getId(),
                                    token,
                                    user.getRoles().toString())));
        } catch (AuthenticationException e){
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiUtils.ApiResult<Long>> update(
            @AuthenticationPrincipal JwtAuthentication token,
            @Valid @RequestPart UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "file", required = false)MultipartFile file
            ) throws IOException {
        return ResponseEntity.ok(ApiUtils.success(userService.update(
                token.getId(),
                userUpdateRequest,
                file
        )));
    }

    @GetMapping("/email")
    public ResponseEntity<ApiUtils.ApiResult<Boolean>> checkValidEmail(
            @Email @RequestParam(value = "value", defaultValue = "") String email
    ){
        return ResponseEntity.ok(ApiUtils.success(userService.isValidEmail(email)));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiUtils.ApiResult<Long>> updatePassword(
            @AuthenticationPrincipal JwtAuthentication token,
            @Valid @RequestBody UserPasswordChangeRequest userPasswordChangeRequest
    ){
        return ResponseEntity.ok(ApiUtils.success(userService.updatePassword(
                token.getId(),
                userPasswordChangeRequest
        )));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiUtils.ApiResult<Long>> delete(
            @AuthenticationPrincipal JwtAuthentication token
    ){
        return ResponseEntity.ok(ApiUtils.success(userService.delete(token.getId())));
    }

    @PostMapping("/{nickname}/follow")
    public ResponseEntity<Void> followUser(
            @AuthenticationPrincipal JwtAuthentication token,
            @PathVariable("nickname") String nickname
    ){
        userService.followUser(token.getId(), nickname);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{nickname}/unfollow")
    public ResponseEntity<Void> unFollowUser(
            @AuthenticationPrincipal JwtAuthentication token,
            @PathVariable("nickname") String nickname
    ){
        userService.unFollowUser(token.getId(), nickname);
        return ResponseEntity.noContent().build();
    }

    private HttpHeaders putJwtToAuthenticationHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtTokenConfigure.getHeader(), "Bearer " + token);
        return httpHeaders;
    }
    // TODO Like한 user 조회
}