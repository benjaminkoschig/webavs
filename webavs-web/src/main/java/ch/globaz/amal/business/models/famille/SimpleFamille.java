package ch.globaz.amal.business.models.famille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class SimpleFamille extends JadeSimpleModel implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeTraitementDossier = null;
    private String dateAvisRIP = null;
    private String dateNaissance = null;
    private String finDefinitive = null;
    private String idContribuable = null;
    private String idFamille = null;
    private String idTier = null;
    private Boolean isContribuable = null;
    private String nnssContribuable = null;
    private String noAVS = null;
    private String nomPrenom = null;
    private String nomPrenomUpper = null;
    private String noPersonne = null;
    private String pereMereEnfant = null;
    private String pereMereEnfantLibelle = null;
    private PersonneEtendueComplexModel personneEtendue = null;
    private String sexe = null;
    private String tauxDeductIc = null;
    private String tauxDeductIfd = null;
    private String typeAvisRIP = null;
    private String typeNoPersonne = null;
    private Boolean carteCulture = null;

    public Boolean getCarteCulture() {
        return carteCulture;
    }

    public void setCarteCulture(Boolean carteCulture) {
        this.carteCulture = carteCulture;
    }

    @Override
    public SimpleFamille clone() throws CloneNotSupportedException {
        return (SimpleFamille) super.clone();
    }

    /**
     * @return the codeTraitementDossier
     */
    public String getCodeTraitementDossier() {
        return codeTraitementDossier;
    }

    /**
     * @return the dateAvisRIP
     */
    public String getDateAvisRIP() {
        return dateAvisRIP;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the codeFinDefinitive
     */
    public String getFinDefinitive() {
        return finDefinitive;
    }

    @Override
    public String getId() {
        return idFamille;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idFamilleContribuable
     */
    public String getIdFamille() {
        return idFamille;
    }

    /**
     * @return the idTier
     */
    public String getIdTier() {
        return idTier;
    }

    public Boolean getIsContribuable() {
        return isContribuable;
    }

    /**
     * @return the nnssContribuable
     */
    public String getNnssContribuable() {
        return nnssContribuable;
    }

    /**
     * @return the noAVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * @return the nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return the nomPrenomUpper
     */
    public String getNomPrenomUpper() {
        return nomPrenomUpper;
    }

    /**
     * @return the noPersonne
     */
    public String getNoPersonne() {
        return noPersonne;
    }

    /**
     * Gets the nss from personne etendue, if possible
     * 
     * @return
     */
    public String getNssFromPersonneEtendue() {
        String csReturn = "";
        try {
            csReturn = getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
            if (csReturn == null) {
                csReturn = "";
            }
        } catch (Exception ex) {
            csReturn = "";
        }
        return csReturn;
    }

    /**
     * @return the pereMereEnfant
     */
    public String getPereMereEnfant() {
        return pereMereEnfant;
    }

    /**
     * @return the pereMereEnfantLibelle
     */
    public String getPereMereEnfantLibelle() {
        return pereMereEnfantLibelle;
    }

    /**
     * Gets the personne etendue linked to the current member
     * 
     * @return
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        if (!JadeStringUtil.isEmpty(getIdTier()) && (personneEtendue == null)) {
            try {
                personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().read(getIdTier());
            } catch (Exception ex) {
                personneEtendue = new PersonneEtendueComplexModel();
            }
        } else if (personneEtendue == null) {
            personneEtendue = new PersonneEtendueComplexModel();
        }
        return personneEtendue;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    public String getTauxDeductIc() {
        return tauxDeductIc;
    }

    public String getTauxDeductIfd() {
        return tauxDeductIfd;
    }

    /**
     * @return the typeAvisRIP
     */
    public String getTypeAvisRIP() {
        return typeAvisRIP;
    }

    /**
     * @return the typeNoPersonne
     */
    public String getTypeNoPersonne() {
        return typeNoPersonne;
    }

    public boolean isEnfant() {
        return IAMCodeSysteme.CS_TYPE_ENFANT.equals(getPereMereEnfant());
    }

    public boolean isMere() {
        return IAMCodeSysteme.CS_TYPE_MERE.equals(getPereMereEnfant());
    }

    public boolean isPere() {
        return IAMCodeSysteme.CS_TYPE_PERE.equals(getPereMereEnfant());
    }

    /**
     * @param codeTraitementDossier
     *            the codeTraitementDossier to set
     */
    public void setCodeTraitementDossier(String codeTraitementDossier) {
        this.codeTraitementDossier = codeTraitementDossier;
    }

    /**
     * @param dateAvisRIP
     *            the dateAvisRIP to set
     */
    public void setDateAvisRIP(String dateAvisRIP) {
        this.dateAvisRIP = dateAvisRIP;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param codeFinDefinitive
     *            the codeFinDefinitive to set
     */
    public void setFinDefinitive(String finDefinitive) {
        this.finDefinitive = finDefinitive;
    }

    @Override
    public void setId(String id) {
        idFamille = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idFamilleContribuable
     *            the idFamilleContribuable to set
     */
    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    /**
     * @param idTier
     *            the idTier to set
     */
    public void setIdTier(String idTier) {
        this.idTier = idTier;
    }

    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    /**
     * @param nnssContribuable
     *            the nnssContribuable to set
     */
    public void setNnssContribuable(String nnssContribuable) {
        this.nnssContribuable = nnssContribuable;
    }

    /**
     * @param noAVS
     *            the noAVS to set
     */
    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    /**
     * @param nomPrenom
     *            the nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * @param nomPrenomUpper
     *            the nomPrenomUpper to set
     */
    public void setNomPrenomUpper(String nomPrenomUpper) {
        this.nomPrenomUpper = nomPrenomUpper;
    }

    /**
     * @param noPersonne
     *            the noPersonne to set
     */
    public void setNoPersonne(String noPersonne) {
        this.noPersonne = noPersonne;
    }

    /**
     * @param pereMereEnfant
     *            the pereMereEnfant to set
     */
    public void setPereMereEnfant(String pereMereEnfant) {
        this.pereMereEnfant = pereMereEnfant;
    }

    /**
     * @param pereMereEnfantLibelle
     *            the pereMereEnfantLibelle to set
     */
    public void setPereMereEnfantLibelle(String pereMereEnfantLibelle) {
        this.pereMereEnfantLibelle = pereMereEnfantLibelle;
    }

    /**
     * @param sexe
     *            the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setTauxDeductIc(String tauxDeductIc) {
        this.tauxDeductIc = tauxDeductIc;
    }

    public void setTauxDeductIfd(String tauxDeductIfd) {
        this.tauxDeductIfd = tauxDeductIfd;
    }

    /**
     * @param typeAvisRIP
     *            the typeAvisRIP to set
     */
    public void setTypeAvisRIP(String typeAvisRIP) {
        this.typeAvisRIP = typeAvisRIP;
    }

    /**
     * @param typeNoPersonne
     *            the typeNoPersonne to set
     */
    public void setTypeNoPersonne(String typeNoPersonne) {
        this.typeNoPersonne = typeNoPersonne;
    }
}
