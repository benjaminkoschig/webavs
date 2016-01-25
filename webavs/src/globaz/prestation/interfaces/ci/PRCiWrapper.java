/*
 * Créé le 12 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.interfaces.ci;

import globaz.globall.shared.GlobazValueObject;
import java.util.HashMap;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class PRCiWrapper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    static final String CI = "CompteIndividuel";

    private static final String EMPTY_STRING = "";

    private static final HashMap FIELDNAMES = new HashMap();

    /** DOCUMENT ME! */
    public static final String PROPERTY_DATE_NAISSANCE = "DATE_NAISSANCE";

    /** DOCUMENT ME! */
    public static final String PROPERTY_ID_COMPTE_INDIVIDUEL = "ID_COMPTE_INDIVIDUEL";

    /** DOCUMENT ME! */
    public static final String PROPERTY_ID_PAYS_ORIGINE = "ID_PAYS_ORIGINE";

    /** DOCUMENT ME! */
    public static final String PROPERTY_NOM_PRENOM = "NOM_PRENOM";

    /** DOCUMENT ME! */
    public static final String PROPERTY_NUMERO_SECURITE_SOCIALE = "NUMERO_SECURITE_SOCIALE";
    /** DOCUMENT ME! */
    public static final String PROPERTY_SEXE = "SEXE";

    static {
        // noms des champs pour TIPersonneAvsAdresse
        HashMap fields = new HashMap();

        fields.put(PROPERTY_NOM_PRENOM, "nomPrenom");
        fields.put(PROPERTY_SEXE, "sexe");
        fields.put(PROPERTY_ID_PAYS_ORIGINE, "paysOrigineId");
        fields.put(PROPERTY_NUMERO_SECURITE_SOCIALE, "numeroAvs");
        fields.put(PROPERTY_ID_COMPTE_INDIVIDUEL, "compteIndividuelId");
        fields.put(PROPERTY_DATE_NAISSANCE, "dateNaissance");

        FIELDNAMES.put(CI, fields);

    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String type;
    private GlobazValueObject vo;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    PRCiWrapper(GlobazValueObject vo, String type) {
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
