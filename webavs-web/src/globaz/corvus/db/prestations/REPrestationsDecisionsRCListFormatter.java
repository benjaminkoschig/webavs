package globaz.corvus.db.prestations;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean;
import globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Centralise le traitement de recherche de date de début / date de fin qui étaient précédemment le même<br/>
 * (dans l'écrande liste des {@link REPrestationsJointTiersViewBean} et des {@link REDecisionJointDemandeRenteViewBean})
 * 
 * @author PBA
 * 
 */
public class REPrestationsDecisionsRCListFormatter {

    private IREPrestatationsDecisionsRCListIterator iterator = null;
    private Comparator<String> myComparator = null;

    public REPrestationsDecisionsRCListFormatter(IREPrestatationsDecisionsRCListIterator iterator) {
        this.iterator = iterator;

        myComparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.decode(o1).compareTo(Integer.decode(o2));
            }
        };
    }

    /**
     * Complète si nécessaire et retourne l'élement suivant de la liste
     * 
     * @return L'élement si la liste en contient encore un, sinon <code>null</code>
     * @throws JAException
     */
    public IREPrestatationsDecisionsRCListViewBean getNextElement() throws JAException {
        if ((iterator != null) && iterator.hasNext()) {

            IREPrestatationsDecisionsRCListViewBean viewBean = iterator.next();
            IREPrestatationsDecisionsRCListViewBean current = viewBean;

            boolean isContentInfini = false;

            JADate dateDebut = null;
            JADate dateFin = null;
            if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutDroit())) {
                dateDebut = new JADate(viewBean.getDateDebutDroit());
            }
            if (IREDecision.CS_TYPE_DECISION_RETRO.equals(viewBean.getCsTypeDecision())) {
                dateFin = new JADate(viewBean.getDateFinRetro());
            } else if (!JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit())) {
                dateFin = new JADate(viewBean.getDateFinDroit());
            }

            if (dateDebut == null) {
                dateDebut = new JADate("31.12.2999");
            }

            List<String> genres = new ArrayList<String>();
            genres.add(viewBean.getGenrePrestation());

            JACalendar cal = new JACalendarGregorian();

            while (iterator.isNextSameEntity()) {
                viewBean = iterator.next();

                JADate dateDebutNext = null;
                JADate dateFinNext = null;

                if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutDroit())) {
                    dateDebutNext = new JADate(viewBean.getDateDebutDroit());
                    if (cal.compare(dateDebut, dateDebutNext) == JACalendar.COMPARE_SECONDLOWER) {
                        dateDebut = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebutNext
                                .toStrAMJ()));
                    }
                }

                if (dateFin != null) {
                    if (!JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit())) {
                        dateFinNext = new JADate(viewBean.getDateFinDroit());
                        if (cal.compare(dateFin, dateFinNext) == JACalendar.COMPARE_FIRSTLOWER) {
                            dateFin = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFinNext
                                    .toStrAMJ()));
                        }
                    }
                }

                if (JadeStringUtil.isIntegerEmpty(viewBean.getDateFinDroit())) {
                    isContentInfini = true;
                }

                if (!(genres.contains(viewBean.getGenrePrestation()))) {
                    genres.add(viewBean.getGenrePrestation());
                }

                if (current.getNumDateDebutDroit() < viewBean.getNumDateDebutDroit()) {
                    current = viewBean;
                }
            }

            Collections.sort(genres, myComparator);

            StringBuilder genrePrestation = new StringBuilder();
            for (int j = 0; genres.size() > j; j++) {
                if (j % 2 != 0) {
                    genrePrestation.append(" - ");
                    genrePrestation.append(genres.get(j));
                } else {
                    if (j != 0) {
                        genrePrestation.append("<br/>");
                    }
                    genrePrestation.append(genres.get(j));
                }
            }
            viewBean.setGenrePrestationAffichage(genrePrestation.toString());

            String dateDebutAffichage = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDebut.toStrAMJ());
            String dateFinAffichage = "";

            if (IREDecision.CS_TYPE_DECISION_RETRO.equals(viewBean.getCsTypeDecision())) {
                dateFinAffichage = viewBean.getDateFinRetro();
            } else {
                if (dateFin != null) {
                    if (!isContentInfini) {
                        dateFinAffichage = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateFin.toStrAMJ());
                    }
                }
            }
            viewBean.setDateDebutAffichage(dateDebutAffichage);
            viewBean.setDateFinAffichage(dateFinAffichage);

            return viewBean;
        }
        return null;
    }
}
