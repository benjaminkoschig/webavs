package globaz.corvus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class RETerminerDemandeRentePrevBilViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateEnvoiCalcul = null;
    private String idDemandeRente = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateEnvoiCalcul() {
        return dateEnvoiCalcul;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public void setDateEnvoiCalcul(String dateEnvoiCalcul) {
        this.dateEnvoiCalcul = dateEnvoiCalcul;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    @Override
    public boolean validate() {
        // TODO Auto-generated method stub
        return true;
    }
}
