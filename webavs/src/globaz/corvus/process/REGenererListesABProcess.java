/*
 * Créé le 29 sept 08
 */
package globaz.corvus.process;

import globaz.corvus.api.arc.downloader.REAnnonces50Container;
import globaz.corvus.itext.REListeAAugmentations;
import globaz.corvus.itext.REListeADiminutions;
import globaz.corvus.itext.REListeB;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.tools.PRDateFormater;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * <H1>Process effectuant la génération des Liste A et B pour tous les elements du conteneur</H1>
 * 
 * @author BSC
 */
public class REGenererListesABProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private REAnnonces50Container container = null;
    private String emailObject = "";

    private REListeAAugmentations listeAAugmentations = null;
    private REListeADiminutions listeADiminutions = null;
    private REListeB listeB = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     */
    public REGenererListesABProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public REGenererListesABProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public REGenererListesABProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {

        boolean succes = false;
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        try {
            _validate();
            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                succes = false;
                throw new Exception(getSession().getLabel("PROCESS_LISTES_AB_VALID_FAIL"));
            }

            // pour imprimer les liste par moi de rapport on cherche
            // la liste complete des mois de rapports pour toutes les listes
            REAnnonces50Container c = new REAnnonces50Container(getSession());
            TreeMap keysMoisRapport = new TreeMap(c.new KeyMoisRapportComparator());

            Iterator it = container.getListeAAugmentation().keySet().iterator();
            while (it.hasNext()) {
                REAnnonces50Container.KeyMoisRapport key = (REAnnonces50Container.KeyMoisRapport) it.next();
                if (!keysMoisRapport.containsKey(key)) {
                    keysMoisRapport.put(key, key);
                }
            }

            it = container.getListeADiminution().keySet().iterator();
            while (it.hasNext()) {
                REAnnonces50Container.KeyMoisRapport key = (REAnnonces50Container.KeyMoisRapport) it.next();
                if (!keysMoisRapport.containsKey(key)) {
                    keysMoisRapport.put(key, key);
                }
            }

            it = container.getListeBAugmentation().keySet().iterator();
            while (it.hasNext()) {
                REAnnonces50Container.KeyMoisRapport key = (REAnnonces50Container.KeyMoisRapport) it.next();
                if (!keysMoisRapport.containsKey(key)) {
                    keysMoisRapport.put(key, key);
                }
            }

            it = container.getListeBDiminution().keySet().iterator();
            while (it.hasNext()) {
                REAnnonces50Container.KeyMoisRapport key = (REAnnonces50Container.KeyMoisRapport) it.next();
                if (!keysMoisRapport.containsKey(key)) {
                    keysMoisRapport.put(key, key);
                }
            }

            // ///////////////////////////////////////////////////////////////////////////////////////
            // Creation des Liestes A et B pour tous les mois de rapports
            // ///////////////////////////////////////////////////////////////////////////////////////

            Iterator iter = keysMoisRapport.keySet().iterator();
            while (iter.hasNext()) {
                REAnnonces50Container.KeyMoisRapport keyMoisAnnee = (REAnnonces50Container.KeyMoisRapport) iter.next();

                if (container.getListeAAugmentation().containsKey(keyMoisAnnee)) {
                    doListeAAugmentations(transaction, (Map) container.getListeAAugmentation().get(keyMoisAnnee),
                            PRDateFormater.convertDate_MMAA_to_MMxAAAA(keyMoisAnnee.getMoisRapport()));
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("PROCESS_LISTES_AB_ERR_LISTEA_AUG"));
                    }
                }

                if (container.getListeADiminution().containsKey(keyMoisAnnee)) {
                    doListeADiminutions(transaction, (Map) container.getListeADiminution().get(keyMoisAnnee),
                            PRDateFormater.convertDate_MMAA_to_MMxAAAA(keyMoisAnnee.getMoisRapport()));
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("PROCESS_LISTES_AB_ERR_LISTEA_DIM"));
                    }
                }

                if (container.getListeBAugmentation().containsKey(keyMoisAnnee)) {
                    doListeBAugmentations(transaction, (Map) container.getListeBAugmentation().get(keyMoisAnnee),
                            PRDateFormater.convertDate_MMAA_to_MMxAAAA(keyMoisAnnee.getMoisRapport()));
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("PROCESS_LISTES_AB_ERR_LISTEB_AUG"));
                    }
                }

                if (container.getListeBDiminution().containsKey(keyMoisAnnee)) {
                    doListeBDiminutions(transaction, (Map) container.getListeBDiminution().get(keyMoisAnnee),
                            PRDateFormater.convertDate_MMAA_to_MMxAAAA(keyMoisAnnee.getMoisRapport()));
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("PROCESS_LISTES_AB_ERR_LISTEB_DIM"));
                    }
                }

            }

            succes = true;

        } catch (Exception e) {

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(session.getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
            }

            return false;
        } finally {

            if (succes) {
                emailObject = getSession().getLabel("EMAIL_OBJECT_GENERATION_LISTE_A_B_SUCCES");
            } else {
                emailObject = getSession().getLabel("EMAIL_OBJECT_GENERATION_LISTE_A_B_ERREUR");
            }
            try {
                // Fusion des documents
                JadePublishDocumentInfo info = createDocumentInfo();
                info.setPublishDocument(true);
                info.setArchiveDocument(false);

                if (getAttachedDocuments().isEmpty()) {
                    getMemoryLog().logMessage(getSession().getLabel("PROCESS_LISTES_AB_AUCUNE_ANNNONCE"),
                            FWMessage.AVERTISSEMENT, this.getClass().toString());
                }

                mergePDF(info, true, 500, false, null);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    /**
     * (non-Javadoc).
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

    }

    /**
     * 
     * @param transaction
     * @throws Exception
     */
    private void doListeAAugmentations(BITransaction transaction, Map annoncesMoisAnnee, String moisAnnee)
            throws Exception {
        listeAAugmentations = new REListeAAugmentations();

        listeAAugmentations.setParentWithCopy(this);
        listeAAugmentations.setTransaction(transaction);
        listeAAugmentations.setControleTransaction(true);
        listeAAugmentations.setMapLevelNomPrenom(annoncesMoisAnnee);
        listeAAugmentations.setMoisRapport(moisAnnee);

        listeAAugmentations.executeProcess();
    }

    /**
     * 
     * @param transaction
     * @throws Exception
     */
    private void doListeADiminutions(BITransaction transaction, Map annoncesMoisAnnee, String moisAnnee)
            throws Exception {
        listeADiminutions = new REListeADiminutions();

        listeADiminutions.setParentWithCopy(this);
        listeADiminutions.setTransaction(transaction);
        listeADiminutions.setControleTransaction(true);
        listeADiminutions.setMapLevelNomPrenom(annoncesMoisAnnee);
        listeADiminutions.setMoisRapport(moisAnnee);

        listeADiminutions.executeProcess();
    }

    /**
     * 
     * @param transaction
     * @throws Exception
     */
    private void doListeBAugmentations(BITransaction transaction, Map annoncesMoisAnnee, String moisAnnee)
            throws Exception {
        listeB = new REListeB();

        listeB.setAugmentation(true);
        listeB.setParentWithCopy(this);
        listeB.setTransaction(transaction);
        listeB.setControleTransaction(true);
        listeB.setMapLevelNomPrenom(annoncesMoisAnnee);
        listeB.setMoisRapport(moisAnnee);

        listeB.executeProcess();
    }

    /**
     * 
     * @param transaction
     * @throws Exception
     */
    private void doListeBDiminutions(BITransaction transaction, Map annoncesMoisAnnee, String moisAnnee)
            throws Exception {
        listeB = new REListeB();

        listeB.setAugmentation(false);
        listeB.setParentWithCopy(this);
        listeB.setTransaction(transaction);
        listeB.setControleTransaction(true);
        listeB.setMapLevelNomPrenom(annoncesMoisAnnee);
        listeB.setMoisRapport(moisAnnee);

        listeB.executeProcess();
    }

    public REAnnonces50Container getContainer() {
        return container;
    }

    /**
     * @return
     */
    public String getEmailObject() {
        return emailObject;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        return emailObject;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setContainer(REAnnonces50Container container) {
        this.container = container;
    }

    /**
     * @param string
     */
    public void setEmailObject(String string) {
        emailObject = string;
    }

}
