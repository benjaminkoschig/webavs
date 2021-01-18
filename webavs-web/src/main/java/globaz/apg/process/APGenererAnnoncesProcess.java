/*
 * Créé le 16 juin 05
 */
package globaz.apg.process;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APDroitPandemie;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.utils.APGenerateurAnnonceRAPG;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import java.text.MessageFormat;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererAnnoncesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = "";
    private String moisAnneeComptable = "";

    /**
     * Crée une nouvelle instance de la classe APGenererAnnoncesProcess.
     */
    public APGenererAnnoncesProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererAnnoncesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APGenererAnnoncesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererAnnoncesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APGenererAnnoncesProcess(BSession session) {
        super(session);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        try {
            // récupération de chaque prestation.
            APPrestationManager managerPrestation = new APPrestationManager();
            managerPrestation.setSession(session);
            managerPrestation.setForIdLot(forIdLot);

            // managerPrestation.setForPrestationsSansAnnonce(true);
            managerPrestation.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
            Boolean hasComplementCIAB = Boolean.FALSE; // jamais de prestations complément CIAB comme on ne retourne que les prestations STANDARD
            BStatement statement = managerPrestation.cursorOpen(transaction);

            APPrestation prestation = null;
            APGenerateurAnnonceRAPG generateurAnnonceRAPG = new APGenerateurAnnonceRAPG();
            while ((prestation = (APPrestation) managerPrestation.cursorReadNext(statement)) != null) {
                APAnnonceAPG annonceACreer = generateurAnnonceRAPG.createAnnonceSedex(session, prestation,
                        getDroit(prestation), moisAnneeComptable, hasComplementCIAB);
                annonceACreer.add(transaction);
                getMemoryLog().logMessage(
                        MessageFormat.format(getSession().getLabel("ANNONCE_AJOUTEE"),
                                new Object[] { annonceACreer.getIdAnnonce() }), FWMessage.INFORMATION,
                        getSession().getLabel("GENERER_ANNONCES"));

                // mise a jour du l'id annonce de la prestation.
                prestation.setIdAnnonce(annonceACreer.getIdAnnonce());
                prestation.update(transaction);
            } // fin de boucle for
        } catch (Exception e) {
            try {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("GENERER_ANNONCES"));
                // transaction.rollback();
            } catch (Exception e1) {
                return false;
            }
            return false;
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }
            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }

        if (JadeStringUtil.isEmpty(moisAnneeComptable)) {
            this._addError(getTransaction(), getSession().getLabel("MOIS_ANNEE_COMPTABLE_OBLIGATOIRE"));
        }
    }

    private APDroitLAPG getDroit(APPrestation prestation) throws Exception {
        APDroitLAPG droit = null;

        if (APTypeDePrestation.PANDEMIE.getCodesystemString().equals(prestation.getGenre())) {
            droit = new APDroitPandemie();
        } else if (prestation.getNoRevision().equals(IAPDroitMaternite.CS_REVISION_MATERNITE_2005)) {
            droit = new APDroitMaternite();
        } else {
            droit = new APDroitAPG();
        }

        droit.setSession(getSession());
        droit.setIdDroit(prestation.getIdDroit());
        droit.retrieve(getTransaction());
        return droit;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return "";
        // ce process n'envoi pas de mails (c'est un fils de générer
        // communications)
    }

    /**
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * getter pour l'attribut mois année comptable
     * 
     * @return la valeur courante de l'attribut mois annee comptable
     */
    public String getMoisAnneeComptable() {
        return moisAnneeComptable;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /**
     * setter pour l'attribut mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMoisAnneeComptable(String string) {
        moisAnneeComptable = string;
    }
}
