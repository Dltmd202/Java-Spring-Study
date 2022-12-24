package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/user-service")
@RestController
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;

    public UserController(Environment env, Greeting greeting, UserService userService){
        this.env = env;
        this.greeting = greeting;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String status(){
        return String.format("It's Working in USer Service in PORT %s",
                env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(){
        return env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userdto = mapper.map(user, UserDto.class);
        userService.createUser(userdto);

        ResponseUser responseUser = mapper.map(userdto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> res = new ArrayList<>();

        userList.forEach(u -> res.add(new ModelMapper().map(u, ResponseUser.class)));

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto user = userService.getUserByUserId(userId);

        ResponseUser res = new ModelMapper().map(user, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
