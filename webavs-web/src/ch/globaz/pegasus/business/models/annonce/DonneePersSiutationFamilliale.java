package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;

public class DonneePersSiutationFamilliale extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeCommuneOFS = null;
    private String communeOrigine = null;
    private String csLienRepondant = null;
    private String csPermis = null;
    private String idDroitMembreFamille = null;
    private String idTiers = null;
    private String idTiersRepondant = null;

    public String getCodeCommuneOFS() {
        return codeCommuneOFS;
    }

    public String getCsLienRepondant() {
        return csLienRepondant;
    }

    public String getCsPermis() {
        return csPermis;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersRepondant() {
        return idTiersRepondant;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCodeCommuneOFS(String codeCommuneOFS) {
        this.codeCommuneOFS = codeCommuneOFS;
    }

    public void setCsLienRepondant(String csLienRepondant) {
        this.csLienRepondant = csLienRepondant;
    }

    public void setCsPermis(String csPermis) {
        this.csPermis = csPermis;
    }

    @Override
    public void setId(String id) {

    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersRepondant(String idTiersRepondant) {
        this.idTiersRepondant = idTiersRepondant;
    }

    @Override
    public void setSpy(String spy) {

    }

    public String getCommuneOrigine() {
        return communeOrigine;
    }

    public void setCommuneOrigine(String communeOrigine) {
        this.communeOrigine = communeOrigine;
    }

}
