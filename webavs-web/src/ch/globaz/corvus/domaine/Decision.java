package ch.globaz.corvus.domaine;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.EtatDecisionRente;
import ch.globaz.corvus.domaine.constantes.TypeDecisionRente;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeTraitementDecisionRente;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Tiers;

/**
 * Objet de domaine représentant une décision de rente
 */
public final class Decision extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail;
    private boolean afficherTexteAnnulerDecision;
    private boolean afficherTexteAvecBonneFoi;
    private boolean afficherTexteIncarceration;
    private boolean afficherTexteInteretMoratoires;
    private boolean afficherTexteObligationPayerCotisation;
    private boolean afficherTexteReductionPourPlafonnement;
    private boolean afficherTexteRemariageRenteDeSurvivant;
    private boolean afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    private boolean afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    private boolean afficherTexteRenteDeVeufLimitee;
    private boolean afficherTexteRenteDeVeuveLimitee;
    private boolean afficherTexteRentePourEnfant;
    private boolean afficherTexteRenteReduitePourSurassurance;
    private boolean afficherTexteSansBonneFoi;
    private boolean afficherTexteSupplementVeuve;
    private PersonneAVS beneficiairePrincipal;
    private Set<CompensationInterDecision> compensationsInterDecision;
    /** JJ.MM.AAAA */
    private String dateDecision;
    /** JJ.MM.AAAA */
    private String datePreparation;
    /** JJ.MM.AAAA */
    private String dateValidation;
    private DemandeRente demande;
    private EtatDecisionRente etat;
    private String gestionnairePreparation;
    private String gestionnaireTraitement;
    private String gestionnaireValidation;
    /** MM.AAAA */
    private String moisDebutRetro;
    /** MM.AAAA */
    private String moisDecisionDepuis;
    /** MM.AAAA */
    private String moisFinRetro;
    private Prestation prestation;
    private String remarque;
    private Set<RenteAccordee> rentesAccordees;
    private Tiers tiersCorrespondance;
    private TypeDecisionRente typeDecision;
    private TypeTraitementDecisionRente typeTraitement;

    public Decision() {
        super();

        adresseEmail = "";
        afficherTexteAnnulerDecision = false;
        afficherTexteAvecBonneFoi = false;
        afficherTexteIncarceration = false;
        afficherTexteInteretMoratoires = false;
        afficherTexteObligationPayerCotisation = false;
        afficherTexteReductionPourPlafonnement = false;
        afficherTexteRemariageRenteDeSurvivant = false;
        afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande = false;
        afficherTexteRenteAvecMontantMinimumMajoreInvalidite = false;
        afficherTexteRenteDeVeufLimitee = false;
        afficherTexteRenteDeVeuveLimitee = false;
        afficherTexteRentePourEnfant = false;
        afficherTexteRenteReduitePourSurassurance = false;
        afficherTexteSansBonneFoi = false;
        afficherTexteSupplementVeuve = false;
        beneficiairePrincipal = new PersonneAVS();
        compensationsInterDecision = new HashSet<CompensationInterDecision>();
        dateDecision = "";
        datePreparation = "";
        dateValidation = "";
        demande = new DemandeRenteVieillesse();
        etat = EtatDecisionRente.EN_ATTENTE;
        gestionnairePreparation = "";
        gestionnaireTraitement = "";
        gestionnaireValidation = "";
        moisDebutRetro = "";
        moisDecisionDepuis = "";
        moisFinRetro = "";
        prestation = new Prestation();
        rentesAccordees = new HashSet<RenteAccordee>();
        remarque = "";
        tiersCorrespondance = new Tiers();
        typeDecision = TypeDecisionRente.DECISION;
        typeTraitement = TypeTraitementDecisionRente.STANDARD;
    }

    /**
     * @return l'adresse email à laquelle le document imprimé sera envoyé
     */
    public final String getAdresseEmail() {
        return adresseEmail;
    }

    /**
     * @return le bénéficiaire principal de cette décision
     */
    public final PersonneAVS getBeneficiairePrincipal() {
        return beneficiairePrincipal;
    }

    /**
     * @return la liste des compensations inter-décision liées à cette décision
     */
    public final Set<CompensationInterDecision> getCompensationsInterDecision() {
        return Collections.unmodifiableSet(compensationsInterDecision);
    }

    /**
     * @return la date à laquelle la décision a été prise (au format JJ.MM.AAAA)
     */
    public final String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return date à laquelle la décision a été préparée (au format JJ.MM.AAAA)
     */
    public final String getDatePreparation() {
        return datePreparation;
    }

    /**
     * @return date à laquelle la décision a été validée (au format JJ.MM.AAAA)
     */
    public final String getDateValidation() {
        return dateValidation;
    }

    /**
     * @return la demande de rente dont découle cette décision
     */
    public final DemandeRente getDemande() {
        return demande;
    }

    /**
     * @return l'état de cette décision
     */
    public final EtatDecisionRente getEtat() {
        return etat;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire ayant préparé cette décision
     */
    public final String getGestionnairePreparation() {
        return gestionnairePreparation;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire étant en charge du traitement de cette décision (celui ayant rentré
     *         la demande de rente dont découle cette décision)
     */
    public final String getGestionnaireTraitement() {
        return gestionnaireTraitement;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire ayant validé cette décision
     */
    public final String getGestionnaireValidation() {
        return gestionnaireValidation;
    }

    /**
     * @return mois de début de la période rétroactive de cette décision (format MM.AAAA)
     */
    public final String getMoisDebutRetro() {
        return moisDebutRetro;
    }

    /**
     * @return mois auquel la décision a été prise comme "courant validé" (date au format MM.AAAA)
     */
    public final String getMoisDecisionDepuis() {
        return moisDecisionDepuis;
    }

    /**
     * @return mois de fin de la période rétroactive de cette décision (au format MM.AAAA)
     */
    public final String getMoisFinRetro() {
        return moisFinRetro;
    }

    /**
     * Calcul et retourne le montant total des créances et dettes pour cette décision
     * 
     * @return le montant total des dettes et créances
     */
    public final BigDecimal getMontantTotalDettesEtCreances() {
        return getSoldePourTypesOrdreVersement(TypeOrdreVersement.DETTE, TypeOrdreVersement.CREANCIER);
    }

    /**
     * Calcul et retourne le montant total des gains du bénéficiaire principal de cette décision
     * 
     * @return le montant total des gains
     */
    public final BigDecimal getMontantTotalGainsBeneficiairePrincipal() {
        return getSoldePourTypesOrdreVersement(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL,
                TypeOrdreVersement.INTERET_MORATOIRE);
    }

    /**
     * @return la liste des ordres de versement de la décision dans un conteneur invariable
     */
    public final Set<OrdreVersement> getOrdresVersement() {
        return Collections.unmodifiableSet(prestation.getOrdresVersement());
    }

    /**
     * Retourne une liste des ordres de versements correspondants au(x) type(s) passé(s) en paramètre
     * 
     * @param types
     *            le(s) type(s) d'ordre(s) de versement voulu(s)
     * @return la liste des ordres de versements correspondants au(x) type(s) passé(s) en paramètre
     */
    public final Set<OrdreVersement> getOrdresVersementPourType(final TypeOrdreVersement... types) {
        Checkers.checkNotNull(types, "must have at least one type");

        Set<OrdreVersement> ordresVersementVoulus = new HashSet<OrdreVersement>();
        Set<TypeOrdreVersement> typesVoulus = new HashSet<TypeOrdreVersement>(Arrays.asList(types));

        for (OrdreVersement unOrdreVersement : prestation.getOrdresVersement()) {
            if (typesVoulus.contains(unOrdreVersement.getType())) {
                ordresVersementVoulus.add(unOrdreVersement);
            }
        }

        return ordresVersementVoulus;
    }

    public final OrdreVersement getOrdreVersementPourRenteAccordeeNouveauDroit(final RenteAccordee renteAccordee) {
        Checkers.checkNotNull(renteAccordee, "renteAccordee");
        Checkers.checkHasID(renteAccordee, "prestationAccordee");

        OrdreVersement ordreVersement = null;

        Iterator<OrdreVersement> iterator = prestation.getOrdresVersement().iterator();
        while (iterator.hasNext() && (ordreVersement == null)) {
            OrdreVersement unOrdreVersement = iterator.next();

            if (renteAccordee.equals(unOrdreVersement.getRenteAccordeeNouveauDroit())) {
                ordreVersement = unOrdreVersement;
            }
        }

        return ordreVersement;
    }

    /**
     * @return la prestation liée à cette décision
     */
    public final Prestation getPrestation() {
        return prestation;
    }

    /**
     * @return la remarque qui sera affiché en bas du document imprimé
     */
    public final String getRemarque() {
        return remarque;
    }

    /**
     * @return la rente principale de cette décision (celle ayant un code prestation principale). S'il n'y en a pas
     *         (rente pour enfant seulement, API, etc...) retourne la première rente de la liste
     */
    public final RenteAccordee getRenteAccordeePrincipale() {
        RenteAccordee renteAccordeePrincipale = null;

        Iterator<RenteAccordee> iterator = rentesAccordees.iterator();
        while (iterator.hasNext() && (renteAccordeePrincipale == null)) {
            RenteAccordee uneRenteDeLaDecision = iterator.next();
            if (uneRenteDeLaDecision.estUneRentePrincipale()) {
                renteAccordeePrincipale = uneRenteDeLaDecision;
            }
        }

        // Si pas de rente avec code prestation principal, on prend la 1ère rente de la décision
        if (renteAccordeePrincipale == null) {
            renteAccordeePrincipale = rentesAccordees.iterator().next();
        }

        return renteAccordeePrincipale;
    }

    /**
     * @return les prestations accordées liées à cette décision (dans un conteneur invariable)
     */
    public final Set<RenteAccordee> getRentesAccordees() {
        return Collections.unmodifiableSet(rentesAccordees);
    }

    /**
     * Calcul et retourne le solde de cette décision (en fonction des ordres de versement)
     * 
     * @return le solde de la décision
     */
    public final BigDecimal getSolde() {
        BigDecimal montantTotalCID = BigDecimal.ZERO;
        // Pour toutes les compensation inter-décision
        for (CompensationInterDecision uneCID : compensationsInterDecision) {
            // On récupère l'OV associé à la CID
            OrdreVersement ovCID = uneCID.getOrdreVersementDecisionCompensee();
            // On recherche tous les OV de type dette liés à la prestation
            Set<OrdreVersement> ovTypeDetteDeLaPrestation = getOrdresVersementPourType(TypeOrdreVersement.DETTE);
            // Si l'ov de la CID est lié à la prestation on cumul le montant
            if (ovTypeDetteDeLaPrestation.contains(ovCID)) {
                montantTotalCID = montantTotalCID.add(uneCID.getMontantCompense());
            }
        }
        BigDecimal soldes = getSoldePourTypesOrdreVersement(TypeOrdreVersement.values());
        return soldes.add(montantTotalCID);
    }

    private BigDecimal getSoldePourTypesOrdreVersement(final TypeOrdreVersement... types) {
        Set<TypeOrdreVersement> typesVoulus = new HashSet<TypeOrdreVersement>(Arrays.asList(types));
        BigDecimal solde = BigDecimal.ZERO;

        for (OrdreVersement unOrdreVersement : prestation.getOrdresVersement()) {
            if (typesVoulus.contains(unOrdreVersement.getType())) {
                solde = solde.add(unOrdreVersement.getMontantPourSolde());
            }
        }

        return solde;
    }

    /**
     * Retourne le solde de cette décision sans prendre en compte les intérêts moratoires. Ceci est utile dans le cas du
     * calcul du solde pour restitution
     * 
     * @return
     */
    public final BigDecimal getSoldeSansInteretsMoratoires() {
        return getSolde().subtract(getSoldePourTypesOrdreVersement(TypeOrdreVersement.INTERET_MORATOIRE));
    }

    /**
     * @return le tiers à qui sera envoyé la décision imprimée
     */
    public final Tiers getTiersCorrespondance() {
        return tiersCorrespondance;
    }

    /**
     * @return le type de décision (décision, décision sur opposition ou communication) cette décision de rente
     */
    public final TypeDecisionRente getTypeDecision() {
        return typeDecision;
    }

    /**
     * @return le type de traitement (standard, courant ou rétroactif) cette décision de rente
     */
    public final TypeTraitementDecisionRente getTypeTraitement() {
        return typeTraitement;
    }

    /**
     * @return vrai si le texte concernant l'annulation de décision doit être affiché sur la décision
     */
    public final boolean isAfficherTexteAnnulerDecision() {
        return afficherTexteAnnulerDecision;
    }

    /**
     * @return vrai si le texte concernant la bonne fois doit être affiché sur la décision
     */
    public final boolean isAfficherTexteAvecBonneFoi() {
        return afficherTexteAvecBonneFoi;
    }

    /**
     * @return vrai si le texte concerant l'incarcération doit être affiché sur la décision
     */
    public final boolean isAfficherTexteIncarceration() {
        return afficherTexteIncarceration;
    }

    /**
     * @return vrai si le texte concernant les intérêts moratoires doit être affiché sur la décision
     */
    public final boolean isAfficherTexteInteretMoratoires() {
        return afficherTexteInteretMoratoires;
    }

    /**
     * @return vrai si le texte concernant l'obligation de payer des cotisations doit être affiché sur la décision
     */
    public final boolean isAfficherTexteObligationPayerCotisation() {
        return afficherTexteObligationPayerCotisation;
    }

    /**
     * @return vrai si le texte concernant le plafonnement du montant doit être affiché sur la décision
     */
    public final boolean isAfficherTexteReductionPourPlafonnement() {
        return afficherTexteReductionPourPlafonnement;
    }

    /**
     * @return vrai si le texte concernant le remariage de survivant doit être affiché sur la décision
     */
    public final boolean isAfficherTexteRemariageRenteDeSurvivant() {
        return afficherTexteRemariageRenteDeSurvivant;
    }

    /**
     * @return vrai si le texte concernant la prescription de 5 ans doit être affiché sur la décision
     */
    public final boolean isAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande() {
        return afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    /**
     * @return vrai si le texte concernant la majoration du montant minium pour l'invalidité doit être affiché sur la
     *         décision
     */
    public final boolean isAfficherTexteRenteAvecMontantMinimumMajoreInvalidite() {
        return afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    }

    /**
     * @return vrai si le texte concernant la limitation d'une rente de veuf doit être affiché sur la décision
     */
    public final boolean isAfficherTexteRenteDeVeufLimitee() {
        return afficherTexteRenteDeVeufLimitee;
    }

    /**
     * @return vrai si le texte concernant la limitation d'une rente de veuve doit être affiché sur la décision
     */
    public final boolean isAfficherTexteRenteDeVeuveLimitee() {
        return afficherTexteRenteDeVeuveLimitee;
    }

    /**
     * @return vrai si le texte concernant les rentes pour enfants doit être affiché sur la décision
     */
    public final boolean isAfficherTexteRentePourEnfant() {
        return afficherTexteRentePourEnfant;
    }

    /**
     * @return vrai si le texte concernant la réduction pour sur-assurance doit être affiché sur la décision
     */
    public final boolean isAfficherTexteRenteReduitePourSurassurance() {
        return afficherTexteRenteReduitePourSurassurance;
    }

    /**
     * @return vrai si le texte concernant le manque de bonne foi doit être affiché sur la décision
     */
    public final boolean isAfficherTexteSansBonneFoi() {
        return afficherTexteSansBonneFoi;
    }

    /**
     * @return vrai si le texte concernant le supplément pour rente de veuve doit être affiché sur la décision
     */
    public final boolean isAfficherTexteSupplementVeuve() {
        return afficherTexteSupplementVeuve;
    }

    /**
     * @return vrai si cette décision est une décision retroactive pure, c'est à dire que toutes les prestations
     *         accordées ont une date de fin
     */
    public final boolean isRetroPur() {
        boolean isRetroPur = true;

        Iterator<RenteAccordee> iterator = getRentesAccordees().iterator();
        while (iterator.hasNext() && isRetroPur) {
            RenteAccordee uneRenteAccordeeDeLaDecision = iterator.next();
            if (JadeStringUtil.isBlankOrZero(uneRenteAccordeeDeLaDecision.getMoisFin())) {
                isRetroPur = false;
            }
        }

        return isRetroPur;
    }

    /**
     * (re-)défini l'adresse email à laquelle le document imprimé sera envoyé
     * 
     * @param adresseEmail
     * @throws NullPointerException
     *             si l'adresse email passée en paramètre est null
     */
    public final void setAdresseEmail(final String adresseEmail) {
        Checkers.checkNotNull(adresseEmail, "decision.adresseEmail");
        this.adresseEmail = adresseEmail;
    }

    /**
     * (re-)défini si le texte concernant l'annulation de décision doit être affiché sur la décision
     * 
     * @param afficherTexteAnnulerDecision
     */
    public final void setAfficherTexteAnnulerDecision(final boolean afficherTexteAnnulerDecision) {
        this.afficherTexteAnnulerDecision = afficherTexteAnnulerDecision;
    }

    /**
     * (re-)défini si le texte concernant la bonne foi doit être affiché sur la décision
     * 
     * @param afficherTexteAvecBonneFoi
     */
    public final void setAfficherTexteAvecBonneFoi(final boolean afficherTexteAvecBonneFoi) {
        this.afficherTexteAvecBonneFoi = afficherTexteAvecBonneFoi;
    }

    /**
     * (re-)défini si le texte concerant l'incarcération doit être affiché sur la décision
     * 
     * @param afficherTexteIncarceration
     */
    public final void setAfficherTexteIncarceration(final boolean afficherTexteIncarceration) {
        this.afficherTexteIncarceration = afficherTexteIncarceration;
    }

    /**
     * (re-)défini si le texte concernant les intérêts moratoires doit être affiché sur la décision
     * 
     * @param afficherTexteInteretMoratoires
     */
    public final void setAfficherTexteInteretMoratoires(final boolean afficherTexteInteretMoratoires) {
        this.afficherTexteInteretMoratoires = afficherTexteInteretMoratoires;
    }

    /**
     * (re-)défini si le texte concernant l'obligation de payer des cotisations doit être affiché sur la décision
     * 
     * @param afficherTexteObligationPayerCotisation
     */
    public final void setAfficherTexteObligationPayerCotisation(final boolean afficherTexteObligationPayerCotisation) {
        this.afficherTexteObligationPayerCotisation = afficherTexteObligationPayerCotisation;
    }

    /**
     * (re-)défini si le texte concernant le plafonnement du montant doit être affiché sur la décision
     * 
     * @param afficherTexteReductionPourPlafonnement
     */
    public final void setAfficherTexteReductionPourPlafonnement(final boolean afficherTexteReductionPourPlafonnement) {
        this.afficherTexteReductionPourPlafonnement = afficherTexteReductionPourPlafonnement;
    }

    /**
     * (re-)défini si le texte concernant le remariage de survivant doit être affiché sur la décision
     * 
     * @param afficherTexteRemariageRenteDeSurvivant
     */
    public final void setAfficherTexteRemariageRenteDeSurvivant(final boolean afficherTexteRemariageRenteDeSurvivant) {
        this.afficherTexteRemariageRenteDeSurvivant = afficherTexteRemariageRenteDeSurvivant;
    }

    /**
     * (re-)défini si le texte concernant la prescription de 5 ans doit être affiché sur la décision
     * 
     * @param afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande
     */
    public final void setAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande(
            final boolean afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande) {
        this.afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande = afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    /**
     * (re-)défini si le texte concernant la majoration du montant minimum pour l'invalidité doit être affiché sur la
     * décision
     * 
     * @param afficherTexteRenteAvecMontantMinimumMajoreInvalidite
     */
    public final void setAfficherTexteRenteAvecMontantMinimumMajoreInvalidite(
            final boolean afficherTexteRenteAvecMontantMinimumMajoreInvalidite) {
        this.afficherTexteRenteAvecMontantMinimumMajoreInvalidite = afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    }

    /**
     * (re-)défini si le texte concernant la limitation d'une rente de veuf doit être affiché sur la décision
     * 
     * @param afficherTexteRenteDeVeufLimitee
     */
    public final void setAfficherTexteRenteDeVeufLimitee(final boolean afficherTexteRenteDeVeufLimitee) {
        this.afficherTexteRenteDeVeufLimitee = afficherTexteRenteDeVeufLimitee;
    }

    /**
     * (re-)défini si le texte concernant la limitation d'une rente de veuve doit être affiché sur la décision
     * 
     * @param afficherTexteRenteDeVeuveLimitee
     */
    public final void setAfficherTexteRenteDeVeuveLimitee(final boolean afficherTexteRenteDeVeuveLimitee) {
        this.afficherTexteRenteDeVeuveLimitee = afficherTexteRenteDeVeuveLimitee;
    }

    /**
     * (re-)défini si le texte concernant les rentes pour enfants doit être affiché sur la décision
     * 
     * @param afficherTexteRentePourEnfant
     */
    public final void setAfficherTexteRentePourEnfant(final boolean afficherTexteRentePourEnfant) {
        this.afficherTexteRentePourEnfant = afficherTexteRentePourEnfant;
    }

    /**
     * (re-)défini si le texte concernant la réduction pour sur-assurance doit être affiché sur la décision
     * 
     * @param afficherTexteRenteReduitePourSurassurance
     */
    public final void setAfficherTexteRenteReduitePourSurassurance(
            final boolean afficherTexteRenteReduitePourSurassurance) {
        this.afficherTexteRenteReduitePourSurassurance = afficherTexteRenteReduitePourSurassurance;
    }

    /**
     * (re-)défini si le texte concernant le manque de bonne foi doit être affiché sur la décision
     * 
     * @param afficherTexteSansBonneFoi
     */
    public final void setAfficherTexteSansBonneFoi(final boolean afficherTexteSansBonneFoi) {
        this.afficherTexteSansBonneFoi = afficherTexteSansBonneFoi;
    }

    /**
     * (re-)défini si le texte concernant le supplément pour rente de veuve doit être affiché sur la décision
     * 
     * @param afficherTexteSupplementVeuve
     */
    public final void setAfficherTexteSupplementVeuve(final boolean afficherTexteSupplementVeuve) {
        this.afficherTexteSupplementVeuve = afficherTexteSupplementVeuve;
    }

    /**
     * (re-)défini le bénéficiaire principale de cette décision
     * 
     * @param beneficiairePrincipal
     *            un objet de domaine représentant une personne physique ayant un NSS
     * @throws NullPointerException
     *             si le bénéficiaire passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le bénéficiaire passé en paramètre n'est pas initialisé
     */
    public final void setBeneficiairePrincipal(final PersonneAVS beneficiairePrincipal) {
        Checkers.checkNotNull(beneficiairePrincipal, "decision.beneficiairePrincipal");
        Checkers.checkHasID(beneficiairePrincipal, "decision.beneficiairePrincipal");
        this.beneficiairePrincipal = beneficiairePrincipal;
    }

    /**
     * (re-)défini la liste des compensations inter-décision liées à cette décision
     * 
     * @param compensationsInterDecision
     *            la liste des CID liées à cette décision
     * @throws NullPointerException
     *             si la compensation inter-décision passée en paramètre est null
     */
    public final void setCompensationsInterDecision(final Set<CompensationInterDecision> compensationsInterDecision) {
        Checkers.checkNotNull(compensationsInterDecision, "decision.compensationsInterDecision");
        this.compensationsInterDecision = compensationsInterDecision;
    }

    /**
     * (re-)défini la date à laquelle la décision a été prise
     * 
     * @param dateDecision
     *            une chaîne de caractère représentant une date (format JJ.MM.AAAA)
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée en paramètre n'a pas le bon format ou est vide
     */
    public final void setDateDecision(final String dateDecision) {
        Checkers.checkNotNull(dateDecision, "decision.dateDecision");
        Checkers.checkFullDate(dateDecision, "decision.dateDecision", true);
        this.dateDecision = dateDecision;
    }

    /**
     * (re-)défini la date à laquelle la décision a été préparée
     * 
     * @param datePreparation
     *            une chaîne de caractère représentant une date (format JJ.MM.AAAA)
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée en paramètre n'est pas au bon format ou est vide
     */
    public final void setDatePreparation(final String datePreparation) {
        Checkers.checkNotNull(datePreparation, "decision.datePreparation");
        Checkers.checkFullDate(datePreparation, "decision.datePreparation", true);
        this.datePreparation = datePreparation;
    }

    /**
     * (re-)défini la date à laquelle la décision a été validée
     * 
     * @param dateValidation
     *            une chaîne de caractère représentant une date (format JJ.MM.AAAA)
     * @throws NullPointerException
     *             si la date passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la date passée en paramètre n'est pas au bon format ou est vide
     */
    public final void setDateValidation(final String dateValidation) {
        Checkers.checkNotNull(dateValidation, "decision.dateValidation");
        Checkers.checkFullDate(dateValidation, "decision.dateValidation", true);
        this.dateValidation = dateValidation;
    }

    /**
     * (re-)défini la demande dont découle cette décision de rente
     * 
     * @param demande
     *            un objet de domaine représentant une demande de rente
     * @throws NullPointerException
     *             si la demande passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la demande passée en paramètre n'est pas initialisée
     */
    public final void setDemande(final DemandeRente demande) {
        Checkers.checkNotNull(demande, "decision.demande");
        Checkers.checkHasID(demande, "decision.demande");
        this.demande = demande;
    }

    /**
     * (re-)défini l'état de cette décision de rente
     * 
     * @param etat
     *            un énuméré représentant l'état de cette décision de rente
     * @throws NullPointerException
     *             si l'état passé en paramètre est null
     */
    public final void setEtat(final EtatDecisionRente etat) {
        Checkers.checkNotNull(etat, "decision.etat");
        this.etat = etat;
    }

    /**
     * (re-)défini le gestionnaire ayant préparé cette décision
     * 
     * @param gestionnairePreparation
     *            le nom d'utilisateur du gestionnaire ayant préparé cette décision
     * @throws NullPointerException
     *             si la chaîne de caractères passée en paramètre est null
     */
    public final void setGestionnairePreparation(final String gestionnairePreparation) {
        Checkers.checkNotNull(gestionnairePreparation, "decision.gestionnairePreparation");
        this.gestionnairePreparation = gestionnairePreparation;
    }

    /**
     * (re-)défini le gestionnaire étant en charge du traitement de cette décision. Il s'agit du gestionnaire ayant créé
     * la demande de rente dont découle cette décision.
     * 
     * @param gestionnaireTraitement
     *            le nom d'utilisateur du gestionnaire étant en charge du traitement de cette décision
     * @throws NullPointerException
     *             si la chaîne de caractères passée en paramètre est null
     */
    public final void setGestionnaireTraitement(final String gestionnaireTraitement) {
        Checkers.checkNotNull(gestionnaireTraitement, "decision.gestionnaireTraitement");
        this.gestionnaireTraitement = gestionnaireTraitement;
    }

    /**
     * (re-)défini le gestionnaire ayant validé cette décision
     * 
     * @param gestionnaireValidation
     *            le nom d'utilisateur du gestionnaire ayant validé cette décision
     * @throws NullPointerException
     *             si la chaîne de caractères passée en paramètre est null
     */
    public final void setGestionnaireValidation(final String gestionnaireValidation) {
        Checkers.checkNotNull(gestionnaireValidation, "decision.gestionnaireValidation");
        this.gestionnaireValidation = gestionnaireValidation;
    }

    /**
     * (re-)défini le mois de début de la période rétroactive de cette décision de rente
     * 
     * @param moisDebutRetro
     *            une chaîne de caractère représentant un mois (format MM.AAAA)
     * @throws NullPointerException
     *             si le mois passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le mois passé en paramètre n'est pas vide et n'est pas au bon format
     * @throws IllegalArgumentException
     *             si le mois passé en paramètre est plus avancé dans le temps que {@link #moisFinRetro}
     */
    public final void setMoisDebutRetro(final String moisDebutRetro) {
        Checkers.checkNotNull(moisDebutRetro, "decision.moisDebutRetro");
        Checkers.checkDateMonthYear(moisDebutRetro, "decision.moisDebutRetro", true);
        Checkers.checkPeriodMonthYear(moisDebutRetro, moisFinRetro, "decision.moisDebutRetro", "decision.moisFinRetro");
        this.moisDebutRetro = moisDebutRetro;
    }

    /**
     * (re-)défini le mois à partir duquel la décision a été dans l'état "courant validé"
     * 
     * @param moisDecisionDepuis
     *            une chaîne de caractère représentant un mois (format MM.AAAA)
     * @throws NullPointerException
     *             si le mois passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le mois passé en paramètre n'est pas vide et n'est pas au bon format
     */
    public final void setMoisDecisionDepuis(final String moisDecisionDepuis) {
        Checkers.checkNotNull(moisDecisionDepuis, "decision.moisDecisionDepuis");
        Checkers.checkDateMonthYear(moisDecisionDepuis, "decision.moisDecisionDepuis", true);
        this.moisDecisionDepuis = moisDecisionDepuis;
    }

    /**
     * (re-)défini le mois de fin de la période rétroactive de cette décision de rente<br/>
     * Ce mois doit être le même, ou plus éloigné dans le temps que {@link #getDateDebutRetro()}
     * 
     * @param moisFinRetro
     *            une chaîne de caractère représentant un mois (format MM.AAAA)
     * @throws NullPointerException
     *             si le mois passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le mois passé en paramètre n'est pas vide et n'est pas au bon format
     * @throws IllegalArgumentException
     *             si le mois passé en paramètre est moins avancé dans le temps que {@link #moisDebutRetro}
     */
    public final void setMoisFinRetro(final String moisFinRetro) {
        Checkers.checkNotNull(moisFinRetro, "decision.moisFinRetro");
        Checkers.checkDateMonthYear(moisFinRetro, "decision.moisFinRetro", true);
        Checkers.checkPeriodMonthYear(moisDebutRetro, moisFinRetro, "decision.moisDebutRetro", "decision.moisFinRetro");
        this.moisFinRetro = moisFinRetro;
    }

    /**
     * (re-)défini la prestation qui est liée à cette décision
     * 
     * @param prestation
     * @throws NullPointerException
     *             si la prestation passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la prestation passée en paramètre n'est pas initialisée
     */
    public final void setPrestation(final Prestation prestation) {
        Checkers.checkNotNull(prestation, "decision.prestation");
        Checkers.checkHasID(prestation, "decision.prestation");
        this.prestation = prestation;
    }

    /**
     * (re-)défini la remarque qui sera affiché en bas du document imprimé
     * 
     * @param remarque
     * @throws NullPointerException
     *             si la remarque passée en paramètre est null
     */
    public final void setRemarque(final String remarque) {
        Checkers.checkNotNull(remarque, "decision.remarque");
        this.remarque = remarque;
    }

    /**
     * (re-)défini les prestations accordées liées à cette décision
     * 
     * @param rentesAccordes
     * @throws NullPointerException
     *             si la liste passée en paramètre est null
     */
    public final void setRentesAccordees(final Set<RenteAccordee> rentesAccordes) {
        Checkers.checkNotNull(rentesAccordes, "decision.rentesAccordes");
        rentesAccordees = rentesAccordes;
    }

    /**
     * (re-)défini le tiers à qui sera envoyé la décision imprimée
     * 
     * @param tiersCorrespondance
     *            un objet de domaine représentant un tiers avec un adresse de courrier
     * @throws NullPointerException
     *             si le tiers passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le tiers passé en paramètre n'est pas initialisé
     */
    public final void setTiersCorrespondance(final Tiers tiersCorrespondance) {
        Checkers.checkNotNull(tiersCorrespondance, "decision.tiersCorrespondance");
        Checkers.checkHasID(tiersCorrespondance, "decision.tiersCorrespondance");
        this.tiersCorrespondance = tiersCorrespondance;
    }

    /**
     * (re-)défini le type de cette décision (communication, décision ou décision sur opposition)
     * 
     * @param typeDecision
     *            le nouveau type de cette décision
     * @throws NullPointerException
     *             si le type de décision passé en paramètre est null
     */
    public final void setTypeDecision(final TypeDecisionRente typeDecision) {
        Checkers.checkNotNull(typeDecision, "decision.type");
        this.typeDecision = typeDecision;
    }

    /**
     * (re-)défini le type de traitement (standard, courant ou rétroactif) cette décision
     * 
     * @param typeTraitement
     *            un énuméré représentant le type de traitement de cette décision
     * @throws NullPointerException
     *             si le type de traitement passé en paramètre est null
     */
    public final void setTypeTraitement(final TypeTraitementDecisionRente typeTraitement) {
        Checkers.checkNotNull(typeTraitement, "decision.typeTraitement");
        this.typeTraitement = typeTraitement;
    }
}
