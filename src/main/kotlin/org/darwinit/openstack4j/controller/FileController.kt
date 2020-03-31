package org.darwinit.openstack4j.controller

import org.darwinit.openstack4j.services.DirectoryLocation
import org.darwinit.openstack4j.services.IStorageService
import org.darwinit.openstack4j.services.ObjectLocation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping()
open class FileController {

    @Autowired
    private lateinit var storageService: IStorageService

    @PostMapping("\${ovh.files.download-mapping}")
    fun download(@RequestParam("container") container: String,
                 @RequestParam("path") path: String,
                 @RequestParam("object") name: String,
                 response: HttpServletResponse
    ) {
        this.storageService.download(
            ObjectLocation(
                name,
                path,
                container
            ), response)
    }

    @PostMapping("\${ovh.files.upload-mapping}")
    fun upload(@RequestParam("container") container: String,
               @RequestParam("path") path: String,
               @RequestParam("file") file: MultipartFile
    ) {
        this.storageService.upload(
            DirectoryLocation(
                path,
                container
            ), file)

    }
}