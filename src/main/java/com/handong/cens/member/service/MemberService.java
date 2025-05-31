package com.handong.cens.member.service;

import com.handong.cens.commons.entity.Category;
import com.handong.cens.member.domain.Member;
import com.handong.cens.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateCategories(Long memberId, List<Integer> categoryCodes) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        List<Category> categoryList = categoryCodes.stream()
                .map(Category::fromCode)
                .toList();

        member.setCategories(categoryList);
    }

    public List<String> getCategories(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        return member.getCategories().stream()
                .map(Category::getDescription)
                .toList();
    }
}
