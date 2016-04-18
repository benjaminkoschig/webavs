package globaz.corvus.process;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.db.infos.PRInfoCompl;
import java.util.Iterator;

public class RETerminerDemandeRentePrevBilProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Pour setter les données depuis le helper

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEnvoiCalcul = "";
    private String idDemandeRente = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     */
    public RETerminerDemandeRentePrevBilProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public RETerminerDemandeRentePrevBilProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe REGenererListesVerificationProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public RETerminerDemandeRentePrevBilProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            REDemandeRente dm = new REDemandeRente();
            dm.setSession(getSession());
            dm.setIdDemandeRente(getIdDemandeRente());
            dm.retrieve(getTransaction());

            PRInfoCompl infoCompl = new PRInfoCompl();
            infoCompl.setSession(getSession());
            infoCompl.setIdInfoCompl(dm.getIdInfoComplementaire());
            infoCompl.retrieve(getTransaction());

            // si pas rente de veuve perdure, on change le type d'info complémentaire
            if (!IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE.equals(infoCompl
                    .getTypeInfoCompl())) {
                // J'insere "Envoyé le" dans le champs document a éditer
                // J'insere la date d'envoi du calcul entrée par l'utilisateur dans
                // le champs date edition
                infoCompl.setTypeInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_ENVOYE);
            }
            infoCompl.setDateInfoCompl(getDateEnvoiCalcul());
            infoCompl.setNoAgence("");
            infoCompl.setNoCaisse("");

            if (infoCompl.isNew()) {
                infoCompl.add(getTransaction());
                dm.setIdInfoComplementaire(infoCompl.getIdInfoCompl());

            } else {
                infoCompl.update(getTransaction());
            }

            // Je recherche toutes les bases de calcul
            REBasesCalculManager bcm = new REBasesCalculManager();
            bcm.setSession(getSession());
            bcm.setForIdRenteCalculee(dm.getIdRenteCalculee());
            bcm.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (Iterator iterator = bcm.iterator(); iterator.hasNext();) {
                REBasesCalcul baseCalcul = (REBasesCalcul) iterator.next();
                REDeleteCascadeDemandeAPrestationsDues.supprimerBaseCalculCascade_noCommit(getSession(),
                        getTransaction(), baseCalcul);
            }

            // Je set l'état de la demande de rente à terminé
            dm.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
            dm.save(getTransaction());

        } catch (Exception e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("ERROR_PROCESS_TERMINERDEMANDERENTE_PREVBIL"));

            return false;
        }

        return true;
    }

    public String getDateEnvoiCalcul() {
        return dateEnvoiCalcul;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setDateEnvoiCalcul(String dateEnvoiCalcul) {
        this.dateEnvoiCalcul = dateEnvoiCalcul;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }
}
