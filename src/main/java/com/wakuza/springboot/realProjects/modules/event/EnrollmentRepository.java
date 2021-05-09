package com.wakuza.springboot.realProjects.modules.event;

import com.wakuza.springboot.realProjects.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    Enrollment findByEventAndAccount(Event event, Account account);

    boolean existsByEventAndAccount(Event event, Account account);
}
