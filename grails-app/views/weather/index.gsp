<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/9/2019
  Time: 1:32 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
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
  <g:include view="subforms/searchbar.gsp"/>
  <g:include view="subforms/searchresults.gsp"/>
</body>
</html>