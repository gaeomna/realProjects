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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/gaeomna")
    public String gaeomna(){return "gaeomna";}
}
