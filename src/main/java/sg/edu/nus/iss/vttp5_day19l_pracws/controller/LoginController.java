package sg.edu.nus.iss.vttp5_day19l_pracws.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.Todo;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(Model model) 
    {
        model.addAttribute("user", new User());
        return "login";
    }

    // @PostMapping("/login")
    // public String login(@RequestParam String username, HttpSession session) {
    //     session.setAttribute("userKey", username);
    //     return "redirect:/todos/list";
    // }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
    }

    if (user.getAge() < 10) {
        return "redirect:/underage";
    }

    session.setAttribute("user", user);
    return "redirect:/todos/list";
}



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
