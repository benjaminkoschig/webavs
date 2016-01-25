package globaz.al.process.rafam;

// import globaz.al.process.ContextProvider;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildAllowanceType;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.jaxbUtils.UnmarshallerEmployeurDelegueSingleton;

public class LoadAFDelegueAnnoncesRafam extends AbstractRafamSedex {

    @Override
    public void run() {

        try {
            String nomFichierDistant = "";
            String tmpLocalWorkFile = "";
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            String urlFichiersDeleguesCAF = JadePropertiesService.getInstance().getProperty(
                    "al.rafam.delegue.filesEnvoi.uri");
            List<String> repositoryAfDelegue = JadeFsFacade.getFolderChildren(urlFichiersDeleguesCAF);

            // String urlDelegueToCaf = "D:/aborescenceEmployeurDelegue/_work/toCdc-CAF";
            // List<String> repositoryAfDelegue = JadeFsFacade.getFolderChildren(urlDelegueToCaf);

            for (int i = 0; repositoryAfDelegue.size() > i; i++) {

                nomFichierDistant = repositoryAfDelegue.get(i);
                if (nomFichierDistant.endsWith(".xml")) {
                    boolean error = false;

                    try {
                        String[] distantUri = (repositoryAfDelegue.get(i)).split("/");
                        String nameOriginalFile = distantUri[distantUri.length - 1];

                        String idEmployeurDelegue = nameOriginalFile.substring(0, 2);

                        // File fileToSendToSedex = new File(nomFichierDistant);

                        tmpLocalWorkFile = JadeFsFacade.readFile(nomFichierDistant);
                        File fileToSendToSedex = new File(tmpLocalWorkFile);
                        if (fileToSendToSedex.isFile()) {

                            ChildAllowanceType currentAnnonces = UnmarshallerEmployeurDelegueSingleton
                                    .unmarshall(fileToSendToSedex);
                            ALServiceLocator.getAnnonceRafamCreationService().creerAnnoncesDelegue(idEmployeurDelegue,
                                    currentAnnonces);

                        }

                    } catch (Exception e) {
                        error = true;
                        e.printStackTrace();
                        // FIXME: use ALRAfamUtils.sendMail
                        sendMailForBug(e, "Problème dans le fichier :" + nomFichierDistant);
                    }
                    // on peut effacer dès que le fichier est traité et le fichier temporaire local aussi
                    if (!error) {
                        JadeFsFacade.delete(nomFichierDistant);
                        JadeFsFacade.delete(tmpLocalWorkFile);
                    }
                    // FIXME: use ALRAfamUtils.sendMail
                    // Envoi mail indiquant que des annonces doivent être validées
                    String email = JadePropertiesService.getInstance().getProperty("al.rafamContactEmail");
                    JadeSmtpClient.getInstance().sendMail(email, "Annonces af-delegue à valider",
                            "Des annonces de votre employeur délégué sont à valider depuis web@Avs", null);

                }

            }

        } catch (Exception e) {
            JadeLogger.warn(this,
                    "LoadAFDelegueAnnoncesRafam#run : Unable to load annonces from af-delegue, " + e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

    }

    private void sendMailForBug(Exception e, String messageComplement) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stacktrace = sw.toString();

        String email = JadePropertiesService.getInstance().getProperty("al.rafamContactEmail");
        String objetMail = "Erreur au chargement des annonces af-delegue";
        String corpsMail = "Impossible d'importer les annonces - " + messageComplement + "\n" + stacktrace;
        try {
            JadeSmtpClient.getInstance().sendMail(email, objetMail, corpsMail, null);
        } catch (Exception e1) {
            e1.printStackTrace();
            System.exit(0);
        }
    }

}
