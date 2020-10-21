package ch.globaz.pegasus.rpc.domaine;

import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;
import ch.globaz.pyxis.domaine.EtatCivil;

/**
 * Tout les montants sont annualisés.
 * 
 * @author dma
 * 
 */
public class PersonElementsCalcul {

    private MembreFamille membreFamille;
    private RpcAddress legalAddress;
    private RpcAddress livingAddress;
    private Montant renteAvsAi;
    private Montant renteApi;
    private Montant renteIj;
    private boolean renteIsSansRente;

    private Montant revenuBruteActiviteLucrative;

    private Montant totalRentes;
    private Montant lpp;
    private Montant renteEtrangere;
    private Montant autresRevenus;
    private Montant retraitCapitalLpp;

    private Montant primeLamal;
    private Montant autresDepenses;
    private Montant revenuBrutHypothetique;

    private Montant homeContributionLca;
    private Montant homeTaxeHomeTotal;
    private Montant homeTaxeHomePrisEnCompte;
    private Montant homeParticipationAuxCoutDesPatients;// ???
    private Montant homeDepensesPersonnelles;
    private boolean homeIsApiFacturee;

    private Montant valeurLocativeProprietaire;
    private Montant usufructIncome;

    private String typeRenteCS;

    private Montant fraisGarde;
    private Montant montantRIP;

    public Montant getValeurLocativeProprietaire() {
        return valeurLocativeProprietaire;
    }

    public void setValeurLocativeProprietaire(Montant valeurLocativeProprietaire) {
        this.valeurLocativeProprietaire = valeurLocativeProprietaire;
    }

    public void addValeurLocativeProprietaire(PersonElementsCalcul elementsCalcul) {
        valeurLocativeProprietaire = valeurLocativeProprietaire.add(elementsCalcul.getValeurLocativeProprietaire());
    }

    public Montant getUsufructIncome() {
        return usufructIncome;
    }

    public void setUsufructIncome(Montant usufructIncome) {
        this.usufructIncome = usufructIncome;
    }

    public void addUsufructIncome(PersonElementsCalcul elementsCalcul) {
        usufructIncome = usufructIncome.add(elementsCalcul.getUsufructIncome());
    }

    public boolean getHomeIsApiFacturee() {
        return homeIsApiFacturee;
    }

    public void setHomeIsApiFacturee(boolean homeIsApiFacturee) {
        this.homeIsApiFacturee = homeIsApiFacturee;
    }

    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setRenteAvsAi(Montant renteAvsAi) {
        this.renteAvsAi = renteAvsAi;
    }

    public void addRenteAvsAi(PersonElementsCalcul elementsCalcul) {
        renteAvsAi = renteAvsAi.add(elementsCalcul.getRenteAvsAi());
    }

    public void setRenteApi(Montant renteApi) {
        this.renteApi = renteApi;
    }

    public void addRenteApi(PersonElementsCalcul elementsCalcul) {
        renteApi = renteApi.add(elementsCalcul.getRenteApi());
    }

    public void setRenteIj(Montant renteIj) {
        this.renteIj = renteIj;
    }

    public void addRenteIj(PersonElementsCalcul elementsCalcul) {
        renteIj = renteIj.add(elementsCalcul.getRenteIj());
    }

    public void setRenteIsSansRente(boolean renteIsSansRente) {
        this.renteIsSansRente = renteIsSansRente;
    }

    public void setRevenuBruteActiviteLucrative(Montant revenuBruteActiviteLucrative) {
        this.revenuBruteActiviteLucrative = revenuBruteActiviteLucrative;
    }

    public void addRevenuBruteActiviteLucrative(PersonElementsCalcul elementsCalcul) {
        revenuBruteActiviteLucrative = revenuBruteActiviteLucrative.add(elementsCalcul
                .getRevenuBruteActiviteLucrative());
    }

    public void setTotalRentes(Montant totalRentes) {
        this.totalRentes = totalRentes;
    }

    public void addTotalRentes(PersonElementsCalcul elementsCalcul) {
        totalRentes = totalRentes.add(elementsCalcul.getTotalRentes());
    }

    public void setLpp(Montant lpp) {
        this.lpp = lpp;
    }

    public void addLpp(PersonElementsCalcul elementsCalcul) {
        lpp = lpp.add(elementsCalcul.getLpp());
    }

    public void setRenteEtrangere(Montant renteEtrangere) {
        this.renteEtrangere = renteEtrangere;
    }

