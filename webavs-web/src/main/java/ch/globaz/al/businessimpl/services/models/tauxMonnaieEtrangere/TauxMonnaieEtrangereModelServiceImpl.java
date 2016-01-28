package ch.globaz.al.businessimpl.services.models.tauxMonnaieEtrangere;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.exceptions.tauxMonnaieEtrangere.ALTauxMonnaieEtrangereException;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereSearchModel;
import ch.globaz.al.business.services.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModelService;
import ch.globaz.al.businessimpl.checker.model.tauxMonnaieEtrangere.TauxMonnaieEtrangereModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistance des données du taux des monnaies étangères
 * 
 * @author PTA
 * 
 */
public class TauxMonnaieEtrangereModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        TauxMonnaieEtrangereModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tauxMonnaieEtrangere TauxMonnaieEtrangereModelService#update
     * (ch.globaz.al.business.models.tauxMonnaieEtrangere .TauxMonnaieEtrangereModel)
     */
    @Override
    public int count(TauxMonnaieEtrangereSearchModel tauxMonnaieSearch) throws JadePersistenceException,
            JadeApplicationException {

        if (tauxMonnaieSearch == null) {
            throw new ALTauxMonnaieEtrangereException(
                    "TauxMonnaieEtrangereServiceImpl#count : tauxMonnaieSearch is null");
        }

        return JadePersistenceManager.count(tauxMonnaieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.tauxMonnaieEtrangere. TauxMonnaieEtrangereModelService#create
     * (ch.globaz.al.business.models.tauxMonnaieEtrangere .TauxMonnaieEtrangereModel)
     */
    @Override
    public TauxMonnaieEtrangereModel create(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException {
        if (tauxMonnaieEtrangere == null) {
            throw new ALTauxMonnaieEtrangereException("Unable to add model (tauxModel) - the model passed is null!");
        }
        // Valide le modèle
        TauxMonnaieEtrangereModelChecker.validate(tauxMonnaieEtrangere);
        // L'ajoute en persistence
        return (TauxMonnaieEtrangereModel) JadePersistenceManager.add(tauxMonnaieEtrangere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.tauxMonnaieEtrangere. TauxMonnaieEtrangereModelService#delete
     * (ch.globaz.al.business.models.tauxMonnaieEtrangere .TauxMonnaieEtrangereModel)
     */
    @Override
    public TauxMonnaieEtrangereModel delete(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException {

        if (tauxMonnaieEtrangere == null) {
            throw new ALTauxMonnaieEtrangereException(
                    "TauxMonnaieEtrangereModelServiceImpl#delete : Unable to remove model (tauxMonnaieEtrangere) - the model passed is null!");
        }

        if (tauxMonnaieEtrangere.isNew()) {
            throw new ALTauxMonnaieEtrangereException(
                    "TauxMonnaieEtrangereModelServiceImpl#delete : Unable to remove model (tauxMonnaieEtrangere) - the model passed is new!");
        }

        // suppression en DB
        return (TauxMonnaieEtrangereModel) JadePersistenceManager.delete(tauxMonnaieEtrangere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.tauxMonnaieEtrangere. TauxMonnaieEtrangereModelService#read
     * (ch.globaz.al.business.models.tauxMonnaieEtrangere .TauxMonnaieEtrangereModel)
     */
    @Override
    public TauxMonnaieEtrangereModel read(String idTauxMonnaierEtrangere) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idTauxMonnaierEtrangere)) {
            throw new ALTauxMonnaieEtrangereException(
                    "Unable to read model (TauxMonnaierEtrangere) - the id passed is not defined!");
        }
        TauxMonnaieEtrangereModel tauxMonnaieEtrangereModel = new TauxMonnaieEtrangereModel();
        tauxMonnaieEtrangereModel.setId(idTauxMonnaierEtrangere);
        return (TauxMonnaieEtrangereModel) JadePersistenceManager.read(tauxMonnaieEtrangereModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.tauxMonnaieEtrangere. TauxMonnaieEtrangereModelService#search
     * (ch.globaz.al.business.models.tauxMonnaieEtrangere .TauxMonnaieEtrangereSearchModel)
     */
    @Override
    public TauxMonnaieEtrangereSearchModel search(TauxMonnaieEtrangereSearchModel tauxMonnaieEtrangereSearch)
            throws JadeApplicationException, JadePersistenceException {
        if (tauxMonnaieEtrangereSearch == null) {
            throw new ALTauxMonnaieEtrangereException(
                    "TauxMonnaieEtrangereServiceImpl#search : tauxMonnaieEtrangereSearch is null");
        }

        return (TauxMonnaieEtrangereSearchModel) JadePersistenceManager.search(tauxMonnaieEtrangereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.tauxMonnaieEtrangere. TauxMonnaieEtrangereModelService#update
     * (ch.globaz.al.business.models.tauxMonnaieEtrangere .TauxMonnaieEtrangereModel)
     */
    @Override
    public TauxMonnaieEtrangereModel update(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException {
        if (tauxMonnaieEtrangere == null) {
            throw new ALTauxMonnaieEtrangereException(
                    "TauxMonnaieEtrangereServiceImpl#update: Unable to update model (tauxMonnaieEtrangere) - the model passed is null!");
        }
        if (tauxMonnaieEtrangere.isNew()) {
            throw new ALDossierModelException(
                    "TauxMonnaieEtrangereServiceImpl#update: Unable to update model (tauxMonnaieEtrangere) - the model passed is new!");
        }
        // Valide l'integrity des données
        TauxMonnaieEtrangereModelChecker.validate(tauxMonnaieEtrangere);

        // Le met à jour en DB
        return (TauxMonnaieEtrangereModel) JadePersistenceManager.update(tauxMonnaieEtrangere);
    }

}
