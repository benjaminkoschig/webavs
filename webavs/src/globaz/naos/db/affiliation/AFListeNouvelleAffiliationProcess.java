package globaz.naos.db.affiliation;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.io.File;
import java.io.FileWriter;

/**
 * @author MMO
 * @since 15 mars 2011
 */

public class AFListeNouvelleAffiliationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constantes
     */
    public final static String NUMERO_INFOROM = "0260CAF";

    private String critereSelection = "";
    /**
     * Attributs
     */
    private String fromDateCreation = "";
    private String toDateCreation = "";

    public AFListeNouvelleAffiliationProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        AFAffiliationManager mgrAffiliation = new AFAffiliationManager();
        mgrAffiliation.setSession(getSession());
        // Inforom 543
        if (getCritereSelection().equalsIgnoreCase(CodeSystem.CRITERE_SELECTION_DATE_ENREGISTREMENT)) {
            mgrAffiliation.setFromDateCreation(getFromDateCreation());
            mgrAffiliation.setToDateCreation(getToDateCreation());
        } else {
            mgrAffiliation.setFromDebutAffiliation(getFromDateCreation());
            mgrAffiliation.setForDateDebutAffLowerOrEqualTo(getToDateCreation());
        }
        mgrAffiliation.find(BManager.SIZE_NOLIMIT);

        if (mgrAffiliation.size() <= 0) {
            getMemoryLog().logMessage(getSession().getLabel("MAIL_LISTE_NOUVELLE_AFFILIATION_NO_DATA"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        createDocument(mgrAffiliation);

        return !(isAborted() || isOnError() || getSession().hasErrors());
    }

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
            getSession().addError(getSession().getLabel("ADRESSE_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private void createDocument(AFAffiliationManager mgrAffiliation) throws Exception {

        File outputFile = null;

        FileWriter outputFileWriter = null;

        try {
            outputFile = File.createTempFile("listeNouvelleAffiliation", ".csv");
            outputFile.deleteOnExit();
            outputFileWriter = new FileWriter(outputFile);

            setProgressScaleValue(mgrAffiliation.size());

            TITiersViewBean tempTiersForAdresse = new TITiersViewBean();
            tempTiersForAdresse.setSession(getSession());

            outputFileWriter.write(giveHeader());

            setProgressScaleValue(mgrAffiliation.size());

            for (int i = 0; i < mgrAffiliation.size(); i++) {

                AFAffiliation entityAffiliation = (AFAffiliation) mgrAffiliation.getEntity(i);

                tempTiersForAdresse.setIdTiers(entityAffiliation.getIdTiers());
                tempTiersForAdresse.retrieve();

                TIAdresseDataSource adresseDataSource = tempTiersForAdresse.getAdresseAsDataSource(
                        IConstantes.CS_AVOIR_ADRESSE_COURRIER, ICommonConstantes.CS_APPLICATION_COTISATION,
                        entityAffiliation.getAffilieNumero(), JACalendar.todayJJsMMsAAAA(), true, null);
                if (adresseDataSource == null) {
                    adresseDataSource = tempTiersForAdresse.getAdresseAsDataSource(
                            IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                            entityAffiliation.getAffilieNumero(), JACalendar.todayJJsMMsAAAA(), true, null);

                }
                outputFileWriter.write(giveLineContent(entityAffiliation, tempTiersForAdresse, adresseDataSource));

                incProgressCounter();
            }

            // Publication du document
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(AFListeNouvelleAffiliationProcess.NUMERO_INFOROM);
            this.registerAttachedDocument(docInfo, outputFile.getPath());

        } finally {
            if (outputFileWriter != null) {
                outputFileWriter.close();
            }
        }
    }

    public String getCritereSelection() {
        return critereSelection;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("MAIL_OBJECT_LISTE_NOUVELLE_AFFILIATION_ERROR");
        } else {
            return getSession().getLabel("MAIL_OBJECT_LISTE_NOUVELLE_AFFILIATION_SUCCES");
        }
    }

    /**
     * getter
     */
    public String getFromDateCreation() {
        return fromDateCreation;
    }

    public String getToDateCreation() {
        return toDateCreation;
    }

    private String giveHeader() {

        StringBuffer header = new StringBuffer();

        header.append(getSession().getLabel("DOCUMENT_COL_NUM_AFF"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_NSS"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_NOM"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_PRENOM"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_RUE"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_CODE_POSTAL"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_LIEU"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_GENRE_AFFILIATION"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_DATE_AFFILIATION"));
        header.append(";");
        header.append(getSession().getLabel("DOCUMENT_COL_PROVISOIRE"));
        header.append("\n");

        return header.toString();
    }

    private String giveLineContent(AFAffiliation theAffiliation, TITiersViewBean theTiers,
            TIAdresseDataSource theAdresseDataSource) {

        String theGenreAffiliation = "";
        try {
            theGenreAffiliation = CodeSystem.getLibelleIso(getSession(), theAffiliation.getTypeAffiliation(),
                    getSession().getIdLangueISO());
        } catch (Exception e) {
            theGenreAffiliation = "";
        }

        StringBuffer lineContent = new StringBuffer();

        lineContent.append(theAffiliation.getAffilieNumero());
        lineContent.append(";");
        lineContent.append(theTiers.getNumAvsActuel());
        lineContent.append(";");
        lineContent.append(theTiers.getDesignation1());
        lineContent.append(";");
        lineContent.append(theTiers.getDesignation2());
        lineContent.append(";");
        if (theAdresseDataSource == null) {
            lineContent.append(" " + " " + " ");
            lineContent.append(";");
            lineContent.append(" ");
            lineContent.append(";");
            lineContent.append(" ");

        } else {
            lineContent.append(theAdresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                    + theAdresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO));
            lineContent.append(";");
            lineContent.append(theAdresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
            lineContent.append(";");
            lineContent.append(theAdresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
        }
        lineContent.append(";");
        lineContent.append(theGenreAffiliation);
        lineContent.append(";");
        lineContent.append(theAffiliation.getDateDebut());
        lineContent.append(";");
        lineContent.append(theAffiliation.isTraitement().booleanValue() ? "x" : "");
        lineContent.append("\n");

        return lineContent.toString();

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setCritereSelection(String critereSelection) {
        this.critereSelection = critereSelection;
    }

    /**
     * setter
     */

    public void setFromDateCreation(String newFromDateCreation) {
        fromDateCreation = newFromDateCreation;
    }

    public void setToDateCreation(String newToDateCreation) {
        toDateCreation = newToDateCreation;
    }

}
