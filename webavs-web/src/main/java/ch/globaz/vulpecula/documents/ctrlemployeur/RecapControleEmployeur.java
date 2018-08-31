package ch.globaz.vulpecula.documents.ctrlemployeur;

import java.util.HashMap;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.SuiviCaisse;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import globaz.globall.db.BSession;

/**
 * Conteneur pour le document de récap d'un employeur
 *
 * @since WebMetier 2.6
 */
public class RecapControleEmployeur {
    private Employeur employeur;
    private Adresse adresseDomicile;
    private Adresse adresseCourrier;
    private String adressePaiement;
    private Date dateReference;

    private Contact contact;

    private Montant soldeOuvert;

    private List<Cotisation> cotisations;

    private List<SuiviCaisse> suiviCaisses;

    private List<ControleEmployeur> controlesEmployeur;

    public String getEmployeurPersonaliteJuridique(BSession session) {
        return getCodeLibelle(session, getEmployeur().getPersonnaliteJuridique());
    }

    public String getContactDescription() {
        return getContact().getPrenom() + " " + getContact().getNom();
    }

    public HashMap<TypeContact, MoyenContact> getMoyenContact() {
        return getContact().getMoyenContact();
    }

    public String getEmployeurPeriodeActivite() {
        return getEmployeur().getDateDebut() + " - " + getEmployeur().getDateFin();
    }

    public String getEmployeurConvention() {
        return getEmployeur().getConvention().getCode() + " " + getEmployeur().getNomConvention();
    }

    /**
     * @param session
     * @return
     */
    private String getCodeLibelle(BSession session, String code) {
        if (session == null) {
            return null;
        }

        return session.getCodeLibelle(code);
    }

    /**
     * @return the employeur
     */
    public Employeur getEmployeur() {
        return employeur;
    }

    public String getEmployeurDescription() {
        return getEmployeur().getAffilieNumero() + " " + getEmployeur().getRaisonSociale();
    }

    /**
     * @return the adresseDomicile
     */
    public String getAdresseDomicile() {
        if (adresseDomicile == null) {
            return "";
        }
        return adresseDomicile.getAdresseFormatte();
    }

    /**
     * @return the adresseCourrier
     */
    public String getAdresseCourrier() {
        if (adresseCourrier == null) {
            return "";
        }
        return adresseCourrier.getAdresseFormatte();
    }

    /**
     * @return the adressePaiement
     */
    public String getAdressePaiement() {
        if (adressePaiement == null) {
            return "";
        }
        return adressePaiement;
    }

    /**
     * @return the contacts
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * @return the soldeOuvert
     */
    public double getSoldeOuvert() {
        if (soldeOuvert == null) {
            return 0.0;
        }
        return Double.parseDouble(soldeOuvert.getValueNormalisee());
    }

    /**
     * @return the assurances
     */
    public List<Cotisation> getCotisations() {
        return cotisations;
    }

    /**
     * @return the suivisCaisses
     */
    public List<SuiviCaisse> getSuiviCaisses() {
        return suiviCaisses;
    }

    /**
     * @return the controlesEmployeur
     */
    public List<ControleEmployeur> getControlesEmployeur() {
        if (controlesEmployeur.size() > 6) {
            return controlesEmployeur.subList(0, 6);
        }
        return controlesEmployeur;
    }

    /**
     * @param employeur the employeur to set
     */
    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    /**
     * @param adresseDomicile the adresseDomicile to set
     */
    public void setAdresseDomicile(Adresse adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }

    /**
     * @param adresseCourrier the adresseCourrier to set
     */
    public void setAdresseCourrier(Adresse adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    /**
     * @param adressePaiement the adressePaiement to set
     */
    public void setAdressePaiement(String adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContact(Contact contacts) {
        contact = contacts;
    }

    /**
     * @param soldeOuvert the soldeOuvert to set
     */
    public void setSoldeOuvert(Montant soldeOuvert) {
        this.soldeOuvert = soldeOuvert;
    }

    /**
     * @param assurances the assurances to set
     */
    public void setCotisations(List<Cotisation> cotisations) {
        this.cotisations = cotisations;
    }

    /**
     * @param suivisCaisses the suivisCaisses to set
     */
    public void setSuiviCaisses(List<SuiviCaisse> listeSuivi) {
        suiviCaisses = listeSuivi;
    }

    /**
     * @param controlesEmployeur the controlesEmployeur to set
     */
    public void setControlesEmployeur(List<ControleEmployeur> controlesEmployeur) {
        this.controlesEmployeur = controlesEmployeur;
    }

    /**
     * @return the dateReference
     */
    public Date getDateReference() {
        return dateReference;
    }

    public String getDateReferenceSwissValue() {
        return dateReference.getSwissValue();
    }

    /**
     * @param dateReference the dateReference to set
     */
    public void setDateReference(Date dateReference) {
        this.dateReference = dateReference;
    }

}
