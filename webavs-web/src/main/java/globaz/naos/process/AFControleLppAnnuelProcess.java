package globaz.naos.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.controleLpp.AFAffilieSoumiLpp;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppConteneur;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppConteneur.Salarie;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppManager;
import globaz.naos.db.controleLpp.AFLineCi;
import globaz.naos.db.controleLpp.AFLineCiManager;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.listes.excel.AFXmlmlMappingSoumisLpp;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.util.AFUtil;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.db.tiers.TIRole;
import globaz.webavs.common.CommonExcelmlContainer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Processus de gestion LPP. Permet de rechercher suivant l'ann�e donn�e, tous les affili�s sans LPP active et qui ont
 * des salari�s qui doivent �tre soumis a la LPP
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFControleLppAnnuelProcess extends BProcess {

    private static final long serialVersionUID = 6612166262485600549L;
    private static final String LISTE_CAS_REJETES = "listeSoumisLpp.xml";
    private String annee;

    private String anneeDebut;
    private String anneeFin;

    private int anneeControle = 0;
    private int anneeCourante = 0;
    private AFAffilieSoumiLppConteneur casRejetes = null;
    private AFAffilieSoumiLppConteneur casSoumis = null;
    private StringBuffer corpsMessage = null;
    private String dateImpression;
    private String email;
    private Boolean modeControle = true; // true = simulation
    private int nbCasRejetes = 0;
    private int nbCasSoumis = 0;
    private double seuilAnnuel = 0;
    private double seuilMensuel = 0;
    private String typeAdresse;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            // Initialisation des variables
            initialisation();

            // 1. Cr�ation d'un conteneur pour regrouper les salari�s par affili�
            creationDonnees();

            // Gestion de l'abort
            if (isAborted()) {
                return false;
            }

            // 2. Application des r�gles de soumission a la LPP
            // => On ne garde que les NSS qui seront dans la liste pour le courrier
            // => cr�ation d'un conteneur avec les cas rejet�s
            applicationReglesSoumission();

            // Gestion de l'abort
            if (isAborted()) {
                return false;
            }

            // 3. On g�re la gestion de suivi
            gestionSuivisLpp();

            // Gestion de l'abort
            if (isAborted()) {
                return false;
            }

            // 4. Cr�ation des listes de r�sultat pour le mail
            creationListes();

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_CONTROLE_LPP_ANNUEL_ERREUR"));

            String messageInformation = "Annee du controle : " + getAnnee() + "\n";
            messageInformation += "dateImpression : " + getDateImpression() + "\n";
            messageInformation += "modeControle : " + isModeControleSimulation() + "\n";
            messageInformation += AFUtil.stack2string(e);

            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_EMAIL_CONTROLE_LPP"));
        }

        if (JadeStringUtil.isEmpty(getAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE_CONTROLE_LPP"));
        }

        // L'anne de contr�le doit etre plus petite que l'ann�e courante
        // et plus grande que 2007
        if (!JadeStringUtil.isEmpty(getAnnee())) {
            int anneeTest = CEUtils.transformeStringToInt(getAnnee());
            int anneeCourante = CEUtils.transformeStringToInt(CEUtils.giveAnneeCourante());

            if (anneeTest > anneeCourante) {
                this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE_CONTROLE_LPP_SUP"));
            }
        }

    }

    /**
     * Application des r�gles de soumission
     * 
     * @throws Exception
     */
    private void applicationReglesSoumission() throws Exception {

        setProcessDescription("Application des r�gles");
        String[] listAffilie = casSoumis.getTableauAffilie();

        // Parcours de tous les affili�s
        for (String idAffSoumis : listAffilie) {
            Salarie[] listSalarie = casSoumis.getTableauSalarieForAffilie(idAffSoumis);

            // Pr�vient les listes vides
            if (listSalarie == null) {
                continue;
            }

            // Parcours de tous les salari�s de l'affili�
            for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                boolean salarieSoumis = true;

                // 1.Controle date <18 ans et retraite
                salarieSoumis = testAge(idAffSoumis, sal);

                // 2. Controle + de 3 mois de suite
                // => si pas 3 mois verif les ann�es avant et apres
                if (salarieSoumis) {
                    salarieSoumis = testDureeTravail(idAffSoumis, sal);
                }

                // 3. Test des seuils
                if (salarieSoumis) {
                    salarieSoumis = testSeuils(idAffSoumis, sal);
                }

                // Si les tests ont indiqu�s qu'il �tait pas soumis
                // On l'enleve de la liste
                if (!salarieSoumis) {
                    casSoumis.removeSalarie(idAffSoumis, sal);
                }
            }

            if ((casSoumis.getListSalarieForAffilie(idAffSoumis) == null)
                    || (casSoumis.getListSalarieForAffilie(idAffSoumis).size() == 0)) {
                casSoumis.removeAffilie(idAffSoumis);
            }
        }

        // Etape termin�, on incremente le compteur
        incProgressCounter();
    }

    private int createDocument(AFAffilieSoumiLppConteneur conteneurList, String nomDoc, String titreDoc,
            String numInforom) throws Exception {
        CommonExcelmlContainer container = AFXmlmlMappingSoumisLpp.loadResults(conteneurList, this, titreDoc,
                numInforom);

        Collection<String> ligne = container.getFieldValues(IAFListeColumns.NUM_AFFILIE);
        String docPath = AFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + AFControleLppAnnuelProcess.LISTE_CAS_REJETES, numInforom + "_" + nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(numInforom);
        this.registerAttachedDocument(docInfo, docPath);
        if (ligne != null) {
            return ligne.size();
        }

        return 0;
    }

    /**
     * Liste des cas qui sont rejet�s
     * 
     * @return
     * @throws Exception
     */
    private void createDocumentCasRejetes() throws Exception {
        String titreDoc = getSession().getLabel("TITRE_LISTE_LPP_CAS_REJETES");
        String numInforom = "0279CAF"; // PO 6352
        String nomDoc = getSession().getLabel("LISTE_LPP_CAS_REJETES");
        nbCasRejetes = createDocument(casRejetes, nomDoc, titreDoc, numInforom);
    }

    /**
     * Cr�ation de la liste des cas soumis
     * 
     * @return
     * @throws Exception
     */
    private void createDocumentCasSoumis() throws Exception {
        String titreDoc = getSession().getLabel("TITRE_LISTE_LPP_CAS_SOUMIS");
        String numInforom = "0280CAF";
        String nomDoc = getSession().getLabel("LISTE_LPP_CAS_SOUMIS");
        nbCasSoumis = createDocument(casSoumis, nomDoc, titreDoc, numInforom);
    }

    /**
     * R�cup�ration des donn�es d'entr�e. Recherche les cas potentiellement soumis a une caisse LPP
     * 
     * @throws Exception
     */
    private void creationDonnees() throws Exception {

        setProcessDescription("G�n�ration des donn�es");

        // -------------
        // R�cup�ration de la liste des affili�s potentiellement soumis
        // et qui n'ont pas de caisse LPP
        // -------------
        AFAffilieSoumiLppManager manager = new AFAffilieSoumiLppManager();
        manager.setSession(getSession());
        manager.setForAnnee(getAnnee());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        Object[] obj_listAf = manager.getContainer().toArray();

        // Pour chaque ligne de la requete, on les regroupe par affili�
        for (Object obj_af : obj_listAf) {
            AFAffilieSoumiLpp affSoumis = (AFAffilieSoumiLpp) obj_af;

            // Si il l'affili� n'a pas de cotisation AVS, on le prend pas en compte
            // BUG:7202
            if (!hasCotisationAVS(affSoumis.getIdAffilie(), getAnnee())) {
                continue;
            }

            casSoumis.addSalarie(affSoumis.getIdTiers(), affSoumis.getIdAffilie(), affSoumis.getNumeroAffilie(),
                    affSoumis.getNom(), affSoumis.getSexe(), affSoumis.getNss(), affSoumis.getMoisDebut(),
                    affSoumis.getMoisFin(), affSoumis.getDateNaissance(), affSoumis.getGenreEcriture(),
                    affSoumis.getIdCompteIndividuel(), affSoumis.getMontant(), affSoumis.isExtourne(),
                    affSoumis.getNivSecuAffilie(), affSoumis.getNivSecuCI());

        }

        // Etape termin�, on incremente le compteur
        incProgressCounter();
    }

    /**
     * Cr�ation des listes de retour qui informe l'utilisateur
     * 
     * @throws Exception
     */
    private void creationListes() throws Exception {

        setProcessDescription("Cr�ation des listes");

        // Cr�ation de la liste des cas rejetes
        createDocumentCasRejetes();

        // Cr�ation de la liste des cas rejetes
        createDocumentCasSoumis();

        // Cr�ation du corps du message
        corpsMessage.append(getSession().getLabel("ANNEE")).append(" : ").append(getAnnee()).append("\n");
        corpsMessage.append(getSession().getLabel("DATE_IMPRESSION")).append(" : ").append(getDateImpression())
                .append("\n");
        corpsMessage.append(getSession().getLabel("MODE_CONTROLE")).append(" : ").append(isModeControleSimulation())
                .append("\n").append("\n\n");
        corpsMessage.append(getSession().getLabel("NBR_CAS_REJETES")).append(nbCasRejetes).append("\n");
        corpsMessage.append(getSession().getLabel("NBR_CAS_SOUMIS")).append(nbCasSoumis).append("\n");

        // On zip le tout
        String nomZip = getSession().getLabel("NOM_ZIP_LPP");

        ZipOutputStream zipOutput = null;
        try {
            String zipFilename = JadeFilenameUtil.addOrReplaceFilenameSuffixUID(Jade.getInstance().getPersistenceDir()
                    + nomZip + ".zip");
            zipOutput = new ZipOutputStream(new FileOutputStream(zipFilename));
            for (Object obj : getAttachedDocuments()) {
                JadePublishDocument document = (JadePublishDocument) obj;
                FileInputStream input = null;
                try {
                    input = new FileInputStream(document.getDocumentLocation());
                    byte[] contents = JadeZipUtil.load(input);
                    ZipEntry zipEntry = new ZipEntry(JadeFilenameUtil.extractFilename(document.getDocumentLocation()));
                    zipOutput.putNextEntry(zipEntry);
                    zipOutput.write(contents, 0, contents.length);
                    zipOutput.flush();
                    zipOutput.closeEntry();
                } finally {
                    input.close();
                }
            }
            getAttachedDocuments().clear();
            JadePublishDocumentInfo documentInfo = createDocumentInfo();
            documentInfo.setDocumentType("");
            documentInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(documentInfo, zipFilename);
            zipOutput.flush();
        } finally {
            zipOutput.close();
        }

        // Etape termin�, on incremente le compteur
        incProgressCounter();
    }

    /**
     * G�n�ration du processus de suivi Lpp
     * 
     * @param numAffilie
     * @param idAffilie
     * @throws Exception
     */
    private void generationSuiviLpp(String idTiers, String numAffilie, String idAffilie) throws Exception {
        // pr�pare les donn�es pour l'envoi
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, numAffilie);
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, AFApplication.DEFAULT_APPLICATION_NAOS);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, idAffilie);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, idTiers);

        // execute le process de g�n�ration
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(getSession());
        gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_LPP);
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(Boolean.FALSE);

        gen.executeProcess();
    }

    /**
     * Permet de cr�er la gestion des suivis pour chaque affili�
     * 
     * @throws Exception
     */
    private void gestionSuivisLpp() throws Exception {

        setProcessDescription("G�n�ration des suivis");

        // Pour chaque affili�
        // -------------------
        String[] listAffilie = casSoumis.getTableauAffilie();

        // Parcours de tous les affili�s
        for (String idAffSoumis : listAffilie) {
            Salarie[] listSalarie = casSoumis.getTableauSalarieForAffilie(idAffSoumis);

            // Pr�vient les listes vides
            if (listSalarie == null) {
                continue;
            }

            // 1. controle si pas d�j� un suivi
            if (isDejaJournalise(getSession(), idAffSoumis, (listSalarie[0]).getNumeroAffilie(), getAnnee())) {

                // Parcours de tous les salari�s de l'affili�
                for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                    sal.setSuivi(true);
                }

                continue;
            }
            // 2. Si pas de suivi, on en cr�e un .

            if (!isModeControleSimulation()) {
                // Si pas en simulation
                generationSuiviLpp(listSalarie[0].getIdTiers(), listSalarie[0].getNumeroAffilie(), idAffSoumis);

                // Parcours de tous les salari�s de l'affili�
                for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                    sal.setSuivi(true);
                }
            }

        }

        // Etape termin�, on incremente le compteur
        incProgressCounter();
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getEmail() {
        return email;
    }

    @Override
    protected String getEMailObject() {

        // Information sur l'ann�e de traitement
        String anneeTraitement = "";
        if (!JadeStringUtil.isEmpty(getAnnee())) {
            anneeTraitement = " (" + getAnnee() + ")";
        }

        // Information sur le type de traitement
        String simulation = isModeControleSimulation() ? getSession().getLabel("CONTROLE_LPP_SIMULATION") + " - " : "";

        // Construction de l'object
        if (isAborted()) {
            return simulation + getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_ABORTED") + anneeTraitement;
        } else if (isOnError() || getSession().hasErrors()) {
            return simulation + getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_ERREUR") + anneeTraitement;
        } else {
            return simulation + getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_OK") + anneeTraitement;
        }
    }

    public Boolean getModeControle() {
        return modeControle;
    }

    /**
     * R�cup�ration du seuil des salaires par le biais des parametres
     * 
     * @return
     * @throws Exception
     */
    private String getSeuilByParameterCode() throws Exception {

        String montantSeuil = "";

        FWFindParameterManager param = new FWFindParameterManager();
        param.setSession(getSession());
        param.setIdApplParametre(AFApplication.DEFAULT_APPLICATION_NAOS);
        param.setIdCodeSysteme("10800042");
        param.setIdCleDiffere("SEUILLPP");
        param.setIdActeurParametre("0");
        param.setPlageValDeParametre("0");
        param.setDateDebutValidite("01.01." + getAnnee());
        try {
            param.find(getTransaction());

        } catch (Exception e) {
            throw new Exception("Technical Exception, unabled to retrieve seuil annuel for " + getAnnee(), e);
        }

        if (param.size() > 0) {
            montantSeuil = ((FWFindParameter) param.getFirstEntity()).getValeurNumParametre();
        } else {
            throw new Exception("Unabled to retrieve seuil annuel for " + getAnnee());
        }

        return montantSeuil;
    }

    @Override
    public String getSubjectDetail() {
        if ((getTransaction() != null) && getTransaction().hasErrors()) {
            return super.getSubjectDetail();
        } else if ((getSession() != null) && getSession().hasErrors()) {
            return super.getSubjectDetail();
        }

        return corpsMessage.toString();

    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    /**
     * Methode permettant de controler si un affili� a une cotisation AVS pour l'ann�e sp�cifi�.
     * 
     * @param idAffilie
     * @param annee
     * @return
     * @throws HerculeException
     */
    private boolean hasCotisationAVS(String idAffilie, String annee) throws Exception {

        if (JadeStringUtil.isEmpty(idAffilie)) {
            throw new Exception("Unabled to find cotisation AVS. idAffilie is null or empty");
        }

        if (JadeStringUtil.isEmpty(annee)) {
            throw new Exception("Unabled to find cotisation AVS. annee is null or empty");
        }

        boolean hasCoti = false;

        AFCotisationManager manager = new AFCotisationManager();
        manager.setSession(getSession());
        manager.setForTypeAssurance(globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        manager.setForAnneeActive(annee);
        manager.setForAffiliationId(idAffilie);

        try {
            hasCoti = manager.getCount() > 0;
        } catch (Exception e) {
            throw new Exception("Unabled to retrieve cotisation avs (idAffilie = " + idAffilie + " / annee = " + annee
                    + ")", e);
        }

        return hasCoti;
    }

    /**
     * Initialisation des variables
     * 
     * @throws Exception
     * @throws NumberFormatException
     */
    private void initialisation() throws NumberFormatException, Exception {
        // Initialisation de la progresse bar
        setProgressScaleValue(5);

        anneeControle = CEUtils.transformeStringToInt(annee);
        anneeCourante = CEUtils.transformeStringToInt(CEUtils.giveAnneeCourante());
        casSoumis = new AFAffilieSoumiLppConteneur();
        casRejetes = new AFAffilieSoumiLppConteneur();
        corpsMessage = new StringBuffer("");

        // D�termination des seuils mensuel
        seuilAnnuel = Double.parseDouble(getSeuilByParameterCode());
        seuilMensuel = seuilAnnuel / 12.0;

        // Etape termin�, on incremente le compteur
        incProgressCounter();
    }

    /**
     * Methode permettant de regarder si il n'y pas d�j� un suivi LPP en cours
     * 
     * @param session
     * @param idAffiliation
     * @param numAffilie
     * @param annee
     * @return
     * @throws Exception
     */
    public boolean isDejaJournalise(BSession session, String idAffiliation, String numAffilie, String annee)
            throws Exception {

        // On sette les crit�res qui font que l'envoi est unique
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, numAffilie);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                AFApplication.DEFAULT_APPLICATION_NAOS);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, idAffiliation);

        LUJournalListViewBean viewBean = new LUJournalListViewBean();
        viewBean.setSession(session);
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DEBUT_SUIVI_LPP);
        viewBean.find(getTransaction());

        // Si le viewBean retourne un enregistrement c'est que l'envoi a d�j�
        // �t� journalis� donc on retourne true
        if (viewBean.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isModeControleSimulation() {
        return modeControle;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModeControle(Boolean modeControle) {
        this.modeControle = modeControle;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    /**
     * R�gle sur l'age l�gal
     * 
     * @param sal
     * @return
     * @throws JAException
     */
    private boolean testAge(String idAffilie, AFAffilieSoumiLppConteneur.Salarie sal) throws JAException {

        // Test si date de naissance existante et sexe
        if (JadeStringUtil.isEmpty(sal.getDateNaissance()) || JadeStringUtil.isEmpty(sal.getSexe())) {
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

        int anneeNaissance = CEUtils.stringDateToAnnee(sal.getDateNaissance());
        JADate dateNaissance = new JADate(sal.getDateNaissance());

        // Test si + de 17 ans et pas en retraite
        if (((anneeControle - anneeNaissance) >= 18) && !CIUtil.isRetraite(dateNaissance, sal.getSexe(), anneeControle)) {
            return true;

        } else if (CIUtil.isAnneeRetraite(dateNaissance, sal.getSexe(), anneeControle)) {
            // Test si on est dans l'ann�e de retraite
            return true;
        }

        return false;
    }

    /**
     * Permet de regarder les mois travaill�s avant ou apr�s une ann�e donn�
     * 
     * @param autreAnnee
     * @param anneeInf
     * @param sal
     * @param nbMois
     * @return
     * @throws Exception
     */
    private boolean testAutreAnneeDureeTravail(String autreAnnee, boolean anneeInf, String idAffilie, Salarie sal,
            int nbMois) throws Exception {

        AFLineCiManager man = new AFLineCiManager();
        man.setSession(getSession());
        man.setForAnnee(autreAnnee);
        man.setForIdAffilie(idAffilie);
        man.setForIdCompteIndividuel(sal.getIdCompteIndividuel());
        man.find(getTransaction());

        for (int i = 0; i < man.size(); i++) {
            AFLineCi ecri = (AFLineCi) man.getEntity(i);
            int moisDebEcri = CEUtils.transformeStringToInt(ecri.getMoisDebut());
            int moisFinEcri = CEUtils.transformeStringToInt(ecri.getMoisFin());

            // Si les mois sont pas dans la continuit�, il est pas soumis
            if (anneeInf && (moisFinEcri != 12)) {
                return false;
            } else if (!anneeInf && (moisDebEcri != 1)) {
                return false;
            }

            // test si il a plus de 3 mois
            if (((moisFinEcri - moisDebEcri + 1) + nbMois) > 3) {
                return true;
            }
        }

        return false;
    }

    /**
     * R�gle sur la dur�e du travail
     * 
     * @param sal
     * @return
     * @throws Exception
     */
    private boolean testDureeTravail(String idAffilie, Salarie sal) throws Exception {

        // Test si periode de travail existante
        if (JadeStringUtil.isEmpty(sal.getMoisDebut()) || JadeStringUtil.isEmpty(sal.getMoisFin())) {
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

        int moisDeb = CEUtils.transformeStringToInt(sal.getMoisDebut());
        int moisFin = CEUtils.transformeStringToInt(sal.getMoisFin());

        // si la dur�e du travail est inf�rieur ou �gal a 3 mois
        if ((moisFin - moisDeb + 1) <= 3) {

            // On part du principe qu'il est valide, on regarde les ann�es avant et apres
            boolean valide = true;

            // Si le mois de d�but n'est pas janvier ou le mois de fin pas d�cembre
            // Il n'est pas soumis a la lpp
            if ((moisDeb > 1) && (moisFin < 12)) {
                valide = false;
            }

            // Si le mois de d�but = janvier
            if (valide && (moisDeb == 1)) {
                // on regarde l'ann�e pr�c�dente pour savoir si il a travaill� octobre, novembre et d�cembre
                valide = testAutreAnneeDureeTravail("" + (anneeControle - 1), true, idAffilie, sal,
                        (moisFin - moisDeb + 1));
            }

            // Si le mois de fin est le mois de d�cembre
            if (valide && (moisFin == 12)) {
                // On regarde l'ann�e suivante
                // Pour autant que ce ne soit pas l'ann�e courante + 1
                if (!(anneeControle >= anneeCourante)) {
                    valide = testAutreAnneeDureeTravail("" + (anneeControle + 1), false, idAffilie, sal, (moisFin
                            - moisDeb + 1));
                }
            }

            return valide;
        }

        // La dur�e du travail d�passe 3 mois, il est sujet a la LPP
        return true;
    }

    /**
     * Regle sur les seuils annuel et mensuel du salaire
     * 
     * @param sal
     * @return
     */
    private boolean testSeuils(String idAffilie, Salarie sal) {

        // Test si periode de travail existante
        if (JadeStringUtil.isEmpty(sal.getMontant()) || JadeStringUtil.isEmpty(sal.getMontant())) {
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

        double salaire = Double.parseDouble(sal.getMontant());

        // Test du seuil annuel
        if (salaire >= seuilAnnuel) {
            // Il d�passe le seuil annuel
            return true;
        }

        int moisDeb = CEUtils.transformeStringToInt(sal.getMoisDebut());
        int moisFin = CEUtils.transformeStringToInt(sal.getMoisFin());
        double nbMois = moisFin - moisDeb + 1; // +1 car le mois de d�but compte
        double salaireMensuel = salaire / nbMois;

        // Test du seuil mensuel
        if (salaireMensuel >= seuilMensuel) {
            // Il d�passe le seuil mensuel
            return true;
        }

        // Il ne d�passe pas la r�gle des seuils
        return false;

    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }
}
