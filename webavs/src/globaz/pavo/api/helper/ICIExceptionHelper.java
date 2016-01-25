package globaz.pavo.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.pavo.api.ICIException;

public class ICIExceptionHelper extends GlobazHelper implements ICIException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ICIExceptionHelper() {
        super("globaz.pavo.db.compte.CIExceptions");
    }

    /**
     * Constructeur du type ICIEcritureHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICIExceptionHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICIEcritureHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICIExceptionHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public void setAffilie(String numeroAffilie) {
        _getValueObject().setProperty("affilie", numeroAffilie);

    }

    @Override
    public void setDateEngagement(String dateEngagement) {
        _getValueObject().setProperty("dateEngagement", dateEngagement);

    }

    @Override
    public void setIsJsp(String isJsp) {
        _getValueObject().setProperty("isJsp", isJsp);

    }

    @Override
    public void setLangueCorrespondance(String langueCorrespondance) {
        // TODO Auto-generated method stub
        _getValueObject().setProperty("langueCorrespondance", langueCorrespondance);
    }

    @Override
    public void setNumeroAvs(String numeroAvs) {
        _getValueObject().setProperty("numeroAvs", numeroAvs);

    }

}
