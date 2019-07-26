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

  <g:each in="${pages}" var="page">
    <a href="/admin/locations?firstLetter=${page}" class="step">${page}</a>
  </g:each>
    <table id="location-table" class="display">
      <thead>
        <tr>
          <th>City</th>
          <th>State / Province</th>
          <th>Country</th>
        </tr>
      </thead>
      <g:each in="${locationDataSet}" var="location">
        <tr>
          <td>${location.city}</td>
          <td>${location.stateProvince}</td>
          <td>${location.country}</td>
        </tr>
      </g:each>
      <tfoot>
        <tr>
          <th>City</th>
          <th>State / Province</th>
          <th>Country</th>
        </tr>
      </tfoot>
    </table>
    <g:each in="${pages}" var="page">
      <a href="/admin/locations?firstLetter=${page}" class="step">${page}</a>
    </g:each>

  </div>
  <script>
      $(document).ready(function () {
          $('#location-table').DataTable({
              select: true,
              ordering: true,
              scroll: false
          });
      });
  </script>
  %{-- <script>
       $(document).ready(function () {
           numRecs = $("select[name='location-table_length']").children("option:selected").val();
           let url = '/location/index?max=' + numRecs;
           console.log(url)
           $('#location-table').dataTable({
               select: true,
               ordering: true,
               processing: true,
               serverSide: true,
               ajax: {
                   url: url,
                   dataSrc:''
               },
               columns: [
                   {data: 'city'},
                   {data: 'stateProvince'},
                   {data: 'country'}
               ]
           });
       });
   </script>--}%

</div>
</body>
</html>