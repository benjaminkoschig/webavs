/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Conjoint;
import ch.globaz.perseus.business.models.situationfamille.SimpleSituationFamiliale;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.situationfamille.SituationFamilialeService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class SituationFamilialeServiceImpl extends PerseusAbstractServiceImpl implements SituationFamilialeService {
    @Override
    public SituationFamiliale addConjoint(SituationFamiliale situationFamiliale, String newIdTiers)
            throws JadePersistenceException, SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException(
                    "Unable to add conjoint in situationFamiliale, the given model is null!");
        }
        if (JadeStringUtil.isEmpty(newIdTiers)) {
            throw new SituationFamilleException(
                    "Unable to add conjoint in situationFamiliale, new newIdTiers is empty!");
        }
        try {
            // Création du nouveau conjoint
            Conjoint newConjoint = new Conjoint();
            newConjoint.getMembreFamille().getSimpleMembreFamille().setIdTiers(newIdTiers);
            newConjoint = PerseusServiceLocator.getConjointService().create(newConjoint);
            situationFamiliale.setConjoint(newConjoint);
            situationFamiliale.getSimpleSituationFamiliale().setIdConjoint(newConjoint.getId());

            return PerseusServiceLocator.getSituationFamilialeService().update(situationFamiliale);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available in change situation familiale : "
                    + e.getMessage(), e);
        }
    }

    @Override
    public SituationFamiliale changeConjoint(SituationFamiliale situationFamiliale, String newIdTiers, String idDemande)
            throws JadePersistenceException, SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException(
                    "Unable to change conjoint in situationFamiliale, the given model is null!");
        }
        // On ne fait plus le contrôle puisque la rente pont n'a pas d'id demande
        // if (JadeStringUtil.isEmpty(idDemande)) {
        // throw new SituationFamilleException("Unable to change conjoint in situationFamiliale, idDemande is empty!");
        // }
        // Suppression des données financières de l'ancien conjoint
        try {
            // Si idDemande n'est pas null (il s'agit d'une demande pc famille et non pas demande de rente-pont)
            if (!JadeStringUtil.isEmpty(idDemande)) {
                PerseusServiceLocator.getDonneeFinanciereService().deleteForDemandeAndMembreFamille(idDemande,
                        situationFamiliale.getConjoint().getMembreFamille().getId());
            }
            // Si on desire supprimer le conjoint
            if (JadeStringUtil.isEmpty(newIdTiers)) {
                situationFamiliale.getSimpleSituationFamiliale().setIdConjoint(null);

                return PerseusServiceLocator.getSituationFamilialeService().update(situationFamiliale);
            }
        } catch (DonneesFinancieresException e) {
            throw new SituationFamilleException(
                    "DonneesFinancieresException, Unable to delete donneesFinancieres for old conjoint : "
                            + e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available : " + e.getMessage(), e);
        }

        return addConjoint(situationFamiliale, newIdTiers);
    }

    @Override
    public SituationFamiliale create(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException("Unable to create situationFamiliale, the given model is null!");
        }

        SimpleSituationFamiliale simpleSituationFamiliale = situationFamiliale.getSimpleSituationFamiliale();
        simpleSituationFamiliale.setIdConjoint(situationFamiliale.getConjoint().getId());
        simpleSituationFamiliale.setIdRequerant(situationFamiliale.getRequerant().getId());

        try {
            simpleSituationFamiliale = PerseusImplServiceLocator.getSimpleSituationFamilialeService().create(
                    simpleSituationFamiliale);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        }

        situationFamiliale.setSimpleSituationFamiliale(simpleSituationFamiliale);

        return situationFamiliale;
    }

    @Override
    public SituationFamiliale delete(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException("Unable to delete situationFamiliale, the given model is null!");
        }
        try {
            situationFamiliale.setSimpleSituationFamiliale(PerseusImplServiceLocator
                    .getSimpleSituationFamilialeService().delete(situationFamiliale.getSimpleSituationFamiliale()));
            // Suppression des enfants dans la table associative
            PerseusImplServiceLocator.getEnfantFamilleService().deleteForSituationFamiliale(situationFamiliale.getId());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        }

        return situationFamiliale;
    }

    @Override
    public SituationFamiliale read(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException {
        if (JadeStringUtil.isEmpty(idSituationFamiliale)) {
            throw new SituationFamilleException("Unable to read a situationFamiliale, the id passed is null!");
        }
        SituationFamiliale situationFamiliale = new SituationFamiliale();
        situationFamiliale.setId(idSituationFamiliale);

        return (SituationFamiliale) JadePersistenceManager.read(situationFamiliale);
    }

    @Override
    public SituationFamiliale update(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException("Unable to update situationFamiliale, the given model is null!");
        }

        try {
            situationFamiliale.setSimpleSituationFamiliale(PerseusImplServiceLocator
                    .getSimpleSituationFamilialeService().update(situationFamiliale.getSimpleSituationFamiliale()));

            // On efface tout ce qui est influencé par le changement
            DemandeSearchModel searchModel = new DemandeSearchModel();
            searchModel.setForIdSituationFamiliale(situationFamiliale.getId());
            searchModel = PerseusServiceLocator.getDemandeService().search(searchModel);
            if (searchModel.getSize() == 1) {
                Demande demande = (Demande) searchModel.getSearchResults()[0];
                PerseusServiceLocator.getDemandeService().updateAndClean(demande, false);
            } else {
                // Il s'agit d'une demande de rente-pont
                // throw new SituationFamilleException(
                // "Unable to update situationFamiliale, cannot find demande for situationFamiliale");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.toString(), e);
        } catch (DemandeException e) {
            throw new SituationFamilleException("DemandeException during siutaionFamiliale update - " + e.toString(), e);
        }

        return situationFamiliale;
    }
}
