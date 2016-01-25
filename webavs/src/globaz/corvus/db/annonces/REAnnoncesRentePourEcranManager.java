package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class REAnnoncesRentePourEcranManager extends REAnnoncesAbstractLevel1AManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REAnnoncesRentePourEcranManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append("DISTINCT ");

        sql.append(REAnnonceHeader.FIELDNAME_CODE_APPLICATION).append(",");
        sql.append(REAnnonceHeader.FIELDNAME_ID_ANNONCE).append(",");
        sql.append(REAnnonceHeader.FIELDNAME_ETAT).append(",");

        sql.append(REAnnonceRente.FIELDNAME_CS_TRAITEMENT).append(",");

        sql.append(REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION).append(",");
        sql.append(REAnnoncesAbstractLevel1A.FIELDNAME_DEBUT_DROIT).append(",");
        sql.append(REAnnoncesAbstractLevel1A.FIELDNAME_FIN_DROIT).append(",");
        sql.append(REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT).append(",");
        sql.append(REAnnoncesAbstractLevel1A.FIELDNAME_MENSUALITE_PRESTATIONS_FR).append(",");

        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".").append(ITITiersDefTable.ID_TIERS)
                .append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_2);

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnoncesRentePourEcran();
    }
}
