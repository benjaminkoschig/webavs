package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompteSearch;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionUtils;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.utils.periode.GroupePeriodesResolver;

/**
 * Le but de cette class et de permettre de retrouver les pca qui ont été remplacer par le nouvelles pca. Pour retrouver
 * les anciennes pca il faut determiner des born(dateDebut et dateFin) cet pour cette raison qu l'on passer une period
 * de pca
 * 
 * @author dma
 */
public class PcaPrecedante {
    public static <T> List<PcaForDecompte> findPcaToReplaced(List<T> newPcas,
            GroupePeriodesResolver.EachPeriode<T> eachNewPca, String idDroit, String noVersionDroitCourant)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(newPcas, eachNewPca);
        Periode dateDebutFin = PcaPrecedante.resolveDateDebutMinDateFinMax(periodes);
        return PcaPrecedante.findPcaToReplaced(dateDebutFin.getDateDebut(), dateDebutFin.getDateFin(), idDroit,
                noVersionDroitCourant);
    }

    public static List<PcaForDecompte> findPcaToReplaced(GroupePeriodes periodes, String idDroit,
            String noVersionDroitCourant) throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        Periode periode = PcaPrecedante.resolveDateDebutMinDateFinMax(periodes);
        return PcaPrecedante.findPcaToReplaced(periode.getDateDebut(), periode.getDateFin(), idDroit,
                noVersionDroitCourant);
    }

    public static List<PcaForDecompte> findPcaCourrante(String idDroit, String noVersionDroitCourant)
            throws JadePersistenceException {
        return findPcaCourrante(idDroit, noVersionDroitCourant);
    }

    public static List<PcaForDecompte> findPcaCourrante(String idDroit, String noVersionDroitCourant, String dateFin)
            throws JadePersistenceException {

        PcaForDecompteSearch search = new PcaForDecompteSearch();

        search.setForIdDroit(idDroit);
        search.setForNoVersion(noVersionDroitCourant);
        search.setOrderKey(PcaForDecompteSearch.ORDER_BY_DATE_DEBUT_AND_CS_ROLE);
        search.setWhereKey(PcaForDecompteSearch.FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE_FOR_DECOMPTE);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (!JadeStringUtil.isBlankOrZero(dateFin)) {
            search.setForDateMin(dateFin);
        }

        return PersistenceUtil.search(search);

    }

    public static List<PcaForDecompte> findPcaToReplaced(String dateMin, String dateMax, String idDroit,
            String noVersionDroitCourant) throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(dateMin)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the dateMin is null!");
        }

        if (JadeStringUtil.isBlankOrZero(dateMax)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the dateMax is null!");
        }

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the dDroit is null!");
        }

        if (JadeStringUtil.isBlankOrZero(noVersionDroitCourant)) {
            throw new IllegalArgumentException("Unable to findPcaToReplaced, the noVersionDroitCourant is null!");
        }

        PcaForDecompteSearch search = new PcaForDecompteSearch();
        List<PcaForDecompte> list = new ArrayList<PcaForDecompte>();
        // Il n'y pas de pca à historisé pour la version initial du droit
        if (!ValiderDecisionUtils.isDroitInitial(noVersionDroitCourant)) {
            if (JadeStringUtil.isBlankOrZero(dateMax)) {
                dateMax = dateMin;
            }
            search.setForDateMin(dateMin);
            search.setForDateMax(dateMax);
            search.setForIdDroit(idDroit);
            search.setForNoVersion(noVersionDroitCourant);
            search.setOrderKey(PcaForDecompteSearch.ORDER_BY_DATE_DEBUT_AND_CS_ROLE);
            search.setWhereKey(PcaForDecompteSearch.FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE_FOR_DECOMPTE);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            list = PersistenceUtil.search(search);
            // On fait ce test pour ne pas remonter tout les pca
            if (list.size() == 0) {
                if (!ValiderDecisionUtils.isDroitInitial(noVersionDroitCourant)) {
                    throw new PCAccordeeException("Unable to find the old pca for change the stat to historisée");
                }
            }
        }
        return list;
    }

    static Periode resolveDateDebutMinDateFinMax(GroupePeriodes periodes) {
        String dateMin = periodes.getDateDebutMin();
        String dateMax = periodes.getDateFinMax();
        if (periodes.hasDateFinNullValue()) {
            dateMax = "12." + periodes.getDateDebutMax().substring(3);
        }
        return new Periode(dateMin, dateMax);
    }

}
