/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.affiliation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAffiliationJTiersCIManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forAucunCIOuvert;
    private String[] forTypeAffiliation;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationJTiersCIManager.
     */
    public AFAffiliationJTiersCIManager() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFAffiliationJTiersCI.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();
        whereClause.append(_getCollection() + "AFAFFIP.MABTRA = 2");

        if ((forTypeAffiliation != null) && (forTypeAffiliation.length > 0)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            } else {
                whereClause.append(" ");
            }

            whereClause.append("(");

            for (int idType = 0; idType < forTypeAffiliation.length; ++idType) {
                if (idType > 0) {
                    whereClause.append(" OR ");
                }

                whereClause.append("MATTAF=");
                whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forTypeAffiliation[idType]));
            }

            whereClause.append(")");
        }

        if (forAucunCIOuvert != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            } else {
                whereClause.append(" ");
            }

            whereClause.append("(SELECT COUNT(KABOUV) FROM ");
            whereClause.append(_getCollection());
            whereClause.append("TIPAVSP INNER JOIN ");
            whereClause.append(_getCollection());
            whereClause.append("CIINDIP ON REPLACE(HXNAVS,'.','')=KANAVS WHERE KABOUV=");
            whereClause.append(this._dbWriteBoolean(statement.getTransaction(), Boolean.TRUE,
                    BConstants.DB_TYPE_BOOLEAN_CHAR));
            whereClause.append(" AND ");
            whereClause.append(_getCollection());
            whereClause.append("AFAFFIP.HTITIE=");
            whereClause.append(_getCollection());
            whereClause.append("TIPAVSP.HTITIE");
            whereClause.append(")=0");
        }

        // ne pas inclure les affiliations radiées
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        } else {
            whereClause.append(" ");
        }
        whereClause.append("MADFIN=0");
        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationJTiersCI();
    }

    /**
     * setter pour l'attribut for aucun CIOuvert
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void forAucunCIOuvert(Boolean boolean1) {
        forAucunCIOuvert = boolean1;
    }

    /**
     * getter pour l'attribut for aucun CIOuvert
     * 
     * @return la valeur courante de l'attribut for aucun CIOuvert
     */
    public Boolean getForAucunCIOuvert() {
        return forAucunCIOuvert;
    }

    /**
     * getter pour l'attribut for type affiliation
     * 
     * @return la valeur courante de l'attribut for type affiliation
     */
    public String[] getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    /**
     * setter pour l'attribut for type affiliation
     * 
     * @param strings
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeAffiliation(String[] strings) {
        forTypeAffiliation = strings;
    }
}
