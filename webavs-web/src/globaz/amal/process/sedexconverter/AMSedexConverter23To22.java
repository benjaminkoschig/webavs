package globaz.amal.process.sedexconverter;

import globaz.amal.process.sedexconverter.utils.NamespaceChangingVisitor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.io.SAXReader;

public class AMSedexConverter23To22 {
    private File fileToConvert;
    private String convertedFiledestPath;
    private static final Map<String, String> URI_NAMESPACE_CONCORDANCE_MAP = initUriNamespaceConcordanceMap();

    public AMSedexConverter23To22(File fileToConvert, String convertedFiledestPath) {
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
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");

        try {
            // lecture du fichier à convertir et récuparation du root
            Document document = reader.read(fileToConvert);
            Element root = document.getRootElement();

            // récupère tous les Namespace de l'élément root (balise message)
            List<Namespace> namespaceList = root.declaredNamespaces();
            for (Namespace namespace : namespaceList) {
                for (Map.Entry<String, String> concordanceEntry : URI_NAMESPACE_CONCORDANCE_MAP.entrySet()) {
                    String concordanceV3 = concordanceEntry.getKey();
                    String concordanceV2 = concordanceEntry.getValue();

                    // si le namespace correspond à un namespace non reconnu, on le modifie
                    if (namespace.getURI().contains(concordanceV3)) {
                        System.out.println("Modification du fichier " + fileToConvert.getPath());
                        // vérification et adaptation du decreeReject si annonce de type 5211-000103
                        if (concordanceV3.equals("pv-5211-000103/3")) {
                            System.out
                                    .println("Annonce de type pv-5211-000103/3 => vérification valeur decreeRejectReason ");
                            String prefix = namespace.getPrefix();
                            Node decreeRejectReasonNode = null;
                            if (prefix.isEmpty()) {
                                decreeRejectReasonNode = root
                                        .selectSingleNode("*[local-name() = 'content']/*[local-name() = 'decreeReject']/*[local-name() = 'decreeRejectReason']");
                            } else {
                                decreeRejectReasonNode = root.selectSingleNode(prefix + ":content/" + prefix
                                        + ":decreeReject/" + prefix + ":decreeRejectReason");
                            }

                            if (decreeRejectReasonNode != null) {
                                if (decreeRejectReasonNode.getText().equals("8")) {
                                    decreeRejectReasonNode.setText("7");
                                    System.out.println("valeur decreeRejectReason modifiée 8 -> 7");
                                }
                            }

                        }

                        // modification du namespace
                        String splitUri[] = namespace.getURI().split(concordanceV3);
                        String newUri = splitUri[0].concat(concordanceV2);
                        Namespace newNamespace = new Namespace(namespace.getPrefix(), newUri);
                        System.out.println("Modification du Namespace : " + namespace.getPrefix() + ":"
                                + namespace.getURI() + "->" + newNamespace.getPrefix() + ":" + newNamespace.getURI());

                        Visitor visitor = new NamespaceChangingVisitor(namespace, newNamespace);
                        document.accept(visitor);
                        root.remove(namespace);
                        root.add(newNamespace);

                        isConverted = true;
                    }
                }
            }

            // enregistrement du fichier
            save(document, new File(convertedFiledestPath + File.separator + fileToConvert.getName()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return isConverted;
    }

    private void save(Document document, File destFile) {
        new File(destFile.getParent()).mkdirs();
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
            document.write(out);
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
