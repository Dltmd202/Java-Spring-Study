package com.example.mongochat.global.error;


import com.sidepr.mono.sns.comment.exception.InvalidCommentRequestException;
import com.sidepr.mono.sns.comment.exception.NotFoundCommentException;
import com.sidepr.mono.sns.comment.exception.NotPermittedCommentException;
import com.sidepr.mono.sns.global.error.exception.NotFoundException;
import com.sidepr.mono.sns.global.error.exception.NotPermittedResourceException;
import com.sidepr.mono.sns.global.error.exception.UnauthorizedException;
import com.sidepr.mono.sns.global.utils.ApiUtils;
import com.sidepr.mono.sns.post.exception.InvalidPostRequestException;
import com.sidepr.mono.sns.post.exception.NotFoundPostException;
import com.sidepr.mono.sns.post.exception.NotPermittedPostException;
import com.sidepr.mono.sns.user.exception.DuplicateUserException;
import com.sidepr.mono.sns.user.exception.NotFoundUserException;
import com.sidepr.mono.sns.user.exception.NotValidPasswordException;
import com.sidepr.mono.sns.user.exception.NotValidUserRelationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.sidepr.mono.sns.global.utils.ApiUtils.error;


@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {


    private ResponseEntity<ApiUtils.ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        return newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<ApiUtils.ApiResult<?>> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error(message, status), headers, status);
    }


    @ExceptionHandler({
            NoHandlerFoundException.class,
            NotFoundException.class,
            NotFoundPostException.class,
            NotFoundCommentException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        return newResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            UnauthorizedException.class,
            NotPermittedResourceException.class,
            NotPermittedPostException.class,
            NotPermittedCommentException.class
    })
    public ResponseEntity<?> handleUnauthorizedException(Exception e) {
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            NotValidPasswordException.class,
            NotValidUserRelationException.class,
            DuplicateUserException.class,
            InvalidPostRequestException.class,
            InvalidCommentRequestException.class,
            NotFoundUserException.class,
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        if (e instanceof MethodArgumentNotValidException) {
            return newResponse(
                    ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
        return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowedException(Exception e) {
        return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}