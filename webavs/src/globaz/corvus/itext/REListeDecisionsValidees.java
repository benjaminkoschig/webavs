package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteListViewBean;
import globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lowagie.text.DocumentException;

public class REListeDecisionsValidees extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pour trier les lignes par nom prenom
     * 
     * @author BSC
     * 
     */
    private class DataLine implements Comparable<DataLine> {

        private String codePrestation = "";
        private String dateDebutDroit = "";
        private String dateFinDroit = "";
        private String detailAssure = "";
        private String etat = "";
        private String idDecision = "";
        private String nomPrenomcomparator;
        private String validePar = "";

        public DataLine(String nom, String prenom) {
            super();
            initNomPrenomForComparator(nom, prenom);
        }

        @Override
        public int compareTo(DataLine o) {
            return nomPrenomcomparator.compareTo(o.getNomPrenomcomparator());
        }

        public String getCodePrestation() {
            return codePrestation;
        }

        public String getDateDebutDroit() {
            return dateDebutDroit;
        }

        public String getDateFinDroit() {
            return dateFinDroit;
        }

        public String getDetailAssure() {
            return detailAssure;
        }

        public String getEtat() {
            return etat;
        }

        public String getIdDecision() {
            return idDecision;
        }

        public String getNomPrenomcomparator() {
            return nomPrenomcomparator;
        }

        public String getValidePar() {
            return validePar;
        }

        private void initNomPrenomForComparator(String nom, String prenom) {

            nomPrenomcomparator = JadeStringUtil.fillWithSpaces(nom, 40) + JadeStringUtil.fillWithSpaces(prenom, 40);
        }

        public void setCodePrestation(String codePrestation) {
            this.codePrestation = codePrestation;
        }

        public void setDateDebutDroit(String dateDebutDroit) {
            this.dateDebutDroit = dateDebutDroit;
        }

        public void setDateFinDroit(String dateFinDroit) {
            this.dateFinDroit = dateFinDroit;
        }

        public void setDetailAssure(String detailAssure) {
            this.detailAssure = detailAssure;
        }

        public void setEtat(String etat) {
            this.etat = etat;
        }

        public void setIdDecision(String idDecision) {
            this.idDecision = idDecision;
        }

        public void setValidePar(String validePar) {
            this.validePar = validePar;
        }
    }

    private String forDepuis = "";
    private String forJusqua = "";
    private List<DataLine> lines = new ArrayList<DataLine>();
    private Map<String, RERenteAccordee> raMap = new HashMap<String, RERenteAccordee>();

    public REListeDecisionsValidees() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, null, "", "liste des décisions validées", new REDecisionsManager(),
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    public REListeDecisionsValidees(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, null, "", "liste des décisions validées", new REDecisionsManager(),
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    /**
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        try {

            _setDocumentTitle("liste des décisions validées (" + getForDepuis() + " - " + getForJusqua() + ")");

            // Création du manager
            REDecisionsManager manager = (REDecisionsManager) _getManager();
            manager.setSession(getSession());
            manager.setForValideDes(forDepuis);
            manager.setForValideJusqua(forJusqua);
            manager.setOrderBy(REDecisionEntity.FIELDNAME_ID_DECISION);
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                abort();
                getMemoryLog().logMessage(getSession().getLabel("LISTE_DECISIONS_VALIDEES_AUCUNE"), FWServlet.ERROR,
                        getSession().getLabel("LISTE_DECISIONS_VALIDEES_AUCUNE"));
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_DECISIONS_VALIDEES);

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        if (JadeStringUtil.isEmpty(getForDepuis())) {
            this._addError(getSession().getLabel("LISTE_DECISIONS_VALIDEES_ERR_DEPUIS"));
        }

        if (JadeStringUtil.isEmpty(getForJusqua())) {
            this._addError(getSession().getLabel("LISTE_DECISIONS_VALIDEES_ERR_JUSQUA"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * Contenu des cellules
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {

        // valeurs
        REDecisionEntity decision = (REDecisionEntity) entity;

        // Récupération des pd pour la décisions
        REValidationDecisionsManager validMgr = new REValidationDecisionsManager();
        validMgr.setSession(getSession());
        validMgr.setForIdDecision(decision.getIdDecision());
        try {
            validMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
            e.printStackTrace();
        }

        if (!validMgr.isEmpty()) {

            for (int i = 0; i < validMgr.size(); i++) {
                REValidationDecisions valid = (REValidationDecisions) validMgr.get(i);

                REPrestationsDuesJointDemandeRenteListViewBean pdMgr = new REPrestationsDuesJointDemandeRenteListViewBean();
                pdMgr.setSession(getSession());
                pdMgr.setForIdPrestationDue(valid.getIdPrestationDue());
                try {
                    pdMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), "", "");
                    e.printStackTrace();
                }

                if (!pdMgr.isEmpty()) {

                    for (int j = 0; j < pdMgr.size(); j++) {
                        REPrestationsDuesJointDemandeRenteViewBean pd = (REPrestationsDuesJointDemandeRenteViewBean) pdMgr
                                .get(j);

                        // REtrieve de la ra (seulement si pas déjà fait !)
                        if (!raMap.containsKey(pd.getIdRenteAccordee())) {

                            RERenteAccordee ra = new RERenteAccordee();
                            ra.setSession(getSession());
                            ra.setIdPrestationAccordee(pd.getIdRenteAccordee());

                            try {
                                ra.retrieve();
                            } catch (Exception e) {
                                getMemoryLog().logMessage(e.getMessage(), "", "");
                                e.printStackTrace();
                            }

                            if (!ra.isNew()) {

                                // ajout dans la map pour ne plus afficher cette
                                // ra
                                raMap.put(pd.getIdRenteAccordee(), ra);

                                // affichage des lignes

                                PRTiersWrapper tier;
                                String detailAssure = "Tier introuvable pour id = " + ra.getIdTiersBeneficiaire();
                                DataLine l = null;

                                try {
                                    tier = PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersBeneficiaire());

                                    if (null != tier) {
                                        detailAssure = tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                                                + tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                                + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                                                + tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                                        l = new DataLine(tier.getProperty(PRTiersWrapper.PROPERTY_NOM),
                                                tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                                    }

                                } catch (Exception e) {
                                    getMemoryLog().logMessage(e.getMessage(), "", "");
                                    e.printStackTrace();
                                }

                                // pour trier les lignes par nom prénom
                                if (l == null) {
                                    l = new DataLine("", "");
                                }

                                l.setDetailAssure(detailAssure);
                                l.setCodePrestation(ra.getCodePrestation());
                                l.setDateDebutDroit(ra.getDateDebutDroit());
                                l.setDateFinDroit(ra.getDateFinDroit());
                                l.setEtat(getSession().getCodeLibelle(ra.getCsEtat()));
                                l.setValidePar(decision.getValidePar());
                                l.setIdDecision(decision.getIdDecision());

                                lines.add(l);
                            }

                        }
                    }

                }

            }

        }

    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_DECISIONS_VALIDEES_OBJET_MAIL");
    }

    public String getForDepuis() {
        return forDepuis;
    }

    public String getForJusqua() {
        return forJusqua;
    }

    /**
     * Initialisation des colonnes et des groupes
     */
    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("LISTE_DECISIONS_VALIDEES_ASSURE"), 60);
        this._addColumnCenter(getSession().getLabel("LISTE_DECISIONS_VALIDEES_CODE_PRESTATION"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DECISIONS_VALIDEES_DATE_DEBUT"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DECISIONS_VALIDEES_DATE_FIN"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DECISIONS_VALIDEES_ETAT"), 15);
        this._addColumnCenter(getSession().getLabel("LISTE_DECISIONS_VALIDEES_PAR"), 15);
        this._addColumnCenter(getSession().getLabel("LISTE_DECISIONS_VALIDEES_NO"), 15);

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForDepuis(String forDepuis) {
        this.forDepuis = forDepuis;
    }

    public void setForJusqua(String forJusqua) {
        this.forJusqua = forJusqua;
    }

    @Override
    protected void summary() throws FWIException {

        super.summary();

        Collections.sort(lines);

        for (DataLine l : lines) {

            _addCell(l.getDetailAssure());
            _addCell(l.getCodePrestation());
            _addCell(l.getDateDebutDroit());
            _addCell(l.getDateFinDroit());
            _addCell(l.getEtat());
            _addCell(l.getValidePar());
            _addCell(l.getIdDecision());

            try {
                this._addDataTableRow();
            } catch (DocumentException e) {
                getMemoryLog().logMessage(e.getMessage(), "", "");
                e.printStackTrace();
            }
        }

    }
}
