<!doctype html>
<html>
    <head>
        <title>${message(code: 'msg.weatherwolf', default: 'Weather Wolf')} - Page not Found</title>
        <meta name="layout" content="main">
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
        <div class="backcard">
            <ul class="errors">
                <li>Error: Page Not Found (404)</li>
                <li>Path: ${request.forwardURI}</li>
            </ul>
        </div>
    </body>
</html>
