package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.osiris.db.comptes.CAJournalManager;
import java.io.Serializable;

public class FAPassage extends BEntity implements IFAPassage, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_AOUT = "901011";
    public final static String CS_AVRIL = "901007";
    public final static String CS_DECEMBRE = "901015";

    public final static String CS_ETAT_ANNULE = "902006";
    public final static String CS_ETAT_COMPTABILISE = "902003";
    public final static String CS_ETAT_IMPRIME = "902004";
    public final static String CS_ETAT_NON_COMPTABILISE = "902005";
    // Etat
    public final static String CS_ETAT_OUVERT = "902001";
    public final static String CS_ETAT_TRAITEMENT = "902002";
    public final static String CS_ETAT_VALIDE = "902007";

    public final static String CS_FEVRIER = "901005";
    // Type facturation
    public final static String CS_JANVIER = "901004";
    public final static String CS_JUILLET = "901010";

    public final static String CS_JUIN = "901009";
    public final static String CS_MAI = "901008";
    public final static String CS_MARS = "901006";
    public final static String CS_NOVEMBRE = "901014";
    public final static String CS_OCTOBRE = "901013";
    public final static String CS_SEPTEMBRE = "901012";
    // Tri passage facturation
    public final static String CS_TRI_DATEFACTURATION = "908001";
    public final static String CS_TRI_LIBELLE = "908002";
    public final static String CS_TRI_NUMERO = "908003";
    public final static String CS_TYPE_EXTERNE = "901002";
    // Type facturation
    public final static String CS_TYPE_INTERNE = "901001";
    public final static String CS_TYPE_PERIODIQUE = "901003";

    public final static String TABLE_FIELDS = "FAPASSP.IDPASSAGE, FAPASSP.IDPLANFACTURATION, FAPASSP.IDREMARQUE, FAPASSP.LIBELLEPASSAGE, "
            + "FAPASSP.IDTYPEFACTURATION, FAPASSP.DATEPERIODE, FAPASSP.DATEFACTURATION, FAPASSP.DATECREATION, FAPASSP.DATECOMPTA, "
            + "FAPASSP.DATEECHEANCE, FAPASSP.STATUS, FAPASSP.IDJOURNAL, FAPASSP.ESTVERROUILLE, FAPASSP.PERSREF, FAPASSP.PERSIGN1, FAPASSP.PERSIGN2, FAPASSP.DATEPERIODE, FAPASSP.STATUS, FAPASSP.AUTO, FAPASSP.DELAI, FAPASSP.PROP";

    /*
     * Retourne le libellé dans la langue de l'utilisateur pour un idPassage et un BSesssion
     */
    public static String getLibellePassage(String idPassage, BSession session) throws Exception {
        if (!JadeStringUtil.isBlank(idPassage)) {
            FAPassage passage = new FAPassage();
            passage.setSession(session);
            passage.setIdPassage(idPassage);
            try {
                passage.retrieve();
                return passage.getLibelle();
            } catch (Exception e) {
                return "Error libelle";
            }
        } else {
            return "";
        }
    }

    private String dateComptabilisation = new String();
    private String dateCreation = new String();
    private String dateEcheance = new String();
    private String dateFacturation = new String();
    private String datePeriode = new String();
    private String delaiJour = "";
    private Boolean estVerrouille = new Boolean(false);
    private Boolean forceDelete = new Boolean(false);
    private String idDernierPassage = new String();
    private String idJournal = new String();
    private String idPassage = new String();
    private String idPlanFacturation = new String();
    private String idRemarque = new String();
    private String idTypeFacturation = new String();
    private Boolean isAuto = new Boolean(false);
    private String libelle = new String();
    private String libelleEtat = new String();
    private String moduleEnCours = new String();
    private FAModulePassage modulePassageEnCours = null;
    private String personneRef = new String();
    private String personneSign1 = new String();
    private String personneSign2 = new String();
    private String remarque = new String();

    private String status = new String();

    private String userCreateur = "";

    /**
     * Commentaire relatif au constructeur FAPassage
     */
    public FAPassage() {
        super();
    }

    /*
     * Traitement après ajout
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
        // Copie des modules du plan de facturation choisi
        FAModulePlanManager planManager = new FAModulePlanManager();
        planManager.setSession(transaction.getSession());
        planManager.setForIdPlanFacturation(getIdPlanFacturation());
        try {
            // Lecture des modules du plan de facturation
            planManager.find(transaction);
            for (int i = 0; i < planManager.size(); i++) {
                // Copie des modules du plan dans les modules du passage
                FAModulePassage module = new FAModulePassage();
                module.setSession(transaction.getSession());
                module.setIdModuleFacturation(((FAModulePlan) planManager.getEntity(i)).getIdModuleFacturation());

                module.setIdPassage(getIdPassage());
                // Si le module est de type liste mettre l'action IMPRIMER sinon
                // GENERER
                FAModuleFacturation modFac = new FAModuleFacturation();
                modFac.setSession(transaction.getSession());
                modFac.setIdModuleFacturation(module.getIdModuleFacturation());
                try {
                    modFac.retrieve(transaction);

                    // Tous les nouveaux modules sont à Vide, yc les Listes (RRI
                    // 05.2005)
                    module.setIdAction(FAModulePassage.CS_ACTION_VIDE);
                    module.setIdPlan(getIdPlanFacturation());

                    try {
                        module.add(transaction);
                    } catch (Exception e) {
                        _addError(transaction, "Erreur lors de la copie des modules. ");
                        _addError(transaction, e.getMessage());
                    }

                } catch (Exception e) {
                    _addError(transaction, "Erreur lors du test du type du module. ");
                    _addError(transaction, e.getMessage());
                }
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la lecture des modules du plan. ");
            _addError(transaction, e.getMessage());
        }
    }

    /*
     * Traitement après suppression
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // Suppression des remarques
        if ((!globaz.jade.client.util.JadeStringUtil.isBlank(idRemarque)) && (!idRemarque.equalsIgnoreCase("0"))) {
            FARemarque rema = new FARemarque();
            rema.setSession(transaction.getSession());
            rema.setIdRemarque(getIdRemarque());
            try {
                rema.retrieve(transaction);
                rema.delete(transaction);
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la suppression de la remarque: " + e.getMessage());
            }
        }
        // Suppression des modules
        FAModulePlanManager planManager = new FAModulePlanManager();
        planManager.setSession(transaction.getSession());
        planManager.setForIdPlanFacturation(getIdPlanFacturation());
        try {
            planManager.find(transaction);
            for (int i = 0; i < planManager.size(); i++) {
                FAModulePassage module = new FAModulePassage();
                module.setSession(transaction.getSession());
                module.setIdModuleFacturation(((FAModulePlan) planManager.getEntity(i)).getIdModuleFacturation());
                module.setIdPassage(getIdPassage());
                try {
                    module.retrieve(transaction);
                    if (!module.isNew()) {
                        // efface le module s'il y a une relation avec l'id du
                        // passage
                        module.delete(transaction);
                    }
                    transaction.disableSpy();
                } catch (Exception e) {
                    _addError(transaction, "Erreur lors de la suppression des modules: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la lecture des modules du plan: " + e.getMessage());
        }
        // Suppression des décomptes
        try {
            FAEnteteFactureManager mgr = new FAEnteteFactureManager();
            mgr.setSession(getSession());
            mgr.setForIdPassage(getIdPassage());
            mgr.find(transaction, 0);
            for (int i = 0; i < mgr.size(); i++) {
                FAEnteteFacture facture = (FAEnteteFacture) mgr.getEntity(i);
                if (!facture.isNew()) { // supprime les décomptes s'ils existent
                    facture.delete(transaction);
                }
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la suppression des décomptes: " + e.getMessage());
        }
    }

    /**
     * @see BEntity#_afterRetrieve(BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(remarque)) {
            // Recherche de la remarque
            setRemarque(FARemarque.getRemarque(getIdRemarque(), transaction));
        }
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        if (getStatus().equals(FAPassage.CS_ETAT_OUVERT)) {
            FAModulePassageManager moduleMana = new FAModulePassageManager();
            moduleMana.setSession(transaction.getSession());
            moduleMana.setForIdPassage(getIdPassage());
            moduleMana.orderByIdPlan();
            try {
                moduleMana.find();
                if (moduleMana.size() > 0) {
                    // Prendre le suivant si l'idPlan est à zéro
                    if (!((FAModulePassage) moduleMana.getFirstEntity()).getIdPlan().equals(getIdPlanFacturation())) {
                        // Suppression des modules
                        FAModulePlanManager planManager = new FAModulePlanManager();
                        planManager.setSession(transaction.getSession());
                        planManager
                                .setForIdPlanFacturation(((FAModulePassage) moduleMana.getFirstEntity()).getIdPlan());

                        try {
                            planManager.find(transaction);
                            for (int i = 0; i < planManager.size(); i++) {
                                FAModulePassage moduleDel = new FAModulePassage();
                                moduleDel.setSession(transaction.getSession());
                                moduleDel.setIdModuleFacturation(((FAModulePlan) planManager.getEntity(i))
                                        .getIdModuleFacturation());
                                moduleDel.setIdPassage(getIdPassage());
                                try {
                                    moduleDel.retrieve(transaction);
                                    if (!moduleDel.isNew()) {
                                        // efface le module s'il y a une
                                        // relation avec l'id du passage
                                        moduleDel.delete(transaction);
                                    }
                                    transaction.disableSpy();
                                } catch (Exception e) {
                                    _addError(transaction,
                                            "Erreur lors de la suppression des modules: " + e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            _addError(transaction, "Erreur lors de la lecture des modules du plan: " + e.getMessage());
                        }

                        try {
                            FAModulePlanManager planManagerNew = new FAModulePlanManager();
                            planManagerNew.setSession(transaction.getSession());
                            planManagerNew.setForIdPlanFacturation(getIdPlanFacturation());
                            // Lecture des modules du plan de facturation
                            planManagerNew.find(transaction);
                            for (int i = 0; i < planManagerNew.size(); i++) {
                                // Copie des modules du plan dans les modules du
                                // passage
                                FAModulePassage moduleNew = new FAModulePassage();
                                moduleNew.setSession(transaction.getSession());
                                moduleNew.setIdModuleFacturation(((FAModulePlan) planManagerNew.getEntity(i))
                                        .getIdModuleFacturation());

                                moduleNew.setIdPassage(getIdPassage());

                                try {

                                    // Tous les nouveaux modules sont à Vide, yc
                                    // les Listes (RRI 05.2005)
                                    moduleNew.setIdAction(FAModulePassage.CS_ACTION_VIDE);
                                    moduleNew.setIdPlan(getIdPlanFacturation());

                                    try {
                                        moduleNew.add(transaction);
                                    } catch (Exception e) {
                                        _addError(transaction, "Erreur lors de la copie des modules. ");
                                        _addError(transaction, e.getMessage());
                                    }

                                } catch (Exception e) {
                                    _addError(transaction, "Erreur lors du test du type du module. ");
                                    _addError(transaction, e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            _addError(transaction, "Erreur lors de la lecture des modules du plan. ");
                            _addError(transaction, e.getMessage());
                        }

                    }
                }
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la lecture des modules du plan: " + e.getMessage());
            }
        }
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdPassage(this._incCounter(transaction, idPassage));
        // setIdPassage(_incCounter(transaction, "0"));
        // Etat par défaut = Ouvert
        if (JadeStringUtil.isIntegerEmpty(getStatus())) {
            setStatus(FAPassage.CS_ETAT_OUVERT);
        }

        if (getSession() != null) {
            setUserCreateur(getSession().getUserId());
        }

        // Mise à jour de la remarque
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getRemarque())) {
            FARemarque rem = new FARemarque();
            rem.setSession(getSession());
            rem.setTexte(getRemarque());
            try {
                rem.add(transaction);
                setIdRemarque(rem.getIdRemarque());
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la mise à jour de la remarque. ");
            }
        }
        try {
            long lDatePassage = Long.parseLong(JACalendar.format(getDateFacturation(), JACalendar.FORMAT_YYYYMMDD));
            long lAjourdhui = Long.parseLong(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD));
            JADate today = JACalendar.today();
            int todayJour = today.getDay();
            JADate lastDayInPreMonth = new JACalendarGregorian().addDays(today, -todayJour);
            long lMoisAvant = Long.parseLong(JACalendar.format(lastDayInPreMonth, JACalendar.FORMAT_YYYYMMDD));
            if ((lDatePassage < lAjourdhui) && (lMoisAvant != lDatePassage)) {
                _addError(transaction, "La date de facturation ne doit pas être inférieure à la date du jour.");
            }
        } catch (Exception e) {
            _addError(transaction, "La date de facturation est invalide.");
        }
    }

    /*
     * Si la suppression n'est pas forcée, suppression seulement si le passage n'est pas comptabilisé et qu'il n'existe
     * pas d'entêtes de facture
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        if (!getForceDelete().booleanValue() && getStatus().equalsIgnoreCase(FAPassage.CS_ETAT_COMPTABILISE)) {
            _addError(transaction, "Le passage est à l'état comptabilisé, suppression impossible. ");
        } else {
            FAEnteteFactureManager entManager = new FAEnteteFactureManager();
            entManager.setISession(getSession());
            entManager.setForIdPassage(getIdPassage());
            entManager.find(transaction);
            if (entManager.getSize() > 0) {
                if (!getForceDelete().booleanValue()) {
                    _addError(transaction, "Le passage contient des entêtes de facture, suppression impossible.");
                } else {
                    for (int i = 0; i < entManager.size(); i++) {
                        FAEnteteFacture myEntete = (FAEnteteFacture) entManager.getEntity(i);
                        myEntete.setForceDelete(new Boolean("true"));
                        myEntete.delete(transaction);
                    }
                }
            }
        }
    }

    /*
     * Traitement avant mise à jour
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // Mise à jour de la remarque
        FARemarque rem = new FARemarque();

        rem.setSession(getSession());
        rem.setTexte(getRemarque());
        try {
            if ((!globaz.jade.client.util.JadeStringUtil.isBlank(getIdRemarque()) && (!getIdRemarque()
                    .equalsIgnoreCase("0")))) {
                rem.setIdRemarque(getIdRemarque());
                transaction.disableSpy();
                rem.update(transaction);
                transaction.enableSpy();
            } else if (!globaz.jade.client.util.JadeStringUtil.isBlank(getRemarque())) {
                rem.add(transaction);
                setIdRemarque(rem.getIdRemarque());
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la mise à jour de la remarque. ");
        }
        try {
            if (!JadeStringUtil.isBlankOrZero(getIdJournal())) {
                CAJournalManager journalMana = new CAJournalManager();
                journalMana.setSession(getSession());
                journalMana.setForIdJournal(getIdJournal());
                journalMana.find();
                if (journalMana.size() == 0) {
                    setIdJournal("0");
                }
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors de la mise à jour du numéro de journal comptable. ");
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAPASSP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPassage = statement.dbReadNumeric("IDPASSAGE");
        idPlanFacturation = statement.dbReadNumeric("IDPLANFACTURATION");
        idRemarque = statement.dbReadNumeric("IDREMARQUE");
        libelle = statement.dbReadString("LIBELLEPASSAGE");
        idTypeFacturation = statement.dbReadNumeric("IDTYPEFACTURATION");
        datePeriode = statement.dbReadDateAMJ("DATEPERIODE", JACalendar.FORMAT_MMsYYYY);
        dateFacturation = statement.dbReadDateAMJ("DATEFACTURATION");
        dateCreation = statement.dbReadDateAMJ("DATECREATION");
        dateComptabilisation = statement.dbReadDateAMJ("DATECOMPTA");
        dateEcheance = statement.dbReadDateAMJ("DATEECHEANCE");
        status = statement.dbReadNumeric("STATUS");
        idJournal = statement.dbReadNumeric("IDJOURNAL");
        estVerrouille = statement.dbReadBoolean("ESTVERROUILLE");
        libelleEtat = statement.dbReadString("LIBELLEETAT");
        remarque = FARemarque.getRemarque(statement);
        personneRef = statement.dbReadString("PERSREF");
        personneSign1 = statement.dbReadString("PERSIGN1");
        personneSign2 = statement.dbReadString("PERSIGN2");
        isAuto = statement.dbReadBoolean("AUTO");
        delaiJour = statement.dbReadNumeric("DELAI");
        userCreateur = statement.dbReadString("PROP");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        /*
         * Contrôle de la date de facturation
         */
        // La date de facturation est obligatoire
        if (JAUtil.isDateEmpty(getDateFacturation())) {
            _addError(statement.getTransaction(), "La date de facturation doit être renseignée.");
        }
        // La date de facturation doit être valable
        _checkDate(statement.getTransaction(), getDateFacturation(), "La date de facturation est invalide.");
        /*
         * Contrôle de la période de facturation
         */
        // La période de facturation est obligatoire pour les facturations
        // périodique
        if ((getIdTypeFacturation().equalsIgnoreCase(FAPassage.CS_TYPE_PERIODIQUE))) {
            if (JAUtil.isDateEmpty(getDatePeriode())) {
                _addError(statement.getTransaction(), "La période de facturation doit être renseignée.");
            }
            _checkDate(statement.getTransaction(), getDatePeriode(), "La période de facturation est invalide.");
        } else {
            // Pour les autres types, la période de facturation ne doit pas être
            // renseignée
            if (!JadeStringUtil.isBlank(getDatePeriode())) {
                _addError(statement.getTransaction(), "La période de facturation ne doit pas être renseignée.");
            }
        }
        // Contrôle que le type de facturation correspond bien au plan.
        if (!JadeStringUtil.isBlankOrZero(getIdPlanFacturation())) {
            FAPlanFacturation plan = new FAPlanFacturation();
            plan.setSession(getSession());
            plan.setIdPlanFacturation(getIdPlanFacturation());
            plan.retrieve();
            if (!plan.getIdTypeFacturation().equals(getIdTypeFacturation())) {
                _addError(statement.getTransaction(), "Le type de facturation n'est pas renseigné correctement");
            }
        }
        /*
         * Contrôle du plan de facturation
         */
        // Le plan de facturation est obligatoire
        if (JadeStringUtil.isBlankOrZero(getIdPlanFacturation())) {
            _addError(statement.getTransaction(), "Le plan de facturation doit être renseigné.");
        }

        if (isAuto.booleanValue() && JadeStringUtil.isBlank(delaiJour)) {
            _addError(statement.getTransaction(), getSession().getLabel("DELAI_JOUR_NON_RENSEIGNE"));
        }

        if (!isAuto.booleanValue() && !JadeStringUtil.isBlankOrZero(delaiJour)) {
            _addError(statement.getTransaction(), getSession().getLabel("DELAI_JOUR_SANS_MODE_AUTO"));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDPASSAGE", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement
                .writeField("IDPASSAGE", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField("IDPLANFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanFacturation(), "idPlanFacturation"));
        statement.writeField("IDREMARQUE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement
                .writeField("LIBELLEPASSAGE", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("IDTYPEFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeFacturation(), "idTypeFacturation"));
        statement.writeField("DATEPERIODE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDatePeriode(), "datePeriode"));
        statement.writeField("DATEFACTURATION",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFacturation(), "dateFacturation"));
        statement.writeField("DATECREATION",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField("DATECOMPTA",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateComptabilisation(), "dateComptabilisation"));
        statement.writeField("DATEECHEANCE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField("STATUS", this._dbWriteNumeric(statement.getTransaction(), getStatus(), "status"));
        statement
                .writeField("IDJOURNAL", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("ESTVERROUILLE", this._dbWriteBoolean(statement.getTransaction(), isEstVerrouille(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estVerrouille"));
        statement.writeField("PERSREF",
                this._dbWriteString(statement.getTransaction(), getPersonneRef(), "personneRef"));
        statement.writeField("PERSIGN1",
                this._dbWriteString(statement.getTransaction(), getPersonneSign1(), "personneSign1"));
        statement.writeField("PERSIGN2",
                this._dbWriteString(statement.getTransaction(), getPersonneSign2(), "personneSign2"));
        statement.writeField("AUTO", this._dbWriteBoolean(statement.getTransaction(), getIsAuto(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isAuto"));
        statement.writeField("DELAI", this._dbWriteNumeric(statement.getTransaction(), getDelaiJour(), "delaiJour"));
        statement
                .writeField("PROP", this._dbWriteString(statement.getTransaction(), getUserCreateur(), "userCreateur"));
    }

    @Override
    public String getDateComptabilisation() {
        return dateComptabilisation;
    }

    @Override
    public String getDateCreation() {
        return dateCreation;
    }

    @Override
    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getDateFacturation() {
        return dateFacturation;
    }

    public JADate getDateFacturationAsJADate() throws Exception {
        return new JADate(getDateFacturation());
    }

    @Override
    public String getDatePeriode() {
        return datePeriode;
    }

    public String getDelaiJour() {
        return delaiJour;
    }

    /**
     * Returns the forceDelete.
     * 
     * @return Boolean
     */
    public Boolean getForceDelete() {
        return forceDelete;
    }

    /**
     * @return
     */
    @Override
    public String getIdDernierPassage() {
        FAPassageManager passage = new FAPassageManager();
        passage.setSession(getSession());
        passage.setForIdTypeFacturation(FAPassage.CS_TYPE_PERIODIQUE);
        passage.orderByIdPassageDecroissant();
        try {
            passage.find();
            if (passage.size() > 0) {
                idDernierPassage = ((FAPassage) passage.getFirstEntity()).getIdPassage();
            }
        } catch (Exception e) {
            JadeLogger.error(passage, e);
        }
        return idDernierPassage;
    }

    @Override
    public String getIdJournal() {
        return idJournal;
    }

    @Override
    public String getIdPassage() {
        return idPassage;
    }

    @Override
    public String getIdPlanFacturation() {
        return idPlanFacturation;
    }

    @Override
    public String getIdRemarque() {
        return idRemarque;
    }

    @Override
    public String getIdTypeFacturation() {
        return idTypeFacturation;
    }

    /**
     * Getter
     */

    @Override
    public Boolean getIsAuto() {
        return isAuto;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.06.2003 11:32:54)
     * 
     * @return String
     */
    public String getLibelleEtat() {
        return libelleEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.06.2003 11:15:23)
     * 
     * @return String
     */
    public String getLibelleType() {
        return getSession().getCodeLibelle(idTypeFacturation);
    }

    /**
     * @return
     */
    public String getModuleEnCours() {
        return moduleEnCours;
    }

    public FAModulePassage getModulePassageEnCours() {
        return modulePassageEnCours;
    }

    public String getPersonneRef() {
        return personneRef;
    }

    public String getPersonneSign1() {
        return personneSign1;
    }

    public String getPersonneSign2() {
        return personneSign2;
    }

    @Override
    public String getRemarque() {
        return remarque;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public String getUserCreateur() {
        return userCreateur;
    }

    public Boolean isEstVerrouille() {
        return estVerrouille;
    }

    @Override
    public void setDateComptabilisation(String newDateComptabilisation) {
        dateComptabilisation = newDateComptabilisation;
    }

    @Override
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    @Override
    public void setDateEcheance(String newDateEcheance) {
        dateEcheance = newDateEcheance;
    }

    @Override
    public void setDateFacturation(String newDateFacturation) {
        dateFacturation = newDateFacturation;
    }

    @Override
    public void setDatePeriode(String newDatePeriode) {
        datePeriode = newDatePeriode;
    }

    public void setDelaiJour(String newDelaiJour) {
        delaiJour = newDelaiJour;
    }

    @Override
    public void setEstVerrouille(Boolean newEstVerrouille) {
        estVerrouille = newEstVerrouille;
    }

    /**
     * Sets the forceDelete.
     * 
     * @param forceDelete
     *            The forceDelete to set
     */
    public void setForceDelete(Boolean forceDelete) {
        this.forceDelete = forceDelete;
    }

    @Override
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    @Override
    public void setIdPassage(String newIdPassage) {
        idPassage = newIdPassage;
    }

    @Override
    public void setIdPlanFacturation(String newIdPlanFacturation) {
        idPlanFacturation = newIdPlanFacturation;
    }

    @Override
    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    @Override
    public void setIdTypeFacturation(String newIdTypeFacturation) {
        idTypeFacturation = newIdTypeFacturation;
    }

    /**
     * Setter
     */

    @Override
    public void setIsAuto(Boolean newIsAuto) {
        isAuto = newIsAuto;
    }

    @Override
    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    /**
     * @param string
     */
    public void setModuleEnCours(String string) {
        moduleEnCours = string;
    }

    public void setModulePassageEnCours(FAModulePassage modulePassageEnCours) {
        this.modulePassageEnCours = modulePassageEnCours;
    }

    public void setPersonneRef(String personneRef) {
        this.personneRef = personneRef;
    }

    public void setPersonneSign1(String personneSign1) {
        this.personneSign1 = personneSign1;
    }

    public void setPersonneSign2(String personneSign2) {
        this.personneSign2 = personneSign2;
    }

    @Override
    public void setRemarque(String newRemarque) {
        remarque = newRemarque;
    }

    @Override
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    public void setUserCreateur(String newUserCreateur) {
        userCreateur = newUserCreateur;
    }

}
