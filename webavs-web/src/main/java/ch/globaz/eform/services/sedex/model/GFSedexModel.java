package ch.globaz.eform.services.sedex.model;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GFSedexModel {
    private String messageId;
    private String messageSubject;
    private LocalDate messageDate;
    private String formulaireNom;
    private String formulaireType;
    private String nomBeneficiaire;
    private String prenomBenefiaicaire;
    private String nssBeneficiaire;
    private LocalDate naissanceBeneficiaire;
    private String zipFile;
    private String zipFilePath;
    private List<String> attachementFile;
}
