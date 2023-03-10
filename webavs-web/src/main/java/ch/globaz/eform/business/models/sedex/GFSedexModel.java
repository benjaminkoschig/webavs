package ch.globaz.eform.business.models.sedex;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GFSedexModel {
    private String messageId;
    private String businessProcessId;
    private String messageSubject;
    private LocalDate messageDate;
    private String formulaireNom;
    private String formulaireType;
    private String formulaireSubType;
    private String nomBeneficiaire;
    private String prenomBenefiaicaire;
    private String nssBeneficiaire;
    private LocalDate naissanceBeneficiaire;
    private String userGestionnaire;
    private String attachementName;
    private byte[] zipFile;
}
