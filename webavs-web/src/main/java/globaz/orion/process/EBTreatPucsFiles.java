package globaz.orion.process;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.orion.utils.EBDanUtils;
import globaz.pavo.process.CIDeclaration;
import globaz.pavo.process.CIImportPucsFileProcess;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIRole;
import globaz.webavs.common.WebavsPublishProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.LabelCommonProvider;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.jadedb.TransactionWrapper;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.DeclarationSalaireBuilder;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.orion.service.EBEbusinessInterface;
import ch.globaz.orion.service.EBPucsFileService;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

/**
 * Processus de traitement des fichiers pucs
 * 
 * @author
 * @revision SCO 10 oct. 2011
 */
public class EBTreatPucsFiles extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String emailAdress = "";
    private String mode = "";
    private List<PucsFile> pucsEntrysToLoad = new ArrayList<PucsFile>();
    private boolean simulation = false;
    private Map<String, List<String>> pucsToMerge;
    private List<PucsFile> pucsEntrys = new ArrayList<PucsFile>();

    private List<String> idsPucsDb = new ArrayList<String>();
    private Collection<String> idMiseEnGed = new ArrayList<String>();
    private Collection<String> idValidationDeLaDs = new ArrayList<String>();

    private static EBEbusinessInterface ebusinessAccessInstance = null;

    public static void initEbusinessAccessInstance(EBEbusinessInterface instance) {
        if (EBTreatPucsFiles.ebusinessAccessInstance == null) {
            EBTreatPucsFiles.ebusinessAccessInstance = instance;
        }
    }

    public void setIdsPucsDb(List<String> idsPucsDb) {
        this.idsPucsDb = idsPucsDb;
    }

    public List<PucsFile> getPucsEntrysToLoad() {
        return pucsEntrysToLoad;
    }

    public void setPucsEntrysToLoad(List<PucsFile> pucsEntrysToLoad) {
        this.pucsEntrysToLoad = pucsEntrysToLoad;
    }

    public Collection<String> getIdMiseEnGed() {
        return idMiseEnGed;
    }

    public void setIdMiseEnGed(Collection<String> idMiseEnGed) {
        this.idMiseEnGed = idMiseEnGed;
    }

    public Collection<String> getIdValidationDeLaDs() {
        return idValidationDeLaDs;
    }

    public void setIdValidationDeLaDs(Collection<String> idValidationDeLaDs) {
        this.idValidationDeLaDs = idValidationDeLaDs;
    }

    public List<PucsFile> getPucsEntrys() {
        return pucsEntrys;
    }

    public void setPucsEntrys(List<PucsFile> pucsEntrys) {
        this.pucsEntrys = pucsEntrys;
    }

    public Map<String, List<String>> getPucsToMerge() {
        return pucsToMerge;
    }

    public void setPucsToMerge(Map<String, List<String>> pucsToMerge) {
        this.pucsToMerge = pucsToMerge;
    }

    /**
     * Création d'une déclaration de salaire vide
     * 
     * @param idAffiliation
     *            un id affiliation
     * @param annee
     *            une année de déclaration
     * @param date
     *            une date
     * @param provenance
     *            la provenance de la déclaration
     * @param idPucsFile
     *            un id de fichier pucs
     * @throws Exception
     */
    private void createDeclarationVide(String idAffiliation, String annee, String date, String provenance,
            String idPucsFile) throws Exception {
        DSDeclarationViewBean vb = new DSDeclarationViewBean();
        vb.setSession(getSession());
        vb.setAffiliationId(idAffiliation);
        vb.setAnnee(annee);
        vb.setDateRetourEff(date);
        vb.setProvenance(provenance);
        vb.setIdPucsFile(idPucsFile);
        vb.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);

        vb.add(getSession().getCurrentThreadTransaction());
    }

    /**
     * Création d'une déclaration de salaire vide<br>
     * si il n'y a pas de salaire et rien dans les compteurs, on la créé<br>
     * Sinon on met a jour la gestion des suivis.
     * 
     * @param nbSalaire
     * @param affilie
     * @param annee
     * @param date
     * @param idPucsFile
     * @throws Exception
     */
    private void gestionDeclarationSalaireSansSalaire(String nbSalaire, AFAffiliation affilie, String annee,
            String date, String idPucsFile) throws Exception {

        if ("0".equals(nbSalaire)) {
            // On crée une DS vide

            createDeclarationVide(affilie.getAffiliationId(), annee, date, DeclarationSalaireProvenance.DAN.getValue(),
                    idPucsFile);

        }
    }

    // @Override
    // public String getDescription() {
    // return getSession().getLabel("DESCRIPTION_PROCESSUS_TRAITEMENT_PUCS");
    // }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getMode() {
        return mode;
    }

    public BSession getSessionPavo() throws Exception {
        BSession local = getSession();
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return (BSession) remoteSession;

    }

    private BSession getSessionDraco() throws Exception {
        BSession local = getSession();
        BISession remoteSession = (BISession) local.getAttribute("sessionDraco");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("DRACO").newSession(local);
            local.setAttribute("sessionDraco", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return (BSession) remoteSession;

    }

    public boolean isSimulation() {
        return simulation;
    }

    /**
     * Mise à jour du mode de déclaration de salaire
     * 
     * @param idAffiliation
     *            un id affiliation
     * @param provenance
     *            une provenance
     * @throws Exception
     */
    private void majModeDeclarationSalaire(AFAffiliation aff, DeclarationSalaireProvenance provenance) throws Exception {

        String modeDeclarationSalaire = CodeSystem.DS_DAN;

        if (provenance.isDan()) {
            // pour la CCVD, si c'est mixte, on prend le code mixte
            if (CodeSystem.DECL_SAL_PRE_MIXTE.equals(aff.getDeclarationSalaire())
                    || CodeSystem.DECL_SAL_MIXTE_DAN.equals(aff.getDeclarationSalaire())) {
                modeDeclarationSalaire = CodeSystem.DECL_SAL_MIXTE_DAN;

            } else {
                modeDeclarationSalaire = CodeSystem.DS_DAN;
            }
        } else if (provenance.isPucs()) {
            modeDeclarationSalaire = CodeSystem.DS_ENVOI_PUCS;
        } else if (provenance.isSwissDec()) {
            modeDeclarationSalaire = CodeSystem.DS_SWISSDEC;
        }

        if (!aff.isNew()) {

            // On ne modifie seuelement que dans le cas ou il y a un chamgement
            if (!modeDeclarationSalaire.equals(aff.getDeclarationSalaire())) {
                aff.setDeclarationSalaire(modeDeclarationSalaire);
                // On ne passe pas dans le validate, le cas met juste à jour le mode DS et on ne veut pas d'autres
                // plausis liées à des reprises de données
                aff.wantCallValidate(false);
                aff.wantCallMethodAfter(false);
                aff.update(getSession().getCurrentThreadTransaction());
            }
        }
    }

    private boolean hasCotisationAf(String idAffilie, String canton) throws Exception {
        // Recherche coti AF pour le canton concerné
        String csCanton = CIUtil.codeUtilisateurToCodeSysteme(getTransaction(), canton, "PYCANTON", getSession());

        AFCotisationManager cotisationManager = new AFCotisationManager();
        cotisationManager.setForAffiliationId(idAffilie);
        cotisationManager.setSession(getSession());
        cotisationManager.setForAssuranceCanton(csCanton);
        cotisationManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        cotisationManager.find();
        if (cotisationManager.getSize() == 0) {
            return false;
        }
        return true;
    }

    protected boolean process() throws Exception {
        try {
            BSessionUtil.initContext(getSession(), this);
            setName(getSession().getLabel("DESCRIPTION_PROCESSUS_TRAITEMENT_PUCS"));
            if ("simulation".equals(getMode())) {
                setSimulation(true);
            }
            boolean hasError = false;
            boolean moveFile = true;

            String workDir = Jade.getInstance().getHomeDir() + "work/";
            List<PucsFileMerge> listPucsFile = PucsFileMerge.build(pucsEntrys, pucsToMerge, workDir);
            for (PucsFileMerge pucsFileMerge : listPucsFile) {
                clearErrorsWarning();
                boolean exceptionAppend = false;
                hasError = true;
                moveFile = true;
                // JadeThread.logClear();
                boolean isForSimultation = isSimulation();

                PucsFile pucsFile = pucsFileMerge.retriveFileAndMergeIfNeeded(getSession(), isSimulation());
                // On commit la transaction, on ne sait pas pourquoi mais ça permet d'éviter un deadlock.
                getSession().getCurrentThreadTransaction().commit();
                try {
                    isForSimultation = (pucsFileMerge.getPucsFile().isForTest()) || isSimulation();

                    String libelleSimulation = "";
                    if (isForSimultation) {
                        libelleSimulation = getSession().getLabel("IMPORT_PUCS_4_MODE_SIMULATION") + " - ";
                    }

                    // Récupération de l'affiliation

                    AFAffiliation aff = EBDanUtils.findAffilie(getSession(), pucsFile.getNumeroAffilie(), "31.12."
                            + pucsFile.getAnneeDeclaration(), "01.01." + pucsFile.getAnneeDeclaration());
                    // Si l'affilié est nul => envoi mail
                    if (aff == null) {
                        JadeSmtpClient.getInstance().sendMail(
                                getSession().getUserEMail(),
                                libelleSimulation + getSession().getLabel("ERREUR_MAJ_PROV_INSTIT_SUBJECT")
                                        + pucsFile.getNumeroAffilie(),
                                pucsFile.getNumeroAffilie() + " : " + getSession().getLabel("ERREUR_AFF_INACTIF")
                                        + pucsFile.getAnneeDeclaration(), null);
                        exceptionAppend = true;
                        hasError = true;
                        continue;
                    }

                    DeclarationSalaire ds = DeclarationSalaireBuilder.build(pucsFileMerge.getDomParser(), pucsFileMerge
                            .getPucsFile().getProvenance());

                    if (ds.isAfSeul()) {
                        boolean error = false;

                        IFAPassage passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                                CodeSystem.TYPE_MODULE_RELEVE);

                        if (passage == null) {
                            moveFile = false;
                            JadeSmtpClient.getInstance().sendMail(
                                    getSession().getUserEMail(),
                                    libelleSimulation
                                            + getSession().getLabel("PROCES_IMPORTATION_ERREUR_AUCUN_JOURNAL_EXISTANT")
                                            + " : " + pucsFile.getNumeroAffilie(),
                                    getSession().getLabel("PROCES_IMPORTATION_ERREUR_AUCUN_JOURNAL_EXISTANT"), null);
                            hasError = true;
                            continue;
                        }

                        Set<String> cantons = ds.resolveDistinctContant();
                        for (String canton : cantons) {
                            if (!hasCotisationAf(aff.getId(), canton)) {
                                moveFile = false;
                                _addError(getSession().getLabel("ERREUR_AUCUNE_COTISATION_AF") + " "
                                        + aff.getAffilieNumero() + " - " + getSession().getLabel("CANTON") + " "
                                        + canton);
                                handleOnError(emailAdress, null, this, pucsFileMerge);
                                error = true;

                            }
                        }
                        if (error) {
                            hasError = true;
                            continue;
                        }
                    }

                    // traitement des DS vides et mise à jour des institutions LAA/LPP pour les DS principales
                    if ((pucsFile.getProvenance().isDan() || pucsFile.getProvenance().isPucs()) && !isForSimultation
                            && pucsFile.getTypeDeclaration().isPrincipale()) {
                        Exception e1 = null;
                        try {
                            // Récupération de l'id affiliation
                            String idAffiliation = aff.getAffiliationId();

                            // Gestion d'une déclaration de salaire sans salaire
                            gestionDeclarationSalaireSansSalaire(pucsFile.getNbSalaires(), aff,
                                    pucsFile.getAnneeDeclaration(), pucsFile.getDateDeReception(),
                                    pucsFile.getFilename());
                            // Mise à jour des insitutions uniquement si on est dans une DS principale
                            if (pucsFileMerge.getPucsFileToMergded().isEmpty()) {
                                updateInstitution(pucsFile.getFilename(), idAffiliation,
                                        pucsFile.getAnneeDeclaration(), pucsFile.getProvenance());
                            } else {
                                updateInstitution(pucsFileMerge.getPucsFileToMergded().get(0).getFilename(),
                                        idAffiliation, pucsFile.getAnneeDeclaration(), pucsFile.getProvenance());
                            }
                        } catch (Exception e) {
                            e1 = e;
                        } finally {
                            if (isOnError() || getSession().hasErrors() || e1 != null
                                    || getSession().getCurrentThreadTransaction().hasErrors()) {
                                handleOnError(emailAdress, e1, this,
                                        getSession().getLabel("ERREUR_MAJ_PROV_INSTIT_ERREUR"), pucsFileMerge);
                                moveFile = false;
                                hasError = true;
                                getTransaction().rollback();
                                continue;
                            }
                        }
                    }

                    // Mise a jour du type de déclaration de salaire
                    if (!isForSimultation && pucsFile.getTypeDeclaration().isPrincipale()) {
                        majModeDeclarationSalaire(aff, pucsFile.getProvenance());
                    }
                    if (getSession().getCurrentThreadTransaction().hasErrors()) {
                        JadeSmtpClient.getInstance().sendMail(
                                getSession().getUserEMail(),
                                libelleSimulation + getSession().getLabel("ERREUR_MAJ_PROV_INSTIT_SUBJECT")
                                        + pucsFile.getNumeroAffilie(),
                                getSession().getLabel("ERREUR_MAJ_PROV_INSTIT_BODY") + aff.getAffilieNumero(), null);
                        // Si problème de rollback, on veut que ça remonte => pas de catch
                        getSession().getCurrentThreadTransaction().rollback();
                        getSession().getCurrentThreadTransaction().clearErrorBuffer();

                    } else {
                        getSession().getCurrentThreadTransaction().commit();
                    }
                    // Si on a des salaires
                    if (!"0".equals(pucsFile.getNbSalaires())) {
                        // On stop le context pour permettre l'utilisation des session définit dans les process. Sinon
                        // le frameWork utilise la session du thread context et on obtient un class cast exception
                        JadeThreadActivator.stopUsingContext(this);
                        // Création d'une déclaration
                        CIDeclaration declaration = new CIDeclaration();

                        declaration.setEMailAddress(getEmailAdress());
                        declaration.setAccepteAnneeEnCours("true");
                        declaration.setAccepteEcrituresNegatives("true");
                        declaration.setAccepteLienDraco(wantLinkDraco());
                        if ("true".equalsIgnoreCase(wantLinkDraco())) {
                            // notifier eBusiness
                        }
                        if (isForSimultation) {
                            declaration.setSimulation("simulation");
                        }
                        declaration.setType(CIDeclaration.CS_PUCS_II);
                        declaration.setIsBatch(new Boolean(true));

                        String file = workDir + pucsFile.getFilename() + ".xml";

                        declaration.setAnneeVersement(pucsFile.getAnneeVersement());
                        declaration.setDeclarationSalaireType(pucsFile.getTypeDeclaration());

                        declaration.setProvenance(pucsFile.getProvenance().getValue());
                        declaration.setNumAffilieBase(pucsFile.getNumeroAffilie());
                        declaration.setDateReceptionForced(pucsFile.getDateDeReception());
                        declaration.setSession(getSessionPavo());
                        declaration.setIdsPucsFile(pucsFileMerge.getIdsPucsFileSeparteByComma());
                        declaration.setFilename(file);
                        declaration.setValidationAutomatique(idValidationDeLaDs.contains(pucsFile.getIdDb()));
                        // on initialise le process pour ne pas avoir un nullPointer
                        CIImportPucsFileProcess ciImportPucsFileProcess = new CIImportPucsFileProcess();
                        declaration.setLauncherImportPucsFileProcess(ciImportPucsFileProcess);
                        declaration.getLauncherImportPucsFileProcess().setISession(getSession());
                        // le traitement des af seule ne traite pas correctement le mode simulation.
                        if (!isForSimultation || (isForSimultation && !pucsFile.isAfSeul())) {
                            declaration.executeProcess();
                        }
                        hasError = declaration.isOnError() || declaration.getSession().hasErrors()
                                || (declaration.getDeclaration() != null && declaration.getDeclaration().hasErrors())
                                || (declaration.getMemoryLog() != null && declaration.getMemoryLog().hasErrors())
                                || declaration.isImportPucs4OnError();
                        if (!hasError && pucsFile.isAfSeul()) {
                            for (PucsFile pf : pucsFileMerge.getPucsFileToMergded()) {
                                String filename = pf.getFilename();
                                if (pucsFile.getProvenance().isFromEbusiness()) {
                                    ebusinessAccessInstance.notifyFinishedPucsFile(filename, pucsFile.getProvenance(),
                                            getSession());
                                }
                                EBPucsFileService.comptabiliserByFilename(filename, getSession());
                            }
                        }

                        if (!declaration.isPUCS4()) {
                            recuperDcoumentEtEnvoiMail(declaration, declaration.getEMailObject());
                        }

                        Map<String, ElementsDomParser> filesPath = new HashMap<String, ElementsDomParser>();
                        try {
                            BSessionUtil.initContext(getSession(), this);

                            for (PucsFile pucs : pucsFileMerge.getPucsFileToMergded()) {
                                ElementsDomParser parser = PucsServiceImpl.buildElementDomParser(pucs.getIdDb(),
                                        getSession());
                                String filePath = PucsServiceImpl.pucFileLisiblePdf(pucsFile.getProvenance(), parser,
                                        pucs, getSession());
                                filesPath.put(filePath, parser);
                            }
                        } finally {
                            JadeThreadActivator.stopUsingContext(this);
                        }
                        if (!pucsFile.isAfSeul()) {
                            if (idValidationDeLaDs.contains(pucsFile.getIdDb()) && !isForSimultation && !hasError) {
                                ValidationAutomatique.execute(getSessionDraco(), declaration,
                                        pucsFileMerge.getDomParser());
                            }
                        }

                        if (idMiseEnGed.contains(pucsFile.getIdDb()) && !hasError) {
                            for (Entry<String, ElementsDomParser> entry : filesPath.entrySet()) {
                                genratePucsFilePdf(pucsFile, declaration, entry.getKey(), entry.getValue(),
                                        isForSimultation);
                            }
                        }
                    }
                } catch (Throwable e) {
                    exceptionAppend = true;
                    handleOnError(getEmailAdress(), e, this, pucsFileMerge);
                } finally {
                    try {
                        if (moveFile || pucsFileMerge.getPucsFile().isForTest()) {
                            if (exceptionAppend || hasError || isForSimultation) {
                                getTransaction().rollback();
                            } else {
                                getTransaction().commit();
                            }
                        }

                    } catch (Exception e) {
                        handleOnError(getEmailAdress(), e, this, pucsFileMerge);
                    }
                    if (hasError) {
                        changePucsFilesStatusToOnError(pucsFileMerge);
                    } else if (pucsFileMerge.getPucsFile().isForTest()) {
                        EBPucsFileService.comptabiliser(pucsFileMerge.getPucsFileToMergded(), getSession());
                    }
                }
            }
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
        return true;
    }

    private void clearErrorsWarning() {
        getSession().getErrors();
        getTransaction().clearErrorBuffer();
        getTransaction().clearWarningBuffer();
        getMemoryLog().clear();
        JadeThread.logClear();
    }

    private void genratePucsFilePdf(PucsFile pucsFile, CIDeclaration declaration, String filePath,
            ElementsDomParser parser, boolean isForSimultation) throws Exception, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {

        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();

        pubInfos.setDocumentTitle("PUCSFile");
        pubInfos.setDocumentSubject(getSession().getLabel("PROCES_GENERATION_PUCS_LISIBLE") + ": "
                + pucsFile.getNumeroAffilie());
        pubInfos.setOwnerEmail(getEmailAdress());
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdress());
        AFAffiliation affiliation = null;
        // pour le cas du mode en simulation et aussi pour les af seul l'affiliation est null !!!!!!
        if (isForSimultation || declaration.getDeclaration() == null) {
            List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(
                    Arrays.asList(pucsFile.getNumeroAffilie()), getSession());
            if (!affiliations.isEmpty()) {
                affiliation = affiliations.get(0);
            } else {
                throw new RuntimeException("No affiliation found: " + pucsFile.getNumeroAffilie());
            }
        } else {
            affiliation = declaration.getDeclaration().getAffiliation();
        }

        String codeSecurity = affiliation.getAccesSecurite();

        boolean hasUserRight = PucsServiceImpl.userHasRight(affiliation, getSession());

        int affilieSecurity = Integer.parseInt(codeSecurity.substring(codeSecurity.length() - 1));
        if (JadeGedFacade.isInstalled() && !isForSimultation && affilieSecurity == 0) {
            pubInfos.setArchiveDocument(true);
        } else {
            pubInfos.setArchiveDocument(false);
        }

        if (hasUserRight && (affilieSecurity != 0 || !JadeGedFacade.isInstalled())) {
            pubInfos.setPublishDocument(true);
        }

        pubInfos.setDocumentProperty(WebavsPublishProperties.TIERS_NUMERO_AFFILIE_FORMATTE, pucsFile.getNumeroAffilie());
        pubInfos.setDocumentProperty(WebavsPublishProperties.TIERS_NUMERO_AFFILIE_NON_FORMATTE,
                CEUtils.unFormatNumeroAffilie(getSession(), pucsFile.getNumeroAffilie()));

        pubInfos.setDocumentProperty("annee", pucsFile.getAnneeDeclaration());

        // K160113_002 - renseigner qqch pour les besoins du connecteur GED
        pubInfos.setDocumentProperty("babel.type.id", "EBU");
        pubInfos.setDocumentProperty("osiris.section.idExterne", pucsFile.getAnneeDeclaration());

        try {
            TIDocumentInfoHelper.fill(pubInfos, affiliation.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    pucsFile.getNumeroAffilie(),
                    pubInfos.getDocumentProperty(WebavsPublishProperties.TIERS_NUMERO_AFFILIE_NON_FORMATTE));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "afterPrintDocument()", e);
        }

        pubInfos.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfos.setDocumentTypeNumber(PucsServiceImpl.NUMERO_INFORM_PUCS_LISIBLE);
        JadePublishDocument publishDoc = new JadePublishDocument(filePath, pubInfos);
        JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(publishDoc));
    }

    private void recuperDcoumentEtEnvoiMail(BProcess process, String mailObject) throws Exception {
        Object[] attachedDocumentArray = process.getAttachedDocuments().toArray();
        String[] attachedDocumentLocationArray = null;
        if (attachedDocumentArray != null) {
            int nbElem = attachedDocumentArray.length;
            attachedDocumentLocationArray = new String[nbElem];
            for (int k = 0; k < nbElem; k++) {
                attachedDocumentLocationArray[k] = ((JadePublishDocument) attachedDocumentArray[k])
                        .getDocumentLocation();
            }
        }
        JadeSmtpClient.getInstance().sendMail(process.getEMailAddress(), mailObject, process.getSubjectDetail(),
                attachedDocumentLocationArray);
    }

    private void handleOnError(String mail, Throwable e, BProcess proces, PucsFileMerge fileMerge) throws Exception {
        handleOnError(mail, e, proces, "", fileMerge);
    }

    /**
     * Traitement global des erreurs pour la gestion des fichiers en erreurs.
     * Si une erreur se produit :
     * <ul>
     * <li>Le/les fichiers PUCS concernés sont passés à l'état {@link EtatPucsFile#EN_ERREUR}
     * <li>Un mail est envoyé à l'utilisteur avec le protocole des erreurs
     * 
     * @param mail
     * @param e
     * @param proces
     * @param
     * @param fileMerge
     * @throws Exception
     */
    private void handleOnError(String mail, Throwable e, BProcess proces, String messageInfo, PucsFileMerge fileMerge)
            throws Exception {
        changePucsFilesStatusToOnError(fileMerge);
        sendMailError1(mail, e, proces, messageInfo, fileMerge);
    }

    /**
     * Démarrage d'une nouvelle transaction afin de mettre les fichiers relatifs au PucsFileMerge en erreur.
     * 
     * @param pucsFileMerge Ensemble de fichiers à mettre en erreur
     * @throws Exception
     */
    private void changePucsFilesStatusToOnError(PucsFileMerge pucsFileMerge) throws Exception {
        if (!isSimulation()) {
            // On utilise le TransactionWrapper afin de supprimer toute les erreurs de la session.
            // La transaction créée en tant que tel n'est pas utilisée. Sinon un deadlock est présent dans le cas où
            // l'on utilise une nouvelle transaction
            TransactionWrapper transaction = TransactionWrapper.forforceCommit(getSession());
            EBPucsFileService.enErreur(pucsFileMerge.getPucsFileToMergded(), getSession());
            transaction.close();
        }
    }

    private void sendMailError1(String mail, Throwable e, BProcess proces, String messageInfo, PucsFileMerge fileMerge)
            throws Exception {
        String isoLangue = proces.getSession().getIdLangueISO();
        String numAffile = "";
        fileMerge.clearDomParser();
        if (fileMerge.getPucsFile() != null) {
            numAffile = fileMerge.getPucsFile().getNumeroAffilie();
        }

        String body = messageInfo + "\n";
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
            String message = "";
            for (JadeBusinessMessage jadeBusinessMessage : messages) {
                message = message
                        + JadeI18n.getInstance().getMessage(getSession().getIdLangueISO(),
                                jadeBusinessMessage.getMessageId()) + "\n";
            }
            body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + message;
        } else {
            body = body + getSession().getErrors() + getTransaction().getErrors();
            if (e != null) {
                body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + e.getMessage();
            }
        }

        body = body + "\n\n" + LabelCommonProvider.getLabel("PROCESS_TEXT_MAIL_ERROR", isoLangue);

        body = body + "\n\n\n********************* "
                + LabelCommonProvider.getLabel("PROCESS_INFORMATION_GLOBAZ", isoLangue) + "*********************\n\n";
        String bodyGlobaz = "";
        if (e != null) {
            bodyGlobaz = bodyGlobaz + "Stack: \t " + Throwables.getStackTraceAsString(e) + "\n\n";
        }
        // new GsonBuilder().setPrettyPrinting().create()

        try {
            bodyGlobaz = bodyGlobaz + "fileMerge:\t " + new Gson().toJson(fileMerge) + "\n\n";
        } catch (Throwable e1) {
            e1.printStackTrace();
        }

        try {
            bodyGlobaz = bodyGlobaz + "Params:\t " + new Gson().toJson(this) + "\n\n";
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            if (JadeThread.logHasMessagesToLevel(JadeBusinessMessageLevels.ERROR)) {
                bodyGlobaz = bodyGlobaz + "Thread messages: "
                        + new Gson().toJson(JadeThread.logMessagesToLevel(JadeBusinessMessageLevels.ERROR)) + "\n\n";
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        body = body + bodyGlobaz;
        JadeSmtpClient.getInstance().sendMail(
                mail,
                proces.getName() + " - " + LabelCommonProvider.getLabel("PROCESS_IN_ERROR", isoLangue) + " "
                        + numAffile, body, null);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    /**
     * Mise à jour des institutions LAA, LPP
     * 
     * @param numAffilie
     * @param idAffiliation
     * @param annee
     * @param declarationSalaireProvenance
     * @throws Exception
     */
    private void updateInstitution(String id, String idAffiliation, String annee,
            DeclarationSalaireProvenance declarationSalaireProvenance) throws Exception {

        String idTiersLaa = null;
        String idTiersLpp = null;
        Integer idLaa;
        Integer idLpp;
        if (declarationSalaireProvenance.isDan()) {
            idLaa = DanServiceImpl.getInstitution(Integer.parseInt(EBDanUtils.LAA), Integer.parseInt(id), getSession());
            idLpp = DanServiceImpl.getInstitution(Integer.parseInt(EBDanUtils.LPP), Integer.parseInt(id), getSession());
        } else if (declarationSalaireProvenance.isPucs()) {
            idLaa = PucsServiceImpl.findIdInstitution(Integer.valueOf(EBDanUtils.LAA), Integer.valueOf(id),
                    getSession());
            idLpp = PucsServiceImpl.findIdInstitution(Integer.valueOf(EBDanUtils.LPP), Integer.valueOf(id),
                    getSession());
        } else {
            throw new RuntimeException("Type provenance not handle :" + declarationSalaireProvenance);
        }

        if (idLaa != null) {
            idTiersLaa = idLaa.toString();
        }

        if (idLpp != null) {
            idTiersLpp = idLpp.toString();
        }

        // On s'occupe du suivi LAA
        // ***************************
        // Recherche si l'affilié a une caisse LAA pour l'année précédente
        updateCaisse(idAffiliation, annee, idTiersLaa, EBDanPreRemplissage.GENRE_CAISSE_LAA);

        // On s'occupe du suivi LPP
        // ***************************
        // Recherche si l'affilié a une caisse lpp pour l'année précédente
        updateCaisse(idAffiliation, annee, idTiersLpp, EBDanPreRemplissage.GENRE_CAISSE_LPP);
    }

    private void updateCaisse(String idAffiliation, String annee, String idTiers, String csGenre) throws Exception {

        String anneePrecedente = EBDanUtils.getAnneePrecedente(annee);
        AFSuiviCaisseAffiliationManager caisseManager = new AFSuiviCaisseAffiliationManager();

        caisseManager.setSession(getSession());
        caisseManager.setForAffiliationId(idAffiliation);
        caisseManager.setForGenreCaisse(csGenre);
        caisseManager.setOrder("MYDDEB desc");
        caisseManager.setForAnnee(anneePrecedente);
        caisseManager.find(getSession().getCurrentThreadTransaction());
        ch.globaz.common.domaine.Date date = null;
        // Si présence d'une caisse
        boolean createSuivi = false;
        if (!caisseManager.isEmpty()) {
            AFSuiviCaisseAffiliation caisse = (AFSuiviCaisseAffiliation) caisseManager.getFirstEntity();
            if (!JadeStringUtil.isBlankOrZero(caisse.getDateDebut())) {
                date = new ch.globaz.common.domaine.Date(caisse.getDateDebut());
            }
            // Si c'est pas la meme que la déclaration, on la met à jour
            if (date == null || (date.getYear() <= Integer.valueOf(anneePrecedente))) {
                if (idTiers != null && !caisse.getIdTiersCaisse().equals(idTiers)) {
                    caisse.setDateFin("31.12." + anneePrecedente);
                    createSuivi = true;
                    caisse.update(getSession().getCurrentThreadTransaction());
                }
            }
        }

        // Si il y a une caisse laa et qu'elle est différente de celle déjà renseigneée (si renseignée)
        if (createSuivi && !JadeStringUtil.isEmpty(idTiers)) {
            // Création d'un nouveau suivi de caisse pour l'année en cours
            AFSuiviCaisseAffiliation caisse = new AFSuiviCaisseAffiliation();
            caisse.setDateDebut("01.01." + annee);

            caisse.setGenreCaisse(csGenre);
            caisse.setIdTiersCaisse(idTiers);
            caisse.setAffiliationId(idAffiliation);
            caisse.add(getSession().getCurrentThreadTransaction());
        }
    }

    private String wantLinkDraco() throws Exception {
        return ((EBApplication) getSession().getApplication()).wantLinkDraco();
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        return process();
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;

    }
}
