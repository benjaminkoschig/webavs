package globaz.naos.db.processFacturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAdhesion;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.process.AFNewProcessFacturation;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

/**
 * Manager pour la recherche des informations pour la facturation des Cotisations. Fait une jointure sur TITIERP,
 * TIPERSP, AFAFFIP, AFCOTIP, AFASSUP, CARUBRP, AFPLAFP //, AFNASSP: Tiers(tiers, personne, role), affiliation,
 * cotisation, assurance, rubrique comptable, plan affiliation, //nombre assurés <BR>
 * La date de facturation doit toujours être renseignée.
 * 
 * @author: sau, mmu
 */
public class AFProcessFacturationManager extends BManager implements Serializable {

    private static final long serialVersionUID = 3945540650112325205L;
    public final static String ANNUEL = "12";
    public final static String JUIN_TRIMESTRE_2 = "06";
    public final static String MARS_TRIMESTRE_1 = "03";
    public final static String SEPTEMBRE_TRIMESTRE_3 = "09";

    public final static String TYPE_AFFILIATION__CAP_CGAS = "CAP_CGAS";
    public final static String TYPE_AFFILIATION_PARITAIRE = "PAR";

    public final static String TYPE_AFFILIATION_PARITAIRE_PERSONNEL = "PAR_PER";
    public final static String TYPE_AFFILIATION_PERSONNEL = "PER";
    public final static String TYPE_AFFILIATION_RI_PC = "RI_PC";
    public final static String TYPE_FACTURATION_ACOMPTE = "FAC_ACOMPTE";
    public final static String TYPE_FACTURATION_RELEVE = "FAC_RELEVE";

    private boolean filtrerPeriodicite;
    private java.lang.String forAffilieNumero = null;
    private String forAssuranceId = null;
    // private java.lang.String forRole = CodeSystem.ROLE_AFFILIE;
    private java.lang.String forDateFacturation = null;
    private java.lang.String forGenreAssurance = null;

    private String forIdAffiliation = null;
    private String forMotifFin = "";

    private String forIdTiers;
    private String forPlanAffiliationId = null;
    private java.lang.String forTypeAffiliation = null;
    private java.lang.String forTypeFacturation = null;
    private int forTypeFacturationPersonnelle = 0;
    private java.lang.String fromDate = null;
    private String idPassage;
    private String inCodeFacturation = "";
    private String notInCodeFacturation = "";
    private java.lang.String toDate = null;
    private boolean wantOrderByCodePeriodicite = false;
    private boolean wantSousEnsembleAffilie = false;

