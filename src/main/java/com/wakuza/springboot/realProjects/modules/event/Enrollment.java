package com.wakuza.springboot.realProjects.modules.event;


import com.wakuza.springboot.realProjects.modules.account.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id @GeneratedValue
    private long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledAt; //가입 시점

    private boolean accepted; //수락했는지

    private boolean attended; //실제 참석했는지

}
