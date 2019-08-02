<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>
<!doctype html>
<html lang="" class="no-js">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
  <asset:stylesheet src="application.css"/>
  <asset:stylesheet src="buttons.dataTables.min.css"/>
  <asset:stylesheet src="select.dataTables.min.css"/>
  <asset:stylesheet src="editor.dataTables.min.css"/>
  <asset:javascript src="jquery-3.3.1.min.js"/>
  <asset:javascript src="jqueryDataTables.min.js"/>
  <asset:javascript src="dataTables.buttons.min.js"/>
  <asset:javascript src="buttons.flash.min.js"/>
  <asset:javascript src="jszip.min.js"/>
  <asset:javascript src="pdfmake.min.js"/>
  <asset:javascript src="vfs_fonts.js"/>
  <asset:javascript src="buttons.html5.min.js"/>
  <asset:javascript src="buttons.print.min.js"/>
  %{--  <asset:javascript src="dataTables.editor.min.js"/>--}%
  <asset:javascript src="handlebars.js"/>
  <asset:javascript src="application.js" defer=""/>
  <g:layoutHead/>
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

  <g:include view="subforms/navbar.gsp"/>
  <div id="wrapper">
    <g:layoutBody/>
  </div>

  <g:include view="subforms/footer.gsp"/>

</body>
</html>
