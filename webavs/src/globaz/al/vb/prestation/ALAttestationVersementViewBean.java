/**
 * 
 */
package globaz.al.vb.prestation;

import globaz.al.process.declarationVersement.ALAttestationsVersementEditionProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author pta
 * 
 */
public class ALAttestationVersementViewBean extends BJadePersistentObjectViewBean {

    /**
     * inclure les prestations en paiements direcs
     */
    private boolean avecVersementDirec = true;
    /**
     * inclure les prestations indirects
     */
    private boolean avecVersementIndirect = true;
    /**
     * inclure les prestations à tiers bénéficiaire
     */
    private boolean avecVersementTiersBeneficiaire = true;
    /**
     * Date comptable début
     */
    private String dateDebut = null;
    /**
     * Date comptable fin
     */
    private String dateFin = null;
    /**
     * Document détaillé par allocataire
     */
    private boolean documentDetailleParAllocataire = true;
    /**
     * Document détaillé par période
     */
    private boolean documentDetaillePeriode = true;
    /**
     * email (par défaut celle de l'utilisateur)
     */
    private String email = null;
    /**
     * nss allocataire
     */
    private String nssAllocataire = null;

    /**
     * numéro de l'affilié
     */
    private String numAffilie = null;

    /**
     * Constructeur de la classe
     */
    public ALAttestationVersementViewBean() {
        // TODO Auto-generated constructor stub
        super();

    }

    /**
     * /* (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        ALAttestationsVersementEditionProcess attestationVersment = new ALAttestationsVersementEditionProcess();
        attestationVersment.setAvecVersementDirec(avecVersementDirec);
        attestationVersment.setAvecVersementIndirect(avecVersementIndirect);
        attestationVersment.setAvecVersementTiersBeneficiaire(avecVersementTiersBeneficiaire);
        attestationVersment.setDateDebut(dateDebut);
        attestationVersment.setDateFin(dateFin);
        attestationVersment.setDocumentDetailleParAllocataire(documentDetailleParAllocataire);
        attestationVersment.setDocumentNonDetaillePeriode(documentDetaillePeriode);
        attestationVersment.setEmail(getEmail());
        attestationVersment.setNssAllocataire(nssAllocataire);
        attestationVersment.setNumAffilie(numAffilie);

        attestationVersment.setSession(getSession());
        BProcessLauncher.start(attestationVersment, false);

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }
        return email;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the nssAllocataire
     */
    public String getNssAllocataire() {
        return nssAllocataire;
    }

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the avecVersementDirec
     */
    public boolean isAvecVersementDirec() {
        return avecVersementDirec;
    }

    /**
     * @return the avecVersementIndirect
     */
    public boolean isAvecVersementIndirect() {
        return avecVersementIndirect;
    }

    /**
     * @return the avecVersementTiersBeneficiaire
     */
    public boolean isAvecVersementTiersBeneficiaire() {
        return avecVersementTiersBeneficiaire;
    }

    /**
     * @return the documentDetailleParAllocataire
     */
    public boolean isDocumentDetailleParAllocataire() {
        return documentDetailleParAllocataire;
    }

    /**
     * @return the documentDetaillePeriode
     */
    public boolean isDocumentDetaillePeriode() {
        return documentDetaillePeriode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");
    }

    /**
     * @param avecVersementDirec
     *            the avecVersementDirec to set
     */
    public void setAvecVersementDirec(boolean avecVersementDirec) {
        this.avecVersementDirec = avecVersementDirec;
    }

    /**
     * @param avecVersementIndirect
     *            the avecVersementIndirect to set
     */
    public void setAvecVersementIndirect(boolean avecVersementIndirect) {
        this.avecVersementIndirect = avecVersementIndirect;
    }

    /**
     * @param avecVersementTiersBeneficiaire
     *            the avecVersementTiersBeneficiaire to set
     */
    public void setAvecVersementTiersBeneficiaire(boolean avecVersementTiersBeneficiaire) {
        this.avecVersementTiersBeneficiaire = avecVersementTiersBeneficiaire;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param documentDetailleParAllocataire
     *            the documentDetailleParAllocataire to set
     */
    public void setDocumentDetailleParAllocataire(boolean documentDetailleParAllocataire) {
        this.documentDetailleParAllocataire = documentDetailleParAllocataire;
    }

    /**
     * @param documentDetaillePeriode
     *            the documentDetaillePeriode to set
     */
    public void setDocumentDetaillePeriode(boolean documentDetaillePeriode) {
        this.documentDetaillePeriode = documentDetaillePeriode;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    /**
     * @param nssAllocataire
     *            the nssAllocataire to set
     */
    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");
    }

}
