package ch.globaz.orion.db;

import globaz.globall.db.BTransaction;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;
import com.google.common.io.Files;

public class EBPucsFileEntity extends JadeEntity {

    private static final long serialVersionUID = 1L;
    private static final String CONST_BLOB = "PUCS_FILE_ID_ENTITY#";

    private String id;
    private String idJob;
    private String idFileName;
    private String idAffiliation;
    private Integer anneeDeclaration;
    private Integer statut;
    private Date dateReception;
    private String handlingUser;
    private String nomAffilie;
    private Integer nbSalaire;
    private String numeroAffilie;
    private String provenance;
    private BigDecimal totalControle;
    private Double sizeFileInKo;
    private Boolean afSeul;
    private Boolean duplicate;
    private Integer niveauSecurite;
    private Boolean salaireInferieurLimite;
    private File file;
    private String searchString;
    private Boolean forTest;
    private Boolean certifieExact;
    private Date dateValidation;
    private String nomValidation;

    @Override
    protected void writeProperties() {
        writeStringAsNumeric(EBPucsFileDefTable.ID_JOB, idJob);
        write(EBPucsFileDefTable.ID_FILE_NAME, idFileName);
        writeStringAsNumeric(EBPucsFileDefTable.ID_AFFILIATION, idAffiliation);
        write(EBPucsFileDefTable.ANNEE_DECLARATION, anneeDeclaration);
        write(EBPucsFileDefTable.STATUS, statut);
        write(EBPucsFileDefTable.HANDLING_USER, handlingUser);
        write(EBPucsFileDefTable.NOM_AFFILIE, nomAffilie);
        write(EBPucsFileDefTable.NB_SALAIRES, nbSalaire);
        write(EBPucsFileDefTable.NUMERO_AFFILIE, numeroAffilie);
        write(EBPucsFileDefTable.TOTAL_CONTROLE, totalControle);
        write(EBPucsFileDefTable.SIZE_FILE_IN_KO, sizeFileInKo);
        write(EBPucsFileDefTable.IS_AF_SEUL, afSeul);
        write(EBPucsFileDefTable.NIVEAU_SECURITE, niveauSecurite);
        write(EBPucsFileDefTable.PROVENANCE, provenance);
        write(EBPucsFileDefTable.DATE_RECEPTION, dateReception);
        write(EBPucsFileDefTable.DUPLICATE, duplicate);
        write(EBPucsFileDefTable.SAL_INF_LIMIT, salaireInferieurLimite);
        write(EBPucsFileDefTable.SEARCH_STRING, searchString);
        write(EBPucsFileDefTable.FOR_TEST, forTest);
        write(EBPucsFileDefTable.CERTIFIE_EXACT, certifieExact);
        write(EBPucsFileDefTable.DATE_VALIDATION, dateValidation);
        write(EBPucsFileDefTable.NOM_VALIDATION, nomValidation);
    }

    @Override
    protected void readProperties() {
        id = this.read(EBPucsFileDefTable.ID);
        idJob = this.readString(EBPucsFileDefTable.ID_JOB);
        idAffiliation = this.read(EBPucsFileDefTable.ID_AFFILIATION);
        idFileName = read(EBPucsFileDefTable.ID_FILE_NAME);
        anneeDeclaration = read(EBPucsFileDefTable.ANNEE_DECLARATION);
        statut = read(EBPucsFileDefTable.STATUS);
        handlingUser = read(EBPucsFileDefTable.HANDLING_USER);
        nomAffilie = read(EBPucsFileDefTable.NOM_AFFILIE);
        nbSalaire = read(EBPucsFileDefTable.NB_SALAIRES);
        numeroAffilie = read(EBPucsFileDefTable.NUMERO_AFFILIE);
        totalControle = read(EBPucsFileDefTable.TOTAL_CONTROLE);
        sizeFileInKo = read(EBPucsFileDefTable.SIZE_FILE_IN_KO);
        afSeul = read(EBPucsFileDefTable.IS_AF_SEUL);
        niveauSecurite = read(EBPucsFileDefTable.NIVEAU_SECURITE);
        provenance = read(EBPucsFileDefTable.PROVENANCE);
        dateReception = read(EBPucsFileDefTable.DATE_RECEPTION);
        duplicate = read(EBPucsFileDefTable.DUPLICATE);
        salaireInferieurLimite = read(EBPucsFileDefTable.SAL_INF_LIMIT);
        searchString = readString(EBPucsFileDefTable.SEARCH_STRING);
        forTest = read(EBPucsFileDefTable.FOR_TEST);
        certifieExact = read(EBPucsFileDefTable.CERTIFIE_EXACT);
        dateValidation = read(EBPucsFileDefTable.DATE_VALIDATION);
        nomValidation = read(EBPucsFileDefTable.NOM_VALIDATION);
    }

    public static void deleteFileOnWorkDirectory(String idFileName) {
        try {
            JadeFsFacade.delete(createFileName(idFileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File retriveFile() {
        File f = new File(createFileName(idFileName));
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(f);
            fileOutputStream.write(readByte());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return f;
    }

    private static String createFileName(String idFileName) {
        return Jade.getInstance().getHomeDir() + "work/" + idFileName + ".xml";
    }

    public InputStream readInputStream() {
        return new ByteArrayInputStream(readByte());
    }

    private byte[] readByte() {
        Object object;
        try {
            object = BlobManager.readBlobWithoutContext(CONST_BLOB + id);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        }
        return (byte[]) object;
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        BlobManager.addBlob(CONST_BLOB + id, Files.toByteArray(file));
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return EBPucsFileDefTable.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdFileName() {
        return idFileName;
    }

    public void setIdFileName(String idFileName) {
        this.idFileName = idFileName;
    }

    public Integer getAnneeDeclaration() {
        return anneeDeclaration;
    }

    public void setAnneeDeclaration(Integer anneeDeclaration) {
        this.anneeDeclaration = anneeDeclaration;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public String getHandlingUser() {
        return handlingUser;
    }

    public void setHandlingUser(String handlingUser) {
        this.handlingUser = handlingUser;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public Integer getNbSalaire() {
        return nbSalaire;
    }

    public void setNbSalaire(Integer nbSalaire) {
        this.nbSalaire = nbSalaire;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public BigDecimal getTotalControle() {
        return totalControle;
    }

    public void setTotalControle(BigDecimal totalControle) {
        this.totalControle = totalControle;
    }

    public Double getSizeFileInKo() {
        return sizeFileInKo;
    }

    public void setSizeFileInKo(Double sizeFileInKo) {
        this.sizeFileInKo = sizeFileInKo;
    }

    public boolean isAfSeul() {
        return afSeul;
    }

    public void setAfSeul(boolean afSeul) {
        this.afSeul = afSeul;
    }

    public Integer getNiveauSecurite() {
        return niveauSecurite;
    }

    public void setNiveauSecurite(Integer niveauSecurite) {
        this.niveauSecurite = niveauSecurite;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public Boolean getSalaireInferieurLimite() {
        return salaireInferieurLimite;
    }

    public void setSalaireInferieurLimite(Boolean salaireInferieurLimite) {
        this.salaireInferieurLimite = salaireInferieurLimite;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public boolean isForTest() {
        return forTest;
    }

    public void setForTest(boolean forTest) {
        this.forTest = forTest;
    }

    public boolean isCertifieExact() {
        return certifieExact;
    }

    public void setCertifieExact(boolean certifieExact) {
        this.certifieExact = certifieExact;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getNomValidation() {
        return nomValidation;
    }

    public void setNomValidation(String nomValidation) {
        this.nomValidation = nomValidation;
    }

}
