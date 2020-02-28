package ch.globaz.al.businessimpl.generation.prestations;

import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.models.prestation.EntetePrestationModelService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.List;

/**
 * Classe de g�n�ration pour les dossiers unitaires (jour ou heure). Elle est utilis�e pendant une g�n�ration de dossier
 * 
 * @author jts
 */
public class GenPrestationUnitaire extends GenPrestationAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationUnitaire(ContextAffilie context) {
        super(context);
    }

    /**
     * @param context
     *            contexte de la g�n�ration
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private void deletePrestationZero(ContextAffilie context) throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationUnitaire#deletePrestationZero : context is null");
        }

        // suppression des prestations de 0 pour la p�riode concern�e
        EntetePrestationSearchModel search = new EntetePrestationSearchModel();
        search.setForIdDossier(context.getContextDossier().getDossier().getId());
        search.setForMontantTotal("0");
        search.setForPeriode(context.getContextDossier().getCurrentPeriode());
        search.setWhereKey("prestationZeroExistante");

        EntetePrestationModelService service = ALImplServiceLocator.getEntetePrestationModelService();

        search = service.search(search);

        for (int i = 0; i < search.getSize(); i++) {
            service.delete(((EntetePrestationModel) search.getSearchResults()[0]));
        }
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationUnitaire#execute : context is null");
        }

        List<CalculBusinessModel> calcul = context.getContextDossier().getCalcul();
        deletePrestationZero(context);
        if (calcul != null) {
            generatePrestation(context, calcul);
        }

    }
}
