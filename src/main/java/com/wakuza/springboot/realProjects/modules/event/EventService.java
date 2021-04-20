package com.wakuza.springboot.realProjects.modules.event;

import com.wakuza.springboot.realProjects.modules.domain.Account;
import com.wakuza.springboot.realProjects.modules.domain.Event;
import com.wakuza.springboot.realProjects.modules.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event, Study study, Account account){
        event.setCreateBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        return eventRepository.save(event);
    }

}
