package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Created by jt on 6/28/17.
 */
@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;
    private final UnitOfMeasureService unitOfMeasureService;

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .switchIfEmpty(s -> { s.onError(new NotFoundException(MessageFormat.format("Ingredient id {0} not found", ingredientId)));})
                .single()
                .map(ingredient -> {
                    IngredientCommand ingredientCmd = ingredientToIngredientCommand
                            .convert(ingredient);
                    ingredientCmd.setRecipeId(recipeId);
                    return ingredientCmd;
                });

    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        String recipeId = command.getRecipeId();
        final Mono<Recipe> savedRecipeMono = recipeReactiveRepository
                .findById(command.getRecipeId())
                .zipWith(unitOfMeasureService.findById(command.getUom().getId()))
                .flatMap(calledServices -> {
                    final Recipe recipe = calledServices.getT1();
                    final UnitOfMeasureCommand unitOfMeasureCommand = calledServices.getT2();
                    command.setUom(unitOfMeasureCommand);
                    final Ingredient ingredientToSave = recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(command.getId()))
                            .findFirst()
                            .map(ingredient -> updateIngredient(ingredient, command))
                            .orElse(ingredientCommandToIngredient.convert(command));
                    if(command.getId() == null || command.getId().equalsIgnoreCase("")) {
                        recipe.addIngredient(ingredientToSave);
                    }
                    return recipeReactiveRepository.save(recipe);
                })
                .onErrorMap(error -> {
                    log.error(error.getMessage());
                    return error;}
                    );

        return savedRecipeMono
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getAmount().equals(command.getAmount())
                        && ingredient.getDescription().equals(command.getDescription())
                        && ingredient.getUom().getId().equals(command.getUom().getId()))
                .single()
                .map(ingredient -> {
                    final IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);
                    ingredientCommand.setRecipeId(recipeId);
                    return ingredientCommand;
                })
                .onErrorMap(error -> {
                    log.error(error.getMessage());
                    return error;}
                    );
    }

    private Ingredient updateIngredient(Ingredient ingredient, IngredientCommand ingredientCommand) {
        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setAmount(ingredientCommand.getAmount());
        ingredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(ingredientCommand.getUom()));
        return ingredient;
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String idToDelete) {
        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                log.debug("found Ingredient");
                Ingredient ingredientToDelete = ingredientOptional.get();
               // ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }

        return Mono.empty();
    }
}
