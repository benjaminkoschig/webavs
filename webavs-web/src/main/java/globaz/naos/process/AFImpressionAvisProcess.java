package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.external.IntModuleImpression;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (26.02.2002 14:53:33)
 * 
 * @author: Administrator
 */
public class AFImpressionAvisProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // le constructeur d'un document
    private java.lang.String dateImpression = new String();
    private boolean flagCodeMaj = false;
    private java.lang.String fromIdExterneRole = new String();
    private java.lang.String idSousType = new String();
    private java.lang.String idTri = new String();
    private java.lang.String idTriDecompte = new String();
    public java.util.HashMap interfaceImpressionContainer = null;
    private java.lang.String libelle = new String();
    private globaz.musca.db.facturation.FAModuleImpressionManager modImpManager;
    private java.util.HashMap modImpMap;
    // Variable pour le comptage
    private int nbImprimer = 1; // compteur d'entêtes de facture
    // nombre d'entêtes de facture que le manager contient
    private int nbPasImprimer = 0;
    private int shouldNbImprimer = 0;
    private int sizeManager = 0;
    private java.lang.String tillIdExterneRole = new String();

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFImpressionAvisProcess() throws Exception {
        super();
    };

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFImpressionAvisProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFImpressionAvisProcess(BSession session) throws Exception {
        super(session);
    }

    /**
     * Insert the method's description here. Creation date: (12.06.2003 11:15:52)
     * 
     * @return boolean
     */
    public boolean _createDocument(FAEnteteFactureManager manager, IntModuleImpression interface_moduleImpression) {

        BStatement statement = null;
        boolean success = false;

        try {
            FAEnteteFacture enteteFacture = null;
            statement = manager.cursorOpen(getTransaction());
            // traitement des lignes trouvées
            int progressCounter = 0;
            while (((enteteFacture = (FAEnteteFacture) manager.cursorReadNext(statement)) != null)
                    && (!enteteFacture.isNew()) && !super.isAborted()) {

            }

        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        } finally {
            try {
                // fermer le cursor
                manager.cursorClose(statement);
            } catch (Exception ee) {
                ee.printStackTrace();
                getMemoryLog().logMessage(ee.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                return false;

            } finally {
                return !isAborted();
            }

        }

    }

    /**
     * Insert the method's description here. Creation date: (12.06.2003 11:33:57)
     * 
     * @return boolean
     */
    public boolean _doWrapperDocument() {
        try {
            // Recupere le répertoire du résultat (dans TEMP)
            BApplication application = (BApplication) GlobazServer.getCurrentSystem().getApplication(
                    getSession().getApplicationId());

            // Reprendre tous les documents qui ont été générés pour le wrapper
            Collection collec = interfaceImpressionContainer.values();
            Iterator it = collec.iterator();
            ArrayList allDocList = new ArrayList();
            IntModuleImpression myMod = null;
            // le chemin absolu du fichier
            String pathRoot = application.getLocalPath() + "/"
                    + globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP;

            // récupération du wrapper du document
            // FWIConcatPdf wrapper = new FWIConcatPdf();
            // wrapper.setPathRoot(pathRoot);

            // ne pas effacer les documents générer à la sortie
            // wrapper.setDeleteOnExit(false);

            getMemoryLog().logMessage(
                    "\n" + getSession().getLabel("OBJEMAIL_FAPRINT_DOCDESCRIPTION") + "\n"
                            + getSession().getLabel("OBJEMAIL_FAPRINT_SEPARATOR"),
                    globaz.framework.util.FWMessage.INFORMATION, "");
            // regrouper les documents par type
            int nbrTotalDoc = 0;
            StringBuffer finaleFilename = new StringBuffer("");

            FAApplication app = (FAApplication) getSession().getApplication();

            boolean executeCleanUp;
            String strExecuteCleanUp = app.getProperty(FAApplication.FLAG_EXECUTECLEANUP, "true");
            if (strExecuteCleanUp.equalsIgnoreCase("true")) {
                executeCleanUp = true;
            } else {
                executeCleanUp = false;
            }

            while (it.hasNext()) {
                myMod = (IntModuleImpression) it.next();
                if (myMod.get_document() != null) {
                    // le nombre de documents par type imprimés
                    // int nbr_doc = myMod.get_document().getFileList().size();
                    // nbrTotalDoc += nbr_doc;
                    // wrapper les docs ensemble
                    // wrapper.setFilesList(myMod.get_document().getFileList());
                    // le nom finale du fichier
                    // finaleFilename.append(
                    // myMod.get_document().getConcatFileName());
                    finaleFilename.append("(");
                    // finaleFilename.append(nbr_doc);
                    finaleFilename.append(")");
                    // Génère le document PDF final avec la fonction concat du
                    // wrapper
                    // wrapper.setConcatFileName(finaleFilename.toString());
                    // String concatFile = wrapper.concat();

                    // effacer les documents temporaires originaux
                    if (executeCleanUp) {
                        // myMod.get_document()._executeCleanUp();
                    }
                    // statistiques des documents dans l'email
                    // zone email
                    getMemoryLog().logMessage(finaleFilename.toString(), globaz.framework.util.FWMessage.INFORMATION,
                            "");

                }
            }
            // zone email
            getMemoryLog().logMessage(
                    "\n" + getSession().getLabel("OBJEMAIL_FAPRINT_SEPARATOR") + "\n"
                            + getSession().getLabel("OBJEMAIL_FAPRINT_DOCTOTAL") + ": " + nbrTotalDoc,
                    globaz.framework.util.FWMessage.INFORMATION, "");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // initialiser l'executionDate avec l'heure actuelle
        Date today = new Date();
        // l'assigner à la classe parente BProcess
        super._setExecutionDate(today);

        // OCA
        return false;
        /*
         * //prendre le passage en cours; passage = new FAPassage(); passage.setIdPassage(getIdPassage());
         * passage.setSession(getSession()); try { passage.retrieve(getTransaction()); } catch (Exception e) { };
         * 
         * AFAvisMutation _document = null; if (_document == null) { _document = new DocAvisMutation(); }
         * _document.setPassage(passage); _document.bindData(avis.numero);
         * 
         * return estImprime;
         */
    }

    @Override
    public String getEMailObject() {
        return null;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}