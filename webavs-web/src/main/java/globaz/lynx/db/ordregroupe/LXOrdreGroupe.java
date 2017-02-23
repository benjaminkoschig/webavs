package globaz.lynx.db.ordregroupe;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.translation.LXCodeSystem;
import globaz.lynx.utils.LXUtils;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.util.ArrayList;

public class LXOrdreGroupe extends BEntity implements APIOrdreGroupe {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Code systeme ETAT
    public static final String CS_ETAT_ANNULE = "7900003";
    public static final String CS_ETAT_GENERE = "7900005";
    public static final String CS_ETAT_OUVERT = "7900001";
    public static final String CS_ETAT_PREPARE = "7900004";
    public static final String CS_ETAT_TRAITEMENT = "7900002";
    public static final String FIELD_CSETAT = "CSETAT";
    public static final String FIELD_DATECREATION = "DATECREATION";

    public static final String FIELD_DATEECHEANCE = "DATEECHEANCE";
    public static final String FIELD_DATEPAIEMENT = "DATEPAIEMENT";
    public static final String FIELD_DATETRANSMISSION = "DATETRANSMISSION";
    public static final String FIELD_IDJOURNALLIE = "IDJOURNALLIE";
    public static final String FIELD_IDORDREGROUPE = "IDORDREGROUPE";
    public static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_NUMEROOG = "NUMEROOG";
    public static final String FIELD_PROPRIETAIRE = "PROPRIETAIRE";
    public static final String TABLE_LXORGRP = "LXORGRP";

    private String csEtat = "";
    private String dateCreation = "";
    private String dateEcheance = "";
    private String datePaiement = "";
    private String dateTransmission = "";
    private String idJournalLie = "";
    private String idOrdreGroupe = "";
    private String idOrganeExecution = "";
    private String idSociete = "";
    private String libelle = "";
    private String nomOrganeExecution = "";
    private String nomSociete = "";
    private String numeroOG = "";
    private LXOrganeExecution organeExecution = null;
    private String proprietaire = "";
    private LXSocieteDebitrice societe;

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdOrdreGroupe(this._incCounter(transaction, getIdOrdreGroupe()));

        if (JadeStringUtil.isBlank(getProprietaire())) {
            setProprietaire(getSession().getUserName());
        }

        if (JadeStringUtil.isBlank(getDateCreation())) {
            setDateCreation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        }

