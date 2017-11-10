package ch.globaz.orion.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    public static Collection<PucsFile> readByIds(Collection<String> ids, BSession session) {
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

    public static void affectCurrentUser(PucsFile pucsFile, BSession session) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setSession(session);
        entity.setIdEntity(pucsFile.getIdDb());
        try {
            entity.retrieve();
            entity.setHandlingUser(session.getUserId());
            entity.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PucsFile readByFilename(String filename, BSession session) {
        EBPucsFileManager manager = new EBPucsFileManager();
        manager.setSession(session);
        manager.setForFilename(filename);
        List<EBPucsFileEntity> entities = manager.search();
        if (!entities.isEmpty()) {
            List<PucsFile> pucsFiles = new ArrayList<PucsFile>();
            for (EBPucsFileEntity ebPucsFileEntity : entities) {
                pucsFiles.add(convert(ebPucsFileEntity));
            }
            return pucsFiles.get(0);
        }
        return null;
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
        try {
            entity.retrieve();
            return entity.retriveFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PucsFile> entitiesToPucsFile(List<EBPucsFileEntity> list) {
        List<PucsFile> pucsFilesFinal = new ArrayList<PucsFile>();
        for (EBPucsFileEntity entity : list) {
            pucsFilesFinal.add(convert(entity));
        }
        return pucsFilesFinal;
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

    public static void enErreur(List<PucsFile> pucsFiles, BTransaction transaction) {
        for (PucsFile pucsFile : pucsFiles) {
            enErreur(pucsFile.getIdDb(), transaction);
        }
    }

    public static void enErreur(List<PucsFile> pucsFiles, BSession session) {
        for (PucsFile pucsFile : pucsFiles) {
            enErreur(pucsFile.getIdDb(), session);
        }
    }

    public static void comptabiliserByFilename(String filename, BSession session) {
        PucsFile pucsFile = readByFilename(filename, session);
        // On est fait se test pour gérer la rétrocomptabilité des ancienne DS.
        // se test pour être supprimé en 2017
        if (pucsFile != null) {
            comptabiliser(pucsFile.getIdDb(), session);
        } else {
            JadeLogger.warn(new EBPucsFileService(),
                    "Aucune entrée trouvée dans la table des pucs_file avec ce fileName: " + filename);
        }
    }

    public static void aTraiterByFilename(String filename, BSession session) {
        PucsFile pucsFile = readByFilename(filename, session);
        // On est fait se test pour gérer la rétrocomptabilité des ancienne DS.
        // se test pour être supprimé en 2017
        if (pucsFile != null) {
            aTraiter(pucsFile.getIdDb(), session);
        } else {
            JadeLogger.warn(new EBPucsFileService(),
                    "Aucune entrée trouvée dans la table des pucs_file avec ce fileName: " + filename);
        }
    }

    public static void aTraiter(String id, BSession session) {
        changeStatut(id, EtatPucsFile.A_TRAITER, session);
    }

    public static void rejeter(String id, BSession session) {
        changeStatut(id, EtatPucsFile.REJETE, session);
    }

    public static void annulerRejeter(String id, BSession session) {
        changeStatut(id, EtatPucsFile.A_VALIDE, session);
    }

    public static void enErreur(String id, BTransaction transaction) {
        changeStatut(id, EtatPucsFile.EN_ERREUR, transaction);
    }

    public static void aTraiter(String id, BTransaction transaction) {
        changeStatut(id, EtatPucsFile.A_TRAITER, transaction);
    }

    public static void changeStatut(String id, EtatPucsFile etat, BTransaction transaction) {
        EBPucsFileEntity entity = read(id, transaction);
        if (entity == null || entity.isNew()) {
            throw new RuntimeException("Aucun PucsFile n'a été trouvé pour l'id " + id);
        }
        EtatPucsFile etatActuel = EtatPucsFile.fromValue(entity.getStatut().toString());
        if (etatActuel.isComptabilise()) {
            throw new RuntimeException("Le fichier ne peut pas être édité car déjà traité");
        }
        entity.setStatut(Integer.parseInt(etat.getValue()));
        try {
            entity.save(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static EBPucsFileEntity read(String id, BTransaction transaction) {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        entity.setIdEntity(id);
        try {
            entity.retrieve(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    public static void enErreur(String id, BSession session) {
        changeStatut(id, EtatPucsFile.EN_ERREUR, session);
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
            if (entity == null || entity.isNew()) {
                throw new RuntimeException("Aucun PucsFile n'a été trouvé pour l'id " + id);
            }
            EtatPucsFile etatActuel = EtatPucsFile.fromValue(entity.getStatut().toString());
            if (etatActuel.isComptabilise() && !etat.isComptabilise()) {
                throw new RuntimeException("Le fichier " + entity.getIdFileName()
                        + " ne peut pas changer de statut car déjà il est déja traité(comptabilisé). Affililé: "
                        + entity.getNumeroAffilie() + ", ID: " + entity.getIdEntity());
            }
            entity.setHandlingUser(session.getUserId());
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
        pucsFile.setForTest(entity.isForTest());
        pucsFile.setHandlingUser(entity.getHandlingUser());
        pucsFile.setNbSalaires(String.valueOf(entity.getNbSalaire()));
        pucsFile.setNomAffilie(entity.getNomAffilie());
        pucsFile.setNumeroAffilie(entity.getNumeroAffilie());
        pucsFile.setProvenance(DeclarationSalaireProvenance.fromValue(entity.getProvenance()));
        pucsFile.setSalaireInferieurLimite(entity.getSalaireInferieurLimite());
        pucsFile.setSizeFileInKo(entity.getSizeFileInKo());
        pucsFile.setTotalControle(new Montant(entity.getTotalControle()).toStringFormat());
        pucsFile.setIdAffiliation(entity.getIdAffiliation());
        pucsFile.setIdDb(entity.getIdEntity());
        pucsFile.setCodeSecuriteCi(entity.getNiveauSecurite());
        pucsFile.setCertifieExact(entity.isCertifieExact());
        pucsFile.setNomValidation(entity.getNomValidation());
        if (entity.getDateValidation() == null) {
            pucsFile.setDateValidation(new Date().getSwissValue());
        } else {
            pucsFile.setDateValidation(new Date(entity.getDateValidation()).getSwissValue());
        }

        return pucsFile;
    }

    public static void addMergePucsFile(List<PucsFile> pucsFiles, BSession session) {
        String nextIdMerged = getNextIdMerged(session);
        try {
            for (PucsFile pucsFile : pucsFiles) {
                EBPucsFileMergedEntity mergedEntity = new EBPucsFileMergedEntity();
                mergedEntity.setIdPucFile(pucsFile.getIdDb());
                mergedEntity.setIdMerged(nextIdMerged);
                mergedEntity.setSession(session);
                mergedEntity.save();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getNextIdMerged(BSession session) {
        String query = SQLWriter.writeWithSchema().select().max(EBPucsFileMergedTableDef.ID_MERGED)
                .from(EBPucsFileMergedTableDef.TABLE).toSql();
        return String.valueOf(QueryExecutor.executeAggregate(query, session).intValue() + 1);
    }

    public static Map<String, AFAffiliation> findAffiliations(Collection<PucsFile> list) {
        List<String> ids = new ArrayList<String>();

        for (PucsFile pucsFile : list) {
            ids.add(pucsFile.getIdAffiliation());
        }

        return AFAffiliationServices
                .searchAffiliationByIdsAndGroupById(ids, BSessionUtil.getSessionFromThreadContext());
    }

}
