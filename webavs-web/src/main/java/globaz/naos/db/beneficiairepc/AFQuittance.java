/*
 * Created on 10.10.2007 JPA Classe Quittance pour les bénéficiaires PC
 */
package globaz.naos.db.beneficiairepc;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pyxis.db.tiers.TITiers;
import java.io.Serializable;
import java.util.Vector;

/**
 * La classe définissant l'entité Quittance (Bénéficiaires PC)
 * 
 * @author jpa
 */
public class AFQuittance extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String unformat(String value) throws Exception {
        if (value == null) {
            return "";
        }
        String str = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != '.') {
                str += value.charAt(i);
            }
        }
        return str;
    }

    public static String unFormat(String chaineFormat) throws Exception {
        String numSansFormat = JAStringFormatter.unFormat(chaineFormat, "", ".");
        numSansFormat = JAStringFormatter.unFormat(numSansFormat, "", "/");
        numSansFormat = JAStringFormatter.unFormat(numSansFormat, "", "-");
        return numSansFormat = JAStringFormatter.unFormat(numSansFormat, "", "_");
    }

    // Fields
    private String annee = "";
    private Boolean ciManuel = Boolean.FALSE;
    private Boolean ciTraite = Boolean.FALSE;
    private String dateNaissance = "";

    private String dateValeur = "";

    private String etatQuittance = "";
    private String idAffBeneficiaire = "";
    // Foreign Key
    private String idJournalQuittance = "";
    private String idLocalite = "";
    // DB Table AFQUIPC
    // Primary Key
    private String idQuittance = "";
    private String idTiers = "";
    private boolean isRentier = true;
    boolean isUpdateFromProcess = false;
    private String messageErreur = null;
    private String montantCI = "";
    private String montantEffectif = "";

    private int nbreMois = 0;

    private Boolean nnss = Boolean.FALSE;

    private String nomAide = "";

    private String nomBeneficiaire = "";

    private String nombreHeures = "";

    private String nomPrenom = "";
    private String numAffilie = "";
    private String numAvsAideMenage = "";
    private String paysOrigineId = "";
    private String periodeDebut = "";

    private String periodeFin = "";
    private String prenomAide = "";
    private String prenomBeneficiaire = "";
    private String prixHeure = "";
    private String sexe = "";

    private String total = "";
    private String totalVerse = "";
    private String updateTag = "";

    Boolean wantValidate = new Boolean(true);

    /**
     * Constructeur d'AFQuittance
     */
    public AFQuittance() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdQuittance(this._incCounter(transaction, idQuittance));
        try {
            // On set la date
            JADate date = new JADate(getPeriodeDebut());
            setAnnee(String.valueOf(date.getYear()));
            setEtatQuittance(AFQuittanceViewBean.ETAT_OUVERT);
        } catch (Exception ex) {
            setAnnee("");
        }

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        try {
            // On set la date
            JADate date = new JADate(getPeriodeDebut());
            setAnnee(String.valueOf(date.getYear()));
            if (!isUpdateFromProcess) {
                setEtatQuittance(AFQuittanceViewBean.ETAT_OUVERT);
                setMessageErreur("");
            }
        } catch (Exception ex) {
            setAnnee("");
        }
        super._beforeUpdate(transaction);
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFQUIPC";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idQuittance = statement.dbReadNumeric("MAQIDQ");
        annee = statement.dbReadNumeric("MAQANN");
        idAffBeneficiaire = statement.dbReadString("MAQTIB");
        numAvsAideMenage = statement.dbReadString("MAQTAM");
        idJournalQuittance = statement.dbReadNumeric("MAQIJR");
        periodeDebut = statement.dbReadString("MAQPDE");
        periodeFin = statement.dbReadString("MAQPFI");
        nombreHeures = statement.dbReadNumeric("MAQNHE");
        prixHeure = statement.dbReadNumeric("MAQPHE");
        totalVerse = statement.dbReadNumeric("MAQTVE");
        dateValeur = statement.dbReadString("MAQDVA");
        idLocalite = statement.dbReadNumeric("MAQILO");
        nomBeneficiaire = statement.dbReadString("HTLDE1");
        prenomBeneficiaire = statement.dbReadString("HTLDE2");
        nomAide = statement.dbReadString("MAQNAI");
        prenomAide = statement.dbReadString("MAQPAI");
        total = statement.dbReadNumeric("total");
        etatQuittance = statement.dbReadString("MAQTET");
        messageErreur = statement.dbReadString("MAQLOG");
        ciTraite = statement.dbReadBoolean("MABCIT");
        ciManuel = statement.dbReadBoolean("MABCIM");
        montantCI = statement.dbReadNumeric("MAQMCI");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (wantValidate.booleanValue()) {

            if (JadeStringUtil.isEmpty(getIdAffBeneficiaire()) && JadeStringUtil.isBlankOrZero(numAffilie)) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_NUM_AFF_BENEFICIAIRE_ERREUR"));
            } else {
                
                BSession sessionPavo = new BSession(CIApplication.DEFAULT_APPLICATION_PAVO);
                getSession().connectSession(sessionPavo);
                CIApplication app = (CIApplication) sessionPavo.getApplication();

                AFAffiliation aff = null;
                if (JadeStringUtil.isBlankOrZero(getIdAffBeneficiaire())) {
                    aff = app.getAffilieByNo(getSession(), numAffilie, true, false, getDateDebut(), getDateFin(),
                            getAnnee(), getJourDebut(), getJourFin());
                } else {
                    aff = new AFAffiliation();
                    aff.setSession(getSession());
                    aff.setAffiliationId(getIdAffBeneficiaire());
                    aff.retrieve();

                    // On contrôle que l'affilié est bien actif durant la période
                    // concernée
                    String idAff = aff._retourIdAffiliation(statement.getTransaction(), aff.getAffilieNumero(),
                            getPeriodeDebut(), getPeriodeFin(), aff.getTypeAffiliation());

                    if (JadeStringUtil.isBlankOrZero(idAff)) {
                        aff = null;
                        idAffBeneficiaire = "";
                        setIdAffBeneficiaireBrut("");
                    }
                }

                if (aff == null || JadeStringUtil.isEmpty(aff.getId())) {
                    _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_PERIODE_AFF_ERREUR"));
                } else {
                    idAffBeneficiaire = aff.getId();
                    setIdAffBeneficiaireBrut(idAffBeneficiaire);
                    if (aff.isNew() == true) {
                        _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_PC_ERREUR"));
                    }
                    if (!(aff.getDeclarationSalaire().equals(CodeSystem.BENEFICIAIRE_PC))) {
                        _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_PC_ERREUR"));
                    }
                }
            }
            boolean validationOK = true;
            // Test validité des dates
            if (JadeStringUtil.isEmpty(getDateValeur())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_DATE_VALEUR_ERREUR"));
            }
            validationOK &= _checkDate(statement.getTransaction(), getDateValeur(),
                    getSession().getLabel("BENEFICIAIRE_DATE_VALEUR_ERREUR"));
            if (JadeStringUtil.isEmpty(getPeriodeDebut())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_DATE_DEBUT_ERREUR"));
            }
            validationOK &= _checkDate(statement.getTransaction(), getPeriodeDebut(),
                    getSession().getLabel("BENEFICIAIRE_DATE_DEBUT_ERREUR"));
            if (JadeStringUtil.isEmpty(getPeriodeFin())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_DATE_FIN_ERREUR"));
            }
            validationOK &= _checkDate(statement.getTransaction(), getPeriodeFin(),
                    getSession().getLabel("BENEFICIAIRE_DATE_FIN_ERREUR"));
            if (JadeStringUtil.isEmpty(getNumAvsAideMenage())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_AIDE_MENAGE_ERREUR"));
            }
            if (JadeStringUtil.isEmpty(getTotalVerse())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_TOTAL_VERSE_ERREUR"));
            }
            // On regarde que la période de fin est bien supérieure à la période
            // de début
            if (!BSessionUtil.compareDateFirstLower(getSession(), getPeriodeDebut(), getPeriodeFin())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_PERIODE_ERREUR"));
            }
            if (JadeStringUtil.isEmpty(getIdJournal())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_JOURNAL_ERREUR"));
            }
            // On va tester si la période est dans la même année civile
            JADate date1 = new JADate(getPeriodeDebut());
            JADate date2 = new JADate(getPeriodeFin());
            if (date1.getYear() != date2.getYear()) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_ANNEE_PERIODE_ERREUR"));
            }
            // On test si le numAvs fait partie d'un CI Ouvert
            CICompteIndividuelManager ci = new CICompteIndividuelManager();
            ci.setSession(getSession());
            // TODO unformat numAvs
            if (getNumAvsAideMenage().length() > 14) {
                ci.setForNumeroAvsNNSS(AFQuittance.unformat(getNumAvsAideMenage()));
            } else {
                ci.setForNumeroAvs(AFQuittance.unformat(getNumAvsAideMenage()));
            }
            ci.find();
            if (ci.size() <= 0) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_CI_ERREUR"));
            }
            // On contrôle que la période soit bien dans l'année.
            AFJournalQuittance jrnQuittance = new AFJournalQuittance();
            jrnQuittance.setSession(getSession());
            jrnQuittance.setIdJournalQuittance(getIdJournalQuittance());
            jrnQuittance.retrieve();
            int anneeJournal = Integer.valueOf(jrnQuittance.getAnnee()).intValue();
            if (date1.getYear() != anneeJournal) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_PERIODE_JRN_ERREUR"));
            }
            if (date2.getYear() != anneeJournal) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_PERIODE_JRN_ERREUR"));
            }

        }

    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MAQIDQ", this._dbWriteNumeric(statement.getTransaction(), getIdQuittance(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MAQIDQ", this._dbWriteNumeric(statement.getTransaction(), getIdQuittance(), ""));
        statement.writeField("MAQANN", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), ""));
        statement.writeField("MAQTIB", this._dbWriteNumeric(statement.getTransaction(), getIdAffBeneficiaire(), ""));
        statement.writeField("MAQTAM", this._dbWriteString(statement.getTransaction(), getNumAvsAideMenage(), ""));
        statement.writeField("MAQIJR", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), ""));
        statement.writeField("MAQPDE", this._dbWriteString(statement.getTransaction(), getPeriodeDebut(), ""));
        statement.writeField("MAQPFI", this._dbWriteString(statement.getTransaction(), getPeriodeFin(), ""));
        statement.writeField("MAQNAI", this._dbWriteString(statement.getTransaction(), getNomAide(), ""));
        statement.writeField("MAQPAI", this._dbWriteString(statement.getTransaction(), getPrenomAide(), ""));
        statement.writeField("MAQNHE", this._dbWriteNumeric(statement.getTransaction(), getNombreHeures(), ""));
        statement.writeField("MAQPHE", this._dbWriteNumeric(statement.getTransaction(), getPrixHeure(), ""));
        statement.writeField("MAQTVE", this._dbWriteNumeric(statement.getTransaction(), getTotalVerse(), ""));
        statement.writeField("MAQDVA", this._dbWriteString(statement.getTransaction(), getDateValeur(), ""));
        statement.writeField("MAQILO", this._dbWriteNumeric(statement.getTransaction(), getIdLocalite(), ""));
        statement.writeField("MAQTET", this._dbWriteString(statement.getTransaction(), getEtatQuittance(), ""));
        statement.writeField("MAQLOG", this._dbWriteString(statement.getTransaction(), getMessageErreur(), ""));
        statement.writeField("MABCIT", this._dbWriteBoolean(statement.getTransaction(), getCiTraite(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "ciTraite"));
        statement.writeField("MABCIM", this._dbWriteBoolean(statement.getTransaction(), getCiManuel(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "ciManuel"));
        statement.writeField("MAQMCI", this._dbWriteNumeric(statement.getTransaction(), getMontantCI(), "montantCI"));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public String getAdresseEmail() {
        try {
            return getSession().getUserEMail();
        } catch (Exception ex) {
            return "";
        }
    }

    public String getAnnee() {
        return annee;
    }

    public String getAvsNNSSDetailEcriture() {
        return getNumeroavsNNSS();
    }

    public Boolean getCiManuel() {
        return ciManuel;
    }

    public Boolean getCiTraite() {
        return ciTraite;
    }

    public String getDateDebut() {
        try {
            JADate date = new JADate(getPeriodeDebut());
            return String.valueOf(date.getMonth());
        } catch (JAException e) {
            return "";
        }
    }

    public String getJourDebut() {
        try {
            JADate date = new JADate(getPeriodeDebut());
            return String.valueOf(date.getDay());
        } catch (JAException e) {
            return "";
        }
    }

    public String getJourFin() {
        try {
            JADate date = new JADate(getPeriodeFin());
            return String.valueOf(date.getDay());
        } catch (JAException e) {
            return "";
        }
    }

    public String getDateFin() {
        try {
            JADate date = new JADate(getPeriodeFin());
            return String.valueOf(date.getMonth());
        } catch (JAException e) {
            return "";
        }
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getEtat() {
        return AFJournalQuittance.getEtatLibelle(getSession(), getIdJournal());
    }

    // *******************************************************
    // Getter and Setter
    // *******************************************************

    public String getEtatQuittance() {
        return etatQuittance;
    }

    public String getEtatQuittanceLibelle() {
        return getSession().getCodeLibelle(getEtatQuittance());
    }

    public String getIdAffBeneficiaire() {
        return idAffBeneficiaire;
    }

    public String getIdJournal() {
        return idJournalQuittance;
    }

    public String getIdJournalQuittance() {
        return idJournalQuittance;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public String getIdQuittance() {
        return idQuittance;
    }

    public String getIdTiers() {
        if (JadeStringUtil.isEmpty(idTiers)) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setId(getIdAffBeneficiaire());
            try {
                aff.retrieve();
                return aff.getIdTiers();
            } catch (Exception e) {
                return "";
            }
        } else {
            return idTiers;
        }
    }

    public Vector<String[]> getListeJournaux() {
        Vector<String[]> liste = new Vector<String[]>();
        try {
            AFJournalQuittanceManager manager = new AFJournalQuittanceManager();
            manager.setSession(getSession());
            // manager.setForEtat(CodeSystem.BENEFICIAIRE_ETAT_OUVERT);
            manager.setOrder("MAJQID DESC");
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                AFJournalQuittance journal = (AFJournalQuittance) manager.getEntity(i);
                liste.add(new String[] { journal.getIdJournalQuittance(),
                        journal.getIdJournalQuittance() + " - " + journal.getDescriptionJournal() });
            }
            return liste;
        } catch (Exception e) {
            return liste;
        }

    }

    public Vector<String[]> getListeJournauxPourImpression() {
        Vector<String[]> liste = getListeJournaux();
        liste.add(new String[] { "", "" });
        return liste;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public String getMessageErreurLibelle() {
        if (!JadeStringUtil.isEmpty(getMessageErreur())) {
            return getMessageErreur();
        } else {
            try {
                AFQuittanceManager quittanceManager = new AFQuittanceManager();
                quittanceManager.setSession(getSession());
                quittanceManager.setForIdJournalQuittance(getIdJournalQuittance());
                quittanceManager.setForAnnee(getAnnee());
                quittanceManager.setForIdTiersBeneficiaire(getIdAffBeneficiaire());
                quittanceManager.find();
                for (int i = 0; i < quittanceManager.size(); i++) {
                    AFQuittance quittance = (AFQuittance) quittanceManager.get(i);
                    if (!JadeStringUtil.isEmpty(quittance.getMessageErreur())) {
                        return quittance.getMessageErreur();
                    }
                }
            } catch (Exception ex) {
                return "";
            }

        }
        return "";
    }

    public String getMois() {
        return periodeDebut + " - " + periodeFin;
    }

    public String getMontantCI() {
        return montantCI;
    }

    public String getMontantEffectif() {
        return montantEffectif;
    }

    public int getNbreMois() {
        return nbreMois;
    }

    public Boolean getNnss() {
        return nnss;
    }

    public String getNomAide() {
        return nomAide;
    }

    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    public String getNomBenefJSP() {
        return getNomPrenomBeneficiaire();
    }

    public String getNombreHeures() {
        return nombreHeures;
    }

    public String getNomPrenom() {
        if ((JadeStringUtil.isEmpty(nomPrenom)) && (!JadeStringUtil.isEmpty(getNumAvsAideMenage()))) {
            try {
                CICompteIndividuelManager manager = new CICompteIndividuelManager();
                manager.setSession(getSession());
                if (getNumeroavsNNSS().equalsIgnoreCase("true")) {
                    manager.setForNumeroAvs(AFQuittance.unFormat(getNumAvsAideMenage()));
                } else {
                    manager.setForNumeroAvs(AFQuittance.unFormat(getNumAvsAideMenage()));
                }

                manager.find();
                CICompteIndividuel entity = (CICompteIndividuel) manager.getFirstEntity();
                return entity.getNomPrenom();
            } catch (Exception e) {
                return "";
            }
        } else {
            return nomPrenom;
        }
    }

    public String getNomPrenomAide() {
        return "TEST";
    }

    public String getNomPrenomBeneficiaire() {
        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(getIdTiers());
        try {
            tiers.retrieve();
            return tiers.getNomPrenom();
        } catch (Exception e) {
            return "";
        }
    }

    public String getNssFormate() {
        return NSUtil.formatAVSNew(getNumAvs(), getNnss().booleanValue());
    }

    public String getNumAffilie() {
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return this.getNumAffilieBeneficiaire();
        } else {
            return numAffilie;
        }
    }

    public String getNumAffilieBeneficiaire() {
        return this.getNumAffilieBeneficiaire(getSession());
    }

    public String getNumAffilieBeneficiaire(BSession session) {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(session);
        aff.setAffiliationId(getIdAffBeneficiaire());
        try {
            aff.retrieve();
            return aff.getAffilieNumero();
        } catch (Exception e) {
            return "";
        }
    }

    public String getNumAvs() {
        return numAvsAideMenage;
    }

    public String getNumAvsAideMenage() {
        return numAvsAideMenage;
    }

    public String getNumeroavsNNSS() {
        if (getNumAvsAideMenage().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getPartialNss() {
        return NSUtil.formatWithoutPrefixe(getNumAvs(), nnss.booleanValue());
    }

    public String getPaysForNNSS() {
        try {
            return getSession().getCode(paysOrigineId) + " - " + getSession().getCodeLibelle(paysOrigineId);
        } catch (Exception e) {
            return "";
        }
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public String getPrenomAide() {
        return prenomAide;
    }

    public String getPrenomBeneficiaire() {
        return prenomBeneficiaire;
    }

    public String getPrixHeure() {
        return prixHeure;
    }

    public String getSexe() {
        return sexe;
    }

    public String getSexeForNNSS() {
        try {
            return getSession().getCodeLibelle(getSexe());
        } catch (Exception e) {
            return "";
        }
    }

    public String getTotal() {
        if (total == null) {
            return getTotalVerse();
        } else {
            return total;
        }
    }

    public String getTotalVerse() {
        return totalVerse;
    }

    public String getUpdateTag() {
        return updateTag;
    }

    public String getUser() {
        try {
            return getSession().getUserId();
        } catch (Exception ex) {
            return "";
        }
    }

    public String isNNSS() {
        if (JadeStringUtil.isNull(getNumAvs())) {
            return "false";
        } else {
            if (getNumAvsAideMenage().length() > 14) {
                return "true";
            } else {
                return "false";
            }
        }
    }

    public boolean isRentier() {
        return isRentier;
    }

    public boolean isUpdateFromProcess() {
        return isUpdateFromProcess;
    }

    public Boolean isWantValidate() {
        return wantValidate;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCiManuel(Boolean ciManuel) {
        this.ciManuel = ciManuel;
    }

    public void setCiTraite(Boolean ciTraite) {
        this.ciTraite = ciTraite;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setEtat(String etat) {
    }

    public void setEtatQuittance(String etatQuittance) {
        this.etatQuittance = etatQuittance;
    }

    public void setIdAffBeneficiaire(String numAffilie) {

        this.numAffilie = numAffilie;
        try {
            BSession sessionPavo = new BSession(CIApplication.DEFAULT_APPLICATION_PAVO);
            getSession().connectSession(sessionPavo);
            CIApplication app = (CIApplication) sessionPavo.getApplication();
            if (JadeStringUtil.isEmpty(getAnnee())) {
                JADate date = new JADate(getPeriodeDebut());
                setAnnee(String.valueOf(date.getYear()));
            }
            AFAffiliation aff = app.getAffilieByNo(getSession(), numAffilie, true, false, getDateDebut(), getDateFin(),
                    getAnnee(), getJourDebut(), getJourFin());
            idAffBeneficiaire = aff.getAffiliationId();
        } catch (Exception e) {
            idAffBeneficiaire = "";
        }
    }

    public void setIdAffBeneficiaireBrut(String idAffBeneficiaire) {
        this.idAffBeneficiaire = idAffBeneficiaire;
    }

    public void setIdJournal(String idJournal) {
        idJournalQuittance = idJournal;
    }

    public void setIdJournalQuittance(String idJournalQuittance) {
        this.idJournalQuittance = idJournalQuittance;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    public void setIdQuittance(String idQuittance) {
        this.idQuittance = idQuittance;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public void setMontantCI(String montantCI) {
        this.montantCI = montantCI;
    }

    public void setMontantEffectif(String montantEffectif) {
        this.montantEffectif = montantEffectif;
    }

    public void setNbreMois(int nbreMois) {
        this.nbreMois = nbreMois;
    }

    public void setNnss(Boolean nnss) {
        this.nnss = nnss;
    }

    public void setNomAide(String nomAide) {
        this.nomAide = nomAide;
    }

    public void setNomBeneficiaire(String nomBeneficiaire) {
        this.nomBeneficiaire = nomBeneficiaire;
    }

    public void setNombreHeures(String nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumAffilieBeneficiaire(String numAffilieBeneficiaire) {
    }

    public void setNumAvsAideMenage(String idTiersAideMenagere) {
        numAvsAideMenage = idTiersAideMenagere;
    }

    public void setNumeroavsNNSS(String numeroavsNNSS) {
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public void setPrenomAide(String prenomAide) {
        this.prenomAide = prenomAide;
    }

    public void setPrenomBeneficiaire(String prenomBeneficiaire) {
        this.prenomBeneficiaire = prenomBeneficiaire;
    }

    public void setPrixHeure(String prixHeure) {
        this.prixHeure = prixHeure;
    }

    public void setRentier(boolean isRentier) {
        this.isRentier = isRentier;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTotalVerse(String totalVerse) {
        this.totalVerse = totalVerse;
    }

    public void setUpdateFromProcess(boolean isUpdateFromProcess) {
        this.isUpdateFromProcess = isUpdateFromProcess;
    }

    public void setUpdateTag(String updateTag) {
        this.updateTag = updateTag;
    }

    public void wantValidate(Boolean wantValidate) {
        this.wantValidate = wantValidate;
    }

}
