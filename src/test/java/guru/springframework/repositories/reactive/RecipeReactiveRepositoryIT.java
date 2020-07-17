package guru.springframework.repositories.reactive;

import guru.springframework.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Before
    public void setUp() throws Exception {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void saveRecipeOk() {
        final Recipe gnocchiRecipe = recipeReactiveRepository.save(Recipe.builder()
                .description("Gnocchi")
                .build()).block();

        assertNotNull(gnocchiRecipe);
        assertEquals("Gnocchi", gnocchiRecipe.getDescription());

    }



}
