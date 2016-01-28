package ch.globaz.vulpecula.process.communicationsalaires;

import globaz.globall.db.BSession;
import java.io.FileNotFoundException;
import java.util.Deque;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.external.api.csv.CSVList;

public class CommunicationSalairesCSV extends CSVList {
    private static final int MAX_LENGTH_N_AFF = 13;
    private static final int MAX_LENGTH_NSS = 13;
    private static final int MAX_LENGTH_NOM_TRAVAILLEUR = 60;
    private static final int MAX_LENGTH_MOIS_DEBUT = 2;
    private static final int MAX_LENGTH_MOIS_FIN = 2;
    private static final int MAX_LENGTH_ANNEE = 4;
    private static final int MAX_LENGTH_MONTANT = 13;

    private static final String H_N_AFF = "UTNUAF";
    private static final String H_NSS = "UTNONA";
    private static final String H_NOM_TRAVAILLEUR = "UTNOMT";
    private static final String H_MOIS_DEBUT = "UTMOID";
    private static final String H_MOIS_FIN = "UTMOIF";
    private static final String H_ANNEE = "UTANNE";
    private static final String H_MONTANT = "UTMAVS";

    private Deque<DecompteSalaire> listeSalaires;

    public CommunicationSalairesCSV(Deque<DecompteSalaire> listeSalaires, BSession session, String filenameRoot)
            throws FileNotFoundException {
        super(session, filenameRoot);
        this.listeSalaires = listeSalaires;
    }

    @Override
    public void createContent() {
        createCell(H_N_AFF);
        createCell(H_NSS);
        createCell(H_NOM_TRAVAILLEUR);
        createCell(H_MOIS_DEBUT);
        createCell(H_MOIS_FIN);
        createCell(H_ANNEE);
        createCell(H_MONTANT);

        while (!listeSalaires.isEmpty()) {
            createRow();
            DecompteSalaire decompteSalaire = listeSalaires.removeFirst();
            createCellFormat(decompteSalaire.getEmployeur().getAffilieNumero(), MAX_LENGTH_N_AFF);
            createCellFormat(prepareNumAVS(decompteSalaire.getPosteTravail().getTravailleurNss()), MAX_LENGTH_NSS);
            createCellFormat(decompteSalaire.getPosteTravail().getNomPrenomTravailleur(), MAX_LENGTH_NOM_TRAVAILLEUR);
            createCellFormat(decompteSalaire.getPeriode().getDateDebut().getMois(), MAX_LENGTH_MOIS_DEBUT);
            createCellFormat(decompteSalaire.getPeriode().getDateFin().getMois(), MAX_LENGTH_MOIS_FIN);
            createCellFormat(decompteSalaire.getPeriode().getDateDebut().getAnnee(), MAX_LENGTH_ANNEE);
            createCellFormat(decompteSalaire.getSalaireTotalAsValue(), MAX_LENGTH_MONTANT);
        }
    }

    private String prepareNumAVS(String numAvsBrut) {
        return unformat(checkNumAvs(numAvsBrut));
    }

    private String checkNumAvs(String numAvsActuel) {
        if (numAvsActuel == null || numAvsActuel.length() == 0) {
            return "0";
        }
        return numAvsActuel;
    }
}
