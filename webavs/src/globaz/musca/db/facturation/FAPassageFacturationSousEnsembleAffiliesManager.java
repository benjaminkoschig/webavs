package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager récupérant le sous-ensemble d'affiliés liés à un passage donné.
 * 
 * @author JSI
 * 
 */
public class FAPassageFacturationSousEnsembleAffiliesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassage = "";

    @Override
    protected String _getWhere(BStatement statement) {

        String where = _getCollection() + "FAFIAFP.IDPASSAGE=" + getIdPassage();
        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAPassageFacturationSousEnsembleAffiliesEntity();
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }
}
