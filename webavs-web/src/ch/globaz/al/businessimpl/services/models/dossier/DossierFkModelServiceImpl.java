package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.business.services.models.dossier.DossierFkModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation des service du modèle <code>DossierFkModel</code>
 * 
 * @author jts
 * 
 */
public class DossierFkModelServiceImpl extends ALAbstractBusinessServiceImpl implements DossierFkModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierFkModelService#count
     * (ch.globaz.al.business.models.dossier.DossierFkSearch)
     */
    @Override
    public int count(DossierFkSearchModel dossierFkSearch) throws JadeApplicationException, JadePersistenceException {

        if (dossierFkSearch == null) {
            throw new ALDossierModelException("DossierFkModelServiceImpl#count : dossierFkSearch is null");
        }
        return JadePersistenceManager.count(dossierFkSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierFkModelService#search
     * (ch.globaz.al.business.models.dossier.DossierFkSearch)
     */
    @Override
    public DossierFkSearchModel search(DossierFkSearchModel dossierFkSearch) throws JadeApplicationException,
            JadePersistenceException {

        if (dossierFkSearch == null) {
            throw new ALDossierModelException("DossierFkModelServiceImpl#search : dossierFkSearch is null");
        }

        return (DossierFkSearchModel) JadePersistenceManager.search(dossierFkSearch);
    }
}