package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.naos.itext.controleEmployeur.AFLettreLibre;

public class AFImprimerLettreLibreProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String controleId;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String email;
    private Boolean preRapport = new Boolean(false);
    private String textelibre;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
	 * 
	 */
    public AFImprimerLettreLibreProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param parent
     */
    public AFImprimerLettreLibreProcess(BProcess parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param session
     */
    public AFImprimerLettreLibreProcess(BSession session) {
        super(session);
        // TODO Auto-generated constructor stub
    }

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
        // try {

        AFLettreLibre doc = new AFLettreLibre();
        doc.setSession(getSession());
        doc.setIdControle(getControleId());
        doc.setDeleteOnExit(false);
        doc.setEMailAddress(getEMailAddress());
        doc.setDateEnvoi(getDateEnvoi());
        doc.setTextelibre(getTextelibre());
        doc.setParent(this);
        doc.executeProcess();

        return false;
        // }
    }

    private JACalendar calendar() throws Exception {
        return getSession().getApplication().getCalendar();
    }

    /**
     * @return
     */
    public String getControleId() {
        return controleId;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
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

    public String getEmail() {
        return email;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // return getSession().getLabel("NAOS_IMPRESSION_LETTRE_LIBRE");
        return null;
    }

    public Boolean getPreRapport() {
        return preRapport;
    }

    public String getTextelibre() {
        return textelibre;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPreRapport(Boolean preRapport) {
        this.preRapport = preRapport;
    }

    public void setTextelibre(String textelibre) {
        this.textelibre = textelibre;
    }

}
