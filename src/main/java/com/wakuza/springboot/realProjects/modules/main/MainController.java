package com.wakuza.springboot.realProjects.modules.main;


import com.wakuza.springboot.realProjects.modules.account.Account;
import com.wakuza.springboot.realProjects.modules.account.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if(account != null){
            model.addAttribute(account);
        }

        return "index";
    }
}