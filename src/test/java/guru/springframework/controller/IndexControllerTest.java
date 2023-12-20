package guru.springframework.controller;

import guru.springframework.domain.Recipe;
import guru.springframework.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class IndexControllerTest {
	private IndexController indexController;

	@Mock
	RecipeService recipeService;

	@Mock
	Model model;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		indexController = new IndexController(recipeService);
	}

	@Test
	public void testMockMvc() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("index"));
	}

	@Test
	public void getIndexPage(){
		//given
		Set<Recipe> recipes = new HashSet<>();
		recipes.add(new Recipe());
		recipes.add(new Recipe());
		recipes.add(new Recipe());

		Mockito.when(recipeService.geRecipes()).thenReturn(recipes);
		ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
		indexController.getIndexPage(model);
		Mockito.verify(model).addAttribute(Mockito.eq("recipes"), argumentCaptor.capture());

/*		Set<Recipe> setInController = argumentCaptor.getValue();
		assertEquals(3, setInController.size());*/


		String viewName = indexController.getIndexPage(model);
		indexController.getIndexPage(model);
		indexController.getIndexPage(model);
		indexController.getIndexPage(model);
		assertEquals("index", viewName);
		Mockito.verify(recipeService, Mockito.times(5)).geRecipes();
		Mockito.verify(model, Mockito.times(5))
				.addAttribute(Mockito.eq("recipes"), Mockito.anySet());
	}

	public class Article{
		private List<Author> listOfAuthors;
		private int id;
		private String name;

		public Article(String name) {
			this.name = name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public class Author{
		private String name;
	}

	public void givenAStreamOfArticles_whenProcessedInSequentiallyWithSpliterator_ProducessRightOutput() {
		List<Article> articles = Stream.generate(() -> new Article("Java"))
				.limit(35000)
				.collect(Collectors.toList());

		Spliterator<Article> spliterator = articles.spliterator();
		while (spliterator.tryAdvance(article -> article.setName(article.getName()
				.concat("- published by Baeldung"))));

		articles.forEach(article -> assertEquals(article.getName(),"Java- published by Baeldung"));
	}

	@Test
	public void givenAStreamOfArticle_whenProcessedUsingTrySplit_thenSplitIntoEqualHalf() {
		List<Article> articles = Stream.generate(() -> new Article("Java"))
				.limit(35000)
				.collect(Collectors.toList());

		Spliterator<Article> split1 = articles.spliterator();

		Spliterator<Article> split2 = split1.trySplit();

		List<Article> articlesListOne = new ArrayList<>();
		List<Article> articlesListTwo = new ArrayList<>();

		split1.forEachRemaining(articlesListOne::add);
		split2.forEachRemaining(articlesListTwo::add);

		assertEquals(articlesListOne.size(), 17500);
		assertEquals(articlesListTwo.size(),17500);

	}
}