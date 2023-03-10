package ch.globaz.eform.businessimpl.services;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.services.GFEFormFileService;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.application.GFApplication;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;

public class GFEFormFileServiceImpl implements GFEFormFileService {
    @Override
    public String getZipFormulaire(String id) throws Exception {
        GFFormulaireModel model = GFEFormServiceLocator.getGFEFormDBService().read(id);

        //Chargement du zip
        EFormFileService fileService = new EFormFileService(GFApplication.EFORM_HOST_FILE_SERVER);
        return fileService.retrieve(GFFileUtils.generateEFormFilePath(model), model.getAttachementName()).getAbsolutePath().replaceAll(StringEscapeUtils.escapeJava(File.separator), "/");
    }
}
