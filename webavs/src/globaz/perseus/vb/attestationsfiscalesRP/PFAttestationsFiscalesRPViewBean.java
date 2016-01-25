package globaz.perseus.vb.attestationsfiscalesRP;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.utils.ged.PRGedUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.models.dossier.Dossier;

public class PFAttestationsFiscalesRPViewBean extends BJadePersistentObjectViewBean {

    // Properties de la caisse principale
    private final static String CAISSE_PRINCIPALE_CODE_POSTALE_CAISSE = "caisse.principale.codePostale.caisse.FR";
    private final static String CAISSE_PRINCIPALE_LIEU_CAISSE = "caisse.principale.lieu.caisse.FR";
    private final static String CAISSE_PRINCIPALE_NOM_CAISSE = "caisse.principale.nom.caisse.FR";
    private final static String CAISSE_PRINCIPALE_NOM_PERSONNE_CONTACT = "caisse.principale.nom.personneContact.FR";
    private final static String CAISSE_PRINCIPALE_PRENOM_PERSONNE_CONTACT = "caisse.principale.prenom.personneContact.FR";
    private final static String CAISSE_PRINCIPALE_RUE_CAISSE = "caisse.principale.rue.caisse.FR";
    private final static String CAISSE_PRINCIPALE_TEL_PERSONNE_CONTACT = "caisse.principale.tel.personneContact.FR";

    // Properties de la caisse secondaire
    private final static String CAISSE_SECONDAIRE_CODE_POSTALE_CAISSE = "caisse.secondaire.codePostale.caisse.FR";
    private final static String CAISSE_SECONDAIRE_LIEU_CAISSE = "caisse.secondaire.lieu.caisse.FR";
    private final static String CAISSE_SECONDAIRE_NOM_CAISSE = "caisse.secondaire.nom.caisse.FR";
    private final static String CAISSE_SECONDAIRE_NOM_PERSONNE_CONTACT = "caisse.secondaire.nom.personneContact.FR";
    private final static String CAISSE_SECONDAIRE_PRENOM_PERSONNE_CONTACT = "caisse.secondaire.prenom.personneContact.FR";
    private final static String CAISSE_SECONDAIRE_RUE_CAISSE = "caisse.secondaire.rue.caisse.FR";
    private final static String CAISSE_SECONDAIRE_TEL_PERSONNE_CONTACT = "caisse.secondaire.tel.personneContact.FR";

    private String anneeAttestation = null;
    private String caisse = null;
    private String codePostaleCaisse = null;
    private String dateDocument = null;
    private String detailAssure = null;
    private String eMailAdresse = null;
    private boolean hasGed = false;
    private String idDossier = null;
    private String isSendToGed = null;
    private String localiteCaisse = null;
    private String nomCaisse = null;
    private String nomPersonneContact = null;
    private String prenomPersonneContact = null;
    private Map properties = new HashMap();
    private String rueCaisse = null;
    private String telephoneContactCaisse = null;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    public String getAnneeAttestation() {
        if ((anneeAttestation == null) || (anneeAttestation == "")) {
            anneeAttestation = JACalendar.todayJJsMMsAAAA().substring(6);
        }
        return anneeAttestation;
    }

    public String getCaisse() {
        return caisse;
    }

    public String getCodePostaleCaisse() {
        return codePostaleCaisse;
    }

    public String getDateDocument() {
        if (dateDocument == null) {
            dateDocument = JACalendar.todayJJsMMsAAAA();
        }
        return dateDocument;
    }

    public String getDetailAssure() {
        return detailAssure;
    }

