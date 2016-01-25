/**
 * 
 */
package globaz.perseus.process.traitements;

import globaz.globall.db.BProcessLauncher;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.perseus.process.PFAbstractJob;
import globaz.perseus.process.decision.PFDecisionProcess;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFTraitementPonctuelProcess extends PFAbstractJob {

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
            // Exécuter le traitement annuel sur les demandes en cours
            List<Decision> listDecisions = PerseusServiceLocator.getTraitementPonctuelService().executerTraitements(
                    getSession(), getLogSession(), getMois(), getTexteDecision());
            // Impression des décisions
            for (Decision decision : listDecisions) {
                PFDecisionProcess process = new PFDecisionProcess();
                process.setSession(getSession());
                process.setDecisionId(decision.getId());
                /**
                 * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et
                 * doit donc être renommée différement (mailAd) pour fonctionner correctement.
                 */
                if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                        decision.getDemande().getSimpleDemande().getCsCaisse())) {
                    process.seteMailAddress(getAdresseMailAGLAU());
                    process.setMailAd(getAdresseMailAGLAU());
                } else {
                    process.seteMailAddress(getAdresseMailCCVD());
                    process.setMailAd(getAdresseMailCCVD());
                }
                process.setDateDocument(decision.getSimpleDecision().getDateDocument());

                BProcessLauncher.startJob(process);
            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Erreur technique grave : " + e.toString() + " : " + e.getMessage());
            e.printStackTrace();
        }
        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeThread.logError(this.getClass().getName(), "Il y'a des erreurs : ");
        }
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }
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
