package globaz.naos.db.avisMutation.avisAddress;

import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.avisMutation.AFAvisMotif;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.divers.TIApplication;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;

public final class AFAvisAddressFactory {
    public static final String TYPE_ADRESSE_EXPLOITATION = "508021";

    public final static AFAvisAddressFactory createInstance(String aDate, AFAvisMotif aReason,
            AFAffiliation anAffiliation, TITiers aTiers) throws Exception {
        AFAvisAddressFactory result = new AFAvisAddressFactory();
        result.date = aDate;
        result.reason = aReason;
        result.affiliation = anAffiliation;
        result.tiers = aTiers;
        result.dsAdresse = result.tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                IConstantes.CS_APPLICATION_DEFAUT, anAffiliation.getAffilieNumero(), result.date, true, null);
        result.dsAdresseExploitation = result.tiers.getAdresseAsDataSource(
                AFAvisAddressFactory.TYPE_ADRESSE_EXPLOITATION, ICommonConstantes.CS_APPLICATION_COTISATION,
                result.affiliation.getAffilieNumero(), result.date, true, null);
        if ((result.dsAdresse == null) && (result.dsAdresseExploitation == null)) {
            // TODO thrower une meilleure exception
            throw new Exception("Aucune adresse trouvée pour le tiers");
        }
        return result;
    }

    private final static boolean sameAddresses(AFAvisAddress a1, AFAvisAddress a2) {
        if ((a1 == null) || (a2 == null)) {
            return false;
        }
        return (a1.getStreet() + a1.getHouseNumber() + a1.getTown() + a1.getSwissZipCode()).equals(a2.getStreet()
                + a2.getHouseNumber() + a2.getTown() + a2.getSwissZipCode());
    }

    private AFAffiliation affiliation;
    private String date;
    private TIAdresseDataSource dsAdresse;
    private TIAdresseDataSource dsAdresseExploitation;

    private AFAvisMotif reason;

    private TITiers tiers;

    private AFAvisAddressFactory() {
    }

    public final AFAvisAddress createPostalAddress() {
        AFAvisAddress result;
        // prendre l'adresse d'exploitation si existante, sinon adresse
        if (dsAdresseExploitation != null) {
            result = new AFDefaultAvisAdress(affiliation, tiers, dsAdresseExploitation);
        } else if (dsAdresse != null) {
            result = new AFDefaultAvisAdress(affiliation, tiers, dsAdresse);
        } else {
            result = null;
        }
        return result;
    }

    public final AFAvisAddress createReturnAddress(AFAffiliation maisonMere) throws Exception {
        AFAvisAddress result = null;
        if ((AFAvisMotif.CREATE_SUCCURSALE == reason) || (AFAvisMotif.UPDATE_SUCCURSALE == reason)
                || (AFAvisMotif.DELETE_SUCCURSALE == reason)) {
            // 13 - création de succursale: on prend l'adresse de la maison mère comme adresse de retour
            if (maisonMere == null) {
                // TODO y'a un blème, faire péter quelque chose!
            } else {
                TIAdresseDataSource dsMaisonMere = maisonMere.getTiers().getAdresseAsDataSource(
                        IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        globaz.webavs.common.ICommonConstantes.CS_APPLICATION_COTISATION,
                        maisonMere.getAffilieNumero(), date, true, null);
                result = new AFDefaultAvisAdress(maisonMere, maisonMere.getTiers(), dsMaisonMere);
            }
        } else if ((AFAvisMotif.CREATE_ASSOCIE == reason) || (AFAvisMotif.UPDATE_ASSOCIE == reason)
                || (AFAvisMotif.DELETE_ASSOCIE == reason)) {
            // 12 - création d'associé
            AFAffiliation employeurAff = AFAffiliationUtil.getEmployeur(affiliation, date);
            // TODO gérer ce bordel d'exceptions là autour
            if (employeurAff == null) {
                throw new Exception("L'employeur de l'associé " + affiliation.getAffilieNumero()
                        + " n'a pas pu être trouvé, veuillez contrôler les liens entre les tiers.");
            }
            TITiers employeur = employeurAff.getTiers();
            TIAdresseDataSource dsAdresseEmployeur = employeur.getAdresseAsDataSource(
                    AFAvisAddressFactory.TYPE_ADRESSE_EXPLOITATION, TIApplication.CS_DEFAUT,
                    employeurAff.getAffilieNumero(), date, true, null);
            result = new AFDefaultAvisAdress(employeurAff, employeur, dsAdresseEmployeur);
        } else {
            if ((dsAdresse != null) && (dsAdresseExploitation != null)) {
                AFDefaultAvisAdress domicileAdresse = new AFDefaultAvisAdress(affiliation, tiers, dsAdresse);
                AFDefaultAvisAdress exploitAdresse = new AFDefaultAvisAdress(affiliation, tiers, dsAdresseExploitation);
                if (!AFAvisAddressFactory.sameAddresses(domicileAdresse, exploitAdresse)) {
                    result = domicileAdresse;
                } else {
                    result = new AFEmptyAvisAdress();
                }
            } else {
                result = new AFEmptyAvisAdress();
            }
        }
        assert result != null;
        return result;
    }

}
