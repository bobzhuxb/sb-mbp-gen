package ${packageName}.config;

import ${packageName}.aop.filter.CachingHttpHeadersFilter;
import ${packageName}.config.help.PropertiesHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.EnumSet;

import static java.net.URLDecoder.decode;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 * @author Bob
 */
@Configuration
@EnableConfigurationProperties(PropertiesHelp.class)
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    private final PropertiesHelp propertiesHelp;

//    private MetricRegistry metricRegistry;

    public WebConfigurer(Environment env, PropertiesHelp propertiesHelp) {

        this.env = env;
        this.propertiesHelp = propertiesHelp;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
//        initMetrics(servletContext, disps);
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            initCachingHttpHeadersFilter(servletContext, disps);
        }
        log.info("Web application fully configured");
    }

    /**
     * Customize the Servlet engine: Mime types, the document root, the cache.
     */
    @Override
    public void customize(WebServerFactory server) {
        setMimeMappings(server);
        // When running in an IDE or with ./gradlew bootRun, set location of the static web assets.
        setLocationForStaticAssets(server);
    }

    private void setMimeMappings(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
            // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
            mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
            // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
            mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
            servletWebServer.setMimeMappings(mappings);
        }
    }

    private void setLocationForStaticAssets(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
            File root;
            String prefixPath = resolvePathPrefix();
            root = new File(prefixPath + "build/www/");
            if (root.exists() && root.isDirectory()) {
                servletWebServer.setDocumentRoot(root);
            }
        }
    }

    /**
     * Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath;
        try {
            fullExecutablePath = decode(this.getClass().getResource("").getPath(), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            /* try without decoding if this ever happens */
            fullExecutablePath = this.getClass().getResource("").getPath();
        }
        String rootPath = Paths.get(".").toUri().normalize().getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        int extractionEndIndex = extractedPath.indexOf("build/");
        if (extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
                                              EnumSet<DispatcherType> disps) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
            servletContext.addFilter("cachingHttpHeadersFilter",
                new CachingHttpHeadersFilter(propertiesHelp));

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/static/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/test/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

//    /**
//     * Initializes Metrics.
//     */
//    private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
//        log.debug("Initializing Metrics registries");
//        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
//            metricRegistry);
//        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
//            metricRegistry);
//
//        log.debug("Registering Metrics Filter");
//        FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter",
//            new InstrumentedFilter());
//
//        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
//        metricsFilter.setAsyncSupported(true);
//
//        log.debug("Registering Metrics Servlet");
//        ServletRegistration.Dynamic metricsAdminServlet =
//            servletContext.addServlet("metricsServlet", new MetricsServlet());
//
//        metricsAdminServlet.addMapping("/management/metrics/*");
//        metricsAdminServlet.setAsyncSupported(true);
//        metricsAdminServlet.setLoadOnStartup(2);
//    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = propertiesHelp.getCors();
//        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
//            log.debug("Registering CORS filter");
//            source.registerCorsConfiguration("/api/**", config);
//            source.registerCorsConfiguration("/management/**", config);
//            source.registerCorsConfiguration("/v2/api-docs", config);
//        }
//        return new CorsFilter(source);
//    }

//    @Autowired(required = false)
//    public void setMetricRegistry(MetricRegistry metricRegistry) {
//        this.metricRegistry = metricRegistry;
//    }
}
