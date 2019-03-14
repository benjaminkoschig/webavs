package ch.globaz.al.businessimpl.rafam.sedex.builder;

import al.ch.ech.xmlns.ech_00097._3.UidOrganisationIdCategorieType;
import al.ch.ech.xmlns.ech_00097._3.UidStructureType;
import al.ch.ech.xmlns.ech_0104._4.Contact;
import al.ch.ech.xmlns.ech_0104._4.Contact.Employer;
import al.ch.ech.xmlns.ech_0104._4.DeliveryOfficeType;
import al.ch.ech.xmlns.ech_0104._4.LegalOffice;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.util.CommonNSSFormater;

/**
 * Classe fournissant les méthodes de base des autres builders. Ces builders sont utilisé pour préparer le modèles
 * d'annonce pour un envoi à la centrale
 *
 *
 * @author jts
 *
 */
public abstract class MessageBuilderAbstractNewXSDVersion {

    /** L'annonce à préparer */
    AnnonceRafamComplexModel annonce = null;

    /**
     * Les codes de pays OFS commence à 8000 alors que nos code sont entre 100 et 999
     * Exemple :
     * Suisse OFS : 8100
     * Suisse Globaz : 100
     * Allemagne OFS : 8207
     * Allemagne Globat : 207
     */
    protected static final String CODE_PAYS_OFS = "8";

    /**
     * Effectue les validations nécessaires avant envoi
     *
     * <ul>
     * <li>Vérification des NSS</li>
     * <li>Vérification du format des dates</li>
     *
     * @return <code>true</code> si toutes les valeurs sont valides, <code>false</code> dans le cas contraire
     */
    public boolean annonceIsValid() {

        CommonNSSFormater nssf = new CommonNSSFormater();

        // /////////////////////////////////////////////////////////////////////////////////////////////
        // validation des NSS
        // /////////////////////////////////////////////////////////////////////////////////////////////

        try {
            if (!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getNssEnfant())) {
                nssf.checkNss(nssf.unformat(annonce.getAnnonceRafamModel().getNssEnfant()));
            }
            if (!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getNssAllocataire())) {
                nssf.checkNss(nssf.unformat(annonce.getAnnonceRafamModel().getNssAllocataire()));
            }
        } catch (Exception e) {
            return false;
        }

        if ((!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getNssEnfant())
                && !JadeStringUtil.isBlank(annonce.getAnnonceRafamModel().getNssAllocataire()))
                && annonce.getAnnonceRafamModel().getNssEnfant()
                        .equals(annonce.getAnnonceRafamModel().getNssAllocataire())) {
            return false;
        }

        // /////////////////////////////////////////////////////////////////////////////////////////////
        // validation des dates
        // /////////////////////////////////////////////////////////////////////////////////////////////

        if (!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getDebutDroit())
                && (!JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getDebutDroit())
                        || !JadeDateUtil.isDateBefore(annonce.getAnnonceRafamModel().getDebutDroit(),
                                ALConstRafam.DATE_MAXIMUM_RAFAM))) {
            return false;
        }

        if (!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getEcheanceDroit())
                && (!JadeDateUtil.isGlobazDate(annonce.getAnnonceRafamModel().getEcheanceDroit())
                        || !JadeDateUtil.isDateBefore(annonce.getAnnonceRafamModel().getEcheanceDroit(),
                                ALConstRafam.DATE_MAXIMUM_RAFAM))) {
            return false;
        }

        if ((!JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getDebutDroit())
                && !JadeStringUtil.isBlankOrZero(annonce.getAnnonceRafamModel().getEcheanceDroit()))
                && JadeDateUtil.isDateBefore(annonce.getAnnonceRafamModel().getEcheanceDroit(),
                        annonce.getAnnonceRafamModel().getDebutDroit())) {
            return false;
        }

        return true;
    }

    /**
     * Prépare l'annonce à envoyer. Si une erreur se produit pendant le traitement le flag internalError de l'annonce en
     * DB est changé à <code>true</code>. En cas de succès l'état de l'annonce est changé à TRANSMIS
     *
     * @return L'annonce préparée. Retourne <code>null</code> si une erreur a été détectée pendant le traitement.
     *
     * @throws JadeApplicationException
     *                                      Exception levée par la couche métier lorsqu'elle n'a pu effectuer
     *                                      l'opération souhaitée
     * @throws JadePersistenceException
     *                                      Exception levée lorsque le chargement ou la mise à jour en DB par la couche
     *                                      de persistence n'a pu se
     *                                      faire
     */
    public abstract Object build() throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère les informations de contact à enregistrer dans l'annonce
     *
     * @param noAffilie
     *                      Numéro d'affilié
     * @param date
     *                      Date pour laquelle récupérer les informations
     *
     * @return les informations de contact
     *
     * @throws JadeApplicationException
     *                                      Exception levée par la couche métier lorsqu'elle n'a pu effectuer
     *                                      l'opération souhaitée
     * @throws JadePersistenceException
     *                                      Exception levée lorsque le chargement ou la mise à jour en DB par la couche
     *                                      de persistence n'a pu se
     *                                      faire
     */
    protected Contact getContact(String noAffilie, String date)
            throws JadePersistenceException, JadeApplicationException {
        AffiliationSearchSimpleModel affilieSearch = new AffiliationSearchSimpleModel();
        affilieSearch.setForNumeroAffilie(noAffilie);
        affilieSearch.setForDateValidite(date);
        affilieSearch = AFBusinessServiceLocator.getAffiliationService().find(affilieSearch);

        if (affilieSearch.getSearchResults().length > 0) {
            AffiliationSimpleModel contactAff = (AffiliationSimpleModel) affilieSearch.getSearchResults()[0];

            Contact contact = new Contact();
            Employer empl = new Employer();
            if (!JadeStringUtil.isBlank(contactAff.getNumeroIDE())) {
                UidStructureType uid = new UidStructureType();
                uid.setUidOrganisationId(getNumeroIDE(contactAff.getNumeroIDE()));
                uid.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
                empl.setUid(uid);
            }
            empl.setName(contactAff.getAffilieNumero());
            empl.setContactInformation(contactAff.getRaisonSociale());
            contact.setEmployer(empl);

            return contact;
        } else {
            return null;
        }
    }

    private Integer getNumeroIDE(String numeroIDE) {
        String numeroIDEWithoutCHE = numeroIDE.substring(3);
        return Integer.valueOf(numeroIDEWithoutCHE);
    }

    protected DeliveryOfficeType buildDeliveryOfficeObject(AnnonceRafamModel annonce) {

        DeliveryOfficeType deliveryOffice = new DeliveryOfficeType();

        deliveryOffice.setOfficeIdentifier(annonce.getOfficeIdentifier());

        if (!JadeStringUtil.isBlank(annonce.getOfficeBranch())) {
            deliveryOffice.setBranch(Long.parseLong(annonce.getOfficeBranch()));
        }

        return deliveryOffice;
    }

    protected LegalOffice buildLegalOfficeObject(AnnonceRafamModel annonce) {

        LegalOffice legalOffice = new LegalOffice();
        legalOffice.setOfficeIdentifier(annonce.getLegalOffice());

        return legalOffice;
    }
}
