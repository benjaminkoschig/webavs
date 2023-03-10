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
public class PFTraitementAdaptationProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMailAGLAU = null;
    private String adresseMailCCVD = null;
    private String mois = null;
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
        return "Traitement ponctuel PC Famille";
    }

    /**
     * @return the mois
     */
    public String getMois() {
        return mois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return "Traitement ponctuel PC Famille";
        // return this.getClass().getName();
    }

    /**
     * @return the texte
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
            // Ex?cuter le traitement annuel sur les demandes en cours
            List<Decision> listDecisions = PerseusServiceLocator.getTraitementAdaptationService().executerTraitements(
                    getSession(), getLogSession(), getMois(), getTexteDecision());

            // Impression des d?cisions
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
     * @param mois
     *            the mois to set
     */
    public void setMois(String mois) {
        this.mois = mois;
    }

    /**
     * @param texte
     *            the texte to set
     */
    public void setTexteDecision(String texte) {
        texteDecision = texte;
    }

}
