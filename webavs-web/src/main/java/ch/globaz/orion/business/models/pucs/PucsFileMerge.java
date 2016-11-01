package ch.globaz.orion.business.models.pucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.businessimpl.services.merge.MergePucs;
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
            mapPucsFile.put(pucsFile.getId(), pucsFile);
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

    public PucsFile retriveFileAndMergeIfNeeded() throws JadeApplicationServiceNotAvailableException, IOException,
            Exception {
        return this.retriveFileAndMergeIfNeeded(BSessionUtil.getSessionFromThreadContext());
    }

    public PucsFile retriveFileAndMergeIfNeeded(BSession session) throws JadeApplicationServiceNotAvailableException,
            IOException, Exception {

        Checkers.checkNotNull(session, "session");
        String numeroAffilie = pucsFile.getNomAffilie();
        try {
            for (PucsFile pucsFile : pucsFileToMergded) {
                numeroAffilie = pucsFile.getNumeroAffilie();
                // EBPucsFileService.retriveFile(id, session)
                // filesRetrieved.put(pucsFile.getId(), retrieveFile(pucsFile.getId(), pucsFile.getProvenance(),
                // session));
            }

            if (!pucsFileToMergded.isEmpty()) {
                MergePucs mergePucs = new MergePucs(workDirectory);
                pucsFile = mergePucs.mergeAndBuildPucsFile(pucsFileToMergded.get(0).getNumeroAffilie(),
                        pucsFileToMergded, session);
                domParser = mergePucs.getParser();

                isMerged = true;
            } else {
                String filePath = null;
                // String filePath = retrieveFile(pucsFile.getId(), pucsFile.getProvenance(), session);
                domParser = new ElementsDomParser(filePath);
                filesRetrieved.put(pucsFile.getId(), filePath);
                pucsFileToMergded.add(pucsFile);
            }
        } catch (Throwable e) {
            throw new RuntimeException("Error append with this numeroAffilie:" + numeroAffilie, e);
        }

        return pucsFile;
    }

    public String getIdsPucsFileSeparteByComma() {
        if (!pucsFileToMergded.isEmpty()) {
            return Joiner.on(";").skipNulls()
                    .join(Iterables.transform(pucsFileToMergded, new Function<PucsFile, String>() {
                        @Override
                        public String apply(PucsFile pucsFile) {
                            if (pucsFile != null) {
                                return pucsFile.getId();
                            } else {
                                return null;
                            }
                        }
                    }));
        } else {
            return pucsFile.getId();
        }
    }

    /**
     * 
     * La fonction close va supprimer les fichiers qui ont été déposé dans le workDirectory et déplacer les fichiers de
     * swissDec dans le répertoire approprié. Si tout c'est bien passé le fichier de swissDec se trouvera dans le
     * répertoire ok, s'il y a eu une erreur, le fichier sera déposé dans le répertoire ko.
     * 
     * Pour les fichiers qui ont été fusionnés, les fichiers sources de type swissDec sont aussi déplacés avec les mêmes
     * règles définis avant. Le fichier fusionné sera déposé dans un répertoire spécifique. La composition du nom du
     * fichier est déterminée de la manière suivante: numeroAffilié_dateuuid_f_nombreDeFichiersFusioné. Le nom des
     * fichiers sources qui ont servi à la fusion est définit de la manière suivante:
     * nomDuFichier_dateuuid_f_indexFichier_nombreFichierFusioné.
     * 
     * @param hasError
     * @throws Exception
     */
    public void close(boolean hasError) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyddMMhhmmssSSS");
        String uuid = format.format(new Date()) + "_f_";
        int i = 1;
        for (PucsFile pucsFile : pucsFileToMergded) {
            if (pucsFile.getProvenance().isSwissDec()) {
                String path = EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue() + pucsFile.getId() + ".xml";
                String pathToCopie;
                if (hasError) {
                    pathToCopie = EBProperties.PUCS_SWISS_DEC_DIRECTORY_KO.getValue();
                } else {
                    pathToCopie = EBProperties.PUCS_SWISS_DEC_DIRECTORY_OK.getValue();
                }

                if (!isMerged) {
                    uuid = format.format(new Date());
                }

                String dest = pathToCopie + pucsFile.getId().replace(".", "") + "_" + uuid + i + "_"
                        + pucsFileToMergded.size() + ".xml";

                JadeFsFacade.copyFile(path, dest);
                JadeFsFacade.delete(path);
                JadeFsFacade.delete(workDirectory + pucsFile.getId() + ".xml");
                i++;
            }
        }

        if (isMerged && pucsFile.getProvenance().isSwissDec()) {
            JadeFsFacade.copyFile(workDirectory + pucsFile.getId() + ".xml",
                    EBProperties.PUCS_MERGED_DIRECTORY.getValue() + pucsFile.getNumeroAffilie().replaceAll("\\.", "")
                            + "_" + uuid + pucsFileToMergded.size() + ".xml");
        }

        for (String path : filesRetrieved.values()) {
            JadeFsFacade.delete(path);
        }
        JadeFsFacade.delete(workDirectory + pucsFile.getId() + ".xml");
    }

    public void clearDomParser() {
        domParser = null;
    }

}
