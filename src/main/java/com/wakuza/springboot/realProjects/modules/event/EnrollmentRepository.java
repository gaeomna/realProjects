package com.wakuza.springboot.realProjects.modules.event;

import com.wakuza.springboot.realProjects.modules.domain.Account;
import com.wakuza.springboot.realProjects.modules.domain.Enrollment;
import com.wakuza.springboot.realProjects.modules.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    Enrollment findByEventAndAccount(Event event, Account account);

    boolean existsByEventAndAccount(Event event, Account account);
}
