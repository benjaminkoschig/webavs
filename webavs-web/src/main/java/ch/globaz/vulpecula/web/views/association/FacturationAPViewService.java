package ch.globaz.vulpecula.web.views.association;

import java.util.Arrays;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.ap.DocumentFactureAPPrinter;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.ModeleEntete;
import ch.globaz.vulpecula.domain.repositories.association.FactureAssociationRepository;

public class FacturationAPViewService {
    private FactureAssociationRepository factureAssociationRepository;

    public FacturationAPViewService(FactureAssociationRepository factureAssociationRepository) {
        this.factureAssociationRepository = factureAssociationRepository;
    }

    public FacturationAPViewService() {
        factureAssociationRepository = VulpeculaRepositoryLocator.getFactureAssociationRepository();
    }

    public boolean changeModeleEntete(String idEntete, String idModeleEntete) {
        EnteteFactureAssociation entete = VulpeculaRepositoryLocator.getEnteteFactureRepository().findById(idEntete);
        ModeleEntete modeleEntete = VulpeculaServiceLocator.getParametrageAPService().findById(idModeleEntete);
        entete.setModele(modeleEntete);
        VulpeculaRepositoryLocator.getEnteteFactureRepository().update(entete);

        return true;
    }

    public void printFactureAP(String idFacture) {
        DocumentFactureAPPrinter printer = new DocumentFactureAPPrinter();
        printer.setIds(Arrays.asList(idFacture));
        printer.start();
    }

    public void deleteFactureAP(String idFacture) {
        try {
            if (idFacture == null) {
                throw new IllegalArgumentException("idFacture ne peut �tre null");
            }

            FactureAssociation facture = factureAssociationRepository.findById(idFacture);

            if (facture == null || facture.getEnteteFacture() == null || facture.getEnteteFacture().getEtat() == null) {
                throw new IllegalStateException("Pas de facture trouv�e avec l'id : " + idFacture);
            }

            EtatFactureAP etatFactureAP = facture.getEnteteFacture().getEtat();
            if (!EtatFactureAP.COMPTABILISE.equals(etatFactureAP) && !EtatFactureAP.GENERE.equals(etatFactureAP)) {
                // au lieu de supprimer la facture, on la met en �tat supprim�
                EnteteFactureAssociation entete = facture.getEnteteFacture();
                entete.setPassageFacturation(null);
                entete.setEtat(EtatFactureAP.SUPPRIME);
                VulpeculaRepositoryLocator.getEnteteFactureRepository().update(entete);
            } else {

                throw new IllegalStateException("Une facture en �tat '" + etatFactureAP
                        + "' ne peut pas �tre supprim�e !");
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Facture non supprim�e ! Cause : " + ex.getMessage());
        }

    }

    public void refuseFactureAP(String idFacture) {
        try {
            if (idFacture == null) {
                throw new IllegalArgumentException("idFacture ne peut �tre null");
            }

            FactureAssociation facture = factureAssociationRepository.findById(idFacture);

            if (facture == null || facture.getEnteteFacture() == null || facture.getEnteteFacture().getEtat() == null) {
                throw new IllegalStateException("Pas de facture trouv�e avec l'id : " + idFacture);
            }

            EtatFactureAP etatFactureAP = facture.getEnteteFacture().getEtat();
            if (!EtatFactureAP.COMPTABILISE.equals(etatFactureAP) && !EtatFactureAP.GENERE.equals(etatFactureAP)) {
                // au lieu de supprimer la facture, on la met en �tat supprim�
                EnteteFactureAssociation entete = facture.getEnteteFacture();
                entete.setEtat(EtatFactureAP.REFUSE);
                VulpeculaRepositoryLocator.getEnteteFactureRepository().update(entete);
            } else {

                throw new IllegalStateException("Une facture en �tat '" + etatFactureAP
                        + "' ne peut pas �tre refus�e !");
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Facture non refus�e ! Cause : " + ex.getMessage());
        }

    }
}
