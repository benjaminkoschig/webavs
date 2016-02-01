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
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.process.REAbstractJadeJob;
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
        List<String> fichiersMutationsATraiterList;

        System.out.println("d�but du traitement");
        try {
            // t�l�chargement du ou des fichiers � traiter
            fichiersMutationsATraiterList = telechargerFichiersMutations();

            for (String fichierMutationName : fichiersMutationsATraiterList) {
                // lecture du fichier et mapping dans la structure d'objet

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("fin du traitement");
    }

    private List<String> telechargerFichiersMutations() throws Exception, IllegalArgumentException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException,
            JadeUrlMalformedException {
        List<String> fichiersMutationsATraiterList = new ArrayList<String>();

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
                JadeLogger.info(CIUpiDailyProcess.class, "T�l�chargement du fichier : " + uriSource + "...");
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
