<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/12/2019
  Time: 3:44 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
</head>

<body>
  <div class="backcard">
    <g:form controller="account" action="updatepassword" method="post">
      <table>
        <tr>
          <td>
            <label><g:message code="msg.newpassword" default="New Password"/>:</label>
          </td>
          <td>
            <input type="password" name="newpassword" id="newpassword" minlength="6" maxlength="100" required>
          </td>
        </tr>
        <tr>
          <td>
            <label><g:message code="msg.confirmpassword" default="Confirm Password"/>:</label>
          </td>
          <td>
            <input type="password" name="newpasswordconfirm" id="newpasswordconfirm" minlength="6" maxlength="100" required>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" value="<g:message code="msg.changepassword" default="Change Password"/>"
                   class="btn-white p-1 all-center">
          </td>
        </tr>
      </table>
    </g:form>
  </div>
</body>
</html>