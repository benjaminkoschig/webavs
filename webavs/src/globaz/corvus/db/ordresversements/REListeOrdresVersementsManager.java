package globaz.corvus.db.ordresversements;

import globaz.corvus.db.prestations.REPrestations;
import globaz.globall.db.BStatement;
import globaz.prestation.db.PRAbstractManager;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager utilisé uniquement pour la liste des ordres de versement d'un lot
 */
public class REListeOrdresVersementsManager extends PRAbstractManager implements
        BIGenericManager<REListeOrdresVersementsEntity> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdLot;

    public REListeOrdresVersementsManager() {
        super();

        forIdLot = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;

        if (forIdLot != null) {
            sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_LOT).append("=").append(forIdLot);
        }

        return sql.toString();
    }

    @Override
    protected REListeOrdresVersementsEntity _newEntity() throws Exception {
        return new REListeOrdresVersementsEntity();
    }

    @Override
    public List<REListeOrdresVersementsEntity> getContainerAsList() {
        List<REListeOrdresVersementsEntity> list = new ArrayList<REListeOrdresVersementsEntity>();

        for (int i = 0; i < size(); i++) {
            list.add((REListeOrdresVersementsEntity) get(i));
        }

        return list;
    }

    public Long getForIdLot() {
        return forIdLot;
    }

    @Override
    public String getOrderByDefaut() {

        StringBuilder sql = new StringBuilder();

        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        // Ordonnancement par nom prénom, puis par idPrestation pour être sur de traite tous les ordres de versement
        // d'une prestation
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1_MAJ).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2_MAJ).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_PRESTATION);

        return sql.toString();
    }

    public void setForIdLot(Long forIdLot) {
        this.forIdLot = forIdLot;
    }

}
