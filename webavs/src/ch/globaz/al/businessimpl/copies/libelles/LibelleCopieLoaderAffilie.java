package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe de r�cup�ration de libell� de copie pour un affil�
 * 
 * @author jts
 */
public class LibelleCopieLoaderAffilie extends LibelleCopieLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de r�cup�rer le libell� � utiliser
     */
    public LibelleCopieLoaderAffilie(ContextLibellesCopiesLoader context) {
        this.context = context;
    }

    @Override
    public String getLibelle() throws JadePersistenceException, JadeApplicationException {

        PersonneEtendueComplexModel tiers = TIBusinessServiceLocator.getPersonneEtendueService().read(
                context.getIdTiers());

        return tiers.getTiers().getDesignation1() + " " + tiers.getTiers().getDesignation2();
    }
}
