/*
 * Créé le 31 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
    /**
     * Crée une nouvelle instance de la classe APEnfantMatManager.
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
}
