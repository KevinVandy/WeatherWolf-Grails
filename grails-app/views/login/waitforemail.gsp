<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/15/2019
  Time: 3:05 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
</head>

<body>
<div class="backcard">
  <p>${flash.success}</p>
</div>
</body>
</html>