package globaz.draco.process;

import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSExportDonneesDistantes;
import globaz.draco.db.declaration.DSLigneDeclarationListViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.draco.services.DSDeclarationServices;
import globaz.draco.util.DSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.nombreAssures.AFNombreAssures;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.pavo.db.inscriptions.CIJournal;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.service.EBEbusinessInterface;
import ch.globaz.orion.service.EBPucsFileService;
import com.google.common.base.Splitter;

public class DSProcessValidation extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -3353019454893928164L;
    public final static String CS_DECL_MIXTE = "19170000";
    private static EBEbusinessInterface ebusinessAccessInstance = null;
    private final String CS_PARAMETRES_IM = "10800042";
    private boolean forceEnvoiMail = false;
    private String idDeclaration;
    private List<PucsFile> pucsFileMergded = new ArrayList<PucsFile>();

    public String noAffilie = "";

    private Boolean notImpressionDecFinalAZero = new Boolean(false);

    /**
     * Commentaire relatif au constructeur DSProcessValidation.
     */
    public DSProcessValidation() {
        super();
    }

    /**
     * Commentaire relatif au constructeur DSProcessValidation.
     * 
     * @param parent globaz.framework.process.FWProcess
     */
    public DSProcessValidation(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur DSProcessValidation.
     * 
     * @param session globaz.globall.db.BSession
     */
    public DSProcessValidation(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        // Sous contrôle d'exceptions
        try {
            // Récupérer la déclaration
            DSDeclarationViewBean decl = new DSDeclarationViewBean();
            decl.setSession(getSession());
            decl.setIdDeclaration(getIdDeclaration());
            decl.retrieve(getTransaction());
            // Contrôler si la déclaration peut être lue
            if (decl.hasErrors() || decl.isNew()) {

                getMemoryLog().logMessage(getSession().getLabel("DECL_NON_EXISTANTE"), FWMessage.ERREUR, "");
                abort();
                return false;
            }
            noAffilie = decl.getNumeroAffilie();
            // Contrôler l'état de la déclaration
            if (!decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {

                getMemoryLog().logMessage(getSession().getLabel("DECL_NON_OUVERTE_POUR_VALIDATION"), FWMessage.ERREUR,
                        "");
                abort();
                return false;
            }
            /***
             * Plausi 1-5-4-1, un décompte complémentaire ne peut être fait que si un 13 existe en compta Plausi
             * salaires différés
             */
            if (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())) {

                if (!existDecompteFinal(decl, getSession())) {
                    this._addError(getTransaction(), getSession().getLabel("MSG_COMPLEMENT_IMPOSSIBLE"));
                    abort();
                    return false;
                }
            }

            // Plausibilité pour le complément LTN -> on ne peut pas avoir de complément LTN(38) si il n'existe pas de
            // decompte LTN(33) en comptabilité
            if (DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())
                    && !existDecompteLtn(decl, getSession())) {
                this._addError(getTransaction(), getSession().getLabel("MSG_COMPLEMENT_IMPOSSIBLE"));
                abort();
                return false;
            }

            // Vérification que les plafonds LTN (affilié et assuré) ne sont pas dépassés
            if (DSDeclarationViewBean.CS_LTN.equals(decl.getTypeDeclaration())
                    || DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())) {
                // Création d'un hashmap qui va contenir toutes les inscriptions individuelles pour l'affilié (33 et 38)
                HashMap<String, ArrayList<DSInscriptionsIndividuelles>> inscriptionsIndividuellesMap = new HashMap<String, ArrayList<DSInscriptionsIndividuelles>>();

                // Récupération du 33 et du ou des 38 pour cet affilié et cette année
                DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
                declarationManager.setSession(getSession());
                declarationManager.setForAffiliationId(decl.getAffiliationId());

                List<String> typeDeclarations = new ArrayList<String>();
                typeDeclarations.add(DSDeclarationViewBean.CS_LTN);
                typeDeclarations.add(DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE);

                declarationManager.setInTypeDeclaration(typeDeclarations);
                declarationManager.setForAnnee(decl.getAnnee());
                try {
                    declarationManager.find();
                } catch (Exception e1) {
                    throw new FWIException("unable to find complement declaration" + e1.getMessage());
                }

                // Parcours chaque déclaration
                if (declarationManager.size() > 0) {
                    for (int i = 0; i < declarationManager.size(); i++) {
                        DSDeclarationViewBean declaration = (DSDeclarationViewBean) declarationManager.get(i);
                        DSInscriptionsIndividuellesManager inscriptionIndManager = new DSInscriptionsIndividuellesManager();
                        inscriptionIndManager.setSession(getSession());
                        inscriptionIndManager.setForIdDeclaration(declaration.getIdDeclaration());
                        try {
                            inscriptionIndManager.find(BManager.SIZE_NOLIMIT);
                        } catch (Exception e) {
                            throw new FWIException("unable to find complement inscription" + e.getMessage());
                        }

                        // on parcours les insriptions et on insère dans le hashmap
                        for (int j = 0; j < inscriptionIndManager.size(); j++) {
                            DSInscriptionsIndividuelles inscription = (DSInscriptionsIndividuelles) inscriptionIndManager
                                    .get(j);
                            if (inscriptionsIndividuellesMap.containsKey(inscription.getNumeroAvs())) {
                                // on insère à la bonne place dans le hashmap
                                inscriptionsIndividuellesMap.get(inscription.getNumeroAvs()).add(inscription);
                            } else {
                                // création d'une nouvelle key
                                ArrayList<DSInscriptionsIndividuelles> inscriptionArray = new ArrayList<DSInscriptionsIndividuelles>();
                                inscriptionArray.add(inscription);
                                inscriptionsIndividuellesMap.put(inscription.getNumeroAvs(), inscriptionArray);
                            }
                        }
                    }
                }

                // Recherche des plafonds LTN
                BigDecimal plafondLtnAffilie = new BigDecimal("0");
                BigDecimal plafondLtnAssure = new BigDecimal("0");
                FWFindParameterManager param = new FWFindParameterManager();
                param.setSession(getSession());
                param.setIdApplParametre(getSession().getApplicationId());
                param.setIdCodeSysteme(DSDeclarationViewBean.CS_PLAFOND_LTN_AFFILIE);
                param.setIdCleDiffere("PLALTNAFF");
                param.setIdActeurParametre("0");
                param.setPlageValDeParametre("0");
                param.setDateDebutValidite("01.01." + decl.getAnnee());
                param.find();
                if (param.size() > 0) {
                    plafondLtnAffilie = new BigDecimal(
                            ((FWFindParameter) param.getFirstEntity()).getValeurNumParametre());
                }

                FWFindParameterManager param2 = new FWFindParameterManager();
                param2.setSession(getSession());
                param2.setIdApplParametre(getSession().getApplicationId());
                param2.setIdCodeSysteme(DSDeclarationViewBean.CS_PLAFOND_LTN_ASSURE);
                param2.setIdCleDiffere("PLALTNASS");
                param2.setIdActeurParametre("0");
                param2.setPlageValDeParametre("0");
                param2.setDateDebutValidite("01.01." + decl.getAnnee());
                param2.find();
                if (param2.size() > 0) {
                    plafondLtnAssure = new BigDecimal(
                            ((FWFindParameter) param2.getFirstEntity()).getValeurNumParametre());
                }

                // Parcours du hashmap pour déterminer si les plafonds sont dépassé ou non
                Set<String> keys = inscriptionsIndividuellesMap.keySet();
                Iterator<String> iterator = keys.iterator();
                boolean plafondLtnAffilieDepasse = false;
                boolean plafondLtnAssureDepasse = false;
                BigDecimal sommeTotale = new BigDecimal("0.00");
                while (iterator.hasNext()) {
                    BigDecimal sommeAssure = new BigDecimal("0.00");
                    String key = iterator.next();
                    ArrayList<DSInscriptionsIndividuelles> inscriptionIndArray = inscriptionsIndividuellesMap.get(key);
                    for (DSInscriptionsIndividuelles inscriptionInd : inscriptionIndArray) {
                        if (inscriptionInd.getGenreEcriture().equals("11")) {
                            sommeAssure = sommeAssure.subtract(new BigDecimal(inscriptionInd.getMontant()));
                            sommeTotale = sommeTotale.subtract(new BigDecimal(inscriptionInd.getMontant()));
                        } else {
                            sommeAssure = sommeAssure.add(new BigDecimal(inscriptionInd.getMontant()));
                            sommeTotale = sommeTotale.add(new BigDecimal(inscriptionInd.getMontant()));
                        }
                    }
                    if (sommeAssure.compareTo(plafondLtnAssure) == 1) {
                        plafondLtnAssureDepasse = true;
                        this._addError(getTransaction(),
                                getSession().getLabel("PLAFOND_LTN_ASSURE") + ": " + NSUtil.formatAVSUnknown(key));
                    }
                }
                if (sommeTotale.compareTo(plafondLtnAffilie) == 1) {
                    plafondLtnAffilieDepasse = true;
                    this._addError(getTransaction(), getSession().getLabel("PLAFOND_LTN_AFFILIE"));
                }
                if (plafondLtnAffilieDepasse || plafondLtnAssureDepasse) {
                    abort();
                    return false;
                }
            }

            // Si il d'agit d'une déclaration complémentaire LTN (38), on Détague de la princpale LTN (33) afin de
            // pouvoir réimprimer l'attestation avec les bonnes valeurs
            if (DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())) {
                // rechercher la déclaration principale (33) liée à cette complémentaire
                DSDeclarationListViewBean declarationPrincipaleManager = new DSDeclarationListViewBean();
                declarationPrincipaleManager.setSession(getSession());
                declarationPrincipaleManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN);
                declarationPrincipaleManager.setForAffiliationId(decl.getAffiliationId());
                declarationPrincipaleManager.setForAnnee(decl.getAnnee());
                declarationPrincipaleManager.find();
                if (declarationPrincipaleManager.size() > 0) {
                    DSDeclarationViewBean declarationPrincipale = (DSDeclarationViewBean) declarationPrincipaleManager
                            .getFirstEntity();
                    // On détague la DS princpale (33)
                    // valeur à 0 pour les attestations fiscales
                    declarationPrincipale.setDateImpressionAttestations("0");
                    // si la déclaration 33 a déjà été traitée dans un décompte (contient une date d'impression)
                    // on renseigne le champs avec la date 11.11.1111 (permet au programme de savoir que des compléments
                    // existent pour cette 33 mais que les montants de cette dernière ne doivent pas être pris en compte
                    // pour le calcule)
                    if (!JadeStringUtil.isBlankOrZero(declarationPrincipale.getDateImpressionDecompteImpots())) {
                        declarationPrincipale.setDateImpressionDecompteImpots("11111111");
                    }
                    declarationPrincipale.wantCallValidate(false);
                    declarationPrincipale.wantCallMethodBefore(false);
                    declarationPrincipale.update(getTransaction());
                }
            }

            // Si un total de contrôle a été saisi, il doit correspondre au total calculé
            // if(!JadeStringUtil.isDecimalEmpty(decl.getTotalControleDS())){
            BigDecimal controle = new BigDecimal(decl.getTotalControleDS());
            BigDecimal calcule = new BigDecimal("0.00");
            try {
                CIJournal journal = new CIJournal();
                if (!JadeStringUtil.isIntegerEmpty(decl.getIdJournal())) {
                    journal = new CIJournal();
                    journal.setSession((BSession) getSessionCI(getSession()));
                    journal.setIdJournal(decl.getIdJournal());
                    journal.retrieve();
                    if (!journal.isNew()) {
                        calcule = new BigDecimal(journal.getTotalInscrit());
                    }
                }

            } catch (Exception e) {
                calcule = new BigDecimal("0.00");
            }
            if (controle.compareTo(calcule) != 0) {
                this._addError(getTransaction(), getSession().getLabel("PLAUSI_CONTROLE_NE_CALCULE"));
                abort();
                return false;
            }
            // Si AGRIVIT, pas de contrôle, car il n'y a pas de saisie individuelle
            // Pour CCVD, s'il n'y pas de saisie individuelle => validation possible pour réd pfa
            if (JadeStringUtil.isBlankOrZero(decl.getIdDeclarationDistante())) {
                if (!"true".equalsIgnoreCase((getSession().getApplication()).getProperty("bonusPFA"))
                        || hasSaisieIndividuelle()) {
                    BigDecimal masseAvs = new BigDecimal(decl.getMasseSalTotalWhitoutFormat());
                    if (masseAvs.compareTo(calcule) != 0) {
                        this._addError(getTransaction(), getSession().getLabel("PLAUSI_CONTROLE_NE_CALCULE"));
                        abort();
                        return false;
                    }
                }
            }
            // }
            // Lecture des lignes de la déclaration
            DSLigneDeclarationListViewBean ligneManager = new DSLigneDeclarationListViewBean();
            ligneManager.setSession(getSession());
            ligneManager.setForIdDeclaration(getIdDeclaration());
            ligneManager.find(getTransaction());
            // Si on ne trouve pas les lignes, on sort
            if (ligneManager.isEmpty() || ligneManager.hasErrors()) {
                getMemoryLog().logMessage(getSession().getLabel("DECL_PAS_LIGNE_DECLARATION"), FWMessage.ERREUR, "");
                abort();
                return false;
            }
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(decl.getTypeDeclaration())) {
                if ("true".equals(getSession().getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))) {
                    for (int i = 0; i < ligneManager.size(); i++) {
                        DSLigneDeclarationViewBean ligne = (DSLigneDeclarationViewBean) ligneManager.get(i);
                        String annee = "";
                        if (JadeStringUtil.isBlankOrZero(decl.getAnnee())) {
                            annee = ligne.getAnneCotisation();
                        } else {
                            annee = decl.getAnnee();
                        }
                        AFTauxAssurance taux = ligne.getTauxLigne("31.12." + annee);
                        // Modif. calcul du taux moyen pour report dans NAOS

                        if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(taux.getGenreValeur())) {
                            AFCalculAssurance.calculTauxMoyen((BSession) getSessionNaos(getSession()), decl
                                    .getAffiliation().getAffiliationId(), ligne.getAssuranceId(), ligne
                                    .getMontantDeclaration(), annee);
                        }

                    }
                }

            }

            // Contrôle LAA/LPP
            if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(decl.getTypeDeclaration())) {

                AFAffiliation affilie = decl.getAffiliation();
                if (!affilie.isNew()) {

                    // Gestion LAA
                    if (!JadeStringUtil.isBlankOrZero(decl.getMasseSalTotal())
                            && !AFAffiliationUtil.hasCaissseLAA(affilie, decl.getAnnee())) {
                        affilie.envoiCtrlLAA();
                    }

                    // Gestion LPP
                    DSApplication application = null;
                    application = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(
                            DSApplication.DEFAULT_APPLICATION_DRACO);
                    if ((application != null) && application.isGestionLppDansValidationDS()
                            && !DSDeclarationViewBean.CS_LTN.equals(decl.getTypeDeclaration())) {
                        if (!AFAffiliationUtil.hasCaissseLPP(affilie, decl.getAnnee())) {

                            // Recherche du seuil pour déclenchement suivi lpp
                            FWFindParameterManager param = new FWFindParameterManager();
                            param.setSession(getSession());
                            param.setIdApplParametre(getSession().getApplicationId());
                            param.setIdCodeSysteme(CS_PARAMETRES_IM);
                            param.setIdCleDiffere("SEUILLPP");
                            param.setIdActeurParametre("0");
                            param.setPlageValDeParametre("0");
                            param.setDateDebutValidite("01.01." + decl.getAnnee());
                            param.find();
                            String montantSeuil = ((FWFindParameter) param.getFirstEntity()).getValeurNumParametre();

                            BPreparedStatement preparedStaChercheAssure = new BPreparedStatement(getTransaction());
                            preparedStaChercheAssure.prepareStatement(getSqlChercheEmployeSoumisLpp());
                            preparedStaChercheAssure.clearParameters();
                            preparedStaChercheAssure.setString(1, decl.getIdDeclaration());
                            preparedStaChercheAssure.setString(2, montantSeuil);
                            ResultSet resultAssure = preparedStaChercheAssure.executeQuery();
                            if (resultAssure.next()) {
                                BigDecimal bigResult = resultAssure.getBigDecimal(1);
                                // si on a trouvé au moins 1 employé devant être soumis à la LPP
                                if (bigResult.intValue() > 0) {
                                    affilie.envoiCtrlLPP(getSessionAf(getSession()).getApplication(), this);
                                }
                            }
                        }
                    }
                }
            }
            if ((!JadeStringUtil.isIntegerEmpty(decl.getNbPersonnel()) || (decl.nombreInscriptions() > 0))
                    && DSDeclarationViewBean.CS_PRINCIPALE.equals(decl.getTypeDeclaration())) {
                // Lors de la validation, on met à jour le nb d'employé dans l'affiliation
                AFCotisationManager cotisations = new AFCotisationManager();
                cotisations.setSession((BSession) getSessionNaos(getSession()));
                cotisations.setForAffiliationId(decl.getAffiliationId());
                cotisations.setForNotMotifFin(globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION);
                cotisations.setForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                cotisations.setForAnneeActive(decl.getAnnee());
                cotisations.find(getTransaction());
                for (int i = 0; i < cotisations.size(); i++) {
                    AFCotisation coti = (AFCotisation) cotisations.getEntity(i);
                    // Si le comptage existe déjà pour l'année et l'affilié, on met à jour, sinon on ajoute
                    AFNombreAssuresManager mgrNbr = new AFNombreAssuresManager();
                    mgrNbr.setSession((BSession) getSessionNaos(getSession()));
                    mgrNbr.setForAffiliationId(decl.getAffiliation().getAffiliationId());
                    mgrNbr.setForAnnee(decl.getAnnee());
                    mgrNbr.setForAssuranceId(coti.getAssuranceId());
                    mgrNbr.find(getTransaction());
                    AFNombreAssures nbAssure = null;
                    String nbPersonnel = decl.getNbPersonnel();
                    if (JadeStringUtil.isBlankOrZero(nbPersonnel)) {
                        nbPersonnel = String.valueOf(DSDeclarationServices.countNbrAssuresByCantonForDeclararion(
                                getSession(), coti.getAssurance().getAssuranceCanton(), decl.getIdDeclaration()));
                    }
                    if (!JadeStringUtil.isBlankOrZero(nbPersonnel)) {
                        if (mgrNbr.size() > 0) {
                            nbAssure = (AFNombreAssures) mgrNbr.getFirstEntity();
                            nbAssure.setNbrAssures(nbPersonnel);
                            nbAssure.update(getTransaction());
                        } else {
                            nbAssure = new AFNombreAssures();
                            nbAssure.setSession((BSession) getSessionNaos(getSession()));
                            nbAssure.setAssuranceId(coti.getAssuranceId());
                            nbAssure.setAnnee(decl.getAnnee());
                            nbAssure.setAffiliationId(decl.getAffiliationId());
                            nbAssure.setNbrAssures(nbPersonnel);
                            nbAssure.add(getTransaction());
                        }
                    }
                }
            }
            // Mise à jour de l'état de la déclaration

            if (DSProcessValidation.CS_DECL_MIXTE.equals(decl.getAffiliation().getDeclarationSalaire())
                    || CodeSystem.DECL_SAL_MIXTE_DAN.equals(decl.getAffiliation().getDeclarationSalaire())) {
                DSExportDonneesDistantes exportDonnees = new DSExportDonneesDistantes();
                exportDonnees.setSession(getSession());
                exportDonnees.setIdDeclaration(idDeclaration);
                exportDonnees.setAffiliationId(decl.getAffiliationId());
                // exportDonnees.setAnnee(decl.getAnnee());
                // exportDonnees.
                exportDonnees.exportMasses();
            }

            /**
             * InfoRom336
             */
            decl.setNotImpressionDecFinalAZero(getNotImpressionDecFinalAZero());
            decl.setEtat(DSDeclarationViewBean.CS_AFACTURER);
            decl.setEnValidation(true);
            decl.setValidationSpy(getSession().getUserFullName());
            decl.setReferenceFacture(getSession().getUserId());
            decl.setValidationDateSpy(JACalendar.todayJJsMMsAAAA());
            decl.update(getTransaction());
            if (decl.hasErrors() || getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getLabel("ERREUR_UPDATE_DECLARATION"), FWMessage.ERREUR, "");
                return false;
            }

            // ------------------------
            // Notification EBusiness
            // ------------------------

            if (!getSession().hasErrors() && !isOnError()) {
                if (pucsFileMergded.isEmpty()) {
                    DeclarationSalaireProvenance provenance = DeclarationSalaireProvenance
                            .fromValueWithOutException(decl.getProvenance());

                    notificationEBusiness(provenance, decl.getIdPucsFile());
                } else {
                    for (PucsFile pucsFile : pucsFileMergded) {
                        notificationEBusiness(pucsFile.getProvenance(), pucsFile.getFilename());
                    }
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);
            return false;
        }
        if (getTransaction().hasErrors()) {
            abort();
        }
        if (!isOnError() && !getForceEnvoiMail()) {
            setSendCompletionMail(false);
        }
        return !isOnError();
    }

    /**
     * Validation
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendMailOnError(true);
        setSendCompletionMail(true);
        // modif jmc 27.06.2006, reprise de l'adresse saisie à l'écran
        // setEMailAddress(getSession().getUserEMail());
    }

    /**
     * Indique si il existe un décompte final(13) en comptabilité
     * 
     * @param declaration
     * @param session
     * @return
     */
    public boolean existDecompteFinal(DSDeclarationViewBean declaration, BSession session) {
        try {
            // Pour éviter les reprises de données incomplètes, on plausibilise depuis une année.
            if (Integer.parseInt(declaration.getAnnee()) < DSUtil.getAnneePlausiDecompte18(session)) {
                return true;
            }

            CACompteAnnexe compteAnnexe = declaration.getCompteAnnexe();
            if (compteAnnexe == null) {
                return false;
            }
            String idCompteAnnexe = compteAnnexe.getIdCompteAnnexe();
            CASectionManager sectionMgrForPlausi = new CASectionManager();
            sectionMgrForPlausi.setSession(getSession());
            sectionMgrForPlausi.setForIdCompteAnnexe(idCompteAnnexe);
            sectionMgrForPlausi.setForCategorieSection(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL);
            sectionMgrForPlausi.setLikeIdExterne(declaration.getAnnee());
            return sectionMgrForPlausi.getCount() > 0;
        } catch (Exception e) {
            // Volontaire : on ne veut pas bloquer l'utilisateur en cas de problème...
            JadeLogger.error(this, e);
            return true;
        }
    }

    /**
     * Indique si il existe un décompte LTN(33) en comptabilité
     * 
     * @param declaration
     * @param session
     * @return
     */
    public boolean existDecompteLtn(DSDeclarationViewBean declaration, BSession session) {
        try {
            // Pour éviter les reprises de données incomplètes, on plausibilise depuis une année.
            if (Integer.parseInt(declaration.getAnnee()) < DSUtil.getAnneePlausiDecompte18(session)) {
                return true;
            }

            CACompteAnnexe compteAnnexe = declaration.getCompteAnnexe();
            if (compteAnnexe == null) {
                return false;
            }
            String idCompteAnnexe = compteAnnexe.getIdCompteAnnexe();
            CASectionManager sectionMgrForPlausi = new CASectionManager();
            sectionMgrForPlausi.setSession(getSession());
            sectionMgrForPlausi.setForIdCompteAnnexe(idCompteAnnexe);
            sectionMgrForPlausi.setForCategorieSection(APISection.ID_CATEGORIE_SECTION_LTN);
            sectionMgrForPlausi.setLikeIdExterne(declaration.getAnnee());
            return sectionMgrForPlausi.getCount() > 0;
        } catch (Exception e) {
            // Volontaire : on ne veut pas bloquer l'utilisateur en cas de problème...
            JadeLogger.error(this, e);
            return true;
        }
    }

    /**
     * Envoi d'un Email pour les informations conernant la fin du process
     * 
     * @see BProcess#getEMailObject()
     * @return java.lang.String
     */
    @Override
    public String getEMailObject() {
        if (isOnError() || isAborted()) {
            return getSession().getLabel("MSG_VALIDATION_ECHEC") + " " + noAffilie;
        } else {
            return getSession().getLabel("MSG_VALIDATION_SUCCES");
        }
    }

    public boolean getForceEnvoiMail() {
        return forceEnvoiMail;
    }

    /**
     * Returns the idDeclaration.
     * 
     * @return String
     */
    public String getIdDeclaration() {
        return idDeclaration;
    }

    public Boolean getNotImpressionDecFinalAZero() {
        return notImpressionDecFinalAZero;
    }

    public BSession getSessionAf(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionNaos");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("NAOS").newSession(local);
            local.setAttribute("sessionNaos", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return (BSession) remoteSession;
    }

    public BISession getSessionCI(BSession local) throws Exception {
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
        return remoteSession;
    }

    public BISession getSessionNaos(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionNaos");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("NAOS").newSession(local);
            local.setAttribute("sessionNaos", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Retourne une requête SQL permettant de récupérer les inscriptions individuelles justifiant un control LPP
     * 
     * @return
     */
    private String getSqlChercheEmployeSoumisLpp() {
        // REQUETE SANS LES JOURS
        // select * from ccjuweb.DSINDP I inner join
        // ccjuweb.CIECRIP E on I.KBIECR=E.KBIECR
        // where TAIDDE=8628 AND E.KBMMON > (20000/12*(E.KBNMOF-(E.KBNMOD-1))) AND E.KBNMOF-(E.KBNMOD-1)>3 ;

        /*
         * StringBuffer sql = new StringBuffer (); sql.append("SELECT * FROM ");
         * sql.append(Jade.getInstance().getDefaultJdbcSchema()); sql.append(".DSINDP I INNER JOIN ");
         * sql.append(Jade.getInstance().getDefaultJdbcSchema()); sql.append(".CIECRIP E on I.KBIECR=E.KBIECR ");
         * sql.append("WHERE I.TAIDDE=? AND E.KBMMON > (?/12*(E.KBNMOF-(E.KBNMOD-1)))"); return sql.toString();
         */

        // REQUETE AVEC LES JOURS
        // select count(*) from (select E.KBMMON as montant,(tenjof +((kbnmof-1)*30)) as
        // jourFin,(tenjod+((kbnmod-1)*30)) as jourDebut from ccjuweb.DSINDP I inner join
        // ccjuweb.CIECRIP E on I.KBIECR=E.KBIECR
        // where TAIDDE=8628) as t
        // where montant >= (20000*(cast(jourFin-jourDebut as float)/360))
        // and (jourFin-jourDebut)>90;

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from (select E.KBMMON as montant,(tenjof +((kbnmof-1)*30)) as jourFin,(tenjod+((kbnmod-1)*30)) as jourDebut from ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".DSINDP I inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIECRIP E on I.KBIECR=E.KBIECR ");
        sql.append("where TAIDDE=?) as t ");
        sql.append("where montant >= (?*(cast(jourFin-jourDebut as float)/360)) ");
        sql.append("and (jourFin-jourDebut)>90");
        return sql.toString();
    }

    public boolean hasSaisieIndividuelle() throws Exception {
        DSInscriptionsIndividuellesManager inscMgr = new DSInscriptionsIndividuellesManager();
        inscMgr.setSession(getSession());
        inscMgr.setForIdDeclaration(idDeclaration);
        if (inscMgr.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static void initEbusinessAccessInstance(EBEbusinessInterface instance) {
        if (DSProcessValidation.ebusinessAccessInstance == null) {
            DSProcessValidation.ebusinessAccessInstance = instance;
        }
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Permet de signaler à eBusiness que la validation de la déclaration a été réalisé.
     * 
     * @param decl
     * @throws Exception
     */
    private void notificationEBusiness(DeclarationSalaireProvenance provenance, String idPucsFile) throws Exception {
        // Si la déclaration a été maj à facturé, elle a été validée
        // On va donc notifier eBusiness

        List<String> ids = Splitter.on(";").trimResults().splitToList(idPucsFile);
        if (provenance.isDan() || provenance.isPucs()) {

            if (!JadeStringUtil.isBlankOrZero(idPucsFile) && (DSProcessValidation.ebusinessAccessInstance != null)) {
                try {
                    for (String id : ids) {
                        PucsFile pucsFile = EBPucsFileService.read(id, getSession());
                        // On ne veut pas notifier plusieurs fois eBusiness dans le cadre déclarations des salaires
                        // complémentaires.
                        if (!pucsFile.getCurrentStatus().isComptabilise()) {
                            DSProcessValidation.ebusinessAccessInstance.notifyFinishedPucsFile(id, provenance,
                                    getSession());
                        }
                    }

                } catch (Exception e) {
                    getMemoryLog().logMessage("Unable to change status in eBusiness for the idPucsFile: " + idPucsFile,
                            FWMessage.FATAL, "Déclaration");
                    setForceEnvoiMail(true);
                }
            }
        }
        for (String id : ids) {
            EBPucsFileService.comptabiliserByFilename(id, getSession());
        }
    }

    public void setForceEnvoiMail(boolean forceEnvoiMail) {
        this.forceEnvoiMail = forceEnvoiMail;
    }

    /**
     * Sets the idDeclaration.
     * 
     * @param idDeclaration The idDeclaration to set
     */
    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

    public void setNotImpressionDecFinalAZero(Boolean notImpressionDecFinalAZero) {
        this.notImpressionDecFinalAZero = notImpressionDecFinalAZero;
    }

    public void setPucsFileMerged(List<PucsFile> pucsFiloMergded) {
        pucsFileMergded = pucsFiloMergded;
    }

}
