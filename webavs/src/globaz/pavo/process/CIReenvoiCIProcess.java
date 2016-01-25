package globaz.pavo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIRassemblementOuverture;

/**
 * @author dgi
 * @since 19.05.05
 * 
 *        Envoi de CI sur la base de l'historique des annonces sur les CI
 * 
 */
public class CIReenvoiCIProcess extends BProcess {

    private static final long serialVersionUID = -5723739850499981072L;
    /** Type de personne */
    public static int ASSURE = 0;
    /** Type de personne */
    public static int CONJOINT = 1;
    /** annonce à envoyer */
    private CIRassemblementOuverture annonce = null;
    // nvx framework plus d'objet en paramètre
    private String idAnnonce = "";
    /** assuré/conjoint */
    private int person = ASSURE;

    public CIReenvoiCIProcess() {
        super();
    }

    public CIReenvoiCIProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Raccord de méthode auto-généré

    }

    @Override
    protected boolean _executeProcess() {

        annonce = new CIRassemblementOuverture();
        annonce.setRassemblementOuvertureId(getIdAnnonce());
        annonce.setSession(getSession());
        try {
            annonce.retrieve();
        } catch (Exception e1) {
            JadeLogger.error(this, e1);
        }
        if (annonce.isNew()) {
            return false;
        }
        if (annonce == null) {
            return false;
        }
        try {
            if (person == ASSURE) {
                annonce.annonceEcritures(getTransaction(), "True");
            } else {
                annonce.annonceEcritures(getTransaction(), "False");
            }
            if (!getTransaction().hasErrors()) {
                setSendCompletionMail(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !isOnError();
    }

    @Override
    protected void _validate() throws Exception {
        // TODO Raccord de méthode auto-généré
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("MSG_REENVOI_EMAIL") + " " + JAUtil.formatAvs(annonce.getNumeroAvs());
    }

    /**
     * @return
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    public int getPerson() {
        return person;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Raccord de méthode auto-généré
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param ouverture
     *            enregistrement de l'historique du CI
     */
    public void setAnnonce(CIRassemblementOuverture annonceCI) {
        annonce = annonceCI;
    }

    /**
     * @param string
     */
    public void setIdAnnonce(String string) {
        idAnnonce = string;
    }

    /**
     * @param string
     *            le type de personne <code>ASSURE</code> ou <code>CONJOINT</code>
     */
    public void setPerson(int type) {
        person = type;
    }

}
