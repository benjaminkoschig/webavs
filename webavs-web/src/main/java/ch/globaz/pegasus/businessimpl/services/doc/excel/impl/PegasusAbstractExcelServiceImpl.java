package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.jade.client.util.JadeUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.common.model.document.Document;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileOutputStream;
import java.io.InputStream;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public abstract class PegasusAbstractExcelServiceImpl extends PegasusAbstractServiceImpl {

    public ExcelmlWorkbook createDoc() throws DocException {
        try {
            return mergeDocumentExcel(getModelName(), getOutPutName(), loadResults());
        } catch (Exception e) {
            throw new DocException("Unable to create the document: " + getModelName(), e);
        }
    }


    public abstract String getModelName();

    public abstract String getOutPutName();

    private ExcelmlWorkbook load(String model) throws Exception {
        ExcelmlWorkbook wk = null;
        InputStream fis = null;
        try {
            fis = JadeUtil.getGlobazInputStream("pegasus/doc/model/excelml/" + model);
            // fis = new FileInputStream(sourceFile);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    public abstract IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException;

    private ExcelmlWorkbook mergeDocumentExcel(String modelName, String nomDoc, IMergingContainer container)
            throws Exception {
        ExcelmlWorkbook wk = null;
        // On va charger le classeur
        wk = load(modelName);
        wk.mergeDocument(container);
        return wk;
    }

    public String save(Document doc, String nomDoc) throws Exception {
        String filePath = Jade.getInstance().getPersistenceDir() + nomDoc;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            doc.write(fos);
            // this.size = fos.getChannel().size();
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return filePath;
    }
}
