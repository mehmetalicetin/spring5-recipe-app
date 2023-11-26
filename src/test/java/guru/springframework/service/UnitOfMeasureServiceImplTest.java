package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {
	UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
	UnitOfMeasurementService service;

	@Mock
	UnitOfMeasureRepository repository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new UnitOfMeasurementServiceImpl(repository, unitOfMeasureToUnitOfMeasureCommand);
	}

	@Test
	public void listAllUoms(){
		Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		unitOfMeasure.setId(1L);
		unitOfMeasures.add(unitOfMeasure);
		UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
		unitOfMeasure2.setId(2L);
		unitOfMeasures.add(unitOfMeasure2);

		when(repository.findAll()).thenReturn(unitOfMeasures);

		Set<UnitOfMeasureCommand> unitOfMeasureCommands = service.listAllUOMs();

		assertEquals(2, unitOfMeasureCommands.size());
		verify(repository, times(1)).findAll();
	}
}
