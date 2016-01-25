package ch.globaz.al.businessimpl.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.adi.AdiDecompteComplexSearchModel;
import ch.globaz.al.business.services.models.adi.AdiDecompteComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation des services de la persistance de AdiDecompteComplexModel
 * 
 * @author PTA
 * 
 */
public class AdiDecompteComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AdiDecompteComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiDecompteComplexModelService #read(java.lang.String)
     */
    @Override
    public AdiDecompteComplexModel read(String idDecompteComplexModel) throws JadeApplicationException,
            JadePersistenceException {
        if (idDecompteComplexModel == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteComplexSearchModel#read unable to read , idDecompteComplexModel is null");
        }
        AdiDecompteComplexModel model = new AdiDecompteComplexModel();
        model.setId(idDecompteComplexModel);
        return (AdiDecompteComplexModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiDecompteComplexModelService
     * #search(ch.globaz.al.business.models.adi.AdiDecompteComplexSearchModel)
     */
    @Override
    public AdiDecompteComplexSearchModel search(AdiDecompteComplexSearchModel decompteGlobal)
            throws JadeApplicationException, JadePersistenceException {
        if (decompteGlobal == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteComplexSearchModel#search unable to search , decompteGlobal is null");
        }
        return (AdiDecompteComplexSearchModel) JadePersistenceManager.search(decompteGlobal);
    }

}
