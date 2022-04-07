package ch.globaz.eform.businessimpl.services;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFEFormModel;
import ch.globaz.eform.business.services.GFEFormFileService;
import globaz.jade.common.Jade;

import java.io.File;
import java.io.FileOutputStream;

public class GFEFormFileServiceImpl implements GFEFormFileService {
    @Override
    public String getZipFormulaire(String id) throws Exception {
        GFEFormModel model = GFEFormServiceLocator.getGFEFormService().readWithBlobs(id);

        String pathName = Jade.getInstance().getPersistenceDir() + model.getAttachementName();
        try (FileOutputStream fos = new FileOutputStream(pathName)) {
            fos.write(model.getAttachement());
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }

        return pathName;
    }
}
