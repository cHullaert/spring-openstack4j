package org.darwinit.openstack4j.configuration

import org.darwinit.openstack4j.controller.FileHandlerInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class WebConfiguration: WebMvcConfigurer {
    @Autowired
    private lateinit var fileHandlerInterceptor: FileHandlerInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(fileHandlerInterceptor)
    }
}