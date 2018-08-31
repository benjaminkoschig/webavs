package ch.globaz.vulpecula.documents.ctrlemployeur;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import ch.globaz.vulpecula.external.models.affiliation.SuiviCaisse;
import ch.globaz.vulpecula.external.models.osiris.CompteAnnexe;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

public class DocumentRecapEmployeurPrinter extends BProcessWithContext {
    private static final long serialVersionUID = 9192905802121978811L;

    private String idEmployeur;
    private String dateReference;

    public DocumentRecapEmployeurPrinter() {
        super();
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        Date dateRef = new Date(dateReference);

        RecapControleEmployeur recap = new RecapControleEmployeur();
        recap.setDateReference(dateRef);

        Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);
        recap.setEmployeur(employeur);
        String idTiers = employeur.getIdTiers();

        if (!employeur.getDateFin().isEmpty()) {
            Date dateFin = new Date(employeur.getDateFin());
            if (dateFin.before(dateRef)) {
                dateRef = dateFin;
            }
        }

        Adresse adresseDomicile = VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdresseDomicileByIdTiersAndDate(idTiers, dateRef);
        recap.setAdresseDomicile(adresseDomicile);

        Adresse adresseCourrier = VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdresseCourrierByIdTiersAndDate(idTiers, dateRef);
        recap.setAdresseCourrier(adresseCourrier);

        // Adresse de paiement
        String avoirAdressePaiement = VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdressePaiementByIdTiers(idTiers, dateRef);
        recap.setAdressePaiement(avoirAdressePaiement);

        // Contact
        Contact contact = VulpeculaRepositoryLocator.getContactRepository().findForIdTiersWithMoyens(idTiers);
        recap.setContact(contact);

        CompteAnnexe ca = VulpeculaServiceLocator.getCompteAnnexeService().findByNumAffilieAndIdTiersAndCategorie(
                employeur.getAffilieNumero(), idTiers, employeur.getTypeAffiliation());
        recap.setSoldeOuvert(ca.getSolde());
        
        // Assurance
        List<Cotisation> cotis = VulpeculaServiceLocator.getCotisationService().findAllByIdAffilie(idEmployeur);

        for (Cotisation cotisation : cotis) {
            PlanCaisse plan = cotisation.getPlanCaisse();
            Administration admin = VulpeculaRepositoryLocator.getAdministrationRepository()
                    .findById(plan.getIdTiersAdministration());
            plan.setAdministration(admin);
        }
        recap.setCotisations(cotis);

        // Suivis des caisses
        List<SuiviCaisse> listeSuivi = VulpeculaServiceLocator.getSuiviCaissesService()
                .findByIdEmployeurAndDate(getSession(), employeur.getId(), dateRef);
        recap.setSuiviCaisses(listeSuivi);

        // Contrôle employeur
        List<ControleEmployeur> controlesEmployeur = VulpeculaRepositoryLocator.getControleEmployeurRepository()
                .findByIdEmployeur(idEmployeur);
        recap.setControlesEmployeur(controlesEmployeur);

        DocumentRecapEmployeurExcel doc = new DocumentRecapEmployeurExcel(getSession(), recap);

        doc.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), doc.getOutputFile());

        return true;
    }

    @Override
    protected String getEMailObject() {
        return (DocumentConstants.RECAP_EMPLOYEUR_EMPLOYEUR_SUBJECT);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @return the idEmployeur
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * @param idEmployeur the idEmployeur to set
     */
    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    /**
     * @return the dateReference
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * @param dateReference the dateReference to set
     */
    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }
}
