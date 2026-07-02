package com.example.aicode.security.oauth;

import com.example.aicode.entity.User;
import com.example.aicode.security.auth.CustomOAuth2User;
import com.example.aicode.security.jwt.JwtService;
import com.example.aicode.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        System.out.println("===== SUCCESS HANDLER CALLED =====");
        CustomOAuth2User oauthUser =
                (CustomOAuth2User) authentication.getPrincipal();

        User user = oauthUser.getUser();

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.saveOrUpdate(user, refreshToken);

        String redirectUrl = frontendUrl
                + "/oauth/success"
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

//        getRedirectStrategy().sendRedirect(
//                request,
//                response,
//                redirectUrl
//        );

        response.setContentType("application/json");

        response.getWriter().write("""
        {
           "accessToken":"%s",
           "refreshToken":"%s"
        }
        """.formatted(accessToken, refreshToken));
    }
}