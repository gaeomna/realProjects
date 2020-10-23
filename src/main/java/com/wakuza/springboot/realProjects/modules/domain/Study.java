package com.wakuza.springboot.realProjects.modules.domain;


import com.wakuza.springboot.realProjects.modules.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@NamedEntityGraph(name = "Study.withAll",attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")})
@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Study {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER) //text타입으로 지정해야함(varchar255으론 적음),study 정보를 조회할때 eager는 무조건 가져오도록
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER) //text타입으로 지정해야함(varchar255으론 적음),study 정보를 조회할때 eager는 무조건 가져오도록
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime; //스터디를 공개한 시간

    private LocalDateTime closedDateTime; //종료한 시간

    private LocalDateTime recruitingUpdatedDateTime; // 인원 모집 on/off 제한 시간

    private boolean recruiting; //모집중인지 여부

    private boolean published; //공개 여부

    private boolean closed; //종료 여부

    private boolean useBanner; //배너 사용 여부


    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount){
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting()
                && !this.members.contains(account) && !this.managers.contains(account);
    } //

    public boolean isMember(UserAccount userAccount){
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount){
        return this.managers.contains(userAccount.getAccount());
    }
}