    public void addRenteEtrangere(PersonElementsCalcul elementsCalcul) {
        renteEtrangere = renteEtrangere.add(elementsCalcul.getRenteEtrangere());
    }

    public void setAutresRevenus(Montant autresRevenus) {
        this.autresRevenus = autresRevenus;
    }

    public void addAutresRevenus(PersonElementsCalcul elementsCalcul) {
        autresRevenus = autresRevenus.add(elementsCalcul.getAutresRevenus());
    }

    public void setRetraitCapitalLpp(Montant retraitCapitalLpp) {
        this.retraitCapitalLpp = retraitCapitalLpp;
    }

    public void addRetraitCapitalLpp(PersonElementsCalcul elementsCalcul) {
        retraitCapitalLpp = retraitCapitalLpp.add(elementsCalcul.getRetraitCapitalLpp());
    }

    public void setPrimeLamal(Montant primeLamal) {
        this.primeLamal = primeLamal;
    }

    public void addPrimeLamal(PersonElementsCalcul elementsCalcul) {
        primeLamal = primeLamal.add(elementsCalcul.getPrimeLamal());
    }

    public void setAutresDepenses(Montant autresDepenses) {
        this.autresDepenses = autresDepenses;
    }

    public void addAutresDepenses(PersonElementsCalcul elementsCalcul) {
        autresDepenses = autresDepenses.add(elementsCalcul.getAutresDepenses());
    }

    public void setRevenuBrutHypothetique(Montant revenuBrutHypothetique) {
        this.revenuBrutHypothetique = revenuBrutHypothetique;
    }

    public void addRevenuBrutHypothetique(PersonElementsCalcul elementsCalcul) {
        revenuBrutHypothetique = revenuBrutHypothetique.add(elementsCalcul.getRevenuBrutHypothetique());
    }

    public void setFraisGarde(Montant fraisGarde) {
        this.fraisGarde = fraisGarde;
    }

    public void setMontantRIP(Montant montantRIP) {
        this.montantRIP = montantRIP;
    }

    public void addFraisGarde(PersonElementsCalcul elementsCalcul) {
        fraisGarde = fraisGarde.add(elementsCalcul.getFraisGarde());
    }

    public void setHomeTaxeHomeTotal(Montant homeTaxeHomeTotal) {
        this.homeTaxeHomeTotal = homeTaxeHomeTotal;
    }

    public void addHomeTaxeHomeTotal(PersonElementsCalcul elementsCalcul) {
        homeTaxeHomeTotal = homeTaxeHomeTotal.add(elementsCalcul.getHomeTaxeHomeTotal());
    }

    public void setHomeTaxeHomePrisEnCompte(Montant homeTaxeHomePrisEnCompte) {
        this.homeTaxeHomePrisEnCompte = homeTaxeHomePrisEnCompte;
    }

    public void addHomeTaxeHomePrisEnCompte(PersonElementsCalcul elementsCalcul) {
        homeTaxeHomePrisEnCompte = homeTaxeHomePrisEnCompte.add(elementsCalcul.getHomeTaxeHomePrisEnCompte());
    }

    public void setHomeParticipationAuxCoutDesPatients(Montant homeParticipationAuxCoutDesPatients) {
        this.homeParticipationAuxCoutDesPatients = homeParticipationAuxCoutDesPatients;
    }

    public void addHomeParticipationAuxCoutDesPatients(PersonElementsCalcul elementsCalcul) {
        homeParticipationAuxCoutDesPatients = homeParticipationAuxCoutDesPatients.add(elementsCalcul
                .getHomeParticipationAuxCoutDesPatients());
    }

    public void setHomeDepensesPersonnelles(Montant homeDepensesPersonnelles) {
        this.homeDepensesPersonnelles = homeDepensesPersonnelles;
    }

    public void addHomeDepensesPersonnelles(PersonElementsCalcul elementsCalcul) {
        homeDepensesPersonnelles = homeDepensesPersonnelles.add(elementsCalcul.getHomeDepensesPersonnelles());
    }

    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    public Montant getRenteAvsAi() {
        return renteAvsAi;
    }

    public Montant getRenteApi() {
        return renteApi;
    }

    public Montant getRenteIj() {
        return renteIj;
    }

    public boolean isRenteIsSansRente() {
        return renteIsSansRente;
    }

    public Montant getRevenuBruteActiviteLucrative() {
        return revenuBruteActiviteLucrative;
    }

    public Montant getTotalRentes() {
        return totalRentes;
    }

