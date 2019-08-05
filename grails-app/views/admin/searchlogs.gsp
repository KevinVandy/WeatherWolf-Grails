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
    <table id="searchlog-table" class="display">
      <thead>
        <tr>
          <th>ID</th>
          <th>${message(code: 'msg.datetime', default: 'Date Time')}</th>
          <th>${message(code: 'msg.search', default: 'Search')}</th>
          <th>${message(code: 'msg.username', default: 'Username')}</th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${searchLogDataSet}" var="searchLog">
          <tr>
            <td>${searchLog.id}</td>
            <td>${searchLog.date}</td>
            <td>${searchLog.searchString}</td>
            <td>${searchLog.user.username}</td>
          </tr>
        </g:each>
      </tbody>
    </table>

  </div>
  <script>
      $(document).ready(function () {
          $('#searchlog-table').DataTable({
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
          $("select[name='searchlog-table_length']").addClass('inline');
          $(".dt-button").addClass('btn-white p m')
      });
  </script>
</body>
</html>