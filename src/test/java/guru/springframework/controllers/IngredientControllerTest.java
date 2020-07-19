package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebFluxTest(IngredientController.class)
@Import({ThymeleafAutoConfiguration.class})
public class IngredientControllerTest {

    @MockBean
    IngredientService ingredientService;

    @MockBean
    UnitOfMeasureService unitOfMeasureService;

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testListIngredients() {
        //given
        final String id = "43t4523fds";
        final String description = "Mole";
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(RecipeCommand.builder()
                .id(id)
                .description(description)
                .build()));

        //when
        webTestClient.get()
                .uri("/recipe/1/ingredients")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = Objects.requireNonNull(response.getResponseBody());
                    assertTrue(responseBody.contains(id));
                });
        //then
        verify(recipeService, times(1)).findCommandById(anyString());
    }

    @Test
    public void testShowIngredient() {
        //given
        final String id = "34rre343";
        final String description = "Potato";
        final String uom = "Each";
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(id);
        ingredientCommand.setDescription(description);
        ingredientCommand.setAmount(BigDecimal.valueOf(4));
        ingredientCommand.setUom(UnitOfMeasureCommand.builder()
                        .id("334fd34")
                        .description(uom)
                        .build());

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));

        //then
        webTestClient.get()
                .uri("/recipe/1/ingredient/2/show")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = Objects.requireNonNull(response.getResponseBody());
                    assertTrue(responseBody.contains(id));
                    assertTrue(responseBody.contains(description));
                    assertTrue(responseBody.contains(uom));
                });
                
    }

    @Ignore
    @Test
    public void testNewIngredientForm() throws Exception {
        //given
//        RecipeCommand recipeCommand = new RecipeCommand();
//        recipeCommand.setId("1");

        //when
//        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));
//        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.just(UnitOfMeasureCommand.builder().build()));
//
        //then
//        mockMvc.perform(get("/recipe/1/ingredient/new"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("recipe/ingredient/ingredientform"))
//                .andExpect(model().attributeExists("ingredient"))
//                .andExpect(model().attributeExists("uomList"));

//        verify(recipeService, times(1)).findCommandById(anyString());

    }

    @Ignore
    @Test
    public void testUpdateIngredientForm() throws Exception {
        //given
//        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
//        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));
//        when(unitOfMeasureService.listAllUoms()).thenReturn(Flux.just(UnitOfMeasureCommand.builder().build()));

        //then
//        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("recipe/ingredient/ingredientform"))
//                .andExpect(model().attributeExists("ingredient"))
//                .andExpect(model().attributeExists("uomList"));
    }

    @Ignore
    @Test
    public void testSaveOrUpdate() throws Exception {
        //given
//        IngredientCommand command = new IngredientCommand();
//        command.setId("3");
//        command.setRecipeId("2");

        //when
//        when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(command));

        //then
//        mockMvc.perform(post("/recipe/2/ingredient")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("id", "")
//                .param("description", "some string")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));

    }

    @Ignore
    @Test
    public void testDeleteIngredient() throws Exception {

        //then
//        mockMvc.perform(get("/recipe/2/ingredient/3/delete")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/recipe/2/ingredients"));
//
//        when(ingredientService.deleteById(anyString(), anyString())).thenReturn(Mono.empty());
//
//        verify(ingredientService, times(1)).deleteById(anyString(), anyString());

    }
}
