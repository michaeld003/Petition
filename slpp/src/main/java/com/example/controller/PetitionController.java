package com.example.controller;



import com.example.model.Petition;
import com.example.model.User;
import com.example.service.PetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/petition")
public class PetitionController {

    private final PetitionService petitionService;

    @Autowired
    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @GetMapping("/new")
    public String showCreatePetitionForm(Model model) {
        model.addAttribute("petition", new Petition());
        return "petition/create";
    }

    @PostMapping("/create")
    public String createPetition(@ModelAttribute Petition petition,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        try {
            petitionService.createPetition(petition, user);
            redirectAttributes.addFlashAttribute("successMessage", "Petition created successfully");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/petition/new";
        }
    }

    @PostMapping("/{id}/sign")
    public String signPetition(@PathVariable Long id,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes redirectAttributes) {
        try {
            petitionService.signPetition(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Petition signed successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String viewPetition(@PathVariable Long id, Model model) {
        model.addAttribute("petition", petitionService.findById(id));
        return "petition/view";
    }
}