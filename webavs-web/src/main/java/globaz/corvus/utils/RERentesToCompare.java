package globaz.corvus.utils;

import ch.globaz.prestation.domaine.CodePrestation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Classe qui permet de trier les rentes entre elles selon le schéma suivant :
 *  - Date de début croissant
 *  - Est principale (selon code prestation)
 *  - Date de fin croissant
 */
public class RERentesToCompare implements Comparable<RERentesToCompare> {

    private static final String SPLIT_CHAR = " - ";
    private final String assure;
    private final String periode;
    private final String montant;
    private final String libelleRente;
    private final String codePrestation;

    public RERentesToCompare(String anAssure, String aPeriode, String aMontant, String aLibelleRente, String aCodePrestation) {
        this.assure = anAssure;
        this.periode = aPeriode;
        this.montant = aMontant;
        this.libelleRente = aLibelleRente;
        this.codePrestation = aCodePrestation;
    }

    @Override
    public int compareTo(RERentesToCompare rentesToCompare) {
        if (Objects.equals(this.getDateDebut(),rentesToCompare.getDateDebut())) {
            if (Objects.equals(this.isPrincipale(),rentesToCompare.isPrincipale())) {
                return this.getDateFin().compareTo(rentesToCompare.getDateFin());
            } else {
                if (this.isPrincipale()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            return this.getDateDebut().compareTo(rentesToCompare.getDateDebut());
        }
    }

    private LocalDate getDateDebut() {
        int posA = periode.indexOf(SPLIT_CHAR);
        String date = periode.substring(0, posA);
        return parseDate(date);
    }

    private LocalDate getDateFin() {
        int posA = periode.lastIndexOf(SPLIT_CHAR);
        String date = periode.substring(posA + SPLIT_CHAR.length());
        return parseDate(date);
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate parseDate = LocalDate.parse(date , formatter);
        return parseDate;
    }

    private boolean isPrincipale() {
        return CodePrestation.getCodePrestation(Integer.parseInt(codePrestation)).isRentePrincipale();
    }

    public String getAssure() {
        return assure;
    }

    public String getPeriode() {
        return periode;
    }

    public String getMontant() {
        return montant;
    }

    public String getLibelleRente() {
        return libelleRente;
    }
}
