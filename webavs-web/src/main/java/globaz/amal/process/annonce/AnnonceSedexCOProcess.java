package globaz.amal.process.annonce;

import globaz.amal.process.AMALabstractProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOReceptionMessage5232_000202_1;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnonceSedexCOProcess extends AMALabstractProcess {
    private String email = null;
    private String annee = null;
    private String idTiersCM = null;

    @Override
    public String getDescription() {
        return "Contentieux Amal : génération de la liste des créances avec garantie de prise en charge effectuée !";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {
        AnnoncesCOReceptionMessage5232_000202_1 annonces202 = new AnnoncesCOReceptionMessage5232_000202_1();
        File file = annonces202.createAndPrintList(annee, idTiersCM);

        String[] files = new String[1];
        if (file != null) {
            files[0] = file.getPath();
        }

        String body;
        if (!JadeStringUtil.isBlankOrZero(idTiersCM)) {
            String caisseMaladie = "";
            try {
                TiersSimpleModel tiersSimpleModel = TIBusinessServiceLocator.getTiersService().read(idTiersCM);
                caisseMaladie = tiersSimpleModel.getDesignation1();
            } catch (Exception e) {
                // Si une exception pète, pas grave, on n'affiche simplement pas la caisse maladie
                caisseMaladie = "Non trouvée";
            }
            body = "\nAnnée : " + annee + "\nCaisse maladie :" + caisseMaladie;
        } else {
            body = "\nAnnée : " + annee + "\nCaisse maladie : non spécifiée";
        }

        try {
            JadeSmtpClient.getInstance().sendMail(getEmail(), getDescription(), body, files);
        } catch (Exception e) {
            JadeThread.logError(
                    "AnnonceSedexCOProcess",
                    "Erreur lors de l'envoi du mail du processu de génération de la liste de comparaison : "
                            + e.getMessage());
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getIdTiersCM() {
        return idTiersCM;
    }

    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

}
