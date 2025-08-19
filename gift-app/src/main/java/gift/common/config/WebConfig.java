package gift.common.config;

import gift.auth.AdminInterceptor;
import gift.auth.LoginInterceptor;
import gift.auth.LoginArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AdminInterceptor adminInterceptor;
    private final LoginArgumentResolver loginArgumentResolver;
    @Value("${spring.front.domain}")
    private String frontDomain;

    public WebConfig(LoginInterceptor loginInterceptor, AdminInterceptor adminInterceptor, LoginArgumentResolver loginArgumentResolver) {
        this.loginInterceptor = loginInterceptor;
        this.adminInterceptor = adminInterceptor;
        this.loginArgumentResolver = loginArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/members/register",
                        "/api/members/register/admin",
                        "/api/members/login",
                        "/members/register",
                        "/members/login",
                        "/members/login/oauth2/code/kakao",
                        "/",
                        "/h2-console/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/error",
                        "/favicon.ico"
                );

        registry.addInterceptor(adminInterceptor)
                .order(2)
                .addPathPatterns("/admin/**", "/api/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontDomain)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
