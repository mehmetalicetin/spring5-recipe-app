package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;

import java.util.List;
import java.util.Set;

public interface UnitOfMeasurementService {
	Set<UnitOfMeasureCommand> listAllUOMs();
}
