package globaz.amal.process.sedexconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AMSedexConverter23To22_2 {
    private File fileToConvert;
    private String convertedFiledestPath;
    private static final Map<String, String> URI_NAMESPACE_CONCORDANCE_MAP = initUriNamespaceConcordanceMap();

    public AMSedexConverter23To22_2(File fileToConvert, String convertedFiledestPath) {
        super();
        this.fileToConvert = fileToConvert;
        this.convertedFiledestPath = convertedFiledestPath;
    }

    private static Map<String, String> initUriNamespaceConcordanceMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pv-common/3", "pv-common/2");
        map.put("pv-5211-000102/3", "pv-5211-000102/2");
        map.put("pv-5211-000103/3", "pv-5211-000103/2");
        map.put("pv-5211-000202/3", "pv-5211-000202/2");
        map.put("pv-5211-000203/3", "pv-5211-000203/2");
        map.put("pv-5211-000301/3", "pv-5211-000301/2");
        map.put("pv-5212-000402/3", "pv-5212-000402/2");
        map.put("pv-5213-000601/3", "pv-5213-000601/2");
        map.put("pv-5214-000701/3", "pv-5214-000701/2");
        return map;
    }

    public boolean convert() {
        boolean isConverted = false;
        try {
            System.out.println("\n-----------------------------------------------------");
            System.out.println("traitement du fichier : " + fileToConvert.getName());
            System.out.println("-----------------------------------------------------");

            // lecture du fichier à convertir
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToConvert));
            String line;
            String oldFileString = "";
            while ((line = bufferedReader.readLine()) != null) {
                oldFileString += line + "\n";
            }
            bufferedReader.close();

            // modification du fichier si nécessaire
            String newFileString = new String(oldFileString);
            for (Map.Entry<String, String> concordanceEntry : URI_NAMESPACE_CONCORDANCE_MAP.entrySet()) {
                String concordanceV3 = concordanceEntry.getKey();
                String concordanceV2 = concordanceEntry.getValue();
                if (newFileString.contains(concordanceV3)) {
                    System.out.println("modification du Namespace : " + concordanceV3 + " --->  " + concordanceV2);
                    newFileString = newFileString.replaceAll(concordanceV3, concordanceV2);
                    isConverted = true;
                    if (concordanceV3.equals("pv-5211-000103/3")) {
                        System.out.println("modification du decreeRejectReason : 8 -> 7");
                        newFileString = newFileString.replaceAll("decreeRejectReason>8<", "decreeRejectReason>7<");
                    }
                }
            }

            // sauvegarde du fichier dans un nouveau fichier
            File convertedFile = new File(convertedFiledestPath + "/" + fileToConvert.getName());
            new File(convertedFile.getParent()).mkdirs();
            FileWriter writer = new FileWriter(convertedFile);
            writer.write(newFileString);
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return isConverted;
    }
}
