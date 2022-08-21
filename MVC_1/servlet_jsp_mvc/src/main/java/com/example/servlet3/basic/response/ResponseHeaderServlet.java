package com.example.servlet3.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        [status-line]
        response.setStatus(HttpServletResponse.SC_OK);

//        [response-header]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        content(response);
        cookie(response);
        redirct(response);

        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }

    private void content(HttpServletResponse response){
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
    }

    private void cookie(HttpServletResponse response){
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);
        response.addCookie(cookie);
    }

    private void redirct(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "/basic/hello-form.html");
    }
}
