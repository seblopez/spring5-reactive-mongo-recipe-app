package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/28/17.
 */
@AllArgsConstructor
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return this.unitOfMeasureRepository.findAll()
                .map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }

    @Override
    public Mono<UnitOfMeasureCommand> findById(String id) {
        return this.unitOfMeasureRepository.findById(id).map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }


}
