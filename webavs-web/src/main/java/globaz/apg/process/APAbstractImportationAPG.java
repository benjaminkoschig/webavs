package globaz.apg.process;

import ch.globaz.common.mail.CommonFilesUtils;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.properties.APProperties;
import globaz.globall.db.*;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Classe abstraite d'importation et de cr�ation des droits APG depuis des eFormulaire.
 */
public abstract class APAbstractImportationAPG extends BProcess  {

    private static final Logger LOG = LoggerFactory.getLogger(APAbstractImportationAPG.class);

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
     * Initialisation de la session
     *
     * @throws Exception : lance une exception si un probl�me intervient lors de l'initialisation du contexte
     */
    protected void initBsession() throws Exception {
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    /**
     *  Fermeture de la session
     */
    protected void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }


    /**
     * M�thode permettant de r�cup�rer les adresses email � qui on souhaite envoyer l'email.
     * @param property la propri�t� applicative contenant l'adresse email
     * @return la liste des adresses email.
     */
    protected final List<String> getListEMailAddressTechnique(APProperties property) {
        List<String> listEmailAddress = new ArrayList<>();
        try {
            listEmailAddress.add(property.getValue());
        } catch (PropertiesException e) {
            LOG.error("ImportAPGPandemie - Erreur � la r�cup�ration de la propri�t� Adresse E-mail !! ", e);
        }
        return listEmailAddress;
    }


    /**
     * M�thode permettant de r�cup�rer le tiers � partir du NSS.
     *
     * @param nss : le nss
     * @return le tiers
     * @throws Exception
     */
    protected PRTiersWrapper getTiersByNss(String nss) throws Exception {
        try {
            return PRTiersHelper.getTiers(bsession, nss);
        } catch (Exception e) {
            errors.add("Impossible de r�cup�rer le tiers : "+nss);
            LOG.error("Erreur lors de la r�cup�ration du tiers " + nss, e);
            throw new Exception("APImportationAPGPandemie#getTiersByNss : erreur lors de la r�cup�ration du tiers ");
        }
    }



    protected boolean isJadeThreadError(){
        if(JadeThread.logMessages() != null) {
            return JadeThread.logMessages().length > 0;
        }
        return false;
    }

    /**
     * Ajout des erreurs blocantes
     * @param methodeSource
     */
    protected void addJadeThreadErrorToListError(String methodeSource) {
        JadeBusinessMessage[] messages = JadeThread.logMessages();
        LOG.error(methodeSource+": ");
        errors.add("Concerne: "+methodeSource+"\n");
        for (int i = 0; (messages != null) && (i < messages.length); i++) {
            LOG.error("-->Erreur: "+messages[i].getContents(null));
            errors.add("-->Erreur: "+messages[i].getContents(null));
        }
        errors.add("\n");
    }

