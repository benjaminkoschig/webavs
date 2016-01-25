package globaz.cygnus.db.contributions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <p>
 * Ajouté lors du mandat InfoRom D0034
 * </p>
 * <p>
 * Le tri se choisi au moyen d'un énuméré Java plutôt qu'une chaîne de caractère (voir {@link OrdreDeTri}). Si vous
 * voulez étendre ce manager, ajoutez les potentiels nouveaux ordres de tri au travers de l'énuméré (et de la méthode
 * {@link #_getOrder(BStatement)}) pour y avoir accès dans votre nouveau manager. Vous n'aurez ainsi pas à
 * ré-implémenter tous les autres ordre de tri dans votre nouveau manager.
 * </p>
 * 
 * @author PBA
 */
public class RFContributionsAssistanceAIManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Permet de définir les différents tri possible dans les résultats retournés par ce manager.
     * 
     * @author PBA
     */
    public static enum OrdreDeTri {
        DateDebutPeriode,
        /**
         * Implémenté uniquement dans {@link RFContributionsJointTiersManager}
         */
        NomPrenomDateDebutCAAI,
        SansTri
    }

    private String forContributionsEnCoursAu;
    private JadePeriodWrapper forContributionsEnCoursDurant;
    private String forIdDossierRFM;
    private OrdreDeTri ordreDeTri;

    public RFContributionsAssistanceAIManager() {
        super();

        forContributionsEnCoursAu = null;
        forContributionsEnCoursDurant = null;
        forIdDossierRFM = null;
        ordreDeTri = OrdreDeTri.SansTri;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder orderBy = new StringBuilder();

        String schema = _getCollection();

        switch (ordreDeTri) {
            case DateDebutPeriode:
                orderBy.append(schema).append(RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI).append(".")
                        .append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE).append(" ASC");
                break;
            default:
                break;
        }

        return orderBy.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String schema = _getCollection();
        String tableContribution = schema + RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI;

        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlankOrZero(forIdDossierRFM)) {
            sql.append(tableContribution).append(".").append(RFContributionsAssistanceAI.ID_DOSSIER_RFM).append("=")
                    .append(getForIdDossierRFM());
        }

        if (!JadeStringUtil.isBlank(forContributionsEnCoursAu)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableContribution).append(".").append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE)
                    .append("<=").append(this._dbWriteDateAMJ(statement.getTransaction(), forContributionsEnCoursAu));
            sql.append(" AND (");
            sql.append(tableContribution).append(".").append(RFContributionsAssistanceAI.DATE_FIN_PERIODE)
                    .append(" IS NULL");
            sql.append(" OR ");
            sql.append(tableContribution).append(".").append(RFContributionsAssistanceAI.DATE_FIN_PERIODE).append("=0");
            sql.append(" OR ");
            sql.append(tableContribution).append(".").append(RFContributionsAssistanceAI.DATE_FIN_PERIODE).append(">=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forContributionsEnCoursAu));
            sql.append(")");
        }

        if (forContributionsEnCoursDurant != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append("(");

            sql.append("(");
            sql.append(tableContribution)
                    .append(".")
                    .append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE)
                    .append("<=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            forContributionsEnCoursDurant.getDateDebut()));
            sql.append(" AND ");
            sql.append(tableContribution)
                    .append(".")
                    .append(RFContributionsAssistanceAI.DATE_FIN_PERIODE)
                    .append(">=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            forContributionsEnCoursDurant.getDateDebut()));
            sql.append(")");

            sql.append(" OR ");

            sql.append("(");
            sql.append(tableContribution)
                    .append(".")
                    .append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE)
                    .append("<=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forContributionsEnCoursDurant.getDateFin()));
            sql.append(" AND ");
            sql.append(tableContribution)
                    .append(".")
                    .append(RFContributionsAssistanceAI.DATE_FIN_PERIODE)
                    .append(">=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forContributionsEnCoursDurant.getDateFin()));
            sql.append(")");

            sql.append(" OR ");

            sql.append("(");
            sql.append(tableContribution)
                    .append(".")
                    .append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE)
                    .append(">=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            forContributionsEnCoursDurant.getDateDebut()));
            sql.append(" AND ");
            sql.append(tableContribution)
                    .append(".")
                    .append(RFContributionsAssistanceAI.DATE_FIN_PERIODE)
                    .append("<=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forContributionsEnCoursDurant.getDateFin()));
            sql.append(")");

            sql.append(")");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFContributionsAssistanceAI();
    }

    public String getForContributionsEnCoursAu() {
        return forContributionsEnCoursAu;
    }

    public JadePeriodWrapper getForContributionsEnCoursDurant() {
        return forContributionsEnCoursDurant;
    }

    public String getForIdDossierRFM() {
        return forIdDossierRFM;
    }

    public OrdreDeTri getOrdreDeTri() {
        return ordreDeTri;
    }

    public void setForContributionsEnCoursAu(String forContributionsEnCoursAu) {
        this.forContributionsEnCoursAu = forContributionsEnCoursAu;
    }

    public void setForContributionsEnCoursDurant(JadePeriodWrapper forContributionsEnCoursDurant) {
        this.forContributionsEnCoursDurant = forContributionsEnCoursDurant;
    }

    public void setForIdDossierRFM(String forIdDossierRFM) {
        this.forIdDossierRFM = forIdDossierRFM;
    }

    public void setOrdreDeTri(OrdreDeTri ordreDeTri) {
        this.ordreDeTri = ordreDeTri;
    }
}
