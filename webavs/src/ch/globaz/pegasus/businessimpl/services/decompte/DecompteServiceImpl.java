package ch.globaz.pegasus.businessimpl.services.decompte;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.decompte.DecompteException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.services.decompte.DecompteService;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.GroupPcaByMonth;
import ch.globaz.pegasus.businessimpl.services.pca.DecomptePca;
import ch.globaz.pegasus.businessimpl.services.pca.PeriodePca;
import ch.globaz.pegasus.businessimpl.utils.decompte.DecompteTotalPC;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;

public class DecompteServiceImpl implements DecompteService {

    private BigDecimal computeMontant(List<PeriodePca> periodesPca) {
        if (periodesPca == null) {
            throw new IllegalArgumentException("Unable to computeMontant, the periodesPca is null!");
        }
        BigDecimal montant = new BigDecimal(0);
        for (PeriodePca periodePca : periodesPca) {
            montant = montant.add(computeMontantPeriode(periodePca));
        }
        return montant;
    }

    private BigDecimal computeMontantPeriode(PeriodePca periodePca) {
        BigDecimal montantConjoint;
        BigDecimal montantRequerant;
        montantRequerant = computeMontantPeriodePersonne(periodePca.getPcaRequerantNew(),
                periodePca.getPcaRequeranReplaced());
        montantConjoint = computeMontantPeriodePersonne(periodePca.getPcaConjointNew(),
                periodePca.getPcaConjointReplaced());
        return montantRequerant.add(montantConjoint).multiply(new BigDecimal(periodePca.getNbMont()));
    }

    private BigDecimal computeMontantPeriodePersonne(PcaDecompte pcaNew, PcaDecompte pcaReplaced) {
        BigDecimal montantNew = returnMontantPca(pcaNew);
        BigDecimal montantReplaced = returnMontantPca(pcaReplaced);
        return montantNew.subtract(montantReplaced);
    }

    private String createDateKeyForMap(String date, int i) {
        String dateKey = null;
        Calendar cal = JadeDateUtil.getGlobazCalendar("01." + date);
        cal.add(Calendar.MONTH, i);
        dateKey = cal.get(Calendar.YEAR)
                + JadeStringUtil.rightJustifyInteger(String.valueOf(cal.get(Calendar.MONTH) + 1), 2);
        return dateKey;
    }

    @Override
    public String[] determinDateMinMax(List<PcaDecompte> newPcas) {
        GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(newPcas, new EachPeriode<PcaDecompte>() {
            @Override
            public String[] dateDebutFin(PcaDecompte t) {
                return new String[] { t.getDateDebut(), t.getDateFin() };
            }
        });

        String[] dates = new String[2];
        dates[0] = periodes.getDateDebutMin();
        if (!periodes.hasDateFinNullValue()) {
            dates[1] = periodes.getDateFinMax();
        } else {
            dates[1] = null;
        }
        return dates;
    }

    private BigDecimal computeMontantJoursAppoint(List<SimpleJoursAppoint> joursAppointNew,
            List<SimpleJoursAppoint> joursAppointReplaced) {

        BigDecimal sum = sumJoursAppoint(joursAppointNew);
        sum = sum.subtract(sumJoursAppoint(joursAppointReplaced));

        return sum;
    }

    private BigDecimal sumJoursAppoint(List<SimpleJoursAppoint> joursAppoint) {
        BigDecimal sum = new BigDecimal(0);
        if (joursAppoint != null) {
            for (SimpleJoursAppoint simpleJoursAppoint : joursAppoint) {
                sum = sum.add(new BigDecimal(simpleJoursAppoint.getMontantTotal()));
            }
        }
        return sum;
    }

    @Override
    public DecomptePca generateDecomptePca(List<PcaDecompte> newPcas, List<PcaDecompte> replacedPcas,
            String dateDernierPmtPca, List<SimpleJoursAppoint> joursAppointNew,
            List<SimpleJoursAppoint> joursAppointReplaced) throws DecompteException {

        if ((newPcas == null) || (newPcas.size() == 0)) {
            throw new DecompteException("Unable to generateDecomptePca the newPcas is null! or empty");
        }

        if (dateDernierPmtPca == null) {
            throw new DecompteException("Unable to generateDecomptePca the dateDernierPmtPca is null!");
        }

        DecomptePca decomptePca = new DecomptePca();
        String[] date = determinDateMinMax(newPcas);
        decomptePca.setDateDebut(date[0]);
        decomptePca.setDateFin(date[1]);
        // On a donc du retro
        if (!isEffetMoisSuivant(newPcas)) {
            List<PeriodePca> periodePca = generatePeriodePca(newPcas, replacedPcas, dateDernierPmtPca);
            decomptePca.setPeriodesPca(periodePca);
            decomptePca.setMontant(computeMontant(periodePca).add(
                    computeMontantJoursAppoint(joursAppointNew, joursAppointReplaced)));

        } else {
            List<PeriodePca> periodePca = generatePeriodePcaWithOutRetro(newPcas, dateDernierPmtPca);
            decomptePca.setPeriodesPca(periodePca);
            decomptePca.setMontant(new BigDecimal(0));
        }
        return decomptePca;
    }

