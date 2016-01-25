package ch.globaz.al.businessimpl.copies.adresse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * Permet de récupérer l'id de tiers adresse dans le cas d'un dossier en paiement indirect
 * 
 * @author jts
 * 
 */
public class IdTiersAdresseLoaderIndirect extends IdTiersAdresseLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de récupérer l'id tiers à utiliser pour l'adresse
     */
    public IdTiersAdresseLoaderIndirect(ContextIdTiersAdresseCopiesLoader context) {
        this.context = context;
    }

    @Override
    public String getIdTiersAdresse() throws JadePersistenceException, JadeApplicationException {

        return AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                context.getDossier().getDossierModel().getNumeroAffilie());
    }
}
