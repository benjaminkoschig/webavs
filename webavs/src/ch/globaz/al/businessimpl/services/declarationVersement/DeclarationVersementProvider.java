package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSDeclarationVersement;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstDeclarationVersement;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.models.dossier.DossierDeclarationVersementComplexModel;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Sur le plan métier, défini quel service va remplir les données en fonction du type de déclarations d'attestations
 * 
 * @author PTA
 * 
 */
public abstract class DeclarationVersementProvider {
    /**
     * 
     * @param dossier
     *            le dossier à traiter
     * @param typeDocument
     *            type de document (global, détaillé)
     * @param typeDeclaration
     *            type de déclaration de versement
     * @return la bonne instance du service à utiliser
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static DeclarationVersementService getDeclarationVersementService(
            DossierDeclarationVersementComplexModel dossier, String typeDocument, String typeDeclaration)
            throws JadeApplicationException {

        // contrôle des paramètres
        // dossier
        if (dossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementProvide#getDeclarationVersementService: dossier is null");
        }
        // type de document
        if (!JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)) {
            throw new ALDeclarationVersementException("DeclarationVersementProvide#getDeclarationVersementService: "
                    + typeDocument + " is not a valid document's type");
        }

        // prendre en compte les déclaratioons sur demande en premier lieu
        if (JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DEMANDE, typeDeclaration, false)) {
            return ALImplServiceLocator.getDeclarationVersementDetailleDemandeService();

        }
        // cas paiement direct non imposé à la source pour un document
        // global
        else if ((dossier.getRetenueImpot() == Boolean.FALSE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_NON_IMP_SOUR, typeDeclaration, false)) {

            return ALImplServiceLocator.getDeclarationVersementGlobalDirectService();

        }

        // cas paiement direct non imposé à la source pour un document
        // detaille
        else if ((dossier.getRetenueImpot() == Boolean.FALSE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_NON_IMP_SOUR, typeDeclaration, false)) {

            return ALImplServiceLocator.getDeclarationVersementDetailleDirectService();

        }

        // cas paiement direct imposé à la source pour un document global
        else if ((dossier.getRetenueImpot() == Boolean.TRUE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, typeDeclaration, false)) {

            return ALImplServiceLocator.getDeclarationVersementGlobalImposeSourceService();
        }

        // cas paiement direct imposé à la source pour un document détaillé
        else if ((dossier.getRetenueImpot() == Boolean.TRUE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, typeDeclaration, false)) {

            // FIXME voir si faire implémentation direct imposé source
            return ALImplServiceLocator.getDeclarationVersementDetailleImposeSourceService();
        }

        // cas d'un paiement indirect avec permis frontalier ou non actif pour
        // un document global
        else if (JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)
                && (JadeStringUtil.equals(dossier.getPermisAllocataire(), ALCSAllocataire.PERMIS_G, false) || JadeStringUtil
                        .equals(dossier.getActiviteAllocataire(), ALCSDossier.ACTIVITE_NONACTIF, false))) {
            return ALImplServiceLocator.getDeclarationVersementGlobalFrontaliersNonActifService();

        }
        // cas d'un paiement indirect avec permis frontalier ou non actif pour
        // un document détaillée
        else if (JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && (JadeStringUtil.equals(dossier.getPermisAllocataire(), ALCSAllocataire.PERMIS_G, false) || JadeStringUtil
                        .equals(dossier.getActiviteAllocataire(), ALCSDossier.ACTIVITE_NONACTIF, false))) {
            return ALImplServiceLocator.getDeclarationVersementDetailleFrontaliersNonActifService();

        }
        // dans les autres cas
        else {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementProvider#getDeclarationVersementAttestationService : Impossible de déterminer le type de déclaration");
        }
    }
}
