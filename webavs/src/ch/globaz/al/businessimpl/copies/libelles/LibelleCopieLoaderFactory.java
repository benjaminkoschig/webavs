package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Factory permettant de déterminer le loader à utiliser pour charger les libellés de copie
 * 
 * @author jts
 */
public class LibelleCopieLoaderFactory {

    /**
     * Retourne une instance de loader permettant de récupérer la liste des copies par défaut
     * 
     * @param context
     *            Contexte contenant les informations permettant de déterminer le loader à utiliser
     * @return instance de loader permettant de récupérer la liste des copies par défaut
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static LibelleCopieLoaderAbstract getLibelleCopieLoader(ContextLibellesCopiesLoader context)
            throws JadeApplicationException, JadePersistenceException {

        if (ALCSCopie.TYPE_DECISION.equals(context.getTypeCopie())) {

            // si indirect et que le destinataire = allocataire ET que dossier
            // salarié
            if (!ALServiceLocator.getDossierBusinessService().isModePaiementDirect(
                    context.getDossier().getDossierModel())
                    && ALServiceLocator.getDossierBusinessService().isAllocataire(context.getDossier(),
                            context.getIdTiers())
                    && ALCSDossier.ACTIVITE_SALARIE.equals(context.getDossier().getDossierModel()
                            .getActiviteAllocataire())) {
                return new LibelleCopieLoaderSalarie(context);
                // Travailleur agricole
            } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(context.getDossier().getDossierModel()
                    .getActiviteAllocataire())
                    && ALServiceLocator.getDossierBusinessService().isAllocataire(context.getDossier(),
                            context.getIdTiers())) {
                return new LibelleCopieLoaderTravailleurAgricole(context);
                // Affilié
            } else if (ALServiceLocator.getDossierBusinessService().isAffilie(context.getDossier(),
                    context.getIdTiers())) {
                return new LibelleCopieLoaderAffilie(context);

            } else if (ALImplServiceLocator.getDossierBusinessService().isAgenceCommunale(context.getIdTiers())) {
                return new LibelleCopieLoaderAgenceCommunale(context);
                // autres cas
            } else {
                return new LibelleCopieLoader(context);
            }
        } else {
            return new LibelleCopieLoader(context);
        }
    }
}
