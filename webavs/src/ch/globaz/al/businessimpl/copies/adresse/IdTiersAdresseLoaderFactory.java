package ch.globaz.al.businessimpl.copies.adresse;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Factory permettant de déterminer le loader à utiliser pour déterminer l'idTiers devant être utiliser pour charger une
 * adresse à afficher sur un document
 * 
 * @author jts
 * 
 */
public class IdTiersAdresseLoaderFactory {

    /**
     * Retourne une instance de loader permettant de récupérer la liste des copies par défaut
     * 
     * @param context
     *            Contexte contenant les informations permettant de déterminer le loader à utiliser
     * @return instance de loader permettant de récupérer la liste des copies par défaut
     * @throws JadeApplicationException
     *             Exception levée si l'id tiers de l'affilié n'a pas pu être récupéré
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
