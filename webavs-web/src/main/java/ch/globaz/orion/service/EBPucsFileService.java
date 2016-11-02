package ch.globaz.orion.service;

import globaz.globall.db.BSession;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.orion.db.EBPucsFileManager;
import ch.globaz.orion.db.EBPucsFileMergedEntity;
import ch.globaz.orion.db.EBPucsFileMergedTableDef;

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

    public static PucsFile readByFilename(String filename, BSession session) {
        EBPucsFileManager manager = new EBPucsFileManager();
        manager.setSession(session);
        manager.setForFilename(filename);
        List<EBPucsFileEntity> entities = manager.search();
        List<PucsFile> pucsFiles = new ArrayList<PucsFile>();
        for (EBPucsFileEntity ebPucsFileEntity : entities) {
            pucsFiles.add(convert(ebPucsFileEntity));
        }
        return pucsFiles.get(0);
    }

    public static PucsFile readWithFile(String id, BSession session) {
        PucsFile pucsFile = read(id, session);
        pucsFile.setFile(retriveFile(id, session));
        return pucsFile;
    }

    public static InputStream retriveFileAsInputStream(String id, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        return entity.readInputStream();
    }

    public static InputStream retriveFileAsInputStream(String id) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
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

    public static void comptabiliser(List<PucsFile> pucsFiles, BSession session) {
        for (PucsFile pucsFile : pucsFiles) {
            comptabiliser(pucsFile.getIdDb(), session);
        }
    }

    public static void enTraitement(List<PucsFile> pucsFiles, BSession session) {
        for (PucsFile pucsFile : pucsFiles) {
            enTraitement(pucsFile.getIdDb(), session);
        }
    }

    public static void comptabiliserByFilename(String filename, BSession session) {
        PucsFile pucsFile = readByFilename(filename, session);
        comptabiliser(pucsFile.getIdDb(), session);
    }

    public static void comptabiliser(String id, BSession session) {
        changeStatut(id, EtatPucsFile.COMPTABILISE, session);
    }

    public static void enTraitement(String id, BSession session) {
        changeStatut(id, EtatPucsFile.EN_TRAITEMENT, session);
    }

    private static void changeStatut(String id, EtatPucsFile etat, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        entity.setSession(session);
        try {
            entity.retrieve();
            EtatPucsFile etatActuel = EtatPucsFile.fromValue(entity.getStatut().toString());
            if (!etatActuel.isEditable()) {
                throw new RuntimeException("Le fichier ne peut pas �tre �dit� car d�j� trait�");
            }
            entity.setStatut(Integer.parseInt(etat.getValue()));
            entity.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PucsFile convert(EBPucsFileEntity entity) {
        PucsFile pucsFile = new PucsFile();
        pucsFile.setFilename(entity.getIdFileName());
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

    public static void addMergePucsFile(List<PucsFile> pucsFiles, BSession session) {
        try {
            for (PucsFile pucsFile : pucsFiles) {
                EBPucsFileMergedEntity mergedEntity = new EBPucsFileMergedEntity();
                mergedEntity.setIdPucFile(pucsFile.getIdDb());
                mergedEntity.setIdMerged(getNextIdMerged(session));
                mergedEntity.save();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getNextIdMerged(BSession session) {
        String query = SQLWriter.write().select().max(EBPucsFileMergedTableDef.ID_MERGED)
                .from("SCHEMA." + EBPucsFileMergedTableDef.TABLE).toSql();
        return String.valueOf(QueryExecutor.executeAggregate(query, session).intValue() + 1);
    }
}
