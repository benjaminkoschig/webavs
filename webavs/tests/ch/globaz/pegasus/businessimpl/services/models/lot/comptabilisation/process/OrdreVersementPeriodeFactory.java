package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

public class OrdreVersementPeriodeFactory {

    public static OrdreVersementPeriode generateOvPeriodeConjoint(String noGroupe, String montantBeneFiciaire,
            String montantRestiution) {
        OrdreVersementPeriode ovPeriode = new OrdreVersementPeriode();
        ovPeriode.setBeneficiaire(OrdreVersementFactory.generateBeneficiaireConjoint(montantBeneFiciaire, "2"));
        ovPeriode.setRestitution(OrdreVersementFactory.generateRestitutionConjoint(montantRestiution, "2"));
        ovPeriode.setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);

        return ovPeriode;
    }

    public static OrdreVersementPeriode generateOvPeriodeRequerant(String noGroupe, String montantBeneFiciaire,
            String montantRestiution) {
        OrdreVersementPeriode ovPeriode = new OrdreVersementPeriode();
        ovPeriode.setBeneficiaire(OrdreVersementFactory.generateBeneficiaire(montantBeneFiciaire, "1"));
        ovPeriode.setRestitution(OrdreVersementFactory.generateRestitution(montantRestiution, "1"));
        ovPeriode.setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);

        return ovPeriode;
    }

    public static OrdreVersementPeriode generateOvPeriodeRequerantWithOutBeneficiaire(String noGroupe) {
        OrdreVersementPeriode ovPeriode = new OrdreVersementPeriode();
        ovPeriode.setRestitution(OrdreVersementFactory.generateRestitution("10", "1"));
        ovPeriode.setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        return ovPeriode;
    }

    public static OrdreVersementPeriode generateOvPeriodeRequerantWithOutRestitution(String noGroupe) {
        OrdreVersementPeriode ovPeriode = new OrdreVersementPeriode();
        ovPeriode.setBeneficiaire(OrdreVersementFactory.generateBeneficiaire("10", null));
        ovPeriode.setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        return ovPeriode;
    }

    public static OrdreVersementPeriode generateOvPeriodeRequerantWithOutRestitution(String noGroupe, String montant) {
        OrdreVersementPeriode ovPeriode = new OrdreVersementPeriode();
        ovPeriode.setBeneficiaire(OrdreVersementFactory.generateBeneficiaire(montant, "1"));
        ovPeriode.setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        return ovPeriode;
    }

    public static OrdreVersementPeriode generateOvPeriodeRquerant(String noGroupe) {
        OrdreVersementPeriode ovPeriode = new OrdreVersementPeriode();
        ovPeriode.setBeneficiaire(OrdreVersementFactory.generateBeneficiaire("10", "1"));
        ovPeriode.setRestitution(OrdreVersementFactory.generateRestitution("10", "1"));
        ovPeriode.setIdCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        return ovPeriode;
    }
}
