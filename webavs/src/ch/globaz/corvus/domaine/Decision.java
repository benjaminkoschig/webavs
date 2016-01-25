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
 * Objet de domaine repr�sentant une d�cision de rente
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
     * @return l'adresse email � laquelle le document imprim� sera envoy�
     */
    public final String getAdresseEmail() {
        return adresseEmail;
    }

    /**
     * @return le b�n�ficiaire principal de cette d�cision
     */
    public final PersonneAVS getBeneficiairePrincipal() {
        return beneficiairePrincipal;
    }

    /**
     * @return la liste des compensations inter-d�cision li�es � cette d�cision
     */
    public final Set<CompensationInterDecision> getCompensationsInterDecision() {
        return Collections.unmodifiableSet(compensationsInterDecision);
    }

    /**
     * @return la date � laquelle la d�cision a �t� prise (au format JJ.MM.AAAA)
     */
    public final String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return date � laquelle la d�cision a �t� pr�par�e (au format JJ.MM.AAAA)
     */
    public final String getDatePreparation() {
        return datePreparation;
    }

    /**
     * @return date � laquelle la d�cision a �t� valid�e (au format JJ.MM.AAAA)
     */
    public final String getDateValidation() {
        return dateValidation;
    }

    /**
     * @return la demande de rente dont d�coule cette d�cision
     */
    public final DemandeRente getDemande() {
        return demande;
    }

    /**
     * @return l'�tat de cette d�cision
     */
    public final EtatDecisionRente getEtat() {
        return etat;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire ayant pr�par� cette d�cision
     */
    public final String getGestionnairePreparation() {
        return gestionnairePreparation;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire �tant en charge du traitement de cette d�cision (celui ayant rentr�
     *         la demande de rente dont d�coule cette d�cision)
     */
    public final String getGestionnaireTraitement() {
        return gestionnaireTraitement;
    }

    /**
     * @return le nom d'utilisateur du gestionnaire ayant valid� cette d�cision
     */
    public final String getGestionnaireValidation() {
        return gestionnaireValidation;
    }

    /**
     * @return mois de d�but de la p�riode r�troactive de cette d�cision (format MM.AAAA)
     */
    public final String getMoisDebutRetro() {
        return moisDebutRetro;
    }

    /**
     * @return mois auquel la d�cision a �t� prise comme "courant valid�" (date au format MM.AAAA)
     */
    public final String getMoisDecisionDepuis() {
        return moisDecisionDepuis;
    }

    /**
     * @return mois de fin de la p�riode r�troactive de cette d�cision (au format MM.AAAA)
     */
    public final String getMoisFinRetro() {
        return moisFinRetro;
    }

    /**
     * Calcul et retourne le montant total des cr�ances et dettes pour cette d�cision
     * 
     * @return le montant total des dettes et cr�ances
     */
    public final BigDecimal getMontantTotalDettesEtCreances() {
        return getSoldePourTypesOrdreVersement(TypeOrdreVersement.DETTE, TypeOrdreVersement.CREANCIER);
    }

    /**
     * Calcul et retourne le montant total des gains du b�n�ficiaire principal de cette d�cision
     * 
     * @return le montant total des gains
     */
    public final BigDecimal getMontantTotalGainsBeneficiairePrincipal() {
        return getSoldePourTypesOrdreVersement(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL,
                TypeOrdreVersement.INTERET_MORATOIRE);
    }

    /**
     * @return la liste des ordres de versement de la d�cision dans un conteneur invariable
     */
    public final Set<OrdreVersement> getOrdresVersement() {
        return Collections.unmodifiableSet(prestation.getOrdresVersement());
    }

    /**
     * Retourne une liste des ordres de versements correspondants au(x) type(s) pass�(s) en param�tre
     * 
     * @param types
     *            le(s) type(s) d'ordre(s) de versement voulu(s)
     * @return la liste des ordres de versements correspondants au(x) type(s) pass�(s) en param�tre
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
     * @return la prestation li�e � cette d�cision
     */
    public final Prestation getPrestation() {
        return prestation;
    }

    /**
     * @return la remarque qui sera affich� en bas du document imprim�
     */
    public final String getRemarque() {
        return remarque;
    }

    /**
     * @return la rente principale de cette d�cision (celle ayant un code prestation principale). S'il n'y en a pas
     *         (rente pour enfant seulement, API, etc...) retourne la premi�re rente de la liste
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

        // Si pas de rente avec code prestation principal, on prend la 1�re rente de la d�cision
        if (renteAccordeePrincipale == null) {
            renteAccordeePrincipale = rentesAccordees.iterator().next();
        }

        return renteAccordeePrincipale;
    }

    /**
     * @return les prestations accord�es li�es � cette d�cision (dans un conteneur invariable)
     */
    public final Set<RenteAccordee> getRentesAccordees() {
        return Collections.unmodifiableSet(rentesAccordees);
    }

    /**
     * Calcul et retourne le solde de cette d�cision (en fonction des ordres de versement)
     * 
     * @return le solde de la d�cision
     */
    public final BigDecimal getSolde() {
        BigDecimal montantTotalCID = BigDecimal.ZERO;
        // Pour toutes les compensation inter-d�cision
        for (CompensationInterDecision uneCID : compensationsInterDecision) {
            // On r�cup�re l'OV associ� � la CID
            OrdreVersement ovCID = uneCID.getOrdreVersementDecisionCompensee();
            // On recherche tous les OV de type dette li�s � la prestation
            Set<OrdreVersement> ovTypeDetteDeLaPrestation = getOrdresVersementPourType(TypeOrdreVersement.DETTE);
            // Si l'ov de la CID est li� � la prestation on cumul le montant
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
     * Retourne le solde de cette d�cision sans prendre en compte les int�r�ts moratoires. Ceci est utile dans le cas du
     * calcul du solde pour restitution
     * 
     * @return
     */
    public final BigDecimal getSoldeSansInteretsMoratoires() {
        return getSolde().subtract(getSoldePourTypesOrdreVersement(TypeOrdreVersement.INTERET_MORATOIRE));
    }

    /**
     * @return le tiers � qui sera envoy� la d�cision imprim�e
     */
    public final Tiers getTiersCorrespondance() {
        return tiersCorrespondance;
    }

    /**
     * @return le type de d�cision (d�cision, d�cision sur opposition ou communication) cette d�cision de rente
     */
    public final TypeDecisionRente getTypeDecision() {
        return typeDecision;
    }

    /**
     * @return le type de traitement (standard, courant ou r�troactif) cette d�cision de rente
     */
    public final TypeTraitementDecisionRente getTypeTraitement() {
        return typeTraitement;
    }

    /**
     * @return vrai si le texte concernant l'annulation de d�cision doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteAnnulerDecision() {
        return afficherTexteAnnulerDecision;
    }

    /**
     * @return vrai si le texte concernant la bonne fois doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteAvecBonneFoi() {
        return afficherTexteAvecBonneFoi;
    }

    /**
     * @return vrai si le texte concerant l'incarc�ration doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteIncarceration() {
        return afficherTexteIncarceration;
    }

    /**
     * @return vrai si le texte concernant les int�r�ts moratoires doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteInteretMoratoires() {
        return afficherTexteInteretMoratoires;
    }

    /**
     * @return vrai si le texte concernant l'obligation de payer des cotisations doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteObligationPayerCotisation() {
        return afficherTexteObligationPayerCotisation;
    }

    /**
     * @return vrai si le texte concernant le plafonnement du montant doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteReductionPourPlafonnement() {
        return afficherTexteReductionPourPlafonnement;
    }

    /**
     * @return vrai si le texte concernant le remariage de survivant doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteRemariageRenteDeSurvivant() {
        return afficherTexteRemariageRenteDeSurvivant;
    }

    /**
     * @return vrai si le texte concernant la prescription de 5 ans doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande() {
        return afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    /**
     * @return vrai si le texte concernant la majoration du montant minium pour l'invalidit� doit �tre affich� sur la
     *         d�cision
     */
    public final boolean isAfficherTexteRenteAvecMontantMinimumMajoreInvalidite() {
        return afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    }

    /**
     * @return vrai si le texte concernant la limitation d'une rente de veuf doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteRenteDeVeufLimitee() {
        return afficherTexteRenteDeVeufLimitee;
    }

    /**
     * @return vrai si le texte concernant la limitation d'une rente de veuve doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteRenteDeVeuveLimitee() {
        return afficherTexteRenteDeVeuveLimitee;
    }

    /**
     * @return vrai si le texte concernant les rentes pour enfants doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteRentePourEnfant() {
        return afficherTexteRentePourEnfant;
    }

    /**
     * @return vrai si le texte concernant la r�duction pour sur-assurance doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteRenteReduitePourSurassurance() {
        return afficherTexteRenteReduitePourSurassurance;
    }

    /**
     * @return vrai si le texte concernant le manque de bonne foi doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteSansBonneFoi() {
        return afficherTexteSansBonneFoi;
    }

    /**
     * @return vrai si le texte concernant le suppl�ment pour rente de veuve doit �tre affich� sur la d�cision
     */
    public final boolean isAfficherTexteSupplementVeuve() {
        return afficherTexteSupplementVeuve;
    }

    /**
     * @return vrai si cette d�cision est une d�cision retroactive pure, c'est � dire que toutes les prestations
     *         accord�es ont une date de fin
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
     * (re-)d�fini l'adresse email � laquelle le document imprim� sera envoy�
     * 
     * @param adresseEmail
     * @throws NullPointerException
     *             si l'adresse email pass�e en param�tre est null
     */
    public final void setAdresseEmail(final String adresseEmail) {
        Checkers.checkNotNull(adresseEmail, "decision.adresseEmail");
        this.adresseEmail = adresseEmail;
    }

    /**
     * (re-)d�fini si le texte concernant l'annulation de d�cision doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteAnnulerDecision
     */
    public final void setAfficherTexteAnnulerDecision(final boolean afficherTexteAnnulerDecision) {
        this.afficherTexteAnnulerDecision = afficherTexteAnnulerDecision;
    }

    /**
     * (re-)d�fini si le texte concernant la bonne foi doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteAvecBonneFoi
     */
    public final void setAfficherTexteAvecBonneFoi(final boolean afficherTexteAvecBonneFoi) {
        this.afficherTexteAvecBonneFoi = afficherTexteAvecBonneFoi;
    }

    /**
     * (re-)d�fini si le texte concerant l'incarc�ration doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteIncarceration
     */
    public final void setAfficherTexteIncarceration(final boolean afficherTexteIncarceration) {
        this.afficherTexteIncarceration = afficherTexteIncarceration;
    }

    /**
     * (re-)d�fini si le texte concernant les int�r�ts moratoires doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteInteretMoratoires
     */
    public final void setAfficherTexteInteretMoratoires(final boolean afficherTexteInteretMoratoires) {
        this.afficherTexteInteretMoratoires = afficherTexteInteretMoratoires;
    }

    /**
     * (re-)d�fini si le texte concernant l'obligation de payer des cotisations doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteObligationPayerCotisation
     */
    public final void setAfficherTexteObligationPayerCotisation(final boolean afficherTexteObligationPayerCotisation) {
        this.afficherTexteObligationPayerCotisation = afficherTexteObligationPayerCotisation;
    }

    /**
     * (re-)d�fini si le texte concernant le plafonnement du montant doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteReductionPourPlafonnement
     */
    public final void setAfficherTexteReductionPourPlafonnement(final boolean afficherTexteReductionPourPlafonnement) {
        this.afficherTexteReductionPourPlafonnement = afficherTexteReductionPourPlafonnement;
    }

    /**
     * (re-)d�fini si le texte concernant le remariage de survivant doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteRemariageRenteDeSurvivant
     */
    public final void setAfficherTexteRemariageRenteDeSurvivant(final boolean afficherTexteRemariageRenteDeSurvivant) {
        this.afficherTexteRemariageRenteDeSurvivant = afficherTexteRemariageRenteDeSurvivant;
    }

    /**
     * (re-)d�fini si le texte concernant la prescription de 5 ans doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande
     */
    public final void setAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande(
            final boolean afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande) {
        this.afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande = afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    /**
     * (re-)d�fini si le texte concernant la majoration du montant minimum pour l'invalidit� doit �tre affich� sur la
     * d�cision
     * 
     * @param afficherTexteRenteAvecMontantMinimumMajoreInvalidite
     */
    public final void setAfficherTexteRenteAvecMontantMinimumMajoreInvalidite(
            final boolean afficherTexteRenteAvecMontantMinimumMajoreInvalidite) {
        this.afficherTexteRenteAvecMontantMinimumMajoreInvalidite = afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    }

    /**
     * (re-)d�fini si le texte concernant la limitation d'une rente de veuf doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteRenteDeVeufLimitee
     */
    public final void setAfficherTexteRenteDeVeufLimitee(final boolean afficherTexteRenteDeVeufLimitee) {
        this.afficherTexteRenteDeVeufLimitee = afficherTexteRenteDeVeufLimitee;
    }

    /**
     * (re-)d�fini si le texte concernant la limitation d'une rente de veuve doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteRenteDeVeuveLimitee
     */
    public final void setAfficherTexteRenteDeVeuveLimitee(final boolean afficherTexteRenteDeVeuveLimitee) {
        this.afficherTexteRenteDeVeuveLimitee = afficherTexteRenteDeVeuveLimitee;
    }

    /**
     * (re-)d�fini si le texte concernant les rentes pour enfants doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteRentePourEnfant
     */
    public final void setAfficherTexteRentePourEnfant(final boolean afficherTexteRentePourEnfant) {
        this.afficherTexteRentePourEnfant = afficherTexteRentePourEnfant;
    }

    /**
     * (re-)d�fini si le texte concernant la r�duction pour sur-assurance doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteRenteReduitePourSurassurance
     */
    public final void setAfficherTexteRenteReduitePourSurassurance(
            final boolean afficherTexteRenteReduitePourSurassurance) {
        this.afficherTexteRenteReduitePourSurassurance = afficherTexteRenteReduitePourSurassurance;
    }

    /**
     * (re-)d�fini si le texte concernant le manque de bonne foi doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteSansBonneFoi
     */
    public final void setAfficherTexteSansBonneFoi(final boolean afficherTexteSansBonneFoi) {
        this.afficherTexteSansBonneFoi = afficherTexteSansBonneFoi;
    }

    /**
     * (re-)d�fini si le texte concernant le suppl�ment pour rente de veuve doit �tre affich� sur la d�cision
     * 
     * @param afficherTexteSupplementVeuve
     */
    public final void setAfficherTexteSupplementVeuve(final boolean afficherTexteSupplementVeuve) {
        this.afficherTexteSupplementVeuve = afficherTexteSupplementVeuve;
    }

    /**
     * (re-)d�fini le b�n�ficiaire principale de cette d�cision
     * 
     * @param beneficiairePrincipal
     *            un objet de domaine repr�sentant une personne physique ayant un NSS
     * @throws NullPointerException
     *             si le b�n�ficiaire pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le b�n�ficiaire pass� en param�tre n'est pas initialis�
     */
    public final void setBeneficiairePrincipal(final PersonneAVS beneficiairePrincipal) {
        Checkers.checkNotNull(beneficiairePrincipal, "decision.beneficiairePrincipal");
        Checkers.checkHasID(beneficiairePrincipal, "decision.beneficiairePrincipal");
        this.beneficiairePrincipal = beneficiairePrincipal;
    }

    /**
     * (re-)d�fini la liste des compensations inter-d�cision li�es � cette d�cision
     * 
     * @param compensationsInterDecision
     *            la liste des CID li�es � cette d�cision
     * @throws NullPointerException
     *             si la compensation inter-d�cision pass�e en param�tre est null
     */
    public final void setCompensationsInterDecision(final Set<CompensationInterDecision> compensationsInterDecision) {
        Checkers.checkNotNull(compensationsInterDecision, "decision.compensationsInterDecision");
        this.compensationsInterDecision = compensationsInterDecision;
    }

    /**
     * (re-)d�fini la date � laquelle la d�cision a �t� prise
     * 
     * @param dateDecision
     *            une cha�ne de caract�re repr�sentant une date (format JJ.MM.AAAA)
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'a pas le bon format ou est vide
     */
    public final void setDateDecision(final String dateDecision) {
        Checkers.checkNotNull(dateDecision, "decision.dateDecision");
        Checkers.checkFullDate(dateDecision, "decision.dateDecision", true);
        this.dateDecision = dateDecision;
    }

    /**
     * (re-)d�fini la date � laquelle la d�cision a �t� pr�par�e
     * 
     * @param datePreparation
     *            une cha�ne de caract�re repr�sentant une date (format JJ.MM.AAAA)
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format ou est vide
     */
    public final void setDatePreparation(final String datePreparation) {
        Checkers.checkNotNull(datePreparation, "decision.datePreparation");
        Checkers.checkFullDate(datePreparation, "decision.datePreparation", true);
        this.datePreparation = datePreparation;
    }

    /**
     * (re-)d�fini la date � laquelle la d�cision a �t� valid�e
     * 
     * @param dateValidation
     *            une cha�ne de caract�re repr�sentant une date (format JJ.MM.AAAA)
     * @throws NullPointerException
     *             si la date pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la date pass�e en param�tre n'est pas au bon format ou est vide
     */
    public final void setDateValidation(final String dateValidation) {
        Checkers.checkNotNull(dateValidation, "decision.dateValidation");
        Checkers.checkFullDate(dateValidation, "decision.dateValidation", true);
        this.dateValidation = dateValidation;
    }

    /**
     * (re-)d�fini la demande dont d�coule cette d�cision de rente
     * 
     * @param demande
     *            un objet de domaine repr�sentant une demande de rente
     * @throws NullPointerException
     *             si la demande pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la demande pass�e en param�tre n'est pas initialis�e
     */
    public final void setDemande(final DemandeRente demande) {
        Checkers.checkNotNull(demande, "decision.demande");
        Checkers.checkHasID(demande, "decision.demande");
        this.demande = demande;
    }

    /**
     * (re-)d�fini l'�tat de cette d�cision de rente
     * 
     * @param etat
     *            un �num�r� repr�sentant l'�tat de cette d�cision de rente
     * @throws NullPointerException
     *             si l'�tat pass� en param�tre est null
     */
    public final void setEtat(final EtatDecisionRente etat) {
        Checkers.checkNotNull(etat, "decision.etat");
        this.etat = etat;
    }

    /**
     * (re-)d�fini le gestionnaire ayant pr�par� cette d�cision
     * 
     * @param gestionnairePreparation
     *            le nom d'utilisateur du gestionnaire ayant pr�par� cette d�cision
     * @throws NullPointerException
     *             si la cha�ne de caract�res pass�e en param�tre est null
     */
    public final void setGestionnairePreparation(final String gestionnairePreparation) {
        Checkers.checkNotNull(gestionnairePreparation, "decision.gestionnairePreparation");
        this.gestionnairePreparation = gestionnairePreparation;
    }

    /**
     * (re-)d�fini le gestionnaire �tant en charge du traitement de cette d�cision. Il s'agit du gestionnaire ayant cr��
     * la demande de rente dont d�coule cette d�cision.
     * 
     * @param gestionnaireTraitement
     *            le nom d'utilisateur du gestionnaire �tant en charge du traitement de cette d�cision
     * @throws NullPointerException
     *             si la cha�ne de caract�res pass�e en param�tre est null
     */
    public final void setGestionnaireTraitement(final String gestionnaireTraitement) {
        Checkers.checkNotNull(gestionnaireTraitement, "decision.gestionnaireTraitement");
        this.gestionnaireTraitement = gestionnaireTraitement;
    }

    /**
     * (re-)d�fini le gestionnaire ayant valid� cette d�cision
     * 
     * @param gestionnaireValidation
     *            le nom d'utilisateur du gestionnaire ayant valid� cette d�cision
     * @throws NullPointerException
     *             si la cha�ne de caract�res pass�e en param�tre est null
     */
    public final void setGestionnaireValidation(final String gestionnaireValidation) {
        Checkers.checkNotNull(gestionnaireValidation, "decision.gestionnaireValidation");
        this.gestionnaireValidation = gestionnaireValidation;
    }

    /**
     * (re-)d�fini le mois de d�but de la p�riode r�troactive de cette d�cision de rente
     * 
     * @param moisDebutRetro
     *            une cha�ne de caract�re repr�sentant un mois (format MM.AAAA)
     * @throws NullPointerException
     *             si le mois pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le mois pass� en param�tre n'est pas vide et n'est pas au bon format
     * @throws IllegalArgumentException
     *             si le mois pass� en param�tre est plus avanc� dans le temps que {@link #moisFinRetro}
     */
    public final void setMoisDebutRetro(final String moisDebutRetro) {
        Checkers.checkNotNull(moisDebutRetro, "decision.moisDebutRetro");
        Checkers.checkDateMonthYear(moisDebutRetro, "decision.moisDebutRetro", true);
        Checkers.checkPeriodMonthYear(moisDebutRetro, moisFinRetro, "decision.moisDebutRetro", "decision.moisFinRetro");
        this.moisDebutRetro = moisDebutRetro;
    }

    /**
     * (re-)d�fini le mois � partir duquel la d�cision a �t� dans l'�tat "courant valid�"
     * 
     * @param moisDecisionDepuis
     *            une cha�ne de caract�re repr�sentant un mois (format MM.AAAA)
     * @throws NullPointerException
     *             si le mois pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le mois pass� en param�tre n'est pas vide et n'est pas au bon format
     */
    public final void setMoisDecisionDepuis(final String moisDecisionDepuis) {
        Checkers.checkNotNull(moisDecisionDepuis, "decision.moisDecisionDepuis");
        Checkers.checkDateMonthYear(moisDecisionDepuis, "decision.moisDecisionDepuis", true);
        this.moisDecisionDepuis = moisDecisionDepuis;
    }

    /**
     * (re-)d�fini le mois de fin de la p�riode r�troactive de cette d�cision de rente<br/>
     * Ce mois doit �tre le m�me, ou plus �loign� dans le temps que {@link #getDateDebutRetro()}
     * 
     * @param moisFinRetro
     *            une cha�ne de caract�re repr�sentant un mois (format MM.AAAA)
     * @throws NullPointerException
     *             si le mois pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le mois pass� en param�tre n'est pas vide et n'est pas au bon format
     * @throws IllegalArgumentException
     *             si le mois pass� en param�tre est moins avanc� dans le temps que {@link #moisDebutRetro}
     */
    public final void setMoisFinRetro(final String moisFinRetro) {
        Checkers.checkNotNull(moisFinRetro, "decision.moisFinRetro");
        Checkers.checkDateMonthYear(moisFinRetro, "decision.moisFinRetro", true);
        Checkers.checkPeriodMonthYear(moisDebutRetro, moisFinRetro, "decision.moisDebutRetro", "decision.moisFinRetro");
        this.moisFinRetro = moisFinRetro;
    }

    /**
     * (re-)d�fini la prestation qui est li�e � cette d�cision
     * 
     * @param prestation
     * @throws NullPointerException
     *             si la prestation pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la prestation pass�e en param�tre n'est pas initialis�e
     */
    public final void setPrestation(final Prestation prestation) {
        Checkers.checkNotNull(prestation, "decision.prestation");
        Checkers.checkHasID(prestation, "decision.prestation");
        this.prestation = prestation;
    }

    /**
     * (re-)d�fini la remarque qui sera affich� en bas du document imprim�
     * 
     * @param remarque
     * @throws NullPointerException
     *             si la remarque pass�e en param�tre est null
     */
    public final void setRemarque(final String remarque) {
        Checkers.checkNotNull(remarque, "decision.remarque");
        this.remarque = remarque;
    }

    /**
     * (re-)d�fini les prestations accord�es li�es � cette d�cision
     * 
     * @param rentesAccordes
     * @throws NullPointerException
     *             si la liste pass�e en param�tre est null
     */
    public final void setRentesAccordees(final Set<RenteAccordee> rentesAccordes) {
        Checkers.checkNotNull(rentesAccordes, "decision.rentesAccordes");
        rentesAccordees = rentesAccordes;
    }

    /**
     * (re-)d�fini le tiers � qui sera envoy� la d�cision imprim�e
     * 
     * @param tiersCorrespondance
     *            un objet de domaine repr�sentant un tiers avec un adresse de courrier
     * @throws NullPointerException
     *             si le tiers pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le tiers pass� en param�tre n'est pas initialis�
     */
    public final void setTiersCorrespondance(final Tiers tiersCorrespondance) {
        Checkers.checkNotNull(tiersCorrespondance, "decision.tiersCorrespondance");
        Checkers.checkHasID(tiersCorrespondance, "decision.tiersCorrespondance");
        this.tiersCorrespondance = tiersCorrespondance;
    }

    /**
     * (re-)d�fini le type de cette d�cision (communication, d�cision ou d�cision sur opposition)
     * 
     * @param typeDecision
     *            le nouveau type de cette d�cision
     * @throws NullPointerException
     *             si le type de d�cision pass� en param�tre est null
     */
    public final void setTypeDecision(final TypeDecisionRente typeDecision) {
        Checkers.checkNotNull(typeDecision, "decision.type");
        this.typeDecision = typeDecision;
    }

    /**
     * (re-)d�fini le type de traitement (standard, courant ou r�troactif) cette d�cision
     * 
     * @param typeTraitement
     *            un �num�r� repr�sentant le type de traitement de cette d�cision
     * @throws NullPointerException
     *             si le type de traitement pass� en param�tre est null
     */
    public final void setTypeTraitement(final TypeTraitementDecisionRente typeTraitement) {
        Checkers.checkNotNull(typeTraitement, "decision.typeTraitement");
        this.typeTraitement = typeTraitement;
    }
}
