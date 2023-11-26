package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {
	@InjectMocks
	IngredientServiceImpl ingredientService;

	@Mock
	IngredientRepository ingredientRepository;

	@Mock
	RecipeRepository recipeRepository;

	@Mock
	IngredientCommandToIngredient ingredientCommandToIngredient;

	@Mock
	IngredientToIngredientCommand ingredientToIngredientCommand;

	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getIngredientByIdTest(){
		Ingredient ingredient = new Ingredient();
		ingredient.setId(1L);
		Optional<Ingredient> optionalIngredient = Optional.of(ingredient);
		when(ingredientRepository.findById(anyLong())).thenReturn(optionalIngredient);

		Ingredient ingredientReturned = ingredientService.findById(1L);
		assertNotNull("Null ingredient returned", ingredientReturned);
		verify(ingredientRepository, times(1)).findById(anyLong());
		verify(ingredientRepository, never()).findAll();
	}

	@Test
	public void getIngredients() {
		Ingredient ingredient = new Ingredient();
		Set<Ingredient> ingredients = new HashSet<>();
		ingredients.add(ingredient);

		when(ingredientRepository.findAll()).thenReturn(ingredients);

		Set<Ingredient> ingredientSet = ingredientService.getIngredients();
		assertEquals(1L, ingredientSet.size());

		verify(ingredientRepository, times(1)).findAll();
	}

	@Test
	public void findByRecipeIdAndIngredientIdHappyPath(){
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId(1L);

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId(1L);

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId(3L);

		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);


		Optional<Recipe> optionalRecipe = Optional.of(recipe);

		when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);
		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

		assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
		assertEquals(Long.valueOf(3L), ingredientCommand.getId());

		verify(recipeRepository, times(1)).findById(anyLong());

	}

	@Test
	public void savedRecipe(){
		IngredientCommand command = new IngredientCommand();
		command.setId(1L);
		command.setRecipeId(1L);

		Optional<Recipe> optionalRecipe = Optional.of(new Recipe());

		Recipe savedRecipe = new Recipe();
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId(1L);


		when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);
		when(recipeRepository.save(any())).thenReturn(savedRecipe);

		IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(command);
		assertEquals(Long.valueOf(1), savedIngredientCommand.getId());

		verify(recipeRepository, times(1)).findById(anyLong());
		verify(recipeRepository, times(1)).save(any(Recipe.class));

	}

	@Test
	public void testDeleteById(){
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		Ingredient ingredient = new Ingredient();
		ingredient.setId(3L);
		recipe.addIngredient(ingredient);
		ingredient.setRecipe(recipe);
		Optional<Recipe> optionalRecipe = Optional.of(recipe);

		when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

		ingredientService.deleteById(1L, 3L);

		verify(recipeRepository, times(1)).findById(anyLong());
		verify(recipeRepository, times(1)).save(any(Recipe.class));
	}
}