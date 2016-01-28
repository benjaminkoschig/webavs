/**
 * 
 */
package globaz.pavo.print.itext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.constantes.IConstantes;

/**
 * Inforom284
 * 
 * @author sel
 * 
 *         <pre>
 * -- Types de documents 
 * insert into SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) 
 * values (331000, 'CIGRPTDCT', 1,2,0,0, 'Extrait_lettre_accompagnement', 2,2,2,2,2, 2, 10300030, 0, '20120605000000Globaz'); 
 * insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values (331000, 'D', '1', '[de]Extrait lettre accompagnement', '20120605000000Globaz'); 
 * insert into SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values (331000, 'F', '1', 'Extrait lettre accompagnement', '20120605000000Globaz'); 
 * 
 * -- CTTYPDOC : Liens vers l'interface de Babel 
 * INSERT INTO SCHEMA.CTTYPDOC (CAITYD,CATDOM,CATTYP,PSPY,CANBIN) VALUES (330001, 329000, 331000, '20120605000000Globaz', 331000000000);
 * </pre>
 */
public class CIExtraitLettreAccompagnement extends CIDocumentManager {

    private static final long serialVersionUID = 810394846963022232L;
    public final static String CI_DOMAINE = "329000";
    public final static String CI_TYPE_LETTRE_ACCOMPAGNEMENT = "331000";

    public static final String NUMERO_REFERENCE_INFOROM = "0294CCI";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "PAVO_LETTRE_ACCOMPAGNEMENT";

    /**
     * @param session
     * @param idLangue
     * @return
     * @throws Exception
     */
    public static String defaultTitrePolitesse(BSession session, String idLangue) throws Exception {
        StringBuffer titre = new StringBuffer();

        titre.append(CodeSystem.getLibelleIso(session, IConstantes.CS_TIERS_TITRE_MADAME, idLangue));
        titre.append(", ");
        titre.append(CodeSystem.getLibelleIso(session, IConstantes.CS_TIERS_TITRE_MONSIEUR, idLangue));

        return titre.toString();
    }

    private String adresseDest = null;

    private Boolean hasNext = true;
    private String NSS = new String();
    private String titrePolitesse = null;

    public CIExtraitLettreAccompagnement() {
        super();
    }

    public CIExtraitLettreAccompagnement(BProcess parent, String fileName) throws FWIException {
        super(parent, fileName);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CIExtraitLettreAccompagnement(BSession parent, String fileName) throws FWIException {
        super(parent, fileName);
    }

    private void _filldocInfo() {
        getDocumentInfo().setTemplateName(CIExtraitLettreAccompagnement.TEMPLATE_NAME);
        getDocumentInfo().setDocumentTypeNumber(CIExtraitLettreAccompagnement.NUMERO_REFERENCE_INFOROM);
        getDocumentInfo().setPublishDocument(true);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, getNSS());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                NSUtil.unFormatAVS(getNSS()));

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        // nothing
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // On va initialiser les documents
        // Définit le type de document à utiliser.
        setTypeDocument(CIExtraitLettreAccompagnement.CI_TYPE_LETTRE_ACCOMPAGNEMENT);
    }

    @Override
    public void createDataSource() throws Exception {
        setSendCompletionMail(false);
        setDocumentTitle(getSession().getLabel("LETTRE_ACCOMPAGNEMENT_TITLE"));
        setTemplateFile(CIExtraitLettreAccompagnement.TEMPLATE_NAME);

        loadCatalogue();

        handleHeaders(getAdresseDest());

        StringBuffer concerneBuffer = new StringBuffer();
        dumpNiveau(1, concerneBuffer, "");
        this.setParametres("P_CONCERNE", concerneBuffer.toString());

        StringBuffer corpsBuffer = new StringBuffer();
        dumpNiveau(2, corpsBuffer, "\n\n");
        dumpNiveau(3, corpsBuffer, "\n\n");
        corpsBuffer.append("\n\n"); // Hack pour afficher tous les caractères avec iReport et co.

        String titre = "";
        if (JadeStringUtil.isBlank(getTitrePolitesse())) {
            titre = CIExtraitLettreAccompagnement.defaultTitrePolitesse(getSession(), _getLangue());
        } else {
            titre = CodeSystem.getLibelleIso(getSession(), getTitrePolitesse(), _getLangue());
        }

        String corps = CIDocumentManager.formatMessage(corpsBuffer.toString(), new Object[] { titre });

        // this.setParametres("P_CONCERNE", "Test Concerne");
        this.setParametres("P_CORPS", corps);
        this.setParametres("P_ANNEXE", getTexte(9, 1).toString());
    }

    /**
     * @return the adresseDest
     */
    public String getAdresseDest() {
        return adresseDest;
    }

    public String getNSS() {
        return NSS;

    }

    /**
     * @return the titre
     */
    public String getTitrePolitesse() {
        return titrePolitesse;
    }

    /**
     * Gestion de l'en-tête/pied de page/signature
     * 
     * @param adresseDestination
     *            L'adresse du destinataire du document
     * @param annonce
     *            L'annonce de l'assuré concernée par ce document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-tête
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @throws Exception
     *             En cas de problème
     */
    protected void handleHeaders(String adresseDestination) throws Exception {
        _filldocInfo();

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), _getLangue());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        // Adresse du destinataire
        if (adresseDestination != null) {
            headerBean.setAdresse(adresseDestination);
        }

        // Date
        headerBean.setDate(JACalendar.format(JACalendar.today().toString(), _getLangue()));
        headerBean.setUser(getSession().getUserInfo());
        headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        if (!JadeStringUtil.isBlank(getSession().getUserFullName())) {
            headerBean.setNomCollaborateur(getSession().getUserFullName());
        } else {
            headerBean.setNomCollaborateur(getSession().getUserInfo().getFirstname() + " "
                    + getSession().getUserInfo().getLastname());
        }

        headerBean.setEmailCollaborateur(getSession().getUserInfo().getEmail());

        // Paramètres relatifs à l'annonce
        headerBean.setNoAffilie("");

        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addFooterParameters(this);
        caisseReportHelper.addSignatureParameters(this);
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

        return hasNext;
    }

    /**
     * @param adresseDest
     *            the adresseDest to set
     */
    public void setAdresseDest(String adresseDest) {
        this.adresseDest = adresseDest;
    }

    public void setNSS(String nss) {
        NSS = nss;

    }

    /**
     * @param titre
     *            the titre to set
     */
    public void setTitrePolitesse(String titre) {
        titrePolitesse = titre;
    }

}
