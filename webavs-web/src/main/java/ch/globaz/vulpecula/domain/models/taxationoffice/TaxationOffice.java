package ch.globaz.vulpecula.domain.models.taxationoffice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * Représentation d'une taxation d'office
 * 
 * @author age
 * 
 */
public class TaxationOffice implements DomainEntity, Serializable {
    private static final long serialVersionUID = -2189470702408712689L;

    public static final int OFFSET_FOR_TAXATIONOFFICE = 500;
    private String id;
    private Decompte decompte;
    private String idPassageFacturation;
    private EtatTaxation etat;
    private String spy;
    private List<LigneTaxation> lignes;
    private Date dateAnnulation;
    private String idSection;

    public TaxationOffice() {
        lignes = new ArrayList<LigneTaxation>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public Decompte getDecompte() {
        return decompte;
    }

    public void setDecompte(Decompte decompte) {
        this.decompte = decompte;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public EtatTaxation getEtat() {
        return etat;
    }

    public void setEtat(EtatTaxation etat) {
        this.etat = etat;
    }

    public String getIdDecompte() {
        return decompte.getId();
    }

    public Date getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(Date dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    /**
     * Retourne l'id du tiers auquel le décompte est rattaché, et donc la taxation décompte.
     * 
     * @return String représentant l'id du tiers
     */
    public String getIdTiers() {
        if (decompte == null) {
            return null;
        }
        return decompte.getIdTiers();
    }

    public String getEtatValue() {
        return etat.getValue();
    }

    public String getPeriodeDebutAsSwissValue() {
        if (decompte == null) {
            return null;
        }
        return decompte.getPeriodeDebutAsSwissValue();
    }

    public String getPeriodeFinAsSwissValue() {
        if (decompte == null) {
            return null;
        }
        return decompte.getPeriodeFinAsSwissValue();
    }

    public String getPeriodeDebutWithDayAsSwissValue() {
        if (decompte == null) {
            return null;
        }
        return decompte.getPeriodeDebutFormate();
    }

    public String getPeriodeFinWithDayAsSwissValue() {
        if (decompte == null) {
            return null;
        }
        return decompte.getPeriodeFinFormate();
    }

    /**
     * Retourne le titre de l'employeur sous forme de code système.
     * 
     * @return String représentant le titre de l'employeur
     */
    public String getTitreEmployeur() {
        if (decompte == null) {
            return null;
        }
        return decompte.getTitreEmployeur();
    }

    /**
     * Retourne la politesse spécifique à l'employeur.
     * 
     * @return String représentant la politesse spécifique de l'employeur
     */
    public String getPolitesseSpecifique(CodeLangue codeLangue) {
        if (decompte == null) {
            return null;
        }
        return decompte.getPolitesseSpecEmployeur(codeLangue);
    }

    public Employeur getEmployeur() {
        if (decompte == null) {
            return null;
        }
        return decompte.getEmployeur();
    }

    /**
     * Retourne la somme des montants de cotisation des lignes de taxation d'office.
     * 
     * @return double représentant la somme des lignes
     */
    public String getMontant() {
        Montant sum = new Montant(0);
        for (LigneTaxation ligneTaxation : lignes) {
            sum = sum.add(ligneTaxation.getMontant());
        }
        return sum.getValue();
    }

    /**
     * Retourne la somme des masses des lignes de taxation d'office.
     * 
     * @return double représentant la somme des masses
     */
    public String getMasse() {
        Montant sum = new Montant(0);
        for (LigneTaxation ligneTaxation : lignes) {
            sum = sum.add(ligneTaxation.getMasse());
        }
        return sum.getValue();
    }

    /**
     * Retourne la somme des taux des lignes de taxation d'office.
     * 
     * @return double représentant la somme des taux
     */
    public Taux getTaux() {
        Taux sum = Taux.ZERO();
        for (LigneTaxation ligneTaxation : lignes) {
            sum = sum.addTaux(ligneTaxation.getTaux());
        }
        return sum;
    }

    /**
     * Ajoute une ligne de taxation dans la taxation d'office.
     * 
     * @param ligneTaxation Ligne de taxation à rajouter
     */
    public void add(LigneTaxation ligneTaxation) {
        lignes.add(ligneTaxation);
    }

    public List<LigneTaxation> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneTaxation> lignes) {
        this.lignes = lignes;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * Retourne le numéro d'affilié de l'employeur dont le décompte est lié à la taxation d'office.
     * 
     * @return String représentant le numéro de l'affilié
     */
    public String getEmployeurAffilieNumero() {
        if (decompte == null) {
            return null;
        }
        return decompte.getEmployeurAffilieNumero();
    }

    /**
     * Retourne l'adresse principale formattée pour les en-têtes de lettre.
     * Par exemple :
     * <p>
     * Aranud Geiser<br>
     * Rue de l'Orée 36 <br />
     * 2523 Lignières
     * </p>
     * 
     * @return String représentant l'adresse principale formattée pour les en-têtes de lettre.
     * 
     */
    public String getAdressePrincipaleFormattee() {
        if (decompte == null) {
            return null;
        }

        return decompte.getFullAdressePrincipaleFormatte();
    }

    /**
     * Mise à jour de l'adresse principale de l'employeur
     * 
     * @param adresse
     */
    public void setAdressePrincipale(Adresse adresse) {
        if (decompte == null) {
            throw new NullPointerException("Le décompte auquel l'adresse doit être liée est null");
        }
        decompte.setAdressePrincipale(adresse);
    }

    /**
     * Retourne la langue de l'employeur
     * 
     * @return
     */
    public CodeLangue getEmployeurLangue() {
        return decompte.getEmployeurLangue();
    }

    /**
     * Retourne le numéro du décompte auquel la taxation d'office est associé.
     * 
     * @return String représentant le numéro de décompte, pouvant être utilisé en tant que section
     */
    public String getNumeroDecompteAsValue() {
        return decompte.getNumeroDecompte().getValue();
    }

    public String getNumeroDecompteForSection() {
        return decompte.getNumeroDecompte().getValue();
    }

    /**
     * Retourne true si l'état de la taxation d'office est saisi
     * 
     * @return true si état saisi, false dans les autres cas
     */
    public boolean isSaisie() {
        return EtatTaxation.SAISI.equals(etat);
    }

    /**
     * Retourne true si l'état de la taxation d'office est comptabilisée
     * 
     * @return true si état comptabilisée, false dans les autres cas
     */
    public boolean isComptabilisee() {
        return EtatTaxation.COMPTABILISE.equals(etat);
    }

    /**
     * Retourne true si l'état de la taxation d'office est annulée
     * 
     * @return true si état annulée, false dans les autres cas
     */
    public boolean isAnnulee() {
        return EtatTaxation.ANNULE.equals(etat);
    }

    /**
     * Passage de la taxation d'office en validé si l'id du passage de facturation est saisie.
     */
    public void valide() {
        if (isSaisie() && idPassageFacturation != null && !idPassageFacturation.equals("0")) {
            etat = EtatTaxation.VALIDE;
        }
    }

    /**
     * Retourne le type de section du décompte
     */
    public TypeSection getTypeSection() {
        return decompte.getTypeSection();
    }
}
