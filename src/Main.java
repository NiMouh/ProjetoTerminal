import myinputs.Ler;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataType;
import org.dom4j.datatype.DatatypeElementFactory;

import java.nio.charset.Charset;

public class Main {

    public void menuPrincipal() {
        System.out.println("\t# Bem-vindo ao programa de anonimização de dados #\n\nO que pretende fazer?\n");
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
            // case 1 -> menuAlterarMetadados();
            // case 2 -> menuCriarHierarquias();
            // case 3 -> menuAnonimizarDados();
            // case 4 -> menuExportarDados();
            // case 5 -> System.exit(0);
        }
    }


    public static void main(String[] args) throws Exception {

        String ficheiro = "data.csv";
        Data data = Data.create(ficheiro, Charset.defaultCharset(), ';');

        int numColumns = data.getHandle().getNumColumns();
        String[] headers = new String[numColumns];

        System.out.println("\t# Bem-vindo ao programa de anonimização de dados #\n\nQual item pretende selecionar?\n");


        // Variável
        for (int i = 0; i < numColumns; i++) {
            System.out.println(i + "- " + data.getHandle().getAttributeName(i));
            headers[i] = data.getHandle().getAttributeName(i);
        }
        int escolha = Ler.umInt();
        while (escolha < 0 || escolha > numColumns) {
            System.out.println("Opção inválida. Tente novamente.");
            escolha = Ler.umInt();
        }

        // Data Type
        System.out.println("Qual o tipo de dados que esta coluna contém?\n");
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
            case 1 -> data.getHandle().getDefinition().setDataType(headers[escolha], DataType.STRING);
            case 2 -> data.getHandle().getDefinition().setDataType(headers[escolha], DataType.INTEGER);
            case 3 -> data.getHandle().getDefinition().setDataType(headers[escolha], DataType.DATE);
            case 4 -> data.getHandle().getDefinition().setDataType(headers[escolha], DataType.DECIMAL);
            case 5 -> data.getHandle().getDefinition().setDataType(headers[escolha], DataType.ORDERED_STRING);
        }

        // Identificador
        System.out.println("Qual o tipo de identificador que esta coluna contém?\n");
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
            case 1 -> data.getHandle().getDefinition().setAttributeType(headers[escolha], AttributeType.IDENTIFYING_ATTRIBUTE);
            case 2 -> data.getHandle().getDefinition().setAttributeType(headers[escolha], AttributeType.SENSITIVE_ATTRIBUTE);
            case 3 -> data.getHandle().getDefinition().setAttributeType(headers[escolha], AttributeType.INSENSITIVE_ATTRIBUTE);
            case 4 -> data.getHandle().getDefinition().setAttributeType(headers[escolha], AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
        }

        // Criar Hierarquia
        System.out.println("Deseja criar uma hierarquia para esta coluna?\n");
        System.out.println("1- Sim");
        System.out.println("2- Não");
        int hierarquia = Ler.umInt();
        while (hierarquia < 1 || hierarquia > 2) {
            System.out.println("Opção inválida. Tente novamente.");
            hierarquia = Ler.umInt();
        }

        // Especificar tipo de hierarquia
        System.out.println("Qual o tipo de hierarquia que pretende criar?\n");


    }
}