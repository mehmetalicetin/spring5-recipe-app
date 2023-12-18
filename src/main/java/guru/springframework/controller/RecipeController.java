package guru.springframework.controller;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {
	private final RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping
	@RequestMapping({"/recipe/{id}/show"})
	public String showById(@PathVariable String id, Model model){
		model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
		return "recipe/show";
	}

	@GetMapping
	@RequestMapping({"/recipe/new"})
	public String newRecipe(Model model){
		model.addAttribute("recipe", new Recipe());
		return "recipe/recipeForm";
	}

	@PostMapping
	@RequestMapping(name = "recipe", method = RequestMethod.POST)
	public String saveOrUpdate(@ModelAttribute RecipeCommand command){
		RecipeCommand savedCommond = recipeService.saveRecipeCommand(command);
		return "redirect:/recipe/"+savedCommond.getId()+"/show";
	}

	@GetMapping
	@RequestMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model){
		model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
		return "recipe/recipeForm";
	}

	@GetMapping
	@RequestMapping("/recipe/{id}/delete")
	public String deleteById(@PathVariable String id){
		recipeService.deleteById(Long.valueOf(id));
		return "redirect:/";
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(Exception exception){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("404Error");
		modelAndView.addObject("exception", exception);
		return modelAndView;
	}
}
