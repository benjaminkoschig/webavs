/*
 * Créé le 8 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.interfaces.tiers;

import globaz.globall.shared.GlobazValueObject;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ISFTiersImpl implements ISFTiers {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    protected static final String EMPTY_STRING = "";

    protected static final String PROPERTY_DATE_DECES = "dateDeces";

    /**
     */
    protected static final String PROPERTY_DATE_NAISSANCE = "dateNaissance";

    /**
     */
    protected static final String PROPERTY_ID_CANTON = "idCanton";

    /**
     */
    protected static final String PROPERTY_ID_PAYS = "idPays";

    /**
     */
    protected static final String PROPERTY_ID_TIERS = "idTiers";

    /**
     */
    protected static final String PROPERTY_LANGUE = "langue";

    /**
     */
    protected static final String PROPERTY_NOM = "designation1_tiers";

    /**
     */
    protected static final String PROPERTY_NUM_AFFILIE = "numAffilie";

    /**
     */
    protected static final String PROPERTY_NUM_AVS_ACTUEL = "numAvsActuel";

    /**
     */
    protected static final String PROPERTY_PRENOM = "designation2_tiers";

    /**
     */
    protected static final String PROPERTY_SEXE = "sexe";

    /**
     */
    protected static final String PROPERTY_TITRE = "titre";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private GlobazValueObject vo;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IPRTiersImpl.
     * 
     * @param vo
     *            DOCUMENT ME!
     */
    protected ISFTiersImpl(GlobazValueObject vo) {
        this.vo = vo;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public String getDateDeces() {
        return getProperty(PROPERTY_DATE_DECES);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getDateNaissance()
     */
    @Override
    public String getDateNaissance() {
        return getProperty(PROPERTY_DATE_NAISSANCE);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getIdCanton()
     */
    @Override
    public String getIdCanton() {
        return getProperty(PROPERTY_ID_CANTON);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getIdPays()
     */
    @Override
    public String getIdPays() {
        return getProperty(PROPERTY_ID_PAYS);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getIdTiers()
     */
    @Override
    public String getIdTiers() {
        return getProperty(PROPERTY_ID_TIERS);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getLangue()
     */
    @Override
    public String getLangue() {
        return getProperty(PROPERTY_LANGUE);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getNoAVS()
     */
    @Override
    public String getNoAVS() {
        return getProperty(PROPERTY_NUM_AVS_ACTUEL);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getNom()
     */
    @Override
    public String getNom() {
        return getProperty(PROPERTY_NOM);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getPrenom()
     */
    @Override
    public String getPrenom() {
        return getProperty(PROPERTY_PRENOM);
    }

    /**
     * getter pour l'attribut property
     * 
     * @param name
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut property
     */
    protected String getProperty(String name) {
        Object value = vo.getProperty(name);

        if (value != null) {
            return value.toString();
        } else {
            return EMPTY_STRING;
        }
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getSexe()
     */
    @Override
    public String getSexe() {
        return getProperty(PROPERTY_SEXE);
    }
}
