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
 * Classe de génération pour les dossiers unitaires (jour ou heure). Elle est utilisée pendant une génération de dossier
 * 
 * @author jts
 */
public class GenPrestationUnitaire extends GenPrestationAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
     */
    public GenPrestationUnitaire(ContextAffilie context) {
        super(context);
    }

    /**
     * @param context
     *            contexte de la génération
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void deletePrestationZero(ContextAffilie context) throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationUnitaire#deletePrestationZero : context is null");
        }

        // suppression des prestations de 0 pour la période concernée
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
