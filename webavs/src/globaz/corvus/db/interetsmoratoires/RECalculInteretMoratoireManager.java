package globaz.corvus.db.interetsmoratoires;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class RECalculInteretMoratoireManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemandeRente = "";
    private String forIdInteretMoratoire = "";
    private String forIdRenteAccordee = "";
    private String forIdTiersAdrPmt = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des bases de calculs et table des rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // jointure entre table des rentes accordées et prestations accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordées et table des calcul
        // interet moratoire
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);

        // jointure entre table des prestations accordées et table des infos
        // comptable
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdRenteAccordee())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdRenteAccordee()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdInteretMoratoire())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECalculInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdInteretMoratoire()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdTiersAdrPmt())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdTiersAdrPmt()));
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECalculInteretMoratoire();
    }

    /**
     * @return
     */
    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    /**
     * @return
     */
    public String getForIdInteretMoratoire() {
        return forIdInteretMoratoire;
    }

    /**
     * @return
     */
    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public String getForIdTiersAdrPmt() {
        return forIdTiersAdrPmt;
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
        return RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE;
    }

    /**
     * @param string
     */
    public void setForIdDemandeRente(String string) {
        forIdDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setForIdInteretMoratoire(String string) {
        forIdInteretMoratoire = string;
    }

    /**
     * @param string
     */
    public void setForIdRenteAccordee(String string) {
        forIdRenteAccordee = string;
    }

    public void setForIdTiersAdrPmt(String forIdTiersAdrPmt) {
        this.forIdTiersAdrPmt = forIdTiersAdrPmt;
    }

}
