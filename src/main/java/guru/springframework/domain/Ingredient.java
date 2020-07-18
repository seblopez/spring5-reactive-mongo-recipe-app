package guru.springframework.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by jt on 6/13/17.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;

}
