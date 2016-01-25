package ch.globaz.common.vb;

import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * ViewBean utilisé lors de la réalisation de vues globales Cette classe
 * implémente les méthodes add(), delete() et update() afin de ne pas avoir à
 * les redéfinir
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public abstract class BJadeSearchObjectELViewBean extends BJadePersistentObjectViewBean {

    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
