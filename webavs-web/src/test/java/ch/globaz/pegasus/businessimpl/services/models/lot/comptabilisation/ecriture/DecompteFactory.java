package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;

public class DecompteFactory {
    public static PrestationOvDecompte generateDecompteRequerant() {
        PrestationOvDecompte decompte = new PrestationOvDecompte();
        decompte.setCompteAnnexeRequerant(CompteAnnexeFactory.generateRequerant());
        decompte.setIdTiersAddressePaiementRequerant(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT);
        decompte.setIdDomaineApplicationRequerant(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_REQUERANT);
        decompte.setIdTiersRequerant(CompteAnnexeFactory.ID_TIERS_REQUERANT);
        return decompte;
    }

    public static PrestationOvDecompte generateDecompteRequerantConjoint() {
        return generateDecompteRequerantConjoint(null);
    }

    public static PrestationOvDecompte generateDecompteRequerantConjoint(PrestationOvDecompte decompteInit) {
        PrestationOvDecompte decompte;
        if (decompteInit == null) {
            decompte = new PrestationOvDecompte();
        } else {
            decompte = decompteInit;
        }

        decompte.setCompteAnnexeRequerant(CompteAnnexeFactory.generateRequerant());
        decompte.setIdTiersAddressePaiementRequerant(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT);
        decompte.setIdDomaineApplicationRequerant(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_REQUERANT);
        decompte.setIdTiersRequerant(CompteAnnexeFactory.ID_TIERS_REQUERANT);

        decompte.setCompteAnnexeConjoint(CompteAnnexeFactory.generateConjoint());
        decompte.setIdTiersAddressePaiementConjoint(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_CONJOINT);
        decompte.setIdDomaineApplicationConjoint(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_CONJOINT);
        decompte.setIdTiersConjoint(CompteAnnexeFactory.ID_TIERS_CONJOINT);
        return decompte;
    }

}
