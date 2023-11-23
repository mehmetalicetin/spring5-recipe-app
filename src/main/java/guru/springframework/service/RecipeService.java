package guru.springframework.service;

import guru.springframework.domain.Recipe;

import java.util.Set;

public interface RecipeService {
	Set<Recipe> geRecipes();

	Recipe findById(Long id);
}
