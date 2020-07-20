package guru.springframework.commands;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Created by jt on 6/21/17.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientCommand {
    private String id;
    private String recipeId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @NotNull
    private UnitOfMeasureCommand uom;
}
