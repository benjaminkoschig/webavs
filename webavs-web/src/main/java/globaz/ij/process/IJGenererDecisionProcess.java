package globaz.ij.process;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.api.doc.ICTScalableDocumentGenerator;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.babel.api.doc.impl.CTScalableDocumentCopie;
import globaz.babel.utils.CTTiersUtils;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.ij.api.decisions.IIJDecisionIJAI;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.decisions.IJAnnexeDecision;
import globaz.ij.db.decisions.IJAnnexeDecisionManager;
import globaz.ij.db.decisions.IJCopieDecision;
import globaz.ij.db.decisions.IJCopieDecisionManager;
import globaz.ij.db.decisions.IJDecisionIJAI;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.itext.IJDecision;
import globaz.ij.itext.IJMoyensDroit;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.itext.PRLettreEnTete;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.Iterator;

public class IJGenererDecisionProcess extends BProcess implements ICTScalableDocumentGenerator {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDecision = "";
    private ICTScalableDocumentProperties documentProperties;
    private String idPrononce = "";
    private ICTScalableDocumentCopie intervenantCopie;
    private boolean isCopieDocument = Boolean.FALSE;
    private Iterator iteratorDestinataireCopie;
    private String numeroDeDecisionAI = "";
    private ICTScalableDocumentProperties scalableDocumentProperties;

    public IJGenererDecisionProcess() throws Exception {
        this(new BSession(IJApplication.DEFAULT_APPLICATION_IJ));
    }

    public IJGenererDecisionProcess(BProcess parent) throws Exception {
        super(parent);
    }

    public IJGenererDecisionProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            // Reprendre tous les paramètres qui viennent de BABEL
            documentProperties = getScalableDocumentProperties();

            if (documentProperties.containsParameter("idPrononce").booleanValue()) {
                idPrononce = documentProperties.getParameter("idPrononce");
            } else {
                getMemoryLog().logMessage("Aucun idPrononcé trouvé dans les paramètres", FWMessage.ERREUR,
                        "IJGenererDecisionProcess");
                abort();
            }

            // Recherche du prononce courant
            IJPrononce p = new IJPrononce();
            p.setSession(getSession());
            p.setIdPrononce(idPrononce);
            p.retrieve();

            // Sauvegarde des donne de la décision
            if ("SAVE_ONLY".equals(documentProperties.getParameter("processAction"))) {
                doSaveOnly(p);
                setSendCompletionMail(false);
                return true;
            }

            // Ajout de l'office AI en copie par défaut
            CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();
            ICTScalableDocumentCopie copieOfficeAiPron = factory.createNewScalableDocumentCopie();

            if (null != p) {
                TIAdministrationManager tiAdminOfficeAiPronMgr = new TIAdministrationManager();
                tiAdminOfficeAiPronMgr.setSession(getSession());
                tiAdminOfficeAiPronMgr.setForCodeAdministration(p.getOfficeAI());
                tiAdminOfficeAiPronMgr.setForGenreAdministration("509004");

                tiAdminOfficeAiPronMgr.find();

                TIAdministrationViewBean tiAdminOfficeAiPron = (TIAdministrationViewBean) tiAdminOfficeAiPronMgr
                        .getFirstEntity();

                if (tiAdminOfficeAiPron != null) {

                    if (!JadeStringUtil.isIntegerEmpty(tiAdminOfficeAiPron.getIdTiersAdministration())) {
                        copieOfficeAiPron.setIdTiers(tiAdminOfficeAiPron.getIdTiersAdministration());
                        String nom = CTTiersUtils.getPrenomNomTiersParIdTiers(getSession(),
                                tiAdminOfficeAiPron.getIdTiersAdministration());
                        copieOfficeAiPron.setPrenomNomTiers(nom);
                        ((CTScalableDocumentCopie) copieOfficeAiPron).setCopieOAI(true);

                        documentProperties.addCopie(copieOfficeAiPron);

                    }
                }
            }

            // 1. Créer le document original
            // 1a. reprendre les copies
            iteratorDestinataireCopie = documentProperties.getCopiesIterator();

