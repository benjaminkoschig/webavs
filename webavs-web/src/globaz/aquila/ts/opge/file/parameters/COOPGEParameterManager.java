package globaz.aquila.ts.opge.file.parameters;

import globaz.globall.db.BEntity;
import globaz.globall.db.FWFindParameterManager;

/**
 * @author dda
 */
public class COOPGEParameterManager extends FWFindParameterManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.FWFindParameterManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COOPGEParameter();
    }

    /**
     * Set les paramètres par défaut pour la recherche du mot de passe FTP Postfinance.
     */
    public void setDefaultParameter() {
        setIdApplParametre(getSession().getApplicationId());
        setIdCodeSysteme("0");
        setIdCleDiffere(COOPGEParameter.PARAM_NUMERO_PLAGE_LAST_POURSUITE);

        setIdActeurParametre("0");
        setPlageValDeParametre("0");
    }

}
