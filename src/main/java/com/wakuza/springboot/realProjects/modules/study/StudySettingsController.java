package com.wakuza.springboot.realProjects.modules.study;


import com.wakuza.springboot.realProjects.modules.account.CurrentUser;
import com.wakuza.springboot.realProjects.modules.domain.Account;
import com.wakuza.springboot.realProjects.modules.domain.Study;
import com.wakuza.springboot.realProjects.modules.settings.form.Profile;
import com.wakuza.springboot.realProjects.modules.study.validator.StudyFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.wakuza.springboot.realProjects.modules.settings.SettingsController.*;


@RequiredArgsConstructor
@Controller
@RequestMapping("/study/{Path}/settings")
public class StudySettingsController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;

    @GetMapping("/description")
    public String viewStudySetting(@CurrentUser Account account, @PathVariable String path, Model model){
        Study study = studyService.getStudyToUpdate(account,path);`
        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute(modelMapper.map(study,StudyDescriptionForm.class));
        return "study/settings/description";
    }

    @PostMapping("/description")
    public String updateStudyInfo(@CurrentUser Account account, @PathVariable String path,
                                  @Valid StudyDescriptionForm studyDescriptionForm, Model model, Errors errors,
                                  RedirectAttributes attributes){
        Study study = studyService.getStudyToUpdate(account, path);
        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(study);
            return "/study/settings/description";
        }
        studyService.updateStudyDescription(study,studyDescriptionForm);
        attributes.addFlashAttribute("massage","스터디 소개를 수정하였습니다.");
        return "redirect:/study/" + getPath(path) + "/settings/description";



    }

}
