package ch.globaz.naos.businessimpl.service;

import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.application.TIApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.model.AffiliationAssuranceComplexModel;
import ch.globaz.naos.business.model.AffiliationAssuranceSearchComplexModel;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.AffiliationTiersSearchComplexModel;
import ch.globaz.naos.business.model.AssuranceCouverteComplexModel;
import ch.globaz.naos.business.model.AssuranceCouverteSearchComplexModel;
import ch.globaz.naos.business.model.LienAffiliationComplexModel;
import ch.globaz.naos.business.model.LienAffiliationSearchComplexModel;
import ch.globaz.naos.business.model.PlanAffiliationCotisationComplexModel;
import ch.globaz.naos.business.model.PlanAffiliationCotisationSearchComplexModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.naos.business.service.AffiliationService;
import ch.globaz.naos.exception.NaosException;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/*
 * Service pour le domaine Affiliation
 */
public class AffiliationServiceImpl implements AffiliationService {

    private String _getCantonAFTiers(String idTiers, String date) throws JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail adressTiers = new AdresseTiersDetail();
        try {

            ArrayList listOrder = new ArrayList();
            // exploitation
            // TODO: Ajouter constante AdresseService.CS_TYPE_EXPLOITATION dans service pyxis
            listOrder.add(new String("508021"));
            // domicile
            listOrder.add(AdresseService.CS_TYPE_DOMICILE);// 508008
            // courrier
            listOrder.add(new String(AdresseService.CS_TYPE_COURRIER));

            adressTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiersCustomCascade(idTiers, date,
                    ALCSTiers.DOMAINE_AF, listOrder, 2);

            // adressTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, new Boolean(true),
            // date, ch.globaz.pyxis.business.service.AdresseService.CS_DOMAINE_AF,
            // ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_COURRIER, "");
        } catch (Exception e) {
            throw new NaosException("ERROR OCCURED IN " + this.getClass().getName()
                    + "._getCantonAFTiers : Tiers Adress not found ! : " + e.getMessage());
        }

