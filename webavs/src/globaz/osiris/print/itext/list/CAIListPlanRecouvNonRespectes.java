package globaz.osiris.print.itext.list;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CACouvertureSectionManager;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrementManager;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandre Cuva, 13-mai-2005
 */
public class CAIListPlanRecouvNonRespectes extends CADocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Entity pour l'impression des plans */
    protected class CAIPlanEcheanceSection {
        /** Les échéances */
        private CAEcheancePlan echeance;
        /** plans de recouvrement */
        private CAPlanRecouvrement plan;
        /** Les sections */
        private CASection section;

        /**
         * @return La valeur courante de la propriété
         */
        public CAEcheancePlan getEcheance() {
            return echeance;
        }

        /**
         * @return La valeur courante de la propriété
         */
        public CAPlanRecouvrement getPlan() {
            return plan;
        }

        /**
         * @return La valeur courante de la propriété
         */
        public CASection getSection() {
            return section;
        }

        /**
         * @param string
         *            La nouvelle valeur de la propriété
         */
        public void setEcheance(CAEcheancePlan plan) {
            echeance = plan;
        }

        /**
         * @param string
         *            La nouvelle valeur de la propriété
         */
        public void setPlan(CAPlanRecouvrement recouvrement) {
            plan = recouvrement;
        }

        /**
         * @param string
         *            La nouvelle valeur de la propriété
         */
        public void setSection(CASection section) {
            this.section = section;
        }

    }

    public static final String NUMERO_REFERENCE_INFOROM = "0045GCA";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "CAIListPlanRecouvNonRespectes";
    public final static String TRICA_NOM = "Nom";
    /** Tri des plans de recouvrement */
    public final static String TRICA_NUMERO = "Numero";
    private static final String XLS_DOC_NAME = "ListePlanRecouvNonRespestes";
    private String beforeNoAffilie = "";
    /** Données du formulaire */
    /** date de référence */
    private String dateRef = "";
    private String fromNoAffilie = "";
    private List idRoles;
    private ArrayList<Map> liste = new ArrayList<Map>();
    /** tri des comptes annexes */
    private String triCA = "";
    private String typeImpression = "pdf";
    private CAOsirisContainer xlsContainer = new CAOsirisContainer();

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAIListPlanRecouvNonRespectes(BProcess parent) throws FWIException {
        super(parent, CAIListPlanRecouvNonRespectes.TEMPLATE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAIListPlanRecouvNonRespectes(BSession parent) throws FWIException {
        super(parent, CAIListPlanRecouvNonRespectes.TEMPLATE_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        try {
            imprimerPlansRecouvNonRespectes();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }
        setNumeroReferenceInforom(CAIListPlanRecouvNonRespectes.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public boolean beforePrintDocument() {
        if ("xls".equals(getTypeImpression())) {
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        // Récupération des données
        ArrayList planEcheanceSectionListeTriee = (ArrayList) currentEntity();
        String modeRecouvrement = "";

        // _setLangueFromTiers();

        // Gestion du modèle et du titre
        setTemplateFile(CAIListPlanRecouvNonRespectes.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("OSIRIS_LETTRE_PLAN_RECOUV") + " "
                + JACalendar.format(getDateRef(), JACalendar.FORMAT_DDsMMsYYYY));

        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(null, false, false, false);

        // Renseigne les paramètres du document
        this.setParametres(CAILettrePlanRecouvParam.COMPANYNAME,
                _getProperty(CADocumentManager.JASP_PROP_BODY_COMPANY_NAME, ""));
        this.setParametres(CAILettrePlanRecouvParam.DATEREF,
                JACalendar.format(getDateRef(), JACalendar.FORMAT_DDsMMsYYYY));
        modeRecouvrement += getSession().getCodeLibelle(CAPlanRecouvrement.CS_BVR) + ",";
        if (!JadeStringUtil.isBlank(modeRecouvrement)) {
            modeRecouvrement = modeRecouvrement.substring(0, modeRecouvrement.length() - 1);
        }
        this.setParametres(CAILettrePlanRecouvParam.TYPEPLAN, modeRecouvrement);

        // Tri des données
        if (getTriCA().equals(CAIListPlanRecouvNonRespectes.TRICA_NUMERO)) {
            this.setParametres(CAILettrePlanRecouvParam.TRICA,
                    _getProperty(CADocumentManager.JASP_PROP_BODY_TRI_NUMERO, ""));
            Collections.sort((List) planEcheanceSectionListeTriee, new CAIListPlanNumeroCompteComparator());
        } else {
            this.setParametres(CAILettrePlanRecouvParam.TRICA,
                    _getProperty(CADocumentManager.JASP_PROP_BODY_TRI_NOM, ""));
            Collections.sort((List) planEcheanceSectionListeTriee, new CAIListPlanNomCompteComparator());
        }

        // Renseigne les lignes dans le tableau du document à partir de la liste
        // triées

        for (Iterator iter = planEcheanceSectionListeTriee.iterator(); iter.hasNext();) {
            CAIPlanEcheanceSection plan = (CAIPlanEcheanceSection) iter.next();
            // Ajoute la ligne dans le document
            HashMap map = new HashMap();
            if (!JadeStringUtil.isBlank(plan.getPlan().getCompteAnnexe().getId())
                    && !JadeStringUtil.isBlank(plan.getPlan().getCompteAnnexe().getTiers().getId())) {
                map.put(CAILettrePlanRecouvParam.COL1,
                        _getProperty(CADocumentManager.JASP_PROP_BODY_AFFILIE, " "
                                + plan.getPlan().getCompteAnnexe().getIdExterneRole() + " "
                                + plan.getPlan().getCompteAnnexe().getTiers().getNom()));
            }
            // if (!StringUtils.isStringEmpty(plan.getSection().getId()))
            // map.put(CAIParameter.COL2,
            // plan.getSection().getDescription(_getLangue()));
            map.put(CAILettrePlanRecouvParam.COL2,
                    JACalendar.format(plan.getEcheance().getDateExigibilite(), JACalendar.FORMAT_DDsMMsYYYY));
            map.put(CAILettrePlanRecouvParam.COL3, new Double(plan.getEcheance().getMontant()));
            map.put(CAILettrePlanRecouvParam.COL4, plan.getPlan().getIdPlanRecouvrement());
            liste.add(map);
        }
        if (liste.size() > 0) {
            this.setDataSource(liste);
        }

        if ("xls".equals(getTypeImpression())) {
            printXlsDocument();
        }
    }

    /**
     * @return the beforeNoAffilie
     */
    public String getBeforeNoAffilie() {
        return beforeNoAffilie;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateRef() {
        if (JadeStringUtil.isBlank(dateRef)) {
            return JACalendar.todayJJsMMsAAAA();
        } else {
            return dateRef;
        }
    }

    /**
     * @return the fromNoAffilie
     */
    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    /**
     * @return the idRoles
     */
    public List getIdRoles() {
        return idRoles;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getTriCA() {
        return triCA;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Imprimer la liste des plans de recouvrement non respectés
     */
    private void imprimerPlansRecouvNonRespectes() throws JAException {
        CAPlanRecouvrementManager planManager = new CAPlanRecouvrementManager();
        CACouvertureSectionManager sectionCouverteManager = new CACouvertureSectionManager();
        CAEcheancePlanManager echeancePlanManager = new CAEcheancePlanManager();
        ArrayList listEntity = new ArrayList();
        try {
            initPlanManager(planManager);

            getParent().setProgressScaleValue(planManager.size());

            for (int i = 0; (i < planManager.size()) && !getParent().isAborted(); i++) {
                // Recherche les écheances pour le plan de recouvrement
                echeancePlanManager.setSession(getSession());
                echeancePlanManager.setForIdPlanRecouvrement(((CAPlanRecouvrement) planManager.getEntity(i))
                        .getIdPlanRecouvrement());
                echeancePlanManager.setToDateExigibilite(getDateRef());
                echeancePlanManager.setForDateEffectiveIsNull();
                echeancePlanManager.find(getTransaction());

                if (getParent().isAborted()) {
                    return;
                }

                // Recherche les sections pour le plan de recouvrement
                sectionCouverteManager.setSession(getSession());
                sectionCouverteManager.setForIdPlanRecouvrement(((CAPlanRecouvrement) planManager.getEntity(i))
                        .getIdPlanRecouvrement());
                sectionCouverteManager.find(getTransaction());

                if (getParent().isAborted()) {
                    return;
                }

                // Pout chaque écheance trouvée
                for (int b = 0; b < echeancePlanManager.size(); b++) {
                    CAIPlanEcheanceSection entity = new CAIPlanEcheanceSection();
                    // Ajout du plan
                    entity.setPlan((CAPlanRecouvrement) planManager.getEntity(i));
                    // Ajout de l'écheance
                    entity.setEcheance((CAEcheancePlan) echeancePlanManager.getEntity(b));

                    listEntity.add(entity);
                }
                getParent().incProgressCounter();
            }
            if (listEntity.size() > 0) {
                addEntity(listEntity);
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
            super.setMsgType(FWViewBeanInterface.WARNING);
            super.setMessage(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
            throw new JAException(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
        } finally {
            planManager = null;
            echeancePlanManager = null;
            listEntity = null;
        }
    }

    /**
     * @param planManager
     * @throws Exception
     */
    private void initPlanManager(CAPlanRecouvrementManager planManager) throws Exception {
        planManager.setSession(getSession());
        planManager.setForIdEtat(CAPlanRecouvrement.CS_ACTIF);
        planManager.setForSelectionRole(CARole.listeIdsRolesPourUtilisateurCourant(getSession()));
        ArrayList listForIdModeRecouvrementIn = new ArrayList();
        listForIdModeRecouvrementIn.add(CAPlanRecouvrement.CS_BVR);
        planManager.setForIdModeRecouvrementIn(listForIdModeRecouvrementIn);

        // Liste de role
        if (getIdRoles() != null) {
            StringBuffer clause = new StringBuffer("");
            for (Iterator idRoleIter = getIdRoles().iterator(); idRoleIter.hasNext();) {
                clause.append((String) idRoleIter.next());

                if (idRoleIter.hasNext()) {
                    clause.append(",");
                }
            }
            planManager.setForSelectionRole(clause.toString());
        }

        // Tranche d'affilies
        if (!JadeStringUtil.isBlank(getFromNoAffilie())) {
            planManager.setFromIdExternalRole(getFromNoAffilie());
        }
        if (!JadeStringUtil.isBlank(getBeforeNoAffilie())) {
            planManager.setUntilIdExternalRole(getBeforeNoAffilie());
        }

        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
    }

    /**
     * Préparation des données pour le document excel
     */
    private void prepareDataForXLSDoc() {

        // Entetes du fichier excel
        xlsContainer.addValue("numeroInforom", CAIListPlanRecouvNonRespectes.NUMERO_REFERENCE_INFOROM);

        // entete date
        xlsContainer.addValue(CAILettrePlanRecouvParam.DATEREF,
                getImporter().getParametre().get(CAILettrePlanRecouvParam.DATEREF));
        // entete tri
        xlsContainer.addValue(CAILettrePlanRecouvParam.TRICA,
                getImporter().getParametre().get(CAILettrePlanRecouvParam.TRICA));
        // entete plan
        xlsContainer.addValue(CAILettrePlanRecouvParam.TYPEPLAN,
                getImporter().getParametre().get(CAILettrePlanRecouvParam.TYPEPLAN));

        // espace
        xlsContainer.addValue(CAILettrePlanRecouvParam.HEADER_BLANK1, "");
        xlsContainer.addValue(CAILettrePlanRecouvParam.HEADER_BLANK2, "");

        // liste
        int size = liste.size();
        for (int i = 0; i < size; i++) {

            Map myMap = liste.get(i);

            xlsContainer.addValue(CAILettrePlanRecouvParam.COL1, myMap.get(CAILettrePlanRecouvParam.COL1));
            xlsContainer.addValue(CAILettrePlanRecouvParam.COL2, myMap.get(CAILettrePlanRecouvParam.COL4));
            xlsContainer.addValue(CAILettrePlanRecouvParam.COL3, myMap.get(CAILettrePlanRecouvParam.COL2));
            xlsContainer.addValue(CAILettrePlanRecouvParam.COL4,
                    ((Double) myMap.get(CAILettrePlanRecouvParam.COL3)).toString());
        }
    }

    public void printXlsDocument() throws Exception {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CAIListPlanRecouvNonRespectes.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAIListPlanRecouvNonRespectes.XLS_DOC_NAME
                            + ".xml");

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            docInfoExcel.setDocumentTitle(CAIListPlanRecouvNonRespectes.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(CAIListPlanRecouvNonRespectes.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        } catch (Exception e) {
            throw new Exception("Error generating excel file", e);
        }
    }

    /**
     * @param beforeNoAffilie
     *            the beforeNoAffilie to set
     */
    public void setBeforeNoAffilie(String beforeNoAffilie) {
        this.beforeNoAffilie = beforeNoAffilie;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateRef(String string) {
        dateRef = string;
    }

    /**
     * @param fromNoAffilie
     *            the fromNoAffilie to set
     */
    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    /**
     * @param idRoles
     *            the idRoles to set
     */
    public void setIdRoles(List idRoles) {
        this.idRoles = idRoles;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setTriCA(String string) {
        triCA = string;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
