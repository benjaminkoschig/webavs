package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.HashSet;

public class CGPeriodeComptable extends CGNeedExerciceComptable implements ITreeListable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static int AK_IDEXTERNE = 1;

    public final static String CS_ANNUEL = "709004";
    public final static String CS_CLOTURE = "709005";
    public final static String CS_CODE_ANNUEL = "13";
    public final static String CS_CODE_CLOTURE = "99";
    public final static String CS_CODE_SEMESTRE_1 = "61";
    public final static String CS_CODE_SEMESTRE_2 = "62";
    public final static String CS_CODE_TRIMESTRE_1 = "31";
    public final static String CS_CODE_TRIMESTRE_2 = "32";
    public final static String CS_CODE_TRIMESTRE_3 = "33";
    public final static String CS_CODE_TRIMESTRE_4 = "34";
    public final static String CS_MENSUEL = "709001";
    public final static String CS_SEMESTRIEL = "709003";

    public final static String CS_TRIMESTRIEL = "709002";

    public static final String FIELD_CODE = "CODE";

    public static final String FIELD_DATEDEBUT = "DATEDEBUT";
    public static final String FIELD_DATEFIN = "DATEFIN";
    public static final String FIELD_ESTCOLTURE = "ESTCOLTURE";
    public static final String FIELD_IDANNONCE = "IDANNONCE";
    public static final String FIELD_IDBOUCLEMENT = "IDBOUCLEMENT";

    public static final String FIELD_IDEXERCOMPTABLE = "IDEXERCOMPTABLE";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String FIELD_IDJOURNAL2 = "IDJOURNAL2";
    public static final String FIELD_IDJOURNAL3 = "IDJOURNAL3";
    public static final String FIELD_IDPERIODECOMPTABLE = "IDPERIODECOMPTABLE";
    public static final String FIELD_IDTYPEPERIODE = "IDTYPEPERIODE";
    public static final String FIELD_NOMFICHIER = "NOMFICHIER";
    public static final String ID_PERIODE_TOUT_EXERCICE = "0";
    private final static String LABEL_PREFIXE = "PERIODE_COMPTABLE_";

    public static final String TABLE_NAME = "CGPERIP";
    private String code = new String();
    private String dateDebut = new String();
    private String dateFin = new String();
    private Boolean estCloture = new Boolean(false);
    private String idAnnonce = new String();
    private String idBouclement = new String();
    private String idJournal = new String();
    // Désigne le journal de clôture de la première période de l'exercice
    // suivant
    private String idJournal2 = "";

    // Désigne le journal de clôture de la période de type clôture
    private String idJournal3 = "";

    private String idPeriodeComptable = new String();

    private String idTypePeriode = new String();
    
    private String nomFichier = "";

    /**
     * Commentaire relatif au constructeur CGPeriodeComptable
     */
    public CGPeriodeComptable() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdPeriodeComptable(_incCounter(transaction, "0"));
    }

    /**
     * Annule l'effacement de la période si la période n'est pas clôturée ou contient encore des journaux.
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // on peut supprimer si :
        // 1) la période est clôturée
        // 2) elle ne contient pas de journaux

        // //La période n'est pas clôturée
        // if (!isEstCloture().booleanValue()) {
        // _addError(transaction, label("SUPPR_PAS_CLOTURE_ERROR"));
        // }
        // else {

        // Contrôle s'il y a des journaux
        CGJournalManager mgr = new CGJournalManager();
        mgr.setSession(getSession());
        mgr.setForIdPeriodeComptable(getIdPeriodeComptable());
        mgr.setForIdExerciceComptable(getIdExerciceComptable());
        mgr.find(transaction);

        if (mgr != null && mgr.size() > 0) {
            _addError(transaction, label("SUPPR_JOURNAUX_PRESENT_ERROR"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idPeriodeComptable = statement.dbReadNumeric(FIELD_IDPERIODECOMPTABLE);
        idExerciceComptable = statement.dbReadNumeric(FIELD_IDEXERCOMPTABLE);
        idJournal = statement.dbReadNumeric(FIELD_IDJOURNAL);
        idJournal2 = statement.dbReadNumeric(FIELD_IDJOURNAL2);
        idJournal3 = statement.dbReadNumeric(FIELD_IDJOURNAL3);
        idBouclement = statement.dbReadNumeric(FIELD_IDBOUCLEMENT);
        code = statement.dbReadString(FIELD_CODE);
        dateDebut = statement.dbReadDateAMJ(FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(FIELD_DATEFIN);
        estCloture = statement.dbReadBoolean(FIELD_ESTCOLTURE);
        idTypePeriode = statement.dbReadNumeric(FIELD_IDTYPEPERIODE);
        idAnnonce = statement.dbReadNumeric(FIELD_IDANNONCE);
        nomFichier = statement.dbReadString(FIELD_NOMFICHIER);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        super._validate(statement);

        if (JadeStringUtil.isBlank(getIdTypePeriode())) {
            _addError(statement.getTransaction(), label("VALID_ERR_1"));
            return;
        }

        // Validation du format de la date de début.
        /* dateDebut - obligatoire */
        if (JAUtil.isDateEmpty(getDateDebut())) {
            _addError(statement.getTransaction(), label("DATE_DEBUT_INVALID"));
            return;
        } else {
            try {
                BSessionUtil.checkDateGregorian(getSession(), getDateDebut());
            } catch (Exception e) {
                _addError(statement.getTransaction(), label("DATE_DEBUT_INVALID"));
                return;
            }
        }
        /* dateFin - obligatoire */
        if (JAUtil.isDateEmpty(getDateFin())) {
            _addError(statement.getTransaction(), label("DATE_FIN_INVALID"));
            return;
        } else {
            try {
                BSessionUtil.checkDateGregorian(getSession(), getDateFin());
            } catch (Exception e) {
                _addError(statement.getTransaction(), label("DATE_FIN_INVALID"));
                return;
            }
        }

        JADate dateDebut = new JADate();
        dateDebut.fromString(getDateDebut());
        JADate dateFin = new JADate();
        dateFin.fromString(getDateFin());

        JADate dateDebutExerciceComptable = new JADate();
        dateDebutExerciceComptable.fromString(getExerciceComptable().getDateDebut());

        JADate dateFinExerciceComptable = new JADate();
        dateFinExerciceComptable.fromString(getExerciceComptable().getDateFin());

        // Date comprise dans l'exercice
        if ((getSession().getApplication().getCalendar().compare(dateDebutExerciceComptable, dateDebut) == JACalendar.COMPARE_FIRSTLOWER || getSession()
                .getApplication().getCalendar().compare(dateDebutExerciceComptable, dateDebut) == JACalendar.COMPARE_EQUALS)
                && (getSession().getApplication().getCalendar().compare(dateFinExerciceComptable, dateFin) == JACalendar.COMPARE_FIRSTUPPER || getSession()
                        .getApplication().getCalendar().compare(dateFinExerciceComptable, dateFin) == JACalendar.COMPARE_EQUALS)) {

            ;// ok, date comprise dans l'exercice comptable

        } else {
            _addError(statement.getTransaction(), label("VALID_ERR_8"));
            return;
        }

        // date début <= date fin
        if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut.toStrAMJ(), dateFin.toStrAMJ())) {
            _addError(statement.getTransaction(), label("VALID_ERR_9"));
            return;
        }

        if (getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()
                || JadeStringUtil.isBlank(getCode())) {

            // Date de début == 1er jour du mois
            if (dateDebut.getDay() != 1) {
                _addError(statement.getTransaction(), label("VALID_ERR_10"));
                return;
            }

            // Date de fin == dernier jour du mois
            JACalendarGregorian calGregorian = new JACalendarGregorian();
            if (!calGregorian.isLastInMonth(dateFin)) {
                _addError(statement.getTransaction(), label("VALID_ERR_11"));
                return;
            }

            if (isAnnuel()) {
                if (dateDebut.getMonth() != 1) {
                    _addError(statement.getTransaction(), label("VALID_ERR_12"));
                    return;
                }
                if (dateFin.getMonth() != 12) {
                    _addError(statement.getTransaction(), label("VALID_ERR_13"));
                    return;
                }
                if (dateFin.getYear() != dateDebut.getYear()) {
                    _addError(statement.getTransaction(), label("VALID_ERR_14"));
                    return;
                }

                setCode(CS_CODE_ANNUEL);
            } else if (isCloture()) {
                setCode(CS_CODE_CLOTURE);
            } else if (isMensuel()) {
                setCode(((dateDebut.getMonth() < 10) ? "0" : "") + dateDebut.getMonth());
                if (dateFin.getMonth() != dateDebut.getMonth()) {
                    _addError(statement.getTransaction(), label("VALID_ERR_15"));
                    return;
                }
            }

            else if (isSemestriel()) {
                // 1er semestre -> date début == 1er janver
                if (dateDebut.getMonth() <= 6) {
                    if (dateDebut.getMonth() != 1) {
                        _addError(statement.getTransaction(), label("VALID_ERR_16"));
                        return;
                    }

                    // 1er semestre -> date fin == 31 juin
                    if (dateFin.getMonth() != 3) {
                        _addError(statement.getTransaction(), label("VALID_ERR_17"));
                        return;
                    }

                    setCode(CS_CODE_SEMESTRE_1);
                }
                // 2eme semestre -> date début == 1er juillet
                else {
                    if (dateDebut.getMonth() != 7) {
                        _addError(statement.getTransaction(), label("VALID_ERR_18"));
                        return;
                    }

                    // 2ème semestre -> date fin == 31 décembre
                    if (dateFin.getMonth() != 12) {
                        _addError(statement.getTransaction(), label("VALID_ERR_19"));
                        return;
                    }

                    setCode(CS_CODE_SEMESTRE_2);
                }
            }

            else if (isTrimestriel()) {
                // 1er trimestre -> date début == 1er janver
                if (dateDebut.getMonth() <= 3) {
                    if (dateDebut.getMonth() != 1) {
                        _addError(statement.getTransaction(), label("VALID_ERR_20"));
                        return;
                    }

                    if (dateFin.getMonth() != 3) {
                        _addError(statement.getTransaction(), label("VALID_ERR_21"));
                        return;
                    }

                    setCode(CS_CODE_TRIMESTRE_1);
                }
                // 2ème trimestre -> date début == 1er avril
                else if (dateDebut.getMonth() <= 6 && dateDebut.getMonth() > 3) {
                    if (dateDebut.getMonth() != 4) {
                        _addError(statement.getTransaction(), label("VALID_ERR_22"));
                        return;
                    }
                    if (dateFin.getMonth() != 6) {
                        _addError(statement.getTransaction(), label("VALID_ERR_23"));
                        return;
                    }

                    setCode(CS_CODE_TRIMESTRE_2);
                }
                // 3ème trimestre -> date début == 1er juillet
                else if (dateDebut.getMonth() <= 9 && dateDebut.getMonth() > 6) {
                    if (dateDebut.getMonth() != 7) {
                        _addError(statement.getTransaction(), label("VALID_ERR_24"));
                        return;
                    }
                    if (dateFin.getMonth() != 9) {
                        _addError(statement.getTransaction(), label("VALID_ERR_25"));
                        return;
                    }

                    setCode(CS_CODE_TRIMESTRE_3);
                }
                // 4ème trimestre -> date début == 1er octobre
                else {
                    if (dateDebut.getMonth() != 10) {
                        _addError(statement.getTransaction(), label("VALID_ERR_26"));
                        return;
                    }
                    if (dateFin.getMonth() != 12) {
                        _addError(statement.getTransaction(), label("VALID_ERR_27"));
                        return;
                    }

                    setCode(CS_CODE_TRIMESTRE_4);
                }
            } else {
                setCode("00");
            }
        }
        if (JadeStringUtil.isBlank(getCode())) {
            _addError(statement.getTransaction(), label("VALID_ERR_2"));
            return;
        }

        if (JadeStringUtil.isBlank(getIdBouclement())) {
            _addError(statement.getTransaction(), label("VALID_ERR_3"));
            return;
        }

        if (getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()) {
            if (getIdTypePeriode().equals(CS_MENSUEL)) {
                CGBouclement bouclement = new CGBouclement();
                bouclement.setSession(getSession());
                bouclement.setIdBouclement(getIdBouclement());
                bouclement.retrieve(statement.getTransaction());
                if (bouclement.isNew()) {
                    _addError(statement.getTransaction(), label("VALID_ERR_4"));
                    return;
                } else {
                    if (!bouclement.getIdTypeBouclement().equals(CGBouclement.CS_BOUCLEMENT_MENSUEL_AVS)) {
                        _addError(statement.getTransaction(), label("VALID_ERR_5"));
                        return;
                    }
                }

            } else if (getIdTypePeriode().equals(CS_ANNUEL)) {
                CGBouclement bouclement = new CGBouclement();
                bouclement.setSession(getSession());
                bouclement.setIdBouclement(getIdBouclement());
                bouclement.retrieve(statement.getTransaction());
                if (bouclement.isNew()) {
                    _addError(statement.getTransaction(), label("VALID_ERR_6"));
                    return;
                } else {
                    if (!bouclement.getIdTypeBouclement().equals(CGBouclement.CS_BOUCLEMENT_ANNUEL_AVS)) {
                        _addError(statement.getTransaction(), label("VALID_ERR_7"));
                        return;
                    }
                }

            }
        }

        // Vérifier que la période comptable n'empiète pas sur une autre période
        // comptable de même type
        CGPeriodeComptableManager periodeManager = new CGPeriodeComptableManager();
        periodeManager.setSession(getSession());
        periodeManager.setForIdExerciceComptable(getIdExerciceComptable());

        // Contrôle des clés alternés, à savoir :
        // Il n'est pas possible de créer deux périodes avec des définitions
        // identiques,
        // soit idTypePeriode, dateDebut et dateFin doivent être uniques pour un
        // exercice comptable donné (clé alternée).
        // De plus, deux périodes de même type ne peuvent pas se chevaucher au
        // niveau des dates pour un exercice comptable donné.

        String currentKeyAlterne = dateDebut.toStrAMJ() + dateFin.toStrAMJ() + getIdTypePeriode();
        String currentType = getIdTypePeriode();
        String currentId = getIdPeriodeComptable();
        periodeManager.find(statement.getTransaction());
        for (int i = 0; i < periodeManager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) periodeManager.getEntity(i);
            // Lors d'un update, ne pas prendre un compte la période a updater.
            if (currentId.equals(periode.getIdPeriodeComptable())) {
                continue;
            }

            String keyAlterne = periode.getDateDebut() + periode.getDateFin() + periode.getIdTypePeriode();
            if (currentKeyAlterne.equals(keyAlterne)) {
                _addError(statement.getTransaction(), label("VALID_ERR_28"));
                return;
            }

            if (currentType.equals(periode.getIdTypePeriode())) {

                // Date comprise dans la période
                if ((getSession().getApplication().getCalendar().compare(new JADate(periode.getDateDebut()), dateDebut) == JACalendar.COMPARE_FIRSTLOWER || getSession()
                        .getApplication().getCalendar().compare(new JADate(periode.getDateDebut()), dateDebut) == JACalendar.COMPARE_EQUALS)
                        && (getSession().getApplication().getCalendar()
                                .compare(new JADate(periode.getDateFin()), dateDebut) == JACalendar.COMPARE_FIRSTUPPER || getSession()
                                .getApplication().getCalendar().compare(new JADate(periode.getDateFin()), dateDebut) == JACalendar.COMPARE_EQUALS)) {

                    _addError(statement.getTransaction(), label("VALID_ERR_29"));
                    return;
                }
            }
        }

    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case AK_IDEXTERNE:
                statement.writeKey(FIELD_IDEXERCOMPTABLE,
                        _dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), ""));
                statement.writeKey(FIELD_IDTYPEPERIODE,
                        _dbWriteNumeric(statement.getTransaction(), getIdTypePeriode(), ""));
                statement.writeKey(FIELD_DATEDEBUT, _dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), ""));
                statement.writeKey(FIELD_DATEFIN, _dbWriteDateAMJ(statement.getTransaction(), getDateFin(), ""));
                break;
            default:
                super._writeAlternateKey(statement, alternateKey);
                break;
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDPERIODECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdPeriodeComptable(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELD_IDPERIODECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdPeriodeComptable(), "idPeriodeComptable"));
        statement.writeField(FIELD_IDEXERCOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_IDJOURNAL2,
                _dbWriteNumeric(statement.getTransaction(), getIdJournal2(), "idJournal2"));
        statement.writeField(FIELD_IDJOURNAL3,
                _dbWriteNumeric(statement.getTransaction(), getIdJournal3(), "idJournal3"));
        statement.writeField(FIELD_IDBOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), "idBouclement"));
        statement.writeField(FIELD_CODE, _dbWriteString(statement.getTransaction(), getCode(), "code"));
        statement.writeField(FIELD_DATEDEBUT, _dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField(FIELD_DATEFIN, _dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField(
                FIELD_ESTCOLTURE,
                _dbWriteBoolean(statement.getTransaction(), isEstCloture(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estCloture"));
        statement.writeField(FIELD_IDTYPEPERIODE,
                _dbWriteNumeric(statement.getTransaction(), getIdTypePeriode(), "idTypePeriode"));
        statement.writeField(FIELD_IDANNONCE, _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField(FIELD_NOMFICHIER, _dbWriteString(statement.getTransaction(), getNomFichier(), "nomFichier"));
    }

    @Override
    public BManager[] getChilds() throws Exception {
        BManager[] managers = new BManager[1];
        CGJournalListViewBean journalManager = new CGJournalListViewBean();
        journalManager.setForIdPeriodeComptable(getIdPeriodeComptable());

        managers[0] = journalManager;

        return managers;
    }

    public String getCode() {
        return code;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.11.2002 10:15:22)
     * 
     * @return String
     */
    public String getFullDescription() {

        String fullDescription = "";
        try {
            String year = JACalendar.getYear(dateDebut) + " ";
            String month = JACalendar.getMonthName(JACalendar.getMonth(dateDebut), getSession().getUserInfo()
                    .getLanguage());

            if (CS_MENSUEL.equals(getIdTypePeriode())) {
                fullDescription += month + " ";
                fullDescription += year;
            } else if (CS_TRIMESTRIEL.equals(getIdTypePeriode())) {
                int monthNum = JACalendar.getMonth(getDateDebut());
                if (monthNum < 5) {
                    fullDescription += label("1_TRIMESTRE");
                } else if (monthNum > 8) {
                    fullDescription += label("3_TRIMESTRE");
                } else {
                    fullDescription += label("2_TRIMESTRE");
                }
                fullDescription += " " + year;
            } else if (CS_SEMESTRIEL.equals(getIdTypePeriode())) {
                int monthNum = JACalendar.getMonth(getDateDebut());
                if (monthNum < 7) {
                    fullDescription += label("1_TRIMESTRE");
                } else {
                    fullDescription += label("2_TRIMESTRE");
                }
                fullDescription += " " + year;

            } else if (CS_ANNUEL.equals(getIdTypePeriode())) {
                fullDescription += CodeSystem.getLibelle(getSession(), CGExerciceComptable.CS_EXERCICE) + " ";
                fullDescription += year;
            } else if (CS_CLOTURE.equals(getIdTypePeriode())) {
                fullDescription += label("CLOTURE") + " " + year;
            } else {
                fullDescription += getDateDebut() + " - " + getDateFin();
            }
        } catch (Exception e) {
            return label("DESCR_ERROR");
        }

        // si periode cloturée, on ajoute un astérix
        if (isEstCloture().booleanValue()) {
            fullDescription += " *";
        }

        return fullDescription;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }
    
    public String getNomFichier() {
        return nomFichier;
    }

    public String getIdBouclement() {
        return idBouclement;
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:43:15)
     * 
     * @return String
     */
    public String getIdJournal2() {
        return idJournal2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:43:15)
     * 
     * @return String
     */
    public String getIdJournal3() {
        return idJournal3;
    }

    /**
     * Getter
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    public String getIdTypePeriode() {
        return idTypePeriode;
    }

    @Override
    public String getLibelle() {
        return getFullDescription();
    }

    public String getLibelleJournal1() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return label("NO_LIBELLE");
        }
        try {
            CGJournal journal = new CGJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal());
            journal.retrieve();
            if (journal.isNew()) {
                return label("NO_JOURNAL");
            } else {
                return journal.getFullDescription();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLibelleJournal2() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal2())) {
            return label("NO_LIBELLE");
        }
        try {
            CGJournal journal = new CGJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal2());
            journal.retrieve();
            if (journal.isNew()) {
                return label("NO_JOURNAL");
            } else {
                return journal.getFullDescription();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLibelleJournal3() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal3())) {
            return label("NO_LIBELLE");
        }
        try {
            CGJournal journal = new CGJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal3());
            journal.retrieve();
            if (journal.isNew()) {
                return label("NO_JOURNAL");
            } else {
                return journal.getFullDescription();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getMonthLibelle() {
        try {
            return JACalendar.getMonthName(JACalendar.getMonth(dateDebut), getSession().getUserInfo().getLanguage());
        } catch (Exception e) {
            return label("DESCR_ERROR");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 15:12:56)
     * 
     * @return boolean
     */
    public boolean isAnnuel() {
        return CS_ANNUEL.equals(getIdTypePeriode());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 15:12:56)
     * 
     * @return boolean
     */
    public boolean isCloture() {
        return CS_CLOTURE.equals(getIdTypePeriode());
    }

    public boolean isDateIncluded(BStatement statement, String date) throws Exception {
        if (JAUtil.isDateEmpty(date)) {
            throw new Exception("La date n'est pas renseignée.");
        }

        JACalendar calendar = new JACalendarGregorian();
        int compareResult = calendar.compare(date, getDateDebut());

        if (compareResult == JACalendar.COMPARE_FIRSTLOWER) {
            return false;
        }

        compareResult = calendar.compare(date, getDateFin());
        if (compareResult == JACalendar.COMPARE_FIRSTUPPER) {
            return false;
        }

        return true;
    }

    public Boolean isEstCloture() {
        return estCloture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 15:14:10)
     * 
     * @return boolean
     */
    public boolean isMensuel() {
        return CS_MENSUEL.equals(getIdTypePeriode());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 15:12:56)
     * 
     * @return boolean
     */
    public boolean isSemestriel() {
        return CS_SEMESTRIEL.equals(getIdTypePeriode());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 15:12:56)
     * 
     * @return boolean
     */
    public boolean isTrimestriel() {
        return CS_TRIMESTRIEL.equals(getIdTypePeriode());
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 15:01:41)
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */
    public final CGPeriodeComptable retrieveLastPeriode(globaz.globall.db.BTransaction transaction) throws Exception {
        return retrieveLastPeriode(null, transaction);
    }

    /**
     * Method retrieveLastPeriode. Récupération de la période comptable précédente à celle passée en paramètre.
     * Ordonnancement des périodes selon le schéma suivant :
     * M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL M : Mensuel T : Trimestriel S : Semestriel AN:
     * Annuel CL: Cloture
     * 
     * @param typePeriode
     * @param transaction
     * @return CGPeriodeComptable
     * @throws Exception
     */

    public final CGPeriodeComptable retrieveLastPeriode(String typePeriode, globaz.globall.db.BTransaction transaction)
            throws Exception {

        // ALGORITHME :
        // Pour trouver la période précédente, on sélectionne tous les exercices
        // comptables antérieurs à l'exercice courant (exercice courant
        // compris),
        // triés par date de fin décroissant. On cherche une période antérieure
        // à la période actuelle dans ces exercices (la première trouvée étant
        // la
        // bonne, excepté cas spéciaux) trié par date de fin ET type de période
        // décroissant, car :
        // période mensuelle < trimestrielle < semestrielle < annuelle < de
        // cloture pour la même date de fin

        CGExerciceComptableManager managerEx = new CGExerciceComptableManager();
        managerEx.setSession(getSession());
        managerEx.setForIdMandat(getExerciceComptable().getIdMandat());
        managerEx.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
        managerEx.setUntilDateFin(getExerciceComptable().getDateFin());
        managerEx.find(transaction);

        for (int i = 0; i < managerEx.size(); i++) {
            HashSet exceptTypesPeriode = null;
            CGExerciceComptable exercice = (CGExerciceComptable) managerEx.getEntity(i);
            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession(getSession());
            manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());

            boolean isWithinSameExercice = false;
            if (getIdExerciceComptable().equals(exercice.getIdExerciceComptable())) {
                isWithinSameExercice = true;
            }

            if (isWithinSameExercice) {
                exceptTypesPeriode = new HashSet();
                exceptTypesPeriode.add(CGPeriodeComptable.CS_CLOTURE);
                if (!isAnnuel() && !isCloture()) {
                    exceptTypesPeriode.add(CGPeriodeComptable.CS_ANNUEL);
                }
            }
            if (exceptTypesPeriode != null) {
                manager.setExceptIdTypePeriode(exceptTypesPeriode);
            }

            if (typePeriode != null) {
                manager.setForIdTypePeriode(typePeriode);
            }

            manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_AND_TYPE_DESC);
            manager.setUntilDateFin(getDateFin());
            manager.find(transaction);

            // Cas spéciaux, selon ordonnancement des periodes (valide si dans
            // meme exercice):
            //
            // M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL
            //

            if (isWithinSameExercice) {

                // On exclu le semestre 2 + (cloture + annuel fait ci-dessus)
                if (CS_CODE_TRIMESTRE_4.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getCode().equals(CS_CODE_SEMESTRE_2)
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu le semestre 1 + (cloture + annuel fait ci-dessus)
                else if (CS_CODE_TRIMESTRE_2.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getCode().equals(CS_CODE_SEMESTRE_1)
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu T4 + S2 + (cloture + annuel fait ci-dessus)
                else if ("12".equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getCode().equals(CS_CODE_SEMESTRE_2) && !per.getCode().equals(CS_CODE_TRIMESTRE_4)
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu T3 + (cloture + annuel fait ci-dessus)
                else if ("09".equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getCode().equals(CS_CODE_TRIMESTRE_3)
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu T2 + S1 + (cloture + annuel fait ci-dessus)
                else if ("06".equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getCode().equals(CS_CODE_SEMESTRE_1) && !per.getCode().equals(CS_CODE_TRIMESTRE_2)
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu T1 + (cloture + annuel fait ci-dessus)
                else if ("03".equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getCode().equals(CS_CODE_TRIMESTRE_1)
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                } else {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

            } else {
                for (int j = 0; j < manager.size(); j++) {
                    CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                    if (!per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                        return per;
                    }
                }
            }
        }
        return null;

    }

    /**
     * Renvoie la période précédente contigüe à la période actuelle. Date de création : (14.03.2003 15:01:41)
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */
    public final CGPeriodeComptable retrieveLastPeriodeContigue(globaz.globall.db.BTransaction transaction)
            throws Exception {
        return retrieveLastPeriodeContigue(null, transaction);
    }

    /**
     * Renvoie la période précédente contigüe à la période actuelle.
     * M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL Si M1, prev == CL de exercice suivant; Si M3, prev
     * == M2 Si T1, prev == M3 ou (CL de exercice suivants). Si M4, prev == T1 ou M3 Si T2, prev = M6 ou T1 Si S1, prev
     * = T2, M6 ou (CL de exerice suivant) Si M7, prev == S1 ou T2 ou M6 Si T3, prev == M9 ou T2; Si M10, prev == T3 ou
     * M9 Si T4, prev == M12 ou T3 Si S2, prev == T4 ou M12 ou S1 Si AN, prev == S2 ou T4 ou M12 Si CL, prev == AN Date
     * de création : (14.03.2003 15:01:41)
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */
    public final CGPeriodeComptable retrieveLastPeriodeContigue(String typePeriode,
            globaz.globall.db.BTransaction transaction) throws Exception {

        CGPeriodeComptable periode = retrieveLastPeriode(typePeriode, transaction);
        if (periode == null || periode.isNew()) {
            return null;
        }

        // Cas de la période M1
        if (!getIdExerciceComptable().equals(periode.getIdExerciceComptable())) {
            if ("01".equals(getCode())) {
                if (periode.isCloture()) {
                    return periode;
                } else {
                    return null;
                }
            }
            // Si T1, prev == M3 ou (CL de exercice suivants).
            if (CS_CODE_TRIMESTRE_1.equals(getCode())) {
                if (periode.isCloture()) {
                    return periode;
                } else {
                    return null;
                }
            }
            if (CS_CODE_SEMESTRE_1.equals(getCode())) {
                if (periode.isCloture()) {
                    return periode;
                } else {
                    return null;
                }
            }
        }

        // Même exercice
        else {
            // Si M2, prev == M1;
            if ("02".equals(getCode())) {
                if ("01".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("03".equals(getCode())) {
                if ("02".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_1.equals(getCode())) {
                if ("03".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("04".equals(getCode())) {
                if ("03".equals(periode.getCode()) || CS_CODE_TRIMESTRE_1.equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("05".equals(getCode())) {
                if ("04".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("06".equals(getCode())) {
                if ("05".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_2.equals(getCode())) {
                if (CS_CODE_SEMESTRE_1.equals(periode.getCode()) || "06".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_SEMESTRE_1.equals(getCode())) {
                if (CS_CODE_TRIMESTRE_2.equals(periode.getCode()) || "06".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("07".equals(getCode())) {
                if ("06".equals(periode.getCode()) || CS_CODE_TRIMESTRE_2.equals(periode.getCode())
                        || CS_CODE_SEMESTRE_1.equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("08".equals(getCode())) {
                if ("07".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("09".equals(getCode())) {
                if ("08".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_3.equals(getCode())) {
                if (CS_CODE_TRIMESTRE_2.equals(periode.getCode()) || "09".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("10".equals(getCode())) {
                if (CS_CODE_TRIMESTRE_3.equals(periode.getCode()) || "09".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("11".equals(getCode())) {
                if ("10".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("12".equals(getCode())) {
                if ("11".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_4.equals(getCode())) {
                if (CS_CODE_TRIMESTRE_3.equals(periode.getCode()) || "12".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_SEMESTRE_2.equals(getCode())) {
                if (CS_CODE_TRIMESTRE_4.equals(periode.getCode()) || "12".equals(periode.getCode())
                        || CS_CODE_SEMESTRE_1.equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (isAnnuel()) {
                if (CS_CODE_TRIMESTRE_4.equals(periode.getCode()) || "12".equals(periode.getCode())
                        || CS_CODE_SEMESTRE_2.equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (isCloture()) {
                if (periode.isAnnuel()) {
                    return periode;
                } else {
                    return null;
                }
            }

        }
        return null;
    }

    /**
     * Date de création : (14.03.2003 15:01:41)
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */
    public final CGPeriodeComptable retrieveNextPeriode(globaz.globall.db.BTransaction transaction) throws Exception {
        return retrieveNextPeriode(null, transaction);
    }

    /**
     * Récupération de la période comptable suivante à celle passée en paramètre. Ordonnancement des périodes selon le
     * schéma suivant : M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL M : Mensuel T : Trimestriel S :
     * Semestriel AN: Annuel CL: Cloture
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */
    public final CGPeriodeComptable retrieveNextPeriode(String typePeriode, globaz.globall.db.BTransaction transaction)
            throws Exception {

        // ALGORITHME :
        // Pour trouver la période suivante, on sélectionne tous les exercices
        // comptables postérieurs à l'exercice courant (exercice courant
        // compris),
        // triés par date de fin croissant. On cherche une période postérieure à
        // la période actuelle dans ces exercices (la première trouvée étant la
        // bonne) trié par date de fin ET type de période croissant, car :
        // période mensuelle < trimestrielle < semestrielle < annuelle < de
        // cloture pour la même date de fin

        CGExerciceComptableManager managerEx = new CGExerciceComptableManager();
        managerEx.setSession(getSession());
        managerEx.setForIdMandat(getExerciceComptable().getIdMandat());
        managerEx.setOrderBy(CGExerciceComptableManager.TRI_DATE_DEBUT);
        managerEx.setFromDateDebut(getExerciceComptable().getDateDebut());
        managerEx.find(transaction);

        for (int i = 0; i < managerEx.size(); i++) {
            CGExerciceComptable exercice = (CGExerciceComptable) managerEx.getEntity(i);
            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession(getSession());
            manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());

            boolean isWithinSameExercice = false;
            if (getIdExerciceComptable().equals(exercice.getIdExerciceComptable())) {
                isWithinSameExercice = true;
            }

            if (typePeriode != null) {
                manager.setForIdTypePeriode(typePeriode);
            }

            manager.setFromDateFin(getDateFin());
            manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_ASC_AND_TYPE_ASC);
            manager.find(transaction);

            // Cas spéciaux, selon ordonnancement des periodes (valide si dans
            // meme exercice):
            // Règle, il faut retourner la première periode différente de la
            // periode courante (sauf exception) :
            // Example : Si période = M2, le requete va retourné dans l'ordre :
            // M2, M3, T1, M4, .... la règle par défaut peur donc s'appliquer
            // Mais, si periode = AN, la requete va retourné dans l'ordre (car
            // même date de fin) :
            // M12, T4, S2, AN et CL il faut donc exclure : M12, T4 et AN.
            //
            // *ordre dans lequel les periodes sont retournée*
            // M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL
            // **************************************************************

            if (isWithinSameExercice) {

                // On exclu M3 + T1
                if (CS_CODE_TRIMESTRE_1.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"03".equals(per.getCode()) && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu M6 + T2
                else if (CS_CODE_TRIMESTRE_2.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"06".equals(per.getCode()) && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu M6 + T2 + S1
                else if (CS_CODE_SEMESTRE_1.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"06".equals(per.getCode()) && !CS_CODE_TRIMESTRE_2.equals(per.getCode())
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu M9 + T3
                if (CS_CODE_TRIMESTRE_3.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"09".equals(per.getCode()) && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu M12 + T4
                else if (CS_CODE_TRIMESTRE_4.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"12".equals(per.getCode()) && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu M12 + T4 + S2
                else if (CS_CODE_SEMESTRE_2.equals(getCode())) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"12".equals(per.getCode()) && !CS_CODE_TRIMESTRE_4.equals(per.getCode())
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // On exclu M12 + T4 + S2 + AN
                else if (isAnnuel()) {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!"12".equals(per.getCode()) && !CS_CODE_TRIMESTRE_4.equals(per.getCode())
                                && !CS_CODE_SEMESTRE_2.equals(per.getCode())
                                && !per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }

                // passe a l'exercice suivant
                else if (isCloture()) {
                    continue;
                } else {
                    for (int j = 0; j < manager.size(); j++) {
                        CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                        if (!per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                            return per;
                        }
                    }
                }
            } else {
                for (int j = 0; j < manager.size(); j++) {
                    CGPeriodeComptable per = (CGPeriodeComptable) manager.getEntity(j);
                    if (!per.getIdPeriodeComptable().equals(getIdPeriodeComptable())) {
                        return per;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Renvoie la période suivante contigüe à la période actuelle. Date de création : (14.03.2003 15:01:41)
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */
    public final CGPeriodeComptable retrieveNextPeriodeContigue(globaz.globall.db.BTransaction transaction)
            throws Exception {
        return retrieveNextPeriodeContigue(null, transaction);
    }

    /**
     * Renvoie la période suivante contigüe à la période actuelle.
     * M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL Si M1, next == M2; Si M3, next == T1 ou M4; Si M6,
     * next == T2 ou S1, ou M7; Si M9, next == T3 ou M10 Si M12, next == T4 ou S2, ou AN; Si T1, next == M4 ou T2; Si
     * T2, next == S1 ou M7 ou T3; Si T3, next == M10 ou T4; Si T4, next == S2 ou AN ; Si S1, next == M7 ou S2 Si S2,
     * next == AN Si AN, next == CL Si CL, next == M1 ou T1 ou S1 de exercice suivants Date de création : (14.03.2003
     * 15:01:41)
     * 
     * @return globaz.helios.db.comptes.CGPeriodeComptable
     */

    public final CGPeriodeComptable retrieveNextPeriodeContigue(String typePeriode,
            globaz.globall.db.BTransaction transaction) throws Exception {
        CGPeriodeComptable periode = retrieveNextPeriode(typePeriode, transaction);
        if (periode == null || periode.isNew()) {
            return null;
        }

        // Cas de la période de Cloture
        if (!getIdExerciceComptable().equals(periode.getIdExerciceComptable())) {
            if (isCloture()) {
                if ("01".equals(periode.getCode()) || CS_CODE_TRIMESTRE_1.equals(periode.getCode())
                        || CS_CODE_SEMESTRE_1.equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            }
        }

        // Même exercice
        else {

            // Si M1, next == M2;
            if ("01".equals(getCode())) {
                if ("02".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("02".equals(getCode())) {
                if ("03".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("03".equals(getCode())) {
                if (CS_CODE_TRIMESTRE_1.equals(periode.getCode()) || "04".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_1.equals(getCode())) {
                if (CS_CODE_TRIMESTRE_2.equals(periode.getCode()) || "04".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("04".equals(getCode())) {
                if ("05".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("05".equals(getCode())) {
                if ("06".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("06".equals(getCode())) {
                if (CS_CODE_TRIMESTRE_2.equals(periode.getCode()) || CS_CODE_SEMESTRE_1.equals(periode.getCode())
                        || "07".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_2.equals(getCode())) {
                if (CS_CODE_SEMESTRE_1.equals(periode.getCode()) || CS_CODE_TRIMESTRE_3.equals(periode.getCode())
                        || "07".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_SEMESTRE_1.equals(getCode())) {
                if (CS_CODE_SEMESTRE_2.equals(periode.getCode()) || "07".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("07".equals(getCode())) {
                if ("08".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("08".equals(getCode())) {
                if ("09".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("09".equals(getCode())) {
                if (CS_CODE_TRIMESTRE_3.equals(periode.getCode()) || "10".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_3.equals(getCode())) {
                if (CS_CODE_TRIMESTRE_4.equals(periode.getCode()) || "10".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("10".equals(getCode())) {
                if ("11".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("11".equals(getCode())) {
                if ("12".equals(periode.getCode())) {
                    return periode;
                } else {
                    return null;
                }
            } else if ("12".equals(getCode())) {
                if (CS_CODE_SEMESTRE_2.equals(periode.getCode()) || CS_CODE_TRIMESTRE_4.equals(periode.getCode())
                        || periode.isAnnuel()) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_TRIMESTRE_4.equals(getCode())) {
                if (CS_CODE_SEMESTRE_2.equals(periode.getCode()) || periode.isAnnuel()) {
                    return periode;
                } else {
                    return null;
                }
            } else if (CS_CODE_SEMESTRE_2.equals(getCode())) {
                if (periode.isAnnuel()) {
                    return periode;
                } else {
                    return null;
                }
            } else if (isAnnuel()) {
                // soit la période suivante est de type clôture
                if (periode.isCloture()) {
                    return periode;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public void setCode(String newCode) {
        code = newCode;
    }

    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(String newDateFin) {
        dateFin = newDateFin;
    }

    public void setEstCloture(Boolean newEstCloture) {
        estCloture = newEstCloture;
    }

    public void setIdAnnonce(String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }
    
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:43:15)
     * 
     * @param newIdjournal2
     *            String
     */
    public void setIdJournal2(String newIdjournal2) {
        idJournal2 = newIdjournal2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:43:15)
     * 
     * @param newIdjournal3
     *            String
     */
    public void setIdJournal3(String newIdjournal3) {
        idJournal3 = newIdjournal3;
    }

    /**
     * Setter
     */
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    /**
     * Method setIdPeriodeComptableFrom. Définit l'ID de la période comptable via un code standard AVS et un ID
     * d'exercice comptable.
     * 
     * @param code
     * @param idExerciceComptable
     * @throws Exception
     */
    public void setIdPeriodeComptableFrom(String code, String idExerciceComptable) throws java.lang.Exception {
        setIdPeriodeComptableFrom(code, idExerciceComptable, null);
    }

    /**
     * Method setIdPeriodeComptableFrom. Définit l'ID de la période comptable via un code standard AVS et un ID
     * d'exercice comptable.
     * 
     * @param code
     * @param idExerciceComptable
     * @param transaction
     * @throws Exception
     */
    public void setIdPeriodeComptableFrom(String code, String idExerciceComptable,
            globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForCode(code);
        manager.find(transaction);
        if (manager.size() == 0) {
            throw (new Exception(label("NO_PERIODE_MATCH")));
        } else if (manager.size() > 1) {
            throw (new Exception(label("MORE_PERIODE_MATCH")));
        } else {
            setIdPeriodeComptable(((CGPeriodeComptable) manager.getEntity(0)).getIdPeriodeComptable());
        }
    }

    public void setIdTypePeriode(String newIdTypePeriode) {
        idTypePeriode = newIdTypePeriode;
    }
}