    public Montant getLpp() {
        return lpp;
    }

    public Montant getRenteEtrangere() {
        return renteEtrangere;
    }

    public Montant getAutresRevenus() {
        return autresRevenus;
    }

    public Montant getRetraitCapitalLpp() {
        return retraitCapitalLpp;
    }

    public Montant getPrimeLamal() {
        return primeLamal;
    }

    public Montant getAutresDepenses() {
        return autresDepenses;
    }

    public Montant getRevenuBrutHypothetique() {
        return revenuBrutHypothetique;
    }

    public Montant getHomeTaxeHomeTotal() {
        return homeTaxeHomeTotal;
    }

    public Montant getHomeTaxeHomePrisEnCompte() {
        return homeTaxeHomePrisEnCompte;
    }

    public Montant getHomeParticipationAuxCoutDesPatients() {
        return homeParticipationAuxCoutDesPatients;
    }

    public Montant getHomeDepensesPersonnelles() {
        return homeDepensesPersonnelles;
    }

    public Montant getHomeContributionLca() {
        return homeContributionLca;
    }

    public void setHomeContributionLca(Montant homeContributionLca) {
        this.homeContributionLca = homeContributionLca;
    }

    public void addHomeContributionLca(PersonElementsCalcul elementsCalcul) {
        homeContributionLca = homeContributionLca.add(elementsCalcul.getHomeContributionLca());
    }

    public String getTypeRenteCS() {
        return typeRenteCS;
    }

    public void setTypeRenteCS(String typeRenteCS) {
        this.typeRenteCS = typeRenteCS;
    }

    public RpcAddress getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(RpcAddress legalAddress) {
        this.legalAddress = legalAddress;
    }

    public RpcAddress getLivingAddress() {
        return livingAddress;
    }

    public void setLivingAddress(RpcAddress livingAddress) {
        this.livingAddress = livingAddress;
    }

    public EtatCivil getSituationFamiliale() {
        return getMembreFamille().getPersonne().getEtatCivil();
    }

    public boolean hasLppPension() {
        return !getLpp().isZero();
    }

    public boolean hasForeignPension() {
        return !getRenteEtrangere().isZero();
    }

    public boolean hasResidenceCosts() {
        return !getHomeTaxeHomeTotal().isZero();
    }

    public boolean isMandataire() {
        // TODO attente de réponse metier pour les cas de fraterie/tutelle
        // getMembreFamille().getDonneesPersonnelles().getIsRepresentantLegal()();
        // TODO vérifier également les cas de couple séparé par la maladie
        return getMembreFamille().getRoleMembreFamille().isRequerant();
    }

    public boolean hasPension() {
        return !isRenteIsSansRente();
    }

    public Integer getNumeroOffice() {
        String num = splitNumeroCaisse()[0];
        if (num != null) {
            return Integer.valueOf(num);
        }
        return null;
    }

    private String[] splitNumeroCaisse() {
        if (getMembreFamille() == null) {
            throw new RuntimeException("Membre famille is null");
        }
        if (getMembreFamille().getDonneesPersonnelles() == null) {
            throw new RuntimeException("Donnees personnelles is null");
        }
        String numeroCaisse = getMembreFamille().getDonneesPersonnelles().getNoCaisseAvs();
        if (numeroCaisse != null && !numeroCaisse.isEmpty()) {
            if (numeroCaisse.contains(".")) {
                return StringUtils.split(numeroCaisse, '.');
            }
            String[] t = { numeroCaisse, null };
            return t;
        }
        return new String[2];
    }

    public boolean hasCompensationAgency() {
        return splitNumeroCaisse().length > 1;
    }

    public Integer getNumeroAgence() {
        String num = splitNumeroCaisse()[1];
        if (num != null) {
            return Integer.valueOf(num);
        }
        return null;
    }

    public boolean isValidLegalAddress() {
        if (legalAddress.isEmpty()) {
            throw new RpcBusinessException("pegasus.rpc.mandatory.legal.address", getMembreFamille().getPersonne());
        } else {
            return true;
        }
    }

    public boolean isValidLivingAddress() {
        return !(getLivingAddress().isEmpty() || "ET".equalsIgnoreCase(livingAddress.getCanton().getAbreviation()));
    }

    public boolean hasResidenceContributions() {
        return !getHomeParticipationAuxCoutDesPatients().isZero();
    }

    public Montant getFraisGarde() {
        return fraisGarde;
    }

    public Montant getMontantRIP() {
        return montantRIP;
    }
}
