package ch.globaz.corvus.domaine;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Pourcentage;
import ch.globaz.corvus.domaine.constantes.Atteinte;
import ch.globaz.corvus.domaine.constantes.Infirmite;
import ch.globaz.corvus.domaine.constantes.OfficeAI;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Une demande de rente d'invalidit�
 */
public final class DemandeRenteInvalidite extends DemandeRente {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Atteinte atteinte;
    private boolean avecMotivation;
    private String dateSuvenanceEvenementAssure;
    private Infirmite infirmite;
    private String moisDebutReductionPourNonCollaboration;
    private String moisFinReductionPourNonCollaboration;
    private int nombrePagesMotivation;
    private OfficeAI officeAI;
    private Pourcentage pourcentageReduction;
    private Pourcentage pourcentageReductionPourFauteGrave;
    private Pourcentage pourcentageReductionPourNonCollaboration;

    public DemandeRenteInvalidite() {
        super();

        atteinte = Atteinte.ATTEINTE_00;
        avecMotivation = false;
        dateSuvenanceEvenementAssure = "";
        infirmite = Infirmite.INFIRMITE_101;
        moisDebutReductionPourNonCollaboration = "";
        moisFinReductionPourNonCollaboration = "";
        nombrePagesMotivation = 0;
        officeAI = OfficeAI.ASSURES_A_L_ETRANGER;
        pourcentageReduction = Pourcentage.ZERO_POURCENT;
        pourcentageReductionPourFauteGrave = Pourcentage.ZERO_POURCENT;
        pourcentageReductionPourNonCollaboration = Pourcentage.ZERO_POURCENT;

        setTypeDemandeRente(TypeDemandeRente.DEMANDE_INVALIDITE);
    }

    @Override
    public Set<CodePrestation> codesPrestationsAcceptesPourCeTypeDeDemande() {
        Set<CodePrestation> codesPrestationsAcceptes = new HashSet<CodePrestation>();

        for (CodePrestation unCodePrestation : CodePrestation.values()) {
            if (unCodePrestation.isAI()) {
                codesPrestationsAcceptes.add(unCodePrestation);
            }
        }

        return codesPrestationsAcceptes;
    }

    /**
     * @return l'atteinte de l'assur� l'ayant conduit � avoir droit � une rente AI
     */
    public Atteinte getAtteinte() {
        return atteinte;
    }

    /**
     * @return la date � laquelle la personne est devenue invalide (le d�but de son droit � une rente AI)
     */
    public String getDateSuvenanceEvenementAssure() {
        return dateSuvenanceEvenementAssure;
    }

    /**
     * @return l'infirmiti� de l'assur�
     */
    public Infirmite getInfirmite() {
        return infirmite;
    }

    /**
     * @return le mois (format MM.AAAA) auquel une r�duction pour non collaboration a d�but� (ou une cha�ne vide s'il
     *         n'y en a pas eu)
     */
    public String getMoisDebutReductionPourNonCollaboration() {
        return moisDebutReductionPourNonCollaboration;
    }

    /**
     * @return le mois (format MM.AAAA) auquel une r�duction pour non collaboration a termin� (ou une cha�ne vide s'il
     *         n'y en a pas eu)
     */
    public String getMoisFinReductionPourNonCollaboration() {
        return moisFinReductionPourNonCollaboration;
    }

    /**
     * @return le nombre de page de motivation du dossier AI de l'assur�
     */
    public int getNombrePagesMotivation() {
        return nombrePagesMotivation;
    }

    /**
     * @return l'office AI comp�tent pour cette rente AI
     */
    public OfficeAI getOfficeAI() {
        return officeAI;
    }

    /**
     * @return le pourcentage de r�duction de la rente AI
     */
    public Pourcentage getPourcentageReduction() {
        return pourcentageReduction;
    }

    /**
     * @return le pourcentage de r�duction de la rente AI pour faute grave
     */
    public Pourcentage getPourcentageReductionPourFauteGrave() {
        return pourcentageReductionPourFauteGrave;
    }

    /**
     * @return le pourcentage de r�duction de la rente AI pour non collaboration
     */
    public Pourcentage getPourcentageReductionPourNonCollaboration() {
        return pourcentageReductionPourNonCollaboration;
    }

    /**
     * @return TODO : � clarifier ce que c'est exactement
     */
    public boolean isAvecMotivation() {
        return avecMotivation;
    }

    /**
     * (re-)d�fini l'atteinte de l'assur� l'ayant conduit � avoir droit � une rente AI
     * 
     * @param atteinte
     *            une atteinte
     * @throws NullPointerException
     *             si l'atteinte est null
     */
    public void setAtteinte(final Atteinte atteinte) {
        Checkers.checkNotNull(atteinte, "demandeAI.atteinte");
        this.atteinte = atteinte;
    }

    /**
     * @param avecMotivation
     *            TODO : � d�finir plus exactement
     */
    public void setAvecMotivation(final boolean avecMotivation) {
        this.avecMotivation = avecMotivation;
    }

