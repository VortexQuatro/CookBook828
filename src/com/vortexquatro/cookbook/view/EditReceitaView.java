package com.vortexquatro.cookbook.view;

import com.vortexquatro.cookbook.domain.Ingrediente;
import com.vortexquatro.cookbook.domain.Receita;
import com.vortexquatro.cookbook.domain.Rendimento;
import com.vortexquatro.cookbook.enums.Categoria;
import com.vortexquatro.cookbook.enums.TipoMedida;
import com.vortexquatro.cookbook.enums.TipoRendimento;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditReceitaView {
    private Receita receita;

    public EditReceitaView(Receita receita) {
        this.receita = receita;
    }

    private int leRendimento(String tipo){
        int rendimentoAnterior, rendimento;

        if (this.receita.getRendimento()!= null){
            if (tipo.equalsIgnoreCase("minimo")){
                rendimentoAnterior = this.receita.getRendimento().getMinimo();
            }
            else{
                rendimentoAnterior = this.receita.getRendimento().getMaximo();
            }

            String strDouble = ConsoleUtils.getUserInput("Qual o rendimento "+ tipo + " da receita?");
            if (strDouble.length() > 0) {
                rendimento = Integer.parseInt(strDouble);
            } else {
                rendimento = rendimentoAnterior;
            }
        }
        else{
            String strDouble = ConsoleUtils.getUserInput("Qual o rendimento "+ tipo + " da receita?");
            if (strDouble.length() > 0) {
                rendimento = Integer.parseInt(strDouble);
            } else {
                rendimento = 0;
            }
        }
        return rendimento;
    }

    private boolean showMenu() {
        String nomeReceita;
        if(this.receita.getNome() != null){
            String opcaoNomeReceita = ConsoleUtils.getUserOption("Você deseja alterar o nome da receita?\nS - Sim   N - Não", "S", "N");
            if (opcaoNomeReceita.equalsIgnoreCase("S")){
                nomeReceita = ConsoleUtils.getUserInput("Digite o nome da receita: ");
            }
            else{
                nomeReceita = this.receita.getNome();
            }
        }
        else{
            nomeReceita = ConsoleUtils.getUserInput("Digite o nome da receita: ");
        }
        this.receita.setNome(nomeReceita);

        Categoria categoria;
        if (this.receita.getCategoria() == Categoria.DOCE || this.receita.getCategoria() == Categoria.SALGADO || this.receita.getCategoria() == Categoria.BEBIDA)
        {
            String opcaoCategoria = ConsoleUtils.getUserOption("Você deseja alterar a categoria da receita?\nS - Sim   N - Não", "S", "N");
            if (opcaoCategoria.equalsIgnoreCase("S")) {
                categoria = AddCategoria();
            } else {
                categoria = this.receita.getCategoria();
            }
        }
        else{
            categoria = AddCategoria();
        }
        this.receita.setCategoria(categoria);

        int rendimentoMinimo = 0;
        if (this.receita.getRendimento()!= null){
            String opcaoRendimentoMinimo = ConsoleUtils.getUserOption("Você deseja alterar o rendimento mínimo da receita?\nS - Sim   N - Não", "S", "N");
            if (opcaoRendimentoMinimo.equalsIgnoreCase("S")){
                rendimentoMinimo = leRendimento("minimo");
            }
            else {
                rendimentoMinimo = this.receita.getRendimento().getMinimo();
            }
          }
        else {
            rendimentoMinimo = leRendimento("minimo");
        }

        int rendimentoMaximo = 0;
        if (this.receita.getRendimento()!= null){
            String opcaoRendimentoMinimo = ConsoleUtils.getUserOption("Você deseja alterar o rendimento máximo da receita?\nS - Sim   N - Não", "S", "N");
            if (opcaoRendimentoMinimo.equalsIgnoreCase("S")){
                rendimentoMaximo = leRendimento("maximo");
            }
            else{
                rendimentoMaximo = this.receita.getRendimento().getMaximo();
            }
        }
        else {
            rendimentoMaximo = leRendimento("maximo");
        }

        TipoRendimento tipoRendimento;
        if(this.receita.getRendimento()!= null){
            String opcaoRendimento = ConsoleUtils.getUserOption("Você deseja alterar o tipo da unidade de rendimento?\nS - Sim   N - Não", "S", "N");
            if (opcaoRendimento.equalsIgnoreCase("N")){
                tipoRendimento = this.receita.getRendimento().getTipo();
            }
            else{
                tipoRendimento = addTipoRendimento();
            }
        }
        else {
            tipoRendimento = addTipoRendimento();
        }
        Rendimento rendimento = new Rendimento(rendimentoMinimo, rendimentoMaximo, tipoRendimento);
        this.receita.setRendimento(rendimento);

        double tempoPreparo;
        if(this.receita.getTempoPreparo()!= 0.0) {
            String opcaoTempoPreparo = ConsoleUtils.getUserOption("Você deseja alterar tempo de preparo da receita?\nS - Sim   N - Não", "S", "N");
            if (opcaoTempoPreparo.equalsIgnoreCase("N")) {
                tempoPreparo = this.receita.getTempoPreparo();
            }
            else{
                tempoPreparo = Double.parseDouble(ConsoleUtils.getUserInput("Digite o tempo de preparo em minutos: "));
            }
        }
        else {
            tempoPreparo = Double.parseDouble(ConsoleUtils.getUserInput("Digite o tempo de preparo em minutos: "));
        }
        this.receita.setTempoPreparo(tempoPreparo);

        if (receita.getIngredientes() != null) {
           String opcaoingredientes = ConsoleUtils.getUserOption("Você deseja alterar os ingredientes da receita?\nS - Sim   N - Não", "S", "N");
           if (opcaoingredientes.equalsIgnoreCase("S")) {
               alteraIngrediente(receita.getIngredientes());
           }
        }
        else{
           // Adiciona ingrediente a lista de ingredientes
           this.receita.addIngrediente(addIngredientes());
        }

        if (receita.getPreparo() != null) {
            String opcaoPassos = ConsoleUtils.getUserOption("Você deseja alterar os passos da receita?\nS - Sim   N - Não", "S", "N");
            if (opcaoPassos.equalsIgnoreCase("S")) {
                alteraPassos(this.receita.getPreparo());
            }
        }
        else{
            // Adiciona procedimentoa à receita
            this.receita.addPreparo(addProcedimentos());
        }
//        System.out.println(receita.toString()) ;
        return false;
    }

    private boolean alteraPassos(List<String> preparo) {
        String[] options = new String[3];
        StringBuilder sb = new StringBuilder("#".repeat(100));

        sb.append("%n").append("  + : Adicionar passo %n");
        options[0] = "+";

        if (preparo.size() > 0) {
            sb.append("  - : Remover passo  %n");
            options[1] = "-";
        }

        sb.append("  # ").append("# ".repeat(48)).append("%n");
        sb.append("  X : Sair  %n");
        options[2] = "X";
        sb.append("#".repeat(100)).append("%n");
        String opcaoContinua;
        do {
            String opcao = ConsoleUtils.getUserOption(sb.toString(), options).toUpperCase(Locale.getDefault());
            switch (opcao) {
                case "+":
                    adicionarPasso(preparo);
                    break;
                case "-":
                    removerPasso(preparo);
                    break;
                case "X":
                    System.out.println("Fim da edição de ingredientes");
                    return false;
                default:
                    System.out.println("Opção inválida!!!");
            }
        opcaoContinua = ConsoleUtils.getUserOption("Você deseja continuar alterando os da receita?\nS - Sim   N - Não", "S", "N");
        } while(opcaoContinua.equalsIgnoreCase("S") && preparo.size() > 0);
        return true;
    }

    private void removerPasso(List<String> preparo) {
            int indicePasso = Integer.parseInt(ConsoleUtils.getUserInput("Digite o índice do passo a ser removido"));
            preparo.remove(indicePasso - 1);
    }

    private void adicionarPasso(List<String> preparo) {
            String nomePasso = ConsoleUtils.getUserInput("Digite o passo a ser adicionado");
            int pos = Integer.parseInt(ConsoleUtils.getUserInput("Digite a posição do passo na receita receita: "));
            preparo.add(pos - 1, nomePasso);
    }

    private boolean alteraIngrediente(List<Ingrediente> ingredientes) {
        String[] options = new String[3];
        StringBuilder sb = new StringBuilder("#".repeat(100));

        sb.append("%n").append("  + : Adicionar ingrediente %n");
        options[0] = "+";

        if (ingredientes.size() > 0) {
            sb.append("  - : Remover ingrediente  %n");
            options[1] = "-";
        }

        sb.append("  # ").append("# ".repeat(48)).append("%n");
        sb.append("  X : Sair  %n");
        options[2] = "X";
        sb.append("#".repeat(100)).append("%n");

        String opcaoContinua;
        do {
        String opcao = ConsoleUtils.getUserOption(sb.toString(), options).toUpperCase(Locale.getDefault());
        switch (opcao) {
            case "+":
                adicionarIngrediente(ingredientes);
                break;
            case "-":
                removerIngrediente(ingredientes);
                break;
            case "X":
                System.out.println("Fim da edição de ingredientes");
                return false;
            default:
                System.out.println("Opção inválida!!!");
        }
        opcaoContinua = ConsoleUtils.getUserOption("Você deseja continuar alterando os ingredientes da receita?\nS - Sim   N - Não", "S", "N");
        } while(opcaoContinua.equalsIgnoreCase("S") && ingredientes.size() > 0);
        return true;
    }

    private void removerIngrediente(List<Ingrediente> ingredientes) {
            int indiceIngrediente = Integer.parseInt(ConsoleUtils.getUserInput("Digite o índice do ingrediente a ser removido"));
            ingredientes.remove(indiceIngrediente - 1);
    }

    private void adicionarIngrediente(List<Ingrediente> ingredientes) {
            String nomeIngrediente = ConsoleUtils.getUserInput("Digite o nome do ingrediente a ser adicionado");

            // solicitar a quantidade do ingrediente
            double quantidadeIngrediente = Double.parseDouble(ConsoleUtils.getUserInput("Digite a quantidade do ingrediente"));

            // solicitar a unidade de medidada da quantidade
            StringBuilder sb = new StringBuilder("Qual a unidade da quantidade?\n");
            String[] options = new String[TipoMedida.values().length];
            for (int i = 0; i < options.length; i++) {
                options[i] = String.valueOf(i);
                sb.append(String.format("%d - %s%n", i, TipoMedida.values()[i]));
            }
            String opcaoTipoIngrediente = ConsoleUtils.getUserOption(sb.toString(), options);
            TipoMedida tipoMedidaIngrediente = null;
            for (int i = 0; i < options.length; i++) {
                if (opcaoTipoIngrediente.equalsIgnoreCase(options[i])) {
                    tipoMedidaIngrediente = TipoMedida.values()[i];
                    break;
                }
            }
            Ingrediente ingrediente = new Ingrediente(nomeIngrediente, quantidadeIngrediente, tipoMedidaIngrediente);
            int pos = ingredientes.size();
            //        ingredientes.add(ingrediente);
            ingredientes.add(pos, ingrediente);
     }

    private TipoRendimento addTipoRendimento() {
        StringBuilder sbTipoRendimento = new StringBuilder("Qual o tipo da unidade de rendimento da receita?\n");
        String[] optionsTipoRendimento = new String[TipoRendimento.values().length];
        for (int i = 0; i < optionsTipoRendimento.length; i++) {
            optionsTipoRendimento[i] = String.valueOf(i);
            sbTipoRendimento.append(String.format("%d - %s%n", i, TipoRendimento.values()[i]));
        }
        String opcaoTiporendimento = ConsoleUtils.getUserOption(sbTipoRendimento.toString(), optionsTipoRendimento);
        TipoRendimento tipoRendimento = null;
        for (int i = 0; i < optionsTipoRendimento.length; i++) {
            if (opcaoTiporendimento.equalsIgnoreCase(optionsTipoRendimento[i])) {
                tipoRendimento = TipoRendimento.values()[i];
                return tipoRendimento;
            }
        }
        return null;
    }

    private List<Ingrediente> addIngredientes(){
        List<Ingrediente> ingredientes = new ArrayList<>();

        String opcaoIngrediente = ConsoleUtils.getUserOption("Você deseja acrescentar um ingrediente a receita ?\nS - Sim   N - Não", "S", "N");
        //Se confirmar, solicita a inclusão do ingrediente à receita.
        while (opcaoIngrediente.equalsIgnoreCase("s")) {
            // solicitar o nome do ingrediente
            String nomeIngrediente = ConsoleUtils.getUserInput("Digite o nome do ingrediente");
            // solicitar a quantidade do ingrediente
            double quantidadeIngrediente = Double.parseDouble(ConsoleUtils.getUserInput("Digite a quantidade do ingrediente"));

            // solicitar a unidade de medidada da quantidade
            StringBuilder sb = new StringBuilder("Qual a unidade da quantidade?\n");
            String[] options = new String[TipoMedida.values().length];
            for (int i = 0; i < options.length; i++) {
                options[i] = String.valueOf(i);
                sb.append(String.format("%d - %s%n", i, TipoMedida.values()[i]));
            }
            String opcaoTipoIngrediente = ConsoleUtils.getUserOption(sb.toString(), options);
            TipoMedida tipoMedidaIngrediente = null;
            for (int i = 0; i < options.length; i++) {
                if (opcaoTipoIngrediente.equalsIgnoreCase(options[i])) {
                    tipoMedidaIngrediente = TipoMedida.values()[i];
                    break;
                }
            }
            Ingrediente ingrediente = new Ingrediente(nomeIngrediente, quantidadeIngrediente, tipoMedidaIngrediente);
            ingredientes.add(ingrediente);

            opcaoIngrediente = ConsoleUtils.getUserOption("Você deseja acrescentar um ingrediente a receita ?\nS - Sim   N - Não", "S", "N");
        }
        return ingredientes;
    }

    private Categoria AddCategoria() {
        StringBuilder sbCategoria = new StringBuilder("Qual a categoria da nova receita?\n");
        String[] optionsCategoria = new String[Categoria.values().length];
        for (int i = 0; i < optionsCategoria.length; i++) {
            optionsCategoria[i] = String.valueOf(i);
            sbCategoria.append(String.format("%d - %s%n", i, Categoria.values()[i]));
        }
        String opcaoCategoria = ConsoleUtils.getUserOption(sbCategoria.toString(), optionsCategoria);
        Categoria categoria = null;
        for (int i = 0; i < optionsCategoria.length; i++) {
            if (opcaoCategoria.equalsIgnoreCase(optionsCategoria[i])) {
                categoria = Categoria.values()[i];
                return categoria;
            }
        }
        return null;
    }

    private  List<String> addProcedimentos(){
        List<String> preparoAux = new ArrayList<>();

        String opcaoPreparo = ConsoleUtils.getUserOption("Você deseja acrescentar um passo à receita ?\nS - Sim   N - Não", "S", "N");
        //Se confirmar, solicita a inclusão do passo à receita.
        while (opcaoPreparo.equalsIgnoreCase("s")) {
            // solicitar a descrição do passo
            String nomePasso = ConsoleUtils.getUserInput("Descreva o passo:");
            preparoAux.add(nomePasso);

            opcaoPreparo = ConsoleUtils.getUserOption("Você deseja acrescentar um passo à receita ?\nS - Sim   N - Não", "S", "N");
        }
        return preparoAux;
    }

    public Receita edit() {
        do {
            //Exibe o layout montado.
            new ReceitaView(receita).fullView(System.out);
            //Exibe o menu de opções.
        } while (showMenu());

        new ReceitaView(receita).fullView(System.out);

        String opcaoSalvarReceita = ConsoleUtils.getUserOption("Você deseja salvar esta receita ?\nS - Sim   N - Não", "S", "N");
        if (opcaoSalvarReceita.equalsIgnoreCase("S")){
            return receita;
        } else {
            return null;
        }
    }
}
