package ch.globaz.orion.ws.allocationfamiliale;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.smtp.JadeSmtpClient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.jws.WebService;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;
import ch.globaz.orion.ws.exceptions.WebAvsException;
import ch.globaz.orion.ws.service.UtilsService;
import ch.globaz.orion.ws.service.adresse.Adresse;
import ch.globaz.orion.ws.service.adresse.AdresseLoader;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.queryexec.bridge.jade.SCM;

@WebService(endpointInterface = "ch.globaz.orion.ws.allocationfamiliale.WebAvsAllocationFamilialeService")
public class WebAvsAllocationFamilialeServiceImpl implements WebAvsAllocationFamilialeService {

    private static final int NB_ANNEE_FIN_VALIDITE = -5;

    @Override
    public ALDossierResultSearch searchDossiersAf(String forNumeroAffilie, String likeNssAllocataire,
            String likeNomAllocataire, String likePrenomAllocataire, List<ALDossierEtat> inEtatDossier, int from,
            int nb, ALDossierOrderBy orderBy, OrderByDirWebAvs orderByDir) throws WebAvsException {

        // vérification des paramètres du service
        if (JadeStringUtil.isEmpty(forNumeroAffilie)) {
            throw new IllegalArgumentException("forNumeroAffilie is null");
        }

        if (orderBy == null) {
            throw new IllegalArgumentException("orderBy is null");
        }

        if (orderByDir == null) {
            throw new IllegalArgumentException("orderByDir is null");
        }

        BSession session = UtilsService.initSession();
        try {

            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // construction de l'orderKey
            String orderKey = buildOrderKey(orderBy, orderByDir);

            // remplissage du search model avec les filtres et paramètres de recherche
            DossierListComplexSearchModel dossierListComplexSearchModel = createAndFillSearchDossiersAfSearchModel(
                    forNumeroAffilie, likeNssAllocataire, likeNomAllocataire, likePrenomAllocataire, inEtatDossier,
                    from, nb, orderKey);

            // exécution de la recherche
            dossierListComplexSearchModel = ALServiceLocator.getDossierListComplexModelService().search(
                    dossierListComplexSearchModel);

            // récupération des résultats
            int nbMatchingRows = dossierListComplexSearchModel.getNbOfResultMatchingQuery();
            List<JadeAbstractModel> listDossiers = Arrays.asList(dossierListComplexSearchModel.getSearchResults());

            // parcours du résultat et construction de l'objet de retour
            List<ALDossier> listDossiersAf = new ArrayList<ALDossier>();
            for (JadeAbstractModel jadeAbstractModel : listDossiers) {
                DossierListComplexModel dossierAf = (DossierListComplexModel) jadeAbstractModel;

                // récupération de la date de radiation si non vide
                Date dateRadiation = null;
                if (!JadeStringUtil.isEmpty(dossierAf.getFinValidite())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    dateRadiation = sdf.parse(dossierAf.getFinValidite());
                }

                // convertir l'état code system en enum
                ALDossierEtat etatDossierEnum = ALDossierEtat.getEnumFromCodeSystem(dossierAf.getEtatDossier());

                // création de l'objet dossier et ajout dans la liste
                ALDossier alDossier = new ALDossier(dossierAf.getNss(), dossierAf.getNomAllocataire(),
                        dossierAf.getPrenomAllocataire(), Integer.parseInt(dossierAf.getIdDossier()), etatDossierEnum,
                        dateRadiation);
                listDossiersAf.add(alDossier);
            }
            return new ALDossierResultSearch(listDossiersAf, nbMatchingRows, listDossiersAf.size());
        } catch (Exception e) {
            JadeLogger.error(this.getClass(), "Unable to searchDossiersAf for affilie :" + forNumeroAffilie + " -> "
                    + e.getMessage());
            throw new WebAvsException("Unable to searchDossiersAf for affilie : " + forNumeroAffilie);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }
    }

