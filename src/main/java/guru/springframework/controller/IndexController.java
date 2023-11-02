package guru.springframework.controller;

import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {
	private final RecipeService recipeService;

	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping("/")
	public String getIndexPage(Model model){
		log.debug("Getting index page");
		model.addAttribute("recipes", recipeService.geRecipes());
		//throw new RuntimeException("aaaaa fatal error :)");
		return "index";
	}
}
