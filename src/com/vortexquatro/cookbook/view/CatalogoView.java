package com.vortexquatro.cookbook.view;

import com.vortexquatro.cookbook.controller.Catalogo;
import com.vortexquatro.cookbook.domain.Receita;
import com.vortexquatro.cookbook.enums.Categoria;

import java.util.Locale;

public class CatalogoView {
    private Catalogo controller;
    private Receita ative;
    private int currentIndex;

    public CatalogoView(Catalogo controller) {
        this.controller = controller;
        if (controller.getTotal() > 0) {
            currentIndex = 1;
            ative = controller.getReceita(currentIndex);
        } else {
            currentIndex = 0;
            ative = null;
        }
    }

    private boolean showMenu() {
        String[] options = new String[7];
        StringBuilder sb = new StringBuilder("#".repeat(100));

        sb.append("%n").append("  + : Adicionar  %n");
        options[0] = "+";

        if (ative != null) {
            sb.append("  E : Editar  %n");
            options[1] = "E";
            sb.append("  - : Remover  %n");
            options[2] = "-";
        }

        if (controller.getTotal() > 1) {
            sb.append("  P : Próxima  %n");
            options[3] = "P";
            sb.append("  A : Anterior  %n");
            options[4] = "A";
            sb.append("  L : Localizar  %n");
            options[5] = "L";
        }

        sb.append("  # ").append("# ".repeat(48)).append("%n");
        sb.append("  X : Sair  %n");
        options[6] = "X";
        sb.append("#".repeat(100)).append("%n");

        String opcao = ConsoleUtils.getUserOption(sb.toString(), options).toUpperCase(Locale.getDefault());
        switch (opcao) {
            case "+":
                add();
                break;
            case "E":
                edit();
                break;
            case "-":
                del();
                break;
            case "P":
                next();
                break;
            case "A":
                previous();
                break;
            case "L":
                find();
                break;
            case "X":
                System.out.println("Obrigado!!");
                return false;
            default:
                System.out.println("Opção inválida!!!");
        }
        return true;
    }

    private void find() {
        //Capturar o nome da receita.
        String name = ConsoleUtils.getUserInput("Qual o nome da receita que deseja localizar?");
        //Procura no Catalogo a receita com o mesmo nome.
        ative = controller.getReceita(name);
        currentIndex = 0;
    }

    private void next() {
        //Se estiver com uma receita ativa, ativa a próxima receita.
        //Se NÃO estiver com uma receita ativa, ativa a primeira receita.
        if (ative != null) currentIndex++;
        try {
            ative = controller.getReceita(currentIndex);
        } catch (IllegalArgumentException e) {
            ative = null;
        }
        if (ative == null) {
            currentIndex = 1;
            ative = controller.getReceita(currentIndex);
        }
    }

    private void previous() {
        //Se estiver com uma receita ativa, ativa a anterior.
        //Se NÃO estiver com uma receita ativa, ativa a última receita.
        if (ative != null) currentIndex--;
        try {
            ative = controller.getReceita(currentIndex);
        } catch (IllegalArgumentException e) {
            ative = null;
        }
        if (ative == null) {
            currentIndex = controller.getTotal();
            ative = controller.getReceita(currentIndex);
        }
    }

    private void del() {
        //Se NÃO estiver com uma receita ativa, mostra mensagem.
        //Se estiver com uma receita ativa, confirma a operação.
        String opcao = ConsoleUtils.getUserOption("Você deseja realmente APAGAR a receita " + ative.getNome() + "?\nS - Sim   N - Não", "S", "N");
        //Se confirmar, solicita ao Catalogo apagar a receita.
        if (opcao.equalsIgnoreCase("S")) {
            controller.del(ative.getNome());
            ative = null;
            if (controller.getTotal() > 0) {
                currentIndex = 0;
                next();
            }
        }
    }

    private void edit() {
        //Se NÃO estiver com uma receita ativa, mostra mensagem.
        //Se estiver com uma receita ativa, abra a tela de edição.
        Receita nova = new EditReceitaView(ative).edit();
        if (nova != null) {
            controller.del(ative.getNome());
            controller.add(nova);
            //Torna a nova receita a ativa.
            ative = nova;
            currentIndex = 0;
        }
    }

    private void add() {
        //Capturar o nome da receita.
        String name = ConsoleUtils.getUserInput("Qual o nome da nova receita?");
        if (!name.isBlank()) {
            //Procura no Catalogo a receita com o mesmo nome.
            Receita other = controller.getReceita(name);
            //Se encontrar, mostra mensagem.
            if (other != null) {
                String opcao = ConsoleUtils.getUserOption("Receita já existente!%nVocê deseja visualizar?%nS - Sim   N - Não", "S", "N");
                //Se confirmar, solicita ao Catalogo apagar a receita.
                if (opcao.equalsIgnoreCase("S")) {
                    ative = other;
                }
            } else {
                //Se NÃO encontrar continua.
                //Capturar dados da nova receita.
                Categoria categoria = AddCategoria();

                //Cria uma nova receita e edita demais componentes.
                Receita nova = new EditReceitaView(new Receita(name, categoria)).edit();
                if (nova != null) {
                    //Passa a receita para o Catalogo adicionar.
                    controller.add(nova);
                    //Torna a nova receita a ativa.
                    ative = nova;
                    currentIndex = 0;
                }
            }
        }
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

    public void view() {
        do {
            //Exibe o layout montado.
            new ReceitaView(ative).fullView(System.out);
            //Exibe o menu de opções.
        } while (showMenu());
    }
}
