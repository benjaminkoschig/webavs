package globaz.aries.print;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.common.JadeCodingUtil;
import globaz.naos.application.AFApplication;
import globaz.pyxis.api.ITIRole;
import globaz.webavs.common.ICommonConstantes;
import java.text.MessageFormat;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public abstract class ARAbstractDoc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }
        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

    private BApplication app;
    private ICTDocument catalogue;

    private String dateFacturation;

    private String langueIsoDoc;

    protected void _setHeader(CaisseHeaderReportBean bean, AffiliationComplexModel affiliationComplex) throws Exception {
        AdresseTiersDetail adresseTiersAffiliation = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                affiliationComplex.getTiersSimpleModel().getIdTiers(), true, JACalendar.todayJJsMMsAAAA(),
                ICommonConstantes.CS_APPLICATION_COTISATION, AdresseService.CS_TYPE_COURRIER, "");
        bean.setAdresse(adresseTiersAffiliation.getAdresseFormate());
        bean.setDate(dateFacturation);
        bean.setNoAffilie(affiliationComplex.getAffiliationSimpleModel().getAffilieNumero());
        bean.setConfidentiel(false);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    @Override
    public abstract void beforeBuildReport() throws FWIException;

    @Override
    public abstract void beforeExecuteReport() throws FWIException;

    protected void catalogue(String domaine, String type) throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            helper.setCsDomaine(domaine);
            helper.setCsTypeDocument(type);
            helper.setCodeIsoLangue(langueIsoDoc);
            helper.setActif(Boolean.TRUE);
            helper.setDefault(Boolean.TRUE);

            // charger le catalogue de texte
            ICTDocument[] candidats = helper.load();
            if ((candidats != null) && (candidats.length > 0)) {
                setCatalogue(candidats[0]);
            }
        } catch (Exception e) {
            setCatalogue(null);
        }
        if (getCatalogue() == null) {
            this.abort(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR);
            throw new FWIException(getSession().getLabel("CATALOGUE_INTROUVABLE"));
        }
    }

    @Override
    public abstract void createDataSource() throws Exception;

    protected void fillDocInfo(AffiliationComplexModel affiliationComplex, String numInforom) throws JAException {
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setDocumentTypeNumber(numInforom);
        getDocumentInfo().setDocumentProperty("annee",
                Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte",
                affiliationComplex.getAffiliationSimpleModel().getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliationComplex.getAffiliationSimpleModel().getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affiliationComplex.getAffiliationSimpleModel().getAffilieNumero());
        }
        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliationComplex.getTiersSimpleModel().getIdTiers(),
                    getSession(), ITIRole.CS_AFFILIE, getDocumentInfo().getDocumentProperty("numero.affilie.formatte"),
                    getDocumentInfo().getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "createDataSource()", e);
        }
    }

    protected String formatMessage(StringBuffer message, Object[] args) {
        return ARAbstractDoc.formatMessage(message.toString(), args);
    }

    public BApplication getApp() {
        return app;
    }

    public ICTDocument getCatalogue() {
        return catalogue;
    }

    public String getDateFacturation() {
        return dateFacturation;
    }

    public String getLangueIsoDoc() {
        return langueIsoDoc;
    }

    protected String getTexte(int niveau, int position, Object[] args) throws FWIException {
        String texte;
        try {
            if (args != null) {
                texte = ARAbstractDoc.formatMessage(getCatalogue().getTextes(niveau).getTexte(position)
                        .getDescription(), args);
            } else {
                texte = getCatalogue().getTextes(niveau).getTexte(position).getDescription();
            }
            return texte;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public abstract GlobazJobQueue jobQueue();

    @Override
    public abstract boolean next() throws FWIException;

    protected void putHeader(AffiliationComplexModel affiliationComplex) throws Exception {
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoDoc);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean, affiliationComplex);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addSignatureParameters(this);
        getImporter().getParametre().put(
                ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), "header.filename"));
    }

    public void setApp(BApplication app) {
        this.app = app;
    }

    public void setCatalogue(ICTDocument catalogue) {
        this.catalogue = catalogue;
    }

    public void setDateFacturation(String dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public void setLangueIsoDoc(String langueIsoDoc) {
        this.langueIsoDoc = langueIsoDoc;
    }
}
