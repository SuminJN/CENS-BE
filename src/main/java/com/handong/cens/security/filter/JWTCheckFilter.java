package com.handong.cens.security.filter;

import com.google.gson.Gson;
import com.handong.cens.commons.util.JWTUtil;
import com.handong.cens.member.domain.MemberRole;
import com.handong.cens.member.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // 체크하지 않을 경로 지정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // Preflight 요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();
        log.info("check uri.............." + path);

        // /api/member/ 경로의 호출은 처크하지 않음
        if (path.startsWith("/oauth2/authorization")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("------------------JWTCheckFilter------------------");

        String authHeaderStr = request.getHeader("Authorization");
        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization Header");
        }

        try {
            // Bearer accessToken
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);

            log.info("JWT Claims={}", claims);

            String loginId = (String) claims.get("loginId");
            String email = (String) claims.get("email");
            String name = (String) claims.get("name");
            String roleStr = (String) claims.get("role");
            MemberRole role = MemberRole.valueOf(roleStr);

            MemberDto memberDto = new MemberDto(loginId, email, name, role);

            log.info("----------------");
            log.info(memberDto);
            log.info(memberDto.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDto, null, memberDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response); // 통과

        } catch (Exception e) {
            log.error("JWT 인증 실패", e);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> error = Map.of("error", "INVALID_ACCESS_TOKEN");

            PrintWriter writer = response.getWriter();
            writer.write(new Gson().toJson(error));
            writer.flush();
            writer.close();
        }

    }
}
