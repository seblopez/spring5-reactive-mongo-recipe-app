package guru.springframework.repositories;

import guru.springframework.bootstrap.RecipeBootstrap;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.CategoryReactiveRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by jt on 6/17/17.
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @Autowired
    CategoryReactiveRepository categoryRepository;

    @Autowired
    RecipeReactiveRepository recipeRepository;
    private UnitOfMeasure uom;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureRepository.deleteAll().block();
        categoryRepository.deleteAll().block();
        recipeRepository.deleteAll().block();

        RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);

        recipeBootstrap.onApplicationEvent(null);

    }

    @Test
    public void findByDescription() throws Exception {

        uom = unitOfMeasureRepository.findByDescription("Teaspoon").block();

        assertEquals("Teaspoon", uom.getDescription());
    }

    @Test
    public void findByDescriptionCup() throws Exception {

        UnitOfMeasure uom = unitOfMeasureRepository.findByDescription("Cup").block();

        assertEquals("Cup", uom.getDescription());
    }

}