    /**
     * (re-)d�fini la date � laquelle l'assur� est devenu invalide (la date � laquelle son droit � une rente AI
     * commence)
     * 
     * @param dateSuvenanceEvenementAssure
     *            une date au format JJ.MM.AAAA, ou une cha�ne vide
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format
     */
    public void setDateSuvenanceEvenementAssure(final String dateSuvenanceEvenementAssure) {
        Checkers.checkNotNull(dateSuvenanceEvenementAssure, "demandeAI.dateSuvenanceEvenementAssure");
        Checkers.checkFullDate(dateSuvenanceEvenementAssure, "demandeAI.dateSuvenanceEvenementAssure", true);
        this.dateSuvenanceEvenementAssure = dateSuvenanceEvenementAssure;
    }

    /**
     * (re-)d�fini l'infirmit� de l'assur�
     * 
     * @param infirmite
     *            une infirmit�
     * @throws NullPointerException
     *             si l'infirmit� est null
     */
    public void setInfirmite(final Infirmite infirmite) {
        Checkers.checkNotNull(infirmite, "demandeAI.infirmite");
        this.infirmite = infirmite;
    }

    /**
     * (re-)d�fini le mois auquel une r�duction pour non collaboration a d�but� (ou une cha�ne vide s'il n'y en a pas
     * eu)
     * 
     * @param moisDebutReductionPourNonCollaboration
     *            un mois (format MM.AAAA) ou une cha�ne vide
     * @throws NullPointerException
     *             si le mois pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le mois pass� n'est pas au bon format
     */
    public void setMoisDebutReductionPourNonCollaboration(final String moisDebutReductionPourNonCollaboration) {
        Checkers.checkNotNull(moisDebutReductionPourNonCollaboration,
                "demandeAI.moisDebutReductionPourNonCollaboration");
        Checkers.checkDateMonthYear(moisDebutReductionPourNonCollaboration,
                "demandeAI.moisDebutReductionPourNonCollaboration", true);
        this.moisDebutReductionPourNonCollaboration = moisDebutReductionPourNonCollaboration;
    }

    /**
     * (re-)d�fini le mois auquel une r�duction pour non collaboration a termin� (ou une cha�ne vide s'il n'y en a pas
     * eu)
     * 
     * @param moisFinReductionPourNonCollaboration
     *            un mois (format MM.AAAA) ou une cha�ne vide
     * @throws NullPointerException
     *             si le mois pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le mois pass� n'est pas au bon format format
     */
    public void setMoisFinReductionPourNonCollaboration(final String moisFinReductionPourNonCollaboration) {
        Checkers.checkNotNull(moisFinReductionPourNonCollaboration, "demandeAI.moisFinReductionPourNonCollaboration");
        Checkers.checkDateMonthYear(moisFinReductionPourNonCollaboration,
                "demandeAI.moisFinReductionPourNonCollaboration", true);
        this.moisFinReductionPourNonCollaboration = moisFinReductionPourNonCollaboration;
    }

    /**
     * (re-)d�fini le nombre de page de motivation du dossier AI de l'assur�
     * 
     * @param nombrePagesMotivation
     *            un nombre de page (plus grand ou �gal � z�ro)
     * @throws IllegalArgumentException
     *             si le nombre de page est n�gatif
     */
    public void setNombrePagesMotivation(final int nombrePagesMotivation) {
        Checkers.checkCantBeNegative(nombrePagesMotivation, "demandeAI.nombrePagesMotivation");
        this.nombrePagesMotivation = nombrePagesMotivation;
    }

    /**
     * (re-)d�fini l'office AI comp�tent pour cette rente AI
     * 
     * @param officeAI
     *            un office AI
     * @throws NullPointerException
     *             si l'office AI est null
     */
    public void setOfficeAI(final OfficeAI officeAI) {
        Checkers.checkNotNull(officeAI, "demandeAI.officeAI");
        this.officeAI = officeAI;
    }

    /**
     * (re-)d�fini le pourcentage de r�duction de la rente AI
     * 
     * @param pourcentageReduction
     *            un pourcentage de r�duction
     * @throws NullPointerException
     *             si le pourcentage pass� en param�tre est null
     */
    public void setPourcentageReduction(final Pourcentage pourcentageReduction) {
        Checkers.checkNotNull(pourcentageReduction, "demandeAI.pourcentageReduction");
        this.pourcentageReduction = pourcentageReduction;
    }

    /**
     * (re-)d�fini le pourcentage de r�duction de la rente AI pour faute grave
     * 
     * @param pourcentageReductionPourFauteGrave
     *            un pourcentage de r�duction
     * @throws NullPointerException
     *             si le pourcentage pass� en param�tre est null
     */
    public void setPourcentageReductionPourFauteGrave(final Pourcentage pourcentageReductionPourFauteGrave) {
        Checkers.checkNotNull(pourcentageReductionPourFauteGrave, "demandeAI.pourcentageReductionPourFauteGrave");
        this.pourcentageReductionPourFauteGrave = pourcentageReductionPourFauteGrave;
    }

    /**
     * (re-)d�fini le pourcentage de r�duction de la rente AI pour non collaboration
     * 
     * @param pourcentageReductionPourNonCollaboration
     *            un pourcentage de r�duction
     * @throws NullPointerException
     *             si le pourcentage pass� en param�tre est null
     */
    public void setPourcentageReductionPourNonCollaboration(final Pourcentage pourcentageReductionPourNonCollaboration) {
        Checkers.checkNotNull(pourcentageReductionPourNonCollaboration,
                "demandeAI.pourcentageReductionPourNonCollaboration");
        this.pourcentageReductionPourNonCollaboration = pourcentageReductionPourNonCollaboration;
    }
}
