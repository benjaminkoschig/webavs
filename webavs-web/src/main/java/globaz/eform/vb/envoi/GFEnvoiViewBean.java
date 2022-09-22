package globaz.eform.vb.envoi;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.commons.nss.NSUtil;
import globaz.eform.translation.CodeSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeComplexModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class GFEnvoiViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFEnvoiViewBean.class);

    private GFDaDossierModel daDossier = new GFDaDossierModel();
    private String filename;
    private String nomGestionnaire;
    private String nomDepartement;
    private String telephoneGestionnaire;
    private String emailGestionnaire;
    private String nomAssure;
    private String prenomAssure;
    private String dateNaissance;
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
        return daDossier.getId();
    }
    @Override
    public void setId(String newId) {
        daDossier.setId(newId);
    }

    public GFDaDossierModel getDaDossier() {
        return daDossier;
    }

    public void setDaDossier(GFDaDossierModel daDossier) {
        this.daDossier = daDossier;
    }

    public String getNss() {
        setAffilier();
        return NSSUtils.formatNss(daDossier.getNssAffilier());
    }

    public void setNss(String nss) {
        daDossier.setNssAffilier(NSSUtils.unFormatNss(nss));
        nomAssure = null;
        prenomAssure = null;
        dateNaissance = null;
        setAffilier();
    }

    private void setAffilier() {
        if (!StringUtils.isBlank(daDossier.getNssAffilier()) && StringUtils.isBlank(nomAssure)) {
            PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
            ts.setForNumeroAvsActuel(NSSUtils.formatNss(daDossier.getNssAffilier()));
            try {
                ts = TIBusinessServiceLocator.getPersonneEtendueService().find(ts);

                if (ts.getSize() == 1) {
                    PersonneEtendueComplexModel model = (PersonneEtendueComplexModel) ts.getSearchResults()[0];

                    nomAssure = model.getTiers().getDesignation1();
                    prenomAssure = model.getTiers().getDesignation2();
                    dateNaissance = model.getPersonne().getDateNaissance();
                }

            } catch (JadePersistenceException | JadeApplicationException e) {
                throw new RuntimeException(e);
            }
        }
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

    public String getCodeCaisse() {
        try {
            if (!StringUtils.isBlank(daDossier.getCodeCaisse())) {
                AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
                search.setForCodeAdministration(daDossier.getCodeCaisse());
                search.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_COMP);

                search = TIBusinessServiceLocator.getAdministrationService().find(search);

                if (search.getSearchResults().length == 1) {
                    AdministrationComplexModel complexModel = (AdministrationComplexModel) search.getSearchResults()[0];
                    return complexModel.getAdmin().getCodeAdministration() + " - " +
                            complexModel.getTiers().getDesignation1() + " " +
                            complexModel.getTiers().getDesignation2();
                }
            }

            return "";
        } catch (JadePersistenceException | JadeApplicationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCodeCaisse(String codeCaisse) {
        daDossier.setCodeCaisse(StringUtils.isBlank(codeCaisse) ? null : codeCaisse.split(" - ")[0]);
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

