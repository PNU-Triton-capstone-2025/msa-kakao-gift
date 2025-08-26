package gift.common.config;

import gift.auth.AdminInterceptor;
import gift.auth.LoginArgumentResolver;
import gift.auth.LoginInterceptor;
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
        // 1. 로그인 인터셉터: 웹 페이지만 보호
        registry.addInterceptor(loginInterceptor)
                .order(1)
                .addPathPatterns("/**") // 모든 경로에 적용하되,
                .excludePathPatterns(
                        // API 경로는 Gateway가 처리하므로 전부 제외
                        "/api/**",

                        // 공개 View 경로는 그대로 유지
                        "/",
                        "/members/login",
                        "/members/register",
                        "/members/login/oauth2/code/kakao",
                        "/css/**", "/js/**", "/error", "/favicon.ico", "/h2-console/**"
                );

        // 2. 관리자 인터셉터: 관리자용 웹 페이지만 보호
        registry.addInterceptor(adminInterceptor)
                .order(2)
                .addPathPatterns("/admin/**"); // API 경로 제외
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