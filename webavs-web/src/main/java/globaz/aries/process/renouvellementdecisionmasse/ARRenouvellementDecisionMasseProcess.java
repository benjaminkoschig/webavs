package globaz.aries.process.renouvellementdecisionmasse;

import globaz.common.process.traitementmasse.CommonAbstractJadeJob;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.common.business.models.ResultatTraitementMasseCsvJournal;

public class ARRenouvellementDecisionMasseProcess extends CommonAbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail = null;
    private String annee = null;
    private String numeroAffilieDebut = null;
    private String numeroAffilieFin = null;
    private String numeroPassageFacturation = null;

    public ARRenouvellementDecisionMasseProcess() {
        super();
    }

    @Override
    public String getAdresseEmail() {
        return adresseEmail;
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    public String getDescription() {
        return "Renouvellement en masse des décisions CGAS";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public String getNumeroAffilieDebut() {
        return numeroAffilieDebut;
    }

    public String getNumeroAffilieFin() {
        return numeroAffilieFin;
    }

    public String getNumeroPassageFacturation() {
        return numeroPassageFacturation;
    }

    @Override
    protected String process() throws Exception {
        try {

            ResultatTraitementMasseCsvJournal journalResultatTraitement = AriesServiceLocator.getDecisionCGASService()
                    .renouvelerDecisionCGASMasse(new JadeProgressBarModel(1), false, annee, numeroAffilieDebut,
                            numeroAffilieFin, numeroPassageFacturation);

            if (journalResultatTraitement.hasErreursTechniques()) {
                JadeThread.logWarn(this.getClass().getName(),
                        "aries.renouvellement.decision.masse.avertissement.cas.en.erreur.technique");
            }

            String fileName = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addFilenameSuffixUID("journalRenouvellementDecisionCGASMasse.csv");

            JadeFsFacade.writeFile(journalResultatTraitement.getLignesInByteFormat(), fileName);

            return fileName;

        } catch (Exception e) {
            handleException(e, "aries.renouvellement.decision.masse.erreur.technique");
        }

        return null;

    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setNumeroAffilieDebut(String numeroAffilieDebut) {
        this.numeroAffilieDebut = numeroAffilieDebut;
    }

    public void setNumeroAffilieFin(String numeroAffilieFin) {
        this.numeroAffilieFin = numeroAffilieFin;
    }

    public void setNumeroPassageFacturation(String numeroPassageFacturation) {
        this.numeroPassageFacturation = numeroPassageFacturation;
    }

}
