package fourth.project.end.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import fourth.project.end.auth.config.BootstrapAdminProperties;
import fourth.project.end.auth.config.JwtProperties;

@Configuration
@EnableConfigurationProperties({
    JwtProperties.class,
    BootstrapAdminProperties.class
})
public class ApplicationConfig {
}
