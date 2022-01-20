package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.globall.db.BSessionUtil;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
class GenerateOvBeneficiaire {
    private GenerateOvComptaAndGroupe generateOvComptaAndGroupe;

    public GenerateOvBeneficiaire(GenerateOvComptaAndGroupe generateOvComptaAndGroupe) {
        this.generateOvComptaAndGroupe = generateOvComptaAndGroupe;
    }

    private void generateForConjoint(final MontantDispo montantDispo, final PrestationOvDecompte decompte) {
        if (montantDispo.hasMontantDispoConjont()) {
            String motifVersement = decompte.formatDecision();
            // decompte.getNssConjoint() + " " + decompte.getNomConjoint() + " "
            // + decompte.getPrenomConjoint() + " PC " + decompte.getDateDebut() + " - " + decompte.getDateFin()
            // + " " + BSessionUtil.getSessionFromThreadContext().getLabel("PEGASUS_COMPTABILISATION_DECISION_DU")
            // + " " + decompte.getDateDecision();

            if (montantDispo.hasMontantDispoDom2RConjoint()) {
                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeRequerant(),
                        decompte.getIdTiersAddressePaiementConjoint(), decompte.getIdDomaineApplicationConjoint(),
                        montantDispo.getDom2RConjoint(), SectionPegasus.DECISION_PC, decompte.getIdTiersConjoint(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                        motifVersement);

            }
            if (montantDispo.hasMontantStandardDisoConjoint()) {
                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeConjoint(),
                        decompte.getIdTiersAddressePaiementConjoint(), decompte.getIdDomaineApplicationConjoint(),
                        montantDispo.getStandarConjoint(), SectionPegasus.DECISION_PC, decompte.getIdTiersConjoint(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                        motifVersement);
            }
        }
    }

    public List<OrdreVersementCompta> generateOvComptaBeneficiare(final MontantDispo montantDispo,
                                                                  final PrestationOvDecompte decompte) {

        if (montantDispo.hasMoney()) {

            generateRequerant(montantDispo, decompte);

            generateForConjoint(montantDispo, decompte);

        }
        return generateOvComptaAndGroupe.getOvsCompta();
    }

    private void generateRequerant(final MontantDispo montantDispo, final PrestationOvDecompte decompte) {

        if (montantDispo.hasMontantDispoRequerant()) {
            String motifVersement = decompte.formatDecision();
            // decompte.getNssRequerant() + " " + decompte.getNomRequerant() + " "
            // + decompte.getPrenomRequerant() + " "
            // + BSessionUtil.getSessionFromThreadContext().getCodeLibelle("64055001") + " "
            // + decompte.getRefPaiement() + " " + decompte.getDateDebut() + " - " + decompte.getDateFin() + " "
            // + BSessionUtil.getSessionFromThreadContext().getLabel("PEGASUS_COMPTABILISATION_DECISION_DU") + " "
            // + decompte.getDateDecision();
            if (montantDispo.hasMontantDispoDom2RRequerant()) {
                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeRequerant(),
                        decompte.getIdTiersAddressePaiementRequerant(), decompte.getIdDomaineApplicationRequerant(),
                        montantDispo.getDom2RRequerant(), SectionPegasus.DECISION_PC, decompte.getIdTiersRequerant(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                        motifVersement);
            }
            if (montantDispo.hasMontantStandardDispoRequerant()) {

                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeRequerant(),
                        decompte.getIdTiersAddressePaiementRequerant(), decompte.getIdDomaineApplicationRequerant(),
                        montantDispo.getStandardRequerant(), SectionPegasus.DECISION_PC,
                        decompte.getIdTiersRequerant(), IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT, motifVersement);

            }
        }
    }
}
