package org.darwinit.openstack4j

import org.darwinit.openstack4j.services.DirectoryLocation
import org.darwinit.openstack4j.services.IStorageService
import org.darwinit.openstack4j.services.ObjectLocation
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.ByteArrayInputStream


@SpringBootTest
@ActiveProfiles("test")
class ApplicationTest {

    @Autowired
    private lateinit var storageService: IStorageService

    @Test
    fun contextLoad() {
        storageService.dummy()
    }

    @Test
    fun storageList() {
        this.storageService.list(
            DirectoryLocation(
                "my-incredible-path",
                "test-demo"
            )
        )
            .forEach {
                println(it)
            }
    }

    @Test
    fun putObject() {
        val value="test-1"
        val stream= ByteArrayInputStream(value.toByteArray())
        println(this.storageService.createObject(
            ObjectLocation(
                "delta-1",
                "my-incredible-path/",
                "test-demo"
            ), stream))
    }

    @Test
    fun deleteObject() {
        this.storageService.delete(
            ObjectLocation(
                "delta-1",
                "my-incredible-path/",
                "test-demo"
            )
        )
    }
}