    /**
     * Renvoie la liste des champs.
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection()
                + "TITIERP.HTITIE, "
                + _getCollection()
                + "TITIERP.HTTLAN, "
                + _getCollection()
                + "TIPERSP.HPDDEC, "
                + _getCollection()
                + "TIPERSP.HPDNAI, "
                + _getCollection()
                + "TIPERSP.HPTSEX, "
                +
                // _getCollection() + "TIROLEP.TYTROL, " +
                _getCollection() + "AFAFFIP.MALNAF, " + _getCollection() + "AFAFFIP.MAIAFF, " + _getCollection()
                + "AFAFFIP.MADDEB, " + _getCollection() + "AFAFFIP.MADFIN, " + _getCollection() + "AFAFFIP.MATTAF, "
                + _getCollection() + "AFAFFIP.MATPER, " + _getCollection() + "AFAFFIP.MATBRA, " + _getCollection()
                + "AFAFFIP.MATCFA, " + _getCollection() + "AFCOTIP.MEICOT, " + _getCollection() + "AFCOTIP.MBIASS, "
                + _getCollection() + "AFCOTIP.MEDDEB, " + _getCollection() + "AFCOTIP.MEDFIN, " + _getCollection()
                + "AFCOTIP.METPER, " + _getCollection() + "AFCOTIP.MEMMAP, " + _getCollection() + "AFCOTIP.MEMTRI, "
                + _getCollection() + "AFCOTIP.MEMANN, " + _getCollection() + "AFCOTIP.MEMMEN, " + _getCollection()
                + "AFCOTIP.METMOT, " + _getCollection() + "AFCOTIP.METMOA, " + _getCollection() + "AFASSUP.MBIASS, "
                + _getCollection() + "AFASSUP.MBTCAN, " + _getCollection() + "AFASSUP.MBIREA, " + _getCollection()
                + "AFASSUP.MBLLIF, " + _getCollection() + "AFASSUP.MBLLID, " + _getCollection() + "AFASSUP."
                + AFAssurance.FIELD_DECOMPTE_13_RELEVE + ", " + _getCollection() + "AFASSUP.MBLLII, "
                + _getCollection() + "AFASSUP.MBIRUB, " + _getCollection() + "AFASSUP.MBTGEN, " + _getCollection()
                + "AFASSUP.MBTTCA, " + _getCollection() + "AFASSUP.MBTTYP, " + _getCollection() + "AFPLAFP.HFIAPP, "
                + _getCollection() + "AFPLAFP.HFIAPL, " + _getCollection() + "AFPLAFP.HFIAPR, " + _getCollection()
                + "AFPLAFP.MUBBLO, " + _getCollection() + "AFPLAFP.MUIPLA, " + _getCollection() + "AFPLAFP.MULFAC, "
                + _getCollection() + "AFPLAFP.MULLIB, " + _getCollection() + "CARUBRP.NATURERUBRIQUE, "
                + "ADH.HTITIE as IDCAISSEADH, " + "PRINC.HTITIE as IDCAISSEPRINC";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + "TITIERP ";
        sqlFrom += "INNER JOIN " + _getCollection() + "TIPERSP ON (" + _getCollection() + "TITIERP.HTITIE = "
                + _getCollection() + "TIPERSP.HTITIE)";
        sqlFrom += "INNER JOIN " + _getCollection() + "AFAFFIP ON (" + _getCollection() + "TITIERP.HTITIE = "
                + _getCollection() + "AFAFFIP.HTITIE)";
        sqlFrom += "INNER JOIN " + _getCollection() + "AFPLAFP ON (" + _getCollection() + "AFAFFIP.MAIAFF = "
                + _getCollection() + "AFPLAFP.MAIAFF AND " + _getCollection() + "AFPLAFP.MUBINA="
                + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + ")";
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "AFADHEP ON (" + _getCollection() + "AFAFFIP.MAIAFF = "
                + _getCollection() + "AFADHEP.MAIAFF AND " + _getCollection() + "AFADHEP.MRTADH="
                + IAFAdhesion.ADHESION_CAISSE_PRINCIPALE + ")";
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "AFPLCAP AS PRINC ON (" + _getCollection()
                + "AFADHEP.MSIPLC = PRINC.MSIPLC)";
        sqlFrom += "INNER JOIN " + _getCollection() + "AFCOTIP ON (" + _getCollection() + "AFPLAFP.MUIPLA = "
                + _getCollection() + "AFCOTIP.MUIPLA)";
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "AFPLCAP AS ADH ON (" + _getCollection()
                + "AFCOTIP.MSIPLC = ADH.MSIPLC)";
        sqlFrom += "INNER JOIN " + _getCollection() + "AFASSUP ON (" + _getCollection() + "AFCOTIP.MBIASS = "
                + _getCollection() + "AFASSUP.MBIASS)";
        sqlFrom += "INNER JOIN " + _getCollection() + "CARUBRP ON (" + _getCollection() + "AFASSUP.MBIRUB = "
                + _getCollection() + "CARUBRP.IDRUBRIQUE)";
        if (isWantSousEnsembleAffilie()) {
            sqlFrom += "INNER JOIN " + _getCollection() + "FAFIAFP ON (" + _getCollection() + "AFAFFIP.MAIAFF = "
                    + _getCollection() + "FAFIAFP.MAIAFF AND " + _getCollection() + "FAFIAFP.IDPASSAGE=" + idPassage
                    + " )";
            // sqlFrom += "INNER JOIN " + this._getCollection() + "FAPASSP ON (" + this._getCollection()
            // + "FAFIAFP.IDPASSAGE = " + this._getCollection() + "FAPASSP.IDPASSAGE";
        }
        return sqlFrom;
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (isWantOrderByCodePeriodicite()) {
            return _getCollection() + "AFCOTIP.METPER ASC";
        }
        return _getCollection() + "AFAFFIP.HTITIE," + _getCollection() + "AFPLAFP.MUIPLA," + _getCollection()
                + "AFASSUP.MBTGEN," + _getCollection() + "CARUBRP.IDEXTERNE," + _getCollection() + "AFCOTIP.MEICOT";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = _getCollection() + "AFAFFIP.MABTRA = 2 ";

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TITIERP.HTITIE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if (!JadeStringUtil.isEmpty(getForTypeFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (AFProcessFacturationManager.TYPE_FACTURATION_ACOMPTE.equals(getForTypeFacturation())) {
                sqlWhere += _getCollection() + "AFAFFIP.MABREP="
                        + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false));
            } else {
                sqlWhere += _getCollection() + "AFAFFIP.MABREP="
                        + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true));
            }

        }
        /*
         * if (! JadeStringUtil.isEmpty(getForRole())) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere
         * += _getCollection() + "TIROLEP.TYTROL=" + _dbWriteNumeric(statement.getTransaction(), getForRole()); }
         */

