package ch.globaz.eform.services.sedex.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDate;

@Getter
@Setter
public class GFSedexModel {
    private String messageId;
    private String messageSubject;
    private LocalDate messageDate;
    private String nomBeneficiaire;
    private String prenomBenefiaicaire;
    private String nssBeneficiaire;
    private LocalDate naissanceBeneficiaire;
    private File zipFile;
    private String zipFilePath;
}
