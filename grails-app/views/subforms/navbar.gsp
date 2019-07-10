<nav class="navbar nav-fixed bg-primary" role="navigation">
  <h1 style="display: inline">Weather Wolf</h1>
  <asset:image src="logo.png" class="logo"/>
  <ul>
    <li><a href="/">Home</a></li>
    <sec:ifLoggedIn>
      <li>
        <a href="/account/index">Account</a>
      </li>
      <li>
        <g:link controller="logout">Logout</g:link>
      </li>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <li>
        <a href="/account/signup">Sign Up</a>
      </li>
      <li>
        <a href="/account/login">Log in</a>
      </li>
    </sec:ifNotLoggedIn>
  </ul>
</nav>

<div class="navbar-spacer"></div>