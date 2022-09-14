package globaz.eform.vb.envoi;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class GFEnvoiViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFEnvoiViewBean.class);

    private String id;
    private String nss = "";
    private String filename;
    private String nomGestionnaire;
    private String nomDepartement;
    private String telephoneGestionnaire;
    private String emailGestionnaire;
    private String nomAssure;
    private String prenomAssure;
    private String dateNaissance;
    private String caisseDestinatrice;
    private String typeDeFichier;
    private String adresse;

    private List<String> fileNameList = new LinkedList<>();
    private List<String> errorFileNameList = new LinkedList<>();
    @Getter
    @Setter
    private String folderUid = JadeUUIDGenerator.createLongUID().toString();
    @Getter
    @Setter
    private String fileNamePersistance;

    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String newId) {
        id = newId;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {

        this.filename = filename;
    }

    public String getNomGestionnaire() {
        return nomGestionnaire;
    }

    public void setNomGestionnaire(String nomGestionnaire) {
        this.nomGestionnaire = nomGestionnaire;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public String getTelephoneGestionnaire() {
        return telephoneGestionnaire;
    }

    public void setTelephoneGestionnaire(String telephoneGestionnaire) {
        this.telephoneGestionnaire = telephoneGestionnaire;
    }

    public String getEmailGestionnaire() {
        return emailGestionnaire;
    }

    public void setEmailGestionnaire(String emailGestionnaire) {
        this.emailGestionnaire = emailGestionnaire;
    }

    public String getNomAssure() {
        return nomAssure;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    public String getPrenomAssure() {
        return prenomAssure;
    }

    public void setPrenomAssure(String prenomAssure) {
        this.prenomAssure = prenomAssure;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCaisseDestinatrice() {
        return caisseDestinatrice;
    }

    public void setCaisseDestinatrice(String caisseDestinatrice) {
        this.caisseDestinatrice = caisseDestinatrice;
    }

    public String getTypeDeFichier() {
        return typeDeFichier;
    }

    public void setTypeDeFichier(String typeDeFichier) {
        this.typeDeFichier = typeDeFichier;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }

    public List<String> getErrorFileNameList() {
        return errorFileNameList;
    }

    public void setErrorFileNameList(List<String> errorFileNameList) {
        this.errorFileNameList = errorFileNameList;
    }

    public String getFileNamePersistance() {
        return fileNamePersistance;
    }

    public void setFileNamePersistance(String fileNamePersistance) {
        this.fileNamePersistance = fileNamePersistance;
    }

    public GFEnvoiViewBean() {
        super();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }


    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }
    @Override
    public void retrieve() throws Exception {

    }
    @Override
    public void update() throws Exception {

    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    public final String isNNSS() {
        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }
        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }


}

