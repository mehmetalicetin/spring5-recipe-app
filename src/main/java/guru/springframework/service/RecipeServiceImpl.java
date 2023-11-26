package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{
	private final RecipeRepository      recipeRepositories;
	private final RecipeCommandToRecipe recipeCommandToRecipe;

	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeRepository recipeRepositories, RecipeCommandToRecipe recipeCommandToRecipe,
			RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeRepositories = recipeRepositories;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Set<Recipe> geRecipes() {
		log.debug("I'm in the service");
		Set<Recipe> recipes = new HashSet<>();
		recipeRepositories.findAll().forEach(recipes::add);
		return recipes;
	}

	@Override
	public Recipe findById(Long id) {
		Optional<Recipe> recipe = recipeRepositories.findById(id);
		if(!recipe.isPresent()){
			throw new RuntimeException("Recipe cannot find by id"+id);
		}
		return recipe.get();
	}

	@Override
	public RecipeCommand findCommandById(Long l) {
		return recipeToRecipeCommand.convert(findById(l));
	}

	@Override
	public RecipeCommand saveRecipeCommand(RecipeCommand command) {
		Recipe recipe = recipeCommandToRecipe.convert(command);
		Recipe savedRecipe = recipeRepositories.save(recipe);
		return recipeToRecipeCommand.convert(savedRecipe);
	}

	@Override
	public void deleteById(Long idToDelete) {
		recipeRepositories.deleteById(idToDelete);
	}
}
