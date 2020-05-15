/*
 * Créé le 12 juil. 05
 */
package globaz.apg.vb.process;

import globaz.apg.db.lots.APLot;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererCompensationsViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static String pad0(int val) {
        if (val < 10) {
            return "0" + val;
        } else {
            return String.valueOf(val);
        }
    }

    private String eMailAddress = "";
    private String etatLot = "";
    private String forIdLot = "";
    private String MoisPeriodeFacturation = "";
    private String typePrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Vector Months = new Vector();

    public String getDescriptionLot() {
        try {
            APLot lot = new APLot();
            lot.setSession(getSession());
            lot.setId(getForIdLot());
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
     * getter pour l'attribut etat lot
     * 
     * @return la valeur courante de l'attribut etat lot
     */
    public String getEtatLot() {
        return etatLot;
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
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * setter pour l'attribut etat lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtatLot(String string) {
        etatLot = string;
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

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
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
