package globaz.naos.api.musca;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class AFFacturationOrganesExternesManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeSelection = "";
    private String forNumAffilie;
    private String forPassageId;
    private boolean wantInnerJoinWhitEnteteFacture = true;

    /**
     * Renvoie la liste des champs.
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (wantInnerJoinWhitEnteteFacture == false) {
            return _getCollection() + "AFPARTP.MFTPAR AS MFTPAR";
        } else {
            return _getCollection() + "AFAFFIP.MALNAF AS MALNAF, " + _getCollection() + "AFAFFIP.MAIAFF AS MAIAFF, "
                    + _getCollection() + "AFPARTP.MFTPAR AS MFTPAR," + _getCollection() + "AFPARTP.MFDDEB AS MFDDEB,"
                    + _getCollection() + "AFPARTP.MFDFIN AS MFDFIN," + _getCollection()
                    + "FAENTFP.IDENTETEFACTURE AS IDENTETEFACTURE," + _getCollection()
                    + "FAENTFP.TOTALFACTURE AS TOTALFACTURE," + _getCollection()
                    + "FAENTFP.IDEXTERNEFACTURE AS IDEXTERNEFACTURE";
        }
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + "AFAFFIP ";
        if (isWantInnerJoinWhitEnteteFacture()) {
            sqlFrom += "INNER JOIN " + _getCollection() + "FAENTFP ON " + _getCollection() + "FAENTFP.IDEXTERNEROLE= "
                    + _getCollection() + "AFAFFIP.MALNAF ";
        }
        sqlFrom += "INNER JOIN " + _getCollection() + "AFPARTP ON " + _getCollection() + "AFAFFIP.MAIAFF = "
                + _getCollection() + "AFPARTP.MAIAFF ";
        return sqlFrom;

    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (isWantInnerJoinWhitEnteteFacture()) {
            return "MALNAF";
        }
        return "";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (isWantInnerJoinWhitEnteteFacture() && !JadeStringUtil.isEmpty(getForPassageId())) {
            sqlWhere += "IDPASSAGE = " + getForPassageId();
        }
        if ((isWantInnerJoinWhitEnteteFacture() == false) && !JadeStringUtil.isEmpty(getForNumAffilie())) {
            sqlWhere += "MALNAF = '" + getForNumAffilie() + "'";

        }
        if (JadeStringUtil.isEmpty(sqlWhere) == false) {
            sqlWhere += " AND ";
        }
        sqlWhere += "MFTPAR in (" + CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE + ", "
                + CodeSystem.PARTIC_AFFILIE_NON_ACTIF_ASSISSTE + ", " + CodeSystem.PARTIC_AFFILIE_REFUGIE + ", "
                + CodeSystem.PARTIC_AFFILIE_RMCAS + ", " + CodeSystem.PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE
                + ")";

        if (wantInnerJoinWhitEnteteFacture) {
            if (JadeStringUtil.isEmpty(sqlWhere) == false) {
                sqlWhere += " AND ";
            }
            sqlWhere += " CAST(substr(mfddeb,1,4) AS INT) <= CAST(substr(idexternefacture,1,4) AS INT)"
                    + " and ((CAST(substr(mfdfin,1,4) AS INT) >= CAST(substr(idexternefacture,1,4) AS INT))or(mfdfin=0))";
        }

        if (JadeStringUtil.isEmpty(getAnneeSelection()) == false) {
            if (JadeStringUtil.isEmpty(sqlWhere) == false) {
                sqlWhere += " AND ";
            }
            sqlWhere += " CAST(substr(mfddeb,1,4) AS INT) <=" + getAnneeSelection()
                    + " and ((CAST(substr(mfdfin,1,4) AS INT) >=" + getAnneeSelection() + ") or (mfdfin=0))";
            if (!wantInnerJoinWhitEnteteFacture) {
                sqlWhere += " group by mftpar having count (*) >1";
            }
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFOrganeExternes();
    }

    public String getAnneeSelection() {
        return anneeSelection;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public java.lang.String getForPassageId() {
        return forPassageId;
    }

    public boolean isWantInnerJoinWhitEnteteFacture() {
        return wantInnerJoinWhitEnteteFacture;
    }

    public void setAnneeSelection(String anneeSelection) {
        this.anneeSelection = anneeSelection;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public void setForPassageId(java.lang.String newForPassageId) {
        forPassageId = newForPassageId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setWantInnerJoinWhitEnteteFacture(boolean wantInnerJoinWhitEnteteFacture) {
        this.wantInnerJoinWhitEnteteFacture = wantInnerJoinWhitEnteteFacture;
    }

}
