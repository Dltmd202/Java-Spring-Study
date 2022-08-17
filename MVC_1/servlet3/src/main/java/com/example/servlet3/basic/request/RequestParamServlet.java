package com.example.servlet3.basic.request;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName +
                        "=" + request.getParameter(paramName)));
        log.info("[전체 파라미터 조회] - end");
        log.info("[단일 파라미터 조회]");
        String username = request.getParameter("username"); System.out.println("request.getParameter(username) = " + username);
        String age = request.getParameter("age");
        log.info("request.getParameter(age) = " + age);
        log.info("[이름이 같은 복수 파라미터 조회]");
        log.info("request.getParameterValues(username)");

        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            log.info("username=" + name);
       }
        resp.getWriter().write("ok");
    }
}
