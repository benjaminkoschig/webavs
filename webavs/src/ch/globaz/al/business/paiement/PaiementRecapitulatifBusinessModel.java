package ch.globaz.al.business.paiement;

import java.math.BigDecimal;

/**
 * Modèle métier contenant les informations nécessaires à l'impression d'une ligne sur le protocole récapitulatif d'un
 * paiement direct
 * 
 * @author jts
 * 
 */
public class PaiementRecapitulatifBusinessModel {

    /**
     * Montant crédit total de la rubrique comptable
     */
    private BigDecimal credit = null;
    /**
     * Montant débit total de la rubrique comptable
     */
    private BigDecimal debit = null;
    /**
     * Numéro de rubrique comptable
     */
    private String numeroCompte = null;
    /**
     * Montant des ordres de versement
     */
    private BigDecimal ordreVersement = null;

    /**
     * Constructeur
     * 
     * @param numeroCompte
     *            Compte Numéro de rubrique comptable
     */
    public PaiementRecapitulatifBusinessModel(String numeroCompte) {
        this.numeroCompte = numeroCompte;
        debit = new BigDecimal("0");
        credit = new BigDecimal("0");
        ordreVersement = new BigDecimal("0");
    }

    /**
     * Ajoute le montant passé au montant au crédit
     * 
     * @param montant
     *            montant à ajouter
     */
    public void addCredit(BigDecimal montant) {
        credit = credit.add(montant);
    }

    /**
     * Ajoute le montant passé au montant au débit
     * 
     * @param montant
     *            montant à ajouter
     */
    public void addDebit(BigDecimal montant) {
        debit = debit.add(montant);
    }

    /**
     * Ajoute le montant passé au montant des ordres de versement
     * 
     * @param montant
     *            montant à ajouter
     */
    public void addOrdreVersement(BigDecimal montant) {
        ordreVersement = ordreVersement.add(montant);
    }

    /**
     * @return the credit
     */
    public BigDecimal getCredit() {
        return credit;
    }

    /**
     * @return the debit
     */
    public BigDecimal getDebit() {
        return debit;
    }

    /**
     * @return the numeroCompte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * @return the ordreVersement
     */
    public BigDecimal getOrdreVersement() {
        return ordreVersement;
    }
}
