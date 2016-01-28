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
 * Sur le plan m�tier, d�fini quel service va remplir les donn�es en fonction du type de d�clarations d'attestations
 * 
 * @author PTA
 * 
 */
public abstract class DeclarationVersementProvider {
    /**
     * 
     * @param dossier
     *            le dossier � traiter
     * @param typeDocument
     *            type de document (global, d�taill�)
     * @param typeDeclaration
     *            type de d�claration de versement
     * @return la bonne instance du service � utiliser
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static DeclarationVersementService getDeclarationVersementService(
            DossierDeclarationVersementComplexModel dossier, String typeDocument, String typeDeclaration)
            throws JadeApplicationException {

        // contr�le des param�tres
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

        // prendre en compte les d�claratioons sur demande en premier lieu
        if (JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DEMANDE, typeDeclaration, false)) {
            return ALImplServiceLocator.getDeclarationVersementDetailleDemandeService();

        }
        // cas paiement direct non impos� � la source pour un document
        // global
        else if ((dossier.getRetenueImpot() == Boolean.FALSE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_NON_IMP_SOUR, typeDeclaration, false)) {

            return ALImplServiceLocator.getDeclarationVersementGlobalDirectService();

        }

        // cas paiement direct non impos� � la source pour un document
        // detaille
        else if ((dossier.getRetenueImpot() == Boolean.FALSE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_NON_IMP_SOUR, typeDeclaration, false)) {

            return ALImplServiceLocator.getDeclarationVersementDetailleDirectService();

        }

        // cas paiement direct impos� � la source pour un document global
        else if ((dossier.getRetenueImpot() == Boolean.TRUE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, typeDeclaration, false)) {

            return ALImplServiceLocator.getDeclarationVersementGlobalImposeSourceService();
        }

        // cas paiement direct impos� � la source pour un document d�taill�
        else if ((dossier.getRetenueImpot() == Boolean.TRUE)
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && !JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, typeDeclaration, false)) {

            // FIXME voir si faire impl�mentation direct impos� source
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
        // un document d�taill�e
        else if (JadeNumericUtil.isEmptyOrZero(dossier.getIdTiersBeneficiaire())
                && JadeStringUtil.equals(typeDocument, ALConstDeclarationVersement.DECLA_TYPE_DOC_DET, false)
                && (JadeStringUtil.equals(dossier.getPermisAllocataire(), ALCSAllocataire.PERMIS_G, false) || JadeStringUtil
                        .equals(dossier.getActiviteAllocataire(), ALCSDossier.ACTIVITE_NONACTIF, false))) {
            return ALImplServiceLocator.getDeclarationVersementDetailleFrontaliersNonActifService();

        }
        // dans les autres cas
        else {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementProvider#getDeclarationVersementAttestationService : Impossible de d�terminer le type de d�claration");
        }
    }
}
