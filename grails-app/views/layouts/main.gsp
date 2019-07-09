<!doctype html>
<html lang="en" class="no-js">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <title>
    <g:layoutTitle default="Grails"/>
  </title>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

  <asset:stylesheet src="application.css"/>

  <g:layoutHead/>
</head>

<body>

  <nav class="navbar nav-fixed bg-primary" role="navigation">
    <h1 style="display: inline">Weather Wolf</h1>
    <asset:image src="logo.png" class="logo"/>

    <ul>
      <li><g:link href="/search/index">Home</g:link></li>
      <li><g:link href="/about">About</g:link></li>
      <sec:ifLoggedIn>
        <li>
          <g:link controller="logout">Logout</g:link>
        </li>
      </sec:ifLoggedIn>
      <sec:ifNotLoggedIn>
        <li><g:link href="/account/index">Sign In</g:link></li>
      </sec:ifNotLoggedIn>

    </ul>

  </nav>

  <div class="navbar-spacer"></div>

  <g:layoutBody/>

  <footer class="footer row" role="contentinfo">
    <div id="controllers" role="navigation" class="text-center">
      <h2>Available Controllers:</h2>
      <ul>
        <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName }}">
          <li class="controller">
            <g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link>
          </li>
        </g:each>
      </ul>
    </div>
  </footer>

  <div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
  </div>

  <asset:javascript src="application.js"/>

</body>
</html>