        if (!JadeStringUtil.isEmpty(getForGenreAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFASSUP.MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), getForGenreAssurance());
        }

        if (!JadeStringUtil.isEmpty(getInCodeFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MATCFA IN (" + getInCodeFacturation() + ")";
        }
        if (getNotInCodeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MATCFA not in (" + getNotInCodeFacturation() + ")";
        }
        if (!JadeStringUtil.isEmpty(getForPlanAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFPLAFP.MUIPLA="
                    + this._dbWriteNumeric(statement.getTransaction(), getForPlanAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getFromDate()) && !JadeStringUtil.isEmpty(getToDate())) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + _getCollection() + "AFAFFIP.MADDEB <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getToDate()) + " and (" + _getCollection()
                    + "AFAFFIP.MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()) + " or "
                    + _getCollection() + "AFAFFIP.MADFIN = 0)) and (" + _getCollection() + "AFCOTIP.MEDDEB <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getToDate()) + " and (" + _getCollection()
                    + "AFCOTIP.MEDFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()) + " or "
                    + _getCollection() + "AFCOTIP.MEDFIN = 0)) " + " and ((" + _getCollection() + "AFADHEP.MRDDEB <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getToDate()) + " AND (" + _getCollection()
                    + "AFADHEP.MRDFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()) + " OR "
                    + _getCollection() + "AFADHEP.MRDFIN = 0)) or (" + _getCollection() + "AFADHEP.MRIADH is null))";

            /*
             * periodicite de la cotisation.
             * 
             * si vrai, il faut:
             * 
             * - que la periodicite de la cotisation soit egale a celle de l'affilie - ou que la periodicite soit
             * trimestrielle si le mois de toDate est mars, juin, septembre ou decembre - ou que la periodicite soit
             * annuelle et que le mois de facturation de la cotis est celui de toDate.
             */
            if (filtrerPeriodicite) {
                int mois = 0;

                try {
                    mois = new JADate(toDate).getMonth();
                } catch (JAException e) {
                    e.printStackTrace();
                }

                sqlWhere += "AND (" + _getCollection() + "AFCOTIP.METPER=" + _getCollection() + "AFAFFIP.MATPER";

                if (mois % 3 == 0) {
                    sqlWhere += " OR " + _getCollection() + "AFCOTIP.METPER=" + CodeSystem.PERIODICITE_TRIMESTRIELLE;
                }

                // Si le mois est égal a 0
                // alors on facture sur le mois de décembre
                // "CodeSystem.MOIS_DECEMBRE"
                // Sinon, on facture sur le mois spécifié "mois"
                String str_mois = CodeSystem.MOIS_DECEMBRE;
                if (mois != 0) {
                    str_mois = CodeSystem.getCodeMois(String.valueOf(mois));
                    if (CodeSystem.TYPE_ASS_LAE.equalsIgnoreCase(getForGenreAssurance())) {
                        str_mois = CodeSystem.MOIS_DECEMBRE;
                    }
                }

                sqlWhere += " OR " + _getCollection() + "AFCOTIP.METPER=" + CodeSystem.PERIODICITE_MENSUELLE + " OR ("
                        + _getCollection() + "AFCOTIP.METPER=" + CodeSystem.PERIODICITE_ANNUELLE;

                if (CodeSystem.MOIS_DECEMBRE.equals(str_mois)) {
                    /*
                     * oca PO4985, les affiliés ayant des cotis annuelles avec le mois à 0 ne sont pas vues lors de la
                     * saisie des relevés de bouclement d'acomptes.
                     */
                    sqlWhere += " AND (" + _getCollection() + "AFCOTIP.METMOA=" + str_mois + " or " + _getCollection()
                            + "AFCOTIP.METMOA=0 " + " or " + _getCollection() + "AFCOTIP.METMOA=837006" + ")";
                } else {
                    sqlWhere += " AND (" + _getCollection() + "AFCOTIP.METMOA=" + str_mois + ")";
                }

                sqlWhere += "))";
            }
        }

        if (!JadeStringUtil.isEmpty(getForAffilieNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF="
                    + this._dbWriteString(statement.getTransaction(), getForAffilieNumero());
        }
        if (!JadeStringUtil.isEmpty(getForDateFacturation())) {

            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }

            try {
                // dates pour le mois
                JADate dateDebutMoisFacturation = new JADate(forDateFacturation);
                dateDebutMoisFacturation.setDay(1);

                JADate dateFinMoisFacturation = new JADate(dateDebutMoisFacturation.toAMJ());
                dateFinMoisFacturation.setDay(getSession().getApplication().getCalendar()
                        .daysInMonth(dateDebutMoisFacturation.getMonth(), dateDebutMoisFacturation.getYear()));

                // dates pour l'année
                JADate dateDebutAnneeFacturation = null;
                JADate dateFinAnneeFacturation = null;

                if (dateDebutMoisFacturation.getMonth() % 3 == 0) {
                    // dates pour le trimestre
                    JADate dateDebutTrimestreFacturation = new JADate(dateDebutMoisFacturation.toAMJ());
                    dateDebutTrimestreFacturation.setMonth(dateDebutTrimestreFacturation.getMonth() - 2);
                    // calcul date pour l'année
                    int annee = (dateDebutMoisFacturation).getYear();
                    dateDebutAnneeFacturation = new JADate(1, 1, annee);

                    dateFinAnneeFacturation = dateFinMoisFacturation;

                    if (dateFinAnneeFacturation.getMonth() == 12) {
                        sqlWhere += "("
                                + _getCollection()
                                + "AFAFFIP.MADDEB <> "
                                + _getCollection()
                                + "AFAFFIP.MADFIN AND "
                                + _getCollection()
                                + "AFAFFIP.MADDEB <= "
                                + this._dbWriteNumeric(statement.getTransaction(), dateFinMoisFacturation.toStrAMJ())
                                + " AND ("
                                + _getCollection()
                                + "AFAFFIP.MADFIN >= "
                                + this._dbWriteNumeric(statement.getTransaction(), dateDebutAnneeFacturation.toStrAMJ())
                                + " OR " + _getCollection() + "AFAFFIP.MADFIN = 0))";

                    } else {

                        sqlWhere += "("
                                + _getCollection()
                                + "AFAFFIP.MADDEB <> "
                                + _getCollection()
                                + "AFAFFIP.MADFIN AND "
                                + _getCollection()
                                + "AFAFFIP.MADDEB <= "
                                + this._dbWriteNumeric(statement.getTransaction(), dateFinMoisFacturation.toStrAMJ())
                                + " AND ("
                                + _getCollection()
                                + "AFAFFIP.MADFIN >= "
                                + this._dbWriteNumeric(statement.getTransaction(),
                                        dateDebutTrimestreFacturation.toStrAMJ()) + " OR " + _getCollection()
                                + "AFAFFIP.MADFIN = 0))";
                    }
                    /*
                     * and (" + _getCollection() + "TIROLEP.TYDDEB <= " + _dbWriteNumeric(statement.getTransaction(),
                     * dateFinMoisFacturation.toStrAMJ()) + " AND (" + _getCollection() + "TIROLEP.TYDFIN >= " +
                     * _dbWriteNumeric(statement.getTransaction(), dateDebutTrimestreFacturation.toStrAMJ()) + " OR " +
                     * _getCollection() + "TIROLEP.TYDFIN = 0))";
                     */
                } else {
                    // calcul date pour l'année
                    int annee = (dateDebutMoisFacturation).getYear();
                    dateDebutAnneeFacturation = new JADate(1, 1, annee);
                    dateFinAnneeFacturation = dateFinMoisFacturation;

                    // 1) sélection des affiliations actives
                    sqlWhere += "(" + _getCollection() + "AFAFFIP.MADDEB <> " + _getCollection()
                            + "AFAFFIP.MADFIN AND " + _getCollection() + "AFAFFIP.MADDEB <= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateFinMoisFacturation.toStrAMJ())
                            + " AND (" + _getCollection() + "AFAFFIP.MADFIN >= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateDebutMoisFacturation.toStrAMJ())
                            + " OR " + _getCollection() + "AFAFFIP.MADFIN = 0))";
                    /*
                     * and (" + _getCollection() + "TIROLEP.TYDDEB <= " + _dbWriteNumeric(statement.getTransaction(),
                     * dateFinMoisFacturation.toStrAMJ()) + " AND (" + _getCollection() + "TIROLEP.TYDFIN >= " +
                     * _dbWriteNumeric(statement.getTransaction(), dateDebutMoisFacturation.toStrAMJ()) + " OR " +
                     * _getCollection() + "TIROLEP.TYDFIN = 0))";
                     */

                }
                // 2) Sélection des cotisations actives
                // cotisations à facturation mensuelle
                sqlWhere += " AND (" + "(" + _getCollection() + "AFCOTIP.METPER = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.PERIODICITE_MENSUELLE) + " AND "
                        + _getCollection() + "AFCOTIP.MEDDEB <> " + _getCollection() + "AFCOTIP.MEDFIN AND "
                        + _getCollection() + "AFCOTIP.MEDDEB <= "
                        + this._dbWriteNumeric(statement.getTransaction(), dateFinMoisFacturation.toStrAMJ())
                        + " AND (" + _getCollection() + "AFCOTIP.MEDFIN >= "
                        + this._dbWriteNumeric(statement.getTransaction(), dateDebutMoisFacturation.toStrAMJ())
                        + " OR " + _getCollection() + "AFCOTIP.MEDFIN = 0)" + ")";

                // cotisations à facturation trimestrielle
                if (dateDebutMoisFacturation.getMonth() % 3 == 0) {
                    // dates pour le trimestre
                    JADate dateDebutTrimestreFacturation = new JADate(dateDebutMoisFacturation.toAMJ());
                    dateDebutTrimestreFacturation.setMonth(dateDebutTrimestreFacturation.getMonth() - 2);

                    JADate dateFinTrimestreFacturation = dateFinMoisFacturation;

                    sqlWhere += " OR ("
                            + _getCollection()
                            + "AFCOTIP.MEDDEB <> "
                            + _getCollection()
                            + "AFCOTIP.MEDFIN AND "
                            + _getCollection()
                            + "AFCOTIP.METPER = "
                            + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.PERIODICITE_TRIMESTRIELLE)
                            + " AND "
                            + _getCollection()
                            + "AFCOTIP.MEDDEB <= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateFinTrimestreFacturation.toStrAMJ())
                            + " AND ("
                            + _getCollection()
                            + "AFCOTIP.MEDFIN >= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateDebutTrimestreFacturation.toStrAMJ())
                            + " OR " + _getCollection() + "AFCOTIP.MEDFIN = 0)" + ")";
                }

                // Si le mois est égal a 0
                // alors on facture sur le mois de décembre
                // "CodeSystem.MOIS_DECEMBRE"
                // Sinon, on facture sur le mois spécifié "mois"
                String str_mois = CodeSystem.MOIS_DECEMBRE;
                if (dateFinAnneeFacturation.getMonth() != 0) {
                    str_mois = CodeSystem.getCodeMois(String.valueOf(dateFinAnneeFacturation.getMonth()));
                }
                if (dateFinAnneeFacturation.getMonth() == 12) {
                    // cotisations à facturation annuelle mois de décembre
                    sqlWhere += " OR (" + _getCollection() + "AFCOTIP.METPER = "
                            + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.PERIODICITE_ANNUELLE)
                            + " AND (" + _getCollection() + "AFCOTIP.METMOA = "
                            + this._dbWriteNumeric(statement.getTransaction(), str_mois) + " OR " + _getCollection()
                            + "AFCOTIP.METMOA=0 )" + " AND " + _getCollection() + "AFCOTIP.MEDDEB <= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateFinAnneeFacturation.toStrAMJ())
                            + " AND (" + _getCollection() + "AFCOTIP.MEDFIN >= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateDebutAnneeFacturation.toStrAMJ())
                            + " OR " + _getCollection() + "AFCOTIP.MEDFIN = 0)" + "))";
                } else {
                    sqlWhere += " OR (" + _getCollection() + "AFCOTIP.METPER = "
                            + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.PERIODICITE_ANNUELLE)
                            + " AND " + _getCollection() + "AFCOTIP.METMOA = "
                            + this._dbWriteNumeric(statement.getTransaction(), str_mois) + " AND " + _getCollection()
                            + "AFCOTIP.MEDDEB <= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateFinAnneeFacturation.toStrAMJ())
                            + " AND (" + _getCollection() + "AFCOTIP.MEDFIN >= "
                            + this._dbWriteNumeric(statement.getTransaction(), dateDebutAnneeFacturation.toStrAMJ())
                            + " OR " + _getCollection() + "AFCOTIP.MEDFIN = 0)" + "))";
                }
                sqlWhere += " and ((" + _getCollection() + "AFADHEP.MRDDEB <= "
                        + this._dbWriteNumeric(statement.getTransaction(), dateFinMoisFacturation.toStrAMJ())
                        + " AND (" + _getCollection() + "AFADHEP.MRDFIN >= "
                        + this._dbWriteNumeric(statement.getTransaction(), dateDebutMoisFacturation.toStrAMJ())
                        + " OR " + _getCollection()
                        + "AFADHEP.MRDFIN = 0";
                // ajout du filtre sur l'adhésion
                if (dateDebutMoisFacturation.getMonth() % 3 == 0) {
                    // dates pour le trimestre
                    JADate dateDebutTrimestreFacturation = new JADate(dateDebutMoisFacturation.toAMJ());
                    dateDebutTrimestreFacturation.setMonth(dateDebutTrimestreFacturation.getMonth() - 2);
                    JADate dateFinTrimestreFacturation = dateFinMoisFacturation;
                    sqlWhere +=  " OR (" + _getCollection() + "AFAFFIP.MATPER = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.PERIODICITE_TRIMESTRIELLE)
                        + " AND "
                        + _getCollection()
                        + "AFADHEP.MRTADH = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE)
                        + " AND "
                        + _getCollection()
                        + "AFADHEP.MRDDEB <= "
                        + this._dbWriteNumeric(statement.getTransaction(), dateFinTrimestreFacturation.toStrAMJ())
                        + " AND "
                        + _getCollection()
                        + "AFADHEP.MRDFIN >= "
                        + this._dbWriteNumeric(statement.getTransaction(), dateDebutTrimestreFacturation.toStrAMJ())
                        + ")";
                 }
                sqlWhere += ")) or (" + _getCollection() + "AFADHEP.MRIADH is null))";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!JadeStringUtil.isEmpty(getForTypeAffiliation())) {

            // Seulement pour les Affiliations qui contiennent que des
            // Cotisations personnelles
            if (getForTypeAffiliation().equals(AFProcessFacturationManager.TYPE_AFFILIATION_PERSONNEL)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection()
                        + "AFAFFIP.MATBRA <> "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CodeSystem.BRANCHE_ECO_NON_ACTIFS_INSTITUTION) + " AND " + "(" + _getCollection()
                        + "AFAFFIP.MATTAF in (";

                /*
                 * Inforom 314s: Si forTypeFacturationPersonnelle = 0 => facturation périodique de toutes les cot.pers.
                 * Si forTypeFacturationPersonnelle = 1 => facturation périodique des indépendants et TSE. Si
                 * forTypeFacturationPersonnelle = 2 => facturation périodique des non actifs.
                 */
                if (getForTypeFacturationPersonnelle() != AFNewProcessFacturation.PERIODIQUE_COT_PERS_NAC) {
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP) + ", "
                            + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                            + ", "
                            + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)
                            + ", " + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_TSE);
                }
                if (getForTypeFacturationPersonnelle() == AFNewProcessFacturation.PERIODIQUE_COT_PERS) {
                    sqlWhere += ", ";
                }
                if (getForTypeFacturationPersonnelle() != AFNewProcessFacturation.PERIODIQUE_COT_PERS_IND) {
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                            + ", "
                            + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_SELON_ART_1A);
                }
                sqlWhere += "))"; // Une cotisation peut ne pas avoir de masse: cf FFPP
                // + " and " + _getCollection() + "AFCOTIP.MEMTRI <> 0 ";

                // Seulement pour les Affiliations qui contiennent que des
                // Cotisations paritaires (+ Personnel TYPE_AFFILI_INDEP_EMPLOY)
            } else if (getForTypeAffiliation().equalsIgnoreCase(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "("
                        + _getCollection()
                        + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_EMPLOY)
                        +
                        // Une cotisation peut ne pas avoir de masse: cf FFPP
                        // " and " + _getCollection() + "AFCOTIP.MEMMAP <> 0
                        " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP_EMPLOY) +

                        " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_EMPLOY_D_F)
                        +
                        // Ajout des non-actifs pour CCJU
                        " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF) + ")";
                // Une cotisation peut ne pas avoir de masse: cf FFPP
                // " and (" + _getCollection() + "AFCOTIP.MEMTRI <> 0 or " +
                // _getCollection() + "AFCOTIP.MEMMAP <> 0)))";

                // Pour toutes les Affiliations
            } else if (getForTypeAffiliation().equalsIgnoreCase(
                    AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE_PERSONNEL)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere +=

                "(" + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_SELON_ART_1A)
                        + " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_TSE) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_EMPLOY) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_EMPLOY_D_F) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_LTN) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP_EMPLOY) + ")";

                // Une cotisation peut ne pas avoir de masse: cf FFPP
                // + " and (" +
                // _getCollection() + "AFCOTIP.MEMTRI <> 0 or " +
                // _getCollection() + "AFCOTIP.MEMMAP <> 0)";
                // Uniquement les affiliation FFPP paritaire
            } else if (getForTypeAffiliation().equalsIgnoreCase(AFProcessFacturationManager.TYPE_AFFILIATION_RI_PC)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection()
                        + "AFAFFIP.MATBRA = "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CodeSystem.BRANCHE_ECO_NON_ACTIFS_INSTITUTION) + " AND " + "(" + _getCollection()
                        + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF) + " or "
                        + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP_EMPLOY) + ") ";

            } else if (getForTypeAffiliation().equalsIgnoreCase(AFProcessFacturationManager.TYPE_AFFILIATION__CAP_CGAS)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += " ( " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_CGAS_EMPLOYEUR)
                        + " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_CGAS_INDEPENDANT)
                        + " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_CAP_EMPLOYEUR)
                        + " or " + _getCollection() + "AFAFFIP.MATTAF = "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_CAP_INDEPENDANT)
                        + " ) ";

            }
        }

        // Ajout de deux conditions de recherche pour la solution 'de course'
        if (!JadeStringUtil.isEmpty(getForIdAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MAIAFF="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }

        // Ajout de deux conditions de recherche pour la solution 'de course'
        if (!JadeStringUtil.isEmpty(getForMotifFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFCOTIP.METMOT = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMotifFin());
        }

        // Ajout de deux conditions de recherche pour la solution 'de course'
        if (!JadeStringUtil.isEmpty(getForAssuranceId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFCOTIP.MBIASS = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceId());
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité. -
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFProcessFacturationViewBean();
    }

    /**
     * vrai pour ne selectionner que les cotisations dont la periodicite coincide avec celle de l'affilie. si vrai, il
     * faut:
     * 
     * - que la periodicite de la cotisation soit egale a celle de l'affilie - ou que la periodicite soit trimestrielle
     * si le mois de toDate est mars, juin, septembre ou decembre - ou que la periodicite soit annuelle et que le mois
     * de facturation de la cotis est celui de toDate.
     */
    public void filtrerPeriodicites(boolean filtrerPeriodicite) {
        this.filtrerPeriodicite = filtrerPeriodicite;
    }

    public java.lang.String getForAffilieNumero() {
        return forAffilieNumero;
    }

    // ***********************************************
    // Getter
    // ***********************************************
    /*
     * public java.lang.String getForRole() { return forRole; }
     */
    public String getForAssuranceId() {
        return forAssuranceId;
    }

    public java.lang.String getForDateFacturation() {
        return forDateFacturation;
    }

    /**
     * @return
     */
    public java.lang.String getForGenreAssurance() {
        return forGenreAssurance;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForMotifFin() {
        return forMotifFin;
    }

    /**
     * @return
     */
    public String getForPlanAffiliationId() {
        return forPlanAffiliationId;
    }

    public java.lang.String getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    public java.lang.String getForTypeFacturation() {
        return forTypeFacturation;
    }

    public int getForTypeFacturationPersonnelle() {
        return forTypeFacturationPersonnelle;
    }

    public java.lang.String getFromDate() {
        return fromDate;
    }

    public String getInCodeFacturation() {
        return inCodeFacturation;
    }

    public String getNotInCodeFacturation() {
        return notInCodeFacturation;
    }

    public java.lang.String getToDate() {
        return toDate;
    }

    public boolean isWantOrderByCodePeriodicite() {
        return wantOrderByCodePeriodicite;
    }

    public boolean isWantSousEnsembleAffilie() {
        return wantSousEnsembleAffilie;
    }

    /**
     * Controle si un passage comporte des affiliés spécifiques à facturer.
     * 
     * @return boolean
     */

    public void setForAffilieNumero(java.lang.String string) {
        forAffilieNumero = string;
    }

    // ***********************************************
    // Setter
    // ***********************************************
    /*
     * public void setForRole(java.lang.String string) { forRole = string; }
     */

    public void setForAssuranceId(java.lang.String string) {
        forAssuranceId = string;
    }

    public void setForDateFacturation(java.lang.String string) {
        forDateFacturation = string;
    }

    /**
     * @param string
     */
    public void setForGenreAssurance(java.lang.String string) {
        forGenreAssurance = string;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForIdPassage(String idPassage) {
        // TODO Auto-generated method stub
        this.idPassage = idPassage;

    }

    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    public void setForMotifFin(String forMotifFin) {
        this.forMotifFin = forMotifFin;
    }

    /**
     * @param string
     */
    public void setForPlanAffiliationId(String string) {
        forPlanAffiliationId = string;
    }

    public void setForTypeAffiliation(java.lang.String string) {
        forTypeAffiliation = string;
    }

    public void setForTypeFacturation(java.lang.String forTypeFacturation) {
        this.forTypeFacturation = forTypeFacturation;
    }

    public void setForTypeFacturationPersonnelle(int forCategoriePersonnelle) {
        forTypeFacturationPersonnelle = forCategoriePersonnelle;
    }

    public void setFromDate(java.lang.String string) {
        fromDate = string;
    }

    public void setInCodeFacturation(String inCodeFacturation) {
        this.inCodeFacturation = inCodeFacturation;
    }

    public void setNotInCodeFacturation(String notInCodeFacturation) {
        this.notInCodeFacturation = notInCodeFacturation;
    }

    public void setToDate(java.lang.String string) {
        toDate = string;
    }

    public void setWantOrderByCodePeriodicite(boolean wantOrderByCodePeriodite) {
        wantOrderByCodePeriodicite = wantOrderByCodePeriodite;
    }

    public void setWantSousEnsembleAffilie(boolean wantSousEnsembleAffilie) {
        this.wantSousEnsembleAffilie = wantSousEnsembleAffilie;
    }

}
