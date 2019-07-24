package com.weatherwolf

import org.apache.camel.builder.RouteBuilder


class EmailRoute extends RouteBuilder{

    def grailsApplication

    @Override
    void configure() throws Exception {
        def config = grailsApplication?.config


    }
}