            // 1b. Créer la décision
            IJDecision decisionOriginale = createDecisionOriginale(p);
            // specificHeader=decisionOriginale.getSpecificHeader();
            numeroDeDecisionAI = decisionOriginale.getNumeroDeDecisionAI();
            dateDecision = decisionOriginale.getDateDecision();

            if (null != p) {

                // 1c. Créer les moyens de droit
                createMoyensDroit(p);

                // 1d. Fusionne tous les documents physiquement ci-dessus pour mettre en GED la décision fusionné avec
                // les moyens de droits
                fusionneDocumentsGED(decisionOriginale);

                // 2. Créer les copies
                while (iteratorDestinataireCopie.hasNext()) {
                    intervenantCopie = (ICTScalableDocumentCopie) iteratorDestinataireCopie.next();

                    // 2a. Créer la page en-tête
                    if ((null != intervenantCopie)
                            && !CaisseHelperFactory.getInstance().getNoCaisseFormatee(getSession().getApplication())
                                    .equals(getNoCodeCaisse(intervenantCopie.getIdTiers()))) {

                        createLettreEntete(intervenantCopie, p.getOfficeAI());
                    }

                    isCopieDocument = true;

                    // 2b. Créer la décision
                    createDecisionCopie();

                    // 2c. Créer les moyens de droit
                    createMoyensDroit(p);
                }

                // 3. Fusionne tous les documents physiquement ci-dessus pour la publication
                fusionneDocumentsPRINT();

                documentProperties.removeCopie(copieOfficeAiPron);

                // Tester si abort
                if (isAborted()) {
                    return false;
                }

            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getSession().getLabel("GENERER_DECISION_KO") + " : " + e.getMessage());
            return false;
        }

        return true;
    }

    private void fusionneDocumentsGED(IJDecision decisionOriginale) throws Exception {
        JadePublishDocumentInfo createCopy = decisionOriginale.getDocumentInfoForCopy();
        createCopy.setPublishDocument(false);
        createCopy.setArchiveDocument(true);
        this.mergePDF(createCopy, true, 500, false, null);
    }

    private IJDecision createDecisionCopie() throws FWIException, Exception {

        IJDecision decisionCopie = new IJDecision();
        decisionCopie.setSession(getSession());
        decisionCopie.setCopieEnTete(true);
        decisionCopie.setEMailAddress(getEMailAddress());
        decisionCopie.setIteratorDestinataireCopie(iteratorDestinataireCopie);
        decisionCopie.setIdPrononce(idPrononce);
        decisionCopie.setScalableDocumentProperties(documentProperties);
        decisionCopie.setParent(this);

        decisionCopie.executeProcess();

        return decisionCopie;
    }

    private IJDecision createDecisionOriginale(IJPrononce prononce) throws FWIException, Exception {

        IJDecision decisionOriginale = new IJDecision();
        decisionOriginale.setSession(getSession());
        decisionOriginale.setEMailAddress(getEMailAddress());
        decisionOriginale.setIteratorDestinataireCopie(iteratorDestinataireCopie);
        decisionOriginale.setIdPrononce(prononce.getIdPrononce());
        decisionOriginale.setScalableDocumentProperties(documentProperties);
        decisionOriginale.setParent(this);

        // stockage des info de la décisions !!!!

        doSaveOnly(prononce);
        decisionOriginale.executeProcess();

        return decisionOriginale;
    }

    private PRLettreEnTete createLettreEntete(ICTScalableDocumentCopie intervenantCopie, String noOfficeAI)
            throws FWIException, Exception {

        PRLettreEnTete lettreEnTete = new PRLettreEnTete();
        lettreEnTete.setSession(getSession());
        // lettreEnTete.setSpecificHeader(specificHeader);
        lettreEnTete.setNoOfficeAI(noOfficeAI);
        lettreEnTete.setNumeroDeDecisionAI(numeroDeDecisionAI);
        lettreEnTete.setDateDecision(dateDecision);

        // retrieve du tiers
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), intervenantCopie.getIdTiers());

        if (null == tier) {
            tier = PRTiersHelper.getAdministrationParId(getSession(), intervenantCopie.getIdTiers());
        }

        lettreEnTete.setTierAdresse(tier);
        lettreEnTete.setIdAffilie(intervenantCopie.getIdAffilie());
        lettreEnTete.setEMailAddress(getEMailAddress());
        lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_IJAI);
        lettreEnTete.setParent(this);
        lettreEnTete.executeProcess();

        return lettreEnTete;
    }

    private IJMoyensDroit createMoyensDroit(IJPrononce prononce) throws FWIException, Exception {

        IJMoyensDroit moyensDroit = new IJMoyensDroit();
        moyensDroit.setSession(getSession());
        moyensDroit.setEMailAddress(getEMailAddress());
        moyensDroit.setParent(this);
        moyensDroit.setPrononce(prononce);

        // Boolean pour ne pas mettre en Ged si on génère une copie
        if (isCopieDocument) {
            moyensDroit.setIsCopieDocument(isCopieDocument);
        }

        // Reprendre le tiers bénéficiaire
        String idTierPrinc = documentProperties.getIdTiersPrincipal();
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTierPrinc);

        if (null == tiers) {
            tiers = PRTiersHelper.getAdministrationParId(getSession(), idTierPrinc);
        }

        moyensDroit.setTiers(tiers);

        // Retrouver si petite ou grande IJ
        if (!prononce.isNew()) {
            if (prononce.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ)) {
                moyensDroit.setPetiteIJ(true);
            } else {
                moyensDroit.setPetiteIJ(false);
            }
        }

        moyensDroit.executeProcess();

        return moyensDroit;

    }

    private void doSaveOnly(IJPrononce prononce) throws Exception {

        IJDecisionIJAI decision = new IJDecisionIJAI();
        decision.setSession(getSession());
        decision.setIdDecision(prononce.getIdDecision());
        decision.retrieve(getTransaction());

        // On stocke les info de la décision...
        if (decision.isNew()) {
            decision.setIdPrononce(prononce.getIdPrononce());
            decision.setEmailAdresse(documentProperties.getParameter("eMailAddress"));
            decision.setDateSurDocument(documentProperties.getParameter("dateSurDocument"));
            decision.setIdTiersAdrCourrier(documentProperties.getParameter("idTierAdresseCourrier"));
            decision.setBeneficiaire(documentProperties.getParameter("beneficiaire"));
            decision.setIdPersonneReference(documentProperties.getParameter("idPersonneReference"));
            decision.setCsCantonTauxImposition(documentProperties.getParameter("cantonTauxImposition"));
            decision.setTauxImposition(documentProperties.getParameter("tauxImposition"));
            decision.setRemarques(documentProperties.getParameter("remarque"));
            decision.setNoRevAGarantir(documentProperties.getParameter("garantitRevision"));
            // decision.setIsSendToGed(new
            // Boolean(documentProperties.getParameter("isSendToGed")));
            decision.setIdDocument(documentProperties.getIdDocument());
            decision.setIdTiersPrincipal(documentProperties.getIdTiersPrincipal());
            decision.setTauxImposition(documentProperties.getParameter("tauxImposition"));
            decision.setIsDecisionValidee(Boolean.FALSE);

            if ("standard".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                if ("assure".equals(documentProperties.getParameter("beneficiaire"))) {
                    decision.setIdTiersAdrPmt(documentProperties.getParameter("idTierAssureAdressePaiement"));
                } else if ("employeur".equals(documentProperties.getParameter("beneficiaire"))) {
                    decision.setIdTiersAdrPmt(documentProperties.getParameter("idTierEmployeurAdressePaiement"));
                }
            } else if ("personnalise".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                decision.setIdTiersAdrPmt(documentProperties.getParameter("idTiersAdressePaiementPersonnalisee"));
            } else if ("aucun".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                decision.setIdTiersAdrPmt("");
            }

            decision.setPersonnalisationAdressePaiement(documentProperties
                    .getParameter("personnalisationAdressePaiement"));

            decision.setIdTiersAdressePaiementPersonnalisee(documentProperties
                    .getParameter("idTiersAdressePaiementPersonnalisee"));
            decision.setIdDomaineAdressePaiementPersonnalisee(documentProperties
                    .getParameter("idDomaineApplicationAdressePaiementPersonnalisee"));
            decision.setNumeroAffilieAdressePaiementPersonnalisee(documentProperties
                    .getParameter("numAffilieAdressePaiementPersonnalisee"));

            decision.setCsEtatMiseEnGed(IIJDecisionIJAI.CS_ETAT_ENVOI_DECISION_EN_GED_ATTENTE);
            decision.setCsEtatSEDEX(IIJDecisionIJAI.CS_ETAT_ENVOI_DECISION_A_SEDEX_ATTENTE);
            decision.add(getTransaction());

            prononce.setIdDecision(decision.getIdDecision());
            prononce.update(getTransaction());

            Iterator iter = documentProperties.getCopiesIterator();
            while (iter.hasNext()) {
                ICTScalableDocumentCopie copie = (ICTScalableDocumentCopie) iter.next();

                IJCopieDecision cp = new IJCopieDecision();
                cp.setSession(getSession());
                cp.setIdDecision(decision.getIdDecision());
                cp.setIdTiers(copie.getIdTiers());
                cp.setPrenomNom(copie.getPrenomNomTiers());
                try {
                    cp.setIsCopieOAI(new Boolean(((CTScalableDocumentCopie) copie).isCopieOAI()));
                } catch (Exception e) {
                    cp.setIsCopieOAI(Boolean.FALSE);
                }
                cp.add(getTransaction());

            }

            iter = documentProperties.getAnnexesIterator();
            while (iter.hasNext()) {
                ICTScalableDocumentAnnexe annexe = (ICTScalableDocumentAnnexe) iter.next();

                IJAnnexeDecision an = new IJAnnexeDecision();
                an.setSession(getSession());
                an.setIdDecision(decision.getIdDecision());
                an.setDescription(annexe.getLibelle());
                an.add(getTransaction());
            }
        }
        // La décision est déjà existante !!!!
        // On met à jours toute les data !!!!
        else {
            decision.setIdPrononce(prononce.getIdPrononce());
            decision.setEmailAdresse(documentProperties.getParameter("eMailAddress"));
            decision.setDateSurDocument(documentProperties.getParameter("dateSurDocument"));
            decision.setIdTiersAdrCourrier(documentProperties.getParameter("idTierAdresseCourrier"));
            decision.setBeneficiaire(documentProperties.getParameter("beneficiaire"));
            decision.setIdPersonneReference(documentProperties.getParameter("idPersonneReference"));
            decision.setCsCantonTauxImposition(documentProperties.getParameter("cantonTauxImposition"));
            decision.setTauxImposition(documentProperties.getParameter("tauxImposition"));
            decision.setRemarques(documentProperties.getParameter("remarque"));
            decision.setNoRevAGarantir(documentProperties.getParameter("garantitRevision"));
            // decision.setIsSendToGed(new
            // Boolean(documentProperties.getParameter("isSendToGed")));
            decision.setIdDocument(documentProperties.getIdDocument());
            decision.setIdTiersPrincipal(documentProperties.getIdTiersPrincipal());
            decision.setTauxImposition(documentProperties.getParameter("tauxImposition"));

            if ("standard".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                if ("assure".equals(documentProperties.getParameter("beneficiaire"))) {
                    decision.setIdTiersAdrPmt(documentProperties.getParameter("idTierAssureAdressePaiement"));
                } else if ("employeur".equals(documentProperties.getParameter("beneficiaire"))) {
                    decision.setIdTiersAdrPmt(documentProperties.getParameter("idTierEmployeurAdressePaiement"));
                }
            } else if ("personnalise".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                decision.setIdTiersAdrPmt(documentProperties.getParameter("idTiersAdressePaiementPersonnalisee"));
            } else if ("aucun".equals(documentProperties.getParameter("personnalisationAdressePaiement"))) {
                decision.setIdTiersAdrPmt("");
            }

            decision.setPersonnalisationAdressePaiement(documentProperties
                    .getParameter("personnalisationAdressePaiement"));

            decision.setIdTiersAdressePaiementPersonnalisee(documentProperties
                    .getParameter("idTiersAdressePaiementPersonnalisee"));
            decision.setIdDomaineAdressePaiementPersonnalisee(documentProperties
                    .getParameter("idDomaineApplicationAdressePaiementPersonnalisee"));
            decision.setNumeroAffilieAdressePaiementPersonnalisee(documentProperties
                    .getParameter("numAffilieAdressePaiementPersonnalisee"));

            decision.update(getTransaction());

            // On supprime et recréé les annexes et copies !!!!
            IJCopieDecisionManager mgr = new IJCopieDecisionManager();
            mgr.setSession(getSession());
            mgr.setForIdDecision(decision.getIdDecision());
            mgr.find(getTransaction());
            for (int i = 0; i < mgr.size(); i++) {
                IJCopieDecision cp = (IJCopieDecision) mgr.getEntity(i);
                cp.delete(getTransaction());
            }

            IJAnnexeDecisionManager mgr2 = new IJAnnexeDecisionManager();
            mgr2.setSession(getSession());
            mgr2.setForIdDecision(decision.getIdDecision());
            mgr2.find(getTransaction());
            for (int i = 0; i < mgr2.size(); i++) {
                IJAnnexeDecision an = (IJAnnexeDecision) mgr2.getEntity(i);
                an.delete(getTransaction());
            }

            Iterator iter = documentProperties.getCopiesIterator();
            while (iter.hasNext()) {
                ICTScalableDocumentCopie copie = (ICTScalableDocumentCopie) iter.next();

                IJCopieDecision cp = new IJCopieDecision();
                cp.setSession(getSession());
                cp.setIdDecision(decision.getIdDecision());
                // cp.setCsTypeCopie(csTypeCopie);
                cp.setIdTiers(copie.getIdTiers());
                cp.setPrenomNom(copie.getPrenomNomTiers());
                cp.setIsCopieOAI(new Boolean(((CTScalableDocumentCopie) copie).isCopieOAI()));
                cp.add(getTransaction());

            }

            iter = documentProperties.getAnnexesIterator();
            while (iter.hasNext()) {
                ICTScalableDocumentAnnexe annexe = (ICTScalableDocumentAnnexe) iter.next();

                IJAnnexeDecision an = new IJAnnexeDecision();
                an.setSession(getSession());
                an.setIdDecision(decision.getIdDecision());
                an.setDescription(annexe.getLibelle());
                an.add(getTransaction());
            }
        }

    }

    private void fusionneDocumentsPRINT() throws Exception {
        JadePublishDocumentInfo info = createDocumentInfo();
        info.setPublishDocument(true);
        info.setArchiveDocument(false);
        this.mergePDF(info, false, 500, false, null);
    }

    @Override
    protected String getEMailObject() {
        return "Décision IJAI";
    }

    private String getNoCodeCaisse(String idTierCaisse) throws Exception {

        TIAdministrationManager admAdrMgr = new TIAdministrationManager();
        admAdrMgr.setSession(getSession());
        admAdrMgr.setForGenreAdministration("509001");
        admAdrMgr.setForIdTiers(idTierCaisse);
        admAdrMgr.find();

        if (admAdrMgr.size() == 1) {
            TIAdministrationViewBean admnVb = (TIAdministrationViewBean) admAdrMgr.getFirstEntity();

            if (null != admnVb) {
                return admnVb.getCodeAdministration();
            } else {
                return "";
            }

        } else {
            return "";
        }
    }

    @Override
    public ICTScalableDocumentProperties getScalableDocumentProperties() {
        return scalableDocumentProperties;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public void setScalableDocumentProperties(ICTScalableDocumentProperties scalableDocumentProperties) {
        this.scalableDocumentProperties = scalableDocumentProperties;
    }
}
