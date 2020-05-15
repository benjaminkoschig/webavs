/*
 *
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;


public class APDroitPandemieDateFinManager extends APDroitLAPGManager {

    private static final long serialVersionUID = 1L;

    private String forGenreService;
    private String forCategorie;
    private Boolean forManifestationAnnulee;
    private Boolean sansDateDeFin;
    private String forDateDeFin;

    private static String TDROIT = "DR1";


    @Override
    protected String _getFrom(BStatement statement) {

        String tableSituationPan = _getCollection() + APDroitPanSituation.TABLE_NAME;
        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + " as "+TDROIT;

        StringBuilder sql = new StringBuilder(tableDroitLAPG);

        // jointure entre tables situation et droits
        sql.append(" INNER JOIN ");
        sql.append(tableSituationPan);
        sql.append(" ON ");
        sql.append(tableSituationPan).append(".").append(APDroitPanSituation.FIELDNAME_ID_DROIT);
        sql.append("=");
        sql.append(TDROIT).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sql = "";

        String superWhere = super._getWhere(statement);
        if (superWhere != null) {
            sql += superWhere;
        }

        if (!JadeStringUtil.isEmpty(forGenreService)) {
            if (sql.length() != 0) {
                sql += " AND ";
            }

            sql +=  TDROIT + "." + APDroitLAPG.FIELDNAME_GENRESERVICE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forGenreService);
        }


        if (forManifestationAnnulee != null) {
            if (sql.length() != 0) {
                sql += " AND ";
            }
            String tableManif = _getCollection() + APDroitPanSituation.TABLE_NAME + "." + APDroitPanSituation.FIELDNAME_MANIFESTATION_ANNULEE_DEBUT;

            if(forManifestationAnnulee) {
                sql +=  "("+tableManif +" IS NOT NULL AND "+tableManif+" <> 0)";
            } else {
                sql +=  "("+tableManif+" IS NULL OR "+tableManif+" = 0)";
            }
        }

        if (sansDateDeFin != null && sansDateDeFin) {
            if (sql.length() != 0) {
                sql += " AND ";
            }
            String tableFinDroit = TDROIT + "." + APDroitLAPG.FIELDNAME_DATEFINDROIT;
            sql +=  "("+tableFinDroit+" IS NULL OR "+tableFinDroit+" = 0)";

        } else if(!JadeStringUtil.isEmpty(forDateDeFin)) {
            if (sql.length() != 0) {
                sql += " AND ";
            }
            String tableFinDroit = TDROIT + "." + APDroitLAPG.FIELDNAME_DATEFINDROIT;
            sql +=  "("+tableFinDroit+" IS NULL OR "+tableFinDroit+" = 0 OR ";
            sql +=  tableFinDroit+" > "+this._dbWriteDateAMJ(statement.getTransaction(), forDateDeFin)+ ")";
        }

        if (!JadeStringUtil.isEmpty(forCategorie)) {
            if (sql.length() != 0) {
                sql += " AND ";
            }

            sql +=  _getCollection() + APDroitPanSituation.TABLE_NAME + "." + APDroitPanSituation.FIELDNAME_CATEGORIE_ENTREPRISE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCategorie);
        }

        if (sql.length() != 0) {
            sql += " AND ";
        }

        // Validé, partiel ou définitif
        sql +=  TDROIT + "." + APDroitLAPG.FIELDNAME_ETAT +
            " IN ("+ IAPDroitLAPG.CS_ETAT_DROIT_VALIDE+","+IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL+","+IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF+")";

        if (sql.length() != 0) {
            sql += " AND ";
        }
        // pas un droit annulé
        sql += TDROIT + "." + APDroitLAPG.FIELDNAME_IDDROIT_LAPG +
                " NOT IN (select "+TDROIT+"."+APDroitLAPG.FIELDNAME_IDDROIT_LAPG+" from "+_getCollection() + APDroitLAPG.TABLE_NAME_LAPG +" as dr3 where dr3."+ APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT +" = "+TDROIT+"."+APDroitLAPG.FIELDNAME_IDDROIT_LAPG+")";

        return  sql;

    }

    public String getForGenreService() {
        return forGenreService;
    }

    public void setForGenreService(String forGenreService) {
        this.forGenreService = forGenreService;
    }

    public Boolean getForManifestationAnnulee() {
        return forManifestationAnnulee;
    }

    public void setForManifestationAnnulee(Boolean forManifestationAnnulee) {
        this.forManifestationAnnulee = forManifestationAnnulee;
    }

    public Boolean getSansDateDeFin() {
        return sansDateDeFin;
    }

    public void setSansDateDeFin(Boolean sansDateDeFin) {
        this.sansDateDeFin = sansDateDeFin;
    }

    public String getForCategorie() {
        return forCategorie;
    }

    public void setForCategorie(String forCategorie) {
        this.forCategorie = forCategorie;
    }

    public String getForDateDeFin() {
        return forDateDeFin;
    }

    public void setForDateDeFin(String forDateDeFin) {
        this.forDateDeFin = forDateDeFin;
    }
}
