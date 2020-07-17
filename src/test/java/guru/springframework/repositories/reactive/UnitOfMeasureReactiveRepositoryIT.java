package guru.springframework.repositories.reactive;

import guru.springframework.domain.UnitOfMeasure;
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
public class UnitOfMeasureReactiveRepositoryIT {

    private final static String KILOGRAM = "Kilogram";

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    public void getSaveUOMBlockingOk() throws Exception {
        unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder()
                .description(this.KILOGRAM)
                .build()).block();

        final Long count = unitOfMeasureReactiveRepository.findAll()
                .count()
                .block();

        assertEquals(Long.valueOf(1), count);

    }

    @Test
    public void getSaveUOMFindByDescriptionBlockingOk() throws Exception {
        unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder()
                .description(this.KILOGRAM)
                .build()).block();

        final UnitOfMeasure kilogram = unitOfMeasureReactiveRepository.findByDescription(this.KILOGRAM)
                .block();

        assertNotNull(kilogram);
        assertEquals(this.KILOGRAM, kilogram.getDescription());

    }

}
