package ch.globaz.vulpecula.web.views.ebusiness;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.commons.lang.Validate;
import ch.globaz.common.sql.QueryUpdateExecutor;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.pegasus.businessimpl.services.process.allocationsNoel.AdressePaiementPrimeNoelService;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService.motifsModification;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PermisTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.external.models.MoyenContactTiersSimpleModel;
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

public class NouveauTravailleurViewService extends TraitementTravailleurDefault {

    public void quittancer(String idAnnonceTravailleur) {
        TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                .findByIdSansQuittance(idAnnonceTravailleur);
        // travailleur.setTraite(true);
        travailleur.setStatus(StatusAnnonceTravailleurEbu.TRAITE);
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleur);
    }

    public String insertTiers(String tiersJSON) {

        Gson gson = new Gson();
        RetourCreationPortail retour = new RetourCreationPortail();
        boolean isTiersModifiable = false;
        PersonneEtendueComplexModel personneEtendue = null;

        TiersInfoGSON tiersGSON = gson.fromJson(tiersJSON, new TypeToken<TiersInfoGSON>() {
        }.getType());

        Validate.notNull(tiersGSON);

        PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
        if (tiersGSON.getNss() != null && !tiersGSON.getNss().isEmpty() && !"756.".equals(tiersGSON.getNss())) {
            searchTiers.setForNumeroAvsActuel(tiersGSON.getNss());
            isTiersModifiable = true;
        } else {
            searchTiers.setForDateNaissance(tiersGSON.getDateNaissance());
            searchTiers.setForDesignation1Like(tiersGSON.getNom());
            searchTiers.setForDesignation2Like(tiersGSON.getPrenom());
            isTiersModifiable = true;
        }

        try {
            PersonneEtendueSearchComplexModel personneEtendueSearch = null;
            if (isTiersModifiable) {
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService().find(searchTiers);
                if (personneEtendueSearch.getSearchResults().length > 0) {
                    personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch.getSearchResults()[0];
                    if (personneEtendue != null) {
                        searchTiers.setFor_isInactif("1");
                        personneEtendue = (PersonneEtendueComplexModel) personneEtendueSearch.getSearchResults()[0];
                        if (personneEtendue != null) {
                            addError("Le tiers existe déjà, le cas est à traiter manuellement", null);
                            // WorkAround : isTiersModifiable set to false pour ne pas entrer en UPDATE => set to true
                            // pour
                            // réactiver la MAJ
                            // isTiersModifiable = true;
                            isTiersModifiable = false;
                        }
                    } else {
                        isTiersModifiable = false;
                    }
                } else {
                    isTiersModifiable = false;
                }
            }

            PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
            TravailleurEbuDomain annonce = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    tiersGSON.getIdPortail());
            personneEtendueComplexModel.getTiers().setLangue(getLangueEmployeur(annonce));
            personneEtendueComplexModel.getTiers().set_personnePhysique("1");

            if (tiersGSON.getSexe().equals("516001")) {
                personneEtendueComplexModel.getTiers().setTitreTiers("502001");
            } else {
                personneEtendueComplexModel.getTiers().setTitreTiers("502002");
            }

            personneEtendueComplexModel.getTiers().setDesignation1(tiersGSON.getNom());
            personneEtendueComplexModel.getTiers().setDesignation2(tiersGSON.getPrenom());
            personneEtendueComplexModel.getPersonne().setDateNaissance(tiersGSON.getDateNaissance());
            personneEtendueComplexModel.getPersonne().setSexe(tiersGSON.getSexe());
            personneEtendueComplexModel.getPersonne().setEtatCivil(tiersGSON.getEtatCivil());

            // Utilisation de la nationalité pour récupérer le pays de la personne
            if (!JadeStringUtil.isEmpty(tiersGSON.getNationalite())) {
                TIPays pays = new TIPays();
                Hashtable<String, String> params = new Hashtable<String, String>();
                params.put("setForCodeIso", tiersGSON.getNationalite());
                BManager manager = pays.find(params);

                pays = (TIPays) manager.getFirstEntity();
                personneEtendueComplexModel.getTiers().setIdPays(pays.getId());
            } else {
                personneEtendueComplexModel.getTiers().setIdPays("");
            }

            if (!isTiersModifiable) {
                if (!"756.".equals(tiersGSON.getNss())) {
                    personneEtendueComplexModel.getPersonneEtendue().setNumAvsActuel(tiersGSON.getNss());
                }
                personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().create(
                        personneEtendueComplexModel);
            } else {
                personneEtendueComplexModel.setSpy(personneEtendue.getSpy());
                personneEtendueComplexModel.setMotifModifDesignation1(motifsModification.MOTIF_MODIFICATION);
                personneEtendueComplexModel.setDateModifDesignation1(JACalendar.todayJJsMMsAAAA());
                personneEtendueComplexModel.setMotifModifDesignation2(motifsModification.MOTIF_MODIFICATION);
                personneEtendueComplexModel.setDateModifDesignation2(JACalendar.todayJJsMMsAAAA());
                personneEtendueComplexModel.setMotifModifTitre(motifsModification.MOTIF_MODIFICATION);
                personneEtendueComplexModel.setDateModifTitre(JACalendar.todayJJsMMsAAAA());
                personneEtendueComplexModel.setMotifModifPays(motifsModification.MOTIF_MODIFICATION);
                personneEtendueComplexModel.setDateModifPays(JACalendar.todayJJsMMsAAAA());
                personneEtendueComplexModel.setId(personneEtendue.getId());
                personneEtendueComplexModel.getTiers().setId(personneEtendue.getTiers().getId());
                if (!"756.".equals(tiersGSON.getNss())) {
                    personneEtendueComplexModel.setMotifModifAvs(motifsModification.MOTIF_MODIFICATION);
                    personneEtendueComplexModel.setDateModifAvs(JACalendar.todayJJsMMsAAAA());
                    personneEtendueComplexModel.getPersonneEtendue().setNumAvsActuel(tiersGSON.getNss());
                } else {
                    personneEtendueComplexModel.setMotifModifAvs(motifsModification.MOTIF_MODIFICATION);
                    personneEtendueComplexModel.setDateModifAvs(JACalendar.todayJJsMMsAAAA());
                    personneEtendueComplexModel.getPersonneEtendue().setNumAvsActuel(
                            personneEtendue.getPersonneEtendue().getNumAvsActuel());
                }
                personneEtendueComplexModel.getPersonneEtendue().setNumContribuableActuel(
                        personneEtendue.getPersonneEtendue().getNumContribuableActuel());
                personneEtendueComplexModel.getTiers().setDesignation3(personneEtendue.getTiers().getDesignation3());
                personneEtendueComplexModel.getTiers().setDesignation4(personneEtendue.getTiers().getDesignation4());
                personneEtendueComplexModel.getTiers().setTitreTiers(personneEtendue.getTiers().getTitreTiers());
                personneEtendueComplexModel.getTiers().set_inactif("2");
                personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().update(
                        personneEtendueComplexModel);
            }

            // Si la création du tiers s'est bien déroulée, on va créer le reste
            if (!personneEtendueComplexModel.isNew()) {
                AdresseComplexModel adresseCourrier = insertAdresse(personneEtendueComplexModel,
                        tiersGSON.getAdresseInfo());

                // Si la création de l'adresse de courrier s'est bien passé, on créé l'adresse de paiement
                if (!adresseCourrier.isNew()) {
                    insertBanque(personneEtendueComplexModel, adresseCourrier, tiersGSON.getAdresseBanqueInfo());
                }

                // Création du travailleur
                if (!isTiersModifiable) {
                    Travailleur travailleur = insertTravailleur(personneEtendueComplexModel,
                            tiersGSON.getTravailleurInfo());
                    if (travailleur.getId() != null) {
                        // Création du poste de travail
                        PosteTravail posteTravail = insertPosteTravail2(travailleur, tiersGSON.getPosteTravailInfo());
                        retour.setIdPosteTravail(posteTravail.getId());
                    }
                } else {
                    Travailleur travailleur = VulpeculaServiceLocator.getTravailleurService()
                            .findByNomPrenomDateNaissance(tiersGSON.getNom(), tiersGSON.getPrenom(),
                                    tiersGSON.getDateNaissance());

                    travailleur.setCorrelationId(tiersGSON.getTravailleurInfo().getCorrelationId());

                    if (!tiersGSON.getTravailleurInfo().getPermisTravail().isEmpty()) {
                        PermisTravail permisTravail = PermisTravail.fromValue(tiersGSON.getTravailleurInfo()
                                .getPermisTravail());
                        travailleur.setPermisTravail(permisTravail);
                    }
                    travailleur.setReferencePermis(tiersGSON.getTravailleurInfo().getReferencePermis());
                    travailleur.setAnnonceMeroba(false);

                    VulpeculaRepositoryLocator.getTravailleurRepository().update(travailleur);
                }

            }

            String idTiers = personneEtendueComplexModel.getTiers().getIdTiers();
            setMoyenContact(idTiers, tiersGSON);

            TravailleurEbuDomain tEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    tiersGSON.getIdPortail());
            // tEbu.setTraite(true);
            tEbu.setStatus(StatusAnnonceTravailleurEbu.TRAITE);
            // tEbu.setModification(true);
            VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(tEbu);
            createSyncFromModification(tEbu);

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

    public static void setMoyenContact(String idTiers, TiersInfoGSON tiersGSON) throws JadePersistenceException {

        Contact contact = VulpeculaRepositoryLocator.getContactRepository().findForIdTiersWithMoyens(idTiers);

        MoyenContact moyenExistant = contact.getMoyenContact().get(TypeContact.PRIVE);
        HashMap<TypeContact, MoyenContact> mapMoyen = contact.getMoyenContact();

        if (moyenExistant == null) {
            MoyenContactTiersSimpleModel model = new MoyenContactTiersSimpleModel();
            model.setApplication("519004");
            model.setIdContact(contact.getId());
            model.setTypeContact(TypeContact.PRIVE.getValue());
            model.setValeur(tiersGSON.getTelephone());
            JadePersistenceManager.add(model);
        } else {
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext2(session);
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
        }

    }

    private static String getQueryUpdateMoyenContact(String HLICON, String HLTTCO, String HLAPPL, String value) {
        return "UPDATE SCHEMA.TIMCOMP SET HLICON=" + HLICON + ", HLCONT='" + value + "', HLTTCO=" + HLTTCO
                + ", HLAPPL=" + HLAPPL + " WHERE HLICON=" + HLICON + " AND HLTTCO=" + HLTTCO + " AND HLAPPL=" + HLAPPL;
    }

    protected static JadeThreadContext initThreadContext2(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    public AdresseComplexModel insertAdresse(PersonneEtendueComplexModel personneEtendueComplexModel,
            AdresseInfoGSON adresseInfoGSON) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        return VulpeculaServiceLocator.getNouveauTravailleurService().insertAdresse(personneEtendueComplexModel,
                adresseInfoGSON);
    }

    public void insertBanque(PersonneEtendueComplexModel personneEtendueComplexModel,
            AdresseComplexModel adresseComplexModel, BanqueInfoGSON banqueGSON) throws Exception {

        String idTiers = personneEtendueComplexModel.getTiers().getId();

        TIAdressePaiement adressePaiement = new TIAdressePaiement();
        adressePaiement.setIdTiersAdresse(idTiers);
        adressePaiement.setIdAdresse(adresseComplexModel.getAdresse().getId());

        if (!banqueGSON.getIban().isEmpty()) {
            adressePaiement.setNumCompteBancaire(banqueGSON.getIban());
            adressePaiement.setCode(IConstantes.CS_ADRESSE_PAIEMENT_IBAN_OK);
            String idTiersBanque = traitementIban(banqueGSON);
            if (idTiersBanque != null) {
                adressePaiement.setIdTiersBanque(idTiersBanque);

                adressePaiement.setIdMonnaie(AdressePaiementPrimeNoelService.CS_CODE_MONNAIE_FRANC_SUISSE);
                adressePaiement.setIdPays(AdressePaiementPrimeNoelService.CS_CODE_PAYS_SUISSE);
                adressePaiement.add();

                TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
                avoirPaiement.setIdApplication(CS_DOMAINE_DEFAUT);
                avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
                avoirPaiement.setDateDebutRelation(Date.now().getSwissValue());
                avoirPaiement.setIdTiers(idTiers);
                avoirPaiement.setSession(BSessionUtil.getSessionFromThreadContext());
                avoirPaiement.add();
            }
        }

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

    private int getAssuranceCodeSystem(String assuranceName) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("HELSANA", 1);
        map.put("CSS", 2);
        map.put("MUTUEL", 3);
        map.put("NON_CTT", 4);
        if (JadeStringUtil.isEmpty(assuranceName)) {
            return map.get("NON_CTT");
        }
        return map.get(assuranceName);
    }

    public PosteTravail insertPosteTravail2(Travailleur travailleur, PosteTravailInfoGSON posteTravailGSON)
            throws UnsatisfiedSpecificationException, JadePersistenceException {

        Employeur employeur = new Employeur();
        employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(posteTravailGSON.getIdAffiliation());

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setEmployeur(employeur);

        posteTravail.setTravailleur(travailleur);

        posteTravail.setPosteCorrelationId(posteTravailGSON.getPosteCorrelationId());

        Qualification qualification = Qualification.fromValue(posteTravailGSON.getQualification());
        posteTravail.setQualification(qualification);

        Periode periodeActivite = new Periode(posteTravailGSON.getDateDebut(), posteTravailGSON.getDateFin());
        posteTravail.setPeriodeActivite(periodeActivite);

        TypeSalaire typeSalaire = TypeSalaire.fromValue(posteTravailGSON.getTypeSalaire());
        posteTravail.setTypeSalaire(typeSalaire);

        Occupation occupation = new Occupation();
        Taux tauxActivite = new Taux(posteTravailGSON.getTauxActivite());
        occupation.setTaux(tauxActivite);
        occupation.setDateValidite(new Date(posteTravailGSON.getDateDebut()));

        posteTravail.setOccupations(Arrays.asList(occupation));

        // set Affiliation caisse maladie
        AffiliationCaisseMaladieService cmService = VulpeculaServiceLocator.getAffiliationCaisseMaladieService();
        String codeAssurance = cmService.translateEbuCodeToWMCode(getAssuranceCodeSystem(posteTravailGSON
                .getAssurance()));
        posteTravail.setIdTiersCM(codeAssurance);
        // create poste avant persister affiliation
        posteTravail = VulpeculaServiceLocator.getPosteTravailService().create(posteTravail);
        // cmService.createForPosteTravail(posteTravail);

        // persist occupation
        // TauxOccupationSimpleModel tauxOccupationSimpleModel = OccupationConverter.convertToPersistence(
        // posteTravail.getId(), occupation);
        // JadePersistenceManager.add(tauxOccupationSimpleModel);

        TravailleurEbuDomain travEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                .findByPosteCorrelationId(posteTravail.getPosteCorrelationId());
        travEbu.setPosteStatus(StatusEbu.MODIFIE);
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travEbu);

        ModificationTravailleurViewService.majStatusGlobal(posteTravailGSON.getIdPortail());

        return posteTravail;
    }

    /**
     * Création du travailleur
     * 
     * @param personneEtendueComplexModel
     * @param travailleurGSON
     * @param correlationId
     * @return
     * @throws JadePersistenceException
     */
    public Travailleur insertTravailleur(PersonneEtendueComplexModel personneEtendueComplexModel,
            TravailleurInfoGSON travailleurGSON) throws JadePersistenceException {

        Travailleur travailleur = new Travailleur();
        travailleur.setIdTiers(personneEtendueComplexModel.getTiers().getId());
        travailleur.setCorrelationId(travailleurGSON.getCorrelationId());

        if (!travailleurGSON.getPermisTravail().isEmpty()) {
            PermisTravail permisTravail = PermisTravail.fromValue(travailleurGSON.getPermisTravail());
            travailleur.setPermisTravail(permisTravail);
        }
        travailleur.setReferencePermis(travailleurGSON.getReferencePermis());
        travailleur.setAnnonceMeroba(false);

        return VulpeculaRepositoryLocator.getTravailleurRepository().create(travailleur);

    }

    public static boolean isTraite(TravailleurEbuDomain travailleur) {
        boolean flag = false;

        if (travailleur.getTiersStatus() == StatusEbu.NO_DIFF || travailleur.getTiersStatus() == StatusEbu.TRAITE
                || travailleur.getTiersStatus() == StatusEbu.REFUSE
                || travailleur.getTiersStatus() == StatusEbu.MODIFIE) {
            flag = true;
        }

        if ((travailleur.getTravailleurStatus() == StatusEbu.NO_DIFF
                || travailleur.getTravailleurStatus() == StatusEbu.TRAITE
                || travailleur.getTravailleurStatus() == StatusEbu.REFUSE || travailleur.getTravailleurStatus() == StatusEbu.MODIFIE)
                && flag) {
            flag = true;
        } else {
            flag = false;
        }

        if ((travailleur.getPosteStatus() == StatusEbu.NO_DIFF || travailleur.getPosteStatus() == StatusEbu.TRAITE
                || travailleur.getPosteStatus() == StatusEbu.REFUSE || travailleur.getPosteStatus() == StatusEbu.MODIFIE)
                && flag) {
            flag = true;
        } else {
            flag = false;
        }

        if ((travailleur.getBanqueStatus() == StatusEbu.NO_DIFF || travailleur.getBanqueStatus() == StatusEbu.TRAITE
                || travailleur.getBanqueStatus() == StatusEbu.REFUSE || travailleur.getBanqueStatus() == StatusEbu.MODIFIE)
                && flag) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    private void createSyncFromModification(TravailleurEbuDomain travailleurEbu) {
        Travailleur travailleur = null;

        if (travailleurEbu.getNss() != null && !travailleurEbu.getNss().isEmpty()) {
            travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(travailleurEbu.getNss());
        } else {
            travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findByNomPrenomDateNaissance(
                    travailleurEbu.getNom(), travailleurEbu.getPrenom(), travailleurEbu.getDateNaissance());
        }

        // TODO gérer le cas ou le travailleur est null
        if (travailleur != null) {
            try {
                VulpeculaServiceLocator.getTravailleurService().notifierSynchronisationEbu(travailleur.getId(),
                        travailleurEbu.getCorrelationId());
            } catch (JadePersistenceException e) {
                addError("JadePersistenceException : " + e.getMessage(), e);
            }
        } else {
            try {
                VulpeculaServiceLocator.getTravailleurService().notifierSynchroAnnonce(travailleurEbu.getId(),
                        travailleurEbu.getCorrelationId(), travailleurEbu.getPosteCorrelationId());
            } catch (JadePersistenceException e) {
                addError("JadePersistenceException : " + e.getMessage(), e);
            }
        }
    }

    private void traitementRefus(TravailleurEbuDomain travailleurEbu) {
        if (isTraite(travailleurEbu)) {
            travailleurEbu.setStatus(ModificationTravailleurViewService.getStatusAnnonce(travailleurEbu));
        } else {
            travailleurEbu.setStatus(StatusAnnonceTravailleurEbu.EN_COURS);
        }
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleurEbu);
        try {
            VulpeculaServiceLocator.getTravailleurService().notifierSynchroAnnonce(travailleurEbu.getId(),
                    travailleurEbu.getCorrelationId(), travailleurEbu.getPosteCorrelationId());
        } catch (JadePersistenceException e) {
            addError("JadePersistenceException : " + e.getMessage(), e);
        }

    }

    public void refuse(String idTravailleur, String refusedField) {
        boolean refuseAll = false;
        if ("all".equals(refusedField)) {
            refuseAll = true;
        }
        if (refuseAll || refusedField.equals("refuseTiers")) {
            TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    idTravailleur);
            travailleur.setTiersStatus(StatusEbu.REFUSE);
            traitementRefus(travailleur);
        }

        if (refuseAll || refusedField.equals("refuseTravailleur")) {
            TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    idTravailleur);
            travailleur.setTravailleurStatus(StatusEbu.REFUSE);
            traitementRefus(travailleur);
        }

        if (refuseAll || refusedField.equals("refusePosteTravail")) {
            TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    idTravailleur);
            travailleur.setPosteStatus(StatusEbu.REFUSE);
            traitementRefus(travailleur);
        }

        if (refuseAll || refusedField.equals("refuseBanque")) {
            TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    idTravailleur);
            travailleur.setBanqueStatus(StatusEbu.REFUSE);
            traitementRefus(travailleur);
        }
    }

    public static int getCountForTiers(String nom, String prenom, String dateNaissance)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();

        searchTiers.setForDateNaissance(dateNaissance);
        searchTiers.setForDesignation1Like(nom);
        searchTiers.setForDesignation2Like(prenom);

        PersonneEtendueSearchComplexModel personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                .find(searchTiers);

        return personneEtendueSearch.getSearchResults().length;
    }

    public static int getCountForTiersNSS(String nss) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();

        searchTiers.setForNumeroAvsActuel(nss);

        PersonneEtendueSearchComplexModel personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                .find(searchTiers);

        return personneEtendueSearch.getSearchResults().length;
    }
}
