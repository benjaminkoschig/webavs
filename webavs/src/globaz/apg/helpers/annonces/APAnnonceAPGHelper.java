package globaz.apg.helpers.annonces;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APBreakableRules;
import globaz.apg.enums.APTypeApplication;
import globaz.apg.exceptions.APEmptyIdException;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.exceptions.APToManyEntityFoundException;
import globaz.apg.vb.annonces.APAnnonceAPGViewBean;
import globaz.apg.vb.annonces.APAnnonceSedexViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DVH
 */
public class APAnnonceAPGHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession iSession) throws Exception {
        super._retrieve(viewBean, action, iSession);

        BSession session = (BSession) iSession;
        APAnnonceAPGViewBean vb = (APAnnonceAPGViewBean) viewBean;
        if (!JadeStringUtil.isBlank(vb.getNumeroAssure())) {
            PRTiersWrapper tiers = PRTiersHelper.getTiers(session, vb.getNumeroAssure());
            if (tiers != null) {
                vb.setIdTiers(tiers.getIdTiers());
            }
        }

        if (JadeStringUtil.isEmpty(vb.getIdAnnonce())) {
            throw new APEmptyIdException(APAnnonceAPG.class, "Id of entity APPrestation is empty");
        } else {
            /**
             * C'est la prestation qui contient une clé étrangère sur la 1ère annonce de paiement (type 1) et non
             * l'inverse. Si l'annonce est une annonce de correction, aucune prestation n'aura de référence sur elle.
             * Dans le cas d'une annonce de correction il faut remonter les annonces par l'idParent jusqu'à tomber sur
             * l'annonce qui n'aura pas d'idParent. Toutes les annonces de correction doivent posséder un idParent
             */
            String idAnnonceFinal = getIdPremiereAnnonce(session, vb.getIdAnnonce(), 0);
            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(session);
            prestationManager.setForIdAnnonce(idAnnonceFinal);
            prestationManager.find(BManager.SIZE_NOLIMIT);
            if (prestationManager.size() == 0) {
                throw new APEntityNotFoundException(APPrestation.class, vb.getIdAnnonce());
            } else if (prestationManager.size() == 0) {
                throw new APToManyEntityFoundException(APPrestation.class, vb.getIdAnnonce());
            } else {
                APPrestation prestation = (APPrestation) prestationManager.get(0);
                APDroitLAPG droit = new APDroitLAPG();
                droit.setSession(session);
                droit.setIdDroit(prestation.getIdDroit());
                droit.retrieve();
                if (droit.isNew()) {
                    throw new APEntityNotFoundException(APDroitLAPG.class, prestation.getIdDroit());
                }

                PRDemande demande = new PRDemande();
                demande.setSession(session);
                demande.setIdDemande(droit.getIdDemande());
                demande.retrieve();
                if (demande.isNew()) {
                    throw new APEntityNotFoundException(PRDemande.class, droit.getIdDemande());
                }

                vb.setIdDroit(droit.getIdDroit());
                if (IPRDemande.CS_TYPE_MATERNITE.equals(demande.getTypeDemande())) {
                    vb.setTypePrestation(APTypeApplication.MATERNITE.toString());
                } else if (IPRDemande.CS_TYPE_APG.equals(demande.getTypeDemande())) {
                    vb.setTypePrestation(APTypeApplication.APG.toString());
                }
            }
        }
    }

    /**
     * Recherche l'annonce de plus haut niveau. C'est à dire celle qui n'a pas d'idParent. Si l'annonce avec l'id n'a
     * pas d'idParent, c'est cet id qui sera retourné. C'est une méthode récursive, une limitation à 50 itération est
     * définie en cas de problème de référence (cyclique, etc). C'est un choix... FIXME : la théorie ci-dessus est
     * fausse : toutes les annonces de correction d'une même annonce auront le même idParent
     * 
     * @param session
     *            La session à utiliser
     * @param idAnnonce
     *            L'id de l'annonce
     * @param ctr
     *            Un compteur d'itération pour limiter la casse en cas de problème de référence.
     * @return l'annonce de plus haut niveau. C'est à dire celle qui n'a pas d'idParent
     * @throws APEntityNotFoundException
     */
    private String getIdPremiereAnnonce(BSession session, String idAnnonce, int ctr) throws APEntityNotFoundException {
        if (ctr >= 50) {
            throw new RuntimeException(
                    "APAnnonceAPGHelper.getIdPremiereAnnonce(idAnnonce, session, ctr) : récursive method was called too many time. 50 iteration limit was reached");
        }
        ctr++;
        String idAnnonceFinal = null;
        APAnnonceAPG annonceAPG = new APAnnonceAPG();
        try {
            annonceAPG.setSession(session);
            annonceAPG.setId(idAnnonce);
            annonceAPG.retrieve();
        } catch (Exception exception) {
            throw new APEntityNotFoundException(APAnnonceAPG.class, idAnnonce, exception);
        }
        if (annonceAPG.isNew()) {
            throw new APEntityNotFoundException(APAnnonceAPG.class, idAnnonce);
        }
        if (!JadeStringUtil.isBlankOrZero(annonceAPG.getIdParent())) {
            idAnnonceFinal = getIdPremiereAnnonce(session, annonceAPG.getIdParent(), ctr);
        } else {
            idAnnonceFinal = annonceAPG.getIdAnnonce();
        }
        return idAnnonceFinal;
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        APAnnonceAPG annonce = (APAnnonceAPG) viewBean;
        if (viewBean instanceof APAnnonceSedexViewBean) {
            annonce.fromChampsAnnonce(((APAnnonceSedexViewBean) viewBean).getChampsAnnonce());
        }

        // Traitement des breakRules inséré par l'utilisateur
        String breakrulesFormatte = validerEtFormatterLesBreakRules(annonce.getBreakRules());
        annonce.setBreakRules(breakrulesFormatte);
        ((APAnnonceSedexViewBean) viewBean).setBreakRules(breakrulesFormatte);
        ((APAnnonceSedexViewBean) viewBean).getChampsAnnonce().setBreakRules(breakrulesFormatte);

        // si l'annonce est une annonce envoyée, il faut créer une nouvelle
        // annonce en état erronée et laisser celle-ci
        // inchangée
        if (annonce.getEtat().equals(IAPAnnonce.CS_ENVOYE)) {
            APAnnonceAPG annonceErronnee = annonce.createClone();

            // On enlève dans tous les cas les montants
            annonceErronnee.setNombreJoursService(null);
            annonceErronnee.setTotalAPG(null);
            annonceErronnee.setTauxJournalierAllocationBase(null);
            annonceErronnee.setMoisAnneeComptable(null);

            if ((viewBean instanceof APAnnonceSedexViewBean)) {
                if (((APAnnonceSedexViewBean) viewBean).isOnlyNss()) {
                    annonceErronnee = new APAnnonceAPG();
                    annonceErronnee.setSession(annonce.getSession());
                    annonceErronnee.setBusinessProcessId(annonce.getBusinessProcessId());
                    annonceErronnee.setContenuAnnonce("4");
                    annonceErronnee.setMessageType(APAnnoncesRapgService.messageType);
                    annonceErronnee.setSubMessageType(APAnnoncesRapgService.subMessageType3);
                    annonceErronnee.setNumeroAgence(annonce.getNumeroAgence());
                    annonceErronnee.setNumeroCaisse(annonce.getNumeroCaisse());
                    annonceErronnee.setNumeroAssure(annonce.getNumeroAssure());
                } else {
                    annonceErronnee.setContenuAnnonce("4");
                    annonceErronnee.setSubMessageType(APAnnoncesRapgService.subMessageType3);
                }
            }

            // On met un timeStamp dans le message
            annonceErronnee.setTimeStamp(JadeDateUtil.getCurrentTime().toString());

            // on lui donne le bon id parent
            if (JadeStringUtil.isIntegerEmpty(annonce.getIdParent())) {
                annonceErronnee.setIdParent(annonce.getIdAnnonce());
            } else {
                annonceErronnee.setIdParent(annonce.getIdParent());
            }

            annonceErronnee.setEtat(IAPAnnonce.CS_ERRONE);

            if (!IAPAnnonce.CS_APGSEDEX.equals(annonce.getTypeAnnonce())) {
                annonceErronnee.setBreakRules("600");
                annonceErronnee.setMessageType(APAnnoncesRapgService.messageType);
                annonceErronnee.setSubMessageType(APAnnoncesRapgService.subMessageType3);
                annonceErronnee.setEventDate(annonce.getPeriodeDe());
                annonceErronnee.setContenuAnnonce("4");
            }
            // Dans tous les cas, se sera une annonce de type Sedex maintenant
            annonceErronnee.setTypeAnnonce(IAPAnnonce.CS_APGSEDEX);

            if (JadeStringUtil.isEmpty(annonceErronnee.getEventDate())) {
                annonceErronnee.setEventDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            }

            annonceErronnee.add();
        } else {
            if (viewBean instanceof APAnnonceSedexViewBean) {
                // Validation des plausis
                // v1-10-00sp1 Désactivation des contrôles à ce niveau là.
                // ApgServiceLocator.getAnnoncesRapgService().fullWithConstants(
                // ((APAnnonceSedexViewBean) viewBean).getChampsAnnonce());
                // if (JadeStringUtil.isEmpty(GlobazServer.getCurrentSystem().getApplication("APG")
                // .getProperty("rapg.checkall"))) {
                // List<String> errors = ApgServiceLocator.getPlausibilitesApgService().checkPlausisXSD(
                // ((APAnnonceSedexViewBean) viewBean).getChampsAnnonce());
                // if (!errors.isEmpty()) {
                // viewBean.setMsgType(FWViewBeanInterface.ERROR);
                // String mess = "";
                // for (String e : errors) {
                // mess += ((BSession) session).getLabel(e) + "<br/>";
                // }
                // viewBean.setMessage(mess);
                // }
                // }

            }
            // comportement standard.
            super._update(viewBean, action, session);
        }
    }

    /**
     * @param breakRules
     * @return
     * @throws APMalformedBreakRule
     */
    private String validerEtFormatterLesBreakRules(String breakRules) throws IllegalArgumentException {
        List<APBreakableRules> rules = new ArrayList<APBreakableRules>();
        if (!JadeStringUtil.isEmpty(breakRules)) {
            // Si le champs contient des ',' c'est qu'il y a plusieurs breakRules
            if (breakRules.contains(",")) {
                String[] values = breakRules.split(",");
                for (String s : values) {
                    APBreakableRules r = getBreakRule(s);
                    // On évite les doublons
                    if (!rules.contains(r)) {
                        rules.add(r);
                    }
                }
            }
            // Sinon c'est qu'il n'y en à qu'une
            else {
                rules.add(getBreakRule(breakRules));
            }
        }

        StringBuilder breakrulesFormatte = new StringBuilder();
        boolean isFirst = true;
        for (APBreakableRules br : rules) {
            if (!isFirst) {
                breakrulesFormatte.append(",");
            }
            breakrulesFormatte.append(br.getCode());
            isFirst = false;
        }
        return breakrulesFormatte.toString();
    }

    /**
     * @param breakRules
     * @throws APMalformedBreakRule
     *             Si la breakRule n'est pas trouvé.
     */
    private APBreakableRules getBreakRule(String breakRules) throws IllegalArgumentException {
        String code = breakRules.replace(" ", "");
        APBreakableRules rule = APBreakableRules.valueOfCode(code);
        if (rule == null) {
            throw new IllegalArgumentException("Invalid breakRule code [" + breakRules + "]");
        }
        return rule;
    }
}
