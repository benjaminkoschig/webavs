package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCO
 */
public class OrdreSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String codeISOMonnaieBonification;
    public String codeISOMonnaieDepot;
    public String idAdressePaiement;
    public String idRubrique;
    public String motif;
    public String natureOrdre;
    public String typeOrdre;
    public String typeVirement;

    public String getCodeISOMonnaieBonification() {
        return codeISOMonnaieBonification;
    }

    public String getCodeISOMonnaieDepot() {
        return codeISOMonnaieDepot;
    }

    @Override
    public String getId() {
        return getIdRubrique();
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getMotif() {
        return motif;
    }

    public String getNatureOrdre() {
        return natureOrdre;
    }

    public String getTypeOrdre() {
        return typeOrdre;
    }

    public String getTypeVirement() {
        return typeVirement;
    }

    public void setCodeISOMonnaieBonification(String codeISOMonnaieBonification) {
        this.codeISOMonnaieBonification = codeISOMonnaieBonification;
    }

    public void setCodeISOMonnaieDepot(String codeISOMonnaieDepot) {
        this.codeISOMonnaieDepot = codeISOMonnaieDepot;
    }

    @Override
    public void setId(String id) {
        setIdRubrique(id);
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNatureOrdre(String natureOrdre) {
        this.natureOrdre = natureOrdre;
    }

    public void setTypeOrdre(String typeOrdre) {
        this.typeOrdre = typeOrdre;
    }

    public void setTypeVirement(String typeVirement) {
        this.typeVirement = typeVirement;
    }

}
