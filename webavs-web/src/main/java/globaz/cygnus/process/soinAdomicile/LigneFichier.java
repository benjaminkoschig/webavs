package globaz.cygnus.process.soinAdomicile;

import globaz.cygnus.process.demande.LigneImport;
import globaz.cygnus.process.financementSoin.NSS;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;
import com.google.common.base.Joiner;

public class LigneFichier implements LigneImport {

    private Map<String, List<String>> errors = new HashMap<String, List<String>>();
    private Exception exception;
    private final String[] orginalValues;
    private final Montant total;
    private final Montant fraisJournalier;
    private final Integer nbJours;
    private final Date dateDebut;
    private final Date dateDecompte;
    private final Date dateFin;
    private final String nomService;
    private final Integer numeroLigne;
    private final NSS numNss;
    private final Periode periode;
    private final String noIdentification;

    /**
     * Constructeur
     * 
     * @param valueTab
     * @param numLigne
     */
    public LigneFichier(String[] valueTab, Integer numLigne) {
        numeroLigne = numLigne;
        orginalValues = valueTab;
        if (isLigneComplete(valueTab)) {
            noIdentification = toString(CellMappingSoinDomicile.NO_ID_HOME);
            numNss = new NSS(valueTab[CellMappingSoinDomicile.NSS.getIndex()]);
            nomService = toString(CellMappingSoinDomicile.SERVICE);
            fraisJournalier = newMontant(CellMappingSoinDomicile.FRAIS_JOURNALIER);
            dateDebut = newDate(CellMappingSoinDomicile.DATE_DEBUT);
            dateFin = newDate(CellMappingSoinDomicile.DATE_FIN);
            nbJours = toInteger(CellMappingSoinDomicile.NB_JOURS);
            total = newMontant(CellMappingSoinDomicile.MONTANT_TOTAL);
            dateDecompte = newDate(CellMappingSoinDomicile.DATE_DECOMPTE);
            if (dateFin != null && dateDebut != null) {
                periode = new Periode(valueTab[CellMappingSoinDomicile.DATE_DEBUT.getIndex()],
                        valueTab[CellMappingSoinDomicile.DATE_FIN.getIndex()]);
            } else {
                periode = null;
            }
            checkValues();
        } else {
            addErrors("LIGNE_INCOMPLETE");
            noIdentification = null;
            periode = null;
            numNss = null;
            nomService = null;
            fraisJournalier = null;
            dateDebut = null;
            dateFin = null;
            nbJours = null;
            total = null;
            dateDecompte = null;
        }
        if (!isValid()) {
            errors.clear();
        }
    }

