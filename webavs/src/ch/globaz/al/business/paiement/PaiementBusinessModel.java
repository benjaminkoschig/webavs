package ch.globaz.al.business.paiement;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Modèle métier contenant les informations nécessaires au traitement d'un paiement direct
 * 
 * @author jts
 * 
 */
public class PaiementBusinessModel implements Comparable<Object> {

    /**
     * Etat du dossier
     */
    private String etatDossier = null;
    /**
     * Date de fin de validité du dossier
     */
    private String finValidite = null;
    /**
     * Numéro de dossier
     */
    private String idDossier = null;
    /**
     * Numéro de la récap
     */
    private String idRecap = null;
    /**
     * Id de l'en-tête de la prestation
     */
    private ArrayList<String> idsEntete = null;
    /**
     * Id du tiers allocataire
     */
    private String idTiersAllocataire = null;
    /**
     * id du tiers bénéficiaire
     */
    private String idTiersBeneficiaire = null;
    /**
     * Montant au crédit
     */
    private BigDecimal montant = null;
    /**
     * Nom de l'allocataire
     */
    private String nomAllocataire = null;
    /**
     * NSS de l'allocataire
     */
    private String nssAllocataire = null;

    /**
     * Numéro de facture
     */
    private String numFacture = null;

    /**
     * Permet le tri pour affichage sur les protocoles
     */
    private int ordre = -1;
    /**
     * fin de la période de la prestation
     */
    private String presPeriodeA = null;

    /**
     * Indique s'il s'agit d'une restitution
     */
    private boolean restitution = false;

    /**
     * numéro de la rubrique comptable
     */
    private String rubrique = null;

    private BigDecimal solde = null;

    /**
     * Solde initial du compte de l'allocataire
     */
    private BigDecimal soldeInitial = null;

    /**
     * Constructeur
     * 
     * @param idDossier
     *            Numéro de dossier
     * @param idRecap
     *            Numéro de la récap
     * @param idTiersBeneficiaire
     *            id du tiers bénéficiaire
     * @param soldeInitial
     *            Solde initial du compte de l'allocataire
     * @param nomAllocataire
     *            Nom de l'allocataire
     * @param nssAllocataire
     *            NSS de l'allocataire
     * @param numFacture
     *            Numéro de facture
     * @param rubrique
     *            Numéro de la rubrique comptable
     * @param restitution
     *            Indique s'il s'agit d'une restitution
     * @param ordre
     *            Ordre dans lequel placer le paiement sur les protocoles
     */
    public PaiementBusinessModel(String idDossier, String idRecap, String idTiersBeneficiaire, BigDecimal soldeInitial,
            String idTiersAllocataire, String nomAllocataire, String nssAllocataire, String numFacture,
            String rubrique, boolean restitution, int ordre) {

        montant = new BigDecimal("0");
        this.idDossier = idDossier;
        this.idRecap = idRecap;
        this.idTiersBeneficiaire = idTiersBeneficiaire;
        setIdTiersAllocataire(idTiersAllocataire);
        this.soldeInitial = soldeInitial;
        solde = soldeInitial;
        this.nomAllocataire = nomAllocataire;
        this.nssAllocataire = nssAllocataire;
        this.numFacture = numFacture;
        this.rubrique = rubrique;
        this.restitution = restitution;
        idsEntete = new ArrayList<String>();
        this.ordre = ordre;
    }

    /**
     * Ajoute un id d'en-tête à la liste des id d'en-tête
     * 
     * @param idEntete
     *            id à ajouter
     */
    public void addIdEntete(String idEntete) {
        idsEntete.add(idEntete);
    }

    /**
     * Ajoute le montant passé en paramètre au montant contenu dans le modèle. <code>montant</code> peut être négatif
     * 
     * @param montant
     *            montant à ajouter
     */
    public void addMontant(BigDecimal montant) {
        this.montant = this.montant.add(montant);
        solde = solde.add(montant);
    }

    @Override
    public int compareTo(Object o) {

        if (!(o instanceof PaiementBusinessModel)) {
            throw new IllegalArgumentException(
                    "PaiementBusinessModel#compareTo : o is not an instance of PaiementBusinessModel");
        }

        if (nomAllocataire.equals(((PaiementBusinessModel) o).getNomAllocataire())) {
            return ordre - ((PaiementBusinessModel) o).getOrdre();
        } else {
            return nomAllocataire.compareTo(((PaiementBusinessModel) o).getNomAllocataire());
        }
    }

    /**
     * @return le montant au crédit
     */
    public BigDecimal getCredit() {
        if (montant.compareTo(new BigDecimal("0")) > 0) {
            return montant;
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * @return le montant au débit
     */
    public BigDecimal getDebit() {
        if (montant.compareTo(new BigDecimal("0")) < 0) {
            return montant;
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * @return the etatDossier
     */
    public String getEtatDossier() {
        return etatDossier;
    }

    /**
     * @return the finValidite
     */
    public String getFinValidite() {
        return finValidite;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return the idsEntete
     */
    public ArrayList<String> getIdsEntete() {
        return idsEntete;
    }

    /**
     * 
     * @return the id tiers allocataire
     */
    public String getIdTiersAllocataire() {
        return idTiersAllocataire;
    }

    /**
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the montant
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return the nomAllocataire
     */
    public String getNomAllocataire() {
        return nomAllocataire;
    }

    /**
     * Retourne le nouveau solde.
     * 
     * Il est égal à : solde initial - débit + crédit
     * 
     * @return nouveau solde
     */
    public BigDecimal getNouveauSolde() {
        return solde;
        // return this.soldeInitial.add(this.getDebit()).add(this.getCredit());
    }

    /**
     * @return the nssAllocataire
     */
    public String getNssAllocataire() {
        return nssAllocataire;
    }

    /**
     * @return le numéro de facture
     */
    public String getNumFacture() {
        return numFacture;
    }

    /**
     * @return the ordre
     */
    private int getOrdre() {
        return ordre;
    }

    /**
     * @return the ordreVersement
     */
    public BigDecimal getOrdreVersement() {

        if (getNouveauSolde().compareTo(new BigDecimal("0")) > 0) {
            return getNouveauSolde();
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * @return the presPeriodeA
     */
    public String getPresPeriodeA() {
        return presPeriodeA;
    }

    /**
     * @return the rubrique
     */
    public String getRubrique() {
        return rubrique;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    /**
     * @return the soldeInitial
     */
    public BigDecimal getSoldeInitial() {
        return soldeInitial;
    }

    /**
     * @return the restitution
     */
    public boolean isRestitution() {
        return restitution;
    }

    /**
     * @param etatDossier
     *            the etatDossier to set
     */
    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    /**
     * @param finValidite
     *            the finValidite to set
     */
    public void setFinValidite(String finValidite) {
        this.finValidite = finValidite;
    }

    /**
     * 
     * @param idTiersAllocataire
     */
    public void setIdTiersAllocataire(String idTiersAllocataire) {
        this.idTiersAllocataire = idTiersAllocataire;
    }

    /**
     * @param presPeriodeA
     *            the presPeriodeA to set
     */
    public void setPresPeriodeA(String presPeriodeA) {
        this.presPeriodeA = presPeriodeA;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }
}
