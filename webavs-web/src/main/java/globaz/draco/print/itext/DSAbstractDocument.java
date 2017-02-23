/*
 * Créé le 5 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.print.itext;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.draco.application.DSApplication;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class DSAbstractDocument extends FWIDocumentManager implements ILEGeneration {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDocument;
    private String csTypeAffiliation;
    private String dateImpression;
    protected LEParamEnvoiDataSource docCourant;
    // Variables
    protected LEEnvoiDataSource documentDataSource;
    private String idDestinataire;
    private String idEnvoiParent;
    private String idTiers;
    private String numAff = "";
    private String periode;

    // **********************************************************************************
    private boolean publishDocument = true;

    public DSAbstractDocument() throws Exception {
        super();
    }

    public DSAbstractDocument(BSession session) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "");
        setSession((BSession) GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).newSession(session));
    }

    public DSAbstractDocument(BSession session, String nomDocument) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, nomDocument);
        setSession((BSession) GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).newSession(session));
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
        try {
            setCsDoc(getDocumentDataSource().getField(LEEnvoiDataSource.CS_TYPE_DOCUMENT));
            String isoLangueTiers = getIsoLangueDestinataire();
            initDocument(isoLangueTiers);

            getDocumentInfo().setDocumentDate(getDateImpression());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, getClass().getName());
            JadeLogger.error(this, e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            // setEMailAddress("sda@globaz.ch");
            setEMailAddress(getSession().getUserEMail());
        }
        setTemplateFile(getTemplate());
        // TODO modifier l'adresse e-mail
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        for (int i = 0; i < docCourant.size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = docCourant.getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }

    }

    public String getCsDoc() {
        return csDocument;
    }

    /**
     * @return
     */
    public String getCsDocument() {
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
    public LEParamEnvoiDataSource getDocCourant() {
        return docCourant;
    }

    /**
     * @return
     */
    public LEEnvoiDataSource getDocumentDataSource() {
        return documentDataSource;
    }

    /**
     * @return
     */
    public String getIdDestinataire() {
        return idDestinataire;
    }

    /**
     * @return
     */
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

    protected abstract String getNomDestinataire() throws Exception;

    public abstract String getNomDoc() throws Exception;

    /**
     * @return
     */
    public String getNumAff() {
        return numAff;
    }

    /**
     * @return
     */
    public String getPeriode() {
        return periode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setNomDoc(java.lang.String)
     */
    /*
     * public void setNomDoc(String nomDoc) { // TODO Raccord de méthode auto-généré
     * 
     * } /* (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#getResult()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
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
    public void setCsDocument(String string) {
        csDocument = string;
    }

    /**
     * @param string
     */
    public void setCsTypeAffiliation(String string) {
        csTypeAffiliation = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setDateImpression(java.lang .String)
     */
    @Override
    public void setDateImpression(String date) {
        dateImpression = date;

    }

    /**
     * @param source
     */
    public void setDocCourant(LEParamEnvoiDataSource source) {
        docCourant = source;
    }

    /**
     * @param source
     */
    @Override
    public void setDocumentDataSource(LEEnvoiDataSource source) {
        documentDataSource = source;
    }

    public void setEmailAdresse() {
        // TODO Raccord de méthode auto-généré
    }

    public abstract void setHeader(CaisseHeaderReportBean headerBean) throws Exception;

    /**
     * @param string
     */
    public void setIdDestinataire(String string) {
        idDestinataire = string;
    }

    /**
     * @param string
     */
    public void setIdEnvoiParent(String string) {
        idEnvoiParent = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setNumAff(String string) {
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
}
