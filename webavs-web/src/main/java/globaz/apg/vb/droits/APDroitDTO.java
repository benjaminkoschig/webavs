/*
 * Créé le 23 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.commons.nss.NSUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.Serializable;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un simple value object de poids leger pour stocker les composantes essentielles d'un droit dans la session http lors
 * du passage aux ecrans ou le droit n'es plus stocke comme viewBean dans la session.
 * </p>
 * 
 * <p>
 * Le raisonnement qui a conduit à l'utilisation d'un tel objet est le suivant:
 * </p>
 * 
 * <ol>
 * <li>Une première idée serait de ne transmettre que l'id du droit dans les query string et comme champ caché dans les
 * formulaires. Cependant, tous les écrans ont besoin d'informations supplémentaires liées au droit. Cela signifie qu'il
 * faudrait à chaque écran charger le droit et le tiers associé, ce qui multiplie les requêtes sur la base.</li>
 * <li>Une deuxième idée serait de passer les 5 paramètres. Cela signifierait qu'il faut modifier toutes les query
 * string et tous les formulaires pour 5 paramètres, ce qui n'est pas parcimonieux non plus.</li>
 * <li>La dernière possibilité est de stocker l'information dans la session. C'est cette solution qui est permise grâce
 * à ce dto.</li>
 * </ol>
 * 
 * @author vre
 */
public class APDroitDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutDroit = "";
    private String genreService = "";
    private String idDroit = "";
    private String idTiers = "";
    private boolean modifiable;
    private String noAVS = "";
    private String nomPrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitDTO.
     */
    public APDroitDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe APDroitDTO.
     * 
     * @param droitDTO
     *            DOCUMENT ME!
     */
    public APDroitDTO(APDroitDTO droitDTO) {
        idDroit = droitDTO.idDroit;
        nomPrenom = droitDTO.nomPrenom;
        noAVS = droitDTO.noAVS;
        dateDebutDroit = droitDTO.dateDebutDroit;
        genreService = droitDTO.genreService;
        modifiable = droitDTO.modifiable;
        idTiers = droitDTO.idTiers;
    }

    /**
     * Crée une nouvelle instance de la classe APDroitDTO.
     * 
     * @param droit
     *            DOCUMENT ME!
     */
    public APDroitDTO(APDroitLAPG droit) {
        idDroit = droit.getIdDroit();
        dateDebutDroit = droit.getDateDebutDroit();
        modifiable = droit.isModifiable();
        genreService = droit.getGenreService();

        PRTiersWrapper tiers = null;

        try {
            tiers = droit.loadDemande().loadTiers();
        } catch (Exception e) {
            // Rien ne sera affiché a l'écran.
            JadeLogger.warn(this, "Initialisation du droit DTO échouée");
        }

        if (tiers != null) {
            noAVS = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            nomPrenom = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        }
        // else n'importe pas
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * getter pour l'attribut genreService
     * 
     * @return la valeur courante de l'attribut genreService
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return NSUtil.formatAVSUnknown(noAVS);
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * getter pour l'attribut modifiable
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * setter pour l'attribut date debut droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroit(String string) {
        dateDebutDroit = string;
    }

    /**
     * setter pour l'attribut genreService
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String string) {
        genreService = string;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * setter pour l'attribut prenom nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut modifiable
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifiable(boolean b) {
        modifiable = b;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

}
