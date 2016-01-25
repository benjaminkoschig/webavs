package globaz.corvus.vb.process;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class REGenererRenteVeuvePerdureViewBean extends PRAbstractViewBeanSupport {

    private String adresseEmail = "";
    private Boolean annexeParDefaut = Boolean.TRUE;
    private Collection<String> annexes = new ArrayList<String>();
    private String dateDebutRenteVieillesse = "";
    private String dateDocument = JadeDateUtil.getGlobazFormattedDate(new Date());
    private String idDemandeRente = "";
    private String idTiers = "";
    private String montantRenteVeuve = "";
    private String montantRenteVieillesse = "";
    private Boolean sendToGed = Boolean.FALSE;
    private Boolean showGedCheckbox = Boolean.FALSE;

    public REGenererRenteVeuvePerdureViewBean() {
        super();
    }

    public String getAdresseEmail() {
        if (JadeStringUtil.isBlank(adresseEmail)) {
            return getSession().getUserEMail();
        } else {
            return adresseEmail;
        }
    }

    public Collection<String> getAnnexes() {
        return annexes;
    }

    public String getDateDebutRenteVieillesse() {
        return dateDebutRenteVieillesse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantRenteVeuve() {
        return montantRenteVeuve;
    }

    public String getMontantRenteVieillesse() {
        return montantRenteVieillesse;
    }

    public Boolean getSendToGed() {
        return sendToGed;
    }

    public Boolean getShowGedCheckbox() {
        return showGedCheckbox;
    }

    public String getTexteAnnexeParDefaut() {
        return "1 mémento sur les prestations complémentaires";
    }

    public boolean isAnnexeParDefaut() {
        return annexeParDefaut.booleanValue();
    }

    public boolean sendToGed() {
        return getSendToGed().booleanValue();
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public void setAnnexeParDefaut(Boolean annexeParDefaut) {
        this.annexeParDefaut = annexeParDefaut;
    }

    public void setAnnexes(Collection<String> annexes) {
        if (annexes != null) {
            this.annexes = annexes;
        } else {
            this.annexes.clear();
        }

    }

    public void setDateDebutRenteVieillesse(String dateDebutRenteVieillesse) {
        this.dateDebutRenteVieillesse = dateDebutRenteVieillesse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantRenteVeuve(String montantRenteVeuve) {
        this.montantRenteVeuve = montantRenteVeuve;
    }

    public void setMontantRenteVieillesse(String montant) {
        montantRenteVieillesse = montant;
    }

    public void setSendToGed(Boolean sendToGed) {
        this.sendToGed = sendToGed;
    }

    public void setShowGedCheckbox(Boolean showGedCheckbox) {
        this.showGedCheckbox = showGedCheckbox;
    }

    public boolean showGedCheckbox() {
        return getShowGedCheckbox().booleanValue();
    }

    @Override
    public boolean validate() {

        if (JadeStringUtil.isBlank(getDateDocument())) {
            _addError("ERREUR_DATE_OBLIGATOIRE");
        } else if (!JadeDateUtil.isGlobazDate(getDateDocument())) {
            _addError("ERREUR_DATE_INVALIDE");
        }

        if (JadeStringUtil.isBlank(getMontantRenteVieillesse())) {
            _addError("ERREUR_MONTANT_OBLIGATOIRE");
        } else {
            try {
                new FWCurrency(getMontantRenteVieillesse());
            } catch (Exception ex) {
                _addError("ERREUR_MONTANT_INVALIDE");
            }
        }

        if (JadeStringUtil.isBlank(getAdresseEmail())) {
            _addError("ERREUR_EMAIL_NON_RENSEIGNE");
        } else if (!getAdresseEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}")) {
            _addError("ERREUR_EMAIL_NON_VALIDE");
        }

        if (getSession().hasErrors()) {
            return false;
        } else {
            return true;
        }
    }
}
