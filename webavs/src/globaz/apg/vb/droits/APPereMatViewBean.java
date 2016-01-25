/*
 * Créé le 23 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APPereMat;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APPereMatViewBean extends APPereMat implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private APDroitDTO droitDTO = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut droit mat
     * 
     * @return la valeur courante de l'attribut date debut droit mat
     */
    public String getDateDebutDroitMat() {
        if (droitDTO != null) {
            return droitDTO.getDateDebutDroit();
        } else {
            return "";
        }
    }

    /**
     * @see globaz.apg.db.droits.APSituationFamilialeMat#getDateNaissance()
     */
    @Override
    public String getDateNaissance() {
        if (JAUtil.isDateEmpty(dateNaissance)) {
            dateNaissance = JACalendar.todayJJsMMsAAAA();
        }

        return dateNaissance;
    }

    /**
     * (non javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.apg.db.droits.APSituationFamilialeMat#getIdDroitMaternite()
     */
    @Override
    public String getIdDroitMaternite() {
        return super.getIdDroitMaternite();
    }

    /**
     * getter pour l'attribut no AVSDroit mat
     * 
     * @return la valeur courante de l'attribut no AVSDroit mat
     */
    public String getNoAVSDroitMat() {
        if (droitDTO != null) {
            return droitDTO.getNoAVS();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut nom prenom droit mat
     * 
     * @return la valeur courante de l'attribut nom prenom droit mat
     */
    public String getNomPrenomDroitMat() {
        if (droitDTO != null) {
            return droitDTO.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * retourne le libellé du type ou (si le type est null) renvoie le type par défaut: type_pere.
     * 
     * @return la valeur courante de l'attribut type
     */
    public String getTypeLibelle() {
        if (JadeStringUtil.isEmpty(type)) {
            return getSession().getCodeLibelle(IAPDroitMaternite.CS_TYPE_PERE);
        } else {
            return getSession().getCodeLibelle(type);
        }
    }

    /**
     * getter pour l'attribut modifiable
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {
        if (droitDTO != null) {
            return droitDTO.isModifiable();
        } else {
            return false;
        }
    }

    /**
     * setter pour l'attribut droit DTO
     * 
     * @param droitDTO
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitDTO(APDroitDTO droitDTO) {
        this.droitDTO = droitDTO;

        if (droitDTO != null) {
            idDroitMaternite = droitDTO.getIdDroit();
        }
    }
}
