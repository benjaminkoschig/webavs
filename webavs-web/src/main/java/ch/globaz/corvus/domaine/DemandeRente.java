package ch.globaz.corvus.domaine;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeCalculDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.DemandePrestation;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Objet de domaine repr�sentant une demande de rente
 */
public abstract class DemandeRente extends DemandePrestation {

    private static final long serialVersionUID = 1L;

    private String dateDebutDuDroitInitial;
    private String dateDepot;
    private String dateFinDuDroitInitial;
    private String dateReception;
    private String dateTraitement;
    private EtatDemandeRente etat;
    private String gestionnaire;
    private Set<RenteAccordee> rentesAccordees;
    private TypeCalculDemandeRente typeCalcul;
    private TypeDemandeRente typeDemandeRente;

    public DemandeRente() {
        super();

        dateDebutDuDroitInitial = "";
        dateDepot = "";
        dateFinDuDroitInitial = "";
        dateReception = "";
        dateTraitement = "";
        gestionnaire = "";
        etat = EtatDemandeRente.ENREGISTRE;
        rentesAccordees = new HashSet<RenteAccordee>();
        typeCalcul = TypeCalculDemandeRente.STANDARD;
        typeDemandeRente = TypeDemandeRente.DEMANDE_VIEILLESSE;
    }

    /**
     * Retourne les codes prestation correspondant au type de la demande
     * 
     * @return des codes prestation
     */
    public abstract Set<CodePrestation> codesPrestationsAcceptesPourCeTypeDeDemande();

    /**
     * <p>
     * Ajoute la rente accord�e pass�e en param�tre aux rentes accord�es de cette demande
     * </p>
     * </p>
     * Il n'est possible d'ajouter une rente accord�e que si la demande est dans un des �tats suivants :
     * <ul>
     * <li> {@link EtatDemandeRente#AU_CALCUL}
     * <li> {@link EtatDemandeRente#CALCULE}
     * <li> {@link EtatDemandeRente#ENREGISTRE}
     * </ul>
     * </p>
     * 
     * @param renteAccordee une rente accord�e
     * @throws NullPointerException si la rente accord�e pass�e en param�tre est null
     * @throws IllegalStateException si la demande est dans un �tat o� elle ne peut plus �tre modifi�e
     */
    public void ajouterRenteAccordee(RenteAccordee renteAccordee) {
        Checkers.checkNotNull(renteAccordee, "renteAccordee");

        switch (etat) {
            case AU_CALCUL:
            case CALCULE:
            case ENREGISTRE:
                rentesAccordees.add(renteAccordee);
                break;
            case COURANT_VALIDE:
            case VALIDE:
            case TERMINE:
            case TRANSFERE:
                throw new IllegalStateException("can't add a prestation when in this state : " + etat);
        }
    }

    /**
     * 
     * @return le montant r�troactif de la demande
     *         Ce montant est tir� des prestations dues de type MONTANT_RETROACTIF_TOTAL
     */
    public final BigDecimal getMontantRetroactif() {
        BigDecimal montantRetroactif = BigDecimal.ZERO;

        for (RenteAccordee renteAccordee : rentesAccordees) {
            montantRetroactif = montantRetroactif.add(renteAccordee.getMontantRetroactif());
        }

        return montantRetroactif;
    }

    /**
     * <p>
     * Retire la rente accord�e pass�e en param�tre des rentes accord�es de cette demande
     * </p>
     * <p>
     * Tout commme {@link #ajouterRenteAccordee(RenteAccordee) l'ajout de rente accord�e}, il n'est possible de retirer
     * une rente accord�e que si la demande est dans un �tat o� elle est modifiable, c'est � dire les �tats suivant :
     * <ul>
     * <li> {@link EtatDemandeRente#AU_CALCUL}
     * <li> {@link EtatDemandeRente#CALCULE}
     * <li> {@link EtatDemandeRente#ENREGISTRE}
     * </ul>
     * </p>
     * </p>
     * 
     * @param renteAccordee une rente accord�e
     * @return vrai si le retrait a effectivement eu lieu, faux si aucune rente n'a �t� retir�e (si la rente ne faisait
     *         pas partie de cette demande)
     * @throws NullPointerException si la rente accord�e pass�e en param�tre est null
     * @throws IllegalStateException si la demande n'est pas dans un �tat modifiable
     */
    public boolean retirerRenteAccordee(RenteAccordee renteAccordee) {
        Checkers.checkNotNull(renteAccordee, "renteAccordee");

        switch (etat) {
            case AU_CALCUL:
            case CALCULE:
            case ENREGISTRE:
                return rentesAccordees.remove(renteAccordee);
            case COURANT_VALIDE:
            case VALIDE:
            case TERMINE:
            case TRANSFERE:
                throw new IllegalStateException("can't add a prestation when in this state : " + etat);
        }

        return false;
    }

