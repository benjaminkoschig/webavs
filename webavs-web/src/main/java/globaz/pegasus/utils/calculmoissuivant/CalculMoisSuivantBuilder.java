package globaz.pegasus.utils.calculmoissuivant;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import globaz.pegasus.vb.droit.PCCalculDroitViewBean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.calcul.CalculMoisSuivant;
import ch.globaz.pegasus.business.models.calcul.CalculMoisSuivantSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

/**
 * Classe chargée de gérer l'affichage html de la liste des dinnées financières pour le calcul mois suivant
 * 
 * Projet Pegasus (v1.11)
 * 
 * @author sce
 * @date 26.10.2012
 */
public class CalculMoisSuivantBuilder {

    /** Collection de configuration du mapping des propriétés des DF */
    private static Map<String, DonneeFinanciereDescriptor> donneFinanciereDroitDescriptor = null;

    private static final String HTML_DIV_CLOSE = "</div>";
    /** balise html */
    private static final String HTML_SPAN_CLOSE = "</span>";

    /** Label du libelle lien df */
    private static final String LABEL_LIBELLE_LIEN_DF = "JSP_CMS_LIBELLE_LINK_DF";
    /** Label du titre du bloc */
    private static final String LABEL_TITRE_BLOC = "JSP_PC_CACUL_DROIT_D_DF_MODIF_CREE";
    /** Classe css periodes html */
    private static final String MANY_PERIOD_ERROR_CSS_CLASS = "period_error";
    /** Propriété date de debut et de fin */
    private static final String PROPRIETE_DATEDEBUT_DF = "dateDebutDonneeFinanciere";

    private static final String PROPRIETE_DATEFIN_DF = "dateFinDonneeFinanciere";
    /** Propriété pour titre de la donnée fianacière */
    private static final String PROPRIETE_TYPE_DF = "csTypeDonneeFinanciere";

    /** Classe css periodes html */
    private static final String RETRO_PERIOD_ERROR_CSS_CLASS = "retro_warning";

    /**
     * Constructeur statique Appel de la collection de configuration depuis le provider
     */
    static {
        CalculMoisSuivantBuilder.donneFinanciereDroitDescriptor = DonneesFinancieresDescriptorProvider
                .getConfigurationMap();

    }

