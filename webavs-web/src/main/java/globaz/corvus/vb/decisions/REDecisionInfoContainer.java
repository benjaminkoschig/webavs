package globaz.corvus.vb.decisions;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.RERemarqueGroupePeriode;
import globaz.corvus.db.decisions.RERemarqueGroupePeriodeManager;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * <H1>Description</H1>
 * <p>
 * Classe servant de container pour regouper les infos du décompte par groupe de rente accordée
 * </p>
 * <p>
 * Structure de la map 'mapRentesAccordees' : key (genrePrst) | |---> REBeneficiairesInfoVO (key) |---> ... |--->
 * REBeneficiairesInfoVO (key)
 * </p>
 * 
 * @author scr
 */

public class REDecisionInfoContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<REAnnexeDecisionViewBean> annexesListDIC = new ArrayList<REAnnexeDecisionViewBean>();
    private List<RECopieDecisionViewBean> copiesListDIC = new ArrayList<RECopieDecisionViewBean>();
    private String csTypeDecision = "";
    private String decisionDu = "";
    private String firstDateDebut = "";
    private String idDecision = null;
    // Contient la liste des ids de base de calcul.
    private final List<String> idsBC = new ArrayList<String>();
    // Clé = idRA
    // Valeur : List des id des prestations dues
    private final Map<Long, List<Long>> idsPrstDuesParIdsRA = new HashMap<Long, List<Long>>();
    private boolean isContentInfini = false;
    private String lastDateDebut = "";
    private String lastDateFin = "";
    private String listIdsPrstDues = "";
    private final Map<KeyPeriodeInfo, List<REBeneficiaireInfoVO>> mapBeneficiairesInfo = new TreeMap<KeyPeriodeInfo, List<REBeneficiaireInfoVO>>();
    private int nbPrstDues = 0;

    private String remarqueDecisionDIC = "";

    public void addIdBC(final String idBC) {
        if ((idBC != null) && !idsBC.contains(idBC)) {
            idsBC.add(idBC);
        }
    }

    public void addPrstDuesParRenteAccordee(final REDecisionEntity decision, final REBeneficiaireInfoVO vo)
            throws Exception {

        if (idsPrstDuesParIdsRA.containsKey(vo.getIdRenteAccordee())) {
            List<Long> idsPD = idsPrstDuesParIdsRA.get(vo.getIdRenteAccordee());

            if (!idsPD.contains(vo.getIdPrestationDue())) {
                idsPD.add(vo.getIdPrestationDue());
            }

            idsPrstDuesParIdsRA.put(vo.getIdRenteAccordee(), idsPD);
        } else {

            List<Long> idsPD = new ArrayList<Long>();
            idsPD.add(vo.getIdPrestationDue());
            idsPrstDuesParIdsRA.put(vo.getIdRenteAccordee(), idsPD);
        }

        if (nbPrstDues == 0) {
            listIdsPrstDues += vo.getIdPrestationDue();
        } else {
            listIdsPrstDues += "," + vo.getIdPrestationDue();
        }

        nbPrstDues++;

        // Pour la première date de début
        if (!JadeStringUtil.isBlankOrZero(vo.getPeriodeDu())) {
            if (getFirstDateDebut().length() == 0) {
                setFirstDateDebut(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeDu()));
            } else {
                if (Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeDu())) < Integer
                        .parseInt(getFirstDateDebut())) {
                    setFirstDateDebut(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeDu()));
                }
            }
        }

        // Pour la dernière date de début
        if (!JadeStringUtil.isBlankOrZero(vo.getPeriodeDu())) {
            if (getLastDateDebut().length() == 0) {
                setLastDateDebut(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeDu()));
            } else {
                if (Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeDu())) > Integer
                        .parseInt(getLastDateDebut())) {
                    setLastDateDebut(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeDu()));
                }
            }
        }

        // Pour la dernière date de fin
        if (!JadeStringUtil.isBlankOrZero(vo.getPeriodeAu())) {
            if (getLastDateFin().length() == 0) {
                setLastDateFin(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeAu()));
            } else {
                if (Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeAu())) > Integer
                        .parseInt(getLastDateFin())) {
                    setLastDateFin(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vo.getPeriodeAu()));
                }
            }
        }

        // et pour isContentInfini
        if (JadeStringUtil.isBlankOrZero(vo.getPeriodeAu())) {
            setContentInfini(true);
        }
        // }
    }

    private String ajouterRemarqueImpotenceDecisionAPI(final BSession session, final KeyPeriodeInfo key,
            final String idDecision) throws Exception {

        String remarque = "";

        // Retrieve de la décision
        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(session);
        decision.setIdDecision(idDecision);
        decision.retrieve();

        // Retrieve de la demande
        REDemandeRenteAPI demandeApi = new REDemandeRenteAPI();
        demandeApi.setSession(session);
        demandeApi.setIdDemandeRente(decision.getIdDemandeRente());
        demandeApi.retrieve();

        // Recherche de la ra pour la période en question
        RERenteAccJoinTblTiersJoinDemRenteManager raMgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
        raMgr.setSession(session);
        raMgr.setForNoDemandeRente(demandeApi.getIdDemandeRente());
        raMgr.setForEnCoursAtMois(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(key.dateDebut));
        raMgr.find();

        RERenteAccJoinTblTiersJoinDemandeRente ra = (RERenteAccJoinTblTiersJoinDemandeRente) raMgr.getFirstEntity();

        if (ra != null) {
            // HACK pour ne faire le ttt qu'une seule fois !
            // S'il y a une remarque dans la table REREMPG pour cette décision, alors on ne fait pas ce ttt.
            RERemarqueGroupePeriodeManager remMgr = new RERemarqueGroupePeriodeManager();
            remMgr.setSession(session);
            remMgr.setForIdDecision(decision.getIdDecision());
            remMgr.find();

            if (remMgr.isEmpty()) {
                if (!demandeApi.isNew()) {

                    String degreImpotenceAPI = "";

                    String idTiersPrincipal = decision.getIdTiersBeneficiairePrincipal();
                    PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, idTiersPrincipal);

                    if (null == tier) {

                        REInformationsComptabilite infoCom = new REInformationsComptabilite();
                        infoCom.setSession(session);
                        infoCom.setIdInfoCompta(ra.getIdInfoCompta());
                        infoCom.retrieve();

                        tier = PRTiersHelper.getTiersParId(session, infoCom.getIdTiersAdressePmt());

                    }

                    String langueBeneficiairePrincipal = tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE);
                    String idCantonDomicileBeneficiaire = tier.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);

                    String nomOfficeAI = "";

                    try {

                        // BZ 6328 : Décision, détail : pour les API, l'office AI de Berne ne vient pas en allemand.
                        nomOfficeAI = findNomCaisseParCantonEtLangueTiers(session, langueBeneficiairePrincipal,
                                idCantonDomicileBeneficiaire, demandeApi.getIdDemandeRente(), ra.getIdBaseCalcul());

                    } catch (Exception e) {
                        throw new Exception("erreur dans la recherche des offices AI");
                    }

                    if (langueBeneficiairePrincipal.equals("503001")) {
                        langueBeneficiairePrincipal = "FR";
                    } else if (langueBeneficiairePrincipal.equals("503002")) {
                        langueBeneficiairePrincipal = "DE";
                    } else if (langueBeneficiairePrincipal.equals("503004")) {
                        langueBeneficiairePrincipal = "IT";
                    } else {
                        langueBeneficiairePrincipal = "FR";
                    }

                    // API Faible
                    if (ra.getCodePrestation().equals("81") || ra.getCodePrestation().equals("84")
                            || ra.getCodePrestation().equals("91") || ra.getCodePrestation().equals("85")
                            || ra.getCodePrestation().equals("89") || ra.getCodePrestation().equals("94")
                            || ra.getCodePrestation().equals("95")) {

                        degreImpotenceAPI = session.getApplication().getLabel("MOTIF_RENTE_API_RENTE_ACCORDEE_FAIBLE",
                                langueBeneficiairePrincipal);

                        // API Moyen
                    } else if (ra.getCodePrestation().equals("82") || ra.getCodePrestation().equals("88")
                            || ra.getCodePrestation().equals("92") || ra.getCodePrestation().equals("86")
                            || ra.getCodePrestation().equals("96")) {

                        degreImpotenceAPI = session.getApplication().getLabel("MOTIF_RENTE_API_RENTE_ACCORDEE_MOYEN",
                                langueBeneficiairePrincipal);

                        // API Grave
                    } else if (ra.getCodePrestation().equals("83") || ra.getCodePrestation().equals("93")
                            || ra.getCodePrestation().equals("87") || ra.getCodePrestation().equals("97")) {

                        degreImpotenceAPI = session.getApplication().getLabel("MOTIF_RENTE_API_RENTE_ACCORDEE_GRAVE",
                                langueBeneficiairePrincipal);

                    }

                    // Màj de l'office AI
                    degreImpotenceAPI = PRStringUtils.replaceString(degreImpotenceAPI, "{officeAI}", nomOfficeAI);

                    remarque = degreImpotenceAPI;

                }
            }
        }

        return remarque;
    }

    private String findNomCaisseByGenre(final BSession session, final REDemandeRenteAPI demandeApi) throws Exception {
        String nomCaisse = "";
        PRTiersWrapper[] officesAI = null;
        officesAI = PRTiersHelper.getAdministrationActiveForGenre(session,
                RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {
                if (officesAI[i].getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION).equals(
                        demandeApi.getCodeOfficeAI())) {

                    nomCaisse = officesAI[i].getProperty(PRTiersWrapper.PROPERTY_NOM);

                }
            }
        }
        return nomCaisse;
    }

    private String findNomCaisseByLangage(final BSession session, final TIAdministrationManager tiAdministrationMgr,
            final String codeISOLangueAssure, String nomCaisse) throws Exception {

        if (codeISOLangueAssure.equals("503002")) {
            tiAdministrationMgr.setForCodeAdministration("302");
            tiAdministrationMgr.find();
        }

        // Récupération du nom de l'office si elle existe, dans la langue de l'assuré
        if (tiAdministrationMgr.size() > 0) {
            for (Iterator<TIAdministrationViewBean> iterAdministration = tiAdministrationMgr.iterator(); iterAdministration
                    .hasNext();) {
                TIAdministrationViewBean tiAdministration = iterAdministration.next();

                if (!JadeStringUtil.isBlank(tiAdministration.getDesignation1())) {
                    nomCaisse = tiAdministration.getDesignation1();
                    break;
                }

            }
        }
        return nomCaisse;
    }

    private String findNomCaisseParCantonEtLangueTiers(final BSession session,
            final String langueBeneficiairePrincipal, final String idCantonDomicileBeneficiaire,
            final String idDemande, String idBaseCalcul) throws Exception {

        String nomCaisse = "";

        REBasesCalcul baseCalculEntity = new REBasesCalcul();
        baseCalculEntity.setIdBasesCalcul(idBaseCalcul);
        baseCalculEntity.retrieve();

        if ((baseCalculEntity != null) && !JadeStringUtil.isBlankOrZero(baseCalculEntity.getCodeOfficeAi())) {
            PRTiersWrapper[] tabTiers = PRTiersHelper.getAdministrationActiveForGenreAndCode(session,
                    RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI, baseCalculEntity.getCodeOfficeAi());

            if (tabTiers != null) {

                for (PRTiersWrapper tiersW : tabTiers) {
                    if (tiersW != null) {
                        nomCaisse = tiersW.getNom();
                    }
                }
            }
        }

        // Si on ne trouve pas le nom de l'office AI selon la base de calcul on passe par le tiers
        if (nomCaisse == "") {
            TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();

            // Recherche des administrations selon le genre
            tiAdministrationMgr.setSession(session);
            tiAdministrationMgr.setForGenreAdministration(RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI);
            tiAdministrationMgr.changeManagerSize(0);

            tiAdministrationMgr.find();

            // Recherche du nom de l'office AI selon le canton
            if (tiAdministrationMgr.size() > 0) {
                for (Iterator<TIAdministrationViewBean> iterAdministration = tiAdministrationMgr.iterator(); iterAdministration
                        .hasNext();) {
                    TIAdministrationViewBean tiAdministration = iterAdministration.next();

                    if ((tiAdministration.getCanton().equals(idCantonDomicileBeneficiaire))
                            && (tiAdministration.getLangue().equals(langueBeneficiairePrincipal))) {
                        nomCaisse = tiAdministration.getDesignation1();
                        break;
                    }
                }
            }

            // Sinon, recherche de la caisse selon la langue du tiers
            if (nomCaisse == "") {
                nomCaisse = findNomCaisseByLangage(session, tiAdministrationMgr, langueBeneficiairePrincipal, nomCaisse);
            }
        }

        return nomCaisse;
    }

    public Iterator<REAnnexeDecisionViewBean> getAnnexesIteratorDIC() throws Exception {
        return getAnnexesListDIC().iterator();
    }

    public List<REAnnexeDecisionViewBean> getAnnexesListDIC() {
        return annexesListDIC;
    }

    public REBeneficiaireInfoVO[] getBeneficiaires(final KeyPeriodeInfo key) {
        if (!mapBeneficiairesInfo.containsKey(key)) {
            return null;
        }
        List<REBeneficiaireInfoVO> lst = mapBeneficiairesInfo.get(key);
        return lst.toArray(new REBeneficiaireInfoVO[lst.size()]);
    }

    public Iterator<RECopieDecisionViewBean> getCopiesIteratorDIC() throws Exception {
        return getCopiesListDIC().iterator();
    }

    public List<RECopieDecisionViewBean> getCopiesListDIC() {
        return copiesListDIC;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getDecisionDu() {
        return decisionDu;
    }

    public String getFirstDateDebut() {
        return firstDateDebut;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public List<String> getIdsBC() {
        return idsBC;
    }

    public Map<Long, List<Long>> getIdsPrstDuesParIdsRA() {
        return idsPrstDuesParIdsRA;
    }

    public String getLastDateDebut() {
        return lastDateDebut;
    }

    public String getLastDateFin() {
        return lastDateFin;
    }

    public List<Long> getListIdsPrst(final Long idRA) {
        if (idsPrstDuesParIdsRA.containsKey(idRA)) {
            return idsPrstDuesParIdsRA.get(idRA);
        } else {
            return null;
        }
    }

    public String getListIdsPrstDues() {
        return listIdsPrstDues;
    }

    public Map<KeyPeriodeInfo, List<REBeneficiaireInfoVO>> getMapBeneficiairesInfo() {
        return mapBeneficiairesInfo;
    }

    public String getRemarqueDecisionDIC() {
        return remarqueDecisionDIC;
    }

    public String getUID() {

        String key = "";
        key += getCsTypeDecision();
        key += "-" + getDecisionDu();

        // Ordonnancement automatique ascendant.
        TreeSet<Long> idsPrsDue = new TreeSet<Long>();

        Set<Long> keys = getIdsPrstDuesParIdsRA().keySet();
        Iterator<Long> iterKeys = keys.iterator();
        while (iterKeys.hasNext()) {

            List<Long> lst = getIdsPrstDuesParIdsRA().get(iterKeys.next());
            Iterator<Long> iter2 = lst.iterator();

            while (iter2.hasNext()) {
                Long elem = iter2.next();

                if (!idsPrsDue.contains(elem)) {
                    idsPrsDue.add(elem);
                }
            }
        }

        // On récupère tous les id des prestations dues, ordonnées
        for (Long element : idsPrsDue) {
            key += "-" + element;
        }
        return key;
    }

    public void initAnnexesCopiesRemarqueDecision(final BSession session, final String idDecision) throws Exception {

        try {

            // Charger toutes les annexes dans le DIC
            List<REAnnexeDecisionViewBean> lstAnnexe = new ArrayList<REAnnexeDecisionViewBean>();

            REAnnexeDecisionListViewBean annexeMgr = new REAnnexeDecisionListViewBean();
            annexeMgr.setSession(session);
            annexeMgr.setForIdDecision(idDecision);
            annexeMgr.find();

            for (Iterator<REAnnexeDecisionViewBean> iterator = annexeMgr.iterator(); iterator.hasNext();) {
                REAnnexeDecisionViewBean annexe = iterator.next();
                annexe.setIdProvisoire(annexe.getIdDecisionAnnexe());
                lstAnnexe.add(annexe);
            }

            setAnnexesListDIC(lstAnnexe);

            // charger toutes les copies dans le DIC
            List<RECopieDecisionViewBean> lstCopie = new ArrayList<RECopieDecisionViewBean>();

            RECopieDecisionListViewBean copieMgr = new RECopieDecisionListViewBean();
            copieMgr.setSession(session);
            copieMgr.setForIdDecision(idDecision);
            copieMgr.find();

            for (Iterator<RECopieDecisionViewBean> iterator = copieMgr.iterator(); iterator.hasNext();) {
                RECopieDecisionViewBean copie = iterator.next();
                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, copie.getIdTiersCopie());
                if (tw != null) {
                    // On ajoute le tiers uniquement s'il n'est pas décédé
                    if (!JadeDateUtil.isGlobazDate(tw.getDateDeces())) {
                        copie.setIdProvisoire(copie.getIdDecisionCopie());
                        lstCopie.add(copie);
                    }
                } else {
                    // Si on n'arrive pas à récupérer le tiers, cela ne signifie pas forcément qu'il n'existe pas
                    // Les administrations n'ont pas de n° AVS et du coup ne sont pas retourné par la méthode
                    // getTiersParId
                    copie.setIdProvisoire(copie.getIdDecisionCopie());
                    lstCopie.add(copie);
                }
            }

            setCopiesListDIC(lstCopie);

            // charger la remarque de la décision
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(session);
            decision.setIdDecision(idDecision);
            decision.retrieve();

            setRemarqueDecisionDIC(decision.getRemarqueDecision());

        } catch (Exception e) {
            throw new Exception("Erreur dans le retrieve des annexes, copies et remarque de la décision : "
                    + e.getMessage());
        }

    }

    public void initMapBenef(final BSession session, final String idDecision) throws Exception {

        try {

            // Retrieve de la décision
            REDecisionEntity decision = new REDecisionEntity();
            decision.setIdDecision(idDecision);
            decision.setSession(session);
            decision.retrieve();

            // Définition du nombre de mois pour l'itération
            int nbMois = 0;

            // Définition de la date courante
            JADate dateCourante;

            // Définition de la date de fin
            String dateFin = "";

            // Définition de la date de décision
            JADate dateDecision = null;

            // DEFINITION DU NOMBRE DE MOIS ---------------------------------------------------------------------------

            // Si la décision est de type courant, 1 seul mois
            if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {
                nbMois = 1;
                dateCourante = new JADate(decision.getDecisionDepuis());

                // Si aucune période n'a de date de fin, ttt spécial
            } else if (getLastDateFin().length() == 0) {

                if (!JadeStringUtil.isBlankOrZero(decision.getDateDecision())) {
                    dateDecision = new JADate(decision.getDateDecision());
                    JADate jDateFin = new JADate(REPmtMensuel.getDateDernierPmt(session));
                    dateFin = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(min(dateDecision, jDateFin).toStrAMJ());
                }

                if (JadeStringUtil.isBlankOrZero(dateFin)) {
                    dateFin = REPmtMensuel.getDateDernierPmt(session);
                }

                // Itération sur tous les mois entre la première date de début et la date du dernier paiement
                nbMois = PRDateFormater.nbrMoisEntreDates(
                        new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getFirstDateDebut())), new JADate(
                                dateFin));

                // Dans le cas du départ dans le futur
                if (nbMois < 1) {
                    nbMois = PRDateFormater.nbrMoisEntreDates(new JADate(dateFin),
                            new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getLastDateDebut())));
                }

                dateCourante = new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getFirstDateDebut()));

                // Dans les autres cas
            } else {

                // hack de la date de fin si il existe une pd sans date de fin
                // IsContentInfini = Au moins une période sans date de fin
                if (isContentInfini() && !decision.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_RETRO)) {
                    JADate d = new JADate(REPmtMensuel.getDateDernierPmt(session));
                    dateFin = PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(d.toStrAMJ());
                } else if (decision.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_RETRO)) {
                    dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decision.getDateFinRetro());
                } else {
                    dateFin = getLastDateFin();
                }

                if (!JadeStringUtil.isBlankOrZero(decision.getDateDecision())) {
                    dateDecision = new JADate(decision.getDateDecision());
                    JADate jDateFin = new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(dateFin));
                    dateFin = PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(min(dateDecision, jDateFin).toStrAMJ());
                }

                // Trouver le nombre de mois entre la firstDateDebut et lastDatefin
                nbMois = PRDateFormater.nbrMoisEntreDates(
                        new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getFirstDateDebut())), new JADate(
                                PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(dateFin)));

                dateCourante = new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getFirstDateDebut()));

            }

            // ITERATION SUR TOUS LES MOIS
            // --------------------------------------------------------------------------------

            List<REBeneficiaireInfoVO> lst = new ArrayList<REBeneficiaireInfoVO>();
            List<REBeneficiaireInfoVO> lstPrec = new ArrayList<REBeneficiaireInfoVO>();

            KeyPeriodeInfo key;

            int nbPrest = 0;
            int nbPrestPrec = 0;

            FWCurrency montantPrest = new FWCurrency();
            FWCurrency montantPrestPrec = new FWCurrency();
            List<String> idTiers = new ArrayList<>();
            List<String> idTiersPrecedant = new ArrayList<>();

            JADate dateDebut = new JADate();
            JADate dateFinPrec = new JADate();

            JACalendar cal = new JACalendarGregorian();

            boolean isFirst = true;

            for (int i = 0; i < nbMois; i++) {

                nbPrestPrec = nbPrest;
                montantPrestPrec = new FWCurrency(montantPrest.toString());
                idTiersPrecedant = idTiers;
                idTiers = new ArrayList<>();

                if (isFirst) {
                    dateDebut = dateCourante;
                }

                REPrestationsDuesManager pdMgr = new REPrestationsDuesManager();
                pdMgr.setSession(session);
                pdMgr.setForIdsPrestDues(getListIdsPrstDues());
                pdMgr.setForPeriodePDInMoisAnnee(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(PRDateFormater
                        .convertDate_AAAAMMJJ_to_MMxAAAA(dateCourante.toStrAMJ())));
                pdMgr.find(BManager.SIZE_NOLIMIT);

                montantPrest = new FWCurrency();
                for (Iterator<REPrestationDue> iterator = pdMgr.iterator(); iterator.hasNext();) {
                    REPrestationDue pd = iterator.next();
                    montantPrest.add(pd.getMontant());
                }

                lstPrec = lst;
                lst = new ArrayList<REBeneficiaireInfoVO>();

                nbPrest = pdMgr.size();

                boolean isLast = false;
                if (i == (nbMois - 1)) {
                    dateFinPrec = new JADate();
                    isLast = true;
                }

                String keyDebut = "";

                RERenteAccordee ra = new RERenteAccordee();

                // Parcourir toutes les pd de la période
                for (Iterator<REPrestationDue> iterator = pdMgr.iterator(); iterator.hasNext();) {
                    REPrestationDue pd = iterator.next();

                    REBeneficiaireInfoVO vo = new REBeneficiaireInfoVO(session, pd.getCsType(), Long.parseLong(pd
                            .getIdRenteAccordee()), pd.getDateDebutPaiement(), pd.getDateFinPaiement());

                    // retrieve de la ra
                    ra.setSession(session);
                    ra.setIdPrestationAccordee(pd.getIdRenteAccordee());
                    ra.retrieve();

                    vo.setFraction(ra.getFractionRente());
                    vo.setGenrePrestation(ra.getCodePrestation());
                    vo.setIdPrestationDue(Long.parseLong(pd.getIdPrestationDue()));
                    vo.setIdTiersBeneficiaire(Long.parseLong(ra.getIdTiersBeneficiaire()));
                    vo.setMontant(pd.getMontant());
                    
                    idTiers.add(ra.getIdTiersBeneficiaire());

                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());

                    if (tiers == null) {
                        throw new Exception("Tiers not found ! idRA/idTiers = " + ra.getIdPrestationAccordee() + "/"
                                + ra.getIdTiersBeneficiaire());
                    }

                    vo.setDescriptionBeneficiare(tiers.getDescription(session));
                    vo.setDateNaissanceBeneficiaire(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));

                    if (!lst.contains(vo)) {
                        lst.add(vo);
                    }
                }

                // Si pas la première itération
                if (!isFirst) {

                    // Comparer avec la liste précédente
                    if ((nbPrestPrec != nbPrest) || (!montantPrest.equals(montantPrestPrec) || !isListesIdentiques(idTiers, idTiersPrecedant))) {

                        // BZ 5062 - Englober toute la création de la clé dans ce test
                        // Ajouter si la liste précédente n'est pas vide
                        if (!lstPrec.isEmpty()) {

                            dateFinPrec = cal.addMonths(dateCourante, -1);

                            // Si différent, ajouter la période précédente
                            key = new KeyPeriodeInfo(
                                    PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(dateDebut.toStrAMJ()),
                                    PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(dateFinPrec.toStrAMJ()));

                            key = retrieveRemarqueKey(key, session, idDecision);

                            // Ajouter le degré d'impotence si décision API !
                            keyDebut = ajouterRemarqueImpotenceDecisionAPI(session, key, idDecision);
                            key.remarque = keyDebut + key.remarque;

                            mapBeneficiairesInfo.put(key, lstPrec);
                        }

                        dateDebut = dateCourante;
                        dateFinPrec = new JADate();

                    }

                }

                // si la dernière itération
                if (isLast) {

                    // Ajouter la dernière période
                    key = new KeyPeriodeInfo(PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(dateDebut.toStrAMJ()),
                            dateFin);

                    // Si période infinie
                    // BZ 4423
                    if (isContentInfini()) {
                        key.dateFin = "";
                    }

                    // Si lastDateFin < lastDateDebut
                    if (cal.compare("01." + lastDateFin, "01." + lastDateDebut) == JACalendar.COMPARE_FIRSTLOWER) {
                        key.dateFin = "";
                    }

                    key = retrieveRemarqueKey(key, session, idDecision);

                    // Ajouter le degré d'impotence si décision API !
                    keyDebut = ajouterRemarqueImpotenceDecisionAPI(session, key, idDecision);
                    key.remarque = keyDebut + key.remarque;

                    // BZ 6441
                    if (!lst.isEmpty()) {
                        mapBeneficiairesInfo.put(key, lst);
                    }

                    dateDebut = dateCourante;
                    dateFinPrec = new JADate();

                }

                isFirst = false;
                dateCourante = cal.addMonths(dateCourante, 1);
            }

        } catch (Exception e) {
            throw new Exception("Erreur dans le tri des périodes : " + e.getMessage());
        }

    }
    
    private boolean isListesIdentiques(List<String> liste1, List<String> liste2) {
        if(liste1.size() != liste2.size()) {
            return false;
        }
        // ! ne test pas les listes avec des doublons
        for(String idTiers : liste1) {
            if(!liste2.contains(idTiers)) {
                return false;
            }
        }
        return true;
    }

    public boolean isContentInfini() {
        return isContentInfini;
    }

    private JADate min(final JADate d1, final JADate d2) throws Exception {

        JACalendar cal = new JACalendarGregorian();
        if (cal.compare(d1, d2) == JACalendar.COMPARE_FIRSTLOWER) {
            return new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJMMAAAA(d1.toStrAMJ()));
        } else {
            return new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJMMAAAA(d2.toStrAMJ()));
        }
    }

    private KeyPeriodeInfo retrieveRemarqueKey(final KeyPeriodeInfo key, final BSession session, final String idDecision)
            throws Exception {

        RERemarqueGroupePeriodeManager remarqueMgr = new RERemarqueGroupePeriodeManager();
        remarqueMgr.setSession(session);
        remarqueMgr.setForDatedu(key.dateDebut);
        remarqueMgr.setForDateAu(key.dateFin);
        remarqueMgr.setForIdDecision(idDecision);
        remarqueMgr.find(BManager.SIZE_NOLIMIT);

        for (Iterator<RERemarqueGroupePeriode> iterator = remarqueMgr.iterator(); iterator.hasNext();) {
            RERemarqueGroupePeriode remarque = iterator.next();

            key.remarque = remarque.getRemarque() + " ";
        }

        return key;
    }

    public void setAnnexesListDIC(final List<REAnnexeDecisionViewBean> annexesListDIC) {
        this.annexesListDIC = annexesListDIC;
    }

    public void setContentInfini(final boolean isContentInfini) {
        this.isContentInfini = isContentInfini;
    }

    public void setCopiesListDIC(final List<RECopieDecisionViewBean> copiesListDIC) {
        this.copiesListDIC = copiesListDIC;
    }

    public void setCsTypeDecision(final String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setDecisionDu(final String decisionDu) {
        this.decisionDu = decisionDu;
    }

    public void setFirstDateDebut(final String firstDateDebut) {
        this.firstDateDebut = firstDateDebut;
    }

    public void setIdDecision(final String idDecision) {
        this.idDecision = idDecision;
    }

    public void setLastDateDebut(final String lastDateDebut) {
        this.lastDateDebut = lastDateDebut;
    }

    public void setLastDateFin(final String lastDateFin) {
        this.lastDateFin = lastDateFin;
    }

    public void setListIdsPrstDues(final String listIdsPrstDues) {
        this.listIdsPrstDues = listIdsPrstDues;
    }

    public void setRemarqueDecisionDIC(final String remarqueDecisionDIC) {
        this.remarqueDecisionDIC = remarqueDecisionDIC;
    }
}
