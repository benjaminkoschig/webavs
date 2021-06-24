package globaz.apg.eformulaire;

import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.properties.APProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class APImportationStatusReport implements Serializable {
    @Getter
    private final List<APImportationStatusFile> fileStatuses = new ArrayList<>();

    public APImportationStatusFile addFile(String fileName, String nss, boolean isWomen){
        APImportationStatusFile fileStatus = new APImportationStatusFile(fileName, nss, isWomen);
        fileStatuses.add(fileStatus);
        return fileStatus;
    }

    public long getNbFichierTraites(){
        return fileStatuses.stream().filter(APImportationStatusFile::isSucceed).count();
    }

    public void generationProtocols(){
        for (APImportationStatusFile file:fileStatuses) {
            generationProtocol(file);
        }
    }

    /**
     * M�thode permettant de g�n�rer le bilan du traitement et l'envoi du mail r�capitulatif.
     *
     */
    private void generationProtocol(APImportationStatusFile statusFile) {
        List<String> mails = getListEMailAddressTechnique();
        LOG.info("Envoi mail � l'adresse de la caisse {}", mails);
        ArrayList<String> filesName = new ArrayList<>();
        filesName.add(statusFile.getFileFullPath());
        ProcessMailUtils.sendMail(mails, getEMailObjectCaisse(statusFile), statusFile.buildStatusMessage(), filesName);
    }

    /**
     * R�cup�rer l'objet du mail.
     *
     * @return l'objet du mail
     */
    private String getEMailObjectCaisse(APImportationStatusFile statusFile) {
        return statusFile.getEMailObjectCaisse();
    }

    /**
     * M�thode permettant de r�cup�rer les adresses email � qui on souhaite envoyer l'email.
     * @return la liste des adresses email.
     */
    private List<String> getListEMailAddressTechnique() {
        List<String> listEmailAddress = new ArrayList<>();
        try {
            String[] addresses = APProperties.EMAIL_AMAT_APAT.getValue().split(";");
            listEmailAddress = Arrays.asList(addresses);
        } catch (PropertiesException e) {
            LOG.error("APImportationStatusReport#getListEMailAddressTechnique - Erreur � la r�cup�ration de la propri�t� Adresse E-mail !! ", e);
        }
        return listEmailAddress;
    }
}
