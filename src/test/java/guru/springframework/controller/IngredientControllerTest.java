package guru.springframework.controller;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {
	@Mock
	RecipeService recipeService;

	@Mock
	IngredientService ingredientService;

	@Mock
	UnitOfMeasurementService unitOfMeasurementService;

	IngredientController controller;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new IngredientController(recipeService, ingredientService, unitOfMeasurementService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testListIngredients() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

		mockMvc.perform(get("/recipe/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(view().name("/recipe/ingredient/list"))
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyLong());
	}

	@Test
	public void testShowIngredient() throws Exception {
		IngredientCommand command = new IngredientCommand();

		when(ingredientService.findCommandById(anyLong())).thenReturn(command);

		mockMvc.perform(get("/recipe/1/ingredient/1/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("/recipe/ingredient/show"));
				//.andExpect(model().attributeExists("ingredient"));
	}

	@Test
	public void testSaveOrUpdate() throws Exception {
		IngredientCommand command = new IngredientCommand();
		command.setId(1L);
		command.setRecipeId(2L);

		when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

		mockMvc.perform(post("/recipe/2/ingredient")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("id", "")
						.param("description", "some string")
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredient/1/show"));
	}

	@Test
	public void testNewIngredientForm() throws Exception {
		//given
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(1L);

		//when
		when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
		when(unitOfMeasurementService.listAllUOMs()).thenReturn(new HashSet<>());

		mockMvc.perform(get("/recipe/1/ingredient/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("/recipe/ingredient/ingredientForm"))
				.andExpect(model().attributeExists("ingredient"))
				.andExpect(model().attributeExists("uomList"));

		verify(recipeService, times(1)).findCommandById(anyLong());
	}

	@Test
	public void testDeleteIngredient() throws Exception {
		mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredients"));

		verify(ingredientService, times(1)).deleteById(anyLong(), anyLong());
	}
}
