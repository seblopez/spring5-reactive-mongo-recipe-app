package guru.springframework.controllers;

import guru.springframework.domain.Notes;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
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

import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by jt on 6/19/17.
 */
@RunWith(SpringRunner.class)
@WebFluxTest(RecipeController.class)
@Import({ThymeleafAutoConfiguration.class})
public class RecipeControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetRecipe() {
        final String id = "323";
        final String description = "Fajita";
        final String someNotes = "Some Notes";

        when(recipeService.findById(anyString())).thenReturn(Mono.just(Recipe.builder()
                .id(id)
                .description(description)
                .notes(Notes.builder()
                        .id("43")
                        .recipeNotes(someNotes)
                        .build())
                .build()));

        webTestClient.get()
                .uri("/recipe/1/show")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = Objects.requireNonNull(response.getResponseBody());
                    assertTrue(responseBody.contains(id));
                    assertTrue(responseBody.contains(description));
                    assertTrue(responseBody.contains(someNotes));
                });
    }

    @Test
    public void testGetRecipeNotFound() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        webTestClient.get()
                .uri("/recipe/1/show")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Ignore
    @Test
    public void testGetNewRecipeForm() throws Exception {
//       mockMvc.perform(get("/recipe/new"))
//               .andExpect(status().isOk())
//               .andExpect(view().name("recipe/recipeform"))
//               .andExpect(model().attributeExists("recipe"));
    }

    @Ignore
    @Test
    public void testPostNewRecipeForm() throws Exception {
//        RecipeCommand command = new RecipeCommand();
//        command.setId("2");
//
//        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));
//
//        mockMvc.perform(post("/recipe")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("id", "")
//                .param("description", "some string")
//                .param("directions", "some directions")
//        )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Ignore
    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {
//        RecipeCommand command = new RecipeCommand();
//        command.setId("2");
//
//        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));
//
//        mockMvc.perform(post("/recipe")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("id", "")
//                .param("cookTime", "3000")
//
//        )
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(view().name("recipe/recipeform"));
    }

    @Ignore
    @Test
    public void testGetUpdateView() throws Exception {
//        RecipeCommand command = new RecipeCommand();
//        command.setId("2");
//
//        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));
//
//        mockMvc.perform(get("/recipe/1/update"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("recipe/recipeform"))
//                .andExpect(model().attributeExists("recipe"));
    }

    @Ignore
    @Test
    public void testDeleteAction() throws Exception {
//        mockMvc.perform(get("/recipe/1/delete"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/"));
//
//        verify(recipeService, times(1)).deleteById(anyString());
    }
}
