package globaz.cygnus.services.comptabilite;

import globaz.corvus.db.lots.RELot;
import globaz.cygnus.api.paiement.IRFLot;
import globaz.cygnus.exceptions.RFBusinessException;
import globaz.cygnus.utils.RFLogToDB;
import globaz.cygnus.vb.paiement.IRFModuleComptable;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BIMessageLog;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author fha
 * @author vch
 */
public class RFComptabiliserDecisionService {

    private APIGestionComptabiliteExterne compta = null;
    private String dateComptable = "";
    private String dateEcheancePaiement;
    private String descriptionLot;
    private String email = "";
    private String idJournalCA = "";
    private String idLot = "";
    private String idOrganeExecution;
    private boolean isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome = false;

    private final String LABEL_DECISION_RFM = "Décision RFM";
    private RELot lot = null;
    private String mailBody = "";
    private String mailObject = "";
    private int numeroDecision;
    private String numeroOG = "";
    private int numeroPDF;

    // SEPA iso20002
    private String isoCsTypeAvis;
    private String isoGestionnaire;
    private String isoHighPriority;

    // private RFPrestationData prestation = null;
    private Set<RFPrestationData> prestationsSet = null;
    private BISession session = null;
    private BITransaction transaction;

    private String typePaiement = "";

    // Préparation de l'OG
    private void doPreparerOG(String idOG, String numeroOG, String dateEcheancePaiement, String description)
            throws Exception {
        String libelleOG = description;
        if (compta != null) {
            int n = Integer.parseInt(numeroOG);
            if (n < 10) {
                libelleOG = "RFM - Pmt journalier/hebdo. 0" + n + "-" + libelleOG;
            } else {
                libelleOG = "RFM - Pmt journalier/hebdo. " + n + "-" + libelleOG;
            }
            compta.preparerOrdreGroupe(idOG, String.valueOf(n), dateEcheancePaiement, CAOrdreGroupe.VERSEMENT,
                    CAOrdreGroupe.NATURE_RENTES_AVS_AI, libelleOG, isoCsTypeAvis, isoGestionnaire, isoHighPriority);
        }
    }

    private void doTraitement(List<RFPrestationData> prestationsMemeAdresseDePaiementList, FWMemoryLog memoryLog,
            RFLogToDB rfmLogger, boolean isLotAVASAD) throws Exception {

        IRFModuleComptable mc = null;
        mc = initInstanceVariables(compta, (BSession) getSession(), (BTransaction) getTransaction(),
                getDateComptable(), new StringBuffer(idLot));

        FWMemoryLog doTraitementMemoryLog = (FWMemoryLog) mc.doTraitement(this, prestationsMemeAdresseDePaiementList,
                rfmLogger, isLotAVASAD);
        memoryLog.logMessage(doTraitementMemoryLog);

        if (memoryLog.hasErrors()) {
            throw new Exception("Erreur(s) lors du traitement de la comptabilisation : " + memoryLog.toString());
        }

    }

    public APIGestionComptabiliteExterne getCompta() {
        return compta;
    }

