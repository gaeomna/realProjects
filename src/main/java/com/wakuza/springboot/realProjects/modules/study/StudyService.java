package com.wakuza.springboot.realProjects.modules.study;


import com.wakuza.springboot.realProjects.modules.domain.Account;
import com.wakuza.springboot.realProjects.modules.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createNewStudy(Study study, Account account){
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);
        return newStudy;
    }

}
