<%@ page import="com.example.servlet3.domain.member.Member" %>
<%@ page import="com.example.servlet3.domain.member.MemberRepository" %><%--
  Created by IntelliJ IDEA.
  User: mac
  Date: 2022/02/16
  Time: 1:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MemberRepository memberRepository = MemberRepository.getInstance();
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>username=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
