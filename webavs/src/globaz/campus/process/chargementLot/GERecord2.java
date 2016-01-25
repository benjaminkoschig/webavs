package globaz.campus.process.chargementLot;

import globaz.campus.application.GEApplication;
import globaz.globall.api.GlobazSystem;

public class GERecord2 extends GEGenericRecord {
    private final GERecordField adresseEtudeField;
    private final GERecordField localiteEtudeField;
    private final GERecordField npaEtudeField;
    private final GERecordField numeroSequenceField;
    private final GERecordField reserve3;
    private final GERecordField rueEtudeField;
    private final GERecordField suffixePostalEtudeField;

    /**
     * Constructor for Record501.
     */
    public GERecord2() {
        this("2");
    }

    protected GERecord2(String _codeEnr) {
        super(_codeEnr);
        boolean nouveauFormat = true;
        try {
            nouveauFormat = ((GEApplication) GlobazSystem.getApplication(GEApplication.DEFAULT_APPLICATION_CAMPUS))
                    .isNouveauFormatEPFL();
        } catch (Exception e) {
            nouveauFormat = true;
        }
        if (nouveauFormat) {
            // /
            numeroSequenceField = instanceField(1, 5, 0, "Numéro de séquence");
            //
            adresseEtudeField = instanceField(7, 46, 1, "P.A. Etude");
            // /
            rueEtudeField = instanceField(47, 86, 2, "Rue et numéro Etude");
            // /
            npaEtudeField = instanceField(87, 90, 3, "Npa Etude");
            // /
            localiteEtudeField = instanceField(91, 130, 4, "Localité Etude");
            // /
            suffixePostalEtudeField = instanceField(131, 132, 5, "Suffixe postal Etude");
            // /
            reserve3 = instanceField(133, 145, 6, "Réserve ligne 2");

            GERecordField[] fields = new GERecordField[7];
            fields[0] = getNumeroSequenceField();
            fields[1] = getAdresseEtudeField();
            fields[2] = getRueEtudeField();
            fields[3] = getNpaEtudeField();
            fields[4] = getLocaliteEtudeField();
            fields[5] = getSuffixePostalEtudeField();
            fields[6] = getReserve3();
            setFields(fields);
        } else {
            // /
            numeroSequenceField = instanceField(1, 5, 0, "Numéro de séquence");
            //
            adresseEtudeField = instanceField(7, 40, 1, "P.A. Etude");
            // /
            rueEtudeField = instanceField(41, 80, 2, "Rue et numéro Etude");
            // /
            npaEtudeField = instanceField(81, 84, 3, "Npa Etude");
            // /
            localiteEtudeField = instanceField(85, 113, 4, "Localité Etude");
            // /
            suffixePostalEtudeField = instanceField(114, 115, 5, "Suffixe postal Etude");
            // /
            reserve3 = instanceField(116, 128, 6, "Réserve ligne 2");

            GERecordField[] fields = new GERecordField[7];
            fields[0] = getNumeroSequenceField();
            fields[1] = getAdresseEtudeField();
            fields[2] = getRueEtudeField();
            fields[3] = getNpaEtudeField();
            fields[4] = getLocaliteEtudeField();
            fields[5] = getSuffixePostalEtudeField();
            fields[6] = getReserve3();
            setFields(fields);
        }
    }

    public GERecordField getAdresseEtudeField() {
        return adresseEtudeField;
    }

    public GERecordField getLocaliteEtudeField() {
        return localiteEtudeField;
    }

    public GERecordField getNpaEtudeField() {
        return npaEtudeField;
    }

    public GERecordField getNumeroSequenceField() {
        return numeroSequenceField;
    }

    public GERecordField getReserve3() {
        return reserve3;
    }

    public GERecordField getRueEtudeField() {
        return rueEtudeField;
    }

    public GERecordField getSuffixePostalEtudeField() {
        return suffixePostalEtudeField;
    }

}
