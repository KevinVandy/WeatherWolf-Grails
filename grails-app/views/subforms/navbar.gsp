<nav class="navbar nav-fixed bg-primary" role="navigation" style="font-size: 15pt; !important;">
  <a href="/home"><h1 style="display: inline"><g:message code="msg.weatherwolf" default="Weather Wolf"/></h1></a>
  <asset:image src="logo.png" class="logo"/>
  <ul>
    <li>
      <select onChange="window.location.href = this.value">
        <option value="" disabled selected><g:message code="language" default="Language"/></option>
        <navbar:localeDropdownListItems uri="${request.forwardURI}" params="${params}"/>
      </select>
    </li>
    <li>
      <a href="/home"><g:message code="msg.home" default="Home"/></a>
    </li>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
      <li>
        <g:link controller="admin" action="index"><g:message code="msg.admin" default="Admin"/></g:link>
      </li>
    </sec:ifAnyGranted>
    <sec:ifLoggedIn>
      <li>
        <g:link controller="account" action="index"><g:message code="msg.account" default="Account"/></g:link>
      </li>
      <li>
        <g:link controller="logout" action="index"><g:message code="msg.logout" default="Logout"/></g:link>
      </li>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <li>
        <g:link controller="signup" action="index"><g:message code="msg.signup" default="Sign up"/></g:link>
      </li>
      <li>
        <g:link controller="login" action="index"><g:message code="msg.login" default="Log in"/></g:link>
      </li>
    </sec:ifNotLoggedIn>
  </ul>
</nav>

<div class="navbar-spacer"></div>