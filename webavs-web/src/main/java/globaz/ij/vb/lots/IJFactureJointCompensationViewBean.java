/*
 * Créé le 06 octobre 05
 */
package globaz.ij.vb.lots;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.db.lots.IJFactureJointCompensation;
import globaz.prestation.tools.PRImagesConstants;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureJointCompensationViewBean extends IJFactureJointCompensation implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.apg.db.lots.APFactureJointCompensation#getDettes()
     */
    @Override
    public String getDettes() throws Exception {
        return JANumberFormatter.fmt(super.getDettes(), true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.lots.APFactureACompenser#getIdFacture()
     */
    public String getIdFacture() {
        if (getIsLigneCompensation().booleanValue()) {
            return "";
        } else {
            return super.getIdFactureCompta();
        }
    }

    /**
     * @see globaz.apg.db.lots.APFactureACompenser#getIsCompenser()
     */
    public String getIsCompenserImageSrc() {
        if (getIsLigneCompensation().booleanValue()) {
            return PRImagesConstants.IMAGE_BLANK;
        } else {
            return getIsCompense().booleanValue() ? PRImagesConstants.IMAGE_OK : PRImagesConstants.IMAGE_ERREUR;
        }
    }

    /**
     * @see globaz.apg.db.lots.APFactureACompenser#getMontant()
     */
    @Override
    public String getMontant() {
        if (getIsLigneCompensation().booleanValue()) {
            return "";
        } else {
            return JANumberFormatter.fmt(super.getMontant(), true, true, true, 2);
        }
    }

    /**
     * @see globaz.apg.db.lots.APFactureJointCompensation#getMontantTotal()
     */
    @Override
    public String getMontantTotal() {
        if (getIsLigneCompensation().booleanValue()) {
            return JANumberFormatter.fmt(super.getMontantTotal(), true, true, true, 2);
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut no facture
     * 
     * @return la valeur courante de l'attribut no facture
     */
    @Override
    public String getNoFacture() {
        if (getIsLigneCompensation().booleanValue()) {
            return "";
        } else {
            return super.getNoFacture();
        }
    }
}
