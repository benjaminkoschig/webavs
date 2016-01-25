/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.cat;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTTexte
 */
public class CTTexteManager extends CTElementManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeIsoLangue = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return CTTexte.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer(super._getWhere(statement));

        if (!JadeStringUtil.isEmpty(forCodeIsoLangue)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTTexte.FIELDNAME_CODE_ISO_LANGUE + "="
                    + _dbWriteString(statement.getTransaction(), forCodeIsoLangue));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTTexte();
    }

    /**
     * getter pour l'attribut for code iso langue
     * 
     * @return la valeur courante de l'attribut for code iso langue
     */
    public String getForCodeIsoLangue() {
        return forCodeIsoLangue;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.babel.db.CTAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return CTTexte.FIELDNAME_CODE_ISO_LANGUE + "," + CTElement.FIELDNAME_NIVEAU + ","
                + CTElement.FIELDNAME_POSITION;
    }

    /**
     * setter pour l'attribut for code iso langue
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCodeIsoLangue(String string) {
        forCodeIsoLangue = string;
    }
}
