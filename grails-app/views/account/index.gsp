<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
</head>

<body>
  <g:if test="${user}">
    <div class="backcard all-center grid-1">
      <g:include view="subforms/msg.gsp"/>
      <div class="grid-1 all-center">
        <h1 class="text-center"><g:message code="msg.youraccount" default="Your Account"/></h1>

        <form action="/account/updateemail" method="post" class="all-center">
          <table class="all-left">
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
                <input type="email" name="email" value="${user.email}"/>
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
            <table class="all-center">
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

        <form action="/account/savesettings" method="post" id="settingsform">
          <table class="all-center">
            <tr>
              <td>
                <label><g:message code="language" default="Language"/></label>
              </td>
              <td>
                <select name="lang" onchange="document.getElementById('settingsform').submit()">
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
                <select name="units">
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
              <td>
                <input type="text" name="location" placeholder="<g:message code='msg.search.placeholder'/>"
                       value="${user.favoriteLocation}"/>
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
        <table>
          <tr>
            <th>Date</th>
            <th>Search</th>
          </tr>
          <g:each in="${user.searchLog}" var="sl">
            <tr>
              <td>${sl.date}</td>
              <td>${sl.searchString}</td>
            </tr>
          </g:each>
        </table>
      </div>

    </div>
  </g:if>
  <g:else>
    Could not find user object
  </g:else>

  <script>
      $("#togglepasswordform").click(function (e) {
          e.preventDefault()
          $("#changepasswordform").slideDown(500);
          $("#togglepasswordform").slideUp(500);
      });
      $("#cancelpasswordform").click(function (e) {
          e.preventDefault()
          $("#changepasswordform").slideUp(500);
          $("#togglepasswordform").slideDown(500);
      });

      $("#toggledeleteform").click(function (e) {
          e.preventDefault()
          $("#deleteaccountform").slideDown(500);
          $("#toggledeleteform").slideUp(500);
      });
      $("#canceldeleteform").click(function (e) {
          e.preventDefault()
          $("#deleteaccountform").slideUp(500);
          $("#toggledeleteform").slideDown(500);
      });

  </script>

</body>
</html>