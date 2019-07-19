package com.weatherwolf

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {

            }
        }

        "/"(view: '/index')
        "500"(view: '/error')
        "404"(view: '/notFound')

        "/city"(resources: 'city') {
            collection {
                '/search'(controller: 'city', action: 'search')
            }
        }
    }
}
