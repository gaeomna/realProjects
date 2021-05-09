package com.wakuza.springboot.realProjects.modules.event;

import com.wakuza.springboot.realProjects.modules.account.Account;
import com.wakuza.springboot.realProjects.modules.account.UserAccount;
import com.wakuza.springboot.realProjects.modules.study.Study;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@NamedEntityGraph(
        name = "Event.withEnrollments",
        attributeNodes = @NamedAttributeNode("enrollments")
)
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    public Study study;

    @ManyToOne
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime; // 만든 시간

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime; // 접수 종료 시간

    @Column(nullable = false)
    private LocalDateTime startDateTime; //모임 시작 일시

    @Column(nullable = false)
    private LocalDateTime endDateTime; //모임 종료 일시

    private Integer limitOfEnrollments;

    @OneToMany(mappedBy = "event")
    @OrderBy("enrolledAt")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING) //enum을 mapping 할때는 항상 @Enumerated을 항상 줘야함
    private EventType eventType;

    public boolean isEnrollableFor(UserAccount userAccount){
        return isNotClosed() && !isAttended(userAccount) &&!isAlreadyEnrolled(userAccount);
    }
    public boolean isDisenrollableFor(UserAccount userAccount){
        return isNotClosed() && !isAttended(userAccount) && isAlreadyEnrolled(userAccount);
    }

    private boolean isNotClosed(){
        return this.endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    public boolean isAttended(UserAccount userAccount){
        Account account = userAccount.getAccount();
        for(Enrollment e : this.enrollments){
            if(e.getAccount().equals(account) && e.isAccepted()){
                return true;
            }
        }
        return false;
    }

    public int numberOfRemainSpots(){
        return this.limitOfEnrollments - (int) this.enrollments.stream().filter(Enrollment::isAccepted).count();
    }

    private boolean isAlreadyEnrolled(UserAccount userAccount){
        Account account = userAccount.getAccount();
        for(Enrollment e : this.enrollments){
            if(e.getAccount().equals(account)){
                return true;
            }
        }
        return false;
    }

    public Long getNumberOfAcceptedEnrollments() {
        return this.enrollments.stream().filter(Enrollment::isAccepted).count();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setEvent(this);
    }

    public void removeEnrollment(Enrollment enrollment){
        this.enrollments.remove(enrollment);
        enrollment.setEvent(this);
    }

    public boolean isAbleToAcceptWaitingEnrollment() {
        return this.eventType == EventType.FCFS && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments();
    }


    private Enrollment getTheFirstWaitingEnrollment() {
        for(Enrollment e : this.enrollments){
            if(!e.isAccepted()){
                return e;
            }
        }
        return null;
    }

    public void acceptNextWaitingEnrollment() {
        if(this.isAbleToAcceptWaitingEnrollment()){
            Enrollment enrollmentToAccept = this.getTheFirstWaitingEnrollment();
            if(enrollmentToAccept != null){
                enrollmentToAccept.setAccepted(true);
            }
        }

    }

    public boolean canAccept(Enrollment enrollment){
        return this.eventType == EventType.CONFIRMATIVE
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended()
                && !enrollment.isAccepted();
    }

    public boolean canReject(Enrollment enrollment){
        return this.eventType == EventType.CONFIRMATIVE
                && this.enrollments.contains(enrollment)
                &&!enrollment.isAttended()
                && enrollment.isAccepted();
    }



    private List<Enrollment> getWaitingList(){
        return this.enrollments.stream().filter(enrollment -> !enrollment.isAccepted()).collect(Collectors.toList());
    }

    public void acceptWaitingList() {
        if(this.isAbleToAcceptWaitingEnrollment()){
            var waitingList = getWaitingList();
            int numberToAccept = (int) Math.min(this.limitOfEnrollments - this.getNumberOfAcceptedEnrollments(),waitingList.size());
            waitingList.subList(0,numberToAccept).forEach(e -> e.setAccepted(true));
        }
    }

    public void accept(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE
                && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()) {
            enrollment.setAccepted(true);
        }
    }

    public void reject(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE) {
            enrollment.setAccepted(false);
        }
    }


}