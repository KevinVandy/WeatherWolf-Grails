<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
</head>

<body>
  <g:if test="${user}">
    <div class="backcard all-center grid-1" style="max-width: 800px;">
      <g:include view="subforms/msg.gsp"/>
      <div class="grid-1 all-center">
        <h1 class="text-center"><g:message code="msg.youraccount" default="Your Account"/></h1>

        <form action="/account/updateemail" method="post" class="all-center">
          <table>
            <tr>
              <td>
                <label><g:message code="msg.username" default="Username"/></label>
              </td>
              <td colspan="2">
                <h3>${user.username}</h3>
              </td>
            </tr>
            <tr>
              <td>
                <label><g:message code="msg.email" default="email"/></label>
              </td>
              <td>
                <input type="email" name="email" value="${user.email}" style="width: 300px;"/>
              </td>
              <td colspan="2">
                <input class="btn-white p" type="submit" value="<g:message code="msg.updateemail" default="Update Email"/>">
              </td>
            </tr>
          </table>
        </form>
        <table>
          <tr>
            <td>
              <button id="togglepasswordform" class="btn-white all-center p"><g:message code="msg.changepassword"
                                                                                        default="Change Password"/></button>
            </td>
          </tr>
        </table>

        <div style="display: none;" id="changepasswordform" class="all-center">
          <g:form controller="account" action="changepassword" method="post">
            <table>
              <tr>
                <td>
                  <label><g:message code="msg.oldpassword" default="Old Password"/>:</label>
                </td>
                <td>
                  <input type="password" name="oldpassword" id="oldpassword" style="width: 300px;" minlength="6" maxlength="100" required>
                </td>
              </tr>
              <tr>
                <td>
                  <label><g:message code="msg.newpassword" default="New Password"/>:</label>
                </td>
                <td>
                  <input type="password" name="newpassword" id="newpassword" style="width: 300px;" minlength="6" maxlength="100" required>
                </td>
              </tr>
              <tr>
                <td>
                  <label><g:message code="msg.confirmpassword" default="Confirm Password"/>:</label>
                </td>
                <td>
                  <input type="password" name="newpasswordconfirm" id="newpasswordconfirm" style="width: 300px;" minlength="6" maxlength="100"
                         required>
                </td>
              </tr>
              <tr>
                <td>
                  <button id="cancelpasswordform" class="btn-white p-1 all-center">Cancel</button>
                </td>
                <td>
                  <input type="submit" value="<g:message code="msg.changepassword" default="Change Password"/>"
                         class="btn-white p-1 all-center">
                </td>
              </tr>
            </table>
          </g:form>
        </div>

        <table>
          <tr>
            <td>
              <button id="toggledeleteform" class="btn-white all-center p"><g:message code="msg.deleteaccount"
                                                                                      default="Delete Account"/></button>
            </td>
          </tr>
        </table>

        <div style="display: none;" id="deleteaccountform" class="all-center">
          <g:form controller="account" action="deleteaccount" method="post"
                  onsubmit="return confirm('Are you sure you want to delete your account?');">
            <table class="all-center">
              <tr>
                <td>
                  <button id="canceldeleteform" class="btn-white p-1 all-center">Cancel</button>
                </td>
                <td>
                  <input type="submit" value="Are you sure you want to delete your account?" class="btn-danger all-center p-1">
                </td>
              </tr>

            </table>
          </g:form>
        </div>

        <h2 class="text-center"><g:message code="msg.settings" default="Settings"/></h2>

        <form action="/account/savesettings" method="post" id="settingsform" class="all-center">
          <table>
            <tr>
              <td>
                <label><g:message code="language" default="Language"/></label>
              </td>
              <td>
                <select name="lang" style="width: 300px;" onchange="document.getElementById('settingsform').submit()">
                  <option value="" disabled><g:message code="language" default="Language"/></option>
                  <option value="en" <g:if test="${user.lang == 'en'}">selected</g:if>><g:message code="language.en"
                                                                                                  default="English"/></option>
                  <option value="es" <g:if test="${user.lang == 'es'}">selected</g:if>><g:message code="language.es"
                                                                                                  default="Spanish"/></option>
                  <option value="fr" <g:if test="${user.lang == 'fr'}">selected</g:if>><g:message code="language.fr" default="French"/></option>
                </select>
              </td>
            <tr/>
            <tr>
              <td>
                <label><g:message code="msg.units" default="Units"/></label>
              </td>
              <td>
                <select name="units" style="width: 300px;">
                  <option value="" disabled><g:message code="msg.units" default="Units"/></option>
                  <option value="C" <g:if test="${user.units == 'C'}">selected</g:if>>C&deg;</option>
                  <option value="F" <g:if test="${user.units == 'F'}">selected</g:if>>F&deg;</option>
                </select>
              </td>
            </tr>
            <tr>
              <td>
                <label><g:message code="msg.favoritelocation" default="Favorite Location"/></label>
              </td>
              <td id="remote">
                <input type="text" name="location" placeholder="<g:message code='msg.search.placeholder'/>"
                       value="${user.favoriteLocation}" class="typeahead" style="width: 300px;"/>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <input class="all-center btn-white p" type="submit" value="<g:message code="msg.savesettings" default="Save Settings"/>">
              </td>
            </tr>
          </table>
        </form>

        <h2><g:message code="msg.searchhistory" default="Search History"/></h2>

        <table id="searchhistorytable">
          <thead>
            <tr>
              <th>Date</th>
              <th>Search</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${user.searchLog}" var="sl">
              <tr>
                <td><g:formatDate date="${sl.date}" type="datetime" style="MEDIUM"/></td>
                <td>
                  <g:form controller="weather" action="index" method="GET">
                    <input type="hidden" name="location" value="${sl.searchString}">
                    <input type="submit" value="${sl.searchString}" class="btn-white p" style="width: 100%;">
                  </g:form>
                </td>
              </tr>
            </g:each>
          </tbody>
        </table>
      <table>
        <g:if test="${user.searchLog}">
          <tr>
            <td colspan="2">
              <g:form controller="account" action="deletesearchhistory" method="POST" onsubmit="return confirm('Are you sure you want to delete your search history?');">
                <input type="submit" value="Delete Search History" class="btn-white p all-center">
              </g:form>
            </td>
          </tr>
        </g:if>
      </table>
      </div>
    </div>
  </g:if>
  <g:else>
    Could not find user object
  </g:else>

  <script>

      $(document).ready(function () {
          $('#searchhistorytable').DataTable({
              dom: 'tip',
              ordering: true
          });
      });

      $("#togglepasswordform").click(function (e) {
          e.preventDefault();
          $("#changepasswordform").slideDown(500);
          $("#togglepasswordform").slideUp(500);
      });
      $("#cancelpasswordform").click(function (e) {
          e.preventDefault();
          $("#changepasswordform").slideUp(500);
          $("#togglepasswordform").slideDown(500);
      });

      $("#toggledeleteform").click(function (e) {
          e.preventDefault();
          $("#deleteaccountform").slideDown(500);
          $("#toggledeleteform").slideUp(500);
      });
      $("#canceldeleteform").click(function (e) {
          e.preventDefault();
          $("#deleteaccountform").slideUp(500);
          $("#toggledeleteform").slideDown(500);
      });

  </script>

</body>
</html>