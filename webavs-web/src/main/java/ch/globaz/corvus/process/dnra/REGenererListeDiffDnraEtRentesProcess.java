package ch.globaz.corvus.process.dnra;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
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
import java.util.List;
import java.util.Locale;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.corvus.process.REAbstractJadeJob;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
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

    static String generateXls(List<DifferenceTrouvee> list, Locale locale) {
        File file = SimpleOutputListBuilder.newInstance().local(locale).addList(list)
                .classElementList(DifferenceTrouvee.class).asXls().outputName("mutationList").build();
        return file.getAbsolutePath();
    }

    @Override
    protected void process() {
        List<String> fichiersMutationsATraiterList;

        System.out.println("début du traitement");
        try {
            // téléchargement du ou des fichiers à traiter
            fichiersMutationsATraiterList = telechargerFichiersMutations();

            for (String fichierMutationName : fichiersMutationsATraiterList) {
                // lecture du fichier et mapping dans la structure d'objet
                MutationsContainer mutationsContainer = MutationParser.parsFile(fichierMutationName);
                mutationsContainer.setFichierMutationName(fichierMutationName);
                List<InfoTiers> listInfosTiers = findInfosTiers(mutationsContainer.extractNssActuel());

                JadeFsFacade.delete(fichierMutationName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("fin du traitement");
    }

    private List<InfoTiers> findInfosTiers(List<String> listNss) {
        StringBuilder sql = new StringBuilder();
        sql.append("select schema.TIPAVSP.HXNAVS as nss, schema.TITIERP.HNIPAY as codeNationalite, schema.TITIERP.HTLDE1 as nom, ");
        sql.append("schema.TITIERP.HTLDE2 as prenom, schema.TIPERSP.HPDNAI as dateNaissance, schema.TIPERSP.HPDDEC as dateDeces, ");
        sql.append("schema.TIPERSP.HPTSEX as sexe, schema.TIPERSP.HPTETC as codeEtatCivil ");
        sql.append("from schema.TIPAVSP ");
        sql.append("inner join schema.TITIERP on schema.TITIERP.HTITIE = schema.TIPAVSP.HTITIE ");
        sql.append("inner join schema.TIPERSP on schema.TIPERSP.HTITIE = schema.TIPAVSP.HTITIE ");
        sql.append("where HXNAVS in (").append(listNss).append(")");
        List<InfoTiers> listInfosTiers = QueryExecutor.execute(sql.toString(), InfoTiers.class, getSession());
        return listInfosTiers;
    }

    private List<String> telechargerFichiersMutations() throws Exception, IllegalArgumentException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException,
            JadeUrlMalformedException {
        List<String> fichiersMutationsATraiterList = new ArrayList<String>();

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

        // parcours de fichiers disponibles. Pour le moment on ne prend que celui du 20160129
        for (JadeFsFileInfo jadeFsFileInfo : jadeFsFileInfoList) {
            System.out.println(jadeFsFileInfo.getUri());
            JadeUrl jadeUrlFichierDistant = new JadeUrl();
            jadeUrlFichierDistant.setUrl(jadeFsFileInfo.getUri());
            String nomFichierDistant = jadeUrlFichierDistant.getFile();
            System.out.println(nomFichierDistant);
            // BigDecimal todayAaaaMmJj = JACalendar.today().toAMJ();
            String fileDate = nomFichierDistant.split("_")[1].split("\\.")[0];
            if (fileDate.equals("20160129")) {
                String uriDest = fichierMutationsLocauxDirectory + nomFichierDistant;
                String uriSource = fichiersMutationsDistantUriNormalized + nomFichierDistant;
                JadeLogger.info(CIUpiDailyProcess.class, "Téléchargement du fichier : " + uriSource + "...");
                JadeFsFacade.copyFile(uriSource, uriDest);
                fichiersMutationsATraiterList.add(nomFichierDistant);
            }
        }

        return fichiersMutationsATraiterList;
    }

    private static BSession getSessionPavo(BSession session) throws Exception {
        return (BSession) GlobazSystem.getApplication("PAVO").newSession(session);
    }

}
