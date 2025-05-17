package com.handong.cens.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    MISSING_REFRESH_TOKEN(BAD_REQUEST, "refresh token이 존재하지 않습니다."),
    MISSING_CATEGORY_CODE(BAD_REQUEST, "존재하지 않는 카테고리 코드입니다."),

    /* 403 FORBIDDEN : 접근 권한 제한 */
    /* Valid : 유효한 */
    VALID_USER(FORBIDDEN, "해당 회원 정보에 접근 권한이 없습니다."),
    VALID_TOKEN(FORBIDDEN, "만료된 토큰입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(NOT_FOUND, "존재하지 않는 토큰입니다."),
    CRAWLING_PAGE_NOT_FOUND(NOT_FOUND, "크롤링할 페이지를 찾을 수 없습니다."),
    ARTICLE_CONTENT_NOT_FOUND(NOT_FOUND, "해당 기사의 본문 내용을 찾을 수 없습니다."),
    ARTICLE_DATE_NOT_FOUND(NOT_FOUND, "해당 기사의 날짜를 찾을 수 없습니다."),
    ARTICLE_NOT_FOUND(NOT_FOUND, "해당 기사를 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    /* DUPLICATE : (다른 무엇과) 똑같은 */

    /* 500 : Internal Server Error*/
    UNSUCCESSFUL_LOGIN(INTERNAL_SERVER_ERROR, "로그인에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}