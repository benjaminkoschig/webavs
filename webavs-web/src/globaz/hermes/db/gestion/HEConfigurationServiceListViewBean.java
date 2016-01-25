package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class HEConfigurationServiceListViewBean extends BManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int colonneSelection;
    // recherche exact sur la reference
    private String forReference = new String();
    // recherche par adresse email
    private String likeEmailAdresse = new String();
    // recherche par id service (RDIDSE)
    private String likeIdService = new String();
    // recherche par référence interne
    private String likeReferenceInterne = new String();
    // recherche par nom du service (RDSERV)
    private String likeServiceName = new String();

    // ordre de recherche
    private String order = "";

    /** retourne la clause from de la requete */
    @Override
    protected String _getFrom(BStatement statement) {
        String from;
        from = _getCollection() + "HESEARC ";
        return from;
    }

    /** retour la clause order by de la requete */
    @Override
    protected String _getOrder(BStatement statement) {
        return getOrder();
    }

    /** retourne la clause WHERE de la requete */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (getLikeIdService().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDIDSE=" + _dbWriteNumeric(statement.getTransaction(), getLikeIdService());
        }
        if (getLikeServiceName().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDSERV LIKE " + _dbWriteString(statement.getTransaction(), getLikeServiceName() + "%");
        }
        if (getLikeReferenceInterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDREFI LIKE " + _dbWriteString(statement.getTransaction(), getLikeReferenceInterne() + "%");
        }
        if (getForReference().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDREFI = " + _dbWriteString(statement.getTransaction(), getForReference());
        }
        if (getLikeEmailAdresse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDMAIL LIKE " + _dbWriteString(statement.getTransaction(), getLikeEmailAdresse() + "%");
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        HEConfigurationServiceViewBean n = new HEConfigurationServiceViewBean();
        return n;

    }

    public int getColonneSelection() {
        return colonneSelection;
    }

    public String getForReference() {
        return forReference;
    }

    public String getLikeEmailAdresse() {
        return likeEmailAdresse;
    }

    public String getLikeIdService() {
        return likeIdService;
    }

    public String getLikeReferenceInterne() {
        return likeReferenceInterne;
    }

    public String getLikeServiceName() {
        return likeServiceName;
    }

    public String getOrder() {
        return order;
    }

    public void setForReference(String forReference) {
        this.forReference = forReference;
    }

    public void setLikeEmailAdresse(String newLikeEmailAdresse) {
        likeEmailAdresse = newLikeEmailAdresse;
    }

    public void setLikeIdService(String newForIdService) {
        likeIdService = newForIdService;
    }

    public void setLikeReferenceInterne(String newLikeReferenceService) {
        likeReferenceInterne = newLikeReferenceService;
    }

    public void setLikeServiceName(String newLikeServiceName) {
        likeServiceName = newLikeServiceName;
    }

    public void setOrder(String newOrder) {
        order = newOrder;
    }

}
