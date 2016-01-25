package globaz.ccvd.services.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class CCVDEvtRegFGISrcManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNIP = new String();

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(getForNIP())) {
            _addError(transaction, "Le NIP doit être renseigné pour cette recherche");
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();
        // filtrer les enregistrements en fonction du NIP de la CCVD
        if (!JadeStringUtil.isEmpty(getForNIP())) {
            where.append("M$IDTIE = ");
            where.append(_dbWriteNumeric(statement.getTransaction(), getForNIP()));
        }
        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CCVDEvtRegFGISrc();
    }

    public String getForNIP() {
        return forNIP;
    }

    public void setForNIP(String forNIP) {
        this.forNIP = forNIP;
    }

}
