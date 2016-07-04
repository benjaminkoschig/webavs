package globaz.phenix.db.communications;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.interfaces.ICommunicationrRetourManager;

public class CPCommunicationFiscaleRetourSEDEXManager extends CPCommunicationFiscaleRetourManager implements
        ICommunicationrRetourManager {

    private static final long serialVersionUID = 1L;
    private String orderBy = "";
    private String tri = "";

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return super._getFrom(statement) + " LEFT OUTER JOIN " + _getCollection()
                + "CPSECON CPSECON ON (CPSECON.IKIRET=CPCRETP.IKIRET)";
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrderBy();
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleRetourSEDEXViewBean();
    }

    @Override
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    @Override
    public String getTri() {
        return tri;
    }

    @Override
    public void setOrderBy(java.lang.String string) {
        orderBy = string;
    }

    @Override
    public void setTri(String tri) {
        this.tri = tri;
    }

    @Override
    public void orderByNumAvs() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("CPSECON.SEVNAVS ASC");
        } else {
            setOrderBy(getOrderBy() + ", CPSECON.SEVNAVS ASC");
        }
    }

}
