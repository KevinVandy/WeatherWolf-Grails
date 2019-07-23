<div id="login">
  <div class="inner p-2" style="width: 500px;">
    <div class="fheader">
      <g:message code="msg.signup.why" default="Sign up to see Personalized Weather"/>
    </div>

    <form action="/account/register" method="POST" id="loginForm" autocomplete="off">
      <if test="${msg != ''}">
        <p class="bg-danger">${msg}</p>
      </if>
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
            <input type="email" name="email" id="email" value="${email}" minlength="5" maxlength="100" required/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="password"><g:message code="msg.password"/>:</label>
          </td>
          <td>
            <input type="password" name="password" id="password" minlength="6" maxlength="100" required/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="passwordconfirm"><g:message code="msg.confirmpassword" default="Confirm Password"/>:</label>
          </td>
          <td>
            <input type="password" name="passwordconfirm" id="passwordconfirm" minlength="6" maxlength="100" required/>
          </td>
        </tr>
        <tr>
          <td>
            <label><g:message code="msg.favoritelocation" default="Favorite Location"/>:</label>
          </td>
          <td id="remote">
            <input type="text" name="favoritelocation" id="favoritelocation" required minlength="2" maxlength="100"
                   placeholder="<g:message code='msg.search.placeholder'/>" class="typeahead"
                   value="<g:if test="${params.location}">${params.location}</g:if><g:else>${favoriteLocation}</g:else>">
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
</script>