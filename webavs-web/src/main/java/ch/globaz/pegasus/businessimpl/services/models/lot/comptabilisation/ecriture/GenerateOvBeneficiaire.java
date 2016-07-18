package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;

class GenerateOvBeneficiaire {
    private GenerateOvComptaAndGroupe generateOvComptaAndGroupe;

    public GenerateOvBeneficiaire(GenerateOvComptaAndGroupe generateOvComptaAndGroupe) {
        this.generateOvComptaAndGroupe = generateOvComptaAndGroupe;
    }

    private void generateForConjoint(final MontantDispo montantDispo, final PrestationOvDecompte decompte) {
        if (montantDispo.hasMontantDispoConjont()) {
            String refPaiement = decompte.formatDecision();
            // decompte.getNssConjoint() + " " + decompte.getNomConjoint() + " "
            // + decompte.getPrenomConjoint() + " PC " + decompte.getDateDebut() + " - " + decompte.getDateFin()
            // + " " + BSessionUtil.getSessionFromThreadContext().getLabel("PEGASUS_COMPTABILISATION_DECISION_DU")
            // + " " + decompte.getDateDecision();
            if (montantDispo.hasMontantDispoDom2RConjoint()) {
                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeRequerant(),
                        decompte.getIdTiersAddressePaiementConjoint(), decompte.getIdDomaineApplicationConjoint(),
                        montantDispo.getDom2RConjoint(), SectionPegasus.DECISION_PC, decompte.getIdTiersConjoint(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                        refPaiement);

            }
            if (montantDispo.hasMontantStandardDisoConjoint()) {
                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeConjoint(),
                        decompte.getIdTiersAddressePaiementConjoint(), decompte.getIdDomaineApplicationConjoint(),
                        montantDispo.getStandarConjoint(), SectionPegasus.DECISION_PC, decompte.getIdTiersConjoint(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                        refPaiement);
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
            String refPaiement = decompte.formatDecision();
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
                        refPaiement);
            }
            if (montantDispo.hasMontantStandardDispoRequerant()) {

                generateOvComptaAndGroupe.addOvCompta(decompte.getCompteAnnexeRequerant(),
                        decompte.getIdTiersAddressePaiementRequerant(), decompte.getIdDomaineApplicationRequerant(),
                        montantDispo.getStandardRequerant(), SectionPegasus.DECISION_PC,
                        decompte.getIdTiersRequerant(), IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT, refPaiement);

            }
        }
    }
}
