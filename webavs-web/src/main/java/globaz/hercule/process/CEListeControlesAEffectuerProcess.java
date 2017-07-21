package globaz.hercule.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControlesAEffectuer;
import globaz.hercule.db.controleEmployeur.CEControlesAEffectuerAnneePrecManager;
import globaz.hercule.db.controleEmployeur.CEControlesAEffectuerManager;
import globaz.hercule.db.controleEmployeur.CEControlesAEffectuerUnion;
import globaz.hercule.db.controleEmployeur.CEControlesAEffectuerUnionManager;
import globaz.hercule.mappingXmlml.CEXmlmlMappingControlesAEffectuer;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.webavs.common.CommonExcelmlContainer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Process qui permet la construction de l'ensemble des données pour l'impression
 * de la liste des contrôles à effectuer.
 * 
 * @author hpe
 * @since Créé le 14 févr. 07
 */

public class CEListeControlesAEffectuerProcess extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -5852398473376113769L;

    public static final String MODEL_NAME = "effectuer.xml";
    public static final String MODEL_REINJECTION_NAME = "effectuerReinjection.xml";
    public static final String NUMERO_INFOROM = "0244CCE";
    public static final String PROPERTY_REATTRIBUTION_AUTO = "reattributionAutoReviseur";

    private String annee = "";
    private String anneeCptr = "";
    private boolean aucunControle = false;
    private String genreControle = "";
    private Boolean wantAnneePrecedente = new Boolean(false);

    private Boolean isAvecReviseur = null;
    private Boolean isGenerationControleEmployeur = null;
    private Boolean isTenirComptePerioAffilie = null;
    private Boolean isTenirComptePerioCaisse = null;
    private Boolean listeExcel = null;
    private Boolean listePdf = null;
    private Boolean reattributionAuto = false;

    private String typeAdresse = "";

    /**
     * Constructeur de CEListeControlesAEffectuerProcess
     */
    public CEListeControlesAEffectuerProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Not implemented
    }

    @Override
    protected boolean _executeProcess() {

        boolean status = true;

        // Esct-ce qu'on attribu le meme réviseur que l'ancien controle de facon
        // automatique (Parametrage dans hercule.properties)
        isReattributionAutoReviseur();

        String nomDoc = getSession().getLabel("LISTE_CONTROLE_EFFECTUER_NOM_DOCUMENT");

        try {
            // ************************
            // 1. Génération de la liste des affiliés
            // ************************
            Map<String, CEControlesAEffectuer> mapEntityByNumAffilie = genererMapAffilie();

            if (isAborted()) {
                return false;
            }

            // ************************
            // Prise en compte des contrôles actifs prévus des années antérieures
            // Inforom579
            // ************************
            Map<String, CEControlesAEffectuer> mapEntityByNumAffilieForAnneePrec = new TreeMap<String, CEControlesAEffectuer>();
            if (isWantAnneePrecedente()) {
                findCrontolesNonEffectuerAnneePrec(mapEntityByNumAffilieForAnneePrec);
            }

            // ************************
            // 2. Génération des données qui seront dans le fichier de sortie
            // ************************
            CommonExcelmlContainer container = genererDataForDocument(mapEntityByNumAffilie,
                    mapEntityByNumAffilieForAnneePrec);

            if (isAborted()) {
                return false;
            }

            // ************************
            // 3. Création de ducument
            // ************************
            String docPath = createDocument(container, nomDoc);

            // ************************
            // 4. Publication du document
            // ************************
            aucunControle = publishDocument(nomDoc, docPath);

            status = true;
        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("LISTE_CONTROLE_EFFECTUER_ERROR"));

            String messageInformation = "Reattribution auto : " + reattributionAuto + "\n";
            messageInformation += "annee : " + annee + "\n";
            messageInformation += "genreControle : " + genreControle + "\n";
            messageInformation += "anneeCptr : " + anneeCptr + "\n";
            messageInformation += "typeAdresse : " + typeAdresse + "\n";
            messageInformation += "wantAnneePrecedente : " + wantAnneePrecedente + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    private void findCrontolesNonEffectuerAnneePrec(final Map<String, CEControlesAEffectuer> mapEntityByNumAffilie)
            throws Exception {

        // Recherche des controles
        // - actif
        // - non effectué
        // - année antérieure a la date donéée au process
        CEControlesAEffectuerAnneePrecManager manager = new CEControlesAEffectuerAnneePrecManager();
        manager.setSession(getSession());
        manager.setForAnnee(getAnnee());
        manager.setForAnneeCptr(getAnneeCptr());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        // On remplit le TreeMap
        for (int j = 0; j < manager.size(); j++) {
            CEControlesAEffectuer entity = (CEControlesAEffectuer) manager.getEntity(j);
            mapEntityByNumAffilie.put(entity.getNumAffilie(), entity);
        }

    }

    private Map<String, CEControlesAEffectuer> genererMapAffilie() throws Exception {
        Map<String, CEControlesAEffectuer> mapEntityByNumAffilie = new TreeMap<String, CEControlesAEffectuer>();

        // Nombre maximum supporté par la DB (Parametrage dans
        // hercule.properties)
        int nombreMaximumSupporteParDB = _getNbMaximumSupporteParDB();

        List<String> idAffilies = retrieveUnion();
        int nombreIterations = idAffilies.size() / nombreMaximumSupporteParDB;
        if (idAffilies.size() % nombreMaximumSupporteParDB > 0) {
            nombreIterations++;
        }
        for (int i = 0; i < nombreIterations; i++) {
            List<String> listeIdAffilie = new ArrayList<String>();
            if (idAffilies.size() >= (i + 1) * nombreMaximumSupporteParDB) {
                listeIdAffilie = idAffilies.subList(i * nombreMaximumSupporteParDB,
                        ((i + 1) * nombreMaximumSupporteParDB));
            } else {
                listeIdAffilie = idAffilies.subList(i * nombreMaximumSupporteParDB, idAffilies.size());
            }

            Map<String, CEControlesAEffectuer> mapEntity = findControleByListeIdAffilie(listeIdAffilie);
            mapEntityByNumAffilie.putAll(mapEntity);
        }

        return mapEntityByNumAffilie;
    }

    private Map<String, CEControlesAEffectuer> findControleByListeIdAffilie(final List<String> listeIdAffilie)
            throws Exception {
        Map<String, CEControlesAEffectuer> mapEntityByNumAffilie = new TreeMap<String, CEControlesAEffectuer>();

        // Création du manager
        CEControlesAEffectuerManager manager = new CEControlesAEffectuerManager();
        manager.setSession(getSession());
        manager.setForAnnee(getAnnee());
        manager.setForGenreControle(getGenreControle());
        manager.setForAnneeCptr(getAnneeCptr());
        manager.setIdAffilies(listeIdAffilie);
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        // On remplit le TreeMap
        for (int j = 0; j < manager.size(); j++) {
            CEControlesAEffectuer entity = (CEControlesAEffectuer) manager.getEntity(j);
            mapEntityByNumAffilie.put(entity.getNumAffilie(), entity);
        }

        return mapEntityByNumAffilie;
    }

    private CommonExcelmlContainer genererDataForDocument(final Map<String, CEControlesAEffectuer> mapEntity,
            final Map<String, CEControlesAEffectuer> mapEntityForAnneePrec) throws Exception {

        CommonExcelmlContainer container = null;

        if (mapEntity.size() > 0 || mapEntityForAnneePrec.size() > 0) {
            CEXmlmlMappingControlesAEffectuer loadData = new CEXmlmlMappingControlesAEffectuer(mapEntity,
                    mapEntityForAnneePrec, this, reattributionAuto);
            container = loadData.processResults();
        }

        return container;
    }

    private String createDocument(final CommonExcelmlContainer container, final String nomDoc) throws Exception {

        if (container == null || container.isEmpty()) {
            return null;
        }

        // On génère le doc
        String docPath = "";

        try {
            docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                    + CEListeControlesAEffectuerProcess.MODEL_NAME, nomDoc, container);
        } catch (Exception e) {
            throw new Exception("Unabled to create " + nomDoc, e);
        }

        return docPath;
    }

    private boolean publishDocument(final String nomDoc, final String docPath) throws IOException {

        if (docPath == null) {
            return false;
        }

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CEListeControlesAEffectuerProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    /**
     * Permet de récupéré le parametrage du nombre maximum supporté par la db.
     */
    private int _getNbMaximumSupporteParDB() {

        int nb = 1000;

        try {
            nb = ((CEApplication) getSession().getApplication()).nombreMaxListeControleAEffectuerSupporteParDB();
        } catch (Exception e1) {
            nb = 1000;
        }

        return nb;
    }

    @Override
    protected void _validate() throws Exception {
        JADate date = new JADate(JACalendar.todayJJsMMsAAAA());
        int anneeCourante = date.getYear();

        if (JadeStringUtil.isEmpty(getAnnee())) {// TODO SCO : Aucun labels
            getSession().addError(getSession().getLabel(""));
        } else if (JadeStringUtil.isEmpty(getAnneeCptr())) {
            getSession().addError(getSession().getLabel(""));
        } else if (Integer.valueOf(getAnneeCptr()).intValue() > anneeCourante) {
            getSession().addError(getSession().getLabel(""));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    public String getAnnee() {
        return annee;
    }

    public String getAnneeCptr() {
        return anneeCptr;
    }

    @Override
    protected String getEMailObject() {
        if (isAucunControle()) {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_AEFFECTUER_AUCUN");
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_AEFFECTUER_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_AEFFECTUER");
        }
    }

    public String getGenreControle() {
        return genreControle;
    }

    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    public Boolean getIsGenerationControleEmployeur() {
        return isGenerationControleEmployeur;
    }

    public Boolean getIsTenirComptePerioAffilie() {
        return isTenirComptePerioAffilie;
    }

    public Boolean getIsTenirComptePerioCaisse() {
        return isTenirComptePerioCaisse;
    }

    public Boolean getListeExcel() {
        return listeExcel;
    }

    public Boolean getListePdf() {
        return listePdf;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public boolean isAucunControle() {
        return aucunControle;
    }

    /**
     * Permet de savoir si les réviseur sont reattribués automatiquement.
     * 
     * @return
     */
    private void isReattributionAutoReviseur() {
        try {
            int result = Integer.parseInt(GlobazSystem.getApplication(CEApplication.DEFAULT_APPLICATION_HERCULE)
                    .getProperty(CEListeControlesAEffectuerProcess.PROPERTY_REATTRIBUTION_AUTO).trim());

            if (result == 1) {
                reattributionAuto = true;
            }

        } catch (Exception e) {
            // Si non présent, la réattribution n'est pas automatique.
            reattributionAuto = false;
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private List<String> retrieveUnion() throws Exception {
        List<String> idAffilies = new ArrayList<String>();

        CEControlesAEffectuerUnionManager union = new CEControlesAEffectuerUnionManager();
        union.setSession(getSession());
        union.setForAnnee(getAnnee());
        union.setForAnneeCptr(getAnneeCptr());
        union.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < union.size(); i++) {
            CEControlesAEffectuerUnion id = (CEControlesAEffectuerUnion) union.getEntity(i);
            idAffilies.add(id.getIdAffiliation());
        }

        return idAffilies;
    }

    public void setAnnee(final String annee) {
        this.annee = annee;
    }

    public void setAnneeCptr(final String anneeCptr) {
        this.anneeCptr = anneeCptr;
    }

    public void setAucunControle(final boolean aucunControle) {
        this.aucunControle = aucunControle;
    }

    public void setGenreControle(final String string) {
        genreControle = string;
    }

    public void setIsAvecReviseur(final Boolean isAvecReviseur) {
        this.isAvecReviseur = isAvecReviseur;
    }

    public void setIsGenerationControleEmployeur(final Boolean boolean1) {
        isGenerationControleEmployeur = boolean1;
    }

    public void setIsTenirComptePerioAffilie(final Boolean boolean1) {
        isTenirComptePerioAffilie = boolean1;
    }

    public void setIsTenirComptePerioCaisse(final Boolean boolean1) {
        isTenirComptePerioCaisse = boolean1;
    }

    public void setListeExcel(final Boolean listeExcel) {
        this.listeExcel = listeExcel;
    }

    public void setListePdf(final Boolean listePdf) {
        this.listePdf = listePdf;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    public Boolean isWantAnneePrecedente() {
        return wantAnneePrecedente;
    }

    public void setWantAnneePrecedente(final Boolean wantAnneePrecedente) {
        this.wantAnneePrecedente = wantAnneePrecedente;
    }

    public Boolean getWantAnneePrecedente() {
        return wantAnneePrecedente;
    }
}
