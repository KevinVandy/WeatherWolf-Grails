<div id="login" >
  <div class="inner p-2" style="width: 500px;">
    <div class="fheader">Login to see Personalized Weather</div>
    <form action="/login/authenticate" method="POST" id="loginForm" autocomplete="off">
      <if test="${binding.hasVariable('msg') && msg != null}">
        <p class="alert-info">${errMsg}</p>
      </if>
      <table>
        <tr>
          <td>
            <label for="username" >Username:</label>
          </td>
          <td>
            <input type="text"  name="username" id="username"/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="password">Password:</label>
          </td>
          <td>
            <input type="password" name="password" id="password"/>
          </td>
        </tr>
        <tr>
          <td>
            <input type="submit" id="submit" value="Login" class="badge"/>
          </td>
          <td>
            <input type="checkbox" class="text-center-inline" name="remember-me" id="remember_me"/>
            <label for="remember_me" class="">Remember me</label>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <p class="text-center">Don't have an account yet? <a href="/account/signup">Sign Up!</a></p>
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