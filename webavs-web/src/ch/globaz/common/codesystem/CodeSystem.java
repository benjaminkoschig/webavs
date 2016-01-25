package ch.globaz.common.codesystem;

import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

/**
 * Cette classe permet de représenter un objet codesystem
 * 
 * Elle agit comme un simple javabean.
 * 
 * Fonctionne avec l'ancienne persistance.
 * 
 * @author jwe
 * 
 */
public class CodeSystem implements JadeCodeSysteme {

    private static final long serialVersionUID = 188044769968959122L;

    private String traduction;
    private String idCodeSysteme;
    private String ordre;
    private String codeUtilisateur;

    @Override
    public String getCodeUtilisateur(Langues langue) {
        return codeUtilisateur;
    }

    public String getCodeUtilisateur() {
        return codeUtilisateur;
    }

    @Override
    public String getIdCodeSysteme() {
        return idCodeSysteme;
    }

    @Override
    public String getOrdre() {
        return ordre;
    }

    @Override
    public String getTraduction(Langues langue) {
        return traduction;
    }

    public String getTraduction() {
        return traduction;
    }

    public void setCodeUtilisateur(String codeUtilisateur) {
        this.codeUtilisateur = codeUtilisateur;
    }

    public void setIdCodeSysteme(String idCodeSysteme) {
        this.idCodeSysteme = idCodeSysteme;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public void setTraduction(String traduction) {
        this.traduction = traduction;
    }
}
