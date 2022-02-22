package com.vortexquatro.cookbook;

import com.vortexquatro.cookbook.controller.Catalogo;
import com.vortexquatro.cookbook.domain.Receita;
import com.vortexquatro.cookbook.enums.Categoria;
import com.vortexquatro.cookbook.view.CatalogoView;

public class CookBook {
    public static void main(String[] args) {
        Catalogo catalogo = new Catalogo();
        catalogo.add(new Receita("Cookies da Lara 1", Categoria.DOCE));
        catalogo.add(new Receita("Cookies da Lara 2", Categoria.DOCE));
        catalogo.add(new Receita("Cookies da Lara 3", Categoria.DOCE));
        catalogo.add(new Receita("Cookies da Lara 4", Categoria.DOCE));
        CatalogoView view = new CatalogoView(catalogo);
        view.view();
    }
}
