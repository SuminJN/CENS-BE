package com.handong.cens.oauth.handler;

import com.handong.cens.commons.util.JWTUtil;
import com.handong.cens.member.domain.Member;
import com.handong.cens.member.dto.MemberDto;
import com.handong.cens.oauth.entity.CustomOauth2UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOauth2UserDetails userDetails = (CustomOauth2UserDetails) authentication.getPrincipal();

        Member member = userDetails.getMember();

        MemberDto memberDto = new MemberDto(
                member.getLoginId(),
                member.getEmail(),
                member.getName(),
                member.getRole()
        );

        Map<String, Object> claims = memberDto.getClaim();

        String accessToken = jwtUtil.generateToken(claims, 30); // 30분
        String refreshToken = jwtUtil.generateToken(claims, 60 * 24); // 24시간

        // 예시: JWT를 리다이렉트 URL 파라미터에 포함시켜 전달
        String redirectUrl = "http://localhost:3000/oauth2/success?" +
                "accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;
        response.sendRedirect(redirectUrl);
    }
}
