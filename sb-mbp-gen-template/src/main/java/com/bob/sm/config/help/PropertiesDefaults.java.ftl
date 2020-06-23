package ${packageName}.config.help;

/**
 * 默认属性配置
 * @author Bob
 */
public class PropertiesDefaults {

    interface Async {

        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }

    interface Http {

        boolean useUndertowUserCipherSuitesOrder = true;

        interface Cache {

            int timeToLiveInDays = 1461; // 4 years (including leap day)
        }
    }

    interface Security {

        interface ClientAuthorization {

            String accessTokenUri = null;
            String tokenServiceId = null;
            String clientId = null;
            String clientSecret = null;
        }

        interface Authentication {

            interface Jwt {

                String secret = null;
                String base64Secret = null;
                long tokenValidityInSeconds = 1800; // 0.5 hour
                long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours;
            }
        }

        interface RememberMe {

            String key = null;
        }
    }

    interface Logging {

        interface Logstash {

            boolean enabled = false;
            String host = "localhost";
            int port = 5000;
            int queueSize = 512;
        }
    }

}
