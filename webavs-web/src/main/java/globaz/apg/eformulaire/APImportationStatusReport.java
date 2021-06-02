package globaz.apg.eformulaire;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class APImportationStatusReport {
    private final List<APImportationStatusFile> fileStatuses = new ArrayList<>();

    public APImportationStatusFile addFile(String fileName, String nss){
        APImportationStatusFile fileStatus = new APImportationStatusFile();
        fileStatus.setFileName(fileName);
        fileStatus.setNss(nss);
        fileStatuses.add(fileStatus);
        return fileStatus;
    }

    public String GenerateStatusMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Status de l'importation des formulaires Paternité et maternités - Fichiers traités %s / Fichiers non traités %s : %s", getNbFichierTraites(),getNbFichierNonTraites(), APImportationStatusFile.LINE_SEP));
        builder.append(String.format("   Fichiers traités (%d) : %s", getNbFichierTraites(), APImportationStatusFile.LINE_SEP));
        fileStatuses.stream().filter(APImportationStatusFile::isSucceed).forEach(file -> builder.append(file.BuildStatusMessage()));
        builder.append("------------------------------"  + APImportationStatusFile.LINE_SEP);
        builder.append(String.format("   Fichiers non traités (%d) : %s", getNbFichierNonTraites(), APImportationStatusFile.LINE_SEP));
        fileStatuses.stream().filter(file -> !file.isSucceed()).forEach(file -> builder.append(file.BuildStatusMessage()));
        return builder.toString();
    }

    public long getNbFichierTraites(){
        return fileStatuses.stream().filter(APImportationStatusFile::isSucceed).count();
    }

    public long getNbFichierNonTraites(){
        return fileStatuses.stream().filter(file -> !file.isSucceed()).count();
    }
}