    @Override
    public ALDossierAndAdressesResultSearch searchDossiersAfAndAdresses(String forNumeroAffilie,
            String likeNssAllocataire, String likeNomAllocataire, String likePrenomAllocataire,
            List<ALDossierEtat> inEtatDossier, int from, int nb, ALDossierOrderBy orderBy, OrderByDirWebAvs orderByDir)
            throws WebAvsException {

        // vérification des paramètres du service
        if (JadeStringUtil.isEmpty(forNumeroAffilie)) {
            throw new IllegalArgumentException("forNumeroAffilie is null");
        }

        if (orderBy == null) {
            throw new IllegalArgumentException("orderBy is null");
        }

        if (orderByDir == null) {
            throw new IllegalArgumentException("orderByDir is null");
        }

        BSession session = UtilsService.initSession();
        try {

            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // construction de l'orderKey
            String orderKey = buildOrderKey(orderBy, orderByDir);

            // remplissage du search model avec les filtres et paramètres de recherche
            DossierListComplexSearchModel dossierListComplexSearchModel = createAndFillSearchDossiersAfSearchModel(
                    forNumeroAffilie, likeNssAllocataire, likeNomAllocataire, likePrenomAllocataire, inEtatDossier,
                    from, nb, orderKey);

            // exécution de la recherche
            dossierListComplexSearchModel = ALServiceLocator.getDossierListComplexModelService().search(
                    dossierListComplexSearchModel);

            // récupération des résultats
            int nbMatchingRows = dossierListComplexSearchModel.getNbOfResultMatchingQuery();
            List<JadeAbstractModel> listDossiers = Arrays.asList(dossierListComplexSearchModel.getSearchResults());

            // parcours du résultat
            List<ALDossierAndAdresses> listAlDossiersAndAdresses = new ArrayList<ALDossierAndAdresses>();
            List<String> nssList = new ArrayList<String>();

            if (!listDossiers.isEmpty()) {
                for (JadeAbstractModel jadeAbstractModel : listDossiers) {
                    DossierListComplexModel dossierAf = (DossierListComplexModel) jadeAbstractModel;
                    if (!dossierAf.getNss().trim().isEmpty()) {
                        nssList.add(dossierAf.getNss());
                    }
                }
                // recherche de tous les idTiers correspondant aux NSS (map key=nss, value=NssTiers)
                SQLWriter writer = SQLWriter.write()
                        .append("SELECT HTITIE as id_Tiers, HXNAVS as nss FROM SCHEMA.TIPAVSP where HXNAVS ")
                        .inForString(nssList);
                Map<String, List<NssTiers>> idTiersAllocatairesMap = SCM.newInstance(NssTiers.class)
                        .query(writer.toSql()).executeAndGroupBy("NSS", String.class);
                // map contenant les nss et les idTiers correspondant (key=nss, value=idTiers)
                Map<String, String> mapNssIdTiers = new HashMap<String, String>();
                List<String> idTiersAllocatairesList = new ArrayList<String>();
                for (Entry<String, List<NssTiers>> entry : idTiersAllocatairesMap.entrySet()) {
                    idTiersAllocatairesList.add(entry.getValue().get(0).getIdTiers());
                    mapNssIdTiers.put(entry.getKey(), entry.getValue().get(0).getIdTiers());
                }
                // chargement toutes les adresses de courrier
                AdresseLoader adresseloader = new AdresseLoader();
                Map<String, List<Adresse>> adressesCourriersMap = adresseloader.loadLastByIdsTiersAndGroupByIdTiers(
                        idTiersAllocatairesList, ALCSTiers.DOMAINE_AF, AdresseService.CS_TYPE_COURRIER);
                // chargement toutes les adresses de domicile
                Map<String, List<Adresse>> adressesDomicilesMap = adresseloader.loadLastByIdsTiersAndGroupByIdTiers(
                        idTiersAllocatairesList, null, AdresseService.CS_TYPE_DOMICILE);
                for (JadeAbstractModel jadeAbstractModel : listDossiers) {
                    DossierListComplexModel dossierAf = (DossierListComplexModel) jadeAbstractModel;

                    // récupération de la date de radiation si non vide
                    Date dateRadiation = null;
                    if (!JadeStringUtil.isEmpty(dossierAf.getFinValidite())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        dateRadiation = sdf.parse(dossierAf.getFinValidite());
                    }

                    // convertir l'état code system en enum
                    ALDossierEtat etatDossierEnum = ALDossierEtat.getEnumFromCodeSystem(dossierAf.getEtatDossier());

                    // récupération des adresses dans la map
                    ALAdresse alAdresseDomicile = null;
                    ALAdresse alAdresseCourrier = null;

                    if (mapNssIdTiers.containsKey(dossierAf.getNss())) {
                        List<Adresse> adresseCourriers = adressesCourriersMap
                                .get(mapNssIdTiers.get(dossierAf.getNss()));
                        List<Adresse> adresseDomiciles = adressesDomicilesMap
                                .get(mapNssIdTiers.get(dossierAf.getNss()));

                        Adresse adresseCourrier;
                        if (adresseCourriers != null && !adresseCourriers.isEmpty()) {
                            adresseCourrier = adresseCourriers.get(0);
                            if (adresseCourrier.getType().isCourrier()) {
                                alAdresseCourrier = new ALAdresse(adresseCourrier.getRue(),
                                        adresseCourrier.getRueNumero(), adresseCourrier.getCasePostale(),
                                        adresseCourrier.getNpa(), adresseCourrier.getLocalite(),
                                        adresseCourrier.getAttention(), adresseCourrier.getDesignation2(),
                                        adresseCourrier.getDesignation3(), null);
                            }
                        }
                        Adresse adresseDomicile;
                        if (adresseDomiciles != null && !adresseDomiciles.isEmpty()) {
                            adresseDomicile = adresseDomiciles.get(0);
                            if (adresseDomicile.getType().isDomicile()) {
                                alAdresseDomicile = new ALAdresse(adresseDomicile.getRue(),
                                        adresseDomicile.getRueNumero(), adresseDomicile.getCasePostale(),
                                        adresseDomicile.getNpa(), adresseDomicile.getLocalite(),
                                        adresseDomicile.getAttention(), adresseDomicile.getDesignation2(),
                                        adresseDomicile.getDesignation3(), null);
                            }
                        }

                    }

                    // création de l'objet dossier et ajout dans la liste
                    ALDossierAndAdresses alDossierAndAdresses = new ALDossierAndAdresses(dossierAf.getNss(),
                            dossierAf.getNomAllocataire(), dossierAf.getPrenomAllocataire(), Integer.parseInt(dossierAf
                                    .getIdDossier()), etatDossierEnum, dateRadiation, alAdresseDomicile,
                            alAdresseCourrier);

                    listAlDossiersAndAdresses.add(alDossierAndAdresses);
                }
            }
            return new ALDossierAndAdressesResultSearch(listAlDossiersAndAdresses, nbMatchingRows,
                    listAlDossiersAndAdresses.size());
        } catch (Exception e) {
            JadeLogger.error(this.getClass(), "Unable to searchDossiersAndAdresses for affilie :" + forNumeroAffilie
                    + " -> " + e.getMessage());
            throw new WebAvsException("Unable to searchDossiersAf for affilie : " + forNumeroAffilie);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }
    }

