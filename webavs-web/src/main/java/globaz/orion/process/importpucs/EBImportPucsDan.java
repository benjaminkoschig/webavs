package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.orion.utils.EBDanUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.process.byitem.ProcessItemsHandlerJadeJob;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.exceptions.OrionPucsException;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileComparator;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.xmlns.eb.dan.Dan;
import ch.globaz.xmlns.eb.dan.EBDanException_Exception;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;

public class EBImportPucsDan extends ProcessItemsHandlerJadeJob<PucsItem> {
    private Map<String, List<AFAffiliation>> affiliations;
    protected transient List<PucsFile> pucsFiles;

    @Override
    public String getKey() {
        return "orion.pucs.import.danPucs";
    }

    @Override
    public String getDescription() {
        return "Process permettant l'importation en DB des fichiers pucs";
    }

    @Override
    public String getName() {
        return "PROCESS_IMPORT_PUCSINDB_PUCS_DAN_NAME";
    }

    @Override
    public void before() {
        try {
            BSessionUtil.initContext(getSession(), this);

            List<PucsEntrySummary> pucsFilesEbu = loadPucs(getSession());
            List<Dan> danFile = loadDan(getSession());
            pucsFiles = mergeList(pucsFilesEbu, danFile);
            affiliations = findAffiliations(pucsFiles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void after() {
        sendMailIfHasError();
        BSessionUtil.stopUsingContext(this);
    }

    @Override
    public List<PucsItem> resolveItems() {
        List<PucsItem> list = new ArrayList<PucsItem>();

        for (PucsFile pucsFile : pucsFiles) {
            pucsFile.setCurrentStatus(EtatPucsFile.A_TRAITER);
            String filePath = PucsServiceImpl.retrieveFile(pucsFile.getFilename(), pucsFile.getProvenance());
            File file = new File(filePath);
            pucsFile.setFile(file);
            list.add(new PucsItem(pucsFile, affiliations.get(pucsFile.getNumeroAffilie()), getSession(), getJobInfos()
                    .getIdJob()));
        }
        return list;
    }

    private Map<String, List<AFAffiliation>> findAffiliations(List<PucsFile> list) {
        Set<String> numAffiliations = new HashSet<String>();

        for (PucsFile pucsFile : list) {
            numAffiliations.add(pucsFile.getNumeroAffilie());
        }

        List<AFAffiliation> listAffiliations = AFAffiliationServices.searchAffiliationByNumeros(numAffiliations,
                getSession());

        Map<String, List<AFAffiliation>> map = new HashMap<String, List<AFAffiliation>>();

        for (AFAffiliation afAffiliation : listAffiliations) {
            if (!map.containsKey(afAffiliation.getAffilieNumero())) {
                map.put(afAffiliation.getAffilieNumero(), new ArrayList<AFAffiliation>());
            }
            map.get(afAffiliation.getAffilieNumero()).add(afAffiliation);
        }
        return map;
    }

    private List<PucsFile> mergeList(List<PucsEntrySummary> pucsFile, List<Dan> danFile) {

        ArrayList<PucsFile> mergeList = new ArrayList<PucsFile>();
        if (pucsFile != null) {
            for (PucsEntrySummary file : pucsFile) {
                mergeList.add(EBDanUtils.mapPucsfile(file));
            }
        }
        if (danFile != null) {
            for (Dan file : danFile) {
                mergeList.add(EBDanUtils.mapDanfile(file));
            }
        }
        PucsFileComparator comp = new PucsFileComparator();
        comp.setSession(BSessionUtil.getSessionFromThreadContext());
        Collections.sort(mergeList, comp);
        return mergeList;
    }

    private List<Dan> loadDan(BSession session) {
        List<Dan> danFileTemp = new ArrayList<Dan>();
        try {
            danFileTemp = DanServiceImpl.listDanFile("", null, session);
        } catch (EBDanException_Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return danFileTemp;
    }

    private List<PucsEntrySummary> loadPucs(BSession session) {
        // Recherche des pucsfile dans ebusiness
        List<PucsEntrySummary> pucsFileTemp = new ArrayList<PucsEntrySummary>();
        try {
            pucsFileTemp = PucsServiceImpl.listPucsFile(0, "", "", "", session);
        } catch (OrionPucsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pucsFileTemp;
    }

}
