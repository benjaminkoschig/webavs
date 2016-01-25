package globaz.cygnus.db.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFQdJointDossierJointTiersJointDemandeViewBean;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Regroupe les informations nécessaires à l'affichage d'une Qd
 * 
 * @author JJE
 * 
 */
public class RFQdJointDossierJointTiersJointDemandeRCListFormatter {

    int i = 0;
    private Iterator<RFQdJointDossierJointTiersJointDemandeViewBean> iterator = null;
    private RFQdJointDossierJointTiersJointDemandeViewBean lineVb = null;
    private String[] listNssForLinkGedGroupBy = null;
    private Comparator<String[]> myIdsComparator = null;

    int size = 0;

    public RFQdJointDossierJointTiersJointDemandeRCListFormatter(
            Iterator<RFQdJointDossierJointTiersJointDemandeViewBean> iterator) {
        this.iterator = iterator;

        myIdsComparator = new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.decode(o1[0]).compareTo(Integer.decode(o2[0]));
            }
        };

    }

    public int getI() {
        return i;
    }

    public String[] getListNssForLinkGedGroupBy() {
        return listNssForLinkGedGroupBy;
    }

    /**
     * Complète si nécessaire et retourne l'élement suivant de la liste
     * 
     * @return L'élement si la liste en contient encore un, sinon <code>null</code>
     * @throws JAException
     */
    public RFQdJointDossierJointTiersJointDemandeViewBean getNextElement() throws Exception {

        RFQdJointDossierJointTiersJointDemandeViewBean linePrecedenteVb = null;

        StringBuffer detailAssureStrb = null;
        StringBuffer periodePetiteQdStrb = null;
        List<String> idsAssureList = null;
        Map<String, String[]> idsPeriodeMap = null;
        Map<String, String[]> idsAugmentationQdMap = null;
        Map<String, String[]> idsSoldeChargeQdMap = null;

        boolean premierPassage = true;
        String idQdCourante = "";

        while ((iterator != null) && (i <= getSize())) {
            if (lineVb == null) {
                lineVb = iterator.next();
                i = 1;
            }

            if ((lineVb != null) && (idQdCourante.equals(lineVb.getIdQd()) || premierPassage)) {

                if (premierPassage) {
                    premierPassage = false;
                    detailAssureStrb = new StringBuffer();
                    periodePetiteQdStrb = new StringBuffer();
                    idsAssureList = new ArrayList<String>();
                    idsPeriodeMap = new HashMap<String, String[]>();
                    idsAugmentationQdMap = new HashMap<String, String[]>();
                    idsSoldeChargeQdMap = new HashMap<String, String[]>();
                    listNssForLinkGedGroupBy = new String[getSize()];
                }

                idQdCourante = lineVb.getIdQd();
                // Recherche (par famille) des assurés concernés par la Qd
                if (!idsAssureList.contains(lineVb.getIdTiers())) {
                    if (detailAssureStrb.length() > 0) {
                        detailAssureStrb = detailAssureStrb.append("<Br>" + lineVb.getDetailAssure());
                        listNssForLinkGedGroupBy[i - 1] = lineVb.getNss();
                    } else {
                        detailAssureStrb = detailAssureStrb.append(lineVb.getDetailAssure());
                        listNssForLinkGedGroupBy[i - 1] = lineVb.getNss();
                    }
                    idsAssureList.add(lineVb.getIdTiers());
                }

                if (!JadeStringUtil.isBlankOrZero(lineVb.getIdAugmentationQd())
                        && !idsAugmentationQdMap.containsKey(lineVb.getIdAugmentationQd())) {
                    idsAugmentationQdMap.put(lineVb.getIdAugmentationQd(),
                            new String[] { lineVb.getIdAugmentationQd(), lineVb.getCsTypeModificationAugmentationQd(),
                                    lineVb.getIdFamilleAugmentationDeQd(), lineVb.getAugmentationDeQd() });
                }

                if (!JadeStringUtil.isBlankOrZero(lineVb.getIdSoldeCharge())
                        && !idsSoldeChargeQdMap.containsKey(lineVb.getIdSoldeCharge())) {
                    idsSoldeChargeQdMap.put(
                            lineVb.getIdSoldeCharge(),
                            new String[] { lineVb.getIdSoldeCharge(), lineVb.getCsTypeModificationSoldeCharge(),
                                    lineVb.getIdFamilleSoldeDeCharge(), lineVb.getSoldeDeCharge() });
                }

                if (lineVb.getCsGenreQd().equals(IRFQd.CS_GRANDE_QD)) {
                    if (!JadeStringUtil.isBlankOrZero(lineVb.getIdPeriodeGrandeQd())
                            && !idsPeriodeMap.containsKey(lineVb.getIdPeriodeGrandeQd())) {
                        idsPeriodeMap.put(
                                lineVb.getIdPeriodeGrandeQd(),
                                new String[] { lineVb.getIdPeriodeGrandeQd(), lineVb.getCsTypeModificationPeriode(),
                                        lineVb.getIdFamillePeriodeGrandeQd(), lineVb.getDateDebutGrandeQd(),
                                        lineVb.getDateFinGrandeQd() });
                    }
                } else {
                    if (periodePetiteQdStrb.length() == 0) {
                        periodePetiteQdStrb.append(lineVb.getDateDebutPetiteQd() + " - " + lineVb.getDateFinPetiteQd());
                    }
                }

                linePrecedenteVb = lineVb;
                if (iterator.hasNext()) {
                    lineVb = iterator.next();
                    i++;
                } else {
                    setLineViewBeanGroupBy(linePrecedenteVb, detailAssureStrb, idsPeriodeMap, idsAugmentationQdMap,
                            idsSoldeChargeQdMap, periodePetiteQdStrb);
                    i++;
                    return linePrecedenteVb;
                }

            } else {
                setLineViewBeanGroupBy(linePrecedenteVb, detailAssureStrb, idsPeriodeMap, idsAugmentationQdMap,
                        idsSoldeChargeQdMap, periodePetiteQdStrb);
                return linePrecedenteVb;
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    /**
     * 
     * Vérifie si il existe un élément plus grand de la meme famille dans une table historisée d'une Qd
     * 
     * @param ids
     * @param idCourant
     * @return
     */
    private boolean hasNextId(List<String[]> ids, String[] idCourant) {
        for (String[] idList : ids) {
            if ((Integer.decode(idList[0]).intValue() > Integer.decode(idCourant[0]).intValue())
                    && (Integer.decode(idList[2]).intValue() == Integer.decode(idCourant[2]).intValue())) {
                return true;
            }
        }
        return false;
    }

    public void setI(int i) {
        this.i = i;
    }

    private void setLineViewBeanGroupBy(RFQdJointDossierJointTiersJointDemandeViewBean lineVb,
            StringBuffer detailAssureStrb, Map<String, String[]> idsPeriodeMap,
            Map<String, String[]> idsAugmentationQdMap, Map<String, String[]> idsSoldeChargeQdMap,
            StringBuffer periodePetiteQdStrb) {

        lineVb.setDetailAssureGroupBy(detailAssureStrb.toString());

        StringBuffer periodesQdStrb = null;
        if (periodePetiteQdStrb.length() > 0) {
            periodesQdStrb = periodePetiteQdStrb;
        } else {
            periodesQdStrb = new StringBuffer();
            List<String[]> idsPeriodelist = new ArrayList<String[]>(idsPeriodeMap.values());
            Collections.sort(idsPeriodelist, myIdsComparator);
            for (String[] periodeCourrante : idsPeriodelist) {
                if (!periodeCourrante[1].equals(IRFQd.CS_SUPPRESSION) && !hasNextId(idsPeriodelist, periodeCourrante)) {
                    if (periodesQdStrb.length() > 0) {
                        periodesQdStrb = periodesQdStrb.append("<Br>" + periodeCourrante[3] + " - "
                                + periodeCourrante[4]);
                    } else {
                        periodesQdStrb = periodesQdStrb.append(periodeCourrante[3] + " - " + periodeCourrante[4]);
                    }
                }
            }
        }
        lineVb.setPeriodesGrandeQdGroupBy(periodesQdStrb.toString());

        BigDecimal augmentationQdBigDec = new BigDecimal("0");
        List<String[]> idsAugmentationQdList = new ArrayList<String[]>(idsAugmentationQdMap.values());
        Collections.sort(idsAugmentationQdList, myIdsComparator);
        for (String[] AugmentationCourrante : idsAugmentationQdList) {
            if (!AugmentationCourrante[1].equals(IRFQd.CS_SUPPRESSION)
                    && !hasNextId(idsAugmentationQdList, AugmentationCourrante)) {
                augmentationQdBigDec = augmentationQdBigDec.add(new BigDecimal(AugmentationCourrante[3]));
            }
        }

        BigDecimal soldeChargeQdBigDec = new BigDecimal("0");
        List<String[]> idsSoldeChargeQdList = new ArrayList<String[]>(idsSoldeChargeQdMap.values());
        Collections.sort(idsSoldeChargeQdList, myIdsComparator);
        for (String[] SoldeChargeCourrant : idsSoldeChargeQdList) {
            if (!SoldeChargeCourrant[1].equals(IRFQd.CS_SUPPRESSION)
                    && !hasNextId(idsSoldeChargeQdList, SoldeChargeCourrant)) {
                soldeChargeQdBigDec = soldeChargeQdBigDec.add(new BigDecimal(SoldeChargeCourrant[3]));
            }
        }

        lineVb.setMntResiduelGroupBy(RFUtils.getMntResiduel(lineVb.getLimiteAnnuelle(),
                JadeStringUtil.isBlankOrZero(augmentationQdBigDec.toString()) ? "0" : augmentationQdBigDec.toString(),
                JadeStringUtil.isBlankOrZero(soldeChargeQdBigDec.toString()) ? "0" : soldeChargeQdBigDec.toString(),
                lineVb.getMontantChargeRfm()));
    }

    public void setSize(int size) {
        this.size = size;
    }

}
