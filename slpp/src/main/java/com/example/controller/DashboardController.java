package com.example.controller;

import com.example.form.PetitionForm;
import com.example.model.Petition;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.service.PetitionService;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PetitionService petitionService;
    private final UserService userService;

    @Autowired
    public DashboardController(PetitionService petitionService, UserService userService) {
        this.petitionService = petitionService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }

        if (user.getRole() == UserRole.COMMITTEE) {
            return "redirect:/committee/dashboard";
        }

        model.addAttribute("petitions", petitionService.findAll());
        model.addAttribute("user", user);
        model.addAttribute("petitionForm", new PetitionForm());
        return "dashboard/petitioner";
    }

    @PostMapping("/petition/create")
    public String createPetition(@Valid @ModelAttribute PetitionForm form,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        try {
            Petition petition = new Petition();
            petition.setTitle(form.getTitle());
            petition.setContent(form.getContent());
            petitionService.createPetition(petition, user);
            redirectAttributes.addFlashAttribute("successMessage", "Petition created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create petition: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/petition/{id}/sign")
    public String signPetition(@PathVariable Long id,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes redirectAttributes) {
        try {
            petitionService.signPetition(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Petition signed successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to sign petition: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}