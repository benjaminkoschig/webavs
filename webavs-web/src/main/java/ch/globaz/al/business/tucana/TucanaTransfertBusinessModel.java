package ch.globaz.al.business.tucana;

import java.math.BigDecimal;

/**
 * Mod�le m�tier contenant les informations n�cessaire � l'impression d'une ligne sur le protocole r�capitulatif d'une
 * compensation
 * 
 * @author jts
 * 
 */
public class TucanaTransfertBusinessModel {

    private String canton = null;
    /**
     * Montant d�bit total de conventionnel
     */
    private BigDecimal convention = null;
    /**
     * Montant total des prestations
     */
    private BigDecimal cumul = null;

    private String rubriqueConventionnel = null;
    private String rubriqueSupplement = null;
    private String rubriqueTypePrestation = null;
    /**
     * Montant d�bit total de suppl�ment
     */
    private BigDecimal supplement = null;

    /**
     * Constructeur
     * 
     * @param numeroCompte
     *            Compte Num�ro de rubrique comptable
     */
    public TucanaTransfertBusinessModel(String canton, String rubriqueConventionnel, String rubriqueSupplement,
            String rubriqueTypePrestation) {

        this.canton = canton;
        this.rubriqueConventionnel = rubriqueConventionnel;
        this.rubriqueSupplement = rubriqueSupplement;
        this.rubriqueTypePrestation = rubriqueTypePrestation;

        cumul = new BigDecimal("0");
        convention = new BigDecimal("0");
        supplement = new BigDecimal("0");
    }

    /**
     * Additionne le <code>montant</code> pass� en param�tre au montant conventionnel contenu dans l'objet
     * 
     * @param montant
     *            le montant � ajouter
     */
    public void addConvention(BigDecimal montant) {
        convention = convention.add(montant);
    }

    /**
     * Additionne le <code>montant</code> pass� en param�tre au montant de cumul contenu dans l'objet
     * 
     * @param montant
     *            le montant � ajouter
     */
    public void addCumul(String montant) {
        cumul = cumul.add(new BigDecimal(montant));
    }

    public void addSupplement(BigDecimal montant) {
        supplement = supplement.add(montant);
    }

    public String getCanton() {
        return canton;
    }

    public BigDecimal getConvention() {
        return convention;
    }

    public BigDecimal getCumul() {
        return cumul;
    }

    public String getRubriqueConventionnel() {
        return rubriqueConventionnel;
    }

    public String getRubriqueSupplement() {
        return rubriqueSupplement;
    }

    public String getRubriqueTypePrestation() {
        return rubriqueTypePrestation;
    }

    public BigDecimal getSupplement() {
        return supplement;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setRubriqueConventionnel(String rubriqueConventionnel) {
        this.rubriqueConventionnel = rubriqueConventionnel;
    }

    public void setRubriqueSupplement(String rubriqueSupplement) {
        this.rubriqueSupplement = rubriqueSupplement;
    }

    public void setRubriqueTypePrestation(String rubriqueTypePrestation) {
        this.rubriqueTypePrestation = rubriqueTypePrestation;
    }
}