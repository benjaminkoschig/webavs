package globaz.ij.helpers.process;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.utils.CTTiersUtils;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.decisions.IJAnnexeDecision;
import globaz.ij.db.decisions.IJAnnexeDecisionManager;
import globaz.ij.db.decisions.IJCopieDecision;
import globaz.ij.db.decisions.IJCopieDecisionManager;
import globaz.ij.db.decisions.IJDecisionIJAI;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.vb.process.IJDecisionIJAIViewBean;
import globaz.ij.vb.process.IJGenererDecisionViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIBanqueViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * @author BSC
 */
public class IJGenererDecisionHelper extends FWHelper {

    private static final Class[] PARAMS = new Class[] { FWViewBeanInterface.class, FWAction.class, BSession.class };

    public static final String SET_SESSION = "setSession";

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        IJGenererDecisionViewBean vb = (IJGenererDecisionViewBean) viewBean;

        // Retrouver la demande par rapport à l'idPrononce et setter les
        // informations du tiers
        IJPrononce prononce = new IJPrononce();
        prononce.setSession((BSession) session);
        prononce.setIdPrononce(vb.getIdPrononce());
        prononce.retrieve();

        PRDemande demandePrestation = new PRDemande();
        demandePrestation.setSession((BSession) session);
        demandePrestation.setIdDemande(prononce.getIdDemande());
        demandePrestation.retrieve();
        vb.setIdDemande(demandePrestation.getIdDemande());

        String idTiersPrincipal = null;
        String idTiersAdrCourrier = null;

        IJDecisionIJAI decision = null;
        // Récupération de tous les éléments de la décisions préalablement
        // générée...
        if (!JadeStringUtil.isBlankOrZero(prononce.getIdDecision())) {
            decision = new IJDecisionIJAI();
            decision.setSession((BSession) session);
            decision.setIdDecision(prononce.getIdDecision());
            decision.retrieve();
        }

        idTiersPrincipal = demandePrestation.getIdTiers();

        if ((decision != null) && !decision.isNew()) {

            idTiersPrincipal = decision.getIdTiersPrincipal();
            idTiersAdrCourrier = decision.getIdTiersAdrCourrier();

            TIAdressePaiementData adressePmt = PRTiersHelper.getAdressePaiementData((BSession) session,
                    ((BSession) session).getCurrentThreadTransaction(), decision.getIdTiersAdrPmt(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, "", JACalendar.todayJJsMMsAAAA());
            vb.setAdressePaiementFormatee(getAdressePmtFormatee((BSession) session, adressePmt));

            if (vb.isRetourDepuisPyxis()) {
                // BZ 7645 : dans le cas d'un changement d'adresse de courrier, cette adresse est stockées dans le
                // viewBean et le flag
                // isRetourPyxis = true. Il faut Donc récupérer cette nouvelle adresse depuis le viewBean
                idTiersAdrCourrier = vb.getIdTierAdresseCourrier();
                vb.setIsRetourDepuisPyxis(false);
            } else {
                if (IIJPrononce.CS_COMMUNIQUE.equals(prononce.getCsEtat())
                        || IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())
                        || IIJPrononce.CS_TRANSFER_DOSSIER.equals(prononce.getCsEtat())) {
                    vb.setTauxImposition(decision.getTauxImposition());
                    vb.setCantonTauxImposition(decision.getCsCantonTauxImposition());
                } else {
                    // On reprend les info du prononcé s'il n'est pas 'LIQUIDE', car un
                    // changement du taux dans le prononcé ne
                    // serait ainsi plus repris dans la décision !!!
                    vb.setTauxImposition(prononce.getTauxImpositionSource());
                    vb.setCantonTauxImposition(prononce.getCsCantonImpositionSource());
                }

                vb.setIdTierAdressePaiement(decision.getIdTiersAdrPmt());
                vb.setBeneficiaire(decision.getBeneficiaire());
                vb.setIdDecision(decision.getIdDecision());
                vb.setRemarque(decision.getRemarques());
                vb.setEMailAddress(decision.getEmailAdresse());
                vb.setDateSurDocument(decision.getDateSurDocument());
                vb.setGarantitRevision(decision.getNoRevAGarantir());
                vb.setIdPersonneReference(decision.getIdPersonneReference());

                // Gestion des cas avant l'insertion du nouveau champ en base de donnée
                if (JadeStringUtil.isBlank(decision.getPersonnalisationAdressePaiement())) {
                    vb.setPersonnalisationAdressePaiement("standard");
                } else {
                    vb.setPersonnalisationAdressePaiement(decision.getPersonnalisationAdressePaiement());
                }

                vb.setIdTiersAdressePaiementPersonnalisee(decision.getIdTiersAdressePaiementPersonnalisee());
                vb.setIdDomaineApplicationAdressePaiementPersonnalisee(decision
                        .getIdDomaineAdressePaiementPersonnalisee());
                vb.setNumAffilieAdressePaiementPersonnalisee(decision.getNumeroAffilieAdressePaiementPersonnalisee());
            }
        }
        // Première génération de décision, on construit tout !!!!
        else {

            // Gestion de l'adresse de courrier
            // Si pas retour des tiers (si idTierAdresseCourrier est vide, on
            // charge l'adresse de courrier de l'idTierDemandeDecision
            // Sinon, on charge l'adresse de courrier de l'idTierAdresseCourrier
            if (JadeStringUtil.isIntegerEmpty(vb.getIdTierAdresseCourrier())) {
                idTiersAdrCourrier = idTiersPrincipal;
            } else {
                idTiersAdrCourrier = vb.getIdTierAdresseCourrier();
            }
            vb.setTauxImposition(prononce.getTauxImpositionSource());
            vb.setCantonTauxImposition(prononce.getCsCantonImpositionSource());

            vb.setBeneficiaire("assure");
            vb.setPersonnalisationAdressePaiement("standard");
        }

