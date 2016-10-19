package globaz.orion.process;

import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.log.JadeLogger;
import globaz.jade.pdf.JadePdfUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.itext.ebusiness.AFLettreInscription;
import globaz.naos.translation.CodeSystem;
import globaz.orion.utils.InscComparator;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.exceptions.OrionInscriptionException;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;
import ch.globaz.orion.businessimpl.services.inscription.InscriptionServiceImpl;
import ch.globaz.xmlns.eb.partnerweb.EBPartnerWebException_Exception;
import ch.globaz.xmlns.eb.partnerweb.InscriptionBackStatusEnum;

/**
 * Processus de traitement des inscriptions e-Business
 * 
 * @author bjo
 * 
 */
public class EBTreatInscription extends EBAbstractJadeJob {

    private static final long serialVersionUID = 1L;
    private static final String LETTRE_INSCRIPTION_DOC_NAME = "inscebu.pdf";
    private static final String LETTRE_INSCRIPTION_TO_CHECK_DOC_NAME = "inscebucheck.pdf";
    private static final List<String> ACCEPTED_LANGUAGE = Arrays.asList("fr", "de", "en");

    /**
     * vérifie qu'un affiliation existe pour cette inscription et met le statut de l'inscription en erreur si
     * nécessaire. L'affiliation doit être de type employeur ou indépendant-employeur. La date de fin doit être vide ou
     * plus grande que la date du jour
     * 
     * @param inscriptionEbusiness
     */
    private void checkAffiliationAndSetIdTiers(InscriptionEbusiness inscriptionEbusiness) {
        // recherche de l'affiliation
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(getSession());
        affiliationManager.setForAffilieNumero(inscriptionEbusiness.getNumAffilie());
        affiliationManager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_EMPLOY,
                CodeSystem.TYPE_AFFILI_INDEP_EMPLOY });
        affiliationManager.setFromDateFin(JACalendar.todayJJsMMsAAAA());
        try {
            affiliationManager.find(BManager.SIZE_USEDEFAULT);
            if (affiliationManager.size() > 0) {
                // récupération de l'affiliation
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();
                // insertion l'id tiers correspondant dans l'inscription
                inscriptionEbusiness.setIdTiers(affiliation.getIdTiers());

                // mise à jour du mode de déclaration de salaire de l'affiliation
                updateModeDeclarationSalaire(affiliation, inscriptionEbusiness.getModeDeclSalaire());
            } else {
                // si aucune affiliation n'est trouvée, on met le statut de l'inscription en erreur
                inscriptionEbusiness.setStatut(InscriptionEbusiness.STATUT_ERREUR);
                inscriptionEbusiness.setRemarque(getSession().getLabel("INSCRIPTION_NO_AFFILIATION_ERROR"));
            }
        } catch (Exception e) {
            // on met l'inscription en erreur
            inscriptionEbusiness.setStatut(InscriptionEbusiness.STATUT_ERREUR);
            inscriptionEbusiness.setRemarque(getSession().getLabel("INSCRIPTION_TECHNIQUE_ERROR"));
            JadeLogger.error(
                    this,
                    "technical error when checkAffiliation for inscription : "
                            + inscriptionEbusiness.getIdInscription());
        }
    }

    public String getAnneePreremplissage() {
        JADate date = JACalendar.today();
        int annee = date.getYear();
        annee = annee - 1;
        return String.valueOf(annee);

    }

    @Override
    public String getDescription() {
        return ("EBTreatInscription");
    }

    @Override
    public String getName() {
        return ("EBTreatInscription");
    }

    /**
     * Imprime la lettre d'inscription et l'insère dans le lot correspondant
     * 
     * @param docs
     * @param docsToCheck
     * @param inscriptionTreated
     * @throws JadeNoBusinessLogSessionError
     */
    private void imprimerLettreInscription(List<String> docs, List<String> docsToCheck,
            InscriptionEbusiness inscriptionTreated) throws JadeNoBusinessLogSessionError {
        // impression de la lettre d'inscription
        AFLettreInscription process = new AFLettreInscription();
        process.setSession(getSession());
        // Il est nécessaire de passer la transaction au processus car sinon il en créer une nouvelle et cela provoque
        // un lock sur la table
        process.setTransaction(getSession().getCurrentThreadTransaction());
        process.setEMailAddress(getSession().getUserEMail());
        process.setInscription(inscriptionTreated);

        try {
            process.executeProcess();
        } catch (Exception e) {
            JadeThread.logError(this.getClass().toString(), getSession().getLabel("LETTRE_INSCRIPTION_PRINT_ERROR")
                    + inscriptionTreated.getNumAffilie());
        }

        // ajoute la lettre au bon lot
        List<JadePublishDocument> attachedDocuments = process.getAttachedDocuments();
        if ((!attachedDocuments.isEmpty()) && (attachedDocuments.get(0) != null)) {
            if (attachedDocuments.get(0).getPublishJobDefinition().getDocumentInfo().getSeparateDocument()) {
                docsToCheck.add(attachedDocuments.get(0).getDocumentLocation());
            } else {
                docs.add(attachedDocuments.get(0).getDocumentLocation());
            }
        }
    }

    /**
     * Imprime sous forme d'une liste Excel le tableau d'inscription passé en paramètre et retourne le pathfile
     * 
     * @param acl
     * @throws Exception
     * @return path fichier
     */
    private String imprimerListeInscription(InscriptionEbusiness[] inscriptionEbusiness) throws Exception {
        if ((inscriptionEbusiness == null) || (inscriptionEbusiness.length < 1)) {
            throw new Exception("Unable to print the list because the table is null or empty");
        }

        EBImprimerListeInscription impressionListe = new EBImprimerListeInscription(inscriptionEbusiness);
        impressionListe.setSession(getSession());
        impressionListe._executeProcess();

        List attachedDocuments = impressionListe.getAttachedDocuments();
        if ((!attachedDocuments.isEmpty()) && (attachedDocuments.get(0) != null)) {
            JadePublishDocument doc = (JadePublishDocument) attachedDocuments.get(0);
            String docLocation = doc.getDocumentLocation();
            if (!JadeStringUtil.isBlank(docLocation)) {
                return docLocation;
            } else {
                throw new Exception("docLocation is null!");
            }
        } else {
            throw new Exception("No attachedDocuments");
        }
    }

    @Override
    protected void process() throws Exception {
        // container pour les lettres d'inscriptions
        List<String> docs = new ArrayList<String>();
        List<String> docsToCheck = new ArrayList<String>();

        // récupération des nouvelles inscriptions depuis l'e-Business (utilisation du service distant)
        InscriptionEbusiness[] listInscriptionNouvelle = null;
        try {
            listInscriptionNouvelle = InscriptionServiceImpl.listeInscriptionsNouvelle(getSession());
        } catch (OrionInscriptionException e1) {
            JadeLogger.error(this, "Unable to list the new inscriptions " + e1.getMessage());
            throw new Exception(e1);
        } catch (EBPartnerWebException_Exception e1) {
            JadeLogger.error(this, "The service listInscriptionNouvelle is not available "
                    + e1.getFaultInfo().getMessage());
            throw new Exception(e1);
        }

        // création d'un tableau pour les inscriptions traitées
        InscriptionEbusiness[] listInscriptionTreated = new InscriptionEbusiness[listInscriptionNouvelle.length];

        // Parcours des nouvelles inscriptions
        for (int i = 0; i < listInscriptionNouvelle.length; i++) {
            // vérifier que l'affiliation existe bien et récupérer l'id tiers correspondant
            checkAffiliationAndSetIdTiers(listInscriptionNouvelle[i]);

            // création du compte Ebusiness. Le service ne crée pas de compte si le statut de l'inscription est "erreur"
            InscriptionEbusiness inscriptionTreated = listInscriptionNouvelle[i].cloneInscription();
            String pwd = "";
            try {
                if (!InscriptionEbusiness.STATUT_ERREUR.equals(inscriptionTreated.getStatut())) {

                    String langue = resolveLangueForMail(inscriptionTreated);

                    pwd = InscriptionServiceImpl.createAffilieAndAdmin(inscriptionTreated, langue, getSession());
                    inscriptionTreated.setPassword(pwd);
                    inscriptionTreated.setStatut(InscriptionEbusiness.STATUT_TERMINEE);
                    inscriptionTreated.setRemarque("");
                }

            } catch (EBPartnerWebException_Exception e) {
                e.printStackTrace();
                inscriptionTreated.setStatut(InscriptionEbusiness.STATUT_ERREUR);
                inscriptionTreated.setRemarque(getSession().getLabel("CREATE_COMPTE_EBU_ERROR"));
            }
            try {
                updateStatus(inscriptionTreated);
                // si propriété, on pré-rempli la DAN
                if (((EBApplication) getSession().getApplication()).wantInscPreRemplissage()
                        && !InscriptionEbusiness.STATUT_ERREUR.equals(inscriptionTreated.getStatut())) {
                    EBDanPreRemplissage process = new EBDanPreRemplissage();
                    process.setSession(getSession());
                    process.setAnnee(getAnneePreremplissage());
                    process.setNumAffilie(inscriptionTreated.getNumAffilie());
                    process.setEmail(getSession().getUserEMail());
                    process.run();
                }
            } catch (EBPartnerWebException_Exception e1) {
                for (String err : e1.getFaultInfo().getErrors()) {
                    getSession().getCurrentThreadTransaction().addErrors(err);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                getTransaction().addErrors("Erreur technique Integer impossible à parser");
            }
            // ajout de l'inscription à la liste des inscriptions traitée
            listInscriptionTreated[i] = inscriptionTreated;

            // si l'inscription n'est pas en erreur on imprime la lettre
            if (!inscriptionTreated.getStatut().equals(InscriptionEbusiness.STATUT_ERREUR)) {
                // on récupère l'id tiers correspondant à l'inscription en cours de traitement
                inscriptionTreated.setIdTiers(listInscriptionNouvelle[i].getIdTiers());
                imprimerLettreInscription(docs, docsToCheck, inscriptionTreated);
            }

            // si il y a des erreur on rollback sinon on commit
            if (getSession().hasErrors() || getSession().getCurrentThreadTransaction().hasErrors() || threadOnError()
                    || inscriptionTreated.getStatut().equals(InscriptionEbusiness.STATUT_ERREUR)) {

                JadeThread.rollbackSession();
                JadeThread.logClear();
                getSession().getCurrentThreadTransaction().clearErrorBuffer();
                getSession().getCurrentThreadTransaction().clearWarningBuffer();
                getSession().getErrors();// vide le buffer d'erreurs
                getSession().getWarnings();// vide le buffer de warning
            } else {
                JadeThread.commitSession();
            }

            System.out.println("Résultat du traitement du compte :");
            System.out.println("----------------------------------------");
            System.out.println("affilié : " + inscriptionTreated.getNumAffilie());
            System.out.println("idTiers : " + inscriptionTreated.getIdTiers());
            System.out.println("password : " + inscriptionTreated.getPassword());
            System.out.println("statut : " + inscriptionTreated.getStatut());
            System.out.println("\n");
        }

        // ###GENERATION DES DOCUMENTS ###
        // publication des lettres d'inscriptions (pdf)
        publishLettreInscription(docs, docsToCheck);

        // publication de la liste des inscriptions traitées (fichier Excel)
        publishListInscription(listInscriptionTreated);
    }

    /**
     * Fusionne, publie et envoi par email les 2 lots de lettres d'inscriptions
     * 
     * @param docs
     * @param docsToCheck
     * @throws JadeNoBusinessLogSessionError
     */
    private void publishLettreInscription(List<String> docs, List<String> docsToCheck)
            throws JadeNoBusinessLogSessionError {
        // définition des règles de fusion
        String pdfMergeRules[] = new String[] { JadePdfUtil.REPLACE_FILE };
        // fusion et publication 1er lot
        if (!docs.isEmpty()) {
            String docsTab[] = new String[docs.size()];
            docsTab = docs.toArray(docsTab);
            try {
                JadePdfUtil.merge(Jade.getInstance().getPersistenceDir()
                        + EBTreatInscription.LETTRE_INSCRIPTION_DOC_NAME, docsTab, pdfMergeRules);
                docs.clear();
                JadePublishDocumentInfo docInfoDocs = new JadePublishDocumentInfo();
                docInfoDocs.setOwnerEmail(getSession().getUserEMail());
                docInfoDocs.setDocumentTypeNumber(AFLettreInscription.NUMERO_REFERENCE_INFOROM);
                docInfoDocs.setDocumentType(AFLettreInscription.NUMERO_REFERENCE_INFOROM);
                docInfoDocs.setArchiveDocument(true);
                docInfoDocs.setPublishDocument(true);
                docInfoDocs.setDocumentTitle(getSession().getLabel("LETTRE_INSCRIPTION_MAIL_TITRE"));
                docInfoDocs.setDocumentSubject(getSession().getLabel("LETTRE_INSCRIPTION_MAIL_SUJET"));
                JadePublishDocument publishDocInscription = new JadePublishDocument(Jade.getInstance()
                        .getPersistenceDir() + EBTreatInscription.LETTRE_INSCRIPTION_DOC_NAME, docInfoDocs);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(publishDocInscription));
            } catch (Exception e) {
                JadeLogger.error(this, "Unable to merge the letters of Inscription" + e.getMessage());
            }
        }
        // fusion et publication 2ème lot
        if (!docsToCheck.isEmpty()) {
            String docsToCheckTab[] = new String[docsToCheck.size()];
            docsToCheckTab = docsToCheck.toArray(docsToCheckTab);
            try {
                JadePdfUtil.merge(Jade.getInstance().getPersistenceDir()
                        + EBTreatInscription.LETTRE_INSCRIPTION_TO_CHECK_DOC_NAME, docsToCheckTab, pdfMergeRules);
                docsToCheck.clear();
                JadePublishDocumentInfo docInfoDocsToCheck = new JadePublishDocumentInfo();
                docInfoDocsToCheck.setOwnerEmail(getSession().getUserEMail());
                docInfoDocsToCheck.setDocumentTypeNumber(AFLettreInscription.NUMERO_REFERENCE_INFOROM);
                docInfoDocsToCheck.setDocumentType(AFLettreInscription.NUMERO_REFERENCE_INFOROM);
                docInfoDocsToCheck.setArchiveDocument(true);
                docInfoDocsToCheck.setPublishDocument(true);
                docInfoDocsToCheck.setDocumentTitle(getSession().getLabel("LETTRE_INSCRIPTION_TO_CHECK_MAIL_TITRE"));
                docInfoDocsToCheck.setDocumentSubject(getSession().getLabel("LETTRE_INSCRIPTION_TO_CHECK_MAIL_SUJET"));
                JadePublishDocument publishDocInscriptionToCheck = new JadePublishDocument(Jade.getInstance()
                        .getPersistenceDir() + EBTreatInscription.LETTRE_INSCRIPTION_TO_CHECK_DOC_NAME,
                        docInfoDocsToCheck);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(publishDocInscriptionToCheck));
            } catch (Exception e) {
                JadeLogger.error(this, "Unable to merge the letters of inscription to check" + e.getMessage());
            }
        }
    }

    /**
     * Génère, publie et envoi par email la liste des inscriptions traitées
     * 
     * @param listInscriptionTreated
     * @throws JadeNoBusinessLogSessionError
     */
    private void publishListInscription(InscriptionEbusiness[] listInscriptionTreated)
            throws JadeNoBusinessLogSessionError {
        if (listInscriptionTreated.length > 0) {
            try {

                // Il faut ordrer par Statut
                // Convertir en arrayList
                ArrayList<InscriptionEbusiness> inscArray = new ArrayList<InscriptionEbusiness>(
                        Arrays.asList(listInscriptionTreated));
                // Appliquer le tri
                Collections.sort(inscArray, new InscComparator());
                // Conversion en array
                listInscriptionTreated = null;
                // Après le tri, on en refait un tableau
                listInscriptionTreated = inscArray.toArray(new InscriptionEbusiness[inscArray.size()]);
                // Impression de la liste
                String inscriptionExcelListPath = imprimerListeInscription(listInscriptionTreated);

                // Publication du fichier excel
                JadePublishDocumentInfo docInfoInscriptionExcelList = new JadePublishDocumentInfo();
                docInfoInscriptionExcelList.setOwnerEmail(getSession().getUserEMail());
                docInfoInscriptionExcelList.setArchiveDocument(false);
                docInfoInscriptionExcelList.setPublishDocument(true);
                docInfoInscriptionExcelList.setDocumentTitle(getSession().getLabel("LISTE_INSCRIPTION_MAIL_TITRE"));
                docInfoInscriptionExcelList.setDocumentSubject(getSession().getLabel("LISTE_INSCRIPTION_MAIL_SUJET"));
                JadePublishDocument publishDocAclExcelList = new JadePublishDocument(inscriptionExcelListPath,
                        docInfoInscriptionExcelList);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(publishDocAclExcelList));
            } catch (Exception e) {
                JadeLogger.error(this, "Unable to print the list of inscriptions" + e.getMessage());
            }
        }
    }

    private void updateModeDeclarationSalaire(AFAffiliation aff, String modeDecl) throws Exception {

        String modeDeclarationSalaire = "";

        // si mode de déclaration est DAN
        if (InscriptionEbusiness.MODE_DECL_DAN.equals(modeDecl)) {
            // Si mode mixte (CCVD)
            if (CodeSystem.DECL_SAL_PRE_MIXTE.equals(aff.getDeclarationSalaire())
                    || CodeSystem.DECL_SAL_MIXTE_DAN.equals(aff.getDeclarationSalaire())) {
                modeDeclarationSalaire = CodeSystem.DECL_SAL_MIXTE_DAN;
            } else {
                modeDeclarationSalaire = CodeSystem.DS_DAN;
            }
        }
        // si mode de déclaration est PUCS
        else if (InscriptionEbusiness.MODE_DECL_PUCS.equals(modeDecl)) {
            modeDeclarationSalaire = CodeSystem.DS_ENVOI_PUCS;
        } else {
            // throw new Exception("mode declaration specified is not possible" + modeDecl);
            // On ne fait rien, certaines caisses ne veulent pas de type déclaration
        }

        // mise à jour de l'affiliation
        if (!aff.isNew()) {
            // Mise à jour 1-11, si un affilié souhaite un mode déclaration de salaire traditionnel => on ne change rien
            if (!modeDeclarationSalaire.equals(aff.getDeclarationSalaire())
                    && !JadeStringUtil.isBlankOrZero(modeDeclarationSalaire)) {
                aff.setDeclarationSalaire(modeDeclarationSalaire);
                aff.setSession(getSession());
                aff.wantCallValidate(false);
                aff.wantCallMethodAfter(false);
                aff.wantCallMethodBefore(false);
                aff.update(getSession().getCurrentThreadTransaction());
            }
        }
    }

    private void updateStatus(InscriptionEbusiness insc) throws NumberFormatException, EBPartnerWebException_Exception {
        if (InscriptionEbusiness.STATUT_ERREUR.equals(insc.getStatut())) {
            InscriptionServiceImpl.changeStatusForInsc(Integer.parseInt(insc.getIdInscription()),
                    InscriptionBackStatusEnum.ERREUR, getSession(), insc.getRemarque());
        } else {
            InscriptionServiceImpl.changeStatusForInsc(Integer.parseInt(insc.getIdInscription()),
                    InscriptionBackStatusEnum.TERMINE, getSession(), "");
        }

    }

    /**
     * Trouve la langue qui sera utilisé pour le mail de retour de l'inscription-
     * Ce sera la langue du tiers pour autant que celle ci soit dans les langues acceptées. Sinon, ce sera "fr" par
     * défaut.
     * 
     * @param inscription Une isncrition
     * @return La langue
     */
    private String resolveLangueForMail(InscriptionEbusiness inscription) {
        String langue = retrieveLangueTiersForInscription(inscription);

        if (langue == null) {
            langue = getSession().getIdLangueISO();
        }

        if (!ACCEPTED_LANGUAGE.contains(langue)) {
            langue = "fr";
        }

        return langue;
    }

    /**
     * Récupération de la langue suivant le tiers
     * 
     * @param inscription Une isncription contenant un idTiers
     * @return La langue du tiers sous format "fr","de", ....
     */
    private String retrieveLangueTiersForInscription(InscriptionEbusiness inscription) {

        if (JadeStringUtil.isIntegerEmpty(inscription.getIdTiers())) {
            return null;
        }

        String langue = null;

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(inscription.getIdTiers());
        try {
            tiers.retrieve();

            langue = tiers.getLangueIso();
        } catch (Exception e) {
            JadeLogger.error(this, "Unable to find the language for inscription" + e.getMessage());
        }

        return langue;
    }
}