package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Created by jt on 7/3/17.
 */
@Slf4j
@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;

    @Override
    @Transactional
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {
        final Recipe recipeFound = recipeRepository.findById(recipeId)
                .map(recipe -> {
                    try {
                        Byte[] byteObjects = new Byte[file.getBytes().length];
                        int i = 0;
                        for (byte b : file.getBytes()) {
                            byteObjects[i++] = b;
                        }
                        recipe.setImage(byteObjects);
                    } catch (IOException e) {
                        //todo handle better
                        log.error("Error occurred", e);
                    }
                    return recipe;
                })
                .block();

        recipeRepository.save(recipeFound).block();

        return Mono.empty();

    }

}
