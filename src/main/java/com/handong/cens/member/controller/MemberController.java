package com.handong.cens.member.controller;

import com.handong.cens.member.service.MemberService;
import com.handong.cens.oauth.entity.CustomOauth2UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "카테고리 조회",
            description = "회원의 관심 카테고리를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원의 관심 카테고리 목록",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = String.class)
                                    ),
                                    examples = @ExampleObject(
                                            name = "카테고리 리스트",
                                            summary = "관심 카테고리 예제",
                                            value = "[\"정치\", \"경제\", \"세계\"]"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories(@AuthenticationPrincipal CustomOauth2UserDetails user) {
        // Long memberId = user.getMember().getId();
        List<String> categoryCodes = memberService.getCategories(1L);
        return ResponseEntity.ok(categoryCodes);
    }

    @Operation(
            summary = "카테고리 업데이트",
            description = "회원의 관심 카테고리를 업데이트합니다.\n\n카테고리 코드:\n- 100: 정치\n- 101: 경제\n- 102: 사회\n- 103: 생활/문화\n- 104: 세계\n- 105: IT/과학",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "카테고리 코드 리스트",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "카테고리 예제",
                                    summary = "정치/경제 선택",
                                    value = "[100, 101]"
                            )
                    )
            )
    )
    @PostMapping("/categories")
    public ResponseEntity<Void> updateCategories(@RequestBody List<Integer> categoryCodes, @AuthenticationPrincipal CustomOauth2UserDetails user) {
//        Long memberId = user.getMember().getId();

        memberService.updateCategories(1L, categoryCodes);
        return ResponseEntity.ok().build();
    }

}
