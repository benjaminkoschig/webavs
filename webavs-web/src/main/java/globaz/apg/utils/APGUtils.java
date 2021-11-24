package globaz.apg.utils;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.properties.APProperties;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * Une simple classe utilitaire. Devrait disparaitre....
 *
 * @author lga
 */
public class APGUtils {

    /**
     * Dans un soucis de coh�rence, il serait plus sage d'utiliser la m�thode avec la transaction...
     *
     * @param session
     *                         La session � utiliser
     * @param idDroit
     *                         L'id du droit � rechercher
     * @param genreService
     *                         Le genre de service du droit � rechercher
     * @return Le droit dans le bon type; APDroitAPG ou APDroitMaternite sinon une exception sera lanc�e
     * @throws Exception
     *                       Si le traitement de r�cup�ration du droit � �chou�
     */
    @Deprecated
    public static final APDroitLAPG loadDroit(BSession session, String idDroit, String genreService) throws Exception {
        return APGUtils.loadDroit(session, null, idDroit, genreService);
    }

    /**
     * Dans un soucis de coh�rence, il serait plus sage d'utiliser la m�thode avec la transaction...
     *
     * @param session
     *                         La session � utiliser
     * @param transaction
     *                         La transaction en cours � utiliser
     * @param idDroit
     *                         L'id du droit � rechercher
     * @param genreService
     *                         Le genre de service du droit � rechercher
     * @return Le droit dans le bon type; APDroitAPG ou APDroitMaternite sinon une exception sera lanc�e
     * @throws Exception
     *                       Si le traitement de r�cup�ration du droit � �chou�
     */
    public static final APDroitLAPG loadDroit(BSession session, BTransaction transaction, String idDroit,
            String genreService) throws Exception {
        APDroitLAPG droit;
        if (session == null) {
            throw new IllegalArgumentException("The provided BSession is null");
        }
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new Exception("idDroit is null or empty");
        }
        if (JadeStringUtil.isEmpty(genreService)) {
            throw new Exception("genre service inconnu");
        }
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            droit = new APDroitMaternite();
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(genreService)) {
            droit = new APDroitPaternite();
        } else if (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(genreService)) {
            droit = new APDroitProcheAidant();
        } else if (APGUtils.isTypeAllocationPandemie(genreService) || IAPDroitLAPG.CS_ANY_PAN.equals(genreService)) {
            droit = new APDroitPandemie();
        } else {
            droit = new APDroitAPG();
        }
        droit.setSession(session);
        droit.setIdDroit(idDroit);
        if (transaction != null) {
            droit.retrieve(transaction);
        } else {
            droit.retrieve();
        }
        return droit;
    }

    public static Boolean isTypeAllocationJourIsole(String csTypeAllocation) {
        String isFerciab = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        return (IAPDroitLAPG.CS_DEMENAGEMENT_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_NAISSANCE_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_MARIAGE_LPART_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DECES_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_JOURNEES_DIVERSES_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_CONGE_JEUNESSE_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_SERVICE_ETRANGER_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DECES_DEMI_JOUR_CIAB.equals(csTypeAllocation)) && "true".equals(isFerciab);
    }

    public static Boolean isTypeAnnonceJourIsole(String serviceType) {
        return APGenreServiceAPG.Demenagement.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.Naissance.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.MariageLPart.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.Deces.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.InspectionRecrutementLiberation.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.CongeJeunesse.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.ServiceEtranger.getCodePourAnnonce().equals(serviceType)
                || APGenreServiceAPG.DecesDemiJour.getCodePourAnnonce().equals(serviceType);
    }

    /**
     * M�thode filtrant les genres de service de type pand�mie.
     *
     * @param csTypeAllocation
     * @return
     */
    public static boolean isTypeAllocationPandemie(String csTypeAllocation) {
        return (IAPDroitLAPG.CS_GARDE_PARENTALE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_QUARANTAINE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE.equals(csTypeAllocation))
                || IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_FERMETURE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_QUARANTAINE_17_09_20.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_SALARIE_PERSONNE_VULNERABLE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE.equals(csTypeAllocation);
    }


    /**
     * M�thode filtrant les genres de service de type pand�mie.
     *
     * @param csTypeAllocation
     * @return
     */
    public static boolean isTypePaternite(String csTypeAllocation) {
        return (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(csTypeAllocation));
    }

    public static boolean isTypeMaternite(String csTypeAllocation) {
        return (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(csTypeAllocation));
    }

    public static boolean isTypeProcheAidant(String csTypeAllocation) {
        return (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(csTypeAllocation));
    }


    /**
     * M�thode filtrant les genres de service qui peuvent avoir une date de fin de droit.
     *
     * @param csTypeAllocation
     * @return
     */
    public static Boolean isGenreServiceAvecDateFin(String csTypeAllocation) {
        return (IAPDroitLAPG.CS_GARDE_PARENTALE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE.equals(csTypeAllocation))
                || IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_FERMETURE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_QUARANTAINE_17_09_20.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_SALARIE_PERSONNE_VULNERABLE.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE.equals(csTypeAllocation);
    }

    public static String getCSDomaineFromTypeDemande(String typePrestation) {
        String csDomaineDefault = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
        if (IPRDemande.CS_TYPE_APG.equals(typePrestation)) {
            csDomaineDefault = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
        } else if (IPRDemande.CS_TYPE_MATERNITE.equals(typePrestation)) {
            csDomaineDefault = IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;
        } else if (IPRDemande.CS_TYPE_PATERNITE.equals(typePrestation)) {
            try {
                csDomaineDefault = APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue();
            } catch (PropertiesException e) {
                csDomaineDefault = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
            }
        } else if (IPRDemande.CS_TYPE_PROCHE_AIDANT.equals(typePrestation)) {
            try {
                csDomaineDefault = APProperties.DOMAINE_ADRESSE_APG_PROCHE_AIDANT.getValue();
            } catch (PropertiesException e) {
                csDomaineDefault = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
            }
        } else if (IPRDemande.CS_TYPE_PANDEMIE.equals(typePrestation)) {
            try {
                csDomaineDefault = APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getValue();
            } catch (PropertiesException e) {
                csDomaineDefault = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
            }
        }
        return csDomaineDefault;
    }

    public static Boolean isDroitModifiable(String etat) {
        return Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(etat);
    }
    public static boolean isTypePrestation(String idPrestation, BSession session, String csTypePrestation) throws Exception {
        APPrestationJointLotTiersDroitManager manager = new APPrestationJointLotTiersDroitManager();
        manager.setSession(session);
        manager.setForIdPrestation(idPrestation);
        manager.find(BManager.SIZE_NOLIMIT);
        APPrestationJointLotTiersDroit prestationJointLotTiersDroit;
        for (int i = 0; i < manager.size(); i++) {
            prestationJointLotTiersDroit = (APPrestationJointLotTiersDroit) (manager.getEntity(i));
            if(prestationJointLotTiersDroit.getGenreService().equals(csTypePrestation)){
                return true;
}
        }
        return false;
    }

    public static boolean isProcheAidant(HttpSession session) {
        return globaz.prestation.api.IPRDemande.CS_TYPE_PROCHE_AIDANT == (String) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION);
    }


    public static String resolveCSDomaineParGenreService(String genreService) {
        return getCSDomaineFromTypeDemande(resolveTypeDemandeParGenreService(genreService));
    }

    public static String resolveTypeDemandeParGenreService(String genreService) {
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            return IPRDemande.CS_TYPE_MATERNITE;
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(genreService)) {
            return IPRDemande.CS_TYPE_PATERNITE;
        } else if (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(genreService)) {
            return IPRDemande.CS_TYPE_PROCHE_AIDANT;
        } else if (APGUtils.isTypeAllocationPandemie(genreService) || IAPDroitLAPG.CS_ANY_PAN.equals(genreService)) {
            return IPRDemande.CS_TYPE_PANDEMIE;
        }
        return IPRDemande.CS_TYPE_APG;
    }
}
