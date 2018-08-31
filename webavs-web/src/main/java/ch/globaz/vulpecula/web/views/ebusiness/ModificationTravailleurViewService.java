package ch.globaz.vulpecula.web.views.ebusiness;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.lang.Validate;
import ch.globaz.common.sql.QueryUpdateExecutor;
import ch.globaz.pegasus.businessimpl.services.process.allocationsNoel.AdressePaiementPrimeNoelService;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService.motifsModification;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PermisTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.web.gson.AdresseInfoGSON;
import ch.globaz.vulpecula.web.gson.BanqueInfoGSON;
import ch.globaz.vulpecula.web.gson.PosteTravailInfoGSON;
import ch.globaz.vulpecula.web.gson.RetourCreationPortail;
import ch.globaz.vulpecula.web.gson.TiersInfoGSON;
import ch.globaz.vulpecula.web.gson.TravailleurInfoGSON;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;
import ch.globaz.vulpecula.ws.bean.StatusEbu;
import ch.globaz.vulpecula.ws.utils.UtilsService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ModificationTravailleurViewService extends TraitementTravailleurDefault {

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static StatusAnnonceTravailleurEbu getStatusAnnonce(TravailleurEbuDomain travailleur) {
        boolean tiersRefused = StatusEbu.REFUSE.equals(travailleur.getTiersStatus())
                || StatusEbu.NO_DIFF.equals(travailleur.getTiersStatus());
        boolean posteRefused = StatusEbu.REFUSE.equals(travailleur.getPosteStatus())
                || StatusEbu.NO_DIFF.equals(travailleur.getPosteStatus());
        boolean travailleurRefused = StatusEbu.REFUSE.equals(travailleur.getTravailleurStatus())
                || StatusEbu.NO_DIFF.equals(travailleur.getTravailleurStatus());
        boolean banqueRefused = StatusEbu.REFUSE.equals(travailleur.getBanqueStatus())
                || StatusEbu.NO_DIFF.equals(travailleur.getBanqueStatus());
        boolean refused = tiersRefused && posteRefused && travailleurRefused && banqueRefused;

        boolean hasNoDiff = StatusEbu.NO_DIFF.equals(travailleur.getBanqueStatus())
                && StatusEbu.NO_DIFF.equals(travailleur.getPosteStatus())
                && StatusEbu.NO_DIFF.equals(travailleur.getPosteStatus())
                && StatusEbu.NO_DIFF.equals(travailleur.getTiersStatus());

        if (hasNoDiff) {
            return StatusAnnonceTravailleurEbu.AUTO;
        } else if (refused) {
            return StatusAnnonceTravailleurEbu.REFUSE;
        } else {
            return StatusAnnonceTravailleurEbu.TRAITE;
        }
    }

    public static void majStatusGlobal(String idTravailleur) {
        TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                idTravailleur);
        if (NouveauTravailleurViewService.isTraite(travailleur)) {
            // travailleur.setTraite(true);
            travailleur.setStatus(getStatusAnnonce(travailleur));
        } else {
            // travailleur.setTraite(false);
            travailleur.setStatus(StatusAnnonceTravailleurEbu.EN_COURS);
        }
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleur);
    }

    public String updateTiers(String tiersJSON) {
        Gson gson = new Gson();
        RetourCreationPortail retour = new RetourCreationPortail();

        TiersInfoGSON tiersGSON = gson.fromJson(tiersJSON, new TypeToken<TiersInfoGSON>() {
        }.getType());

        Validate.notNull(tiersGSON);

        try {
            PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
            searchTiers.setForIdTiers(tiersGSON.getIdTiersExistant());

            PersonneEtendueSearchComplexModel personneEtendueSearch = TIBusinessServiceLocator
                    .getPersonneEtendueService().find(searchTiers);

            if (personneEtendueSearch.getNbOfResultMatchingQuery() == 1) {

                PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) personneEtendueSearch
                        .getSearchResults()[0];
                TravailleurEbuDomain annonce = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                        tiersGSON.getIdPortail());
                personneEtendueComplexModel.getTiers().setLangue(getLangueEmployeur(annonce));
                String today = new Date().getSwissValue();
                // Si nss modifié, on doit lui donner la raison
                if (!isNullOrEmpty(tiersGSON.getNss())
                        && isDifferent(personneEtendueComplexModel.getPersonneEtendue().getNumContribuableActuel(),
                                tiersGSON.getNss())) {
                    personneEtendueComplexModel.setDateModifAvs(today);
                    personneEtendueComplexModel.setMotifModifAvs(motifsModification.MOTIF_MODIFICATION);
                    if (!"756.".equals(tiersGSON.getNss())) {
                        personneEtendueComplexModel.getPersonneEtendue().setNumAvsActuel(tiersGSON.getNss());
                    } else {
                        personneEtendueComplexModel.getPersonneEtendue().setNumAvsActuel("");
                    }

                }
                if (!isNullOrEmpty(tiersGSON.getNom())
                        && isDifferent(personneEtendueComplexModel.getTiers().getDesignation1(), tiersGSON.getNom())) {
                    personneEtendueComplexModel.setDateModifDesignation1(today);
                    personneEtendueComplexModel.setMotifModifDesignation1(motifsModification.MOTIF_MODIFICATION);
                    personneEtendueComplexModel.getTiers().setDesignation1(tiersGSON.getNom());
                }

                if (!isNullOrEmpty(tiersGSON.getPrenom())
                        && isDifferent(personneEtendueComplexModel.getTiers().getDesignation2(), tiersGSON.getPrenom())) {
                    personneEtendueComplexModel.setDateModifDesignation2(today);
                    personneEtendueComplexModel.setMotifModifDesignation2(motifsModification.MOTIF_MODIFICATION);
                    personneEtendueComplexModel.getTiers().setDesignation2(tiersGSON.getPrenom());
                }

                // Utilisation de la nationalité pour récupérer le pays de la personne
                TIPays pays = new TIPays();
                Hashtable<String, String> params = new Hashtable<String, String>();
                params.put("setForCodeIso", tiersGSON.getNationalite());
                BManager manager = pays.find(params);
                pays = (TIPays) manager.getFirstEntity();

                if (!isNullOrEmpty(tiersGSON.getNationalite())
                        && isDifferent(personneEtendueComplexModel.getTiers().getIdPays(), pays.getIdPays())) {
                    personneEtendueComplexModel.setDateModifPays(today);
                    personneEtendueComplexModel.setMotifModifPays(motifsModification.MOTIF_MODIFICATION);
                    personneEtendueComplexModel.getTiers().setIdPays(pays.getIdPays());
                }

                personneEtendueComplexModel.getPersonne().setDateNaissance(tiersGSON.getDateNaissance());

                if (!isNullOrEmpty(tiersGSON.getSexe())
                        && isDifferent(personneEtendueComplexModel.getPersonne().getSexe(), tiersGSON.getSexe())) {

                    // Si le sexe est homme et que le titre du tiers est femme, on change
                    if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(tiersGSON.getSexe())
                            && IConstantes.CS_TIERS_TITRE_MADAME.equals(personneEtendueComplexModel.getTiers()
                                    .getTitreTiers())) {
                        personneEtendueComplexModel.getTiers().setTitreTiers(IConstantes.CS_TIERS_TITRE_MONSIEUR);
                    }
                    // Si le sexe est homme et que le titre du tiers est femme, on change
                    if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(tiersGSON.getSexe())
                            && IConstantes.CS_TIERS_TITRE_MONSIEUR.equals(personneEtendueComplexModel.getTiers()
                                    .getTitreTiers())) {
                        personneEtendueComplexModel.getTiers().setTitreTiers(IConstantes.CS_TIERS_TITRE_MADAME);
                    }

                    personneEtendueComplexModel.getPersonne().setSexe(tiersGSON.getSexe());
                    personneEtendueComplexModel.setDateModifTitre(today);
                    personneEtendueComplexModel.setMotifModifTitre(motifsModification.MOTIF_MODIFICATION);
                }
                personneEtendueComplexModel.getPersonne().setEtatCivil(tiersGSON.getEtatCivil());

                personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().update(
                        personneEtendueComplexModel);

                updateAdresse(personneEtendueComplexModel, tiersGSON.getAdresseInfo());

                updateMoyenContact(personneEtendueComplexModel.getTiers().getId(), tiersGSON);

                setStatus(tiersGSON.getIdPortail(), StatusEbu.MODIFIE, "tiers");
                majStatusGlobal(tiersGSON.getIdPortail());
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            addError("JadeApplicationServiceNotAvailableException : " + e.getMessage(), e);
        } catch (JadePersistenceException e) {
            addError("JadePersistenceException : " + e.getMessage(), e);
        } catch (JadeApplicationException e) {
            addError("JadeApplicationException : " + e.getMessage(), e);
        } catch (Exception e) {
            addError("Erreur : " + e.getMessage(), e);
        }

        return gson.toJson(retour);
    }

    private static String getLangueEmployeur(TravailleurEbuDomain annonce) {
        Employeur emp = VulpeculaRepositoryLocator.getEmployeurRepository().findById(annonce.getIdEmployeur());
        return emp.getLangue();
    }

    private void updateMoyenContact(String idTiers, TiersInfoGSON tiersGSON) throws JadePersistenceException {

        Contact contact = VulpeculaRepositoryLocator.getContactRepository().findForIdTiersWithMoyens(idTiers);

        HashMap<TypeContact, MoyenContact> mapMoyen = contact.getMoyenContact();

        if (contact.getMoyenContact().size() != 0 && contact.getMoyenContact().get(TypeContact.PRIVE) != null
                && !JadeStringUtil.isEmpty(contact.getMoyenContact().get(TypeContact.PRIVE).getValeur())) {

            // MoyenContactTiersSimpleModel model = new MoyenContactTiersSimpleModel();
            // model.setId(mapMoyen.get(TypeContact.PRIVE).getId());
            // model.setSpy(mapMoyen.get(TypeContact.PRIVE).getSpy());
            // model.setValeur(tiersGSON.getTelephone());
            // model.setApplication("519004");
            // model.setTypeContact(TypeContact.PRIVE.getValue());
            // model.setIdContact(contact.getId());
            // JadePersistenceManager.update(model);

            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);
            // SCM.newInstance(MoyenContact.class).query("").execute();
            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

                String HLICON = mapMoyen.get(TypeContact.PRIVE).getId();
                String HLTTCO = TypeContact.PRIVE.getValue();
                String HLAPPL = "519004";
                String value = tiersGSON.getTelephone();
                QueryUpdateExecutor.executeUpdate(getQueryUpdateMoyenContact(HLICON, HLTTCO, HLAPPL, value),
                        MoyenContact.class, session);

            } catch (SQLException e) {
                session.addError(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

        } else {
            NouveauTravailleurViewService.setMoyenContact(idTiers, tiersGSON);
        }
    }

    private String getQueryUpdateMoyenContact(String HLICON, String HLTTCO, String HLAPPL, String value) {
        return "UPDATE SCHEMA.TIMCOMP SET HLICON=" + HLICON + ", HLCONT='" + value + "', HLTTCO=" + HLTTCO
                + ", HLAPPL=" + HLAPPL + " WHERE HLICON=" + HLICON + " AND HLTTCO=" + HLTTCO + " AND HLAPPL=" + HLAPPL;
    }

    private void setStatus(String idTravailleur, StatusEbu status, String type) {
        TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                idTravailleur);

        if (type.equals("tiers")) {
            travailleur.setTiersStatus(status);
        }
        if (type.equals("poste")) {
            travailleur.setPosteStatus(status);
        }
        if (type.equals("banque")) {
            travailleur.setBanqueStatus(status);
        }
        if (type.equals("travailleur")) {
            travailleur.setTravailleurStatus(status);
        }

        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleur);

    }

    public boolean isDifferent(String original, String other) {
        return !original.equalsIgnoreCase(other);
    }

    private int countLocaliteMatchNpa(String npa) throws Exception {
        TILocaliteManager mgr = new TILocaliteManager();
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.setInclureInactif(Boolean.TRUE);

        if (npa.length() == 4) {
            mgr.setForNumPostal(npa + "00");
        } else {
            mgr.setForNumPostal(npa);
        }
        mgr.find();
        return mgr.size();
    }

    public AdresseComplexModel updateAdresse(PersonneEtendueComplexModel personneEtendueComplexModel,
            AdresseInfoGSON adresseInfoGSON) throws Exception {

        int countLocalite = countLocaliteMatchNpa(adresseInfoGSON.getNpa());
        AdresseTiersDetail adresse = VulpeculaServiceLocator.getAdresseService().getAdresseTiers(
                personneEtendueComplexModel.getTiers().getId(), false, new Date().getSwissValue(), CS_DOMAINE_DEFAUT,
                CS_TYPE_COURRIER, "");
        boolean isCourrier = true;
        if (adresse.getFields() == null) {
            isCourrier = false;
            adresse = VulpeculaServiceLocator.getAdresseService().getAdresseTiers(
                    personneEtendueComplexModel.getTiers().getId(), false, new Date().getSwissValue(),
                    CS_DOMAINE_DEFAUT, CS_TYPE_DOMICILE, "");
        }
        if (adresse.getFields() == null) {
            return VulpeculaServiceLocator.getNouveauTravailleurService().insertAdresse(personneEtendueComplexModel,
                    adresseInfoGSON);

        }
        String dateDebutAdresse = adresse.getFields().get("dateDebutRelation");
        boolean updatedToday = false;
        if (!JadeStringUtil.isBlankOrZero(dateDebutAdresse)) {
            updatedToday = new Date(dateDebutAdresse).getSwissValue().equals(new Date().getSwissValue());
        }

        if (countLocalite == 1) {
            AdresseComplexModel adresseComplexModel = new AdresseComplexModel();
            adresseComplexModel.setTiers(personneEtendueComplexModel);
            adresseComplexModel.getAvoirAdresse().setDateDebutRelation(Date.now().getSwissValue());
            adresseComplexModel.getTiers().setId(personneEtendueComplexModel.getTiers().getId());
            adresseComplexModel.getLocalite().setNumPostal(adresseInfoGSON.getNpa());
            adresseComplexModel.getAdresse().setRue(adresseInfoGSON.getRue());
            adresseComplexModel.getAdresse().setNumeroRue(adresseInfoGSON.getRueNumero());

            if (!updatedToday && isCourrier) {
                adresseComplexModel.getAdresse().setCasePostale(adresseInfoGSON.getCasePostale());
                return VulpeculaServiceLocator.getAdresseService().addAdresse(adresseComplexModel, CS_DOMAINE_DEFAUT,
                        CS_TYPE_COURRIER, true);
            } else if (!updatedToday && !isCourrier) {
                return VulpeculaServiceLocator.getAdresseService().addAdresse(adresseComplexModel, CS_DOMAINE_DEFAUT,
                        CS_TYPE_DOMICILE, true);
            } else if (updatedToday && !isCourrier) {
                return VulpeculaServiceLocator.getAdresseService().updateAdresse(adresseComplexModel,
                        CS_DOMAINE_DEFAUT, CS_TYPE_DOMICILE, true);
            } else {
                // updatedToday && isCourrier
                adresseComplexModel.getAdresse().setCasePostale(adresseInfoGSON.getCasePostale());
                return VulpeculaServiceLocator.getAdresseService().updateAdresse(adresseComplexModel,
                        CS_DOMAINE_DEFAUT, CS_TYPE_COURRIER, true);
            }

        } else {
            addError(
                    "Erreur mise a jour de l'adresse => Plusieurs localité pour le npa renseigné, le cas est à traiter manuellement",
                    null);
            return new AdresseComplexModel();
        }

    }

    public TIAdressePaiement updateBanque(String banqueJSON) throws Exception {

        Gson gson = new Gson();

        BanqueInfoGSON banqueGSON = gson.fromJson(banqueJSON, new TypeToken<BanqueInfoGSON>() {
        }.getType());

        Validate.notNull(banqueGSON);

        try {

            AdresseTiersDetail adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    banqueGSON.getIdTiersExistant(), true, Date.now().getSwissValue(), CS_DOMAINE_DEFAUT,
                    CS_TYPE_DOMICILE, null);

            String idTiers = banqueGSON.getIdTiersExistant();

            TIAdressePaiement adressePaiement = new TIAdressePaiement();
            adressePaiement.setIdTiersAdresse(idTiers);
            adressePaiement.setIdAdresse(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_ID_ADRESSE));

            if (!banqueGSON.getIban().isEmpty()) {
                adressePaiement.setNumCompteBancaire(banqueGSON.getIban());
                adressePaiement.setCode(IConstantes.CS_ADRESSE_PAIEMENT_IBAN_OK);
                String idTiersBanque = traitementIban(banqueGSON);
                if (idTiersBanque != null) {
                    adressePaiement.setIdTiersBanque(idTiersBanque);

                }
            }

            adressePaiement.setIdMonnaie(AdressePaiementPrimeNoelService.CS_CODE_MONNAIE_FRANC_SUISSE);
            adressePaiement.setIdPays(AdressePaiementPrimeNoelService.CS_CODE_PAYS_SUISSE);
            adressePaiement.add();

            TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
            avoirPaiement.setIdApplication(CS_DOMAINE_DEFAUT);
            avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
            avoirPaiement.setIdAdrPmtIntUnique(adressePaiement.getIdAdressePaiement());
            avoirPaiement.setDateDebutRelation(Date.now().getSwissValue());
            avoirPaiement.setIdTiers(idTiers);
            avoirPaiement.setSession(BSessionUtil.getSessionFromThreadContext());
            avoirPaiement.add();

            setStatus(banqueGSON.getIdPortail(), StatusEbu.MODIFIE, "banque");
            majStatusGlobal(banqueGSON.getIdPortail());

            return adressePaiement;

        } catch (Exception ex) {
            addError("Erreur mise a jour travailleur => " + ex.getMessage(), ex);
        }

        return null;
    }

    public PosteTravail updatePosteTravail(String posteTravailJSON) {
        Gson gson = new Gson();

        PosteTravailInfoGSON posteTravailGSON = gson.fromJson(posteTravailJSON, new TypeToken<PosteTravailInfoGSON>() {
        }.getType());

        Validate.notNull(posteTravailGSON);
        Date oldDateFin = null;

        try {

            if (JadeStringUtil.isEmpty(posteTravailGSON.getIdPosteTravailExistant())) {

                PosteTravail posteTravail = insertPosteTravail(posteTravailJSON);
                PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().update(posteTravail);
                setStatus(posteTravailGSON.getIdPortail(), StatusEbu.AJOUTE, "poste");
                majStatusGlobal(posteTravailGSON.getIdPortail());
                return poste;

            } else {
                PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                        posteTravailGSON.getIdPosteTravailExistant());
                oldDateFin = posteTravail.getPeriodeActivite().getDateFin();
                posteTravail.setIdPortail(posteTravailGSON.getIdPortail());
                posteTravail.setCorrelationId(posteTravailGSON.getCorrelationId());
                posteTravail.setPosteCorrelationId(posteTravailGSON.getPosteCorrelationId());
                List<Occupation> occupationList = VulpeculaRepositoryLocator.getOccupationRepository()
                        .findOccupationsByIdPosteTravail(posteTravailGSON.getIdPosteTravailExistant());

                Qualification qualification = Qualification.fromValue(posteTravailGSON.getQualification());
                posteTravail.setQualification(qualification);

                String dateDebutExistante = posteTravail.getPeriodeActivite().getDateDebutAsSwissValue();
                Periode periodeActivite = new Periode(dateDebutExistante, posteTravailGSON.getDateFin());
                posteTravail.setPeriodeActivite(periodeActivite);

                TypeSalaire typeSalaire = TypeSalaire.fromValue(posteTravailGSON.getTypeSalaire());
                posteTravail.setTypeSalaire(typeSalaire);

                Boolean tauxFound = false;
                Boolean sameDebut = false;
                Occupation occupation = new Occupation();
                Taux tauxActivite = new Taux(posteTravailGSON.getTauxActivite());
                occupation.setTaux(tauxActivite);
                for (Occupation tmpOccupation : occupationList) {
                    if (tmpOccupation.getTauxAsValue().equals(tauxActivite.getValue())) {
                        tauxFound = true;
                    }
                    if (tmpOccupation.getDateValiditeAsValue().equals(posteTravailGSON.getDateTauxActivite())) {
                        sameDebut = true;
                        tmpOccupation.setTaux(tauxActivite);
                    }
                }
                if (Date.isNull(posteTravailGSON.getDateTauxActivite())) {
                    occupation.setDateValidite(new Date(posteTravailGSON.getDateDebut()));
                } else {
                    occupation.setDateValidite(new Date(posteTravailGSON.getDateTauxActivite()));
                }
                if (!tauxFound && !sameDebut) {
                    occupationList.add(occupation);
                }
                posteTravail.setOccupations(occupationList);

                PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().updateForEbu(posteTravail,
                        oldDateFin);
                poste = VulpeculaServiceLocator.getPosteTravailService().update(poste);
                setStatus(posteTravailGSON.getIdPortail(), StatusEbu.MODIFIE, "poste");
                majStatusGlobal(posteTravailGSON.getIdPortail());

                return poste;

            }

        } catch (Exception ex) {
            addError("Erreur mise a jour travailleur => " + ex.getMessage(), ex);
        }

        return null;

    }

    public Travailleur updateTravailleur(String travailleurInfoJSON) {
        Gson gson = new Gson();
        RetourCreationPortail retour = new RetourCreationPortail();

        TravailleurInfoGSON travailleurInfoGSON = gson.fromJson(travailleurInfoJSON,
                new TypeToken<TravailleurInfoGSON>() {
                }.getType());

        Validate.notNull(travailleurInfoGSON);

        try {
            Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                    travailleurInfoGSON.getIdTravailleurExistant());
            travailleur.setIdTiers(travailleurInfoGSON.getIdTiersExistant());
            travailleur.setCorrelationId(travailleurInfoGSON.getCorrelationId());

            if (!travailleurInfoGSON.getPermisTravail().isEmpty()) {
                PermisTravail permisTravail = PermisTravail.fromValue(travailleurInfoGSON.getPermisTravail());
                travailleur.setPermisTravail(permisTravail);
            }
            travailleur.setReferencePermis(travailleurInfoGSON.getReferencePermis());
            travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().update(travailleur);

            setStatus(travailleurInfoGSON.getIdPortail(), StatusEbu.MODIFIE, "travailleur");
            majStatusGlobal(travailleurInfoGSON.getIdPortail());

            return travailleur;
        } catch (Exception ex) {
            addError("Erreur mise a jour travailleur => " + ex.getMessage(), ex);
        }

        return null;
    }

    public PosteTravail insertPosteTravail(String posteTravailJSON) throws UnsatisfiedSpecificationException,
            JadePersistenceException {

        Gson gson = new Gson();

        PosteTravailInfoGSON posteGSON = gson.fromJson(posteTravailJSON, new TypeToken<PosteTravailInfoGSON>() {
        }.getType());

        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                posteGSON.getIdTravailleur());
        return insertPosteTravail2(travailleur, posteGSON);
    }

    public PosteTravail insertPosteTravail2(Travailleur travailleur, PosteTravailInfoGSON posteTravailGSON)
            throws UnsatisfiedSpecificationException, JadePersistenceException {

        Employeur employeur = new Employeur();
        employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(posteTravailGSON.getIdAffiliation());

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setEmployeur(employeur);

        posteTravail.setCorrelationId(posteTravailGSON.getCorrelationId());
        posteTravail.setPosteCorrelationId(posteTravailGSON.getPosteCorrelationId());

        posteTravail.setTravailleur(travailleur);

        Qualification qualification = Qualification.fromValue(posteTravailGSON.getQualification());
        posteTravail.setQualification(qualification);

        Periode periodeActivite = new Periode(posteTravailGSON.getDateDebut(), posteTravailGSON.getDateFin());
        posteTravail.setPeriodeActivite(periodeActivite);

        TypeSalaire typeSalaire = TypeSalaire.fromValue(posteTravailGSON.getTypeSalaire());
        posteTravail.setTypeSalaire(typeSalaire);

        Occupation occupation = new Occupation();
        occupation.setDateValidite(Date.now());
        Taux tauxActivite = new Taux(posteTravailGSON.getTauxActivite());
        occupation.setTaux(tauxActivite);

        Boolean tauxFound = false;
        for (Occupation tmpOccupation : posteTravail.getOccupations()) {
            if (tmpOccupation.getTauxAsValue().equals(tauxActivite)) {
                tauxFound = true;
            }
        }

        if (Date.isNull(posteTravailGSON.getDateTauxActivite())) {
            occupation.setDateValidite(new Date(posteTravailGSON.getDateDebut()));
        } else {
            occupation.setDateValidite(new Date(posteTravailGSON.getDateTauxActivite()));
        }

        if (!tauxFound) {
            posteTravail.addTauxOccupation(occupation);
        }

        for (AdhesionCotisationPosteTravail adh : posteTravail.getAdhesionsCotisations()) {
            adh.setCotisation(VulpeculaServiceLocator.getCotisationService().findById(adh.getIdCotisation()));
        }

        return VulpeculaServiceLocator.getPosteTravailService().create(posteTravail);
    }

}
