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
          <th>Num Searches</th>
          <th>Enabled</th>
          <th>Ban</th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${userDataSet}" var="userData">
          <tr>
            <td>${userData.id}</td>
            <td>${userData.username}</td>
            <td>
              <g:form controller="admin" action="contactuser" method="post" class="m" style="display: inline;">
                <input type="hidden" name="email" value="${userData.email}">
                <input type="submit" value="Contact" class="btn-white p">
              </g:form>
              ${userData.email}
            </td>
            <td>${userData.favoriteLocation}</td>
            <td>${userData.lang}</td>
            <td>${userData.units}</td>
            <td>${userData.searchLog.size()}</td>
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
              select: {
                  style: 'os',
                  selector: 'td:first-child'
              },
              ordering: true,
              dom: 'Blfrtip',
              buttons: [
                  'copyHtml5', 'excelHtml5', 'pdfHtml5', 'csvHtml5'
              ]
          });

          $("select[name='user-table_length']").addClass('block');
          // $(".dt-button").addClass('btn-white p m');
      });
  </script>

</body>
</html>