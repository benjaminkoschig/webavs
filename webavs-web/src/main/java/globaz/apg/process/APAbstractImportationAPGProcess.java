package globaz.apg.process;

import ch.globaz.common.mail.CommonFilesUtils;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.properties.APProperties;
import globaz.globall.db.*;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Classe abstraite d'importation et de création des droits APG depuis des eFormulaire.
 */
public abstract class APAbstractImportationAPGProcess extends BProcess  {

    private static final Logger LOG = LoggerFactory.getLogger(APAbstractImportationAPGProcess.class);

    protected static final String BACKUP_FOLDER = "/backup/";
    protected static final String ERRORS_FOLDER = "/errors/";
    protected static final String XML_EXTENSION = ".xml";
    protected static final int MAX_TREATMENT = 40;
    protected BSession bsession;
    protected LinkedList<String> errors = new LinkedList();
    protected LinkedList<String> infos = new LinkedList();
    protected LinkedList<String> errorsCreateBeneficiaries = new LinkedList();

    protected String backupFolder;
    protected String errorsFolder;
    protected String storageFolder;
    protected String demandeFolder;
    protected List<String> nssTraites = new ArrayList<>();
    protected int nbTraites = 0;


    /**
     * Méthode permettant de récupérer les adresses email à qui on souhaite envoyer l'email.
     * @param property la propriété applicative contenant l'adresse email
     * @return la liste des adresses email.
     */
    protected final List<String> getListEMailAddressTechnique(APProperties property) {
        List<String> listEmailAddress = new ArrayList<>();
        try {
            String[] addresses = APProperties.EMAIL_AMAT_APAT.getValue().split(";");
            for (String address : addresses) {
                listEmailAddress.add(address);
            }
            //listEmailAddress.add(property.getValue());
        } catch (PropertiesException e) {
            LOG.error("ImportAPG-AMAT-APAT - Erreur à la récupération de la propriété Adresse E-mail !! ", e);
        }
        return listEmailAddress;
    }


    /**
     * Méthode permettant de récupérer le tiers à partir du NSS.
     *
     * @param nss : le nss
     * @return le tiers
     * @throws Exception
     */
    protected PRTiersWrapper getTiersByNss(String nss) throws Exception {
        try {
            return PRTiersHelper.getTiers(bsession, nss);
        } catch (Exception e) {
            errors.add("Impossible de récupérer le tiers : "+nss);
            LOG.error("Erreur lors de la récupération du tiers " + nss, e);
            throw new Exception("APImportationAPGPandemie#getTiersByNss : erreur lors de la récupération du tiers ");
        }
    }

    /**
     * Création de l'ID de la caisse lié au droit.
     *
     * @return l'id de la caisse.
     */
    protected String creationIdCaisse() {
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            return noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            errors.add("Impossible de récupérer les propriétés n° caisse et n° agence");
            LOG.error("APImportationAPGPandemie#creationIdCaisse : A fatal exception was thrown when accessing to the CommonProperties " + exception);
        }
        return null;
    }


    /**
     * Formattage du NPA
     *
     * @param npa le npa à formatter
     * @return le npa formatté.
     */
    protected String formatNPA(String npa) {
        if(!Objects.isNull(npa)){
            String npaTrim = StringUtils.trim(npa);
            if(StringUtils.isNumeric(npaTrim)){
                return npaTrim;
            }else{
                infos.add("NPA incorrect ! Celui-ci doit être dans un format numérique uniquement ! Valeur: "+npa);
            }
        }
        return "";
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    protected boolean hasError(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly() || !errors.isEmpty();
    }

    /**
     * Permet de déplacer le fichier suite au traitement.
     *
     * @param nomFichierDistant
     * @param nameOriginalFile
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    protected String movingFile(String nomFichierDistant, String nameOriginalFile, Boolean processSuccess) {
        String storageFolderTemp = "";
        String storageFileTempTrace = "";
        String storageFileTempStorage = "";
        String fileToSend = "";
        try{
            if (!JadeFsFacade.exists(backupFolder)) {
                JadeFsFacade.createFolder(backupFolder);
            }
            if (!JadeFsFacade.exists(errorsFolder)) {
                JadeFsFacade.createFolder(errorsFolder);
            }

            if (processSuccess) {
                // Création du répertoire de storage
                for (String nss : nssTraites) {
                    storageFolderTemp = CommonFilesUtils.createPathFiles(nss, backupFolder);
                }

                // Copie du fichier dans le storage
                if (!storageFolderTemp.isEmpty()) {
                    JadeFsFacade.copyFile(nomFichierDistant, storageFolderTemp + nameOriginalFile);
                    storageFileTempStorage = storageFolderTemp + nameOriginalFile;
                    LOG.info("Copie dans "+storageFileTempStorage);
                }

            } else {
                // copie du fichier en erreur
                JadeFsFacade.copyFile(nomFichierDistant, errorsFolder + nameOriginalFile);
                storageFileTempTrace = errorsFolder + nameOriginalFile;
            }
            LOG.info("Copie dans " + storageFileTempTrace);


            // On ne supprime le fichier que si celui-ci a pu être copié dans un des deux répertoires
            // Et on retourne un des deux emplacements du fichier copiés
            if(JadeFsFacade.exists(storageFileTempTrace)){
                JadeFsFacade.delete(nomFichierDistant);
                fileToSend = storageFileTempTrace;
            } else if(JadeFsFacade.exists(storageFileTempStorage)){
                JadeFsFacade.delete(nomFichierDistant);
                fileToSend = storageFileTempStorage;
            }else{
                LOG.warn("Fichier non supprimé car celui-ci n'a pas pu être copié dans un des deux emplacements suivants :\n-"
                        + storageFileTempTrace +"\n-"+storageFileTempTrace);
            }

        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
            LOG.error("Erreur lors du déplacement du fichier " + nameOriginalFile, e);
        }
        return fileToSend;
    }
}
