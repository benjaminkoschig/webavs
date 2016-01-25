package ch.globaz.al.businessimpl.documents.excel;

import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.exception.OpNodeInstanciationException;
import globaz.op.common.exception.OpWrongFormatException;
import globaz.op.common.exception.OpXmlReaderException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.common.model.document.Document;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * 
 * @author jts
 * 
 */
public abstract class ALAbstractExcelServiceImpl extends ALAbstractBusinessServiceImpl {

    /**
     * 
     * @param parameters
     * @return
     * @throws JadeApplicationException
     */
    protected ExcelmlWorkbook createDoc(Map<String, Object> parameters) throws JadeApplicationException {
        try {
            return mergeDocumentExcel(getModelName(), getOutputName(), prepareData(parameters));
        } catch (Exception e) {
            throw new ALDocumentException("AbstractExcelServiceImpl#createDoc : Unable to create the document: "
                    + getModelName(), e);
        }
    }

    /**
     * 
     * @param parameters
     * @return
     * @throws Exception
     */
    public String createDocAndSave(Map<String, Object> parameters) throws Exception {
        ExcelmlWorkbook wk = createDoc(parameters);
        String nomDoc = getOutputName() + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    /**
     * 
     * @return
     */
    protected abstract String getModelName();

    /**
     * 
     * @return
     */
    protected abstract String getOutputName();

    /**
     * 
     * @param model
     * @return
     * @throws IOException
     * @throws OpWrongFormatException
     * @throws OpXmlReaderException
     * @throws OpNodeInstanciationException
     * @throws Exception
     */
    private ExcelmlWorkbook load(String model) throws IOException, OpNodeInstanciationException, OpXmlReaderException,
            OpWrongFormatException {
        ExcelmlWorkbook wk = null;
        InputStream fis = null;
        try {
            fis = JadeUtil.getGlobazInputStream("al/doc/model/excelml/" + model);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    /**
     * 
     * @param modelName
     * @param nomDoc
     * @param container
     * @return
     * @throws Exception
     */
    private ExcelmlWorkbook mergeDocumentExcel(String modelName, String nomDoc, IMergingContainer container)
            throws Exception {

        ExcelmlWorkbook wk = load(modelName);
        wk.mergeDocument(container);
        return wk;
    }

    /**
     * 
     * @param parameters
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    protected abstract IMergingContainer prepareData(Map<String, Object> parameters)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;

    /**
     * 
     * @param doc
     * @param nomDoc
     * @return
     * @throws Exception
     */
    protected String save(Document doc, String nomDoc) throws Exception {
        String filePath = Jade.getInstance().getPersistenceDir() + nomDoc;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            doc.write(fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return filePath;
    }
}
