<%@ page import="java.text.NumberFormat" contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
</head>

<body>
  <g:include view="subforms/navadmin.gsp"/>
  <div class="backcard">
    <g:include view="subforms/msg.gsp"/>
    <p>${message(code: 'msg.totallocations', default: 'Total Locations')}: <strong>${NumberFormat.getNumberInstance(new Locale(user.lang)).format(locationCount)}</strong></p>
    <g:form controller="admin" action="locations" method="GET">
      <table>
        <tr>
          <td>
            <label class="all-right">${message(code: 'msg.testsearchbar', default: 'Test Search Bar')}</label>
          </td>
          <td>
            <input type="text" name="q" style=" width: 400px; font-size: 1.5em;" placeholder="${message(code: 'msg.search.placeholder', default: 'City, [State / Province], [Country]')}"
                   class="typeahead" value="${(params.q && params.q.length() > 1) ? params.q : ''}"/>
            <input type="submit" value="${message(code: 'msg.searchall', default: 'Search All')}" class="btn-white p m">
        </tr>
        <tr>
          <td colspan="2">
            <p class="all-center">${message(code: 'msg.orfilterbyfirstletter', default: 'Or filter by first letter')}</p>
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
    <h2>${message(code: 'msg.addnewlocation', default: 'Add New Location')} <span id="toggle-add-location" class="p toggle-5day"><g:if test="${flash}">-</g:if> <g:else>+</g:else></span></h2>
    <g:form controller="admin" action="addlocation" method="post">
      <input type="hidden" name="q" value="${params.q}">

      <fieldset id="add-location-form" class="m-2" <g:if test="${flash}">style="display: block"</g:if> <g:else>style="display: none"</g:else>>
        <legend class="p">${message(code: 'msg.addnewlocation', default: 'Add New Location')}</legend>
        <table>
          <tr>
            <td>
              <label for="city" class="all-right">${message(code: 'msg.city', default: 'City')}</label>
            </td>
            <td colspan="2">
              <input type="text" name="city" id="city" class="text-left typeaheadCity">
            </td>
            <td rowspan="5">
              <button id="intelligentfill" class="btn-primary p m all-center">${message(code: 'msg.intelligentautofill', default: 'Intelligent Auto Fill')}</button>
              <button id="clearform" class="btn-white p m all-center">${message(code: 'msg.clearform', default: 'Clear Form')}</button>
            </td>
          </tr>
          <tr>
            <td>
              <label for="stateProvince" class="all-right">${message(code: 'msg.stateProvince', default: 'State / Province')}</label>
            </td>
            <td colspan="2">
              <input type="text" name="stateProvince" id="stateProvince" class="text-left typeaheadStateProvince">
            </td>
          </tr>
          <tr>
            <td>
              <label for="country" class="all-right">${message(code: 'msg.country', default: 'Country')}</label>
            </td>
            <td colspan="2">
              <input type="text" name="country" id="country" class="text-left typeaheadCountry">
            </td>
          </tr>
          <tr>
            <td>
              <label class="all-right">${message(code: 'msg.latitude', default: 'Latitude')}</label>
            </td>
            <td><g:field type="number" step="any" name="lat" id="lat" style="max-width: 150px;"/></td>

          </tr>
          <tr>
            <td>
              <label class="all-right">${message(code: 'msg.longitude', default: 'Longitude')}</label>
            </td>
            <td><g:field type="number" step="any" name="lng" id="lng" style="max-width: 150px;"/></td>
          </tr>
          <tr>
            <td colspan="4">
              <input type="submit" value="${message(code: 'msg.addnewlocation', default: 'Add New Location')}" class="btn-white p all-center">
            </td>
          </tr>
        </table>
      </fieldset>
    </g:form>

    <table id="location-table" class="display" style="display: none">
      <thead>
        <tr>
          <th>ID</th>
          <th>${message(code: 'msg.city', default: 'City')}</th>
          <th>${message(code: 'msg.stateProvince', default: 'State / Province')}</th>
          <th>${message(code: 'msg.country', default: 'Country')}</th>
          <th>${message(code: 'msg.coordinates', default: 'Coordinates')}</th>
          <th>${message(code: 'msg.delete', default: 'Delete')}</th>
        </tr>
      </thead>
      <g:if test="${locationDataSet}">
        <g:each in="${locationDataSet}" var="location">
          <tr>
            <td>${location.id}</td>
            <td>${location.city}</td>
            <td>${location.stateProvince}</td>
            <td>${location.country}</td>
            <td>${location.latitude.round(2)}, ${location.longitude.round(2)}</td>
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
              search: {
                  caseInsensitive: true,
                  smart: true
              },
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

      $("#intelligentfill").click(async function (e) {
          e.preventDefault();
          const url =
              "/location/fill?city=" + $('#city').val() +
              "&stateProvince=" + $('#stateProvince').val() +
              "&country=" + $('#country').val() +
              "&lat=" + $('#lat').val() +
              "&lng=" + $('#lng').val();
          $.get(url, function (res) {
              $("#city").val(res.city);
              $("#stateProvince").val(res.stateProvince);
              $("#country").val(res.country);
              $("#lat").val(res.latitude);
              $("#lng").val(res.longitude);
          });
      });

      $('#clearform').click(function (e) {
          e.preventDefault();
          $("#city").val('');
          $("#stateProvince").val('');
          $("#country").val('');
          $("#lat").val('');
          $("#lng").val('');
      });
  </script>

</body>
</html>