package globaz.ij.acorweb.mapper;

import acor.xsd.in.ij.BasesCalculDecompteCourantIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.acor.adapter.IJAttestationsJoursAdapter;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;

import static globaz.prestation.acor.web.mapper.PRAcorMapper.loadCodeOrNull;

public class IJCalculDecompteIJMapper {
    private final IJBaseIndemnisation baseIndemnisation;
    private final IJIJCalculee ijijCalculee;

    public IJCalculDecompteIJMapper(final IJBaseIndemnisation baseIndemnisation, final IJIJCalculee ijijCalculee) {
        this.baseIndemnisation = baseIndemnisation;
        this.ijijCalculee = ijijCalculee;
    }

    public BasesCalculDecompteCourantIJ map() {
        try {
            IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(this.baseIndemnisation, this.ijijCalculee);

            BasesCalculDecompteCourantIJ basesCalculDecompteCourantIJ = new BasesCalculDecompteCourantIJ();
            basesCalculDecompteCourantIJ.setStatut(1);
            basesCalculDecompteCourantIJ.setId(this.ijijCalculee.getIdIJCalculee());
            basesCalculDecompteCourantIJ.setDebut(Dates.toXMLGregorianCalendar(attestationsJours.getDateDebutPeriode()));
            basesCalculDecompteCourantIJ.setFin(Dates.toXMLGregorianCalendar(attestationsJours.getDateFinPeriode()));
            basesCalculDecompteCourantIJ.setDateCreation(Dates.toXMLGregorianCalendar(Dates.nowFormatSwiss()));
            basesCalculDecompteCourantIJ.setJoursAPayer(computJourAPayer(attestationsJours));
            basesCalculDecompteCourantIJ.setTauxImposition(null);
            basesCalculDecompteCourantIJ.setRaisonInterruption(loadCodeOrNull(
                    this.baseIndemnisation.getSession(),
                    this.baseIndemnisation.getCsMotifInterruption()));

            return basesCalculDecompteCourantIJ;
        } catch (PRACORException e) {
            throw new CommonTechnicalException(e);
        }
    }

    private String computJourAPayer(final IJAttestationsJoursAdapter attestationsJours) {
        if (!JadeStringUtil.isBlankOrZero(attestationsJours.getAttestationsJours())) {
            return attestationsJours.getAttestationsJours();
        } else {
            try {
                int nbrJoursInterne = 0;
                int nbrJoursExterne = 0;
                JADate dd = new JADate(attestationsJours.getDateDebutPeriode());
                JADate df = new JADate(attestationsJours.getDateFinPeriode());

                JACalendar cal = new JACalendarGregorian();
                long nbrJoursDansPeriode = cal.daysBetween(dd, df) + 1;

                if (!JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursInternes())) {
                    nbrJoursInterne = Integer.parseInt(attestationsJours.getNbJoursInternes());
                }
                if (!JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursExternes())) {
                    nbrJoursExterne = Integer.parseInt(attestationsJours.getNbJoursExternes());
                }
                StringBuilder detailJour = new StringBuilder();

                for (int i = 0; i < nbrJoursInterne; i++) {
                    detailJour.append("1");
                }
                for (int i = 0; i < nbrJoursExterne; i++) {
                    detailJour.append("2");
                }
                long diff = nbrJoursDansPeriode - nbrJoursExterne - nbrJoursInterne;

                for (int i = 0; i < diff; i++) {
                    detailJour.append("0");
                }
                return detailJour.toString();
            } catch (JAException e) {
                throw new CommonTechnicalException(e);
            }
        }
    }
}

