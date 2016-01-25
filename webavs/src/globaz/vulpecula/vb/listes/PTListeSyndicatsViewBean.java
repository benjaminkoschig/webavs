package globaz.vulpecula.vb.listes;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class PTListeSyndicatsViewBean extends PTListeProcessViewBean {
    private String liste;
    private String idSyndicat;
    private String idCaisseMetier;
    private String annee;

    private List<Administration> syndicats;
    private List<Administration> caissesMetiers;

    @Override
    public void retrieve() throws Exception {
        syndicats = VulpeculaRepositoryLocator.getAdministrationRepository().findAllSyndicats();
        caissesMetiers = VulpeculaRepositoryLocator.getAdministrationRepository().findAllCaissesMetiers();
    }

    public String getListe() {
        return liste;
    }

    public void setListe(String liste) {
        this.liste = liste;
    }

    public String getIdSyndicat() {
        return idSyndicat;
    }

    public String getIdCaisseMetier() {
        return idCaisseMetier;
    }

    public void setIdSyndicat(String idSyndicat) {
        this.idSyndicat = idSyndicat;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }

    public List<Administration> getSyndicats() {
        return syndicats;
    }

    public List<Administration> getCaissesMetiers() {
        return caissesMetiers;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getCurrentYear() {
        return Date.now().getAnnee();
    }
}
