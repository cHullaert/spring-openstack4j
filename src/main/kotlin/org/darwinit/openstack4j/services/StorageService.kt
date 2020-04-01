package org.darwinit.openstack4j.services

import org.darwinit.openstack4j.configuration.OVHConfiguration
import org.apache.commons.io.IOUtils
import org.openstack4j.model.common.Identifier
import org.openstack4j.model.common.Payloads
import org.openstack4j.model.storage.`object`.options.ObjectListOptions
import org.openstack4j.model.storage.`object`.options.ObjectPutOptions
import org.openstack4j.openstack.OSFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.net.URLConnection
import javax.servlet.http.HttpServletResponse


@Service
open class StorageService: IStorageService {

    @Autowired
    private lateinit var ovhConfiguration: OVHConfiguration

    private val client by lazy {
        val endpoint = OSFactory.builderV3()
            .endpoint(ovhConfiguration.endpoint)

        val domainIdentifier: Identifier? = createDomainIdentifier()
        val projectIdentifier: Identifier? = createProjectIdentifier()

        val local=when {
            ovhConfiguration.authentication.token!=null -> {
                endpoint.token(ovhConfiguration.authentication.token)
                        .scopeToProject(projectIdentifier,
                                        domainIdentifier).authenticate()
            }
            ovhConfiguration.authentication.user.id!=null -> {
                endpoint.credentials(ovhConfiguration.authentication.user.id,
                                    ovhConfiguration.authentication.password,
                                    domainIdentifier)
                        .scopeToProject(projectIdentifier,
                                        domainIdentifier).authenticate()
            }
            else -> throw IllegalArgumentException("no authentication")
        }

        local.useRegion(this.ovhConfiguration.region)

        local
    }

    private fun createProjectIdentifier() = Identifier.byId(ovhConfiguration.project.id)

    private fun createDomainIdentifier(): Identifier? {
        return if (ovhConfiguration.domain.id != null) {
            Identifier.byId(ovhConfiguration.domain.id)
        } else {
            Identifier.byName(ovhConfiguration.domain.name)
        }
    }

    override fun dummy() {
        val account = this.client.objectStorage().account().get()
        val objects =this.client.objectStorage().objects().list("test-demo")
        objects.forEach {
            println(it.name)
        }
        println("container count: "+account.containerCount)

    }

    override fun containerExists(name: String): Boolean {
        return this.client.objectStorage().containers().list().firstOrNull {
            it.name==name
        } != null
    }

    override fun createContainer(name: String): Boolean {
        return this.client.objectStorage().containers().create(name).isSuccess
    }

    override fun list(location: DirectoryLocation): List<StorageObject> {
        return this.client.objectStorage().objects().list(location.container,
                                                          ObjectListOptions.create().path(location.path))
            .map {
                StorageObject(it.name, it.directoryName)
            }
    }

    override fun createObject(objectLocation: ObjectLocation, stream: InputStream): String {

        return this.client.objectStorage()
                    .objects()
                    .put(objectLocation.container, objectLocation.name, Payloads.create(stream),
                            ObjectPutOptions.create().path(objectLocation.path))
    }

    override fun delete(objectLocation: ObjectLocation): Boolean {
        return this.client.objectStorage().objects().delete(objectLocation.container,
            createFullPath(objectLocation)
        ).isSuccess
    }

    override fun download(objectLocation: ObjectLocation): InputStream {
        return this.client.objectStorage().objects().download(objectLocation.container,
            createFullPath(objectLocation)
        ).inputStream
    }

    override fun download(objectLocation: ObjectLocation, response: HttpServletResponse) {
        val inputStream: InputStream = this.download(objectLocation)
        InputStreamResource(inputStream)

        val fullPath = createFullPath(objectLocation)

        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename=$fullPath")
        response.contentType = URLConnection.guessContentTypeFromName(fullPath)

        // Copy the stream to the response's output stream.
        IOUtils.copy(inputStream, response.outputStream)
        response.flushBuffer()
    }

    private fun createFullPath(objectLocation: ObjectLocation)=if(objectLocation.path.endsWith("/"))
                                                                    objectLocation.path + objectLocation.name
                                                                else
                                                                    objectLocation.path+"/"+objectLocation.name

    override fun upload(directoryLocation: DirectoryLocation, file: MultipartFile): String {
        return this.createObject(
            ObjectLocation(
                file.originalFilename,
                directoryLocation.path,
                directoryLocation.container
            ),
                                 file.inputStream)
    }


}