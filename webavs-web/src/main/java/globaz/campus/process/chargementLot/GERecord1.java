package globaz.campus.process.chargementLot;

import globaz.campus.application.GEApplication;
import globaz.globall.api.GlobazSystem;

public class GERecord1 extends GEGenericRecord {
    private final GERecordField codeDoctorant;
    private final GERecordField dateNaissanceField;
    private final GERecordField etatCivilField;
    private final GERecordField nomField;
    private final GERecordField numeroAvsField;
    private final GERecordField numeroSequenceField;
    private final GERecordField numImmatriculationField;
    private final GERecordField prenomField;
    private final GERecordField reserve1;
    private final GERecordField reserve2;
    private final GERecordField sexeField;

    /**
     * Constructor for Record501.
     */
    public GERecord1() {
        this("1");
    }

    protected GERecord1(String _codeEnr) {
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
            numeroAvsField = instanceField(7, 19, 1, "NNS");
            numeroAvsField.setFormat(GERecordField.FORMAT_AVS);
            // /
            nomField = instanceField(20, 59, 2, "Nom de l'étudiant");
            // /
            prenomField = instanceField(60, 99, 3, "Prénom de l'étudiant");
            // /
            dateNaissanceField = instanceField(100, 105, 4, "Date de naissance de l'étudiant");
            dateNaissanceField.setFormat(GERecordField.FORMAT_DATE_DDsMMsYYYY);
            // /
            sexeField = instanceField(106, 106, 5, "Sexe de l'étudiant");
            // /
            etatCivilField = instanceField(107, 107, 6, "Etat civil de l'étudiant");
            // /
            numImmatriculationField = instanceField(108, 115, 7, "Numéro d'immatricultation");
            // /
            reserve1 = instanceField(116, 123, 8, "Réserve ligne 1");
            // /
            codeDoctorant = instanceField(124, 124, 9, "Code doctorant");
            // /
            reserve2 = instanceField(125, 150, 10, "Réserve ligne 2");
            // /
            GERecordField[] fields = new GERecordField[11];
            fields[0] = getNumeroSequenceField();
            fields[1] = getNumeroAvsField();
            fields[2] = getNomField();
            fields[3] = getPrenomField();
            fields[4] = getDateNaissanceField();
            fields[5] = getSexeField();
            fields[6] = getEtatCivilField();
            fields[7] = getNumImmatriculationField();
            fields[8] = getReserve1();
            fields[9] = getCodeDoctorant();
            fields[10] = getReserve2();
            setFields(fields);
        } else {
            // /
            numeroSequenceField = instanceField(1, 5, 0, "Numéro de séquence");
            //
            numeroAvsField = instanceField(7, 17, 1, "NNS");
            numeroAvsField.setFormat(GERecordField.FORMAT_AVS);
            // /
            nomField = instanceField(18, 47, 2, "Nom de l'étudiant");
            // /
            prenomField = instanceField(48, 77, 3, "Prénom de l'étudiant");
            // /
            dateNaissanceField = instanceField(78, 83, 4, "Date de naissance de l'étudiant");
            dateNaissanceField.setFormat(GERecordField.FORMAT_DATE_DDsMMsYYYY);
            // /
            sexeField = instanceField(84, 84, 5, "Sexe de l'étudiant");
            // /
            etatCivilField = instanceField(85, 85, 6, "Etat civil de l'étudiant");
            // /
            numImmatriculationField = instanceField(86, 93, 7, "Numéro d'immatricultation");
            // /
            reserve1 = instanceField(94, 101, 8, "Réserve ligne 1");
            // /
            codeDoctorant = instanceField(102, 102, 9, "Code doctorant");
            // /
            reserve2 = instanceField(103, 128, 10, "Réserve ligne 2");
            // /
            GERecordField[] fields = new GERecordField[11];
            fields[0] = getNumeroSequenceField();
            fields[1] = getNumeroAvsField();
            fields[2] = getNomField();
            fields[3] = getPrenomField();
            fields[4] = getDateNaissanceField();
            fields[5] = getSexeField();
            fields[6] = getEtatCivilField();
            fields[7] = getNumImmatriculationField();
            fields[8] = getReserve1();
            fields[9] = getCodeDoctorant();
            fields[10] = getReserve2();
            setFields(fields);
        }
    }

    public GERecordField getCodeDoctorant() {
        return codeDoctorant;
    }

    public GERecordField getDateNaissanceField() {
        return dateNaissanceField;
    }

    public GERecordField getEtatCivilField() {
        return etatCivilField;
    }

    public GERecordField getNomField() {
        return nomField;
    }

    public GERecordField getNumeroAvsField() {
        return numeroAvsField;
    }

    public GERecordField getNumeroSequenceField() {
        return numeroSequenceField;
    }

    public GERecordField getNumImmatriculationField() {
        return numImmatriculationField;
    }

    public GERecordField getPrenomField() {
        return prenomField;
    }

    public GERecordField getReserve1() {
        return reserve1;
    }

    public GERecordField getReserve2() {
        return reserve2;
    }

    public GERecordField getSexeField() {
        return sexeField;
    }

}
