package globaz.corvus.db.ordresversements;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

public class REOrdreVersementJoinRenteVerseeATortManager extends BManager implements
        BIGenericManager<REOrdreVersementJoinRenteVerseeATort> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdPrestation;

    public REOrdreVersementJoinRenteVerseeATortManager() {
        super();

        forIdPrestation = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;

        if (forIdPrestation != null) {
            sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_PRESTATION).append("=")
                    .append(forIdPrestation);
        }

        return sql.toString();
    }

    @Override
    protected REOrdreVersementJoinRenteVerseeATort _newEntity() throws Exception {
        return new REOrdreVersementJoinRenteVerseeATort();
    }

    @Override
    public List<REOrdreVersementJoinRenteVerseeATort> getContainerAsList() {
        List<REOrdreVersementJoinRenteVerseeATort> list = new ArrayList<REOrdreVersementJoinRenteVerseeATort>();
        for (int i = 0; i < size(); i++) {
            list.add((REOrdreVersementJoinRenteVerseeATort) get(i));
        }
        return list;
    }

    public Long getForIdPrestation() {
        return forIdPrestation;
    }

    public void setForIdPrestation(Long forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }
}
