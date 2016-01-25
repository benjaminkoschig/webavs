package ch.globaz.vulpecula.process.decompte;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

/**
 * Représentation objet de la colonne "VALUE" de la table "JAPRENTI" pour le
 * process de génération dse décomptes vides.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 14 mars 2014
 * 
 */
public final class PTProcessDecompteInfo {
    public static final String SEPARATOR = ";";

    public static final int INDEX_TYPE = 0;
    public static final int INDEX_PERIODE_DEBUT = 1;
    public static final int INDEX_PERIODE_FIN = 2;
    public static final int INDEX_DATE_ETABLISSEMENT = 3;
    public static final int INDEX_ANNEE = 4;
    public static final int INDEX_PERIODICITE = 5;

    private final TypeDecompte type;
    private final PeriodeMensuelle periode;
    private final Date dateEtablissement;
    private final String periodicite;
    private final int annee;

    public PTProcessDecompteInfo(String type, String periodeDebut, String periodeFin, String dateEtablissement,
            String annee, String periodicite) {
        this(type, periodeDebut, periodeFin, dateEtablissement, Integer.valueOf(annee), periodicite);
    }

    public PTProcessDecompteInfo(String type, String periodeDebut, String periodeFin, String dateEtablissement,
            int annee, String periodicite) {
        this.type = TypeDecompte.fromValue(type);
        periode = new PeriodeMensuelle(periodeDebut, periodeFin);
        this.dateEtablissement = new Date(dateEtablissement);
        this.periodicite = periodicite;
        this.annee = annee;
    }

    public PTProcessDecompteInfo(TypeDecompte type, PeriodeMensuelle periodeMensuelle, Date dateEtablissement,
            int annee, String periodicite) {
        this.type = type;
        periode = periodeMensuelle;
        this.dateEtablissement = dateEtablissement;
        this.annee = annee;
        this.periodicite = periodicite;
    }

    public static PTProcessDecompteInfo createFromPersistence(String decompteInfoAsString) {
        String[] decompteInfoArray = decompteInfoAsString.split(SEPARATOR);
        return new PTProcessDecompteInfo(decompteInfoArray[INDEX_TYPE], decompteInfoArray[INDEX_PERIODE_DEBUT],
                decompteInfoArray[INDEX_PERIODE_FIN], decompteInfoArray[INDEX_DATE_ETABLISSEMENT],
                decompteInfoArray[INDEX_ANNEE], decompteInfoArray[INDEX_PERIODICITE]);

    }

    public String serialize() {
        return type.getValue() + SEPARATOR + periode.getPeriodeDebutAsValue() + SEPARATOR
                + periode.getPeriodeFinAsValue() + SEPARATOR + dateEtablissement.getValue() + SEPARATOR + annee
                + SEPARATOR + periodicite;
    }

    public TypeDecompte getType() {
        return type;
    }

    public PeriodeMensuelle getPeriode() {
        return periode;
    }

    public Date getDateEtablissement() {
        return dateEtablissement;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public int getAnnee() {
        return annee;
    }
}
