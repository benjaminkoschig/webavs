/*
 * Créé le 1 mars 07
 */
package globaz.draco.print.list;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASectionManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class DSListeDSRentre_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String NUM_REF_INFOROM_LISTE_SALAIRES_25J = "0252CDS";

    private AFAffiliation affilie;

    private String dateReference = new String();

    private DSDeclarationViewBean declaration;
    private Iterator itDSRentre;
    private List listSalaire = new ArrayList();
    public final String TEMPLATE_FILENAME = new String("DRACO_LISTE_SALAIRES_25J");

    private int totalLigne = 0;

    public DSListeDSRentre_Doc() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:28:36)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public DSListeDSRentre_Doc(BProcess parent) throws FWIException {
        this(parent, DSApplication.DEFAULT_APPLICATION_ROOT, "listDSRentre");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSListeDSRentre_Doc(BProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle(getSession().getLabel("DRACO_LISTE_25_JOURS"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:28:36)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public DSListeDSRentre_Doc(BSession session) throws FWIException {
        this(session, DSApplication.DEFAULT_APPLICATION_ROOT, "listDSRentre");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSListeDSRentre_Doc(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle(getSession().getLabel("DRACO_LISTE_25_JOURS"));
    }

    protected void _headerText() {
        getDocumentInfo().setDocumentTypeNumber(NUM_REF_INFOROM_LISTE_SALAIRES_25J);
        super.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                getTemplateProperty(
                        getDocumentInfo(),
                        ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                + JadeStringUtil.toUpperCase(getSession().getIdLangueISO())));
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("DRACO_LISTE_25_JOURS"));

        super.setParametres(DSiTextParameterList.LABEL_DATE_REFERENCE,
                getSession().getLabel("DRACO_LISTE_DATE_REFERENCE"));
        super.setParametres(DSiTextParameterList.PARAM_DATE_REFERENCE, getDateReference());

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:31:37)
     */
    protected void _summaryText() {
        super.setParametres(DSiTextParameterList.LABEL_TOTAUX, getSession().getLabel("TOTAUX"));
        super.setParametres(DSiTextParameterList.PARAM_TOTAL_LIGNE, new Integer(totalLigne));
        super.setParametres(FWIImportParametre.PARAM_REFERENCE,
                getSession().getUserId() + " (" + JACalendar.todayJJsMMsAAAA() + ")");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:33:40)
     */
    protected void _tableHeader() {
        super.setColumnHeader(1, getSession().getLabel("DRACO_LISTE_NO_AFFILIE"));
        super.setColumnHeader(2, getSession().getLabel("DRACO_LISTE_RAISON_SOCIALE"));
        super.setColumnHeader(3, getSession().getLabel("DRACO_LISTE_DATE_RECEPTION"));
        super.setColumnHeader(4, getSession().getLabel("DRACO_LISTE_STATUT_DS"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if (getEMailAddress() == null || getEMailAddress().equals("")) {
            _addError("Le champ email doit être renseigné.");
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() throws FWIException {

    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() throws FWIException {

        itDSRentre = listSalaire.iterator();
        // nom du template
        super.setTemplateFile(TEMPLATE_FILENAME);
        itDSRentre = listSalaire.iterator();

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
        DSListeDSRentre_DS manager = null;
        List source = new ArrayList();
        // Sous contrôle d'exceptions
        try {
            JACalendarGregorian newDate = new JACalendarGregorian();
            String dateReference25 = "";
            dateReference25 = newDate.addDays(getDateReference(), -25);
            manager = new DSListeDSRentre_DS();
            manager.setSession(getSession());

            // S'il n'y a pas d'erreur lors du rapatriement des données, passer
            // aux traitements
            if (!isAborted()) {

                // Where clause
                manager.setForEtatNotLike(DSDeclarationViewBean.CS_COMPTABILISE);
                manager.setForDateRetourEffLowerOrEquals(dateReference25);
                int compt = 0;
                while (manager.next()) {
                    compt++;
                    setProgressDescription(compt + "/" + manager.size() + "<br>");
                    if (isAborted()) {
                        setProgressDescription("Traitement interrompu<br> sur la ligne : " + compt + "/"
                                + manager.size() + "<br>");
                        if (getParent() != null && getParent().isAborted()) {
                            getParent()
                                    .setProcessDescription(
                                            "Traitement interrompu<br> sur la ligne : " + compt + "/" + manager.size()
                                                    + "<br>");
                        }
                        break;
                    } else {
                        affilie = manager.getAffilie();
                        if (affilie == null || affilie.isNew()) {
                            continue;
                        }
                        declaration = manager.getEntity();
                        if (DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(declaration.getTypeDeclaration())
                                || DSDeclarationViewBean.CS_PRINCIPALE.equals(declaration.getTypeDeclaration())) {
                            CACompteAnnexe cAnnexe = new CACompteAnnexe();
                            cAnnexe.setSession(getSession());
                            cAnnexe.setIdExterneRole(affilie.getAffilieNumero());
                            cAnnexe.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                                    getSession().getApplication()));
                            cAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                            cAnnexe.retrieve();
                            if (cAnnexe != null) {
                                List categorieSection = new ArrayList();
                                categorieSection.add(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE);
                                categorieSection.add(DSDeclarationViewBean.CS_PRINCIPALE);

                                CASectionManager section = new CASectionManager();
                                section.setSession(getSession());
                                section.setForIdCompteAnnexe(cAnnexe.getIdCompteAnnexe());
                                section.setForCategorieSectionIn(categorieSection);
                                section.setLikeIdExterne(declaration.getAnnee());
                                section.find();
                                if (section.size() > 0) {
                                    continue;
                                }
                            }
                        }
                        totalLigne++;
                        source.add(manager.getFieldValues(affilie));
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
            throw new FWIException(e);
        }

        _headerText();
        _summaryText();
        _tableHeader();

        if (!source.isEmpty()) {
            super.setDataSource(source);
        }
    }

    public java.lang.String getDateReference() {
        return dateReference;
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
        if (itDSRentre.hasNext()) {
            dateReference = (String) itDSRentre.next();
            return true;
        }
        return false;
    }

    public void setDateReference(java.lang.String dateReference) {
        if (!listSalaire.contains(dateReference)) {
            listSalaire.add(dateReference);
        }
        this.dateReference = dateReference;
    }

}
