package br.com.starmais.starmais;

import br.com.starmais.model.DadosSerie;
import br.com.starmais.service.ConsumoAPI;
import br.com.starmais.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StarmaisApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StarmaisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();

		var json =  consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();

		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

		System.out.println(dados);


	}
}
