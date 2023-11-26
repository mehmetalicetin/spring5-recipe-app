package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService{
	private final RecipeRepository recipeRepository;
	private final IngredientRepository          ingredientRepository;
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final UnitOfMeasureRepository unitOfMeasureRepository;


	public Set<Ingredient> getIngredients() {
		log.debug("I'm in the service");
		Set<Ingredient> ingredients = new HashSet<>();
		ingredientRepository.findAll().forEach(ingredients::add);
		return ingredients;
	}

	@Override
	public Ingredient findById(Long id) {
		Optional<Ingredient> ingredient = ingredientRepository.findById(id);
		if(!ingredient.isPresent()){
			throw new RuntimeException("Ingredient cannot find by id:"+id);
		}
		return ingredient.get();
	}

	@Override
	public IngredientCommand findCommandById(Long l) {
		return ingredientToIngredientCommand.convert(findById(l));
	}


/*	@Override
	public IngredientCommand saveIngredientCommand(IngredientCommand command) {
		Ingredient ingredient = ingredientCommandToIngredient.convert(command);
		Ingredient savedIngredient = ingredientRepository.save(ingredient);
		return ingredientToIngredientCommand.convert(savedIngredient);
	}*/

	@Override
	public void deleteById(Long idToDelete) {
		ingredientRepository.deleteById(idToDelete);
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId){
		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
		if(!optionalRecipe.isPresent())
			throw new RuntimeException("Recipe cannot find");

		Recipe recipe = optionalRecipe.get();

		Set<Ingredient> ingredients = recipe.getIngredients();
		Optional<IngredientCommand> optionalIngredientCommand = Optional.empty();
		for (Ingredient e : ingredients) {
			if (Objects.equals(e.getId(), ingredientId)) {
				IngredientCommand convert = ingredientToIngredientCommand.convert(e);
				optionalIngredientCommand = Optional.of(convert);
				break;
			}
		}
		if(!optionalIngredientCommand.isPresent()){
			throw new RuntimeException("Ingredient cannot find");
		}
		log.debug("optionalIngredient -> {}", optionalIngredientCommand);
		return optionalIngredientCommand.get();
	}

	@Override
	public void deleteById(Long recipeId, Long ingredientId) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
		if(!optionalRecipe.isPresent())
			throw new RuntimeException("Recipe cannot find");

		Recipe recipe = optionalRecipe.get();
		Set<Ingredient> ingredients = recipe.getIngredients();
		Ingredient ingredient = ingredients.stream().filter(e->e.getId().equals(ingredientId)).findAny().orElseThrow();

		ingredient.setRecipe(null);
		ingredients.remove(ingredient);

		recipeRepository.save(recipe);
	}

	@Transactional
	@Override
	public IngredientCommand saveIngredientCommand(IngredientCommand command){
		Optional<Recipe> optionalRecipe = recipeRepository.findById(command.getRecipeId());
		if(optionalRecipe.isEmpty()){
			log.error("Recipe cannot find");
			return new IngredientCommand();
		}

		Recipe recipe = optionalRecipe.get();
		Optional<Ingredient> optionalIngredient =
				recipe.getIngredients()
						.stream()
						.filter(ingredient -> ingredient.getId().equals(command.getId()))
						.findFirst();

		if(optionalIngredient.isPresent()){
			Ingredient ingredientFound = optionalIngredient.get();
			ingredientFound.setAmount(command.getAmount());
			ingredientFound.setDescription(command.getDescription());
			ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
					.orElseThrow());
		} else {
			recipe.addIngredient(Objects.requireNonNull(ingredientCommandToIngredient.convert(command)));
		}

		Recipe savedRecipe = recipeRepository.save(recipe);

		return ingredientToIngredientCommand.convert(
				savedRecipe.getIngredients()
						.stream()
						.filter(e->e.getDescription().equals(command.getDescription()))
						.filter(e->e.getAmount().equals(command.getAmount()))
						.filter(e->e.getUom().getId().equals(command.getUom().getId()))
						.findFirst()
						.orElseThrow());
	}


}
