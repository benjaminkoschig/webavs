package globaz.libra.db.formules;

import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormulePDFDefTable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIFormuleJointManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDomaine = new String();
    private String forLibelleDocument = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIFormuleJointManager.
     */
    public LIFormuleJointManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        sqlWhere.append(IENFormuleDefTable.CS_TYPE);
        sqlWhere.append(" = ");
        sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS));

        if (!JadeStringUtil.isIntegerEmpty(forCsDomaine)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IENFormulePDFDefTable.DOMAINE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forCsDomaine));
        }

        // traitement du positionnement
        if (forLibelleDocument.length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("WEBAVS.FWCOUP.PCOLUT").append(" LIKE ")
                    .append(_dbWriteString(statement.getTransaction(), forLibelleDocument + "%"));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIFormuleJoint)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIFormuleJoint();
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForCsDomaine() {
        return forCsDomaine;
    }

    public String getForLibelleDocument() {
        return forLibelleDocument;
    }

    public void setForCsDomaine(String forCsDomaine) {
        this.forCsDomaine = forCsDomaine;
    }

    public void setForLibelleDocument(String forLibelleDocument) {
        this.forLibelleDocument = forLibelleDocument;
    }

}
