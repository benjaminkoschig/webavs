package globaz.naos.vb.attestation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;

/**
 * ViewBean pour l'écran de lancement de la génération de l'attestation de paiement des charges sociales
 * 
 * @author bjo
 * 
 */
public class AFAttestationChaSocProcessViewBean implements FWViewBeanInterface, BIPersistentObject {
    public static final int NB_JOUR_VALIDITE = 30;
    private AFAffiliation affiliation;
    private String dateAttestation;
    private String dateValidite;
    private String email;
    private String idAffiliation;
    private String message;
    private String msgType;
    private String nombreExemplaire;
    private Boolean paiementRegulier;
    private BSession session;

    private String titre;

    /**
     * @throws Exception
     */
    public AFAttestationChaSocProcessViewBean() throws Exception {
        super();
        dateAttestation = JACalendar.todayJJsMMsAAAA();
        dateValidite = AFAffiliationUtil.addDaysToDate(JACalendar.today(),
                AFAttestationChaSocProcessViewBean.NB_JOUR_VALIDITE);
        nombreExemplaire = "1";
        paiementRegulier = new Boolean(true);
        affiliation = new AFAffiliation();
    }

    @Override
    public void add() throws Exception {
        throw new Exception("method add is not implemented for this viewBean");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("method delete is not implemented for this viewBean");
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

    public String getEmail() {
        return email;
    }

    @Override
    public String getId() {
        return getIdAffiliation();
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getNombreExemplaire() {
        return nombreExemplaire;
    }

    public Boolean getPaiementRegulier() {
        return paiementRegulier;
    }

    public BSession getSession() {
        return session;
    }

    public String getTitre() {
        return titre;
    }

    /*
     * (non-Javadoc) Charge entierement le viewBean pour l'affichage
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        setAffiliation(AFAffiliationUtil.getAffiliation(getIdAffiliation(), getSession()));
        setEmail(getSession().getUserEMail());
        // recherche du titre dans le tiers affilié
        setTitre(getSession().getCodeLibelle(getAffiliation().getTiers().getTitreTiers()));
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

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(String id) {
        idAffiliation = id;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    @Override
    public void setISession(BISession newSession) {
        session = (BSession) newSession;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setNombreExemplaire(String nombreExemplaire) {
        this.nombreExemplaire = nombreExemplaire;
    }

    public void setPaiementRegulier(Boolean paiementRegulier) {
        this.paiementRegulier = paiementRegulier;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public void update() throws Exception {
        throw new Exception("method update is not implemented for this viewBean");
    }
}
