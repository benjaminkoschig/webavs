package globaz.orion.vb.pucs;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.orion.vb.EBAbstractListViewBeanPagination;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileDefTable;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.orion.db.EBPucsFileManager;
import ch.globaz.orion.service.EBPucsFileService;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;
import org.springframework.web.servlet.ModelAndView;

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

        // Transformation des date"String" en date"LocalDate" pour comparaison
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Ajout des conditions sur les dates

        if (!Objects.isNull(manager.getForDateDebut()) && (manager.getForDateDebut() != "")) {
            LocalDate dateDebut = LocalDate.parse(manager.getForDateDebut(), format2);
            LocalDate dateFin = dateDebut;

            if (!Objects.isNull(manager.getForDateFin()) && (manager.getForDateFin() != "")) {
                dateFin = LocalDate.parse(manager.getForDateFin(), format2);
            }

            // La date de début ne peut pas être aprés la date de fin
            if (dateDebut.isAfter(dateFin)) {
                throw new Exception("La date de fin ne peut pas être antérieur à la date de début");
            }


        }


        if ("HANDLING_USER".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy("UPPER(" +EBPucsFileDefTable.HANDLING_USER.getColumnName()+ ") ASC, "+ EBPucsFileDefTable.NUMERO_AFFILIE.getColumnName());
        } else if ("DATE_RECEPTION".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy(EBPucsFileDefTable.DATE_RECEPTION.getColumnName() + " ASC");
        } else if ("NOM_AFFILIE".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy(EBPucsFileDefTable.NOM_AFFILIE.getColumnName());
        } else if ("NUMERO_AFFILIE".equalsIgnoreCase(orderBy)) {
            manager.setOrderBy(EBPucsFileDefTable.NUMERO_AFFILIE.getColumnName());
        }


        manager.find(BManager.SIZE_USEDEFAULT);


        perpareList();
    }

    public void perpareList() {
        List<EBPucsFileEntity> list = manager.toList();
        pucsFilesFinal = EBPucsFileService.entitiesToPucsFile(list);



        mapAffiliation = findAffiliations(list);
        mapNumAffiliationParticularite = resolveParticularites(mapAffiliation);

        if (orderBy == null || orderBy.isEmpty()) {
            sortByFusionable();
        }

        // Liste des Users mise dans une List triée par ordre alphabétique
        for ( PucsFile pucsFile : pucsFilesFinal) {
            manager.setUsers(pucsFile.getHandlingUser().toUpperCase());
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

    public String getOrderBy() {
        return orderBy;
    }

    public List<PucsEntrySummary> getPucsFiles() {
        return pucsFiles;
    }

    public void setDateSoumission(String dateSoumission) {
        manager.setForDateSoumission(dateSoumission);
    }

    public void setStatut(String statut) {
        manager.setStatut(statut);
    }

    public void setLikeAffilie(String likeAffilie) {
        manager.setLikeAffilie(likeAffilie);
    }

    public void setType(String type) {
        manager.setForProvenance(DeclarationSalaireProvenance.fromValueWithOutException(type));
    }

    public String getTypeValue() {
        return manager.getForProvenance().getValue();
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

    public void setForDateDebut(String forDateDebut) {
        manager.setForDateDebut(forDateDebut);
    }

    public String getForDateDebut() {
        return manager.getForDateDebut();
    }

    public void setforDateFin(String forDateFin) {
        manager.setForDateFin(forDateFin);
    }

    public String getForDateFin() {
        return manager.getForDateFin();
    }

    public void setForUser(String forUser) {
        manager.setForUser(forUser);
    }

    public String getForUser() {
        return manager.getForUser();
    }

    public List getUsers() {
        return manager.getUsers();
    }

    public Map getUsersJson() {
        return manager.getUsersJson();
    }

    public String getForTypeDeclaration(){
        return manager.getForTypeDeclaration();
    }

    public String getStatut(){
        return manager.getStatut();
    }

    public String getLikeAffilie(){
        return manager.getLikeAffilie();
    }

    public String getFullText(){
        return manager.getFullText();
    }

    public Boolean getIsSelectionnerAllHandling() {
        return manager.getIsSelectionnerAllHandling();
    }

    public void setIsSelectionnerAllHandling(Boolean isSelectionnerAllHandling) {
        this.manager.setIsSelectionnerAllHandling(isSelectionnerAllHandling);
    }

    public Boolean getIsSelectionnerToHandle() {
        return  manager.getIsSelectionnerToHandle();
    }

    public void setIsSelectionnerToHandle(Boolean isSelectionnerToHandle) {
        this.manager.setIsSelectionnerToHandle(isSelectionnerToHandle);
    }
}
