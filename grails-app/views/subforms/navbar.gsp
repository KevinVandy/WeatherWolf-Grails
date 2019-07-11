<nav class="navbar nav-fixed bg-primary" role="navigation" style="font-size: 15pt; !important;">
  <a href="/"><h1 style="display: inline">Weather Wolf</h1></a>
  <asset:image src="logo.png" class="logo"/>
  <ul>
    <li>
      <select onChange="window.location.href = this.value">
        <option value="" disabled selected><g:message code="language" default="Language" /></option>
        <navbar:localeDropdownListItems uri="${request.forwardURI}" params="${params}" />
      </select>
    </li>
    <li><a href="/"><g:message code="msg.home" default="Home" /></a></li>
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