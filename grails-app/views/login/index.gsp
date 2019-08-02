<%@ page contentType="text/html;charset=UTF-8" %>
<sec:ifNotLoggedIn>
  <html>
  <head>
    <meta name="layout" content="main"/>
    <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
  </head>

  <body>
    <g:include view="subforms/loginform.gsp"/>
  </body>
  </html>
</sec:ifNotLoggedIn>
<sec:ifLoggedIn>
  <script>
      window.location = "/home"
  </script>
  <noscript>
    <a href="/home">Link to Weather Wolf home</a>
  </noscript>
</sec:ifLoggedIn>