package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.orion.utils.EBDanUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

public class EBImportPucsDanProcess extends ProcessItemsHandlerJadeJob<PucsItem> {
    public static final String KEY = "orion.pucs.import.danPucs";
    private transient List<PucsFile> pucsFiles;

    @Override
    public String getKey() {
        return KEY;
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
            AFAffiliation affiliation = findAffiation(pucsFile);

            list.add(new PucsItem(pucsFile, affiliation, getSession(), getJobInfos().getIdJob()));
        }
        return list;
    }

    private AFAffiliation findAffiation(PucsFile pucsFile) {
        return AFAffiliationServices.getAffiliationParitaireByNumero(pucsFile.getNumeroAffilie(),
                pucsFile.getAnneeDeclaration(), getSession());
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
            throw new RuntimeException(e);
        }
        return danFileTemp;
    }

    private List<PucsEntrySummary> loadPucs(BSession session) {
        // Recherche des pucsfile dans ebusiness
        List<PucsEntrySummary> pucsFileTemp = new ArrayList<PucsEntrySummary>();
        try {
            pucsFileTemp = PucsServiceImpl.listPucsFile(0, "", "", "", session);
        } catch (OrionPucsException e) {
            throw new RuntimeException(e);
        }
        return pucsFileTemp;
    }

}
