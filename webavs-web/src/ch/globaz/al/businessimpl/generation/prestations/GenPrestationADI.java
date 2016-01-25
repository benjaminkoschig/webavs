package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe effectuant une g�n�ration pour un dossier ADI. Elle est utilis� dans le cas d'une g�n�ration de prestation
 * d�finitive pour un dossier ADI.
 * 
 * Si une prestation existe d�j� pour la p�riode contenue dans le contexte d'ex�cution. Si c'est le cas, une extourne
 * est effectu�e.
 * 
 * @author jts
 * 
 */
public class GenPrestationADI extends GenPrestationDossier {

    /**
     * Liste des calculs pour chaque droit de chaque p�riode (mois) � g�n�rer
     */
    private HashMap<String, ArrayList<CalculBusinessModel>> details;

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationADI(ContextAffilie context) {
        super(context);
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationADI#execute : context is null");
        }

        if (executeExtourne(context) >= 0) {
            executeADI();
        }
    }

    /**
     * Ex�cute la g�n�ration d'une prestation ADI
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void executeADI() throws JadeApplicationException, JadePersistenceException {

        String periode = context.getContextDossier().getNextPeriode();

        // g�n�ration pour chaque p�riode
        while (periode != null) {

            ArrayList<CalculBusinessModel> droitsPeriode = details.get(periode);

            if (droitsPeriode == null) {
                throw new ALGenerationException("GenPrestationADI#execute : calcul for period " + periode
                        + " is missing. Generation aborted");
            }

            // traitement des droits pour la p�riode en cours
            for (CalculBusinessModel droit : droitsPeriode) {
                addDetailPrestation(context, droit);
            }

            periode = context.getContextDossier().getNextPeriode();
        }
    }

    /**
     * 
     * @param details
     *            Liste contenant les d�tails de prestation � g�n�rer
     */
    public void setDetailsList(HashMap<String, ArrayList<CalculBusinessModel>> details) {
        this.details = details;
    }
}
