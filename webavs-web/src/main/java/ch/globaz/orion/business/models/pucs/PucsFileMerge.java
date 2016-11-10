package ch.globaz.orion.business.models.pucs;

import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.orion.businessimpl.services.merge.MergePucs;
import ch.globaz.orion.service.EBPucsFileService;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class PucsFileMerge {
    private PucsFile pucsFile = new PucsFile();
    private List<PucsFile> pucsFileToMergded = new ArrayList<PucsFile>();
    private final String workDirectory;
    private boolean isMerged = false;
    private Map<String, String> filesRetrieved = new HashMap<String, String>();
    private transient ElementsDomParser domParser;

    public ElementsDomParser getDomParser() {
        return domParser;
    }

    PucsFileMerge(List<PucsFile> pucsFileToMergded, String workDirectory) {
        this.pucsFileToMergded = pucsFileToMergded;
        this.workDirectory = workDirectory;
    }

    PucsFileMerge(PucsFile pucsFile, String workDirectory) {
        this.pucsFile = pucsFile;
        this.workDirectory = workDirectory;
    }

    public PucsFile getPucsFile() {
        return pucsFile;
    }

    public List<PucsFile> getPucsFileToMergded() {
        return pucsFileToMergded;
    }

    @Override
    public String toString() {
        return "PucsFileMerge [pucsFile=" + pucsFile + ", pucsFileToMergded=" + pucsFileToMergded + "]";
    }

    public static List<PucsFileMerge> build(List<PucsFile> pucsEntrys, Map<String, List<String>> pucsToMerge,
            String workDirectory) {
        Map<String, PucsFile> mapPucsFile = new HashMap<String, PucsFile>();
        List<PucsFileMerge> pucsFileMerges = new ArrayList<PucsFileMerge>();

        for (PucsFile pucsFile : pucsEntrys) {
            mapPucsFile.put(pucsFile.getIdDb(), pucsFile);
        }

        for (Entry<String, List<String>> entry : pucsToMerge.entrySet()) {
            List<PucsFile> pucsFiles = new ArrayList<PucsFile>();
            for (String id : entry.getValue()) {
                PucsFile pucsFile = mapPucsFile.get(id);
                pucsFiles.add(pucsFile);
                pucsEntrys.remove(pucsFile);
            }
            pucsFileMerges.add(new PucsFileMerge(pucsFiles, workDirectory));
        }

        for (PucsFile pucsFile : pucsEntrys) {
            pucsFileMerges.add(new PucsFileMerge(pucsFile, workDirectory));
        }

        return pucsFileMerges;
    }

    public PucsFile retriveFileAndMergeIfNeeded(BSession session, boolean isSimulation)
            throws JadeApplicationServiceNotAvailableException, IOException, Exception {

        Checkers.checkNotNull(session, "session");
        String numeroAffilie = pucsFile.getNumeroAffilie();
        try {
            if (!pucsFileToMergded.isEmpty()) {
                MergePucs mergePucs = new MergePucs(workDirectory);
                pucsFile = mergePucs.mergeAndBuildPucsFile(pucsFileToMergded.get(0).getNumeroAffilie(),
                        pucsFileToMergded, session);
                domParser = mergePucs.getParser();

                isMerged = true;
                if (!isSimulation) {
                    addMergeEntry(pucsFileToMergded, session);
                }
            } else {
                // String filePath = retrieveFile(pucsFile.getId(), pucsFile.getProvenance(), session);
                domParser = new ElementsDomParser(EBPucsFileService.retriveFile(pucsFile.getIdDb(), session));
                pucsFileToMergded.add(pucsFile);
            }
            if (!isSimulation) {
                changeStatusToEnTraitement(pucsFileToMergded, session);
            }
        } catch (Throwable e) {
            throw new RuntimeException("Error append with this numeroAffilie:" + numeroAffilie, e);
        }

        return pucsFile;
    }

    /**
     * Ajout d'une entrée dans la table contenant les fichiers mergés.
     * 
     * @param pucsFiles Liste des fichiers qui ont été mergés.
     * @param session
     */
    private void addMergeEntry(List<PucsFile> pucsFiles, BSession session) {
        EBPucsFileService.addMergePucsFile(pucsFiles, session);
    }

    /**
     * Changement du statut des fichiers PUCS.
     * 
     * @param pucsFiles Liste des fichiers dont le statut doit être changé
     * @param session
     */
    private void changeStatusToEnTraitement(List<PucsFile> pucsFiles, BSession session) {
        EBPucsFileService.enTraitement(pucsFiles, session);
    }

    public String getIdsPucsFileSeparteByComma() {
        if (!pucsFileToMergded.isEmpty()) {
            return Joiner.on(";").skipNulls()
                    .join(Iterables.transform(pucsFileToMergded, new Function<PucsFile, String>() {
                        @Override
                        public String apply(PucsFile pucsFile) {
                            if (pucsFile != null) {
                                return pucsFile.getFilename();
                            } else {
                                return null;
                            }
                        }
                    }));
        } else {
            return pucsFile.getFilename();
        }
    }

    public void clearDomParser() {
        domParser = null;
    }

}
