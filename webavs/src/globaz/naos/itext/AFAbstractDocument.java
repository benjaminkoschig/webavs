/*
 * Créé le 14 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;
import globaz.naos.application.AFApplication;
import globaz.pyxis.api.ITIRole;
import java.text.MessageFormat;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class AFAbstractDocument extends FWIDocumentManager implements ILEGeneration {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDocument;
    private String csTypeAffiliation;
    private String dateImpression;
    protected LEParamEnvoiDataSource docCourant;
    protected LEEnvoiDataSource documentDataSource;
    protected String documentDate;
    private String idAffiliation;
    private String idDestinataire;
    private String idEnvoiParent;
    private String idTiers;
    private String libellePlan;
    // private boolean generateOnlyOne = true;
    private String numAff = "";
    private String periode;
    private boolean publishDocument = true;

    // **********************************************************************************
    public AFAbstractDocument() throws Exception {
        setDocumentRoot(AFApplication.DEFAULT_APPLICATION_NAOS_REP);
        // constructeur sans args, prerequis FW2
    }

    public AFAbstractDocument(BSession session) throws Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "");
        setSession((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(session));
    }

    public AFAbstractDocument(BSession session, String nomDocument) throws Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, nomDocument);
        setSession((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(session));
    }

    public void addPropriete(String csTypeProp, String valeur) {
        if (ILEConstantes.CS_PARAM_GEN_ID_TIERS.equals(csTypeProp)) {
            setIdTiers(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_NUMERO.equals(csTypeProp)) {
            setNumAff(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ROLE.equals(csTypeProp)) {
            setCsTypeAffiliation(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT.equals(csTypeProp)) {
            setIdEnvoiParent(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(csTypeProp)) {
            setIdDestinataire(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_PERIODE.equals(csTypeProp)) {
            setPeriode(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION.equals(csTypeProp)) {
            setIdAffiliation(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_PLAN.equals(csTypeProp)) {
            setLibellePlan(valeur);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterPrintDocument ()
     */
    @Override
    public void afterPrintDocument() {
        deleteAllDocument();
        getExporter().deleteAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport ()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        // TODO Raccord de méthode auto-généré
    }

    public void beforeBuildReport(ICaisseReportHelper caisseReportHelper) throws FWIException {
        try {
            setImpressionParLot(true);
            // set le type de document
            setCsDoc(getDocumentDataSource().getField(LEEnvoiDataSource.CS_TYPE_DOCUMENT));
            String isoLangueTiers = getIsoLangueDestinataire();
            // init l'entête de la lettre
            if (caisseReportHelper == null) {
                caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                        getSession().getApplication(), isoLangueTiers);
            }
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            setHeader(headerBean, isoLangueTiers);
            caisseReportHelper.addHeaderParameters(this, headerBean);
            caisseReportHelper.addSignatureParameters(this);
            initDocument(isoLangueTiers);
            loadCatTexte(isoLangueTiers);

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, this.getClass().getName());
            JadeLogger.error(this, e.getMessage());
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // paramètre le doc pdf sans la fenêtre des options ouvertes
        super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
        // set email adresse si vide
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            setEMailAddress(getSession().getUserEMail());
        }
        // set le modèle
        try {
            setTemplateFile(getTemplate());
        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage());
        }
    }

    @Override
    public void createDataSource() throws Exception {

        // parse tous les paramètres et set le doc courant
        for (int i = 0; i < getDocumentDataSource().getParamEnvoi().size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = getDocumentDataSource().getParamEnvoi().getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }
    }

    protected void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAff());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(getNumAff()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAff());
        }
        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getDocumentInfo().getDocumentProperty("numero.affilie.formatte"), getDocumentInfo()
                            .getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "afterPrintDocument()", e);
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setDocumentDate(getDateImpression());
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
    }

    protected String format(String message, String[] args) {
        StringBuffer msgBuf = new StringBuffer();

        msgBuf.append(message.charAt(0));

        for (int idChar = 1; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar - 1) == '\'') && (message.charAt(idChar) != '\'')) {
                msgBuf.append('\'');
            }

            msgBuf.append(message.charAt(idChar));
        }

        return MessageFormat.format(msgBuf.toString(), args).toString();
    }

    public abstract String getCategorie();

    public String getCsDoc() {
        return csDocument;
    }

    /**
     * @return
     */
    public String getCsTypeAffiliation() {
        return csTypeAffiliation;
    }

    /**
     * @return
     */
    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @return
     */
    public LEEnvoiDataSource getDocumentDataSource() {
        return documentDataSource;
    }

    public abstract String getDomaine();

    @Override
    protected String getEMailObject() {
        String emailObject;
        try {
            emailObject = "L'impression du document " + getNomDoc() + " pour l'affilié " + getNumAff()
                    + " a été réalisée avec succès";
        } catch (Exception e) {
            emailObject = "Erreur : " + e.getMessage();
        }
        return emailObject;
    }

    protected ICTDocument[] getICTDocument(String isoLangue) {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "Error while api for document");
        }
        document.setISession(getSession());
        document.setCsDomaine(getDomaine());
        document.setCsTypeDocument(getCategorie());
        document.setDefault(new Boolean(true));
        document.setCodeIsoLangue(isoLangue);
        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }
        return res;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * @return
     */
    public String getIdDestinataire() {
        return idDestinataire;
    }

    public String getIdEnvoiParent() {
        return idEnvoiParent;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public abstract String getIsoLangueDestinataire() throws Exception;

    public String getLibellePlan() {
        return libellePlan;
    }

    public abstract int getNbLevel();

    protected abstract String getNomDestinataire() throws Exception;

    public abstract String getNomDoc() throws Exception;

    /**
     * @return
     */
    protected String getNumAff() {
        return numAff;
    }

    /**
     * @return
     */
    public String getPeriode() {
        return periode;
    }

    @Override
    public LEEnvoiDataSource getResult() {
        return documentDataSource;
    }

    // méthodes abstraites
    // *************************************************************
    protected abstract String getTemplate();

    protected abstract void initDocument(String isoLangue) throws Exception;

    public boolean isPublishDocument() {
        return publishDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
	 *
	 */
    protected void loadCatTexte(String isoLangueTiers) throws Exception {
        StringBuffer buffer;
        // passe les paramètres nécessaires
        ICTDocument listeTxt = getICTDocument(isoLangueTiers)[0];
        if (listeTxt != null) {
            ICTListeTextes crtListe;
            for (int i = 1; i < getNbLevel() + 1; i++) {
                buffer = new StringBuffer();
                try {
                    crtListe = listeTxt.getTextes(i);
                } catch (Exception e) {
                    crtListe = null;
                }
                if (crtListe != null) {
                    for (Iterator titresIter = crtListe.iterator(); titresIter.hasNext();) {
                        if (buffer.length() > 0) {
                            buffer.append("\n\n");
                        }
                        buffer.append(((ICTTexte) titresIter.next()).getDescription());
                    }
                    setFieldToCatTexte(i, buffer.toString());
                }
            }
        }

    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext = false;
        if (docCourant == null) {
            hasNext = true;
            docCourant = getDocumentDataSource().getParamEnvoi();
        }
        return hasNext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setCsDoc(java.lang.String)
     */
    public void setCsDoc(String csDoc) {
        csDocument = csDoc;
    }

    /**
     * @param string
     */
    public void setCsTypeAffiliation(String string) {
        csTypeAffiliation = string;
    }

    /**
     * @param string
     */
    @Override
    public void setDateImpression(String date) {
        dateImpression = date;
    }

    /**
     * @param source
     */
    @Override
    public void setDocumentDataSource(LEEnvoiDataSource source) {
        documentDataSource = source;
    }

    public abstract void setFieldToCatTexte(int i, String value) throws Exception;

    public abstract void setHeader(CaisseHeaderReportBean headerBean, String isoLangueTiers) throws Exception;

    /**
     * @param string
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    /**
     * @param string
     */
    public void setIdDestinataire(String string) {
        idDestinataire = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setIdEnvoiParent(java.lang .String)
     */
    public void setIdEnvoiParent(String idEnvoiParent) {
        this.idEnvoiParent = idEnvoiParent;
    }

    public void setIdTiers(String i) {
        idTiers = i;
    }

    public void setLibellePlan(String libellePlan) {
        this.libellePlan = libellePlan;
    }

    @Override
    public void setNomDoc(String nomDoc) {
        getExporter().setExportFileName(nomDoc);
    }

    /**
     * @param string
     */
    protected void setNumAff(String string) {
        numAff = string;
    }

    /**
     * @param string
     */
    public void setPeriode(String string) {
        periode = string;
    }

    @Override
    public void setPublishDocument(boolean value) {
        publishDocument = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setSessionModule(globaz.globall .db.BSession)
     */
    @Override
    public void setSessionModule(BSession session) throws Exception {
        setSession((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(session));
    }

}
