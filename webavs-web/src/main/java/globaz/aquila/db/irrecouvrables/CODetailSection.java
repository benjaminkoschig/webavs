/*
 * Créé le 14 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.irrecouvrables;

import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.db.comptes.CATauxRubriques;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CODetailSection implements Serializable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final BigDecimal CENT = new BigDecimal(100);
    private static final BigDecimal ZERO = new BigDecimal(0);

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idCaisseProfessionnelle;
    private String idExterne;
    private String idRubrique;
    private String idSection;
    private String libelle;
    private BigDecimal montantDu;
    private BigDecimal montantIrrecouvrable;
    private Map<String, BigDecimal> montantParAnnee = null;
    private BigDecimal montantVerse;
    private BigDecimal taux;
    private String typeOrdre;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CODetailSection.
     */
    public CODetailSection() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return the idCaisseProfessionnelle
     */
    public String getIdCaisseProfessionnelle() {
        return idCaisseProfessionnelle;
    }

    /**
     * @return
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * getter pour l'attribut id rubrique
     * 
     * @return la valeur courante de l'attribut id rubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * getter pour l'attribut id section
     * 
     * @return la valeur courante de l'attribut id section
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * getter pour l'attribut libelle
     * 
     * @return la valeur courante de l'attribut libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * getter pour l'attribut masse
     * 
     * @return la valeur courante de l'attribut masse
     */
    public BigDecimal getMasse() {
        if (taux == null) {
            return CODetailSection.ZERO;
        }

        return montantDu.divide(taux.divide(CODetailSection.CENT, 5, BigDecimal.ROUND_HALF_EVEN), 2,
                BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * getter pour l'attribut montant du
     * 
     * @return la valeur courante de l'attribut montant du
     */
    public BigDecimal getMontantDu() {
        return montantDu;
    }

    /**
     * getter pour l'attribut montant du formatte
     * 
     * @return la valeur courante de l'attribut montant du formatte
     */
    public String getMontantDuFormatte() {
        return JANumberFormatter.format(montantDu);
    }

    /**
     * getter pour l'attribut montant irrecouvrable
     * 
     * @return la valeur courante de l'attribut montant irrecouvrable
     */
    public BigDecimal getMontantIrrecouvrable() {
        if (montantIrrecouvrable == null) {
            resetMontants();
        }

        return montantIrrecouvrable;
    }

    /**
     * getter pour l'attribut montant irrecouvrable formatte
     * 
     * @return la valeur courante de l'attribut montant irrecouvrable formatte
     */
    public String getMontantIrrecouvrableFormatte() {
        return JANumberFormatter.format(getMontantIrrecouvrable());
    }

    public Map<String, BigDecimal> getMontantParAnnee() {
        return montantParAnnee;
    }

    /**
     * getter pour l'attribut montant verse
     * 
     * @return la valeur courante de l'attribut montant verse
     */
    public BigDecimal getMontantVerse() {
        return montantVerse;
    }

    /**
     * getter pour l'attribut montant verse formatte
     * 
     * @return la valeur courante de l'attribut montant verse formatte
     */
    public String getMontantVerseFormatte() {
        return JANumberFormatter.format(montantVerse);
    }

    /**
     * getter pour l'attribut taux
     * 
     * @return la valeur courante de l'attribut taux
     */
    public BigDecimal getTaux() {
        return taux != null ? taux : CODetailSection.ZERO;
    }

    /**
     * getter pour l'attribut type ordre
     * 
     * @return la valeur courante de l'attribut type ordre
     */
    public String getTypeOrdre() {
        return typeOrdre;
    }

    /** recalcule le montant irrecouvrable en se basant sur le montant du et le montant verse. */
    public void resetMontants() {
        montantIrrecouvrable = montantDu.subtract(montantVerse);
    }

    /**
     * @param idCaisseProfessionnelle
     *            the idCaisseProfessionnelle to set
     */
    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle) {
        this.idCaisseProfessionnelle = idCaisseProfessionnelle;
    }

    /**
     * @param idExterne
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * setter pour l'attribut id rubrique
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    /**
     * setter pour l'attribut id section
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    /**
     * setter pour l'attribut libelle
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelle(String string) {
        libelle = string;
    }

    /**
     * setter pour l'attribut montant du
     * 
     * @param decimal
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantDu(BigDecimal decimal) {
        montantDu = decimal;
    }

    /**
     * setter pour l'attribut montant irrecouvrable
     * 
     * @param decimal
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIrrecouvrable(BigDecimal decimal) {
        montantIrrecouvrable = decimal;
    }

    public void setMontantParAnnee(Map<String, BigDecimal> map) {
        montantParAnnee = map;
    }

    /**
     * setter pour l'attribut montant verse
     * 
     * @param decimal
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantVerse(BigDecimal decimal) {
        montantVerse = decimal;
    }

    /**
     * setter pour l'attribut taux
     * 
     * @param decimal
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaux(BigDecimal decimal) {
        taux = decimal;
    }

    /**
     * setter pour l'attribut taux
     * 
     * @param taux
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaux(CATauxRubriques taux) {
        if (taux != null) {
            if (APIVPDetailMontant.CS_VP_MONTANT_SALARIE.equals(typeOrdre)) {
                this.setTaux(taux.getTauxSalarie());
            } else {
                this.setTaux(taux.getTauxEmployeur());
            }
        }
    }

    /**
     * setter pour l'attribut taux
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaux(String string) {
        taux = new BigDecimal(string);
        taux = taux.setScale(5);
    }

    /**
     * setter pour l'attribut type ordre
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeOrdre(String string) {
        typeOrdre = string;
    }

}
