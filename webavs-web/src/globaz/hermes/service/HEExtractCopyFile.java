/*
 * Créé le 6 févr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.service;

import globaz.globall.db.BApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.hermes.exception.HETransmissionFichierException;
import globaz.hermes.utils.HEBatchUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEExtractCopyFile extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CLOTURE = "cloture";
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    public static final String ENCODING = "Cp037";
    private static final String EXTRAIT = "extrait";
    private static final String PROPRIETE_TRAITEMENT = "zas.copy.traitement";
    private static final String PROPRIETE_TRAITEMENT_PREV = "zas.copy.prev";

    public static void main(String[] args) {
        boolean profile = false;

        try {

            if (args.length < 2) {
                System.out.println(HEBatchUtils.getCurrentTime()
                        + " java globaz.hermes.service.HEExtractCopyFile <uid> <pwd> [" + CLOTURE + "|" + EXTRAIT
                        + "] [fichier source] [fichier destination] [Liste motifs (ex: 92,98)]");
                throw new Exception("Wrong number of arguments");
            }

            //
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[0], args[1]);

            //
            HEExtractCopyFile myProcess = new HEExtractCopyFile();

            //
            myProcess.setSession(session);

            //
            // init email address
            myProcess.setSendCompletionMail(true);
            myProcess.setSendMailOnError(true);
            myProcess.setControleTransaction(true);

            if (args[2] != null && (CLOTURE.equals(args[2]) || (EXTRAIT.equals(args[2])))) {
                myProcess.setType(args[2]);
                if (args.length == 4) {
                    if (!JadeStringUtil.isEmpty(args[3])) {
                        myProcess.setMotifs(args[3]);
                    }
                }
                if (args.length > 4) {
                    if (!JadeStringUtil.isEmpty(args[3])) {
                        myProcess.setFichierSource(args[3]);
                    }
                    if (!JadeStringUtil.isEmpty(args[4])) {
                        myProcess.setFichierDestination(args[4]);
                    }
                    if (!JadeStringUtil.isEmpty(args[5])) {
                        myProcess.setMotifs(args[5]);
                    }
                }
            } else {
                // Setter les autres arguments si pas null
                if (args.length == 3) {
                    if (!JadeStringUtil.isEmpty(args[2])) {
                        myProcess.setMotifs(args[2]);
                    }
                }
                if (args.length > 3) {
                    if (!JadeStringUtil.isEmpty(args[2])) {
                        myProcess.setFichierSource(args[2]);
                    }
                    if (!JadeStringUtil.isEmpty(args[3])) {
                        myProcess.setFichierDestination(args[3]);
                    }
                    if (!JadeStringUtil.isEmpty(args[4])) {
                        myProcess.setMotifs(args[4]);
                    }
                }
            }
            //
            if (profile = ("true".equals(myProcess.getSession().getApplication().getProperty("profiling")))) {
                Jade.getInstance().beginProfiling(HEExtractCopyFile.class, args);
            }
            //
            // run le process
            JadeJobInfo job = BProcessLauncher.start(myProcess);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Process extract copy's file not executed successfully !");
                System.out.println(job.getFatalErrorMessage());
                System.exit(CODE_RETOUR_ERREUR);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process extract copy's file executed successfully !");
                System.exit(CODE_RETOUR_OK);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Process extract copy's file has error(s) !");

            // erreur critique, je retourne un code d'erreur 200
            System.exit(CODE_RETOUR_ERREUR);
        } finally {
            if (profile) {
                Jade.getInstance().endProfiling();
            }
        }
    }

    public String fichierDestination = null;
    public String fichierSource = null;
    public String motifs = null;

    private String type = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        BTransaction transaction = null;
        boolean append = false;
        try {
            if (JadeStringUtil.isEmpty(fichierDestination)) {
                fichierDestination = Jade.getInstance().getSharedDir()
                        + getSession().getApplication().getProperty("ftp.copy.file");
            }
            File out = new File(fichierDestination);

            try {
                // si le fichier existe, suppression du fichier
                if (out.exists()) {
                    out.delete();
                }
                if (!JadeStringUtil.isEmpty(fichierSource)) {
                    JadeFsFacade.copyFile(fichierSource, fichierDestination);
                    append = true;
                }
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                // préparation du writer en fonction des properties
                BufferedWriter writer;
                boolean isFileEBCDIC = "true"
                        .equals(getSession().getApplication().getProperty("ftp.file.input.ebcdic"));
                boolean hasCarriageReturns = "true".equals(getSession().getApplication().getProperty(
                        "ftp.file.input.carriagereturn"));
                if (isFileEBCDIC) {
                    // j'écris en EBCDIC
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(fichierDestination, append), ENCODING));
                } else {
                    writer = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(fichierDestination, append)));
                }
                String[] motifs = getFiltreMotifs();
                // génère le contenu du fichier selon la config
                IHEExtractCopyFileTraitement traiteur = getTraiteur();
                traiteur.setForListMotif(motifs);
                traiteur.genererFichier(writer, hasCarriageReturns, getSession(), transaction);
                writer.close();
                //
                if (!transaction.isRollbackOnly()) {
                    // transmet le fichier selon la config
                    sendFile(fichierDestination, getSession());
                    // tout est ok, on peut valider
                    transaction.commit();
                    System.exit(CODE_RETOUR_OK);
                } else {
                    transaction.rollback();
                    System.exit(CODE_RETOUR_ERREUR);
                }
            } catch (Exception e) {
                e.printStackTrace();// TODO virer une fois les tests terminés
                if (transaction != null) {
                    transaction.rollback();
                }
                System.exit(CODE_RETOUR_ERREUR);
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                out.delete();
            }
        } catch (Exception e) {

        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    public String getFichierDestination() {
        return fichierDestination;
    }

    public String getFichierSource() {
        return fichierSource;
    }

    private String[] getFiltreMotifs() {
        String[] res = null;
        if (!JadeStringUtil.isEmpty(motifs)) {
            StringTokenizer motifs = new StringTokenizer(getMotifs(), ",");
            res = new String[motifs.countTokens()];
            for (int i = 0; motifs.hasMoreTokens(); i++) {
                String token = motifs.nextToken();
                res[i] = token.trim();
            }
        }
        return res;
    }

    public String getMotifs() {
        return motifs;
    }

    private IHEExtractCopyFileTraitement getTraiteur() throws Exception {
        String className = getSession().getApplication().getProperty(PROPRIETE_TRAITEMENT);
        if (EXTRAIT.equals(getType())) {
            // si le type spécifié est le mode prévisionnel, on prend la bonne
            // propriété !
            className = getSession().getApplication().getProperty(PROPRIETE_TRAITEMENT_PREV);
        }
        if (className == null) {
            return new HEExtractCopyFileDefaultTraitement();
        } else {
            return (IHEExtractCopyFileTraitement) Class.forName(className).newInstance();
        }
    }

    public String getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void sendFile(String fileName, BSession session) throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new HETransmissionFichierException("Impossible de récupérer l'application", e1.getMessage());
        }
        String ftpPath = application.getProperty("ftp.copy.path");
        if (!JadeStringUtil.isEmpty(ftpPath)) {
            String ftpFile = JadeFilenameUtil.extractFilename(fileName);
            if (JadeStringUtil.isEmpty(ftpFile)) {
                throw new HETransmissionFichierException("erreur lors de l'extraction du nom du fichier !");
            }
            String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpFile;
            String replace = application.getProperty("ftp.copy.replace");
            if ("true".equalsIgnoreCase(replace)) {
                JadeFsFacade.delete(ftpUri);
            }
            try {
                if (JadeFsFacade.exists(ftpUri)) {
                    throw new HETransmissionFichierException("Le fichier est déjà présent sur le serveur FTP !");
                }
            } catch (Exception e) {
                throw new HETransmissionFichierException(e.getMessage());
            }
            try {
                JadeFsFacade.copyFile(fileName, ftpUri);
            } catch (Exception e) {
                throw new HETransmissionFichierException("Echec lors de l'envoi du fichier", e.getMessage());
            }
        } else {
            System.out.println("ftp.copy.path est vide, aucun postage ftp !");
        }
    }

    public void setFichierDestination(String fichierDestination) {
        this.fichierDestination = fichierDestination;
    }

    public void setFichierSource(String fichierSource) {
        this.fichierSource = fichierSource;
    }

    public void setMotifs(String motifs) {
        this.motifs = motifs;
    }

    public void setType(String type) {
        this.type = type;
    }

}
