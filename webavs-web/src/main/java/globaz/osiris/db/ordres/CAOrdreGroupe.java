package globaz.osiris.db.ordres;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.exception.CAOGRegroupISODepassement;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.db.ordres.format.CAProcessFormatOrdreDTA;
import globaz.osiris.db.ordres.format.CAProcessFormatOrdreLSV;
import globaz.osiris.db.ordres.format.CAProcessFormatOrdreLSVBanque;
import globaz.osiris.db.ordres.format.CAProcessFormatOrdreOPAE;
import globaz.osiris.db.ordres.format.opt.CAProcessFormatOrdreOPAELite;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import globaz.osiris.db.ordres.sepa.CAProcessFormatOrdreSEPA;
import globaz.osiris.db.ordres.sepa.SepaSendOrderProcessor;
import globaz.osiris.db.ordres.sepa.utils.CASepaCommonUtils;
import globaz.osiris.db.ordres.utils.CAOrdreGroupeFtpUtils;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.process.CAProcessAnnulerOrdre;
import globaz.osiris.process.CAProcessAttacherOrdre;
import globaz.osiris.process.CAProcessOrdre;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.process.journal.CAProcessAnnulerJournal;
import globaz.osiris.translation.CACodeSystem;

/**
 * CA ordre group� Date de cr�ation : (13.12.2001 10:20:46)
 *
 * @author: Brand
 * @revision SCO 19 mars 2010
 */
public class CAOrdreGroupe extends BEntity implements Serializable, APIOrdreGroupe {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String ANNULE = "208007";
    public static final String ERREUR = "208004";
    public static final String ERREUR_PREPARATION = "208006";
    public static final String FIELD_DATECREATION = "DATECREATION";
    public static final String FIELD_DATEECHEANCE = "DATEECHEANCE";
    public static final String FIELD_DATETRANSMISSION = "DATETRANSMISSION";
    public static final String FIELD_ESTCONFIDENTIEL = "ESTCONFIDENTIEL";
    public static final String FIELD_ETAT = "ETAT";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String FIELD_IDLOG = "IDLOG";
    public static final String FIELD_IDORDREGROUPE = "IDORDREGROUPE";
    public static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    public static final String FIELD_IDPOSTEJOURNAL = "IDPOSTEJOURNAL";
    public static final String FIELD_MOTIF = "MOTIF";
    public static final String FIELD_NATUREORDRESLIVRES = "NATUREORDRESLIVRES";
    public static final String FIELD_NOMBRETRANSACTIONS = "NOMBRETRANSACTIONS";
    public static final String FIELD_NOMSUPPORT = "NOMSUPPORT";
    public static final String FIELD_NUMEROOG = "NUMEROOG";

    public static final String FIELD_TOTAL = "TOTAL";
    public static final String FIELD_TYPEORDREGROUPE = "TYPEORDREGROUPE";

    public static final String FIELD_ISOTYPEAVIS = "ISOTYPEAVIS";
    public static final String FIELD_ISONUMLIVR = "ISONUMLIVR";
    public static final String FIELD_ISOHAUTEPRIO = "ISOHAUTEPRIO";
    public static final String FIELD_ISOGEST = "ISOGEST";
    public static final String FIELD_ISOORDRESTAT = "ISOORDRESTAT";
    public static final String FIELD_ISOTRANSACSTAT = "ISOTRANSACSTAT";

    public static final String GENERE = "208005";
    public final static String NATURE_ALFA_ACM = "209008";
    public final static String NATURE_ASSURANCE_MATERNITE = "209004";
    public final static String NATURE_PCF = "209009";
    public final static String NATURE_RENTES_AVS_AI = "209005";
    public final static String NATURE_VERSEMENT_AF = "209007";

    public final static String NATURE_VERSEMENT_AMC = "209011";
    public final static String NATURE_VERSEMENT_APG = "209003";
    public final static String NATURE_VERSEMENT_FCF = "209010";
    public final static String NATURE_VERSEMENT_IJAI = "209002";
    public final static String NATURE_VERSEMENT_RECOUVREMENT_COT = "209006";

    public final static String NATURE_VERSEMENT_REMBOURSEMENT_COT = "209001";

    public final static String NATURE_VERSEMENT_PRESTATIONS_CONVENTIONNELLES = "209012";

    public final static String ORDRESTOUS = "209000";
    public static final String OUVERT = "208001";
    public final static String RECOUVREMENT = "207003";
    public static final String TABLE_CAORGRP = "CAORGRP";
    public final static String TOUS = "207001";
    public static final String TRAITEMENT = "208002";
    public static final String TRANSMIS = "208003";
    public final static String VERSEMENT = "207002";

    /**
     * OCA pr�ventif Au cas ou l'implementation "Lite" de l'opae aurait un bug, on peut toujours forcer l'utilisation de
     * la version 1 pour tout les cas.
     *
     *
     * pour avoir la cle dans fx : insert into SCHEMA.fwparp
     * (pparia,pparap,ppacdi,pcosid,pparpd,ppadde,pcoitc,pparva,pprade) values
     * (0,'FX','OPAE_V1',1,0,20110317,0,'false','force opae v1');
     *
     */
    public static boolean isForceOPAEV1(BSession session) throws Exception {
        FWFindParameterManager mgr = new FWFindParameterManager();
        mgr.setSession(session);
        mgr.setIdCodeSysteme("1");
        mgr.setIdApplParametre("FX");
        mgr.setIdCleDiffere("OPAE_V1");
        mgr.find();
        if (mgr.size() > 0) {
            FWFindParameter e = (FWFindParameter) mgr.getFirstEntity();
            String val = e.getValeurAlphaParametre();
            if ("TRUE".equalsIgnoreCase(val)) {
                return true; // utilise la version 1 (CAProcessFormatOrdreOPAE)
            }
        }
        return false; // utilise l'implementation Lite (CAProcessFormatOrdreOPAELite)
    }

    private FWParametersSystemCode csEtat = null;
    private FWParametersSystemCodeManager csEtats = null;
    private FWParametersSystemCode csNatureOrdresLivres = null;
    private FWParametersSystemCodeManager csNaturesOrdresLivres = null;
    private CAOrganeExecutionManager csOrganeExecution = null;
    private FWParametersSystemCode csTypeOrdreGroupe = null;
    private FWParametersSystemCodeManager csTypesOrdreGroupe = null;
    private String dateCreation = "";
    private String dateEcheance = "";
    private String dateTransmission = "";
    private String estConfidentiel = "";
    private String etat = "";
    private String idJournal = "";
    private String idLog = "";
    private String idOrdreGroupe = "";
    private String idOrganeExecution = "";
    private String idPosteJournal = "";
    private CAJournal journal;
    private List listeDateEcheance = new ArrayList();
    private FWLog log;
    private FWMemoryLog memoryLog = null;

    private String motif = "";

    private String natureOrdresLivres = "";
    private String nombreTransactions = "0";
    private String nomSupport = "";
    private String numeroOG = "";
    // Num�ro et d�signation (Exemple : 127 - Paiement interm...)
    private String ordreGroupeLong = "";
    private CAOrganeExecution organeExecution = null;
    private String total = "0.0";
    private String typeOrdreGroupe = "";
    private FWParametersUserCode ucEtat = null;

    // private String isoNumLivraison = "";
    private String isoHighPriority = "";
    private String isoCsTypeAvis = "";
    private String isoGestionnaire = "";
    private String isoCsOrdreStatutExec = "";
    private String isoCsTransmissionStatutExec = "";
    // Cr�ation des codes syst�mes
    private FWParametersUserCode ucNatureOrdresLivres = null;

    private FWParametersUserCode ucTypeOrdreGroupe = null;

