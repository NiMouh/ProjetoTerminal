import myinputs.Ler;
import org.deidentifier.arx.*;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased.Range;
import org.deidentifier.arx.aggregates.StatisticsBuilder;
import org.deidentifier.arx.aggregates.quality.QualityMeasureColumnOriented;
import org.deidentifier.arx.aggregates.quality.QualityMeasureRowOriented;
import org.deidentifier.arx.criteria.DistinctLDiversity;
import org.deidentifier.arx.criteria.EqualDistanceTCloseness;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.risk.RiskEstimateBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

public class Setup {
    public Setup() {
        // Recebe nome do ficheiro e importa os dados.
        System.out.println("Escreva o nome do arquivo:");
        String ficheiro = Ler.umaString();
        Data dados;
        try {
            dados = Data.create(ficheiro, Charset.defaultCharset(), ';');
        } catch (Exception e) {
            System.out.println("Erro ao ler o ficheiro");
            return;
        }

        // Define o formato dos dados.
        defineMetadados(dados);

        // Define o tipo de atributo.
        defineAtributos(dados);

        // Menu de abertura.
        System.out.println("\t# Bem-vindo ao programa de anonimização de dados #\n\nO que pretende fazer?");
        int opcao;
        do {
            System.out.println("""
                    1 - Remodificar meta-dados dos atributos
                    2 - Remodificar tipo de atributos
                    3 - Criar hierarquias
                    4 - Anonimizar dados
                    5 - Exportar dados anonimizados
                    6 - Gerar Estatísticas
                    7 - Sair
                    """);
            opcao = Ler.umInt();

            switch (opcao) {
                case 1 -> defineMetadados(dados);
                case 2 -> defineAtributos(dados);
                case 3 -> defineHierarquias(dados);
                case 4 -> anonimizarDados(dados);
                case 5 -> exportarDados(dados);
                case 6 -> gerarEstatisticas(dados, dados);
                case 7 -> System.out.println("Até à próxima!");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao < 1 || opcao > 7);
    }

    public void defineMetadados(Data dados) {
        int numeroColunas = dados.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            String nomeColuna = dados.getHandle().getAttributeName(indexColuna);
            System.out.println("Qual o tipo de dados que a variável " + nomeColuna + " contém:");
            System.out.println("""
                    1- String
                    2- Integer
                    3- Date/Time
                    4- Decimal
                    5- Ordinal
                    """);
            int tipoColuna;
            do {
                tipoColuna = Ler.umInt();
                switch (tipoColuna) {
                    case 1 -> dados.getDefinition().setDataType(nomeColuna, DataType.STRING);
                    case 2 -> dados.getDefinition().setDataType(nomeColuna, DataType.INTEGER);
                    case 3 -> dados.getDefinition().setDataType(nomeColuna, DataType.DATE);
                    case 4 -> dados.getDefinition().setDataType(nomeColuna, DataType.DECIMAL);
                    case 5 -> dados.getDefinition().setDataType(nomeColuna, DataType.ORDERED_STRING);
                    default -> System.out.println("Opção inválida");
                }
            } while (tipoColuna < 1 || tipoColuna > 5);
        }
    }

    public void defineAtributos(Data dados) {
        int numeroColunas = dados.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            String nomeColuna = dados.getHandle().getAttributeName(indexColuna);
            System.out.println("Qual o tipo de atributo que a variável " + nomeColuna + " é:");
            System.out.println("""
                    1- Identificador
                    2- Identificador sensível
                    3- Identificador não sensível
                    4- Quase identificador
                    """);
            int tipoColuna;
            do {
                tipoColuna = Ler.umInt();
                switch (tipoColuna) {
                    case 1 -> dados.getDefinition().setAttributeType(nomeColuna, AttributeType.IDENTIFYING_ATTRIBUTE);
                    case 2 -> dados.getDefinition().setAttributeType(nomeColuna, AttributeType.SENSITIVE_ATTRIBUTE);
                    case 3 -> dados.getDefinition().setAttributeType(nomeColuna, AttributeType.INSENSITIVE_ATTRIBUTE);
                    case 4 ->
                            dados.getDefinition().setAttributeType(nomeColuna, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
                    default -> System.out.println("Opção inválida");
                }
            } while (tipoColuna < 1 || tipoColuna > 4);
        }
    }

