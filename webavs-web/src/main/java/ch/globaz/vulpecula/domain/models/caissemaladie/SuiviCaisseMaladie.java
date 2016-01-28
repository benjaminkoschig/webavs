package ch.globaz.vulpecula.domain.models.caissemaladie;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class SuiviCaisseMaladie implements DomainEntity {
    public static final int TYPE_01_05 = 1;
    public static final int TYPE_06_FA = 2;

    private String id;
    private Travailleur travailleur;
    private Administration caisseMaladie;
    private TypeDocumentCaisseMaladie typeDocument;
    private Date dateEnvoi;
    private boolean isEnvoye;
    private String spy;

    public Administration getCaisseMaladie() {
        return caisseMaladie;
    }

    public void setCaisseMaladie(Administration caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdTravailleur() {
        return travailleur.getId();
    }

    public String getNoAVSTravailleur() {
        return travailleur.getNumAvsActuel();
    }

    public String getNomTravailleur() {
        return travailleur.getDesignation1();
    }

    public String getPrenomTravailleur() {
        return travailleur.getDesignation2();
    }

    public String getLibelleCaisseMaladie() {
        if (caisseMaladie.getDesignation2().length() > 0) {
            return caisseMaladie.getDesignation1() + " " + caisseMaladie.getDesignation2();
        }
        return caisseMaladie.getDesignation1();
    }

    public String getIdCaisseMaladie() {
        if (caisseMaladie == null) {
            return null;
        }
        return caisseMaladie.getIdTiers();
    }

    public TypeDocumentCaisseMaladie getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocumentCaisseMaladie typeDocument) {
        this.typeDocument = typeDocument;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public boolean isEnvoye() {
        return isEnvoye;
    }

    public void setEnvoye(boolean isEnvoye) {
        this.isEnvoye = isEnvoye;
    }

}
