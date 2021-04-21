package com.wakuza.springboot.realProjects.modules.event;

import com.wakuza.springboot.realProjects.modules.domain.Event;
import com.wakuza.springboot.realProjects.modules.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event,Long> {
    @EntityGraph(value = "Event.withEnrollments",type = EntityGraph.EntityGraphType.LOAD)
    List<Event> findByStudyOrderByStartDateTime(Study study);
}
