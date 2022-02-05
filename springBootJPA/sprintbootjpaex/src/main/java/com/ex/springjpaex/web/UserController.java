package com.ex.springjpaex.web;

import com.ex.springjpaex.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public
}