        if (JadeStringUtil.isBlank(getCsEtat())) {
            setCsEtat(LXOrdreGroupe.CS_ETAT_OUVERT);
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdSociete())
                && !new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), transaction, getDateEcheance(),
                        getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // Suppression impossible d'un ordre groupé
        _addError(transaction, getSession().getLabel("ORDREGROUPE_SUPPR_IMPOSSIBLE"));
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete())
                && !new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), transaction, getDateEcheance(),
                        getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return LXOrdreGroupe.TABLE_LXORGRP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdOrdreGroupe(statement.dbReadNumeric(LXOrdreGroupe.FIELD_IDORDREGROUPE));
        setIdSociete(statement.dbReadNumeric(LXOrdreGroupe.FIELD_IDSOCIETE));
        setIdOrganeExecution(statement.dbReadNumeric(LXOrdreGroupe.FIELD_IDORGANEEXECUTION));
        setIdJournalLie(statement.dbReadNumeric(LXOrdreGroupe.FIELD_IDJOURNALLIE));
        setDateEcheance(statement.dbReadDateAMJ(LXOrdreGroupe.FIELD_DATEECHEANCE));
        setDatePaiement(statement.dbReadDateAMJ(LXOrdreGroupe.FIELD_DATEPAIEMENT));
        setDateCreation(statement.dbReadDateAMJ(LXOrdreGroupe.FIELD_DATECREATION));
        setDateTransmission(statement.dbReadDateAMJ(LXOrdreGroupe.FIELD_DATETRANSMISSION));
        setLibelle(statement.dbReadString(LXOrdreGroupe.FIELD_LIBELLE));
        setCsEtat(statement.dbReadNumeric(LXOrdreGroupe.FIELD_CSETAT));
        setProprietaire(statement.dbReadString(LXOrdreGroupe.FIELD_PROPRIETAIRE));
        setNumeroOG(statement.dbReadNumeric(LXOrdreGroupe.FIELD_NUMEROOG));

        try {
            // Recuperation du nom et complement sur la table des tiers
            setNomSociete(LXUtils.getNomComplet(statement.dbReadString("HTLDE1"), statement.dbReadString("HTLDE2")));
        } catch (Exception e) {
            // nothing
        }
        try {
            // Recuperation du nom de l'organe d'execution
            setNomOrganeExecution(statement.dbReadString(LXOrganeExecution.FIELD_NOM));
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Controle de l'id organe d'execution
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_ORGANE_EXECUTION"));
        }

        // Controle de l'id societe
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        // Vérifier le code système état
        if (LXCodeSystem.getCsEtatOrdreGroupe(getSession()).getCodeSysteme(getCsEtat()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_ETAT"));
        }

        // Vérifie présence du libellé
        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_LIBELLE_OBLIGATOIRE"));
        }

        // Vérification présence de la date d'echéance
        if (!JadeDateUtil.isGlobazDate(getDateEcheance())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_DATE_ECHEANCE"));
        }

        if (JadeStringUtil.isDecimalEmpty(getNumeroOG())
                && LXOrganeExecution.CS_GENRE_POSTE.equals(((LXOrganeExecution) getOrganeExecution()).getCsGenre())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_NUMERO_OG"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(LXOrdreGroupe.FIELD_IDORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(LXOrdreGroupe.FIELD_IDORDREGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), "idOrdreGroupe"));
        statement.writeField(LXOrdreGroupe.FIELD_IDSOCIETE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(LXOrdreGroupe.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(LXOrdreGroupe.FIELD_IDJOURNALLIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalLie(), "idJournalCG"));
        statement.writeField(LXOrdreGroupe.FIELD_DATEECHEANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField(LXOrdreGroupe.FIELD_DATEPAIEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), getDatePaiement(), "datePaiement"));
        statement.writeField(LXOrdreGroupe.FIELD_DATECREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField(LXOrdreGroupe.FIELD_DATETRANSMISSION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateTransmission(), "dateTransmission"));
        statement.writeField(LXOrdreGroupe.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(LXOrdreGroupe.FIELD_CSETAT,
                this._dbWriteNumeric(statement.getTransaction(), getCsEtat(), "csEtat"));
        statement.writeField(LXOrdreGroupe.FIELD_PROPRIETAIRE,
                this._dbWriteString(statement.getTransaction(), getProprietaire(), "proprietaire"));
        statement.writeField(LXOrdreGroupe.FIELD_NUMEROOG,
                this._dbWriteNumeric(statement.getTransaction(), getNumeroOG(), "numeroOG"));
    }

    public String getCsEtat() {
        return csEtat;
    }

    @Override
    public String getDateCreation() {
        return dateCreation;
    }

    @Override
    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * @return the datePaiement
     */
    public String getDatePaiement() {
        return datePaiement;
    }

    public String getDateTransmission() {
        return dateTransmission;
    }

    public String getIdJournalLie() {
        return idJournalLie;
    }

    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIdSociete() {
        return idSociete;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String getMotif() {
        // TODO DDA : Motif spécial ou libellé ok ?
        return getLibelle();
    }

    @Override
    public String getNbTransactions() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_ORGANE_EXECUTION"));
        }

        LXOperationManager manager = getPaiementManager();

        return "" + manager.getCount();
    }

    public String getNomOrganeExecution() {
        return nomOrganeExecution;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    @Override
    public String getNumeroOG() {
        return numeroOG;
    }

    @Override
    public APIOrganeExecution getOrganeExecution() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_ORGANE_EXECUTION"));
        }

        if (organeExecution == null) {
            organeExecution = new LXOrganeExecution();
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            organeExecution.retrieve();

            if (organeExecution.hasErrors()) {
                throw new Exception(organeExecution.getErrors().toString());
            }

            if (organeExecution.isNew()) {
                throw new Exception(getSession().getLabel("RETRIEVE_ORGANEEXECUTION_IMPOSSIBLE"));
            }
        }

        return organeExecution;
    }

    private LXOperationManager getPaiementManager() throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());

        manager.setForIdOrdreGroupe(getIdOrdreGroupe());

        ArrayList<String> forCsTypeOperationIn = new ArrayList<String>();

        if (LXOrganeExecution.CS_GENRE_CAISSE.equals(((LXOrganeExecution) getOrganeExecution()).getCsGenre())) {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        } else if (LXOrganeExecution.CS_GENRE_LSV.equals(((LXOrganeExecution) getOrganeExecution()).getCsGenre())) {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
        } else {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);
        }

        manager.setForIdTypeOperationIn(forCsTypeOperationIn);

        manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

        return manager;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    /**
     * Return la société débitrice.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() {
        retrieveSociete();

        return societe;
    }

    @Override
    public String getTotal() throws Exception {
        FWCurrency tmp = new FWCurrency(getTotalPaiement());
        tmp.abs();
        return tmp.toString();
    }

    /**
     * Return le total des escomptes. Information pour l'écran de détail de l'ordre groupé.
     * 
     * @return
     */
    public String getTotalEscompte() {
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            return "";
        }

        try {
            LXOperationManager manager = new LXOperationManager();
            manager.setSession(getSession());

            manager.setForIdOrdreGroupe(getIdOrdreGroupe());

            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(LXOperation.CS_TYPE_ESCOMPTE);
            manager.setForIdTypeOperationIn(idTypeOperationIn);

            manager.setForCsEtatNot(LXOperation.CS_ETAT_ANNULE);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le total des paiements. Information pour l'écran de détail de l'ordre groupé.
     * 
     * @return
     */
    public String getTotalPaiement() {

        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            return "";
        }

        try {
            LXOperationManager manager = getPaiementManager();

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }

    }

    @Override
    public String getTypeOrdreGroupe() {
        return CAOrdreGroupe.VERSEMENT;
    }

    /**
     * L'ordre groupé est-il en état ANNULE ?
     * 
     * @return
     */
    public boolean isAnnule() {
        return ((getCsEtat() != null) && (getCsEtat().equals(LXOrdreGroupe.CS_ETAT_ANNULE)));
    }

    /**
     * L'ordre groupé est-il en état GENERE ?
     * 
     * @return
     */
    public boolean isGenere() {
        return ((getCsEtat() != null) && (getCsEtat().equals(LXOrdreGroupe.CS_ETAT_GENERE)));
    }

    /**
     * L'ordre groupé est-il en état OUVERT ?
     * 
     * @return
     */
    public boolean isOuvert() {
        return ((getCsEtat() != null) && (getCsEtat().equals(LXOrdreGroupe.CS_ETAT_OUVERT)));
    }

    /**
     * L'ordre groupé est-il en état PREPARE ?
     * 
     * @return
     */
    public boolean isPrepare() {
        return ((getCsEtat() != null) && (getCsEtat().equals(LXOrdreGroupe.CS_ETAT_PREPARE)));
    }

    /**
     * L'ordre groupé est-il en état TRAITEMENT ?
     * 
     * @return
     */
    public boolean isTraitement() {
        return ((getCsEtat() != null) && (getCsEtat().equals(LXOrdreGroupe.CS_ETAT_TRAITEMENT)));
    }

    /**
     * Retrouve la societe si pas encore chargée.
     */
    private void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && (societe == null)) {
            try {
                societe = new LXSocieteDebitrice();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * @param datePaiement
     *            the datePaiement to set
     */
    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setDateTransmission(String dateTransmission) {
        this.dateTransmission = dateTransmission;
    }

    public void setIdJournalLie(String idJournalLie) {
        this.idJournalLie = idJournalLie;
    }

    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setNomOrganeExecution(String nomOrganeExecution) {
        this.nomOrganeExecution = nomOrganeExecution;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    @Override
    public String getNumLivraison() {
        // not use in lynx, for iso/sepa in CA
        return null;
    }

    @Override
    public String getIsoHighPriority() {
        // not use in lynx, for iso/sepa in CA
        return null;
    }
}
