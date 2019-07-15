<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/9/2019
  Time: 2:30 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
</head>

<body>

  <g:if test="${user}">
    <div class="backcard all-center grid-1">
      <g:if test="${msg}">
        <p class="alert-info">${msg}</p>
      </g:if>
      <h2 class="text-center">Your Account</h2>

      <form action="/account/updateemail" method="post">
        <table class="all-left">
          <tr>
            <td>
              <label>Username</label>
            </td>
            <td colspan="2">
              <h3>${user.username}</h3>
            </td>
          </tr>
          <tr>
            <td>
              <label>Email</label>
            </td>
            <td>
              <input type="email" name="email" value="${user.email}"/>
            </td>
            <td colspan="2">
              <input class="btn-white p" type="submit" value="Update Email">
            </td>
          </tr>
        </table>
      </form>

      <table>
        <tr>
          <td>
            <a href="/account/changepassword"><button class="btn-white all-center p">Change Password</button></a>
          </td>
        </tr>
      </table>

      <h2 class="text-center">Settings</h2>

      <form action="/account/savesettings" method="post">
        <table class="all-left">
          <tr>
            <td>
              <label>Language</label>
            </td>
            <td>
              <select name="lang">
                <option value="" disabled><g:message code="language" default="Language"/></option>
                <option value="en" <g:if test="${user.lang == 'en'}">selected</g:if> ><g:message code="language.en" default="English" /> </option>
                <option value="en" <g:if test="${user.lang == 'es'}">selected</g:if> ><g:message code="language.es" default="Spanish" /> </option>
                <option value="en" <g:if test="${user.lang == 'fr'}">selected</g:if> ><g:message code="language.fr" default="French" /> </option>
              </select>
            </td>
          <tr/>
          <tr>
            <td>
              <label>Units</label>
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
              <label>Favorite Location</label>
            </td>
            <td>
              <input type="text" name="location" placeholder="<g:message code='msg.search.placeholder'/>"
                     value="${user.favoriteLocation}"/>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <input class="all-center btn-white" type="submit" value="Save Settings">
            </td>
          </tr>
        </table>
      </form>

      <h2>Search History</h2>
      <table>

      </table>
    </div>
  </g:if>

</body>
</html>