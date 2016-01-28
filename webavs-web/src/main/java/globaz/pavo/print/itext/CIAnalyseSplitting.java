package globaz.pavo.print.itext;

import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.process.FWProcess;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.splitting.CIAnnonceSplitting;
import globaz.pavo.db.splitting.CICumulSplitting;
import globaz.pavo.db.splitting.CICumulsRCI;
import globaz.pavo.db.splitting.CIDomicileSplitting;
import globaz.pavo.db.splitting.CIDomicileSplittingManager;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pavo.util.CIUtil;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CIAnalyseSplitting extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CIDomicileSplittingManager domManagerAssure = null;
    private CIDomicileSplittingManager domManagerConjoint = null;
    private CIDossierSplitting dossier = null;
    private boolean hasNext = true;
    private String idDossier = "";
    private BProcess m_context = null;
    private String refAssure = "";

    private String refConjoint = "";

    public CIAnalyseSplitting() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));

    }

    public CIAnalyseSplitting(BSession session) throws FWIException {
        this(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("MSG_APERCU_AVANT_SPLITTING"));
    }

    public CIAnalyseSplitting(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle(getSession().getLabel("MSG_APERCU_AVANT_SPLITTING"));
    }

    public CIAnalyseSplitting(FWProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle(getSession().getLabel("MSG_APERCU_AVANT_SPLITTING"));
    }

    protected void _footers() {
    }

    protected void _header() {
        super.setParametres(CIItextParam.getHeader(1), getSession().getLabel("APERCU_RCI_TITRE"));
        super.setParametres(CIItextParam.getHeader(2), getSession().getLabel("APERCU_RCI_MARIAGE"));
        super.setParametres(CIItextParam.getHeader(3), getSession().getLabel("APERCU_RCI_DIVORCE"));
        super.setParametres(CIItextParam.getHeader(4), getSession().getLabel("APERCU_RCI_ASSURE"));
        super.setParametres(CIItextParam.getHeader(5), getSession().getLabel("APERCU_RCI_CONJOINT"));
        super.setParametres(CIItextParam.getHeader(6), getSession().getLabel("APERCU_RCI_CLOTURE"));

        super.setParametres(CIItextParam.getHeaderLabel(1), getDateMariage());
        super.setParametres(CIItextParam.getHeaderLabel(2), getDateDivorce());
        super.setParametres(CIItextParam.getHeaderLabel(3), getAvsAssure());
        super.setParametres(CIItextParam.getHeaderLabel(4), getNomPrenomAssure());
        super.setParametres(CIItextParam.getHeaderLabel(5), getAvsConjoint());
        super.setParametres(CIItextParam.getHeaderLabel(6), getNomPrenomConjoint());
        super.setParametres(CIItextParam.getHeaderLabel(7), getClotureAssure());
        super.setParametres(CIItextParam.getHeaderLabel(8), getClotureConjoint());
        super.setParametres(CIItextParam.getHeaderLabel(9), dossier.getEtatFormateAssure());
        super.setParametres(CIItextParam.getHeaderLabel(10), dossier.getEtatFormateConjoint());

        super.setParametres("P_" + FWIImportParametre.getCol(1), getSession().getLabel("APERCU_RCI_REVENUS"));
        super.setParametres("P_" + FWIImportParametre.getCol(2), getSession().getLabel("APERCU_RCI_ANNEES"));

    }

    protected void _summary() {
        super.setParametres(CIItextParam.getSummary(1), getSession().getLabel("APERCU_RCI_LEGENDE"));

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        setDocumentTitle(getAvsAssure() + " / " + getAvsConjoint());
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            dossier = new CIDossierSplitting();
            dossier.setIdDossierSplitting(getIdDossier());
            dossier.setSession(getSession());
            dossier.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setTemplateFile("PAVO_CI_RCI");
    }

    /**
     * Construit le container pour la création de la liste des revenus pour l'assuré et son ex-conjoint
     * 
     * @param cumulsAssure
     * @param cumulsConjoint
     * @return
     */
    public Hashtable buildCumuls(Hashtable cumulsAssure, Hashtable cumulsConjoint) throws Exception {
        Hashtable result = new Hashtable();
        Iterator it = cumulsAssure.values().iterator();
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(getSession());
        ciManager.orderByAvs(false);

        while (it.hasNext()) {
            // boucle sur les année de l'assuré
            CICumulSplitting cumul = (CICumulSplitting) it.next();
            String anneeEnCours = cumul.getAnnee().toString();
            CICumulsRCI rci = new CICumulsRCI(cumul.getAnnee().intValue());
            rci.setCumulsAssure(cumul);
            if (cumulsConjoint.containsKey(anneeEnCours)) {
                // existe aussi pour le conjoint
                rci.setCumulsConjoint((CICumulSplitting) cumulsConjoint.get(anneeEnCours));
            }
            if (dossier != null) {
                // ajout des données de mariage
                // Modif jmc 12.10.2006, ajout du test après mariage
                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateMariage(), "01.01." + anneeEnCours)
                        && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDivorce(), "31.12."
                                + anneeEnCours)) {
                    // période de mariage
                    rci.setPeriodeMariage(true);
                }
                // ajout périodes à l'étranger
                if (isPeriodeEtranger(dossier.getIdTiersAssure(), anneeEnCours)) {
                    rci.setPeriodeEtrangerAssure(true);
                }
                if (isPeriodeEtranger(dossier.getIdTiersConjoint(), anneeEnCours)) {
                    rci.setPeriodeEtrangerConjoint(true);
                }
                // années jeunesse et rente
                CICompteIndividuel ci = ciManager.getCIRegistreAssures(dossier.getIdTiersAssure(), getTransaction());
                if (ci != null) {
                    if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                            "01.01." + String.valueOf(cumul.getAnnee().intValue() - 20), ci.getDateNaissance())) {
                        rci.setPeriodeJeunesseAssure(true);
                    }
                    if (CIUtil.isRetraite(new JADate(ci.getDateNaissance()), ci.getSexe(), cumul.getAnnee().intValue())) {
                        rci.setPeriodeRenteAssure(true);
                    }
                }
                ci = ciManager.getCIRegistreAssures(dossier.getIdTiersConjoint(), getTransaction());
                if (ci != null) {
                    if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                            "01.01." + String.valueOf(cumul.getAnnee().intValue() - 20), ci.getDateNaissance())) {
                        rci.setPeriodeJeunesseConjoint(true);
                    }
                    if (CIUtil.isRetraite(new JADate(ci.getDateNaissance()), ci.getSexe(), cumul.getAnnee().intValue())) {
                        rci.setPeriodeRenteConjoint(true);
                    }
                }
            }
            result.put(anneeEnCours, rci);
        }
        // ajouter les années restantes pour le conjoint
        it = cumulsConjoint.values().iterator();
        while (it.hasNext()) {
            // boucle sur les année ddu conjoint
            CICumulSplitting cumul = (CICumulSplitting) it.next();
            String anneeEnCours = cumul.getAnnee().toString();
            if (!result.containsKey(anneeEnCours)) {
                // cette année n'est pas encore dans la liste
                CICumulsRCI rci = new CICumulsRCI(cumul.getAnnee().intValue());
                rci.setCumulsConjoint(cumul);
                if (dossier != null) {
                    // ajout des données de mariage
                    if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateMariage(), "01.01."
                            + anneeEnCours)
                            && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDivorce(), "31.12."
                                    + anneeEnCours)) {
                        // période de mariage
                        rci.setPeriodeMariage(true);
                    }
                    // périodes étranger
                    if (isPeriodeEtranger(dossier.getIdTiersConjoint(), anneeEnCours)) {
                        rci.setPeriodeEtrangerConjoint(true);
                    }
                    // années jeunesse
                    CICompteIndividuel ci = ciManager.getCIRegistreAssures(dossier.getIdTiersConjoint(),
                            getTransaction());
                    if (ci != null) {
                        if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                "01.01." + String.valueOf(cumul.getAnnee().intValue() - 20), ci.getDateNaissance())) {
                            rci.setPeriodeJeunesseConjoint(true);
                        }
                        if (CIUtil.isRetraite(new JADate(ci.getDateNaissance()), ci.getSexe(), cumul.getAnnee()
                                .intValue())) {
                            rci.setPeriodeRenteConjoint(true);
                        }
                    }
                }
                result.put(anneeEnCours, rci);
            }
        }

        return result;
    }

    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber("0065CCI");
        CIAnnonceSplitting annonceAssure = new CIAnnonceSplitting(getTransaction(), getRefAssure(), false);
        CIAnnonceSplitting annonceConjoint = new CIAnnonceSplitting(getTransaction(), getRefConjoint(), false);
        Hashtable container = buildCumuls(annonceAssure.getCumuls(), annonceConjoint.getCumuls());

        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource((new TreeMap(container)).values());
        super.setDataSource(source);

        _header();
        _footers();
        _summary();

    }

    /**
     * Retourne le numéro AVS de l'assuré
     * 
     * @return String
     */
    public String getAvsAssure() {
        if (dossier != null) {
            return NSUtil.formatAVSUnknown(dossier.getIdTiersAssure());
        }
        return "";
    }

    /**
     * Retourne le numéro AVS du conjoint
     * 
     * @return String
     */
    public String getAvsConjoint() {
        if (dossier != null) {
            return NSUtil.formatAVSUnknown(dossier.getIdTiersConjoint());
        }
        return "";
    }

    /**
     * Retourne la dernière clôture de l'assuré
     * 
     * @return String
     */
    public String getClotureAssure() {
        if (dossier != null) {
            CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
            ciManager.setSession(getSession());
            ciManager.orderByAvs(false);
            CICompteIndividuel ci = ciManager.getCIRegistreAssures(dossier.getIdTiersAssure(), getTransaction());
            if (ci != null) {
                return ci.getDerniereClotureFormattee();
            }
        }
        return "";
    }

    /**
     * Retourne la dernière clôture du conjoint
     * 
     * @return String
     */
    public String getClotureConjoint() {
        if (dossier != null) {
            CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
            ciManager.setSession(getSession());
            ciManager.orderByAvs(false);
            CICompteIndividuel ci = ciManager.getCIRegistreAssures(dossier.getIdTiersConjoint(), getTransaction());
            if (ci != null) {
                return ci.getDerniereClotureFormattee();
            }
        }
        return "";
    }

    /**
     * Returns the m_context.
     * 
     * @return BProcess
     */
    public BProcess getContext() {
        return m_context;
    }

    /**
     * Retourne la date de divorce
     * 
     * @return String
     */
    public String getDateDivorce() {
        if (dossier != null) {
            return dossier.getDateDivorce();
        }
        return "";
    }

    /**
     * Retourne la date de mariage
     * 
     * @return String
     */
    public String getDateMariage() {
        if (dossier != null) {
            return dossier.getDateMariage();
        }
        return "";
    }

    /**
     * @return
     */
    public CIDossierSplitting getDossier() {
        return dossier;
    }

    /**
     * @return
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * Retourne le nom et prénom de l'assuré
     * 
     * @return String
     */
    public String getNomPrenomAssure() {
        if (dossier != null) {
            return dossier.getTiersAssureNomComplet();
        }
        return "";
    }

    /**
     * Retourne le nom et prénom du conjoint
     * 
     * @return String
     */
    public String getNomPrenomConjoint() {
        if (dossier != null) {
            return dossier.getTiersConjointNomComplet();
        }
        return "";
    }

    /**
     * @return
     */
    public String getRefAssure() {
        return refAssure;
    }

    /**
     * @return
     */
    public String getRefConjoint() {
        return refConjoint;
    }

    private boolean isPeriodeEtranger(String idPartenaire, String annee) throws Exception {
        CIDomicileSplittingManager domManager = null;
        if (domManagerAssure != null && domManagerAssure.getForIdTiersPartenaire().equals(idPartenaire)) {
            domManager = domManagerAssure;
        } else if (domManagerConjoint != null && domManagerConjoint.getForIdTiersPartenaire().equals(idPartenaire)) {
            domManager = domManagerConjoint;
        }
        if (domManager == null) {
            domManager = new CIDomicileSplittingManager();
            domManager.setSession(getSession());
            domManager.setForIdDossierSplitting(dossier.getIdDossierSplitting());
            domManager.setForIdTiersPartenaire(idPartenaire);
            domManager.find(getTransaction());
        }
        for (int i = 0; i < domManager.size(); i++) {
            CIDomicileSplitting dom = (CIDomicileSplitting) domManager.getEntity(i);
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dom.getDateDebut(), annee)
                    && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dom.getDateFin(), "31.12." + annee)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (hasNext) {
            hasNext = false;
            return true;
        }
        return false;
    }

    /**
     * Sets the m_context.
     * 
     * @param m_context
     *            The m_context to set
     */
    public void setContext(BProcess context) {
        m_context = context;
    }

    /**
     * @param splitting
     */
    public void setDossier(CIDossierSplitting splitting) {
        dossier = splitting;
    }

    /**
     * @param string
     */
    public void setIdDossier(String string) {
        idDossier = string;
    }

    /**
     * @param string
     */
    public void setRefAssure(String string) {
        refAssure = string;
    }

    /**
     * @param string
     */
    public void setRefConjoint(String string) {
        refConjoint = string;
    }

}
