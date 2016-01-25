package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Classe de r�cup�ration de libell� de copie pour un travailleur agricole
 * 
 * @author jts
 */
public class LibelleCopieLoaderTravailleurAgricole extends LibelleCopieLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de r�cup�rer le libell� � utiliser
     */
    public LibelleCopieLoaderTravailleurAgricole(ContextLibellesCopiesLoader context) {
        this.context = context;
    }

    @Override
    public String getLibelle() throws JadePersistenceException, JadeApplicationException {
        return JadeThread.getMessage("al.documentCommun.copie.salarie");
    }

}
