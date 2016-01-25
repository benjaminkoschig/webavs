package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

public class REAnnoncesDiminution10EmeManager extends PRAbstractManager implements
        BIGenericManager<REAnnoncesDiminution10Eme> {

    private static final long serialVersionUID = 1L;
    private String forCodePrestation = "";
    private String forMoisRapport = "";
    private String forNss = "";

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forMoisRapport)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection()
                    + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A
                    + "."
                    + REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT
                    + " = "
                    + _dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisRapport));
        }

        if (!JadeStringUtil.isBlankOrZero(forNss)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A + "."
                    + REAnnoncesAbstractLevel1A.FIELDNAME_NO_ASS_AYANT_DROIT + " = "
                    + _dbWriteString(statement.getTransaction(), forNss);
        }

        if (!JadeStringUtil.isEmpty(forCodePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A + "."
                    + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION + " = "
                    + _dbWriteString(statement.getTransaction(), forCodePrestation);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnoncesDiminution10Eme();
    }

    public String getForCodePrestation() {
        return forCodePrestation;
    }

    public String getForMoisRapport() {
        return forMoisRapport;
    }

    public String getForNss() {
        return forNss;
    }

    @Override
    public String getOrderByDefaut() {
        return REAnnonceHeader.FIELDNAME_ID_ANNONCE;
    }

    public void setForCodePrestation(String forCodePrestation) {
        this.forCodePrestation = forCodePrestation;
    }

    public void setForMoisRapport(String forMoisRapport) {
        this.forMoisRapport = forMoisRapport;
    }

    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    @Override
    public List<REAnnoncesDiminution10Eme> getContainerAsList() {
        List<REAnnoncesDiminution10Eme> list = new ArrayList<REAnnoncesDiminution10Eme>();

        for (int i = 0; i < size(); i++) {
            list.add((REAnnoncesDiminution10Eme) get(i));
        }

        return list;
    }

}
