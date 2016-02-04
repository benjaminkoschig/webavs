package ch.globaz.corvus.process.dnra;

import globaz.corvus.db.dnra.REFichierDnraJournalierTraite;
import globaz.corvus.db.dnra.REFichierDnraJournalierTraiteManager;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
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
import globaz.jade.url.JadeUrlMalformedException;
import globaz.pavo.db.upidaily.CIUpiDailyProcess;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.corvus.process.REAbstractJadeJob;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
import com.sun.star.lang.IllegalArgumentException;

/**
 * Compare les donn�es du NRA transmises via le fichier journalier (delta-quotidien) avec les donn�es des rentes et
 * g�n�re une liste recensant ces diff�rences.
 * 
 * @author bjo
 * 
 */
public class REGenererListeDiffDnraEtRentesProcess extends REAbstractJadeJob {

    private static final long serialVersionUID = 7164797280525378834L;

    @Override
    public String getDescription() {
        return "Compare les donn�es du NRA transmises via le fichier journalier avec les donn�es des rentes et g�n�re une liste recensant ces diff�rences.";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {
        List<File> fichiersMutationsATraiterList;

        JadeLogger.info(getName(), "D�but du traitement de g�n�ration des listes de diff�rences");
        try {
            // t�l�chargement du ou des fichiers � traiter
            fichiersMutationsATraiterList = telechargerFichiersMutations();

            // traitement de chaque fichier et g�n�ration de la liste
            for (File fichierMutation : fichiersMutationsATraiterList) {
                System.out.println("t�l�chargement du fichier : " + fichierMutation.getName() + " ---->"
                        + fichierMutation.getAbsolutePath());

                // parsing et mapping dans la structure d'objet
                MutationsContainer mutationsContainer = MutationParser.parsFile(fichierMutation.getAbsolutePath());
                mutationsContainer.setFichierMutationName(fichierMutation.getAbsolutePath());

                // recherche des infos sur les tiers relatives aux mutations
                List<InfoTiers> listInfosTiers = findInfosTiers(mutationsContainer.extractNssActuel());

                // identification des diff�rences entre les mutations annonc�es et les donn�es DB
                DifferenceFinder differenceFinder = new DifferenceFinder();
                List<DifferenceTrouvee> differenceTrouvees = differenceFinder.findAllDifference(
                        mutationsContainer.getList(), listInfosTiers);

                // g�n�ration de la liste au format xls
                String path = generateXls(differenceTrouvees, new Locale(getSession().getIdLangueISO()));
                System.out.println(path);

                // marquerFichierDnraCommeTraite(fichierMutation.getName());

                // suppression du fichier journalier
                JadeFsFacade.delete(fichierMutation.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("fin du traitement");
    }

    /**
     * Remonte toutes les informations relatives � la liste de NSS pass�e en param�tre
     * 
     * @param listNss
     * @return
     */
    private List<InfoTiers> findInfosTiers(List<String> listNss) {
        System.out.println("nbNss � chercher dans webtiers : " + listNss.size());
        List<InfoTiers> listInfosTiers = new ArrayList<InfoTiers>();
        List<List<String>> splittedList = QueryExecutor.split(listNss, 1000);
        for (List<String> list : splittedList) {
            StringBuilder sql = new StringBuilder();
            sql.append("select schema.TIPAVSP.HXNAVS as nss, schema.TITIERP.HNIPAY as codeNationalite, schema.TITIERP.HTLDE1 as nom, ");
            sql.append("schema.TITIERP.HTLDE2 as prenom, schema.TIPERSP.HPDNAI as dateNaissance, schema.TIPERSP.HPDDEC as dateDeces, ");
            sql.append("schema.TIPERSP.HPTSEX as sexe, schema.TIPERSP.HPTETC as codeEtatCivil ");
            sql.append("from schema.TIPAVSP ");
            sql.append("inner join schema.TITIERP on schema.TITIERP.HTITIE = schema.TIPAVSP.HTITIE ");
            sql.append("inner join schema.TIPERSP on schema.TIPERSP.HTITIE = schema.TIPAVSP.HTITIE ");
            sql.append("where HXNAVS in (").append(QueryExecutor.forInString(list)).append(")");
            listInfosTiers.addAll(QueryExecutor.execute(sql.toString(), InfoTiers.class, getSession()));
        }
        System.out.println("nbNss trouv�s dans webtiers : " + listInfosTiers.size());
        return listInfosTiers;
    }

    /**
     * Insert dans la DB le nom du fichier afin de l'identifier comme trait�
     * 
     * @param nomFichier
     * @throws Exception
     */
    private void marquerFichierDnraCommeTraite(String nomFichier) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            REFichierDnraJournalierTraite fichierDnra = new REFichierDnraJournalierTraite();
            fichierDnra.setNomFichierDnraJournalierTraite(nomFichier);
            fichierDnra.add(transaction);
            transaction.commit();                       // tout s'est bien pass� on commit
        } catch (Exception e) {
            transaction.rollback();                     // probl�me, on rollback le tout
            JadeLogger.error(this.getClass(), "impossible d'ajouter le fichier dans la DB : " + nomFichier);
        } finally {
            if (transaction != null) {                    // s'assurer que la transaction n'est pas null
                transaction.closeTransaction();         // on clot
            }
        }
    }

    /**
     * Se connecte au serveur d�finit par la propri�t� en DB "nraUpiServer" et de t�l�charge tous les
     * fichiers qui n'ont pas encore �t� trait�s (selon historique) par le process. Les fichiers sont d�pos�s dans le
     * r�pertoire "shared" (jade.xml). La m�thode retourne les paths de tous les fichiers t�l�charg�s (trier par nom).
     * 
     * @return liste des paths sur les fichiers t�l�charg�s.
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

        // r�cup�ration de l'URI o� sont stock�s les fichiers d�-zipp�s et d�crypt�s
        String fichiersMutationsDistantUri = getSessionPavo(getSession()).getApplication().getProperty("nraUpiServer");
        if (JadeStringUtil.isEmpty(fichiersMutationsDistantUri)) {
            throw new IllegalArgumentException("l'URI des fichiers de mutations NRA est vide");
        }
        // d�finition du r�pertoire local pour le t�l�chargement des fichiers
        String fichierMutationsLocauxDirectory = Jade.getInstance().getSharedDir();
        if (JadeStringUtil.isEmpty(fichierMutationsLocauxDirectory)) {
            throw new IllegalArgumentException(
                    "Le r�pertoire local n�cessaire au t�l�chargement des fichiers n'est pas valide");
        }

        // normalisation du path en fonction du filesystem utilis�
        String fichiersMutationsDistantUriNormalized = JadeFilenameUtil.normalizePathRoot(fichiersMutationsDistantUri);
        JadeLogger.info(CIUpiDailyProcess.class, "Recherche des fichiers disponibles sur : "
                + fichiersMutationsDistantUriNormalized);

        // r�cup�ration de la liste des fichiers disponibles dans le r�pertoire distant
        List<JadeFsFileInfo> jadeFsFileInfoList = JadeFsFacade
                .getFolderChildrenInfo(fichiersMutationsDistantUriNormalized);
        if ((jadeFsFileInfoList == null) || (jadeFsFileInfoList.size() == 0)) {
            JadeLogger.info(CIUpiDailyProcess.class, "Pas de fichiers trouv�s sur : "
                    + fichiersMutationsDistantUriNormalized);
            return fichiersMutationsATraiterList;
        }

        List<String> nomsFichiersDnraDejaTraites = recupererNomsFichiersDnraDejaTraites();

        // parcours de fichiers disponibles. Pour le moment on ne prend que celui du 20160129
        for (JadeFsFileInfo jadeFsFileInfo : jadeFsFileInfoList) {
            System.out.println(jadeFsFileInfo.getUri());
            JadeUrl jadeUrlFichierDistant = new JadeUrl();
            jadeUrlFichierDistant.setUrl(jadeFsFileInfo.getUri());
            String nomFichierDistant = jadeUrlFichierDistant.getFile();
            System.out.println(nomFichierDistant);
            // BigDecimal todayAaaaMmJj = JACalendar.today().toAMJ();
            String dateFichier = nomFichierDistant.split("_")[1].split("\\.")[0];
            if (!nomsFichiersDnraDejaTraites.contains(nomFichierDistant)) {
                String uriDest = fichierMutationsLocauxDirectory + nomFichierDistant;
                String uriSource = fichiersMutationsDistantUriNormalized + nomFichierDistant;
                JadeLogger.info(CIUpiDailyProcess.class, "T�l�chargement du fichier : " + uriSource + "...");
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
     * r�cup�re dans la table REDNRAJ les noms des fichiers qui ont d�j� �t� trait�s par le process. Retourne une liste
     * vide si aucun cas.
     * 
     * @return
     * @throws Exception
     */
    private List<String> recupererNomsFichiersDnraDejaTraites() throws Exception {
        List<String> nomsFichiersDnraDejaTraites = new ArrayList<String>();

        // r�cup�ration des fichiers d�j� trait�s par le process
        REFichierDnraJournalierTraiteManager fichierDnraJournalierTraiteManager = new REFichierDnraJournalierTraiteManager();
        fichierDnraJournalierTraiteManager.setSession(getSession());
        fichierDnraJournalierTraiteManager.find(BManager.SIZE_NOLIMIT);

        // construction de la liste des noms d�j� trait�s
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
     * G�n�rer une liste au format XLS
     * 
     * @param list
     * @param locale
     * @return
     */
    static String generateXls(List<DifferenceTrouvee> list, Locale locale) {
        File file = SimpleOutputListBuilder.newInstance().local(locale).addList(list)
                .classElementList(DifferenceTrouvee.class).asXls().outputName("mutationList").build();
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

}
