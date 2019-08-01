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
        "405"(view: '/hackerman')

        "/location"(resources: 'location') {
            collection {
                '/index'(controller: 'location', action: 'index')
                '/search'(controller: 'location', action: 'search')
                '/searchcity'(controller: 'location', action: 'searchcity')
                '/searchstateprovince'(controller: 'location', action: 'searchstateprovince')
                '/searchcountry'(controller: 'location', action: 'searchcountry')
                '/fill'(controller: 'location', action: 'fill')
            }
        }

    }
}
