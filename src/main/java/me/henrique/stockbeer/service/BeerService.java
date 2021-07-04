package me.henrique.stockbeer.service;

import lombok.AllArgsConstructor;
import me.henrique.stockbeer.dto.BeerDTO;
import me.henrique.stockbeer.entity.Beer;
import me.henrique.stockbeer.exceptions.BeerAlreadyRegisteredException;
import me.henrique.stockbeer.exceptions.BeerNotFoundException;
import me.henrique.stockbeer.mapper.BeerMapper;
import me.henrique.stockbeer.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIsAlreadyRegistered(beerDTO.getName());
        Beer beerToSave = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beerToSave);

        return beerMapper.toDTO(savedBeer);
    }

    private void verifyIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optSavedBeer = beerRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));

        return beerMapper.toDTO(foundBeer);
    }
}
