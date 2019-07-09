<div id="login" >
  <div class="inner" class="all-left">
    <div class="fheader">Login to see Personalized Weather</div>
    <form action="/login/authenticate" method="POST" id="loginForm" autocomplete="off">
      <table>
        <tr>
          <th>
            <label for="username" >Username:</label>
          </th>
          <td>
            <input type="text"  name="username" id="username"/>
          </td>
        </tr>
        <tr>
          <th>
            <label for="password">Password:</label>
          </th>
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
      </table>
    </form>
  </div>
</div>
<script>
    (function () {
        document.forms['loginForm'].elements['username'].focus();
    })();
</script>