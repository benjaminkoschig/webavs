/**
 * 
 */
package globaz.perseus.process.traitements;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.perseus.process.PFAbstractJob;
import globaz.perseus.process.decision.PFImpressionDecisionTraitementMasseProcess;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFTraitementAnnuelProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMailAGLAU = null;
    private String adresseMailCCVD = null;
    private String texteDecision = null;

    /**
     * @return the adresseMailAGLAU
     */
    public String getAdresseMailAGLAU() {
        return adresseMailAGLAU;
    }

    /**
     * @return the adresseMailCCVD
     */
    public String getAdresseMailCCVD() {
        return adresseMailCCVD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Traitement annuel PC Famille";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return "Traitement annuel PC Famille";
        // return this.getClass().getName();
    }

    /**
     * @return the texteDecision
     */
    public String getTexteDecision() {
        return texteDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.perseus.process.PFAbstractJob#process()
     */
    @Override
    protected void process() throws Exception {
        try {
            // Exécuter le traitement annuel sur les demandes en cours
            List<Decision> listDecisions = PerseusServiceLocator.getTraitementAnnuelService()
                    .executerTraitementsAnnuels(getSession(), getLogSession(), getTexteDecision());
            // Impression des décisions
            if (!listDecisions.isEmpty()) {
                PFImpressionDecisionTraitementMasseProcess process = new PFImpressionDecisionTraitementMasseProcess();
                process.setSession(getSession());
                process.setListeDecision(listDecisions);
                process.setMailAddressCaissePrincipale(getAdresseMailCCVD());
                process.setMailAddressCaisseSecondaire(getAdresseMailAGLAU());
                BProcessLauncher.startJob(process);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_ERREUR")
                            + e.toString());
        }

        logError(getSession().getLabel("PROCESS_ERREUR_MESSAGE"));

        List<String> email = new ArrayList<String>();
        email.add(getAdresseMailCCVD());
        email.add(getAdresseMailAGLAU());
        this.sendCompletionMail(email);
    }

    /**
     * @param adresseMailAGLAU
     *            the adresseMailAGLAU to set
     */
    public void setAdresseMailAGLAU(String adresseMailAGLAU) {
        this.adresseMailAGLAU = adresseMailAGLAU;
    }

    /**
     * @param adresseMailCCVD
     *            the adresseMailCCVD to set
     */
    public void setAdresseMailCCVD(String adresseMailCCVD) {
        this.adresseMailCCVD = adresseMailCCVD;
    }

    /**
     * @param texteDecision
     *            the texteDecision to set
     */
    public void setTexteDecision(String texteDecision) {
        this.texteDecision = texteDecision;
    }

}
