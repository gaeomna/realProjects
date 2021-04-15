package com.wakuza.springboot.realProjects.modules.domain;

import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Event {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    public Study study;

    @ManyToOne
    private Account createBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createDateTime; // 만든 시간

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime; // 접수 종료 시간

    @Column(nullable = false)
    private LocalDateTime startDateTime; //모임 시작 일시

    @Column(nullable = false)
    private LocalDateTime endDateTime; //모임 종료 일시

    private Integer limitOfEnrollments;

    @OneToMany(mappedBy = "event")
    private List<Enrollment> enrollments;

    @Enumerated(EnumType.STRING) //enum을 mapping 할때는 항상 @Enumerated을 항상 줘야함
    private EventType eventType;
}