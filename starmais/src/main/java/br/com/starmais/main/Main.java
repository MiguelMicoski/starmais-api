package br.com.starmais.main;

import br.com.starmais.model.*;
import br.com.starmais.repository.SerieRepository;
import br.com.starmais.service.ConsumoAPI;
import br.com.starmais.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private ConverteDados conversor = new ConverteDados();
    private final String API_KEY = "&apikey=6585022c";
    private List <DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    int opcao = 22;

    public Main(SerieRepository repositorio) {

        this.repositorio = repositorio;
    }

    public void exibeMenu()
    {


        while (opcao != 0) {
                var menu = """
                        1 - Buscar séries
                        2 - Buscar episódios
                        3 - Listar séries buscadas
                        4 - Buscar série por titulo
                        5 - Buscar séries por autor
                        6 - Top 5 séres
                        7 - Buscar por categoria
                        8 - Buscar por limite de temporadas e avaliação
                        9 - Buscar episódio por trecho
                    10 - Top 5 episódios de tal série
                    11 - Buscar ep a partir de uma data
                                    
                    0 - Sair                                 
                    """;


            System.out.println(menu);
            opcao  = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;

                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarItensPorCategoria();
                    break;
                case 8:
                    buscarPorTotalTemporadaeAvaliacao();
                    break;
                case 9:
                    buscarEpisódioPorTrecho();
                    break;
                case 10:
                    buscarEpisodiosPorSerie();
                    break;
                case 11: 
                    buscarEpisodioDepoisDeUmaData();
                    break;
                            
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }

    }

    private void buscarEpisodioDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void buscarEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }


    }

    private void buscarEpisódioPorTrecho(){
        System.out.println("Digite o nome do ep para busca?");
        var trechoEp = leitura.nextLine();
        List<Episodio> epsEncontrados = repositorio.episodiosPorTrecho(trechoEp);
        epsEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));

    }

    private void buscarPorTotalTemporadaeAvaliacao(){
        System.out.println("Qual o limite de temporadas deseja buscar?");
        var totalTemporadas = leitura.nextInt();
        System.out.println("E qual a nota mínima de avaliação das séries deseja ver?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarItensPorCategoria(){
        System.out.println("Por qual gênero deseja buscar séries?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series da categoria " + nomeGenero + ":");
        seriesPorCategoria.forEach(System.out::println);
    }


    private void buscarTop5Series(){
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach((s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao())));
    }
    private void buscarSeriePorAtor() {
       System.out.println("Digite o nome do ator para busca");
       var ator = leitura.nextLine();
       System.out.println("Qual a nota mínima de avaliação das séries deseja ver?");
       var avaliacao = leitura.nextDouble();
       Optional<Serie> serieBuscada = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(ator, avaliacao);
        if (serieBuscada.isPresent()){
            System.out.println("Série em que o " + ator + " trabalhou");
            serieBuscada.stream().forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        }
        else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBusca.isPresent()){
            System.out.println("Série: " + serieBusca.get());
        }
        else {
            System.out.println("Série não encontrada");
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);

        dadosSeries.add(dados);

    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Digite o nome da série que quer buscar os episódios: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);


            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.eps().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repositorio.save(serieEncontrada);

        }

        else{
            System.out.println("Série não encontrada");

        }
    }



    private void listarSeriesBuscadas(){
        series = repositorio.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);

    }

    }








