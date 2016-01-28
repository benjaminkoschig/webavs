package ch.globaz.utils.excel;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.common.Jade;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileOutputStream;
import java.io.InputStream;
import ch.globaz.perseus.business.exceptions.doc.DocException;

/**
 * Structure de base afin de générer un fichier Excel grâce à Excelml.
 * 
 * @author PBA
 */
public abstract class ExcelAbstractDocumentGenerator {

    private static final String defaultDocumentName = "GlobazExcelDocument";

    /**
     * <p>
     * Créer et retourne un document Excel, nommé par {@link #getOutputName()()}, construit avec le modèle défini dans
     * {@link #getModelPath()} et avec les données définies dans {@link #loadData()}.
     * </p>
     * 
     * @return un document Excel
     * @throws DocException
     *             si une erreur survient lors de la génération du document
     */
    public ExcelmlWorkbook createDoc() throws DocException {
        try {
            return mergeDocumentExcel(getModelPath(), getOutputName(), loadData());
        } catch (Exception e) {
            throw new DocException("Unable to create the document: " + getModelPath() + " Erreur : " + e.toString(), e);
        }
    }

    /**
     * Défini le modèle qui sera utilisé pour générer le fichier Excel.<br/>
     * Le chemin doit être relatif à la base de l'application web (WebContent).
     * 
     * @return le chemin du modèle de fichier Excel à utiliser
     */
    public abstract String getModelPath();

    /**
     * Défini le nom du fichier qui sera construit.
     * 
     * @return le nom du fichier qui sera généré
     */
    public abstract String getOutputName();

    private ExcelmlWorkbook load(String modelPath) throws Exception {
        ExcelmlWorkbook wk = null;
        InputStream fis = null;
        try {
            fis = JadeUtil.getGlobazInputStream(modelPath);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    /**
     * Construit et retourne les données du ficher Excel.
     * 
     * @return les données qui seront utilisées pour générer le ficher Excel
     * @throws Exception
     */
    public abstract IMergingContainer loadData() throws Exception;

    private ExcelmlWorkbook mergeDocumentExcel(String modelName, String nomDoc, IMergingContainer container)
            throws Exception {
        ExcelmlWorkbook wk = null;
        wk = load(modelName);
        wk.mergeDocument(container);
        return wk;
    }

    /**
     * Sauve le document et retourne son emplacement.
     * 
     * @param document
     * @return
     * @throws Exception
     */
    public String save(ExcelmlWorkbook document) throws Exception {
        StringBuilder documentName = new StringBuilder();
        int dotXMLindex = getOutputName().lastIndexOf(".xml");
        switch (dotXMLindex) {
            case -1:
                if (JadeStringUtil.isBlank(getOutputName())) {
                    documentName.append(ExcelAbstractDocumentGenerator.defaultDocumentName);
                } else {
                    documentName.append(getOutputName());
                }
                break;
            case 0:
                documentName.append(ExcelAbstractDocumentGenerator.defaultDocumentName);
                break;
            default:
                documentName.append(getOutputName().substring(0, dotXMLindex));
        }
        documentName.append("_").append(JadeUUIDGenerator.createStringUUID()).append(".xml");

        String filePath = Jade.getInstance().getPersistenceDir() + documentName.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            document.write(fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return filePath;
    }
}
