/*
 * Cr?? le 15 juin 05
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitPaterniteManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroitParent = "";
    private String idDroitAExclure;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdDroitParent)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroitParent);
        }

        if (!JadeStringUtil.isIntegerEmpty(idDroitAExclure)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitLAPG.FIELDNAME_IDDROIT_LAPG + " <> "
                    + _dbWriteNumeric(statement.getTransaction(), idDroitAExclure);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APDroitMaternite();
    }

    /**
     * getter pour l'attribut for id droit parent
     *
     * @return la valeur courante de l'attribut for id droit parent
     */
    public String getForIdDroitParent() {
        return forIdDroitParent;
    }

    /**
     * (non-Javadoc)
     *
     * @see PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APDroitMaternite.FIELDNAME_IDDROIT_MAT + " DESC";
    }

    /**
     * setter pour l'attribut for id droit parent
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroitParent(String string) {
        forIdDroitParent = string;
    }

    /**
     * D?finit un id de droit qui doit ?tre exclus de la recherche
     * 
     * @param idDroitAExclure
     */
    public void setIdDroitAExclure(String idDroitAExclure) {
        this.idDroitAExclure = idDroitAExclure;
    }

}
