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
      <a href="/home/"><g:message code="msg.home" default="Home"/></a>
    </li>
    <sec:ifLoggedIn>
      <li>
        <a href="/account/index"><g:message code="msg.account" default="Account"/></a>
      </li>
      <li>
        <g:link controller="logout" action="index"><g:message code="msg.logout" default="Logout"/></g:link>
      </li>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <li>
        <a href="/account/signup"><g:message code="msg.signup" default="Sign up"/></a>
      </li>
      <li>
        <a href="/account/login"><g:message code="msg.login" default="Log in"/></a>
      </li>
    </sec:ifNotLoggedIn>
  </ul>
</nav>

<div class="navbar-spacer"></div>