package ch.globaz.al.business.paiement;

import java.math.BigDecimal;

/**
 * Mod�le m�tier contenant les informations n�cessaires � l'impression d'une ligne sur le protocole r�capitulatif d'un
 * paiement direct
 * 
 * @author jts
 * 
 */
public class PaiementRecapitulatifBusinessModel {

    /**
     * Montant cr�dit total de la rubrique comptable
     */
    private BigDecimal credit = null;
    /**
     * Montant d�bit total de la rubrique comptable
     */
    private BigDecimal debit = null;
    /**
     * Num�ro de rubrique comptable
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
     *            Compte Num�ro de rubrique comptable
     */
    public PaiementRecapitulatifBusinessModel(String numeroCompte) {
        this.numeroCompte = numeroCompte;
        debit = new BigDecimal("0");
        credit = new BigDecimal("0");
        ordreVersement = new BigDecimal("0");
    }

    /**
     * Ajoute le montant pass� au montant au cr�dit
     * 
     * @param montant
     *            montant � ajouter
     */
    public void addCredit(BigDecimal montant) {
        credit = credit.add(montant);
    }

    /**
     * Ajoute le montant pass� au montant au d�bit
     * 
     * @param montant
     *            montant � ajouter
     */
    public void addDebit(BigDecimal montant) {
        debit = debit.add(montant);
    }

    /**
     * Ajoute le montant pass� au montant des ordres de versement
     * 
     * @param montant
     *            montant � ajouter
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
