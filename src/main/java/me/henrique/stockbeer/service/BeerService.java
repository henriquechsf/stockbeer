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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));

        return beerMapper.toDTO(foundBeer);
    }

    public List<BeerDTO> listAll() {
       return beerRepository.findAll()
               .stream()
               .map(beerMapper::toDTO)
               .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    private Beer verifyIfExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    private void verifyIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optSavedBeer = beerRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }
}
