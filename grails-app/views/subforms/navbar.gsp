<nav class="navbar nav-fixed bg-primary" role="navigation" style="font-size: 15pt; !important;">
  <a href="/home<sec:ifLoggedIn>?units=${user.units}</sec:ifLoggedIn>"><h1 style="display: inline"><g:message code="msg.weatherwolf" default="Weather Wolf"/></h1></a>
  <asset:image src="logo.png" class="logo"/>
  <ul>
    <li>
      <sec:ifNotLoggedIn>
        <select onChange="window.location.href = this.value">
          <option value="" disabled selected><g:message code="language" default="Language"/></option>
          <navbar:localeDropdownListItems uri="${request.forwardURI}" params="${params}"/>
        </select>
      </sec:ifNotLoggedIn>
      <sec:ifLoggedIn>
        <form id="changelang" action="/account/changelang" method="get">
          <select name="lang" onchange="document.getElementById('changelang').submit()">
            <option value="" disabled selected><g:message code="language" default="Language"/></option>
            <option value="en"><g:message code="language.en" default="English"/></option>
            <option value="es"><g:message code="language.es" default="Spanish"/></option>
            <option value="fr"><g:message code="language.fr" default="French"/></option>
          </select>
        </form>
      </sec:ifLoggedIn>
    </li>
    <li>
      <a href="/home<sec:ifLoggedIn>?units=${user.units}</sec:ifLoggedIn>"><g:message code="msg.home" default="Home"/></a>
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