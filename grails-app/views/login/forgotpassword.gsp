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
    <g:form controller="login" action="sendpasswordresetemail" method="post">
      <g:include view="subforms/msg.gsp"/>
      <p><g:message code="msg.forgotpassword" default="Forgot your password? Don't worry! You can reset it through your email."/></p>
      <table>
        <tr>
          <td>
            <label for="username"><g:message code="msg.username" default="Username"/>:</label>
          </td>
          <td>
            <g:textField name="username" id="username" minlength="3" maxlength="100" required=""/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="email"><g:message code="msg.email" default="Email"/>:</label>
          </td>
          <td>
            <g:field type="email" name="email" id="email" minlength="5" maxlength="100" required=""/>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <g:submitButton name="submit" value="${message(code: 'msg.sendpasswordreset', default: 'Send Password Reset Link')}"
                            class="btn-white p-1 all-center"/>
          </td>
        </tr>
      </table>
    </g:form>
  </div>
</body>
</html>