package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.orion.utils.EBDanUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.orion.business.exceptions.OrionPucsException;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileComparator;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.EtatSwissDecPucsFile;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.xmlns.eb.dan.Dan;
import ch.globaz.xmlns.eb.dan.EBDanException_Exception;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;

public class EBImportPucsDan extends ImportPucsPorcess {

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
    public List<PucsFile> loadPucs() throws FileNotFoundException {
        List<PucsEntrySummary> pucsFilesEbu = loadPucs(getSession());
        List<Dan> danFile = loadDan(getSession());
        List<PucsFile> pucsFiles = mergeList(pucsFilesEbu, danFile);
        for (PucsFile pucsFile : pucsFiles) {
            String filePath = PucsServiceImpl.retrieveFile(pucsFile.getId(), pucsFile.getProvenance(),
                    EtatSwissDecPucsFile.A_TRAITER);
            File file = new File(filePath);
            pucsFile.setFile(file);
        }
        return pucsFiles;
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
