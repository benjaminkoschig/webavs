package globaz.naos.process.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.db.taxeCo2.AFTaxeCo2Manager;
import globaz.naos.itext.taxeCo2.AFLettreTaxeCo2UnAffilie_doc;

public class AFImprimerLettreTaxeCo2Process extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "listeTaxeCo2.xml";
    public final static String NUMERO_INFOROM = "0229CAF";

    private String dateImpression = "";
    private String forAnnee = "";
    private String forIdTaxeCo2 = "";

    // Constructeur
    public AFImprimerLettreTaxeCo2Process() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception, Exception {

        AFTaxeCo2Manager manager = new AFTaxeCo2Manager();

        initManager(manager);

        if (manager.size() == 1) {
            return createDocument(manager);
        } else {
            setSendCompletionMail(false);
        }

        return false;

    }

    // Méthode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(AFTaxeCo2Manager manager) throws Exception, Exception {
        try {

            AFLettreTaxeCo2UnAffilie_doc document = new AFLettreTaxeCo2UnAffilie_doc();

            document.setSession(getSession());
            document.setParentWithCopy(this);
            document.setAnnee(getForAnnee());
            document.setDateImpression(getDateImpression());
            document.setTaxe((AFTaxeCo2) manager.getFirstEntity());

            document.executeProcess();

            // this.mergePDF(this.createDocumentInfo(), true, 0, false, null);

            String nomDoc = (getSession().getLabel("LETTRE_TAXE_CO2") + " " + getForAnnee());

            // Publication du document
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
            docInfo.setDocumentTitle(nomDoc);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(AFImprimerLettreTaxeCo2Process.NUMERO_INFOROM);

            return true;
        } catch (Exception e) {
            abort();
            e.printStackTrace();
            return false;
        }
    }

    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_LETTRE_TAXE_CO2_ERROR");
        } else {
            return (getSession().getLabel("EMAIL_LETTRE_TAXE_CO2"));
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdTaxeCo2() {
        return forIdTaxeCo2;
    }

    private void initManager(AFTaxeCo2Manager manager) throws Exception {
        manager.setSession(getSession());
        manager.setForTaxeCo2Id(getForIdTaxeCo2());
        manager.find(BManager.SIZE_NOLIMIT);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdTaxeCo2(String forIdTaxeCo2) {
        this.forIdTaxeCo2 = forIdTaxeCo2;
    }
}
