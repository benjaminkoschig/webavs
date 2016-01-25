/*
 * Créé le 31 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
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
public class APEnfantComptesOrdonnesAPGManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private transient String fields = null;

    private String forIdSituationFamAPG = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = APEnfantComptesOrdonnesAPG.createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return APEnfantAPG.FIELDNAME_DATEDEBUTDROIT;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdSituationFamAPG)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEnfantAPG.FIELDNAME_IDSITUATIONFAM + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdSituationFamAPG);
        }

        return sqlWhere + " GROUP BY " + APEnfantAPG.FIELDNAME_DATEDEBUTDROIT;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APEnfantComptesOrdonnesAPG();
    }

    /**
     * getter pour l'attribut fro id situation fam APG
     * 
     * @return la valeur courante de l'attribut fro id situation fam APG
     */
    public String getForIdSituationFamAPG() {
        return forIdSituationFamAPG;
    }

    /**
     * setter pour l'attribut fro id situation fam APG
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdSituationFamAPG(String string) {
        forIdSituationFamAPG = string;
    }
}
