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
  }
  </style>
</head>

<body>
  <content tag="nav">

  </content>

  <div class="svg" role="presentation">
  </div>

  <div id="content" role="main">

    <sec:ifLoggedIn>
      <h3 class="text-center">Hello <sec:username/></h3>

      <div>
        <g:include view="subforms/search.gsp"/>
      </div>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <div class="grid-2">
        <div>
          <g:include view="subforms/search.gsp"/>
        </div>

        <div>
          <g:include view="subforms/loginform.gsp"/>
        </div>
      </div>
    </sec:ifNotLoggedIn>


  </div>

</body>
</html>
