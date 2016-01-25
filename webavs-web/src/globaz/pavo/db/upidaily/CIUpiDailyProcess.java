package globaz.pavo.db.upidaily;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.fs.message.JadeFsFileInfo;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.url.JadeUrl;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.upi.CIGenereInactiveInvalid;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class CIUpiDailyProcess extends BProcess {

    // static String REP_SERVEUR_DISTANT =
    // "sftp://nraupi:nraupi@SHEBNRA1.INTRANET-HEB.CH:22/db2/db2home/nraupi/batch/globazNra/save";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static String CODE_MUTATION_INACTIVE = "2";
    static String CODE_MUTATION_INVALIDE = "3";
    static String CODE_MUTATION_VALABLE_JOURNALIER = "1";
    static String CODE_MUTATION_VALABLE_MENSUEL = "0";
    static String FLAG_VALIDITE_PLUS_A_JOUR = "1";
    static String NRA_FEMME = "2";
    static String NRA_HOMME = "1";

    private ArrayList filesToProcessed = new ArrayList(); // fichiers à traiter
    private ArrayList listErreurs = new ArrayList();
    private ArrayList listInactive = new ArrayList();
    private ArrayList listInvalide = new ArrayList();
    // fichiers téléchargés
    private String raisonUpdate = "";
    private String repServeurDistant = null;// chemin du serveur distant NRA/UPI
    // définit dans pavo.properties
    private String repServeurLocal = null;// chemin du répértoire où déposer les

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        JadeLogger.info(CIUpiDailyProcess.class, "--- DEBUT du process CIUpiDailyProcess ---");

        repServeurDistant = getSession().getApplication().getProperty("nraUpiServer");
        if (JadeStringUtil.isEmpty(repServeurDistant)) {
            throw new Exception("Serveur NRA/UPI non définit");
        }

        repServeurLocal = Jade.getInstance().getSharedDir();
        if (JadeStringUtil.isEmpty(repServeurLocal)) {
            throw new Exception("Répertoire partagé non définit");
        }

        JadeLogger.info(CIUpiDailyProcess.class, "Répértoire local : " + repServeurLocal);

        // Téléchargement des fichiers
        downloadFilesToProcessed();

        // Traitement des fichiers
        processFiles();

        JadeLogger.info(CIUpiDailyProcess.class, "--- FIN du process CIUpiDailyProcess ---");

        return true;
    }

    private void downloadFilesToProcessed() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException, Exception {
        // Récupération de la liste des fichiers disponible sur le serveur
        // distant
        String serverUrl = JadeFilenameUtil.normalizePathRoot(repServeurDistant);
        JadeLogger.info(CIUpiDailyProcess.class, "Recherche des fichiers disponibles sur : " + serverUrl);
        List files = JadeFsFacade.getFolderChildrenInfo(serverUrl);
        if ((files == null) || (files.size() == 0)) {
            JadeLogger.info(CIUpiDailyProcess.class, "Pas de fichiers trouvés sur : " + serverUrl);
            throw new Exception("Aucun fichier disponible sur " + serverUrl);
        }
        JadeLogger.info(CIUpiDailyProcess.class, files.size() + " fichier(s) trouvé(s) sur : " + repServeurDistant);

        // création de la liste des fichiers déjà traités
        ArrayList filesAlreadyProcessed = new ArrayList();
        CIUpiDailyManager upiDailyManager = new CIUpiDailyManager();
        upiDailyManager.setSession(getSession());
        upiDailyManager.find(BManager.SIZE_NOLIMIT);
        if (upiDailyManager.size() != 0) {
            // remplissage du arrayList
            for (int i = 0; i < upiDailyManager.size(); i++) {
                CIUpiDaily upiDaily = (CIUpiDaily) upiDailyManager.getEntity(i);
                filesAlreadyProcessed.add(upiDaily.getNomUpiDaily());
            }
        }

        // nombre de fichiers à télécharger
        JadeLogger.info(CIUpiDailyProcess.class, Integer.toString(files.size() - upiDailyManager.size())
                + " fichier(s) à télécharger");

        // parcours de la liste des fichiers du serveur distant et copie en
        // local si nécessaire
        String serverUrlLocal = repServeurLocal;
        String fileName = null;
        JadeUrl tmpUrl = new JadeUrl();
        for (Iterator iter = files.iterator(); iter.hasNext();) {
            try {
                JadeFsFileInfo info = (JadeFsFileInfo) iter.next();
                tmpUrl.setUrl(info.getUri());
                fileName = tmpUrl.getFile();

                // télécharge le fichier si il n'a pas déjà été traité (présent
                // dans la table CIUPIP) et que la date est plus petite que la
                // date du jour
                JADate today = JACalendar.today();
                BigDecimal todayAaaaMmJj = today.toAMJ();
                BigDecimal dateFichier = new BigDecimal(fileName.substring(fileName.indexOf("_") + 1).substring(0, 8));

                if (!filesAlreadyProcessed.contains(fileName) && (dateFichier.compareTo(todayAaaaMmJj) < 0)) {
                    JadeUrl outUrl = new JadeUrl();
                    outUrl.setPath(serverUrlLocal);
                    String localFile = outUrl.getPath() + fileName;
                    String remoteFile = serverUrl + fileName;
                    JadeLogger.info(CIUpiDailyProcess.class, "Téléchargement du fichier : " + remoteFile + "...");
                    JadeFsFacade.copyFile(remoteFile, localFile);

                    // ajout des fichiers à traité dans l'arraylist
                    filesToProcessed.add(fileName);
                } else {
                    JadeLogger.info(CIUpiDailyProcess.class, "Fichier déjà traité ou fichier du jour : " + fileName);
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, getClass().toString());
            }
        }

        JadeLogger.info(CIUpiDailyProcess.class, "Téléchargement des fichiers terminé");
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError() && !isAborted()) {
            return "Terminé";
        } else {
            return "Echec";
        }
    }

    private String getSqlChercheAssure() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT KAIIND,KANAVS,KALNOM,KAIPAY,KADNAI,KATSEX,KANSRC FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP ");
        sql.append("WHERE KANAVS=? AND KAIREG=" + CICompteIndividuel.CS_REGISTRE_ASSURES);
        return sql.toString();
    }

    private String getSqlUpdateCI() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP SET KALNOM = ?,KADNAI=?,KAIPAY=?,KATSEX=?,KANSRC=? WHERE KAIIND = ?");
        return sql.toString();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * nraDateNaissance (=jjmmaaaa)-> aaaammjj
     * 
     * @param nraDateNaissance
     * @return
     * @throws Exception
     */
    private String prepareNraDateNaissance(String nraDateNaissance) throws Exception {
        if (JadeStringUtil.isEmpty(nraDateNaissance) || nraDateNaissance.length() != 8) {
            throw new Exception("La date de naissance n'est pas valide");
        }

        String jour = nraDateNaissance.substring(0, 2);
        String mois = nraDateNaissance.substring(2, 4);
        String annee = nraDateNaissance.substring(4);
        String dateNaissance = annee + mois + jour;

        // jjmmaaaa -> aaaammjj
        return dateNaissance;
    }

    /**
     * nraNomPrenom (="xxx,yyy")-> xxx,yyy
     * 
     * @param nraNomPrenom
     * @return
     * @throws Exception
     */
    private String prepareNraNomPrenom(String nraNomPrenom) throws Exception {
        if (JadeStringUtil.isEmpty(nraNomPrenom)) {
            throw new Exception("Le nom et prénom n'est pas valide");
        }
        StringTokenizer tokens = new StringTokenizer(nraNomPrenom, "\"\"");
        String nraNomPrenomModifie = tokens.nextToken();
        return nraNomPrenomModifie;
    }

    /**
     * nraPays (xxx) -> 315xxx
     * 
     * @param nraPays
     * @return
     * @throws Exception
     */
    private String prepareNraPays(String nraPays) throws Exception {
        if (JadeStringUtil.isEmpty(nraPays) || nraPays.length() != 3) {
            throw new Exception("Le code du pays n'est pas valide");
        }
        return new String(315 + nraPays);
    }

    /**
     * nraSexe (=1 ou =2)-> CS_HOMME si 1 et CS_FEMME si 2
     * 
     * @param nraSexe
     * @return
     * @throws Exception
     */
    private String prepareNraSexe(String nraSexe) throws Exception {
        if (!JadeStringUtil.isEmpty(nraSexe) && nraSexe.equals(NRA_HOMME)) {
            return CICompteIndividuel.CS_HOMME;
        } else if (!JadeStringUtil.isEmpty(nraSexe) && nraSexe.equals(NRA_FEMME)) {
            return CICompteIndividuel.CS_FEMME;
        } else {
            throw new Exception("Le sexe n'est pas valide");
        }
    }

    private void processFiles() throws FileNotFoundException, IOException, Exception, SQLException {
        // avant de traiter les fichier, on tri l'arraylist afin de traiter les
        // fichiers dans l'ordre croissant
        Collections.sort(filesToProcessed);

        // parcours des fichiers à traité
        JadeLogger.info(CIUpiDailyProcess.class, "Nombre de fichiers à traiter : " + filesToProcessed.size());
        JadeLogger.info(CIUpiDailyProcess.class, "Liste des fichiers à traiter : ");
        if (filesToProcessed.size() == 0) {
            getMemoryLog().logMessage("Aucun nouveau fichier à traiter", FWMessage.INFORMATION,
                    this.getClass().toString());
        }
        for (int i = 0; i < filesToProcessed.size(); i++) {
            JadeLogger.info(CIUpiDailyProcess.class, "=>" + filesToProcessed.get(i));
        }
        for (Iterator iter = filesToProcessed.iterator(); iter.hasNext();) {
            String fileName = (String) iter.next();
            JadeLogger.info(CIUpiDailyProcess.class, "Début du traitement du fichier : " + fileName);

            BufferedReader fileBuffered = new BufferedReader(new FileReader(repServeurLocal + "/" + fileName));
            String line = "";
            int countLine = 0;
            // parcours des lignes du fichier
            while ((line = fileBuffered.readLine()) != null) {
                countLine++;
                // paramètres du fichier csv
                String oldAvs = null;
                String newAvs = null;
                String codeMutation = null;
                String flagValidide = null;
                String nomPrenom = null;
                String sexe = null;
                String dateNaissance = null;
                String codeNationalite = null;
                String sourceDonnees = "";
                raisonUpdate = "";

                // preparedStatement pour les différentes requêtes
                BPreparedStatement preparedStaChercheAssureInvalide = null;
                BPreparedStatement preparedStaChercheAssureInactive = null;
                BPreparedStatement preparedStaUpdateAssure = null;
                BPreparedStatement preparedStaChercheAssure = null;
                try {
                    StringTokenizer tokens = new StringTokenizer(line, ";");
                    // remplissage des champs avec les données du fichier NRA
                    oldAvs = tokens.nextToken();
                    newAvs = tokens.nextToken();
                    codeMutation = tokens.nextToken();
                    flagValidide = tokens.nextToken();
                    nomPrenom = tokens.nextToken();
                    sexe = tokens.nextToken();
                    dateNaissance = tokens.nextToken();
                    codeNationalite = tokens.nextToken();
                    sourceDonnees = "";
                    if (tokens.hasMoreTokens()) {
                        sourceDonnees = tokens.nextToken();
                    }
                    if (codeMutation.equals(CODE_MUTATION_VALABLE_JOURNALIER)) {
                        if (flagValidide.equals(FLAG_VALIDITE_PLUS_A_JOUR)) {
                            // Recherche l'assuré dans la DB de la caisse
                            preparedStaChercheAssure = new BPreparedStatement(getTransaction());
                            preparedStaChercheAssure.prepareStatement(getSqlChercheAssure());
                            preparedStaChercheAssure.clearParameters();
                            preparedStaChercheAssure.setString(1, newAvs);
                            ResultSet resultAssure = preparedStaChercheAssure.executeQuery();
                            if (resultAssure.next()) {
                                if (!JadeStringUtil.isEmpty(resultAssure.getString("KANAVS"))
                                        && resultAssure.getString("KANAVS").trim().equals(newAvs.trim())) {
                                    JadeLogger.info(CIUpiDailyProcess.class,
                                            "TRAITEMENT DE L'ASSURE : " + resultAssure.getString("KANAVS") + ","
                                                    + resultAssure.getString("KALNOM"));

                                    // prépare les données du fichier NRA pour
                                    // la comparaison avec le CI et la mise à
                                    // jour du CI dans la DB
                                    nomPrenom = prepareNraNomPrenom(nomPrenom);
                                    sexe = prepareNraSexe(sexe);
                                    dateNaissance = prepareNraDateNaissance(dateNaissance);
                                    codeNationalite = prepareNraPays(codeNationalite);

                                    // vérifie si l'entête ci doit être mis à
                                    // jour
                                    boolean mustUpdate = false;

                                    if (!nomPrenom.trim().equals(resultAssure.getString("KALNOM").trim())) {
                                        mustUpdate = true;
                                        raisonUpdate = raisonUpdate + ",nom et prénom différent";
                                    }
                                    if (!sexe.trim().equals(String.valueOf(resultAssure.getInt("KATSEX")).trim())) {
                                        mustUpdate = true;
                                        raisonUpdate = raisonUpdate + ",sexe différent";
                                    }
                                    if (!dateNaissance.trim().equals(
                                            String.valueOf(resultAssure.getInt("KADNAI")).trim())) {
                                        mustUpdate = true;
                                        raisonUpdate = raisonUpdate + ",date de naissance différente";
                                    }
                                    if (!codeNationalite.trim().equals(
                                            String.valueOf(resultAssure.getInt("KAIPAY")).trim())) {
                                        mustUpdate = true;
                                        raisonUpdate = raisonUpdate + ",nationalité différente";
                                    }
                                    if (!JadeStringUtil.isBlankOrZero(sourceDonnees)
                                            && !sourceDonnees.trim().equals(
                                                    String.valueOf(resultAssure.getInt("KANSRC")).trim())) {
                                        mustUpdate = true;
                                        raisonUpdate = raisonUpdate + ",source différente";
                                    }

                                    if (mustUpdate) {
                                        // on fait la mise à jour
                                        JadeLogger.info(CIUpiDailyProcess.class, "MISE A JOUR DE L'ENTETE CI DE : "
                                                + newAvs + " " + resultAssure.getString("KALNOM") + " " + raisonUpdate);
                                        preparedStaUpdateAssure = new BPreparedStatement(getTransaction());
                                        preparedStaUpdateAssure.prepareStatement(getSqlUpdateCI());
                                        preparedStaUpdateAssure.clearParameters();
                                        preparedStaUpdateAssure.setString(1, nomPrenom);
                                        preparedStaUpdateAssure.setBigDecimal(2, new BigDecimal(dateNaissance));
                                        preparedStaUpdateAssure.setBigDecimal(3, new BigDecimal(codeNationalite));
                                        preparedStaUpdateAssure.setBigDecimal(4, new BigDecimal(sexe));
                                        if (!JadeStringUtil.isEmpty(sourceDonnees)) {
                                            preparedStaUpdateAssure.setBigDecimal(5, new BigDecimal(sourceDonnees));
                                        } else {
                                            preparedStaUpdateAssure.setBigDecimal(5, new BigDecimal("0"));
                                        }
                                        preparedStaUpdateAssure.setBigDecimal(6,
                                                new BigDecimal(resultAssure.getInt("KAIIND")));
                                        preparedStaUpdateAssure.execute();
                                        if (!getTransaction().hasErrors()) {
                                            getTransaction().commit();
                                            getMemoryLog().logMessage(
                                                    "L'assuré " + newAvs + " " + nomPrenom + " a été mis à jour",
                                                    FWMessage.INFORMATION, this.getClass().toString());
                                        } else {
                                            throw new Exception("Erreur lors du l'update : "
                                                    + getTransaction().getErrors());
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                        } else {
                            continue;
                        }
                    } else if (codeMutation.equals(CODE_MUTATION_INACTIVE)) {
                        // log dans le fichier des inactive
                        // Recherche l'assuré dans la DB de la caisse
                        preparedStaChercheAssureInactive = new BPreparedStatement(getTransaction());
                        preparedStaChercheAssureInactive.prepareStatement(getSqlChercheAssure());
                        preparedStaChercheAssureInactive.clearParameters();
                        preparedStaChercheAssureInactive.setString(1, oldAvs);
                        ResultSet resultAssureInactive = preparedStaChercheAssureInactive.executeQuery();
                        if (resultAssureInactive.next()) {
                            // créer la liste
                            JadeLogger.info(CIUpiDailyProcess.class,
                                    "INACTIF :" + resultAssureInactive.getString("KANAVS"));
                            ArrayList inactiveInformation = new ArrayList();
                            inactiveInformation.add(resultAssureInactive.getString("KANAVS"));
                            inactiveInformation.add(resultAssureInactive.getString("KALNOM"));
                            inactiveInformation.add(JADate.newDateFromAMJ(
                                    String.valueOf(resultAssureInactive.getInt("KADNAI"))).toStr("."));
                            inactiveInformation.add(getSession().getCodeLibelle(
                                    String.valueOf(resultAssureInactive.getInt("KATSEX"))));
                            inactiveInformation.add(getSession().getCodeLibelle(
                                    String.valueOf(resultAssureInactive.getInt("KAIPAY"))));

                            listInactive.add(inactiveInformation);
                        }
                    } else if (codeMutation.equals(CODE_MUTATION_INVALIDE)) {
                        // log dans le fichier des invalide
                        // Recherche l'assuré dans la DB de la caisse
                        preparedStaChercheAssureInvalide = new BPreparedStatement(getTransaction());
                        preparedStaChercheAssureInvalide.prepareStatement(getSqlChercheAssure());
                        preparedStaChercheAssureInvalide.clearParameters();
                        preparedStaChercheAssureInvalide.setString(1, oldAvs);
                        ResultSet resultAssureInvalide = preparedStaChercheAssureInvalide.executeQuery();
                        if (resultAssureInvalide.next()) {
                            // créer la liste
                            JadeLogger.info(CIUpiDailyProcess.class,
                                    "INVALIDE :" + resultAssureInvalide.getString("KANAVS"));
                            ArrayList invalideInformation = new ArrayList();
                            invalideInformation.add(resultAssureInvalide.getString("KANAVS"));
                            invalideInformation.add(resultAssureInvalide.getString("KALNOM"));
                            invalideInformation.add(JADate.newDateFromAMJ(
                                    String.valueOf(resultAssureInvalide.getInt("KADNAI"))).toStr("."));
                            invalideInformation.add(getSession().getCodeLibelle(
                                    String.valueOf(resultAssureInvalide.getInt("KATSEX"))));
                            invalideInformation.add(getSession().getCodeLibelle(
                                    String.valueOf(resultAssureInvalide.getInt("KAIPAY"))));

                            listInvalide.add(invalideInformation);
                        }
                    } else if (codeMutation.equals(CODE_MUTATION_VALABLE_MENSUEL)) {
                        continue;
                    } else {
                        throw new Exception("Le code de mutation n'est pas valide");
                    }
                } catch (Exception e) {
                    listErreurs.add(new CIUpiDailyLog(fileName, String.valueOf(countLine), line, e.getMessage()));
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                } finally {
                    try {
                        if (preparedStaChercheAssureInvalide != null) {
                            preparedStaChercheAssureInvalide.closePreparedStatement();
                        }
                        if (preparedStaChercheAssureInactive != null) {
                            preparedStaChercheAssureInactive.closePreparedStatement();
                        }
                        if (preparedStaUpdateAssure != null) {
                            preparedStaUpdateAssure.closePreparedStatement();
                        }
                        if (preparedStaChercheAssure != null) {
                            preparedStaChercheAssure.closePreparedStatement();
                        }
                    } catch (Exception e) {
                        listErreurs.add(new CIUpiDailyLog(fileName, String.valueOf(countLine), line, e.getMessage()));
                    }
                }
            }

            // insertion dans la table CIUPIP pour indiquer que le fichier a été
            // traité
            // ajout du fichier dans la table CIUPIP
            BTransaction myTransaction = new BTransaction(getSession());
            CIUpiDaily upiDaily = new CIUpiDaily();
            upiDaily.setSession(getSession());
            upiDaily.setNomUpiDaily(fileName);
            JadeLogger.info(CIUpiDailyProcess.class, "Traitement du fichier " + fileName + " terminé");
            upiDaily.add(myTransaction);
            if (!myTransaction.hasErrors()) {
                myTransaction.commit();
            } else {
                getMemoryLog().logMessage(
                        "Le fichier " + fileName + " n'as pas pu être mis en état traité dans la table CIUPIP"
                                + myTransaction.getErrors().toString(), FWViewBeanInterface.ERROR,
                        getClass().toString());
                myTransaction.rollback();
                myTransaction.clearErrorBuffer();
            }
        }

        // Génération des différents documents (invalidés,inactifs et erreurs)
        try {
            if (listInactive.size() != 0) {
                // génération du fichier d'inactive
                CIGenereInactiveInvalid genereInactive = new CIGenereInactiveInvalid();
                genereInactive.setInactive(true);
                genereInactive.setErrors(listInactive);
                genereInactive.setSession(getSession());
                String outPutInactive = genereInactive.getOutputFile();
                if (!JadeStringUtil.isBlankOrZero(outPutInactive)) {
                    registerAttachedDocument(null, outPutInactive);
                }
            }

            if (listInvalide.size() != 0) {
                // génération du fichier d'invalide
                CIGenereInactiveInvalid genereInvalide = new CIGenereInactiveInvalid();
                genereInvalide.setInvalid(true);
                genereInvalide.setErrors(listInvalide);
                genereInvalide.setSession(getSession());
                String outPutInvalide = genereInvalide.getOutputFile();
                if (!JadeStringUtil.isBlankOrZero(outPutInvalide)) {
                    registerAttachedDocument(null, outPutInvalide);
                }
            }

            if (listErreurs.size() != 0) {
                // génération du fichier d'erreur
                CIUpiDailyHtmlOut htmlOut = new CIUpiDailyHtmlOut();
                htmlOut.setSession(getSession());
                htmlOut.setData(listErreurs);
                htmlOut.setFilename("upiDailyErrors.html");
                registerAttachedDocument(null, htmlOut.getOutputFile());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la génération des documents " + e.getMessage(),
                    FWViewBeanInterface.ERROR, getClass().toString());
        }
    }

}
