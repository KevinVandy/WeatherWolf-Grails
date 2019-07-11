<!doctype html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Welcome to Grails</title>
  <style type="text/css" media="screen">
  body {
    background-image: url("${resource(dir: "images", file: "background.jpg")}");
    background-repeat: no-repeat;
    background-size: cover;
    background-attachment: fixed;
  }
  </style>
</head>

<body>
  <div id="content" role="main">
    <g:include view="subforms/searchbar.gsp"/>
    <sec:ifLoggedIn>

      <h3 class="text-center"><sec:username/>'s personal weather</h3>
      <g:include view="subforms/searchresults.gsp" />
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <div>
        <g:include view="subforms/loginform.gsp"/>
      </div>
    </sec:ifNotLoggedIn>
  </div>
</body>
</html>
