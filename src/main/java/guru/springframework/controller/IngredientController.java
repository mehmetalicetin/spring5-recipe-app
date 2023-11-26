package guru.springframework.controller;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {
	private final RecipeService recipeService;
	private final IngredientService ingredientService;

	private final UnitOfMeasurementService unitOfMeasurementService;


	public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasurementService unitOfMeasurementService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasurementService = unitOfMeasurementService;
	}

	@GetMapping
	@RequestMapping({"/recipe/{id}/ingredients"})
	public String listIngredients(@PathVariable String id, Model model){
		log.debug("Getting Ingredients for recipe id:{}",id);
		model.addAttribute("recipe", recipeService.findCommandById(Long.parseLong(id)));
		return "/recipe/ingredient/list";
	}

	@GetMapping
	@RequestMapping({"/recipe/{id}/ingredient/{ingredientId}/show"})
	public String findIngredient(@PathVariable String id, @PathVariable String ingredientId, Model model){
		log.debug("Getting Ingredients for recipe id:{}",id);
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(id), Long.parseLong(ingredientId)));
		return "/recipe/ingredient/show";
	}

	@GetMapping
	@RequestMapping({"/recipe/{recipeId}/ingredient/{ingredientId}/update"})
	public String updateRecipeIngredient(@PathVariable  String recipeId, @PathVariable String ingredientId, Model model){
		log.debug("Getting Ingredients for recipe id:{} for update", recipeId);
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(recipeId), Long.parseLong(ingredientId)));
		model.addAttribute("uomList", unitOfMeasurementService.listAllUOMs());
		return "/recipe/ingredient/ingredientForm";
	}

	@PostMapping("recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command){
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

		log.debug("saved receipe id:" + savedCommand.getRecipeId());
		log.debug("saved ingredient id:" + savedCommand.getId());

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}

	@GetMapping("recipe/{recipeId}/ingredient/new")
	public String newRecipe(@PathVariable String recipeId, Model model){
		//make sure we have a good id value
		RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));
		//todo: raise exception if null

		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(Long.parseLong(recipeId));
		model.addAttribute("ingredient", ingredientCommand);

		ingredientCommand.setUom(new UnitOfMeasureCommand());

		model.addAttribute("uomList", unitOfMeasurementService.listAllUOMs());

		return "/recipe/ingredient/ingredientForm";
	}

	@GetMapping
	@RequestMapping("/recipe/{recipeId}/ingredient/{id}/delete")
	public String deleteById(@PathVariable String recipeId, @PathVariable String id){
		log.debug("deleting ingredient id:"+id);
		ingredientService.deleteById(Long.valueOf(recipeId), Long.parseLong(id));
		return "redirect:/recipe/"+recipeId+"/ingredients";
	}
}
