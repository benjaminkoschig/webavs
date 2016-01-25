/*
 * Créé le 7 oct. 05
 */
package globaz.ij.vb.process;

import globaz.globall.db.BSession;
import globaz.ij.db.lots.IJLot;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJListeControleViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatLot = "";
    private String descriptionLot = null;
    private String eMailAddress = "";
    private String idLot = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut etat lot
     * 
     * @return la valeur courante de l'attribut etat lot
     */
    public String getCsEtatLot() {
        return csEtatLot;
    }

    /**
     * getter pour l'attribut description lot
     * 
     * @return la valeur courante de l'attribut description lot
     */
    public String getDescriptionLot() {
        if (descriptionLot == null) {
            BSession session = (BSession) getISession();

            try {
                IJLot lot = new IJLot();

                lot.setSession(session);
                lot.setIdLot(idLot);
                lot.retrieve();
                descriptionLot = lot.getDescription();
            } catch (Exception e) {
                descriptionLot = session.getLabel("ECHEC_CHARGER_DESCRIPTION_LOT");
            }
        }

        return descriptionLot;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        if (JadeStringUtil.isEmpty(eMailAddress)) {
            eMailAddress = getSession().getUserEMail();
        }

        return eMailAddress;
    }

    /**
     * getter pour l'attribut no lot
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getIdLot() {
        return idLot;
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
     * setter pour l'attribut description lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionLot(String string) {
        descriptionLot = string;
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
     * setter pour l'attribut no lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
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
