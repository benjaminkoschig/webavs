package globaz.orion.vb.pucs;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.orion.vb.EBAbstractListViewBeanPagination;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileDefTable;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.orion.db.EBPucsFileManager;
import ch.globaz.orion.service.EBPucsFileService;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;

public class EBPucsFileListViewBean extends EBAbstractListViewBeanPagination {

    private String orderBy;
    private List<PucsEntrySummary> pucsFiles = null;
    private List<PucsFile> pucsFilesFinal = new ArrayList<PucsFile>();
    private Map<String, List<String>> mapNumAffiliationParticularite;
    private Map<String, AFAffiliation> mapAffiliation;
    private EBPucsFileManager manager = new EBPucsFileManager();

    @Override
    public BManager getManager() {
        return manager;
    }

    @Override
    public void findNext() throws Exception {
        manager.findNext();
        perpareList();
    }

    @Override
    public void findPrev() throws Exception {
        manager.findPrev();
        perpareList();
    }

    @Override
    public void find() throws Exception {
        if ("DATE_RECEPTION".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy(EBPucsFileDefTable.DATE_RECEPTION.getColumnName() + " ASC");
        } else if ("NOM_AFFILIE".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy(EBPucsFileDefTable.NOM_AFFILIE.getColumnName());
        } else if ("NUMERO_AFFILIE".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy(EBPucsFileDefTable.NUMERO_AFFILIE.getColumnName());
        }
        manager.find(BManager.SIZE_USEDEFAULT);
        perpareList();
    }

    private void perpareList() {
        List<EBPucsFileEntity> list = manager.toList();
        pucsFilesFinal = EBPucsFileService.entitiesToPucsFile(list);

        mapAffiliation = findAffiliations(list);
        mapNumAffiliationParticularite = resolveParticularites(mapAffiliation);

        if (orderBy == null || orderBy.isEmpty()) {
            sortByFusionable();
        }
    }

    private Map<String, AFAffiliation> findAffiliations(List<EBPucsFileEntity> list) {
        Set<String> ids = new HashSet<String>();

        for (EBPucsFileEntity entity : list) {
            ids.add(entity.getIdAffiliation());
        }

        return AFAffiliationServices
                .searchAffiliationByIdsAndGroupById(ids, BSessionUtil.getSessionFromThreadContext());

    }

    private Map<String, List<String>> resolveParticularites(Map<String, AFAffiliation> mapAff) {

        Map<String, List<String>> particularites = AFParticulariteAffiliation.findParticularites(
                BSessionUtil.getSessionFromThreadContext(), mapAffiliation.keySet(),
                CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL, CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE);
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        for (Entry<String, List<String>> entry : particularites.entrySet()) {
            AFAffiliation afAffiliation = mapAff.get(entry.getKey());
            if (afAffiliation != null) {
                map.put(afAffiliation.getAffiliationId(), entry.getValue());
            }
        }
        return map;
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

    @Override
    public BIPersistentObject get(int idx) {
        PucsFile pucsFile = pucsFilesFinal.get(idx);
        List<String> particularites = mapNumAffiliationParticularite.get(pucsFile.getIdAffiliation());
        AFAffiliation afAffiliation = mapAffiliation.get(pucsFile.getIdAffiliation());

        return pucsFilesFinal.size() > idx ? new EBPucsFileViewBean(pucsFile, particularites, afAffiliation)
                : new EBPucsFileViewBean();
    }

    @Override
    public int getSize() {
        return pucsFilesFinal == null ? 0 : pucsFilesFinal.size();
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<PucsEntrySummary> getPucsFiles() {
        return pucsFiles;
    }

    public void setDateSoumission(String dateSoumission) {
        manager.setForDateSoumission(dateSoumission);
    }

    public void setStatut(String statut) {
        manager.setForStatut(statut);
    }

    public void setLikeAffilie(String likeAffilie) {
        manager.setLikeAffilie(likeAffilie);
    }

    public void setType(String type) {
        manager.setForProvenance(DeclarationSalaireProvenance.fromValueWithOutException(type));
    }

    public void setFullText(String fullText) {
        manager.setFullText(fullText);
    }

    public void setPucsFiles(List<PucsEntrySummary> pucsFiles) {
        this.pucsFiles = pucsFiles;
    }

    public void setforTypeDeclaration(String typeDeclaration) {
        manager.setForTypeDeclaration(typeDeclaration);
    }

}
