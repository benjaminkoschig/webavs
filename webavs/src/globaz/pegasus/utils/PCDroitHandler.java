package globaz.pegasus.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import globaz.pegasus.vb.droit.PCDroitListViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletRequest;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.VersionDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCDroitHandler {

    public static String formatteDescriptionMembreFamille(BSession objSession, DroitMembreFamille membreFamille) {
        return membreFamille.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()

                + (JadeStringUtil.isBlankOrZero(membreFamille.getMembreFamille().getDateDeces()) ? ""
                        : "<span  style=color:red> ( </span><span style=font-family:wingdings>U</span>&nbsp;"
                                + membreFamille.getMembreFamille().getDateDeces() + "<span  style=color:red> )</span>")

                + " / " + membreFamille.getMembreFamille().getNom() + " "
                + membreFamille.getMembreFamille().getPrenom() + " / "
                + membreFamille.getMembreFamille().getDateNaissance() + " / "
                + PCUserHelper.getLibelleCourtSexe(membreFamille.getMembreFamille().getPersonneEtendue()) + " / "
                + PCUserHelper.getLibellePays(membreFamille.getMembreFamille().getPersonneEtendue().getTiers());
    }

    public static String getFromattedTitreHTML(BSession objSession, MembreFamilleEtendu membreFamille) {
        return getFromattedTitreHTML(objSession, membreFamille.getDroitMembreFamille(), membreFamille
                .getSimpleDonneesPersonnelles().getNoCaisseAvs());
    }

    public static String getFromattedTitreHTML(BSession objSession, DroitMembreFamille membreFamille,
            String numeroCaisseAvs) {
        String html = "<h1>"
                + objSession.getCodeLibelle(membreFamille.getSimpleDroitMembreFamille().getCsRoleFamillePC())
                + "</h1><p>" + PCDroitHandler.formatteDescriptionMembreFamille(objSession, membreFamille)
                + " <a class='lienTiers external_link' href='./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="
                + membreFamille.getMembreFamille().getPersonneEtendue().getTiers().getIdTiers() + "'>"
                + objSession.getLabel("JSP_PC_DECALCUL_D_TIERS") + "</a>"
                + "<span class='areaTitreAVS'><span class='labelNoAvs'>"
                + objSession.getLabel("JSP_PC_DROIT_L_NO_AVS_CAISSE") + "</span> " + numeroCaisseAvs + "</span></p>";
        return html;
    }

    public static String getInfoHtmlRequerant(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille requerant) {
        return PCDroitHandler.getInfoHtmlRequerant(droit.getSimpleVersionDroit(), requerant.getMembreFamille()
                .getPersonneEtendue());
    }

    public static String getInfoHtmlRequerant(SimpleVersionDroit droit, PersonneEtendueComplexModel requerant) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        String result = "";
        if (requerant != null) {
            result = "<div><span class='labelRequerant' idTiersMembreFamilleRequerant='"
                    + requerant.getTiers().getIdTiers() + "'>"
                    + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_NO_VERSION_REQUERANT")
                    + "</span> <span id='requerant' class='requerantDecalage'>"
                    + PCUserHelper.getDetailAssure(session, requerant) + "</span></div>";

            result += "<div id='infoDroit' idVersionDroit='"
                    + droit.getIdVersionDroit()
                    + "'>"
                    + "<span> "
                    + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_NO_VERSION")
                    + "</span><span id='infoDroitNoVersion' class='data'>"
                    + droit.getNoVersion()
                    + "</span> <br /><span id='areaAssureIdCsEtatDroit' name='areaAssureIdCsEtatDroit' areaAssureIdCsEtatDroit='"
                    + droit.getCsEtatDroit() + "'> " + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_ETAT")
                    + " </span>" + session.getCodeLibelle(droit.getCsEtatDroit()) + "<br /><span >"
                    + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_DATE_ANNONCE")
                    + " </span><span id='infoDroitDateAnnonce' class='data'>" + droit.getDateAnnonce()
                    + "</span></div>";
        }
        return "<div class='areaAssure head ui-widget-content'>" + result + "</div>";
    }

    public static String getInfoHtmlRequerant(String idVersionDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (idVersionDroit == null) {
            throw new DroitException(
                    "Unable to getInfoHtmlRequerant idVersionDroit, the idVersionDroit passed is null!");
        }

        VersionDroitMembreFamilleSearch search = new VersionDroitMembreFamilleSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search.setForCsRoletMembreFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        search = PegasusImplServiceLocator.getDroitMembreFamilleService().search(search);
        VersionDroitMembreFamille membreFamille = null;
        if (search.getSize() == 1) {
            membreFamille = (VersionDroitMembreFamille) search.getSearchResults()[0];
        } else {
            throw new DroitException("can't retrieve droit. " + search.getSize()
                    + " element(s) was found for the idVersion(" + idVersionDroit + ")");
        }

        return PCDroitHandler.getInfoHtmlRequerant(membreFamille.getSimpleVersionDroit(), membreFamille
                .getMembreFamille().getPersonneEtendue());
    }

    public static String getInfoHtmlRequerantByIdDemande(String idDemande) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DroitSearch search = new DroitSearch();
        Droit droit = null;
        search.setWhereKey(DroitSearch.CURRENT_VERSION);
        search.setForIdDemandePc(idDemande);
        search = PegasusServiceLocator.getDroitService().searchDroit(search);
        if (search.getSize() == 1) {
            droit = (Droit) search.getSearchResults()[0];
        } else {
            throw new DroitException("can't retrieve droit. " + search.getSize()
                    + " element(s) was found for the idDemande(" + idDemande + ")");
        }
        return PCDroitHandler.getInfoHtmlRequerant(droit.getSimpleVersionDroit().getIdVersionDroit());
    }

    /**
     * Retourne une descriptin du requerant mais limité à la demande
     * 
     * @param idDemande
     * @return
     * @throws DemandeException
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static String getInfoHtmlRequerantForDemande(String idDemande) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // chargement de la demande
        Demande demande = PegasusServiceLocator.getDemandeService().read(idDemande);
        // personne etendue
        PersonneEtendueComplexModel requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();

        BSession session = BSessionUtil.getSessionFromThreadContext();
        String result = "";

        if (requerant != null) {
            result = "<div><span class='labelRequerant' idTiersMembreFamilleRequerant="
                    + requerant.getTiers().getIdTiers() + ">"
                    + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_NO_VERSION_REQUERANT")
                    + "</span> <span id='requerant' class='requerantDecalage'>"
                    + PCUserHelper.getDetailAssure(session, requerant) + "</span></div>";

            result += "<div id='infoDroit'>" + "<span> " + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_NO_DEMANDE")
                    + "</span><span id='infoDroitNoVersion' class='data'>" + demande.getSimpleDemande().getIdDemande()
                    + "</span> <br /><span> " + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_ETAT_DEMANDE")
                    + " </span>" + session.getCodeLibelle(demande.getSimpleDemande().getCsEtatDemande())
                    + "<br /><span >" + session.getLabel("JSP_PC_HEADER_DETAIL_ASSURE_DATE_DEPOT")
                    + " </span><span id='infoDroitDateAnnonce' class='data'>"
                    + demande.getSimpleDemande().getDateDepot() + "</span></div>";
        }

        return "<div class='areaAssure head ui-widget-content'>" + result + "</div>";

    }

    public static String getOngletHtml(BSession objSession, PCAbstractRequerantDonneeFinanciereViewBean viewBean,
            String[][] codeOnglet, ServletRequest request, String path) {
        String theUsrAction = request.getParameter("userAction");
        String paramURL = ".afficher&noVersion=" + viewBean.getNoVersion() + "&idVersionDroit="
                + viewBean.getIdVersion() + "&idDossier=" + viewBean.getIdDossier();
        String ongletLink = path + "?idDroit=" + viewBean.getDroit().getId() + "&selectedId="
                + viewBean.getDroit().getId() + "&userAction=";
        String li = "";

        String titreMenu = "&idTitreMenu=" + request.getParameter("idTitreMenu");

        for (Iterator it = Arrays.asList(codeOnglet).iterator(); it.hasNext();) {
            String[] tuple = (String[]) it.next();
            if ((tuple[2] + ".afficher").equals(theUsrAction)) {
                li = li + "<li class='selected'>" + objSession.getLabel(tuple[0]) + "</li>";
            } else {
                li = li + "<li><a href=\"" + ongletLink + tuple[2] + paramURL + titreMenu + "&titreOnglet="
                        + objSession.getLabel(tuple[0]) + "\" title=\"" + objSession.getLabel(tuple[0]) + "\">"
                        + objSession.getLabel(tuple[1]) + "</a>";
            }

        }
        return "<ul class='onglets'>" + li + "</ul>";
    }

    public static String getRequerantDetail(BSession session, DroitMembreFamille membreFamille) {
        String result = "";
        if (membreFamille != null) {
            result = PCUserHelper.getDetailAssure(session, membreFamille.getMembreFamille().getPersonneEtendue());
        }
        return result;
    }

    public static String getRequerantDetail(BSession session, String idDroit) throws DroitException {
        DroitMembreFamilleEtenduSearch search = new DroitMembreFamilleEtenduSearch();
        search.setForIdDroit(idDroit);
        List<String> list = new ArrayList<String>();
        list.add(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        String detail = null;
        search.setForCsRoletMembreFamilleIn(list);
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new DroitException("Unable to getRequerantDetail, the idDroit passed is null!");
        }
        try {
            if (idDroit == null) {
                throw new DroitException("Unable to getRequerantDetailByidDroit, the idDroit is null!");
            }
            search = PegasusServiceLocator.getDroitService().searchDroitMemebreFamilleEtendu(search);
            if (search.getSize() == 1) {
                DroitMembreFamille membreFamille = ((DroitMembreFamilleEtendu) search.getSearchResults()[0])
                        .getDroitMembreFamille();
                detail = PCDroitHandler.getRequerantDetail(session, membreFamille);
            } else {
                throw new DroitException("Unable to find the requerant too many values");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Unable to find the requerant", e);
        } catch (JadePersistenceException e) {
            throw new DroitException("Unable to find the requerant", e);
        }
        return detail;
    }

    /**
     * Utilise uniquement dans l'ecran listant les versions de droit d'une demande
     * 
     * @param listVb
     * @return true si le ListViewBean contient une version de droit modifiable, false dans les autres cas
     */
    public static boolean hasVersionDroitModifiable(PCDroitListViewBean listVb) {
        if (listVb == null) {
            return false;
        }
        Iterator iter = listVb.iterator();
        while (iter.hasNext()) {
            Droit droit = (Droit) iter.next();
            if (IPCDroits.CS_ENREGISTRE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                    || IPCDroits.CS_AU_CALCUL.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                    || IPCDroits.CS_CALCULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Définit si un droit provient de la reprise de données<br/>
     * Se base pour cela sur les code systèmes suivants:<br/>
     * - 64063011, reprise<br/>
     * - 64063014, reprise adaptation erronée
     * 
     * @exception IllegalArgumentException
     *                si null ou vide
     * @param csMotifDroit
     * @return la correspondance (equals) du code système définit dans le droit avec les deux définis pour la reprise
     */
    public static boolean isDroitFromReprise(final String csMotifDroit) {

        if (JadeStringUtil.isBlank(csMotifDroit)) {
            throw new IllegalArgumentException("The cs Motif Droit passed is not allowed: [" + csMotifDroit + "]");
        }
        return csMotifDroit.equals(IPCDroits.CS_MOTIF_DROIT_REPRISE)
                || csMotifDroit.equals(IPCDroits.CS_MOTIF_DROIT_REPRISE_ADAPTATION_ERRONE);
    }

    /**
     * Permet de savoir si une version du droit peut etre corrigee
     * 
     * @param droit
     *            la version du droit
     * @return true si la version du droit peut etre corrigee, false dans les autre cas.
     */
    public static boolean isVersionDroitCorrigeable(Droit droit) {
        if (IPCDroits.CS_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
            // on cherche si le droit existe dans une version modifiable
            DroitSearch droitSearch = new DroitSearch();
            droitSearch.setForCsEtatDroitIn(Arrays.asList(IPCDroits.CS_ENREGISTRE, IPCDroits.CS_AU_CALCUL,
                    IPCDroits.CS_CALCULE));
            droitSearch.setForIdDemandePc(droit.getDemande().getSimpleDemande().getIdDemande());
            try {
                return PegasusServiceLocator.getDroitService().count(droitSearch) == 0;
            } catch (DroitException e) {
                e.printStackTrace();
                return false;
            } catch (JadeApplicationServiceNotAvailableException e) {
                e.printStackTrace();
                return false;
            } catch (JadePersistenceException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Permet de savoir si une version du droit peut etre corrigee
     * 
     * @param csEtatDroit
     *            l'etat du droit
     * @param hasDemandeVersionDroitModifiable
     *            indique si une des versions du droit peut etre modifiee
     * @return true si la version du droit peut etre corrigee, false dans les autre cas.
     */
    public static boolean isVersionDroitCorrigeable(String csEtatDroit, boolean hasDemandeVersionDroitModifiable) {
        return (IPCDroits.CS_VALIDE.equals(csEtatDroit)) && !hasDemandeVersionDroitModifiable;
    }

    /**
     * Permet de savoir si une version du droit peut etre supprimee.
     * 
     * @param csEtatDroit
     *            l'etat du droit
     * @return true si la version du droit peut etre supprimee, false dans les autres cas.
     */
    public static boolean isVersionDroitSupprimable(String csEtatDroit) {
        return (IPCDroits.CS_ENREGISTRE.equals(csEtatDroit) || IPCDroits.CS_AU_CALCUL.equals(csEtatDroit) || IPCDroits.CS_CALCULE
                .equals(csEtatDroit));
    }

}
