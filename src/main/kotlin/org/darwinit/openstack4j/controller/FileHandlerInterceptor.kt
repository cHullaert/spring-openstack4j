package org.darwinit.openstack4j.controller

import org.darwinit.openstack4j.configuration.OVHConfiguration
import org.darwinit.openstack4j.services.IInterceptorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class FileHandlerInterceptor: HandlerInterceptor {
    @Autowired
    private lateinit var interceptorService: IInterceptorService

    @Autowired
    private lateinit var ovhConfiguration: OVHConfiguration

    override fun preHandle(request: HttpServletRequest?,
                            response: HttpServletResponse?,
                            handler: Any?): Boolean {

        return if (request?.requestURI==ovhConfiguration.files.downloadMapping){
            interceptorService.handleDownload(request, response, handler)
        } else  if (request?.requestURI==ovhConfiguration.files.uploadMapping) {
            interceptorService.handleUpload(request, response, handler)
        }
        else true
    }
}