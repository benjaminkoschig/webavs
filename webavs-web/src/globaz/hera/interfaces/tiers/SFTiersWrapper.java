/*
 * Créé le 12 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.interfaces.tiers;

import globaz.globall.shared.GlobazValueObject;
import java.util.HashMap;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class SFTiersWrapper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String EMPTY_STRING = "";

    private static final HashMap FIELDNAMES = new HashMap();

    public static final String PROPERTY_CODE_ADMINISTRATION = "CODE_ADMINISTRATION";

    public static final String PROPERTY_DATE_DECES = "DATE_DECES";

    /** DOCUMENT ME! */
    public static final String PROPERTY_DATE_NAISSANCE = "DATE_NAISSANCE";

    /** DOCUMENT ME! */
    public static final String PROPERTY_ID_CANTON = "ID_CANTON";

    /** Pays de domicile! */
    public static final String PROPERTY_ID_PAYS = "ID_PAYS";

    /** Nationalite */
    public static final String PROPERTY_ID_PAYS_DOMICILE = "ID_PAYS_DOMICILE";

    /** DOCUMENT ME! */
    public static final String PROPERTY_ID_TIERS = "ID_TIERS";

    public static final String PROPERTY_LANGUE = "LANGUE";

    /** DOCUMENT ME! */
    public static final String PROPERTY_NOM = "NOM";

    /** le numéro postal de la localité */
    public static final String PROPERTY_NPA = "NPA";

    /** DOCUMENT ME! */
    public static final String PROPERTY_NUM_AVS_ACTUEL = "NUM_AVS_ACTUEL";

    /** La propriété état civil pour une <b>PersonneAVS</b> */
    public static final String PROPERTY_PERSONNE_AVS_ETAT_CIVIL = "ETAT_CIVIL";

    /** DOCUMENT ME! */
    public static final String PROPERTY_PRENOM = "PRENOM";

    /** DOCUMENT ME! */
    public static final String PROPERTY_SEXE = "SEXE";
    /** DOCUMENT ME! */
    public static final String PROPERTY_TITRE = "TITRE";

    static final String TI_PERSONNE_AVS = "TIPersonneAvs";
    static final String TI_PERSONNE_AVS_ADRESSE = "TIPersonneAvsAdresse";

    static {
        // noms des champs pour TIPersonneAvsAdresse
        HashMap fields = new HashMap();

        fields.put(PROPERTY_DATE_NAISSANCE, "dateNaissance");
        fields.put(PROPERTY_DATE_DECES, "dateDeces");
        fields.put(PROPERTY_ID_CANTON, "idCanton");
        fields.put(PROPERTY_ID_PAYS, "idPaysTiers");
        fields.put(PROPERTY_ID_PAYS_DOMICILE, "idPays");
        fields.put(PROPERTY_ID_TIERS, "idTiers");
        fields.put(PROPERTY_NOM, "designation1_tiers");
        fields.put(PROPERTY_NPA, "npa");
        fields.put(PROPERTY_NUM_AVS_ACTUEL, "numAvsActuel");
        fields.put(PROPERTY_PERSONNE_AVS_ETAT_CIVIL, "etatCivil");
        fields.put(PROPERTY_PRENOM, "designation2_tiers");
        fields.put(PROPERTY_SEXE, "sexe");
        fields.put(PROPERTY_TITRE, "titre_tiers");
        fields.put(PROPERTY_LANGUE, "langue");

        FIELDNAMES.put(TI_PERSONNE_AVS_ADRESSE, fields);

        // noms des champs pour TIPersonneAvs
        fields = new HashMap();

        fields.put(PROPERTY_DATE_NAISSANCE, "dateNaissance");
        fields.put(PROPERTY_DATE_DECES, "dateDeces");
        fields.put(PROPERTY_ID_CANTON, "canton");
        fields.put(PROPERTY_ID_PAYS_DOMICILE, "idPays");
        fields.put(PROPERTY_ID_TIERS, "idTiers");
        fields.put(PROPERTY_NOM, "designation1");
        fields.put(PROPERTY_NPA, "npa");
        fields.put(PROPERTY_NUM_AVS_ACTUEL, "numAvsActuel");
        fields.put(PROPERTY_PERSONNE_AVS_ETAT_CIVIL, "etatCivil");
        fields.put(PROPERTY_PRENOM, "designation2");
        fields.put(PROPERTY_SEXE, "sexe");
        fields.put(PROPERTY_TITRE, "titreTiers");
        fields.put(PROPERTY_LANGUE, "langue");
        fields.put(PROPERTY_CODE_ADMINISTRATION, "codeAdministration");

        FIELDNAMES.put(TI_PERSONNE_AVS, fields);
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String type;
    private GlobazValueObject vo;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    SFTiersWrapper(GlobazValueObject vo, String type) {
        this.vo = vo;
        this.type = type;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut property
     * 
     * @param name
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut property
     */
    public String getProperty(String name) {
        Object value = vo.getProperty((String) ((HashMap) FIELDNAMES.get(type)).get(name));

        if (value != null) {
            return value.toString();
        } else {
            return EMPTY_STRING;
        }
    }
}
