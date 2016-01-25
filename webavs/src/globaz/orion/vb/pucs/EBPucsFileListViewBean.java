package globaz.orion.vb.pucs;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.utils.EBDanUtils;
import globaz.orion.vb.EBAbstractListViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileComparator;
import ch.globaz.orion.business.models.pucs.PucsSearchCriteria;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.FindPucsSwissDec;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.xmlns.eb.dan.Dan;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;
import com.google.common.base.Splitter;

public class EBPucsFileListViewBean extends EBAbstractListViewBean {

    private String dateSoumission = "";
    private String forAnnee = "";
    private String likeAffilie = "";
    private String orderby = "";
    private List<PucsEntrySummary> pucsFiles = null;
    private List<PucsFile> pucsFilesFinal = new ArrayList<PucsFile>();
    private PucsSearchCriteria search = null;
    private String type = "";

    public EBPucsFileListViewBean() {
        search = new PucsSearchCriteria();
    }

    @Override
    public void find() throws Exception {
        DeclarationSalaireProvenance provenance = null;
        if (!JadeStringUtil.isBlank(type)) {
            provenance = DeclarationSalaireProvenance.fromValue(type);
        }
        // Recherche des DS ouvert et qui ont un pucs file
        Collection<String> listDS = findDS();
        List<PucsEntrySummary> pucsFileTemp = null;
        List<Dan> danFileTemp = null;
        List<PucsFile> pucsSwissDec = new ArrayList<PucsFile>();
        // Recherche des pucsfile dans ebusiness
        if (provenance == null || provenance.isPucs()) {
            pucsFileTemp = PucsServiceImpl.listPucsFile(type, 0, likeAffilie, forAnnee, dateSoumission,
                    (BSession) getISession());
        }
        if (provenance == null || provenance.isDan()) {
            Integer dateSoumInt = null;
            if ((dateSoumission != null) && (dateSoumission.length() == 8)) {
                dateSoumInt = Integer.parseInt(JACalendar.format(dateSoumission, JACalendar.FORMAT_YYYYMMDD));
            }
            danFileTemp = DanServiceImpl.listDanFile(likeAffilie, dateSoumInt, (BSession) getISession());
        }
        if (provenance == null || provenance.isSwissDec()) {
            FindPucsSwissDec swissDec = new FindPucsSwissDec((BSession) getISession());
            pucsSwissDec = swissDec.loadPucsSwissDecATraiter();
        }

        List<PucsFile> pucsFilesMerged = mergeList(listDS, pucsFileTemp, danFileTemp, pucsSwissDec);
        List<PucsFile> pucsFilesFiltred = new ArrayList<PucsFile>();
        for (PucsFile pucsFile : pucsFilesMerged) {
            if (forAnnee != null && forAnnee.trim().length() > 0 && dateSoumission != null
                    && dateSoumission.trim().length() > 0) {
                if (pucsFile.getAnneeDeclaration().equals(forAnnee)
                        && pucsFile.getDateDeReception().equals(dateSoumission)) {
                    pucsFilesFiltred.add(pucsFile);
                }
            } else if (forAnnee != null && forAnnee.trim().length() > 0) {
                if (pucsFile.getAnneeDeclaration().equals(forAnnee)) {
                    pucsFilesFiltred.add(pucsFile);
                }
            } else if (dateSoumission != null && dateSoumission.trim().length() > 0) {
                if (pucsFile.getDateDeReception().equals(dateSoumission)) {
                    pucsFilesFiltred.add(pucsFile);
                }
            } else {
                pucsFilesFiltred.add(pucsFile);
            }
        }
        for (PucsFile pucsFile : pucsFilesFiltred) {
            if (likeAffilie != null && likeAffilie.trim().length() > 0) {
                if (pucsFile.getNumeroAffilie().contains(likeAffilie)) {
                    pucsFilesFinal.add(pucsFile);
                }
            } else {
                pucsFilesFinal.add(pucsFile);
            }
        }

        sortByFusionable();
    }

