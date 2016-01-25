package globaz.helios.process.consolidation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.helios.api.consolidation.ICGConsolidationSolde;
import globaz.helios.api.consolidation.ICGConsolidationSoldeManager;
import globaz.helios.application.CGApplication;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.bouclement.CGBouclementManager;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableListViewBean;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.db.consolidation.CGSoldeBouclementSuccursale;
import globaz.helios.db.consolidation.CGSoldeBouclementSuccursaleManager;
import globaz.helios.db.consolidation.CGSuccursale;
import globaz.helios.db.consolidation.CGSuccursaleManager;
import globaz.helios.process.consolidation.utils.CGConsolidationConstants;
import globaz.helios.process.consolidation.utils.CGImportConsolidationUtils;
import globaz.helios.process.consolidation.utils.CGProcessConsolidationUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.client.xml.JadeXmlReaderException;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import java.io.FileInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CGProcessImportConsolidation extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CGExerciceComptableViewBean exerciceComptable;
    private String fileName;
    private String idExerciceComptable;

    /**
     * Constructor for CGProcessImportConsolidation.
     */
    public CGProcessImportConsolidation() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImportConsolidation.
     * 
     * @param parent
     */
    public CGProcessImportConsolidation(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportConsolidation.
     * 
     * @param session
     */
    public CGProcessImportConsolidation(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        Document doc = getDocument();
        Element consolidation = doc.getDocumentElement();
        String noCaisse = consolidation.getAttribute(CGConsolidationConstants.ATTRIBUT_CAISSE);
        if (!testNoCaisse(noCaisse)) {
            _addError(getTransaction(), getSession().getLabel("CONSOLIDATION_NO_CAISSE_NON_VALIDE"));
            return false;
        }
        String dateDebut = consolidation.getAttribute(CGConsolidationConstants.ATTRIBUT_DATEDEBUT);
        String dateFin = consolidation.getAttribute(CGConsolidationConstants.ATTRIBUT_DATEFIN);
        if (!testDateExerciceComptable(dateDebut, dateFin)) {
            _addError(getTransaction(), getSession().getLabel("CONSOLIDATION_DATE_NON_VALIDE"));
            return false;
        }
        if (isFileNameBlank()) {
            _addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
            return false;
        }
        String idSuccursale = getIdSuccursale(consolidation.getAttribute(CGConsolidationConstants.ATTRIBUT_AGENCE));
        NodeList elements = consolidation.getElementsByTagName(CGConsolidationConstants.TAG_PERIODE);
        for (int i = 0; i < elements.getLength(); i++) {
            Element periodeElem = (Element) elements.item(i);
            NodeList compteElements = periodeElem.getElementsByTagName(CGConsolidationConstants.TAG_COMPTE);
            for (int k = 0; k < compteElements.getLength(); k++) {
                Element compteElem = (Element) compteElements.item(k);
                String idExterne = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_IDEXTERNE);
                String libelle = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_LIBELLE);
                String idNature = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_IDNATURE);
                String idDomaine = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_IDDOMAINE);
                String idGenre = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_IDGENRE);
                String soldeActif = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_SOLDEACTIF);
                String soldePassif = CGImportConsolidationUtils.getChildFirstValue(compteElem,
                        CGConsolidationConstants.TAG_SOLDEPASSIF);
                String idCompte = getIdCompte(idExterne, libelle, idNature, idDomaine, idGenre);
                String idPeriodeComptable = getIdPeriodeComptable(periodeElem);
                updateSolde(new CGSoldeManager(), idSuccursale, idPeriodeComptable, idCompte, soldeActif, soldePassif);
                updateSolde(new CGSoldeManager(), idSuccursale, CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, idCompte,
                        soldeActif, soldePassif);
                updateSolde(new CGSoldeBouclementSuccursaleManager(), idSuccursale, idPeriodeComptable, idCompte,
                        soldeActif, soldePassif);
                updateSolde(new CGSoldeBouclementSuccursaleManager(), idSuccursale,
                        CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, idCompte, soldeActif, soldePassif);
            }
        }

        lissageSecteur199();

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (isFileNameBlank()) {
            _addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
        }
    }

    /**
     * 
     * @param sourceFileName
     * @return
     * @throws JadeXmlReaderException
     */
    private Document getDocument() throws Exception {
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(), Jade
                .getInstance().getHomeDir() + "work/" + getFileName());

        return JadeXmlReader.parseFile(new FileInputStream(Jade.getInstance().getHomeDir() + "work/" + getFileName()));
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("CONSOLIDATION_IMPORT_ERROR");
        } else {
            return getSession().getLabel("CONSOLIDATION_IMPORT_OK");
        }
    }

    /**
     * Retourne l'éxercice comptable en cours du siège.
     * 
     * @return
     * @throws Exception
     */
    private CGExerciceComptableViewBean getExerciceComptable() throws Exception {
        if (exerciceComptable == null) {
            exerciceComptable = CGProcessConsolidationUtil.getExerciceComptable(getSession(), getTransaction(),
                    getIdExerciceComptable());
        }
        return exerciceComptable;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Retourne l'id de bouclement du mandat siège.
     * 
     * @return
     * @throws Exception
     */
    private String getIdBouclement(Element e) throws Exception {
        CGBouclementManager manager = new CGBouclementManager();
        manager.setSession(getSession());
        manager.setForIdMandat(getExerciceComptable().getIdMandat());

        if (e.getAttribute(CGConsolidationConstants.ATTRIBUT_CODE).equals(CGPeriodeComptable.CS_CODE_ANNUEL)) {
            manager.setForIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL_AVS);
        } else {
            manager.setForIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_MENSUEL_AVS);
        }

        manager.find();
        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(getSession().getLabel("GLOBAL_BOUCLEMENT_INEXISTANT"));
        }
        return ((CGBouclement) manager.getFirstEntity()).getIdBouclement();
    }

    /**
     * Retourne l'id compte du compte spécifié dans l'élément xml importer.<br/>
     * Si le compte n'éxiste pas encore dans le plan comptable siège ce dernier sera ajouté.
     * 
     * @param idExterne
     * @param libelle
     * @param idNature
     * @param idDomaine
     * @param idGenre
     * @return
     * @throws Exception
     */
    private String getIdCompte(String idExterne, String libelle, String idNature, String idDomaine, String idGenre)
            throws Exception {
        CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
        manager.setSession(getSession());
        manager.setForIdExterne(idExterne);
        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setForIdNature(idNature);
        manager.find(getTransaction());
        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }
        if (manager.isEmpty()) {
            CGPlanComptableViewBean planComptable = new CGPlanComptableViewBean();
            planComptable.setSession(getSession());
            planComptable.setIdExerciceComptable(getIdExerciceComptable());
            planComptable.setIdMandat(getExerciceComptable().getIdMandat());
            planComptable.setIdExterne(idExterne);
            planComptable.setLibelleFr(libelle);
            planComptable.setIdNature(idNature);
            planComptable.setIdDomaine(idDomaine);
            planComptable.setIdGenre(idGenre);
            planComptable.setAReouvrir(new Boolean(true));
            planComptable.setCodeISOMonnaie(CGCompte.CODE_ISO_CHF);
            planComptable.setEstCompteAvs(false);
            planComptable.setEcritureManuelleEstVerrouille(new Boolean(false));
            planComptable.setEstPeriode(new Boolean(false));
            planComptable.setEstVerrouille(new Boolean(false));
            planComptable.add(getTransaction());
            if (planComptable.hasErrors()) {
                throw new Exception(planComptable.getErrors().toString());
            }
            return planComptable.getIdCompte();
        } else {
            return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdCompte();
        }
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Retourne l'id de la période comptable contenu dans l'élément xml.<br/>
     * Si la période comptable n'est pas encore ouverte dans le plan comptable du siège cette dernière sera
     * créée/ouverte.
     * 
     * @param e
     * @return
     * @throws Exception
     */
    private String getIdPeriodeComptable(Element e) throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());
        manager.setForCode(e.getAttribute(CGConsolidationConstants.ATTRIBUT_CODE));
        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.find(getTransaction());
        if (manager.hasErrors() || manager.size() > 1) {
            throw new Exception(manager.getErrors().toString());
        }
        if (manager.isEmpty()) {
            CGPeriodeComptable periodeComptable = new CGPeriodeComptable();
            periodeComptable.setSession(getSession());
            periodeComptable.setIdExerciceComptable(getIdExerciceComptable());
            periodeComptable.setIdTypePeriode(e.getAttribute(CGConsolidationConstants.ATTRIBUT_IDTYPE));
            periodeComptable.setIdBouclement(getIdBouclement(e));
            periodeComptable.setDateDebut(e.getAttribute(CGConsolidationConstants.ATTRIBUT_DATEDEBUT));
            periodeComptable.setDateFin(e.getAttribute(CGConsolidationConstants.ATTRIBUT_DATEFIN));
            periodeComptable.setEstCloture(new Boolean(true));
            periodeComptable.add(getTransaction());
            return periodeComptable.getIdPeriodeComptable();
        }
        return ((CGPeriodeComptable) manager.getFirstEntity()).getIdPeriodeComptable();
    }

    /**
     * Retourne l'id de la succursale en fonction du numéro d'agence.
     * 
     * @param noAgence
     * @return
     * @throws Exception
     */
    public String getIdSuccursale(String noAgence) throws Exception {
        CGSuccursaleManager manager = new CGSuccursaleManager();
        manager.setSession(getSession());
        manager.setForNumeroSuccursale(noAgence);
        manager.find(getTransaction());
        if (manager.hasErrors() || (manager.size() > 1)) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            throw new Exception(getSession().getLabel("AUCUNE_AGENCE"));
        }

        return ((CGSuccursale) manager.getFirstEntity()).getIdSuccursale();
    }

    /**
     * Retourne le no d'agence de l'application.
     * 
     * @return
     */
    public String getNoAgence() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getExerciceComptable().getMandat().getNoCaisse())) {
                return getExerciceComptable().getMandat().getNoAgence();
            } else {
                CGApplication application = (CGApplication) GlobazServer.getCurrentSystem().getApplication(
                        CGApplication.DEFAULT_APPLICATION_HELIOS);
                return CaisseHelperFactory.getInstance().getNoAgence(application);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le no de caisse de l'application.
     * 
     * @return
     */
    public String getNoCaisse() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getExerciceComptable().getMandat().getNoCaisse())) {
                return getExerciceComptable().getMandat().getNoCaisse();
            } else {
                CGApplication application = (CGApplication) GlobazServer.getCurrentSystem().getApplication(
                        CGApplication.DEFAULT_APPLICATION_HELIOS);
                return CaisseHelperFactory.getInstance().getNoCaisse(application);
            }
        } catch (Exception e) {
            return "";
        }
    }

    private CGSolde getSolde(String idPeriodeComptable, String idCompte) throws Exception {
        CGSoldeManager manager = new CGSoldeManager();
        manager.setISession(getSession());
        manager.setForIdPeriodeComptable(idPeriodeComptable);
        manager.setForIdCompte(idCompte);
        manager.setForIdExerComptable(getIdExerciceComptable());
        manager.setForIdMandat(getExerciceComptable().getIdMandat());
        manager.setForEstPeriode(new Boolean(!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)));
        manager.find(getTransaction());

        if (manager.isEmpty()) {
            return null;
        }

        return (CGSolde) manager.getFirstEntity();
    }

    /**
     * Le nom de fichier est-il vide ?
     * 
     * @return
     */
    private boolean isFileNameBlank() {
        return JadeStringUtil.isBlank(getFileName());
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Si un lissage des comptes 199.1220x s'effectue il faut reporter ces modifications sur avoir et doit (ainsi
     * qu'avoir et doit de la période 0).
     * 
     * @param idPeriode
     * @param soldeCompteReport
     * @param soldeCompteBecomingZero
     * @throws Exception
     */
    private void lissageAdaptDoitAvoir(String idPeriode, CGSolde soldeCompteReport, CGSolde soldeCompteBecomingZero)
            throws Exception {
        FWCurrency avoirCompteBecomingZero = new FWCurrency(soldeCompteBecomingZero.getAvoir());
        FWCurrency doitCompteBecomingZero = new FWCurrency(soldeCompteBecomingZero.getDoit());

        FWCurrency tmp = new FWCurrency(soldeCompteReport.getAvoir());
        tmp.add(avoirCompteBecomingZero);
        soldeCompteReport.setAvoir(tmp.toString());
        soldeCompteReport.setAvoirProvisoire(tmp.toString());
        soldeCompteBecomingZero.setAvoir("0.00");
        soldeCompteBecomingZero.setAvoirProvisoire("0.00");

        tmp = new FWCurrency(soldeCompteReport.getDoit());
        tmp.add(doitCompteBecomingZero);
        soldeCompteReport.setDoit(tmp.toString());
        soldeCompteReport.setDoitProvisoire(tmp.toString());
        soldeCompteBecomingZero.setDoit("0.00");
        soldeCompteBecomingZero.setDoitProvisoire("0.00");

        if (!CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE.equals(idPeriode)) {
            CGSolde soldeCompteReportPeriodeZero = getSolde(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE,
                    soldeCompteReport.getIdCompte());
            tmp = new FWCurrency(soldeCompteReportPeriodeZero.getAvoir());
            tmp.add(avoirCompteBecomingZero);
            soldeCompteReportPeriodeZero.setAvoir(tmp.toString());
            soldeCompteReportPeriodeZero.setAvoirProvisoire(tmp.toString());

            tmp = new FWCurrency(soldeCompteReportPeriodeZero.getDoit());
            tmp.add(doitCompteBecomingZero);
            soldeCompteReportPeriodeZero.setDoit(tmp.toString());
            soldeCompteReportPeriodeZero.setDoitProvisoire(tmp.toString());

            CGSolde soldeCompteReportBecomingZeroPeriodeZero = getSolde(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE,
                    soldeCompteBecomingZero.getIdCompte());
            tmp = new FWCurrency(soldeCompteReportBecomingZeroPeriodeZero.getAvoir());
            tmp.sub(avoirCompteBecomingZero);
            soldeCompteReportBecomingZeroPeriodeZero.setAvoir(tmp.toString());
            soldeCompteReportBecomingZeroPeriodeZero.setAvoirProvisoire(tmp.toString());

            tmp = new FWCurrency(soldeCompteReportBecomingZeroPeriodeZero.getDoit());
            tmp.sub(doitCompteBecomingZero);
            soldeCompteReportBecomingZeroPeriodeZero.setDoit(tmp.toString());
            soldeCompteReportBecomingZeroPeriodeZero.setDoitProvisoire(tmp.toString());

            soldeCompteReportPeriodeZero.update(getTransaction());
            soldeCompteReportBecomingZeroPeriodeZero.update(getTransaction());
        }
    }

    /**
     * Si un lissage des comptes 199.1220x s'effectue il faut mettre à jour le solde de la période 0.
     * 
     * @param idPeriode
     * @param compteToAdd
     * @param compteToSub
     * @param soldeDiff
     * @throws Exception
     */
    private void lissageReportSoldePeriodeToutExercice(String idPeriode, CGPlanComptableViewBean compteToAdd,
            CGPlanComptableViewBean compteToSub, FWCurrency soldeDiff) throws Exception {
        if (!CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE.equals(idPeriode)) {
            CGSolde soldeCompteToAdd = getSolde(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, compteToAdd.getIdCompte());
            FWCurrency soldeExerciceToAdd = new FWCurrency(soldeCompteToAdd.getSolde());
            soldeExerciceToAdd.add(soldeDiff);
            soldeCompteToAdd.setSolde(soldeExerciceToAdd.toString());
            soldeCompteToAdd.setSoldeProvisoire(soldeExerciceToAdd.toString());

            CGSolde soldeCompteToSub = getSolde(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, compteToSub.getIdCompte());
            FWCurrency soldeExerciceToSub = new FWCurrency(soldeCompteToSub.getSolde());
            soldeExerciceToSub.sub(soldeDiff);
            soldeCompteToSub.setSolde(soldeExerciceToSub.toString());
            soldeCompteToSub.setSoldeProvisoire(soldeExerciceToSub.toString());

            soldeCompteToAdd.update(getTransaction());
            soldeCompteToSub.update(getTransaction());
        }
    }

    private void lissageSecteur199() throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_ASC_AND_TYPE_ASC);

        manager.find(getTransaction());

        for (int i = 0; i < manager.size(); i++) {
            lissageSecteur199(((CGPeriodeComptable) manager.get(i)).getIdPeriodeComptable());
        }

        lissageSecteur199(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE);
    }

    /**
     * Lissage des comptes 1990.120X.XXX0
     * 
     * @throws Exception
     */
    private void lissageSecteur199(String idPeriode) throws Exception {
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());
        planManager.setForIdMandat(getExerciceComptable().getIdMandat());
        planManager.setForIdExerciceComptable(getIdExerciceComptable());
        planManager.setBeginWithIdExterne("199.120");
        planManager.setForIdPeriodeComptable(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE);
        planManager.setForEstPeriode(new Boolean(false));

        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < planManager.size(); i++) {
            CGPlanComptableViewBean compte120X = (CGPlanComptableViewBean) planManager.getEntity(i);

            CGSolde soldeCompte120X = getSolde(idPeriode, compte120X.getIdCompte());
            if (soldeCompte120X != null) {
                FWCurrency soldeCumul1 = new FWCurrency(soldeCompte120X.getSolde());

                if (!soldeCumul1.isZero()) {
                    String idExterne = "199.220"
                            + compte120X.getIdExterne().substring(compte120X.getIdExterne().length() - 1,
                                    compte120X.getIdExterne().length());

                    CGPlanComptableManager planManager2 = new CGPlanComptableManager();
                    planManager2.setSession(getSession());
                    planManager2.setForIdMandat(getExerciceComptable().getIdMandat());
                    planManager2.setForIdExerciceComptable(getIdExerciceComptable());
                    planManager2.setForIdExterne(idExterne);
                    planManager2.setForIdPeriodeComptable(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE);
                    planManager2.setForEstPeriode(new Boolean(false));

                    planManager2.find(getTransaction(), BManager.SIZE_NOLIMIT);

                    if (!planManager2.isEmpty()) {
                        CGPlanComptableViewBean compte220X = (CGPlanComptableViewBean) planManager2.getFirstEntity();

                        CGSolde soldeCompte220X = getSolde(idPeriode, compte220X.getIdCompte());
                        if (soldeCompte220X != null) {
                            FWCurrency soldeCumul2 = new FWCurrency(soldeCompte220X.getSolde());

                            if (!soldeCumul2.isZero()) {
                                FWCurrency cumul = new FWCurrency(soldeCumul1.toString());
                                cumul.add(soldeCumul2);
                                if (cumul.isPositive()) {
                                    // Update solde 120x
                                    // A zéro solde 220x

                                    soldeCompte120X.setSolde(cumul.toString());
                                    soldeCompte120X.setSoldeProvisoire(cumul.toString());

                                    soldeCompte220X.setSolde("0.00");
                                    soldeCompte220X.setSoldeProvisoire("0.00");

                                    lissageAdaptDoitAvoir(idPeriode, soldeCompte120X, soldeCompte220X);

                                    soldeCompte120X.update(getTransaction());
                                    soldeCompte220X.update(getTransaction());

                                    lissageReportSoldePeriodeToutExercice(idPeriode, compte120X, compte220X,
                                            soldeCumul2);
                                } else {
                                    // A zéro solde 120x
                                    // Update solde 220x

                                    soldeCompte120X.setSolde("0.00");
                                    soldeCompte120X.setSoldeProvisoire("0.00");

                                    soldeCompte220X.setSolde(cumul.toString());
                                    soldeCompte220X.setSoldeProvisoire(cumul.toString());

                                    lissageAdaptDoitAvoir(idPeriode, soldeCompte220X, soldeCompte120X);

                                    soldeCompte120X.update(getTransaction());
                                    soldeCompte220X.update(getTransaction());

                                    lissageReportSoldePeriodeToutExercice(idPeriode, compte220X, compte120X,
                                            soldeCumul1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Mise à jour ou set le solde, débit et crédit de l'entité solde.
     * 
     * @param entity
     * @param soldeActif
     * @param soldePassif
     */
    private void setSolde(ICGConsolidationSolde entity, String soldeActif, String soldePassif) {
        FWCurrency doit = new FWCurrency(entity.getDoit());
        FWCurrency avoir = new FWCurrency(entity.getAvoir());
        FWCurrency solde = new FWCurrency(entity.getSolde());
        if (!JadeStringUtil.isBlank(soldeActif)) {
            doit.add(soldeActif);
            solde.add(soldeActif);
        }
        if (!JadeStringUtil.isBlank(soldePassif)) {
            avoir.sub(soldePassif);
            solde.sub(soldePassif);
        }
        entity.setDoit(doit.toString());
        entity.setAvoir(avoir.toString());
        entity.setSolde(solde.toString());
        entity.setDoitProvisoire(doit.toString());
        entity.setAvoirProvisoire(avoir.toString());
        entity.setSoldeProvisoire(solde.toString());
    }

    /**
     * Test la date de début et date de fin du fichier xml avec l'exercice comptable en cours. Les dates doivent
     * correspondres pour pouvoir importé les montants.
     * 
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws Exception
     */
    private boolean testDateExerciceComptable(String dateDebut, String dateFin) throws Exception {
        return (getExerciceComptable().getDateDebut().equals(dateDebut) && getExerciceComptable().getDateFin().equals(
                dateFin));
    }

    /**
     * Test le no de caisse.<br/>
     * Si le no de caisse du fichier d'exportation != du numéro de caisse de l'application d'importation => erreur.
     * 
     * @param noCaisse
     * @return
     * @throws Exception
     */
    private boolean testNoCaisse(String noCaisse) throws Exception {
        return getNoCaisse().equals(noCaisse);
    }

    /**
     * Mise à jour du solde, débit et crédit.<br/>
     * Méthod utilisé pour mettre à jour CGSolde et CGSoldeBouclementSuccursale.
     * 
     * @param manager
     * @param idSuccursale
     * @param idPeriodeComptable
     * @param idCompte
     * @param soldeActif
     * @param soldePassif
     * @throws Exception
     */
    private void updateSolde(ICGConsolidationSoldeManager manager, String idSuccursale, String idPeriodeComptable,
            String idCompte, String soldeActif, String soldePassif) throws Exception {
        manager.setISession(getSession());
        manager.setForIdSuccursale(idSuccursale);
        manager.setForIdPeriodeComptable(idPeriodeComptable);
        manager.setForIdCompte(idCompte);
        manager.setForIdExerComptable(getIdExerciceComptable());
        manager.setForIdMandat(getExerciceComptable().getIdMandat());
        manager.setForEstPeriode(new Boolean(!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)));
        manager.find(getTransaction());
        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }
        if (manager.isEmpty()) {
            ICGConsolidationSolde entity;
            if (manager instanceof CGSoldeBouclementSuccursaleManager) {
                entity = new CGSoldeBouclementSuccursale();
            } else {
                entity = new CGSolde();
            }
            entity.setISession(getSession());
            entity.setIdSuccursale(idSuccursale);
            entity.setIdPeriodeComptable(idPeriodeComptable);
            entity.setIdExerComptable(getIdExerciceComptable());
            entity.setIdMandat(getExerciceComptable().getIdMandat());
            entity.setIdCompte(idCompte);
            entity.setEstPeriode(new Boolean(!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)));
            entity.setAvoir(new FWCurrency().toString());
            entity.setDoit(new FWCurrency().toString());
            entity.setSolde(new FWCurrency().toString());
            setSolde(entity, soldeActif, soldePassif);
            entity.add(getTransaction());
        } else {
            ICGConsolidationSolde entity;
            if (manager instanceof CGSoldeBouclementSuccursaleManager) {
                entity = (CGSoldeBouclementSuccursale) manager.getFirstEntity();
            } else {
                entity = (CGSolde) manager.getFirstEntity();
            }
            setSolde(entity, soldeActif, soldePassif);
            entity.update(getTransaction());
        }
    }
}
