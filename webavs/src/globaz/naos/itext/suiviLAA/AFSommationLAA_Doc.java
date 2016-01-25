/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.suiviLAA;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFSommationLAA_Doc extends AFAbstractTiersDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFSommationLAA_Doc() throws Exception {
        super();
    }

    /**
     * @param session
     * @throws Exception
     */
    public AFSommationLAA_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFSommationLAA_Param.L_NOMDOCSOMMATION));
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
        fillDocInfo();
    }

    @Override
    protected void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber("0106CAF");
        super.fillDocInfo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getCategorie()
     */
    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_SOMMATION_LAA;
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
        return AFSommationLAA_Param.NB_LEVEL;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getNomDoc()
     */
    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFSommationLAA_Param.L_NOMDOCSOMMATION);
    }

    @Override
    protected String getTemplate() {
        return AFSommationLAA_Param.TEMPLATE_SOMMATION_LAA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#initDocument(java.lang.String)
     */
    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
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
                this.setParametres(AFSommationLAA_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFSommationLAA_Param.P_CORPS,
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
        bean.setRecommandee(true);
        super.setHeader(bean, isoLangueTiers);
    }

}
