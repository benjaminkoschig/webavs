package globaz.aquila.process;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.access.poursuite.CODossierEtapeManager;
import globaz.aquila.print.list.COListDossiersEtape;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaNumericUtils;

/**
 * @author sel Créé le 19 sept. 06
 */
public class COProcessListDossierEtape extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String beforeNumAffilie = "";
    private Boolean blocageInactif = new Boolean(false);
    private String forIdCategorie = "";
    private String forIdEtapeForListDossierEtape = "";
    private String forIdSequence = "";
    private String forIdTypeSection = "";

    private String forSelectionRole = "";

    private String forSolde = "";
    private String forSoldeOperator = "";
    private String forTypeDate = "";
    private String forTypeExecution = "";
    private String fromDateDebut = "";
    private String fromNumAffilie = "";
    private String likeNumSection = "";
    private String toDateFin = "";

    public COProcessListDossierEtape() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {

            CODossierEtapeManager manager = new CODossierEtapeManager();
            manager.setSession(getSession());
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForIdCategorie(getForIdCategorie());
            manager.setLikeNumSection(getLikeNumSection());
            manager.setForIdTypeSection(getForIdTypeSection());
            manager.setFromNumAffilie(getFromNumAffilie());
            manager.setBeforeNumAffilie(getBeforeNumAffilie());
            if ("DateE".equalsIgnoreCase(getForTypeDate())) {
                manager.setFromDateExecution(getFromDateDebut());
                manager.setToDateExecution(getToDateFin());
            } else {
                manager.setFromDateDeclenchement(getFromDateDebut());
                manager.setToDateDeclenchement(getToDateFin());
            }
            manager.setForSoldeOperator(getForSoldeOperator());
            manager.setForSolde(getForSolde());

            manager.setForIdSequence(getForIdSequence());

            if ("EE".equals(getForTypeExecution())) {
                manager.setForIdEtapeForListDossierEtape(getForIdEtapeForListDossierEtape());
            } else {
                manager.setForIdEtapeSuivanteForListDossierEtape(getForIdEtapeForListDossierEtape());
            }

            manager.setForTriListeCA("2");
            manager.setForTriListeSection("1");

            manager.find(BManager.SIZE_NOLIMIT);

            String theCsSequence = "";
            if (!JadeStringUtil.isBlankOrZero(getForIdSequence())) {
                COSequence theSequence = new COSequence();
                theSequence.setSession(getSession());
                theSequence.setIdSequence(getForIdSequence());
                theSequence.retrieve();
                theCsSequence = theSequence.getLibSequence();
            }

            String theCsEtape = "";
            if (!JadeStringUtil.isBlankOrZero(getForIdEtapeForListDossierEtape())) {
                COEtape theEtape = new COEtape();
                theEtape.setSession(getSession());
                theEtape.setIdEtape(getForIdEtapeForListDossierEtape());
                theEtape.retrieve();
                theCsEtape = theEtape.getLibEtape();
            }

            // Création du document
            COListDossiersEtape excelDoc = new COListDossiersEtape(getSession());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.setCsSequence(theCsSequence);
            excelDoc.setCsEtape(theCsEtape);
            excelDoc.setRole(getForSelectionRole());
            excelDoc.setCategorie(getForIdCategorie());
            excelDoc.setDeNumAffilie(getFromNumAffilie());
            excelDoc.setaNumAffilie(getBeforeNumAffilie());
            excelDoc.setSymboleSolde(getForSoldeOperator());
            excelDoc.setSolde(getForSolde());
            excelDoc.setForTypeExecution(getForTypeExecution());
            if ("EP".equals(getForTypeExecution())) {
                excelDoc.setBlocageInactif(getBlocageInactif());
            } else {
                excelDoc.setBlocageInactif(Boolean.TRUE);
            }
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            // registerAttachedDocument(excelDoc.getOutputFile());

            return true;

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            e.getMessage();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (!JadeStringUtil.isBlankOrZero(getForSolde())
                && !AriesAurigaNumericUtils.isNumericDecimal(getForSolde(), 13, 2)) {
            getSession().addError(getSession().getLabel("DOSSIER_ETAPE_SOLDE_MANDATORY"));
        }

        if (!JadeStringUtil.isEmpty(getFromDateDebut()) && !JadeDateUtil.isGlobazDate(getFromDateDebut())) {
            getSession().addError(getSession().getLabel("DOSSIER_ETAPE_DATE_DEBUT_MANDATORY"));
        }

        if (!JadeStringUtil.isEmpty(getToDateFin()) && !JadeDateUtil.isGlobazDate(getToDateFin())) {
            getSession().addError(getSession().getLabel("DOSSIER_ETAPE_DATE_FIN_MANDATORY"));
        }

        if ("EP".equalsIgnoreCase(getForTypeExecution()) && JadeStringUtil.isEmpty(getForIdEtapeForListDossierEtape())) {
            getSession()
                    .addError(getSession().getLabel("DOSSIER_ETAPE_ERREUR_PROCHAINE_ETAPE_SANS_ETAPE_SELECTIONNEE"));
        }

    }

    public String getBeforeNumAffilie() {
        return beforeNumAffilie;
    }

    public Boolean getBlocageInactif() {
        return blocageInactif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LISTE_DOSSIERS_ETAPE_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("LISTE_DOSSIERS_ETAPE_SUJETMAIL_OK");
        }
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForIdEtapeForListDossierEtape() {
        return forIdEtapeForListDossierEtape;
    }

    public String getForIdSequence() {
        return forIdSequence;
    }

    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getForSolde() {
        return forSolde;
    }

    public String getForSoldeOperator() {
        return forSoldeOperator;
    }

    public String getForTypeDate() {
        return forTypeDate;
    }

    public String getForTypeExecution() {
        return forTypeExecution;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getLikeNumSection() {
        return likeNumSection;
    }

    public String getToDateFin() {
        return toDateFin;
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

    public void setBeforeNumAffilie(String beforeNumAffilie) {
        this.beforeNumAffilie = beforeNumAffilie;
    }

    public void setBlocageInactif(Boolean blocageInactif) {
        this.blocageInactif = blocageInactif;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForIdEtapeForListDossierEtape(String forIdEtapeForListDossierEtape) {
        this.forIdEtapeForListDossierEtape = forIdEtapeForListDossierEtape;
    }

    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
    }

    public void setForIdTypeSection(String newForIdTypeSection) {
        forIdTypeSection = newForIdTypeSection;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    public void setForSolde(String forSolde) {
        this.forSolde = forSolde;
    }

    public void setForSoldeOperator(String forSoldeOperator) {
        this.forSoldeOperator = forSoldeOperator;
    }

    public void setForTypeDate(String newForTypeDate) {
        forTypeDate = newForTypeDate;
    }

    public void setForTypeExecution(String forTypeExecution) {
        this.forTypeExecution = forTypeExecution;
    }

    public void setFromDateDebut(String newFromDateDebut) {
        fromDateDebut = newFromDateDebut;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setLikeNumSection(String newLikeNumSection) {
        likeNumSection = newLikeNumSection;
    }

    public void setToDateFin(String newToDateFin) {
        toDateFin = newToDateFin;
    }

}
