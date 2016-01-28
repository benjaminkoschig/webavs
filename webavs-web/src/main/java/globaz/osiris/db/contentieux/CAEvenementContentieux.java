package globaz.osiris.db.contentieux;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIEvenementContentieux;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.api.APIRemarque;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationContentieux;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.utils.CAGestionRemarque;
import globaz.osiris.db.utils.CAPosteJournalisation;
import globaz.osiris.db.utils.IntRemarque;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (13.12.2001 15:49:24)
 * 
 * @author: Administrator
 */
public class CAEvenementContentieux extends BEntity implements Serializable, IntRemarque, APIEvenementContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static int AK_IDOPERATION = 2;

    public final static int AK_IDSECPARAM = 1;
    public final static String DOMCONTENTIEUX = "221001";
    public final static String MOTANULATION = "222004";
    public final static String MOTCREATION = "222001";
    public final static String MOTEDITION = "222003";
    public final static String MOTMUTATION = "222002";
    // définition des constantes
    public static final String TABLE_CAEVCTP = "CAEVCTP";

    private String dateDeclenchement = new String();
    private String dateExecution = new String();
    private Boolean estDeclenche = new Boolean(false);
    private Boolean estExtourne = new Boolean(false);
    private Boolean estIgnoree = new Boolean(false);
    private Boolean estModifie = new Boolean(false);
    private String idAdresse = new String();
    private String idEvenementContentieux = new String();
    private String idOperation = new String();
    private String idParametreEtape = new String();
    private String idPosteJournalisation = new String();
    private String idRemarque = new String();
    private String idSection = new String();
    private String idTiers = new String();
    private String idTiersOfficePoursuites = new String();
    private CAJournal journal = null;
    private String modifie = new String();
    private String montant = new String();
    private String motif = new String();
    private String motifJournalisation = new String();
    private boolean nouvelleTransaction = false;
    private CAParametreEtape parametreEtape = null;
    private globaz.osiris.db.utils.CARemarque remarque = null;
    private boolean saveRemarque = false;
    private CASection section = null;
    private String taxes = new String();

    private String texteRemarque = new String();

    /**
     * Commentaire relatif au constructeur CAEvenementContentieux
     */
    public CAEvenementContentieux() {
        super();
    }

    /**
     * Effectue des traitements après une lecture dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Contrôler qu'il y est une remarque
        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            setTexteRemarque(getRemarque().getTexte());
        }
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // S'il existe un motif de journalisation
        if (!JadeStringUtil.isBlank(getMotifJournalisation())) {
            // Ajoute un élément contentieux lors d'une modification de
            // l'évènement contentieux
            globaz.osiris.db.utils.CAElementJournalisation elementJournalisation = new globaz.osiris.db.utils.CAElementJournalisation();
            elementJournalisation.setIdPosteJournalisation(getIdPosteJournalisation());
            elementJournalisation.setDate(globaz.globall.util.JACalendar.today().toString());
            elementJournalisation.setHeure(new globaz.globall.util.JATime(globaz.globall.util.JACalendar.now())
                    .toString());
            elementJournalisation.setIdDomaineJournalisation(CAEvenementContentieux.DOMCONTENTIEUX);
            elementJournalisation.setIdMotifJournalisation(getMotifJournalisation());
            elementJournalisation.setTexte(getMotif());
            elementJournalisation.setUser(getSession().getUserName());
            elementJournalisation.add(transaction);
            setMotif("");
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Contrôler si modifié
        if (modifie.equalsIgnoreCase("Oui")) {
            setEstModifie(Boolean.TRUE);
        }

        // incrémente le prochain numéro
        setIdEvenementContentieux(this._incCounter(transaction, idEvenementContentieux));

        // Ajoute un poste journalisation lors d'un ajout d'un événement
        // contentieux
        globaz.osiris.db.utils.CAPosteJournalisation posteJournalisation = new globaz.osiris.db.utils.CAPosteJournalisation();
        posteJournalisation.setReference(_getTableName());
        posteJournalisation.add(transaction);

        // Ajoute un élément contentieux
        globaz.osiris.db.utils.CAElementJournalisation elementJournalisation = new globaz.osiris.db.utils.CAElementJournalisation();
        elementJournalisation.setIdPosteJournalisation(posteJournalisation.getIdPosteJournalisation());
        elementJournalisation.setDate(globaz.globall.util.JACalendar.today().toString());
        elementJournalisation.setHeure(new globaz.globall.util.JATime(globaz.globall.util.JACalendar.now()).toString());
        elementJournalisation.setIdDomaineJournalisation(CAEvenementContentieux.DOMCONTENTIEUX);
        elementJournalisation.setIdMotifJournalisation(getMotifJournalisation());
        elementJournalisation.setTexte(getMotif());
        elementJournalisation.setUser(getSession().getUserName());
        elementJournalisation.add(transaction);
        setMotif("");

        executionManuelle(transaction);

        // Création de l'enregistrement remarque si il y a une remarque
        if (!JadeStringUtil.isBlank(getTexteRemarque())) {
            globaz.osiris.db.utils.CARemarque remarqueAjout = new globaz.osiris.db.utils.CARemarque();
            remarqueAjout.setTexte(getTexteRemarque());
            remarqueAjout.add(transaction);
            setIdRemarque(remarqueAjout.getIdRemarque());
        }

        // Met à jour l'id poste journalisation dans Evenement contentieux
        setIdPosteJournalisation(posteJournalisation.getIdPosteJournalisation());
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Suppression impossible si déclenché
        if (getEstDeclenche().booleanValue() || !JadeStringUtil.isIntegerEmpty(getIdOperation())) {
            _addError(transaction, getSession().getLabel("7220"));
        } else {
            // Chargement de CAPosteJournalisation
            CAPosteJournalisation postJour = new CAPosteJournalisation();
            postJour.setIdPosteJournalisation(getIdPosteJournalisation());
            postJour.retrieve(transaction);
            if (!postJour.isNew()) {
                // Effacement
                postJour.delete(transaction);
                if (postJour.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7221"));
                }
            } else
            // Tester le "-1" à cause des cas de reprise
            if (!getIdPosteJournalisation().equals("-1")) {
                _addError(transaction, getSession().getLabel("7222"));
            }
        }
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        executionManuelle(transaction);

        // Mise à jour de la remarque
        _saveRemarque(transaction);

        // Si la remarque a été effacée
        if (JadeStringUtil.isBlank(getTexteRemarque())) {
            setIdRemarque("0");
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAEVCTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        dateDeclenchement = statement.dbReadDateAMJ("DATEDECLENCHEMENT");
        dateExecution = statement.dbReadDateAMJ("DATEEXECUTION");
        estDeclenche = statement.dbReadBoolean("ESTDECLENCHE");
        estExtourne = statement.dbReadBoolean("ESTEXTOURNE");
        estModifie = statement.dbReadBoolean("ESTMODIFIE");
        estIgnoree = statement.dbReadBoolean("ESTIGNOREE");
        idAdresse = statement.dbReadNumeric("IDADRESSE");
        idEvenementContentieux = statement.dbReadNumeric("IDEVECON");
        idParametreEtape = statement.dbReadNumeric("IDPARAMETREETAPE");
        idPosteJournalisation = statement.dbReadNumeric("IDPOSJOU");
        idRemarque = statement.dbReadNumeric("IDREMARQUE");
        idSection = statement.dbReadNumeric("IDSECTION");
        montant = statement.dbReadNumeric("MONTANT", 2);
        taxes = statement.dbReadNumeric("TAXES", 2);
        idTiers = statement.dbReadNumeric("IDTIERS");
        idTiersOfficePoursuites = statement.dbReadNumeric("IDTIEOFFPOU");
        idOperation = statement.dbReadNumeric("IDOPERATION");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2002 16:32:32)
     */
    private void _saveRemarque(BTransaction transaction) {

        try {

            if (saveRemarque) {

                CAGestionRemarque gestionRemarque = new CAGestionRemarque(this);
                gestionRemarque.add(transaction);
            }
            saveRemarque = false;
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdPosteJournalisation(), getSession().getLabel("7223"));
        _propertyMandatory(statement.getTransaction(), getIdEvenementContentieux(), getSession().getLabel("7224"));
        _propertyMandatory(statement.getTransaction(), getIdSection(), getSession().getLabel("7225"));
        _propertyMandatory(statement.getTransaction(), getIdParametreEtape(), getSession().getLabel("7226"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2002 10:33:12)
     * 
     * @param alternateKey
     *            int
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {

        // Clé alternée numéro 1 : idSection et idParametreEtape
        switch (alternateKey) {
            case AK_IDSECPARAM:
                statement.writeKey("IDSECTION", this._dbWriteNumeric(statement.getTransaction(), getIdSection(), ""));
                statement.writeKey("IDPARAMETREETAPE",
                        this._dbWriteNumeric(statement.getTransaction(), getIdParametreEtape(), ""));
                break;
            case AK_IDOPERATION:
                statement.writeKey("IDOPERATION",
                        this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDEVECON",
                this._dbWriteNumeric(statement.getTransaction(), getIdEvenementContentieux(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("DATEDECLENCHEMENT",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDeclenchement(), "dateDeclenchement"));
        statement.writeField("DATEEXECUTION",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateExecution(), "dateExecution"));
        statement.writeField("ESTDECLENCHE", this._dbWriteBoolean(statement.getTransaction(), getEstDeclenche(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estDeclenche"));
        statement.writeField("ESTEXTOURNE", this._dbWriteBoolean(statement.getTransaction(), getEstExtourne(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estExtourne"));
        statement.writeField("ESTMODIFIE", this._dbWriteBoolean(statement.getTransaction(), getEstModifie(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estModifie"));
        statement.writeField("ESTIGNOREE", this._dbWriteBoolean(statement.getTransaction(), getEstIgnoree(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estIgnoree"));
        statement
                .writeField("IDADRESSE", this._dbWriteNumeric(statement.getTransaction(), getIdAdresse(), "idAdresse"));
        statement.writeField("IDEVECON",
                this._dbWriteNumeric(statement.getTransaction(), getIdEvenementContentieux(), "idEveCon"));
        statement.writeField("IDPARAMETREETAPE",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreEtape(), "idParametreEtape"));
        statement.writeField("IDPOSJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournalisation(), "idPosJou"));
        statement.writeField("IDREMARQUE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement
                .writeField("IDSECTION", this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField("MONTANT", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("TAXES", this._dbWriteNumeric(statement.getTransaction(), getTaxes(), "taxes"));
        statement.writeField("IDTIERS", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("IDTIEOFFPOU", this._dbWriteNumeric(statement.getTransaction(),
                getIdTiersOfficePoursuites(), "idTiersOfficePousrsuites"));
        statement.writeField("IDOPERATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), "idOperation"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 16:35:12)
     */
    @Override
    public void annulerDeclenchement() {

        // Si l'événement a été déclenché, on annule
        if (getEstDeclenche().booleanValue()) {
            setDateExecution("");
            setEstDeclenche(new Boolean(false));
            setMontant("");
            setTaxes("");
            setIdAdresse("");
            setIdTiers("");
            setIdTiersOfficePoursuites("");
            setIdOperation("");
            // Journaliser l'annulation
            setMotifJournalisation(CAEvenementContentieux.MOTANULATION);
            // setMotif("");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.09.2002 08:46:09)
     */
    @Override
    public void annulerEtapeContentieux(BITransaction transaction) {
        // Annulation d'une étape de contentieux
        try {
            if (transaction == null) {
                transaction = new BTransaction(getSession());
            }
            BTransaction bTransaction = (BTransaction) transaction;
            try {
                if (!transaction.isOpened()) {
                    transaction.openTransaction();
                    nouvelleTransaction = true;
                }

                // Chargement de l'opération
                CAOperationContentieux oper = new CAOperationContentieux();
                oper.setISession(transaction.getISession());
                oper.setIdSection(getIdSection());
                oper.setIdOperation(getIdOperation());
                oper.retrieve(transaction);

                if (!oper.isNew() && !oper.hasErrors()) {

                    // désactiver opération
                    oper.setMotif(getMotif());
                    oper.desactiver((BTransaction) transaction);
                    if (!oper.isNew() && !oper.hasErrors()) {

                        // Suppression de l'opération contentieux
                        oper.setOperSaveEvenContentieux(true);
                        oper.delete(transaction);
                        if (!oper.hasErrors()) {
                            if (nouvelleTransaction) {

                                // Validation de la mise à jour avec un commit
                                transaction.commit();
                            }
                            // else : l'opération annulerEtapeContentieux s'est
                            // déroulé avec succès
                            // } else {
                            //
                            // // //Erreur lors de la mise à jour de l'événement
                            // contentieux
                            // // _addError(bTransaction,
                            // getSession().getLabel("7227"));
                            // // if (nouvelleTransaction)
                            // // transaction.rollback();
                            // }
                        } else {
                            // Erreur lors de la suppression de l'opération
                            _addError(bTransaction, getSession().getLabel("5028"));
                            if (nouvelleTransaction) {
                                transaction.rollback();
                            }
                        }
                    } else {
                        // Erreur lors de la désactivition de l'opération
                        _addError(bTransaction, getSession().getLabel("7228"));
                        if (nouvelleTransaction) {
                            transaction.rollback();
                        }
                    }

                } else {
                    // Erreur chargement de l'opération
                    _addError(bTransaction, getSession().getLabel("7229"));
                    if (nouvelleTransaction) {
                        transaction.rollback();
                    }
                }
            } catch (Exception e) {
                // Rollback
                if (nouvelleTransaction) {
                    transaction.rollback();
                }
            } finally {
                if (nouvelleTransaction) {
                    transaction.closeTransaction();
                }
            }

        } catch (Exception e) {
        }

    }

    /**
     * Si une date d'exécution est définie et que idOperation vaut zéro récupère un journal journalier (ou le crée si
     * nécessaire), crée l'opération, mets à jour l'id de l'opération de l'évenement contentieux, mets à jour l'id de la
     * dernière étape de la section.
     * 
     * @param transaction
     * @throws Exception
     */
    private void executionManuelle(globaz.globall.db.BTransaction transaction) throws Exception {
        if (!JAUtil.isDateEmpty(getDateExecution())
                && (getIdOperation().equals("0") || JadeStringUtil.isBlank(getIdOperation()))) {
            setEstDeclenche(new Boolean(true));

            // récupération/création du journal
            journal = CAJournal.fetchJournalJournalier(getSession(), transaction);

            // création de l'opération
            CAOperationContentieux operation = new CAOperationContentieux();
            operation.setSession(getSession());
            operation.setEvenementContentieux(this);
            operation.setIdJournal(journal.getIdJournal());
            operation.add();

            operation.activer(transaction);
            operation.update(transaction);

            operation.retrieve();

            // mise à jour de l'id de l'opération du contentieux
            setIdOperation(operation.getIdOperation());

            // mise à jour de l'id de la dernière étape
            CASection section = new CASection();
            section.setSession(getSession());
            section.setIdSection(getIdSection());
            section.retrieve();
            section.setIdLastEtapeCtx(getIdEvenementContentieux());
            section.update();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 16:28:02)
     * 
     * @return String
     */
    @Override
    public String getAdresseComplete() throws Exception {

        // Initialiser
        String s = "";

        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdAdresse()) && !JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                IntAdresseCourrier adr = (IntAdresseCourrier) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntAdresseCourrier.class);
                adr.retrieve(getIdAdresse());
                IntTiers tie = (IntTiers) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntTiers.class);
                tie.retrieve(getIdTiers());
                globaz.osiris.formatter.CAAdresseCourrierFormatter fmt = new globaz.osiris.formatter.CAAdresseCourrierFormatter(
                        tie, adr);
                String[] sAdr = fmt.getFullAdresse();
                for (int i = 0; i < sAdr.length; i++) {
                    if (sAdr[i] != null) {
                        s = s + sAdr[i] + "\n";
                    }
                }
            }
        } catch (Exception e) {
        }

        // Fournir l'adresse
        return s;
    }

    /**
     * Getter
     */
    @Override
    public String getDateDeclenchement() {
        return dateDeclenchement;
    }

    @Override
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:15:50)
     * 
     * @return Boolean
     */
    @Override
    public Boolean getEstDeclenche() {
        return estDeclenche;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:17:32)
     * 
     * @return Boolean
     */
    @Override
    public Boolean getEstExtourne() {
        return estExtourne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:18:41)
     * 
     * @return Boolean
     */
    @Override
    public Boolean getEstIgnoree() {
        return estIgnoree;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:18:03)
     * 
     * @return Boolean
     */
    @Override
    public Boolean getEstModifie() {
        return estModifie;
    }

    @Override
    public String getIdAdresse() {
        return idAdresse;
    }

    @Override
    public String getIdEvenementContentieux() {
        return idEvenementContentieux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 15:11:18)
     * 
     * @return String
     */
    @Override
    public String getIdOperation() {
        return idOperation;
    }

    @Override
    public String getIdParametreEtape() {
        return idParametreEtape;
    }

    @Override
    public String getIdPosteJournalisation() {
        return idPosteJournalisation;
    }

    @Override
    public String getIdRemarque() {
        return idRemarque;
    }

    @Override
    public String getIdSection() {
        return idSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 16:36:34)
     * 
     * @return String
     */
    @Override
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 16:37:26)
     * 
     * @return String
     */
    @Override
    public String getIdTiersOfficePoursuites() {
        return idTiersOfficePoursuites;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 17:07:32)
     * 
     * @return String
     */
    @Override
    public String getModifie() {
        return modifie;
    }

    @Override
    public String getMontant() {
        return montant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.07.2002 13:03:13)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    @Override
    public FWCurrency getMontantToCurrency() {
        return new FWCurrency(getMontant());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.06.2002 13:06:49)
     * 
     * @return String
     */
    @Override
    public String getMotif() {
        return motif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 16:41:02)
     * 
     * @return String
     */
    @Override
    public String getMotifJournalisation() {

        // Motif par défaut si vide
        if (JadeStringUtil.isBlank(motifJournalisation)) {
            if (isNew()) {
                return CAEvenementContentieux.MOTCREATION;
            } else {
                return CAEvenementContentieux.MOTMUTATION;
                // Sinon, motif fourni
            }
        } else {
            return motifJournalisation;
        }
    }

    public CAOperationContentieux getOperationContentieux(BSession session) {
        // Chargement de l'opération

        try {
            CAOperationContentieux oper = new CAOperationContentieux();
            oper.setISession(session);
            oper.setIdSection(getIdSection());
            oper.setIdOperation(getIdOperation());
            oper.retrieve();

            if ((oper == null) || oper.isNew()) {
                return null;
            } else {
                return oper;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 15:19:07)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     */
    @Override
    public APIParametreEtape getParametreEtape() {
        // Instancier si null
        if (parametreEtape == null) {
            parametreEtape = new CAParametreEtape();
            parametreEtape.setSession(getSession());
            parametreEtape.setIdParametreEtape(getIdParametreEtape());
            try {
                parametreEtape.retrieve();
                if (parametreEtape.isNew() || parametreEtape.hasErrors()) {
                    _addError(null, getSession().getLabel("7230"));
                    parametreEtape = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                parametreEtape = null;
            }
        }
        return parametreEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.06.2002 09:58:17)
     * 
     * @return globaz.osiris.db.utils.CARemarque
     */
    @Override
    public APIRemarque getRemarque() {
        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            return null;
        }

        // Si log pas déjà chargé
        if (remarque == null) {
            // Instancier un nouveau LOG
            remarque = new globaz.osiris.db.utils.CARemarque();
            remarque.setSession(getSession());

            // Récupérer le log en question
            remarque.setIdRemarque(getIdRemarque());
            try {
                remarque.retrieve();
                if (remarque.isNew() || remarque.hasErrors()) {
                    remarque = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                remarque = null;
            }
        }
        return remarque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.07.2002 16:17:37)
     * 
     * @return globaz.osiris.db.comptes.CASection
     */
    @Override
    public APISection getSection() {
        if ((section == null) && !JadeStringUtil.isIntegerEmpty(getIdSection())) {
            section = new CASection();
            section.setISession(getSession());
            section.setIdSection(getIdSection());
            try {
                section.retrieve();
                if (section.isNew()) {
                    section = null;
                }
            } catch (Exception e) {
                section = null;
            }

            if (section == null) {
                _addError(null, getSession().getLabel("7231"));
            }
        }

        return section;
    }

    @Override
    public String getTaxes() {
        return taxes;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.07.2002 13:03:43)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    @Override
    public FWCurrency getTaxesToCurrency() {
        return new FWCurrency(getTaxes());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 08:16:19)
     * 
     * @return String
     */
    @Override
    public String getTexteRemarque() {
        return texteRemarque;
    }

    /**
     * Setter
     */
    @Override
    public void setDateDeclenchement(String newDateDeclenchement) {
        dateDeclenchement = newDateDeclenchement;
    }

    @Override
    public void setDateExecution(String newDateExecution) {
        dateExecution = newDateExecution;
        setEstDeclenche(new Boolean(!JAUtil.isDateEmpty(newDateExecution)));

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:15:50)
     * 
     * @param newEstDeclenche
     *            Boolean
     */
    @Override
    public void setEstDeclenche(Boolean newEstDeclenche) {
        estDeclenche = newEstDeclenche;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:17:32)
     * 
     * @param newEstExtourne
     *            Boolean
     */
    @Override
    public void setEstExtourne(Boolean newEstExtourne) {
        estExtourne = newEstExtourne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:18:41)
     * 
     * @param newEstIgnoree
     *            Boolean
     */
    @Override
    public void setEstIgnoree(Boolean newEstIgnoree) {
        estIgnoree = newEstIgnoree;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 07:18:03)
     * 
     * @param newEstModifie
     *            Boolean
     */
    @Override
    public void setEstModifie(Boolean newEstModifie) {
        estModifie = newEstModifie;
    }

    @Override
    public void setIdAdresse(String newIdAdresse) {
        idAdresse = newIdAdresse;
    }

    @Override
    public void setIdEvenementContentieux(String newIdEvenementContentieux) {
        idEvenementContentieux = newIdEvenementContentieux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 15:11:18)
     * 
     * @param newIdOperation
     *            String
     */
    @Override
    public void setIdOperation(String newIdOperation) {
        idOperation = newIdOperation;
    }

    @Override
    public void setIdParametreEtape(String newIdParametreEtape) {
        idParametreEtape = newIdParametreEtape;
    }

    @Override
    public void setIdPosteJournalisation(String newIdPosteJournalisation) {
        idPosteJournalisation = newIdPosteJournalisation;
    }

    @Override
    public void setIdRemarque(String newIdRemarque) {
        remarque = null;
        idRemarque = newIdRemarque;
    }

    @Override
    public void setIdSection(String newIdSection) {
        idSection = newIdSection;
        section = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 16:36:34)
     * 
     * @param newIdTiers
     *            String
     */
    @Override
    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 16:37:26)
     * 
     * @param newIdTiersOfficePoursuites
     *            String
     */
    @Override
    public void setIdTiersOfficePoursuites(String newIdTiersOfficePoursuites) {
        idTiersOfficePoursuites = newIdTiersOfficePoursuites;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 17:07:32)
     * 
     * @param newModifie
     *            String
     */
    @Override
    public void setModifie(String newModifie) {
        modifie = newModifie;
    }

    @Override
    public void setMontant(String newMontant) {
        montant = newMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.06.2002 13:06:49)
     * 
     * @param newMotif
     *            String
     */
    @Override
    public void setMotif(String newMotif) {
        motif = newMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 16:41:02)
     * 
     * @param newMotifJournalisation
     *            String
     */
    @Override
    public void setMotifJournalisation(String newMotifJournalisation) {
        motifJournalisation = newMotifJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.06.2002 09:58:17)
     * 
     * @param newRemarque
     *            globaz.osiris.db.utils.CARemarque
     */
    public void setRemarque(globaz.osiris.db.utils.CARemarque newRemarque) {
        remarque = newRemarque;
    }

    @Override
    public void setTaxes(String newTaxes) {
        taxes = newTaxes;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 08:16:19)
     * 
     * @param newTexteRemarque
     *            String
     */
    @Override
    public void setTexteRemarque(String newTexteRemarque) {
        texteRemarque = newTexteRemarque;
        saveRemarque = true;
    }
}
