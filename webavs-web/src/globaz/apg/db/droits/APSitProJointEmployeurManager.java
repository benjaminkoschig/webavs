/*
 * Créé le 30 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APSitProJointEmployeurManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient String fields = null;
    private String forIdDroit = "";

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = APSitProJointEmployeur.createFields();
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = APSitProJointEmployeur.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APSituationProfessionnelle.FIELDNAME_IDDROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSitProJointEmployeur();
    }

    /**
     * getter pour l'attribut for id droit
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
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
        return APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF;
    }

    /**
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }
}
