package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierListComplexModelException;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexSearchModel;
import ch.globaz.al.business.services.models.dossier.DossierListComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * class d'implémentation des services de DossierDétailComplex
 * 
 * @author PTA
 */
public class DossierListComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DossierListComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierListComplexModelService #read(java.lang.String)
     */
    @Override
    public DossierListComplexModel read(String idDossierListModel) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDossierListModel)) {
            throw new ALDossierListComplexModelException(
                    "unable to read dossierListComplexModel- the id passed is empty");

        }
        DossierListComplexModel dossierListComplexModel = new DossierListComplexModel();
        dossierListComplexModel.setId(idDossierListModel);

        return (DossierListComplexModel) JadePersistenceManager.read(dossierListComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierListComplexModelService #
     * search(ch.globaz.al.business.models.dossier.DossierListComplexSearchModel )
     */
    @Override
    public DossierListComplexSearchModel search(DossierListComplexSearchModel dossierListComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (dossierListComplexSearchModel == null) {
            throw new ALDossierListComplexModelException(
                    "unable to search dossierDetailComplexModel- the model passed is null");
        }
        return (DossierListComplexSearchModel) JadePersistenceManager.search(dossierListComplexSearchModel);
    }
}
