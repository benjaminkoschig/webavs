/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.suiviAttestIP;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
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
public class AFAttestationIP_Doc extends AFAbstractTiersDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFAttestationIP_Doc() throws Exception {
        super();
    }

    /**
     * @param session
     * @throws Exception
     */
    public AFAttestationIP_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFAttestationIP_Param.L_NOMDOCATTESTATION));
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
        getDocumentInfo().setDocumentTypeNumber("0251CAF");
        super.fillDocInfo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getCategorie()
     */
    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_ATTESTATION_IP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getDomaine()
     */
    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LPP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getNbLevel()
     */
    @Override
    public int getNbLevel() {
        return AFAttestationIP_Param.NB_LEVEL;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getNomDoc()
     */
    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFAttestationIP_Param.L_NOMDOCATTESTATION);
    }

    @Override
    protected String getTemplate() {
        return AFAttestationIP_Param.TEMPLATE_ATTESTATION_IP;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
        StringBuilder coordEmpl = new StringBuilder();
        coordEmpl.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AFFILIE
                + isoLangueTiers));

        // Recherche IDE
        CaisseHeaderReportBean headerbean = new CaisseHeaderReportBean();
        AFIDEUtil.addNumeroIDEInDocForNumAffilie(getSession(), headerbean, getNumAff());
        if (!JadeStringUtil.isEmpty(headerbean.getNumeroIDE())) {
            coordEmpl = AFIDEUtil.removeEndingSpacesAndDoublePoint(coordEmpl);
            coordEmpl.append("/");
            coordEmpl.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_IDE
                    + isoLangueTiers));
            coordEmpl.append(" ");
            coordEmpl.append(getNumAff());
            coordEmpl.append("/");
            coordEmpl.append(headerbean.getNumeroIDE());
        } else {
            coordEmpl.append(getNumAff());
        }

        coordEmpl.append("\n" + getAdresse().getAdresseDestinataire(getIdTiers(), getNumAff()));
        this.setParametres(AFAttestationIP_Param.P_ADRESSE_EMPLOYEUR, coordEmpl.toString());
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
                this.setParametres(AFAttestationIP_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFAttestationIP_Param.P_CORPS, value);
                break;
            case 3:
                this.setParametres(AFAttestationIP_Param.P_CORPS2, value);
                break;
            case 4:
                this.setParametres(AFAttestationIP_Param.P_ANNEXE, value);
                break;
            case 5:
                this.setParametres(AFAttestationIP_Param.P_ANNEXES, value);
                break;
            case 6:
                this.setParametres(AFAttestationIP_Param.P_COPIE, value);
                break;
            case 7:
                String[] param = new String[1];
                param[0] = (new AFAdresseDestination(getSession())).getAdresseDestinataire(getIdTiers(), getNumAff());
                this.setParametres(AFAttestationIP_Param.P_COPIE2, format(value, param));
                break;
        }

    }
}
