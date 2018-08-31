package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTListePersonnesAvecAgeViewBean extends BJadeSearchObjectELViewBean {

    private String email;
    private String annee;

    private boolean launched = false;

    /**
     * Méthode utilisée par le framework pour setter l'email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public String getAnnee() {
        if (annee == null) {
            annee = new Annee(Date.getCurrentYear()).toString();
        }
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }
}
