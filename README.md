# Purpose

The idea is to offer a Spring based interface for openstack4j, for today only the object storage is 
available. 

# Usage

clone it, build it, use it.  
Don't forget to scan components for org.darwinit.openstack4j package

And most important, feel free.

## Object storage

```yaml
ovh:
  files:
    # url for upload multi part file (please consider size in spring app)
    upload-mapping: /files/upload
    # url for download file
    download-mapping: /files/download
  # region of the bucket
  region: #region#
  # autentication
  authentication:
    user:
      #user name
      name: christof
    # user password
    password: incredible-password
    # token instead password based authentication
    token: incredible-token
  
  domain:
    # id du domain
    id: default
    # or his name
    name: Default
  project:
    id: project-id
  # endpoint for authentication
  endpoint: https://auth.cloud.ovh.net/v3

```

### Security
Please create your own bean with implementation on IInterceptorService to manipulate query or use standard
Spring based web security with roles and so one.