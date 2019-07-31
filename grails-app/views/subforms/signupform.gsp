<div id="login">
  <div class="inner p-2" style="width: 500px;">
    <g:include view="subforms/msg.gsp"/>
    <div class="fheader">
      <g:message code="msg.signup.why" default="Sign up to see Personalized Weather"/>
    </div>

    <form action="/signup/register" method="POST" id="loginForm" autocomplete="off">
      <table>
        <tr>
          <td>
            <label for="username"><g:message code="msg.username" default="Username"/>:</label>
          </td>
          <td>
            <input type="text" name="username" id="username" value="${username}" minlength="3" maxlength="50" required/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="email"><g:message code="msg.email" default="Email"/>:</label>
          </td>
          <td>
            <input type="email" name="email" id="email" value="${email}" minlength="6" maxlength="100" required/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="password"><g:message code="msg.password"/>:</label>
          </td>
          <td>
            <g:passwordField name="password" id="password" minlength="6" maxlength="100" required=""/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="passwordconfirm"><g:message code="msg.confirmpassword" default="Confirm Password"/>:</label>
          </td>
          <td>
            <g:passwordField name="passwordconfirm" id="passwordconfirm" minlength="6" maxlength="100" required=""/>
          </td>
        </tr>
        <tr>
          <td></td>
          <td>
            <g:checkBox name="togglePassword" id="togglePassword" />Show Password
          </td>
        </tr>
        <tr>
          <td>
            <label><g:message code="msg.favoritelocation" default="Favorite Location"/>:</label>
          </td>
          <td id="remote">
            <input type="text" name="favoritelocation" id="favoritelocation" required minlength="2" maxlength="100"
                   placeholder="<g:message code='msg.search.placeholder'/>" class="typeahead"
                   value="${favoritelocation}">
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" id="submit" value="<g:message code="msg.signup" default="Register"/>" class="badge all-center"/>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <p class="text-center"><g:message code="msg.alreadyhaveacount" default="Already Have an Account?"/> <a
                href="/login"><g:message code="msg.login" default="Login"/></a></p>
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

    //disable typing spaces in username
    $("#username").on({
        keydown: function (e) {
            if (e.which === 32)
                return false;
        },
        change: function () {
            this.value = this.value.replace(/\s/g, "");
        }
    });

    $("#togglePassword").change(function () {
        if ($('#password').attr('type') == 'text') {
            $('#password').attr('type', 'password');
            $('#passwordconfirm').attr('type', 'password');
        } else {
            $('#password').attr('type', 'text');
            $('#passwordconfirm').attr('type', 'text');
        }
    });
</script>