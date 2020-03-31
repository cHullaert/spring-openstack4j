package org.darwinit.openstack4j.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "ovh")
class OVHConfiguration {

    var authentication: Authentication =
        Authentication()
    var domain: Domain =
        Domain()
    var project: Project =
        Project()
    var endpoint: String = ""
    val region: String = "SBG"
    val files = Files()

    class Project {
        var id: String? =null
    }

    class Files {
        var downloadMapping: String = "/files/download"
        var uploadMapping: String = "/files/upload"
    }

    class Domain {
        var id: String? = null
        var name: String? = null
    }

    class User {
        var id: String? = null
        var name: String? = null
    }

    class Authentication {
        var token: String? = null
        var user = User()
        var domain = Domain()
        var password: String = ""
    }
}