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
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
</head>

<body>
  <g:include view="subforms/searchbar.gsp"/>
  <g:include view="subforms/searchresults.gsp"/>
</body>
</html>