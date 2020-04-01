package org.darwinit.openstack4j.services

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import javax.servlet.http.HttpServletResponse

data class StorageObject(val name: String, val directory: String?)

data class DirectoryLocation(val path: String, val container: String)
data class ObjectLocation(val name: String, val path: String, val container: String)

interface IStorageService {
    fun dummy()
    fun containerExists(name: String): Boolean
    fun createContainer(name: String): Boolean
    fun list(location: DirectoryLocation): List<StorageObject>
    fun createObject(objectLocation: ObjectLocation, stream: InputStream): String
    fun delete(objectLocation: ObjectLocation): Boolean
    fun download(objectLocation: ObjectLocation): InputStream
    fun download(objectLocation: ObjectLocation, response: HttpServletResponse)
    fun upload(directoryLocation: DirectoryLocation, file: MultipartFile): String
}