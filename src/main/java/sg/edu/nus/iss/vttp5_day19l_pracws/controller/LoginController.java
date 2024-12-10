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

    // requestParam is for binding simple data types (e.g., String, int, boolean) based on the form field's name attribute.
    // When you declare a method parameter with @RequestParam (e.g., @RequestParam String username), Spring assumes that the name of the parameter (i.e., username) matches the name attribute of the HTML form field.
    
    // @PostMapping("/login")
    // public String login(@RequestParam String username, HttpSession session) {
    //     session.setAttribute("userKey", username);
    //     return "redirect:/todos/list";
    // }

    // When using @modelattribute, it is meant for binding entire objects
    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user, 
    BindingResult bindingResult, Model model,
    HttpSession session) {
        // if (bindingResult.hasErrors()) {
        //     return "login";
        // }

        if ((user.getAge() == null) || (user.getFullName() == "")) // when using leaves the field blank, it is "", not the same as null
        {
            return "redirect:/refused";
        }

        if (user.getAge() < 10) {

            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userAge", user.getAge());
            return "redirect:/underage";
        }

        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userAge", user.getAge());
        
        return "redirect:/todos/list";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/refused")
    public String showRefusedPage() {
        return "refused";
    }

    @GetMapping("/underage")
    public String showUnderagePage(HttpSession httpSession, Model model){

        // Get the user data from the session
        String userName = (String) httpSession.getAttribute("userName");
        Integer userAge = (Integer) httpSession.getAttribute("userAge");

        if (userAge != null)
        {
            int yearsTooYoung = 10 - userAge;
            model.addAttribute("yearsTooYoung", yearsTooYoung);
            model.addAttribute("userName", userName);
        }

        return "underage";
    }
    
    
}
