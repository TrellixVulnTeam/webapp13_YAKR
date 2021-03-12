package es.webapp13.porthub.controller;

import es.webapp13.porthub.repository.PortfolioItemRepository;
import es.webapp13.porthub.Repository.TemplateRepository;
import es.webapp13.porthub.Repository.UserRepository;
import es.webapp13.porthub.model.PortfolioItem;
import es.webapp13.porthub.model.Template;
import es.webapp13.porthub.model.User;
import es.webapp13.porthub.service.PortfolioItemService;
import es.webapp13.porthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfigController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private PortfolioItemService portfolioItemService;

    @Autowired
    private PortfolioItemRepository portfolioItemRepository;


    @GetMapping("/student-edit-account")
    public String studentEditAccountLink(Model model) {
        model.addAttribute("active_main", true);
        return "student-edit-account";
    }

    @GetMapping("/student-edit-account-notifications")
    public String studentEditAccountNotificationsLink(Model model) {
        model.addAttribute("active_notifications", true);
        model.addAttribute("portfolioItems", portfolioItemService.getPortfolioItems("id"));
        return "student-edit-account-notifications";
    }


    @PostMapping("/student-edit-account-notifications")
    public String studentEditAccountNotificationsForm(Model model, PortfolioItem portfolioItem) {
        model.addAttribute("active_notifications", true);
        portfolioItemService.addPortfolioItem("id", portfolioItem);
        model.addAttribute("portfolioItems", portfolioItemService.getPortfolioItems("id"));
        return "student-edit-account-notifications";
    }

    @GetMapping("/deleted-portfolio-item")
    public String portfolioItemDeleteLink() {
        portfolioItemService.deletePortfolioItem("id", 3);
        return "student-edit-account-notifications";
    }

    @GetMapping("/settings-edit-account-edit-portfolioitem-{userId}-{id}")
    public String portfolioItemEditLink(Model model,@PathVariable long id, @PathVariable String userId) {
        model.addAttribute("portfolioItem",portfolioItemService.getPortfolioItem(userId,id));
        return "settings-edit-account-edit-portfolioitem";
    }

    @PostMapping("/settings-edit-account-edit-portfolioitem-{userId}-{id}")
    public String portfolioItemEditForm(Model model,@PathVariable long id, @PathVariable String userId,PortfolioItem newPortfolioItem) {
        portfolioItemService.updatePortfolioItem(newPortfolioItem,id);
        return "portfolioitem-update-confirmation";
    }

    @GetMapping("/student-edit-account-password")
    public String studentEditAccountPasswordLink(Model model) {
        model.addAttribute("active_password", true);
        return "student-edit-account-password";
    }

    @GetMapping("/student-edit-account-profile")
    public String studentEditAccountProfileLink(Model model) {
        model.addAttribute("active_profile", true);
        return "student-edit-account-profile";
    }

    @GetMapping("/my-templates")
    public String userTemplatesLink(Model model){
        model.addAttribute("templates", userService.getTemplates());
        return "my-templates";
    }

    @GetMapping("/set-active-template")
    public String activeTemplateLink(Model model, @RequestParam String name){
        Template activeTemplate = templateRepository.findFirstByName(name);
        User user = userService.getActiveUser();
        user.setActiveTemplate(activeTemplate);
        userRepository.save(user);
        return "change-active-template-confirmation";
    }

}
