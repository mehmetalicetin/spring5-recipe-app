package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;

import java.util.Set;

public interface IngredientService {
	Set<Ingredient> getIngredients();

	Ingredient findById(Long id);

	IngredientCommand findCommandById(Long l);

	IngredientCommand saveIngredientCommand(IngredientCommand command);

	void deleteById(Long idToDelete);

	IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

	void deleteById(Long recipeId, Long ingredientId);
}
