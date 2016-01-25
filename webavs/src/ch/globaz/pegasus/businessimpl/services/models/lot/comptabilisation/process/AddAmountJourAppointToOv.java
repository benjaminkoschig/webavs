package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

/**
 * Le but de cette class est d'ajouter le montant des jour d'appoint sur le bon ordre de versement
 * 
 * @author dma
 * 
 */
public class AddAmountJourAppointToOv {

    static void addAmountJoursAppointToOrdreVersement(SimpleOrdreVersement ovJourAppoint,
            SimpleOrdreVersement ordreVersement) {
        BigDecimal amountPresation = new BigDecimal(ordreVersement.getMontant());
        BigDecimal amountJoursAppoint = new BigDecimal(ovJourAppoint.getMontant());
        ordreVersement.setMontant(amountPresation.add(amountJoursAppoint).toString());
    }

    private Map<String, SimpleOrdreVersement> mapJourAppoint = new HashMap<String, SimpleOrdreVersement>();
    private Map<String, SimpleOrdreVersement> mapOv = new HashMap<String, SimpleOrdreVersement>();
    private List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();

    public AddAmountJourAppointToOv(List<SimpleOrdreVersement> ovs) {
        // Collections.copy(this.ovs, ovs);
        this.ovs = ovs;
        sortOvsByNumeroGroup();
        splitOvToSpecifiedMap();
    }

    /**
     * Ajout le montant des jours d'appoint sur les ordres de versements qui sont liée
     * 
     * @throws ComptabiliserLotException. Cette
     *             Exception arrive s'il existe un ov de type jour d'appoint et que l'on arrive pas a trouvé
     *             l'ov(restitution , bénéficiaire) liée.
     */
    public List<SimpleOrdreVersement> addAmountJourAppointOnMatchedOv() throws ComptabiliserLotException {
        for (Entry<String, SimpleOrdreVersement> ovJourAppoint : mapJourAppoint.entrySet()) {
            SimpleOrdreVersement ov = findOrdreVersementBoundToJourAppoint(ovJourAppoint.getValue());
            AddAmountJourAppointToOv.addAmountJoursAppointToOrdreVersement(ovJourAppoint.getValue(), ov);
        }
        return ovs;
    }

    SimpleOrdreVersement findOrdreVersementBoundToJourAppoint(SimpleOrdreVersement ovJourAppoint)
            throws ComptabiliserLotException {
        if (!mapOv.containsKey(ovJourAppoint.getIdPca())) {
            throw new ComptabiliserLotException("Any one ov bounded for this ovJourAppoint: "
                    + ovJourAppoint.toString());
        }
        return mapOv.get(ovJourAppoint.getIdPca());
    }

    public Map<String, SimpleOrdreVersement> getMapJourAppoint() {
        return mapJourAppoint;
    }

    public Map<String, SimpleOrdreVersement> getMapOv() {
        return mapOv;
    }

    private void sortOvsByNumeroGroup() {
        Collections.sort(ovs, new Comparator<SimpleOrdreVersement>() {
            @Override
            public int compare(SimpleOrdreVersement o1, SimpleOrdreVersement o2) {
                if ((o1.getNoGroupePeriode() != null) && (o2.getNoGroupePeriode() != null)) {
                    if (!o1.getNoGroupePeriode().equals(o2.getNoGroupePeriode())) {
                        return Integer.valueOf(o1.getNoGroupePeriode()).compareTo(
                                Integer.valueOf(o2.getNoGroupePeriode()));
                    } else {
                        return Integer.valueOf(o2.getIdPca()).compareTo(Integer.valueOf(o1.getIdPca()));
                    }
                }
                return 0;
            }
        });
    }

    /**
     * Il est possible d'avoir plusieurs ov qui pointe sur la même PCA. On vas seulement utiliser le premier ov car il
     * sont trié par le numéro de group qui correspond aux période des PCA. Le plus petit numéros correpond à la pca la
     * plus ancienne
     */
    private void splitOvToSpecifiedMap() {
        for (SimpleOrdreVersement ov : ovs) {
            if (OrdreVersementTypeResolver.isJoursAppoint(ov)) {
                if (!mapJourAppoint.containsKey(ov.getIdPca())) {
                    mapJourAppoint.put(ov.getIdPca(), ov);
                }
            } else if (OrdreVersementTypeResolver.isBeneficiaireOrRestitution(ov)) {
                if (!mapOv.containsKey(ov.getIdPca())) {
                    mapOv.put(ov.getIdPca(), ov);
                }
            }
        }
    }

}