    public String getNoIdentification() {
        return noIdentification;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public void addException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Montant getTotal() {
        return total;
    }

    public Montant getFraisJournalier() {
        return fraisJournalier;
    }

    public Integer getNbJours() {
        return nbJours;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateDecompte() {
        return dateDecompte;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getNomService() {
        return nomService;
    }

    @Override
    public Integer getNumeroLigne() {
        return numeroLigne;
    }

    public NSS getNumNss() {
        return numNss;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void addErrors(String message) {
        errors.put(message, new ArrayList<String>());
    }

    public void addErrors(String message, String... params) {
        errors.put(message, Arrays.asList(params));
    }

    public boolean hasError() {
        if (exception != null) {
            return true;
        }
        return !errors.isEmpty();
    }

    @Override
    public String getDescription() {
        return Joiner.on(";").join(orginalValues);
    }

    public boolean isValid() {
        if (periode == null && numNss == null && nomService == null && dateDebut == null && dateFin == null
                && nbJours == null && total == null && dateDecompte == null) {
            return false;
        }
        return true;

    }

    private void checkValues() {
        if (numNss == null || !numNss.isValid()) {
            addErrors(CellMappingSoinDomicile.NSS.toString());
        }

        if (fraisJournalier != null) {
            if (!fraisJournalier.isNormalized()) {
                addErrors(CellMappingSoinDomicile.FRAIS_JOURNALIER.toString());
            }
            if (fraisJournalier.isNegative()) {
                addErrors(CellMappingSoinDomicile.FRAIS_JOURNALIER.toString());
            }
        }

        if (nbJours != null && nbJours <= 0) {
            addErrors(CellMappingSoinDomicile.NB_JOURS.toString());
        }

        if ((fraisJournalier != null && nbJours != null && total != null)
                && !fraisJournalier.multiply(nbJours).equals(total)) {
            addErrors(CellMappingSoinDomicile.MONTANT_TOTAL.toString(),
                    String.valueOf(fraisJournalier.multiply(nbJours)));
        }

        if (periode != null) {
            if (dateFin.before(dateDebut)) {
                addErrors("DATE_DE_FIN_AVANT_DATE_DE_DEBUT", dateFin.getSwissValue(), dateDebut.getSwissMonthValue());
            }
            Integer nbDays = periode.countNbDays();
            if (nbDays != nbJours) {
                addErrors("NOMBRE_JOUR_NON_EGALE", String.valueOf(nbJours), String.valueOf(nbDays));
            }
        }
    }

    private Integer toInteger(CellMappingSoinDomicile mapping) {
        try {
            return Integer.valueOf(orginalValues[mapping.getIndex()]);
        } catch (NumberFormatException ex) {
            addErrors(mapping.toString());
        }
        return null;
    }

    private String toString(CellMappingSoinDomicile mapping) {
        try {
            return String.valueOf(orginalValues[mapping.getIndex()]);
        } catch (NumberFormatException ex) {
            addErrors(mapping.toString());
        }
        return null;
    }

    private Date newDate(CellMappingSoinDomicile mapping) {
        try {
            return new Date(orginalValues[mapping.getIndex()]);
        } catch (IllegalArgumentException ex) {
            addErrors(mapping.toString());
        }
        return null;
    }

    private Montant newMontant(CellMappingSoinDomicile mapping) {
        try {
            return new Montant(orginalValues[mapping.getIndex()]);
        } catch (IllegalArgumentException ex) {
            addErrors(mapping.toString());
        }
        return null;
    }

    public boolean hasChevauchement(LigneFichier ligneFichier) {
        return getPeriode().comparerChevauchement(ligneFichier.getPeriode()) != ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES;
    }

    private boolean isLigneComplete(String valueTab[]) {
        for (String data : valueTab) {
            if (JadeStringUtil.isBlank(data)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPeriodeEmpty() {
        return periode == null;
    }

    @Override
    public Map<String, List<String>> getErrors() {
        return errors;
    }

    @Override
    public int compareTo(LigneImport ligne) {
        return numeroLigne.compareTo(ligne.getNumeroLigne());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateDecompte == null) ? 0 : dateDecompte.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        result = prime * result + ((errors == null) ? 0 : errors.hashCode());
        result = prime * result + ((fraisJournalier == null) ? 0 : fraisJournalier.hashCode());
        result = prime * result + ((nbJours == null) ? 0 : nbJours.hashCode());
        result = prime * result + ((nomService == null) ? 0 : nomService.hashCode());
        result = prime * result + ((numNss == null) ? 0 : numNss.hashCode());
        result = prime * result + ((numeroLigne == null) ? 0 : numeroLigne.hashCode());
        result = prime * result + ((total == null) ? 0 : total.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LigneFichier other = (LigneFichier) obj;
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
        if (errors == null) {
            if (other.errors != null) {
                return false;
            }
        } else if (!errors.equals(other.errors)) {
            return false;
        }
        if (fraisJournalier == null) {
            if (other.fraisJournalier != null) {
                return false;
            }
        } else if (!fraisJournalier.equals(other.fraisJournalier)) {
            return false;
        }
        if (nbJours == null) {
            if (other.nbJours != null) {
                return false;
            }
        } else if (!nbJours.equals(other.nbJours)) {
            return false;
        }
        if (nomService == null) {
            if (other.nomService != null) {
                return false;
            }
        } else if (!nomService.equals(other.nomService)) {
            return false;
        }
        if (numNss == null) {
            if (other.numNss != null) {
                return false;
            }
        } else if (!numNss.equals(other.numNss)) {
            return false;
        }
        if (numeroLigne == null) {
            if (other.numeroLigne != null) {
                return false;
            }
        } else if (!numeroLigne.equals(other.numeroLigne)) {
            return false;
        }
        if (total == null) {
            if (other.total != null) {
                return false;
            }
        } else if (!total.equals(other.total)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LigneFichier [errors=" + errors + ", orginalValues=" + Arrays.toString(orginalValues) + ", total="
                + total + ", fraisJournalier=" + fraisJournalier + ", nbJours=" + nbJours + ", dateDebut=" + dateDebut
                + ", dateDecompte=" + dateDecompte + ", dateFin=" + dateFin + ", nomService=" + nomService
                + ", numeroLigne=" + numeroLigne + ", numNss=" + numNss + ", periode=" + periode + "]";
    }

}
