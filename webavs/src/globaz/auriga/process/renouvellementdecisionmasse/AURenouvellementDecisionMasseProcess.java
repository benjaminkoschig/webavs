package globaz.auriga.process.renouvellementdecisionmasse;

import globaz.common.process.traitementmasse.CommonAbstractJadeJob;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.common.business.models.ResultatTraitementMasseCsvJournal;

public class AURenouvellementDecisionMasseProcess extends CommonAbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail = null;
    private String annee = null;
    private String numeroAffilieDebut = null;
    private String numeroAffilieFin = null;
    private String numeroPassageFacturation = null;

    public AURenouvellementDecisionMasseProcess() {
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
        return "Renouvellement en masse des décisions CAP";
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

            ResultatTraitementMasseCsvJournal journalResultatTraitement = AurigaServiceLocator.getDecisionCAPService()
                    .renouvelerDecisionCAPMasse(new JadeProgressBarModel(1), false, annee, numeroAffilieDebut,
                            numeroAffilieFin, numeroPassageFacturation);

            if (journalResultatTraitement.hasErreursTechniques()) {
                JadeThread.logWarn(this.getClass().getName(),
                        "auriga.renouvellement.decision.masse.avertissement.cas.en.erreur.technique");
            }

            String fileName = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addFilenameSuffixUID("journalRenouvellementDecisionCAPMasse.csv");

            JadeFsFacade.writeFile(journalResultatTraitement.getLignesInByteFormat(), fileName);

            return fileName;

        } catch (Exception e) {
            handleException(e, "auriga.renouvellement.decision.masse.erreur.technique");
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
