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
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
</head>

<body>
  <g:include view="subforms/navadmin.gsp"/>
  <div class="backcard">
    <g:include view="subforms/msg.gsp"/>
    <table id="email-table" class="display">
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
          $('#email-table').DataTable({
              select: true,
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
          $("select[name='email-table_length']").addClass('inline');
          $(".dt-button").addClass('btn-white p m')
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