package globaz.naos.db.cotisation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité Cotisation.
 * 
 * @author sau
 */
public class AFCotisationManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AFFICHAGE_RESUME = "resume";
    public static final String AFFICHAGE_RESUME_DATE = "resumeDate";

    private String dateDebutLessEqual = "";
    private String dateDebutPeriode;
    private String dateFinGreaterEqual = "";
    private String dateFinGreater = "";

    private String dateFinPeriode;
    private java.lang.String forAdhesionId;
    private java.lang.String forAffiliationId;
    private java.lang.String forAnneeActive;

    private java.lang.String forAnneeDeclaration;
    private String forAssuranceCanton;
    private java.lang.String forAssuranceGenre;
    private java.lang.String forAssuranceId;
    private String forDate;

    private java.lang.Boolean forDateDifferente = Boolean.FALSE;
    private String forDateFin;
    private String forGenreAssurance;

    private Boolean forInactive;
    private java.lang.Boolean forIsMaisonMere;
    private String forMasseAnnuelleGreaterEqual = "";

    private String forNotMotifFin;

    private java.lang.String forPlanAffiliationId;
    private java.lang.String forPlanCaisseId;

    private String forTypeAssurance;

    private String notForTypeAssurance;
    private String notForCotisationId;

    private java.lang.String order;

    private String showInactif = "false";

    private String typeAffichage = "";

    private boolean withAnnuelZero = false;

    @Override
    protected String _getFields(BStatement statement) {
        // System.out.println("appel de getfield");
        if (AFCotisationManager.AFFICHAGE_RESUME.equals(typeAffichage)) {
            // affichage résumé
            return "0 MEICOT, "
                    + _getCollection()
                    + "AFCOTIP.MBIASS, MBTGEN, 0 HTITIE, 0 MAIAFF, MIN(MEDDEB) MEDDEB, MAX(MEDFIN) MEDFIN, MIN(MEDFIN) MEDFINMIN, 0 METMOT, METPER, 0 MEIMER, 0 MEMMAP, 0 MEMTRI, 0 MEMSEM, 0 MEMANN, 0 MEMMEN, '' PSPY, 0 MENANN, '2' MEBMER, 0 MUIPLA, MSIPLC, MRIADH, '2' METPEE, METMOA, 0 MBIRUB";
        } else if (AFCotisationManager.AFFICHAGE_RESUME_DATE.equals(typeAffichage)) {
            // affichage résumé
            return "MIN(MEDDEB) MEDDEB, MAX(MEDFIN) MEDFIN, MIN(MEDFIN) MEDFINMIN";
        } else {
            return "*";
        }
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + "AFCOTIP";

        // return _getCollection()+"AFCOTIP";
        if (JadeStringUtil.isEmpty(getForPlanAffiliationId()) && JadeStringUtil.isEmpty(getForAdhesionId())
                && !JadeStringUtil.isEmpty(getForAffiliationId())) {
            if ("true".equalsIgnoreCase(showInactif)) {
                fromClause += " INNER JOIN " + _getCollection() + "AFPLAFP ON " + _getCollection() + "AFCOTIP.MUIPLA="
                        + _getCollection() + "AFPLAFP.MUIPLA";
            } else {
                fromClause += " INNER JOIN "
                        + _getCollection()
                        + "AFPLAFP ON ("
                        + _getCollection()
                        + "AFCOTIP.MUIPLA="
                        + _getCollection()
                        + "AFPLAFP.MUIPLA AND "
                        + _getCollection()
                        + "AFPLAFP.MUBINA="
                        + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                                BConstants.DB_TYPE_BOOLEAN_CHAR) + ")";
            }
        }
        // ajout jointure sur assurance
        fromClause += " INNER JOIN " + _getCollection() + "AFASSUP ON " + _getCollection() + "AFCOTIP.MBIASS="
                + _getCollection() + "AFASSUP.MBIASS";
        // ajout jointure sur assurance
        fromClause += " INNER JOIN " + _getCollection() + "CARUBRP ON " + _getCollection() + "AFASSUP.MBIRUB = "
                + _getCollection() + "CARUBRP.IDRUBRIQUE";

        return fromClause;
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getDateDebutLessEqual())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " MEDDEB <= " + this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutLessEqual());
        }

        if (!JadeStringUtil.isEmpty(getDateFinGreaterEqual())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " ( MEDFIN=0 OR MEDFIN >= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getDateFinGreaterEqual()) + " ) ";
        }

        if (!JadeStringUtil.isEmpty(getDateFinGreater())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " ( MEDFIN=0 OR MEDFIN > "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getDateFinGreater()) + " ) ";
        }

        if (!JadeStringUtil.isEmpty(getForMasseAnnuelleGreaterEqual())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFCOTIP.MEMMAP >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMasseAnnuelleGreaterEqual());
        }

        if (getForInactive() != null) {
            if (getForInactive().booleanValue() == false) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "INACTIVE="
                        + this._dbWriteBoolean(statement.getTransaction(), getForInactive(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR, "forInactive");
            }
        }

        if (JadeStringUtil.isEmpty(getForPlanAffiliationId()) && JadeStringUtil.isEmpty(getForAdhesionId())) {

            if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "AFPLAFP.MAIAFF="
                        + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
            }
        }

        if (!JadeStringUtil.isEmpty(getForPlanAffiliationId())) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MUIPLA=" + this._dbWriteNumeric(statement.getTransaction(), getForPlanAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getForPlanCaisseId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MSIPLC=" + this._dbWriteNumeric(statement.getTransaction(), getForPlanCaisseId());
        }

        if (!JadeStringUtil.isEmpty(getForAssuranceGenre())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceGenre());
        }

        if ((getForIsMaisonMere() != null) && getForIsMaisonMere().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MEBMER ="
                    + this._dbWriteBoolean(statement.getTransaction(), getForIsMaisonMere(),
                            BConstants.DB_TYPE_BOOLEAN_NUMERIC, "forIsMaisonMere");
        }

        if (!JadeStringUtil.isEmpty(forTypeAssurance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTTYP=" + this._dbWriteNumeric(statement.getTransaction(), forTypeAssurance);
        }

        if (!JadeStringUtil.isEmpty(notForTypeAssurance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTTYP<>" + this._dbWriteNumeric(statement.getTransaction(), notForTypeAssurance);
        }

        if (!JadeStringUtil.isEmpty(getForAssuranceCanton())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceCanton());
        }

        if (!JadeStringUtil.isEmpty(forGenreAssurance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTGEN=" + this._dbWriteNumeric(statement.getTransaction(), forGenreAssurance);
        }

        if (!JadeStringUtil.isEmpty(getForAdhesionId())) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MRIADH=" + this._dbWriteNumeric(statement.getTransaction(), getForAdhesionId());
        }

        if (getForDateFin() != null) { // on veut pouvoir selectionner pour une
            // date vide

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MEDFIN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin());
        }

        if (JadeStringUtil.isEmpty(getForAssuranceId())) {

            // traitement du positionnement
            if (!JadeStringUtil.isEmpty(getForAnneeDeclaration())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                String date00 = getForAnneeDeclaration() + "0000";
                String date99 = getForAnneeDeclaration() + "9999";
                sqlWhere += "((MEDDEB < " + date99 + " AND MEDFIN >= " + date00 + ") OR (MEDDEB < " + date99
                        + " AND MEDFIN = 0))";
            }

            // traitement du positionnement
            /*
             * if (! JadeStringUtil.isEmpty(getForAffiliationId()) && ! JadeStringUtil.isEmpty(getForIdTiers())) { if
             * (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere += "MAIAFF=" +
             * _dbWriteNumeric(statement.getTransaction(), getForAffiliationId()); return sqlWhere; }
             */

            /*
             * if (! JadeStringUtil.isEmpty(getForAffiliationId())) { if (sqlWhere.length() != 0) { sqlWhere += " AND ";
             * } sqlWhere += "MAIAFF=" + _dbWriteNumeric(statement.getTransaction(), getForAffiliationId()); }
             */

            // traitement du positionnement
            /*
             * if (! JadeStringUtil.isEmpty(getForIdTiers())) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; }
             * sqlWhere += "HTITIE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTiers()); return sqlWhere; }
             */

        } else {
            // traitement du positionnement
            if (!JadeStringUtil.isEmpty(getForAssuranceId())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "AFCOTIP.MBIASS="
                        + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceId());

                // traitement du positionnement
                /*
                 * if (! JadeStringUtil.isEmpty(getForAffiliationId())) { if (sqlWhere.length() != 0) { sqlWhere +=
                 * " AND "; } sqlWhere += "MAIAFF=" + _dbWriteNumeric(statement.getTransaction(),
                 * getForAffiliationId()); return sqlWhere; }
                 */
            }
        }
        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForAnneeActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // String dateMax =
            // _dbWriteDateAMJ(statement.getTransaction(), "31.12." +
            // getForAnneeActive());
            sqlWhere += "MEDDEB<" + getForAnneeActive() + "1231 " + " AND ( MEDFIN=0 OR MEDFIN>" + getForAnneeActive()
                    + "0101" + " )";
        }

        if (!JadeStringUtil.isEmpty(getForNotMotifFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " METMOT <> " + this._dbWriteNumeric(statement.getTransaction(), getForNotMotifFin());
        }

        if (JadeStringUtil.isEmpty(order)) {
            order = _getCollection() + "AFCOTIP.MBIASS, MEDDEB";
        }

        if (!JadeStringUtil.isEmpty(forDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MEDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate)
                    + " AND (MEDFIN=0 OR MEDFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate) + ")";
        }

        if (!JadeStringUtil.isEmpty(dateDebutPeriode) && !JadeStringUtil.isEmpty(dateFinPeriode)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MEDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), dateFinPeriode)
                    + " AND (MEDFIN=0 OR MEDFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), dateDebutPeriode)
                    + ")";

            int mois = 0;

            try {
                mois = new JADate(dateFinPeriode).getMonth();
            } catch (JAException e) {
                e.printStackTrace();
            }
            // tri sur la périodicité
            sqlWhere += " AND (";
            sqlWhere += "METPER=" + globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE + " OR (METPER="
                    + globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE;
            if ((mois == 12) || withAnnuelZero) {
                // si décembre on test le code système de décembre ou 0
                sqlWhere += " AND (METMOA=" + globaz.naos.translation.CodeSystem.getCodeMois(String.valueOf(mois))
                        + " OR METMOA=0))";
            } else {
                sqlWhere += " AND METMOA=" + globaz.naos.translation.CodeSystem.getCodeMois(String.valueOf(mois)) + ")";
            }
            // plus trimestrielle
            if (mois % 3 == 0) {
                sqlWhere += " OR METPER=" + globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE;
            }
            sqlWhere += ")";

        }

        if (AFCotisationManager.AFFICHAGE_RESUME.equals(typeAffichage)) {
            sqlWhere += " GROUP BY " + _getCollection()
                    + "AFCOTIP.MBIASS, IDEXTERNE, MBTGEN, METPER, MSIPLC, MRIADH, METMOA";
        }

        if (getForDateDifferente().equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MEDDEB <> MEDFIN";
        }

        if (!JadeStringUtil.isEmpty(getNotForCotisationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MEICOT <> " + this._dbWriteNumeric(statement.getTransaction(), getNotForCotisationId());
        }

        return sqlWhere;
    }

    public void _setForInactiveString(String forInactive) {
        setForInactive(new Boolean(forInactive));
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        AFCotisation cot = new AFCotisation();
        return cot;
    }

    public String getDateDebutLessEqual() {
        return dateDebutLessEqual;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinGreaterEqual() {
        return dateFinGreaterEqual;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public java.lang.String getForAdhesionId() {
        return forAdhesionId;
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public java.lang.String getNotForCotisationId() {
        return notForCotisationId;
    }

    public java.lang.String getForAnneeActive() {
        return forAnneeActive;
    }

    /**
     * @return la valeur courante du canton de l'assurance
     */
    public String getForAssuranceCanton() {
        return forAssuranceCanton;
    }

    public java.lang.String getForAnneeDeclaration() {
        return forAnneeDeclaration;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * @return
     */
    public java.lang.String getForAssuranceGenre() {
        return forAssuranceGenre;
    }

    public java.lang.String getForAssuranceId() {
        return forAssuranceId;
    }

    /**
     * critère pour les cotisations actives pour une date donnée.
     * 
     * @return
     */
    public String getForDate() {
        return forDate;
    }

    public java.lang.Boolean getForDateDifferente() {
        return forDateDifferente;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * getter pour l'attribut for genre assurance.
     * 
     * @return la valeur courante de l'attribut for genre assurance
     */
    public String getForGenreAssurance() {
        return forGenreAssurance;
    }

    public Boolean getForInactive() {
        return forInactive;
    }

    /**
     * @return
     */
    public java.lang.Boolean getForIsMaisonMere() {
        return forIsMaisonMere;
    }

    public String getForMasseAnnuelleGreaterEqual() {
        return forMasseAnnuelleGreaterEqual;
    }

    public String getForNotMotifFin() {
        return forNotMotifFin;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getForPlanAffiliationId() {
        return forPlanAffiliationId;
    }

    public java.lang.String getForPlanCaisseId() {
        return forPlanCaisseId;
    }

    /**
     * getter pour l'attribut for type assurance.
     * 
     * @return la valeur courante de l'attribut for type assurance
     */
    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    /**
     * getter pour l'attribut not for type assurance.
     * 
     * @return la valeur courante de l'attribut not for type assurance
     */
    public String getNotForTypeAssurance() {
        return notForTypeAssurance;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public String getShowInactif() {
        return showInactif;
    }

    /**
     * @return
     */
    public String getTypeAffichage() {
        return typeAffichage;
    }

    public boolean isWithAnnuelZero() {
        return withAnnuelZero;
    }

    public void setDateDebutLessEqual(String dateDebutLessEqual) {
        this.dateDebutLessEqual = dateDebutLessEqual;
    }

    public void setDateFinGreaterEqual(String dateFinGreaterEqual) {
        this.dateFinGreaterEqual = dateFinGreaterEqual;
    }

    public void setForAdhesionId(java.lang.String string) {
        forAdhesionId = string;
    }

    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    public void setNotForCotisationId(java.lang.String string) {
        notForCotisationId = string;
    }

    public void setForAnneeActive(java.lang.String newForAnneeActive) {
        // System.out.println("annee "+newForAnneeActive);
        forAnneeActive = newForAnneeActive;
    }

    public void setForAnneeDeclaration(java.lang.String newForAnneeDeclaration) {
        forAnneeDeclaration = newForAnneeDeclaration;
    }

    /**
     * @param string
     */

    public void setForAssuranceCanton(String forAssuranceCanton) {
        this.forAssuranceCanton = forAssuranceCanton;
    }

    public void setForAssuranceGenre(java.lang.String string) {
        forAssuranceGenre = string;
    }

    public void setForAssuranceId(java.lang.String newForAssuranceId) {
        forAssuranceId = newForAssuranceId;
    }

    /**
     * ajoute un critère qui retourne les cotisations actives pour la date donnée.
     * 
     * @param forDate
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDateDifferente(java.lang.Boolean forDateDifferente) {
        this.forDateDifferente = forDateDifferente;
    }

    public void setForDateFin(String string) {
        forDateFin = string;
    }

    /**
     * setter pour l'attribut for genre assurance.
     * 
     * @param forGenreAssurance
     *            une nouvelle valeur pour cet attribut
     */
    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
    }

    public void setForInactive(Boolean forInactive) {
        this.forInactive = forInactive;
    }

    /**
     * @param boolean1
     */
    public void setForIsMaisonMere(java.lang.Boolean boolean1) {
        forIsMaisonMere = boolean1;
    }

    public void setForMasseAnnuelleGreaterEqual(String forMasseAnnuelleGreaterEqual) {
        this.forMasseAnnuelleGreaterEqual = forMasseAnnuelleGreaterEqual;
    }

    public void setForNotMotifFin(String string) {
        forNotMotifFin = string;
    }

    /**
     * ajoute un critère qui retourne les cotisations qui chevauchent la période donnée.
     * 
     * @param dateDebutPeriode
     * @param dateFinPeriode
     */
    public void setForPeriode(String dateDebutPeriode, String dateFinPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setForPlanAffiliationId(java.lang.String string) {
        forPlanAffiliationId = string;
    }

    public void setForPlanCaisseId(java.lang.String string) {
        forPlanCaisseId = string;
    }

    /**
     * setter pour l'attribut for type assurance.
     * 
     * @param forTypeAssurance
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = forTypeAssurance;
    }

    /**
     * setter pour l'attribut not for type assurance.
     * 
     * @param notForTypeAssurance
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForTypeAssurance(String notForTypeAssurance) {
        this.notForTypeAssurance = notForTypeAssurance;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }

    public void setShowInactif(String showInactif) {
        this.showInactif = showInactif;
    }

    public String getDateFinGreater() {
        return dateFinGreater;
    }

    public void setDateFinGreater(String dateFinGreater) {
        this.dateFinGreater = dateFinGreater;
    }

    /**
     * @param string
     */
    public void setTypeAffichage(String string) {
        // System.out.println("type "+string);
        typeAffichage = string;
    }

    public void setWithAnnuelZero(boolean withAnnuelZero) {
        this.withAnnuelZero = withAnnuelZero;
    }

}
