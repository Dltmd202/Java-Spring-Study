<%@ page import="com.example.servlet3.domain.member.Member" %>
<%@ page import="com.example.servlet3.domain.member.MemberRepository" %><%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 2022/02/16
  Time: 1:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=${member.id}</li>
    <li>username=${member.username}</li>
    <li>username=${member.age}</li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
