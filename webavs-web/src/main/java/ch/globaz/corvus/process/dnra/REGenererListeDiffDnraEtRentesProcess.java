package ch.globaz.corvus.process.dnra;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordeeManager;
import globaz.corvus.db.dnra.REFichierDnraJournalierTraite;
import globaz.corvus.db.dnra.REFichierDnraJournalierTraiteManager;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.properties.REProperties;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.fs.message.JadeFsFileInfo;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.url.JadeUrl;
import globaz.jade.url.JadeUrlMalformedException;
import globaz.pavo.db.upidaily.CIUpiDailyProcess;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.codesystem.CodeSystemeResolver;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.sql.ConverterDb;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.common.sql.converters.DateRenteConverter;
import ch.globaz.common.sql.converters.EtatCivilConverter;
import ch.globaz.common.sql.converters.PaysConverter;
import ch.globaz.common.sql.converters.SexeConverter;
import ch.globaz.corvus.process.REAbstractJadeJob;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.loader.PaysLoader;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.HeaderFooter;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import com.sun.star.lang.IllegalArgumentException;

/**
 * Compare les données du NRA transmises via le fichier journalier (delta-quotidien) avec les données des rentes et
 * génère une liste recensant ces différences.
 * 
 * @author bjo
 * 
 */
public class REGenererListeDiffDnraEtRentesProcess extends REAbstractJadeJob {

    private static final long serialVersionUID = 7164797280525378834L;

