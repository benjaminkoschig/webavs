package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe de récupération de libellé de copie pour une agence communale
 * 
 * @author jts
 */
public class LibelleCopieLoaderAgenceCommunale extends LibelleCopieLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de récupérer le libellé à utiliser
     */
    public LibelleCopieLoaderAgenceCommunale(ContextLibellesCopiesLoader context) {
        this.context = context;
    }

    @Override
    public String getLibelle() throws JadePersistenceException, JadeApplicationException {

        AdministrationComplexModel tiers = TIBusinessServiceLocator.getAdministrationService().read(
                context.getIdTiers());

        return tiers.getTiers().getDesignation1() + " " + tiers.getTiers().getDesignation2();
    }
}