    /**
     * @return vrai si une cr�ance a �t� d�finie pour cette demande
     */
    public final boolean comporteDesCreances() {
        boolean creancesPresentes = false;

        Iterator<RenteAccordee> iterator = rentesAccordees.iterator();
        while (iterator.hasNext() && !creancesPresentes) {
            RenteAccordee uneRenteAccordee = iterator.next();
            if (uneRenteAccordee.comporteUneRepartitionDeCreance()) {
                creancesPresentes = true;
            }
        }

        return creancesPresentes;
    }

    /**
     * @return vrai si une des prestations accord�es comporte un int�r�t moratoire
     */
    public final boolean comporteDesInteretsMoratoires() {
        boolean interetMoratoirePresent = false;
        Iterator<RenteAccordee> iterator = rentesAccordees.iterator();

        while (iterator.hasNext() && !interetMoratoirePresent) {
            RenteAccordee uneRenteAccordee = iterator.next();
            if (uneRenteAccordee.comporteUnInteretMoratoire()) {
                interetMoratoirePresent = true;
            }
        }

        return interetMoratoirePresent;
    }

    /**
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @return vrai si une des rentes de la demande comporte le code cas sp�cial pass� en param�tre
     * @throws NullPointerException
     *             si le code cas sp�cial pass� en param�tre est null
     */
    public final boolean comporteDesRentesAccordeesAvecCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "codeCasSpecial");
        boolean codeCasSpecialPresent = false;

        Iterator<RenteAccordee> iterator = rentesAccordees.iterator();
        while (iterator.hasNext() && !codeCasSpecialPresent) {
            codeCasSpecialPresent |= iterator.next().comporteCodeCasSpecial(codeCasSpecial);
        }

        return codeCasSpecialPresent;
    }

    /**
     * @return vrai si une rente de cette demande a un code prestation ne correspondant pas au type de la demande (rente
     *         vieillesse sur demande AI par exemple)
     */
    public final boolean comporteDesRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande() {
        return filtrerRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande().size() > 0;
    }

    /**
     * @return vrai si un trou (d'un mois ou plus) est pr�sent dans la somme des p�riodes de droit des rentes accord�es
     *         de cette demande
     */
    public final boolean comporteUnTrouDansLesPeriodesDeDroitDesRentesAccordees() {

        Periode periodeGlobale = null;

        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            if (periodeGlobale == null) {
                periodeGlobale = uneRenteDeLaDemande.getPeriodeDuDroit();
            } else {
                Periode union = periodeGlobale.unionMois(uneRenteDeLaDemande.getPeriodeDuDroit());

                if (union == null) {
                    return true;
                } else {
                    periodeGlobale = union;
                }
            }
        }

        return false;
    }

    /**
     * @return vrai si au moins une rente est en cours (voir {@link RenteAccordee#estEnCours()})
     */
    public final boolean estEnCours() {
        boolean possedeUneRenteEnCours = false;

        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            possedeUneRenteEnCours |= uneRenteDeLaDemande.estEnCours();
        }

        return possedeUneRenteEnCours;
    }

    /**
     * @return les b�n�ficiaires des rentes accord�es de cette demande de rente dans un conteneur non modifiable
     */
    public final Set<PersonneAVS> getBeneficiairesDeLaDemande() {
        Set<PersonneAVS> beneficiairesDeLaDemande = new HashSet<PersonneAVS>();

        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            beneficiairesDeLaDemande.add(uneRenteDeLaDemande.getBeneficiaire());
        }

        return Collections.unmodifiableSet(beneficiairesDeLaDemande);
    }

    /**
     * @return la date de d�but du droit initial (format JJ.MM.AAAA), lors du calcul. Ne prend pas en compte les dates
     *         des �ventuelles rentes principales du/des conjoint(s) du requ�rant ayant �t� rabattues sur cette demande
     *         mais uniquement le droit du requ�rant.
     */
    public final String getDateDebutDuDroitInitial() {
        return dateDebutDuDroitInitial;
    }

    /**
     * @return la date de d�p�t de cette demande de rente (format JJ.MM.AAAA)
     */
    public final String getDateDepot() {
        return dateDepot;
    }

    /**
     * @return la date de fin du droit initial (format JJ.MM.AAAA), lors du calcul. Ne prend pas en compte les dates des
     *         �ventuelles rentes principales du/des conjoint(s) du requ�rant ayant �t� rabattues sur cette demande mais
     *         uniquement le droit du requ�rant.
     */
    public final String getDateFinDuDroitInitial() {
        return dateFinDuDroitInitial;
    }

    /**
     * @return la date de r�c�ption de la demande de rente, au foramt JJ.MM.AAAA
     */
    public final String getDateReception() {
        return dateReception;
    }

    /**
     * @return la date de traitement de cette demande de rente (au format JJ.MM.AAAA)
     */
    public final String getDateTraitement() {
        return dateTraitement;
    }

    /**
     * @return l'�tat de cette demande de rente
     */
    public final EtatDemandeRente getEtat() {
        return etat;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire de cette demande ou une cha�ne vide s'il n'est pas d�fini
     */
    public final String getGestionnaire() {
        return gestionnaire;
    }

    /**
     * Parcours les rentes accord�es, et construit une p�riode avec la plus petite et la plus grande date de droit
     * trouv�es dans ces rentes accord�es (ne prend pas en compte les trous dans le droit)
     * 
     * @return la p�riode couvrant le droit des rentes accord�es
     */
    public final Periode getPeriodeDuDroitDesRentesAccordees() {

        String plusPetiteDateDesRentesAccordees = Periode.MOIS_MAX;
        String plusGrandeDateDesRentesAccordees = Periode.MOIS_MIN;

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            if (JadeDateUtil.isDateMonthYearAfter(plusPetiteDateDesRentesAccordees, uneRenteAccordee.getMoisDebut())) {
                plusPetiteDateDesRentesAccordees = uneRenteAccordee.getMoisDebut();
            }
            if (JadeDateUtil.isDateMonthYearBefore(plusGrandeDateDesRentesAccordees, uneRenteAccordee.getMoisFin())
                    || JadeStringUtil.isBlankOrZero(uneRenteAccordee.getMoisFin())) {
                plusGrandeDateDesRentesAccordees = uneRenteAccordee.getMoisFin();
            }
        }

        if (plusPetiteDateDesRentesAccordees.equals(Periode.MOIS_MAX)) {
            plusPetiteDateDesRentesAccordees = "";
        }
        if (plusGrandeDateDesRentesAccordees.equals(Periode.MOIS_MIN)) {
            plusGrandeDateDesRentesAccordees = "";
        }

        return new Periode(plusPetiteDateDesRentesAccordees, plusGrandeDateDesRentesAccordees);
    }

    /**
     * <p>
     * La rente principale sera d�finie avec l'ordre de priorit� suivante :
     * </p>
     * <ul>
     * <li>1) rente principale</li>
     * <li>2) rente compl�mentaire pour conjoint</li>
     * <li>3) rente compl�mentaire pour enfant</li>
     * <li>4 ) rente API</li>
     * </ul>
     * <p>
     * Pour des informations sur quelle rente est une principale ou non, voir <a
     * href="http://dev-confluence.ju.globaz.ch/pages/viewpage.action?pageId=3768397">Les genres de rente sur
     * Confluence</a>
     * </p>
     * 
     * @return la rente accord�e principale de cette demande.
     * @throws envera
     *             une {@link IllegalArgumentException} si la demande ne comporte aucune rente
     */
    public final RenteAccordee getRenteAccordeePrincipale() {
        if (rentesAccordees.isEmpty()) {
            throw new IllegalArgumentException("can't find the main prestation if there isn't any");
        }
        RenteAccordee prestationAccordeePrincipale = null;

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            if (uneRenteAccordee.estUneRentePrincipale()) {
                prestationAccordeePrincipale = uneRenteAccordee;
            }
        }

        // S'il n'y a pas de prestation principale, on recherche les compl�mentaires pour conjoint
        if (prestationAccordeePrincipale == null) {
            for (RenteAccordee uneRenteAccordee : rentesAccordees) {
                if (uneRenteAccordee.estUneRenteComplementairePourConjoint()) {
                    prestationAccordeePrincipale = uneRenteAccordee;
                }
            }
        }

        // S'il n'y a pas de compl�mentaire pour conjoint, on recherche les compl�mentaires pour enfant
        if (prestationAccordeePrincipale == null) {
            for (RenteAccordee uneRenteAccordee : rentesAccordees) {
                if (uneRenteAccordee.estUneRenteComplementairePourEnfant()) {
                    prestationAccordeePrincipale = uneRenteAccordee;
                }
            }
        }

        // S'il n'y a pas de compl�mentaire pour enfant, c'est que c'est une demande API, on prend donc celle ayant la
        // date de fin la plus �loign�e dans le temps
        if (prestationAccordeePrincipale == null) {
            for (RenteAccordee uneRenteAccordee : rentesAccordees) {
                if (prestationAccordeePrincipale == null) {
                    prestationAccordeePrincipale = uneRenteAccordee;
                } else if (!JadeStringUtil.isBlankOrZero(prestationAccordeePrincipale.getMoisFin())
                        && JadeStringUtil.isBlankOrZero(uneRenteAccordee.getMoisFin())) {
                    prestationAccordeePrincipale = uneRenteAccordee;
                } else if (!JadeStringUtil.isBlankOrZero(prestationAccordeePrincipale.getMoisFin())
                        && !JadeStringUtil.isBlankOrZero(uneRenteAccordee.getMoisFin())
                        && JadeDateUtil.isDateMonthYearBefore(prestationAccordeePrincipale.getMoisFin(),
                                uneRenteAccordee.getMoisFin())) {
                    prestationAccordeePrincipale = uneRenteAccordee;
                }
            }
        }

        // si toujours pas de prestation accord�e principale, c'est qu'il n'y a pas de prestation accord�e
        // on l�ve donc une exception
        if (prestationAccordeePrincipale == null) {
            throw new IllegalStateException("No prestations were found");
        }

        return prestationAccordeePrincipale;
    }

    /**
     * @return une liste immuable contenant les prestations accord�es li�es � cette demande de rente
     */
    public final Collection<RenteAccordee> getRentesAccordees() {
        return Collections.unmodifiableSet(rentesAccordees);
    }

    /**
     * @return le requ�rant de cette demande
     */
    public final PersonneAVS getRequerant() {
        return getDossier().getRequerant();
    }

    /**
     * @return le type de calcul appliqu� pour cette demande de rente
     */
    public final TypeCalculDemandeRente getTypeCalcul() {
        return typeCalcul;
    }

    /**
     * @return le type de cette demande de rente
     */
    public final TypeDemandeRente getTypeDemandeRente() {
        return typeDemandeRente;
    }

    /**
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @return vrai si un des rentes de la demande ne comporte par le code cas sp�cial pass� en param�tre
     * @throws NullPointerException
     *             si le code cas sp�cial pass� en param�tre est null
     */
    public final boolean comporteDesRentesAccordeesSansCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "codeCasSpecial");

        boolean codeCasSpecialAbsent = false;

        Iterator<RenteAccordee> iterator = rentesAccordees.iterator();
        while (iterator.hasNext() && !codeCasSpecialAbsent) {
            codeCasSpecialAbsent |= !iterator.next().comporteCodeCasSpecial(codeCasSpecial);
        }

        return codeCasSpecialAbsent;
    }

    /**
     * <p>
     * Retourne une liste des rentes ayant le code cas sp�cial pass� en param�tre.
     * </p>
     * 
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @return les rentes ayant ce code cas sp�cial, ou une liste vide si rien n'a �t� trouv�
     * @throw {@link IllegalArgumentException} si le code cas sp�cial est invalide (null)
     */
    public final Set<RenteAccordee> filtrerRentesAccordeesAvecCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Checkers.checkNotNull(codeCasSpecial, "codeCasSpecial");
        Set<RenteAccordee> rentesAvecCodeCasSpecial = new HashSet<RenteAccordee>();
        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            if (uneRenteDeLaDemande.comporteCodeCasSpecial(codeCasSpecial)) {
                rentesAvecCodeCasSpecial.add(uneRenteDeLaDemande);
            }
        }
        return rentesAvecCodeCasSpecial;
    }

    /**
     * <p>
     * Retourne la liste des rentes dont le b�n�ficiaire est celui pass� en param�tre
     * </p>
     * 
     * @param beneficiaire
     *            un b�n�ficiaire
     * @return les rentes de ce b�n�ficiaire dans cette demande
     * @throws NullPointerException
     *             si le b�n�ficiaire pass� en param�tre est null
     */
    public final Set<RenteAccordee> filtrerRentesAccordeesDontLeBeneficiaireEst(final PersonneAVS beneficiaire) {
        Checkers.checkNotNull(beneficiaire, "beneficiaire");
        Set<RenteAccordee> rentesDuBeneficiaire = new HashSet<RenteAccordee>();
        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            if (beneficiaire.equals(uneRenteDeLaDemande.getBeneficiaire())) {
                rentesDuBeneficiaire.add(uneRenteDeLaDemande);
            }
        }
        return rentesDuBeneficiaire;
    }

    public final Set<RenteAccordee> rentesAccordeesNeCorrespondantPasAuTypeDeLaDemande() {
        Set<RenteAccordee> rentesDeTypeDifferent = new HashSet<RenteAccordee>();
        Set<CodePrestation> codesPrestationSelonLeTypeDeLaDemande = codesPrestationsAcceptesPourCeTypeDeDemande();

        for (RenteAccordee uneRenteAccordeeDeLaDemande : rentesAccordees) {
            if (!codesPrestationSelonLeTypeDeLaDemande.contains(uneRenteAccordeeDeLaDemande.getCodePrestation())) {
                rentesDeTypeDifferent.add(uneRenteAccordeeDeLaDemande);
            }
        }

        return rentesDeTypeDifferent;
    }

    /**
     * Retourne les rentes accord�es dont le code prestation ne correspond pas au type de la demande (une rente
     * d'invalidit� sur une demande de vieillesse par exemple)
     * 
     * @return une liste contenant les rentes accord�es, ou une liste vide si rien n'est trouv�
     */
    public final Set<RenteAccordee> filtrerRentesAccordeesNeCorrespondantPasAuTypeDeLaDemande() {
        Set<RenteAccordee> rentesDeTypeDifferent = new HashSet<RenteAccordee>();
        Set<CodePrestation> codesPrestationSelonLeTypeDeLaDemande = codesPrestationsAcceptesPourCeTypeDeDemande();

        for (RenteAccordee uneRenteAccordeeDeLaDemande : rentesAccordees) {
            if (!codesPrestationSelonLeTypeDeLaDemande.contains(uneRenteAccordeeDeLaDemande.getCodePrestation())) {
                rentesDeTypeDifferent.add(uneRenteAccordeeDeLaDemande);
            }
        }

        return rentesDeTypeDifferent;
    }

    /**
     * <p>
     * Retourne une liste des rentes n'ayant pas le code cas sp�cial pass� en param�tre
     * </p>
     * 
     * @param codeCasSpecial
     *            un code cas sp�cial
     * @return les rentes n'ayant pas ce code cas sp�cial, ou une liste vide si rien n'a �t� trouv�
     * @throw {@link IllegalArgumentException} si le code cas sp�cial est invalide (null)
     */
    public final Set<RenteAccordee> filtrerRentesAccordeesSansCodeCasSpecial(final CodeCasSpecialRente codeCasSpecial) {
        Set<RenteAccordee> rentesSansCodeCasSpecial = new HashSet<RenteAccordee>();
        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            if (!uneRenteDeLaDemande.comporteCodeCasSpecial(codeCasSpecial)) {
                rentesSansCodeCasSpecial.add(uneRenteDeLaDemande);
            }
        }
        return rentesSansCodeCasSpecial;
    }

    /**
     * (re-)d�fini le d�but du droit initial de cette demande de rente, uniquement du point de vue du requ�rant (ne pas
     * confondre avec le droit des rentes principales des conjoints du requ�rant ayant �t� rabattues sur cette demande)
     * 
     * @param dateDebutDuDroitInitial
     *            une date au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format, ou est vide
     */
    public final void setDateDebutDuDroitInitial(final String dateDebutDuDroitInitial) {
        Checkers.checkNotNull(dateDebutDuDroitInitial, "demande.dateDebutDuDroitInitial");
        Checkers.checkFullDate(dateDebutDuDroitInitial, "demande.dateDebutDuDroitInitial", true);
        this.dateDebutDuDroitInitial = dateDebutDuDroitInitial;
    }

    /**
     * (re-)d�fini la date de d�p�t de cette demande de rente
     * 
     * @param dateDepot
     *            une date au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format, ou est vide
     */
    public final void setDateDepot(final String dateDepot) {
        Checkers.checkNotNull(dateDepot, "demande.dateDepot");
        Checkers.checkFullDate(dateDepot, "demande.dateDepot", true);
        this.dateDepot = dateDepot;
    }

    /**
     * (re-)d�fini la fin du droit initial de cette demande de rente, uniquement du point de vue du requ�rant (ne pas
     * confondre avec le droit des rentes principales des conjoints du requ�rant ayant �t� rabattues sur cette demande)
     * 
     * @param dateFinDuDroitInitial
     *            une date au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format, ou est vide
     */
    public final void setDateFinDuDroitInitial(final String dateFinDuDroitInitial) {
        Checkers.checkNotNull(dateFinDuDroitInitial, "demande.dateFinDuDroitInitial");
        Checkers.checkFullDate(dateFinDuDroitInitial, "demande.dateFinDuDroitInitial", true);
        this.dateFinDuDroitInitial = dateFinDuDroitInitial;
    }

    /**
     * (re-)d�fini la date de r�ception de cette demande de rente
     * 
     * @param dateReception
     *            une date au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format, ou est vide
     */
    public final void setDateReception(final String dateReception) {
        Checkers.checkNotNull(dateReception, "demande.dateReception");
        Checkers.checkFullDate(dateReception, "demande.dateReception", true);
        this.dateReception = dateReception;
    }

    /**
     * (re-)d�fini la date � laquelle cette demande a �t� trait�e
     * 
     * @param dateTraitement
     *            une date au format JJ.MM.AAAA
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format, ou est vide
     */
    public final void setDateTraitement(final String dateTraitement) {
        Checkers.checkNotNull(dateTraitement, "demande.dateTraitement");
        Checkers.checkFullDate(dateTraitement, "demande.dateTraitement", true);
        this.dateTraitement = dateTraitement;
    }

    /**
     * (re-)d�fini l'�tat de cette demande de rente
     * 
     * @param etatDemandeRente
     *            un �tat valide (non-null)
     * @throws NullPointerException
     *             si l'�tat pass� en param�tre est null
     */
    public final void setEtat(final EtatDemandeRente etatDemandeRente) {
        Checkers.checkNotNull(etatDemandeRente, "demande.etat");
        etat = etatDemandeRente;
    }

    /**
     * (re-)d�fini le gestionnaire de cette demande de rente
     * 
     * @param gestionnaire
     *            un nom d'utilisateur de gestionnaire, ne peut pas �tre null
     * @throws NullPointerException
     *             si le gestionnaire pass� en param�tre est null
     */
    public final void setGestionnaire(final String gestionnaire) {
        Checkers.checkNotNull(gestionnaire, "demande.gestionnaire");
        this.gestionnaire = gestionnaire;
    }

    /**
     * (re-)d�fini les rentes accord�es li�es � cette demande de rente
     * 
     * @param rentesAccordees
     *            la liste des rentes accord�es, ne peut pas �tre null
     * @throws NullPointerException
     *             si la liste pass� en param�tre est null
     */
    public final void setRentesAccordees(final Collection<RenteAccordee> rentesAccordees) {
        Checkers.checkNotNull(rentesAccordees, "demande.rentesAccordees");
        // pas de test sur le genre des rentes par rapport au type de demande, car cela posait trop de probl�me d� au
        // fonctionnement actuel de l'application
        this.rentesAccordees = new HashSet<RenteAccordee>(rentesAccordees);
    }

    /**
     * (re-)d�fini le type de calcul appliqu� lors du calcul de cette demande de rente
     * 
     * @param typeCalculDemandeRente
     *            un type de calcul valide
     * @throws NullPointerException
     *             si le type de calcul pass� en param�tre est null
     */
    public final void setTypeCalcul(final TypeCalculDemandeRente typeCalculDemandeRente) {
        Checkers.checkNotNull(typeCalculDemandeRente, "demande.typeCalcul");
        typeCalcul = typeCalculDemandeRente;
    }

    /**
     * (re-)d�fini le type de cette demande de rente
     * 
     * @param typeDemandeRente
     *            un type de demande de rente
     * @throws NullPointerException
     *             si le type de demande pass� en param�tre est null
     */
    public final void setTypeDemandeRente(final TypeDemandeRente typeDemandeRente) {
        Checkers.checkNotNull(typeDemandeRente, "demande.type");
        this.typeDemandeRente = typeDemandeRente;
    }

    /**
     * <p>
     * Retournes les rentes de cette demande dont le b�n�ficiaire est la personne pass�e en param�tre.
     * </p>
     * <p>
     * Si cette personne n'a pas de rente dans cette demande, la liste retourn�e sera vide
     * </p>
     * 
     * @param beneficiaire une personne
     * @return la liste des rentes accord�es dont cette personne est b�n�ficiaire dans cette demande, ou une liste vide
     *         si elle n'est au b�n�fice d'aucune rente accord�e dans cette demande
     * @throws NullPointerException si la personne pass�e en param�tre est null
     * @throws IllegalArgumentException si la personne pass�e en param�tre n'est pas initialis�e (n'a pas d'ID)
     */
    public Set<RenteAccordee> getRentesAccordeesDuBeneficiaire(PersonneAVS beneficiaire) {
        Checkers.checkNotNull(beneficiaire, "beneficiaire");
        Checkers.checkHasID(beneficiaire, "beneficiaire");

        Set<RenteAccordee> rentesDuBeneficiaire = new HashSet<RenteAccordee>();

        for (RenteAccordee uneRenteDeLaDemande : rentesAccordees) {
            if (uneRenteDeLaDemande.getBeneficiaire().equals(beneficiaire)) {
                rentesDuBeneficiaire.add(uneRenteDeLaDemande);
            }
        }

        return rentesDuBeneficiaire;
    }

    /**
     * @param mois un mois au format MM.AAAA
     * @return vrai si une rente de cette demande d�bute avant le mois pass� en param�tre
     * @throws NullPointerException si le mois pass� en param�tre est null
     * @throws IllegalArgumentException si le mois pass� en param�tre n'est pas au bon format
     */
    public boolean comporteDesRentesAccordeesCommencantAvantCeMois(String mois) {

        Checkers.checkNotNull(mois, "mois");
        Checkers.checkDateMonthYear(mois, "mois", false);

        boolean uneRenteCommenceAvantLeMois = false;

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            uneRenteCommenceAvantLeMois |= JadeDateUtil.isDateMonthYearBefore(uneRenteAccordee.getMoisDebut(), mois);
        }

        return uneRenteCommenceAvantLeMois;
    }

    /**
     * @param mois un mois au format MM.AAAA
     * @return vrai si une rente de cette demande d�bute apr�s le mois pass� en param�tre
     * @throws NullPointerException si le mois pass� en param�tre est null
     * @throws IllegalArgumentException si le mois pass� en param�tre n'est pas au bon format
     */
    public boolean comporteDesRentesAccordeesCommencantApresCeMois(String mois) {

        Checkers.checkNotNull(mois, "mois");
        Checkers.checkDateMonthYear(mois, "mois", false);

        boolean uneRenteCommenceApresLeMois = false;

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            uneRenteCommenceApresLeMois |= JadeDateUtil.isDateMonthYearAfter(uneRenteAccordee.getMoisDebut(), mois);
        }

        return uneRenteCommenceApresLeMois;
    }

    /**
     * @param mois un mois au format MM.AAAA
     * @return vrai si une rente de cette demande d�bute durant le mois pass� en param�tre
     * @throws NullPointerException si le mois pass� en param�tre est null
     * @throws IllegalArgumentException si le mois pass� en param�tre n'est pas au bon format
     */
    public boolean comporteDesRentesAccordeesCommencantDansCeMois(String mois) {

        Checkers.checkNotNull(mois, "mois");
        Checkers.checkDateMonthYear(mois, "mois", false);

        boolean uneRenteCommenceDansLeMois = false;

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            uneRenteCommenceDansLeMois |= mois.equals(uneRenteAccordee.getMoisDebut());
        }

        return uneRenteCommenceDansLeMois;
    }
}
