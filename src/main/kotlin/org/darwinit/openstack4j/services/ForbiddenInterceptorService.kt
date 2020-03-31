package org.darwinit.openstack4j.services

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
@Profile("darwin-staging")
class ForbiddenInterceptorService: IInterceptorService {
    override fun handleDownload(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        response?.status=403
        return false
    }

    override fun handleUpload(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        response?.status=403
        return false
    }
}