        vb.setIdTierAdresseCourrier(idTiersAdrCourrier);
        vb.setIdTierDemandeDecision(idTiersPrincipal);
        if (!JadeStringUtil.isBlankOrZero(idTiersPrincipal)) {
            PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, idTiersPrincipal);
            if (null != tier) {
                vb.setNoAVSTiersPrincipal(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                vb.setNomPrenomTiersPrincipal(tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            }
        }

        // si le tiers bénéficiaire est null, il ne sert a rien de faire une
        // recherche
        if (!JadeStringUtil.isIntegerEmpty(vb.getIdTierAdresseCourrier())) {
            vb.setAdresseCourrierFormatee(this.getAdresseCourrierFormate((BSession) session, idTiersAdrCourrier));
        }

        // Gestion des adresses courrier/Paiement de l'assuré

        TIAdressePaiementData adressePmtAssure = PRTiersHelper.getAdressePaiementData((BSession) session,
                ((BSession) session).getCurrentThreadTransaction(), idTiersPrincipal,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, "", JACalendar.todayJJsMMsAAAA());

        vb.setIdTierAssureAdressePaiement(idTiersPrincipal);
        vb.setAdressePaiementAssureFormatee(getAdressePmtFormatee((BSession) session, adressePmtAssure));
        vb.setAdresseCourrierAssureFormatee(this.getAdresseCourrierFormate((BSession) session, adressePmtAssure));
        // New added

        // Gestion des adresses courrier/Paiement de l'employeur

        // Tout d'abord, retrouver la situation professionnelle (on prendra le
        // premier employeur)
        IJSituationProfessionnelleManager sitProMgr = new IJSituationProfessionnelleManager();
        sitProMgr.setSession((BSession) session);
        sitProMgr.setForIdPrononce(prononce.getIdPrononce());
        sitProMgr.find(1);

        String idTiersEmployeur = null;
        String idEmployeur = null;
        if (sitProMgr.size() > 0) {
            IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) sitProMgr.get(0);
            idEmployeur = sitPro.getIdEmployeur();
        }

        if (!JadeStringUtil.isEmpty(idEmployeur)) {

            IJEmployeur employeur = new IJEmployeur();
            employeur.setSession((BSession) session);
            employeur.setIdEmployeur(idEmployeur);
            employeur.retrieve();

            idTiersEmployeur = employeur.getIdTiers();

            TIAdressePaiementData adressePmtEmp = PRTiersHelper.getAdressePaiementData((BSession) session,
                    ((BSession) session).getCurrentThreadTransaction(), idTiersEmployeur,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, employeur.getIdAffilie(),
                    JACalendar.todayJJsMMsAAAA());

            vb.setIdTierEmployeurAdressePaiement(idTiersEmployeur);
            vb.setAdressePaiementEmployeurFormatee(getAdressePmtFormatee((BSession) session, adressePmtEmp));
            vb.setAdresseCourrierEmployeurFormatee(this.getAdresseCourrierFormate((BSession) session, adressePmtEmp));
        }

