package globaz.corvus.process;

import globaz.corvus.api.arc.downloader.REDownloaderInscriptionsCI;
import globaz.corvus.application.REApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.gsc.bean.JadeRoleGroup;
import globaz.jade.admin.gsc.service.JadeRoleGroupService;
import globaz.jade.admin.gsc.service.JadeRoleUserService;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserGroupService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (23.06.2009 10:29:22)
 * 
 * @author: bsc
 */
public class REImporterCIProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;

    private static String[] jasperToArray(REImporterCIProcess process) {
        String[] filenames = new String[process.getAttachedDocuments().size()];
        int index = 0;
        for (Iterator iter = process.getAttachedDocuments().iterator(); iter.hasNext();) {
            JadePublishDocument document = (JadePublishDocument) iter.next();
            if (document.getPublishJobDefinition().getDocumentInfo().getPublishDocument()) {
                filenames[index++] = document.getDocumentLocation();
            }
        }
        return filenames;
    }

    public static List loadMails(String role, BSession session) throws Exception {
        List mails = new LinkedList();

        // pour les roles attaches via un group
        if (!JadeStringUtil.isBlank(role)) {
            JadeRoleGroupService roleGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getRoleGroupService();
            JadeUserGroupService userGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserGroupService();
            JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserService();
            Map criteres = new HashMap();
            criteres.put(JadeRoleGroupService.CR_FOR_IDROLE, role);
            JadeRoleGroup[] groups = roleGroupService.findForCriteres(criteres);
            if (groups != null) {
                for (int i = 0; i < groups.length; i++) {
                    String[] userids = userGroupService.findAllIdUserForIdGroup(groups[i].getIdGroup());
                    if (userids != null) {
                        for (int j = 0; j < userids.length; j++) {
                            JadeUser user = userService.load(userids[j]);
                            if ((user != null) && (!JadeStringUtil.isBlank(user.getEmail()))
                                    && (!mails.contains(user.getEmail()))) {
                                mails.add(user.getEmail());
                            }
                        }
                    }
                }
            }

            // pour les roles attaches directement a l'utilisateur
            JadeRoleUserService roleUserService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getRoleUserService();
            String[] userids = roleUserService.findAllIdUserForIdRole(role);
            if (userids != null) {
                for (int j = 0; j < userids.length; j++) {
                    JadeUser user = userService.load(userids[j]);
                    if ((user != null) && (!JadeStringUtil.isBlank(user.getEmail()))
                            && (!mails.contains(user.getEmail()))) {
                        mails.add(user.getEmail());
                    }
                }
            }

        }
        if (mails.size() == 0) {
            mails.add(session.getApplication().getProperty(REApplication.PROPERTY_DEFAULT_EMAIL));
        }
        return mails;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 13:54:49)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        boolean profile = false;
        REImporterCIProcess myProcess = new REImporterCIProcess();

        try {

            if (args.length < 2) {
                throw new Exception("Wrong number of arguments");
            }

            // démarrage en mode CommandeLineJob pour ne pas exécuter les processus SEDEX
            Jade.getInstanceForCommandLineJob();

            // recuperation des arguments
            myProcess.setUserID(args[0]);
            myProcess.setPwd(args[1]);

            // creation de la session
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("CORVUS")
                    .newSession(args[0], args[1]);

            // init session process
            myProcess.setSession(session);

            // init email address
            myProcess.setSendCompletionMail(true);
            myProcess.setSendMailOnError(true);
            myProcess.setControleTransaction(true);

            // run le process
            myProcess.executeProcess();

            // logger le memoryLog dans le system.err
            if (myProcess.getMemoryLog().isOnErrorLevel()) {
                for (int i = 0; i < myProcess.getMemoryLog().size(); i++) {
                    System.err.println(myProcess.getMemoryLog().getMessage(i).getFullMessage());
                    System.err.println("\n");
                }
                System.out.println("Process CORVUS (REImporterCIProcess) has error(s)!");

                REImporterCIProcess.sendError(new Exception(), myProcess);

                // erreur critique, je retourne un code d'erreur 200
                System.exit(REImporterCIProcess.CODE_RETOUR_ERREUR);
            } else {
                // le mail des resultats
                REImporterCIProcess.sendResult(myProcess);

                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process CORVUS (REImporterCIProcess) executed successfully !");
                System.exit(REImporterCIProcess.CODE_RETOUR_OK);
            }

        } catch (Exception e) {
            try {
                REImporterCIProcess.sendError(e, myProcess);
            } catch (Exception e2) {
                e2.printStackTrace(System.err);
            }
            e.printStackTrace(System.err);
            System.out.println("Process CORVUS (REImporterCIProcess) has error(s)!");
            // erreur critique, je retourne un code d'erreur 200
            System.exit(REImporterCIProcess.CODE_RETOUR_ERREUR);
        } finally {
            if (profile) {
                Jade.getInstance().endProfiling();
            }
        }
    }

    private static void sendError(Exception e, REImporterCIProcess process) throws Exception {
        // les destinataires
        List mails = REImporterCIProcess.loadMails(
                process.getSession().getApplication().getProperty(REApplication.RESPONSABLE_CI), process.getSession());

        System.out.println(JadeDateUtil.getFormattedDateTimeMillis(new Date())
                + " Envoi de l'erreur de CORVUS (REImporterCIProcess) à " + mails);

        // l'e-mail
        JadeSmtpClient.getInstance().sendMail(JadeConversionUtil.toStringArray(mails),
                process.getSession().getLabel("EMAIL_OBJECT_IMPORTER_CI_PROCESS"),
                JadeStringUtil.isBlank(process.getSubjectDetail()) ? e.getMessage() : process.getSubjectDetail(),
                new String[] {});
    }

    private static void sendResult(REImporterCIProcess process) throws Exception {
        // les destinataires
        List mails = REImporterCIProcess.loadMails(
                process.getSession().getApplication().getProperty(REApplication.RESPONSABLE_CI), process.getSession());

        System.out.println(JadeDateUtil.getFormattedDateTimeMillis(new Date())
                + " Envoi du résultat de CORVUS (REImporterCIProcess) à " + mails);

        // l'e-mail
        JadeSmtpClient.getInstance().sendMail(JadeConversionUtil.toStringArray(mails),
                process.getSession().getLabel("EMAIL_OBJECT_IMPORTER_CI_PROCESS"), process.getSubjectDetail(),
                REImporterCIProcess.jasperToArray(process));
    }

    // private String[] args;
    private String param0;

    private String param1;

    private String pwd = "";

    private String userID = "";

    public REImporterCIProcess() {
        super();
    }

    /**
     * Method REImporterCIProcess.
     * 
     * @param uid
     * @param pwd
     */
    public REImporterCIProcess(String uid, String pwd) {
        super();
        setUserID(uid);
        setPwd(pwd);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        System.out.println("Process is terminating...");
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        // creation de la transaction
        BTransaction transaction = (BTransaction) getSession().newTransaction();
        transaction.openTransaction();

        REDownloaderInscriptionsCI rci = new REDownloaderInscriptionsCI(transaction, getSession());

        // importation des CI
        try {

            rci.download();

        } finally {
            transaction.closeTransaction();
        }

        // transfert des logs
        getMemoryLog().logMessage(rci.getLog());

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendMailOnError(false);
        setSendCompletionMail(false);
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "";
    }

    /**
     * @return
     */
    public String getParam0() {
        return param0;
    }

    /**
     * @return
     */
    public String getParam1() {
        return param1;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setParam0(String string) {
        param0 = string;
    }

    /**
     * @param string
     */
    public void setParam1(String string) {
        param1 = string;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Sets the userID.
     * 
     * @param userID
     *            The userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
