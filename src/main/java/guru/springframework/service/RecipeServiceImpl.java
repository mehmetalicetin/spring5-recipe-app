package guru.springframework.service;

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
	private final RecipeRepository recipeRepositories;

	public RecipeServiceImpl(RecipeRepository recipeRepositories) {
		this.recipeRepositories = recipeRepositories;
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
}
