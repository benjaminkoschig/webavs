package ch.globaz.orion.business.models.pucs;

import java.io.File;
import java.io.Serializable;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;

public class PucsFile implements Serializable {

    private static final long serialVersionUID = 1L;
    private String anneeDeclaration = null;
    private EtatPucsFile currentStatus = null;
    private String dateDeReception = null;
    private String handlingUser = null;
    private String filename = null;
    private String nbSalaires = null;
    private String nomAffilie = null;
    private String numeroAffilie = null;
    private Boolean salaireInferieurLimite = null;
    private String totalControle = null;
    private DeclarationSalaireProvenance provenance = null;
    private double sizeFileInKo = 0;
    private boolean isAfSeul = false;
    private boolean isForTest = false;
    private boolean lock;
    private boolean isAffiliationExistante = false;
    private boolean duplicate = false;
    private String idDb;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isAffiliationExistante() {
        return isAffiliationExistante;
    }

    public void setIsAffiliationExistante(boolean isAffiliationExistante) {
        this.isAffiliationExistante = isAffiliationExistante;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isForTest() {
        return isForTest;
    }

    public void setForTest(boolean isForTest) {
        this.isForTest = isForTest;
    }

    public String getAnneeDeclaration() {
        return anneeDeclaration;
    }

    public boolean isStatusHandling() {
        return PucsSearchCriteria.CS_HANDLING.equals(currentStatus);
    }

    public String getDateDeReception() {
        return dateDeReception;
    }

    public String getHandlingUser() {
        return handlingUser;
    }

    public String getFilename() {
        return filename;
    }

    public String getNbSalaires() {
        return nbSalaires;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public Boolean hasSalaireInferieurLimite() {
        return salaireInferieurLimite;
    }

    public String getTotalControle() {
        return totalControle;
    }

    public void setAnneeDeclaration(String anneeDeclaration) {
        this.anneeDeclaration = anneeDeclaration;
    }

    public EtatPucsFile getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EtatPucsFile etat) {
        currentStatus = etat;
    }

    public void setDateDeReception(String dateDeReception) {
        this.dateDeReception = dateDeReception;
    }

    public void setHandlingUser(String handlingUser) {
        this.handlingUser = handlingUser;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setNbSalaires(String nbSalaires) {
        this.nbSalaires = nbSalaires;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setSalaireInferieurLimite(Boolean salaireInferieurLimite) {
        this.salaireInferieurLimite = salaireInferieurLimite;
    }

    public void setTotalControle(String totalControle) {
        this.totalControle = totalControle;
    }

    public DeclarationSalaireProvenance getProvenance() {
        return provenance;
    }

    public void setProvenance(DeclarationSalaireProvenance provenance) {
        this.provenance = provenance;
    }

    public double getSizeFileInKo() {
        return sizeFileInKo;
    }

    public void setSizeFileInKo(double sizeFileInKo) {
        this.sizeFileInKo = sizeFileInKo;
    }

    public boolean isAfSeul() {
        return isAfSeul;
    }

    public void setAfSeul(boolean isAfSeul) {
        this.isAfSeul = isAfSeul;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public String getIdDb() {
        return idDb;
    }

    public void setIdDb(String idDb) {
        this.idDb = idDb;
    }

    public void setAffiliationExistante(boolean isAffiliationExistante) {
        this.isAffiliationExistante = isAffiliationExistante;
    }

    public boolean isSwissDec() {
        return provenance.isSwissDec();
    }

    public boolean isRefuse() {
        return currentStatus.isRefuse();
    }

    public boolean isEditable() {
        return currentStatus.isEditable();
    }

    public boolean isATraiter() {
        return currentStatus.isATraiter();
    }

    public boolean isTraitable() {
        return currentStatus.isTraitable();
    }

    @Override
    public String toString() {
        return "PucsFile [anneeDeclaration=" + anneeDeclaration + ", currentStatus=" + currentStatus
                + ", dateDeReception=" + dateDeReception + ", handlingUser=" + handlingUser + ", filename=" + filename
                + ", nbSalaires=" + nbSalaires + ", nomAffilie=" + nomAffilie + ", numeroAffilie=" + numeroAffilie
                + ", salaireInferieurLimite=" + salaireInferieurLimite + ", totalControle=" + totalControle
                + ", provenance=" + provenance + ", sizeFileInKo=" + sizeFileInKo + ", isAfSeul=" + isAfSeul
                + ", isForTest=" + isForTest + ", lock=" + lock + ", isAffiliationExistante=" + isAffiliationExistante
                + ", duplicate=" + duplicate + "]";
    }
}
