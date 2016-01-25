package globaz.cygnus.db.decisions;

import globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean;
import globaz.globall.util.JAException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class RFDecisionJointTiersRcListFormatter {

    int i = 0;
    private Iterator<RFDecisionJointTiersViewBean> iterator = null;
    private RFDecisionJointTiersViewBean lineVb = null;
    private String[] listNssForLinkGedGroupBy = null;
    private Comparator<String[]> myIdsComparator = null;

    int size = 0;

    public RFDecisionJointTiersRcListFormatter(Iterator<RFDecisionJointTiersViewBean> iterator) {
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
    public RFDecisionJointTiersViewBean getNextElement() throws Exception {

        RFDecisionJointTiersViewBean linePrecedenteVb = null;

        StringBuffer detailAssureStrb = null;
        List<String> idsAssureList = null;

        boolean premierPassage = true;
        String idDecisionCourante = "";

        while ((iterator != null) && (i <= getSize())) {
            if (lineVb == null) {
                lineVb = iterator.next();
                i = 1;
            }

            if ((lineVb != null) && (idDecisionCourante.equals(lineVb.getIdDecision()) || premierPassage)) {

                if (premierPassage) {
                    premierPassage = false;
                    detailAssureStrb = new StringBuffer();
                    idsAssureList = new ArrayList<String>();
                    listNssForLinkGedGroupBy = new String[getSize()];
                }

                idDecisionCourante = lineVb.getIdDecision();
                // Recherche (par famille) des assurés concernés par la Qd
                if (!idsAssureList.contains(lineVb.getIdTiers())) {
                    if (detailAssureStrb.length() > 0) {
                        detailAssureStrb = detailAssureStrb.append("<Br>" + lineVb.getDetailRequerant());
                        listNssForLinkGedGroupBy[i - 1] = lineVb.getNss();
                    } else {
                        detailAssureStrb = detailAssureStrb.append(lineVb.getDetailRequerant());
                        listNssForLinkGedGroupBy[i - 1] = lineVb.getNss();
                    }
                    idsAssureList.add(lineVb.getIdTiers());
                }

                linePrecedenteVb = lineVb;
                if (iterator.hasNext()) {
                    lineVb = iterator.next();
                    i++;
                } else {
                    setLineViewBeanGroupBy(linePrecedenteVb, detailAssureStrb);
                    i++;
                    return linePrecedenteVb;
                }

            } else {
                setLineViewBeanGroupBy(linePrecedenteVb, detailAssureStrb);
                return linePrecedenteVb;
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public void setI(int i) {
        this.i = i;
    }

    private void setLineViewBeanGroupBy(RFDecisionJointTiersViewBean lineVb, StringBuffer detailAssureStrb) {
        lineVb.setDetailAssureGroupBy(detailAssureStrb.toString());
    }

    public void setSize(int size) {
        this.size = size;
    }

}