    /**
     * Commentaire relatif au constructeur CAOrdreGroupe
     */
    public CAOrdreGroupe() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2002 15:26:25)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) {

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.03.2002 15:40:23)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incr�mente le prochain num�ro
        setIdOrdreGroupe(this._incCounter(transaction, idOrdreGroupe));

        // type d'ordre group� = versement par d�faut
        // sch--- setTypeOrdreGroupe(this.VERSEMENT);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 14:47:48)
     *
     * @exception Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // Refuser s'il y a des op�rations associ�es � l'ordre group�
        if (hasOperations()) {
            _addError(transaction, getSession().getLabel("5231"));
        }

        // Refuser si l'ordre group� est en traitement
        if (getEtat().equals(CAOrdreGroupe.TRAITEMENT)) {
            _addError(transaction, getSession().getLabel("5232"));
        }

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 14:47:48)
     *
     * @exception Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Sauvegarder le log m�moire s'il existe et s'il y a des messages
        if (memoryLog != null) {
            setIdLog("0");
            if (getMemoryLog().hasMessages()) {

                // Demander la sauvegarde
                log = getMemoryLog().saveToFWLog(transaction);

                // En cas d'erreur, on signale que la sauvegarde du log a �chou�
                if (hasErrors()) {
                    _addError(transaction, getSession().getLabel("5011"));
                    log = null;
                }

                // Si la sauvegarde a r�ussi
                if (log != null) {
                    setIdLog(log.getIdLog());
                }
            }
        }

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAOrdreGroupe.TABLE_CAORGRP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateCreation = statement.dbReadDateAMJ(CAOrdreGroupe.FIELD_DATECREATION);
        dateEcheance = statement.dbReadDateAMJ(CAOrdreGroupe.FIELD_DATEECHEANCE);
        dateTransmission = statement.dbReadDateAMJ(CAOrdreGroupe.FIELD_DATETRANSMISSION);
        estConfidentiel = statement.dbReadString(CAOrdreGroupe.FIELD_ESTCONFIDENTIEL);
        idJournal = statement.dbReadNumeric(CAOrdreGroupe.FIELD_IDJOURNAL);
        idLog = statement.dbReadNumeric(CAOrdreGroupe.FIELD_IDLOG);
        idOrdreGroupe = statement.dbReadNumeric(CAOrdreGroupe.FIELD_IDORDREGROUPE);
        idOrganeExecution = statement.dbReadNumeric(CAOrdreGroupe.FIELD_IDORGANEEXECUTION);
        idPosteJournal = statement.dbReadNumeric(CAOrdreGroupe.FIELD_IDPOSTEJOURNAL);
        motif = statement.dbReadString(CAOrdreGroupe.FIELD_MOTIF);
        natureOrdresLivres = statement.dbReadNumeric(CAOrdreGroupe.FIELD_NATUREORDRESLIVRES);
        nombreTransactions = statement.dbReadNumeric(CAOrdreGroupe.FIELD_NOMBRETRANSACTIONS);
        nomSupport = statement.dbReadString(CAOrdreGroupe.FIELD_NOMSUPPORT);
        numeroOG = statement.dbReadNumeric(CAOrdreGroupe.FIELD_NUMEROOG);
        total = statement.dbReadNumeric(CAOrdreGroupe.FIELD_TOTAL, 2);
        typeOrdreGroupe = statement.dbReadNumeric(CAOrdreGroupe.FIELD_TYPEORDREGROUPE);
        etat = statement.dbReadNumeric(CAOrdreGroupe.FIELD_ETAT);

        // isoNumLivraison = statement.dbReadString(CAOrdreGroupe.FIELD_ISONUMLIVR);
        isoGestionnaire = statement.dbReadString(CAOrdreGroupe.FIELD_ISOGEST);
        isoHighPriority = statement.dbReadString(CAOrdreGroupe.FIELD_ISOHAUTEPRIO);
        isoCsTypeAvis = statement.dbReadNumeric(CAOrdreGroupe.FIELD_ISOTYPEAVIS);
        isoCsOrdreStatutExec = statement.dbReadNumeric(CAOrdreGroupe.FIELD_ISOORDRESTAT);
        isoCsTransmissionStatutExec = statement.dbReadNumeric(CAOrdreGroupe.FIELD_ISOTRANSACSTAT);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdOrdreGroupe(), getSession().getLabel("5151"));
        _propertyMandatory(statement.getTransaction(), getMotif(), getSession().getLabel("7240"));
        _checkDate(statement.getTransaction(), getDateEcheance(), getSession().getLabel("7241"));
        _propertyMandatory(statement.getTransaction(), getDateEcheance(), getSession().getLabel("7242"));
        _propertyMandatory(statement.getTransaction(), getIdOrganeExecution(), getSession().getLabel("5178"));
        _propertyMandatory(statement.getTransaction(), getTypeOrdreGroupe(), getSession().getLabel("7243"));

        // V�rifier le type d'ordre group�
        if (getCsTypesOrdreGroupe().getCodeSysteme(getTypeOrdreGroupe()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7244") + getTypeOrdreGroupe());
        }

        // V�rifier la nature de l'ordre group�
        if ((!JadeStringUtil.isIntegerEmpty(getNatureOrdresLivres()))
                && (getCsNaturesOrdresLivres().getCodeSysteme(getNatureOrdresLivres()) == null)) {
            _addError(statement.getTransaction(), getSession().getLabel("7245") + getNatureOrdresLivres());
        }

        // V�rifier que la date d'�ch�ance soit sup�rieure � la date du jour
        // JACalendarGregorian cal = new JACalendarGregorian();
        // String dateDuJour = cal.format(cal.today(),
        // JACalendarGregorian.FORMAT_DDsMMsYYYY);
        /*
         * try { if (JAUtil.compareDateFirstLower(this.getDateEcheance(),dateDuJour)) {
         * _addError(statement.getTransaction(), "La date d'�ch�ance doit �tre sup�rieure � la date du jour "); } }
         * catch (Exception e) { e.printStackTrace(); }
         */

        // Si organe ex�cution = POSTE, num�ro OG obligatoire, sinon le champ
        // doit �tre nul
        // D0180 - ISO20022, le num OG est obigatoire seulement pour les poste OPAE, pas les POSTE ISO20022
        try {
            if (getOrganeExecution() != null) {
                if (getOrganeExecution().getGenre().equals(globaz.osiris.api.ordre.APIOrganeExecution.POSTE)
                        && getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_OPAE_DTA)) {
                    _propertyMandatory(statement.getTransaction(), getNumeroOG(), getSession().getLabel("7246"));
                    if (getNumeroOG().length() > 2) {
                        _addError(statement.getTransaction(), getSession().getLabel("7247"));
                    }
                } else {
                    if (!JadeStringUtil.isIntegerEmpty(getNumeroOG())) {
                        _addError(statement.getTransaction(), getSession().getLabel("7248"));
                    }
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("5178"));
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.toString());
        }

        // V�rifier le montant total >= 0
        FWCurrency cTotal = getTotalToCurrency();
        if (cTotal.isNegative()) {
            _addError(statement.getTransaction(), getSession().getLabel("7249"));
        }

        // V�rifier le nombre de transactions
        FWCurrency cTrans = new FWCurrency(getNombreTransactions());
        if (cTrans.isNegative()) {
            _addError(statement.getTransaction(), getSession().getLabel("7250"));
        }

        // D0180 - ISO20022, validation des champs ISO
        try {
            if (getOrganeExecution() != null) {
                if (getOrganeExecution().getGenre().equals(globaz.osiris.api.ordre.APIOrganeExecution.POSTE)
                        && getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022)) {
                    _propertyMandatory(statement.getTransaction(), getIsoGestionnaire(),
                            getSession().getLabel("OG_VALIDATE_GESTIONNAIRE_MANDATORY"));
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("5178"));
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.toString());
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAOrdreGroupe.FIELD_IDORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAOrdreGroupe.FIELD_DATECREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField(CAOrdreGroupe.FIELD_DATEECHEANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField(CAOrdreGroupe.FIELD_DATETRANSMISSION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateTransmission(), "dateTransmission"));
        statement.writeField(CAOrdreGroupe.FIELD_ESTCONFIDENTIEL,
                this._dbWriteString(statement.getTransaction(), getEstConfidentiel(), "estConfidentiel"));
        statement.writeField(CAOrdreGroupe.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(CAOrdreGroupe.FIELD_IDLOG,
                this._dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField(CAOrdreGroupe.FIELD_IDORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), "idOrdreGroupe"));
        statement.writeField(CAOrdreGroupe.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(CAOrdreGroupe.FIELD_IDPOSTEJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournal(), "idPosteJournal"));
        statement.writeField(CAOrdreGroupe.FIELD_MOTIF,
                this._dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField(CAOrdreGroupe.FIELD_NATUREORDRESLIVRES,
                this._dbWriteNumeric(statement.getTransaction(), getNatureOrdresLivres(), "natureOrdresLivres"));
        statement.writeField(CAOrdreGroupe.FIELD_NOMBRETRANSACTIONS,
                this._dbWriteNumeric(statement.getTransaction(), getNombreTransactions(), "nombreTransactions"));
        statement.writeField(CAOrdreGroupe.FIELD_NOMSUPPORT,
                this._dbWriteString(statement.getTransaction(), getNomSupport(), "nomSupport"));
        statement.writeField(CAOrdreGroupe.FIELD_NUMEROOG,
                this._dbWriteNumeric(statement.getTransaction(), getNumeroOG(), "numeroOG"));
        statement.writeField(CAOrdreGroupe.FIELD_TOTAL,
                this._dbWriteNumeric(statement.getTransaction(), getTotal(), "total"));
        statement.writeField(CAOrdreGroupe.FIELD_TYPEORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getTypeOrdreGroupe(), "typeOrdreGroupe"));
        statement.writeField(CAOrdreGroupe.FIELD_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etat de l'ordre group�"));

        statement.writeField(CAOrdreGroupe.FIELD_ISOTYPEAVIS,
                this._dbWriteNumeric(statement.getTransaction(), getIsoCsTypeAvis(), "type d'avis de l'ordre group�"));
        statement.writeField(CAOrdreGroupe.FIELD_ISOORDRESTAT, this._dbWriteNumeric(statement.getTransaction(),
                getIsoCsOrdreStatutExec(), "statut d'exe de l'ordre group�"));
        statement.writeField(CAOrdreGroupe.FIELD_ISOTRANSACSTAT, this._dbWriteNumeric(statement.getTransaction(),
                getIsoCsTransmissionStatutExec(), "statut d'exe de la transaction l'ordre group�"));
        statement.writeField(CAOrdreGroupe.FIELD_ISOHAUTEPRIO,
                this._dbWriteString(statement.getTransaction(), getIsoHighPriority(), "priorit� d'ex�cution"));
        statement.writeField(CAOrdreGroupe.FIELD_ISOGEST,
                this._dbWriteString(statement.getTransaction(), getIsoGestionnaire(), "gestionnaire"));
        // statement.writeField(CAOrdreGroupe.FIELD_ISONUMLIVR,
        // this._dbWriteString(statement.getTransaction(), getIsoNumLivraison(), "numero de livraison"));

    }

    /**
     * Avant l'ex�cution de l'attachement des ordres Date de cr�ation : (22.02.2002 09:27:00)
     */
    public void beforeExecuteAttacherOrdre(CAProcessAttacherOrdre context) {

        // V�rifier l'�tat ouvert
        if (!JadeStringUtil.isIntegerEmpty(getEtat()) && !getEtat().equals(CAOrdreGroupe.ERREUR_PREPARATION)) {
            _addError(null, getSession().getLabel("5402"));
        } else {
            setEtat(CAOrdreGroupe.TRAITEMENT);
        }
    }

    /**
     * Ex�cut� avant l'ex�cution de l'ordre Date de cr�ation : (13.02.2002 13:41:55)
     */
    public void beforeExecuteOrdre(CAProcessOrdre context) {

        // V�rifier l'�tat ouvert
        if (JadeStringUtil.isIntegerEmpty(getEtat()) || getEtat().equals(CAOrdreGroupe.ERREUR_PREPARATION)) {
            _addError(null, getSession().getLabel("5227"));
        } else {
            setEtat(CAOrdreGroupe.TRAITEMENT);
        }
    }

    /**
     * D�tacher un ordre de versement du journal Date de cr�ation : (13.05.2002 14:56:46)
     *
     * @param ordreVersement
     *            globaz.osiris.db.comptes.CAOperationOrdreVersement
     */
    public void detacherOrdre(CAOperationOrdreVersement ordreVersement) {

        // Sous contr�le d'exception
        try {

            // V�rifier le param�tre
            if (ordreVersement == null) {
                _addError(null, getSession().getLabel("5233"));
                return;
            }

            // Erreur si l'ordre est vers�
            if (ordreVersement.getEstVerse()) {
                _addError(null, getSession().getLabel("5235"));
                return;
            }

            // V�rifier si attach�
            if (!JadeStringUtil.isIntegerEmpty(ordreVersement.getIdOrdreGroupe())) {

                // V�rifier si l'ordre group� correspond
                if (!ordreVersement.getIdOrdreGroupe().equals(getIdOrdreGroupe())) {
                    _addError(null, getSession().getLabel("5234"));
                    return;
                }

                // D�tacher
                ordreVersement.setIdOrdreGroupe("0");

                // D�cr�menter le nombre de transaction
                try {
                    long l = Long.parseLong(getNombreTransactions());
                    l--;
                    setNombreTransactions(String.valueOf(l));
                } catch (Exception e) {
                    _addError(null, e.getMessage());
                    return;
                }

                // D�cr�menter le montant
                FWCurrency cTotal = new FWCurrency(getTotal());
                cTotal.sub(ordreVersement.getMontant());
                setTotal(cTotal.toString());

                // Mettre � jour l'ordre de versement
                ordreVersement.setSession(getSession());
                ordreVersement.update();
                if (ordreVersement.hasErrors()) {
                    _addError(null, getSession().getLabel("5408"));
                }
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
    }

    public final void erreur(BTransaction transaction, String msg) {
        _addError(transaction, msg);
    }

    /**
     * Annulation d'un versement Date de cr�ation : (29.04.2002 09:16:32)
     *
     * @param context
     *            CAProcessAnnulerOrdre
     * @throws Exception
     */
    public void executeAnnulerOrdre(CAProcessAnnulerOrdre context) throws Exception {
        // On acc�pte si l'ordre est en traiement
        if (!getEtat().equals(CAOrdreGroupe.TRAITEMENT)) {
            throw new Exception(getSession().getLabel("5222"));
        }

        // S'il existe un journal comptabilis�, le supprimer
        if (getJournal() != null) {
            CAProcessAnnulerJournal proc = new CAProcessAnnulerJournal(context);
            proc.setSession(context.getSession());
            proc.setTransaction(context.getTransaction());
            proc.setIdJournal(getIdJournal());
            proc.executeProcess();

            if (!context.getTransaction().hasErrors()) {
                setIdJournal("0");
            } else {
                throw new Exception(getSession().getLabel("5031"));
            }
        }

        if (context.isAborted()) {
            return;
        }

        // Traiter les ordres selon leur nature
        if (getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
            executeAnnulerOrdreVersement(context);
        } else {
            executeAnnulerOrdreRecouvrement(context);
        }

        // S'il n'y a pas d'erreus, on met � jour le statut
        if (!context.isAborted() && !context.getTransaction().hasErrors()) {
            setDateTransmission("");
            setEtat("");
            setDateCreation("");
            setTotal("0");
            setNombreTransactions("0");
            setEtat(CAOrdreGroupe.ANNULE);
        } else {
            throw new Exception(context.getTransaction().getErrors().toString());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 09:33:49)
     *
     * @param context
     *            globaz.osiris.process.CAProcessAnnulerOrdre
     * @throws Exception
     */
    private void executeAnnulerOrdreRecouvrement(CAProcessAnnulerOrdre context) throws Exception {
        // Initialiser
        String sLastIdOperation = "0";

        // Instancier un manager
        CAOperationOrdreRecouvrementManager mgr = new CAOperationOrdreRecouvrementManager();
        mgr.setSession(context.getSession());
        mgr.setForIdOrdreGroupe(getIdOrdreGroupe());
        mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);

        // Compter les transactions
        context.setState(getSession().getLabel("6107"));
        context.setProgressScaleValue(mgr.getCount(context.getTransaction()));

        // Boucle principale
        while (true) {

            if (context.isAborted()) {
                return;
            }

            // Lecture du manager
            mgr.clear();
            mgr.find(context.getTransaction());

            // Sortir si vide
            if (mgr.size() == 0) {
                break;
            }

            // Lecture des ordres
            for (int i = 0; (i < mgr.size()) && !context.isAborted(); i++) {

                // R�cup�rer l'ordre
                CAOperationOrdreRecouvrement or = (CAOperationOrdreRecouvrement) mgr.getEntity(i);
                or.setSession(context.getSession());

                or.retrieve(context.getTransaction());

                // Dernier identifiant
                sLastIdOperation = or.getIdOperation();

                // Si l'ordre est vers� ou en erreur, on annuler le Recouvrement
                if (or.getEstVerse()
                        || or.getOrdreGroupe().getOrganeExecution().getGenre().equals(APIOrganeExecution.BANQUE)) {
                    or.annulerVerser();
                }

                // D�tachement si n�cessaire
                or.setIdOrdreGroupe("0");

                // Mise � jour
                or.update(context.getTransaction());

                // Fin si erreur
                if (or.hasErrors()) {
                    _addError(context.getTransaction(), getSession().getLabel("5408") + " " + or.toString());
                    return;
                }

                // Incr�menter le compteur
                context.incProgressCounter();
                context.setProgressDescription(sLastIdOperation);
            }

            // Prochain id
            mgr.setAfterIdOperation(sLastIdOperation);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 09:33:26)
     *
     * @param context
     *            CAProcessAnnulerOrdre
     * @throws Exception
     */
    private void executeAnnulerOrdreVersement(CAProcessAnnulerOrdre context) throws Exception {
        // Initialiser
        String sLastIdOperation = "0";

        // Instancier un manager
        CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
        mgr.setSession(context.getSession());
        mgr.setForIdOrdreGroupe(getIdOrdreGroupe());
        mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);

        // Compter les transactions
        context.setState(getSession().getLabel("6107"));
        context.setProgressScaleValue(mgr.getCount(context.getTransaction()));

        // Boucle principale
        while (true) {

            if (context.isAborted()) {
                return;
            }

            // Lecture du manager
            mgr.clear();
            mgr.find(context.getTransaction());

            // Sortir si vide
            if (mgr.size() == 0) {
                break;
            }

            // Lecture des ordres
            for (int i = 0; (i < mgr.size()) && !context.isAborted(); i++) {

                // R�cup�rer l'ordre
                CAOperationOrdreVersement ov = (CAOperationOrdreVersement) mgr.getEntity(i);
                ov.setSession(context.getSession());

                ov.retrieve(context.getTransaction());

                // Dernier identifiant
                sLastIdOperation = ov.getIdOperation();

                // Si l'ordre est vers� ou en erreur, on annuler le versement
                if (ov.getEstVerse()) {
                    ov.annulerVerser();
                }

                // D�tachement si n�cessaire
                ov.setIdOrdreGroupe("0");

                // Mise � jour
                ov.update(context.getTransaction());

                // Fin si erreur
                if (ov.hasErrors()) {
                    _addError(context.getTransaction(), getSession().getLabel("5408") + " " + ov.toString());
                    return;
                }

                // Incr�menter le compteur
                context.incProgressCounter();
                context.setProgressDescription(sLastIdOperation);
            }

            // Prochain id
            mgr.setAfterIdOperation(sLastIdOperation);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 10:35:30)
     *
     * @param param
     *            CAProcessAttacherOrdre
     */
    public void executeAttacherOrdre(CAProcessAttacherOrdre context, String idJournalSource)
            throws CAOGRegroupISODepassement {

        // On acc�pte si l'ordre est en traiement
        if (!getEtat().equals(CAOrdreGroupe.TRAITEMENT)) {
            _addError(null, getSession().getLabel("5222"));
            return;
        }

        // Par d�faut, il y a des erreurs....
        setEtat(CAOrdreGroupe.ERREUR_PREPARATION);

        // Sous controle d'exceptions
        try {
            if (getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                executeAttacherOrdreVersement(context, idJournalSource);
            } else {
                executeAttacherOrdreRecouvrement(context);
            }

        } catch (CAOGRegroupISODepassement depassement) {
            throw depassement;
            // R�cup�rer les exceptions
        } catch (Exception e) {
            _addError(null, e.getMessage());
        } finally {
            // Si aucune transaction, on donne un message d'erreur
            if (JadeStringUtil.isIntegerEmpty(getNombreTransactions())) {
                getMemoryLog().logMessage("5404", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Si OK, l'ordre est ouvert
            if (!hasErrors() && !getMemoryLog().hasErrors()) {
                setEtat(CAOrdreGroupe.OUVERT);
                setDateCreation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            }
        }

        // Fin de la proc�dure
        return;
    }

    private void executeAttacherOrdreRecouvrement(CAProcessAttacherOrdre context) {
        // Initialiser
        FWCurrency cTotal = new FWCurrency();
        long lNTransactions = 0;
        String lastIdOperation = "0";

        // Sous contr�le d'exception
        try {
            // Instancier un manager
            CAOperationOrdreRecouvrementManager mgr = new CAOperationOrdreRecouvrementManager();
            mgr.setSession(context.getSession());
            mgr.setForCodeISOPays("CH");
            mgr.setOrderBy(CAOperationOrdreManager.ORDRE_IDORDREGROUPE_IDOPERATION);
            // On prend que les ordres qui ne sont pas bloqu�s
            mgr.wantForEstBloque(false);

            // On r�cup�re les ordres non rattach�s qui correspondent
            mgr.setForIdOrdreGroupe("0");
            // - � la nature des ordres d�sir�s
            if (!JadeStringUtil.isIntegerEmpty(getNatureOrdresLivres())
                    && (!getNatureOrdresLivres().equals(CAOrdreGroupe.ORDRESTOUS))) {
                mgr.setForIdNatureOrdre(getNatureOrdresLivres());
            }

            // - date d'�ch�ance de l'ordre inf�rieure ou �gale � la date de
            // l'ordre group�
            mgr.setUntilDate(getDateEcheance());
            // - dont l'�tat est comptabilis�
            mgr.setForEtat(APIOperation.ETAT_COMPTABILISE);

            while (true) {

                // R�cup�rer les prochaines transactions
                mgr.clear();
                mgr.find(context.getTransaction(), BManager.SIZE_NOLIMIT);
                if (mgr.hasErrors()) {
                    _addError(context.getTransaction(), getSession().getLabel("5403"));
                    return;
                }

                // Sortir si vide
                if (mgr.size() == 0) {
                    break;
                }

                // V�rifier condition de sortie
                if (context.isAborted()) {
                    return;
                }

                // Parcourir les ordres
                for (int i = 0; i < mgr.size(); i++) {

                    // V�rifier condition de sortie
                    if (context.isAborted()) {
                        return;
                    }

                    // R�cup�rer l'ordre
                    CAOperationOrdreRecouvrement oper = (CAOperationOrdreRecouvrement) mgr.getEntity(i);
                    oper.setSession(context.getSession());

                    // Sauver le dernier ID
                    lastIdOperation = oper.getIdOperation();

                    // V�rifier l'organe d'ex�cution
                    if (JadeStringUtil.isIntegerEmpty(oper.getIdOrganeExecution())
                            || oper.getIdOrganeExecution().equals(getIdOrganeExecution())) {

                        // Contr�ler s'il s'agit d'un CCP ou d'une banque
                        if (getOrganeExecution().getGenre().equals(APIOrganeExecution.BANQUE)) {
                            try {
                                if (!oper.getAdressePaiement().getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
                                    continue;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        } else if (getOrganeExecution().getGenre().equals(APIOrganeExecution.POSTE)) {
                            try {
                                if (!oper.getAdressePaiement().getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                                    continue;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        } else {
                            continue;
                        }

                        // Le rattacher
                        oper.setIdOrdreGroupe(getIdOrdreGroupe());

                        // Mettre � jour
                        oper.update(context.getTransaction());
                        if (oper.hasErrors()) {
                            _addError(context.getTransaction(), getSession().getLabel("5025"));
                            return;
                        }

                        // Totaux
                        lNTransactions++;
                        cTotal.add(oper.getMontant());
                    }
                }

                // Prochain num�ro
                mgr.setAfterIdOperation(lastIdOperation);

            }

            // Stocker le nombre total de transactions et le montant vers�
            setTotal(cTotal.toString());
            setNombreTransactions(String.valueOf(lNTransactions));

            // Pi�ger les exception
        } catch (Exception e) {
            _addError(context.getTransaction(), e.getMessage());
            return;
        }

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 10:46:32)
     *
     * @param context
     *            CAProcessAttacherOrdre
     */
    private void executeAttacherOrdreVersement(CAProcessAttacherOrdre context, String idJournalSource)
            throws CAOGRegroupISODepassement {

        // Initialiser
        FWCurrency cTotal = new FWCurrency();
        long lNTransactions = 0;
        String lastIdOperation = "0";
        long maxOVforThisOG = Long.MAX_VALUE;
        // Sous contr�le d'exception
        try {
            maxOVforThisOG = CASepaCommonUtils.getOvMaxByOG(this);
            // Instancier un manager
            CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
            mgr.setSession(context.getSession());
            mgr.setOrderBy(CAOperationOrdreManager.ORDRE_IDORDREGROUPE_IDOPERATION);
            // On prend que les ordres qui ne sont pas bloqu�s
            mgr.wantForEstBloque(false);

            // On restreint les ordres sur le journal sp�cifi� en param�tre
            if (!JadeStringUtil.isIntegerEmpty(idJournalSource)) {
                mgr.setForIdJournal(idJournalSource);
            }

            // On r�cup�re les ordres non rattach�s qui correspondent
            mgr.setForIdOrdreGroupe("0");
            // - � la nature des ordres d�sir�s
            if (!JadeStringUtil.isIntegerEmpty(getNatureOrdresLivres())
                    && (!getNatureOrdresLivres().equals(CAOrdreGroupe.ORDRESTOUS))) {
                mgr.setForIdNatureOrdre(getNatureOrdresLivres());
            }

            // - date d'�ch�ance de l'ordre inf�rieure ou �gale � la date de
            // l'ordre group�
            mgr.setUntilDate(getDateEcheance());
            // - dont l'�tat est comptabilis�
            mgr.setForEtat(APIOperation.ETAT_COMPTABILISE);

            while (true) {

                // R�cup�rer les prochaines transactions
                mgr.clear();
                mgr.find(context.getTransaction(), BManager.SIZE_NOLIMIT);
                if (mgr.hasErrors()) {
                    _addError(context.getTransaction(), getSession().getLabel("5403"));
                    return;
                }

                // Sortir si vide
                if (mgr.size() == 0) {
                    break;
                }

                // V�rifier condition de sortie
                if (context.isAborted()) {
                    return;
                }

                // Parcourir les ordres
                for (int i = 0; i < mgr.size(); i++) {
                    if (lNTransactions == maxOVforThisOG) {
                        throw new CAOGRegroupISODepassement();
                    }
                    // V�rifier condition de sortie
                    if (context.isAborted()) {
                        return;
                    }

                    // R�cup�rer l'ordre
                    CAOperationOrdreVersement oper = (CAOperationOrdreVersement) mgr.getEntity(i);
                    oper.setSession(context.getSession());

                    // Sauver le dernier ID
                    lastIdOperation = oper.getIdOperation();

                    // V�rifier l'organe d'ex�cution
                    if (JadeStringUtil.isIntegerEmpty(oper.getIdOrganeExecution())
                            || oper.getIdOrganeExecution().equals(getIdOrganeExecution())) {
                        FWCurrency tmp = new FWCurrency(oper.getMontant());
                        tmp.add(oper.getSection().getSolde());
                        if (!oper.getIdTypeOperation().equals(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE)
                                && CAApplication.getApplicationOsiris().getCAParametres().isCheckMontantARembourser()
                                && tmp.isPositive()) {
                            oper.setEstBloque(new Boolean(true));
                            oper.setIdOrdreGroupe("0");
                            oper.getMemoryLog().logMessage(
                                    getSession().getLabel("SOLDE_SECTION_INFERIEUR_MONTANT_REMBOURSE"),
                                    FWMessage.ERREUR, this.getClass().getName());

                            getMemoryLog().logMessage(
                                    getSession().getLabel("SOLDE_SECTION_INFERIEUR_MONTANT_REMBOURSE") + " ("
                                            + oper.getCompteAnnexe().getIdExterneRole() + " - "
                                            + oper.getSection().getIdExterne() + ")",
                                    FWMessage.INFORMATION, this.getClass().getName());
                        } else {
                            // Le rattacher
                            oper.setIdOrdreGroupe(getIdOrdreGroupe());

                            // Totaux
                            lNTransactions++;

                            cTotal.add(oper.getMontant());
                        }

                        oper.update(context.getTransaction());
                        if (oper.hasErrors()) {
                            _addError(context.getTransaction(), getSession().getLabel("5025"));
                            return;
                        }

                    }

                }

                // Prochain num�ro
                mgr.setAfterIdOperation(lastIdOperation);

            }
            // Si on a atteint la limite d'ov par og
        } catch (CAOGRegroupISODepassement dep) {
            throw dep;

            // Pi�ger les exception
        } catch (Exception e) {
            _addError(context.getTransaction(), e.getMessage());
            return;
        } finally {
            // Stocker le nombre total de transactions et le montant vers�
            setTotal(cTotal.toString());
            setNombreTransactions(String.valueOf(lNTransactions));

        }

    }

    /**
     * Ex�cution d'un ordre de versement / recouvrement Date de cr�ation : (13.02.2002 11:17:52)
     *
     * @param context
     *            CAProcessContext les param�tres pour l'ex�cution de l'ordre
     */
    public void executeOrdre(CAProcessOrdre context) {

        // Initialiser
        String sLocalFilename = null;

        // On acc�pte si l'ordre est en traiement
        if (!getEtat().equals(CAOrdreGroupe.TRAITEMENT)) {
            _addError(context.getTransaction(), getSession().getLabel("5222"));
            return;
        }

        // Par d�faut, il y a des erreurs....
        setEtat(CAOrdreGroupe.ERREUR);

        // Sous controle d'exceptions
        try {

            // Instancier un formatteur selon le type d'ordre
            CAOrdreFormateur of = null;

            if (getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {

                // Bug 6504
                // Bug 3972
                traitementJournalCommit(context);
                if (getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022)) {
                    of = new CAProcessFormatOrdreSEPA();
                } else {
                    if (getOrganeExecution().getGenre().equals(APIOrganeExecution.BANQUE)) {
                        of = new CAProcessFormatOrdreDTA();
                    } else if (getOrganeExecution().getGenre().equals(APIOrganeExecution.POSTE)) {
                        if (CAOrdreGroupe.isForceOPAEV1(getSession()) || (getJournal() == null)
                                || !getJournal().getEtat().equals(CAJournal.COMPTABILISE)) {
                            /*
                             * Si le journal associ� a l'ordre group� n'est pas encore comptabilis�, ou que l'on force
                             * l'utilisation de la premi�re impl�mentation.
                             */
                            of = new CAProcessFormatOrdreOPAE();
                        } else {
                            /*
                             * Dans le cas ou le journal associ� a l'ordre group� est d�j� comptabilis�, on peut donc
                             * utiliser le formateur OPAE "lite" qui ne fait que sortir le fichier OPAE, sans aucune
                             * op�ration comptable. La version lite ne traite que les transaction 22 (poste suisse),
                             * 24(mandat suisse) et 27 (banque suisse).
                             *
                             * A noter �galement que les transactions 28 ne sont par support�es pour le moment.
                             */
                            of = new CAProcessFormatOrdreOPAELite();

                        }

                    }
                }
            } else if (getTypeOrdreGroupe().equals(CAOrdreGroupe.RECOUVREMENT)) {
                context.setComptabiliserOrdre(false);
                if (getOrganeExecution().getGenre().equals(APIOrganeExecution.POSTE)) {
                    of = new CAProcessFormatOrdreLSV();
                } else if (getOrganeExecution().getGenre().equals(APIOrganeExecution.BANQUE)) {
                    of = new CAProcessFormatOrdreLSVBanque();
                }
            }

            // Si le formatteur n'a pas �t� trouv�, on sort
            if (of == null) {
                _addError(context.getTransaction(), getSession().getLabel("5202"));
                return;
            }

            // Partager la session
            of.setSession(getSession());

            // Partager le log avec le formatteur
            of.setMemoryLog(getMemoryLog());

            // Utilisation d'un retour chariot
            of.setInsertNewLine(context.getInsertNewLine());

            // Echo vers la console
            of.setEchoToConsole(context.getEchoToConsole());

            // Si g�n�ration du fichier d'�change demand�e
            if (context.getGenererFichierEchange()) {

                // Si nom du fichier pas fourni, en cr�er un par d�faut
                if (JadeStringUtil.isBlank(context.getFileName())) {
                    context.setFileName(Jade.getInstance().getHomeDir() + CAApplication.DEFAULT_OSIRIS_ROOT + "/work/"
                            + getDefaultFilename());
                }

                // G�n�ration d'un nom unique
                sLocalFilename = Jade.getInstance().getHomeDir() + CAApplication.DEFAULT_OSIRIS_ROOT + "/work/"
                        + "ordreGroupe" + getIdOrdreGroupe() + ".out";

                try {
                    of.setPrintWriter(new PrintWriter(
                            new OutputStreamWriter(new java.io.FileOutputStream(sLocalFilename), "ISO8859_1")));
                } catch (Exception e) {
                    _addError(context.getTransaction(), e.getMessage());
                    _addError(context.getTransaction(), getSession().getLabel("5229") + " " + sLocalFilename);
                    return;
                }
            }

            // Formatter l'ent�te
            if (context.getGenererFichierEchange()) {
                of.formatHeader(this);
            }

            // Traiter les erreurs
            if (getMemoryLog().hasErrors()) {
                return;
            }

            // V�rifier le contexte d'ex�cution
            if (context.isAborted()) {
                return;
            }

            // Ex�cuter les ordres
            if (getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                of.executeOrdreVersement(this, context);
            } else {
                executeOrdreRecouvrement(of, context);
            }

            // V�rifier le contexte d'ex�cution
            if (context.isAborted()) {
                return;
            }

            // Formatter la fin de fichier
            if (!hasErrors() && context.getGenererFichierEchange()) {
                of.formatEOF(this);
            }

            // Cl�ture du fichier
            if (context.getGenererFichierEchange()) {
                of.getPrintWriter().close();
                // Renommer le fichier selon les param�tres fournis
                File fSrc = new File(sLocalFilename);
                File fDest = new File(context.getFileName());

                if (fDest.exists()) {
                    fDest.delete();
                }

                if (!fSrc.renameTo(fDest)) {
                    _addError(context.getTransaction(), getSession().getLabel("5229") + " " + context.getFileName());
                    return;
                }
            }

            // Si l'on d�sire comptabiliser
            if ((getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) < 0) && context.getComptabiliserOrdre()) {

                // Si le journal n'est pas d�j� comptabilis�
                if (!getJournal().getEtat().equals(CAJournal.COMPTABILISE)) {
                    // Demander la comptabilisation du journal
                    if (!new CAComptabiliserJournal().comptabiliser(context, getJournal())) {
                        _addError(context.getTransaction(), getSession().getLabel("5226"));
                        return;
                    }
                }
            }
            // Si tout est ok
            if (!context.isAborted() && !hasErrors()
                    && (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) < 0)) {
                // Le fichier a �t� g�n�r�
                setEtat(CAOrdreGroupe.GENERE);
                setDateTransmission(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));

                // Mise � jour
                this.update(context.getTransaction());
                if (isNew() || hasErrors()) {
                    context.getMemoryLog().logStringBuffer(getSession().getErrors(), this.getClass().getName());
                    context.getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                }

                if (context.getGenererFichierEchange()) {
                    if (getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_OPAE_DTA)) {
                        CAOrdreGroupeFtpUtils.sendOrRegisterFile(context, this, getOrganeExecution(),
                                context.getFileName());
                    } else if (getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022)) {
                        // TODO new FTP file location, adapt or develop?
                        /*
                         * CAOrdreGroupeFtpUtils.sendOrRegiserFile(context, this, getOrganeExecution(),
                         * context.getFileName());
                         */

                        // TODO c'est quoi le switch qui fait passer en ftp ou en mail?
                        SepaSendOrderProcessor sendProcessor = new SepaSendOrderProcessor();

                        // getDefaultFilename()

                        InputStream is = null;

                        try {
                            is = new FileInputStream(sLocalFilename);
                            sendProcessor.sendOrdreGroupeByFtp(getSession(), is,
                                    "ordreGroupe" + getIdOrdreGroupe() + ".xml");
                            setEtat(TRANSMIS);
                        } catch (SepaException e) {
                            JadeLogger.info(this, "could not send the data to ftp: " + e);
                            JadeLogger.info(this, e);

                            try {
                                sendProcessor.sendOrdreGroupeByMail(is);
                                // quand envoy� par mail, ne doit pas prendre l'�tat "transmis"
                            } catch (SepaException e1) {
                                JadeLogger.error(this, "could not send the data by email: " + e1);
                                JadeLogger.error(this, e1);

                                _addError(context.getTransaction(), e1.getMessage());
                                getMemoryLog().logMessage(e1.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                            }
                        } finally {
                            IOUtils.closeQuietly(is);
                        }
                    }
                }
            }

            // R�cup�rer les exceptions
        } catch (Exception e) {
            _addError(context.getTransaction(), e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        // Fin de la proc�dure
        return;
    }

    /**
     * Ex�cution d'un ordre de recouvrement Date de cr�ation : (13.02.2002 11:33:24)
     */
    private void executeOrdreRecouvrement(CAOrdreFormateur of, CAProcessOrdre context) {
        // Initialiser
        long nbTransaction = 0;
        FWCurrency fTotalCalcule = new FWCurrency();

        // Sous contr�le d'exceptions
        try {

            // Instancier un manager
            CAOperationOrdreRecouvrementManager mgr = new CAOperationOrdreRecouvrementManager();
            mgr.setSession(context.getSession());
            mgr.setForCodeISOPays("CH");
            mgr.setForIdOrdreGroupe(getIdOrdreGroupe());
            mgr.setOrderBy(CAOperationOrdreManager.ORDER_IDORDREGROUPE_NOMCACHE);

            // R�cup�rer les ordres
            mgr.find(context.getTransaction(), BManager.SIZE_NOLIMIT);

            // Calculer le nombre � ex�cuter
            context.setState(getSession().getLabel("6113"));
            context.setProgressScaleValue(mgr.size());

            // Boucler sur les entit�s
            for (int i = 0; i < mgr.size(); i++) {

                // V�rifier le contexte d'ex�cution
                if (context.isAborted()) {
                    return;
                }

                // R�cup�rer l'ordre de recouvrement
                CAOperationOrdreRecouvrement or = (CAOperationOrdreRecouvrement) mgr.getEntity(i);
                or.setSession(getSession());

                // Num�roter la transaction
                nbTransaction++;
                or.setNumTransaction(String.valueOf(nbTransaction));

                // Valider l'ordre avant le paiement
                or._validerAvantRecouvrement(context.getTransaction());

                // S'il y a des erreurs, on le signale
                if (or.getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
                    getMemoryLog().logMessage("5204", or.getNumTransaction(), FWMessage.ERREUR,
                            this.getClass().getName());
                } else {

                    // D�clencher le recouvrement
                    if (context.getComptabiliserOrdre()) {
                        if (context.getGenererFichierEchange()) {
                            or.verser(of, getJournal(), context.getTransaction());
                        } else {
                            or.verser(null, getJournal(), context.getTransaction());
                        }
                    } else if (context.getGenererFichierEchange()) {
                        or.verser(of, null, context.getTransaction());
                    }

                    // V�rifier les erreurs
                    if (context.getTransaction().hasErrors()) {
                        _addError(context.getTransaction(), getSession().getLabel("5205") + " " + or.getIdOperation());
                        return;
                    }
                }

                // Mise � jour de l'ordre de recouvrement
                or.update(context.getTransaction());
                if (context.getTransaction().hasErrors()) {
                    _addError(context.getTransaction(), getSession().getLabel("5205") + " " + or.getIdOperation());
                    return;
                }

                // Totaliser
                fTotalCalcule.add(or.getMontant());

                // Progression
                context.incProgressCounter();
            }

            // Erreur s'il n'y a pas de transaction
            if (nbTransaction == 0) {
                getMemoryLog().logMessage("5203", null, FWMessage.ERREUR, this.getClass().getName());
            } else {
                // V�rifier le nombre de transactions et le montant total
                int iNombreTransactions = 0;
                FWCurrency fTotal = new FWCurrency(getTotal());
                try {
                    iNombreTransactions = Integer.parseInt(getNombreTransactions());
                } catch (Exception e) {
                    // Do nothing ?
                }

                if (iNombreTransactions != nbTransaction) {
                    getMemoryLog().logMessage("5215", null, FWMessage.ERREUR, this.getClass().getName());
                }

                if (!fTotal.equals(fTotalCalcule)) {
                    getMemoryLog().logMessage("5216", null, FWMessage.ERREUR, this.getClass().getName());
                }

            }
        } catch (Exception e) {
            _addError(context.getTransaction(), e.getMessage());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:37:11)
     *
     * @return FWParametersSystemCode
     */
    public FWParametersSystemCode getCsEtat() {

        if (csEtat == null) {
            // liste pas encore chargee, on la charge
            csEtat = new FWParametersSystemCode();
            csEtat.getCode(getEtat());
        }
        return csEtat;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:38:30)
     *
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsEtats() {
        // liste d�j� charg�e ?
        if (csEtats == null) {
            // liste pas encore charg�e, on la charge
            csEtats = new FWParametersSystemCodeManager();
            csEtats.setSession(getSession());
            csEtats.getListeCodes("OSIETAOGR", getSession().getIdLangue());
        }
        return csEtats;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:37:11)
     *
     * @return FWParametersSystemCode
     */
    public FWParametersSystemCode getCsNatureOrdresLivres() {

        if (csNatureOrdresLivres == null) {
            // liste pas encore chargee, on la charge
            csNatureOrdresLivres = new FWParametersSystemCode();
            csNatureOrdresLivres.setSession(getSession());
            csNatureOrdresLivres.getCode(getNatureOrdresLivres());
        }
        return csNatureOrdresLivres;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:38:30)
     *
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsNaturesOrdresLivres() {
        if (csNaturesOrdresLivres == null) {
            csNaturesOrdresLivres = CACodeSystem.getNatureVersementsManager(getSession());
        }
        return csNaturesOrdresLivres;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.03.2002 15:08:14)
     *
     * @return String
     */
    public globaz.osiris.db.ordres.CAOrganeExecutionManager getCsOrganeExecution() {
        // liste d�j� charg�e ?
        if (csOrganeExecution == null) {
            // liste pas encore charg�e, on la charge
            csOrganeExecution = new CAOrganeExecutionManager();
            csOrganeExecution.setSession(getSession());
            csOrganeExecution.getFromNom();
        }
        return csOrganeExecution;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:37:11)
     *
     * @return FWParametersSystemCode
     */
    public FWParametersSystemCode getCsTypeOrdreGroupe() {

        if (csTypeOrdreGroupe == null) {
            // liste pas encore chargee, on la charge
            csTypeOrdreGroupe = new FWParametersSystemCode();
            csTypeOrdreGroupe.setSession(getSession());
            csTypeOrdreGroupe.getCode(getTypeOrdreGroupe());
        }
        return csTypeOrdreGroupe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:38:30)
     *
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypesOrdreGroupe() {
        // liste d�j� charg�e ?
        if (csTypesOrdreGroupe == null) {
            // liste pas encore charg�e, on la charge
            csTypesOrdreGroupe = new FWParametersSystemCodeManager();
            csTypesOrdreGroupe.setSession(getSession());
            csTypesOrdreGroupe.getListeCodes("OSIORDGRO", getSession().getIdLangue());
        }
        return csTypesOrdreGroupe;
    }

    /**
     * Getter
     */
    @Override
    public String getDateCreation() {
        return dateCreation;
    }

    @Override
    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateTransmission() {
        return dateTransmission;
    }

    /**
     * Retourne le nom du fichier d'�change selon les param�tres de l'ordre Date de cr�ation : (17.04.2002 15:45:30)
     *
     * @return String
     */
    public String getDefaultFilename() throws Exception {
        String filename = "file.dat";

        // Si organe d'ex�cution d�fini
        if (getOrganeExecution() != null) {
            // En fonction du type d'ordre
            if (getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                // DTA
                if (getOrganeExecution().getGenre().equals(APIOrganeExecution.BANQUE)) {
                    filename = "dtalsv.";
                    if (!JadeStringUtil.isBlank(getOrganeExecution().getIdentifiantDTA())) {
                        filename = filename + getOrganeExecution().getIdentifiantDTA().substring(0, 3);
                    } else {
                        filename = filename + "dta";
                    }
                } else {
                    // OPAE
                    filename = "pttcria";
                }
            }
        }

        return filename;
    }

    public String getEstConfidentiel() {
        return estConfidentiel;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIdPosteJournal() {
        return idPosteJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     *
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CAJournal getJournal() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return null;
        }

        // Si log pas d�j� charg�
        if (journal == null) {
            // Instancier un nouveau LOG
            journal = new CAJournal();
            journal.setSession(getSession());

            // R�cup�rer le log en question
            journal.setIdJournal(getIdJournal());
            try {
                journal.retrieve();
                if (journal.hasErrors() || journal.isNew()) {
                    journal = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                journal = null;
            }
        }

        return journal;
    }

    /**
     * @return the listeDateEcheance
     */
    public List getListeDateEcheance() {
        listeDateEcheance.clear();
        CAOperationOrdreManager manager = new CAOperationOrdreManager();
        manager.setSession(getSession());
        manager.setForIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);
        manager.setForIdOrdreGroupe("0");
        manager.wantForEstBloque(false);
        manager.setForEtat(APIOperation.ETAT_COMPTABILISE);
        manager.setGroupByDate(Boolean.TRUE);
        manager.setOrderBy(CAOperation.FIELD_DATE);
        try {
            manager.find();
            for (int i = 0; i < manager.getSize(); i++) {
                listeDateEcheance.add(((CAOperation) manager.getEntity(i)).getDate());
            }
        } catch (Exception e) {
            return listeDateEcheance;
        }
        return listeDateEcheance;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 16:53:13)
     *
     * @return globaz.osiris.db.utils.FWLog
     */
    public FWLog getLog() {

        // Si le log n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdLog())) {
            return null;
        }

        // Si log pas d�j� charg�
        if (log == null) {
            // Instancier un nouveau LOG
            log = new FWLog();
            log.setSession(getSession());

            // R�cup�rer le log en question
            log.setIdLog(getIdLog());
            try {
                log.retrieve();
                if (log.hasErrors() || log.isNew()) {
                    log = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                log = null;
            }
        }

        return log;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 11:13:19)
     *
     * @return globaz.osiris.db.utils.FWMemoryLog
     */
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
            memoryLog.setSession(getSession());
        }
        return memoryLog;
    }

    @Override
    public String getMotif() {
        return motif;
    }

    public String getNatureOrdresLivres() {
        return natureOrdresLivres;
    }

    @Override
    public String getNbTransactions() throws Exception {
        return getNombreTransactions();
    }

    public String getNombreTransactions() {
        return nombreTransactions;
    }

    public String getNomSupport() {
        return nomSupport;
    }

    @Override
    public String getNumeroOG() {
        return numeroOG;
    }

    public String getIsoNumLivraison() {
        return "OG-" + getId();
    }

    @Override
    public String getIsoHighPriority() {
        return isoHighPriority;
    }

    public String getIsoCsTypeAvis() {
        return (JadeStringUtil.isEmpty(isoCsTypeAvis) ? APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS : isoCsTypeAvis);
    }

    public String getIsoGestionnaire() {
        return (JadeStringUtil.isEmpty(isoGestionnaire) ? getSession().getUserName() : isoGestionnaire);
    }

    public String getIsoCsOrdreStatutExec() {
        return (JadeStringUtil.isEmpty(isoCsOrdreStatutExec) ? APIOrdreGroupe.ISO_ORDRE_STATUS_A_TRANSMETTRE
                : isoCsOrdreStatutExec);
    }

    public String getIsoCsTransmissionStatutExec() {
        return (JadeStringUtil.isEmpty(isoCsTransmissionStatutExec) ? APIOrdreGroupe.ISO_TRANSAC_STATUS_AUCUNE
                : isoCsTransmissionStatutExec);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.03.2002 15:45:13)
     *
     * @return String
     */
    public String getOrdreGroupeLong() {
        ordreGroupeLong = getIdOrdreGroupe() + " - " + getMotif();
        return ordreGroupeLong;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 13:46:51)
     *
     * @return globaz.osiris.db.ordres.CAOrganeExecution
     */
    @Override
    public APIOrganeExecution getOrganeExecution() throws Exception {

        // Si l'ordre group� n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            return null;
        }

        // Si pas d�j� charg�
        if (organeExecution == null) {
            // Instancier nouvelle entit�
            organeExecution = new CAOrganeExecution();
            organeExecution.setSession(getSession());

            // R�cup�rer l'entit�
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            try {
                organeExecution.retrieve();
                if (organeExecution.hasErrors() || organeExecution.isNew()) {
                    organeExecution = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                organeExecution = null;
            }
        }

        return organeExecution;
    }

    @Override
    public String getTotal() throws Exception {
        return JANumberFormatter.deQuote(total);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.05.2002 08:23:08)
     *
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getTotalToCurrency() {
        return new FWCurrency(JANumberFormatter.deQuote(total));
    }

    @Override
    public String getTypeOrdreGroupe() {
        return typeOrdreGroupe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.01.2002 16:36:04)
     *
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcEtat() {

        if (ucEtat == null) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
        }
        ucEtat.setSession(getSession());

        // R�cup�rer le code syst�me dans la langue de l'utilisateur
        if (!JadeStringUtil.isIntegerEmpty(getEtat())) {
            ucEtat.setIdCodeSysteme(getEtat());
            ucEtat.setIdLangue(getSession().getIdLangue());
            try {
                ucEtat.retrieve();
                if (ucEtat.hasErrors() || ucEtat.isNew()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7251"));
            }
        }

        return ucEtat;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.01.2002 16:36:04)
     *
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcNatureOrdresLivres() {

        if (ucNatureOrdresLivres == null) {
            // liste pas encore chargee, on la charge
            ucNatureOrdresLivres = new FWParametersUserCode();
            ucNatureOrdresLivres.setSession(getSession());

            // R�cup�rer le code syst�me dans la langue de l'utilisateur
            ucNatureOrdresLivres.setIdCodeSysteme(getNatureOrdresLivres());
            ucNatureOrdresLivres.setIdLangue(getSession().getIdLangue());
            try {
                ucNatureOrdresLivres.retrieve();
                if (ucNatureOrdresLivres.hasErrors() || ucNatureOrdresLivres.isNew()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7251"));
            }
        }

        return ucNatureOrdresLivres;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.01.2002 16:36:04)
     *
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeOrdreGroupe() {

        if (ucTypeOrdreGroupe == null) {
            // liste pas encore chargee, on la charge
            ucTypeOrdreGroupe = new FWParametersUserCode();
            ucTypeOrdreGroupe.setSession(getSession());

            // R�cup�rer le code syst�me dans la langue de l'utilisateur
            ucTypeOrdreGroupe.setIdCodeSysteme(getTypeOrdreGroupe());
            ucTypeOrdreGroupe.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeOrdreGroupe.retrieve();
                if (ucTypeOrdreGroupe.hasErrors() || ucTypeOrdreGroupe.isNew()) {
                    _addError(null, getSession().getLabel("7251"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7251"));
            }
        }

        return ucTypeOrdreGroupe;
    }

    /**
     * Retourne vrai si l'ordre group� contient des ordres (op�rations) Date de cr�ation : (25.04.2002 14:22:28)
     *
     * @return boolean vrai si l'ordre group� contient des ordres
     */
    public boolean hasOperations() {

        try {
            // Instancier un manager pour les ordres de versement et compter les
            // op�rations
            if (getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
                mgr.setSession(getSession());
                mgr.setForIdOrdreGroupe(getIdOrdreGroupe());
                if (mgr.getCount() > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }

        return false;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.03.2002 15:08:14)
     *
     * @param newOrganesExecution
     *            String
     */
    public void setCsOrganeExecution(globaz.osiris.db.ordres.CAOrganeExecutionManager newCsOrganeExecution) {
        csOrganeExecution = newCsOrganeExecution;
    }

    /**
     * Setter
     */
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    public void setDateEcheance(String newDateEcheance) {
        dateEcheance = newDateEcheance;
    }

    public void setDateTransmission(String newDateTransmission) {
        dateTransmission = newDateTransmission;
    }

    public void setEstConfidentiel(String newEstConfidentiel) {
        estConfidentiel = newEstConfidentiel;
    }

    public void setEtat(String newEtat) {
        etat = newEtat;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setIdLog(String newIdLog) {
        // Supprimer le log pr�c�dent le cas �ch�ant
        if (getLog() != null) {
            try {
                getLog().delete();
                if (getLog().hasErrors()) {
                    _addError(null, getSession().getLabel("7000") + " " + getLog().getIdLog());
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        log = null;
        idLog = newIdLog;
    }

    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
    }

    public void setIdOrganeExecution(String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    public void setIdPosteJournal(String newIdPosteJournal) {
        idPosteJournal = newIdPosteJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 11:13:19)
     *
     * @param newMemoryLog
     *            globaz.osiris.db.utils.FWMemoryLog
     */
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    public void setMotif(String newMotif) {
        motif = newMotif;
    }

    public void setNatureOrdresLivres(String newNatureOrdresLivres) {
        natureOrdresLivres = newNatureOrdresLivres;
    }

    public void setNombreTransactions(String newNombreTransactions) {
        nombreTransactions = newNombreTransactions;
    }

    public void setNomSupport(String newNomSupport) {
        nomSupport = newNomSupport;
    }

    public void setNumeroOG(String newNumeroOG) {
        numeroOG = newNumeroOG;
    }

    public void setTotal(String newTotal) {
        total = newTotal;
    }

    public void setTypeOrdreGroupe(String newTypeOrdreGroupe) {
        typeOrdreGroupe = newTypeOrdreGroupe;
    }

    public void setIsoHighPriority(String isoHighPriority) {
        this.isoHighPriority = isoHighPriority;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public void setIsoCsOrdreStatutExec(String isoCsOrdreStatutExec) {
        this.isoCsOrdreStatutExec = isoCsOrdreStatutExec;
    }

    public void setIsoCsTransmissionStatutExec(String isoCsTransmissionStatutExec) {
        this.isoCsTransmissionStatutExec = isoCsTransmissionStatutExec;
    }

    // public void setIsoNumLivraison(String isoNumLivraison) {
    // this.isoNumLivraison = isoNumLivraison;
    // }

    // *******************************************************
    // Getter pour ex�cution de l'ordre group�
    // *******************************************************

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.03.2002 16:27:39)
     */

    public void setUcEtat(FWParametersUserCode newUcEtat) {
        ucEtat = newUcEtat;
    }

    /**
     * @param context
     * @throws Exception
     */
    private void traitementJournalCommit(CAProcessOrdre context) throws Exception {
        // V�rifier s'il y a lieu de comptabiliser les ordres
        if (context.getComptabiliserOrdre()) {

            // S'il n'y a pas de journal, il faut un g�n�rer un
            if (getJournal() == null) {
                journal = new CAJournal();
                journal.setSession(getSession());
                journal.setDateValeurCG(getDateEcheance());
                journal.setLibelle(getMotif());
                journal.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);

                // L'ins�rer
                journal.add(context.getTransaction());
                if (journal.hasErrors()) {
                    throw new Exception(getSession().getLabel("5225"));
                }

                // Mise � jour de l'identifiant du journal
                setIdJournal(journal.getIdJournal());
                this.save(context.getTransaction());
                context.getTransaction().commit();

                // S'il y a d�j� un journal non encore comptabilis�
                // d�finitivement
            } else if (!getJournal().getEtat().equals(CAJournal.COMPTABILISE)) {
                journal = getJournal();
            } else {
                journal = null;
                context.setComptabiliserOrdre(false);
                getMemoryLog().logMessage("5230", null, FWMessage.AVERTISSEMENT, this.getClass().getName());
            }
        }
    }

    @Override
    public String getNumLivraison() {
        return getIsoNumLivraison();
    }

    @Override
    public String getTypeAvis() {
        return getIsoCsTypeAvis();
    }

}
