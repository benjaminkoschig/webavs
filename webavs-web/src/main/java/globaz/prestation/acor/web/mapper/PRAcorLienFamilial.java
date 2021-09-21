package globaz.prestation.acor.web.mapper;

import globaz.hera.api.ISFMembreFamilleRequerant;

class PRAcorLienFamilial {

    // ~ Instance fields
    // --------------------------------------------------------------------------------------------

    private ISFMembreFamilleRequerant conjoint;
    private boolean conjointHomme;
    private String dateFin;
    private String dateMariage;
    private String typeLien;

    // ~ Constructors
    // -----------------------------------------------------------------------------------------------

    public PRAcorLienFamilial(ISFMembreFamilleRequerant conjoint, boolean conjointHomme, String typeLien, String dateMariage) {
        this.conjoint = conjoint;
        this.conjointHomme = conjointHomme;
        this.typeLien = typeLien;
        this.dateMariage = dateMariage;
    }

    // ~ Methods
    // ----------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut conjoint.
     *
     * @return la valeur courante de l'attribut conjoint
     */
    public ISFMembreFamilleRequerant getConjoint() {
        return conjoint;
    }

    /**
     * getter pour l'attribut date fin.
     *
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut date mariage.
     *
     * @return la valeur courante de l'attribut date mariage
     */
    public String getDateMariage() {
        return dateMariage;
    }

    /**
     * getter pour l'attribut type lien.
     *
     * @return la valeur courante de l'attribut type lien
     */
    public String getTypeLien() {
        return typeLien;
    }

    /**
     * getter pour l'attribut conjoint homme.
     *
     * @return la valeur courante de l'attribut conjoint homme
     */
    public boolean isConjointHomme() {
        return conjointHomme;
    }

    /**
     * setter pour l'attribut conjoint.
     *
     * @param conjoint
     *            une nouvelle valeur pour cet attribut
     */
    public void setConjoint(ISFMembreFamilleRequerant conjoint) {
        this.conjoint = conjoint;
    }

    /**
     * setter pour l'attribut conjoint homme.
     *
     * @param conjointHomme
     *            une nouvelle valeur pour cet attribut
     */
    public void setConjointHomme(boolean conjointHomme) {
        this.conjointHomme = conjointHomme;
    }

    /**
     * setter pour l'attribut date fin.
     *
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut date mariage.
     *
     * @param dateMariage
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateMariage(String dateMariage) {
        this.dateMariage = dateMariage;
    }

    /**
     * setter pour l'attribut type lien.
     *
     * @param typeRelation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeLien(String typeRelation) {
        typeLien = typeRelation;
    }
}
