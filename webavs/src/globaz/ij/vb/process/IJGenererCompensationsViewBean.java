/*
 * Créé le 12 juil. 05
 */
package globaz.ij.vb.process;

import globaz.ij.db.lots.IJLot;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererCompensationsViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static String pad0(int val) {
        if (val < 10) {
            return "0" + val;
        } else {
            return String.valueOf(val);
        }
    }

    private String csEtatLot = "";
    private String eMailAddress = "";
    private String forIdLot = "";
    private String MoisPeriodeFacturation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Vector Months = new Vector();

    /**
     * getter pour l'attribut etat lot
     * 
     * @return la valeur courante de l'attribut etat lot
     */
    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getDescriptionLot() {

        try {
            IJLot lot = new IJLot();
            lot.setSession(getSession());
            lot.setIdLot(getForIdLot());
            lot.retrieve();

            return lot.getDescription();
        } catch (Exception e) {
            return "Lot non trouvé";
        }

    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
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
     * @return
     */
    public String getMoisPeriodeFacturation() {
        return MoisPeriodeFacturation;
    }

    /**
     * @return
     */
    public Vector getMonths() {
        for (int idMois = 1; idMois <= 12; ++idMois) {
            Months.add(new String[] { pad0(idMois), pad0(idMois) });
        }
        return Months;
    }

    /**
     * setter pour l'attribut etat lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtatLot(String string) {
        csEtatLot = string;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
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
     * @param string
     */
    public void setMoisPeriodeFacturation(String string) {
        MoisPeriodeFacturation = string;
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.apg.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}
