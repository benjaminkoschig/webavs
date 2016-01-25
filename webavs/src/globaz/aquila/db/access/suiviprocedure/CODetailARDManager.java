/*
 * Créé le 27 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.suiviprocedure;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author sch
 */
public class CODetailARDManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateARD = "";
    private String forIdCompteAuxiliaire = "";
    private String forIdContentieux = "";

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdCompteAuxiliaire)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += (CODetailARD.FIELDNAME_IDCOMPTEANNEXE + "=" + this._dbWriteNumeric(statement.getTransaction(),
                    forIdCompteAuxiliaire));
        }
        if (!JadeStringUtil.isEmpty(forIdContentieux)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += (CODetailARD.FIELDNAME_IDCONTENTIEUX + "=" + this._dbWriteNumeric(statement.getTransaction(),
                    forIdContentieux));
        }
        if (!JadeStringUtil.isEmpty(forDateARD)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += (CODetailARD.FIELDNAME_DATE_ARD + "=" + this._dbWriteDateAMJ(statement.getTransaction(),
                    forDateARD));
        }
        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CODetailARD();
    }

    public String getForDateARD() {
        return forDateARD;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getForIdCompteAuxiliaire() {
        return forIdCompteAuxiliaire;
    }

    public String getForIdContentieux() {
        return forIdContentieux;
    }

    public void setForDateARD(String forDateARD) {
        this.forDateARD = forDateARD;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setForIdCompteAuxiliaire(String string) {
        forIdCompteAuxiliaire = string;
    }

    public void setForIdContentieux(String forIdContentieux) {
        this.forIdContentieux = forIdContentieux;
    }

}
