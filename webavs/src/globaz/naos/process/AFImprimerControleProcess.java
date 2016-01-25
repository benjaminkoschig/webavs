/*
 * Créé le 8 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.itext.controleEmployeur.AFRapport_P1_Doc;
import globaz.naos.itext.controleEmployeur.AFRapport_P2_Doc;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cree les debuts de suivis pour les documents d'annonces de salaires.
 * </p>
 * 
 * <p>
 * Genere les documents pour tous les affilies non radies qui ont la case 'envois automatiques des annonces de salaires'
 * ou pour l'affilie dont l'identifiant est renseigne.
 * </p>
 * 
 * @author vre
 */
public class AFImprimerControleProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String controleId;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private Boolean preRapport = new Boolean(false);

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
	 * 
	 */
    public AFImprimerControleProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param parent
     */
    public AFImprimerControleProcess(BProcess parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param session
     */
    public AFImprimerControleProcess(BSession session) {
        super(session);
        // TODO Auto-generated constructor stub
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            AFRapport_P1_Doc documentP1 = new AFRapport_P1_Doc();
            AFRapport_P2_Doc documentP2 = new AFRapport_P2_Doc();

            AFControleEmployeurManager manager = new AFControleEmployeurManager();

            documentP1.setSession(getSession());
            documentP1.setPreRapport(getPreRapport().booleanValue());
            documentP2.setSession(getSession());
            manager.setSession(getSession());

            // charger les plans d'affiliations pour cet affilie
            manager.setForControleEmployeurId(getControleId());
            manager.find();

            for (int idPlan = 0; idPlan < manager.size(); ++idPlan) {
                creerDocumentP1(documentP1, getControleId());
                creerDocumentP2(documentP2, getControleId());
            }

            this.mergePDF(createDocumentInfo(), true, 0, false, null);

            return true;
        } catch (Exception e) {
            this._addError("erreur durant la génération du rapport: " + e.getMessage());
            abort();

            return false;
        }
    }

    private JACalendar calendar() throws Exception {
        return getSession().getApplication().getCalendar();
    }

    /**
     * démarre le suivi pour un document.
     * 
     * @param annonceSalaires_Doc
     *            DOCUMENT ME!
     * @param affiliationId
     *            DOCUMENT ME!
     * @param planAffiliationId
     *            DOCUMENT ME!
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void creerDocumentP1(AFRapport_P1_Doc document, String controleId) throws Exception {
        document.setParent(this);
        document.setIdControle(controleId);

        document.executeProcess();
    }

    private void creerDocumentP2(AFRapport_P2_Doc document, String controleId) throws Exception {
        document.setParent(this);
        document.setIdControle(controleId);

        document.executeProcess();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param message
     *            DOCUMENT ME!
     * @param args
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    /**
     * @return
     */
    public String getControleId() {
        return controleId;
    }

    /**
     * getter pour l'attribut date envoi.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("NAOS_IMPRESSION_RAPPORT");
    }

    public Boolean getPreRapport() {
        return preRapport;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void log(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
    }

    /**
     * @param string
     */
    public void setControleId(String string) {
        controleId = string;
    }

    /**
     * setter pour l'attribut date envoi.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setPreRapport(Boolean preRapport) {
        this.preRapport = preRapport;
    }

}
