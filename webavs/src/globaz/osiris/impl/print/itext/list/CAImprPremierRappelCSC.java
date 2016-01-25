package globaz.osiris.impl.print.itext.list;

import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.globall.api.BIDocument;
import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.webavs.common.ICommonConstantes;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Sylvain Crelier
 * @version 1.0.0
 * 
 *          Préparer les données à remplir pour impression du premier rappel
 */
public class CAImprPremierRappelCSC implements ICADocumentRappelCSC {
    protected IntAdresseCourrier adresseCourrier = null;
    protected String dateRappel = "";
    protected String dateSurDocument = "";
    protected JasperPrint extraitCompteDoc = null;
    protected String idExterneRole = "";
    protected String idTriFormate = "";
    protected String ligneAdresse = new String();
    protected String montant = "";
    protected String noConsulat = "";
    protected String noReference = "";

    protected BISession session = null;

    protected IntTiers tiers = null;

    /**
     * Commentaire relatif au constructeur AJPremierRappel.
     */
    public CAImprPremierRappelCSC() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.05.2002 15:05:13)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void beforePrint(BTransaction transaction) throws Exception {
        // Rechercher les données pour afficher sur l'écran
        // Rechercher Adresse

        try {
            setLigneAdresse(getTiers().getAdresseLienAsString(IntTiers.TYPE_LIEN_MANDATAIRE,
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, ICommonConstantes.CS_APPLICATION_COTISATION,
                    getDateSurDocument(), null));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        noConsulat = "Unknown";
        idTriFormate = "";
    }

    /**
     * Returns the adresseCourrier.
     * 
     * @return IntAdresseCourrier
     */
    @Override
    public IntAdresseCourrier getAdresseCourrier() {
        return adresseCourrier;
    }

    /**
     * Returns the dateRappel.
     * 
     * @return String
     */
    @Override
    public String getDateRappel() {
        return dateRappel;
    }

    /**
     * Returns the dateSurDocument.
     * 
     * @return String
     */
    @Override
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * @return
     */
    @Override
    public JasperPrint getDispositionsLegales(CAApplication application, String codeIsoLangue, String name)
            throws Exception {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2002 18:14:45)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    @Override
    public BIDocument getDocumentClass() throws Exception {
        return new CAProcessImprPremierRappelCSC();
    }

    @Override
    public JasperPrint getExtraitCompteDoc() {
        return extraitCompteDoc;
    }

    @Override
    public FWIScriptDocument getFWIDocumentClass() {
        try {
            return (FWIScriptDocument) getDocumentClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the idExterneRole.
     * 
     * @return String
     */
    @Override
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.impl.print.itext.list.ICADocumentRappelCSC#getIdTriFormate ()
     */
    @Override
    public String getIdTriFormate() {
        return idTriFormate;
    }

    /**
     * Returns the session.
     * 
     * @return BISession
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @return
     */
    @Override
    public String getLigneAdresse() {
        return ligneAdresse;
    }

    /**
     * Returns the montant.
     * 
     * @return String
     */
    @Override
    public String getMontant() {
        return montant;
    }

    /**
     * Returns the noConsulat.
     * 
     * @return String
     */
    @Override
    public String getNoConsulat() {
        return noConsulat;
    }

    /**
     * Returns the noReference.
     * 
     * @return String
     */
    public String getNoReference() {
        return noReference;
    }

    /**
     * Returns the tiers.
     * 
     * @return IntTiers
     */
    @Override
    public IntTiers getTiers() {
        return tiers;
    }

    /**
     * Sets the adresseCourrier.
     * 
     * @param adresseCourrier
     *            The adresseCourrier to set
     */
    @Override
    public void setAdresseCourrier(IntAdresseCourrier adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    /**
     * Sets the dateRappel.
     * 
     * @param dateRappel
     *            The dateRappel to set
     */
    @Override
    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    /**
     * Sets the dateSurDocument.
     * 
     * @param dateSurDocument
     *            The dateSurDocument to set
     */
    @Override
    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.impl.print.itext.list.ICADocumentRappelCSC#setExtraitCompteDoc
     * (net.sf.jasperreports.engine.JasperPrint)
     */
    @Override
    public void setExtraitCompteDoc(JasperPrint doc) {
        extraitCompteDoc = doc;
    }

    /**
     * Sets the idExterneRole.
     * 
     * @param idExterneRole
     *            The idExterneRole to set
     */
    @Override
    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.impl.print.itext.list.ICADocumentRappelCSC#setIdTriFormate (java.lang.String)
     */
    @Override
    public void setIdTriFormate(String idTri) {
        idTriFormate = idTri;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    @Override
    public void setISession(BISession session) {
        this.session = session;
    }

    /**
     * @param string
     */
    public void setLigneAdresse(String string) {
        ligneAdresse = string;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    @Override
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * Sets the noReference.
     * 
     * @param noReference
     *            The noReference to set
     */
    public void setNoReference(String noReference) {
        this.noReference = noReference;
    }

    /**
     * Sets the tiers.
     * 
     * @param tiers
     *            The tiers to set
     */
    @Override
    public void setTiers(IntTiers tiers) {
        this.tiers = tiers;
    }

}