        // Gestion de l'adresse de paiement personnalisable
        if (!JadeStringUtil.isBlank(vb.getIdTiersAdressePaiementPersonnalisee())) {
            TIAdressePaiementData adressePmtEmp = PRTiersHelper.getAdressePaiementData((BSession) session,
                    ((BSession) session).getCurrentThreadTransaction(), vb.getIdTiersAdressePaiementPersonnalisee(),
                    vb.getIdDomaineApplicationAdressePaiementPersonnalisee(),
                    vb.getNumAffilieAdressePaiementPersonnalisee(), JACalendar.todayJJsMMsAAAA());

            vb.setAdressePaiementPersonnaliseeFormatee(getAdressePmtFormatee((BSession) session, adressePmtEmp));
            // pour éviter d'avoir le texte "Aucune adresse trouvée" si l'utilisateur n'a jamais rentrée
            // d'adresse de paiement personnalisée
            if ((adressePmtEmp != null) && !adressePmtEmp.isNew()) {
                vb.setAdresseCourrierPersonnaliseeFormatee(this.getAdresseCourrierFormate((BSession) session,
                        adressePmtEmp));
            }
        }

        if (JadeGedFacade.isInstalled()) {
            List l = JadeGedFacade.getDocumentNamesList();
            for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                String s = (String) iterator.next();
                if ((s != null) && s.startsWith(IPRConstantesExternes.DECISION_IJAI)) {
                    vb.setDisplaySendToGed("1");
                    break;
                } else {
                    vb.setDisplaySendToGed("0");
                }
            }
        }
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        super._start(viewBean, action, session);
    }

    private void addAnnexe(ICTScalableDocument viewBean, ICTScalableDocumentAnnexe annexe) {

        if ((null != viewBean) && (null != annexe)) {
            Iterator itAnnexes = viewBean.getScalableDocumentProperties().getAnnexesIterator();
            boolean exist = false;
            while (itAnnexes.hasNext()) {
                ICTScalableDocumentAnnexe itAnnexe = (ICTScalableDocumentAnnexe) itAnnexes.next();
                if (null != itAnnexe) {
                    if ((annexe.getLibelle() != null) && annexe.getLibelle().equals(itAnnexe.getLibelle())) {
                        exist = true;
                    }
                }
            }

            if (!exist) {
                viewBean.getScalableDocumentProperties().addAnnexe(annexe);
            }
        }
    }

    private void addCopie(ICTScalableDocument viewBean, ICTScalableDocumentCopie copie) {

        if ((null != viewBean) && (null != copie)) {
            Iterator itCopies = viewBean.getScalableDocumentProperties().getCopiesIterator();
            boolean exist = false;
            while (itCopies.hasNext()) {
                ICTScalableDocumentCopie itCopie = (ICTScalableDocumentCopie) itCopies.next();
                if (null != itCopie) {
                    if (copie.getIdTiers().equals(itCopie.getIdTiers())) {
                        exist = true;
                    }
                }
            }

            if (!exist) {
                viewBean.getScalableDocumentProperties().addCopie(copie);
            }
        }
    }

    public void afficherValidationDecision(FWViewBeanInterface viewBean, FWAction action, BSession session) {

        // Validation des éléments
        IJDecisionIJAIViewBean vb = (IJDecisionIJAIViewBean) viewBean;
        System.out.println("afficherValidationDecision called !!!");
        try {
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allerVersChoisirParagraphes(FWViewBeanInterface viewBean, FWAction action, BSession session) {

        // Validation des éléments
        IJGenererDecisionViewBean vb = (IJGenererDecisionViewBean) viewBean;

        // adresse email
        if (JadeStringUtil.isEmpty(vb.getEMailAddress())) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(session.getLabel("EMAIL_ERR"));
            session.addError(session.getLabel("EMAIL_ERR"));
        }

        // date sur document
        if (JadeStringUtil.isEmpty(vb.getDateSurDocument())) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(session.getLabel("DATE_DOC_ERR"));
            session.addError(session.getLabel("DATE_DOC_ERR"));
        }

        // adresse de courrier
        if (JadeStringUtil.isEmpty(vb.getIdTierAdresseCourrier())) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(session.getLabel("ADR_COURR_ERR"));
            session.addError(session.getLabel("ADR_COURR_ERR"));
        }

        // Ajout des copies par défauts - la caisse qui prend la décision - l'agence communale AVS (si celle-ci est
        // existante dans les tiers)
        try {
            CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();
            // la caisse qui prend la décision
            ICTScalableDocumentCopie copieCaisse = factory.createNewScalableDocumentCopie();

            // Une décision est déjà existante, on récupère les annexes/copies
            // par défaut de cette décision
            if (!JadeStringUtil.isBlankOrZero(vb.getIdDecision())) {
                // Ajout des annexes / copies de la décision

                if (!JadeStringUtil.isBlankOrZero(vb.getIdDecision())) {
                    // Récupération des copies...
                    IJCopieDecisionManager mgr = new IJCopieDecisionManager();
                    mgr.setSession(session);
                    mgr.setForIdDecision(vb.getIdDecision());
                    mgr.find();
                    for (int i = 0; i < mgr.size(); i++) {
                        IJCopieDecision copie = (IJCopieDecision) mgr.getEntity(i);
                        // La copie destinée à l'OAI ne doit pas apparaître dans
                        // la liste des copies,
                        // car elle sort automatiquement. On l'exclut donc de la
                        // liste !!!!
                        if ((copie.getIsCopieOAI() != null) && copie.getIsCopieOAI().booleanValue()) {
                            continue;
                        }
                        ICTScalableDocumentCopie scalableCopie = factory.createNewScalableDocumentCopie();

                        // l'intervenant
                        // scalableCopie.setCsIntervenant(copie.get);
                        scalableCopie.setIdTiers(copie.getIdTiers());
                        scalableCopie.setPrenomNomTiers(copie.getPrenomNom());
                        addCopie((ICTScalableDocument) viewBean, scalableCopie);
                    }

                    // Récupération des annexes...
                    IJAnnexeDecisionManager mgr2 = new IJAnnexeDecisionManager();
                    mgr2.setSession(session);
                    mgr2.setForIdDecision(vb.getIdDecision());
                    mgr2.find();
                    for (int i = 0; i < mgr2.size(); i++) {
                        IJAnnexeDecision annexe = (IJAnnexeDecision) mgr2.getEntity(i);
                        ICTScalableDocumentAnnexe scalableAnnexe = factory.createNewScalableDocumentAnnexe();
                        // l'intervenant
                        scalableAnnexe.setLibelle(annexe.getDescription());
                        addAnnexe((ICTScalableDocument) viewBean, scalableAnnexe);
                    }
                }
            }

            // Création des copies par défaut...
            else {
                TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
                tiAdminCaisseMgr.setSession(session);
                tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                        session.getApplication()));
                tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
                tiAdminCaisseMgr.find();

                TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

                if (tiAdminCaisse != null) {
                    copieCaisse.setIdTiers(tiAdminCaisse.getIdTiersAdministration());
                    String nom = CTTiersUtils.getPrenomNomTiersParIdTiers(session,
                            tiAdminCaisse.getIdTiersAdministration());
                    copieCaisse.setPrenomNomTiers(nom);
                } else {
                    copieCaisse.setPrenomNomTiers(session.getLabel("PAS_DE_TIERS_DEFINI_CAISSE"));
                    copieCaisse.setIdTiers("");
                }

                addCopie((ICTScalableDocument) viewBean, copieCaisse);

                // l'agence communale AVS (si celle-ci est existante dans les
                // tiers)
                ICTScalableDocumentCopie copieAgenceCommunaleAVS = factory.createNewScalableDocumentCopie();

                String idTiersPrononce = ((ICTScalableDocument) viewBean).getScalableDocumentProperties()
                        .getIdTiersPrincipal();

                if (!JadeStringUtil.isEmpty(idTiersPrononce)) {
                    TICompositionTiersManager compTiersMgr = new TICompositionTiersManager();
                    compTiersMgr.setSession(session);
                    compTiersMgr.setForIdTiersParent(idTiersPrononce);
                    compTiersMgr.setForTypeLien("507007");
                    compTiersMgr.find();

                    if (compTiersMgr.size() > 0) {
                        TICompositionTiers compTiers = (TICompositionTiers) compTiersMgr.getFirstEntity();
                        if (null != compTiers) {
                            copieAgenceCommunaleAVS.setIdTiers(compTiers.getIdTiersEnfant());
                            String nom = CTTiersUtils
                                    .getPrenomNomTiersParIdTiers(session, compTiers.getIdTiersEnfant());
                            copieAgenceCommunaleAVS.setPrenomNomTiers(nom);

                            addCopie((ICTScalableDocument) viewBean, copieAgenceCommunaleAVS);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Recherche une méthode PUBLIQUE de cette classe qui porte le nom de la partie action de l'instance 'action' et
     * prend les mêmes paramètres que cette méthode sauf pour BSession a la place de BISession et l'invoke.
     * </p>
     * <p>
     * le tout est fait dans un bloc try...catch, il est donc possible de lancer une exception dans ces méthodes. Cette
     * méthode retourne ensuite le viewBean retourné par ladite méthode ou celui transis en argument si la méthode n'a
     * pas de return value.
     * </p>
     * <p>
     * si la partie action contient la chaîne SET_ACTION, la session est simplement settée dans le viewBean et celui-ci
     * est retourné.
     * </p>
     * 
     * @throws ClassCastException
     *             si session n'est pas une instance de BSession
     */
    protected FWViewBeanInterface deleguerExecute(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws ClassCastException {
        if (IJGenererDecisionHelper.SET_SESSION.equals(action.getActionPart())) {
            viewBean.setISession(session);

            return viewBean;
        } else {
            try {
                Method method = this.getClass().getMethod(action.getActionPart(), IJGenererDecisionHelper.PARAMS);
                Object retValue = method.invoke(this, new Object[] { viewBean, action, (BSession) session });

                if ((retValue != null) && (retValue instanceof FWViewBeanInterface)) {
                    return (FWViewBeanInterface) retValue;
                }
            } catch (InvocationTargetException ie) {
                if (ie.getTargetException() != null) {
                    ((BSession) session).addError(ie.getTargetException().getMessage());
                } else {
                    JadeLogger.error(ie, ie.getMessage());
                    ((BSession) session).addError(ie.getMessage());
                }
            } catch (Exception e) {
                JadeLogger.error(e, e.getMessage());
                ((BSession) session).addError(e.getMessage());
            }

            return viewBean;
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private String getAdresseCourrierFormate(BSession session, String idTiersAdrCourrier) throws Exception {
        return PRTiersHelper.getAdresseCourrierFormatee(session, idTiersAdrCourrier, "",
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI).replaceAll("\n", "<br/>");
    }

    private String getAdresseCourrierFormate(BSession session, TIAdressePaiementData adressePmt) throws Exception {

        String result = "";

        // formatter les infos de l'adresse pour l'affichage correct dans l'écran
        if ((adressePmt != null) && !adressePmt.isNew()) {
            TIAdressePaiementDataSource sourcePmtAssure = new TIAdressePaiementDataSource();
            sourcePmtAssure.load(adressePmt);
            // formatter l'adresse
            result = new TIAdressePaiementBeneficiaireFormater().format(sourcePmtAssure).replaceAll("\n", "<br/>");
        } else {
            result = session.getLabel("IJDECISION_ADRESSE_NON_TROUVE");
        }

        return result;
    }

    private String getAdressePmtFormatee(BSession session, TIAdressePaiementData adressePmt) throws Exception {

        String result = "";

        // formatter les infos de l'adresse pour l'affichage correct dans l'écran
        if ((adressePmt != null) && !adressePmt.isNew()) {
            TIAdressePaiementDataSource sourcePmtAssure = new TIAdressePaiementDataSource();

            sourcePmtAssure.load(adressePmt);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adressePmt.getCcp())) {
                String adressePaiement = "";

                TIBanqueViewBean banque = new TIBanqueViewBean();
                banque.setSession(session);
                banque.setIdTiers(adressePmt.getIdTiersBanque());

                if (!JadeStringUtil.isEmpty(adressePmt.getCompte())) {
                    adressePaiement = session.getLabel("IJDECISION_COMPTE") + adressePmt.getCompte() + "\n";
                }
                if (!JadeStringUtil.isEmpty(adressePmt.getClearing())) {
                    adressePaiement += session.getLabel("IJDECISION_CLEARING") + adressePmt.getClearing() + "\n";
                }
                adressePaiement += banque.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER);

                result = adressePaiement;
            } else {
                result = new TIAdressePaiementCppFormater().format(sourcePmtAssure);
            }
        }

        return result.replaceAll("\n", "<br/>");
    }
}
