<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/25/2019
  Time: 4:25 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Weather Wolf - Admin</title>
</head>

<body>
  <g:include view="subforms/navadmin.gsp"/>
  <div class="grid-3 m-2">
    <g:link controller="admin" action="users">
      <div class="card all-center m">
        <h1>Manage Users</h1>
      </div>
    </g:link>
    <g:link controller="admin" action="searchlogs">
      <div class="card all-center m">
        <h1>Search Logs</h1>
      </div>
    </g:link>
    <g:link controller="admin" action="emaillogs">
      <div class="card all-center m">
        <h1>Email Logs</h1>
      </div>
    </g:link>
    <g:link controller="admin" action="locations">
      <div class="card all-center m">
        <h1>Manage Location Suggestions</h1>
      </div>
    </g:link>
  </div>

</body>
</html>