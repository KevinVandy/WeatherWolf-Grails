<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/26/2019
  Time: 10:09 AM
--%>

<%@ page import="com.weatherwolf.security.Role" contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
</head>

<body>
  <g:include view="subforms/navadmin.gsp"/>
  <div class="backcard">
    <g:include view="subforms/msg.gsp"/>
    <table id="user-table" class="display">
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>Favorite Location</th>
          <th>Language</th>
          <th>Units</th>
          <th># Searches</th>
          <th>Roles</th>
          <th>Enabled</th>
          <th>Ban</th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${userDataSet}" var="userData">
          <tr>
            <td>${userData.id}</td>
            <td>${userData.username}</td>
            <td><a href="mailto:${userData.email}?subject=Weather%20Wolf">${userData.email}</a></td>
            <td>${userData.favoriteLocation}</td>
            <td>${userData.lang}</td>
            <td>${userData.units}</td>
            <td>${userData.searchLog.size()}</td>
            <td>
              <form action="/admin/changeadminstatus" method="post" id="${userData.username}adminstatusform">
                <input type="hidden" name="userId" value="${userData.id}">
                <input type="checkbox" name="adminstatus" value="1"
                       onchange="document.getElementById('${userData.username}adminstatusform').submit()"
                       <g:if test="${userData.getAuthorities().contains(new Role("ROLE_ADMIN"))}">checked</g:if>>
                <label>Admin</label>
              </form>

              <form action="/admin/changeuserstatus" method="post" id="${userData.username}userstatusform">
                <input type="hidden" name="userId" value="${userData.id}">
                <input type="checkbox" name="userstatus" value="1"
                       onchange="document.getElementById('${userData.username}userstatusform').submit()"
                       <g:if test="${userData.getAuthorities().contains(new Role("ROLE_CLIENT"))}">checked disabled</g:if>>
                <label>User</label>
              </form>
            </td>
            <td>
              <g:if test="${userData.enabled}">
                <g:form controller="admin" action="disableuser" method="post">
                  <input type="hidden" name="userId" value="${userData.id}">
                  <input type="submit" value="Disable" class="btn-danger p">
                </g:form>
              </g:if>
              <g:else>
                <g:form controller="admin" action="enableuser" method="post">
                  <input type="hidden" name="userId" value="${userData.id}">
                  <input type="submit" value="Enable" class="btn-primary p">
                </g:form>
              </g:else>
            </td>
            <td>
              <g:form controller="admin" action="deleteuser" method="post"
                      onsubmit="return confirm('Are you sure you want to delete this account?');">
                <input type="hidden" name="userId" value="${userData.id}">
                <input type="submit" value="Ban" class="btn-danger p">
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
              ordering: true,
              dom: 'Blfrtip',
              buttons: [
                  'copyHtml5', 'excelHtml5', 'pdfHtml5', 'csvHtml5'
              ],
              language: {
                  <g:if test="${user.lang == 'en'}">
                  "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json"
                  </g:if>
                  <g:if test="${user.lang == 'es'}">
                  "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Spanish.json"
                  </g:if>
                  <g:if test="${user.lang == 'fr'}">
                  "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/French.json"
                  </g:if>
              }
          });

          $("select[name='user-table_length']").addClass('block');
          // $(".dt-button").addClass('btn-white p m');
      });
  </script>

</body>
</html>