        return adressTiers.getFields().get("canton_id").toString();

    }

    /**
     * @deprecated remplacé par _getInfosAF
     */
    @Deprecated
    private void _getInfos(AssuranceInfo infoResult, AffiliationAssuranceComplexModel data, String date,
            boolean withCoti) throws JadePersistenceException, JadeApplicationException {

        if (!withCoti) {
            infoResult.setCanton(_getCantonAFTiers(data.getTiersAffiliation().getId(), date));
        }
        if (!data.getAssurance().isNew() && withCoti) {

            infoResult.setCategorieCotisant(data.getParametreAssurance().getValeurNum());
            // canton lié à la cotisation si AF, sinon canton exploitation
            if (AffiliationService.CS_TYPE_COTI_AF.equals(data.getAssurance().getTypeAssurance())) {
                infoResult.setCanton(data.getAssurance().getAssuranceCanton());
            } else {
                infoResult.setCanton(_getCantonAFTiers(data.getTiersAffiliation().getId(), date));
            }
        }

        infoResult.setCodeCaisseProf(data.getCaisseProf().getCodeAdministration());
        // infoResult.setCouvert => déjà défini selon règles
        if (!data.getCotisation().isNew() && withCoti) {
            infoResult.setDateDebutCotisation(data.getCotisation().getDateDebut());
            infoResult.setDateFinCotisation(data.getCotisation().getDateFin());
            infoResult.setIdCotisation(data.getCotisation().getId());
        }
        infoResult.setDateDebutAffiliation(data.getAffiliation().getDateDebut());
        infoResult.setDateFinAffiliation(data.getAffiliation().getDateFin());
        infoResult.setDesignation(data.getAffiliation().getRaisonSociale());
        infoResult.setDesignationAbrege(data.getAffiliation().getRaisonSocialeCourt());
        infoResult.setGenreAffiliation(data.getAffiliation().getTypeAffiliation());
        infoResult.setIdAffiliation(data.getAffiliation().getId());
        infoResult.setIdCotisation(data.getCotisation().getId());
        infoResult.setIdTiersAffiliation(data.getTiersAffiliation().getIdTiers());
        infoResult.setIdTiersCaisseProf(data.getCaisseProf().getIdTiersAdministration());
        infoResult.setLangue(data.getTiersAffiliation().getLangue());
        infoResult.setNumeroAffilie(data.getAffiliation().getAffilieNumero());
        infoResult.setPeriodicitieAffiliation(data.getAffiliation().getPeriodicite());
        infoResult.setPeriodicitieCotisation(data.getCotisation().getPeriodicite());
        infoResult.setTitre(data.getTiersAffiliation().getTitreTiers());

    }

    /*
     * Remplit la structure infoResult avec les valeurs dans data, sauf activité et canton la partie cotisation est
     * utilisé que si de type AF
     */
    private void _getInfosAF(AssuranceInfo infoResult, AffiliationAssuranceComplexModel data)
            throws JadePersistenceException, JadeApplicationException {

        infoResult.setDateDebutAffiliation(data.getAffiliation().getDateDebut());
        infoResult.setDateFinAffiliation(data.getAffiliation().getDateFin());
        infoResult.setPeriodicitieAffiliation(data.getAffiliation().getPeriodicite());

        infoResult.setCodeCaisseProf(data.getCaisseProf().getCodeAdministration());
        infoResult.setIdTiersCaisseProf(data.getCaisseProf().getIdTiersAdministration());

        // info a remplir que si coti AF ? TODO:vérifier si seulement cotiAF
        if (AffiliationService.CS_TYPE_COTI_AF.equals(data.getAssurance().getTypeAssurance())) {
            infoResult.setCategorieCotisant(data.getParametreAssurance().getValeurNum());
            infoResult.setIdCotisation(data.getCotisation().getId());
            infoResult.setDateDebutCotisation(data.getCotisation().getDateDebut());
            infoResult.setDateFinCotisation(data.getCotisation().getDateFin());
            infoResult.setPeriodicitieCotisation(data.getCotisation().getPeriodicite());
        }
        infoResult.setLangue(data.getTiersAffiliation().getLangue());
        infoResult.setNumeroAffilie(data.getAffiliation().getAffilieNumero());
        infoResult.setTitre(data.getTiersAffiliation().getTitreTiers());
        infoResult.setDesignation(data.getAffiliation().getRaisonSociale());
        infoResult.setDesignationAbrege(data.getAffiliation().getRaisonSocialeCourt());
        infoResult.setGenreAffiliation(data.getAffiliation().getTypeAffiliation());
        infoResult.setIdAffiliation(data.getAffiliation().getAffiliationId());
        infoResult.setIdTiersAffiliation(data.getAffiliation().getIdTiers());

        infoResult.setLibelleCourt(data.getAssurance().getAssuranceLibelleCourtFr());
        infoResult.setLibelleLong(data.getAssurance().getAssuranceLibelleFr());
        infoResult.setIdAssurance(data.getAssurance().getAssuranceId());
    }

    @Override
    public AffiliationSearchSimpleModel find(AffiliationSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException {
        return (AffiliationSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.naos.business.service.AffiliationService# findAllForNumeroAffilieLike(java.lang.String)
     */
    @Override
    public String[] findAllForNumeroAffilieLike(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException {
        try {
            String numeroAffilieLike = numeroAffilie;
            AffiliationSearchSimpleModel model = new AffiliationSearchSimpleModel();
            model.setForNumeroAffilieLike(numeroAffilieLike);
            JadePersistenceManager.search(model);
            int size = model.getSize();
            String[] tabNumAff = new String[size];
            for (int i = 0; i < size; i++) {
                tabNumAff[i] = ((AffiliationSimpleModel) model.getSearchResults()[i]).getAffilieNumero();
            }
            return tabNumAff;
        } catch (Exception e) {
            // TODO : i14n
            throw new NaosException("Impossible de trouver les numéros d'affiliés començant par " + numeroAffilie, e);
        }
    }

    @Override
    public String findIdTiersForNumeroAffilie(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException {

        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();

        searchModel.setForNumeroAffilie(numeroAffilie);
        searchModel = find(searchModel);
        AffiliationSimpleModel firstModel = (AffiliationSimpleModel) searchModel.getSearchResults()[0];

        switch (searchModel.getSize()) {
            case 0:
                return null;
            case 1:
                return firstModel.getIdTiers();
            default:
                return firstModel.getIdTiers();
        }
    }

    @Override
    public AffiliationSimpleModel findMaisonMere(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException {
        LienAffiliationSearchComplexModel model = new LienAffiliationSearchComplexModel();
        model.setForChildNumeroAffilie(numeroAffilieFormatte);
        model.setForDateLien(JACalendar.todayJJsMMsAAAA());
        model.setForTypeLien(AffiliationService.CS_TYPE_LIEN_SUCCURSALE);
        try {
            JadePersistenceManager.search(model);
            if (model.getSearchResults().length > 1) {
                // Préventif, ne devrait pas être possible
                // TODO : i14n
                throw new Exception("Plusieurs succursales trouvées pour l'affiliation " + numeroAffilieFormatte);
            } else if (model.getSearchResults().length == 1) {
                LienAffiliationComplexModel m = (LienAffiliationComplexModel) model.getSearchResults()[0];
                return m.getParent();

            } else {
                return null;
            }

        } catch (Exception e) {
            // TODO : i14n
            throw new NaosException("Un problème technique est survenu lors de la recherche de la maison mère", e);
        }
    }

    // public AssuranceInfo getAssuranceInfo(String numeroAffilie, String date,
    // String typeDossier) throws JadePersistenceException,
    // JadeApplicationException {
    // return this.getAssuranceInfo(numeroAffilie, date, typeDossier, false,
    // "0");
    // }

    @Override
    public int findNombreSuccursales(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException {
        LienAffiliationSearchComplexModel model = new LienAffiliationSearchComplexModel();
        model.setForParentNumeroAffilie(numeroAffilieFormatte);
        model.setForDateLien(JACalendar.todayJJsMMsAAAA());
        model.setForTypeLien(AffiliationService.CS_TYPE_LIEN_SUCCURSALE);
        try {
            return JadePersistenceManager.count(model);
        } catch (Exception e) {
            // TODO : i14n
            throw new NaosException("Un problème technique est survenu lors de la recherche du nombre de succursales",
                    e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.naos.business.service.AffiliationService#getAssuranceInfo(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public AssuranceInfo getAssuranceInfo(String numeroAffilie, String date, String genreAssurance,
            String typeAssurance, String categorieCotisant) throws JadePersistenceException, JadeApplicationException {

        AssuranceCouverteSearchComplexModel model = new AssuranceCouverteSearchComplexModel();
        model.setForNumeroAffilie(numeroAffilie);
        model.setForDateCotisation(date);
        model.setForGenreAssurance(genreAssurance);
        model.setForTypeAssurance(typeAssurance);
        model.setForValeurNumParamAssurance(categorieCotisant);
        JadePersistenceManager.search(model);

        int nbRecords = model.getSize();
        AssuranceInfo infoResult = new AssuranceInfo();
        infoResult.setNumeroAffilie(numeroAffilie);
        switch (nbRecords) {
            case 0:
                /*
                 * Pas de couverture trouvée pour cette assurance, elle n'est pas couverte
                 */
                infoResult.setCouvert(new Boolean(false));
                return infoResult;
            case 1:
                /*
                 * Assurance couverte (et 1 seul enregistrement trouvé) transfert les informations dans le resultat
                 */
                infoResult.setCouvert(new Boolean(true));
                AssuranceCouverteComplexModel data = (AssuranceCouverteComplexModel) model.getSearchResults()[0];
                infoResult.setLangue(data.getTiersAffiliation().getLangue());
                infoResult.setTitre(data.getTiersAffiliation().getTitreTiers());
                infoResult.setDesignation(data.getAffiliation().getRaisonSociale());
                infoResult.setDesignationAbrege(data.getAffiliation().getRaisonSocialeCourt());
                infoResult.setDateDebutCotisation(data.getCotisation().getDateDebut());
                infoResult.setDateFinCotisation(data.getCotisation().getDateFin());
                infoResult.setCanton(data.getAssurance().getAssuranceCanton());
                infoResult.setIdTiersCaisseProf(data.getCaisseProf().getIdTiersAdministration());
                infoResult.setCodeCaisseProf(data.getCaisseProf().getCodeAdministration());
                infoResult.setPeriodicitieCotisation(data.getCotisation().getPeriodicite());
                infoResult.setPeriodicitieAffiliation(data.getAffiliation().getPeriodicite());
                infoResult.setCategorieCotisant(data.getParametreAssurance().getValeurNum());
                infoResult.setIdAffiliation(data.getAffiliation().getAffiliationId());
                infoResult.setIdTiersAffiliation(data.getAffiliation().getIdTiers());
                return infoResult;

            default:
                /*
                 * Il ne doit pas y avoir plusieurs enregistrement pour les critères passé en paramètres.
                 */
                // TODO : i14n
                throw new NaosException(
                        "Plusieurs enregistrements trouvés, alors que 0 ou 1 enregistrement sont attendu.");
        } // fin switch
    }

    /**
     * 
     * Permet de savoir si une assurance est couverte ou non
     * 
     * @param numeroAffilie
     * @param date
     * @param isSuccursale
     * @param numeroAffilieSuccursale
     * @return un objet de type AssuranceInfo qui contient le résultat
     * @throws JadePersistenceException
     *             si une erreur technique lié à la persistence des données survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs enregistrements sont trouvés pour les paramètres donnés.
     */
    @Override
    public AssuranceInfo getAssuranceInfoAF(String numeroAffilie, String date, String typeDossier)
            throws JadePersistenceException, JadeApplicationException {
        // état initial du résultat retourné
        AssuranceInfo infoResult = new AssuranceInfo();
        infoResult.setNumeroAffilie(numeroAffilie);
        infoResult.setCouvert(false);

        AffiliationAssuranceSearchComplexModel searchModel = new AffiliationAssuranceSearchComplexModel();
        searchModel.setForNumeroAffilie(numeroAffilie);
        searchModel.setForDateCotisation(date);
        searchModel.setForDateAffiliation(date);
        JadePersistenceManager.search(searchModel);

        int nbRecordsCotiActive = searchModel.getSize();
        // si on trouve pas de cotisation active, on recherche que pour avoir
        // l'affiliation active même sans coti
        if (nbRecordsCotiActive == 0) {
            searchModel.setWhereKey("ifNoAssurance");
            JadePersistenceManager.search(searchModel);
            nbRecordsCotiActive = searchModel.getSize();
            // si on trouve pas d'affiliation active à ce date on recherche que pour avoir l'affiliation
            if (nbRecordsCotiActive == 0) {
                searchModel.setWhereKey("ifNoAffiliationActive");
                JadePersistenceManager.search(searchModel);
                nbRecordsCotiActive = searchModel.getSize();
                // si toujours rien ici, on retourne le résultat, car rien trouvé de plus
                if (nbRecordsCotiActive == 0) {
                    return infoResult;
                }
            }
        }

        AffiliationSimpleModel affiliationResult = ((AffiliationAssuranceComplexModel) searchModel.getSearchResults()[0])
                .getAffiliation();
        AffiliationAssuranceComplexModel data = (AffiliationAssuranceComplexModel) searchModel.getSearchResults()[0];
        AffiliationAssuranceComplexModel dataWithCotiAF = new AffiliationAssuranceComplexModel();
        AffiliationAssuranceComplexModel dataWithCoti = new AffiliationAssuranceComplexModel();

        int nbCotiAFFound = 0;
        // stocke dans une map les id coti AF pour être sûr d'itérer correctement
        Map<String, AffiliationAssuranceComplexModel> resultsChecked = new HashMap<String, AffiliationAssuranceComplexModel>();

        // on boucle sur les coti active pour savoir si bon type de coti selon typeDossier et avoir les infos
        // de celle déterminée comme active
        for (int ind = 0; ind < nbRecordsCotiActive; ind++) {
            dataWithCoti = (AffiliationAssuranceComplexModel) searchModel.getSearchResults()[ind];

            if ("S".equalsIgnoreCase(typeDossier)) {
                // TODO (lot x) : gérer le cas affiliation genre indépendant et
                // employeur
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PARITAIRE.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    // si c'est une maison mère on pourrait avoir plusieurs coti AF définie (1 par succursale)
                    // on vérifie si on a pas déjà itérer sur cette cotisation (car possible avec la jointure de
                    // AffiliationAssuranceComplexModel)
                    if (!resultsChecked.containsKey(dataWithCoti.getCotisation().getCotisationId())) {
                        nbCotiAFFound++;

                    }
                    dataWithCotiAF = dataWithCoti;
                    resultsChecked.put(dataWithCoti.getCotisation().getCotisationId(), dataWithCoti);

                    // si on tombe sur une exception, c'est celle ci qui fait foi et pas un autre
                    if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(dataWithCoti.getCotisation().getMotifFin())) {
                        nbCotiAFFound = 1;
                        break;
                    }
                }

            }
            if ("N".equalsIgnoreCase(typeDossier) || ("P".equalsIgnoreCase(typeDossier))) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AVS_AI
                                .equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;
                }
                if ("N".equalsIgnoreCase(typeDossier)
                        && AffiliationService.TYPE_AFFILI_BENEF_AF.equals(data.getAffiliation().getTypeAffiliation())) {
                    infoResult.setCouvert(true);
                    break;
                }

            }
            if ("A".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AVS_AI
                                .equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;

                }
            }
            if ("T".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())) {

                    infoResult.setCouvert(true);
                    break;

                } else {
                    if ((dataWithCoti.getAssurance() != null)
                            && AffiliationService.CS_TYPE_COTI_AUTRE.equals(dataWithCoti.getAssurance()
                                    .getTypeAssurance())
                            && AffiliationService.CS_TYPE_PARITAIRE.equals(dataWithCoti.getAssurance()
                                    .getAssuranceGenre())) {

                        infoResult.setCouvert(true);
                    }
                }
            }

            if ("C".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AVS_AI
                                .equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PARITAIRE.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;
                }
            }

            if ("I".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;

                } else {
                    if ((dataWithCoti.getAssurance() != null)
                            && AffiliationService.CS_TYPE_COTI_AVS_AI.equals(dataWithCoti.getAssurance()
                                    .getTypeAssurance())
                            && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance()
                                    .getAssuranceGenre())) {

                        infoResult.setCouvert(true);
                    }
                }
            }

            // si l'affiliation est de type TSE, on vérifie les coti AF sans tenir compte du type d'activité
            if ((AffiliationService.TYPE_AFFILI_TSE.equals(dataWithCoti.getAffiliation().getTypeAffiliation()) || AffiliationService.TYPE_AFFILI_TSE_VOLONTAIRE
                    .equals(dataWithCoti.getAffiliation().getTypeAffiliation()))) {

                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;

                }

            }

        }// fin FOR

        if (nbCotiAFFound == 1) {
            infoResult.setCanton(dataWithCotiAF.getAssurance().getAssuranceCanton());
            _getInfosAF(infoResult, dataWithCotiAF);
            // Affiliation vérification le type (maison mère, succursale, normal)
            AffiliationSimpleModel maisonMere = AFBusinessServiceLocator.getAffiliationService()
                    .findMaisonMere(dataWithCotiAF.getAffiliation().getAffilieNumero());
            // pas une succursale tester la masse salariale si facturé par accompte,
            // sinon ok
            if ((maisonMere == null) && !dataWithCotiAF.getAffiliation().getReleveParitaire()) {
                if (JadeStringUtil.isIntegerEmpty(dataWithCotiAF.getCotisation().getMasseAnnuelle())) {

                    List<String> warnings = infoResult.getWarningsContainer();
                    warnings.add("naos.cotisation.af.aucuneMasse");
                    infoResult.setWarningsContainer(warnings);
                }
            }

            // TODO: si coti facturé à la maison mère => tester maison mère et si inactive => warning aucune coti maison
            // mère
            // et tester si maison mère existe

        } else {
            // FIXME: infoResult.setCanton(this._getCantonAFTiers(affiliationResult.getIdTiers(), date));
            // et plus besoin de le faire dans chaque condition en dessous
            if (ALConstCaisse.CAISSE_CCVD.equals(ALServiceLocator.getParametersServices().getNomCaisse())
                    && "A".equals(typeDossier)) {
                infoResult.setCouvert(true);
            }
            if ("S".equals(typeDossier) && (nbCotiAFFound == 0)) {
                List<String> warnings = infoResult.getWarningsContainer();
                warnings.add("naos.cotisation.af.aucuneCotiAF");
                infoResult.setWarningsContainer(warnings);
            }
            if ("S".equals(typeDossier) && (nbCotiAFFound > 1)) {
                // FIXME:améliorer la gestion des cas affiliation ayant plusieurs coti AF active
                if (ALConstCaisse.CAISSE_CCVD.equals(ALServiceLocator.getParametersServices().getNomCaisse())) {
                    List<String> warnings = infoResult.getWarningsContainer();
                    warnings.add("naos.cotisation.af.plusieursCotiAF");
                    infoResult.setWarningsContainer(warnings);
                }
            }
            // si on a un coti AF, on se base sur celle là pour récupérer les infos affiliation
            if (!dataWithCotiAF.isNew()) {
                infoResult.setCanton(_getCantonAFTiers(affiliationResult.getIdTiers(), date));
                _getInfosAF(infoResult, dataWithCotiAF);
            } else {
                // si dataWithCoti est nouveau, c'est qu'on a pas trouvé de coti qu'on cherchait selon type de dossier
                // AF
                // donc on prend data pour avoir les infos affiliation au moins, car on les veut pour afficher les infos
                // et
                // trouver le canton selon tiers affiliation
                if (dataWithCoti.isNew()) {
                    infoResult.setCanton(_getCantonAFTiers(affiliationResult.getIdTiers(), date));
                    _getInfosAF(infoResult, data);
                } else {
                    infoResult.setCanton(_getCantonAFTiers(dataWithCoti.getTiersAffiliation().getId(), date));
                    _getInfosAF(infoResult, dataWithCoti);

                }
            }

        }

        // on met inactif si date passée pas dans période affiliation
        if (JadeDateUtil.isDateAfter(date, affiliationResult.getDateDebut())) {
            if (JadeStringUtil.isBlankOrZero(affiliationResult.getDateFin())
                    || JadeDateUtil.isDateBefore(date, affiliationResult.getDateFin())) {
                // NOTHING
            } else {
                infoResult.setCouvert(false);
            }
        }
        // si pas actif et que cotiAF active, on logge un avertissement et on met actif
        if (!infoResult.getCouvert() && hasCotisationAFSansAdhesion(affiliationResult.getAffiliationId(), date)) {
            List<String> warnings = infoResult.getWarningsContainer();
            warnings.add("naos.cotisation.af.aucuneAdhesion");
            warnings.remove("naos.cotisation.af.aucuneCotiAF");
            infoResult.setWarningsContainer(warnings);
            infoResult.setCouvert(true);
        }

        return infoResult;

    }

    /**
     * retourne si cotiAF active pour la date donnée mais sans adhésion
     * 
     * @param idAffiliation
     * @param date
     * @return
     */
    private boolean hasCotisationAFSansAdhesion(String idAffiliation, String date) {

        PlanAffiliationCotisationSearchComplexModel searchModel = new PlanAffiliationCotisationSearchComplexModel();
        searchModel.setForAffiliationId(idAffiliation);
        searchModel.setForTypeAssurance(AffiliationService.CS_TYPE_COTI_AF);
        searchModel.setForDateCotisation(date);
        searchModel.setDefinedSearchSize(0);
        try {
            searchModel = (PlanAffiliationCotisationSearchComplexModel) JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            return false;
        }

        for (int i = 0; i < searchModel.getSize(); i++) {
            PlanAffiliationCotisationComplexModel currentPlanCoti = (PlanAffiliationCotisationComplexModel) searchModel
                    .getSearchResults()[i];
            if (JadeNumericUtil.isEmptyOrZero(currentPlanCoti.getCotisation().getAdhesionId())) {
                return true;
            }

        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.naos.business.service.AffiliationService#isAffiliationExists (java.lang.String)
     */
    @Override
    public Boolean isAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException {

        Boolean result = null;
        AffiliationSearchSimpleModel model = new AffiliationSearchSimpleModel();
        model.setForNumeroAffilie(numeroAffilie);
        int size = JadePersistenceManager.count(model);

        if (size < 0) {
            // préventif, ne devrait jamais arriver !
            // TODO : i14n
            throw new NaosException("isAffiliationExists, nb result < 0 !");
        } else if (size > 1) {
            // préventif, ne devrait jamais arriver !
            // TODO : i14n
            throw new NaosException("(" + size + ") pour le numeroAffilie :" + numeroAffilie);
        } else if (size == 0) {
            result = new Boolean(false);
        } else if (size == 1) {
            result = new Boolean(true);
        }

        if (result == null) {
            // something got really wrong...
            // TODO : i14n
            throw new NaosException("impossible de déterminer le nombre d'affiliation pour le numéro d'affilié :"
                    + numeroAffilie);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.naos.business.service.AffiliationService#isNumeroAffilieValide (java.lang.String)
     */
    @Override
    public Boolean isNumeroAffilieValide(String numeroAffilie) throws JadeApplicationException {

        Boolean result = new Boolean(false);
        IFormatData formater = null;
        try {
            TIApplication app = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
            formater = app.getAffileFormater();
        } catch (Exception e) {
            throw new NaosException(e.getMessage());
        }

        if (formater == null) {
            // Préventif, ne doit jamais arriver
            // TODO : i14n
            throw new NaosException("ERROR OCCURED IN " + this.getClass().getName()
                    + ".isNumeroAffilieValide : formater is null");
        }
        try {
            formater.check(numeroAffilie);
            result = new Boolean(true);
        } catch (Exception e) {
            result = new Boolean(false);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.naos.business.service.AffiliationService#isAffiliationExists (java.lang.String)
     */
    @Override
    public int nombreAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException {

        int result = 0;
        AffiliationSearchSimpleModel model = new AffiliationSearchSimpleModel();
        model.setForNumeroAffilie(numeroAffilie);
        int size = JadePersistenceManager.count(model);

        if (size >= 0) {
            result = size;

        } else if (size < 0) {

            throw new NaosException("impossible de determiner le nombre d'affiliation pour le numéro d'affilié :"
                    + numeroAffilie);
        }

        return result;
    }

    @Override
    public AffiliationSimpleModel read(String idAffiliation) throws JadeApplicationException, JadePersistenceException {
        AffiliationSimpleModel affiliationModel = new AffiliationSimpleModel();
        affiliationModel.setId(idAffiliation);
        return (AffiliationSimpleModel) JadePersistenceManager.read(affiliationModel);

    }

    @Override
    public AffiliationAssuranceSearchComplexModel searchAffiliationAssurance(
            AffiliationAssuranceSearchComplexModel searchModel) throws JadePersistenceException {
        return (AffiliationAssuranceSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AffiliationTiersSearchComplexModel widgetFind(AffiliationTiersSearchComplexModel searchComplexModel)
            throws JadePersistenceException, JadeApplicationException {

        if (!JadeStringUtil.isBlankOrZero(searchComplexModel.getLikeDesignationUpper())) {
            searchComplexModel.setLikeDesignationUpper(searchComplexModel.getLikeDesignationUpper().toUpperCase());
        }
        return (AffiliationTiersSearchComplexModel) JadePersistenceManager.search(searchComplexModel);
    }

}
