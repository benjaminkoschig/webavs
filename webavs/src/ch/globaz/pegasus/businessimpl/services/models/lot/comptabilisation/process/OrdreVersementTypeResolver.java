package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

public class OrdreVersementTypeResolver {
    public static boolean isAllocationNoel(SimpleOrdreVersement ov) {
        return IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL.equals(ov.getCsType());
    }

    public static boolean isBeneficiaireOrRestitution(SimpleOrdreVersement ov) {
        return OrdreVersementTypeResolver.isBeneficiarePrincipal(ov) || OrdreVersementTypeResolver.isRestitution(ov);
    }

    public static boolean isBeneficiarePrincipal(SimpleOrdreVersement ov) {
        return IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ov.getCsType());
    }

    public static boolean isCreancier(SimpleOrdreVersement ov) {

        if (IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE.equals(ov.getCsType())) {
            return true;
        } else if (IREOrdresVersements.CS_TYPE_TIERS.equals(ov.getCsType())) {
            return true;
        } else if (IREOrdresVersements.CS_TYPE_IMPOT_SOURCE.equals(ov.getCsType())) {
            return true;
        }
        return false;
    }

    public static boolean isDette(SimpleOrdreVersement ov) {
        return IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType());
    }

    public static boolean isJoursAppoint(SimpleOrdreVersement ov) {
        return IREOrdresVersements.CS_TYPE_JOURS_APPOINT.equals(ov.getCsType());
    }

    public static boolean isRestitution(SimpleOrdreVersement ov) {
        return IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(ov.getCsType());
    }
}
