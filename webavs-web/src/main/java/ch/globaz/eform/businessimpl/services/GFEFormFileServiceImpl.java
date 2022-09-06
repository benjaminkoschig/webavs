package ch.globaz.eform.businessimpl.services;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.services.GFEFormFileService;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;

public class GFEFormFileServiceImpl implements GFEFormFileService {
    @Override
    public String getZipFormulaire(String id) throws Exception {
        GFFormulaireModel model = GFEFormServiceLocator.getGFEFormDBService().read(id);

        //Chargement du zip
        EFormFileService fileService = EFormFileService.instance();
        return fileService.retrieve(GFFileUtils.generateFilePath(model), model.getAttachementName()).getAbsolutePath().replaceAll(StringEscapeUtils.escapeJava(File.separator), "/");
    }
}
