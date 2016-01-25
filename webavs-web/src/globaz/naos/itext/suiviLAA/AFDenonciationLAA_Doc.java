/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.suiviLAA;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.itext.AFAdresseDestination;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFDenonciationLAA_Doc extends AFAbstractTiersDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    AFAdresseDestination adresse;

    public AFDenonciationLAA_Doc() throws Exception {
        super();
    }

    /**
     * @param session
     * @throws Exception
     */
    public AFDenonciationLAA_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFDenonciationLAA_Param.L_NOMDOCDENONCIATION));
    }

    @Override
    public void beforeBuildReport() {
        ICaisseReportHelper caisseReportHelper;
        try {
            caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), super.getIsoLangueDestinataire());
            CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
            signBean.setService2("");
            signBean.setSignataire2("");
            signBean.setService(getSession().getLabel("MSG_SERVICE_NOM"));
            signBean.setSignataire(getSession().getUserFullName());
            caisseReportHelper.addSignatureParameters(this, signBean);
            super.beforeBuildReport(caisseReportHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createDataSource() throws Exception {
        super.createDataSource();

        // première chose à faire renseigner le docinfo
        fillDocInfo();
    }

    @Override
    protected void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber("0107CAF");
        super.fillDocInfo();
    }

    // public String getIsoLangueDestinataire() throws Exception {
    // return getAdresse().getIsoLangueCaisseSupplLAA();
    // }
    /**
     * @return
     */
    @Override
    public AFAdresseDestination getAdresse() {
        if (adresse == null) {
            adresse = new AFAdresseDestination(getSession());
        }
        return adresse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getCategorie()
     */
    @Override
    public String getCategorie() {
        for (int i = 0; i < getDocumentDataSource().getParamEnvoi().size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = getDocumentDataSource().getParamEnvoi().getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }
        AFAffiliationViewBean aff = new AFAffiliationViewBean();
        aff.setAffiliationId(getIdAffiliation());
        aff.setSession(getSession());
        try {
            aff.retrieve();
        } catch (Exception e) {
        }
        if (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_LTN)) {

            return CodeSystem.TYPE_CAT_ANNONCE_LAA_TSE;
        } else {

            return CodeSystem.TYPE_CAT_ANNONCE_LAA;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getDomaine()
     */
    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LAA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getNbLevel()
     */
    @Override
    public int getNbLevel() {
        return AFDenonciationLAA_Param.NB_LEVEL;
    }

    @Override
    protected String getNomDestinataire() throws Exception {
        return getAdresse().getNomDestinataire(getIdTiers());
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFDenonciationLAA_Param.L_NOMDOCDENONCIATION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractTiersDocument#getParams(java.lang.String, globaz.globall.db.BSession)
     */
    @Override
    protected String[] getParams(String id, BSession session) throws Exception {
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        String date = envoiHandler.getDateEnvoi(id, session);
        String[] params = new String[1];
        params[0] = JACalendar.format(date, getIsoLangueDestinataire());
        return params;
    }

    @Override
    protected String getTemplate() {
        return AFDenonciationLAA_Param.TEMPLATE_DENONCIATION_LAA;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
        StringBuffer coordEmpl = new StringBuffer(getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AFFILIE + isoLangueTiers));

        // Recherche IDE
        CaisseHeaderReportBean headerbean = new CaisseHeaderReportBean();
        AFIDEUtil.addNumeroIDEInDocForNumAffilie(getSession(), headerbean, getNumAff());
        if (!JadeStringUtil.isEmpty(headerbean.getNumeroIDE())) {
            coordEmpl = AFIDEUtil.removeEndingSpacesAndDoublePoint(coordEmpl);
            coordEmpl.append("/");
            coordEmpl.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_IDE
                    + isoLangueTiers));
            coordEmpl.append("  ");
            coordEmpl.append(getNumAff());
            coordEmpl.append("/");
            coordEmpl.append(headerbean.getNumeroIDE());
        } else {
            coordEmpl.append(getNumAff());
        }

        coordEmpl.append("\n");
        coordEmpl.append(getAdresse().getAdresseDestinataire(getIdTiers(), getNumAff()));
        this.setParametres(AFDenonciationLAA_Param.P_ADRESSE_EMPLOYEUR, coordEmpl.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#setFieldToCatTexte(int, java.lang.String)
     */
    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        switch (i) {
            case 1:
                this.setParametres(AFDenonciationLAA_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFDenonciationLAA_Param.P_CORPS, value);
                break;
            case 3:
                this.setParametres(AFDenonciationLAA_Param.P_CORPS2, value);
                break;
            case 4:
                this.setParametres(AFDenonciationLAA_Param.P_ANNEXES, value);
                break;
            case 5:
                this.setParametres(AFDenonciationLAA_Param.P_ANNEXE,
                        format(value, getParams(getIdEnvoiParent(), getSession())));
                break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#setHeader(globaz.caisse.report.helper .CaisseHeaderReportBean,
     * java.lang.String)
     */
    @Override
    public void setHeader(CaisseHeaderReportBean bean, String isoLangueTiers) throws Exception {
        super.setHeader(bean, isoLangueTiers);
        // sécurité, être sûr que le tiers est différent du tiers destinataire
        if (getIdDestinataire().equals(getIdTiers())) {
            bean.setAdresse("");
        } else {
            bean.setAdresse(getAdresse().getAdresseDestinataire(getIdDestinataire()));
        }
        documentDate = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA() : getDateImpression();
        bean.setDate(JACalendar.format(documentDate, isoLangueTiers));
        bean.setNoAffilie(" ");
        // bean.setNomCollaborateur(" ");
        // bean.setNoAvs(" ");
    }
}
