package com.wakuza.springboot.realProjects.modules.event;

import com.wakuza.springboot.realProjects.modules.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event,Long> {

}
