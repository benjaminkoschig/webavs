package ch.globaz.vulpecula.businessimpl.services.passagefacturation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import ch.globaz.musca.business.models.PlanFacturationPassageSearchComplexModel;
import ch.globaz.vulpecula.business.services.passagefacturation.PassageFacturationServiceCRUD;

public class PassageFacturationServiceCRUDImpl implements PassageFacturationServiceCRUD {
    /**
     * Retourne un passage de facturation de type ouvert
     */
    @Override
    public PlanFacturationPassageSearchComplexModel searchPassageFacturationTO(
            PlanFacturationPassageSearchComplexModel searchModel) throws JadePersistenceException {
        // TODO ne prendre que les passages à l'état ouvert !
        if (searchModel == null) {
            throw new JadePersistenceException(
                    "Unable to search passage de facturation, the search model passed is null!");
        }

        if (searchModel.getLikeLibellePassage() != null) {
            searchModel.setLikeLibellePassage(searchModel.getLikeLibellePassage());
        }
        if (searchModel.getForIdPassage() != null) {
            searchModel.setForIdPassage(searchModel.getForIdPassage());
        }
        // On ne recherche que les passages de type TO
        searchModel.setForTypeFacturation(FAModuleFacturation.CS_MODULE_TAXATION_OFFICE);
        searchModel.setForEtat(FAPassage.CS_ETAT_OUVERT);

        return (PlanFacturationPassageSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public PlanFacturationPassageSearchComplexModel searchPassageFacturationPrestations(
            PlanFacturationPassageSearchComplexModel searchModel) throws JadePersistenceException {
        if (searchModel.getLikeLibellePassage() != null) {
            searchModel.setLikeLibellePassage(searchModel.getLikeLibellePassage().toUpperCase());
        }
        return (PlanFacturationPassageSearchComplexModel) JadePersistenceManager.search(searchModel);
    }
}