    @Override
    public ALDossierAndAdresses readDossiersAfAndAdresses(String numeroDossier) throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroDossier)) {
            throw new IllegalArgumentException("numeroDossier is null or empty");
        }

        BSession session = UtilsService.initSession();

        try {
            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // chargement du dossier AF
            DossierComplexModel dossierAf = ALServiceLocator.getDossierComplexModelService().read(numeroDossier);
            String idTiersAllocataire = dossierAf.getAllocataireComplexModel().getAllocataireModel()
                    .getIdTiersAllocataire();

            // chargement de toutes les adresses
            List<String> idTiersAllocatairesList = new ArrayList<String>();
            idTiersAllocatairesList.add(idTiersAllocataire);
            AdresseLoader adresseloader = new AdresseLoader();

            // de courrier
            ALAdresse alAdresseCourrier = null;
            Map<String, List<Adresse>> adressesCourriersMap = adresseloader.loadLastByIdsTiersAndGroupByIdTiers(
                    idTiersAllocatairesList, ALCSTiers.DOMAINE_AF, AdresseService.CS_TYPE_COURRIER);
            List<Adresse> adressesCourrier = adressesCourriersMap.get(idTiersAllocataire);
            Adresse adresseCourrier;
            if (adressesCourrier != null && !adressesCourrier.isEmpty()) {
                adresseCourrier = adressesCourriersMap.get(idTiersAllocataire).get(0);
                if (adresseCourrier.getType().isCourrier()) {
                    alAdresseCourrier = new ALAdresse(adresseCourrier.getRue(), adresseCourrier.getRueNumero(),
                            adresseCourrier.getCasePostale(), adresseCourrier.getNpa(), adresseCourrier.getLocalite(),
                            adresseCourrier.getAttention(), adresseCourrier.getDesignation2(),
                            adresseCourrier.getDesignation3(), null);
                }
            }

            // de domicile
            ALAdresse alAdresseDomicile = null;
            Map<String, List<Adresse>> adressesDomicilesMap = adresseloader.loadLastByIdsTiersAndGroupByIdTiers(
                    idTiersAllocatairesList, null, AdresseService.CS_TYPE_DOMICILE);
            List<Adresse> adressesDomicile = adressesDomicilesMap.get(idTiersAllocataire);
            Adresse adresseDomicile;
            if (adressesDomicile != null && !adressesDomicile.isEmpty()) {
                adresseDomicile = adressesDomicilesMap.get(idTiersAllocataire).get(0);
                if (adresseDomicile.getType().isDomicile()) {
                    alAdresseDomicile = new ALAdresse(adresseDomicile.getRue(), adresseDomicile.getRueNumero(),
                            adresseDomicile.getCasePostale(), adresseDomicile.getNpa(), adresseDomicile.getLocalite(),
                            adresseDomicile.getAttention(), adresseDomicile.getDesignation2(),
                            adresseDomicile.getDesignation3(), null);
                }
            }

            // création de l'objet de retour
            String nss = dossierAf.getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue()
                    .getNumAvsActuel();
            String nomAllocataire = dossierAf.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1();
            String prenomAllocataire = dossierAf.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                    .getTiers().getDesignation2();
            String idDossier = dossierAf.getDossierModel().getIdDossier();
            ALDossierEtat etatDossierEnum = ALDossierEtat.getEnumFromCodeSystem(dossierAf.getDossierModel()
                    .getEtatDossier());
            Date dateRadiation = null;
            if (!JadeStringUtil.isEmpty(dossierAf.getDossierModel().getFinValidite())) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                dateRadiation = sdf.parse(dossierAf.getDossierModel().getFinValidite());
            }

            ALDossierAndAdresses alDossierAndAdresses = new ALDossierAndAdresses(nss, nomAllocataire,
                    prenomAllocataire, Integer.parseInt(idDossier), etatDossierEnum, dateRadiation, alAdresseDomicile,
                    alAdresseCourrier);
            return alDossierAndAdresses;
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to readDossierAf for dossier :" + numeroDossier + " -> " + e.getMessage());
            throw new WebAvsException("Unable to readDossierAf for dossier : " + numeroDossier);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }
    }

    @Override
    public Boolean updateOrCreateAdressesAllocataire(String numeroDossier, ALAdresse adresseDomicile,
            ALAdresse adresseCourrier, String remarqueDomicile, String remarqueCourrier) throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroDossier)) {
            throw new IllegalArgumentException("numeroDossier is null or empty");
        }

        BSession session = UtilsService.initSession();
        try {
            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // chargement du dossier AF
            DossierComplexModel dossierAf = ALServiceLocator.getDossierComplexModelService().read(numeroDossier);
            String idTiersAllocataire = dossierAf.getAllocataireComplexModel().getAllocataireModel()
                    .getIdTiersAllocataire();

            String nomAllocataire = dossierAf.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1();
            String prenomAllocataire = dossierAf.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                    .getTiers().getDesignation2();

            // chargement de toutes les adresses
            List<String> idTiersAllocatairesList = new ArrayList<String>();
            idTiersAllocatairesList.add(idTiersAllocataire);
            AdresseLoader adresseloader = new AdresseLoader();

            // de courrier
            Map<String, List<Adresse>> adressesCourrierActuellesMap = adresseloader
                    .loadLastByIdsTiersAndGroupByIdTiers(idTiersAllocatairesList, ALCSTiers.DOMAINE_AF,
                            AdresseService.CS_TYPE_COURRIER);
            List<Adresse> adressesCourrierActuelles = adressesCourrierActuellesMap.get(idTiersAllocataire);
            Adresse adresseCourrierActuelle = null;
            if (adressesCourrierActuelles != null && !adressesCourrierActuelles.isEmpty()) {
                adresseCourrierActuelle = adressesCourrierActuelles.get(0);
                if (!adresseCourrierActuelle.getType().isCourrier()) {
                    adresseCourrierActuelle = null;
                }
            }

            // de domicile
            Map<String, List<Adresse>> adressesDomicileActuellesMap = adresseloader
                    .loadLastByIdsTiersAndGroupByIdTiers(idTiersAllocatairesList, null, AdresseService.CS_TYPE_DOMICILE);
            List<Adresse> adressesDomicileActuelles = adressesDomicileActuellesMap.get(idTiersAllocataire);
            Adresse adresseDomicileActuelle = null;
            if (adressesDomicileActuelles != null && !adressesDomicileActuelles.isEmpty()) {
                adresseDomicileActuelle = adressesDomicileActuelles.get(0);
                if (!adresseDomicileActuelle.getType().isDomicile()) {
                    adresseDomicileActuelle = null;
                }
            }

            String nssAllocataire = dossierAf.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                    .getPersonneEtendue().getNumAvsActuel();

            // envoi de l'email
            sendMailMutationAdresses(adresseDomicile, adresseCourrier, nssAllocataire, nomAllocataire,
                    prenomAllocataire, adresseCourrierActuelle, adresseDomicileActuelle, remarqueDomicile,
                    remarqueCourrier);
            return true;
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to readDossierAf for dossier :" + numeroDossier + " -> " + e.getMessage());
            throw new WebAvsException("Unable to readDossierAf for dossier : " + numeroDossier);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }

    }

    private void sendMailMutationAdresses(ALAdresse adresseDomicile, ALAdresse adresseCourrier, String nssAllocataire,
            String nomAllocataire, String prenomAllocataire, Adresse adresseCourrierActuelle,
            Adresse adresseDomicileActuelle, String remarqueDomicile, String remarqueCourrier)
            throws PropertiesException {
        String to = EBProperties.EMAIL_MUTATION_ADRESSE_CAF.getValue();
        if (JadeStringUtil.isEmpty(to)) {
            throw new PropertiesException("email adresse is empty");
        }

        String subject = "EBusiness : Mutations d'adresses pour " + nomAllocataire + " " + prenomAllocataire + " - "
                + nssAllocataire;

        StringBuilder body = new StringBuilder();
        body.append("<table width='800' border='0' cellspacing='0' style='padding:3px;'>");

        // en-tête
        body.append("<tr bgcolor='#4A4C4E'");
        body.append("<td width='50%'>");
        body.append("<b><font color='#FFFFFF'>ADRESSES ACTUELLES</b></font>");
        body.append("</td>");
        body.append("<td>");
        body.append("<b><font color='#FFFFFF'>ADRESSES TRANSMISES</b></font>");
        body.append("</td>");
        body.append("</tr>");

        // en-tête domicile
        body.append("<tr bgcolor='#CEE3F6'>");
        body.append("<td>");
        body.append("<b>Domicile</b>");
        body.append("</td>");
        body.append("<td>");
        body.append("<b>Domicile</b>");
        body.append("</td>");
        body.append("</tr>");

        // adresse domicile
        body.append("<tr valign='top' bgcolor='#CEE3F6'>");
        // actuelles
        body.append("<td>");
        if (adresseDomicileActuelle != null) {
            body.append(adresseDomicileActuelle.getAdresseFormatte());
        }
        body.append("</td>");
        // transmises
        body.append("<td>");
        if (adresseDomicile != null) {
            body.append(adresseDomicile.getAdresseFormatee(nomAllocataire, prenomAllocataire));
        }
        body.append("<br>");
        body.append("<br>");
        body.append("<b><i>Remarque : </b></i>");
        if (!JadeStringUtil.isEmpty(remarqueDomicile)) {
            body.append("<br><i>" + remarqueDomicile + "</i>");
        } else {
            body.append("<i> - </i>");
        }
        body.append("</td>");
        body.append("</tr>");

        // ligne vide
        body.append("<tr bgcolor='#CEE3F6'>");
        body.append("<td>");
        body.append("</td>");
        body.append("<td>");
        body.append("</td>");
        body.append("</tr>");

        // ligne vide
        body.append("<tr bgcolor='#E0F0FF'>");
        body.append("<td>");
        body.append("</td>");
        body.append("<td>");
        body.append("</td>");
        body.append("</tr>");

        // en-tête courrier
        body.append("<tr bgcolor='#E0F0FF'>");
        body.append("<td>");
        body.append("<b>Courrier</b>");
        body.append("</td>");
        body.append("<td>");
        body.append("<b>Courrier</b>");
        body.append("</td>");
        body.append("</tr>");

        // Adresse courrier
        body.append("<tr valign='top' bgcolor='#E0F0FF'>");
        // actuelles
        body.append("<td>");
        if (adresseCourrierActuelle != null) {
            body.append(adresseCourrierActuelle.getAdresseFormatte());
        }
        body.append("</td>");
        // transmises
        body.append("<td>");
        if (adresseCourrier != null) {
            body.append(adresseCourrier.getAdresseFormatee(nomAllocataire, prenomAllocataire));
        }
        body.append("<br>");
        body.append("<br>");
        body.append("<b><i>Remarque : </b></i>");
        if (!JadeStringUtil.isEmpty(remarqueCourrier)) {
            body.append("<br><i>" + remarqueCourrier + "</i>");
        } else {
            body.append("<i> - </i>");
        }
        body.append("</td>");
        body.append("</tr>");
        body.append("</table>");

        sendMail(to, subject, body.toString());
    }

    private static void sendMail(String email, String subject, String body) {
        try {
            JadeSmtpClient.getInstance().sendMail(email, subject, body, null);
        } catch (Exception e) {
            JadeLogger.error("Unabled to send mail to " + email, e);
        }
    }

    /**
     * retourne le model de recherche rempli avec les paramètres passés
     * 
     * @param forNumeroAffilie
     * @param likeNssAllocataire
     * @param likeNomAllocataire
     * @param likePrenomAllocataire
     * @param inEtatDossier
     * @param from
     * @param nb
     * @param orderKey
     * @return
     */
    private DossierListComplexSearchModel createAndFillSearchDossiersAfSearchModel(String forNumeroAffilie,
            String likeNssAllocataire, String likeNomAllocataire, String likePrenomAllocataire,
            List<ALDossierEtat> inEtatDossier, int from, int nb, String orderKey) {
        DossierListComplexSearchModel dossierListComplexSearchModel = new DossierListComplexSearchModel();
        dossierListComplexSearchModel.setForNumAffilie(forNumeroAffilie);
        if (!JadeStringUtil.isEmpty(likeNssAllocataire)) {
            dossierListComplexSearchModel.setLikeNssAllocataire(likeNssAllocataire);
        }
        if (!JadeStringUtil.isEmpty(likeNomAllocataire)) {
            dossierListComplexSearchModel.setLikeNomAllocataire(likeNomAllocataire);
        }
        if (!JadeStringUtil.isEmpty(likePrenomAllocataire)) {
            dossierListComplexSearchModel.setLikePrenomAllocataire(likePrenomAllocataire);
        }
        if (inEtatDossier != null && !inEtatDossier.isEmpty() && inEtatDossier.get(0) != null) {
            List<String> listEtatCs = new ArrayList<String>();
            for (ALDossierEtat etat : inEtatDossier) {
                listEtatCs.add(etat.getCodeSystem());
            }
            dossierListComplexSearchModel.setInEtatsDossier(listEtatCs);
        }

        ch.globaz.common.domaine.Date date = new ch.globaz.common.domaine.Date();
        ch.globaz.common.domaine.Date forFinValiditeGreater = date.addYear(NB_ANNEE_FIN_VALIDITE);
        dossierListComplexSearchModel.setForFinValiditeGreater(forFinValiditeGreater.getSwissValue());
        dossierListComplexSearchModel.setWhereKey("EBUSINESS");
        dossierListComplexSearchModel.setOrderKey(orderKey);
        dossierListComplexSearchModel.setOffset(from);
        dossierListComplexSearchModel.setDefinedSearchSize(nb);
        return dossierListComplexSearchModel;
    }

    /**
     * Retourne l'orderkey en fonction de l'orderBy et de la direction du tri
     * 
     * @param orderBy
     * @param orderByDir
     * @return
     */
    private String buildOrderKey(ALDossierOrderBy orderBy, OrderByDirWebAvs orderByDir) {
        StringBuilder orderKey = new StringBuilder();
        switch (orderBy) {
            case ORDER_BY_NO_DOSSIER:
                orderKey.append(ALDossierOrderBy.ORDER_BY_NO_DOSSIER.getText());
                break;
            case ORDER_BY_NSS:
                orderKey.append(ALDossierOrderBy.ORDER_BY_NSS.getText());
                break;
            case ORDER_BY_ETAT:
                orderKey.append(ALDossierOrderBy.ORDER_BY_ETAT.getText());
                break;
            case ORDER_BY_NOM_PRENOM_ALLOCATAIRE:
                orderKey.append(ALDossierOrderBy.ORDER_BY_NOM_PRENOM_ALLOCATAIRE.getText());
                break;
            case ORDER_BY_FIN_VALIDITE:
                orderKey.append(ALDossierOrderBy.ORDER_BY_FIN_VALIDITE.getText());
                break;
            default:
                throw new IllegalArgumentException("order by not allowed" + orderBy);
        }

        switch (orderByDir) {
            case ASC:
                orderKey.append(OrderByDirWebAvs.ASC.getText());
                break;
            case DESC:
                orderKey.append(OrderByDirWebAvs.DESC.getText());
                break;
            default:
                throw new IllegalArgumentException("order by direction not allowed" + orderByDir);
        }
        return orderKey.toString();
    }

}
