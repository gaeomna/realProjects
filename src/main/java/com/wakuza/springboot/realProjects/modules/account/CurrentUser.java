package com.wakuza.springboot.realProjects.modules.account;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 컴파일 이후에도 JVM에 의해서 참조가 가능합니다.
//@Retention(RetentionPolicy.CLASS) // 컴파일러가 클래스를 참조할 때까지 유효합니다.
//@Retention(RetentionPolicy.SOURCE) // 어노테이션 정보는 컴파일 이후 없어집니다.
@Target(ElementType.PARAMETER)
//@Target({
//        ElementType.PACKAGE, // 패키지 선언시
//        ElementType.TYPE, // 타입 선언시
//        ElementType.CONSTRUCTOR, // 생성자 선언시
//        ElementType.FIELD, // 멤버 변수 선언시
//        ElementType.METHOD, // 메소드 선언시
//        ElementType.ANNOTATION_TYPE, // 어노테이션 타입 선언시
//        ElementType.LOCAL_VARIABLE, // 지역 변수 선언시
//        ElementType.PARAMETER, // 매개 변수 선언시
//        ElementType.TYPE_PARAMETER, // 매개 변수 타입 선언시
//        ElementType.TYPE_USE // 타입 사용시
//})
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
