package globaz.vulpecula.vb.listes;

import globaz.jade.context.JadeThread;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class PTCaissemaladieViewBean extends PTListeProcessViewBean {
    private String email;
    private String periodicite;
    private String liste;
    private String date;
    private String idCaisseMaladie;

    private List<Administration> caissesMaladies;

    @Override
    public void retrieve() throws Exception {
        caissesMaladies = VulpeculaRepositoryLocator.getAdministrationRepository().findAllCaissesMaladies();
    }

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public String getListe() {
        return liste;
    }

    public void setListe(String liste) {
        this.liste = liste;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCaisseMaladie() {
        return idCaisseMaladie;
    }

    public void setIdCaisseMaladie(String idCaisseMaladie) {
        this.idCaisseMaladie = idCaisseMaladie;
    }

    public List<Administration> getCaissesMaladies() {
        return caissesMaladies;
    }
}
