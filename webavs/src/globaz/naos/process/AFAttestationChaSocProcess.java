package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.itext.attestation.AFAttestationChaSoc_Doc;

public class AFAttestationChaSocProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation affiliation;
    private String dateAttestation;
    private String dateValidite;
    private String nombreExemplaire;
    private Boolean paiementRegulier;
    private String titre;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        System.out.println("Lancement du process!!!");
        AFAttestationChaSoc_Doc attesationDoc = new AFAttestationChaSoc_Doc();
        return true;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlankOrZero(dateAttestation)) {
            this._addError(getTransaction(), getSession().getLabel("VAL_DATE_ATT_CHARGES_SOCIALES"));
        }
        if (JadeStringUtil.isBlankOrZero(dateValidite)) {
            this._addError(getTransaction(), getSession().getLabel("VAL_DATE_VALID_ATT_CHARGES_SOCIALES"));
        }
        if (JadeStringUtil.isBlankOrZero(nombreExemplaire)) {
            this._addError(getTransaction(), getSession().getLabel("VAL_NB_EXEMPLAIRE_ATT_CHARGES_SOCIALES"));
        }
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public String getDateAttestation() {
        return dateAttestation;
    }

    public String getDateValidite() {
        return dateValidite;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("MSG_ATTESTATION_CHARGES_SOCIALES_ECHOUE");
        } else {
            return getSession().getLabel("MSG_ATTESTATION_CHARGES_SOCIALES_REUSSI");
        }
    }

    public String getNombreExemplaire() {
        return nombreExemplaire;
    }

    public Boolean getPaiementRegulier() {
        return paiementRegulier;
    }

    public String getTitre() {
        return titre;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setDateAttestation(String dateAttestation) {
        this.dateAttestation = dateAttestation;
    }

    public void setDateValidite(String dateValidite) {
        this.dateValidite = dateValidite;
    }

    public void setNombreExemplaire(String nombreExemplaire) {
        this.nombreExemplaire = nombreExemplaire;
    }

    public void setPaiementRegulier(Boolean paiementRegulier) {
        this.paiementRegulier = paiementRegulier;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

}
