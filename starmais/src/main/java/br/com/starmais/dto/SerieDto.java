package br.com.starmais.dto;

import br.com.starmais.model.Categoria;

public record SerieDto( Long id,

 String titulo,
 Integer totalTemporadas,
 Double avaliacao,
 Categoria genero,
 String atores,
 String sinopse,
 String poster) {
}
