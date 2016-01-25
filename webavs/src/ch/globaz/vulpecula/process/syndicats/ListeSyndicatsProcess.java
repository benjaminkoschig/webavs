package ch.globaz.vulpecula.process.syndicats;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.syndicat.AffiliationSyndicatService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public abstract class ListeSyndicatsProcess extends BProcessWithContext {
    private static final long serialVersionUID = -6695182440313223821L;

    protected String idSyndicat;
    protected String idCaisseMetier;
    protected Annee annee;
    protected Map<Administration, Map<Administration, List<AffiliationSyndicat>>> affiliationsGroupBySyndicat;
    protected Administration syndicat;
    protected List<String> listeErreur = new ArrayList<String>();

    protected AffiliationSyndicatService affiliationSyndicatService = VulpeculaServiceLocator
            .getAffiliationSyndicatService();

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        try {
            retrieve();
            if (!listeErreur.isEmpty()) {
                String erreurMessage = getSession().getLabel("CAISSE_PRINCIPALE_LISTE_SYNDICATS_ERREUR") + "\n";
                for (String nomTravailleurErreur : listeErreur) {
                    erreurMessage += nomTravailleurErreur + "\n";
                }
                getMemoryLog().logMessage(erreurMessage, FWMessage.ERREUR, this.getClass().getName());
            }

            print();
        } catch (IllegalStateException ex) {
            getTransaction().addErrors(ex.getMessage());
        }
        return true;
    }

    protected void retrieve() {
        affiliationsGroupBySyndicat = affiliationSyndicatService
                .findByAnneeWithCumulSalaireGroupBySyndicatAndCaisseMetier(idSyndicat, idCaisseMetier, getAnnee(),
                        listeErreur);
    }

    protected abstract void print() throws Exception;

    public String getIdSyndicat() {
        return idSyndicat;
    }

    public void setIdSyndicat(String idSyndicat) {
        this.idSyndicat = idSyndicat;
    }

    public String getIdCaisseMetier() {
        return idCaisseMetier;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}
