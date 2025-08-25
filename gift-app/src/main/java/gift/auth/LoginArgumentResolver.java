package gift.auth;

import gift.member.domain.RoleType;
import gift.member.dto.MemberTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    public LoginArgumentResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class)
                && parameter.getParameterType().equals(MemberTokenRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = AuthUtil.extractToken(request);

        Long id = jwtUtil.getId(token);
        String email = jwtUtil.getEmail(token);
        RoleType role = jwtUtil.getRoleType(token);

        return new MemberTokenRequest(id, email, null, role, token);
    }
}
