package globaz.apg.pojo.wrapper;

import globaz.apg.api.prestation.IAPPrestation;

/**
 * Descpription Prestation calcul�e par le syst�me pour une date de d�but et date de fin donn�e.Prestation calcul�e par
 * le syst�me pour une date de d�but et date de fin donn�e.
 * 
 * @author $user$
 */
public class APPrestationWrapper implements IAPPrestation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String dateDebut = "";
    protected String idDroit = "";
    private String idPrestation = "";

    /**
     * @return
     */
    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    @Override
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return
     */
    @Override
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * @param string
     */
    public void setIdPrestation(String string) {
        idPrestation = string;
    }

}
