<div id="login">
  <div class="inner p-2" style="width: 500px;">
    <g:include view="subforms/msg.gsp"/>
    <div class="fheader"><g:message code="msg.login.why" default="Login to see Personalized Weather"/></div>

    <form action="/login/authenticate" method="POST" id="loginForm" autocomplete="off">
      <table>
        <tr>
          <td>
            <label for="username"><g:message code="msg.username" default="Username"/>:</label>
          </td>
          <td>
            <input type="text" name="username" id="username" value="${params.username}"/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="password"><g:message code="msg.password" default="Password"/>:</label>
          </td>
          <td>
            <input type="password" name="password" id="password"/>
            <a href="/login/forgotpassword">Forgot your password?</a>
          </td>
        </tr>
        <tr>
          <td>
            <input type="submit" id="submit" value="<g:message code="msg.login"/>" class="badge"/>
          </td>
          <td>
            <input type="checkbox" class="text-center-inline" name="remember-me" id="remember_me"/>
            <label for="remember_me" class=""><g:message code="msg.rememberme" default="Remember me"/></label>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <p class="text-center"><g:message code="msg.donthaveaccount" default="Don't have an account yet?"/> <a
                href="/signup"><g:message code="msg.signup"/>!</a></p>
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>
<script>
    (function () {
        document.forms['loginForm'].elements['username'].focus();
    })();
</script>