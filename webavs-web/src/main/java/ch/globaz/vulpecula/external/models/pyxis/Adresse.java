/**
 * 
 */
package ch.globaz.vulpecula.external.models.pyxis;

import java.util.ArrayList;
import java.util.List;

/**
 * Représentation métier d'une adresse au sens du module Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public class Adresse {
    private String localite;
    private String idLocalite;
    private String cantonCourt;
    private String canton;
    private String casePostale;
    private String attention;
    private String npa;
    private String pays;
    private String isoPays;
    private String cantonOFAS;
    private String description1;
    private String description2;
    private String description3;
    private String description4;
    private String csTitre;
    private String titre;

    private String rue;
    private String rueNumero;

    public List<String> getAdresseAsStringLines() {
        List<String> liste = new ArrayList<String>();

        if (isNotEmpty(titre)) {
            liste.add(titre);
        }

        if (isNotEmpty(description1) && isNotEmpty(description2)) {
            liste.add(description1 + " " + description2);
        } else if (isNotEmpty(description1)) {
            liste.add(description1);
        } else if (isNotEmpty(description2)) {
            liste.add(description2);
        }

        if (isNotEmpty(description3) && isNotEmpty(description4)) {
            // Jira BMS-1960
            liste.add(description3);
            liste.add(description4);
        } else if (isNotEmpty(description3)) {
            liste.add(description3);
        } else if (isNotEmpty(description4)) {
            liste.add(description4);
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

    public List<String> getAdresseAsStringLinesWithoutTitre() {
        List<String> liste = new ArrayList<String>();

        if (isNotEmpty(description1) && isNotEmpty(description2)) {
            liste.add(description1 + " " + description2);
        } else if (isNotEmpty(description1)) {
            liste.add(description1);
        } else if (isNotEmpty(description2)) {
            liste.add(description2);
        }

        if (isNotEmpty(description3) && isNotEmpty(description4)) {
            // Jira BMS-1960
            liste.add(description3);
            liste.add(description4);
        } else if (isNotEmpty(description3)) {
            liste.add(description3);
        } else if (isNotEmpty(description4)) {
            liste.add(description4);
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
        StringBuffer adressAsString = new StringBuffer();
        for (String ligne : getAdresseAsStringLines()) {
            adressAsString.append(ligne);
            adressAsString.append("\n");
        }
        return adressAsString.toString();
    }

    public String getAdresseFormatteWithoutTitre() {
        StringBuffer adressAsString = new StringBuffer();
        for (String ligne : getAdresseAsStringLinesWithoutTitre()) {
            adressAsString.append(ligne);
            adressAsString.append("\n");
        }
        return adressAsString.toString();
    }

    /**
     * Retourne le nombre de lignes de l'adresse
     * 
     * @return
     */
    public int nbLignes() {
        return getAdresseAsStringLines().size();
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getDescription4() {
        return description4;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
    }

    @Override
    public String toString() {
        return getAdresseFormatte();
    }

    private boolean isNotEmpty(final String stringToTest) {
        return stringToTest != null && stringToTest.trim().length() > 0;
    }

    public String getRueNumero() {
        return rueNumero;
    }

    public void setRueNumero(final String rueNumero) {
        this.rueNumero = rueNumero;
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
     * Mise à jour de la rue
     * 
     * @param rue
     *            String représentant la nouvelle rue
     */
    public void setRue(final String rue) {
        this.rue = rue;
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
     * Mise à jour de la localité
     * 
     * @param localite
     *            String représentant la nouvelle localité
     */
    public void setLocalite(final String localite) {
        this.localite = localite;
    }

    /**
     * Retourne la canton au format abrégé
     * 
     * @return String représentant la canton au format abrégé (Valais = VS)
     */
    public String getCantonCourt() {
        return cantonCourt;
    }

    /**
     * Mise à jour du canton au format abrégé
     * 
     * @param cantonCourt
     *            String représentant le nouveau canton abrégé
     */
    public void setCantonCourt(final String cantonCourt) {
        this.cantonCourt = cantonCourt;
    }

    /***
     * Retourne la canton
     * 
     * @return String représentant le canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Mise à jour du canton
     * 
     * @param canton
     *            String représentant le nouveau canton
     */
    public void setCanton(final String canton) {
        this.canton = canton;
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
     * Mise à jour de la case postale
     * 
     * @param casePostale
     *            String représentant la nouvelle case postale
     */
    public void setCasePostale(final String casePostale) {
        this.casePostale = casePostale;
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
     * Mise à jour de la partie "A l'attention" de l'adresse
     * 
     * @param attention
     *            String représentant la nouvelle "A l'attention" de l'adresse
     */
    public void setAttention(final String attention) {
        this.attention = attention;
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
     * Mise à jour du npa
     * 
     * @param npa
     *            String représentant le nouveau npa
     */
    public void setNpa(final String npa) {
        this.npa = npa;
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
     * Mise à jour du pays
     * 
     * @param pays
     *            String représentant le nouveau pays
     */
    public void setPays(final String pays) {
        this.pays = pays;
    }

    /**
     * @return the isoPays
     */
    public String getIsoPays() {
        return isoPays;
    }

    /**
     * @param isoPays the isoPays to set
     */
    public void setIsoPays(String isoPays) {
        this.isoPays = isoPays;
    }

    /**
     * Retourne le numéro du canton selon la notation OFAS (nombre sur 3
     * chiffres)
     * 
     * @return String représentant le numéro du canton OFAS
     */
    public String getCantonOFAS() {
        return cantonOFAS;
    }

    /**
     * Mise à jour du numéro de canton selon la notation OFAS
     * 
     * @param cantonOFAS
     *            String représentant le nouveau numéro du canton OFAS
     */
    public void setCantonOFAS(final String cantonOFAS) {
        this.cantonOFAS = cantonOFAS;
    }

    /**
     * @return the csTitre
     */
    public String getCsTitre() {
        return csTitre;
    }

    /**
     * @param csTitre the csTitre to set
     */
    public void setCsTitre(String csTitre) {
        this.csTitre = csTitre;
    }

    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }
}
