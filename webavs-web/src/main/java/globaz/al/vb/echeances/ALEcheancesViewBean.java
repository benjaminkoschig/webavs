/**
 * 
 */
package globaz.al.vb.echeances;

import globaz.al.process.echeances.ALEcheancesExcelProcess;
import globaz.al.process.echeances.ALEcheancesImprimerProcess;
import globaz.al.process.echeances.ALProtocoleEcheancesProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.constantes.enumerations.echeances.ALEnumDocumentGroup;
import ch.globaz.al.business.constantes.enumerations.echeances.ALEnumDocumentMode;

/**
 * ViewBean gérant le modèle permettant l'impression d'échéances et la création de listes provisoires
 * 
 * @author PTA
 * 
 */
public class ALEcheancesViewBean extends BJadePersistentObjectViewBean {

    /**
     * prendre en compte les dossiers adi
     */
    private Boolean adiExclu = false;

    /**
     * indique si la copie pour les allocataires doit être imprimée pour les dossiers à tiers bénéficiaire
     */
    private Boolean copieAllocPourDossierBeneficiaire = false;

    /**
     * date d'échéance
     */
    private String date = null;

    /**
     * type de liste à traiter (listes provisoire ou traitement définitifs)
     */
    private String typeAvis = null;
    
    private String typeDocument = null;
    
    private String groupPar = null;
    
    private String mailSepare = null;
    
    private String groupParPays = null;

    /**
     * Constucteur
     */
    public ALEcheancesViewBean() {
        super();
        setDate(date);
        setTypeAvis(typeAvis);
        setAdiExclu(adiExclu);
        setCopieAllocPourDossierBeneficiaire(copieAllocPourDossierBeneficiaire);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

        if (JadeStringUtil.equals(typeAvis, ALConstEcheances.LISTE_IMPRESSION, false)) {
            ALEcheancesImprimerProcess processImprimerEcheances = new ALEcheancesImprimerProcess();
            processImprimerEcheances.setDateEcheance(date);
            processImprimerEcheances.setSession(getSession());
            processImprimerEcheances.setAdiExclu(adiExclu);
            processImprimerEcheances.setCopieAllocPourDossierBeneficiaire(copieAllocPourDossierBeneficiaire);
            processImprimerEcheances.setGroupParPays("on".equals(groupParPays));
            BProcessLauncher.start(processImprimerEcheances, false);

        } else if (JadeStringUtil.equals(typeAvis, ALConstEcheances.LISTE_PROVISOIRE, false)) {
            if(ALEnumDocumentMode.EXCEL.getValue().equals(typeDocument)) {
                ALEcheancesExcelProcess processEcheances = new ALEcheancesExcelProcess();
                processEcheances.setDateEcheance(date);
                processEcheances.setAdiExclu(adiExclu);
                processEcheances.setGroupPar(ALEnumDocumentGroup.fromValue(groupPar));
                processEcheances.setMailSepare("on".equals(mailSepare));
                processEcheances.setSession(getSession());
                BProcessLauncher.start(processEcheances, false);
            } else {
                ALProtocoleEcheancesProcess processEcheances = new ALProtocoleEcheancesProcess();
                processEcheances.setDateEcheance(date);
                processEcheances.setAdiExclu(adiExclu);
                processEcheances.setGroupPar(ALEnumDocumentGroup.fromValue(groupPar));
                processEcheances.setMailSepare("on".equals(mailSepare));
                processEcheances.setSession(getSession());
                BProcessLauncher.start(processEcheances, false);
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");
    }

    public Boolean getAdiExlu() {
        return adiExclu;
    }

    public Boolean getCopieAllocPourDossierBeneficiaire() {
        return copieAllocPourDossierBeneficiaire;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return null;
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
        return null;
    }

    /**
     * @return the typeAvis
     */
    public String getTypeAvis() {
        return typeAvis;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");
    }

    public void setAdiExclu(Boolean adiExlu) {
        adiExclu = adiExlu;
    }

    public void setCopieAllocPourDossierBeneficiaire(Boolean copieAllocPourDossierBeneficiaire) {
        this.copieAllocPourDossierBeneficiaire = copieAllocPourDossierBeneficiaire;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // DO NOTHING
    }

    /**
     * @param typeAvis
     *            the typeAvis to set
     */
    public void setTypeAvis(String typeAvis) {
        this.typeAvis = typeAvis;
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

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getGroupPar() {
        return groupPar;
    }

    public void setGroupPar(String groupPar) {
        this.groupPar = groupPar;
    }

    public String getMailSepare() {
        return mailSepare;
    }

    public void setMailSepare(String mailSepare) {
        this.mailSepare = mailSepare;
    }

    public String getGroupParPays() {
        return groupParPays;
    }

    public void setGroupParPays(String groupParPays) {
        this.groupParPays = groupParPays;
    }
    

}
