/*
 * Cr�� le 31 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APEnfantPatManager extends APSituationFamilialePatManager {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String forIdSituationFamiliale = "";
    private String forIdDroitPaternite = "";
    private String forNssEnfant = "";
    /**
     * Cr�e une nouvelle instance de la classe APEnfantMatManager.
     */
    public APEnfantPatManager() {
        super(IAPDroitMaternite.CS_TYPE_ENFANT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APEnfantPat();
    }
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdSituationFamiliale())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEnfantPat.FIELDNAME_IDSITFAMPATERNITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdSituationFamiliale);
        }
        if (!JadeStringUtil.isIntegerEmpty(getForIdDroitPaternite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEnfantPat.FIELDNAME_IDDROITPATERNITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroitPaternite);
        }

        if(!JadeStringUtil.isEmpty(getForNssEnfant())){
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEnfantPat.FIELDNAME_NO_AVS + "="
                    + _dbWriteString(statement.getTransaction(), forNssEnfant);
        }
        return sqlWhere;
    }
    public String getForIdSituationFamiliale() {
        return forIdSituationFamiliale;
    }

    public void setForIdSituationFamiliale(String forIdSituationFamiliale) {
        this.forIdSituationFamiliale = forIdSituationFamiliale;
    }

    @Override
    public String getForIdDroitPaternite() {
        return forIdDroitPaternite;
    }

    @Override
    public void setForIdDroitPaternite(String forIdDroitPaternite) {
        this.forIdDroitPaternite = forIdDroitPaternite;
    }

    public void setForNssEnfant(String nss){
        forNssEnfant = nss;
    }

    public String getForNssEnfant(){
        return forNssEnfant;
    }
}
