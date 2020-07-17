package guru.springframework.repositories.reactive;

import guru.springframework.domain.Category;
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
public class CategoryReactiveRepositoryIT {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Before
    public void setUp() throws Exception {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void testSaveBlockAndSaved1Ok() {
        categoryReactiveRepository.save(Category.builder()
                .description("Chinese")
                .build()).block();

        final Long categoryCount = categoryReactiveRepository.count().block();

        assertEquals(Long.valueOf(1), categoryCount);

    }

    @Test
    public void testSaveBlockAndRetrieveByDescriptionOk() {
        final String chinese = "Chinese";

        categoryReactiveRepository.save(Category.builder()
                .description(chinese)
                .build()).block();

        final Category category = categoryReactiveRepository.findByDescription(chinese).block();

        assertNotNull(category);
        assertEquals(chinese, category.getDescription());

    }
}
