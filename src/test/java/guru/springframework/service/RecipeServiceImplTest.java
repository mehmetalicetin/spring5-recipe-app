package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {
	RecipeServiceImpl recipeService;

	@Mock
	RecipeRepository recipeRepositories;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeServiceImpl(recipeRepositories);
	}

	public void getRecipeByIdTest(){
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		Optional<Recipe> recipeOptional = Optional.of(recipe);
		when(recipeRepositories.findById(anyLong())).thenReturn(recipeOptional);

		Recipe recipeReturned = recipeService.findById(1L);
		assertNotNull("Null recipe returned", recipeReturned);
		verify(recipeRepositories, times(1)).findById(anyLong());
		verify(recipeRepositories, never()).findAll();
	}

	@Test
	public void geRecipes() {
		Recipe recipe = new Recipe();
		Set<Recipe> recipeSet = new HashSet<>();
		recipeSet.add(recipe);

		when(recipeRepositories.findAll()).thenReturn(recipeSet);

		Set<Recipe> recipes = recipeService.geRecipes();
		assertEquals(1L, recipes.size());

		verify(recipeRepositories, times(1)).findAll();
	}
}