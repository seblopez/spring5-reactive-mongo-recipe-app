package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    IngredientService ingredientService;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        this.unitOfMeasureCommandToUnitOfMeasure = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient,
                recipeReactiveRepository, recipeRepository, unitOfMeasureCommandToUnitOfMeasure, unitOfMeasureService);
    }

    @Test
    public void findByRecipeIdAndId() throws Exception {
    }

    @Test
    public void findByRecipeIdAndReceipeIdHappyPath() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("1");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3").block();

        //when
        assertEquals("3", ingredientCommand.getId());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
    }


    @Test
    public void testSaveNewIngredientRecipeCommand() {
        //given
        final UnitOfMeasureCommand each = UnitOfMeasureCommand.builder()
                .id("232")
                .description("Each")
                .build();

        IngredientCommand command = IngredientCommand.builder()
                .recipeId("2")
                .description("Avocado")
                .amount(BigDecimal.ONE)
                .uom(each)
                .build();

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(Ingredient.builder()
                .description("Avocado")
                .amount(BigDecimal.ONE)
                .uom(UnitOfMeasure.builder()
                        .id("232")
                        .description("Each")
                        .build())
                .build());

        savedRecipe.getIngredients().iterator().next();

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(Recipe.builder().build()));
        when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(savedRecipe));
        when(unitOfMeasureService.findById(anyString())).thenReturn(Mono.just(each));

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        //then
        assertNotNull(savedCommand.getId());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));
        verify(unitOfMeasureService).findById(anyString());

    }

    @Test
    public void testSaveExistingIngredientRecipeCommand() {
        //given
        final UnitOfMeasureCommand each = UnitOfMeasureCommand.builder()
                .id("232")
                .description("Each")
                .build();

        final Recipe recipeToUpdate = Recipe.builder()
                .id("2323-ajf343dsd")
                .description("Pionono Kani Kama")
                .build();

        recipeToUpdate.addIngredient(Ingredient.builder()
                .id("34dsd3-2332-2243-21fdfs")
                .description("Avocado")
                .amount(BigDecimal.ONE)
                .uom(UnitOfMeasure.builder()
                        .id("232")
                        .description("Each")
                        .build())
                .build());

        IngredientCommand command = IngredientCommand.builder()
                .id("34dsd3-2332-2243-21fdfs")
                .recipeId("2323-ajf343dsd")
                .description("Avocado")
                .amount(BigDecimal.valueOf(2))
                .uom(each)
                .build();

        Recipe savedRecipe = Recipe.builder()
                .id("2323-ajf343dsd")
                .description("Pionono Kani Kama")
                .build();

        savedRecipe.addIngredient(Ingredient.builder()
                .id("34dsd3-2332-2243-21fdfs")
                .description("Avocado")
                .amount(BigDecimal.valueOf(2))
                .uom(UnitOfMeasure.builder()
                        .id("232")
                        .description("Each")
                        .build())
                .build());

        savedRecipe.getIngredients().iterator().next();

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipeToUpdate));
        when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(savedRecipe));
        when(unitOfMeasureService.findById(anyString())).thenReturn(Mono.just(each));

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        //then
        assertEquals("34dsd3-2332-2243-21fdfs", savedCommand.getId());
        assertEquals(BigDecimal.valueOf(2), savedCommand.getAmount());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));
        verify(unitOfMeasureService).findById(anyString());

    }

    @Test
    public void testDeleteById() throws Exception {
        //given
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        //when
        ingredientService.deleteById("1", "3");

        //then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}
