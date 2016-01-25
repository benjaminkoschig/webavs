/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.db.decisions;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFDecision extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANNEE_QD = "EBDAQD";
    public static final String FIELDNAME_BORDEREAU_ACCOMPAGNEMENT = "EBBBAC";
    public static final String FIELDNAME_BULLETIN_VERSEMENT_RETOUR = "EBBBVR";
    public static final String FIELDNAME_DATE_DEBUT_RETRO = "EBDDRE";
    public static final String FIELDNAME_DATE_DERNIER_PAIEMENT = "EBDDPR";
    public static final String FIELDNAME_DATE_FIN_RETRO = "EBDFRE";
    public static final String FIELDNAME_DATE_PREPARATION = "EBDPRE";
    public static final String FIELDNAME_DATE_SUR_DOCUMENT = "EBDSDO";
    public static final String FIELDNAME_DATE_VALIDATION = "EBDVAL";
    public static final String FIELDNAME_DECOMPTE_FACTURE_RETOUR = "EBBDFR";
    public static final String FIELDNAME_ETAT_DECISION = "EBTETA";
    public static final String FIELDNAME_GENRE_DECISION = "EBTGDE";
    public static final String FIELDNAME_ID_ADRESSE_DOMICILE = "EBIADD";
    public static final String FIELDNAME_ID_ADRESSE_PAIEMENT = "EBIADP";
    public static final String FIELDNAME_ID_DECISION = "EBIDEC";
    public static final String FIELDNAME_ID_EXECUTION_PROCESS = "EBIEPR";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "EBIGES";
    public static final String FIELDNAME_ID_PREPARE_PAR = "EBIPRP";
    public static final String FIELDNAME_ID_QD_PRINICIPALE = "EBIQDP";
    public static final String FIELDNAME_ID_VALIDE_PAR = "EBIVAP";
    public static final String FIELDNAME_INCITATION_DEPOT_NOUVELLE_DEMANDE = "EBBIDD";
    public static final String FIELDNAME_MONTANT_A_REMBOURSER_DSAS = "EBMARD";
    public static final String FIELDNAME_MONTANT_COURANT_PARTIE_FUTURE = "EBMCPF";
    public static final String FIELDNAME_MONTANT_COURANT_PARTIE_RETROACTIVE = "EBMCPR";
    public static final String FIELDNAME_MONTANT_DEPASSEMENT_QD = "EBMDQD";
    public static final String FIELDNAME_MONTANT_EXCEDENT_DE_RECETTE_PC = "EBMERP";
    public static final String FIELDNAME_MONTANT_TOTAL_RFM = "EBMRFM";
    public static final String FIELDNAME_NUMERO_DECISION = "EBNNUM";
    public static final String FIELDNAME_RETOUR_BV = "EBBRBV";
    public static final String FIELDNAME_TEXTE_ANNEXE = "EBLTAN";
    public static final String FIELDNAME_TEXTE_REMARQUE = "EBLTRE";
    public static final String FIELDNAME_TYPE_PAIEMENT = "EBTTPA";

    public static final String TABLE_NAME = "RFDECIS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final RFDecision loadDecision(BSession session, BITransaction transaction, String idDecision)
            throws Exception {
        RFDecision retValue;

        retValue = new RFDecision();
        retValue.setIdDecision(idDecision);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String anneeQD = "";
    private Boolean bordereauAccompagnement = Boolean.TRUE;
    private Boolean bulletinVersementRetour = Boolean.FALSE;
    private String dateDebutRetro = "";
    private String dateDernierPaiement = "";
    private String dateFinRetro = "";
    private String datePreparation = "";
    private String dateSurDocument = "";
    private String dateValidation = "";
    private Boolean decompteFactureRetour = Boolean.FALSE;
    private String etatDecision = "";
    private String genreDecision = "";
    private String idAdresseDomicile = "";
    private String idAdressePaiement = "";
    private String idDecision = "";
    private String idExecutionProcess = "";
    private String idGestionnaire = "";
    private String idPreparePar = "";
    private String idQdPrincipale = "";
    private String idValidePar = "";
    private Boolean incitationDepotNouvelleDemande = Boolean.FALSE;
    private String montantARembourserParLeDsas = "";
    private String montantCourantPartieFuture = "";
    private String montantCourantPartieRetroactive = "";
    private String montantDepassementQd = "";
    private String montantExcedentDeRecette = "";
    private String montantTotalRFM = "";
    private String numeroDecision = "";
    private Boolean retourBV = Boolean.FALSE;

    private String texteAnnexe = "";
    private String texteRemarque = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String typePaiement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDecision.
     */
    public RFDecision() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecision(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des décisions
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDecision.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des décisions
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric(RFDecision.FIELDNAME_ID_DECISION);
        numeroDecision = statement.dbReadString(RFDecision.FIELDNAME_NUMERO_DECISION);
        anneeQD = statement.dbReadNumeric(RFDecision.FIELDNAME_ANNEE_QD);
        idAdressePaiement = statement.dbReadNumeric(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT);
        idAdresseDomicile = statement.dbReadNumeric(RFDecision.FIELDNAME_ID_ADRESSE_DOMICILE);
        idValidePar = statement.dbReadString(RFDecision.FIELDNAME_ID_VALIDE_PAR);
        dateValidation = statement.dbReadDateAMJ(RFDecision.FIELDNAME_DATE_VALIDATION);
        dateSurDocument = statement.dbReadDateAMJ(RFDecision.FIELDNAME_DATE_SUR_DOCUMENT);
        idPreparePar = statement.dbReadString(RFDecision.FIELDNAME_ID_PREPARE_PAR);
        datePreparation = statement.dbReadDateAMJ(RFDecision.FIELDNAME_DATE_PREPARATION);
        etatDecision = statement.dbReadNumeric(RFDecision.FIELDNAME_ETAT_DECISION);
        idGestionnaire = statement.dbReadString(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
        texteRemarque = statement.dbReadString(RFDecision.FIELDNAME_TEXTE_REMARQUE);
        texteAnnexe = statement.dbReadString(RFDecision.FIELDNAME_TEXTE_ANNEXE);
        genreDecision = statement.dbReadNumeric(RFDecision.FIELDNAME_GENRE_DECISION);
        incitationDepotNouvelleDemande = statement
                .dbReadBoolean(RFDecision.FIELDNAME_INCITATION_DEPOT_NOUVELLE_DEMANDE);
        retourBV = statement.dbReadBoolean(RFDecision.FIELDNAME_RETOUR_BV);
        decompteFactureRetour = statement.dbReadBoolean(RFDecision.FIELDNAME_DECOMPTE_FACTURE_RETOUR);
        bulletinVersementRetour = statement.dbReadBoolean(RFDecision.FIELDNAME_BULLETIN_VERSEMENT_RETOUR);
        bordereauAccompagnement = statement.dbReadBoolean(RFDecision.FIELDNAME_BORDEREAU_ACCOMPAGNEMENT);
        montantCourantPartieFuture = statement.dbReadNumeric(RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_FUTURE, 2);
        montantCourantPartieRetroactive = statement.dbReadNumeric(
                RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_RETROACTIVE, 2);
        montantDepassementQd = statement.dbReadNumeric(RFDecision.FIELDNAME_MONTANT_DEPASSEMENT_QD, 2);
        montantExcedentDeRecette = statement.dbReadNumeric(RFDecision.FIELDNAME_MONTANT_EXCEDENT_DE_RECETTE_PC, 2);
        montantTotalRFM = statement.dbReadNumeric(RFDecision.FIELDNAME_MONTANT_TOTAL_RFM, 2);
        montantARembourserParLeDsas = statement.dbReadNumeric(RFDecision.FIELDNAME_MONTANT_A_REMBOURSER_DSAS, 2);
        idQdPrincipale = statement.dbReadNumeric(RFDecision.FIELDNAME_ID_QD_PRINICIPALE);
        typePaiement = statement.dbReadNumeric(RFDecision.FIELDNAME_TYPE_PAIEMENT);
        dateDebutRetro = statement.dbReadDateAMJ(RFDecision.FIELDNAME_DATE_DEBUT_RETRO);
        dateFinRetro = statement.dbReadDateAMJ(RFDecision.FIELDNAME_DATE_FIN_RETRO);
        dateDernierPaiement = statement.dbReadDateAMJ(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);
        idExecutionProcess = statement.dbReadNumeric(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des décisions
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDecision.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));

    }

    /**
     * Méthode d'écriture des champs dans la table des décisions
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFDecision.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(RFDecision.FIELDNAME_NUMERO_DECISION,
                this._dbWriteString(statement.getTransaction(), numeroDecision, "numeroDecision"));
        statement.writeField(RFDecision.FIELDNAME_ANNEE_QD,
                this._dbWriteDateYear(statement.getTransaction(), anneeQD, "anneeQD"));
        statement.writeField(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAdressePaiement, "idAdressePaiement"));
        statement.writeField(RFDecision.FIELDNAME_ID_ADRESSE_DOMICILE,
                this._dbWriteNumeric(statement.getTransaction(), idAdresseDomicile, "idAdresseDomicile"));
        statement.writeField(RFDecision.FIELDNAME_ID_VALIDE_PAR,
                this._dbWriteString(statement.getTransaction(), idValidePar, "idValidePar"));
        statement.writeField(RFDecision.FIELDNAME_DATE_VALIDATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateValidation, "dateValidation"));
        statement.writeField(RFDecision.FIELDNAME_DATE_SUR_DOCUMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateSurDocument, "dateSurDocument"));
        statement.writeField(RFDecision.FIELDNAME_ID_PREPARE_PAR,
                this._dbWriteString(statement.getTransaction(), idPreparePar, "idPreparePar"));
        statement.writeField(RFDecision.FIELDNAME_DATE_PREPARATION,
                this._dbWriteDateAMJ(statement.getTransaction(), datePreparation, "datePreparation"));
        statement.writeField(RFDecision.FIELDNAME_ETAT_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), etatDecision, "etatDecision"));
        statement.writeField(RFDecision.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(RFDecision.FIELDNAME_TEXTE_REMARQUE,
                this._dbWriteString(statement.getTransaction(), texteRemarque, "texteRemarque"));
        statement.writeField(RFDecision.FIELDNAME_TEXTE_ANNEXE,
                this._dbWriteString(statement.getTransaction(), texteAnnexe, "texteAnnexe"));
        statement.writeField(RFDecision.FIELDNAME_GENRE_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), genreDecision, "genreDecision"));
        statement.writeField(RFDecision.FIELDNAME_INCITATION_DEPOT_NOUVELLE_DEMANDE, this._dbWriteBoolean(
                statement.getTransaction(), incitationDepotNouvelleDemande, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "incitationDepotNouvelleDemande"));
        statement
                .writeField(RFDecision.FIELDNAME_RETOUR_BV, this._dbWriteBoolean(statement.getTransaction(), retourBV,
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "retourBV"));
        statement.writeField(RFDecision.FIELDNAME_DECOMPTE_FACTURE_RETOUR, this._dbWriteBoolean(
                statement.getTransaction(), decompteFactureRetour, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "decompteFactureRetour"));
        statement.writeField(RFDecision.FIELDNAME_BULLETIN_VERSEMENT_RETOUR, this._dbWriteBoolean(
                statement.getTransaction(), bulletinVersementRetour, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "bulletinVersementRetour"));
        statement.writeField(RFDecision.FIELDNAME_BORDEREAU_ACCOMPAGNEMENT, this._dbWriteBoolean(
                statement.getTransaction(), bordereauAccompagnement, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "bordereauAccompagnement"));
        statement.writeField(RFDecision.FIELDNAME_MONTANT_DEPASSEMENT_QD,
                this._dbWriteNumeric(statement.getTransaction(), montantDepassementQd, "montantDepassementQd"));
        statement.writeField(RFDecision.FIELDNAME_MONTANT_EXCEDENT_DE_RECETTE_PC,
                this._dbWriteNumeric(statement.getTransaction(), montantExcedentDeRecette, "montantExcedentDeRecette"));
        statement.writeField(RFDecision.FIELDNAME_MONTANT_TOTAL_RFM,
                this._dbWriteNumeric(statement.getTransaction(), montantTotalRFM, "montantTotalRFM"));
        statement.writeField(RFDecision.FIELDNAME_MONTANT_A_REMBOURSER_DSAS, this._dbWriteNumeric(
                statement.getTransaction(), montantARembourserParLeDsas, "montantARembourserParLeDsas"));
        statement.writeField(RFDecision.FIELDNAME_ID_QD_PRINICIPALE,
                this._dbWriteNumeric(statement.getTransaction(), idQdPrincipale, "idQdPrincipale"));
        statement.writeField(RFDecision.FIELDNAME_TYPE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), typePaiement, "typePaiement"));
        statement.writeField(RFDecision.FIELDNAME_DATE_DEBUT_RETRO,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutRetro, "dateDebutRetro"));
        statement.writeField(RFDecision.FIELDNAME_DATE_FIN_RETRO,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinRetro, "dateFinRetro"));
        statement.writeField(RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_FUTURE, this._dbWriteNumeric(
                statement.getTransaction(), montantCourantPartieFuture, "montantCourantPartieFuture"));
        statement.writeField(RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_RETROACTIVE, this._dbWriteNumeric(
                statement.getTransaction(), montantCourantPartieRetroactive, "montantCourantPartieRetroactive"));
        statement.writeField(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDernierPaiement, "dateDernierPaiement"));
        statement.writeField(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS,
                this._dbWriteNumeric(statement.getTransaction(), idExecutionProcess, "idExecutionProcess"));

    }

    public String getAnneeQD() {
        return anneeQD;
    }

    public Boolean getBordereauAccompagnement() {
        return bordereauAccompagnement;
    }

    public Boolean getBulletinVersementRetour() {
        return bulletinVersementRetour;
    }

    public String getDateDebutRetro() {
        return dateDebutRetro;
    }

    public String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateFinRetro() {
        return dateFinRetro;
    }

    public String getDatePreparation() {
        return datePreparation;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    public Boolean getDecompteFactureRetour() {
        return decompteFactureRetour;
    }

    public String getEtatDecision() {
        return etatDecision;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public String getIdAdresseDomicile() {
        return idAdresseDomicile;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPreparePar() {
        return idPreparePar;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdValidePar() {
        return idValidePar;
    }

    public Boolean getIncitationDepotNouvelleDemande() {
        return incitationDepotNouvelleDemande;
    }

    public String getMontantARembourserParLeDsas() {
        return montantARembourserParLeDsas;
    }

    public String getMontantCourantPartieFuture() {
        return montantCourantPartieFuture;
    }

    public String getMontantCourantPartieRetroactive() {
        return montantCourantPartieRetroactive;
    }

    public String getMontantDepassementQd() {
        return montantDepassementQd;
    }

    public String getMontantExcedentDeRecette() {
        return montantExcedentDeRecette;
    }

    public String getMontantTotalRFM() {
        return montantTotalRFM;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public Boolean getRetourBV() {
        if (retourBV) {
            bulletinVersementRetour = retourBV;
        }
        return retourBV;
    }

    public String getTexteAnnexe() {
        return texteAnnexe;
    }

    public String getTexteRemarque() {
        return texteRemarque;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setAnneeQD(String anneeQD) {
        this.anneeQD = anneeQD;
    }

    public void setBordereauAccompagnement(Boolean bordereauAccompagnement) {
        this.bordereauAccompagnement = bordereauAccompagnement;
    }

    public void setBulletinVersementRetour(Boolean bulletinVersementRetour) {
        this.bulletinVersementRetour = bulletinVersementRetour;
    }

    public void setDateDebutRetro(String dateDebutRetro) {
        this.dateDebutRetro = dateDebutRetro;
    }

    public void setDateDernierPaiement(String dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public void setDateFinRetro(String dateFinRetro) {
        this.dateFinRetro = dateFinRetro;
    }

    public void setDatePreparation(String datePreparation) {
        this.datePreparation = datePreparation;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    public void setDecompteFactureRetour(Boolean decompteFactureRetour) {
        this.decompteFactureRetour = decompteFactureRetour;
    }

    public void setEtatDecision(String etatDecision) {
        this.etatDecision = etatDecision;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setIdAdresseDomicile(String idAdresseDomicile) {
        this.idAdresseDomicile = idAdresseDomicile;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPreparePar(String idPreparePar) {
        this.idPreparePar = idPreparePar;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdValidePar(String idValidePar) {
        this.idValidePar = idValidePar;
    }

    public void setIncitationDepotNouvelleDemande(Boolean incitationDepotNouvelleDemande) {
        this.incitationDepotNouvelleDemande = incitationDepotNouvelleDemande;
    }

    public void setMontantARembourserParLeDsas(String montantARembourserParLeDsas) {
        this.montantARembourserParLeDsas = montantARembourserParLeDsas;
    }

    public void setMontantCourantPartieFuture(String montantCourantPartieFuture) {
        this.montantCourantPartieFuture = montantCourantPartieFuture;
    }

    public void setMontantCourantPartieRetroactive(String montantCourantPartieRetroactive) {
        this.montantCourantPartieRetroactive = montantCourantPartieRetroactive;
    }

    public void setMontantDepassementQd(String montantDepassementQd) {
        this.montantDepassementQd = montantDepassementQd;
    }

    public void setMontantExcedentDeRecette(String montantExcedentDeRecette) {
        this.montantExcedentDeRecette = montantExcedentDeRecette;
    }

    public void setMontantTotalRFM(String montantTotalRFM) {
        this.montantTotalRFM = montantTotalRFM;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setRetourBV(Boolean retourBV) {
        this.retourBV = retourBV;
    }

    public void setTexteAnnexe(String texteAnnexe) {
        this.texteAnnexe = texteAnnexe;
    }

    public void setTexteRemarque(String texteRemarque) {
        this.texteRemarque = texteRemarque;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

}