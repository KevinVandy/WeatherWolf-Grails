<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/26/2019
  Time: 10:09 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
</head>

<body>
  <g:include view="subforms/navadmin.gsp"/>
  <div class="backcard">

    <table id="user-table" class="display">
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>Favorite Location</th>
          <th>Language</th>
          <th>Units</th>
          <th>Contact</th>
          <th>Delete</th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${userDataSet}" var="userData">
          <tr>
            <td>${userData.id}</td>
            <td>${userData.username}</td>
            <td>${userData.email}</td>
            <td>${userData.favoriteLocation}</td>
            <td>${userData.lang}</td>
            <td>${userData.units}</td>
            <td>
              <g:form controller="admin" action="contactuser" method="post">
                <input type="hidden" name="userId" value="${userData.id}">
                <input type="submit" value="Contact" class="btn-white p">
              </g:form>
            </td>
            <td>
              <g:form controller="admin" action="deleteuser" method="post"
                      onsubmit="return confirm('Are you sure you want to delete this account?');">
                <input type="hidden" name="userId" value="${userData.id}">
                <input type="submit" value="Delete" class="btn-danger p">
              </g:form>
            </td>
          </tr>
        </g:each>
      </tbody>
    </table>

  </div>
  <script>
      $(document).ready(function () {
          $('#user-table').DataTable({
              select: true,
              ordering: true
          });
      });
  </script>

</body>
</html>