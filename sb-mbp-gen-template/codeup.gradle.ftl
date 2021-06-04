apply plugin: 'maven'

// load properties
Properties properties = new Properties()
File propertiesFile = project.file("codeup.properties")
if (propertiesFile.exists()) {
    properties.load(propertiesFile.newDataInputStream())
}

def NEXUS_USERNAME = properties.getProperty("NEXUS_USERNAME")
def NEXUS_PASSWORD = properties.getProperty("NEXUS_PASSWORD")
def MAVEN_URL = properties.getProperty("MAVEN_URL")

allprojects {
    repositories {
        maven {
            url 'https://maven.aliyun.com/repository/public'
        }
        maven {
            credentials {
                username NEXUS_USERNAME
                password NEXUS_PASSWORD
            }
            url MAVEN_URL
        }
    }
}