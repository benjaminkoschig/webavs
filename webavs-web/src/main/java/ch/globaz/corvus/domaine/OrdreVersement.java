package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.pyxis.domaine.PersonneAVS;

public final class OrdreVersement extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean compensationInterDecision;
    private boolean compense;
    private BigDecimal montantCompense;
    private BigDecimal montantDette;
    private Prestation prestation;
    private RenteAccordee renteAccordeeNouveauDroit;
    private RenteVerseeATort renteVerseeATort;
    private PersonneAVS titulaire;
    private TypeOrdreVersement type;

    public OrdreVersement() {
        super();

        compensationInterDecision = false;
        compense = false;
        montantCompense = BigDecimal.ZERO;
        montantDette = BigDecimal.ZERO;
        prestation = new Prestation();
        renteAccordeeNouveauDroit = new RenteAccordee();
        renteVerseeATort = new RenteVerseeATort();
        titulaire = new PersonneAVS();
        type = TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL;
    }

    /**
     * @return le montant compens� de cet ordre de versement
     */
    public BigDecimal getMontantCompense() {
        return montantCompense;
    }

    /**
     * @return le montant total des arri�r�s
     */
    public BigDecimal getMontantDette() {
        return montantDette;
    }

    /**
     * Retourne le montant, forc� positivement ou n�gativement en fonction du {@link #getType() type d'ordre de
     * versement} et du flag {@link #isCompense()}
     * 
     * @return le montant pour le calcul du solde de la d�cision
     */
    public BigDecimal getMontantPourSolde() {

        BigDecimal montantPourSolde = BigDecimal.ZERO;

        switch (type) {
            case BENEFICIAIRE_PRINCIPAL:
            case INTERET_MORATOIRE:
            case ALOCATION_DE_NOEL:
                montantPourSolde = montantPourSolde.add(montantCompense.abs());
                break;
            case CREANCIER:
            case IMPOT_A_LA_SOURCE:
            case ASSURANCE_SOCIALE:
                montantPourSolde = montantPourSolde.subtract(montantCompense.abs());
                break;
            case DETTE:
                if (isCompense()) {
                    montantPourSolde = montantPourSolde.subtract(montantCompense.abs());
                }
                break;
            case DETTE_RENTE_AVANCES:
            case DETTE_RENTE_DECISION:
            case DETTE_RENTE_PRST_BLOQUE:
            case DETTE_RENTE_RESTITUTION:
            case DETTE_RENTE_RETOUR:
                if (isCompense()) {
                    montantPourSolde = montantPourSolde.subtract(montantCompense.abs());
                }
            default:
                break;
        }

        return montantPourSolde;
    }

    /**
     * @return la prestation dont est issu cette ordre de versement
     */
    public Prestation getPrestation() {
        return prestation;
    }

    /**
     * @return la rente accord�e du nouveau droit li� � cette OV (il se peut qu'il n'y en ait pas, et du coup l'objet
     *         retourner sera non vide mais pas null)
     */
    public RenteAccordee getRenteAccordeeNouveauDroit() {
        return renteAccordeeNouveauDroit;
    }

    /**
     * @return la rente vers�e � tort li�e � cet ordre de versement s'il y en a une, sinon un objet non initialis�
     */
    public RenteVerseeATort getRenteVerseeATort() {
        return renteVerseeATort;
    }

    /**
     * @return le titulaire de cet ordre de versement
     */
    public PersonneAVS getTitulaire() {
        return titulaire;
    }

    /**
     * @return le type de cet ordre de versement sous la forme d'un �num�r�
     */
    public TypeOrdreVersement getType() {
        return type;
    }

    /**
     * @return vrai si cet ordre de versement est li� � une compensation inter-d�cision
     */
    public boolean isCompensationInterDecision() {
        return compensationInterDecision;
    }

    /**
     * @return vrai si cet ordre de versement est compens� (si le montant compens� sera pris en compte, dans le cas
     *         d'une dette, lors de la validation de la d�cision)
     */
    public boolean isCompense() {
        return compense;
    }

    /**
     * (re-)d�fini si cet ordre de versement est li� � une compensation inter-d�cision
     * 
     * @param compensationInterDecision
     */
    public void setCompensationInterDecision(final boolean compensationInterDecision) {
        this.compensationInterDecision = compensationInterDecision;
    }

    /**
     * (re-)d�fini si cet ordre de versement est compens� (si le montant compens� sera pris en compte, dans le cas d'une
     * dette, lors de la validation de la d�cision)
     * 
     * @param compense
     */
    public void setCompense(final boolean compense) {
        this.compense = compense;
    }

    /**
     * (re-)d�fini le montant compens� de cet ordre de versement
     * 
     * @param montantCompense
     *            un montant (ne peut pas �tre null)
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     */
    public void setMontantCompense(final BigDecimal montantCompense) {
        Checkers.checkNotNull(montantCompense, "ordreVersement.montantCompense");
        this.montantCompense = montantCompense;
    }

    /**
     * (re-)d�fini le montant total des arri�r�s
     * 
     * @param montantDette
     *            un montant (ne peut pas �tre null)
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     */
    public void setMontantDette(final BigDecimal montantDette) {
        Checkers.checkNotNull(montantDette, "ordreVersement.montantDette");
        this.montantDette = montantDette;
    }

    /**
     * (re-)d�fini la prestation dont est issu cet ordre de versement
     * 
     * @param prestation
     * @throws NullPointerException
     *             si la prestation pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la prestation pass�e en param�tre n'est pas initialis�e
     */
    public void setPrestation(final Prestation prestation) {
        Checkers.checkNotNull(prestation, "ordreVersement.prestation");
        Checkers.checkHasID(prestation, "ordreVersement.prestation");
        this.prestation = prestation;
    }

    /**
     * (re-)d�fini la rente accord�e du nouveau droit li�e � cette ordre de versement
     * 
     * @param renteAccordeeNouveauDroit
     *            une rente accord�e (ne peut pas �tre null)
     * @throws NullPointerException
     *             si la rente pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la rente pass�e en param�tre n'est pas initialis�e
     */
    public void setRenteAccordeeNouveauDroit(final RenteAccordee renteAccordeeNouveauDroit) {
        Checkers.checkNotNull(renteAccordeeNouveauDroit, "ordreVersement.renteAccordeeNouveauDroit");
        Checkers.checkHasID(renteAccordeeNouveauDroit, "ordreVersement.renteAccordeeNouveauDroit");
        this.renteAccordeeNouveauDroit = renteAccordeeNouveauDroit;
    }

    /**
     * (re-)d�fini la rente vers�e � tort li�e � cet ordre de versement
     * 
     * @param renteVerseeATort
     *            une rente vers�e � tort (ne peut pas �tre null)
     * @throws NullPointerException
     *             si la rente vers�e � tort pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la rente vers�e � tort pass�e en param�tre n'est pas initialis�e
     */
    public void setRenteVerseeATort(final RenteVerseeATort renteVerseeATort) {
        Checkers.checkNotNull(renteVerseeATort, "ordreVersement.renteVerseeATort");
        Checkers.checkHasID(renteVerseeATort, "ordreVersement.renteVerseeATort");
        this.renteVerseeATort = renteVerseeATort;
    }

    /**
     * (re-)d�fini le titulaire de cet ordre de versement
     * 
     * @param titulaire
     * @throws NullPointerException
     *             si le titulaire pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le titulaire pass� en param�tre n'est pas initialis�e
     */
    public void setTitulaire(final PersonneAVS titulaire) {
        Checkers.checkNotNull(titulaire, "ordreVersement.titulaire");
        Checkers.checkHasID(titulaire, "ordreVersement.titulaire");
        this.titulaire = titulaire;
    }

    /**
     * (re-)d�fini le type de cet ordre de versement
     * 
     * @param type
     * @throws NullPointerException
     *             si le type pass� en param�tre est null
     */
    public void setType(final TypeOrdreVersement type) {
        Checkers.checkNotNull(type, "ordreVersment.type");
        this.type = type;
    }
}
