package ch.globaz.al.business.compensation;

import java.math.BigDecimal;

/**
 * Modèle métier contenant les informations nécessaire à l'impression d'une ligne sur le protocole récapitulatif d'une
 * compensation
 * 
 * @author jts
 * 
 */
public class CompensationRecapitulatifBusinessModel implements Comparable<Object> {

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
     * Constructeur
     * 
     * @param numeroCompte
     *            Compte Numéro de rubrique comptable
     */
    public CompensationRecapitulatifBusinessModel(String numeroCompte) {
        this.numeroCompte = numeroCompte;
        debit = new BigDecimal("0");
        credit = new BigDecimal("0");
    }

    /**
     * Additionne le <code>montant</code> passé en paramètre au montant de crédit contenu dans l'objet
     * 
     * @param montant
     *            le montant à ajouter
     */
    public void addCredit(String montant) {
        credit = credit.add(new BigDecimal(montant));
    }

    /**
     * Additionne le <code>montant</code> passé en paramètre au montant de débit contenu dans l'objet
     * 
     * @param montant
     *            le montant à ajouter
     */
    public void addDebit(String montant) {
        debit = debit.add(new BigDecimal(montant));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {

        if (!(o instanceof CompensationRecapitulatifBusinessModel)) {
            throw new IllegalArgumentException(
                    "CalculBusinessModel#compareTo : o is not an instance of CompensationRecapitulatifBusinessModel");
        }

        return getNumeroCompte().compareTo(((CompensationRecapitulatifBusinessModel) o).getNumeroCompte());
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
}
