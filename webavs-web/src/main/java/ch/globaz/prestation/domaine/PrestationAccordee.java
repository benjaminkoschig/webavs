package ch.globaz.prestation.domaine;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.InteretMoratoire;
import ch.globaz.corvus.domaine.RepartitionCreance;
import ch.globaz.osiris.domaine.CompteAnnexe;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Tiers;

/**
 * <p>
 * Objet de domaine représentant une prestation accordée
 * </p>
 * <p>
 * Implémente {@link Comparable} avec un tri naturel en fonction des périodes de droit,
 * </p>
 */
public class PrestationAccordee extends EntiteDeDomaine implements Comparable<PrestationAccordee> {

    private static final long serialVersionUID = 1L;

    private Tiers adresseDePaiement;
    private PersonneAVS beneficiaire;
    private boolean bloquee;
    private CodePrestation codePrestation;
    private CompteAnnexe compteAnnexe;
    private EnTeteBlocage enTeteBlocage;
    private EtatPrestationAccordee etat;
    private Integer idTiersAdressePaiement;
    private InteretMoratoire interetMoratoire;
    private String moisDebut;
    private String moisFin;
    private BigDecimal montant;
    private String referencePourLePaiement;
    private Set<RepartitionCreance> repartitionsCreance;
    private boolean hasCodeSpecial60 = false;
    private String idTiersNssCompl1;
    private String idTiersNssCompl2;

    public PrestationAccordee() {
        super();

        adresseDePaiement = new Tiers();
        beneficiaire = new PersonneAVS();
        codePrestation = CodePrestation.INCONNU;
        compteAnnexe = new CompteAnnexe();
        bloquee = false;
        enTeteBlocage = new EnTeteBlocage();
        etat = EtatPrestationAccordee.CALCULE;
        idTiersAdressePaiement = 0;
        interetMoratoire = new InteretMoratoire();
        moisDebut = "";
        moisFin = "";
        montant = BigDecimal.ZERO;
        referencePourLePaiement = "";
        repartitionsCreance = new HashSet<RepartitionCreance>();
        idTiersNssCompl1 ="";
        idTiersNssCompl2  ="";
    }

    @Override
    public int compareTo(final PrestationAccordee autrePrestation) {
        Periode periodeDeCettePrestation = new Periode(moisDebut, moisFin);
        Periode periodeAutrePrestation = new Periode(autrePrestation.moisDebut, autrePrestation.moisFin);

        return periodeDeCettePrestation.compareTo(periodeAutrePrestation);
    }

    public final boolean comporteUneRepartitionDeCreance() {
        return repartitionsCreance.size() > 0;
    }

    public final boolean comporteUnInteretMoratoire() {
        return interetMoratoire.getId().intValue() > 0;
    }

    /**
     * @return vrai si un en-tête de blocage avec un solde différent de zéro est lié à cette rente accordée
     */
    public final boolean comporteUnMontantBloque() {
        return enTeteBlocage.unBlocageEstEnCours();
    }

    public final boolean estBloquee() {
        return bloquee;
    }

    /**
     * @return vrai s'il n'y a pas de date de fin à cette prestation accordée
     */
    public final boolean estEnCours() {
        return JadeStringUtil.isBlankOrZero(moisFin);
    }

    /**
     * Pour la compatibilité avec JSTL/EL
     * 
     * @return vrai s'il n'y a pas de date de fin à cette prestation accordée
     */
    public final boolean isEnCours() {
        return estEnCours();
    }

    public final Tiers getAdresseDePaiement() {
        return adresseDePaiement;
    }

    public final PersonneAVS getBeneficiaire() {
        return beneficiaire;
    }

    public final CodePrestation getCodePrestation() {
        return codePrestation;
    }

    public final CompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    public EnTeteBlocage getEnTeteBlocage() {
        return enTeteBlocage;
    }

    public final EtatPrestationAccordee getEtat() {
        return etat;
    }

    public final Integer getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public final InteretMoratoire getInteretMoratoire() {
        return interetMoratoire;
    }

    public final String getMoisDebut() {
        return moisDebut;
    }

    public final String getMoisFin() {
        return moisFin;
    }

    public final BigDecimal getMontant() {
        return montant;
    }

    public final Periode getPeriodeDuDroit() {
        return new Periode(moisDebut, moisFin);
    }

    public final String getReferencePourLePaiement() {
        return referencePourLePaiement;
    }

