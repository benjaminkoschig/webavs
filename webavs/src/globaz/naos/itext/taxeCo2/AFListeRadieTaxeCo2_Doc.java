/*
 * Créé le 1 mars 07
 */
package globaz.naos.itext.taxeCo2;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class AFListeRadieTaxeCo2_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String NUM_REF_INFOROM_LISTE_TAXECO2_RADIE = "0229CAF";

    private java.lang.String anneeMasse = new String();
    private java.lang.String anneeRedistri = new String();

    private Iterator itTaxeCo2;

    private List listAnnee = new ArrayList();

    private AFTaxeCo2 taxeCo2;

    public final String TEMPLATE_FILENAME = new String("NAOS_LISTE_RADIE_TAXECO2");

    public AFListeRadieTaxeCo2_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:28:36)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public AFListeRadieTaxeCo2_Doc(BProcess parent) throws FWIException {
        this(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "listRadieTaxeCo2");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public AFListeRadieTaxeCo2_Doc(BProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Liste des radiés (Taxe CO2)");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:28:36)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public AFListeRadieTaxeCo2_Doc(BSession session) throws FWIException {
        this(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "listRadieTaxeCo2");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public AFListeRadieTaxeCo2_Doc(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Liste des radiés (Taxe CO2)");
    }

    protected void _headerText() {

        super.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                getTemplateProperty(
                        getDocumentInfo(),
                        ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                + JadeStringUtil.toUpperCase(getSession().getIdLangueISO())));
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("LISTRADIETAXECO2"));

        super.setParametres(AFiTextParameterList.LABEL_ANNEE_MASSE, getSession().getLabel("ANNEE_MASSE"));
        super.setParametres(AFiTextParameterList.PARAM_ANNEE_MASSE, getAnneeMasse());
        super.setParametres(AFiTextParameterList.LABEL_ANNEE_REDISTRI, getSession().getLabel("ANNEE_REDISTRI"));
        super.setParametres(AFiTextParameterList.PARAM_ANNEE_REDISTRI, getAnneeRedistri());

        super.setParametres(AFiTextParameterList.LABEL_DATE_IMPRESSION, getSession().getLabel("DATE_IMPRESSION"));
        super.setParametres(AFiTextParameterList.PARAM_DATE_IMPRESSION, JACalendar.todayJJsMMsAAAA());

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:31:37)
     */
    protected void _summaryText() {
        super.setParametres(AFiTextParameterList.LABEL_TOTAUX, getSession().getLabel("TOTAUX"));
        super.setParametres(FWIImportParametre.PARAM_REFERENCE,
                getSession().getUserId() + " (" + JACalendar.todayJJsMMsAAAA() + ")");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:33:40)
     */
    protected void _tableHeader() {
        super.setColumnHeader(1, getSession().getLabel("AFFILIE"));
        super.setColumnHeader(2, getSession().getLabel("ANNEE_MASSE_RETOUR"));
        super.setColumnHeader(3, getSession().getLabel("ANNEE_REDISTRI_RETOUR"));
        super.setColumnHeader(4, getSession().getLabel("MASSE"));
        super.setColumnHeader(5, getSession().getLabel("MOTIFFIN"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    public void beforeBuildReport() throws FWIException {

    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() throws FWIException {

        itTaxeCo2 = listAnnee.iterator();
        // nom du template
        super.setTemplateFile(TEMPLATE_FILENAME);
        itTaxeCo2 = listAnnee.iterator();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:53:46)
     * 
     * @param id
     *            java.lang.String
     */
    public void bindData(String id) throws java.lang.Exception {
        super._executeProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(AFListeRadieTaxeCo2_Doc.NUM_REF_INFOROM_LISTE_TAXECO2_RADIE);
        AFListeRadieTaxeCo2_DS manager = null;
        List source = new ArrayList();
        // Sous contrôle d'exceptions
        try {
            manager = new AFListeRadieTaxeCo2_DS();
            manager.setSession(getSession());

            // S'il n'y a pas d'erreur lors du rapatriement des données, passer
            // aux traitements
            if (!isAborted()) {

                // Where clause
                manager.setForAnneeMasse(getAnneeMasse());
                manager.setHasMotifFin(new Boolean(true));
                int compt = 0;
                while (manager.next()) {
                    compt++;
                    setProgressDescription(compt + "/" + manager.size() + "<br>");
                    if (isAborted()) {
                        setProgressDescription("Traitement interrompu<br> sur la ligne : " + compt + "/"
                                + manager.size() + "<br>");
                        if ((getParent() != null) && getParent().isAborted()) {
                            getParent()
                                    .setProcessDescription(
                                            "Traitement interrompu<br> sur la ligne : " + compt + "/" + manager.size()
                                                    + "<br>");
                        }
                        break;
                    } else {
                        source.add(manager.getFieldValues());
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            throw new FWIException(e);
        }

        _headerText();
        _summaryText();
        _tableHeader();

        if (!source.isEmpty()) {
            super.setDataSource(source);
        }
    }

    public java.lang.String getAnneeMasse() {
        return anneeMasse;
    }

    public java.lang.String getAnneeRedistri() {
        return anneeRedistri;
    }

    public AFTaxeCo2 getTaxeCo2() {
        return taxeCo2;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (itTaxeCo2.hasNext()) {
            anneeMasse = (String) itTaxeCo2.next();
            return true;
        }
        return false;
    }

    public void setAnneeMasse(java.lang.String annee) {
        if (!listAnnee.contains(annee)) {
            listAnnee.add(annee);
        }
        anneeMasse = annee;
    }

    public void setAnneeRedistri(java.lang.String anneeRedistri) {
        this.anneeRedistri = anneeRedistri;
    }

    public void setTaxeCo2(AFTaxeCo2 taxeCo2) {
        this.taxeCo2 = taxeCo2;
    }

}
