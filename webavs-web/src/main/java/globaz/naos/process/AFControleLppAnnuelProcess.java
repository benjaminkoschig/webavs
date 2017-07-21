/*
 * Globaz SA.
 */
package globaz.naos.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.controleLpp.AFAffilieSoumiLpp;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppConteneur;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppConteneur.Salarie;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppManager;
import globaz.naos.db.controleLpp.AFLineCi;
import globaz.naos.db.controleLpp.AFLineCiManager;
import globaz.naos.db.controleLpp.AFSuiviCaisseForControleLpp;
import globaz.naos.db.controleLpp.AFSuiviCaisseForControleLppManager;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.listes.excel.AFXmlmlMappingSoumisLpp;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.util.AFUtil;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.db.tiers.TIRole;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import globaz.webavs.common.op.CommonExcelDocumentParser;
import globaz.webavs.common.op.CommonExcelFilterNotSupportedException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;

/**
 * Processus de gestion LPP. Permet de rechercher suivant l'année donnée, tous les affiliés sans LPP active et qui ont
 * des salariés qui doivent être soumis a la LPP
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFControleLppAnnuelProcess extends BProcess {

    private static final long serialVersionUID = 6612166262485600549L;
    private static final String NUM_INFOROM_CAS_COUMIS = "0280CAF";
    private static final String NUM_INFOROM_NAME_SPACE = "headerNumInforom";
    private static final String LISTE_CAS_REJETES = "listeSoumisLpp.xml";

    private String anneeDebut;
    private String anneeFin;

    private int anneeCourante;
    private AFAffilieSoumiLppConteneur casRejetes;
    private AFAffilieSoumiLppConteneur casSoumis;

    private AFAffilieSoumiLppConteneur casRejetesFinal;
    private AFAffilieSoumiLppConteneur casSoumisFinal;

    private StringBuilder corpsMessage;

    // true = simulation
    private Boolean modeControle = true;
    private int nbCasRejetes;
    private int nbCasSoumis;
    private double seuilAnnuel;
    private double seuilMensuel;
    private String typeAdresse;

    private String fileName;

    public AFControleLppAnnuelProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            // Initialisation des variables
            initialisation();

            // boucle sur chaque année
            int anneeStart = Integer.parseInt(getAnneeDebut());
            int anneeEnd = Integer.parseInt(getAnneeFin());

            if (!fileName.isEmpty() && !modeControle) {
                // On va réinjecter les données via le fichier renseigné
                reinjectionDonnees();

                // On gére la gestion de suivi
                gestionSuivisLpp();

                // Gestion de l'abort
                if (isAborted()) {
                    return false;
                }

                casRejetesFinal.addAllSalarie(casRejetes.getMapAffilie());
                casSoumisFinal.addAllSalarie(casSoumis.getMapAffilie());
                casRejetes.clear();
                casSoumis.clear();

                // Etape terminé, on incremente le compteur
                incProgressCounter();

            } else {
                for (int anneeControle = anneeStart; anneeControle <= anneeEnd; anneeControle++) {

                    // Détermination des seuils mensuel
                    seuilAnnuel = Double.parseDouble(getSeuilByParameterCode(anneeControle));
                    seuilMensuel = seuilAnnuel / 12.0;

                    // 1. Création d'un conteneur pour regrouper les salariés par affilié
                    creationDonnees(anneeControle);

                    // Gestion de l'abort
                    if (isAborted()) {
                        return false;
                    }

                    // 2. Application des régles de soumission a la LPP
                    // => On ne garde que les NSS qui seront dans la liste pour le courrier
                    // => création d'un conteneur avec les cas rejetés
                    applicationReglesSoumission(anneeControle);

                    // Gestion de l'abort
                    if (isAborted()) {
                        return false;
                    }

                    // 3. On gére la gestion de suivi
                    gestionSuivisLpp();

                    // Gestion de l'abort
                    if (isAborted()) {
                        return false;
                    }

                    casRejetesFinal.addAllSalarie(casRejetes.getMapAffilie());
                    casSoumisFinal.addAllSalarie(casSoumis.getMapAffilie());
                    casRejetes.clear();
                    casSoumis.clear();
                }

                // 4. Création des listes de résultat pour le mail
                creationListes();
            }

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_CONTROLE_LPP_ANNUEL_ERREUR"));

            String messageInformation = "Annees du controle : " + getAnneeDebut() + " à " + getAnneeFin() + "\n";
            messageInformation += "modeControle : " + isModeControleSimulation() + "\n";
            messageInformation += AFUtil.stack2string(e);

            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
        if (StringUtils.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_EMAIL_CONTROLE_LPP"));
        }

        if (StringUtils.isEmpty(getAnneeDebut())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE_DEBUT_CONTROLE_LPP"));
        }

        if (StringUtils.isEmpty(getAnneeFin())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE_FIN_CONTROLE_LPP"));
        }

        if (!StringUtils.isEmpty(getAnneeDebut()) && !StringUtils.isEmpty(getAnneeFin())
                && JADate.getYear(getAnneeFin()).intValue() < JADate.getYear(getAnneeDebut()).intValue()) {
            String label = getSession().getLabel("VAL_ANNEE_FIN_PLUS_PETIT_ANNEE_DEBUT_CONTROLE_LPP");
            this._addError(getTransaction(), label);
        }
    }

    /**
     * Application des régles de soumission
     * 
     * @throws Exception
     */
    private void applicationReglesSoumission(int anneeControle) throws Exception {

        setProcessDescription("Application des règles");
        String[] listAffilie = casSoumis.getTableauAffilie();

        // Parcours de tous les affiliés
        for (String idAffSoumis : listAffilie) {
            Salarie[] listSalarie = casSoumis.getTableauSalarieForAffilie(idAffSoumis);

            // Prévient les listes vides
            if (listSalarie == null) {
                continue;
            }

            // Parcours de tous les salariés de l'affilié
            for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                boolean salarieSoumis = true;

                // 1.Controle date <18 ans et retraite
                salarieSoumis = testAgeIsSoumis(anneeControle, idAffSoumis, sal);

                // 2. Controle + de 3 mois de suite
                // => si pas 3 mois verif les années avant et apres
                if (salarieSoumis) {
                    salarieSoumis = testDureeTravail(anneeControle, idAffSoumis, sal);
                }

                // 3. Test des seuils
                if (salarieSoumis) {
                    salarieSoumis = testSeuils(idAffSoumis, sal);
                }

                // Si les tests ont indiqués qu'il était pas soumis
                // On l'enleve de la liste
                if (!salarieSoumis) {
                    casSoumis.removeSalarie(idAffSoumis, sal);
                }
            }

            if ((casSoumis.getListSalarieForAffilie(idAffSoumis) == null)
                    || (casSoumis.getListSalarieForAffilie(idAffSoumis).isEmpty())) {
                casSoumis.removeAffilie(idAffSoumis);
            }
        }

        // Etape terminé, on incremente le compteur
        incProgressCounter();
    }

    private int createDocument(AFAffilieSoumiLppConteneur conteneurList, String nomDoc, String titreDoc,
            String numInforom) throws Exception {
        CommonExcelmlContainer container = AFXmlmlMappingSoumisLpp.loadResults(conteneurList, this, titreDoc,
                numInforom);

        Collection<String> ligne = container.getFieldValues(IAFListeColumns.NUM_AFFILIE);
        String docPath = AFExcelmlUtils.createDocumentExcel(AFControleLppAnnuelProcess.LISTE_CAS_REJETES, numInforom
                + "_" + nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(numInforom);
        this.registerAttachedDocument(docInfo, docPath);
        if (ligne != null) {
            return ligne.size();
        }

        return 0;
    }

    /**
     * Liste des cas qui sont rejetés
     * 
     * @return
     * @throws Exception
     */
    private void createDocumentCasRejetes() throws Exception {
        String titreDoc = getSession().getLabel("TITRE_LISTE_LPP_CAS_REJETES");
        String numInforom = "0279CAF";
        String nomDoc = getSession().getLabel("LISTE_LPP_CAS_REJETES");
        nbCasRejetes = createDocument(casRejetesFinal, nomDoc, titreDoc, numInforom);
    }

    /**
     * Création de la liste des cas soumis
     * 
     * @return
     * @throws Exception
     */
    private void createDocumentCasSoumis() throws Exception {
        String titreDoc = getSession().getLabel("TITRE_LISTE_LPP_CAS_SOUMIS");
        String numInforom = NUM_INFOROM_CAS_COUMIS;
        String nomDoc = getSession().getLabel("LISTE_LPP_CAS_SOUMIS");
        nbCasSoumis = createDocument(casSoumisFinal, nomDoc, titreDoc, numInforom);
    }

    /***
     * Contrôle du fichier reçu et injection des données.
     */
    private boolean reinjectionDonnees() throws Exception {
        boolean status = true;
        CommonExcelDataContainer container = null;

        String path = Jade.getInstance().getHomeDir() + "work/" + getFileName();

        // copy du fichier dans le répertoire work pour le traiter
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(), path);

        // parse du document, retourne un container avec toutes les entrées et
        // valeurs trouvées dans le document
        try {
            container = CommonExcelDocumentParser.parseWorkBook(AFExcelmlUtils.loadPath(path));

        } catch (CommonExcelFilterNotSupportedException e) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION_FILTRE"));
            String messageInformation = "Unsupported filtered Excel file injection throw exception in Parser due to file :"
                    + getFileName() + "\n";
            messageInformation += CEUtils.stack2string(e);
            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());
            status = false;
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_FICHIER_REINJECTION"));

            String messageInformation = "Nom du fichier : " + getFileName() + "\n";
            messageInformation += CEUtils.stack2string(e);

            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        if (status) {
            String numInforom = container.getHeaderValue(NUM_INFOROM_NAME_SPACE);

            if (JadeStringUtil.isEmpty(numInforom)) {
                getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_FOUND"));
                AFUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("NUM_INFOROM_NOT_FOUND"), this
                        .getClass().getName());
                return false;
            } else if (!(numInforom.equals(NUM_INFOROM_CAS_COUMIS))) {
                getTransaction().addErrors(getSession().getLabel("NUM_INFOROM_NOT_IMPLEMENTED"));
                AFUtil.addMailInformationsError(getMemoryLog(), getSession().getLabel("NUM_INFOROM_NOT_IMPLEMENTED"),
                        this.getClass().getName());
                return false;
            }

            Iterator<CommonLine> lines = container.returnLinesIterator();

            // On itère sur chaque ligne
            while (lines.hasNext()) {
                CommonLine line = lines.next();
                HashMap<String, String> lineMap = line.returnLineHashMap();

                String numeroAffilie = returnValeurHashMapWithNumAffilie(IAFListeColumns.NUM_AFFILIE, lineMap,
                        getTransaction(), "");

                String nom = returnValeurHashMapWithNumAffilie(IAFListeColumns.NOM, lineMap, getTransaction(),
                        numeroAffilie);
                String motif = returnValeurHashMapWithNumAffilie(IAFListeColumns.MOTIF, lineMap, getTransaction(),
                        numeroAffilie);
                String nss = returnValeurHashMapWithNumAffilie(IAFListeColumns.NSS, lineMap, getTransaction(),
                        numeroAffilie);
                String sexe = returnValeurHashMapWithNumAffilie(IAFListeColumns.SEXE, lineMap, getTransaction(),
                        numeroAffilie);
                String dateNaissance = returnValeurHashMapWithNumAffilie(IAFListeColumns.DATE_NAISSANCE, lineMap,
                        getTransaction(), numeroAffilie);
                String pediode = returnValeurHashMapWithNumAffilie(IAFListeColumns.PERIODE_TRAVAIL, lineMap,
                        getTransaction(), numeroAffilie);
                String annee = returnValeurHashMapWithNumAffilie(IAFListeColumns.ANNEE, lineMap, getTransaction(),
                        numeroAffilie);
                String montant = returnValeurHashMapWithNumAffilie(IAFListeColumns.MONTANT, lineMap, getTransaction(),
                        numeroAffilie);

                AFAffiliation affi = AFAffiliationServices.getAffiliationParitaireByNumero(numeroAffilie, annee,
                        getSession());

                casSoumis.addSalarieFromReinjection(affi.getIdTiers(), affi.getAffiliationId(), numeroAffilie, motif,
                        nom, sexe, nss, pediode.split("-")[0], pediode.split("-")[1], Integer.parseInt(annee),
                        dateNaissance, montant);
            }
        }

        // Etape terminé, on incremente le compteur
        incProgressCounter();

        return status;
    }

    /***
     * Méthode qui retourne la valeur choisie dans la hashmap en fonction du numéro d'affilié.
     * 
     * @param valeur
     * @param lineMap
     * @param transaction
     * @param numAffilie
     * @return
     */
    private static String returnValeurHashMapWithNumAffilie(final String valeur, final HashMap<String, String> lineMap,
            final BTransaction transaction, final String numAffilie) {
        String valeurRetour = "";

        if (lineMap.containsKey(valeur)) {
            valeurRetour = lineMap.get(valeur);
        }

        return valeurRetour;
    }

    /**
     * Récupération des données d'entrée. Recherche les cas potentiellement soumis a une caisse LPP
     * 
     * @throws Exception
     */
    private void creationDonnees(int forAnnee) throws Exception {

        setProcessDescription("Génération des données");

        // Récupération des suivis
        AFSuiviCaisseForControleLppManager mSuivi = new AFSuiviCaisseForControleLppManager();
        mSuivi.setForAnnee(forAnnee);
        mSuivi.setSession(getSession());
        mSuivi.find(getTransaction(), BManager.SIZE_NOLIMIT);

        List<AFSuiviCaisseForControleLpp> objSuiviCaisse = mSuivi.toList();

        // -------------
        // Récupération de la liste des affiliés potentiellement soumis
        // et qui n'ont pas de caisse LPP
        // -------------
        AFAffilieSoumiLppManager manager = new AFAffilieSoumiLppManager();
        manager.setSession(getSession());
        manager.setForAnnee(forAnnee);
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        List<AFAffilieSoumiLpp> objListAf = manager.toList();

        // Pour chaque ligne de la requete, on les regroupe par affilié
        for (AFAffilieSoumiLpp affSoumis : objListAf) {

            boolean isCouvertParUneCaisse = false;

            // Test si couvert par un suivi caisse
            for (AFSuiviCaisseForControleLpp caisseLpp : objSuiviCaisse) {
                if (caisseLpp.getIdAffilie().equals(affSoumis.getIdAffilie())) {

                    isCouvertParUneCaisse = testIsCouvert(forAnnee, affSoumis.getMoisDebut(), affSoumis.getMoisFin(),
                            caisseLpp.getDateDebut(), caisseLpp.getDateFin());

                    // Si couvert par une caisse et pas de motif de non soumission
                    if (isCouvertParUneCaisse && "0".equals(caisseLpp.getMotifSuivi())) {
                        break;
                    } else if (isCouvertParUneCaisse) {
                        affSoumis.setMotifSuivi(caisseLpp.getMotifSuivi());
                        break;
                    }
                    isCouvertParUneCaisse = false;
                }

            }

            // Si couvert par une caisse, on passe au suivant.
            if (isCouvertParUneCaisse && JadeStringUtil.isEmpty(affSoumis.getMotifSuivi())) {
                continue;
            }

            // Si il l'affilié n'a pas de cotisation AVS, on le prend pas en compte
            // BUG:7202
            if (!hasCotisationAVS(affSoumis.getIdAffilie(), forAnnee)) {
                continue;
            }

            casSoumis.addSalarie(affSoumis.getIdTiers(), affSoumis.getIdAffilie(), affSoumis.getNumeroAffilie(),
                    getSession().getCodeLibelle(affSoumis.getMotifSuivi()), affSoumis.getNom(), affSoumis.getSexe(),
                    affSoumis.getNss(), affSoumis.getMoisDebut(), affSoumis.getMoisFin(), forAnnee,
                    affSoumis.getDateNaissance(), affSoumis.getGenreEcriture(), affSoumis.getIdCompteIndividuel(),
                    affSoumis.getMontant(), affSoumis.isExtourne(), affSoumis.getNivSecuAffilie(),
                    affSoumis.getNivSecuCI());

        }

        // Etape terminé, on incremente le compteur
        incProgressCounter();
    }

    static boolean testIsCouvert(int forAnnee, String moisDebut, String moisFin, String dateDebutSuivi,
            String dateFinSuivi) {

        // Contruction de la période de suivi
        String debutPeriodeSuivi = "";
        if (JadeStringUtil.isBlankOrZero(dateDebutSuivi)) {
            debutPeriodeSuivi = "01." + forAnnee;
        } else {
            Date d = new Date(dateDebutSuivi);
            debutPeriodeSuivi = d.getSwissMonthValue();
        }

        String finPeriodeSuivi = "";
        if (JadeStringUtil.isBlankOrZero(dateFinSuivi)) {
            finPeriodeSuivi = "12." + forAnnee;
        } else {
            Date d = new Date(dateFinSuivi);
            finPeriodeSuivi = d.getSwissMonthValue();
        }
        Periode periodeDuSuivi = new Periode(debutPeriodeSuivi, finPeriodeSuivi);

        // Cosntruction de la périod de test
        String debutPeriodeTest = "";
        if (JadeStringUtil.isBlankOrZero(moisDebut)) {
            debutPeriodeTest = "01." + forAnnee;
        } else {
            debutPeriodeTest = StringUtils.leftPad(moisDebut, 2, "0") + "." + forAnnee;
        }
        String finPeriodeTest = "";
        if (JadeStringUtil.isBlankOrZero(moisFin)) {
            finPeriodeTest = "12." + forAnnee;
        } else {
            finPeriodeTest = StringUtils.leftPad(moisFin, 2, "0") + "." + forAnnee;
        }
        Periode periodeDeTest = new Periode(debutPeriodeTest, finPeriodeTest);

        // Comparaison des périodes
        ComparaisonDePeriode comp = periodeDuSuivi.comparerChevauchementMois(periodeDeTest);

        if (comp == ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT) {
            return true;
        }

        return false;
    }

    /**
     * Création des listes de retour qui informe l'utilisateur
     * 
     * @throws Exception
     */
    private void creationListes() throws Exception {

        setProcessDescription("Création des listes");

        // Cröation de la liste des cas rejetes
        createDocumentCasRejetes();

        // Cröation de la liste des cas soumis
        createDocumentCasSoumis();

        // Cröation du corps du message
        corpsMessage.append(getSession().getLabel("ANNEES")).append(" : ").append(getAnneeDebut()).append(" - ")
                .append(getAnneeFin()).append("\n");
        corpsMessage.append(getSession().getLabel("MODE_CONTROLE")).append(" : ").append(isModeControleSimulation())
                .append("\n").append("\n\n");
        corpsMessage.append(getSession().getLabel("NBR_CAS_REJETES")).append(nbCasRejetes).append("\n");
        corpsMessage.append(getSession().getLabel("NBR_CAS_SOUMIS")).append(nbCasSoumis).append("\n");

        // On zip le tout
        String nomZip = getSession().getLabel("NOM_ZIP_LPP");

        ZipOutputStream zipOutput = null;
        try {
            String zipFilename = JadeFilenameUtil.addOrReplaceFilenameSuffixUID(Jade.getInstance().getPersistenceDir()
                    + nomZip + ".zip");
            zipOutput = new ZipOutputStream(new FileOutputStream(zipFilename));
            for (Object obj : getAttachedDocuments()) {
                JadePublishDocument document = (JadePublishDocument) obj;
                FileInputStream input = null;
                try {
                    input = new FileInputStream(document.getDocumentLocation());
                    byte[] contents = JadeZipUtil.load(input);
                    ZipEntry zipEntry = new ZipEntry(JadeFilenameUtil.extractFilename(document.getDocumentLocation()));
                    zipOutput.putNextEntry(zipEntry);
                    zipOutput.write(contents, 0, contents.length);
                    zipOutput.flush();
                    zipOutput.closeEntry();
                } finally {
                    input.close();
                }
            }
            getAttachedDocuments().clear();
            JadePublishDocumentInfo documentInfo = createDocumentInfo();
            documentInfo.setDocumentType("");
            documentInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(documentInfo, zipFilename);
            zipOutput.flush();
        } finally {
            zipOutput.close();
        }

        // Etape terminé, on incremente le compteur
        incProgressCounter();
    }

    /**
     * Génération du processus de suivi Lpp
     * 
     * @param numAffilie
     * @param idAffilie
     * @throws Exception
     */
    private void generationSuiviLpp(String idTiers, String numAffilie, String idAffilie, int annee) throws Exception {
        // prépare les données pour l'envoi
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, numAffilie);
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, AFApplication.DEFAULT_APPLICATION_NAOS);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, idAffilie);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, idTiers);
        params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, String.valueOf(annee));

        // execute le process de génération
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(getSession());
        // gen.setEMailAddress(getEMailAddress());
        gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_ANNUEL_LPP);
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(Boolean.TRUE);

        gen.executeProcess();
    }

    /**
     * Permet de créer la gestion des suivis pour chaque affilié
     * 
     * @throws Exception
     */
    private void gestionSuivisLpp() throws Exception {

        setProcessDescription("Génération des suivis");

        // Pour chaque affilié
        // -------------------
        String[] listAffilie = casSoumis.getTableauAffilie();

        // Parcours de tous les affiliés
        for (String idAffSoumis : listAffilie) {
            Salarie[] listSalarie = casSoumis.getTableauSalarieForAffilie(idAffSoumis);

            // Prévient les listes vides
            if (listSalarie == null) {
                continue;
            }

            // 1. controle si pas déjà un suivi
            if (isDejaJournalise(getSession(), idAffSoumis, (listSalarie[0]).getNumeroAffilie())) {

                // Parcours de tous les salariés de l'affilié
                for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                    sal.setSuivi(true);
                }

                continue;
            }
            // 2. Si pas de suivi, on en crée un .

            if (!isModeControleSimulation()) {
                // Si pas en simulation
                generationSuiviLpp(listSalarie[0].getIdTiers(), listSalarie[0].getNumeroAffilie(), idAffSoumis,
                        listSalarie[0].getAnnee());

                // Parcours de tous les salariés de l'affilié
                for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                    sal.setSuivi(true);
                }
            }

        }

        // Etape terminé, on incremente le compteur
        incProgressCounter();
    }

    @Override
    protected String getEMailObject() {

        String emailObject;

        if (fileName.isEmpty()) {
            // Information sur l'année de traitement
            String anneeTraitement = " (" + getAnneeDebut() + " - " + getAnneeFin() + ")";

            // Information sur le type de traitement
            String simulation = "";
            if (isModeControleSimulation()) {
                simulation = (getSession().getLabel("CONTROLE_LPP_SIMULATION") + " - ");
            }

            // Construction de l'object
            if (isAborted()) {
                emailObject = simulation + getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_ABORTED")
                        + anneeTraitement;
            } else if (isOnError() || getSession().hasErrors()) {
                emailObject = simulation + getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_ERREUR")
                        + anneeTraitement;
            } else {
                emailObject = simulation + getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_OK") + anneeTraitement;
            }
        } else {
            emailObject = getSession().getLabel("IMPRESSION_CONTROLE_LPP_ANNUEL_OK") + " ("
                    + getSession().getLabel("MENU_REINJECTION_LISTE_EXCEL") + ")";
        }

        return emailObject;
    }

    public Boolean getModeControle() {
        return modeControle;
    }

    /**
     * Récupération du seuil des salaires par le biais des parametres
     * 
     * @return
     * @throws Exception
     */
    private String getSeuilByParameterCode(int annee) {

        FWFindParameterManager param = new FWFindParameterManager();
        param.setSession(getSession());
        param.setIdApplParametre(AFApplication.DEFAULT_APPLICATION_NAOS);
        param.setIdCodeSysteme("10800042");
        param.setIdCleDiffere("SEUILLPP");
        param.setIdActeurParametre("0");
        param.setPlageValDeParametre("0");
        param.setDateDebutValidite("01.01." + annee);
        try {
            param.find(getTransaction(), BManager.SIZE_USEDEFAULT);

        } catch (Exception e) {
            throw new CommonTechnicalException("Technical Exception, unabled to retrieve seuil annuel for " + annee, e);
        }

        if (param.size() > 0) {
            return ((FWFindParameter) param.getFirstEntity()).getValeurNumParametre();
        } else {
            throw new CommonTechnicalException("Unabled to retrieve seuil annuel for " + annee);
        }
    }

    @Override
    public String getSubjectDetail() {
        if ((getTransaction() != null) && getTransaction().hasErrors()) {
            return super.getSubjectDetail();
        }
        if ((getSession() != null) && getSession().hasErrors()) {
            return super.getSubjectDetail();
        }

        return corpsMessage.toString();

    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    /**
     * Methode permettant de controler si un affilié a une cotisation AVS pour l'année spécifié.
     * 
     * @param idAffilie
     * @param annee
     * @return
     * @throws HerculeException
     */
    private boolean hasCotisationAVS(String idAffilie, int annee) {

        if (JadeStringUtil.isEmpty(idAffilie)) {
            throw new IllegalArgumentException("Unabled to find cotisation AVS. idAffilie is null or empty");
        }

        if (annee == 0) {
            throw new IllegalArgumentException("Unabled to find cotisation AVS. annee is null or empty");
        }

        boolean hasCoti = false;

        AFCotisationManager manager = new AFCotisationManager();
        manager.setSession(getSession());
        manager.setForTypeAssurance(globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        manager.setForAnneeActive(String.valueOf(annee));
        manager.setForAffiliationId(idAffilie);

        try {
            hasCoti = manager.getCount() > 0;
        } catch (Exception e) {
            throw new CommonTechnicalException("Unabled to retrieve cotisation avs (idAffilie = " + idAffilie
                    + " / annee = " + annee + ")", e);
        }

        return hasCoti;
    }

    /**
     * Initialisation des variables
     * 
     * @throws Exception
     * @throws NumberFormatException
     */
    private void initialisation() {
        // Initialisation de la progresse bar
        setProgressScaleValue(5);

        anneeCourante = CEUtils.transformeStringToInt(CEUtils.giveAnneeCourante());
        casSoumis = new AFAffilieSoumiLppConteneur();
        casRejetes = new AFAffilieSoumiLppConteneur();
        casSoumisFinal = new AFAffilieSoumiLppConteneur();
        casRejetesFinal = new AFAffilieSoumiLppConteneur();
        corpsMessage = new StringBuilder("");

        // Etape terminé, on incremente le compteur
        incProgressCounter();
    }

    /**
     * Methode permettant de regarder si il n'y pas déjà un suivi LPP en cours
     * 
     * @param session
     * @param idAffiliation
     * @param numAffilie
     * @param annee
     * @return
     * @throws Exception
     */
    public boolean isDejaJournalise(BSession session, String idAffiliation, String numAffilie) throws Exception {

        // On sette les critères qui font que l'envoi est unique
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, numAffilie);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                AFApplication.DEFAULT_APPLICATION_NAOS);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, idAffiliation);

        LUJournalListViewBean viewBean = new LUJournalListViewBean();
        viewBean.setSession(session);
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_DEBUT_SUIVI_ANNUEL_LPP);
        viewBean.find(getTransaction(), BManager.SIZE_USEDEFAULT);

        // Si le viewBean retourne un enregistrement c'est que l'envoi a déjà
        // été journalisé donc on retourne true
        if (!viewBean.isEmpty()) {
            List<LUJournalViewBean> lst = viewBean.toList();

            // On regarde si il y a une journalisation sans date de réception.
            for (LUJournalViewBean luJournalViewBean : lst) {
                if (StringUtils.isEmpty(luJournalViewBean.getDateReception())) {
                    return true;
                }
            }
        }

        return false;

    }

    public boolean isModeControleSimulation() {
        return modeControle;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setModeControle(Boolean modeControle) {
        this.modeControle = modeControle;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    /**
     * Régle sur l'age légal
     * 
     * @param sal
     * @return
     * @throws JAException
     */
    private boolean testAgeIsSoumis(int anneeControle, String idAffilie, AFAffilieSoumiLppConteneur.Salarie sal) {

        // Test si date de naissance existante et sexe
        if (JadeStringUtil.isEmpty(sal.getDateNaissance()) || JadeStringUtil.isEmpty(sal.getSexe())
                || !Date.isValid(sal.getDateNaissance())) {
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

        // Test si il a l'âge légal
        // Try / catch pour d'éventuelle problème, (genre une date 00.00.1958)
        try {
            return testCalculAgeIsSoumis(anneeControle, sal.getDateNaissance(), sal.getSexe(),
                    StringUtils.leftPad(sal.getMoisDebut(), 2, "0"));
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage() + " // Nss : " + sal.getNss());
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

    }

    static boolean testCalculAgeIsSoumis(int anneeControle, String dNaissance, String sexe, String moisDebutSalaire) {
        Date dateNaissance = new Date(dNaissance);

        // Test si + de 17 ans
        if ((anneeControle - dateNaissance.getYear()) >= 18) {

            // Il ne doit pas être en retraite
            if (!CIUtil.isRetraite(dateNaissance.getYear(), sexe, anneeControle)) {
                return true;

            } else if (CIUtil.isAnneeRetraite(dateNaissance.getYear(), sexe, anneeControle)) {
                // Si année de retraite, on regarde si la période est avant le mois de retraite
                Date dateAControler = new Date(anneeControle + moisDebutSalaire + "01");
                Date dateAComparer = new Date(anneeControle + dateNaissance.getMois() + dateNaissance.getJour());
                if (dateAControler.before(dateAComparer)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Permet de regarder les mois travaillés avant ou aprés une année donné
     * 
     * @param autreAnnee
     * @param anneeInf
     * @param sal
     * @param nbMois
     * @return
     * @throws Exception
     */
    private boolean testAutreAnneeDureeTravail(String autreAnnee, boolean anneeInf, String idAffilie, Salarie sal,
            int nbMois) throws Exception {

        AFLineCiManager man = new AFLineCiManager();
        man.setSession(getSession());
        man.setForAnnee(autreAnnee);
        man.setForIdAffilie(idAffilie);
        man.setForIdCompteIndividuel(sal.getIdCompteIndividuel());
        man.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < man.size(); i++) {
            AFLineCi ecri = (AFLineCi) man.getEntity(i);
            int moisDebEcri = CEUtils.transformeStringToInt(ecri.getMoisDebut());
            int moisFinEcri = CEUtils.transformeStringToInt(ecri.getMoisFin());

            // Si les mois sont pas dans la continuité, il est pas soumis
            if (anneeInf && (moisFinEcri != 12)) {
                return false;
            }
            if (!anneeInf && (moisDebEcri != 1)) {
                return false;
            }

            // test si il a plus de 3 mois
            if (((moisFinEcri - moisDebEcri + 1) + nbMois) > 3) {
                return true;
            }
        }

        return false;
    }

    /**
     * Régle sur la durée du travail
     * 
     * @param sal
     * @return
     * @throws Exception
     */
    private boolean testDureeTravail(int anneeControle, String idAffilie, Salarie sal) throws Exception {

        // Test si periode de travail existante
        if (JadeStringUtil.isEmpty(sal.getMoisDebut()) || JadeStringUtil.isEmpty(sal.getMoisFin())) {
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

        int moisDeb = CEUtils.transformeStringToInt(sal.getMoisDebut());
        int moisFin = CEUtils.transformeStringToInt(sal.getMoisFin());

        // si la durée du travail est inférieur ou égal a 3 mois
        if ((moisFin - moisDeb + 1) <= 3) {

            // On part du principe qu'il est valide, on regarde les années avant et apres
            boolean valide = true;

            // Si le mois de début n'est pas janvier ou le mois de fin pas décembre
            // Il n'est pas soumis a la lpp
            if ((moisDeb > 1) && (moisFin < 12)) {
                valide = false;
            }

            // Si le mois de début = janvier
            if (valide && (moisDeb == 1)) {
                // on regarde l'année précédente pour savoir si il a travaillé octobre, novembre et décembre
                valide = testAutreAnneeDureeTravail(Integer.toString(anneeControle - 1), true, idAffilie, sal, (moisFin
                        - moisDeb + 1));
            }

            // Si le mois de fin est le mois de décembre
            if (valide && (moisFin == 12)) {
                // On regarde l'année suivante
                // Pour autant que ce ne soit pas l'année courante + 1
                if (!(anneeControle >= anneeCourante)) {
                    valide = testAutreAnneeDureeTravail(Integer.toString(anneeControle + 1), false, idAffilie, sal,
                            (moisFin - moisDeb + 1));
                }
            }

            return valide;
        }

        // La durée du travail dépasse 3 mois, il est sujet a la LPP
        return true;
    }

    /**
     * Regle sur les seuils annuel et mensuel du salaire
     * 
     * @param sal
     * @return
     */
    private boolean testSeuils(String idAffilie, Salarie sal) {

        // Test si periode de travail existante
        if (JadeStringUtil.isEmpty(sal.getMontant())) {
            casRejetes.addSalarie(idAffilie, sal);
            return false;
        }

        double salaire = Double.parseDouble(sal.getMontant());

        // Test du seuil annuel
        if (salaire > seuilAnnuel) {
            // Il dépasse le seuil annuel
            return true;
        }

        int moisDeb = CEUtils.transformeStringToInt(sal.getMoisDebut());
        int moisFin = CEUtils.transformeStringToInt(sal.getMoisFin());
        // +1 car le mois de début compte
        double nbMois = moisFin - moisDeb + 1;
        double salaireMensuel = salaire / nbMois;

        // Test du seuil mensuel
        if (salaireMensuel >= seuilMensuel) {
            // Il dépasse le seuil mensuel
            return true;
        }

        // Il ne dépasse pas la règle des seuils
        return false;

    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
