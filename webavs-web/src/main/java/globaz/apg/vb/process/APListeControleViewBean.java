/*
 * Créé le 30 nov. 06
 */
package globaz.apg.vb.process;

import globaz.apg.db.lots.APLot;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class APListeControleViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatLot = "";
    private String descriptionLot = null;
    private String eMailAddress = "";
    private String idLot = "";
    private String typeImpression = "pdf";


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
                APLot lot = new APLot();

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


    public String getTypeImpression() {
        return typeImpression;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
