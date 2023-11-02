package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoriesTest {
	@Autowired
	UnitOfMeasureRepositories unitOfMeasureRepositories;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@DirtiesContext
	public void findByDescription() {
		Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepositories.findByDescription("Teaspoon");
		assertEquals("Teaspoon", unitOfMeasure.get().getDescription());
	}

	@Test
	public void findByDescriptionCup() {
		Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepositories.findByDescription("Cup");
		assertEquals("Cup", unitOfMeasure.get().getDescription());
	}
}