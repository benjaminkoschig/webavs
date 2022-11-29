package globaz.draco.process;

import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.service.EBEbusinessInterface;
import ch.globaz.orion.service.EBPucsFileService;
import com.google.common.base.Splitter;
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

public class DSProcessValidation extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -3353019454893928164L;
    public final static String CS_DECL_MIXTE = "19170000";
    private static EBEbusinessInterface ebusinessAccessInstance = null;
    private final String CS_PARAMETRES_IM = "10800042";
    private boolean forceEnvoiMail = false;
    private String idDeclaration;
    private boolean isBatch = false;
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

        // Sous contr�le d'exceptions
        try {
            // R�cup�rer la d�claration
            DSDeclarationViewBean decl = new DSDeclarationViewBean();
            decl.setSession(getSession());
            decl.setIdDeclaration(getIdDeclaration());
            decl.retrieve(getTransaction());
            // Contr�ler si la d�claration peut �tre lue
            if (decl.hasErrors() || decl.isNew()) {

                getMemoryLog().logMessage(getSession().getLabel("DECL_NON_EXISTANTE"), FWMessage.ERREUR, "");
                abort();
                return false;
            }
            noAffilie = decl.getNumeroAffilie();
            // Contr�ler l'�tat de la d�claration
            if (!decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {

                getMemoryLog().logMessage(getSession().getLabel("DECL_NON_OUVERTE_POUR_VALIDATION"), FWMessage.ERREUR,
                        "");
                abort();
                return false;
            }

            /**
             *  Controles supplemetaires
             */
            DSApplication app = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
            DSProcessValidationControlesSupplementaires controlesSup = new DSProcessValidationControlesSupplementaires(getSession(), decl);
            if(getIsBatch()) {
                boolean errorValidationSupplementaire = false;
                String messageError = "";
                if (app != null && app.isValidationControlesSupplementaires()) {

                    if (controlesSup.masseAFetAVScorrespondentPas()) {
                        errorValidationSupplementaire = true;
                        messageError+= getSession().getLabel("ERREUR_VALIDATION_MASSE_AVS_MASSE_AF") + " " + decl.getNumeroAffilie() + "\n";
                    }

                    // V�rifier que la masse PC Familles soit identique � celle de la masse AF VD
                    if (controlesSup.massePCFamilleEtMasseAFVDNeCorrespondentPas()) {
                        errorValidationSupplementaire = true;
                        messageError+=getSession().getLabel("ERREUR_VALIDATION_MASSE_PC_FAMILLE_MASSE_AFVD") + " " + decl.getNumeroAffilie() + "\n";
                    }

                    // S�il y a diff�rents cantons, les masses doivent �tre indiqu�es dans chaque assurance
                    if (controlesSup.masseCantonPasDansAssurance()) {
                        errorValidationSupplementaire = true;
                        messageError+=getSession().getLabel("ERREUR_VALIDATION_ASSURANCE_CANTON") + " " + decl.getNumeroAffilie() + "\n";
                    }

                    // Le montant AC + AC2 = le montant AVS pour toutes les saisies individuelles ayant de l�AC
                    if (controlesSup.inscriptionsMontantACetAVSneCorrespondentPas()) {
                        errorValidationSupplementaire = true;
                        messageError+=getSession().getLabel("ERREUR_VALIDATION_MONTANTAC_AC2_MONTANTAVS") + " " + decl.getNumeroAffilie() + "\n";
                    }
                }

                if (app != null && controlesSup.contientPasToutesLesAssurancesRequises(app.listValidationAssurances())) {
                    errorValidationSupplementaire = true;
                    messageError+=getSession().getLabel("ERREUR_VALIDATION_PUCS_BATCH_ASSURANCES_MANQUANTES") + " " + decl.getNumeroAffilie() + "\n";
                }
                if(errorValidationSupplementaire) {
                    return returnError(messageError);
                }
            }

            /***
             * Plausi 1-5-4-1, un d�compte compl�mentaire ne peut �tre fait que si un 13 existe en compta Plausi
             * salaires diff�r�s
             */
            if (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())) {

                if (!existDecompteFinal(decl, getSession())) {
                    this._addError(getTransaction(), getSession().getLabel("MSG_COMPLEMENT_IMPOSSIBLE"));
                    abort();
                    return false;
                }
            }

            // Plausibilit� pour le compl�ment LTN -> on ne peut pas avoir de compl�ment LTN(38) si il n'existe pas de
            // decompte LTN(33) en comptabilit�
            if (DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())
                    && !existDecompteLtn(decl, getSession())) {
                this._addError(getTransaction(), getSession().getLabel("MSG_COMPLEMENT_IMPOSSIBLE"));
                abort();
                return false;
            }

            // V�rification que les plafonds LTN (affili� et assur�) ne sont pas d�pass�s
            if (DSDeclarationViewBean.CS_LTN.equals(decl.getTypeDeclaration())
                    || DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())) {
                // Cr�ation d'un hashmap qui va contenir toutes les inscriptions individuelles pour l'affili� (33 et 38)
                HashMap<String, ArrayList<DSInscriptionsIndividuelles>> inscriptionsIndividuellesMap = new HashMap<String, ArrayList<DSInscriptionsIndividuelles>>();

                // R�cup�ration du 33 et du ou des 38 pour cet affili� et cette ann�e
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

                // Parcours chaque d�claration
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

                        // on parcours les insriptions et on ins�re dans le hashmap
                        for (int j = 0; j < inscriptionIndManager.size(); j++) {
                            DSInscriptionsIndividuelles inscription = (DSInscriptionsIndividuelles) inscriptionIndManager
                                    .get(j);
                            if (inscriptionsIndividuellesMap.containsKey(inscription.getNumeroAvs())) {
                                // on ins�re � la bonne place dans le hashmap
                                inscriptionsIndividuellesMap.get(inscription.getNumeroAvs()).add(inscription);
                            } else {
                                // cr�ation d'une nouvelle key
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

                // Parcours du hashmap pour d�terminer si les plafonds sont d�pass� ou non
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

            // Si il d'agit d'une d�claration compl�mentaire LTN (38), on D�tague de la princpale LTN (33) afin de
            // pouvoir r�imprimer l'attestation avec les bonnes valeurs
            if (DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(decl.getTypeDeclaration())) {
                // rechercher la d�claration principale (33) li�e � cette compl�mentaire
                DSDeclarationListViewBean declarationPrincipaleManager = new DSDeclarationListViewBean();
                declarationPrincipaleManager.setSession(getSession());
                declarationPrincipaleManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN);
                declarationPrincipaleManager.setForAffiliationId(decl.getAffiliationId());
                declarationPrincipaleManager.setForAnnee(decl.getAnnee());
                declarationPrincipaleManager.find();
                if (declarationPrincipaleManager.size() > 0) {
                    DSDeclarationViewBean declarationPrincipale = (DSDeclarationViewBean) declarationPrincipaleManager
                            .getFirstEntity();
                    // On d�tague la DS princpale (33)
                    // valeur � 0 pour les attestations fiscales
                    declarationPrincipale.setDateImpressionAttestations("0");
                    // si la d�claration 33 a d�j� �t� trait�e dans un d�compte (contient une date d'impression)
                    // on renseigne le champs avec la date 11.11.1111 (permet au programme de savoir que des compl�ments
                    // existent pour cette 33 mais que les montants de cette derni�re ne doivent pas �tre pris en compte
                    // pour le calcule)
                    if (!JadeStringUtil.isBlankOrZero(declarationPrincipale.getDateImpressionDecompteImpots())) {
                        declarationPrincipale.setDateImpressionDecompteImpots("11111111");
                    }
                    declarationPrincipale.wantCallValidate(false);
                    declarationPrincipale.wantCallMethodBefore(false);
                    declarationPrincipale.update(getTransaction());
                }
            }

            // Si un total de contr�le a �t� saisi, il doit correspondre au total calcul�
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
            // Si AGRIVIT, pas de contr�le, car il n'y a pas de saisie individuelle
            // Pour CCVD, s'il n'y pas de saisie individuelle => validation possible pour r�d pfa
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
            // Lecture des lignes de la d�claration
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
                        if (!AFApplication.isTauxParTranche(ligne.getAssuranceId())) {
                            String annee = "";
                            if (JadeStringUtil.isBlankOrZero(decl.getAnnee())) {
                                annee = ligne.getAnneCotisation();
                            } else {
                                annee = decl.getAnnee();
                            }
                            AFTauxAssurance taux = ligne.getTauxLigne("31.12." + annee);

                            //ESVE calculer le taux moyen sp�cifique � la FERCIAM
                            AFCalculAssurance.updateTauxMoyen(getSession()
                                    , decl.getAffiliation().getAffiliationId()
                                    , ligne.getAssuranceId()
                                    , ligne.getAssurance().getTypeAssurance()
                                    , ligne.getAssurance().getAssuranceGenre()
                                    , taux.getGenreValeur()
                                    , decl.getMasseSalTotal()
                                    , annee
                                    , decl.getTypeDeclaration());
                            //ligne.setMontantDeclaration(ligneDecAssCotiAvsAi.getCotisationDue());
                        }
                    }
                }

            }

            // Contr�le LAA/LPP
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
                    application = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                            .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
                    if ((application != null) && application.isGestionLppDansValidationDS()
                            && !DSDeclarationViewBean.CS_LTN.equals(decl.getTypeDeclaration())) {
                        if (!AFAffiliationUtil.hasCaissseLPP(affilie, decl.getAnnee())) {

                            // Recherche du seuil pour d�clenchement suivi lpp
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
                                // si on a trouv� au moins 1 employ� devant �tre soumis � la LPP
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
                // Lors de la validation, on met � jour le nb d'employ� dans l'affiliation
                AFCotisationManager cotisations = new AFCotisationManager();
                cotisations.setSession((BSession) getSessionNaos(getSession()));
                cotisations.setForAffiliationId(decl.getAffiliationId());
                cotisations.setForNotMotifFin(globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION);
                cotisations.setForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                cotisations.setForAnneeActive(decl.getAnnee());
                cotisations.find(getTransaction());
                for (int i = 0; i < cotisations.size(); i++) {
                    AFCotisation coti = (AFCotisation) cotisations.getEntity(i);
                    // Si le comptage existe d�j� pour l'ann�e et l'affili�, on met � jour, sinon on ajoute
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
            // Mise � jour de l'�tat de la d�claration

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
            String valideSpyComplement = getSession().getApplication()
                    .getProperty(DSApplication.VALIDATION_SPY_COMPLEMENT, "false");

            if ("true".equalsIgnoreCase(valideSpyComplement)) {
                String complementUser = DSDeclarationServices.getPersValider(getSession());
                if (JadeStringUtil.isBlankOrZero(complementUser)) {
                    this._addError(getTransaction(), getSession().getLabel("MSG_COMPLEMENT_SPY_MANQUANT"));
                    abort();
                    return false;
                }
                decl.setValidationSpy(complementUser);
            } else {
                decl.setValidationSpy(getSession().getUserFullName());
            }

            decl.setNotImpressionDecFinalAZero(getNotImpressionDecFinalAZero());
            decl.setEtat(DSDeclarationViewBean.CS_AFACTURER);
            decl.setEnValidation(true);
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

    private boolean returnError(String msg) {
        this._addError(getTransaction(), msg);
        abort();
        return false;
    }

    /**
     * Validation
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendMailOnError(true);
        setSendCompletionMail(true);
        // modif jmc 27.06.2006, reprise de l'adresse saisie � l'�cran
        // setEMailAddress(getSession().getUserEMail());
    }

    /**
     * Indique si il existe un d�compte final(13) en comptabilit�
     *
     * @param declaration
     * @param session
     * @return
     */
    public boolean existDecompteFinal(DSDeclarationViewBean declaration, BSession session) {
        try {
            // Pour �viter les reprises de donn�es incompl�tes, on plausibilise depuis une ann�e.
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
            // Volontaire : on ne veut pas bloquer l'utilisateur en cas de probl�me...
            JadeLogger.error(this, e);
            return true;
        }
    }

    /**
     * Indique si il existe un d�compte LTN(33) en comptabilit�
     *
     * @param declaration
     * @param session
     * @return
     */
    public boolean existDecompteLtn(DSDeclarationViewBean declaration, BSession session) {
        try {
            // Pour �viter les reprises de donn�es incompl�tes, on plausibilise depuis une ann�e.
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
            // Volontaire : on ne veut pas bloquer l'utilisateur en cas de probl�me...
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

    public boolean getIsBatch() {
        return isBatch;
    }

    public Boolean getNotImpressionDecFinalAZero() {
        return notImpressionDecFinalAZero;
    }

    public BSession getSessionAf(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionNaos");
        if (remoteSession == null) {
            // pas encore de session pour l'application demand�
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
            // pas encore de session pour l'application demand�
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

    public static BISession getSessionNaos(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionNaos");
        if (remoteSession == null) {
            // pas encore de session pour l'application demand�
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
     * Retourne une requ�te SQL permettant de r�cup�rer les inscriptions individuelles justifiant un control LPP
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
        sql.append(
                "select count(*) from (select E.KBMMON as montant,(tenjof +((kbnmof-1)*30)) as jourFin,(tenjod+((kbnmod-1)*30)) as jourDebut from ");
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
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     *
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Permet de signaler � eBusiness que la validation de la d�claration a �t� r�alis�.
     *
     * @param provenance
     * @param idPucsFile
     * @throws Exception
     */
    private void notificationEBusiness(DeclarationSalaireProvenance provenance, String idPucsFile) throws Exception {
        // Si la d�claration a �t� maj � factur�, elle a �t� valid�e
        // On va donc notifier eBusiness

        List<String> ids = Splitter.on(";").trimResults().splitToList(idPucsFile);
        if (provenance.isDan() || provenance.isPucs()) {

            if (!JadeStringUtil.isBlankOrZero(idPucsFile) && (DSProcessValidation.ebusinessAccessInstance != null)) {
                try {
                    for (String id : ids) {
                        PucsFile pucsFile = EBPucsFileService.readByFilename(id, getSession());
                        // On ne veut pas notifier plusieurs fois eBusiness dans le cadre d�clarations des salaires
                        // compl�mentaires.
                        // On est fait se test pucsFile == null pour g�rer la r�trocomptabilit� des ancienne DS.
                        // se test pour �tre supprim� en 2017
                        if (pucsFile == null || !pucsFile.getCurrentStatus().isComptabilise()) {
                            DSProcessValidation.ebusinessAccessInstance.notifyFinishedPucsFile(id, provenance,
                                    getSession());
                        }
                    }

                } catch (Exception e) {
                    getMemoryLog().logMessage("Unable to change status in eBusiness for the idPucsFile: " + idPucsFile,
                            FWMessage.FATAL, "D�claration");
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

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setNotImpressionDecFinalAZero(Boolean notImpressionDecFinalAZero) {
        this.notImpressionDecFinalAZero = notImpressionDecFinalAZero;
    }

    public void setPucsFileMerged(List<PucsFile> pucsFiloMergded) {
        pucsFileMergded = pucsFiloMergded;
    }

}
