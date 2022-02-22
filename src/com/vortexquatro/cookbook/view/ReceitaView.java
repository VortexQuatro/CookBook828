package com.vortexquatro.cookbook.view;

import com.vortexquatro.cookbook.domain.Ingrediente;
import com.vortexquatro.cookbook.domain.Receita;

import java.io.PrintStream;

public class ReceitaView {
    private Receita receita;

    public ReceitaView(Receita receita) {

        this.receita = receita;
    }

    public void fullView(PrintStream out) {
        if (receita == null) {
            out.printf("%n%s%n%n", "Nenhuma receita encontrada!");
        } else {
            headerView(out);
            ingredientesView(out);
            preparoView(out);
        }
    }

    public void headerView(PrintStream out) {
        out.printf("%n%s%n%n", receita.getNome());
        out.printf("Categoria: %s%n", receita.getCategoria().name());
        double tempoPreparo = receita.getTempoPreparo();
        if (tempoPreparo > 0.0) {
            out.printf("Tempo de preparo: %s minutos %n", receita.getTempoPreparo());
        }
 //       out.printf("Tempo de preparo: %s minutos %n", receita.getTempoPreparo());
        if (receita.getRendimento() != null) {
            if (receita.getRendimento().getMinimo() != receita.getRendimento().getMaximo()) {
                out.printf("Rendimento: de %s à %s %s%n", receita.getRendimento().getMinimo(), receita.getRendimento().getMaximo(), receita.getRendimento().getTipo().name());
            } else {
                out.printf("Rendimento: %s %s%n", receita.getRendimento().getMinimo(), receita.getRendimento().getTipo().name());
            }
        }
    }

    public void ingredientesView(PrintStream out) {
        out.printf("%s%n", "-- Ingredientes --");
        if (receita.getIngredientes() == null || receita.getIngredientes().isEmpty()) {
            out.printf("%s%n", "Nenhum ingrediente encontrado!");
        } else {
             for (int i = 0; i < receita.getIngredientes().size(); i++) {
                out.printf("%d - %s %s de %s%n",  i+1, receita.getIngredientes().get(i).getQuantidade(),  receita.getIngredientes().get(i).getTipo(), receita.getIngredientes().get(i).getNome());
            }
        }
    }

    public void preparoView(PrintStream out) {
        out.printf("%n%s%n", "-- Modo de preparo --");
        if (receita.getPreparo() == null || receita.getPreparo().isEmpty()) {
            out.printf("%s%n", "Nenhum preparo encontrado!");
        } else {
            for (int i = 0; i < receita.getPreparo().size(); i++) {
                out.println(i+1 + " - "+ receita.getPreparo().get(i));
            }
//            for (String s : receita.getPreparo()) {
//                out.println(s);
//            }
//            receita.getPreparo().forEach(s -> {
//                out.println(s);
//            });
//            receita.getPreparo().forEach(out::println);
        }
    }
}
