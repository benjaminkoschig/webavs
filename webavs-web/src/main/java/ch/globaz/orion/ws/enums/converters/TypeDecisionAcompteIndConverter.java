package ch.globaz.orion.ws.enums.converters;

import ch.globaz.orion.ws.enums.TypeDecisionAcompteInd;

public class TypeDecisionAcompteIndConverter {
    private TypeDecisionAcompteIndConverter() {
        throw new UnsupportedOperationException();
    }

    public static TypeDecisionAcompteInd convertTypeDecisionAcompteInd(Integer csTypeDecision) {
        TypeDecisionAcompteInd typeDecisionAcompteInd;
        switch (csTypeDecision) {
            case 605001:
            case 605003:
                typeDecisionAcompteInd = TypeDecisionAcompteInd.PROVISOIRE;
                break;
            case 605002:
            case 605004:
                typeDecisionAcompteInd = TypeDecisionAcompteInd.DEFINITIF;
                break;
            case 605007:
                typeDecisionAcompteInd = TypeDecisionAcompteInd.ACCOMPTE;
                break;
            default:
                typeDecisionAcompteInd = TypeDecisionAcompteInd.PROVISOIRE;
                break;

        }
        return typeDecisionAcompteInd;
    }

    public static ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd convertTypeDecisionAcompteIndToTypeEbusiness(
            Integer csTypeDecision) {
        ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd typeDecisionAcompteInd;
        switch (csTypeDecision) {
            case 605001:
            case 605003:
                typeDecisionAcompteInd = ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd.PROVISOIRE;
                break;
            case 605002:
            case 605004:
                typeDecisionAcompteInd = ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd.DEFINITIF;
                break;
            case 605007:
                typeDecisionAcompteInd = ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd.ACCOMPTE;
                break;
            default:
                typeDecisionAcompteInd = ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd.PROVISOIRE;
                break;

        }
        return typeDecisionAcompteInd;
    }

    public static String convertTypeDecisionAcompteIndEbusinessToCs(ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd type) {
        String cs;
        switch (type) {
            case ACCOMPTE:
                cs = "605007";
                break;
            case DEFINITIF:
                cs = "605002";
                break;
            case PROVISOIRE:
                cs = "605001";
                break;
            default:
                throw new IllegalStateException("unable to convert type, this type is not allowed " + type);
        }
        return cs;
    }
}
