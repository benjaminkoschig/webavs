package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextDossier;

/**
 * Classe effectuant une génération pour un dossier dans une caisse horlogère. Elle est utilisé dans le cas d'une
 * génération manuelle d'un seul dossier effectué par l'utilisateur et non au cours d'une génération globale ou pour un
 * affilié.
 * 
 * Contrairement à une génération globale, elle permet d'effectuer une génération si une prestation existe déjà pour la
 * période contenue dans le contexte d'exécution. Si c'est le cas, une extourne est effectuée.
 * 
 * La différence par rapport à la classe {@link GenPrestationDossierHorloger} est que même si la prestation existante a
 * l'état SA, elle n'est pas supprimée et une extourne est effectuée
 * 
 * @author jts
 */
public class GenPrestationDossierHorloger extends GenPrestationDossier {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
     */
    public GenPrestationDossierHorloger(ContextAffilie context) {
        super(context);
    }

    /**
     * Vérifie si une prestation comptabilisée existe déjà pour la période en cours de traitement. Si c'est le cas, on
     * exécute la génération d'une extourne pour la période concernée.
     * 
     * Si une prestation à l'état SA existe, une erreur est logguée
     * 
     * @param context
     *            Contexte de la génération
     * @return 1 si une extourne a été exécuté, 0 si aucune prestation n'existe, -1 si une prestation en SA existe déjà
     *         (erreur)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    @Override
    protected int executeExtourne(ContextAffilie context) throws JadeApplicationException, JadePersistenceException {

        DetailPrestationGenComplexSearchModel search = searchExistingPrest(context);

        ArrayList<String> processed = new ArrayList<String>();
        String lastDate = null;
        String lastPeriod = null;

        // si des prestations existent, génération d'une prestation inverse
        if (search.getSize() > 0) {
            for (int i = 0; i < search.getSize(); i++) {
                DetailPrestationGenComplexModel oldPrest = (DetailPrestationGenComplexModel) search.getSearchResults()[i];

                if (JadeStringUtil.isBlank(lastPeriod)) {
                    lastPeriod = oldPrest.getPeriodeValidite();
                }

                if (JadeStringUtil.isBlank(lastDate)) {
                    lastDate = oldPrest.getDateVersement();
                }

                if (!oldPrest.getPeriodeValidite().equals(lastPeriod) || !oldPrest.getDateVersement().equals(lastDate)) {
                    lastDate = oldPrest.getDateVersement();
                    processed.add(lastPeriod);
                    lastPeriod = oldPrest.getPeriodeValidite();
                }

                RecapitulatifEntrepriseModel recap = ALServiceLocator.getRecapitulatifEntrepriseModelService().read(
                        oldPrest.getIdRecap());

                if (!JadeNumericUtil.isEmptyOrZero(recap.getIdProcessusPeriodique())
                        || !ALCSPrestation.ETAT_SA.equals(oldPrest.getEtatPrestation())) {

                    if (!processed.contains(lastPeriod)) {
                        context = addExtourne(context, oldPrest);
                    }
                } else if (ALCSPrestation.ETAT_SA.equals(oldPrest.getEtatPrestation())) {

                    EntetePrestationModel entete = ALServiceLocator.getEntetePrestationModelService().read(
                            oldPrest.getIdEntete());

                    JadeThread.logError(ContextDossier.class.getName(), "al.generation.warning.prestationExistante",
                            new String[] { entete.getPeriodeDe() + " - " + entete.getPeriodeA() });

                    return -1;
                }
            }

            context.savePrestations();
            return 1;
        }

        return 0;
    }
}