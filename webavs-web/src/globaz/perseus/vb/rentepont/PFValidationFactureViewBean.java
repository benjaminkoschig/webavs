package globaz.perseus.vb.rentepont;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Map;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFValidationFactureViewBean extends BJadePersistentObjectViewBean {

    private Map<String, String> type;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {

        // HashSet hs = new HashSet();
        // hs.add(CSTypeQD.FRAIS_MALADIE.getCodeSystem());
        // %>
        // <TD colspan="3"><ct:FWCodeSelectTag name="searchModel.forCSTypeQD" codeType="PFTYPEQD" wantBlank="true"
        // except="<%=hs %>" defaut=""/></T

        type = PerseusServiceLocator.getTypesSoinsRentePontService().getMapSurTypes(getISession());
    }

    public Map<String, String> getType() {
        return type;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

}