    @Override
    public String getDescription() {
        return "Compare les données du NRA transmises via le fichier journalier avec les données des rentes et génère une liste recensant ces différences.";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {
        List<File> fichiersMutationsATraiterList;

        // charger la liste des adresses email
        List<String> mailsList = new ArrayList<String>();
        try {
            mailsList = loadMailsDestinataires();
        } catch (PropertiesException e) {
            mailsList.add(getSession().getUserEMail());
        }

        JadeLogger.info(getName(), "Début du traitement de génération des listes de différences");
        try {
            // téléchargement du ou des fichiers à traiter
            fichiersMutationsATraiterList = telechargerFichiersMutations();

            // traitement de chaque fichier et génération de la liste
            for (File fichierMutation : fichiersMutationsATraiterList) {
                JadeLogger.info(getName(), "Traitement du fichier : " + fichierMutation.getName() + " "
                        + fichierMutation.getAbsolutePath());

                // parsing et mapping dans la structure d'objet
                PaysLoader paysLoader = new PaysLoader();
                MutationParser mutationParser = new MutationParser();
                MutationsContainer mutationsContainer = mutationParser.parsFile(fichierMutation.getAbsolutePath(),
                        paysLoader);
                mutationsContainer.setFichierMutationName(fichierMutation.getAbsolutePath());

                CodeSystemeResolver codeSystemeResolver = new CodeSystemeResolver(getSession().getIdLangueISO());
                codeSystemeResolver.addAllAndSerach(Sexe.getCodeFamille(), EtatCivil.getCodeFamille());

                // recherche des infos sur les tiers relatives aux mutations qui ont un nss actif ou inactivé
                Set<InfoTiers> listInfosTiersNssActifInactif = findInfosTiers(
                        mutationsContainer.extractNssActifEtInactif(), paysLoader.getMapPaysByCodeCentrale());
                // recherche des infos sur les tiers relatives aux mutations qui on un nss invalidé
                Set<InfoTiers> listInfosTiersNssInvalide = findInfosTiers(mutationsContainer.extractNssInvalide(),
                        paysLoader.getMapPaysByCodeCentrale());

                // filtrer les infoTiers
                supprimerInfoTiersNonDesires(listInfosTiersNssActifInactif);
                supprimerInfoTiersNonDesires(listInfosTiersNssInvalide);

                consolideEtatCivilDepuisLaSituationFamilliale(fichierMutation, listInfosTiersNssActifInactif);

                // identification des différences entre les mutations annoncées et les données DB
                DifferenceFinder differenceFinder = new DifferenceFinder(new Locale(getSession().getIdLangueISO()),
                        codeSystemeResolver);

                List<DifferenceTrouvee> differenceTrouvees = differenceFinder.findAllDifference(
                        mutationsContainer.getList(), listInfosTiersNssActifInactif, listInfosTiersNssInvalide);

                // génération de la liste au format XLS
                String generatedXlsFilePath = generateXls(differenceTrouvees, getSession(), fichierMutation.getName());
                JadeLogger.info(getName(), "le fichier XLS suivant a été généré avec succès" + generatedXlsFilePath);

                // flaguer le fichier
                marquerFichierDnraCommeTraite(fichierMutation.getName(), differenceTrouvees.size());

                // suppression du fichier journalier
                JadeFsFacade.delete(fichierMutation.getAbsolutePath());

                Integer nbDifference = differenceTrouvees.size();
                // envoyer l'email
                sendMail(mailsList, generatedXlsFilePath, mutationParser.getErrors(), nbDifference);
            }
        } catch (Exception e) {
            JadeLogger.error(getName(), "une erreur est survenue lors du traitement");
            ProcessMailUtils.sendMailError(mailsList, e, this, "le traitement ne s'est pas terminé correctement",
                    getTransaction());
        }

        JadeLogger.info(getName(), "Fin du traitement de génération des listes de différences");

    }

    private void consolideEtatCivilDepuisLaSituationFamilliale(File fichierMutation,
            Set<InfoTiers> listInfosTiersNssInvalide) {
        ch.globaz.common.domaine.Date dateFile = resolveDateFile(fichierMutation);

        for (InfoTiers infoTiers : listInfosTiersNssInvalide) {
            infoTiers.setEtatCivil(findEtatCivil(infoTiers.getIdTiers(), dateFile));
        }
    }

    private ch.globaz.common.domaine.Date resolveDateFile(File fichierMutation) {
        ch.globaz.common.domaine.Date date = new ch.globaz.common.domaine.Date(fichierMutation.getName()
                .substring(fichierMutation.getName().indexOf("_") + 1).substring(0, 8));
        return date;
    }

    /**
     * Supprime les tiers qu'on ne veut pas prendre en compte pour la comparaison
     * 
     * @param listInfosTiers
     * @throws Exception
     */
    private void supprimerInfoTiersNonDesires(Set<InfoTiers> listInfosTiers) throws Exception {
        Iterator<InfoTiers> infoTiersIterator = listInfosTiers.iterator();
        while (infoTiersIterator.hasNext()) {
            InfoTiers infoTiers = infoTiersIterator.next();
            if (!hasDemandeOrRenteActiveForFamilly(infoTiers.getIdTiers(), getSession())) {
                infoTiersIterator.remove();
            }
        }
    }

    private void sendMail(List<String> mailsList, String joinFilePath, Map<Exception, String> mapErrors,
            Integer nbDifference) throws Exception {
        // ajout de la pièce jointe
        List<String> joinsFilesPathsList = new ArrayList<String>();
        joinsFilesPathsList.add(joinFilePath);

        StringBuilder errors = new StringBuilder();
        String body = "";

        // sujet et corps du mail
        String subject = FWMessageFormat.format(getSession().getLabel("LIST_CORVUS_DIFFERENCE_MAIL_SUBJECT_SUCCESS"),
                nbDifference);

        if (!mapErrors.isEmpty()) {

            subject = FWMessageFormat.format(getSession().getLabel("LIST_CORVUS_DIFFERENCE_MAIL_SUBJECT_SUCCESS"),
                    nbDifference);

            body = getSession().getLabel("LIST_CORVUS_DIFFERENCE_MAIL_BODY_ERREURS_DETECTEES") + "\n\n";

            for (Entry<Exception, String> entry : mapErrors.entrySet()) {
                errors.append("   ").append(entry.getKey().getMessage()).append(" ")
                        .append(getSession().getLabel("LIST_CORVUS_DIFFERENCE_MAIL_BODY_ERREURS_MUTATION"))
                        .append(": ");
                errors.append(entry.getValue().trim()).append("\n");
            }
        }

        if (!mapErrors.isEmpty()) {
            body = body + errors + "\n\n";
        }

        body = body + getSession().getLabel("LIST_CORVUS_DIFFERENCE_MAIL_BODY_SUCCESS");

        // envoi
        ProcessMailUtils.sendMail(mailsList, subject, body, joinsFilesPathsList);
    }

    private List<String> loadMailsDestinataires() throws PropertiesException {
        String emails = null;
        emails = REProperties.DNRA_PROCESS_EMAILS.getValue();
        List<String> mailsList = Arrays.asList(emails.split(","));
        return mailsList;
    }

    /**
     * Remonte l'état civile pour un tiers à une date donnée si la date est null on prend la date du jour.
     * 
     * @param listNss
     * @return
     */
    private EtatCivil findEtatCivil(String idTiers, ch.globaz.common.domaine.Date date) {

        try {
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(getSession());
            membre.setIdTiers(idTiers);
            membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);

            membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            membre.retrieve();
            if (membre.isNew()) {
                // On tente de récupérer le membre de famille pour le domaine
                // standard !!!
                membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                membre.retrieve();
            }
            String etatCivil = null;
            if (!membre.isNew()) {
                etatCivil = membre.getEtatCivil(date.getSwissValue());
            }
            Map<String, EtatCivil> mapSituationFamilliale = new HashMap<String, EtatCivil>() {
                {
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE, EtatCivil.CELIBATAIRE);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE, EtatCivil.MARIE);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE, EtatCivil.DIVORCE);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF, EtatCivil.VEUF);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT, EtatCivil.SEPARE_DE_FAIT);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT, EtatCivil.SEPARE);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE, EtatCivil.LPART);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES, EtatCivil.LPART_DIS_DECES);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_JUDICIAIREMENT, EtatCivil.LPART_DISSOUT);
                    put(ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT, EtatCivil.LPART_SEP_FAIT);
                }
            };

            if (mapSituationFamilliale.containsKey(etatCivil)) {
                return mapSituationFamilliale.get(etatCivil);
            } else {
                // Aucune relation trouvé
                return EtatCivil.CELIBATAIRE;
            }

        } catch (Exception e) {
            throw new java.lang.RuntimeException(e);
        }

    }

    /**
     * Remonte toutes les informations relatives à la liste de NSS passée en paramètre
     * 
     * @param listNss
     * @return
     */
    private Set<InfoTiers> findInfosTiers(Set<String> listNss, Map<String, Pays> mapPaysByCodeCentral) {
        List<InfoTiers> listInfosTiers = new ArrayList<InfoTiers>();
        List<List<String>> splittedList = QueryExecutor.splitBy1000(listNss);

        Set<ConverterDb<?>> converters = QueryExecutor.newSetConverter(new SexeConverter(), new DateRenteConverter(),
                new PaysConverter(mapPaysByCodeCentral), new EtatCivilConverter());
        for (List<String> list : splittedList) {
            StringBuilder sql = new StringBuilder();
            sql.append("select schema.TIPAVSP.HXNAVS as nss, schema.TITIERP.HNIPAY as codeNationalite, schema.TITIERP.HTLDE1 as nom, schema.TITIERP.HTLDU1 as nomUpper, ");
            sql.append("schema.TITIERP.HTLDE2 as prenom, schema.TITIERP.HTLDU2 as prenomUpper, schema.TIPERSP.HPDNAI as dateNaissance, schema.TIPERSP.HPDDEC as dateDeces, ");
            sql.append("schema.TITIERP.HNIPAY as pays, schema.TIPERSP.HPTSEX as sexe, schema.TITIERP.HTITIE as idTiers ");
            sql.append("from schema.TIPAVSP ");
            sql.append("inner join schema.TITIERP on schema.TITIERP.HTITIE = schema.TIPAVSP.HTITIE ");
            sql.append("inner join schema.TIPERSP on schema.TIPERSP.HTITIE = schema.TIPAVSP.HTITIE ");
            sql.append("where HXNAVS in (").append(QueryExecutor.forInString(list)).append(")");
            listInfosTiers.addAll(QueryExecutor.execute(sql.toString(), InfoTiers.class, converters));
        }
        final Set<InfoTiers> infosTiers = new HashSet<InfoTiers>();
        infosTiers.addAll(listInfosTiers);

        return infosTiers;
    }

    /**
     * Insert dans la DB le nom du fichier afin de l'identifier comme traité
     * 
     * @param nomFichier
     * @param nbMutationTrouvee
     * @throws Exception
     */
    private void marquerFichierDnraCommeTraite(String nomFichier, int nbMutationTrouvee) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            REFichierDnraJournalierTraite fichierDnra = new REFichierDnraJournalierTraite();
            fichierDnra.setNomFichierDnraJournalierTraite(nomFichier);
            fichierDnra.setNbMutationTrouvee(nbMutationTrouvee);
            fichierDnra.add(transaction);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            JadeLogger.error(this.getClass(), "impossible d'ajouter le fichier dans la DB : " + nomFichier);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * Se connecte au serveur définit par la propriété en DB "nraUpiServer" et de télécharge tous les
     * fichiers qui n'ont pas encore été traités (selon historique) par le process. Les fichiers sont déposés dans le
     * répertoire "shared" (jade.xml). La méthode retourne les paths de tous les fichiers téléchargés (trier par nom).
     * 
     * @return liste des paths sur les fichiers téléchargés.
     * @throws Exception
     * @throws IllegalArgumentException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     * @throws JadeUrlMalformedException
     */
    private List<File> telechargerFichiersMutations() throws Exception, IllegalArgumentException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException,
            JadeUrlMalformedException {
        List<File> fichiersMutationsATraiterList = new ArrayList<File>();

        // récupération de l'URI où sont stockés les fichiers dé-zippés et décryptés
        String fichiersMutationsDistantUri = getSessionPavo(getSession()).getApplication().getProperty("nraUpiServer");
        if (JadeStringUtil.isEmpty(fichiersMutationsDistantUri)) {
            throw new IllegalArgumentException("l'URI des fichiers de mutations NRA est vide");
        }
        // définition du répertoire local pour le téléchargement des fichiers
        String fichierMutationsLocauxDirectory = Jade.getInstance().getSharedDir();
        if (JadeStringUtil.isEmpty(fichierMutationsLocauxDirectory)) {
            throw new IllegalArgumentException(
                    "Le répertoire local nécessaire au téléchargement des fichiers n'est pas valide");
        }

        // normalisation du path en fonction du filesystem utilisé
        String fichiersMutationsDistantUriNormalized = JadeFilenameUtil.normalizePathRoot(fichiersMutationsDistantUri);
        JadeLogger.info(CIUpiDailyProcess.class, "Recherche des fichiers disponibles sur : "
                + fichiersMutationsDistantUriNormalized);

        // récupération de la liste des fichiers disponibles dans le répertoire distant
        List<JadeFsFileInfo> jadeFsFileInfoList = JadeFsFacade
                .getFolderChildrenInfo(fichiersMutationsDistantUriNormalized);
        if ((jadeFsFileInfoList == null) || (jadeFsFileInfoList.size() == 0)) {
            JadeLogger.info(CIUpiDailyProcess.class, "Pas de fichiers trouvés sur : "
                    + fichiersMutationsDistantUriNormalized);
            return fichiersMutationsATraiterList;
        }

        List<String> nomsFichiersDnraDejaTraites = recupererNomsFichiersDnraDejaTraites();

        // parcours de fichiers disponibles
        for (JadeFsFileInfo jadeFsFileInfo : jadeFsFileInfoList) {
            JadeUrl jadeUrlFichierDistant = new JadeUrl();
            jadeUrlFichierDistant.setUrl(jadeFsFileInfo.getUri());
            String nomFichierDistant = jadeUrlFichierDistant.getFile();
            if (!nomsFichiersDnraDejaTraites.contains(nomFichierDistant)) {
                String uriDest = fichierMutationsLocauxDirectory + nomFichierDistant;
                String uriSource = fichiersMutationsDistantUriNormalized + nomFichierDistant;
                JadeLogger.info(CIUpiDailyProcess.class, "Téléchargement du fichier : " + uriSource + "...");
                JadeFsFacade.copyFile(uriSource, uriDest);
                File fichierDest = new File(uriDest);
                fichiersMutationsATraiterList.add(fichierDest);
            }
        }

        // trier la collection
        Collections.sort(fichiersMutationsATraiterList);

        return fichiersMutationsATraiterList;
    }

    /**
     * récupère dans la table REDNRAJ les noms des fichiers qui ont déjà été traités par le process. Retourne une liste
     * vide si aucun cas.
     * 
     * @return
     * @throws Exception
     */
    private List<String> recupererNomsFichiersDnraDejaTraites() throws Exception {
        List<String> nomsFichiersDnraDejaTraites = new ArrayList<String>();

        // récupération des fichiers déjà traités par le process
        REFichierDnraJournalierTraiteManager fichierDnraJournalierTraiteManager = new REFichierDnraJournalierTraiteManager();
        fichierDnraJournalierTraiteManager.setSession(getSession());
        fichierDnraJournalierTraiteManager.find(BManager.SIZE_NOLIMIT);

        // construction de la liste des noms déjà traités
        if (fichierDnraJournalierTraiteManager.size() > 0) {
            List<REFichierDnraJournalierTraite> fichiersDrnaDejaTraitesList = fichierDnraJournalierTraiteManager
                    .getContainerAsList();
            for (REFichierDnraJournalierTraite fichier : fichiersDrnaDejaTraitesList) {
                nomsFichiersDnraDejaTraites.add(fichier.getNomFichierDnraJournalierTraite());
            }
        }

        return nomsFichiersDnraDejaTraites;
    }

    /**
     * Générer une liste au format XLS
     * 
     * @param list
     * @param locale
     * @return
     */
    static String generateXls(List<DifferenceTrouvee> list, BSession session, String fileName) {
        final String nomCaisse;
        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        nomCaisse = (FWIImportProperties.getInstance().getProperty(docInfo, ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                + session.getIdLangueISO().toUpperCase()));
        String title = FWMessageFormat.format(session.getLabel("LIST_CORVUS_DIFFERENCE_TROUVEE_TITLE"),
                new ch.globaz.common.domaine.Date().getSwissValue(), fileName);
        Configuration configuration = Configurations.buildeDefault();
        HeaderFooter headerFooter;
        headerFooter = configuration.getHeaderFooter();
        headerFooter.setRightTop(session.getLabel("LIST_CORVUS_DIFFERENCE_TROUVEE_RIGHT_TOP"));
        headerFooter.setLeftTop(nomCaisse);
        headerFooter.setLeftBottom(session.getUserName() + " ("
                + ch.globaz.common.domaine.Date.toDayformatteSwissValueWithTime() + ")");

        Collections.sort(list, new Comparator<DifferenceTrouvee>() {
            @Override
            public int compare(DifferenceTrouvee o1, DifferenceTrouvee o2) {
                return o1.getNss().compareTo(o2.getNss());
            }
        });

        File file = SimpleOutputListBuilderJade.newInstance().outputNameAndAddPath("5158PRE_REListeExcelWebAvsNRA")
                .configure(configuration).addList(list).classElementList(DifferenceTrouvee.class)
                .addTitle(title, Align.LEFT).asXls().build();
        return file.getAbsolutePath();
    }

    /**
     * Retourne une session PAVO
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private static BSession getSessionPavo(BSession session) throws Exception {
        return (BSession) GlobazSystem.getApplication("PAVO").newSession(session);
    }

    /**
     * Indique si un tiers ou un membre de sa famille a une rente en cours
     * 
     * @param idTiers
     * @param session
     * @return
     * @throws Exception
     */
    private boolean hasDemandeOrRenteActiveForFamilly(String idTiers, BSession session) throws Exception {

        // Utilisation de ce manager car AMHA c'est le seul capable et utilisé dans les rentes pour retrouver une
        // demande en fonction d'un idTiers
        REDemandeRenteJointPrestationAccordeeManager demandeManager = new REDemandeRenteJointPrestationAccordeeManager();
        demandeManager.setSession(session);

        // Recherche le tiers requérant et nom le bénéficiaire. Ce qui nous intéresse ici est de savoir si l'id tiers
        // reçu en argument à une demande à son nom
        demandeManager.setForIdTiersRequ(idTiers);

        // Gestion des états indésirable de la demande
        StringBuilder sb = new StringBuilder();
        sb.append(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
        sb.append(", ");
        sb.append(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
        sb.append(", ");
        sb.append(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
        demandeManager.setForCSEtatDemandeNotIn(sb.toString());

        demandeManager.find(); // OSEF du SIZE_NO_LIMIT ;)
        if (demandeManager.getContainer().size() >= 1) {
            return true;
        }

        // Manager utilisé pour la recherche des prestation accordées.
        REPrestationAccordeeManager prestationManager = new REPrestationAccordeeManager();
        prestationManager.setSession(session);

        // Il n'y pas besion de
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);

        ISFMembreFamilleRequerant[] mf = null;
        try {
            mf = sf.getMembresFamille(idTiers);
        } catch (Exception e) {
            // il n'existe pas de situation familiale pour cette personne, on retourne volontairement false
            return false;
        }

        int today = Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date()));
        ch.globaz.common.domaine.Date t = new ch.globaz.common.domaine.Date();

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat dateReader = new SimpleDateFormat("MM.yyyy");

        for (ISFMembreFamilleRequerant membre : mf) {
            if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
                prestationManager.setForIdTiersBeneficiaire(membre.getIdTiers());
                prestationManager.find();
                for (Object o : prestationManager.getContainer()) {
                    REPrestationsAccordees pa = (REPrestationsAccordees) o;

                    int ddfd = JadeStringUtil.isBlankOrZero(pa.getDateFinDroit()) ? 0 : Integer.valueOf(dateFormater
                            .format(dateReader.parse(pa.getDateFinDroit())));

                    // 1ère condition : pas de date de fin
                    if (ddfd == 0) {
                        return true;
                    }

                    // 2ème check si la date de fin est plus que la date du jour
                    else if (ddfd > today) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
