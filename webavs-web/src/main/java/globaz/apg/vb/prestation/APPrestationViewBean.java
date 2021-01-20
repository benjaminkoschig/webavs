/*
 * Créé le 1 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.prestation;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APPrestationViewBean extends APPrestation implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean calculACOR = false;
    private boolean calculMATCIAB2 = false;

    private transient APDroitLAPG droit = null;
    private APDroitDTO droitDTO = null;

    private boolean erreurCalcul;
    private transient String genreService = "";

    private String idPrestationPrecedante = "";

    private String idPrestationSuivante = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        if (droitDTO != null) {
            return droitDTO.getDateDebutDroit();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut genre service
     * 
     * @return la valeur courante de l'attribut genre service
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * getter pour l'attribut id prestation precedante
     * 
     * @return la valeur courante de l'attribut id prestation precedante
     */
    public String getIdPrestationPrecedante() {
        return idPrestationPrecedante;
    }

    /**
     * getter pour l'attribut id prestation suivante
     * 
     * @return la valeur courante de l'attribut id prestation suivante
     */
    public String getIdPrestationSuivante() {
        return idPrestationSuivante;
    }

    /**
     * getter pour l'attribut no AVSDroit
     * 
     * @return la valeur courante de l'attribut no AVSDroit
     */
    public String getNoAVSDroit() {
        if (droitDTO != null) {
            return droitDTO.getNoAVS();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut nom prenom droit
     * 
     * @return la valeur courante de l'attribut nom prenom droit
     */
    public String getNomPrenomDroit() {
        if (droitDTO != null) {
            return droitDTO.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut calcul ACOR
     * 
     * @return la valeur courante de l'attribut calcul ACOR
     */
    public boolean isCalculACOR() {
        return calculACOR;
    }

    /**
     * getter pour l'attribut calcul MATCIAB2
     *
     * @return la valeur courante de l'attribut calcul MATCIAB2
     */
    public boolean isCalculMATCIAB2() {
        return calculMATCIAB2;
    }

    /**
     * getter pour l'attribut derniere prestation
     * 
     * @return la valeur courante de l'attribut derniere prestation
     */
    public boolean isDernierePrestation() {
        return JadeStringUtil.isEmpty(idPrestationSuivante);
    }

    /**
     * getter pour l'attribut erreur calcul
     * 
     * @return la valeur courante de l'attribut erreur calcul
     */
    public boolean isErreurCalcul() {
        return erreurCalcul;
    }

    /**
     * getter pour l'attribut premiere prestation
     * 
     * @return la valeur courante de l'attribut premiere prestation
     */
    public boolean isPremierePrestation() {
        return JadeStringUtil.isEmpty(idPrestationPrecedante);
    }

    /**
     * getter pour l'attribut validee
     * 
     * @return la valeur courante de l'attribut validee
     */
    public boolean isValidee() {
        return IAPPrestation.CS_ETAT_PRESTATION_VALIDE.equals(getEtat());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APDroitLAPG loadDroit() throws Exception {
        if ((droit == null) || !droit.getIdDroit().equals(idDroit)) {
            droit = APGUtils.loadDroit(getSession(), idDroit, genreService);
        }
        return droit;
    }

    /**
     * setter pour l'attribut calcul ACOR
     * 
     * @param calculACOR
     *            une nouvelle valeur pour cet attribut
     */
    public void setCalculACOR(boolean calculACOR) {
        this.calculACOR = calculACOR;
    }

    /**
     * setter pour l'attribut calcul MATCIAB2
     *
     * @param calculMATCIAB2
     *            une nouvelle valeur pour cet attribut
     */
    public void setCalculMATCIAB2(boolean calculMATCIAB2) {
        this.calculMATCIAB2 = calculMATCIAB2;
    }

    /**
     * setter pour l'attribut droit dto
     * 
     * @param droitDTO
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitDto(APDroitDTO droitDTO) {
        this.droitDTO = droitDTO;

        if (droitDTO != null) {
            idDroit = droitDTO.getIdDroit();
            genreService = droitDTO.getGenreService();
        }
    }

    /**
     * setter pour l'attribut erreur calcul
     * 
     * @param erreurCalcul
     *            une nouvelle valeur pour cet attribut
     */
    public void setErreurCalcul(boolean erreurCalcul) {
        this.erreurCalcul = erreurCalcul;
    }

    /**
     * setter pour l'attribut genre service
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String string) {
        genreService = string;
    }

    /**
     * setter pour l'attribut id prestation precedante
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestationPrecedante(String string) {
        idPrestationPrecedante = string;
    }

    /**
     * setter pour l'attribut id prestation suivante
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestationSuivante(String string) {
        idPrestationSuivante = string;
    }
}
