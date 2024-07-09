package br.com.starmais.service;

import br.com.starmais.dto.SerieDto;
import br.com.starmais.model.Serie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ConverteDados implements IConverteDados{

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }



    }



}
