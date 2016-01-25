/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.lots;

import globaz.apg.db.lots.APCompensation;
import globaz.apg.db.lots.APFactureJointCompensation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRImagesConstants;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureJointCompensationViewBean extends APFactureJointCompensation implements FWViewBeanInterface {

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
    @Override
    public String getIdFacture() {
        if (getIsLigneCompensation().booleanValue()) {
            return "";
        } else {
            return super.getIdFacture();
        }
    }

    /**
     * @see globaz.apg.db.lots.APFactureACompenser#getIsCompenser()
     */
    public String getIsCompenserImageSrc() {
        if (getIsLigneCompensation().booleanValue()) {
            return PRImagesConstants.IMAGE_BLANK;
        } else {
            return getIsCompenser().booleanValue() ? PRImagesConstants.IMAGE_OK : PRImagesConstants.IMAGE_ERREUR;
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

    /**
     * Le genre de la prestation ayant engendre la compensation
     * 
     * @return
     */
    public String retrieveGenrePrestationLabel() {

        final APCompensation compensation = new APCompensation();
        compensation.setSession(getSession());
        compensation.setIdCompensation(getIdCompensation());

        try {
            compensation.retrieve();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        APTypeDePrestation typeDePRestation = null;
        if (!JadeStringUtil.isEmpty(compensation.getGenrePrestation())) {
            try {
                Integer val = Integer.valueOf(compensation.getGenrePrestation());
                typeDePRestation = APTypeDePrestation.resoudreTypeDePrestationParCodeSystem(val);
            } catch (Exception e) {
                // Nothing to do.. we have no luck :/
            }
        }

        String result = "";
        if (typeDePRestation != null) {
            result = getSession().getLabel(typeDePRestation.getNomTypePrestation());
        }
        return result;
    }
}
