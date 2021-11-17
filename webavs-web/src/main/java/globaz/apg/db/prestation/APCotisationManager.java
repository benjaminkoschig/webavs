/*
 * Cr�� le 18 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCotisationManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCotisation = "";
    private String forIdRepartitionBeneficiairePaiement = "";
    private String forType = "";
    private String fromIdCotisation = "";

    private String notForIdCotisation = "";
    private String toIdCotisation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdCotisation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCotisation.FIELDNAME_ID_COTISATION);
            whereClause.append("=");
            whereClause.append(forIdCotisation);
        }

        if (!JadeStringUtil.isIntegerEmpty(fromIdCotisation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCotisation.FIELDNAME_ID_COTISATION);
            whereClause.append(">=");
            whereClause.append(fromIdCotisation);
        }

        if (!JadeStringUtil.isIntegerEmpty(toIdCotisation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCotisation.FIELDNAME_ID_COTISATION);
            whereClause.append("<=");
            whereClause.append(toIdCotisation);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRepartitionBeneficiairePaiement)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);
            whereClause.append("=");
            whereClause.append(forIdRepartitionBeneficiairePaiement);
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdCotisation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCotisation.FIELDNAME_ID_COTISATION);
            whereClause.append("<>");
            whereClause.append(notForIdCotisation);
        }

        if (!JadeStringUtil.isIntegerEmpty(forType)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(APCotisation.FIELDNAME_TYPE);
            whereClause.append("=");
            whereClause.append(this._dbWriteString(statement.getTransaction(), forType));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APCotisation();
    }

    /**
     * getter pour l'attribut for id cotisation
     * 
     * @return la valeur courante de l'attribut for id cotisation
     */
    public String getForIdCotisation() {
        return forIdCotisation;
    }

    /**
     * getter pour l'attribut for id repartition beneficiaire paiement
     * 
     * @return la valeur courante de l'attribut for id repartition beneficiaire paiement
     */
    public String getForIdRepartitionBeneficiairePaiement() {
        return forIdRepartitionBeneficiairePaiement;
    }

    public String getFromIdCotisation() {
        return fromIdCotisation;
    }

    public String getNotForIdCotisation() {
        return notForIdCotisation;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APCotisation.FIELDNAME_ID_COTISATION;
    }

    public String getToIdCotisation() {
        return toIdCotisation;
    }

    /**
     * setter pour l'attribut for id cotisation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCotisation(String string) {
        forIdCotisation = string;
    }

    /**
     * setter pour l'attribut for id repartition beneficiaire paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdRepartitionBeneficiairePaiement(String string) {
        forIdRepartitionBeneficiairePaiement = string;
    }

    public void setFromIdCotisation(String fromIdCotisation) {
        this.fromIdCotisation = fromIdCotisation;
    }

    public void setNotForIdCotisation(String notForIdCotisation) {
        this.notForIdCotisation = notForIdCotisation;
    }

    public void setToIdCotisation(String toIdCotisation) {
        this.toIdCotisation = toIdCotisation;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    public String getForType() {
        return forType;
    }
}
