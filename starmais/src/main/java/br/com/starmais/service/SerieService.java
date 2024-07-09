package br.com.starmais.service;

import br.com.starmais.dto.EpisodioDto;
import br.com.starmais.dto.SerieDto;
import br.com.starmais.model.Categoria;
import br.com.starmais.model.Episodio;
import br.com.starmais.model.Serie;
import br.com.starmais.repository.SerieRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDto> obterTodasAsSeries() {
        return converteDados(serieRepository.findAll());
    }

    public List<SerieDto> obterTop5Series() {
        return converteDados(serieRepository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDto> obterTop5UltimasSeriesLancadas() {
        return converteDados(serieRepository.lancamentosMaisRecentes());
    }

    public SerieDto obterSeriePorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getSinopse(), s.getPoster());
        }
        return null;
    }

    private List<SerieDto> converteDados(List<Serie> serie) {
        return serie.stream().map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getSinopse(), s.getPoster())).
                collect(Collectors.toList());
    }


    public List<EpisodioDto> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo())).collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDto> obterTemporadasPorNumero(Long id, Long numero){
      return serieRepository.obterEpisodiosPorTemporada(id, numero)
              .stream().map(e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
              .collect(Collectors.toList());
    }

    public List<SerieDto> obterSeriePorGenero(String genero){
        Categoria categoria = Categoria.fromPortugues(genero);
        return converteDados(serieRepository.findByGenero(categoria));
    }
}
