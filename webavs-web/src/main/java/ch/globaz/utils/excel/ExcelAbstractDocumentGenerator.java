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
 * Structure de base afin de g�n�rer un fichier Excel gr�ce � Excelml.
 * 
 * @author PBA
 */
public abstract class ExcelAbstractDocumentGenerator {

    private static final String defaultDocumentName = "GlobazExcelDocument";

    /**
     * <p>
     * Cr�er et retourne un document Excel, nomm� par {@link #getOutputName()()}, construit avec le mod�le d�fini dans
     * {@link #getModelPath()} et avec les donn�es d�finies dans {@link #loadData()}.
     * </p>
     * 
     * @return un document Excel
     * @throws DocException
     *             si une erreur survient lors de la g�n�ration du document
     */
    public ExcelmlWorkbook createDoc() throws DocException {
        try {
            return mergeDocumentExcel(getModelPath(), getOutputName(), loadData());
        } catch (Exception e) {
            throw new DocException("Unable to create the document: " + getModelPath() + " Erreur : " + e.toString(), e);
        }
    }

    /**
     * D�fini le mod�le qui sera utilis� pour g�n�rer le fichier Excel.<br/>
     * Le chemin doit �tre relatif � la base de l'application web (WebContent).
     * 
     * @return le chemin du mod�le de fichier Excel � utiliser
     */
    public abstract String getModelPath();

    /**
     * D�fini le nom du fichier qui sera construit.
     * 
     * @return le nom du fichier qui sera g�n�r�
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
     * Construit et retourne les donn�es du ficher Excel.
     * 
     * @return les donn�es qui seront utilis�es pour g�n�rer le ficher Excel
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
