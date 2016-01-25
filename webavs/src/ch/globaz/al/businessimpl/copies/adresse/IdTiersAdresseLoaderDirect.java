package ch.globaz.al.businessimpl.copies.adresse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * Permet de r�cup�rer l'id de tiers adresse dans le cas d'un dossier en paiement direct
 * 
 * @author jts
 * 
 */
public class IdTiersAdresseLoaderDirect extends IdTiersAdresseLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de r�cup�rer l'id tiers � utiliser pour l'adresse
     */
    public IdTiersAdresseLoaderDirect(ContextIdTiersAdresseCopiesLoader context) {
        this.context = context;
    }

    @Override
    public String getIdTiersAdresse() throws JadePersistenceException, JadeApplicationException {

        String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                context.getDossier().getDossierModel().getNumeroAffilie());

        if (context.getIdTiers().equals(idTiersAffilie)) {
            return idTiersAffilie;
        } else {

            return context.getDossier().getDossierModel().getIdTiersBeneficiaire();
        }
    }
}