    public void defineHierarquias(Data dados) {
        int numeroColunas = dados.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            System.out.println("""
                    Deseja criar ou importar uma hierarquia para esta variável?
                    1- Criar
                    2- Importar
                    """);
            int opcao;
            do {
                opcao = Ler.umInt();
                switch (opcao) {
                    case 1 -> criarHierarquia(dados, indexColuna);
                    case 2 -> importarHierarquia(dados, indexColuna);
                    default -> System.out.println("Opção inválida");
                }
            } while (opcao < 1 || opcao > 2);
        }
    }

    public void criarHierarquia(Data dados, int indexColuna) {
        DefaultHierarchy hierarquia = Hierarchy.create();
        String nomeColuna = dados.getHandle().getAttributeName(indexColuna);

        // If the column has data type integer, create a hiearchy based on intervals
        if (dados.getHandle().getDataType(nomeColuna) == DataType.INTEGER) {
            // Criar a função de agregação para intervalos (createIntervalFunction)
            // Definir o valor máximo e minimo de alcance (range)
            System.out.println("Qual o valor mínimo do intervalo?");
            int minimoRange = Ler.umInt();
            System.out.println("Qual o valor máximo do intervalo?");
            int maximoRange = Ler.umInt();
            // Escrever range...
            Range<Integer> rangeMin = new Range<>(minimoRange, minimoRange, minimoRange);
            Range<Integer> rangeMax = new Range<>(maximoRange, maximoRange, maximoRange + 20);
            // Designar o intervalor inicial (start)
            System.out.println("Qual o valor inicial do intervalo?");
            int valorInicial = Ler.umInt();
            System.out.println("Qual o valor final do intervalo?");
            int valorFinal = Ler.umInt();
            // Escrever intervalo...
            // Definir o número de intervalos (size do Group)
            System.out.println("Qual o número de intervalos?");
            int numeroIntervalos = Ler.umInt();
            /*
            HierarchyBuilder<NumberInterval> builder = new HierarchyBuilderInterval(DataType.INTEGER, rangeMin, rangeMax);
            builder.add(start, size);
            HierarchyBuilder.IntervalFunction<NumberInterval> function = builder.createIntervalFunction();
            * */
        }

        // If the column has data type ordinal, create a hiearchy based on ordering
        if (dados.getHandle().getDataType(nomeColuna) == DataType.ORDERED_STRING) {
            // TODO
        }

        // If the column has data type string, create a hiearchy based on masking
        if (dados.getHandle().getDataType(nomeColuna) == DataType.STRING) {
            // TODO
        }

        dados.getDefinition().setHierarchy(nomeColuna, hierarquia);
    }

    public void importarHierarquia(Data dados, int indexColuna) {
        System.out.println("Qual o nome do ficheiro que pretende importar?");
        String nomeFicheiro = Ler.umaString();
        try {
            Hierarchy hierarquia = Hierarchy.create(nomeFicheiro, Charset.defaultCharset(), ';');
            dados.getDefinition().setHierarchy(dados.getHandle().getAttributeName(indexColuna), hierarquia);
        } catch (IOException e) {
            System.out.println("Erro ao importar a hierarquia.");
        }
    }

    public void anonimizarDados(Data dados) {
        System.out.println("# K-Anonymity #");
        System.out.println("Qual o valor de k que pretende utilizar?");
        int valorK = Ler.umInt();
        while (valorK < 2) {
            System.out.println("O valor de k tem de ser superior a 2.");
            valorK = Ler.umInt();
        }
        ARXAnonymizer anonimizador = new ARXAnonymizer();
        ARXConfiguration configuracoes = ARXConfiguration.create();
        configuracoes.addPrivacyModel(new KAnonymity(valorK));
        configuracoes.setSuppressionLimit(0.01d); // 1% de linhas suprimidas

        int opcao;
        do {
            System.out.println("""
                    Qual modelo de privacidade pretende utilizar, além do k-anonymity?
                    1- L-Diversity
                    2- T-Closeness
                    3- Apenas k-anonymity
                    """);
            opcao = Ler.umInt();

            switch (opcao) {
                case 1 -> {
                    System.out.println("Qual o valor de l que pretende utilizar?");
                    int valorL = Ler.umInt();
                    while (valorL < 2) {
                        System.out.println("O valor de l tem de ser superior a 2.");
                        valorL = Ler.umInt();
                    }
                    System.out.println("Qual o nome da variável que pretende utilizar para o modelo de privacidade L-Diversity?");
                    String nomeVariavel = Ler.umaString();
                    configuracoes.addPrivacyModel(new DistinctLDiversity(nomeVariavel, valorL));

                } // L-Diversity
                case 2 -> {
                    System.out.println("Qual o valor de t que pretende utilizar?");
                    double valorT = Ler.umDouble();
                    while (valorT < 0 || valorT > 1) {
                        System.out.println("O valor de t tem de estar entre 0 e 1.");
                        valorT = Ler.umDouble();
                    }
                    System.out.println("Qual o nome da variável que pretende utilizar para o modelo de privacidade L-Diversity?");
                    String nomeVariavel = Ler.umaString();
                    configuracoes.addPrivacyModel(new EqualDistanceTCloseness(nomeVariavel, valorT));
                } // T-Closeness
                case 3 -> System.out.println("Apenas k-anonymity será utilizado.");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 3);

        try {
            ARXResult resultado = anonimizador.anonymize(dados, configuracoes);
            System.out.println("Qual o nome do ficheiro que pretende exportar?");
            String nomeFicheiro = Ler.umaString();
            resultado.getOutput(false).save(nomeFicheiro, ';');
        } catch (Exception e) {
            System.out.println("Erro ao anonimizar/guardar os dados.");
        }

    }

    public ARXResult anonimizarPorK(Data dados, int valorK) throws IOException {
        ARXAnonymizer anonimizador = new ARXAnonymizer();
        ARXConfiguration configuracoes = ARXConfiguration.create();
        configuracoes.addPrivacyModel(new KAnonymity(valorK));
        configuracoes.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        return anonimizador.anonymize(dados, configuracoes);
    }

    public ARXResult anonimizarPorL(Data dados, int valorL, String nomeVariavel) throws IOException {
        ARXAnonymizer anonimizador = new ARXAnonymizer();
        ARXConfiguration configuracoes = ARXConfiguration.create();
        configuracoes.addPrivacyModel(new DistinctLDiversity(nomeVariavel, valorL));
        configuracoes.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        return anonimizador.anonymize(dados, configuracoes);
    }

    public ARXResult anonimizarPorT(Data dados, double valorT, String nomeVariavel) throws IOException {
        ARXAnonymizer anonimizador = new ARXAnonymizer();
        ARXConfiguration configuracoes = ARXConfiguration.create();
        configuracoes.addPrivacyModel(new EqualDistanceTCloseness(nomeVariavel, valorT));
        configuracoes.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        return anonimizador.anonymize(dados, configuracoes);
    }

    public void gerarEstatisticas(Data dadosOriginais, Data dadosAnonimizados) {
        // Exibir a quantidade de dados restantes e suprimidos em relação ao original
        int dadosSuprimidos = estatisticasDadosSuprimidos(dadosAnonimizados);
        int tamanhoOriginal = dadosAnonimizados.getHandle().getNumRows();
        System.out.println("Number of original records: " + dadosOriginais.getHandle().getNumRows());
        System.out.println("Number of measurements: (inc. Supressed)" + tamanhoOriginal);
        System.out.println("Number of measurements: (exc. Supressed)" + (tamanhoOriginal - dadosSuprimidos));

        DataHandle handleAnonimizado = dadosAnonimizados.getHandle();
        DataHandle handleOriginal = dadosOriginais.getHandle();
        // Exibir o risco estimado de reidentificação ('prosecutor risk', 'journalist risk', 'marketer risk')
        // Path process -> DataHandle -> RiskEstimate -> RiskModelSampleRisks
        RiskEstimateBuilder estimativa = handleAnonimizado.getRiskEstimator();
        System.out.println("Prosecutor risk: " + estimativa.getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk());
        System.out.println("Journalist risk: " + estimativa.getSampleBasedReidentificationRisk().getEstimatedJournalistRisk());
        System.out.println("Marketer risk: " + estimativa.getSampleBasedReidentificationRisk().getEstimatedMarketerRisk());


        // Exibir nível de qualidade do conjunto de dados anonimizados
        // Path process -> DataHandle -> StatisticsBuilder -> StatisticsQuality
        StatisticsBuilder estatisticas = handleAnonimizado.getStatistics();


        // Atribute-level quality (Column Oriented)
        estatisticasQualidadeAtributos(dadosAnonimizados, estatisticas);

        // Record-level quality (Row Oriented)
        estatisticasQualidadeRegistos(estatisticas);
    }

    public int estatisticasDadosSuprimidos(Data dados) {
        int dadosSuprimidos = 0;
        int tamanhoOriginal = dados.getHandle().getNumRows();
        for (int index = 0; index < tamanhoOriginal; index++) {
            if (dados.getHandle().isSuppressed(index)) {
                dadosSuprimidos++;
            }
        }
        return dadosSuprimidos;
    }

    public void estatisticasQualidadeAtributos(Data dados, StatisticsBuilder estatisticas) {
        QualityMeasureColumnOriented intensidadeGeneralizacao = estatisticas.getQualityStatistics().getGeneralizationIntensity();
        QualityMeasureColumnOriented falhas = estatisticas.getQualityStatistics().getMissings();
        QualityMeasureColumnOriented entropia = estatisticas.getQualityStatistics().getNonUniformEntropy();
        QualityMeasureColumnOriented erroQuadratico = estatisticas.getQualityStatistics().getAttributeLevelSquaredError();

        for (String atributo : dados.getDefinition().getQuasiIdentifyingAttributes()) {
            System.out.println("Attribute: " + atributo + " - Generalization Intensity: " + intensidadeGeneralizacao.getValue(atributo)
                    + " - Missings: " + falhas.getValue(atributo) + " - Non-Uniform Entropy: " + entropia.getValue(atributo)
                    + " - Attribute Level Squared Error: " + erroQuadratico.getValue(atributo));
        }
    }

    public void estatisticasQualidadeRegistos(StatisticsBuilder estatisticas) {
        QualityMeasureRowOriented identificabilidade = estatisticas.getQualityStatistics().getDiscernibility();
        QualityMeasureRowOriented tamanhoDaClasse = estatisticas.getQualityStatistics().getAverageClassSize();
        QualityMeasureRowOriented erroQuadratico = estatisticas.getQualityStatistics().getRecordLevelSquaredError();

        System.out.println("Generalization Intensity: " + identificabilidade.getValue());
        System.out.println("Average Class Size: " + tamanhoDaClasse.getValue());
        System.out.println("Record level Squared Error: " + erroQuadratico.getValue());
    }

    public static void exportarDados(Data dados) {
        DataHandle handle = dados.getHandle();
        System.out.println("Qual o nome do ficheiro que pretende exportar?");
        String nomeFicheiro = Ler.umaString();
        try {
            handle.save(nomeFicheiro, ';');
        } catch (Exception e) {
            System.out.println("Erro ao exportar os dados.");
        }
    }

    public static void main(String[] args) {
        Setup setup = new Setup();
    }
}
