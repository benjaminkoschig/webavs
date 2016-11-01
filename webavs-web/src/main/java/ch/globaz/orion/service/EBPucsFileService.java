package ch.globaz.orion.service;

import globaz.globall.db.BSession;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.orion.db.EBPucsFileManager;

public class EBPucsFileService {
    public static PucsFile read(String id, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        try {
            entity.retrieve();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return convert(entity);
    }

    public static List<PucsFile> readByIds(List<String> ids, BSession session) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("La liste d'ids est vide");
        }
        EBPucsFileManager manager = new EBPucsFileManager();
        manager.setSession(session);
        manager.setInIds(ids);
        List<EBPucsFileEntity> entities = manager.search();
        List<PucsFile> pucsFiles = new ArrayList<PucsFile>();
        for (EBPucsFileEntity ebPucsFileEntity : entities) {
            pucsFiles.add(convert(ebPucsFileEntity));
        }
        return pucsFiles;
    }

    public static PucsFile readWithFile(String id, BSession session) {
        PucsFile pucsFile = read(id, session);
        pucsFile.setFile(retriveFile(id, session));
        return pucsFile;
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

    public static void accepter(String id, BSession session) {
        changeStatut(id, EtatPucsFile.A_TRAITER, session);
    }

    public static void rejeter(String id, BSession session) {
        changeStatut(id, EtatPucsFile.REJETE, session);
    }

    public static void annulerRejeter(String id, BSession session) {
        changeStatut(id, EtatPucsFile.A_VALIDE, session);
    }

    private static void changeStatut(String id, EtatPucsFile etat, BSession session) {
        if (!etat.isEditable()) {
            throw new RuntimeException("Le fichier ne peut pas être édité car déjà traité");
        }
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        try {
            entity.retrieve();
            entity.setStatut(Integer.parseInt(etat.getValue()));
            entity.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PucsFile convert(EBPucsFileEntity entity) {
        PucsFile pucsFile = new PucsFile();
        pucsFile.setId(entity.getIdFileName());
        pucsFile.setAfSeul(entity.isAfSeul());
        pucsFile.setAnneeDeclaration(String.valueOf(entity.getAnneeDeclaration()));
        pucsFile.setCurrentStatus(EtatPucsFile.fromValue(String.valueOf(entity.getStatut())));
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