    public final Set<RepartitionCreance> getRepartitionCreance() {
        return Collections.unmodifiableSet(repartitionsCreance);
    }

    public boolean isBloquee() {
        return bloquee;
    }

    public final boolean isRenteComplementaire() {
        return codePrestation.isRenteComplementaire();
    }

    public final boolean isRenteComplementairePourConjoint() {
        return codePrestation.isRenteComplementairePourConjoint();
    }

    public final boolean isRenteComplementairePourEnfant() {
        return codePrestation.isRenteComplementairePourEnfant();
    }

    public final boolean isRentePrincipale() {
        return codePrestation.isRentePrincipale();
    }

    public final void setAdresseDePaiement(final Tiers adresseDePaiement) {
        Checkers.checkNotNull(adresseDePaiement, "prestationAccordee.adresseDePaiement");
        this.adresseDePaiement = adresseDePaiement;
    }

    public final void setBeneficiaire(final PersonneAVS beneficiaire) {
        Checkers.checkNotNull(beneficiaire, "prestationAccordee.beneficiaire");
        this.beneficiaire = beneficiaire;
    }

    public void setBloquee(final boolean estBloquee) {
        bloquee = estBloquee;
    }

    public final void setCodePrestation(final CodePrestation codePrestation) {
        Checkers.checkNotNull(codePrestation, "prestationAccordee.codePrestation");
        this.codePrestation = codePrestation;
    }

    public final void setCompteAnnexe(final CompteAnnexe compteAnnexe) {
        Checkers.checkNotNull(compteAnnexe, "prestationAccordee.compteAnnexe");
        this.compteAnnexe = compteAnnexe;
    }

    public void setEnTeteBlocage(final EnTeteBlocage enTeteBlocage) {
        Checkers.checkNotNull(enTeteBlocage, "prestationAccordee.enTeteBlocage");
        this.enTeteBlocage = enTeteBlocage;
    }

    public final void setEtat(final EtatPrestationAccordee etat) {
        Checkers.checkNotNull(etat, "prestationAccordee.etat");
        this.etat = etat;
    }

    public final void setIdTiersAdressePaiement(final Integer idTiersAdressePaiement) {
        Checkers.checkNotNull(idTiersAdressePaiement, "prestationAccordee.idTiersAdressePaiement");
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public final void setInteretMoratoire(final InteretMoratoire interetMoratoire) {
        Checkers.checkNotNull(interetMoratoire, "prestationAccordee.interetMoratoire");
        this.interetMoratoire = interetMoratoire;
    }

    public final void setMoisDebut(final String moisDebut) {
        Checkers.checkNotNull(moisDebut, "prestationAccordee.moisDebut");
        Checkers.checkDateMonthYear(moisDebut, "prestationAccordee.moisDebut", false);
        this.moisDebut = moisDebut;
    }

    public final void setMoisFin(final String moisFin) {
        Checkers.checkNotNull(moisFin, "prestationAccordee.moisFin");
        Checkers.checkDateMonthYear(moisFin, "prestationAccordee.moisFin", true);
        this.moisFin = moisFin;
    }

    public final void setMontant(final BigDecimal montant) {
        Checkers.checkNotNull(montant, "prestationAccordee.montant");
        this.montant = montant;
    }

    public final void setReferencePourLePaiement(final String referencePourLePaiement) {
        Checkers.checkNotNull(referencePourLePaiement, "prestationAccordee.referencePourLePaiement");
        this.referencePourLePaiement = referencePourLePaiement;
    }

    public final void setRepartitionCreance(final Set<RepartitionCreance> repartitionsCreance) {
        Checkers.checkNotNull(repartitionsCreance, "prestationAccordee.repartitionsCreance");
        this.repartitionsCreance = repartitionsCreance;
    }
    public boolean isHasCodeSpecial60() {
        return hasCodeSpecial60;
    }

    public void setHasCodeSpecial60(boolean hasCodeSpecial60) {
        this.hasCodeSpecial60 = hasCodeSpecial60;
    }

    public String getIdTiersNssCompl1() {
        return idTiersNssCompl1;
    }

    public void setIdTiersNssCompl1(String idTiersNssCompl1) {
        this.idTiersNssCompl1 = idTiersNssCompl1;
    }

    public String getIdTiersNssCompl2() {
        return idTiersNssCompl2;
    }

    public void setIdTiersNssCompl2(String idTiersNssCompl2) {
        this.idTiersNssCompl2 = idTiersNssCompl2;
    }
}
