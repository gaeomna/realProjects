package com.wakuza.springboot.realProjects.modules.account;


import com.wakuza.springboot.realProjects.modules.domain.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User { //spring security가 다루는 user정보와 domain에서 다루는 user정보의 일종의 어댑터

    private Account account; //domain에서 가지고 있는 user정보

    public UserAccount(Account account) {
        super(account.getNickname(),account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }

}