    private void sortByFusionable() {
        final Map<String, List<PucsFile>> mapPubcByNumAffilie = new HashMap<String, List<PucsFile>>();
        for (PucsFile pucs : pucsFilesFinal) {

            String key = pucs.getNumeroAffilie().trim();

            if (!mapPubcByNumAffilie.containsKey(key)) {
                mapPubcByNumAffilie.put(key, new ArrayList<PucsFile>());
            }
            mapPubcByNumAffilie.get(key).add(pucs);
        }

        Collections.sort(pucsFilesFinal, new Comparator<PucsFile>() {
            @Override
            public int compare(PucsFile o1, PucsFile o2) {

                int siz1 = (mapPubcByNumAffilie.get(o1.getNumeroAffilie().trim())).size();
                int siz2 = (mapPubcByNumAffilie.get(o2.getNumeroAffilie().trim())).size();

                int compare = siz2 - siz1;

                if (compare == 0) {
                    return o1.getNumeroAffilie().trim().compareTo(o2.getNumeroAffilie().trim());
                }
                return compare;
            }
        });
    }

    public Collection<String> findDS() throws Exception {

        Collection<String> listDs = new ArrayList<String>();

        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setSession((BSession) getISession());
        manager.setForEtat(DSDeclarationViewBean.CS_OUVERT);
        manager.setForIdPucsFileNotEmpty(true);

        manager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.getSize(); i++) {
            DSDeclarationViewBean ds = (DSDeclarationViewBean) manager.getEntity(i);
            List<String> ids = Splitter.on(";").trimResults().splitToList(ds.getIdPucsFile());
            for (String id : ids) {
                listDs.add(id + "#" + ds.getProvenance());
            }

        }

        return listDs;
    }

    @Override
    public BIPersistentObject get(int idx) {
        return (pucsFilesFinal != null) && (pucsFilesFinal.size() > idx) ? new EBPucsFileViewBean(
                pucsFilesFinal.get(idx)) : new EBPucsFileViewBean();
    }

    public String getDateSoumission() {
        return dateSoumission;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getLikeAffilie() {
        return likeAffilie;
    }

    public String getOrderby() {
        return orderby;
    }

    public List<PucsEntrySummary> getPucsFiles() {
        return pucsFiles;
    }

    public PucsSearchCriteria getSearch() {
        return search;
    }

    @Override
    public int getSize() {
        return pucsFilesFinal == null ? 0 : pucsFilesFinal.size();
    }

    public String getType() {
        return type;
    }

    private List<PucsFile> mergeList(Collection<String> listDS, List<PucsEntrySummary> pucsFileTemp,
            List<Dan> danFileTemp, List<PucsFile> pucsSwissDec) {

        if (((pucsFileTemp == null) || (pucsFileTemp.size() == 0))
                && ((danFileTemp == null) || (danFileTemp.size() == 0)) && pucsSwissDec.isEmpty()) {
            return new ArrayList<PucsFile>();
        }

        ArrayList<PucsFile> mergeList = new ArrayList<PucsFile>();
        if (((pucsFileTemp != null) && (!pucsFileTemp.isEmpty()))) {
            for (PucsEntrySummary file : pucsFileTemp) {
                if (!listDS.contains(file.getIdPucsEntry() + "#" + DeclarationSalaireProvenance.PUCS.getValue())) {
                    mergeList.add(EBDanUtils.mapPucsfile(file));
                }
            }
        }
        if (((danFileTemp != null) && (danFileTemp.size() != 0))) {
            for (Dan file : danFileTemp) {
                if (!listDS.contains(file.getIdDan() + "#" + DeclarationSalaireProvenance.DAN.getValue())) {
                    mergeList.add(EBDanUtils.mapDanfile(file));
                }
            }
        }

        if (((pucsSwissDec != null) && (pucsSwissDec.size() != 0))) {
            for (PucsFile file : pucsSwissDec) {
                // if (!listDS.contains(file.getId() + "#" + DeclarationSalaireProvenance.SWISS_DEC.getValue())) {
                mergeList.add(file);
                // }
            }
        }

        PucsFileComparator comp = new PucsFileComparator();
        comp.setSession((BSession) getISession());
        Collections.sort(mergeList, comp);
        return mergeList;
    }

    public void setDateSoumission(String dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setLikeAffilie(String likeAffilie) {
        this.likeAffilie = likeAffilie;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setPucsFiles(List<PucsEntrySummary> pucsFiles) {
        this.pucsFiles = pucsFiles;
    }

    public void setSearch(PucsSearchCriteria search) {
        this.search = search;
    }

    public void setType(String type) {
        this.type = type;
    }

}