    private List<PeriodePca> generatePeriodePca(List<PcaDecompte> newPcas, List<PcaDecompte> replacedPca,
            String dateDernierPmtPca) throws DecompteException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();

        GroupPcaByMonth newPcaByMonth = groupPcaByMonth(newPcas, dateDernierPmtPca);
        GroupPcaByMonth replacedPcaByMonth = groupPcaByMonth(replacedPca, dateDernierPmtPca);

        List<String> keys = orderKeys(newPcaByMonth.getMapRequerantByMonth().keySet());

        PcaDecompte pcaRequerantTemp = null;
        PcaDecompte pcaRequerantReplacedTemp = null;
        PeriodePca periodePcaTemp = null;

        for (String key : keys) {
            PcaDecompte pcaRequerant = newPcaByMonth.getMapRequerantByMonth().get(key);
            PcaDecompte pcaConjoint = newPcaByMonth.getMapPcaConjointByMonth().get(key);
            PcaDecompte pcaRequerantReplaced = replacedPcaByMonth.getMapRequerantByMonth().get(key);
            PcaDecompte pcaConjointReplaced = replacedPcaByMonth.getMapPcaConjointByMonth().get(key);
            if (!pcaRequerant.equals(pcaRequerantTemp)) {
                if (pcaRequerantTemp != null) {
                    periodesPca.add(periodePcaTemp);
                }
                pcaRequerantTemp = pcaRequerant;
                pcaRequerantReplacedTemp = pcaRequerantReplaced;
                periodePcaTemp = new PeriodePca();
                periodePcaTemp.setPcaRequerantNew(pcaRequerant);
                periodePcaTemp.setPcaConjointNew(pcaConjoint);
                periodePcaTemp.setPcaRequeranReplaced(pcaRequerantReplaced);
                periodePcaTemp.setPcaConjointReplaced(pcaConjointReplaced);
            } else if ((pcaRequerantReplaced != null) && !pcaRequerantReplaced.equals(pcaRequerantReplacedTemp)) {
                pcaRequerantReplacedTemp = pcaRequerantReplaced;
                periodesPca.add(periodePcaTemp);
                periodePcaTemp = new PeriodePca();
                periodePcaTemp.setPcaRequerantNew(pcaRequerant);
                periodePcaTemp.setPcaConjointNew(pcaConjoint);
                periodePcaTemp.setPcaRequeranReplaced(pcaRequerantReplaced);
                periodePcaTemp.setPcaConjointReplaced(pcaConjointReplaced);
            }
            periodePcaTemp.setNbMont(periodePcaTemp.getNbMont() + 1);
        }
        periodesPca.add(periodePcaTemp);
        return periodesPca;
    }

    private List<PeriodePca> generatePeriodePcaWithOutRetro(List<PcaDecompte> newPcas, String dateDernierPmtPca)
            throws DecompteException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();

        Map<String, List<PcaDecompte>> mapPcaNewByRole = groupByCsRole(newPcas);
        // Normalement il ne devrait jamais avoir plus de 2 elements dans cette map. Car le cs role d'une pca ne
        // peut
        // être soit un requérant et le conjoint
        if (mapPcaNewByRole.size() > 2) {
            throw new DecompteException("Too my type csRoleBenficiaire was founded");
        }

        List<PcaDecompte> pcasRequerant = mapPcaNewByRole.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        List<PcaDecompte> pcasConjoint = mapPcaNewByRole.get(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);

        if (pcasRequerant.size() > 2) {
            throw new DecompteException("Too many pca founded for a calcul effet mois suivant");
        }

        PeriodePca periodePcaTemp = new PeriodePca();
        periodePcaTemp.setPcaRequerantNew(pcasRequerant.get(0));
        if (pcasConjoint != null) {
            if (pcasConjoint.size() > 2) {
                throw new DecompteException("Too many pca founded for a calcul effet mois suivant");
            }
            periodePcaTemp.setPcaConjointNew(pcasConjoint.get(0));
        }

        periodePcaTemp.setNbMont(0);
        periodesPca.add(periodePcaTemp);
        return periodesPca;
    }

    @Override
    public DecompteTotalPcVO getDecompteTotalPCA(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationException {

        if (idVersionDroit == null) {
            throw new DecompteException("Unable to generate the decompte, the idVersion droit passed is null");
        }
        DecompteTotalPC decompteTotal = new DecompteTotalPC(idVersionDroit);
        return decompteTotal.generateVO();
    }

    private Map<String, List<PcaDecompte>> groupByCsRole(List<PcaDecompte> newPcas) {
        return JadeListUtil.groupBy(newPcas, new JadeListUtil.Key<PcaDecompte>() {
            @Override
            public String exec(PcaDecompte e) {
                return e.getCsRoleBeneficiaire();
            }
        });
    }

    private GroupPcaByMonth groupPcaByMonth(List<PcaDecompte> pcas, String dateDernierPmtPca) throws DecompteException {
        GroupPcaByMonth pcaByMonth = new GroupPcaByMonth();
        if (pcas != null) {
            Map<String, List<PcaDecompte>> mapPcaNewByRole = groupByCsRole(pcas);

            // Normalement il ne devrait jamais avoir plus de 2 elements dans cette map. Car le cs role d'une pca ne
            // peut
            // être soit un requérant et le conjoint
            if (mapPcaNewByRole.size() > 2) {
                throw new DecompteException("Too my type csRoleBenficiaire was founded");
            }

            HashMap<String, PcaDecompte> mapRequerantByDate = splitPCAInMois(
                    mapPcaNewByRole.get(IPCDroits.CS_ROLE_FAMILLE_REQUERANT), dateDernierPmtPca);

            HashMap<String, PcaDecompte> mapPcaConjointByDate = splitPCAInMois(
                    mapPcaNewByRole.get(IPCDroits.CS_ROLE_FAMILLE_CONJOINT), dateDernierPmtPca);

            pcaByMonth.setMapPcaConjointByMonth(mapPcaConjointByDate);
            pcaByMonth.setMapRequerantByMonth(mapRequerantByDate);
        }
        return pcaByMonth;
    }

    private boolean isEffetMoisSuivant(List<PcaDecompte> newPcas) {
        if (newPcas.get(0).getSimplePCAccordee().getIsCalculRetro()) {
            return false;
        }
        return true;
        // if (dateDebutDernierePca.equals(JadeDateUtil.addMonths("01." + dateDernierPmtPca, 1).substring(3))
        // && (newPcas.size() == 1)) {
        // return true;
        // }
        // return false;
    }

    @Override
    public Boolean isMontantDisponibleRetnable(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationException {
        DecompteTotalPcVO decompte = getDecompteTotalPCA(idVersionDroit);

        // Si on a une dette ou une créance le montant ne peut pas être négatif
        if ((decompte.getDettesCompta().getList().size() > 0) || (decompte.getCreanciers().getList().size() > 0)) {
            if ((decompte.getTotal().signum() == -1) || (decompte.getTotal().signum() == 0)) {
                return false;
            }
        }
        return true;
    }

    private int nbMonth(PcaDecompte pca, String dateDernierPmtPca) {
        String dateFin = pca.getDateFin();
        if (JadeStringUtil.isEmpty(dateFin)) {
            dateFin = dateDernierPmtPca;
        }
        String dateFinModif = "01." + dateFin;
        String dateDebutModif = "01." + pca.getDateDebut();
        int nbMonth = JadeDateUtil.getNbMonthsBetween(dateDebutModif, dateFinModif);

        // on donne le nombre de mois entre les deux date + 1 (car du 12.2010 au 12.2010, il n'y a pas de mois entre les
        // deux dates, mais nous avons bien paye un mois)

        nbMonth = nbMonth + 1;
        return nbMonth;
    }

    private List<String> orderKeys(Set<String> keys) {
        List<String> k = new ArrayList<String>(keys);
        Collections.sort(k, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });
        return k;
    }

    private BigDecimal returnMontantPca(PcaDecompte pca) {
        BigDecimal montant = null;
        if (pca == null) {
            montant = new BigDecimal(0);
        } else {
            montant = new BigDecimal(pca.getMontantPCMensuelle());
        }
        return montant;
    }

    private HashMap<String, PcaDecompte> splitPCAInMois(List<PcaDecompte> pcas, String dateDernierPmtPca)
            throws DecompteException {
        HashMap<String, PcaDecompte> mapPca = new HashMap<String, PcaDecompte>();
        if (pcas != null) {
            for (PcaDecompte pca : pcas) {
                int nbMonth = nbMonth(pca, dateDernierPmtPca);
                for (int i = 0; (i < nbMonth); i++) {
                    String dateMois = createDateKeyForMap(pca.getDateDebut(), i);
                    if (mapPca.containsKey(dateMois)) {
                        throw new DecompteException("The key '" + dateMois
                                + "' is already used that mean exist many pca for the same periode, pca:"
                                + pca.toString());
                    }
                    mapPca.put(dateMois, pca);
                }
            }
        }
        return mapPca;
    }

    @Override
    public int saveRemarqueSurDecompte(String idVersionDroit, String remarque) throws JadePersistenceException,
            JadeApplicationException {
        String replaced = remarque.replaceAll("&#44;", ",");
        if (replaced.length() > 1024) {
            return 0;
        }
        try {
            SimpleVersionDroit versionDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().read(
                    idVersionDroit);
            versionDroit.setRemarqueDecompte(replaced);
            PegasusImplServiceLocator.getSimpleVersionDroitService().update(versionDroit);
            return 1;
        } catch (JadePersistenceException e) {
            return -1;
        }
    }
}
