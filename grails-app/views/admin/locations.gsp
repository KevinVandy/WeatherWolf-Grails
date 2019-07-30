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
    <g:form controller="admin" action="locations">
      <table>
        <tr>
          <td>
            <label class="all-right">Search All Pre-existing</label>
          </td>
          <td>
            <input type="text" name="q" style=" width: 400px; font-size: 1.5em;" placeholder="<g:message code='msg.search.placeholder'/>"
                   class="typeahead" />
            <input type="submit" value="Search All" class="btn-white p m">
        </tr>
        <tr>
          <td colspan="2">
            <p class="all-center">Or filter by first letter</p>
          </td>
        </tr>
        <tr>
          <td colspan="2" class="text-center">
            <g:each in="${pages}" var="page">
              <a href="/admin/locations?q=${page}" class="step">${page}</a>
            </g:each>
          </td>
        </tr>
      </table>
    </g:form>
    <h2>Add New Location <span id="toggle-add-location" class="p toggle-5day"><g:if test="${flash}">-</g:if> <g:else>+</g:else></span></h2>
    <g:form controller="admin" action="addlocation" method="post">
      <input type="hidden" name="q" value="${params.q}">

      <fieldset id="add-location-form" class="m-2" <g:if test="${flash}">style="display: block"</g:if> <g:else>style="display: none"</g:else>>
        <legend class="p">Add New Location</legend>
        <table>
          <tr>
            <td>
              <label for="city" class="all-right">City</label>
            </td>
            <td>
              <input type="text" name="city" id="city" class="text-left typeaheadCity">
            </td>
          </tr>
          <tr>
            <td>
              <label for="stateProvince" class="all-right">State / Province</label>
            </td>
            <td>
              <input type="text" name="stateProvince" id="stateProvince" class="text-left typeaheadStateProvince">
            </td>
          </tr>
          <tr>
            <td>
              <label for="country" class="all-right">Country</label>
            </td>
            <td>
              <input type="text" name="country" id="country" class="text-left typeaheadCountry">
            </td>
          </tr>
          <tr>
            <td></td>
            <td>
              <input type="submit" value="Add New Location" class="btn-white p">
            </td>
          </tr>
        </table>
      </fieldset>
    </g:form>

    <table id="location-table" class="display" style="display: none">
      <thead>
        <tr>
          <th>City</th>
          <th>State / Province</th>
          <th>Country</th>
          <th>Delete</th>
        </tr>
      </thead>
      <g:if test="${locationDataSet}">
        <g:each in="${locationDataSet}" var="location">
          <tr>
            <td>${location.city}</td>
            <td>${location.stateProvince}</td>
            <td>${location.country}</td>
            <td>
              <g:form controller="admin" action="deletelocation" method="post"
                      onsubmit="return confirm('Are you sure you want to delete this location?');">
                <input type="hidden" name="q" value="${params.q}">
                <input type="hidden" name="locationId" value="${location.id}">
                <input type="submit" value="Delete" class="btn-danger p m-0">
              </g:form>
            </td>
          </tr>
        </g:each>
        <g:if test="${locationDataSet.size() > 10}">
          <tfoot>
            <tr>
              <th>City</th>
              <th>State / Province / Region</th>
              <th>Country</th>
              <th>Delete</th>
            </tr>
          </tfoot>
        </g:if>
      </g:if>
    </table>
    <br/>

    <div class="text-center">
      <g:each in="${pages}" var="page">
        <a href="/admin/locations?q=${page}" class="step">${page}</a>
      </g:each>
    </div>

  </div>
  <script>
      $(document).ready(function () {
          $('#location-table').DataTable({
              select: 'single',
              ordering: true,
              scroll: false,
              search: {
                  caseInsensitive: true,
                  smart: true
              },
              dom: 'Blfrtip',
              buttons: [
                  'copyHtml5', 'excelHtml5', 'pdfHtml5', 'csvHtml5'
              ]
          });
          $("select[name='location-table_length']").addClass('inline');
          $(".dt-button").addClass('btn-white p m')
          $('input[type=search]').addClass('typeahead p').attr('placeholder', 'Filter Results');
          $('#location-table').fadeIn(1000);
          $('input[type=search]').focus()
      });

      $("#toggle-add-location").click(function () {
          $("#add-location-form").slideToggle(500);
          if ($("#toggle-add-location").html() === "-") {
              $("#toggle-add-location").html("+");
          } else {
              $("#toggle-add-location").html("-");
          }
      });
  </script>

</body>
</html>