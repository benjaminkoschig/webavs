package ch.globaz.common.vb;

import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * ViewBean utilis� lors de la r�alisation de vues globales Cette classe
 * impl�mente les m�thodes add(), delete() et update() afin de ne pas avoir �
 * les red�finir
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 6 janv. 2014
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
