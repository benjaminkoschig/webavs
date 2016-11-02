package ch.globaz.orion.businessimpl.services.merge;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.FindPucsSwissDec;
import ch.globaz.orion.service.EBPucsFileService;

/**
 * Le But de cette class est de prendre en entré plusieurs fichiers xml et d’en faire un puis d'additionner certaine
 * valeurs.
 * Etant donné que tous les traitements des fichiers PUCS se font en parcourant le DOM on fait la même chose dans cette
 * classe. De plus comme on support différent type de fichiers PUCS il est aussi plus facile de parcourir le DOM. Le
 * jour où l’on décide de ne supporter plus qu’un type de fichier il serait bien de penser à JAXB.
 **/
public class MergePucs {

    private String workDirectory;
    private List<String> fileNamesMerged = new ArrayList<String>();
    private String uuid = JadeUUIDGenerator.createStringUUID();
    private ElementsDomParser parser;

    public String getUuid() {
        return uuid;
    }

    public MergePucs(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    static boolean isSameProvenance(List<PucsFile> pucsFiles) {
        DeclarationSalaireProvenance proveance = pucsFiles.get(0).getProvenance();
        for (PucsFile pucsFile : pucsFiles) {
            if (!proveance.equals(pucsFile.getProvenance())) {
                return false;
            }
        }
        return true;
    }

    public PucsFile mergeAndBuildPucsFile(String numAffilie, List<PucsFile> pucsFiles, BSession session)
            throws JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException,
            ClassCastException, JadeClassCastException {
        if (!isSameProvenance(pucsFiles)) {
            throw new CommonTechnicalException(
                    "Il n'est pas possible de fusionner des fichiers PUCS de proveances différentes");
        }

        parser = mergeForAffilie(pucsFiles, session);
        String filePath = out(numAffilie, parser.getDocument());
        PucsFile PucsFile = FindPucsSwissDec.buildPucsByFile(filePath, pucsFiles.get(0).getProvenance(), session);
        return PucsFile;
    }

    public ElementsDomParser getParser() {
        return parser;
    }

    String out(String numAffilie, Document document) {
        String fileName = workDirectory + numAffilie + "_" + uuid + "_fusion.xml";
        fileNamesMerged.add(fileName);
        createFile(fileName, document);
        return fileName;
    }

    ElementsDomParser mergeForAffilie(List<PucsFile> pucsFiles, BSession session) {
        ElementsDomParser fusion = null;
        for (PucsFile pucsFile : pucsFiles) {
            try {
                if (fusion == null) {
                    fusion = new ElementsDomParser(EBPucsFileService.retriveFileAsInputStream(pucsFile.getIdDb(),
                            session));
                } else {
                    ElementsDomParser documentCourant = new ElementsDomParser(
                            EBPucsFileService.retriveFileAsInputStream(pucsFile.getIdDb()));
                    NodeList staff = documentCourant.find("SalaryDeclaration Company Staff Person").getResult().get(0);
                    Node staffFusion = fusion.find("SalaryDeclaration Company Staff").getResult().get(0).item(0);
                    for (int i = 0; i < staff.getLength(); i++) {
                        Node node = fusion.getDocument().importNode(staff.item(i), true);
                        staffFusion.appendChild(node);
                    }
                    // Si AF_seul
                    if (documentCourant.find("Total-AHV-AVS-Incomes").getFirstValue() == null) {
                        sumAndAddInNode("NumberOf-FAK-CAF-Salary-Tags", fusion, documentCourant);
                        sumAndAddInNode("Total-FAK-CAF-FamilyIncomeSupplement", fusion, documentCourant);
                        sumAndAddInNode("SalaryTotals Total-FAK-CAF-ContributorySalary", fusion, documentCourant);
                    } else {
                        sumAndAddInNode("SalaryCounters NumberOf-AHV-AVS-Salary-Tags", fusion, documentCourant);
                    }

                    // SalaryTotals AHV-AVS-Totals
                    sumAndAddInNode("Total-AHV-AVS-Incomes", fusion, documentCourant);
                    sumAndAddInNode("Total-AHV-AVS-Open", fusion, documentCourant);
                    sumAndAddInNode("Total-ALV-AC-Incomes", fusion, documentCourant);
                    sumAndAddInNode("Total-ALVZ-ACS-Incomes", fusion, documentCourant);
                    sumAndAddInNode("Total-ALV-AC-Open", fusion, documentCourant);
                }
            } catch (Exception e) {
                throw new CommonTechnicalException(e);
            }
        }

        Element filesMerged = fusion.getDocument().createElement("filesMerged");
        // fusion.getDocument().createTextNode(Joiner.on(",").join(pucsFiles))
        Document document = fusion.getDocument();
        for (PucsFile pucsFile : pucsFiles) {
            Element file = document.createElement("file");
            Element provenance = document.createElement("provenance");
            provenance.appendChild(document.createTextNode(pucsFile.getProvenance().toString()));
            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(pucsFile.getFilename()));
            file.appendChild(id);
            file.appendChild(provenance);
            filesMerged.appendChild(file);
        }

        fusion.getDocument().getDocumentElement().appendChild(filesMerged);

        return fusion;
    }

    private void sumAndAddInNode(String domPath, ElementsDomParser fusion, ElementsDomParser parser) {
        Node node = fusion.find(domPath).getFirstNode();
        if (node != null && node.getChildNodes() != null && node.getChildNodes().item(0) != null
                && node.getChildNodes().item(0).getNodeValue() != null) {
            BigDecimal value = new BigDecimal(node.getChildNodes().item(0).getNodeValue(), new MathContext(16,
                    RoundingMode.HALF_UP)).add(new BigDecimal(parser.find(domPath).getFirstValue()), new MathContext(
                    16, RoundingMode.HALF_UP));
            node.getChildNodes().item(0).setNodeValue(value.toString());
        }
    }

    private void createFile(String fileName, Document document) throws TransformerFactoryConfigurationError {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transFactory.newTransformer();
            StringWriter buffer = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(document), new StreamResult(buffer));
            String str = buffer.toString();

            FileOutputStream file = null;
            try {
                file = new FileOutputStream(fileName);
                Writer out = new BufferedWriter(new OutputStreamWriter(file, "UTF-8"));
                try {
                    out.write(str);
                } finally {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e1) {
            throw new CommonTechnicalException(e1);
        }
    }
}