    public String getDateComptable() {
        return dateComptable;
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    public String getDescriptionLot() {
        return descriptionLot;
    }

    public String getEmail() {
        return email;
    }

    public String getIdJournalCA() {
        return idJournalCA;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public RELot getLot() {
        return lot;
    }

    public String getMailBody() {
        return mailBody;
    }

    public String getMailObject() {
        return mailObject;
    }

    public int getNumeroDecision() {
        return numeroDecision;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public int getNumeroPDF() {
        return numeroPDF;
    }

    public Set<RFPrestationData> getPrestationsSet() {
        return prestationsSet;
    }

    public BISession getSession() {
        return session;
    }

    public BITransaction getTransaction() {
        return transaction;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    private BIMessageLog initCompta(BTransaction transactionCygnus) throws Exception {

        compta = (APIGestionComptabiliteExterne) getSession().getAPIFor(APIGestionComptabiliteExterne.class);
        compta.setDateValeur(dateComptable);
        compta.setEMailAddress(getEmail());
        compta.setSendCompletionMail(false);
        compta.setTransaction(transactionCygnus);

        if (lot != null) {
            compta.setLibelle(lot.getDescription());
        } else {
            compta.setLibelle(LABEL_DECISION_RFM + " - " + dateComptable);
        }

        // initialisation des rubriques
        RFModuleComptableFactory factory = RFModuleComptableFactory.getInstance();
        factory.initIdsRubriques(getSession(), isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome());

        // important pour qu'on ne pointe pas sur le memoryLog de la compta!
        FWMemoryLog initComptaMemoryLog = new FWMemoryLog();
        initComptaMemoryLog.logMessage((FWMemoryLog) compta.getMessageLog());
        return initComptaMemoryLog;
    }

    private RFModCpt_Normal initInstanceVariables(APIGestionComptabiliteExterne compta, BSession session,
            BTransaction transaction, String dateEcheance, StringBuffer idLot) throws Exception {
        RFModCpt_Normal traiterOVs = new RFModCpt_Normal(false);

        traiterOVs.setCompta(compta);
        traiterOVs.setSession(session);
        traiterOVs.setTransaction(transaction);
        traiterOVs.setDateComptable(dateComptable);
        traiterOVs.setIdLot(idLot);

        return traiterOVs;
    }

    public boolean isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome() {
        return isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome;
    }

    public void lancerComptabilisation(FWMemoryLog memoryLog) throws Exception {

        Date d1 = new Date();

        // initialiser la compta
        memoryLog.logMessage((FWMemoryLog) initCompta((BTransaction) getTransaction()));
        memoryLog.setSession((BSession) getSession());

        // si erreur en compta : aucune génération
        if (memoryLog.hasErrors()) {
            throw new Exception("Erreur(s) lors de l'initialisation de la comptabilisation : " + memoryLog.toString());
        }

        List<RFPrestationData> prestationsMemeAdresseDePaiementList = new ArrayList<RFPrestationData>();
        String idAdresseDePaiement = "";
        String idTiersRequrant = "";
        // On détermine le type du lot, on veut savoir si c'est de l'AVASAD.
        RELot myLot = new RELot();
        BSession bSession = (BSession) getSession();
        myLot.setSession(bSession);
        myLot.setIdLot(idLot);
        if (myLot.getSession().hasErrors()) {
            JadeLogger.warn(this, "session has errors: " + myLot.getSession().getErrors().toString());
        }
        myLot.retrieve();
        if (myLot.isNew()) {
            // lot pas lu depuis la DB... on explose
            String message = MessageFormat.format(bSession.getLabel("RF_PROCESS_LOT_COMPTABILISER_LOT_PAS_EN_DB"),
                    idLot);
            if (myLot.getSession().hasErrors()) {
                JadeLogger.warn(this, "session has errors: " + myLot.getSession().getErrors().toString());
            }
            JadeLogger.error(this, message);
            throw new RFBusinessException(message);
        }
        String typeLot = myLot.getCsTypeLot();
        boolean isLotAVASAD = IRFLot.CS_TYP_LOT_AVASAD.equals(typeLot);
        // création du logger. Le FWLot en DB n'est créé que lors du premier ajout
        RFLogToDB rfmLogger = new RFLogToDB((BSession) getSession());
        if (isLotAVASAD) {
            rfmLogger.logInfoToDB("Type du lot #" + idLot + ": " + typeLot + "(AVASAD:" + isLotAVASAD + ")",
                    "RFComptabiliserDecisionService.lancerComptabilisation.");
        }
        // boucle pour vérifier que chaque prestation a une adresse de paiement valable
        BTransaction bTransaction = (BTransaction) getTransaction();
        String dateComptable = getDateComptable();
        {
            StringBuilder errorsInAdresses = new StringBuilder();
            for (RFPrestationData prestation : prestationsSet) {
                Set<RFOrdreVersementData> ovs = prestation.getOrdresVersement();
                for (RFOrdreVersementData ov : ovs) {
                    BigDecimal montantOV = new BigDecimal(ov.getMontantOrdreVersement());
                    if (montantOV.compareTo(BigDecimal.ZERO) == 1) {
                        String idTiersAdressePaiement = ov.getIdTiersAdressePaiement();
                        TIAdressePaiementData adressepaiement = PRTiersHelper.getAdressePaiementData(bSession,
                                bTransaction, idTiersAdressePaiement,
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", dateComptable);
                        if (adressepaiement.isNew()) {
                            errorsInAdresses.append(MessageFormat.format(bSession
                                    .getLabel("RF_PROCESS_LOT_COMPTABILISER_ADRESSE_PAIEMENT_PAS_BONNE_UNITAIRE"), ov
                                    .getNssTiers(), ov.getNomTiers(), ov.getPrenomTiers(),
                                    prestation.getIdPrestation(), prestation.getMontantPrestation(), dateComptable));
                        }
                    }
                }
            }
            if (errorsInAdresses.length() > 0) {
                String message = MessageFormat.format(
                        bSession.getLabel("RF_PROCESS_LOT_COMPTABILISER_ADRESSE_PAIEMENT_PAS_BONNE_EXCEPTION"), idLot)
                        + errorsInAdresses.toString();

                throw new RFBusinessException(message);
            }
        }

        idJournalCA = compta.createJournal().getIdJournal();

        for (Iterator<RFPrestationData> it = prestationsSet.iterator(); it.hasNext();) {
            RFPrestationData prestation = it.next();
            String idTiersRequrantPrestationCourante = prestation.getIdTiersBeneficiaire();

            // On regroupe les prestations par adresse de paiement et par requérant
            if (((JadeStringUtil.isEmpty(idAdresseDePaiement) && JadeStringUtil.isEmpty(idTiersRequrant)) || (idAdresseDePaiement
                    .equals(prestation.getIdAdresseDePaiement()) && idTiersRequrant
                    .equals(idTiersRequrantPrestationCourante)))
                    && it.hasNext()) {

                idAdresseDePaiement = prestation.getIdAdresseDePaiement();
                idTiersRequrant = idTiersRequrantPrestationCourante;

                prestationsMemeAdresseDePaiementList.add(prestation);

                if (!it.hasNext()) {
                    doTraitement(prestationsMemeAdresseDePaiementList, memoryLog, rfmLogger, isLotAVASAD);
                }

            } else {

                doTraitement(prestationsMemeAdresseDePaiementList, memoryLog, rfmLogger, isLotAVASAD);

                idAdresseDePaiement = prestation.getIdAdresseDePaiement();
                idTiersRequrant = ((RFOrdreVersementData) prestation.getOrdresVersement().toArray()[0]).getIdTiers();
                prestationsMemeAdresseDePaiementList = new ArrayList<RFPrestationData>();
                prestationsMemeAdresseDePaiementList.add(prestation);

                if (!it.hasNext()) {
                    doTraitement(prestationsMemeAdresseDePaiementList, memoryLog, rfmLogger, isLotAVASAD);
                }
            }
        }

        /*****/

        if (memoryLog.hasErrors()) {
            throw new Exception("Erreur(s) lors de la comptabilisation : " + memoryLog.toString());
        }

        // passage en compta
        if (compta != null) {
            compta.comptabiliser();
            // ordres groupés
            doPreparerOG(getIdOrganeExecution(), getNumeroOG(), getDateEcheancePaiement(), JACalendar.today()
                    .toString());
            if (compta.getMessageLog().hasErrors()) {
                memoryLog.logMessage((FWMemoryLog) compta.getMessageLog());
            }
        } else {
            throw new Exception("Erreur(s) comptabilité non initialisé : " + memoryLog.toString());
        }

        System.out.println(" Traitement de la compta " + (new Date().getTime() - d1.getTime()) + "\n");
        if (isLotAVASAD) {
            rfmLogger.logInfoToDB("Lot AVASAD " + idLot + " terminé.",
                    "RFComptabiliserDecisionService.lancerComptabilisation");
        }
    }

    /*
     * private String rechercheBeneficiairePrestation(RFPrestationData prestation) throws Exception {
     * 
     * for (RFOrdreVersementData ordreVersement : prestation.getOrdresVersement()) { if
     * (!ordreVersement.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) { return
     * ordreVersement.getIdTiers(); } }
     * 
     * // La prestation ne contient que des restitutions, pour connaître le bénéficiaire on remonte aux autres //
     * prestations de la décision for (Iterator<RFPrestationData> it = this.prestationsSet.iterator(); it.hasNext();) {
     * 
     * RFPrestationData prestationDecision = it.next();
     * 
     * if (prestationDecision.getIdDecision().equals(prestation.getIdDecision()) &&
     * !prestationDecision.getIdPrestation().equals(prestation.getIdPrestation())) { for (RFOrdreVersementData
     * ordreVersement : prestationDecision.getOrdresVersement()) { if
     * (!ordreVersement.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) { return
     * ordreVersement.getIdTiers(); } } } }
     * 
     * // La prestation concerne uniquement une restitution, on remonte à la table association dossier-décision pour //
     * connaître le tiers bénéficiaire // TODO: Créer un nouveau Manager RFDecisionJointTiersManager rfAssDosMgr = new
     * RFDecisionJointTiersManager(); rfAssDosMgr.setSession((BSession) this.getSession());
     * rfAssDosMgr.setForIdDecision(prestation.getIdDecision()); rfAssDosMgr.find();
     * 
     * if (rfAssDosMgr.size() == 1) { return ((RFDecisionJointTiers) rfAssDosMgr.getFirstEntity()).getIdTiers(); } else
     * { throw new Exception(
     * "RFComptabiliserDecisionService.rechercheBeneficiairePrestation(): impossible de retrouver le bénéficiaire de la prestation"
     * ); }
     * 
     * }
     */

    public void setAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome(
            boolean isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome) {
        this.isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome = isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome;
    }

    public void setCompta(APIGestionComptabiliteExterne compta) {
        this.compta = compta;
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdJournalCA(String idJournalCA) {
        this.idJournalCA = idJournalCA;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setLot(RELot lot) {
        this.lot = lot;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public void setMailObject(String mailObject) {
        this.mailObject = mailObject;
    }

    public void setNumeroDecision(int numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public void setNumeroPDF(int numeroPDF) {
        this.numeroPDF = numeroPDF;
    }

    public void setPrestationsSet(Set<RFPrestationData> prestationsSet) {
        this.prestationsSet = prestationsSet;
    }

    public void setSession(BISession session) {
        this.session = session;
    }

    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHighPriority() {
        return isoHighPriority;
    }

    public void setIsoHighPriority(String isoHightPriority) {
        isoHighPriority = isoHightPriority;
    }

}
