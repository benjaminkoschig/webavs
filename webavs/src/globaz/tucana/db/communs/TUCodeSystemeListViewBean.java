package globaz.tucana.db.communs;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.tucana.constantes.ITUFWTablesDef;

/**
 * @author FGo
 * 
 *         List View Bean pour sélection des codes sytèmes
 */
public class TUCodeSystemeListViewBean extends FWParametersSystemCodeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forLibelle = new String();
    private String fromCode = new String();
    private String likeLibelle = new String();

    /**
     * Constructor for AICodeInfirmiteListViewBean.
     */
    public TUCodeSystemeListViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return super._getOrder(statement);
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(super._getWhere(statement));

        // traitement du positionnement
        if (getFromCode().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUFWTablesDef.PCOUID).append(">=")
                    .append(_dbWriteString(statement.getTransaction(), getFromCode()));
        }

        // traitement du positionnement
        if (getLikeLibelle().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("UPPER (").append(ITUFWTablesDef.PCOLUT).append(")").append(" LIKE UPPER (")
                    .append(_dbWriteString(statement.getTransaction(), "%" + getLikeLibelle() + "%")).append(")");
        }
        // traitement du positionnement
        if (getForLibelle().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUFWTablesDef.PCOLUT).append(" = ")
                    .append(_dbWriteString(statement.getTransaction(), getForLibelle()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUCodeSystemeViewBean();
    }

    /**
     * @return
     */
    public String getForLibelle() {
        return forLibelle;
    }

    /**
     * Returns the fromCode.
     * 
     * @return String
     */
    public String getFromCode() {
        return fromCode;
    }

    /**
     * Returns the likeLibelle.
     * 
     * @return String
     */
    public String getLikeLibelle() {
        return likeLibelle;
    }

    /**
     * @param string
     */
    public void setForLibelle(String newForLibelle) {
        forLibelle = newForLibelle;
    }

    /**
     * Sets the fromCode.
     * 
     * @param fromCode
     *            The fromCode to set
     */
    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    /**
     * Sets the likeLibelle.
     * 
     * @param likeLibelle
     *            The likeLibelle to set
     */
    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }

}