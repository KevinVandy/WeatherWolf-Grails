<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/26/2019
  Time: 10:49 AM
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
          <th>Time Sent</th>
          <th>Recipient</th>
          <th>Subject</th>
          <th>Body<button id="toggle-content" class="btn-white m p text-center">Show Content</button></th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${emailLogDataSet}" var="emailLog">
          <tr>
            <td>${emailLog.id}</td>
            <td>${emailLog.timeSent}</td>
            <td>${emailLog.toAddress}</td>
            <td>${emailLog.subject}</td>
            <td>
              <div class="emailbody" style="display: none;">${emailLog.body}</div>
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

      $("#toggle-content").click(function () {
          $(".emailbody").fadeToggle(500);
          if ($("#toggle-content").html() === "Hide Content") {
              $("#toggle-content").html("Show Content");
          } else {
              $("#toggle-content").html("Hide Content");
          }
      });
  </script>
</body>
</html>