    /**
     * Cr�ation de l'ID de la caisse li� au droit.
     *
     * @return l'id de la caisse.
     */
    protected String creationIdCaisse() {
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            return noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            errors.add("Impossible de r�cup�rer les propri�t�s n� caisse et n� agence");
            LOG.error("APImportationAPGPandemie#creationIdCaisse : A fatal exception was thrown when accessing to the CommonProperties " + exception);
        }
        return null;
    }


    /**
     * Formattage du NPA
     *
     * @param npa le npa � formatter
     * @return le npa formatt�.
     */
    protected String formatNPA(String npa) {
        if(!Objects.isNull(npa)){
            String npaTrim = StringUtils.trim(npa);
            if(StringUtils.isNumeric(npaTrim)){
                return npaTrim;
            }else{
                infos.add("NPA incorrect ! Celui-ci doit �tre dans un format num�rique uniquement ! Valeur: "+npa);
            }
        }
        return "";
    }

    /**
     * Formattage de la date XMLGregorianCalendar en String.
     *
     * @param date la date au format XMLGregorianCalendar
     * @return la date formatt� en String
     */
    protected String tranformGregDateToGlobDate(XMLGregorianCalendar date){
        if (Objects.nonNull(date)){
            return JadeDateUtil.getGlobazFormattedDate(date.toGregorianCalendar().getTime());
        } else {
            return "";
        }

    }

    /**
     * Cr�ation du R�le APG tiers si il n'existe pas pour ce tiers.
     *
     * @param idTiers : l'id Tiers pour lequel on souhaite cr�er un r�le APG.
     * @throws Exception
     */
    protected void creationRoleApgTiers(String idTiers) throws Exception {
        TIRoleManager roleManager = new TIRoleManager();
        roleManager.setSession(bsession);
        roleManager.setForIdTiers(idTiers);
        BStatement statement = null;
        BTransaction trans = (BTransaction) bsession.newTransaction();
        trans.openTransaction();
        try {
            statement = roleManager.cursorOpen(bsession.getCurrentThreadTransaction());
            TIRole role = null;
            boolean isRolePresent = false;

            while ((role = (TIRole) roleManager.cursorReadNext(statement)) != null) {
                if (IntRole.ROLE_APG.equals(role.getRole())) {
                    isRolePresent = true;
                }
            }

            if (!isRolePresent) {
                // on ajoute le r�le APG au Tier si il ne l'a pas deja
                ITIRole newRole = (ITIRole) bsession.getAPIFor(ITIRole.class);
                newRole.setIdTiers(idTiers);
                newRole.setISession(PRSession.connectSession(bsession, TIApplication.DEFAULT_APPLICATION_PYXIS));
                newRole.setRole(IntRole.ROLE_APG);
                newRole.add(bsession.getCurrentThreadTransaction());
            }
            trans.commit();
        } catch (Exception e) {
            errors.add("Une erreur s'est produite dans la cr�ation du r�le du tiers : "+idTiers);
            LOG.error("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la cr�ation du r�le du tiers : "+idTiers);
            if (trans != null) {
                trans.rollback();
            }
            if(isJadeThreadError()){
//                addJadeThreadErrorToListInfos("R�le");
                // Il faut qu'on puisse ajouter le droit m�me s'il y a eu un probl�me dans l'ajout du r�le
                JadeThread.logClear();
            }
            throw new Exception("ImportAPGPandemie#creationRoleApgTiers - Une erreur s'est produite dans la cr�ation du r�le du tiers : "+idTiers);
        } finally {
            try {
                if (statement != null) {
                    try {
                        roleManager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                trans.closeTransaction();
            }
        }
    }

    /**
     * R�cup�ration de l'affiliation par son num�ro.
     *
     * @param numeroAffiliate : le num�ro d'affili�.
     * @return l'affiliation si elle a �t� trouv�e.
     * @throws Exception
     */
    protected AFAffiliation findAffiliationByNumero(String numeroAffiliate) throws Exception {
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(bsession);
        manager.setForAffilieNumero(numeroAffiliate);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            return (AFAffiliation) manager.getFirstEntity();
        }
        return null;
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
     * Permet de d�placer le fichier suite au traitement.
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
                // Cr�ation du r�pertoire de storage
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


            // On ne supprime le fichier que si celui-ci a pu �tre copi� dans un des deux r�pertoires
            // Et on retourne un des deux emplacements du fichier copi�s
            if(JadeFsFacade.exists(storageFileTempTrace)){
                JadeFsFacade.delete(nomFichierDistant);
                fileToSend = storageFileTempTrace;
            } else if(JadeFsFacade.exists(storageFileTempStorage)){
                JadeFsFacade.delete(nomFichierDistant);
                fileToSend = storageFileTempStorage;
            }else{
                LOG.warn("Fichier non supprim� car celui-ci n'a pas pu �tre copi� dans un des deux emplacements suivants :\n-"
                        + storageFileTempTrace +"\n-"+storageFileTempTrace);
            }

        } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
            LOG.error("Erreur lors du d�placement du fichier " + nameOriginalFile, e);
        }
        return fileToSend;
    }

}
