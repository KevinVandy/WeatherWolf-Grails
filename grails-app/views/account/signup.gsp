<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/10/2019
  Time: 1:51 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Welcome to Grails</title>
  <style type="text/css" media="screen">
  body {
    background-image: url("${resource(dir: "images", file: "background.jpg")}");
    background-repeat: no-repeat;
    background-size: cover;
    background-attachment: fixed;
  }
  </style>
</head>

<body>
  <g:include view="subforms/signupform.gsp" />
</body>
</html>