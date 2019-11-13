package globaz.prestation.interfaces.tiers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import ch.globaz.common.domaine.Date;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeDateUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Conteneur invariable de données concernant un tiers.<br/>
 * Pour charger un de ces conteneurs, voir les différentes méthodes de {@link PRTiersHelper}.
 * </p>
 * <p>
 * Implémente {@link #hashCode()} et {@link #equals(Object)} en fonction de l'id tiers, du NSS et du couple nom/prénom.
 * Peut donc être utilisé sans autre dans un {@link Set} afin de s'assurer de n'avoir qu'une seule fois chaque tiers.
 * </p>
 * <p>
 * Implémente {@link Comparable} et a donc un ordre de tri naturel (nom->prénom->idTiers par ordre alphabétique) et peut
 * donc être stocké dans un conteneur trié (tel que {@link SortedSet} ou {@link SortedMap})
 * </p>
 *
 * @author VRE
 * @author PBA
 */
public class PRTiersWrapper implements Serializable, Comparable<PRTiersWrapper> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String EMPTY_STRING = "";
    private static final Map<String, Map<String, String>> FIELDNAMES = new HashMap<String, Map<String, String>>();

    public static final String PROPERTY_CODE_ADMINISTRATION = "CODE_ADMINISTRATION";
    public static final String PROPERTY_DATE_DECES = "DATE_DECES";
    public static final String PROPERTY_DATE_NAISSANCE = "DATE_NAISSANCE";
    public static final String PROPERTY_ID_CANTON = "ID_CANTON";
    public static final String PROPERTY_ID_PAYS = "ID_PAYS";
    public static final String PROPERTY_ID_ADRESSE = "ID_ADRESSE";
    /** Nationalité */
    public static final String PROPERTY_ID_PAYS_DOMICILE = "ID_PAYS_DOMICILE";
    public static final String PROPERTY_ID_TIERS = "ID_TIERS";
    /** Indique si le tiers est actif ou non */
    public static final String PROPERTY_INACTIF = "INACTIF";
    public static final String PROPERTY_LANGUE = "LANGUE";

    public static final String PROPERTY_NOM = "NOM";
    /** le numéro postal de la localité */
    public static final String PROPERTY_NPA = "NPA";
    public static final String PROPERTY_NPA_SUP = "NPA_SUP";

    public static final String PROPERTY_NUM_AFFILIE = "NUM_AFFILIE";
    public static final String PROPERTY_NUM_AVS_ACTUEL = "NUM_AVS_ACTUEL";
    /** La propriété état civil pour une <b>PersonneAVS</b> */
    public static final String PROPERTY_PERSONNE_AVS_ETAT_CIVIL = "ETAT_CIVIL";
    public static final String PROPERTY_PRENOM = "PRENOM";
    public static final String PROPERTY_SEXE = "SEXE";
    public static final String PROPERTY_TITRE = "TITRE";
    public static final String PROPERTY_DESIGNATION_3 = "DESIGNATION3";

    static final String TI_PERSONNE_AVS = "TIPersonneAvs";
    static final String TI_PERSONNE_AVS_ADRESSE = "TIPersonneAvsAdresse";
    static final String TI_TIERSVIEWBEAN = "TITiersViewBean"; // idem TI_PERSONNE_AVS mais sans passer par le vo.

    static {
        // noms des champs pour TIPersonneAvsAdresse
        Map<String, String> fields = new HashMap<String, String>();

        fields.put(PRTiersWrapper.PROPERTY_DATE_NAISSANCE, "dateNaissance");
        fields.put(PRTiersWrapper.PROPERTY_DATE_DECES, "dateDeces");
        fields.put(PRTiersWrapper.PROPERTY_ID_CANTON, "idCanton");
        fields.put(PRTiersWrapper.PROPERTY_ID_PAYS, "idPaysTiers");
        fields.put(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE, "idPays");
        fields.put(PRTiersWrapper.PROPERTY_ID_TIERS, "idTiers");
        fields.put(PRTiersWrapper.PROPERTY_NOM, "designation1_tiers");
        fields.put(PRTiersWrapper.PROPERTY_NPA, "npa");
        fields.put(PRTiersWrapper.PROPERTY_NPA_SUP, "npa_sup");
        fields.put(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL, "numAvsActuel");
        fields.put(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL, "etatCivil");
        fields.put(PRTiersWrapper.PROPERTY_PRENOM, "designation2_tiers");
        fields.put(PRTiersWrapper.PROPERTY_SEXE, "sexe");
        fields.put(PRTiersWrapper.PROPERTY_TITRE, "titre_tiers");
        fields.put(PRTiersWrapper.PROPERTY_LANGUE, "langue");
        fields.put(PRTiersWrapper.PROPERTY_INACTIF, "inactif");
        fields.put(PRTiersWrapper.PROPERTY_NUM_AFFILIE, "numAffilieActuel");
        fields.put(PRTiersWrapper.PROPERTY_ID_ADRESSE, "idAdresseUnique");

        PRTiersWrapper.FIELDNAMES.put(PRTiersWrapper.TI_PERSONNE_AVS_ADRESSE, fields);

        // noms des champs pour TIPersonneAvs
        fields = new HashMap<String, String>();

        fields.put(PRTiersWrapper.PROPERTY_DATE_NAISSANCE, "dateNaissance");
        fields.put(PRTiersWrapper.PROPERTY_DATE_DECES, "dateDeces");
        fields.put(PRTiersWrapper.PROPERTY_ID_CANTON, "canton");
        fields.put(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE, "idPays");
        fields.put(PRTiersWrapper.PROPERTY_ID_TIERS, "idTiers");
        fields.put(PRTiersWrapper.PROPERTY_NOM, "designation1");
        fields.put(PRTiersWrapper.PROPERTY_NPA, "npa");
        fields.put(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL, "numAvsActuel");
        fields.put(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL, "etatCivil");
        fields.put(PRTiersWrapper.PROPERTY_PRENOM, "designation2");
        fields.put(PRTiersWrapper.PROPERTY_DESIGNATION_3, "designation3");
        fields.put(PRTiersWrapper.PROPERTY_SEXE, "sexe");
        fields.put(PRTiersWrapper.PROPERTY_TITRE, "titreTiers");
        fields.put(PRTiersWrapper.PROPERTY_LANGUE, "langue");
        fields.put(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION, "codeAdministration");
        fields.put(PRTiersWrapper.PROPERTY_INACTIF, "inactif");
        fields.put(PRTiersWrapper.PROPERTY_NUM_AFFILIE, "numAffilieActuel");

        PRTiersWrapper.FIELDNAMES.put(PRTiersWrapper.TI_PERSONNE_AVS, fields);
    }

    private Map<String, Map<String, String>> FIELDNAMES_VB = new HashMap<String, Map<String, String>>();

    private String type;
    private GlobazValueObject vo;

    PRTiersWrapper(GlobazValueObject vo, String type) {
        this.vo = vo;
        this.type = type;
    }

    PRTiersWrapper(TITiersViewBean vb) {
        type = PRTiersWrapper.TI_TIERSVIEWBEAN;

        Map<String, String> fields = new HashMap<String, String>();

        fields.put(PRTiersWrapper.PROPERTY_DATE_NAISSANCE, vb.getDateNaissance());
        fields.put(PRTiersWrapper.PROPERTY_DATE_DECES, vb.getDateDeces());
        fields.put(PRTiersWrapper.PROPERTY_ID_CANTON, vb.getCanton());
        fields.put(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE, vb.getIdPays());
        fields.put(PRTiersWrapper.PROPERTY_ID_PAYS, vb.getIdPays());
        fields.put(PRTiersWrapper.PROPERTY_ID_TIERS, vb.getIdTiers());
        fields.put(PRTiersWrapper.PROPERTY_NOM, vb.getDesignation1());
        fields.put(PRTiersWrapper.PROPERTY_NPA, "");
        fields.put(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL, vb.getNumAvsActuel());
        fields.put(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL, vb.getEtatCivil());
        fields.put(PRTiersWrapper.PROPERTY_PRENOM, vb.getDesignation2());
        fields.put(PRTiersWrapper.PROPERTY_SEXE, vb.getSexe());
        fields.put(PRTiersWrapper.PROPERTY_TITRE, vb.getTitreTiers());
        fields.put(PRTiersWrapper.PROPERTY_LANGUE, vb.getLangue());
        fields.put(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION, "");
        fields.put(PRTiersWrapper.PROPERTY_INACTIF, "" + vb.getInactif());

        FIELDNAMES_VB.clear();
        FIELDNAMES_VB.put(PRTiersWrapper.TI_TIERSVIEWBEAN, fields);
    }

    @Override
    public int compareTo(PRTiersWrapper unAutreTiers) {

        int compareNom = getNom().compareTo(unAutreTiers.getNom());
        if (compareNom != 0) {
            return compareNom;
        }

        int comparePrenom = getPrenom().compareTo(unAutreTiers.getPrenom());
        if (comparePrenom != 0) {
            return comparePrenom;
        }

        return getIdTiers().compareTo(unAutreTiers.getIdTiers());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PRTiersWrapper) {
            return compareTo((PRTiersWrapper) obj) == 0;
        }
        return false;
    }

    public String getCanton() {
        return getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
    }

    public String getDateDeces() {
        return getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);
    }

    public String getDateNaissance() {
        return getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
    }
    public String getDateNaissanceYYYYMMDD(){
        return new Date(getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE)).getValue();
    }

    public String getDescription(BSession session) {
        return getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - " + getProperty(PRTiersWrapper.PROPERTY_NOM)
                + " " + getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " ("
                + getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                + session.getCodeLibelle(getProperty(PRTiersWrapper.PROPERTY_SEXE)) + ")";
    }

    public String getEtatCivil() {
        return getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL);
    }

    public String getIdPays() {
        return getProperty(PRTiersWrapper.PROPERTY_ID_PAYS);
    }

    public String getIdTiers() {
        return getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
    }

    public String getLangue() {
        return getProperty(PRTiersWrapper.PROPERTY_LANGUE);
    }

    public String getNom() {
        return getProperty(PRTiersWrapper.PROPERTY_NOM);
    }

    public String getNSS() {
        return getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    }

    public String getPrenom() {
        return getProperty(PRTiersWrapper.PROPERTY_PRENOM);
    }

    /**
     * getter pour l'attribut property
     *
     * @param name
     *            DOCUMENT ME!
     *
     * @return la valeur courante de l'attribut property
     */
    public String getProperty(String name) {
        Object value = null;
        if (PRTiersWrapper.TI_TIERSVIEWBEAN.equals(type)) {
            value = (FIELDNAMES_VB.get(type)).get(name);
        } else {
            value = vo.getProperty((PRTiersWrapper.FIELDNAMES.get(type)).get(name));
        }
        if (value != null) {
            return value.toString();
        } else {
            return PRTiersWrapper.EMPTY_STRING;
        }
    }

    public String getSexe() {
        return getProperty(PRTiersWrapper.PROPERTY_SEXE);
    }

    public String getTitre() {
        return getProperty(PRTiersWrapper.PROPERTY_TITRE);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public boolean isInactif() {
        return Boolean.parseBoolean(getProperty(PRTiersWrapper.PROPERTY_INACTIF));
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(PRTiersWrapper.class.getName());
        toStringBuilder.append("(");

        toStringBuilder.append("id:").append(getIdTiers()).append(",");
        toStringBuilder.append("nss:").append(getNSS()).append(",");
        toStringBuilder.append("nom:").append(getNom()).append(",");
        toStringBuilder.append("prenom").append(getPrenom());

        toStringBuilder.append(")");
        return toStringBuilder.toString();
    }
}
