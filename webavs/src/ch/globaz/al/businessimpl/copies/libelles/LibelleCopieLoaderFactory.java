package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Factory permettant de d�terminer le loader � utiliser pour charger les libell�s de copie
 * 
 * @author jts
 */
public class LibelleCopieLoaderFactory {

    /**
     * Retourne une instance de loader permettant de r�cup�rer la liste des copies par d�faut
     * 
     * @param context
     *            Contexte contenant les informations permettant de d�terminer le loader � utiliser
     * @return instance de loader permettant de r�cup�rer la liste des copies par d�faut
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static LibelleCopieLoaderAbstract getLibelleCopieLoader(ContextLibellesCopiesLoader context)
            throws JadeApplicationException, JadePersistenceException {

        if (ALCSCopie.TYPE_DECISION.equals(context.getTypeCopie())) {

            // si indirect et que le destinataire = allocataire ET que dossier
            // salari�
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
                // Affili�
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
