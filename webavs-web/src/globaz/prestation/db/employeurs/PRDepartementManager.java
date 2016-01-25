/*
 * Créé le 7 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.db.employeurs;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class PRDepartementManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffilie = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdAffilie)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRDepartement.FIELDNAME_ID_AFFILIE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdAffilie);
        }

        // il n'y a que les departements qui nous interessent
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }

        sqlWhere += PRDepartement.FIELDNAME_TYPE_PART + "="
                + _dbWriteNumeric(statement.getTransaction(), PRDepartement.PARTIC_AFFILIE_DEPARTEMENT);

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new PRDepartement();
    }

    /**
     * getter pour l'attribut for id affilie
     * 
     * @return la valeur courante de l'attribut for id affilie
     */
    public String getForIdAffilie() {
        return forIdAffilie;
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
        return PRDepartement.FIELDNAME_ID_PART;
    }

    /**
     * setter pour l'attribut for id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAffilie(String string) {
        forIdAffilie = string;
    }
}
