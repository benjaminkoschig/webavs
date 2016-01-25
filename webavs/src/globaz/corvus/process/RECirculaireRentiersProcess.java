package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiersManager;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.excel.RECirculaireRentiers;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;

/**
 * 
 * @author HPE
 * 
 */
public class RECirculaireRentiersProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public RECirculaireRentiersProcess() {
        super();
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            // Création du manager
            REPrestAccJointInfoComptaJointTiersManager manager = new REPrestAccJointInfoComptaJointTiersManager();
            manager.setSession(getSession());
            manager.setForEnCoursAtMois(getMoisAnnee());
            manager.setForDateDebutBefore(getMoisAnnee());
            manager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + "," + IREPrestationAccordee.CS_ETAT_PARTIEL);
            manager.setOrderBy(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NOM + ","
                    + REPrestAccJointInfoComptaJointTiers.FIELDNAME_PRENOM);

            // Création du manager pour la recherche des bénéficiaires PC
            StringBuffer forCodePrestationIn = new StringBuffer();
            forCodePrestationIn.append("'" + PRCodePrestationPC._110.getCodePrestationAsString() + "'");
            forCodePrestationIn.append(",");
            forCodePrestationIn.append("'" + PRCodePrestationPC._113.getCodePrestationAsString() + "'");
            forCodePrestationIn.append(",");
            forCodePrestationIn.append("'" + PRCodePrestationPC._150.getCodePrestationAsString() + "'");

            StringBuffer forEtatPrestationIn = new StringBuffer();
            forEtatPrestationIn.append(EtatPrestationAccordee.DIMINUE.getCodeSysteme());
            forEtatPrestationIn.append(",");
            forEtatPrestationIn.append(EtatPrestationAccordee.PARTIEL.getCodeSysteme());
            forEtatPrestationIn.append(",");
            forEtatPrestationIn.append(EtatPrestationAccordee.VALIDE.getCodeSysteme());

            REPrestationAccordeeManager prestationAccordeeManager = new REPrestationAccordeeManager();
            prestationAccordeeManager.setSession(getSession());
            prestationAccordeeManager.setForCodesPrestationsIn(forCodePrestationIn.toString());
            prestationAccordeeManager.setForCsEtatIn(forEtatPrestationIn.toString());
            prestationAccordeeManager.setForEnCoursAtMois(moisAnnee);

            // Création du document
            RECirculaireRentiers excelDoc = new RECirculaireRentiers(getSession());
            excelDoc.populateSheetListe(manager, prestationAccordeeManager, getTransaction());

            // Création des infos de publication
            JadePublishDocumentInfo documentInfo = JadePublishDocumentInfoProvider.newInstance(this);
            documentInfo.setOwnerEmail(getEMailAddress());
            documentInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            documentInfo.setDocumentTitle(getSession().getLabel("PROCESS_CIRC_MAIL"));
            documentInfo.setDocumentSubject(getSession().getLabel("PROCESS_CIRC_MAIL"));
            documentInfo.setArchiveDocument(false);

            // attachement du fichier de sortie au mail
            this.registerAttachedDocument(documentInfo, excelDoc.getOutputFile());

            return true;

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            e.getMessage();
            return false;
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_CIRC_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
