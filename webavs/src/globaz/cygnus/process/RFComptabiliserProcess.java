package globaz.cygnus.process;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.paiement.RFLotJointPrestationJointOV;
import globaz.cygnus.db.paiement.RFLotJointPrestationJointOVManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.paiement.RFPrestationManager;
import globaz.cygnus.services.comptabilite.RFComptabiliserDecisionService;
import globaz.cygnus.services.comptabilite.RFComptabiliserMiseEnGedService;
import globaz.cygnus.services.comptabilite.RFOrdreVersementData;
import globaz.cygnus.services.comptabilite.RFPrestationData;
import globaz.cygnus.services.validerDecision.RFSimulerValidationService;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.osiris.application.CAApplication;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.tools.PRSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author FHA
 * @revision JJE
 */
public class RFComptabiliserProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private RFComptabiliserDecisionService comptabiliser = null;
    private String dateComptable = "";
    private String dateEcheancePaiement = "";
    private String descriptionLot = "";
    private String EMailObject = "";
    private String idGestionnaire = "";
    private String idLot = "";
    private String idOrganeExecution = "";
    private boolean isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome = false;
    private boolean isMisAjourLotErreur = true;
    private String numeroOG = "";
    private Set<RFPrestationData> prestationsSet = null;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
                setAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome(true);
            }

            String idTiersFondationSas = RFPropertiesUtils.getIdTiersAvanceSas();

            // connexion à Osiris
            BISession sessionOsiris = PRSession.connectSession(getSession(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
            BITransaction transactionOsiris = new BTransaction((BSession) sessionOsiris);
            transactionOsiris.openTransaction();

            setSession((BSession) sessionOsiris);
            setTransaction(transactionOsiris);

            setMemoryLog(new FWMemoryLog());
            getMemoryLog().setSession(getSession());

            // recherche des prestations et OVs liées au lot
            retrievePrestationsOVs();

            // On test si le lot ne contient que des prestations validées
            hasLotPrestationsValidees();

            // On lance la simulation de validation des décision pour obtenir le décompte
            RFSimulerValidationService.lancerSimulationValidation(getEMailAddress(), getIdGestionnaire(), false,
                    createDocInfoDecompte(), "", getSession(), getTransaction(), Boolean.FALSE, idTiersFondationSas,
                    idLot);

            // lance le process comptabilisation du lot
            comptabiliser = initialiserCompta();

            comptabiliser.lancerComptabilisation(getMemoryLog());

            getMemoryLog().logMessage(RFSimulerValidationService.getMemoryLog());

            // modification de l'état du lot, des prestations et des demandes
            updateLot(comptabiliser.getIdJournalCA());

            this.registerAttachedDocument(RFSimulerValidationService.getDocInfo(),
                    RFSimulerValidationService.getDocPath());

            // mise en GED des décisions du lot
            if (JadeGedFacade.isInstalled()) {
                miseEnGedDesDecisionsDuLot();
            }

            return true;

        } catch (Exception e) {
            // Exception pour le process de comptabilisation du lot
            JadeLogger.error(this, e.toString());
            e.printStackTrace();

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "RFComptabiliserProcess._executeProcess())");

            if (isMisAjourLotErreur()) {
                updateLotErreur();
            } else if (getTransaction() != null) {
                getTransaction().setRollbackOnly();
            }

            return false;

        } finally {

            if ((getTransaction() != null)) {
                try {
                    if (getTransaction().hasErrors() || getTransaction().isRollbackOnly()) {
                        getTransaction().rollback();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    try {
                        throw e1;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } finally {
                    try {
                        getTransaction().closeTransaction();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
    }

    private JadePublishDocumentInfo createDocInfoDecompte() {

        JadePublishDocumentInfo documentInfo = JadePublishDocumentInfoProvider.newInstance(this);
        documentInfo.setOwnerEmail(getEMailAddress());
        documentInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        documentInfo.setDocumentTitle(getSession().getLabel("PROCESS_COMPTABILISATION_DECISIONS_DOCUMENTS"));
        documentInfo.setArchiveDocument(false);
        documentInfo.setPublishDocument(false);
        documentInfo.setDocumentSubject(getSession().getLabel("PROCESS_COMPTABILISATION_DECISIONS_DOCUMENTS"));

        return documentInfo;
    }

    /**
     * on met le résultat de la requête dans un Set de prestations et d'OVs
     * 
     * @param lotJointPrestationJointOVManager
     */
    private void creationPrestationsOVsSet(RFLotJointPrestationJointOVManager lotJointPrestationJointOVManager) {

        prestationsSet = new LinkedHashSet<RFPrestationData>();
        Set<RFOrdreVersementData> ordresVersementSet = new HashSet<RFOrdreVersementData>();
        String currentIdPrestation = "";
        RFLotJointPrestationJointOV itLotJointPrestationJointOV = null;
        RFLotJointPrestationJointOV itLotJointPrestationJointOVPrecedante = null;

        /*
         * on parcours le manager : pour chaque prestation on ajoute la nouvelle prestation au Set de prestation pour
         * chaque OV on ajoute une OV au Set d'OV on ajoute le Set d'OV à la dernière prestation
         */
        int i = 0;

        for (Iterator<RFLotJointPrestationJointOV> it = lotJointPrestationJointOVManager.iterator(); it.hasNext();) {

            itLotJointPrestationJointOV = it.next();
            // on set l'idLot que l'on traite
            idLot = itLotJointPrestationJointOV.getIdLot();
            // tant que c'est la même prestation
            if (JadeStringUtil.isEmpty(currentIdPrestation)) {

                currentIdPrestation = itLotJointPrestationJointOV.getIdPrestation();
                // ajout d'un OV
                ordresVersementSet.add(new RFOrdreVersementData(itLotJointPrestationJointOV.getIdOrdreVersement(),
                        itLotJointPrestationJointOV.getTypeVersement(), itLotJointPrestationJointOV.getNumeroFacture(),
                        itLotJointPrestationJointOV.getIdExterne(), itLotJointPrestationJointOV.getCsRole(),
                        itLotJointPrestationJointOV.getIdDomaineApplication(), itLotJointPrestationJointOV
                                .getIdTiersAdressePaiement(), itLotJointPrestationJointOV.getIdTiers(),
                        itLotJointPrestationJointOV.getMontantOrdreVersement(),
                        itLotJointPrestationJointOV.getIdRole(), itLotJointPrestationJointOV.getIdTypeDeSoin(),
                        itLotJointPrestationJointOV.getIsForcerPayement(), itLotJointPrestationJointOV.getNss(),
                        itLotJointPrestationJointOV.getNom(), itLotJointPrestationJointOV.getPrenom(),
                        itLotJointPrestationJointOV.getMontantDepassementQD(), itLotJointPrestationJointOV
                                .getIsImportation(), itLotJointPrestationJointOV.getIsCompense(),
                        itLotJointPrestationJointOV.getIdSousTypeDeSoin(), itLotJointPrestationJointOV
                                .getIdSectionOrdreVersement()));

            } else if (currentIdPrestation.equals(itLotJointPrestationJointOV.getIdPrestation())) { // mm prestation
                // ajout d'une nouvelle OV au tableau courant d'OVs
                ordresVersementSet.add(new RFOrdreVersementData(itLotJointPrestationJointOV.getIdOrdreVersement(),
                        itLotJointPrestationJointOV.getTypeVersement(), itLotJointPrestationJointOV.getNumeroFacture(),
                        itLotJointPrestationJointOV.getIdExterne(), itLotJointPrestationJointOV.getCsRole(),
                        itLotJointPrestationJointOV.getIdDomaineApplication(), itLotJointPrestationJointOV
                                .getIdTiersAdressePaiement(), itLotJointPrestationJointOV.getIdTiers(),
                        itLotJointPrestationJointOV.getMontantOrdreVersement(),
                        itLotJointPrestationJointOV.getIdRole(), itLotJointPrestationJointOV.getIdTypeDeSoin(),
                        itLotJointPrestationJointOV.getIsForcerPayement(), itLotJointPrestationJointOV.getNss(),
                        itLotJointPrestationJointOV.getNom(), itLotJointPrestationJointOV.getPrenom(),
                        itLotJointPrestationJointOV.getMontantDepassementQD(), itLotJointPrestationJointOV
                                .getIsImportation(), itLotJointPrestationJointOV.getIsCompense(),
                        itLotJointPrestationJointOV.getIdSousTypeDeSoin(), itLotJointPrestationJointOV
                                .getIdSectionOrdreVersement()));

            } else { // nouvelle prestation
                // on ajoute la tableau d'OV à l'ancienne prestation
                itLotJointPrestationJointOVPrecedante = (RFLotJointPrestationJointOV) lotJointPrestationJointOVManager
                        .getContainer().get(i - 1);

                prestationsSet
                        .add(new RFPrestationData(itLotJointPrestationJointOVPrecedante.getIdPrestation(),
                                itLotJointPrestationJointOVPrecedante.getDateMoisAnnee(),
                                itLotJointPrestationJointOVPrecedante.getMontantTotal(),
                                itLotJointPrestationJointOVPrecedante.getIdLot(), itLotJointPrestationJointOVPrecedante
                                        .getCsEtatPrestation(), ordresVersementSet,
                                itLotJointPrestationJointOVPrecedante.getTypePrestation(),
                                itLotJointPrestationJointOVPrecedante.getIdDecision(),
                                itLotJointPrestationJointOVPrecedante.getRemboursementRequerant(),
                                itLotJointPrestationJointOVPrecedante.getRemboursementConjoint(),
                                itLotJointPrestationJointOVPrecedante.getIsRI(), itLotJointPrestationJointOVPrecedante
                                        .getIsLAPRAMS(), itLotJointPrestationJointOVPrecedante.getIdAdressePaiement(),
                                itLotJointPrestationJointOVPrecedante.getIdTiersBeneficiaire()));

                currentIdPrestation = itLotJointPrestationJointOV.getIdPrestation();

                // on remet le tableau de prestation à vide
                ordresVersementSet = new HashSet<RFOrdreVersementData>();
                // ajout d'un OV
                ordresVersementSet.add(new RFOrdreVersementData(itLotJointPrestationJointOV.getIdOrdreVersement(),
                        itLotJointPrestationJointOV.getTypeVersement(), itLotJointPrestationJointOV.getNumeroFacture(),
                        itLotJointPrestationJointOV.getIdExterne(), itLotJointPrestationJointOV.getCsRole(),
                        itLotJointPrestationJointOV.getIdDomaineApplication(), itLotJointPrestationJointOV
                                .getIdTiersAdressePaiement(), itLotJointPrestationJointOV.getIdTiers(),
                        itLotJointPrestationJointOV.getMontantOrdreVersement(),
                        itLotJointPrestationJointOV.getIdRole(), itLotJointPrestationJointOV.getIdTypeDeSoin(),
                        itLotJointPrestationJointOV.getIsForcerPayement(), itLotJointPrestationJointOV.getNss(),
                        itLotJointPrestationJointOV.getNom(), itLotJointPrestationJointOV.getPrenom(),
                        itLotJointPrestationJointOV.getMontantDepassementQD(), itLotJointPrestationJointOV
                                .getIsImportation(), itLotJointPrestationJointOV.getIsCompense(),
                        itLotJointPrestationJointOV.getIdSousTypeDeSoin(), itLotJointPrestationJointOV
                                .getIdSectionOrdreVersement()));
            }
            i++;
        }
        // on doit ajouter la dernière prestation si il y en a au moins une !
        if (lotJointPrestationJointOVManager.size() > 0) {
            prestationsSet.add(new RFPrestationData(itLotJointPrestationJointOV.getIdPrestation(),
                    itLotJointPrestationJointOV.getDateMoisAnnee(), itLotJointPrestationJointOV.getMontantTotal(),
                    itLotJointPrestationJointOV.getIdLot(), itLotJointPrestationJointOV.getCsEtatPrestation(),
                    ordresVersementSet, itLotJointPrestationJointOV.getTypePrestation(), itLotJointPrestationJointOV
                            .getIdDecision(), itLotJointPrestationJointOV.getRemboursementRequerant(),
                    itLotJointPrestationJointOV.getRemboursementConjoint(), itLotJointPrestationJointOV.getIsRI(),
                    itLotJointPrestationJointOV.getIsLAPRAMS(), itLotJointPrestationJointOV.getIdAdressePaiement(),
                    itLotJointPrestationJointOV.getIdTiersBeneficiaire()));
        }

        // affichage des resultats dans la console
        /*
         * for (RFPrestationData prest : this.prestationsSet) { System.out.println("id prestation : " +
         * prest.getIdPrestation() + "id adresse paiement: " + prest.getIdAdresseDePaiement()); for
         * (RFOrdreVersementData ov : prest.getOrdresVersement()) { System.out.println("id OV : " +
         * ov.getIdOrdreVersement()); } }
         */

    }

    public RFComptabiliserDecisionService getComptabiliser() {
        return comptabiliser;
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

    @Override
    protected String getEMailObject() {
        // TODO: selon le cas mettre échec de la compta
        if (JadeStringUtil.isEmpty(EMailObject)) {
            return getSession().getLabel("PROCESS_COMPTABILISER");
        } else {
            return EMailObject;
        }
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public Set<RFPrestationData> getPrestationsSet() {
        return prestationsSet;
    }

    private void hasLotPrestationsValidees() throws Exception {
        int nbGest = 0;
        String idGest = null;
        String nomGest = null;
        StringBuffer sbGestionnaires = new StringBuffer();
        List<String> gestList = new ArrayList<String>();

        boolean hasPrestaMisEnLot = false;

        for (RFPrestationData prestationCourante : prestationsSet) {

            if (prestationCourante.getEtatPrestation().equals(IRFPrestations.CS_ETAT_PRESTATION_MIS_EN_LOT)) {
                hasPrestaMisEnLot = true;
                idGest = getIdGestionnaireBloquant(prestationCourante.getIdDecision());

                if (!JadeStringUtil.isBlankOrZero(idGest)) {
                    if (!gestList.contains(idGest)) {
                        gestList.add(idGest);
                        nomGest = PRGestionnaireHelper.getNomGestionnaire(idGest);
                        // On ajoute une virgule si le gestionnaire ajouté n'est pas le premier
                        if (nbGest > 0) {
                            sbGestionnaires.append(", ");
                        }

                        sbGestionnaires.append(nomGest);
                        nbGest++;
                    }
                }
            }
        }

        if (hasPrestaMisEnLot) {
            // On laisse le lot ouvert
            setMisAjourLotErreur(false);

            StringBuffer message = new StringBuffer();
            message.append(getSession().getLabel("ERREUR_COMPTABILISER_PRESTATIONS_MIS_EN_LOT")).append(" ");
            message.append(sbGestionnaires.toString());

            throw new Exception(message.toString());
        }
    }

    /**
     * Retourne l'id du gestionnaire qui a préparé la décision dont la prestation découle
     * 
     * @param idDecision
     * @return String
     * @throws Exception
     */
    private String getIdGestionnaireBloquant(String idDecision) throws Exception {
        String idGest = "";

        RFDecisionManager deciMgr = new RFDecisionManager();
        deciMgr.setForIdDecision(idDecision);
        deciMgr.setSession(getSession());
        deciMgr.changeManagerSize(0);
        deciMgr.find();

        if (deciMgr.size() > 0) {

            Iterator<RFDecision> rfDeciItr = deciMgr.iterator();

            while (rfDeciItr.hasNext()) {
                RFDecision deci = rfDeciItr.next();

                // On récupère l'id du gestionnaire qui a préparé la décision
                idGest = deci.getIdPreparePar();
            }
        }
        return idGest;
    }

    // initialiser les attributs pour la compta.
    private RFComptabiliserDecisionService initialiserCompta() {
        RFComptabiliserDecisionService comptabiliser = new RFComptabiliserDecisionService();

        comptabiliser.setSession(getSession());
        comptabiliser.setTransaction(getTransaction());
        comptabiliser.setPrestationsSet(getPrestationsSet());
        comptabiliser.setEmail(getEMailAddress());
        comptabiliser.setIdLot(getIdLot());
        comptabiliser.setIdOrganeExecution(getIdOrganeExecution());
        comptabiliser.setDateEcheancePaiement(getDateEcheancePaiement());
        comptabiliser.setDateComptable(getDateComptable());
        comptabiliser.setNumeroOG(getNumeroOG());
        comptabiliser.setDescriptionLot(getDescriptionLot());
        comptabiliser
                .setAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome(isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome);

        return comptabiliser;
    }

    public boolean isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome() {
        return isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome;
    }

    public boolean isMisAjourLotErreur() {
        return isMisAjourLotErreur;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Methode qui permet de lancer la génération des document pour la mise en GED
     * 
     * @throws Exception
     */
    protected void miseEnGedDesDecisionsDuLot() throws Exception {

        // Récupération d'une session #CYGNUS pour atteindre labels + catalogues de textes des documents.
        BISession sessionCygnus = PRSession.connectSession(getSession(), RFApplication.DEFAULT_APPLICATION_CYGNUS);
        // Traitement des décisions du lot, et appel du process de génération des documents.
        RFComptabiliserMiseEnGedService rfComptaMiseEnGedService = new RFComptabiliserMiseEnGedService();
        rfComptaMiseEnGedService.setSession((BSession) sessionCygnus);
        rfComptaMiseEnGedService.setTransaction(getTransaction());
        rfComptaMiseEnGedService.setAdresseMail(getEMailAddress());
        rfComptaMiseEnGedService.setIdGestionnaire(getIdGestionnaire());
        rfComptaMiseEnGedService.miseEnGedDesDecisionsDuLot(getIdLot(), prestationsSet);

    }

    private RFLotJointPrestationJointOVManager recherchePrestationsOVs() throws Exception {

        RFLotJointPrestationJointOVManager lotJointPrestationJointOVManager = new RFLotJointPrestationJointOVManager();
        lotJointPrestationJointOVManager.setSession(getSession());
        lotJointPrestationJointOVManager.setForEtatsLot(new String[] { IRELot.CS_ETAT_LOT_OUVERT,
                IRELot.CS_ETAT_LOT_ERREUR });
        lotJointPrestationJointOVManager.setForLotOwner(IRELot.CS_LOT_OWNER_RFM);
        lotJointPrestationJointOVManager.setForIdLot(idLot);
        lotJointPrestationJointOVManager.setForOrderBy(RFPrestation.FIELDNAME_ID_ADRESSE_PAIEMENT + ","
                + RFPrestation.FIELDNAME_ID_TIERS_BENEFICIAIRE + "," + RFPrestation.FIELDNAME_ID_PRESTATION);
        lotJointPrestationJointOVManager.changeManagerSize(0);
        lotJointPrestationJointOVManager.find();

        return lotJointPrestationJointOVManager;
    }

    private void retrievePrestationsOVs() throws Exception {
        RFLotJointPrestationJointOVManager prestationJointOVManager = recherchePrestationsOVs();
        creationPrestationsOVsSet(prestationJointOVManager);

        /*
         * SELECT FOIADP,FOIPRE FROM CCJUWEB.RFLOTS LEFT JOIN CCJUWEB.RELOTS ON
         * CCJUWEB.RFLOTS.FWILOT=CCJUWEB.RELOTS.YTILOT LEFT JOIN CCJUWEB.RFPREST ON
         * CCJUWEB.RFPREST.FOILOT=CCJUWEB.RFLOTS.FWILOT LEFT JOIN CCJUWEB.RFORVER ON
         * CCJUWEB.RFORVER.FQIPRE=CCJUWEB.RFPREST.FOIPRE INNER JOIN CCJUWEB.TIPERSP ON
         * CCJUWEB.TIPERSP.HTITIE=CCJUWEB.RFORVER.FQITIE LEFT JOIN CCJUWEB.TIPAVSP ON
         * CCJUWEB.TIPAVSP.HTITIE=CCJUWEB.TIPERSP.HTITIE LEFT JOIN CCJUWEB.TITIERP ON
         * CCJUWEB.TIPERSP.HTITIE=CCJUWEB.TITIERP.HTITIE WHERE FOILOT = 1028 AND YTTETA IN (52834002,52834004) AND
         * YTTOWN = 52858002 ORDER BY FOIADP,FOIPRE
         */
    }

    public void setAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome(
            boolean isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome) {
        this.isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome = isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome;
    }

    public void setComptabiliser(RFComptabiliserDecisionService comptabiliser) {
        this.comptabiliser = comptabiliser;
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

    public void setEMailObject(String eMailObject) {
        EMailObject = eMailObject;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setMisAjourLotErreur(boolean isMisAjourLotErreur) {
        this.isMisAjourLotErreur = isMisAjourLotErreur;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public void setPrestationsSet(Set<RFPrestationData> prestationsSet) {
        this.prestationsSet = prestationsSet;
    }

    private void updateLot(String idJournalCA) throws Exception {

        Set<String> idsDecisionSet = new HashSet<String>();

        if (!JadeStringUtil.isBlankOrZero(idLot)) {
            RELot lot = new RELot();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve();

            if (!lot.isNew()) {

                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                lot.setDateEnvoiLot(JACalendar.todayJJsMMsAAAA());
                lot.setIdJournalCA(idJournalCA);

                RFPrestationManager rfPreMgr = new RFPrestationManager();
                rfPreMgr.setSession(getSession());
                rfPreMgr.setForIdLot(lot.getIdLot());
                rfPreMgr.changeManagerSize(0);
                rfPreMgr.find(getTransaction());

                Iterator<RFPrestation> rfPreItr = rfPreMgr.iterator();

                while (rfPreItr.hasNext()) {
                    RFPrestation rfPrestationitr = rfPreItr.next();

                    if (null != rfPrestationitr) {
                        RFPrestation rfPrest = new RFPrestation();
                        rfPrest.setSession(getSession());
                        rfPrest.setIdPrestation(rfPrestationitr.getIdPrestation());

                        rfPrest.retrieve();

                        if (!rfPrest.isNew()) {

                            idsDecisionSet.add(rfPrest.getIdDecision());

                            rfPrest.setCsEtatPrestation(IRFPrestations.CS_ETAT_PRESTATION_VALIDE);
                            rfPrest.update(getTransaction());

                        } else {
                            setMisAjourLotErreur(false);
                            throw new Exception(
                                    "RFComptabiliserProcess.updateLot():Impossible de retrouver les prestations du lot");
                        }
                    } else {
                        setMisAjourLotErreur(false);
                        throw new Exception(
                                "RFComptabiliserProcess.updateLot():Impossible de retrouver les prestations du lot");
                    }
                }

                lot.update(getTransaction());

                RFDemandeManager rfDemandeMgr = new RFDemandeManager();
                rfDemandeMgr.setSession(getSession());

                String[] idsDecisionArray = new String[idsDecisionSet.size()];
                int i = 0;
                for (String idCourant : idsDecisionSet) {
                    idsDecisionArray[i] = idCourant;
                    i++;
                }

                rfDemandeMgr.setForIdsDecision(idsDecisionArray);
                rfDemandeMgr.changeManagerSize(0);
                rfDemandeMgr.find();

                Iterator<RFDemande> rfDemandeItr = rfDemandeMgr.iterator();

                while (rfDemandeItr.hasNext()) {
                    RFDemande demandeCourante = rfDemandeItr.next();
                    if (null != demandeCourante) {
                        RFDemande demande = new RFDemande();
                        demande.setSession(getSession());
                        demande.setIdDemande(demandeCourante.getIdDemande());

                        demande.retrieve();

                        if (!demande.isNew()) {
                            demande.setCsEtat(IRFDemande.PAYE);
                            demande.update(getTransaction());
                        } else {
                            setMisAjourLotErreur(false);
                            throw new Exception("RFComptabiliserProcess.updateDemande():Demande (entité) introuvable");
                        }
                    } else {
                        setMisAjourLotErreur(false);
                        throw new Exception("RFComptabiliserProcess.updateDemande():Demande introuvable");
                    }
                }

            } else {
                setMisAjourLotErreur(false);
                throw new Exception("RFComptabiliserProcess.updateLot():lot ouvert introuvable");
            }
        } else {
            setMisAjourLotErreur(false);
            throw new Exception("RFComptabiliserProcess.updateLot():lot ouvert introuvable");
        }
    }

    private void updateLotErreur() throws Exception {

        setEMailObject(getSession().getLabel("PROCESS_COMPTABILISER_ECHEC"));
        if (!JadeStringUtil.isBlankOrZero(idLot)) {
            RELot lot = new RELot();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve();

            if (!lot.isNew()) {

                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_ERREUR);
                lot.setDateEnvoiLot(JACalendar.todayJJsMMsAAAA());
                if (null != comptabiliser) {
                    lot.setIdJournalCA(comptabiliser.getIdJournalCA());// vide
                }
                lot.update(getTransaction());
            }
        }
    }

}
