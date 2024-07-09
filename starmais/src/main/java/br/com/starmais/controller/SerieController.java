package br.com.starmais.controller;


import br.com.starmais.dto.EpisodioDto;
import br.com.starmais.dto.SerieDto;
import br.com.starmais.model.Episodio;
import br.com.starmais.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDto> obterSeries() {
        return serieService.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> obterSeriestop5() {
        return serieService.obterTop5Series();

    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterLancamentos() {
        return serieService.obterTop5UltimasSeriesLancadas();
    }

    @GetMapping("/{id}")

    public SerieDto obterSeriePorId(@PathVariable Long id) {
        return serieService.obterSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDto> obterTodasTemporadas(@PathVariable Long id) {
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDto> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero) {
        return serieService.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDto> obterSeriesPorGenero(@PathVariable String genero)
    {
        return serieService.obterSeriePorGenero(genero);
    }


}
