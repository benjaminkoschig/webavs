/*
 * Créé le 23 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import ch.globaz.al.businessimpl.services.compensation.CompensationFactureServiceImpl;
import ch.globaz.common.util.Dates;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(APDroitDTO.class);

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    protected String nom;
    protected String prenom;
    protected String csSexe;
    protected String csEtatCivil;
    protected String dateNaissance;
    protected String npa;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutDroitDelaiCadre = "";
    private String dateDebutDroit = "";
    private String genreService = "";
    private String idDroit = "";
    private String idTiers = "";
    private boolean modifiable;
    private String noAVS = "";
    private String nomPrenom = "";
    private String canton = "";

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
        dateDebutDroitDelaiCadre = droitDTO.dateDebutDroitDelaiCadre;
        genreService = droitDTO.genreService;
        modifiable = droitDTO.modifiable;
        idTiers = droitDTO.idTiers;
        npa = droitDTO.npa;
        canton = droitDTO.canton;
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
        npa = droit.getNpa();

        if(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(genreService) && droit instanceof APDroitProcheAidant){
            dateDebutDroitDelaiCadre = ((APDroitProcheAidant) droit).resolveDateDebutDelaiCadre().map(Dates::formatSwiss)
                    .orElse("");
        }
        PRTiersWrapper tiers = null;

        try {
            tiers = droit.loadDemande().loadTiers();
        } catch (Exception e) {
            // Rien ne sera affiché a l'écran.
            JadeLogger.warn(this, "Initialisation du droit DTO échouée");
        }

        if (tiers != null) {
            noAVS = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            nom = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
            prenom = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            csSexe = tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE);
            csEtatCivil = tiers.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL);
            dateNaissance = tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
            nomPrenom = nom + " " + prenom;
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
     * getter pour l'attribut date debut droit
     *
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroitDelaiCadre() {
        return dateDebutDroitDelaiCadre;
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
     * setter pour l'attribut date debut droit delai cadre
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroitDelaiCadre(String string) {
        dateDebutDroitDelaiCadre = string;
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
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut modifiable
     * 
     * @param modifiable une nouvelle valeur pour cet attribut
     */
    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
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

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getCanton(BSession session) {
        if (npa != null && !npa.isEmpty()) {
            TILocaliteManager localiteManager = new TILocaliteManager();
            localiteManager.setSession(session);
            if (npa.length() == 4) {
                localiteManager.setForNumPostal(npa + "00");
            } else {
                localiteManager.setForNumPostal(npa);
            }
            try {
                localiteManager.find(1);
                if (localiteManager.size() > 0) {

                    TILocalite localite = (TILocalite) localiteManager.getFirstEntity();
                    return localite.getIdCanton();
                }

            } catch (Exception e) {
                LOG.error("Impossible de récupérer la localité pour le NPA ("+npa+") : " + e.getMessage());
            }
        }

        return null;
    }


    public void setCanton(String canton) {
        this.canton = canton;
    }
}
