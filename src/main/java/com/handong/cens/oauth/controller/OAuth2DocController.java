package com.handong.cens.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "OAuth2", description = "구글 소셜 로그인 관련 엔드포인트")
public class OAuth2DocController {

    @Operation(
            summary = "구글 OAuth2 로그인",
            description = "Google 로그인 페이지로 리다이렉트합니다.\n\n" +
                    "성공 시 프론트엔드로 accessToken, refreshToken을 포함한 URL로 리다이렉트됩니다.\n\n" +
                    "이 엔드포인트는 Spring Security가 처리합니다."
    )
    @GetMapping("/oauth2/authorization/google")
    public void googleLoginRedirectDoc() {
        // Swagger 문서용 dummy 엔드포인트 (실제로는 Security에서 처리됨)
        throw new UnsupportedOperationException("Spring Security에서 처리됨");
    }
}