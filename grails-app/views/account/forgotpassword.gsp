<%--
  Created by IntelliJ IDEA.
  User: kvancott
  Date: 7/12/2019
  Time: 3:35 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
</head>

<body>
  <div class="backcard">
    <g:form controller="account" action="sendpasswordresetemail" method="post">
      <p><g:message code="msg.forgotpassword" default="Forgot your password? Don't worry! You can reset it through your email."/></p>
      <table>
        <tr>
          <td>
            <label for="email"><g:message code="msg.email" default="Email"/>:</label>
          </td>
          <td>
            <input type="email" name="email" id="email" minlength="5" maxlength="100" required>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" value="<g:message code="msg.sendpasswordreset" default="Send Password Reset Link"/>"
                   class="btn-white p-1 all-center">
          </td>
        </tr>
      </table>
    </g:form>
  </div>
</body>
</html>