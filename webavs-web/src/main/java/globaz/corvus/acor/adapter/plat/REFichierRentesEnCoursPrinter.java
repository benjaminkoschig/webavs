package globaz.corvus.acor.adapter.plat;

import globaz.corvus.db.historiques.REHistoriqueRentes;
import globaz.corvus.db.historiques.REHistoriqueRentesJoinTiersManager;
import globaz.corvus.helpers.historiques.REHistoriqueRentesJoinTiersHelper;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REFichierRentesEnCoursPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Iterator assures;
    // Les rentes en cours sont des rentes accordées.
    private REHistoriqueRentes[] historiqueRentes;

    private int iHistoriqueRentes;
    private Object membre;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REFichierPeriodePrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public REFichierRentesEnCoursPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);

        // On initialise/créé l'historique des rentes
        BITransaction tr = null;
        try {
            tr = getSession().newTransaction();
            tr.openTransaction();
            REHistoriqueRentesJoinTiersHelper.doReloadHistorique(getSession(), (BTransaction) tr, adapter()
                    .idTiersAssure());
            tr.commit();
        } catch (Exception e) {
            try {
                tr.rollback();
            } catch (Exception e2) {
                ;
            }
            return;
        } finally {
            if (tr != null) {
                try {
                    tr.closeTransaction();
                } catch (Exception e3) {
                    ;
                }
            }
        }

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REACORDemandeAdapter adapter() {
        return (REACORDemandeAdapter) parent;
    }

    /**
     * Retourne toutes les rentes en cours d'un membre de la famille.
     * 
     * @param idMembreFamille
     * @return
     */
    private REHistoriqueRentes[] getRentesEnCours(String idMembreFamille) throws Exception {

        // On accède en direct les membres de la famille de la situation
        // familiale
        // pour des raison d'optimisation.

        ISFMembreFamille mf = adapter().situationFamiliale().getMembreFamille(idMembreFamille);

        if (mf == null) {
            throw new PRACORException("Tiers not found - idMbrFam/idTiers = " + idMembreFamille + "/null");
        }

        if (JadeStringUtil.isBlankOrZero(mf.getIdTiers())) {
            return new REHistoriqueRentes[0];
        }

        // On récupère tous les historiques pour chaque assuré
        // Les RA de type API n'ont pas d'historique
        REHistoriqueRentesJoinTiersManager mgr = new REHistoriqueRentesJoinTiersManager();
        mgr.setSession(getSession());
        mgr.setForIdTiersIn(mf.getIdTiers());
        mgr.setForIsEnvoyerAcor(Boolean.TRUE);
        mgr.find(BManager.SIZE_NOLIMIT);

        // On met la liste des rentes accordées ds une table de wrapper
        REHistoriqueRentes[] historiques = new REHistoriqueRentes[mgr.size()];

        for (int i = 0; i < mgr.size(); i++) {
            REHistoriqueRentes elm = (REHistoriqueRentes) mgr.get(i);
            historiques[i] = elm;

        }
        return historiques;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {

        if (assures == null) {
            assures = adapter().membres().iterator();
        }

        if ((historiqueRentes == null) || (iHistoriqueRentes >= historiqueRentes.length)) {
            // s'il n'y a plus de membre, on arrete le processus
            if (!assures.hasNext()) {
                return false;
            }

            // sinon on passe aux historique du membre suivant.
            iHistoriqueRentes = 0;
            historiqueRentes = null;

            while (assures.hasNext() && (historiqueRentes == null)) {
                membre = assures.next();

                try {

                    historiqueRentes = getRentesEnCours(((ISFMembreFamilleRequerant) membre).getIdMembreFamille());
                } catch (PRACORException e1) {
                    throw e1;
                } catch (Exception e) {
                    throw new PRACORException(getSession().getLabel("ERREUR_RENTES_ACC_ASSURE"), e);
                }

                if ((historiqueRentes != null) && (historiqueRentes.length == 0)) {
                    historiqueRentes = null;
                }
            }
        }
        return (historiqueRentes != null) && (iHistoriqueRentes < historiqueRentes.length);
    }

    /**
     * @deprecated Replaced by printLigne(StringBuffer writer)
     * 
     * 
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        throw new PRACORException(
                "deprecated method. Should not be used any more. Use : 'printLigne(StringBuffer writer)' instead.");

    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {

        REHistoriqueRentes wrapper = historiqueRentes[iHistoriqueRentes++];

        // 1. le no AVS de l'assure

        String idTiersBenef = wrapper.getIdTiers();
        PRTiersWrapper tw;
        try {
            tw = PRTiersHelper.getTiersParId(getSession(), idTiersBenef);
        } catch (Exception e) {
            throw new PRACORException(e.toString(), e);
        }

        this.writeAVS(writer, tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

        // 2. genre de prestation
        this.writeEntier(writer, wrapper.getCodePrestation());

        // 3. fraction de rente
        // writeEntier(writer, PRACORConst.csFractionRenteToAcor(getSession(),
        // wrapper.getProperty(REHistoriqueRenteWrapper.PROPERTY_FRACTION_RENTE)));
        this.writeEntier(writer, wrapper.getFractionRente());

        // 4. date début du droit
        this.writeEntier(writer, PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(wrapper.getDateDebutDroit()));

        // 5. date fin du droit
        this.writeEntier(writer, PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(wrapper.getDateFinDroit()));

        // 6. montant de la prestation
        this.writeEntier(writer, Integer.toString((new BigDecimal(wrapper.getMontantPrestation())).intValue()));

        // 7. revenu annuel moyen
        this.writeEntier(writer, Integer.toString((new BigDecimal(wrapper.getRam())).intValue()));

        // 8. durée cotisation ram
        this.writeEntier(writer, PRDateFormater.convertDate_AAxMM_to_AAMM(wrapper.getDureeCotRam()));

        // 9. code revenu
        this.writeEntier(writer, wrapper.getCodeRevenu());

        // 10. echelle de rente
        this.writeEntier(writer, wrapper.getEchelle());

        // 11. durée cotisation avant 73
        this.writeEntier(writer, PRDateFormater.convertDate_AAxMM_to_AAMM(wrapper.getDureeCotAv73()));

        // 12. durée cotisation après 73
        this.writeEntier(writer, PRDateFormater.convertDate_AAxMM_to_AAMM(wrapper.getDureeCotAp73()));

        // 13. mois appoint avant 73
        this.writeEntier(writer, wrapper.getMoisAppointAv73());

        // 14. mois appoint après 73
        this.writeEntier(writer, wrapper.getMoisAppointAp73());

        // 15. durée cotis. de la classe d'age
        this.writeEntier(writer, wrapper.getDureeCotiClasseAge());

        // 16. année de niveau

        String anneeNiveau = wrapper.getAnneeNiveau();

        if ("00".equals(anneeNiveau)) {
            anneeNiveau = "2000";
        } else if (JadeStringUtil.isBlankOrZero(anneeNiveau)) {
            anneeNiveau = "0000";
        } else {
            if (Integer.parseInt(anneeNiveau) < 60) {
                anneeNiveau = "20" + anneeNiveau;
            } else {
                anneeNiveau = "19" + anneeNiveau;
            }
        }

        this.writeEntier(writer, anneeNiveau);

        // 17. code cas spécial
        String cs1 = wrapper.getCs1();
        String cs2 = wrapper.getCs2();
        String cs3 = wrapper.getCs3();
        String cs4 = wrapper.getCs4();
        String cs5 = wrapper.getCs5();

        if (cs1.length() == 1) {
            cs1 = "0" + cs1;
        }

        if (cs2.length() == 1) {
            cs2 = "0" + cs2;
        }

        if (cs3.length() == 1) {
            cs3 = "0" + cs3;
        }

        if (cs4.length() == 1) {
            cs4 = "0" + cs4;
        }

        if (cs5.length() == 1) {
            cs5 = "0" + cs5;
        }

        this.writeEntier(writer, cs1 + cs2 + cs3 + cs4 + cs5);

        // 18. supplément de carrière
        this.writeEntier(writer, wrapper.getSupplementCarriere());

        // 19. degré d'invalidité
        this.writeEntier(writer, wrapper.getDegreInvalidite());

        // 20. clé infirmité + atteinte fonctionnelle
        this.writeEntier(writer, wrapper.getCleInfirmiteAtteinteFct());

        // 21. survenance évén. assure
        String dateServEvtAssure = wrapper.getSurvenanceEvenementAssure();

        // pas de date de survenance evenement assure pour tous les types de
        // rentes
        if (JadeStringUtil.isDecimalEmpty(dateServEvtAssure)) {
            this.writeEntier(writer, "0");
        } else {
            // Format MM.AAAA
            try {
                JADate survEvtAssure = new JADate(dateServEvtAssure);
                this.writeEntier(writer, survEvtAssure.toStrAMJ());
            } catch (JAException e) {
                this.writeEntier(writer, "0");
            }
        }

        String b = "0";
        if ((wrapper.getIsInvaliditePrecoce() != null) && wrapper.getIsInvaliditePrecoce().booleanValue()) {
            b = "1";
        }
        // 22. invalide précoce
        this.writeEntier(writer, b);

        // 23. office ai
        this.writeEntier(writer, wrapper.getOfficeAI());

        // 24. durée ajournement
        this.writeEntier(writer, wrapper.getDureeAjournement());

        // 25. supplément ajournement
        b = wrapper.getSupplementAjournement();
        FWCurrency m = new FWCurrency(0);
        if (b != null) {
            m = new FWCurrency(b);
        }
        this.writeEntier(writer, String.valueOf(m.intValue()));

        // 26. date de révocation
        String date = wrapper.getDateRevocationAjournement();
        if (JadeStringUtil.isBlank(date) && wrapper.getIsRenteAjournee()) {
            date = "99.9999";
        }
        if (date.length() < 6) {
            date = 0 + date;
        }
        this.writeEntier(writer, PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(date));

        // 27. montant bonus éducatif
        this.writeEntier(writer, wrapper.getMontantBTE());

        // 28. nbr. année bonif. bte
        this.writeEntier(writer, wrapper.getNbrAnneeBTE());

        // 29. nbr. année bonif. bta
        this.writeEntier(writer, wrapper.getNbrAnneeBTA());

        // 30. nbr. année bonif. transitoire
        this.writeEntier(writer, wrapper.getNbrAnneeBTR());

        // 31. code rev. splitté
        if ((wrapper.getIsRevenuSplitte() != null) && wrapper.getIsRevenuSplitte().booleanValue()) {
            b = "1";
        } else {
            b = "0";
        }
        this.writeEntier(writer, b);

        // 32. code survivant
        if ((wrapper.getIsSurvivantInvalid() != null) && wrapper.getIsSurvivantInvalid().booleanValue()) {
            b = "1";
        } else {
            b = "0";
        }
        this.writeEntier(writer, b);

        // 33. nbr. année anticipation
        this.writeEntier(writer, wrapper.getNbrAnneeAnticipation());

        // 34. montant redic. pour anticipation
        String s = wrapper.getMontantReducAnticipation();
        m = new FWCurrency(0);
        if (s != null) {
            m = new FWCurrency(s);
        }
        this.writeEntier(writer, String.valueOf(m.intValue()));

        // 35. date déb. anticipation
        this.writeEntier(writer, PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(wrapper.getDateDebutAnticipation()));

        // 36. somme des rev. non revalorisé, tjrs 0. Plus utilisé
        this.writeEntier(writer, "0");

        // 37. première année de cotisation, tjrs 0. Plus utilisé
        this.writeEntier(writer, "0");

        // 38. année du montant du ram
        this.writeEntier(writer, wrapper.getAnneeMontantRAM());

        // 39. durée cotis.étrangère av. 73
        this.writeEntier(writer, PRDateFormater.convertDate_AAxMM_to_AAMM(wrapper.getDureeCotiEtrangereAv73()));

        // 40.durée cotis. étrangère ap. 73
        this.writeEntier(writer, PRDateFormater.convertDate_AAxMM_to_AAMM(wrapper.getDureeCotiEtrangereAp73()));

        // 41. transféré
        if ((wrapper.getIsTransfere() != null) && wrapper.getIsTransfere().booleanValue()) {
            b = "T";
        } else {
            b = "F";
        }

        this.writeChaine(writer, b);

        // 42. droitappliqué
        this.writeEntier(writer, wrapper.getDroitApplique());

        //
        this.writeChampVideSansFinDeChamp(writer);
    }
}
