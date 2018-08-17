package ch.globaz.pegasus.rpc.domaine;

import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;

/**
 * Wrapper des données d'une decision dans l'annonce
 *
 * @author cel
 *
 */
public class RpcDecisionAnnonceComplete {
    private final Demande demande;
    private final PcaDecision pcaDecision;
    private final RpcCalcul rpcCalcul;
    private final VersionDroit versionDroit;
    private final MembresFamilleWithDonneesFinanciere membresFamilleWithDonneesFinanciere;
    private final PcaDecision pcaDecisionPartner;
    private final PersonsElementsCalcul personsElementsCalcul;

    public RpcDecisionAnnonceComplete(PcaDecision pcaDecision, RpcCalcul rpcCalcul, VersionDroit versionDroit,
            MembresFamilleWithDonneesFinanciere membresFamilleWithDonneesFinanciere,
            PersonsElementsCalcul personsElementsCalcul, PcaDecision pcaDecisionPartner, Demande demande) {
        this.pcaDecision = pcaDecision;
        this.rpcCalcul = rpcCalcul;
        this.versionDroit = versionDroit;
        this.membresFamilleWithDonneesFinanciere = membresFamilleWithDonneesFinanciere;
        this.pcaDecisionPartner = pcaDecisionPartner;
        this.personsElementsCalcul = personsElementsCalcul;
        this.demande = demande;
    }

    public PcaDecision getPcaDecision() {
        return pcaDecision;
    }

    public RpcCalcul getRpcCalcul() {
        return rpcCalcul;
    }

    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    public MembresFamilleWithDonneesFinanciere getMembresFamilleWithDonneesFinanciere() {
        return membresFamilleWithDonneesFinanciere;
    }

    public List<MembreFamilleWithDonneesFinanciere> getListMembreFamilleWithDonneesFinanciere() {
        return membresFamilleWithDonneesFinanciere.getMembresFamilleWithDonneesFinanciere();
    }

    public PersonsElementsCalcul getPersonsElementsCalcul() {
        return personsElementsCalcul;
    }

    public boolean isPcaOctroiPartiel() {
        return pcaDecision.getPca().getEtatCalcul().isOctroiePartiel();
    }

    public boolean hasLoyers() {
        return !rpcCalcul.getLoyers().isZero();

    }

    public boolean hasImmobilier() {
        return !rpcCalcul.getImmobilier().isZero();
    }

    public boolean isProrietaire() {
        return !personsElementsCalcul.sumValeurLocativeProprietaire().isZero();
    }

    public RpcVitalNeedsCategory resolveVitalNeedsCategory(PersonElementsCalcul personData, Demande demande) {
        return resolveVitalNeedsCategory(personData, pcaDecision, pcaDecisionPartner, personsElementsCalcul, demande);
    }

    private RpcVitalNeedsCategory resolveVitalNeedsCategory(PersonElementsCalcul personData, PcaDecision pcaDecision,
            PcaDecision pcaDecisionPartner, PersonsElementsCalcul personsElementsCalcul, Demande demande) {

        if (personData.getMembreFamille().getRoleMembreFamille().isEnfant() || demande.getIsFratrie()) {
            if (isHome(personData, pcaDecision)) {
                return RpcVitalNeedsCategory.NO_NEEDS;
            } else {
                return RpcVitalNeedsCategory.CHILD;
            }
        } else {
            if (isHome(personData, pcaDecision)) {
                return RpcVitalNeedsCategory.NO_NEEDS;
            } else if (personsElementsCalcul.hasConjoint() && pcaDecisionPartner == null) {
                return RpcVitalNeedsCategory.COUPLE;
            } else {
                return RpcVitalNeedsCategory.ALONE;
            }
        }
    }
    
    private boolean isHome(PersonElementsCalcul personData, PcaDecision pcaDecision) {
        return personData.getMembreFamille().getPersonne().getId().equals(pcaDecision.getPca().getBeneficiaire().getId())
                && pcaDecision.getPca().getGenre().isHome();
    }

    public boolean hasDateFin() {
        return getPcaDecision().getDecision().getDateFin() != null;
    }

    public boolean isNotRefus() {
        return !(getPcaDecision().getPca() == null) && !getPcaDecision().getPca().getEtatCalcul().isRefus();
    }

    public boolean hasPartner() {
        return pcaDecisionPartner != null;
    }

    public String getDecisionIdPartner() {
        return pcaDecisionPartner.getDecision().getId();
    }

    public boolean isRefusRaisonEco() {
        return getPcaDecision().getDecision().getType().isRefusApresCalcul();
    }

    public PcaGenre resolvePcaGenre(PersonElementsCalcul personData) {
        return resolvePcaGenre(personData, pcaDecision, pcaDecisionPartner);
    }

    public static PcaGenre resolvePcaGenre(PersonElementsCalcul personData, PcaDecision pcaDecision,
            PcaDecision pcaDecisionPartner) {
        if (pcaDecision.getPca().getRoleBeneficiaire().equals(personData.getMembreFamille().getRoleMembreFamille())) {
            return pcaDecision.getPca().getGenre();
        } else if ((pcaDecisionPartner != null) && pcaDecisionPartner.getPca().getRoleBeneficiaire()
                .equals(personData.getMembreFamille().getRoleMembreFamille())) {
            return pcaDecisionPartner.getPca().getGenre();
        } else {
            // cas des enfants
            return PcaGenre.DOMICILE;
        }
    }

    public Montant resolveLoyerTotalBrut() {
        if (hasPartner()) {
            return getMembresFamilleWithDonneesFinanciere()
                    .filtreByRoleMembreFamille(pcaDecision.getPca().getRoleBeneficiaire()).sumLoyerTotalBrut();
        } else {
            return getMembresFamilleWithDonneesFinanciere().sumLoyerTotalBrut();
        }
    }

    public boolean hasInteretsHypotecaireSansPlafond() {
        return !rpcCalcul.getDepensesFraisImmoInteretHypothequaire().isZero();
    }

    public boolean hasInteretsHypotecaire() {
        return !rpcCalcul.getInteretsHypothequairesFraisMaintenance().isZero();
    }

    public Demande getDemande() {
        return demande;
    }

    public boolean isConjointProprietaire() {
        return !rpcCalcul.getValeurImmeubleHabitation().isZero();
    }

    public boolean hasDettesHypotecaire() {
        return !rpcCalcul.getDettesHypothequaires().isZero();
    }
}
