package globaz.vulpecula.vb.ap;

import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTFacturationAPViewBean extends BJadeSearchObjectELViewBean {

    private String email;
    private String idEmployeur;
    private String designationEmployeur;
    private String idAssociationProfessionnelle;
    private String designationAssociationProfessionnelle;
    private String annee;
    private String idPassage = "";
    private String libellePassage = "";
    private Boolean replaceFacture;
    private GenreCotisationAssociationProfessionnelle cotiGenre;

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    /**
     * Méthode utilisée par le framework pour setter l'email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public String getEmployeurViewService() {
        return EmployeurServiceCRUD.class.getName();
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getDesignationEmployeur() {
        return designationEmployeur;
    }

    public void setDesignationEmployeur(String designationEmployeur) {
        this.designationEmployeur = designationEmployeur;
    }

    public String getIdAssociationProfessionnelle() {
        return idAssociationProfessionnelle;
    }

    public void setIdAssociationProfessionnelle(String idAssociationProfessionnelle) {
        this.idAssociationProfessionnelle = idAssociationProfessionnelle;
    }

    public String getDesignationAssociationProfessionnelle() {
        return designationAssociationProfessionnelle;
    }

    public void setDesignationAssociationProfessionnelle(String designationAssociationProfessionnelle) {
        this.designationAssociationProfessionnelle = designationAssociationProfessionnelle;
    }

    public String getMessageAnneeManquante() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.MESSAGE_ANNEE_MANQUANTE);
    }

    public String getMessagePassageManquante() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.PT_PASSAGE_MANQUANT);
    }

    public String getMessageConfirmDialog() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_FACTURATION_AP_CONFIRM_DIALOG");
    }

    public Boolean getReplaceFacture() {
        return replaceFacture;
    }

    public void setReplaceFacture(Boolean replaceFacture) {
        this.replaceFacture = replaceFacture;
    }

    public void setGenre(String genre) {
        if (genre != null && genre.length() > 0) {
            cotiGenre = GenreCotisationAssociationProfessionnelle.fromValue(genre);
        }
    }

    public GenreCotisationAssociationProfessionnelle getCotiGenre() {
        return cotiGenre;
    }

    public void init(BISession session) {
        try {
            globaz.musca.api.IFAPassage passage = null;
            // Recherche si séparation indépendant et non-actif - Inforom 314s
            Boolean isSeprationIndNac = false;
            try {
                isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .getProperty(FAApplication.SEPARATION_IND_NA));
            } catch (Exception e) {
                isSeprationIndNac = Boolean.FALSE;
            }
            if (isSeprationIndNac) {
                passage = ServicesFacturation.getProchainPassageFacturation((BSession) session, null,
                        FAModuleFacturation.CS_MODULE_ASSOCIATIONS_PROF);
            } else {
                passage = ServicesFacturation.getProchainPassageFacturation((BSession) session, null,
                        globaz.musca.db.facturation.FAModuleFacturation.CS_MODULE_COT_PERS);
            }
            if (passage != null) {
                setIdPassage(passage.getIdPassage());
                setLibellePassage(passage.getLibelle());
            } else {
                setIdPassage("");
                setLibellePassage("");
            }
        } catch (Exception e) {
            setIdPassage("");
            setLibellePassage("");
        }
    }
}
