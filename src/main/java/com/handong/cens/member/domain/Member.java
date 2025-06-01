package com.handong.cens.member.domain;

import com.handong.cens.commons.entity.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Setter
    @ElementCollection
    @CollectionTable(name = "member_category", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING) // Category가 enum일 경우
    @Column(name = "category")
    private List<Category> categories;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    @Column(unique = true)
    private String providerId;

    // 엔티티 저장 전 기본 권한 설정
    @PrePersist
    public void setDefaultRole() {
        if (this.role == null) {
            this.role = MemberRole.USER;
        }
    }
}