package ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;

/**
 * Cette class permet de calculer le montant pour une période d'une PCA
 * 
 * @author dma
 */
public class CalculRestitution {

    private String dateDernierPmt;
    private String dateSuppression;
    private BigDecimal total = new BigDecimal(0);

    public CalculRestitution(String dateSuppression, String dateDernierPmt) {
        if (JadeStringUtil.isBlankOrZero(dateSuppression)) {
            throw new IllegalArgumentException("Unable to CalculRestitution, the  dateSuppression is empty");
        }
        if (JadeStringUtil.isBlankOrZero(dateDernierPmt)) {
            throw new IllegalArgumentException("Unable to CalculRestitution, the dateDernierPmt is empty!");
        }
        this.dateSuppression = dateSuppression;
        this.dateDernierPmt = dateDernierPmt;
    }

    private String addOneMonth(String date) {
        return JadeDateUtil.addMonths("01." + date, 1).substring(3);
    }

    public BigDecimal calculeMontantRestitution(PCAccordee pca) {
        return this.calculeMontantRestitution(pca.getSimplePCAccordee().getDateDebut(), pca.getSimplePCAccordee()
                .getDateFin(), pca.getSimplePrestationsAccordees().getMontantPrestation(), pca
                .getSimplePrestationsAccordeesConjoint().getMontantPrestation());
    }

    public BigDecimal calculeMontantRestitution(String dateDebut, String dateFin, String montant,
            String montantConjointDom2R) {

        BigDecimal montantPca = computMontantPca(montant, montantConjointDom2R);

        int nbreMois = computNbMonth(dateDebut, dateFin);

        BigDecimal totalPca = montantPca.multiply(new BigDecimal(nbreMois));
        total = total.add(totalPca);
        return totalPca;
    }

    private BigDecimal computMontantPca(String montant, String montantConjointDom2R) {
        BigDecimal montantPca = new BigDecimal(montant);

        // si prestations pour conjoint
        if (!JadeStringUtil.isBlankOrZero(montantConjointDom2R)) {
            montantPca = montantPca.add(new BigDecimal(montantConjointDom2R));
        }
        return montantPca;
    }

    private int computNbMonth(String dateDebut, String dateFin) {
        int nbreMois = 0;
        int plusUn = 1;

        try {
            if (dateDernierPmt.equals(dateSuppression)
                    || (JadeDateUtil.isDateMonthYearBefore(dateSuppression, dateDebut) && (dateFin == null) && JADate
                            .getYear("01." + dateSuppression).equals(JADate.getYear("01." + dateDernierPmt)))) {
                return 0;
            }
        } catch (JAException e) {
            return 0;
        }

        if ((JadeDateUtil.isDateMonthYearBefore(dateSuppression, dateFin) || JadeStringUtil.isBlankOrZero(dateFin))
                && !dateDebut.equals(dateDernierPmt)) {
            if (JadeDateUtil.isDateMonthYearAfter(dateDebut, dateDernierPmt)) {
                dateDebut = addOneMonth(dateDernierPmt);

            } else if (JadeDateUtil.isDateMonthYearBefore(dateDebut, dateSuppression)
                    || dateSuppression.equals(dateDebut)) {
                dateDebut = dateSuppression;
                plusUn = 0;
            }
        } else if (dateSuppression.equals(dateFin)) {
            dateDebut = dateSuppression;
            plusUn = 0;
        }

        // Si la date de fin est vide on prend la date du prochain paiement
        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            dateFin = dateDernierPmt;
        }

        if (JadeDateUtil.isDateMonthYearBefore(dateSuppression, dateDebut) || dateSuppression.equals(dateDebut)) {
            if ((JadeDateUtil.isDateMonthYearBefore(dateSuppression, dateFin) || dateSuppression.equals(dateFin))) {

                nbreMois = JadeDateUtil.getNbMonthsBetween("01." + dateDebut, "01." + dateFin);
                nbreMois = plusUn + nbreMois;// on incrémente pour avoir le bon nombre de mois
            }
        }

        // if (JadeDateUtil.isDateMonthYearAfter(this.dateSuppression, this.dateDernierPmt)
        // || this.dateSuppression.equals(this.dateDernierPmt)) {
        // return 0;
        // }
        //
        // // Si la date de fin est vide on prend la date du prochain paiement
        // if (JadeStringUtil.isBlankOrZero(dateFin)) {
        // dateFin = this.dateDernierPmt;
        // }
        //
        // if (JadeDateUtil.isDateMonthYearBefore(dateDebut, this.dateSuppression)) {
        // dateDebut = this.dateSuppression;
        // }
        //
        // nbreMois = JadeDateUtil.getNbMonthsBetween("01." + dateDebut, "01." + dateFin) + 1;
        return nbreMois;
    }

    //
    public BigDecimal getTotal() {
        return total;
    }

    private BigDecimal getYear(String date) {
        try {
            return JADate.getYear("01." + date);
        } catch (JAException e) {
            new RuntimeException(e);
        }
        return null;
    }

    boolean hasPeriodeIndefinit(String dateFin) {
        return JadeStringUtil.isBlankOrZero(dateFin);
    }
}
