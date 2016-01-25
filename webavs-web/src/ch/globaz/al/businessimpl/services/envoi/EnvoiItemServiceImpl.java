/**
 * 
 */
package ch.globaz.al.businessimpl.services.envoi;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.model.document.Document;
import globaz.op.wordml.model.document.WordmlDocument;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.envois.ALEnvoiData;
import ch.globaz.al.business.envois.ALEnvoiDataFactory;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.envoi.EnvoiItemService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author dhi
 * 
 */
public class EnvoiItemServiceImpl extends ALAbstractBusinessServiceImpl implements EnvoiItemService {

    private static final String FILE_URL_PREFIX = "file:";
    private static int iNbIteration = 0;

    private static final String JAR_URL_SEPARATOR = "!/";

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.envoi.EnvoiItemService#changeEnvoiStatus(java.lang.String)
     */
    @Override
    public void changeEnvoiStatus(String idEnvoiItem) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.envoi.EnvoiItemService#generateWordFile(java.lang.String)
     */
    @Override
    public HashMap<String, Object> generateWordFile(String idDossier, String idFormule) {
        // ---------------------------------------------------------------
        // Préparation de la hashmap de retour
        // ---------------------------------------------------------------
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        String msgError = "";
        String msgWarning = "";
        // --------------------------------------------------------------------
        // Récupération de la date du jour
        // --------------------------------------------------------------------
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
        SimpleDateFormat sdfShort = new SimpleDateFormat("dd.MM.yyyy");
        String csDateComplete = sdf.format(date);
        String csDateShort = sdfShort.format(date);
        // ---------------------------------------------------------------
        // Etape 1 - Création du document
        // ---------------------------------------------------------------
        Document currentDoc = null;
        try {
            currentDoc = generateWordFileStep1(idDossier, idFormule);
        } catch (Exception ex) {
            msgError = "Exception raised when trying to generate the word document : " + ex.toString();
            JadeThread.logError(toString(), ex.getMessage());
            currentDoc = null;
        }
        if (currentDoc != null) {
            // ---------------------------------------------------------------
            // Etape 2 - Enregistrement dans persistence
            // ---------------------------------------------------------------
            Boolean bError = generateWordFileStep2(currentDoc, idDossier, idFormule, csDateComplete);
            if (!bError) {
                // ---------------------------------------------------------------
                // Etape 3 - Copie s/ répertoire partagé - effacement persistence
                // ---------------------------------------------------------------
                bError = generateWordFileStep3(currentDoc, idDossier, idFormule, csDateComplete);
                if (!bError) {
                    // ---------------------------------------------------------------
                    // Etape 4 - Enregistrement dans la table des jobs/envois
                    // ---------------------------------------------------------------
                    bError = generateWordFileStep4(idDossier, idFormule, csDateShort, csDateComplete);
                    if (bError) {
                        msgWarning = "Unable to write the envoi into the job/envoi tables.\nAutomatic features are disabled : ";
                        msgWarning += "\n- Edition is not possible";
                        msgWarning += "\n- No archiving inside the folder";
                        msgWarning += "\n- No GED insertion";
                        msgWarning += "\n- No Journalisation";
                        // Save the doc into the hashmap
                        returnMap.put("document", "");// currentDoc);
                    } else {
                        // Save the link into the hashmap
                        returnMap.put("document",
                                getWordMLDocumentFilePathFromClient(idDossier, idFormule, csDateComplete));
                    }
                } else {
                    msgWarning = "The word file cannot be saved on the server (SMB).\nAutomatic features are disabled : ";
                    msgWarning += "\n- Edition is not possible";
                    msgWarning += "\n- No archiving inside the folder";
                    msgWarning += "\n- No GED insertion";
                    msgWarning += "\n- No Journalisation";
                    // Save the doc into the hashmap
                    returnMap.put("document", "");// currentDoc);
                }
            } else {
                msgWarning = "The word file cannot be saved on the server (persistence).\nAutomatic features are disabled : ";
                msgWarning += "\n- Edition is not possible";
                msgWarning += "\n- No archiving inside the folder";
                msgWarning += "\n- No GED insertion";
                msgWarning += "\n- No Journalisation";
                // Save the doc into the hashmap
                returnMap.put("document", "");// currentDoc);
            }
        } else {
            msgError = "Unable to create a Word Document for dossier " + idDossier + ", formule : " + idFormule;
            // document empty
            returnMap.put("document", "");
        }
        // ---------------------------------------------------------------
        // Si errors, clé file contient le fichier xml ou rien
        // Si ok, clé file contient le chemin du fichier
        // ---------------------------------------------------------------
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.WARN)) {
            for (int iMessage = 0; iMessage < JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN).length; iMessage++) {
                msgWarning += "\n"
                        + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN)[iMessage]
                                .getContents(JadeThread.currentLanguage());
            }
        }
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            for (int iMessage = 0; iMessage < JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
                msgError += "\n"
                        + JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                                .getContents(JadeThread.currentLanguage());
            }
        }
        JadeThread.logClear();
        returnMap.put("warning", msgWarning);
        returnMap.put("error", msgError);
        return returnMap;
    }

    /**
     * Création du document en fonction d'un idDossier et d'un idFormule
     * 
     * @param idDossier
     * @param idFormule
     * @return WordMLDocument ou null si erreurs
     * @throws Exception
     * @throws JadeApplicationServiceNotAvailableException
     */
    private Document generateWordFileStep1(String idDossier, String idFormule)
            throws JadeApplicationServiceNotAvailableException, Exception {
        // Préparation de la map nom-classe, id
        HashMap<Object, Object> map = prepareMapIdForFusion(idDossier);

        // Préparation de l'EnvoiData
        ALEnvoiData currentEnvoiData = ALEnvoiDataFactory.getALEnvoiData(map, idFormule);

        // Création du document
        Object returnDocument = ENServiceLocator.getDocumentMergeService().createDocument(currentEnvoiData);
        if (returnDocument instanceof WordmlDocument) {
            return (Document) returnDocument;
        } else {
            return null;
        }
    }

    /**
     * Enregistrement du document en persistence
     * 
     * @param currentDoc
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return true si des erreurs ont été rencontrées
     */
    private Boolean generateWordFileStep2(Document currentDoc, String idDossier, String idFormule, String csDateComplete) {
        // -------------------------------------------------
        // Save the file on the server web - persistence dir
        // -------------------------------------------------
        BufferedWriter out = null;
        String path = Jade.getInstance().getPersistenceDir();
        String filePath = path + getWordMLDocumentFileName(idDossier, idFormule, csDateComplete);
        Boolean bError = false;
        try {
            // check the path and create it if needed
            new File(path).mkdirs();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF8"));
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            out.newLine();
            StringWriter currentWriter = new StringWriter();
            currentDoc.toXml(currentWriter);
            out.write(currentWriter.toString());
            out.flush();
        } catch (Exception ex) {
            bError = true;
            JadeThread.logError(toString(), "Error Writer Tempory file wordExport.doc" + ex.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    bError = true;
                    JadeThread
                            .logError(toString(), "Error closing Writer Tempory file wordExport.doc" + e.getMessage());
                }
            }
        }
        return bError;
    }

    /**
     * Copy du document dans le répertoire de travail des utilisateurs
     * 
     * @param currentDoc
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return true si des erreurs ont été rencontrées
     */
    private Boolean generateWordFileStep3(Document currentDoc, String idDossier, String idFormule, String csDateComplete) {
        String pathSource = Jade.getInstance().getPersistenceDir();
        String filePathSource = pathSource + getWordMLDocumentFileName(idDossier, idFormule, csDateComplete);
        String filePathTarget = getWordMLDocumentFilePathFromServer(idDossier, idFormule, csDateComplete);
        Boolean bError = false;
        // copie
        try {
            if (!JadeStringUtil.isEmpty(filePathTarget)) {
                JadeFsFacade.copyFile(filePathSource, filePathTarget);
            } else {
                bError = true;
            }
        } catch (Exception e) {
            bError = true;
            JadeThread.logError(toString(), "Error copying doc file to Shared URL " + filePathSource + " - "
                    + filePathTarget + " >> " + e.toString());
        }
        // efface le fichier source
        try {
            JadeFsFacade.delete(filePathSource);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Error deleting doc file from persistence " + filePathSource + " >> " + ex.toString());
        }
        return bError;
    }

    /**
     * Ecriture du document généré en db pour trace et mise en GED postérieure
     * 
     * @param idDossier
     * @param idFormule
     * @param csDateShort
     * @param csDateComplete
     * @return true si des erreurs ont été rencontrées
     */
    private Boolean generateWordFileStep4(String idDossier, String idFormule, String csDateShort, String csDateComplete) {
        Boolean bError = false;
        EnvoiJobSimpleModel currentJob = null;

        // --------------------------------------------------------------------------
        // Recherche d'un job existant sinon création
        // --------------------------------------------------------------------------
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        String currentUserId = currentSession.getUserId();
        EnvoiJobSimpleModelSearch jobSearch = new EnvoiJobSimpleModelSearch();
        jobSearch.setForJobUser(currentUserId);
        jobSearch.setForJobDate(csDateShort);
        jobSearch.setForJobStatus(ALCSEnvoi.STATUS_ENVOI_GENERATED);
        try {
            jobSearch = ALImplServiceLocator.getEnvoiJobSimpleModelService().search(jobSearch);
            if (jobSearch.getSize() < 1) {
                currentJob = new EnvoiJobSimpleModel();
                currentJob.setJobDate(csDateShort);
                currentJob.setJobDescription(currentUserId + "'s daily documents - " + csDateShort);
                currentJob.setJobUser(currentUserId);
                currentJob.setJobStatus(ALCSEnvoi.STATUS_ENVOI_GENERATED);
                currentJob.setJobType(ALCSEnvoi.TYPE_ENVOI_MANUAL);
                currentJob = ALImplServiceLocator.getEnvoiJobSimpleModelService().create(currentJob);
            } else if (jobSearch.getSize() == 1) {
                currentJob = (EnvoiJobSimpleModel) jobSearch.getSearchResults()[0];
            } else {
                JadeThread.logError(toString(), "Multiple records : Error searching job for idDossier " + idDossier
                        + " idFormule " + idFormule);
                bError = true;
            }
        } catch (Exception ex) {
            bError = true;
            JadeThread.logError(toString(), "Error searching job for idDossier " + idDossier + " idFormule "
                    + idFormule);
        }
        // --------------------------------------------------------------------------
        // Le job est prêt, création de l'enregistrement du document
        // --------------------------------------------------------------------------
        if (currentJob != null) {
            EnvoiItemSimpleModel currentEnvoi = new EnvoiItemSimpleModel();
            currentEnvoi.setEnvoiFileName(getWordMLDocumentFileName(idDossier, idFormule, csDateComplete));
            currentEnvoi.setEnvoiNoGroupe("0");
            currentEnvoi.setEnvoiStatus(ALCSEnvoi.STATUS_ENVOI_GENERATED);
            currentEnvoi.setEnvoiType(ALCSEnvoi.TYPE_ENVOI_MANUAL);
            currentEnvoi.setIdExternalLink(idDossier);
            currentEnvoi.setIdFormule(idFormule);
            currentEnvoi.setIdJob(currentJob.getId());
            try {
                currentEnvoi = ALImplServiceLocator.getEnvoiItemSimpleModelService().create(currentEnvoi);
            } catch (Exception ex) {
                bError = true;
                JadeThread.logError(toString(), "Error creating envoi item for idDossier " + idDossier + " idFormule "
                        + idFormule);
            }
        }
        try {
            if (bError) {
                JadeThread.rollbackSession();
            } else {
                JadeThread.commitSession();
            }
        } catch (Exception ex) {
            bError = true;
            JadeThread.logError(toString(), "Error when committing/rollbacking envoi/job");

        }
        return bError;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.envoi.EnvoiItemService#getAllClassList(java.lang.String)
     */
    @Override
    public ArrayList<String> getAllClassList(String basePackage) {
        ArrayList<String> returnArray = new ArrayList<String>();
        java.lang.Package[] allPackages = java.lang.Package.getPackages();
        for (int iPackage = 0; iPackage < allPackages.length; iPackage++) {
            String packageName = allPackages[iPackage].getName();
            if (packageName.indexOf(basePackage) >= 0) {
                ArrayList<String> allClasses = lireListService(packageName);
                for (int iClasse = 0; iClasse < allClasses.size(); iClasse++) {
                    String completeName = packageName + "." + allClasses.get(iClasse);
                    // check si classe public ou interface
                    try {
                        Class currentClass = Class.forName(completeName);
                        int modifiers = currentClass.getModifiers();
                        if (Modifier.isInterface(modifiers)) {
                            // JadeLogger.info(this, "Interface : " + completeName);
                            continue;
                        }
                        if (!Modifier.isPublic(modifiers)) {
                            // JadeLogger.info(this, "NOT PUBLIC : " + completeName);
                            continue;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        continue;
                    }

                    if (!returnArray.contains(completeName)) {
                        returnArray.add(completeName);
                    }
                }
            }
        }
        Collections.sort(returnArray);
        return returnArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.envoi.EnvoiItemService#getAllMethodList(java.lang.String)
     */
    @Override
    public ArrayList<String> getAllMethodList(String classFullName) {
        // JadeLogger.info(this, "-------- Itérateur : " + EnvoiItemServiceImpl.iNbIteration + " >> Methods for class "
        // + classFullName + " -------------------------------------");
        ArrayList<String> returnArray = new ArrayList<String>();
        try {
            Class currentClass = Class.forName(classFullName);
            Method[] allMethods = currentClass.getMethods();
            for (int iCurrentMethod = 0; iCurrentMethod < allMethods.length; iCurrentMethod++) {
                Method currentMethod = allMethods[iCurrentMethod];
                int modifiers = currentMethod.getModifiers();
                String currentString = currentMethod.getName();
                // méthodes gets public
                if (currentString.indexOf("get") != 0) {
                    continue;
                } else if (currentString.indexOf("getClass") == 0) {
                    continue;
                } else if (!Modifier.isPublic(modifiers)) {
                    continue;
                } else if (currentString.toLowerCase().indexOf("getpassword") >= 0) {
                    continue;
                }
                // Sans paramètre
                if (currentMethod.getGenericParameterTypes().length > 0) {
                    continue;
                }
                // remove the get
                currentString = currentString.substring(3);
                // 1ère lettre en minuscule
                currentString = currentString.substring(0, 1).toLowerCase() + currentString.substring(1);

                // type natif, retourne, autrement réflexion sur la classe
                if (currentMethod.getGenericReturnType().equals(String.class)
                        || currentMethod.getGenericReturnType().equals(StringBuffer.class)
                        || currentMethod.getGenericReturnType().equals(Integer.class)
                        || currentMethod.getGenericReturnType().equals(int.class)
                        || currentMethod.getGenericReturnType().equals(BigDecimal.class)
                        || currentMethod.getGenericReturnType().equals(BigInteger.class)
                        || currentMethod.getGenericReturnType().equals(Character.class)
                        || currentMethod.getGenericReturnType().equals(char.class)
                        || currentMethod.getGenericReturnType().equals(Byte.class)
                        || currentMethod.getGenericReturnType().equals(byte.class)
                        || currentMethod.getGenericReturnType().equals(Short.class)
                        || currentMethod.getGenericReturnType().equals(short.class)
                        || currentMethod.getGenericReturnType().equals(Long.class)
                        || currentMethod.getGenericReturnType().equals(long.class)
                        || currentMethod.getGenericReturnType().equals(Float.class)
                        || currentMethod.getGenericReturnType().equals(float.class)
                        || currentMethod.getGenericReturnType().equals(Double.class)
                        || currentMethod.getGenericReturnType().equals(double.class)
                        || currentMethod.getGenericReturnType().equals(Boolean.class)
                        || currentMethod.getGenericReturnType().equals(boolean.class)
                        || currentMethod.getGenericReturnType().equals(void.class)
                        || currentMethod.getGenericReturnType().equals(Void.class)) {
                    returnArray.add(currentString);
                } else if (currentMethod.getReturnType().getName().indexOf("java.") < 0) {
                    int iLastIteration = EnvoiItemServiceImpl.iNbIteration;
                    EnvoiItemServiceImpl.iNbIteration++;
                    if (EnvoiItemServiceImpl.iNbIteration < 8) {
                        String searchedType = currentMethod.getReturnType().getName();
                        ArrayList<String> objectMethods = new ArrayList<String>();
                        objectMethods = getAllMethodList(searchedType);
                        for (int iElement = 0; iElement < objectMethods.size(); iElement++) {
                            returnArray.add(currentString + "." + objectMethods.get(iElement));
                        }
                    } else {
                        // JadeLogger.info(this, "-------! Itérateur : " + EnvoiItemServiceImpl.iNbIteration
                        // + " >> Methods for class " + classFullName + " -------------------------------------");
                    }
                    EnvoiItemServiceImpl.iNbIteration = iLastIteration;

                }
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error getting methods for " + classFullName + " : " + ex.toString());
        }
        return returnArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.envoi.EnvoiItemService#getAvailableDocumentsCSList(java.lang.String)
     */
    @Override
    public ArrayList<FWParametersCode> getAvailableDocumentsCSList(String idDossier) {

        // Préparation de l'arraylist de retour
        ArrayList<FWParametersCode> allCodes = new ArrayList<FWParametersCode>();

        // Recherche de tous les codes système alenvoidoc
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("ALENVOIDOC");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return allCodes;
        }
        for (Iterator it = cm.getContainer().iterator(); it.hasNext();) {
            FWParametersCode code = (FWParametersCode) it.next();
            // Appel AJAX, cacher les informations de session (user - pwd)
            code.setSession(null);
            code.getCurrentCodeUtilisateur().setSession(null);
            allCodes.add(code);
        }

        return allCodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.envoi.EnvoiItemService#getAvailableDocumentsList(java.lang.String)
     */
    @Override
    public ArrayList<EnvoiTemplateComplexModel> getAvailableDocumentsList(String idDossier)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {
        // Préparation de l'arraylist
        ArrayList<EnvoiTemplateComplexModel> allTemplates = new ArrayList<EnvoiTemplateComplexModel>();

        // Recherche de tous les modèles disponibles
        EnvoiTemplateComplexModelSearch searchModel = new EnvoiTemplateComplexModelSearch();
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel = ALServiceLocator.getEnvoiTemplateComplexService().search(searchModel);
        for (int iModel = 0; iModel < searchModel.getSize(); iModel++) {
            EnvoiTemplateComplexModel currentModel = (EnvoiTemplateComplexModel) searchModel.getSearchResults()[iModel];
            allTemplates.add(currentModel);
        }

        // retourne l'arraylist remplie
        return allTemplates;
    }

    /**
     * Récupération du nom du fichier à créer/créé
     * 
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return
     */
    private String getWordMLDocumentFileName(String idDossier, String idFormule, String csDateComplete) {
        String fileName = "";
        fileName += idDossier + "_" + idFormule + "_" + csDateComplete + ".doc";
        return fileName;
    }

    /**
     * Construction du nom du fichier en fonction de différents paramètres - avec filePath
     * 
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return nom du fichier complet
     */
    private String getWordMLDocumentFilePathFromClient(String idDossier, String idFormule, String csDateComplete) {
        String filePath = "";
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(ALCSEnvoi.SHARED_PATH_FROM_CLIENT);
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Path from Client to the shared directory not found. Exception : " + ex.toString());
        }
        if (searchModel.getSize() == 1) {
            EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel.getSearchResults()[0];
            filePath += currentParametres.getValeurParametre();
        } else {
            JadeThread.logError(toString(), "Path from Client to the shared directory not found.");
        }
        filePath += getWordMLDocumentFileName(idDossier, idFormule, csDateComplete);
        return filePath;
    }

    /**
     * Construction du nom du fichier en fonction de différents paramètres - avec filePath
     * 
     * @param idDossier
     * @param idFormule
     * @param csDateComplete
     * @return nom du fichier complet
     */
    private String getWordMLDocumentFilePathFromServer(String idDossier, String idFormule, String csDateComplete) {
        String filePath = "";
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(ALCSEnvoi.SHARED_PATH_FROM_APPLICATION_SERVER);
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
        } catch (Exception ex) {
            JadeThread.logError(toString(),
                    "Path from application server to the shared directory not found. Exception : " + ex.toString());
        }
        if (searchModel.getSize() == 1) {
            EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel.getSearchResults()[0];
            filePath += currentParametres.getValeurParametre();
        } else {
            JadeThread.logError(toString(), "Path from application server to the shared directory not found.");
        }
        filePath += getWordMLDocumentFileName(idDossier, idFormule, csDateComplete);
        return filePath;
    }

    /**
     * Récupération des ressources + noms classes en fonction d'un nom de package
     * 
     * @param nom_package
     * @return
     */
    private ArrayList<String> lireListService(String nom_package) {

        // Instanciation de la liste de service
        ArrayList<String> listService = new ArrayList<String>();

        // Mis en forme du nom du package pour la methode getresources (pour la methode getresource, il faut rajouter /
        // au début de la chaîne
        String name = new String(nom_package);
        name = name.replace('.', '/');

        // TODO : Necessaire sous Websphere - A ne pas mettre sous Jonas
        name = name + "/";

        URL url = null;
        Enumeration<URL> myEnum = null;
        try {
            // Recuperation sous forme d'enumeration de la liste des URL contenant ce package
            myEnum = EnvoiItemServiceImpl.class.getClassLoader().getResources(name);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (myEnum != null) {
            // Parcourt la liste des URL
            while (myEnum.hasMoreElements()) {

                // Récupération d'une URL
                url = myEnum.nextElement();

                File directory = new File(url.getFile());

                // New code
                // ======
                if (directory.exists()) {
                    // Recupere la liste des fichiers dans le package (si les classes sont directement dans un
                    // repertoire)
                    String[] files = directory.list();
                    for (int i = 0; i < files.length; i++) {

                        // Ne prends que le fichier finissant par .class. Ne pas tenir compte des sources si present.
                        if (files[i].endsWith(".class")) {
                            // removes the .class extension
                            String classname = files[i].substring(0, files[i].length() - 6);

                            listService.add(classname);
                        }
                    }
                } else {
                    try {
                        // Si on est pas dans un repertoire
                        // le package doit être contenu dans un jar
                        URLConnection con = url.openConnection();
                        JarFile jfile = null;
                        String starts = null;

                        if (con instanceof JarURLConnection) {
                            // Should usually be the case for traditional JAR files.
                            JarURLConnection jarCon = (JarURLConnection) con;
                            jfile = jarCon.getJarFile();
                            starts = jarCon.getEntryName();
                        } else {
                            String urlFile = url.getFile();
                            int separatorIndex = urlFile.indexOf(EnvoiItemServiceImpl.JAR_URL_SEPARATOR);
                            String jarFileUrl = urlFile.substring(0, separatorIndex);
                            if (jarFileUrl.startsWith(EnvoiItemServiceImpl.FILE_URL_PREFIX)) {
                                jarFileUrl = jarFileUrl.substring(EnvoiItemServiceImpl.FILE_URL_PREFIX.length());
                            }
                            jfile = new JarFile(jarFileUrl);
                            starts = urlFile
                                    .substring(separatorIndex + EnvoiItemServiceImpl.JAR_URL_SEPARATOR.length());
                        }

                        Enumeration<JarEntry> e = jfile.entries();
                        while (e.hasMoreElements()) {
                            // Parcourt les differents elements du jar
                            ZipEntry entry = e.nextElement();
                            String entryname = entry.getName();
                            if (entryname.startsWith(starts) && (entryname.lastIndexOf('/') <= starts.length())
                                    && entryname.endsWith(".class")) {
                                // Ne prends que les fichiers class appartenant au package dans le JAR

                                // Ne recupere que le nom de la classe sans le package
                                String classname = entryname.substring(0, entryname.length() - 6);
                                if (classname.startsWith("/")) {
                                    classname = classname.substring(1);
                                }
                                classname = classname.substring(starts.length());
                                listService.add(classname);
                            }
                        }
                    } catch (IOException ioex) {
                        System.err.println(ioex);
                    }
                }
            }
        }

        return listService;

    }

    /**
     * Préparation de la map minimale classe - id
     * 
     * @param idDossier
     * @return
     */
    private HashMap<Object, Object> prepareMapIdForFusion(String idDossier) {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(DossierComplexModel.class.getName(), idDossier);
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        map.put(currentSession.getClass().getName(), currentSession.getUserId());
        return map;

    }

}
