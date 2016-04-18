package globaz.corvus.process;

import globaz.babel.db.copies.CTCopies;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointBaseCalcul;
import globaz.corvus.db.demandes.REDemandeRenteJointBaseCalculManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.topaz.RERenteVeuvePerdureOO;
import globaz.corvus.utils.REGedUtils;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.itext.PRLettreEnTete;
import globaz.prestation.topaz.PRLettreEnTeteOO;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REGenererRenteVeuvePerdureProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean annexeParDefaut = Boolean.FALSE;
    private String annexes = "";
    private String dateDebutRenteVieillesse = "";
    private String dateDocument = "";
    private String emailAddress = "";
    private String idDemandeRente = "";
    private String idTiers = "";
    private Boolean isSendToGed = Boolean.FALSE;
    private String montantRenteVeuve = "";
    private String montantRenteVieillesse = "";

    public REGenererRenteVeuvePerdureProcess() {
        super();
    }

    private void addCopiePourAgenceCommunale(JadePrintDocumentContainer allDoc, BTransaction transaction,
            PRTiersWrapper tierAdmin, REDemandeRente demandeRente) throws Exception {

        // Création de la page de garde pour la copie envoyée à la commune d'origine
        JadePublishDocumentInfo pageDeGardeInfo = JadePublishDocumentInfoProvider.newInstance(this);
        pageDeGardeInfo.setOwnerEmail(getEmailAddress());
        pageDeGardeInfo.setDocumentDate(getDateDocument());
        pageDeGardeInfo.setPublishDocument(false);
        pageDeGardeInfo.setArchiveDocument(false);

        DocumentData pageDeGardeData = createPRLettreEnTeteOO(getSession(), tierAdmin, null, getDateDocument());
        allDoc.addDocument(pageDeGardeData, pageDeGardeInfo);

        // Création de la copie pour la commune origine
        JadePublishDocumentInfo copieDocInfo = new JadePublishDocumentInfo();
        copieDocInfo.setDocumentTitle(getSession().getLabel("RENTE_VEUVE_PERDURE"));
        copieDocInfo.setDocumentSubject(getSession().getLabel("RENTE_VEUVE_PERDURE"));
        copieDocInfo.setOwnerEmail(getEmailAddress());
        copieDocInfo.setDocumentType(IRENoDocumentInfoRom.DECISION_DE_RENTES_VEUVE_PERDURE);
        copieDocInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.DECISION_DE_RENTES_VEUVE_PERDURE);
        copieDocInfo.setDocumentDate(getDateDocument());
        copieDocInfo.setPublishDocument(false);
        copieDocInfo.setArchiveDocument(false);

        RERenteVeuvePerdureOO copieRenteVeuvePerdureOO = new RERenteVeuvePerdureOO(getSession(), getDateDocument(),
                getIdDemandeRente(), getMontantRenteVeuve(), getMontantRenteVieillesse(),
                getDateDebutRenteVieillesse(), getIdTiers(), true, demandeRente.getCsTypeDemandeRente());
        copieRenteVeuvePerdureOO.setAnnexes(annexes);
        copieRenteVeuvePerdureOO.setAnnexeParDefaut(isAnnexeParDefaut());
        copieRenteVeuvePerdureOO.setCopiePourAgenceAvs(tierAdmin.getIdTiers());
        copieRenteVeuvePerdureOO.genererLettre();
        allDoc.addDocument(copieRenteVeuvePerdureOO.getData(), copieDocInfo);
    }

    /**
     * <p>
     * Génère les DocumentData pour la création d'un en-tête<br/>
     * Copie de la méthode "createLettreEntete" de {@link REImprimerDecisionProcess}
     * </p>
     * 
     * @param session
     * @param idTier
     * @param idProcedureCommunication
     * @param dateDuDocument
     * @return
     * @throws Exception
     */
    private DocumentData createPRLettreEnTeteOO(BSession session, PRTiersWrapper tier, String idProcedureCommunication,
            String dateDuDocument) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(session);

        // BZ 5536
        // retrieve de la procédure de communication si elle est définie
        CTCopies procedureCommunication = null;
        if (!JadeStringUtil.isBlankOrZero(idProcedureCommunication)) {
            procedureCommunication = new CTCopies();
            procedureCommunication.setSession(session);
            procedureCommunication.setIdCopie(idProcedureCommunication);
            procedureCommunication.retrieve();
        }

        lettreEnTete.setTierAdresse(tier);
        lettreEnTete.setSession(session);
        lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
        // lettreEnTete.setDateDocument(JACalendar.format(dateDuDocument,
        // PRUtil.getISOLangueTiers(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
        lettreEnTete.setDateDocument(dateDuDocument);
        // BZ 5536
        if ((procedureCommunication != null) && !procedureCommunication.isNew()) {
            lettreEnTete.setReferenceProcedureComunication(procedureCommunication.getReference());
        }
        lettreEnTete.generationLettre();

        return lettreEnTete.getDocumentData();
    }

    public Boolean getAnnexeParDefaut() {
        return annexeParDefaut;
    }

    public String getAnnexes() {
        return annexes;
    }

    private String getCodeAdministrationFromIdTiersAdmin(BTransaction transaction, BSession session, String idTierAdmin)
            throws Exception {
        TIAdministrationViewBean administrationCommunale = new TIAdministrationViewBean();
        administrationCommunale.setSession(session);
        administrationCommunale.setIdTiersAdministration(idTierAdmin);
        administrationCommunale.retrieve(transaction);
        return administrationCommunale.getCodeInstitution();
    }

    public String getDateDebutRenteVieillesse() {
        return dateDebutRenteVieillesse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("DESCRIPTION_RENTE_VEUVE_PERDURE");
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Recherche le code de l'administration ou est domicilié le tier principale en fonction d'un type de lien 507007
     * 
     * @param transaction
     * @param session
     * @param idTierPrincipale
     * @return IdTier de la commune ou est établis l'idTier principal
     * @throws Exception
     */
    private String getIdTiersAdministrationComunale(BTransaction transaction, BSession session, String idTierPrincipale)
            throws Exception {

        String idTierEnfant = null;
        TICompositionTiersManager compTiersMgr = new TICompositionTiersManager();
        compTiersMgr.setForIdTiersParent(idTiers);
        compTiersMgr.setForTypeLien("507007");
        compTiersMgr.setSession(session);

        compTiersMgr.find(transaction);
        if (compTiersMgr.isEmpty()) {
            return null;
        }

        for (int i = 0; i < compTiersMgr.size(); i++) {
            TICompositionTiers entity = (TICompositionTiers) compTiersMgr.get(i);

            TIAdministrationViewBean administrationCommunale = new TIAdministrationViewBean();
            administrationCommunale.setSession(session);
            administrationCommunale.setIdTiersAdministration(entity.getIdTiersEnfant());
            administrationCommunale.retrieve(transaction);

            if (!administrationCommunale.isNew()) {
                idTierEnfant = entity.getIdTiersEnfant();
            }
        }
        return idTierEnfant;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getMontantRenteVeuve() {
        return montantRenteVeuve;
    }

    public String getMontantRenteVieillesse() {
        return montantRenteVieillesse;
    }

    @Override
    public String getName() {
        return getDescription();
    }

    public boolean isAnnexeParDefaut() {
        return annexeParDefaut;
    }

    public boolean isSendToGed() {
        return getIsSendToGed();
    }

    @Override
    public void run() {
        BTransaction transaction = null;
        try {
            transaction = getTransaction();

            if (transaction == null) {
                transaction = (BTransaction) getSession().newTransaction();
            }

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(idDemandeRente);
            demandeRente.retrieve(transaction);

            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())) {
                REPeriodeInvaliditeManager periodeInvaliditeManager = new REPeriodeInvaliditeManager();
                periodeInvaliditeManager.setSession(getSession());
                periodeInvaliditeManager.setForIdDemandeRente(idDemandeRente);
                periodeInvaliditeManager.setOrderBy(REPeriodeInvalidite.FIELDNAME_DATE_DEBUT_INVALIDITE);
                periodeInvaliditeManager.find(1);

                if (periodeInvaliditeManager.size() == 1) {
                    REPeriodeInvalidite periode = (REPeriodeInvalidite) periodeInvaliditeManager.get(0);
                    setDateDebutRenteVieillesse(periode.getDateDebutInvalidite());
                } else {
                    throw new Exception(getSession().getLabel("ERREUR_PERIODE_RENTE_INVALIDITE"));
                }

            }

            // ce document n'est imprimable que pour des rentes vieillesse
            if (!IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demandeRente.getCsTypeDemandeRente())
                    && !IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())) {
                throw new Exception(getSession().getLabel("ERREUR_DEMANDE_RENTE_TYPE_INVALIDE"));
            }

            // pas dans un état Enregistré, Au calcul, Calculé ou Terminé
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(demandeRente.getCsEtat())
                    && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(demandeRente.getCsEtat())
                    && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demandeRente.getCsEtat())
                    && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(demandeRente.getCsEtat())) {
                throw new Exception(getSession().getLabel("ERREUR_DEMANDE_RENTE_ETAT_INVALIDE"));
            }

            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

            JadePublishDocumentInfo destinationInfo = JadePublishDocumentInfoProvider.newInstance(this);
            destinationInfo.setDocumentTitle(getSession().getLabel("RENTE_VEUVE_PERDURE"));
            destinationInfo.setDocumentSubject(getSession().getLabel("RENTE_VEUVE_PERDURE"));
            destinationInfo.setOwnerEmail(getEmailAddress());
            destinationInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAddress());
            destinationInfo.setPublishDocument(true);
            destinationInfo.setArchiveDocument(false);

            allDoc.setMergedDocDestination(destinationInfo);

            // Doit-on inclure une copie pour agence communale
            String propertiesCopieAgenceComm = getSession().getApplication().getProperty("isCopieAgenceCommunale");
            boolean inclureCopiePourAgenceCommunale = Boolean.parseBoolean(propertiesCopieAgenceComm);
            PRTiersWrapper tierAdministration = null;
            String idTierAdmin = null;

            // Si on doit inclure une copie, on vas rechercher les infos de l'administration
            if (inclureCopiePourAgenceCommunale) {
                idTierAdmin = getIdTiersAdministrationComunale(transaction, getSession(), getIdTiers());
                tierAdministration = PRTiersHelper.getAdministrationParId(getSession(), idTierAdmin);
                // Si on ne trouve pas l'admin, ce n'est pas normal ==> Exception
                if (tierAdministration == null) {
                    throw new Exception(FWMessageFormat.format(getSession().getLabel("WARNING_AGENCE_COMM"),
                            idTierAdmin));
                } else {
                    // Si le code institution == 9998, PAS DE COPIE car c'est une caisse d'un autre canton
                    String codeInstitution = getCodeAdministrationFromIdTiersAdmin(transaction, getSession(),
                            idTierAdmin);
                    if ((codeInstitution == null) || "9998".equals(codeInstitution) || "9999".equals(codeInstitution)) {
                        inclureCopiePourAgenceCommunale = false;
                    }
                }
            }

            // Document principal
            JadePublishDocumentInfo decisionDocInfo = JadePublishDocumentInfoProvider.newInstance(this);
            decisionDocInfo.setDocumentTitle(getSession().getLabel("RENTE_VEUVE_PERDURE"));
            decisionDocInfo.setDocumentSubject(getSession().getLabel("RENTE_VEUVE_PERDURE"));
            decisionDocInfo.setOwnerEmail(getEmailAddress());
            decisionDocInfo.setDocumentType(IRENoDocumentInfoRom.DECISION_DE_RENTES_VEUVE_PERDURE);
            decisionDocInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.DECISION_DE_RENTES_VEUVE_PERDURE);
            decisionDocInfo.setDocumentDate(getDateDocument());
            decisionDocInfo.setPublishDocument(false);
            decisionDocInfo.setArchiveDocument(isSendToGed());
            decisionDocInfo.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourCetteDemandeRente(getSession(), demandeRente, false, true)));

            if (isSendToGed()) {
                TIDocumentInfoHelper.fill(decisionDocInfo, idTiers, getSession(), null, null, null);
            }

            RERenteVeuvePerdureOO renteVeuvePerdureOO = new RERenteVeuvePerdureOO(getSession(), getDateDocument(),
                    getIdDemandeRente(), getMontantRenteVeuve(), getMontantRenteVieillesse(),
                    getDateDebutRenteVieillesse(), getIdTiers(), false, demandeRente.getCsTypeDemandeRente());
            renteVeuvePerdureOO.setAnnexes(annexes);
            renteVeuvePerdureOO.setAnnexeParDefaut(isAnnexeParDefaut());

            if (inclureCopiePourAgenceCommunale) {
                renteVeuvePerdureOO.setCopiePourAgenceAvs(tierAdministration.getIdTiers());
            }
            renteVeuvePerdureOO.genererLettre();

            allDoc.addDocument(renteVeuvePerdureOO.getData(), decisionDocInfo);

            if (inclureCopiePourAgenceCommunale) {
                addCopiePourAgenceCommunale(allDoc, transaction, tierAdministration, demandeRente);
            }

            this.createDocuments(allDoc);
            // ---------//

            // si la rente n'est pas terminée (on exclut l'état validé), on supprime sa base de calcul
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(demandeRente.getCsEtat())
                    && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())) {
                REDemandeRenteJointBaseCalculManager manager = new REDemandeRenteJointBaseCalculManager();
                manager.setSession(getSession());
                manager.setForIdDemandeRente(demandeRente.getIdDemandeRente());
                manager.find(transaction);

                // Si la demande a des bases de calcul, on les supprime, ainsi que les prestations par la cascade.
                if (manager.size() > 0) {
                    for (int i = 0; i < manager.size(); i++) {
                        REBasesCalcul baseCalcul = new REBasesCalcul();
                        baseCalcul.setSession(getSession());
                        baseCalcul.setIdBasesCalcul(((REDemandeRenteJointBaseCalcul) manager.get(i)).getIdBaseCalcul());
                        baseCalcul.retrieve(transaction);

                        if (!baseCalcul.isNew()) {
                            // Suppression de : bases de calcul et des éléments liés
                            REDeleteCascadeDemandeAPrestationsDues.supprimerRenteVeuvePerdureCascade_noCommit(
                                    getSession(), transaction, baseCalcul);

                            transaction.commit();
                        }
                    }
                }

                // lorsque le traitement est terminé, on passe la demande en état terminé
                demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
                // et la date du document comme date de fin de droit
                demandeRente.setDateFin(dateDocument);
                demandeRente.update(transaction);
            }
        } catch (Exception ex) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, REGenererRenteVeuvePerdureProcess.class
                            .getName(), ex.getMessage()));
        } finally {
            try {
                if (transaction.hasErrors()) {
                    getLogSession().error(this.getClass().getName(), transaction.getErrors().toString());
                    JadeLogger.warn(this, transaction.getErrors().toString());
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            try {
                if (getLogSession().hasMessages()) {
                    List<String> emails = new ArrayList<String>();
                    emails.add(getEmailAddress());
                    sendCompletionMail(emails);
                }

            } catch (Exception ex) {
                getLogSession().addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                REGenererRenteVeuvePerdureProcess.class.getName(), getSession().getLabel(
                                        "ERREUR_ENVOI_MAIL_COMPLETION")));
            }
        }
    }

    public void setAnnexeParDefaut(Boolean annexeParDefaut) {
        this.annexeParDefaut = annexeParDefaut;
    }

    public void setAnnexes(String annexes) {
        this.annexes = annexes;
    }

    public void setDateDebutRenteVieillesse(String dateDebutRenteVieillesse) {
        this.dateDebutRenteVieillesse = dateDebutRenteVieillesse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setMontantRenteVeuve(String montantRenteVeuve) {
        this.montantRenteVeuve = montantRenteVeuve;
    }

    public void setMontantRenteVieillesse(String montantRenteVieillesse) {
        this.montantRenteVieillesse = montantRenteVieillesse;
    }
}
