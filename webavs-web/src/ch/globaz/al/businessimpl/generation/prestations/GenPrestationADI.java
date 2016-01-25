package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe effectuant une génération pour un dossier ADI. Elle est utilisé dans le cas d'une génération de prestation
 * définitive pour un dossier ADI.
 * 
 * Si une prestation existe déjà pour la période contenue dans le contexte d'exécution. Si c'est le cas, une extourne
 * est effectuée.
 * 
 * @author jts
 * 
 */
public class GenPrestationADI extends GenPrestationDossier {

    /**
     * Liste des calculs pour chaque droit de chaque période (mois) à générer
     */
    private HashMap<String, ArrayList<CalculBusinessModel>> details;

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
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
     * Exécute la génération d'une prestation ADI
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void executeADI() throws JadeApplicationException, JadePersistenceException {

        String periode = context.getContextDossier().getNextPeriode();

        // génération pour chaque période
        while (periode != null) {

            ArrayList<CalculBusinessModel> droitsPeriode = details.get(periode);

            if (droitsPeriode == null) {
                throw new ALGenerationException("GenPrestationADI#execute : calcul for period " + periode
                        + " is missing. Generation aborted");
            }

            // traitement des droits pour la période en cours
            for (CalculBusinessModel droit : droitsPeriode) {
                addDetailPrestation(context, droit);
            }

            periode = context.getContextDossier().getNextPeriode();
        }
    }

    /**
     * 
     * @param details
     *            Liste contenant les détails de prestation à générer
     */
    public void setDetailsList(HashMap<String, ArrayList<CalculBusinessModel>> details) {
        this.details = details;
    }
}
