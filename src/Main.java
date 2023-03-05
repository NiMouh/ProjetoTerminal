import myinputs.Ler;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;

import java.io.IOException;
import java.nio.charset.Charset;

public class Main {

    private static void menuPrincipal(Data dados) {

        int numColumns = dados.getHandle().getNumColumns();
        String[] headers = new String[numColumns];
        for (int i = 0; i < numColumns; i++)
            headers[i] = dados.getHandle().getAttributeName(i);

        System.out.println("\t# Bem-vindo ao programa de anonimização de dados #\n\nO que pretende fazer?");
        System.out.println("1 - Modificar meta-dados dos atributos");
        System.out.println("2 - Criar hierarquias");
        System.out.println("3 - Anonimizar dados");
        System.out.println("4 - Exportar dados anonimizados");
        System.out.println("5 - Sair");
        int opcao = Ler.umInt();
        while (opcao < 1 || opcao > 5) {
            System.out.println("Opção inválida. Tente novamente.");
            opcao = Ler.umInt();
        }

        switch (opcao) {
            case 1 -> {
                System.out.println("Qual o atributo que pretende modificar?");
                for (int i = 0; i < numColumns; i++)
                    System.out.println(i + 1 + " - " + headers[i]);
                System.out.println("0 - Voltar ao menu principal");
                int escolha = Ler.umInt();
                while (escolha < 0 || escolha > numColumns) {
                    System.out.println("Opção inválida. Tente novamente.");
                    escolha = Ler.umInt();
                }
                if (escolha != 0)
                    menuModificarMetaDados(dados, escolha);
                else
                    menuPrincipal(dados);
            }
            case 2 -> {
                System.out.println("Qual o atributo que pretende criar uma hierarquia?");
                for (int i = 0; i < numColumns; i++)
                    System.out.println(i + 1 + "- " + headers[i]);
                System.out.println("0 - Voltar ao menu principal");
                int escolha = Ler.umInt();
                while (escolha < 0 || escolha > numColumns) {
                    System.out.println("Opção inválida. Tente novamente.");
                    escolha = Ler.umInt();
                }
                if (escolha != 0)
                    menuCriarHierarquias(dados, escolha);
                else
                    menuPrincipal(dados);
            }
            case 3 -> menuAnonimizarDados(dados);
            case 4 -> menuExportarDados(dados);
            case 5 -> System.exit(0);
        }
    }

    private static void menuModificarMetaDados(Data dados, int escolha) {
        // Variável
        System.out.println("Qual o tipo de dados que esta variável contém?\n");
        System.out.println("1- String");
        System.out.println("2- Integer");
        System.out.println("3- Date/Time");
        System.out.println("4- Decimal");
        System.out.println("5- Ordinal");
        int tipo = Ler.umInt();
        while (tipo < 1 || tipo > 5) {
            System.out.println("Opção inválida. Tente novamente.");
            tipo = Ler.umInt();
        }
        switch (tipo) {
            case 1 -> dados.getHandle().getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.STRING);
            case 2 -> dados.getHandle().getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.INTEGER);
            case 3 -> dados.getHandle().getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.DATE);
            case 4 -> dados.getHandle().getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.DECIMAL);
            case 5 -> dados.getHandle().getDefinition().setDataType(dados.getHandle().getAttributeName(escolha), DataType.ORDERED_STRING);
        }

        // Identificador
        System.out.println("Qual o tipo de identificador que esta variável contém?\n");
        System.out.println("1- Identificador");
        System.out.println("2- Identificador sensível");
        System.out.println("3- Identificador não sensível");
        System.out.println("4- Quase identificador");
        int identificador = Ler.umInt();
        while (identificador < 1 || identificador > 4) {
            System.out.println("Opção inválida. Tente novamente.");
            identificador = Ler.umInt();
        }
        switch (identificador) {
            case 1 ->
                    dados.getHandle().getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.IDENTIFYING_ATTRIBUTE);
            case 2 ->
                    dados.getHandle().getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.SENSITIVE_ATTRIBUTE);
            case 3 ->
                    dados.getHandle().getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.INSENSITIVE_ATTRIBUTE);
            case 4 ->
                    dados.getHandle().getDefinition().setAttributeType(dados.getHandle().getAttributeName(escolha), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
        }
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
        System.out.println("Deseja criar/importar uma hierarquia para esta variável?\n");
        System.out.println("1- Sim");
        System.out.println("2- Não");
        int hierarquia = Ler.umInt();
        while (hierarquia < 1 || hierarquia > 2) {
            System.out.println("Opção inválida. Tente novamente.");
            hierarquia = Ler.umInt();
        }
        if (hierarquia == 1) {
            System.out.println("Qual o tipo de hierarquia que pretende criar?\n");
            System.out.println("1- Hierarquia de intervalos (para variáveis numéricas)");
            System.out.println("2- Hierarquia de ordenação (para variáveis com escala ordinal)");
            System.out.println("3- Hierarquia de marcaramento (para strings alfanuméricas)");
            int tipoHierarquia = Ler.umInt();
            while (tipoHierarquia < 1 || tipoHierarquia > 3) {
                System.out.println("Opção inválida. Tente novamente.");
                tipoHierarquia = Ler.umInt();
            }
            switch (tipoHierarquia) {
                case 1 -> menuCriarHierarquiasIntervalos(dados, escolha);
                case 2 -> menuCriarHierarquiasOrdenacao(dados, escolha);
                case 3 -> menuCriarHierarquiasMarcaramento(dados, escolha);
            }
        } else {
            menuPrincipal(dados);
        }
    }

    private static void menuCriarHierarquiasMarcaramento(Data dados, int escolha) {
        System.out.println("Pretende criar ou importar uma hierarquia de marcaramento?\n");
        DefaultHierarchy hierarchy = Hierarchy.create();

    }

    private static void menuCriarHierarquiasOrdenacao(Data dados, int escolha) {
    }

    private static void menuCriarHierarquiasIntervalos(Data dados, int escolha) {
        System.out.println("Pretende criar ou importar uma hierarquia de intervalos?\n");
        System.out.println("1- Criar");
        System.out.println("2- Importar");
        System.out.println("3- Voltar ao menu principal");
        int opcao = Ler.umInt();
        while (opcao < 1 || opcao > 3) {
            System.out.println("Opção inválida. Tente novamente.");
            opcao = Ler.umInt();
        }
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
                    dados.getHandle().getDefinition().setHierarchy(dados.getHandle().getAttributeName(escolha), hierarchy);
                } catch (IOException e) {
                    System.out.println("Erro ao importar a hierarquia.");
                }
            }
            case 3 -> menuPrincipal(dados);
        }

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

        System.out.println("Pretende adicionar outro modelo de privacidade?\n");
        System.out.println("1- Sim");
        System.out.println("2- Não");
        int modelo = Ler.umInt();
        while (modelo < 1 || modelo > 2) {
            System.out.println("Opção inválida. Tente novamente.");
            modelo = Ler.umInt();
        }
        if (modelo == 1) {
            System.out.println("Qual o modelo de privacidade que pretende adicionar?\n");
            System.out.println("1- D-LDiversity");
            System.out.println("2- T-Closeness");
            System.out.println("3- Voltar ao menu principal");
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
            }
        } else {
            menuPrincipal(dados);
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