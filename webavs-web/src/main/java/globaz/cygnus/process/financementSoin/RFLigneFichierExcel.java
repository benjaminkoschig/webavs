package globaz.cygnus.process.financementSoin;

import globaz.cygnus.utils.RFUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RFLigneFichierExcel implements Comparable<RFLigneFichierExcel> {

    private static final BigDecimal MONTANT_MAX_FRAIS_JOURNALIER = new BigDecimal("23.00");
    private static final String NSS_A_ZERO_POUR_LIGNE_NON_TIERS = "000.0000.0000.00";

    private ArrayList<CellulesExcelEnum> cellulesEnErreur = new ArrayList<CellulesExcelEnum>();
    private String dateDebut = null;
    private String dateDecompte = null;
    private String dateFin = null;
    private String fraisJournalier = null;
    private String montantTotal = null;
    private String nbJours = null;
    private String nomHome = null;
    private String nomPrenom = null;
    private String numeroLigne = null;
    private String numHome = null;
    private String numNss = null;

    /**
     * Constructeur
     * 
     * @param valueTab
     * @param numLigne
     */
    public RFLigneFichierExcel(String valueTab[], String numLigne) {

        if (isLigneComplete(valueTab)) {

            numHome = valueTab[Integer.valueOf(CellulesExcelEnum.NUM_HOME.getIndexCellule())];
            nomHome = valueTab[Integer.valueOf(CellulesExcelEnum.NOM_HOME.getIndexCellule())];
            numNss = valueTab[Integer.valueOf(CellulesExcelEnum.NUM_NSS.getIndexCellule())];
            nomPrenom = valueTab[Integer.valueOf(CellulesExcelEnum.NOM_PRENOM.getIndexCellule())];
            fraisJournalier = valueTab[Integer.valueOf(CellulesExcelEnum.FRAIS_JOURNALIER.getIndexCellule())];
            dateDebut = valueTab[Integer.valueOf(CellulesExcelEnum.DATE_DEBUT.getIndexCellule())];
            dateFin = valueTab[Integer.valueOf(CellulesExcelEnum.DATE_FIN.getIndexCellule())];
            nbJours = valueTab[Integer.valueOf(CellulesExcelEnum.NB_JOURS.getIndexCellule())];
            montantTotal = valueTab[Integer.valueOf(CellulesExcelEnum.MONTANT_TOTAL.getIndexCellule())];
            dateDecompte = valueTab[Integer.valueOf(CellulesExcelEnum.DATE_DECOMPTE.getIndexCellule())];
            numeroLigne = numLigne;

            // Vérification des valeurs de chaque cellule
            checkErrorsData();

        } else {
            traiterLigneIncomplete(valueTab, numLigne);
        }
    }

    public void addCelluleExcelEnum(CellulesExcelEnum cellule) {
        cellulesEnErreur.add(cellule);
    }

    /**
     * Controles la cohérence des dates. Format des dates, et si la dateDebut est supérieur à dateFin
     */
    private void checkDates() {
        // controle la dateDébut
        if ((dateDebut == null) || ((!JadeDateUtil.isGlobazDate(dateDebut)))) {
            cellulesEnErreur.add(CellulesExcelEnum.DATE_DEBUT);
        }

        // Controle la dateFin
        if ((dateFin == null) || (!JadeDateUtil.isGlobazDate(dateFin))) {
            cellulesEnErreur.add(CellulesExcelEnum.DATE_FIN);
        }

        // controle l'ordre des dateDebut et dateFin
        if ((!cellulesEnErreur.contains(CellulesExcelEnum.DATE_DEBUT))
                && (!cellulesEnErreur.contains(CellulesExcelEnum.DATE_FIN))) {
            if (JadeDateUtil.isDateAfter(dateDebut, dateFin)) {
                cellulesEnErreur.add(CellulesExcelEnum.DATE_DEBUT);
            }
        }

        // controle la dateDecompte
        if ((dateDecompte == null) || (!JadeDateUtil.isGlobazDate(dateDecompte))) {
            cellulesEnErreur.add(CellulesExcelEnum.DATE_DECOMPTE);
        }
    }

    /**
     * Controle cohérence des champs souhaités
     */
    private void checkErrorsData() {
        checkNss();
        checkFraisJournalier();
        checkDates();
        checkNbJours();
        checkMontantTotal();

    }

    /**
     * Contrôle la cohérence des frais journalier //
     */
    private void checkFraisJournalier() {

        if ((fraisJournalier == null) || (!JadeNumericUtil.isNumeric(fraisJournalier))) {
            cellulesEnErreur.add(CellulesExcelEnum.FRAIS_JOURNALIER);
        } else {

            BigDecimal fraisJournalier = new BigDecimal(this.fraisJournalier);

            // Si montant supérieur à 23.00, retourne 1 / si montant negatif, retourne -1
            if ((fraisJournalier.compareTo(RFLigneFichierExcel.MONTANT_MAX_FRAIS_JOURNALIER) > 0)) {
                cellulesEnErreur.add(CellulesExcelEnum.FRAIS_JOURNALIER);
            }
        }
    }

    /**
     * Controle si le montantTotal = nbJours x fraisJournalier
     */
    private void checkMontantTotal() {

        if ((cellulesEnErreur.contains(CellulesExcelEnum.NB_JOURS))
                || (cellulesEnErreur.contains(CellulesExcelEnum.FRAIS_JOURNALIER) || (cellulesEnErreur == null) || (!JadeNumericUtil
                        .isNumeric(montantTotal)))) {
            cellulesEnErreur.add(CellulesExcelEnum.MONTANT_TOTAL);
        } else {

            BigDecimal nbJours = new BigDecimal(this.nbJours);
            BigDecimal montantJournalier = new BigDecimal(fraisJournalier);
            BigDecimal montantTotal = new BigDecimal(this.montantTotal);

            if ((nbJours.multiply(montantJournalier).compareTo(montantTotal) < 0)
                    || (nbJours.multiply(montantJournalier).compareTo(montantTotal) > 0)) {
                cellulesEnErreur.add(CellulesExcelEnum.MONTANT_TOTAL);
            }
        }
    }

    /**
     * Controle si le nombre de jours entre dateDebut et dateFin = nombre de jours total
     */
    private void checkNbJours() {

        int nbJoursPeriode = 0;
        int nbJoursTotal = 0;

        if ((nbJours == null) || (!JadeNumericUtil.isNumeric(nbJours))) {
            cellulesEnErreur.add(CellulesExcelEnum.NB_JOURS);
        } else {
            // Si aucune erreur de date : comparaison du nombre de jours entre les périodes et du nombre de jours total
            if (!cellulesEnErreur.contains(CellulesExcelEnum.DATE_DEBUT)
                    && !cellulesEnErreur.contains(CellulesExcelEnum.DATE_FIN)) {
                nbJoursPeriode = PRDateUtils.getNbDayBetween2(dateDebut, dateFin) + 1;

                nbJoursTotal = Integer.valueOf(nbJours);

                if ((nbJoursPeriode != nbJoursTotal)) {
                    if (!cellulesEnErreur.contains(CellulesExcelEnum.NB_JOURS)) {
                        cellulesEnErreur.add(CellulesExcelEnum.NB_JOURS);
                    }
                }
            } else {
                cellulesEnErreur.add(CellulesExcelEnum.NB_JOURS);
            }

        }
    }

    /**
     * Contrôle la cohérence du nss
     * 
     * @param valueTab
     */
    private void checkNss() {

        if ((numNss == null) || !RFUtils.isNNS(numNss)) {
            cellulesEnErreur.add(CellulesExcelEnum.NUM_NSS);
        }
    }

    @Override
    public int compareTo(RFLigneFichierExcel ligne) {
        if (Integer.parseInt(numeroLigne) < Integer.parseInt(ligne.numeroLigne)) {
            return -1;
        } else if (Integer.parseInt(numeroLigne) > Integer.parseInt(ligne.numeroLigne)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Methode pour comparer l'égalité d'une propriété entre 2 objets
     * 
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        RFLigneFichierExcel other = (RFLigneFichierExcel) obj;
        if (cellulesEnErreur == null) {
            if (other.cellulesEnErreur != null) {
                return false;
            }
        } else if (!cellulesEnErreur.equals(other.cellulesEnErreur)) {
            return false;
        }
        if (dateDebut == null) {
            if (other.dateDebut != null) {
                return false;
            }
        } else if (!dateDebut.equals(other.dateDebut)) {
            return false;
        }
        if (dateDecompte == null) {
            if (other.dateDecompte != null) {
                return false;
            }
        } else if (!dateDecompte.equals(other.dateDecompte)) {
            return false;
        }
        if (dateFin == null) {
            if (other.dateFin != null) {
                return false;
            }
        } else if (!dateFin.equals(other.dateFin)) {
            return false;
        }
        if (fraisJournalier == null) {
            if (other.fraisJournalier != null) {
                return false;
            }
        } else if (!fraisJournalier.equals(other.fraisJournalier)) {
            return false;
        }
        if (montantTotal == null) {
            if (other.montantTotal != null) {
                return false;
            }
        } else if (!montantTotal.equals(other.montantTotal)) {
            return false;
        }
        if (nbJours == null) {
            if (other.nbJours != null) {
                return false;
            }
        } else if (!nbJours.equals(other.nbJours)) {
            return false;
        }
        if (nomHome == null) {
            if (other.nomHome != null) {
                return false;
            }
        } else if (!nomHome.equals(other.nomHome)) {
            return false;
        }
        if (nomPrenom == null) {
            if (other.nomPrenom != null) {
                return false;
            }
        } else if (!nomPrenom.equals(other.nomPrenom)) {
            return false;
        }
        if (numHome == null) {
            if (other.numHome != null) {
                return false;
            }
        } else if (!numHome.equals(other.numHome)) {
            return false;
        }
        if (numNss == null) {
            if (other.numNss != null) {
                return false;
            }
        } else if (!numNss.equals(other.numNss)) {
            return false;
        }
        return true;
    }

    public List<CellulesExcelEnum> getCellulesEnErreur() {
        return Collections.unmodifiableList(cellulesEnErreur);
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDecompte() {
        return dateDecompte;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getFraisJournalier() {
        return fraisJournalier;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public String getNbJours() {
        return nbJours;
    }

    public String getNomHome() {
        return nomHome;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumeroLigne() {
        return numeroLigne;
    }

    public String getNumHome() {
        return numHome;
    }

    public String getNumNss() {
        return numNss;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellulesEnErreur == null) ? 0 : cellulesEnErreur.hashCode());
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateDecompte == null) ? 0 : dateDecompte.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        result = prime * result + ((fraisJournalier == null) ? 0 : fraisJournalier.hashCode());
        result = prime * result + ((montantTotal == null) ? 0 : montantTotal.hashCode());
        result = prime * result + ((nbJours == null) ? 0 : nbJours.hashCode());
        result = prime * result + ((nomHome == null) ? 0 : nomHome.hashCode());
        result = prime * result + ((nomPrenom == null) ? 0 : nomPrenom.hashCode());
        result = prime * result + ((numHome == null) ? 0 : numHome.hashCode());
        result = prime * result + ((numNss == null) ? 0 : numNss.hashCode());
        return result;
    }

    private boolean isLigneComplete(String valueTab[]) {
        for (String data : valueTab) {
            if (JadeStringUtil.isBlank(data)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param cellulesEnErreur
     *            the cellulesEnErreur to set
     */
    public void setCellulesEnErreur(ArrayList<CellulesExcelEnum> cellulesEnErreur) {
        this.cellulesEnErreur = cellulesEnErreur;
    }

    /**
     * Methode pour récupérer les données existante, d'une ligne incomplète.
     * 
     * @param valueTab
     * @param numeroLigne
     */
    private void traiterLigneIncomplete(String valueTab[], String numeroLigne) {

        if (!JadeStringUtil.isBlank(numeroLigne)) {
            this.numeroLigne = numeroLigne;
        }

        for (int i = 0; i < valueTab.length; i++) {

            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.NUM_HOME.getIndexCellule())) {
                numHome = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.NOM_HOME.getIndexCellule())) {
                nomHome = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.NUM_NSS.getIndexCellule())) {
                numNss = valueTab[i];
            } else {
                numNss = RFLigneFichierExcel.NSS_A_ZERO_POUR_LIGNE_NON_TIERS;
            }

            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.NOM_PRENOM.getIndexCellule())) {
                nomPrenom = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.FRAIS_JOURNALIER.getIndexCellule())) {
                fraisJournalier = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.DATE_DEBUT.getIndexCellule())) {
                dateDebut = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.DATE_FIN.getIndexCellule())) {
                dateFin = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.NB_JOURS.getIndexCellule())) {
                nbJours = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.MONTANT_TOTAL.getIndexCellule())) {
                montantTotal = valueTab[i];
            }
            if (!JadeStringUtil.isBlank(valueTab[i]) && (i == CellulesExcelEnum.DATE_DECOMPTE.getIndexCellule())) {
                dateDecompte = valueTab[i];
            }
        }
        cellulesEnErreur.add(CellulesExcelEnum.LIGNE_INCOMPLETE);
    }
}
