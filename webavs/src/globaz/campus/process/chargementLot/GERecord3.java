package globaz.campus.process.chargementLot;

import globaz.campus.application.GEApplication;
import globaz.globall.api.GlobazSystem;

public class GERecord3 extends GEGenericRecord {
    private final GERecordField adresseLegaleField;
    private final GERecordField localiteLegalField;
    private final GERecordField npaLegalField;
    private final GERecordField numeroSequenceField;
    private final GERecordField reserve4;
    private final GERecordField rueLegalField;
    private final GERecordField suffixePostalLegalField;

    /**
     * Constructor for Record501.
     */
    public GERecord3() {
        this("3");
    }

    protected GERecord3(String _codeEnr) {
        super(_codeEnr);
        boolean nouveauFormat = true;
        try {
            nouveauFormat = ((GEApplication) GlobazSystem.getApplication(GEApplication.DEFAULT_APPLICATION_CAMPUS))
                    .isNouveauFormatEPFL();
        } catch (Exception e) {
            nouveauFormat = true;
        }
        if (nouveauFormat) {
            numeroSequenceField = instanceField(1, 5, 0, "Numéro de séquence");
            //
            adresseLegaleField = instanceField(7, 46, 1, "P.A. Légale");
            // /
            rueLegalField = instanceField(47, 86, 2, "Rue et numéro légal");
            // /
            npaLegalField = instanceField(87, 90, 3, "Npa légal");
            // /
            localiteLegalField = instanceField(91, 130, 4, "Localité légal");
            // /
            suffixePostalLegalField = instanceField(131, 132, 5, "Suffixe postal légal");
            // /
            reserve4 = instanceField(133, 145, 6, "Suffixe postal légal");

            GERecordField[] fields = new GERecordField[7];
            fields[0] = getNumeroSequenceField();
            fields[1] = getAdresseLegaleField();
            fields[2] = getRueLegalField();
            fields[3] = getNpaLegalField();
            fields[4] = getLocaliteLegalField();
            fields[5] = getSuffixePostalLegalField();
            fields[6] = getReserve4();
            setFields(fields);
        } else {
            // /
            numeroSequenceField = instanceField(1, 5, 0, "Numéro de séquence");
            //
            adresseLegaleField = instanceField(7, 40, 1, "P.A. Légale");
            // /
            rueLegalField = instanceField(41, 80, 2, "Rue et numéro légal");
            // /
            npaLegalField = instanceField(81, 84, 3, "Npa légal");
            // /
            localiteLegalField = instanceField(85, 113, 4, "Localité légal");
            // /
            suffixePostalLegalField = instanceField(114, 115, 5, "Suffixe postal légal");
            // /
            reserve4 = instanceField(116, 128, 6, "Suffixe postal légal");

            GERecordField[] fields = new GERecordField[7];
            fields[0] = getNumeroSequenceField();
            fields[1] = getAdresseLegaleField();
            fields[2] = getRueLegalField();
            fields[3] = getNpaLegalField();
            fields[4] = getLocaliteLegalField();
            fields[5] = getSuffixePostalLegalField();
            fields[6] = getReserve4();
            setFields(fields);
        }
    }

    public GERecordField getAdresseLegaleField() {
        return adresseLegaleField;
    }

    public GERecordField getLocaliteLegalField() {
        return localiteLegalField;
    }

    public GERecordField getNpaLegalField() {
        return npaLegalField;
    }

    public GERecordField getNumeroSequenceField() {
        return numeroSequenceField;
    }

    public GERecordField getReserve4() {
        return reserve4;
    }

    public GERecordField getRueLegalField() {
        return rueLegalField;
    }

    public GERecordField getSuffixePostalLegalField() {
        return suffixePostalLegalField;
    }
}
