package ${packageName}.config.help;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 系统属性配置工具类
 * @author Bob
 */
@ConfigurationProperties(prefix = "tools", ignoreUnknownFields = false)
@PropertySources({
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true)
})
public class PropertiesHelp {

    private final Async async = new Async();

    private final Http http = new Http();

    private final Security security = new Security();

    private final CorsConfiguration cors = new CorsConfiguration();

    public Async getAsync() {
        return async;
    }

    public Http getHttp() {
        return http;
    }

    public Security getSecurity() {
        return security;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public static class Async {

        private int corePoolSize = PropertiesDefaults.Async.corePoolSize;

        private int maxPoolSize = PropertiesDefaults.Async.maxPoolSize;

        private int queueCapacity = PropertiesDefaults.Async.queueCapacity;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Http {

        private final Cache cache = new Cache();

        /**
         * Https has to be active with cipher suite define also
         */
        private boolean useUndertowUserCipherSuitesOrder = PropertiesDefaults.Http.useUndertowUserCipherSuitesOrder;

        public Cache getCache() {
            return cache;
        }

        public static class Cache {

            private int timeToLiveInDays = PropertiesDefaults.Http.Cache.timeToLiveInDays;

            public int getTimeToLiveInDays() {
                return timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }

        public boolean isUseUndertowUserCipherSuitesOrder() {
            return useUndertowUserCipherSuitesOrder;
        }

        public void setUseUndertowUserCipherSuitesOrder(boolean useUndertowUserCipherSuitesOrder) {
            this.useUndertowUserCipherSuitesOrder = useUndertowUserCipherSuitesOrder;
        }
    }

    public static class Security {

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        private final RememberMe rememberMe = new RememberMe();

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public static class ClientAuthorization {

            private String accessTokenUri = PropertiesDefaults.Security.ClientAuthorization.accessTokenUri;

            private String tokenServiceId = PropertiesDefaults.Security.ClientAuthorization.tokenServiceId;

            private String clientId = PropertiesDefaults.Security.ClientAuthorization.clientId;

            private String clientSecret = PropertiesDefaults.Security.ClientAuthorization.clientSecret;

            public String getAccessTokenUri() {
                return accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }

        public static class Authentication {

            private final Jwt jwt = new Jwt();

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {

                private String secret = PropertiesDefaults.Security.Authentication.Jwt.secret;

                private String base64Secret = PropertiesDefaults.Security.Authentication.Jwt.base64Secret;

                private long tokenValidityInSeconds = PropertiesDefaults.Security.Authentication.Jwt
                        .tokenValidityInSeconds;

                private long tokenValidityInSecondsForRememberMe = PropertiesDefaults.Security.Authentication.Jwt
                        .tokenValidityInSecondsForRememberMe;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class RememberMe {

            @NotNull
            private String key = PropertiesDefaults.Security.RememberMe.key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }

    public static class Logging {

        private final Logstash logstash = new Logstash();

        public Logstash getLogstash() {
            return logstash;
        }

        public static class Logstash {

            private boolean enabled = PropertiesDefaults.Logging.Logstash.enabled;

            private String host = PropertiesDefaults.Logging.Logstash.host;

            private int port = PropertiesDefaults.Logging.Logstash.port;

            private int queueSize = PropertiesDefaults.Logging.Logstash.queueSize;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }
    }
    
}
