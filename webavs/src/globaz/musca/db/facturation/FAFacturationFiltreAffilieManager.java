package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class FAFacturationFiltreAffilieManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassage;

    @Override
    protected String _getFrom(BStatement statement) {
        String from = _getCollection() + "FAFIAFP";
        return from;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String where = _getCollection() + "FAFIAFP.IDPASSAGE=" + getIdPassage();
        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAFacturationFiltreAffilieEntity();
    }

    private String getIdPassage() {
        return idPassage;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }
}
