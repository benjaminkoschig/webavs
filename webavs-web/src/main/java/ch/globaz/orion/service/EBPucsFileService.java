package ch.globaz.orion.service;

import globaz.globall.db.BSession;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileEntity;

public class EBPucsFileService {

    public static PucsFile readWithFile(String id, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        try {
            entity.retrieve();
            entity.setFile(entity.retriveFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return convert(entity);
    }

    public static InputStream readInputStream(String id, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        return entity.readInputStream();
    }

    public static File retriveFile(String id, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        return entity.retriveFile();
    }

    public static List<PucsFile> entitiesToPucsFile(List<EBPucsFileEntity> list) {
        List<PucsFile> pucsFilesFinal = new ArrayList<PucsFile>();
        for (EBPucsFileEntity entity : list) {
            pucsFilesFinal.add(convert(entity));
        }
        return pucsFilesFinal;
    }

    private static PucsFile convert(EBPucsFileEntity entity) {
        PucsFile pucsFile = new PucsFile();
        pucsFile.setId(entity.getIdFileName());
        pucsFile.setAfSeul(entity.isAfSeul());
        pucsFile.setAnneeDeclaration(String.valueOf(entity.getAnneeDeclaration()));
        pucsFile.setCurrentStatus(String.valueOf(entity.getStatut()));
        pucsFile.setDateDeReception(new Date(entity.getDateReception()).getSwissValue());
        pucsFile.setDuplicate(entity.isDuplicate());
        pucsFile.setHandlingUser(entity.getHandlingUser());
        pucsFile.setNbSalaires(String.valueOf(entity.getNbSalaire()));
        pucsFile.setNomAffilie(entity.getNomAffilie());
        pucsFile.setNumeroAffilie(entity.getNumeroAffilie());
        pucsFile.setProvenance(DeclarationSalaireProvenance.fromValue(entity.getProvenance()));
        pucsFile.setSalaireInferieurLimite(entity.getSalaireInferieurLimite());
        pucsFile.setSizeFileInKo(entity.getSizeFileInKo());
        pucsFile.setTotalControle(new Montant(entity.getTotalControle()).toStringFormat());
        pucsFile.setIdDb(entity.getIdEntity());
        return pucsFile;
    }
}
