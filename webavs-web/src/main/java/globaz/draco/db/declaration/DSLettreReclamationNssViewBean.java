package globaz.draco.db.declaration;

import globaz.draco.vb.DSAbstractPersistentViewBean;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author SCO
 * @since 15 juil. 2011
 */
public class DSLettreReclamationNssViewBean extends DSAbstractPersistentViewBean {

    private String annee;
    private String dateDocument;
    private String delaiRappel;
    private String email;
    private String fromAffilie = "";
    private String genreEdition;
    private String observation;
    private String toAffilie = "";
    private String typeDeclaration;
    private String typeDocument;

    public String getAnnee() {
        return annee;
    }

    public String getDateDocument() {
        if (JadeStringUtil.isEmpty(dateDocument)) {
            return JACalendar.todayJJsMMsAAAA();
        }

        return dateDocument;
    }

    public String getDelaiRappel() {
        return delaiRappel;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getFromAffilie() {
        return fromAffilie;
    }

    public String getGenreEdition() {
        return genreEdition;
    }

    /**
     * Get libellé de l'état de la facture
     * 
     * @param code
     * @return
     */
    public String getLibelleTypeDeclaration(String code) {
        String libelle = "";

        if (!JadeStringUtil.isIntegerEmpty(code)) {
            FWParametersUserCode userCode = new FWParametersUserCode();
            userCode.setSession(getSession());
            userCode.setIdCodeSysteme(code);
            userCode.setIdLangue(getSession().getIdLangue());
            try {
                userCode.retrieve();
                if (!userCode.isNew()) {
                    libelle = userCode.getLibelle();
                }
            } catch (Exception e) {
                libelle = "";
            }
        }

        return libelle;
    }

    public String getObservation() {
        return observation;
    }

    public String getToAffilie() {
        return toAffilie;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDelaiRappel(String delaiRappel) {
        this.delaiRappel = delaiRappel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setGenreEdition(String genreEdition) {
        this.genreEdition = genreEdition;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }
}
