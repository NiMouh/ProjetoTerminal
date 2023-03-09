import myinputs.Ler;
import org.deidentifier.arx.*;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public class Main {

    private static void menuPrincipal(Data dados) {

        int numColumns = dados.getHandle().getNumColumns();
        String[] headers = new String[numColumns];
        for (int i = 0; i < numColumns; i++)
            headers[i] = dados.getHandle().getAttributeName(i);

        System.out.println("\t# Bem-vindo ao programa de anonimização de dados #\n\nO que pretende fazer?");
        int opcao;
        do {
            System.out.println("""
                    1 - Modificar meta-dados dos atributos
                    2 - Criar hierarquias
                    3 - Anonimizar dados
                    4 - Exportar dados anonimizados
                    5 - Gerar Estatísticas
                    6 - Sair
                    """);
            opcao = Ler.umInt();

            switch (opcao) {
                case 1 -> {
                    System.out.println("Qual o atributo que pretende modificar?");
                    for (int i = 0; i < numColumns; i++)
                        System.out.println(i + " - " + headers[i]);
                    System.out.println(numColumns + " - Voltar ao menu principal");
                    int escolha = Ler.umInt();
                    while (escolha < 0 || escolha > numColumns) {
                        System.out.println("Opção inválida. Tente novamente.");
                        escolha = Ler.umInt();
                    }
                    if (escolha == numColumns)
                        menuPrincipal(dados);
                    else
                        menuModificarMetaDados(dados, escolha);
                }
                case 2 -> {
                    System.out.println("Qual o atributo que pretende criar uma hierarquia?");
                    for (int i = 0; i < numColumns; i++)
                        System.out.println(i + "- " + headers[i]);
                    System.out.println(numColumns + " - Voltar ao menu principal");
                    int escolha = Ler.umInt();
                    while (escolha < 0 || escolha > numColumns) {
                        System.out.println("Opção inválida. Tente novamente.");
                        escolha = Ler.umInt();
                    }
                    if (escolha == numColumns)
                        menuPrincipal(dados);
                    else
                        menuCriarHierarquias(dados, escolha);
                }
                case 3 -> menuAnonimizarDados(dados);
                case 4 -> menuExportarDados(dados);
                case 5 -> menuEstatisticas(dados);
                case 6 -> System.exit(0);
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao < 1 || opcao > 5);
    }

    private static void menuEstatisticas(Data dados) {

    }

    private static void menuModificarMetaDados(Data dados, int escolha) {
        // Tipo de Variável
        int tipo;
        do {
            System.out.println("""
                    Qual o tipo de dados que esta variável contém?
                    1- String
                    2- Integer
                    3- Date/Time
                    4- Decimal
                    5- Ordinal
                    """);
            tipo = Ler.umInt();

            switch (tipo) {
                case 1 -> dados.getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.STRING);
                case 2 -> dados.getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.INTEGER);
                case 3 -> dados.getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.DATE);
                case 4 -> dados.getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.DECIMAL);
                case 5 -> dados.getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.ORDERED_STRING);
                default -> System.out.println("Opção inválida. Tente novamente.");
            } // Colocar condição para quando o utilizador insere tipo inválido (Parsing Exception)
        } while (tipo < 1 || tipo > 5);

        String[] coluna = dados.getHandle().getDistinctValues(escolha);
        for (String s : coluna)
            System.out.println(s);
        // Tipo de Identificador
        int identificador;
        do {
            System.out.println("""
                    Qual o tipo de identificador que esta variável contém?
                    1- Identificador
                    2- Identificador sensível
                    3- Identificador não sensível
                    4- Quase identificador
                    """);
            identificador = Ler.umInt();
            switch (identificador) {
                case 1 -> dados.getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.IDENTIFYING_ATTRIBUTE);
                case 2 -> dados.getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.SENSITIVE_ATTRIBUTE);
                case 3 -> dados.getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.INSENSITIVE_ATTRIBUTE);
                case 4 ->
                        dados.getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
            }
        } while (identificador < 1 || identificador > 4);
        /* Metódo de transformação
        System.out.println("Qual o método de transformação que pretende aplicar?\n");
        System.out.println("1- Generalização");
        System.out.println("2- Microagregação");
        System.out.println("3- Clustering e microagregação");
        int metodo = Ler.umInt();
        while (metodo < 1 || metodo > 3) {
            System.out.println("Opção inválida. Tente novamente.");
            metodo = Ler.umInt();
        }
        */

        // Voltar ao menu principal
        menuPrincipal(dados);
    }

    public static void menuCriarHierarquias(Data dados, int escolha) {
        System.out.println("""
                Deseja criar/importar uma hierarquia para esta variável?
                1- Sim
                2- Não
                """);
        int hierarquia = Ler.umInt();
        while (hierarquia < 1 || hierarquia > 2) {
            System.out.println("Opção inválida. Tente novamente.");
            hierarquia = Ler.umInt();
        }
        if (hierarquia != 1) {
            menuPrincipal(dados);
        } else {
            int tipoHierarquia;
            do {
                System.out.println("Qual o tipo de hierarquia que pretende criar?\n");
                System.out.println("1- Hierarquia de intervalos (para variáveis numéricas)");
                System.out.println("2- Hierarquia de ordenação (para variáveis com escala ordinal)");
                System.out.println("3- Hierarquia de marcaramento (para strings alfanuméricas)");
                tipoHierarquia = Ler.umInt();
                switch (tipoHierarquia) {
                    case 1 -> menuCriarHierarquiasIntervalos(dados, escolha);
                    case 2 -> menuCriarHierarquiasOrdenacao(dados, escolha);
                    case 3 -> menuCriarHierarquiasMarcaramento(dados, escolha);
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } while (tipoHierarquia < 1 || tipoHierarquia > 3);
        }
    }

    private static void menuCriarHierarquiasMarcaramento(Data dados, int escolha) {
        System.out.println("Pretende criar ou importar uma hierarquia de marcaramento?\n");
        int opcao;
        do {
            System.out.printf("""
                    Pretende criar ou importar uma hierarquia de marcaramento para a variável %s?
                    1- Sim
                    2- Não
                    """, dados.getHandle().getAttributeName(escolha));
            opcao = Ler.umInt();
            switch (opcao) {
                case 1 -> {
                    // Criar hierarquia
                }
                case 2 -> {
                    System.out.println("Qual o nome do ficheiro que pretende importar?");
                    String nomeFicheiro = Ler.umaString();
                    Hierarchy hierarchy;
                    try {
                        hierarchy = Hierarchy.create(nomeFicheiro, Charset.defaultCharset(), ';');
                        // Aplicar a hierarquia à variável
                        dados.getHandle().getDefinition().setHierarchy(dados.getHandle().getAttributeName(escolha), hierarchy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao < 1 || opcao > 2);
    }

    private static void menuCriarHierarquiasOrdenacao(Data dados, int escolha) {
        int opcao;
        do {
            System.out.printf("""
                    Pretende criar ou importar uma hierarquia de intervalos para a variável %s?
                    1- Sim
                    2- Não
                    3- Voltar ao menu principal
                    """, dados.getHandle().getAttributeName(escolha));
            opcao = Ler.umInt();
            switch (opcao) {
                case 1 -> {
                    // Criar hierarquia
                }
                case 2 -> {
                    System.out.println("Qual o nome do ficheiro que pretende importar?");
                    String nomeFicheiro = Ler.umaString();
                    Hierarchy hierarchy;
                    try {
                        hierarchy = Hierarchy.create(nomeFicheiro, Charset.defaultCharset(), ';');
                        // Aplicar a hierarquia à variável
                        dados.getHandle().getDefinition().setHierarchy(dados.getHandle().getAttributeName(escolha), hierarchy);
                    } catch (IOException e) {
                        System.out.println("Erro ao importar a hierarquia.");
                    }
                }
                case 3 -> menuPrincipal(dados);
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao < 1 || opcao > 3 || opcao == 3);
    }

    private static void menuCriarHierarquiasIntervalos(Data dados, int escolha) {
        int opcao;
        do {
            System.out.printf("""
                    Pretende criar ou importar uma hierarquia de intervalos para a variável %s?
                    1- Sim
                    2- Não
                    3- Voltar ao menu principal
                    """, dados.getHandle().getAttributeName(escolha));
            opcao = Ler.umInt();
            switch (opcao) {
                case 1 -> {
                    if (dados.getDefinition().getDataType(dados.getHandle().getAttributeName(escolha)) == DataType.INTEGER) {
                        System.out.println("Qual o valor mínimo do intervalo?");
                        int minimo = Ler.umInt();
                        System.out.println("Qual o valor máximo do intervalo?");
                        int maximo = Ler.umInt();
                        System.out.println("Quantos níveis de intervalos pretende criar?");
                        int niveisIntervalos = Ler.umInt();
                        HierarchyBuilderIntervalBased<Long> builder = HierarchyBuilderIntervalBased.create(DataType.INTEGER);
                        // Ainda não sei adicionar intervalos
                        Hierarchy hierarchy = builder.build();
                        // Aplicar a hierarquia à variável
                        dados.getDefinition().setHierarchy(dados.getHandle().getAttributeName(escolha), hierarchy);
                    } else if (dados.getDefinition().getDataType(dados.getHandle().getAttributeName(escolha)) == DataType.DECIMAL) {
                        System.out.println("Qual o valor mínimo do intervalo?");
                        double minimo = Ler.umDouble();
                        System.out.println("Qual o valor máximo do intervalo?");
                        double maximo = Ler.umDouble();
                        System.out.println("Quantos níveis de intervalos pretende criar?");
                        int niveisIntervalos = Ler.umInt();
                        HierarchyBuilderIntervalBased<Double> builder = HierarchyBuilderIntervalBased.create(DataType.DECIMAL);
                        // Ainda não sei adicionar intervalos
                        Hierarchy hierarchy = builder.build();
                        // Aplicar a hierarquia à variável
                        dados.getDefinition().setHierarchy(dados.getHandle().getAttributeName(escolha), hierarchy);
                    }
                }
                case 2 -> {
                    System.out.println("Qual o nome do ficheiro que pretende importar?");
                    String nomeFicheiro = Ler.umaString();
                    Hierarchy hierarchy;
                    try {
                        hierarchy = Hierarchy.create(nomeFicheiro, Charset.defaultCharset(), ';');
                        dados.getDefinition().setHierarchy(dados.getHandle().getAttributeName(escolha), hierarchy);
                    } catch (IOException e) {
                        System.out.println("Erro ao importar a hierarquia.");
                    }
                }
                case 3 -> menuPrincipal(dados);
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao < 1 || opcao > 3 || opcao == 3);
    }

    private static void menuExportarDados(Data data) {
        DataHandle handle = data.getHandle();
        System.out.println("Qual o nome do ficheiro que pretende exportar?");
        String nomeFicheiro = Ler.umaString();
        try {
            handle.save(nomeFicheiro, ';');
        } catch (IOException e) {
            System.out.println("Erro ao exportar os dados.");
        }
    }

    private static void menuAnonimizarDados(Data dados) {
        System.out.println("Qual o valor do K que pretende utilizar para a anonimização?");
        int valorAnonimizacao = Ler.umInt();
        while (valorAnonimizacao < 2) {
            System.out.println("Valor inválido. Tente novamente.");
            valorAnonimizacao = Ler.umInt();
        }
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(valorAnonimizacao));
        config.setSuppressionLimit(1d); // 100% de supressão que é o padrão
        try {
            ARXResult result = anonymizer.anonymize(dados, config);
            System.out.println("Qual o nome do ficheiro que pretende exportar?");
            String nomeFicheiro = Ler.umaString();
            result.getOutput(false).save(nomeFicheiro, ';');
        } catch (IOException e) {
            System.out.println("Erro ao anonimizar os dados.");
        }
        System.out.println("""
                Pretende adicionar outro modelo de privacidade?
                1- Sim
                2- Não
                """);
        int modelo = Ler.umInt();
        while (modelo < 1 || modelo > 2) {
            System.out.println("Opção inválida. Tente novamente.");
            modelo = Ler.umInt();
        }
        if (modelo != 1) {
            menuPrincipal(dados);
        } else {
            System.out.println("""
                    Qual o modelo de privacidade que pretende adicionar?
                    1- D-LDiversity
                    2- T-Closeness
                    3- Voltar ao menu principal
                    """);
            int modeloPrivacidade = Ler.umInt();
            while (modeloPrivacidade < 1 || modeloPrivacidade > 3) {
                System.out.println("Opção inválida. Tente novamente.");
                modeloPrivacidade = Ler.umInt();
            }
            switch (modeloPrivacidade) {
                case 1 -> {
                    // D-LDiversity
                }
                case 2 -> {
                    // T-Closeness
                }
                case 3 -> menuPrincipal(dados);
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }

    }

    public static void main(String[] args) {

        System.out.println("Qual o ficheiro que pretende utilizar?");
        String ficheiro = Ler.umaString();
        Data data;
        try {
            data = Data.create(ficheiro, Charset.defaultCharset(), ';');
            menuPrincipal(data);
        } catch (IOException e) {
            System.out.println("Erro ao importar o ficheiro.");
        }
    }
}