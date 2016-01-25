package globaz.corvus.db.ci;

import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REInscriptionCIManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnonceInscriptionCI = "";
    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    private String forIdCI = "";
    private String forIdRCI = "";

    private String forIdTiers = "";
    private String forNoAgence = "";
    private String forNoCaisse = "";
    private List<String> idsAExclure;

    private boolean isCITraiteExclu = false;

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
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);

        // jointure entre table des ci et table des rassemblements de ci
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECompteIndividuel.TABLE_NAME_CI);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERassemblementCI.FIELDNAME_ID_CI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECompteIndividuel.TABLE_NAME_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RECompteIndividuel.FIELDNAME_ID_CI);

        // jointure entre table des rassemblements de ci et des inscriptions ci
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInscriptionCI.TABLE_NAME_INS_CI);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERassemblementCI.FIELDNAME_ID_RCI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REInscriptionCI.TABLE_NAME_INS_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInscriptionCI.FIELDNAME_ID_RCI);

        if (!JadeStringUtil.isEmpty(getForNoCaisse()) || !JadeStringUtil.isEmpty(getForNoAgence())) {
            // jointure entre table des inscriptions ci et des headers d'annonce
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(REInscriptionCI.TABLE_NAME_INS_CI);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(REInscriptionCI.FIELDNAME_ID_ARC);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        }

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdTiers())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECompteIndividuel.FIELDNAME_ID_TIERS);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdCI())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECompteIndividuel.FIELDNAME_ID_CI);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdCI()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdRCI())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_ID_RCI);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdRCI()));
        }

        if (!JadeStringUtil.isEmpty(getForNoCaisse())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REAnnonceHeader.FIELDNAME_NUMERO_CAISSE);
            whereClause.append("=");
            whereClause.append(this._dbWriteString(statement.getTransaction(), getForNoCaisse()));
        }

        if (!JadeStringUtil.isEmpty(getForNoAgence())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REAnnonceHeader.FIELDNAME_NUMERO_AGENCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteString(statement.getTransaction(), getForNoAgence()));
        }

        if (isCITraiteExclu) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND (");
            }
            whereClause.append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
            whereClause.append(" IS NULL OR ");
            whereClause.append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
            whereClause.append(" <> ");
            whereClause.append(REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_TRAITE);
            whereClause.append(")");
        }

        if ((idsAExclure != null) && (idsAExclure.size() > 0)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REInscriptionCI.FIELDNAME_ID_INSCRIPTION);
            whereClause.append(" NOT IN ( ");
            for (int ctr = 0; ctr < idsAExclure.size(); ctr++) {
                whereClause.append(idsAExclure.get(ctr));
                if ((ctr + 1) < idsAExclure.size()) {
                    whereClause.append(", ");
                }
            }
            whereClause.append(this._dbWriteString(statement.getTransaction(), getForNoAgence()));
            whereClause.append(" )");
        }
        if (!JadeStringUtil.isBlankOrZero(forIdAnnonceInscriptionCI)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REInscriptionCI.FIELDNAME_ID_ARC);
            whereClause.append(" = ");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdAnnonceInscriptionCI));
        }
        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REInscriptionCI();
    }

    public final String getForIdAnnonceInscriptionCI() {
        return forIdAnnonceInscriptionCI;
    }

    /**
     * @return
     */
    public String getForIdCI() {
        return forIdCI;
    }

    /**
     * @return
     */
    public String getForIdRCI() {
        return forIdRCI;
    }

    /**
     * @return
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return
     */
    public String getForNoAgence() {
        return forNoAgence;
    }

    /**
     * @return
     */
    public String getForNoCaisse() {
        return forNoCaisse;
    }

    /**
     * Retourne une liste d'id qui seront exclus de la recherche
     * 
     * @return
     */
    public final List<String> getIdsAExclure() {
        return idsAExclure;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RERassemblementCI.FIELDNAME_ID_RCI;
    }

    public final void setForIdAnnonceInscriptionCI(String forIdAnnonceInscriptionCI) {
        this.forIdAnnonceInscriptionCI = forIdAnnonceInscriptionCI;
    }

    /**
     * @param string
     */
    public void setForIdCI(String string) {
        forIdCI = string;
    }

    /**
     * @param string
     */
    public void setForIdRCI(String string) {
        forIdRCI = string;
    }

    /**
     * @param string
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    /**
     * @param string
     */
    public void setForNoAgence(String string) {
        forNoAgence = string;
    }

    /**
     * @param string
     */
    public void setForNoCaisse(String string) {
        forNoCaisse = string;
    }

    /**
     * Renseigne une liste d'Id à exclure lors de la recherche
     * 
     * @param idAExclure
     *            Liste d'ids à exclure lors de la recherche
     */
    public final void setIdsAExclure(List<String> idAExclure) {
        idsAExclure = idAExclure;
    }

    /**
     * Exclus les CI dont le champs REANICI.ZBTCIA = 52860002 (Traite) C'est à dire que ces CI étaient en attente d'un
     * CI additionnel et il a été reçu, il ne faut donc plus considérer ces CI traité
     * 
     * @param isCITraiteExclu
     */
    public final void setIsCITraiteExclu(boolean isCITraiteExclu) {
        this.isCITraiteExclu = isCITraiteExclu;
    }
}
