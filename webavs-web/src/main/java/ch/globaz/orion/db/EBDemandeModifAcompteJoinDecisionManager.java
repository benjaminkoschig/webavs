package ch.globaz.orion.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;

public class EBDemandeModifAcompteJoinDecisionManager extends JadeManager<EBDemandeModifAcompteJoinDecisionEntity> {

    private static final long serialVersionUID = 1L;

    private String likeNumAffilie;
    private String forAnnee;
    private String likeNom;
    private String fromDateReception;
    private DemandeModifAcompteStatut forStatut = DemandeModifAcompteStatut.UNDEFINDED;
    private String forType;
    private Collection<String> inStatut;
    private String forIdAffiliation;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new EBDemandeModifAcompteJoinDecisionEntity();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "ID AS ID, " + "IDDEMANDE_PORTAIL AS IDDEMANDE_PORTAIL, " + "IDAFFILIATION AS IDAFFILIATION, "
                + "NUMAFFILIE AS NUMAFFILIE, " + "ANNEE AS ANNEE, " + "REVENU AS REVENU, " + "CAPITAL AS CAPITAL, "
                + "CS_STATUT AS CS_STATUT, " + "REMARQUE AS REMARQUE, " + "DATERECEPTION AS DATERECEPTION, "
                + "IDDECISION_REF AS IDDECISION_REF, IATTDE AS TYPE_DECISION";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();

        String decisionTable = "CPDECIP";
        String decisionIdColumn = "IAIDEC";
        // String decisionAffiliationCalculTable = "CPDOCAP";
        // String decisionAffiliationCalculIdColumn = "IAIDEC";
        String affiliationTable = "AFAFFIP";
        String affiliationIdColumn = "MAIAFF";
        String tiersTable = "TITIERP";
        String tiersIdColumn = "HTITIE";

        from.append(_getCollection());
        from.append(EBDemandeModifAcompteDefTable.TABLE);

        // LEFT JOIN SCHEMA.CPEDECIP ON EBDEM_MODIF_ACO.IDDECISION_REF=CPDECIP.IAIDEC
        from.append(" LEFT JOIN ");
        from.append(_getCollection());
        from.append(decisionTable);
        from.append(" ON ");
        from.append(_getCollection());
        from.append(EBDemandeModifAcompteDefTable.TABLE);
        from.append(".");
        from.append(EBDemandeModifAcompteDefTable.ID_DECISION_REF.getColumnName());
        from.append("=");
        from.append(_getCollection());
        from.append(decisionTable);
        from.append(".");
        from.append(decisionIdColumn);

        // LEFT JOIN SCHEMA.CPEDECIP ON EBDEM_MODIF_ACO.IDDECISION_REF=CPDECIP.IAIDEC AND (IHIDCA=600007 OR
        // IHIDCA=600019)
        // from.append(" LEFT OUTER JOIN ");
        // from.append(_getCollection());
        // from.append(decisionAffiliationCalculTable);
        // from.append(" ON ");
        // from.append(_getCollection());
        // from.append(decisionTable);
        // from.append(".");
        // from.append(decisionIdColumn);
        // from.append("=");
        // from.append(_getCollection());
        // from.append(decisionAffiliationCalculTable);
        // from.append(".");
        // from.append(decisionAffiliationCalculIdColumn);
        // from.append(" AND (IHIDCA=600007 OR IHIDCA=600019)");

        // INNER JOIN SCHEMA.AFAFFIP ON AFAFFIP.MAIAFF=EBDEM_MODIF_ACO.ID_AFFILIATION
        from.append(" INNER JOIN ");
        from.append(_getCollection());
        from.append(affiliationTable);
        from.append(" ON ");
        from.append(_getCollection());
        from.append(affiliationTable);
        from.append(".");
        from.append(affiliationIdColumn);
        from.append("=");
        from.append(_getCollection());
        from.append(EBDemandeModifAcompteDefTable.TABLE);
        from.append(".");
        from.append(EBDemandeModifAcompteDefTable.ID_AFFILIATION.getColumnName());

        // INNER JOIN SCHEMA.TITERP ON TITIERP.HTITIE=AFAFFIP.HTITIE
        from.append(" INNER JOIN ");
        from.append(_getCollection());
        from.append(tiersTable);
        from.append(" ON ");
        from.append(_getCollection());
        from.append(tiersTable);
        from.append(".");
        from.append(tiersIdColumn);
        from.append("=");
        from.append(_getCollection());
        from.append(affiliationTable);
        from.append(".");
        from.append(tiersIdColumn);

        return from.toString();
    }

    @Override
    protected void createWhere(SQLWriter sqlWhere) {

        sqlWhere.and(EBDemandeModifAcompteDefTable.NUM_AFFILIE).fullLike(likeNumAffilie);

        if (likeNom != null) {
            String nomUpperWithoutSpecial = JadeStringUtil.convertSpecialChars(likeNom).toUpperCase();
            sqlWhere.and("(schema.TITIERP.HTLDU1 like '%" + nomUpperWithoutSpecial
                    + "%' OR schema.TITIERP.HTLDU2 like '%" + nomUpperWithoutSpecial + "%')");
        }

        if (!JadeStringUtil.isEmpty(forAnnee)) {
            sqlWhere.and("schema.EBDEM_MODIF_ACO.ANNEE=" + forAnnee);
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdAffiliation())) {
            sqlWhere.and("schema.EBDEM_MODIF_ACO.IDAFFILIATION=" + getForIdAffiliation());
        }

        if (!JadeStringUtil.isEmpty(fromDateReception)) {
            sqlWhere.and(EBDemandeModifAcompteDefTable.DATE_RECEPTION).greaterOrEqualForDate(fromDateReception);
        }

        if (forStatut != null && !forStatut.equals(DemandeModifAcompteStatut.UNDEFINDED)) {
            sqlWhere.and("schema.EBDEM_MODIF_ACO.CS_STATUT=" + forStatut.getValue());
        }

        if (!JadeStringUtil.isEmpty(forType)) {
            sqlWhere.and("schema.CPDECIP.IATTDE=" + forType);
        }

        if (inStatut != null && !inStatut.isEmpty()) {
            sqlWhere.and(EBDemandeModifAcompteDefTable.CS_STATUT).in(inStatut);
        }

    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getFromDateReception() {
        return fromDateReception;
    }

    public void setFromDateReception(String fromDateReception) {
        this.fromDateReception = fromDateReception;
    }

    public DemandeModifAcompteStatut getForStatut() {
        return forStatut;
    }

    public void setForStatut(DemandeModifAcompteStatut forStatut) {
        this.forStatut = forStatut;
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    public Collection<String> getInStatut() {
        return inStatut;
    }

    public void setInStatut(Collection<String> inStatut) {
        this.inStatut = inStatut;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

}
