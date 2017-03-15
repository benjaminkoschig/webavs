/*
 * Globaz SA
 */
package ch.globaz.common.domaine;

import java.util.ArrayList;
import java.util.List;

public class Adresse {

    private final String localite;
    private final String casePostale;
    private final String attention;
    private final String npa;
    private final String pays;
    private final String csTitre;
    private final String rue;
    private final String rueNumero;
    private final String designation1;
    private final String designation2;
    private final String designation3;
    private final String designation4;
    private final String idAdresse;

    private final String csTitreTiers;
    private final String designationTiers1;
    private final String designationTiers2;
    private final String designationTiers3;
    private final String designationTiers4;

    public Adresse(String localite, String casePostale, String attention, String npa, String pays, String cantonOFAS,
            String csTitre, String rue, String rueNumero, String designation1, String designation2,
            String designation3, String designation4, String idAdresse, String designationTiers1,
            String designationTiers2, String designationTiers3, String designationTiers4, String csTitreTiers) {
        this.localite = localite;

        this.casePostale = casePostale;
        this.attention = attention;
        this.npa = npa;
        this.pays = pays;
        if (csTitre != null && !"0".equals(csTitre)) {
            this.csTitre = csTitre;
        } else {
            this.csTitre = "";
        }

        this.rue = rue;
        this.rueNumero = rueNumero;
        this.designation1 = designation1;
        this.designation2 = designation2;
        this.designation3 = designation3;
        this.designation4 = designation4;
        this.idAdresse = idAdresse;

        this.designationTiers1 = designationTiers1;
        this.designationTiers2 = designationTiers2;
        this.designationTiers3 = designationTiers3;
        this.designationTiers4 = designationTiers4;
        this.csTitreTiers = csTitreTiers;

    }

    public Adresse() {
        localite = "";
        casePostale = "";
        attention = "";
        npa = "";
        pays = "";
        csTitre = "";
        rue = "";
        rueNumero = "";
        designation1 = "";
        designation2 = "";
        designation3 = "";
        designation4 = "";
        idAdresse = "";
        designationTiers1 = "";
        designationTiers2 = "";
        designationTiers3 = "";
        designationTiers4 = "";
        csTitreTiers = "";
    }

    String resolveDesignation(String designationAdresse, String designationTiers) {
        if (isNotEmpty(designation1)) {
            return designationAdresse;
        }
        return designationTiers;
    }

    public String resolveDesignation1() {
        return resolveDesignation(designation1, designationTiers1);
    }

    public String resolveDesignation2() {
        return resolveDesignation(designation2, designationTiers2);
    }

    public String resolveDesignation3() {
        return resolveDesignation(designation3, designationTiers3);
    }

    public String resolveDesignation4() {
        return resolveDesignation(designation4, designationTiers4);
    }

    public String resolveCsTitre() {
        if (isNotEmpty(designation1)) {
            return csTitre;
        } else {
            return csTitreTiers;
        }
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getDesignation3() {
        return designation3;
    }

    public String getDesignation4() {
        return designation4;
    }

    public List<String> getAdresseAsStringLines() {
        List<String> liste = new ArrayList<String>();

        if (isNotEmpty(designation3) && isNotEmpty(designation4)) {
            liste.add(designation3 + " " + designation4);
        } else if (isNotEmpty(designation3)) {
            liste.add(designation3);
        } else if (isNotEmpty(designation4)) {
            liste.add(designation4);
        }

        if (isNotEmpty(attention)) {
            liste.add(attention);
        }

        if (isNotEmpty(rue)) {
            if (isNotEmpty(rueNumero)) {
                liste.add(rue + " " + rueNumero);
            } else {
                liste.add(rue);
            }
        }

        if (isNotEmpty(casePostale)) {
            liste.add(casePostale);
        }

        if (isNotEmpty(npa) && isNotEmpty(localite)) {
            liste.add(npa + " " + localite);
        }
        return liste;
    }

    public String getAdresseFormatte() {
        StringBuilder adressAsString = new StringBuilder();
        for (String ligne : getAdresseAsStringLines()) {
            adressAsString.append(ligne);
            adressAsString.append("\n");
        }
        return adressAsString.toString();
    }

    private boolean isNotEmpty(final String stringToTest) {
        return stringToTest != null && !stringToTest.trim().isEmpty();
    }

    public String getRueNumero() {
        return rueNumero;
    }

    /**
     * Retourne la rue
     * 
     * @return String représentant la rue
     */
    public String getRue() {
        return rue;
    }

    /**
     * Retourne la localite
     * 
     * @return String représentant la localité
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * Retourne la case postale
     * 
     * @return String représentant la case postale
     */
    public String getCasePostale() {
        return casePostale;
    }

    /**
     * Retourne la partie "A l'attention" de l'adresse
     * 
     * @return String représentant "A l'attention"
     */
    public String getAttention() {
        return attention;
    }

    /**
     * Retourne la npa
     * 
     * @return String représentant le npa
     */
    public String getNpa() {
        return npa;
    }

    /**
     * Retourne le pays
     * 
     * @return String représentant le pays
     */
    public String getPays() {
        return pays;
    }

    /**
     * @return the csTitre
     */
    public String getCsTitre() {
        return csTitre;
    }

    public String getIdAdresse() {
        return idAdresse;
    }

    @Override
    public String toString() {
        return "Adresse [localite=" + localite + ", casePostale=" + casePostale + ", attention=" + attention + ", npa="
                + npa + ", pays=" + pays + ", csTitre=" + csTitre + ", rue=" + rue + ", rueNumero=" + rueNumero
                + ", designation1=" + designation1 + ", designation2=" + designation2 + ", designation3="
                + designation3 + ", designation4=" + designation4 + ", idAdresse=" + idAdresse + "]";
    }

}
