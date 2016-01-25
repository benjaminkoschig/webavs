package globaz.helios.db.modeles;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:50)
 * 
 * @author: Administrator
 */

public class CGLigneModeleEcritureViewBean extends CGLigneModeleEcriture implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idMandat = new String();
    private String idModeleEcriture = new String();
    private Boolean saisieEcran = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CGModeleEcritureViewBean.
     */
    public CGLigneModeleEcritureViewBean() {
        super();
    }

    public CGEnteteModeleEcritureViewBean getEnteteViewBean() {
        if (!JadeStringUtil.isIntegerEmpty(getIdEnteteModeleEcriture())) {
            CGEnteteModeleEcritureViewBean entete = new CGEnteteModeleEcritureViewBean();
            entete.setSession(getSession());
            entete.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());

            try {
                entete.retrieve();
            } catch (Exception e) {
                return null;
            }

            if (entete.isNew()) {
                return null;
            }

            return entete;
        } else {
            return null;
        }
    }

    public String getFormattedMontant() {
        return JANumberFormatter.fmt(getMontant(), true, true, false, 2);
    }

    public String getFormattedMontantMonnaie() {
        return JANumberFormatter.fmt(getMontantMonnaie(), true, true, false, 2);
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idModeleEcriture.
     * 
     * @return String
     */
    public String getIdModeleEcriture() {
        return idModeleEcriture;
    }

    public Boolean getSaisieEcran() {
        return saisieEcran;
    }

    public CGModeleEcriture retrieveModeleEcriture() {

        if (JadeStringUtil.isBlank(getIdModeleEcriture())) {
            return null;
        } else {
            CGModeleEcriture modele = new CGModeleEcriture();
            modele.setSession(getSession());
            modele.setIdModeleEcriture(getIdModeleEcriture());

            try {
                modele.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return modele;
        }
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idModeleEcriture.
     * 
     * @param idModeleEcriture
     *            The idModeleEcriture to set
     */
    public void setIdModeleEcriture(String idModeleEcriture) {
        this.idModeleEcriture = idModeleEcriture;
    }

    public void setSaisieEcran(String newSaisieEcran) {
        try {
            saisieEcran = Boolean.valueOf(newSaisieEcran);
        } catch (Exception ex) {
            saisieEcran = new Boolean(false);
        }
    }

}