    /**
     * Methode retournant le flux html à afficher dans la page de préparation du calcul Contient la liste des données
     * financiere modifies ou cres pour cette version
     * 
     * @param le
     *            modèle de recherche des données financières à traiter
     * @return la liste des id des donnes financieres modifies pour la version
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CalculException
     * @throws PmtMensuelException
     * @throws PrixChambreException
     */
    public static void build(PCCalculDroitViewBean vb) throws CalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PmtMensuelException,
            PrixChambreException {

        // liste des identityGroup a passé au calculateur
        List<String> listeIdEntityGroup = new ArrayList<String>();

        // liste des données de la version pour le viewBean
        List<CalculMoisSuivant> listeDonneesFinancieresForVb = new ArrayList<CalculMoisSuivant>();

        // etat erreur periode modifiées plusieurs fois
        boolean calculMoisSuivantBlocked = false;
        boolean donneesModifOrCreateForVersion = false;

        // Appel du service et construction du flux html des données financières modifiés
        CalculMoisSuivantSearch dfDroitSerach = PegasusImplServiceLocator.getCalculDroitService()
                .searchDonneesFinancieresForVersionDroit(vb.getDroit());

        // Création du bloc principal (titre + zone)
        StringBuilder fluxHtml = new StringBuilder("");
        fluxHtml.append(CalculMoisSuivantBuilder.buildBlocPrincipal());

        // traitement si pas de résultats --> pas de données modifiés ou créer pour cette version
        if (dfDroitSerach.getSearchResults().length == 0) {
            fluxHtml.append(CalculMoisSuivantBuilder.buildTexteNoDF());
        } else {
            // Liste typé du modèle de recherche
            @SuppressWarnings("unchecked")
            List<CalculMoisSuivant> list = PersistenceUtil.typeSearch(dfDroitSerach, dfDroitSerach.whichModelClass());

            // ajout du champ position dans les entités. Récupére les positions des propriétéé définis dans la map de
            // concfiguration pour les injecter dans le modèle, tri de la liste (ordre d'affichage des DF)
            CalculMoisSuivantBuilder.injectAndSortDonneesFinancieresByPosistion(list);

            // map groupés
            Map<String, List<CalculMoisSuivant>> dfsByMembreFamille = JadeListUtil.groupBy(list,
                    new Key<CalculMoisSuivant>() {
                        @Override
                        public String exec(CalculMoisSuivant e) {
                            return e.getDroitMembreFamille().getSimpleDroitMembreFamille().getIdDroitMembreFamille();
                        }
                    });

            // iteration sur les membre de familles
            for (String idMembreFamille : dfsByMembreFamille.keySet()) {
                // Reécupération des données pour le membre de famille
                DroitMembreFamille droitMembreFamille = (dfsByMembreFamille.get(idMembreFamille).get(0))
                        .getDroitMembreFamille();
                SimpleDonneesPersonnelles simpleDonneesPersonelles = (dfsByMembreFamille.get(idMembreFamille).get(0))
                        .getSimpleDonneesPersonelles();
                // Ajout des infos du membre de fmaille
                fluxHtml.append(CalculMoisSuivantBuilder.buildBlocMembreFamille(droitMembreFamille,
                        simpleDonneesPersonelles));

                // check des doubles modifications
                ArrayList<CalculMoisSuivant> dfErrorsList = CalculMoisSuivantBuilder
                        .checkDonneesFinancieresByMembreFamille(dfsByMembreFamille.get(idMembreFamille));

                // check des df 100% retro
                ArrayList<CalculMoisSuivant> dfRetroDfList = CalculMoisSuivantBuilder
                        .checkDonneesFinancieresForDFRetro(dfsByMembreFamille.get(idMembreFamille));

                // Si la liste contient une entrés, blocage calcul mois sauivants
                calculMoisSuivantBlocked = dfErrorsList.size() > 0;

                // Iteration sur les donness financieres du membre de famille
                for (CalculMoisSuivant donneeFinanciere : dfsByMembreFamille.get(idMembreFamille)) {

                    // on traite la donnée, si elle doit etre prise en compte
                    if (isDonneeNoCopieOrClose(donneeFinanciere)) {
                        donneesModifOrCreateForVersion = true;
                        // Ajout dans la liste de retour avec exclusion de la liste retro
                        if (!dfRetroDfList.contains(donneeFinanciere)) {
                            listeIdEntityGroup.add(donneeFinanciere.getIdEntityGroup());
                        }
                        // ajout dans liste vb
                        listeDonneesFinancieresForVb.add(donneeFinanciere);
                        // recupération du cs de la donnee financiere
                        String csDF = donneeFinanciere.getCsTypeDonneeFinanciere();

                        fluxHtml.append(CalculMoisSuivantBuilder.builDonneeFinanciereHtmlLignes(csDF, donneeFinanciere,
                                dfErrorsList.contains(donneeFinanciere), dfRetroDfList.contains(donneeFinanciere)));
                    }

                }
            }
        }

        // Cloture des balises
        fluxHtml.append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE).append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        // retour du flux à afficher
        vb.setdFForVersionListHtml(fluxHtml.toString());
        // Set etat autorisation calcul mois suivant
        vb.setCMSBlocedkDuToError(calculMoisSuivantBlocked);
        vb.setIsDfModifForVersion(donneesModifOrCreateForVersion);
        // Set liste df modifie
        vb.setDonneesFinancieresForVersion(listeIdEntityGroup);
        // set liste donneeForVersion
        vb.setListeDFModifieForVersion(listeDonneesFinancieresForVb);

    }

    /**
     * Retourne l'état de prise en compte de la donnes financiere
     * -> La données ne doit pas etre une copie de la version precedent
     * -> Si c'est une copie de la version precedente, elle doit etre close
     * 
     * @return
     */
    private static boolean isDonneeNoCopieOrClose(CalculMoisSuivant df) {

        // opérateur ou court-circuit, on évalue periode close seulement si copie
        return !df.getIsCopieFromPreviousVersion() || df.getIsPeriodeClose();

    }

    /**
     * Constrcution du bloc de description du membre de fmaille
     * 
     * @param droitMembreFamille
     * @param simpleDonneesPersonelles
     * @return le flux html
     */
    private static String buildBlocMembreFamille(DroitMembreFamille droitMembreFamille,
            SimpleDonneesPersonnelles simpleDonneesPersonelles) {
        StringBuilder htmlStr = new StringBuilder("<div class=\"gl-header mbr-famille\">");
        htmlStr.append(CalculMoisSuivantBuilder.getMembreFamilleInfoTitre(droitMembreFamille, simpleDonneesPersonelles))
                .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        return htmlStr.toString();
    }

    /**
     * Construction du bloc principal à afficher dans la page. Bloc titre et contenu
     * 
     * @return le flux html à afficher
     */
    private static String buildBlocPrincipal() {
        StringBuilder htmlStr = new StringBuilder("");
        htmlStr.append("<div id=\"df_modif\" class=\"titre\">");
        htmlStr.append("<h1 class=\"ui-widget-header \">");
        // label titre bloc
        String libelleTitre = CalculMoisSuivantBuilder.getLabel(CalculMoisSuivantBuilder.LABEL_TITRE_BLOC);
        htmlStr.append(libelleTitre).append("</h1>");
        htmlStr.append("<div id=\"contenu_df_modif\" class=\"gl-pre-content\" style=\"background-color: #FFFFFF;\" >");
        return htmlStr.toString();
    }

    /**
     * Methode de construction de la ligne de la donnee financiere
     * 
     * @param isManyPeriodError
     *            définit si la df est en erreur (plusieurs periodes modifiées)
     * @param csDf
     * @param donneeFinanciere
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PrixChambreException
     */
    @SuppressWarnings("unchecked")
    private static String builDonneeFinanciereHtmlLignes(String csDf, CalculMoisSuivant donneeFinanciere,
            Boolean isManyPeriodError, Boolean isRetro) throws PrixChambreException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // getsion erreurs de périodes
        String manyPeriodErrorClass = "";
        String retroPeriodCssClass = "";

        if (isManyPeriodError) {
            manyPeriodErrorClass = CalculMoisSuivantBuilder.MANY_PERIOD_ERROR_CSS_CLASS;
        }
        // gestion warning retro
        if (isRetro) {
            retroPeriodCssClass = CalculMoisSuivantBuilder.RETRO_PERIOD_ERROR_CSS_CLASS;
        }

        String url = CalculMoisSuivantBuilder.buildUrlForDf(donneeFinanciere);

        StringBuilder htmlStr = new StringBuilder("<div data-url=\"" + url + "\" class=\"row-fluid row-type-df "
                + manyPeriodErrorClass + retroPeriodCssClass + "\">");

        /********************* Titre de la donnée financiere **********************/
        htmlStr.append("<div class=\"span2\">")
                .append(CalculMoisSuivantBuilder.getLibelle(CalculMoisSuivantBuilder.getFieldValue(
                        CalculMoisSuivantBuilder.PROPRIETE_TYPE_DF, donneeFinanciere))).append("</div>");
        /********************* Période de la donnée financiere **********************/
        StringBuilder dateDebut = new StringBuilder(CalculMoisSuivantBuilder.getFieldValue(
                CalculMoisSuivantBuilder.PROPRIETE_DATEDEBUT_DF, donneeFinanciere));
        String dateFin = CalculMoisSuivantBuilder.getFieldValue(CalculMoisSuivantBuilder.PROPRIETE_DATEFIN_DF,
                donneeFinanciere);
        String periode = dateDebut.append(" - ").append(dateFin).toString();

        htmlStr.append("<div class=\"span2\">").append(periode).append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);

        /********************* Lien url donnée financière ****************************/
        // lien df
        htmlStr.append("<div class=\"span4\">").append("<a data-g-externallink=\" \" href=\"").append(url)
                .append("\">")
                .append(CalculMoisSuivantBuilder.getLabel(CalculMoisSuivantBuilder.LABEL_LIBELLE_LIEN_DF))
                .append("</a>").append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);

        // on clot la balie titre df
        htmlStr.append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);

        // Traitement spécifique taxeJournaliereHome

        // Récupération des propritétés a afficher de la df
        ArrayList<DonneeFinancierePropertiesDescriptor> dfProperties = CalculMoisSuivantBuilder.donneFinanciereDroitDescriptor
                .get(csDf).getProprietes();
        // Tri de la liste en fonction des positions
        Collections.sort(dfProperties);
        // balise debut de ligne
        htmlStr.append("<div class=\"row-fluid row-line-df\">");
        // Traitement spécifique tyeJournaliereHome
        if (donneeFinanciere.getCsTypeDonneeFinanciere().equals(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE)) {
            htmlStr.append(CalculMoisSuivantBuilder.dealTaxesJournalieresHomePropertiesLine(donneeFinanciere));
        } else {
            // Parcours de la liste des propriétés à afficher
            for (DonneeFinancierePropertiesDescriptor property : dfProperties) {
                // Si la reflexion a posé problème, on ne gère pas la propriété
                String fieldValue = CalculMoisSuivantBuilder
                        .getFieldValue(property.getPropertyName(), donneeFinanciere);

                if (fieldValue != null) {
                    // Recupération de la valeur de la propriété avec reflexion
                    htmlStr.append("<div class=\"span2 cell-prop\">")
                            .append(CalculMoisSuivantBuilder.getLabel(property.getI18nLabel()))
                            .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);

                    htmlStr.append(CalculMoisSuivantBuilder.generateValueField(property, fieldValue));
                }
            }
        }

        htmlStr.append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        return htmlStr.toString();

    }

    private static String buildTexteNoDF() {
        StringBuilder htmlStr = new StringBuilder("");
        htmlStr.append("<div class=\"nodf\">");
        htmlStr.append(CalculMoisSuivantBuilder.getLabel("JSP_CMS_NODF")).append(
                CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        return htmlStr.toString();
    }

    private static String buildUrlForDf(CalculMoisSuivant donneeFinanciere) {
        StringBuilder userActionUrl = new StringBuilder("pegasus?userAction=");
        // Recup config
        DonneeFinanciereDescriptor desc = CalculMoisSuivantBuilder.donneFinanciereDroitDescriptor.get(donneeFinanciere
                .getCsTypeDonneeFinanciere());
        // ajout user action
        userActionUrl.append(desc.getUserAction()).append(".afficher");
        // selected ID, idDroit
        userActionUrl.append("&selectedId=").append(donneeFinanciere.getIdDroit());
        // idDemandePC
        userActionUrl.append("&idDemandePc=").append(donneeFinanciere.getIdDemandePc());
        // id dossier
        userActionUrl.append("&idDossier=").append(donneeFinanciere.getIdDossier());
        // no version
        userActionUrl.append("&noVersion=").append(donneeFinanciere.getNoVersion());
        // idVersionDroit
        userActionUrl.append("&idVersionDroit=").append(donneeFinanciere.getIdVersionDroit());
        // titre menu
        userActionUrl.append("&idTitreMenu=").append(desc.getIdTitreMenu());
        // zitre onglet
        userActionUrl.append("&idTitreOnglet=").append(desc.getTitreOnglet());

        return userActionUrl.toString();

    }

    /**
     * Vérification des données financières. La vérification va regarder: - pour une version de droit la période d'une
     * donnée fianancière doit avoir été modifié une et une seule fois
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PmtMensuelException
     */
    private static ArrayList<CalculMoisSuivant> checkDonneesFinancieresByMembreFamille(
            List<CalculMoisSuivant> listeDFbyMF) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {

        String dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();

        // Liste des df en erreur (df ayant plus de une modif dans cette version)
        ArrayList<CalculMoisSuivant> listeDFDoubleModification = new ArrayList<CalculMoisSuivant>();

        // groupement par type df
        Map<String, List<CalculMoisSuivant>> dfsByMembreFamilleGroupByEntityGroup = CalculMoisSuivantBuilder
                .returnMapGroupeByEntityGroupDF(listeDFbyMF);

        // entity group
        for (String dfEntityGroup : dfsByMembreFamilleGroupByEntityGroup.keySet()) {

            // booléen servant à détecter si une période de deonnées financière à une date de debut egale à la date mois
            // suivant,
            // et si, pour le meme entityGroup, une donne financiere la precede immédiatement (Cas de copie de df
            // interne, suite à "valider")
            boolean isDFWithDateDebutMoisSuivant = false;
            boolean isDFWithDateFinPrecedentMoisSuivant = false;

            // on itere sur les df, exclusions des copies
            int dfByEntityGroup = 0;
            for (CalculMoisSuivant dfParEntityGroup : dfsByMembreFamilleGroupByEntityGroup.get(dfEntityGroup)) {

                if (!dfParEntityGroup.getIsCopieFromPreviousVersion()) {
                    // gestion du cas de copie "interne" de df
                    if (dfParEntityGroup.getDateDebutDonneeFinanciere().equals(dateProchainPaiement)) {
                        isDFWithDateDebutMoisSuivant = true;
                    }
                    if (dfParEntityGroup.getDateFinDonneeFinanciere()
                            .equals(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                                    + dateProchainPaiement, -1)))) {
                        isDFWithDateFinPrecedentMoisSuivant = true;
                    }

                    dfByEntityGroup++;
                }
            }

            boolean dfCopieInterne = (isDFWithDateDebutMoisSuivant && isDFWithDateFinPrecedentMoisSuivant);

            // Si le nombre d'occurence est plus grand que 1, et que la detection des dates de debut et de fin rapport a
            // la date moissuivant est false
            // on ajoutes les entités des df dans la liste d'erreurs
            if ((dfByEntityGroup > 1) && !dfCopieInterne) {

                listeDFDoubleModification.addAll(dfsByMembreFamilleGroupByEntityGroup.get(dfEntityGroup));

            }
        }
        return listeDFDoubleModification;
    }

    /**
     * Construction d'une liste des données financières 100% retro
     * 
     * @param listeDFbyMF
     * @return
     */
    private static ArrayList<CalculMoisSuivant> checkDonneesFinancieresForDFRetro(List<CalculMoisSuivant> listeDFbyMF) {
        // liste des df 100% retro
        ArrayList<CalculMoisSuivant> listeDF100Retro = new ArrayList<CalculMoisSuivant>();

        // groupement par type df
        Map<String, List<CalculMoisSuivant>> dfsByMembreFamilleGroupByEntityGroup = CalculMoisSuivantBuilder
                .returnMapGroupeByEntityGroupDF(listeDFbyMF);

        // itération sur les entityGroup
        for (String dfEntityGroup : dfsByMembreFamilleGroupByEntityGroup.keySet()) {
            // Si une et une seule donnée financière par entityGroup et que date de fin fermé --> calcul 100% retro
            // if (dfsByMembreFamilleGroupByEntityGroup.get(dfEntityGroup).size() == 1) {
            // Si date de fin non vide
            for (CalculMoisSuivant donneeUnique : dfsByMembreFamilleGroupByEntityGroup.get(dfEntityGroup)) {
                if (!JadeStringUtil.isBlank(donneeUnique.getDateFinDonneeFinanciere())) {
                    listeDF100Retro.add(donneeUnique);
                }
            }

        }

        return listeDF100Retro;

    }

    /**
     * Gestion spécifique de la ligne des taxes journalières
     * 
     * @param donneeTaxesJournaliereHome
     * @return
     * @throws PrixChambreException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private static String dealTaxesJournalieresHomePropertiesLine(CalculMoisSuivant donneeTaxesJournaliereHome)
            throws PrixChambreException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        StringBuilder htmlStr = new StringBuilder("");

        // recupération des id pour lancer le read spécifique
        String idTypeChambre = donneeTaxesJournaliereHome.getTaxeJournaliereIdTypeChambre();
        String idHome = donneeTaxesJournaliereHome.getTaxeJournaliereIdHome();

        // instanciation du modèle de recherche
        PrixChambreSearch search = new PrixChambreSearch();
        search.setForIdTypeChambre(idTypeChambre);
        search.setForIdHome(idHome);
        search.setForDateDebut(donneeTaxesJournaliereHome.getDateDebutDonneeFinanciere());
        search.setForDateFin(donneeTaxesJournaliereHome.getDateFinDonneeFinanciere());
        search = PegasusImplServiceLocator.getPrixChambreService().search(search);

        PrixChambre pchambre = (PrixChambre) search.getSearchResults()[0];
        String montant = pchambre.getSimplePrixChambre().getPrixJournalier();
        String designation = pchambre.getTypeChambre().getSimpleTypeChambre().getDesignation();
        String type = pchambre.getTypeChambre().getSimpleTypeChambre().getCsCategorie();
        String descHome = pchambre.getTypeChambre().getHome().getSimpleHome().getNomBatiment();
        Boolean isVersement = donneeTaxesJournaliereHome.getTaxeJournaliereIsVersementDirect();

        // Construction de la propriété montant
        htmlStr.append("<div class=\"span2 cell-prop\">")
                .append(CalculMoisSuivantBuilder.getLabel("JSP_CMS_64007008_MONTANTJOURNALIER"))
                .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        // valeur
        htmlStr.append("<div class=\"span1\">").append(montant).append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        // Construction de la description
        htmlStr.append("<div class=\"span2 cell-prop\">")
                .append(CalculMoisSuivantBuilder.getLabel("JSP_CMS_64007008_DESC"))
                .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        // Valeur
        StringBuilder valueDesc = new StringBuilder(descHome).append(" - ").append(designation).append("/")
                .append(CalculMoisSuivantBuilder.getLibelle(type));
        htmlStr.append("<div class=\"span2\">").append(valueDesc.toString())
                .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        //Valeur Versement Direct
        if(isVersement){
            htmlStr.append("<div class=\"span2 cell-prop\">")
                    .append(CalculMoisSuivantBuilder.getLabel("JSP_CMS_64007008_VERSEMENT_DIRECT"))
                    .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
            htmlStr.append("<div class=\"span1\">").append(CalculMoisSuivantBuilder.getLabel("JSP_CMS_64007008_VERSEMENT_DIRECT_COCHE")).append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
        }




        return htmlStr.toString();
    }

    /**
     * Génération de la balise pour la valeur On va aiguiller l'ajout ou pas du formattage du montant
     * 
     * @param property
     * @param value
     * @return la chaine de caratère généré
     */
    private static String generateValueField(DonneeFinancierePropertiesDescriptor property, String value) {
        StringBuilder returnHtml = new StringBuilder("");

        // Si la valeur n'est pas nulle
        if (value != null) {
            // Si value cs, on applique pas le data g amount
            if (property.isCSToTranslate()) {
                String valueStr = CalculMoisSuivantBuilder.getLibelle(value);
                StringBuilder valueB = new StringBuilder("");

                if (valueStr.length() > 8) {
                    valueB = new StringBuilder(valueStr.substring(0, 5));
                    valueB.append("...");
                } else {
                    valueB = valueB.append(valueStr);
                }

                returnHtml.append("<div class=\"span1\">").append("<span data-g-bubble=\"wantMarker:false,text:¦")
                        .append(valueStr).append("¦\">").append(valueB.toString())
                        .append(CalculMoisSuivantBuilder.HTML_SPAN_CLOSE)
                        .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);

            } else {

                returnHtml.append("<div class=\"span1\"><span data-g-amount=\" \">").append(value)
                        .append(CalculMoisSuivantBuilder.HTML_SPAN_CLOSE)
                        .append(CalculMoisSuivantBuilder.HTML_DIV_CLOSE);
            }
        }

        return returnHtml.toString();
    }

    public static Map<String, DonneeFinanciereDescriptor> getDonneFinanciereDroitDescriptor() {
        return CalculMoisSuivantBuilder.donneFinanciereDroitDescriptor;
    }

    /**
     * Retourne par introspection la valeur du champ
     * 
     * @param fieldName
     * @return la valeur sous forme de string
     */
    private static String getFieldValue(String fieldName, Object obj) {

        Field field;
        String value = null;

        try {
            field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(Boolean.TRUE);
            value = field.get(obj).toString();
        } catch (SecurityException e) {
            JadeLogger.error(
                    CalculMoisSuivantBuilder.class,
                    "the property " + fieldName + " cannot be getting by refexion!. SecurityException occured: "
                            + e.getMessage() + e.getStackTrace());
        } catch (NoSuchFieldException e) {
            JadeLogger
                    .error(CalculMoisSuivantBuilder.class,
                            "the property " + fieldName + " cannot be getting by refexion!. The field doesn't exist: "
                                    + e.getMessage() + e.getStackTrace());
        } catch (IllegalArgumentException e) {
            JadeLogger.error(CalculMoisSuivantBuilder.class,
                    "the property " + fieldName + " cannot be getting by refexion!. IllegalArgumentException occured: "
                            + e.getMessage() + e.getStackTrace());
        } catch (IllegalAccessException e) {
            JadeLogger.error(CalculMoisSuivantBuilder.class, "the property " + fieldName
                    + " cannot be getting by refexion!. IllegalAccessException exception occured: " + e.getMessage()
                    + e.getStackTrace());
        }
        return value;

    }

    /**
     * retourne le label en session
     * 
     * @return le label
     */
    private static String getLabel(String label) {
        if (label == null) {
            return "";
        }
        return BSessionUtil.getSessionFromThreadContext().getLabel(label);
    }

    /**
     * retourne le libelle en session
     * 
     * @return le libelle
     */
    private static String getLibelle(String csCode) {
        if (csCode == null) {
            return "";
        }
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csCode);
    }

    /**
     * Construction de la zone infos du membre de famille
     * 
     * @param droitMembreFamille
     * @param donneesPersonelles
     * @return
     */
    private static String getMembreFamilleInfoTitre(DroitMembreFamille droitMembreFamille,
            SimpleDonneesPersonnelles donneesPersonelles) {

        StringBuilder html = new StringBuilder("");

        // ajout libellé membre de famille
        html.append("<span class=\"typeMembreFamille\">")
                .append(BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                        droitMembreFamille.getSimpleDroitMembreFamille().getCsRoleFamillePC()))
                .append(CalculMoisSuivantBuilder.HTML_SPAN_CLOSE);
        // ajout infos générales
        html.append("<span class=\"membreFamilleInfosGenerales\">")
                .append(" ")
                .append(PCDroitHandler.formatteDescriptionMembreFamille(BSessionUtil.getSessionFromThreadContext(),
                        droitMembreFamille)).append(CalculMoisSuivantBuilder.HTML_SPAN_CLOSE);

        return html.toString();
    }

    /**
     * Injection des positions des données fiancières depuis le fichier de config dans le modèle. Tri de la liste
     * 
     * @param list
     * @throws CalculException
     */
    @SuppressWarnings("unchecked")
    private static void injectAndSortDonneesFinancieresByPosistion(List<CalculMoisSuivant> list) throws CalculException {
        for (CalculMoisSuivant df : list) {
            // Récuperation du descripteur
            DonneeFinanciereDescriptor descripteurDF = CalculMoisSuivantBuilder.donneFinanciereDroitDescriptor.get(df
                    .getCsTypeDonneeFinanciere());

            // Si descripteur null, il manque dans le provider de descripteur
            if (descripteurDF == null) {
                throw new CalculException("The descriptor for the donnee financiere " + df.getCsTypeDonneeFinanciere()
                        + " [" + CalculMoisSuivantBuilder.getLibelle(df.getCsTypeDonneeFinanciere())
                        + "] is missing in the provider [DonneesFinancieresDescriptorProvider]");
            }
            df.setPosition(CalculMoisSuivantBuilder.donneFinanciereDroitDescriptor.get(df.getCsTypeDonneeFinanciere())
                    .getPosition());
        }

        Collections.sort(list, new CalculMoisSuivantComparator());
    }

    /**
     * Groupement des CalculMoisSuivant par idEntityGroup
     * 
     * @param listeDFbyMF
     * @return
     */
    private static Map<String, List<CalculMoisSuivant>> returnMapGroupeByEntityGroupDF(
            List<CalculMoisSuivant> listeDFbyMF) {
        // groupement par type df
        Map<String, List<CalculMoisSuivant>> dfsByMembreFamilleGroupByCsType = JadeListUtil.groupBy(listeDFbyMF,
                new Key<CalculMoisSuivant>() {
                    @Override
                    public String exec(CalculMoisSuivant e) {
                        return e.getIdEntityGroup();
                    }
                });
        return dfsByMembreFamilleGroupByCsType;
    }

}