    public String geteMailAdresse(String mail) {
        if (eMailAdresse == null) {
            eMailAdresse = mail;
        }
        return eMailAdresse;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    public String getLocaliteCaisse() {
        return localiteCaisse;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public String getNomPersonneContact() {
        return nomPersonneContact;
    }

    public String getPrenomPersonneContact() {
        return prenomPersonneContact;
    }

    public Map getProperties() {
        return properties;
    }

    public String getRueCaisse() {
        return rueCaisse;
    }

    // TODO Récupération de la session user
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTelephoneContactCaisse() {
        return telephoneContactCaisse;
    }

    public void init() throws Exception {
        Dossier dossier = new Dossier();
        dossier.setId(idDossier);
        dossier = (Dossier) JadePersistenceManager.read(dossier);
        setDetailAssure(PFUserHelper.getDetailAssure((BSession) getISession(), dossier.getDemandePrestation()
                .getPersonneEtendue()));
    }

    public boolean isHasGed() {
        return hasGed;
    }

    protected final Properties loadTemplateProperties() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            String filename = "PF_ATTESTATIONSFISCALES_RP";
            int extensionSeparator = filename.indexOf('.');
            if (extensionSeparator >= 0) {
                filename = filename.substring(0, extensionSeparator);
            }
            filename += ".properties";
            inputStream = this.getClass().getResourceAsStream('/' + filename);
            if (inputStream == null) {
                try {
                    inputStream = new FileInputStream(filename);
                } catch (Exception e) {
                    throw new IOException("File '" + filename + "' not found");
                }
            }
        } catch (IOException ioe) {
            inputStream = null;
        }
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (Exception e) {

            }
        }
        Set set = properties.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            String value = (String) properties.get(key);
            getProperties().put(key, value);
        }
        return properties;
    }

    @Override
    public void retrieve() throws Exception {
        init();
    }

    public void setAnneeAttestation(String anneeAttestation) {
        this.anneeAttestation = anneeAttestation;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setCodePostaleCaisse(String codePostaleCaisse) {
        this.codePostaleCaisse = codePostaleCaisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDetailAssure(String detailAssure) {
        this.detailAssure = detailAssure;
    }

    public void setDonneeCaisse(String csCaisse) {
        loadTemplateProperties();
        if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
            setNomCaisse((String) getProperties().get(PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_NOM_CAISSE));
            setRueCaisse((String) getProperties().get(PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_RUE_CAISSE));
            setCodePostaleCaisse((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_CODE_POSTALE_CAISSE));
            setLocaliteCaisse((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_LIEU_CAISSE));
            setPrenomPersonneContact((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_PRENOM_PERSONNE_CONTACT));
            setNomPersonneContact((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_NOM_PERSONNE_CONTACT));
            setTelephoneContactCaisse((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_PRINCIPALE_TEL_PERSONNE_CONTACT));
            setHasGed(PRGedUtils.isDocumentInGed(IPRConstantesExternes.PCF_ATTESTATION_FISCALE_RP, csCaisse,
                    getSession()));
        } else {
            setNomCaisse((String) getProperties().get(PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_NOM_CAISSE));
            setRueCaisse((String) getProperties().get(PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_RUE_CAISSE));
            setCodePostaleCaisse((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_CODE_POSTALE_CAISSE));
            setLocaliteCaisse((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_LIEU_CAISSE));
            setPrenomPersonneContact((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_PRENOM_PERSONNE_CONTACT));
            setNomPersonneContact((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_NOM_PERSONNE_CONTACT));
            setTelephoneContactCaisse((String) getProperties().get(
                    PFAttestationsFiscalesRPViewBean.CAISSE_SECONDAIRE_TEL_PERSONNE_CONTACT));
            setHasGed(PRGedUtils.isDocumentInGed(IPRConstantesExternes.PCF_ATTESTATION_FISCALE_RP, csCaisse,
                    getSession()));
        }
    }

    public void seteMailAdresse(String eMailAdresse) {
        this.eMailAdresse = eMailAdresse;
    }

    public void setHasGed(boolean hasGed) {
        this.hasGed = hasGed;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setLocaliteCaisse(String localiteCaisse) {
        this.localiteCaisse = localiteCaisse;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    public void setNomPersonneContact(String nomPersonneContact) {
        this.nomPersonneContact = nomPersonneContact;
    }

    public void setPrenomPersonneContact(String prenomPersonneContact) {
        this.prenomPersonneContact = prenomPersonneContact;
    }

    public void setRueCaisse(String rueCaisse) {
        this.rueCaisse = rueCaisse;
    }

    public void setTelephoneContactCaisse(String telephoneContactCaisse) {
        this.telephoneContactCaisse = telephoneContactCaisse;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }
}
