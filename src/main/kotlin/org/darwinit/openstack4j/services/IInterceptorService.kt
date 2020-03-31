package org.darwinit.openstack4j.services

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface IInterceptorService {

    fun handleDownload(request: HttpServletRequest?,
                       response: HttpServletResponse?,
                       handler: Any?): Boolean
    fun handleUpload(request: HttpServletRequest?,
                       response: HttpServletResponse?,
                       handler: Any?): Boolean
}