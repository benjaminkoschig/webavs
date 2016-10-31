package ch.globaz.orion.db;

import globaz.globall.db.BTransaction;
import globaz.jade.exception.JadePersistenceException;
import java.io.ByteArrayInputStream;
import java.io.File;
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
    private boolean afSeul;
    private boolean affiliationExistante;
    private boolean duplicate;
    private Integer niveauSecurite;
    private Boolean salaireInferieurLimite;
    private File file;
    private InputStream inputStream;

    @Override
    protected void writeProperties() {
        writeStringAsNumeric(EBPucsFileDefTable.ID_JOB, idJob);
        write(EBPucsFileDefTable.ID_FILE_NAME, idFileName);
        write(EBPucsFileDefTable.ANNEE_DECLARATION, anneeDeclaration);
        write(EBPucsFileDefTable.STATUS, statut);
        write(EBPucsFileDefTable.HANDLING_USER, handlingUser);
        write(EBPucsFileDefTable.NOM_AFFILIE, nomAffilie);
        write(EBPucsFileDefTable.NB_SALAIRES, nbSalaire);
        write(EBPucsFileDefTable.NUMERO_AFFILIE, numeroAffilie);
        write(EBPucsFileDefTable.TOTAL_CONTROLE, totalControle);
        write(EBPucsFileDefTable.SIZE_FILE_IN_KO, sizeFileInKo);
        write(EBPucsFileDefTable.IS_AF_SEUL, afSeul);
        write(EBPucsFileDefTable.AFFILIATION_EXISTANTE, affiliationExistante);
        write(EBPucsFileDefTable.NIVEAU_SECURITE, niveauSecurite);
        write(EBPucsFileDefTable.PROVENANCE, provenance);
        write(EBPucsFileDefTable.DATE_RECEPTION, dateReception);
        write(EBPucsFileDefTable.DUPLICATE, duplicate);
        write(EBPucsFileDefTable.SAL_INF_LIMIT, salaireInferieurLimite);
    }

    @Override
    protected void readProperties() {
        id = this.read(EBPucsFileDefTable.ID);
        idJob = this.readString(EBPucsFileDefTable.ID_JOB);
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
        affiliationExistante = read(EBPucsFileDefTable.AFFILIATION_EXISTANTE);
        niveauSecurite = read(EBPucsFileDefTable.NIVEAU_SECURITE);
        provenance = read(EBPucsFileDefTable.PROVENANCE);
        dateReception = read(EBPucsFileDefTable.DATE_RECEPTION);
        duplicate = read(EBPucsFileDefTable.DUPLICATE);
        salaireInferieurLimite = read(EBPucsFileDefTable.SAL_INF_LIMIT);
    }

    public void retrieveWithFile() throws Exception {
        this.retrieve();
        inputStream = readInputStream();
        // FileInputStream fileInputStream = new FileInputStream(file);
        // File f = new File("D:\\Temp\\ebu\\" + id + ".xml");
        // FileOutputStream fileOutputStream = new FileOutputStream(f);
        // fileOutputStream.write((byte[]) object);

        // ByteArrayOutputStream out = new ByteArrayOutputStream();
        // out.write((byte[]) object);
        // outputStream = out;
        // Object object = (InputStream);
        // file = (FileInputStream) BlobMFileWithoutContext(CONST_BLOB + id);
    }

    public InputStream readInputStream() {
        Object object;
        try {
            object = BlobManager.readBlobWithoutContext(CONST_BLOB + id);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream((byte[]) object);
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        // // ByteStreams.toByteArray(new FileInputStream(file));
        // BlobManager.addBlob(CONST_BLOB + id, Files.toByteArray(file));
        // ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf)
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

    public boolean isAffiliationExistante() {
        return affiliationExistante;
    }

    public void setAffiliationExistante(boolean affiliationExistante) {
        this.affiliationExistante = affiliationExistante;
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

    public InputStream getInputStream() {
        return inputStream;
    }

}
