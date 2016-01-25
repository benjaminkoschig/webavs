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
 * Classe effectuant une g�n�ration pour un dossier dans une caisse horlog�re. Elle est utilis� dans le cas d'une
 * g�n�ration manuelle d'un seul dossier effectu� par l'utilisateur et non au cours d'une g�n�ration globale ou pour un
 * affili�.
 * 
 * Contrairement � une g�n�ration globale, elle permet d'effectuer une g�n�ration si une prestation existe d�j� pour la
 * p�riode contenue dans le contexte d'ex�cution. Si c'est le cas, une extourne est effectu�e.
 * 
 * La diff�rence par rapport � la classe {@link GenPrestationDossierHorloger} est que m�me si la prestation existante a
 * l'�tat SA, elle n'est pas supprim�e et une extourne est effectu�e
 * 
 * @author jts
 */
public class GenPrestationDossierHorloger extends GenPrestationDossier {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationDossierHorloger(ContextAffilie context) {
        super(context);
    }

    /**
     * V�rifie si une prestation comptabilis�e existe d�j� pour la p�riode en cours de traitement. Si c'est le cas, on
     * ex�cute la g�n�ration d'une extourne pour la p�riode concern�e.
     * 
     * Si une prestation � l'�tat SA existe, une erreur est loggu�e
     * 
     * @param context
     *            Contexte de la g�n�ration
     * @return 1 si une extourne a �t� ex�cut�, 0 si aucune prestation n'existe, -1 si une prestation en SA existe d�j�
     *         (erreur)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    @Override
    protected int executeExtourne(ContextAffilie context) throws JadeApplicationException, JadePersistenceException {

        DetailPrestationGenComplexSearchModel search = searchExistingPrest(context);

        ArrayList<String> processed = new ArrayList<String>();
        String lastDate = null;
        String lastPeriod = null;

        // si des prestations existent, g�n�ration d'une prestation inverse
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