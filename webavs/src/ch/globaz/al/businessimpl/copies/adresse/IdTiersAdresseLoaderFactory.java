package ch.globaz.al.businessimpl.copies.adresse;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Factory permettant de d�terminer le loader � utiliser pour d�terminer l'idTiers devant �tre utiliser pour charger une
 * adresse � afficher sur un document
 * 
 * @author jts
 * 
 */
public class IdTiersAdresseLoaderFactory {

    /**
     * Retourne une instance de loader permettant de r�cup�rer la liste des copies par d�faut
     * 
     * @param context
     *            Contexte contenant les informations permettant de d�terminer le loader � utiliser
     * @return instance de loader permettant de r�cup�rer la liste des copies par d�faut
     * @throws JadeApplicationException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     */
    public static IdTiersAdresseLoaderAbstract getIdTiersAdresseLoader(ContextIdTiersAdresseCopiesLoader context)
            throws JadeApplicationException {

        // paiement direct
        if (ALServiceLocator.getDossierBusinessService().isModePaiementDirect(context.getDossier().getDossierModel())) {
            return new IdTiersAdresseLoaderDirect(context);
            // paiement indirect
        } else {
            return new IdTiersAdresseLoaderIndirect(context);
        }
    }
}
