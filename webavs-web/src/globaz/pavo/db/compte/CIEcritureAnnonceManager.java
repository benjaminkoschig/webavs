package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager de <tt>CIRassemblementOuverture</tt>. Date de création : (12.11.2002 13:27:56)
 * 
 * @author: David Girardin
 */
public class CIEcritureAnnonceManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAssure = new String();

    /** (KKIRAO) */
    private String forRassemblementOuvertureId = new String();
    private String order = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CILRAEP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForRassemblementOuvertureId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KPIRAO=" + _dbWriteNumeric(statement.getTransaction(), getForRassemblementOuvertureId());
        }
        if (getForAssure().length() != 0) {
            if ("True".equals(getForAssure())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KPIECA<>0 ";

            }
            if ("False".equals(getForAssure())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KPIECC<>0 ";

            }
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIEcritureAnnonce();
    }

    /**
     * Returns the forAssure.
     * 
     * @return String
     */
    public String getForAssure() {
        return forAssure;
    }

    /**
     * Returns the forRassemblementOuvertureId.
     * 
     * @return String
     */
    public String getForRassemblementOuvertureId() {
        return forRassemblementOuvertureId;
    }

    /**
     * Returns the order.
     * 
     * @return String
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the forAssure.
     * 
     * @param forAssure
     *            The forAssure to set
     */
    public void setForAssure(String forAssure) {
        this.forAssure = forAssure;
    }

    /**
     * Sets the forRassemblementOuvertureId.
     * 
     * @param forRassemblementOuvertureId
     *            The forRassemblementOuvertureId to set
     */
    public void setForRassemblementOuvertureId(String forRassemblementOuvertureId) {
        this.forRassemblementOuvertureId = forRassemblementOuvertureId;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

}
