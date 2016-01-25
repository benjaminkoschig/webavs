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
     * @return le montant compensé de cet ordre de versement
     */
    public BigDecimal getMontantCompense() {
        return montantCompense;
    }

    /**
     * @return le montant total des arriérés
     */
    public BigDecimal getMontantDette() {
        return montantDette;
    }

    /**
     * Retourne le montant, forcé positivement ou négativement en fonction du {@link #getType() type d'ordre de
     * versement} et du flag {@link #isCompense()}
     * 
     * @return le montant pour le calcul du solde de la décision
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
     * @return la rente accordée du nouveau droit lié à cette OV (il se peut qu'il n'y en ait pas, et du coup l'objet
     *         retourner sera non vide mais pas null)
     */
    public RenteAccordee getRenteAccordeeNouveauDroit() {
        return renteAccordeeNouveauDroit;
    }

    /**
     * @return la rente versée à tort liée à cet ordre de versement s'il y en a une, sinon un objet non initialisé
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
     * @return le type de cet ordre de versement sous la forme d'un énuméré
     */
    public TypeOrdreVersement getType() {
        return type;
    }

    /**
     * @return vrai si cet ordre de versement est lié à une compensation inter-décision
     */
    public boolean isCompensationInterDecision() {
        return compensationInterDecision;
    }

    /**
     * @return vrai si cet ordre de versement est compensé (si le montant compensé sera pris en compte, dans le cas
     *         d'une dette, lors de la validation de la décision)
     */
    public boolean isCompense() {
        return compense;
    }

    /**
     * (re-)défini si cet ordre de versement est lié à une compensation inter-décision
     * 
     * @param compensationInterDecision
     */
    public void setCompensationInterDecision(final boolean compensationInterDecision) {
        this.compensationInterDecision = compensationInterDecision;
    }

    /**
     * (re-)défini si cet ordre de versement est compensé (si le montant compensé sera pris en compte, dans le cas d'une
     * dette, lors de la validation de la décision)
     * 
     * @param compense
     */
    public void setCompense(final boolean compense) {
        this.compense = compense;
    }

    /**
     * (re-)défini le montant compensé de cet ordre de versement
     * 
     * @param montantCompense
     *            un montant (ne peut pas être null)
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     */
    public void setMontantCompense(final BigDecimal montantCompense) {
        Checkers.checkNotNull(montantCompense, "ordreVersement.montantCompense");
        this.montantCompense = montantCompense;
    }

    /**
     * (re-)défini le montant total des arriérés
     * 
     * @param montantDette
     *            un montant (ne peut pas être null)
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     */
    public void setMontantDette(final BigDecimal montantDette) {
        Checkers.checkNotNull(montantDette, "ordreVersement.montantDette");
        this.montantDette = montantDette;
    }

    /**
     * (re-)défini la prestation dont est issu cet ordre de versement
     * 
     * @param prestation
     * @throws NullPointerException
     *             si la prestation passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la prestation passée en paramètre n'est pas initialisée
     */
    public void setPrestation(final Prestation prestation) {
        Checkers.checkNotNull(prestation, "ordreVersement.prestation");
        Checkers.checkHasID(prestation, "ordreVersement.prestation");
        this.prestation = prestation;
    }

    /**
     * (re-)défini la rente accordée du nouveau droit liée à cette ordre de versement
     * 
     * @param renteAccordeeNouveauDroit
     *            une rente accordée (ne peut pas être null)
     * @throws NullPointerException
     *             si la rente passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la rente passée en paramètre n'est pas initialisée
     */
    public void setRenteAccordeeNouveauDroit(final RenteAccordee renteAccordeeNouveauDroit) {
        Checkers.checkNotNull(renteAccordeeNouveauDroit, "ordreVersement.renteAccordeeNouveauDroit");
        Checkers.checkHasID(renteAccordeeNouveauDroit, "ordreVersement.renteAccordeeNouveauDroit");
        this.renteAccordeeNouveauDroit = renteAccordeeNouveauDroit;
    }

    /**
     * (re-)défini la rente versée à tort liée à cet ordre de versement
     * 
     * @param renteVerseeATort
     *            une rente versée à tort (ne peut pas être null)
     * @throws NullPointerException
     *             si la rente versée à tort passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la rente versée à tort passée en paramètre n'est pas initialisée
     */
    public void setRenteVerseeATort(final RenteVerseeATort renteVerseeATort) {
        Checkers.checkNotNull(renteVerseeATort, "ordreVersement.renteVerseeATort");
        Checkers.checkHasID(renteVerseeATort, "ordreVersement.renteVerseeATort");
        this.renteVerseeATort = renteVerseeATort;
    }

    /**
     * (re-)défini le titulaire de cet ordre de versement
     * 
     * @param titulaire
     * @throws NullPointerException
     *             si le titulaire passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le titulaire passé en paramètre n'est pas initialisée
     */
    public void setTitulaire(final PersonneAVS titulaire) {
        Checkers.checkNotNull(titulaire, "ordreVersement.titulaire");
        Checkers.checkHasID(titulaire, "ordreVersement.titulaire");
        this.titulaire = titulaire;
    }

    /**
     * (re-)défini le type de cet ordre de versement
     * 
     * @param type
     * @throws NullPointerException
     *             si le type passé en paramètre est null
     */
    public void setType(final TypeOrdreVersement type) {
        Checkers.checkNotNull(type, "ordreVersment.type");
        this.type = type;
    }
}
