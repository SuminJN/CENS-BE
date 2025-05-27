package com.handong.cens.member.dto;

import com.handong.cens.member.domain.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberDto extends User {

    private String loginId;
    private String email;
    private String name;
    private MemberRole role;

    public MemberDto(String loginId, String email, String name, MemberRole role) {
        super(email, "N/A", List.of(() -> "ROLE_" + role.name()));

        this.loginId = loginId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // JWT 생성 시 사용
    public Map<String, Object> getClaim() {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("loginId", loginId);
        dataMap.put("email", email);
        dataMap.put("name", name);
        dataMap.put("role", role.name());

        return dataMap;
    }
}
