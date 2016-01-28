package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REDemandeRenteManager extends PRAbstractManagerHierarchique {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeDemandeRente = "";
    private String forIdDemandePrestation = "";
    private String forIdDemandeRente = "";
    private String forIdRenteCalculee = "";

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = REDemandeRenteJointDemande.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @param (BStatement)statement
     * 
     * @return la valeur courante de l'attribut whereClause selon les différents paramètres choisis
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdDemandeRente())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdDemandeRente()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDemandePrestation())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdDemandePrestation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdRenteCalculee())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdRenteCalculee()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsTypeDemandeRente())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForCsTypeDemandeRente()));
        }

        return whereClause.toString();
    }

    /**
     * Création d'une nouvelle entité de type REDemandeRente
     * 
     * @return Une nouvelle entité de type REDemandeRente
     * 
     * @throws Exception
     * 
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRente();
    }

    /**
     * getter pour l'attribut forCsTypeDemandeRente
     * 
     * @return la valeur courante de l'attribut forCsTypeDemandeRente
     */
    public String getForCsTypeDemandeRente() {
        return forCsTypeDemandeRente;
    }

    /**
     * getter pour l'attribut forIdDemandePrestation
     * 
     * @return la valeur courante de l'attribut forIdDemandePrestation
     */
    public String getForIdDemandePrestation() {
        return forIdDemandePrestation;
    }

    /**
     * getter pour l'attribut forIdDemandeRente
     * 
     * @return la valeur courante de l'attribut forIdDemandeRente
     */
    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public String getForIdRenteCalculee() {
        return forIdRenteCalculee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRAbstractManagerHierarchique#getHierarchicalOrderBy ()
     */
    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE;
    }

    /**
     * setter pour l'attribut forCsTypeDemandeRente.
     * 
     * @param forCsTypeDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsTypeDemandeRente(String forCsTypeDemande) {
        forCsTypeDemandeRente = forCsTypeDemande;
    }

    /**
     * setter pour l'attribut forIdDemandePrestation.
     * 
     * @param forIdDemandePrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDemandePrestation(String forIdDemande) {
        forIdDemandePrestation = forIdDemande;
    }

    /**
     * setter pour l'attribut forIdDemandeRente.
     * 
     * @param forIdDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDemandeRente(String string) {
        forIdDemandeRente = string;
    }

    public void setForIdRenteCalculee(String forIdRenteCalculee) {
        this.forIdRenteCalculee = forIdRenteCalculee;
    }

}
