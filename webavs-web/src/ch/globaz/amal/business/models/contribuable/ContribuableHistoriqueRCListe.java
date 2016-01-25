/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author CBU
 * 
 */
public class ContribuableHistoriqueRCListe extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleContribuableInfos contribuableInfos = null;
    private String dateNaissanceCtbInfo = null;
    private String idContribuableInfo = null;
    private Boolean isTransfered = null;
    private String nnssCtbInfo = null;
    private String nomCtbInfo = null;
    private String nomCtbInfoUpper = null;
    private String numContribuableCtbInfo = null;
    private String prenomCtbInfo = null;
    private String prenomCtbInfoUpper = null;

    public ContribuableHistoriqueRCListe() {
        super();
        contribuableInfos = new SimpleContribuableInfos();
    }

    public ContribuableHistoriqueRCListe(SimpleContribuableInfos contribuableInfos) {
        super();
        this.contribuableInfos = contribuableInfos;
    }

    public SimpleContribuableInfos getContribuableInfos() {
        return contribuableInfos;
    }

    public String getDateNaissanceCtbInfo() {
        return dateNaissanceCtbInfo;
    }

    @Override
    public String getId() {
        return getIdContribuableInfo();
    }

    public String getIdContribuableInfo() {
        return idContribuableInfo;
    }

    public Boolean getIsTransfered() {
        return isTransfered;
    }

    public String getNnssCtbInfo() {
        return nnssCtbInfo;
    }

    public String getNomCtbInfo() {
        return nomCtbInfo;
    }

    public String getNomCtbInfoUpper() {
        return nomCtbInfoUpper;
    }

    public String getNumContribuableCtbInfo() {
        return numContribuableCtbInfo;
    }

    public String getPrenomCtbInfo() {
        return prenomCtbInfo;
    }

    public String getPrenomCtbInfoUpper() {
        return prenomCtbInfoUpper;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setContribuableInfos(SimpleContribuableInfos contribuableInfos) {
        this.contribuableInfos = contribuableInfos;
    }

    public void setDateNaissanceCtbInfo(String dateNaissanceCtbInfo) {
        this.dateNaissanceCtbInfo = dateNaissanceCtbInfo;
    }

    @Override
    public void setId(String id) {
        setIdContribuableInfo(id);
    }

    public void setIdContribuableInfo(String idContribuableInfo) {
        this.idContribuableInfo = idContribuableInfo;
    }

    // public ContribuableHistoriqueRCListe(SimpleContribuable simpleContribuable) {
    // super(simpleContribuable);
    // this.contribuableInfos = new SimpleContribuableInfos();
    // }
    //
    // public ContribuableHistoriqueRCListe(SimpleContribuableInfos contribuableInfos) {
    // super();
    // this.contribuableInfos = contribuableInfos;
    // super.setSimpleContribuable(new SimpleContribuable());
    // }

    public void setIsTransfered(Boolean isTransfered) {
        this.isTransfered = isTransfered;
    }

    public void setNnssCtbInfo(String nnssCtbInfo) {
        this.nnssCtbInfo = nnssCtbInfo;
    }

    public void setNomCtbInfo(String nomCtbInfo) {
        this.nomCtbInfo = nomCtbInfo;
    }

    public void setNomCtbInfoUpper(String nomCtbInfoUpper) {
        this.nomCtbInfoUpper = nomCtbInfoUpper;
    }

    public void setNumContribuableCtbInfo(String numContribuableCtbInfo) {
        this.numContribuableCtbInfo = numContribuableCtbInfo;
    }

    public void setPrenomCtbInfo(String prenomCtbInfo) {
        this.prenomCtbInfo = prenomCtbInfo;
    }

    public void setPrenomCtbInfoUpper(String prenomCtbInfoUpper) {
        this.prenomCtbInfoUpper = prenomCtbInfoUpper;
    }

    @Override
    public void setSpy(String spy) {
    }
}
