package com.example.servlet3.basic.request;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(name="requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);

        resp.getWriter().write("ok");
    }

    private void printStartLine(HttpServletRequest request) {
        log.info("--- REQUEST-LINE - start ---");
        log.info("request.getMethod() = {}", request.getMethod()); //GET
        log.info("request.getProtocol = {}", request.getProtocol()); //HTTP/1.1
        log.info("request.getScheme() = {}", request.getScheme()); //http
        log.info("request.getRequestURL() = {}", request.getRequestURL()); // http://localhost:8080/request-header
        log.info("request.getRequestURI() = {}", request.getRequestURI()); // /request-test
        log.info("request.getQueryString() = {}", request.getQueryString()); //username=hi
        log.info("request.isSecure() = {}", request.isSecure()); //https 사용 유무
        log.info("--- REQUEST-LINE - end ---");
    }

    //Header 모든 정보
    private void printHeaders(HttpServletRequest request) {
        log.info("--- Headers - start ---");

//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()){
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + headerName);
//        }

        request.getHeaderNames().asIterator()
                        .forEachRemaining(headerName -> log.info(headerName + ": " + request.getHeader(headerName)));
        log.info("--- Headers - end ---");
    }

    //Header 편리한 조회
    private void printHeaderUtils(HttpServletRequest request) {
        log.info("--- Header 편의 조회 start ---");
        log.info("[Host 편의 조회]");
        log.info("request.getServerName() = " + request.getServerName()); //Host 헤더
        log.info("request.getServerPort() = " + request.getServerPort()); //Host 헤더
        log.info("[Accept-Language 편의 조회]");
        log.info("request.getLocale() = " + request.getLocale());
        log.info("[cookie 편의 조회]");
        log.info("[Content 편의 조회]");
        log.info("request.getContentType() = " + request.getContentType());
        log.info("request.getContentLength() = " + request.getContentLength());
        log.info("request.getCharacterEncoding() = " + request.getCharacterEncoding());
        log.info("--- Header 편의 조회 end ---");
    }

    //기타 정보
    private void printEtc(HttpServletRequest request) {
        log.info("--- 기타 조회 start ---");
        log.info("[Remote 정보]");
        log.info("request.getRemoteHost() = " + request.getRemoteHost());
        log.info("request.getRemoteAddr() = " + request.getRemoteAddr());
        log.info("request.getRemotePort() = " + request.getRemotePort());
        log.info("[Local 정보]");
        log.info("request.getLocalName() = " + request.getLocalName());
        log.info("request.getLocalAddr() = " + request.getLocalAddr());
        log.info("request.getLocalPort() = " + request.getLocalPort());
        log.info("--- 기타 조회 end ---");
    }
}
