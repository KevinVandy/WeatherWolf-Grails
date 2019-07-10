<div id="login">
  <div class="inner p-2" style="width: 500px;">
    <div class="fheader">Sign up to see Personalized Weather</div>

    <form action="/account/register" method="POST" id="loginForm" autocomplete="off">
      <if test="${binding.hasVariable('msg') && msg != null}">
        <p class="bg-danger">${errMsg}</p>
      </if>
      <table>
        <tr>
          <td>
            <label for="username">Username:</label>
          </td>
          <td>
            <input type="text" name="username" id="username" required=""/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="email">Email:</label>
          </td>
          <td>
            <input type="email" name="email" id="email" required=""/>
          </td>
        </tr>
        <tr>
          <td>
            <label for="password">Password:</label>
          </td>
          <td>
            <input type="password" name="password" id="password" required=""/>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" id="submit" value="Register" class="badge all-center"/>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <p class="text-center">Already have an account? <a href="/account/login">Login</a></p>
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