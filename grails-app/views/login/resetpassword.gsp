<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
</head>

<body>
  <div class="backcard">
    <g:include view="subforms/msg.gsp"/>
    <g:if test="${validToken}">
      <g:form controller="login" action="updatepassword" method="post">
        <input type="hidden" name="username" value="${params.username}">
        <input type="hidden" name="forgotPasswordToken" value="${params.forgotPasswordToken}">
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
    </g:if>
  </div>
</body>
</html>