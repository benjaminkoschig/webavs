package globaz.naos.itext;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.naos.util.AFIDEUtil;

public abstract class AFAbstractTiersDocument extends AFAbstractDocument {

    private static final long serialVersionUID = 1L;
    AFAdresseDestination adresse;

    public AFAbstractTiersDocument() throws Exception {
        super();
    }

    public AFAbstractTiersDocument(BSession session, String nomDocument) throws Exception {
        super(session, nomDocument);
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        adresse = null;
    }

    public AFAdresseDestination getAdresse() {
        if (adresse == null) {
            adresse = new AFAdresseDestination(getSession());
        }
        return adresse;
    }

    public String getFormulePolitesse() throws Exception {
        return getAdresse().getFormulePolitesse(getIdDestinataire());
    }

    @Override
    public String getIsoLangueDestinataire() throws Exception {
        return getAdresse().getLangueDestinataire(getIdDestinataire());
    }

    public String getLangueTiers() throws Exception {
        return getAdresse().getLangueTiers(getIdDestinataire());
    }

    @Override
    protected String getNomDestinataire() throws Exception {
        return getAdresse().getNomDestinataire(getIdDestinataire());
    }

    protected String[] getParams(String id, BSession session) throws Exception {
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        String[] params;
        if (JadeStringUtil.isIntegerEmpty(id)) {
            params = new String[2];
            params[0] = getFormulePolitesse();
            params[1] = params[0];
        } else {
            params = new String[3];
            params[0] = getFormulePolitesse();
            params[1] = JACalendar.format(envoiHandler.getDateEnvoi(id, session), getIsoLangueDestinataire());
            params[2] = params[0];
        }
        return params;
    }

    @Override
    public void setHeader(CaisseHeaderReportBean bean, String isoLangueTiers) throws Exception {
        bean.setAdresse(getAdresse().getAdresseDestinataire(getIdDestinataire(), getNumAff()));
        documentDate = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA() : getDateImpression();
        bean.setDate(JACalendar.format(documentDate, isoLangueTiers.toLowerCase()));
        bean.setNoAffilie(getNumAff());

        // Renseinge le Numéro IDE
        AFIDEUtil.addNumeroIDEInDoc(getSession(), bean, getIdAffiliation());

        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setUser(getSession().getUserInfo());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setNoAvs(" ");
    }

    @Override
    public void setNomDoc(String nomDoc) {
        super.setNomDoc(getIdTiers());
    }
}
