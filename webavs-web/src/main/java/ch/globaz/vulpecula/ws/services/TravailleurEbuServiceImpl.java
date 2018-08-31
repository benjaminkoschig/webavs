package ch.globaz.vulpecula.ws.services;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.common.sql.QueryUpdateExecutor;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.ebusiness.SynchronisationTravailleur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.web.views.ebusiness.ModificationTravailleurViewService;
import ch.globaz.vulpecula.ws.bean.AdresseEbu;
import ch.globaz.vulpecula.ws.bean.AssuranceMaladieEbu;
import ch.globaz.vulpecula.ws.bean.CompteBanquaireEbu;
import ch.globaz.vulpecula.ws.bean.ContratAssuranceMaladieEbu;
import ch.globaz.vulpecula.ws.bean.DureeContratEbu;
import ch.globaz.vulpecula.ws.bean.LocaliteEbu;
import ch.globaz.vulpecula.ws.bean.PermisTravailEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;
import ch.globaz.vulpecula.ws.bean.StatusEbu;
import ch.globaz.vulpecula.ws.bean.StatusReponse;
import ch.globaz.vulpecula.ws.bean.TravailleurEbu;
import ch.globaz.vulpecula.ws.utils.UtilsService;

/**
 * @since eBMS 1.0
 */
@WebService(endpointInterface = "ch.globaz.vulpecula.ws.services.TravailleurEbuService")
public class TravailleurEbuServiceImpl extends VulpeculaAbstractService implements TravailleurEbuService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static void setStatus(TravailleurEbuDomain travailleurEbu) {
        Travailleur travailleurExistant = null;

        if (travailleurEbu.getIdTravailleur() != null && !travailleurEbu.getIdTravailleur().isEmpty()) {
            travailleurExistant = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                    travailleurEbu.getIdTravailleur());
        }
        if (travailleurExistant == null && travailleurEbu.getNss() != null && !travailleurEbu.getNss().isEmpty()) {
            travailleurExistant = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(
                    travailleurEbu.getNss());
        }

        if (travailleurExistant == null && travailleurEbu.getCorrelationId() != null
                && !travailleurEbu.getCorrelationId().isEmpty()) {
            travailleurExistant = VulpeculaRepositoryLocator.getTravailleurRepository().findByCorrelationId(
                    travailleurEbu.getCorrelationId());
        }
        if (travailleurExistant == null) {
            travailleurExistant = VulpeculaServiceLocator.getTravailleurService()
                    .findByNomPrenomDateNaissanceEmployeur(travailleurEbu.getNom(), travailleurEbu.getPrenom(),
                            travailleurEbu.getDateNaissance(), travailleurEbu.getIdEmployeur());
        }

        if (travailleurExistant == null) {
            travailleurExistant = VulpeculaServiceLocator.getTravailleurService().findByNomPrenomDateNaissance(
                    travailleurEbu.getNom(), travailleurEbu.getPrenom(), travailleurEbu.getDateNaissance());
        }

        if (travailleurExistant != null) {
            compareEbuEtExistant(travailleurEbu, travailleurExistant);
        } else {
            travailleurEbu.setStatus(StatusAnnonceTravailleurEbu.EN_COURS);
            travailleurEbu.setTiersStatus(StatusEbu.A_TRAITER);
            travailleurEbu.setPosteStatus(StatusEbu.A_TRAITER);
            travailleurEbu.setTravailleurStatus(StatusEbu.A_TRAITER);
            travailleurEbu.setBanqueStatus(StatusEbu.A_TRAITER);
            VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleurEbu);
        }

        ModificationTravailleurViewService.majStatusGlobal(travailleurEbu.getId());
    }

    private static void compareEbuEtExistant(TravailleurEbuDomain travailleurEbu, Travailleur travailleurExistant) {
        checkTiersInfo(travailleurEbu, travailleurExistant);
        checkPosteInfo(travailleurEbu, travailleurExistant);
        checkTravailleurInfo(travailleurEbu, travailleurExistant);
        checkBanqueInfo(travailleurEbu, travailleurExistant);
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleurEbu);
    }

    private static void checkBanqueInfo(TravailleurEbuDomain travailleurEbu, Travailleur travailleurExistant) {
        boolean isSame = true;

        String iban = VulpeculaRepositoryLocator.getAdresseRepository().findIbanByIdTiers(
                travailleurExistant.getIdTiers(), new Date());
        if (!JadeStringUtil.isEmpty(iban) && JadeStringUtil.isEmpty(travailleurEbu.getCompteBancaire().getIban())) {
            travailleurEbu.getCompteBancaire().setIban(iban);
        }

        if (!travailleurEbu.getCompteBancaire().getIban().equals(iban)) {
            isSame = false;
        }

        if (isSame) {
            travailleurEbu.setBanqueStatus(StatusEbu.NO_DIFF);
        } else {
            travailleurEbu.setBanqueStatus(StatusEbu.A_TRAITER);
        }
    }

    private static void checkTravailleurInfo(TravailleurEbuDomain travailleurEbu, Travailleur travailleurExistant) {
        boolean isSame = true;

        if (travailleurExistant.getPermisTravail() != null
                && !JadeStringUtil.isEmpty(travailleurEbu.getPermisSejour().getCategoriePermis())) {
            if (!travailleurEbu.getPermisSejour().getCategoriePermis()
                    .equals(travailleurExistant.getPermisTravail().getValue())) {
                isSame = false;
            }
        } else if (travailleurExistant.getPermisTravail() == null
                && !JadeStringUtil.isEmpty(travailleurEbu.getPermisSejour().getCategoriePermis())) {
            isSame = false;
        }

        if (isSame) {
            travailleurEbu.setTravailleurStatus(StatusEbu.NO_DIFF);
        } else {
            travailleurEbu.setTravailleurStatus(StatusEbu.A_TRAITER);
        }
    }

    private static void checkPosteInfo(TravailleurEbuDomain travailleurEbu, Travailleur travailleurExistant) {
        boolean isSame = true;
        PosteTravail posteTravailEbu = travailleurEbu.getPosteTravailFictifForSynchro();

        List<Qualification> qualifications = new ArrayList<Qualification>();
        qualifications.add(travailleurEbu.getCodeProfessionnel());

        PosteTravail posteTravailExistant = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByTravailleurEmployeurEtQualification(travailleurExistant.getId(),
                        travailleurEbu.getIdEmployeur(), qualifications);

        if (posteTravailExistant != null && posteTravailExistant.getIdEmployeur() != null) {
            if (!posteTravailEbu.getQualificationAsValue().equals(posteTravailExistant.getQualificationAsValue())) {
                isSame = false;
            }
            if (!posteTravailEbu.getPeriodeActivite().getDateDebut()
                    .equals(posteTravailExistant.getPeriodeActivite().getDateDebut())) {
                isSame = false;
            }

            if (posteTravailEbu.getPeriodeActivite().getDateFin() != null) {
                if (!posteTravailEbu.getPeriodeActivite().getDateFin()
                        .equals(posteTravailExistant.getPeriodeActivite().getDateFin())) {
                    isSame = false;
                }
            } else if (posteTravailEbu.getPeriodeActivite().getDateFin() == null
                    && posteTravailExistant.getPeriodeActivite().getDateFin() != null
                    && !JadeStringUtil.isEmpty(posteTravailExistant.getPeriodeActivite().getDateFinAsSwissValue())) {
                isSame = false;
            }

            if (posteTravailEbu.getTypeSalaire() != null) {
                if (!posteTravailEbu.getTypeSalaireAsValue().equals(posteTravailExistant.getTypeSalaireAsValue())) {
                    isSame = false;
                }
            }

            Taux tauxAcomparer = new Taux(travailleurEbu.getTauxActivite());
            if (!tauxAcomparer.equals(posteTravailExistant.getOccupationActuel().getTaux())) {
                isSame = false;
            }

        } else {
            isSame = false;
        }

        if (isSame) {
            travailleurEbu.setPosteStatus(StatusEbu.NO_DIFF);
        } else {
            travailleurEbu.setPosteStatus(StatusEbu.A_TRAITER);
        }

    }

    private static void checkTiersInfo(TravailleurEbuDomain travailleurEbu, Travailleur travailleurExistant) {
        boolean isSame = true;

        if (!travailleurEbu.getNss().isEmpty()
                && !travailleurEbu.getNss().equals(travailleurExistant.getNumAvsActuel())) {
            isSame = false;
        }
        if (!travailleurEbu.getNom().equals(travailleurExistant.getDesignation1())) {
            isSame = false;
        }
        if (!travailleurEbu.getPrenom().equals(travailleurExistant.getDesignation2())) {
            isSame = false;
        }
        if (!travailleurEbu.getDateNaissance().equals(travailleurExistant.getDateNaissance())) {
            isSame = false;
        }
        if (!travailleurEbu.getSexe().equals(travailleurExistant.getSexe())) {
            isSame = false;
        }

        if (!String.valueOf(travailleurEbu.getEtatCivil().getCodeSysteme()).equals(travailleurExistant.getEtatCivil())) {
            isSame = false;
        }

        if (travailleurExistant.getPays() == null
                || !travailleurEbu.getNationalite().equals(travailleurExistant.getPays().getCodeIso())) {
            isSame = false;
        }
        if (travailleurExistant.getAdressePrincipale() == null
                || !travailleurEbu.getAdresse().getRue().equals(travailleurExistant.getAdressePrincipale().getRue())) {
            isSame = false;
        }
        if (travailleurExistant.getAdressePrincipale() == null
                || !travailleurEbu.getAdresse().getRueNumero()
                        .equals(travailleurExistant.getAdressePrincipale().getRueNumero())) {
            isSame = false;
        }
        String npa = travailleurEbu.getAdresse().getNpa();
        if (npa != null && !JadeStringUtil.isEmpty(npa) && npa.length() > 4
                && "CH".equals(travailleurEbu.getAdresse().getPays())) {
            npa = npa.substring(0, 4);
        }
        if (travailleurExistant.getAdressePrincipale() == null
                || (npa != null && !npa.equals(travailleurExistant.getAdressePrincipale().getNpa()))) {
            isSame = false;
        }
        String casePostale = travailleurEbu.getAdresse().getCasePostale();
        if (casePostale == null || JadeStringUtil.isBlankOrZero(casePostale.trim())) {
            casePostale = "";
        }
        if (travailleurExistant.getAdressePrincipale() != null
                && !(travailleurExistant.getAdressePrincipale().getCasePostale() == null && JadeStringUtil
                        .isBlankOrZero(casePostale))) {
            if (!casePostale.equals(travailleurExistant.getAdressePrincipale().getCasePostale().trim())) {
                isSame = false;
            }
        }

        // Check Telephone
        HashMap<TypeContact, MoyenContact> mapContact = null;
        try {
            mapContact = VulpeculaRepositoryLocator.getContactRepository().findMoyenContactForIdTiers(
                    travailleurExistant.getIdTiers());
        } catch (Exception ex) {
        }

        String telephoneExistant = "";
        String telephoneExistantFormatted = "";
        if (mapContact != null) {
            MoyenContact moyen = mapContact.get(TypeContact.PRIVE);

            if (moyen != null && !JadeStringUtil.isEmpty(moyen.getValeur())) {
                telephoneExistant = moyen.getValeur();
                telephoneExistantFormatted = getDigitFromString(moyen.getValeur());
            }

            if (JadeStringUtil.isEmpty(travailleurEbu.getTelephone())) {
                travailleurEbu.setTelephone(telephoneExistant);
            }
        }

        if (!getDigitFromString(travailleurEbu.getTelephone()).equals(telephoneExistantFormatted)) {
            isSame = false;
        }

        if (isSame) {
            travailleurEbu.setTiersStatus(StatusEbu.NO_DIFF);
        } else {
            travailleurEbu.setTiersStatus(StatusEbu.A_TRAITER);
        }

    }

    public static String getDigitFromString(String stringToFormat) {
        return stringToFormat.replaceAll("\\D+", "");
    }

    private void hasAnnonce(TravailleurEbu travailleur) {
        boolean toOverwrite = true;
        TravailleurEbuDomain dejaAnnonce = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                .findByCorrelationId(travailleur.getCorrelationId());

        if (dejaAnnonce != null && !travailleur.getPosteCorrelationId().equals(dejaAnnonce.getPosteCorrelationId())) {
            toOverwrite = false;
        }

        if (dejaAnnonce != null && !dejaAnnonce.isTraite() && toOverwrite) {
            dejaAnnonce.setStatus(StatusAnnonceTravailleurEbu.AUTO);
            dejaAnnonce.setTiersStatus(StatusEbu.REFUSE);
            dejaAnnonce.setBanqueStatus(StatusEbu.REFUSE);
            dejaAnnonce.setTravailleurStatus(StatusEbu.REFUSE);
            dejaAnnonce.setPosteStatus(StatusEbu.REFUSE);
            if (travailleur.getId() != null && !JadeStringUtil.isBlankOrZero(travailleur.getId())) {
                try {
                    VulpeculaServiceLocator.getTravailleurService().notifierSynchronisationEbu(travailleur.getId(),
                            travailleur.getCorrelationId());
                } catch (JadePersistenceException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(dejaAnnonce);
        }
    }

    @Override
    public StatusAnnonceEbu annonceTravailleur(TravailleurEbu nouveauTravailleur) {
        String message = "";
        StatusAnnonceEbu status = new StatusAnnonceEbu();
        status.setReponse(StatusReponse.OK);

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        TravailleurEbuDomain newTravailleur = null;

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            checkTravailleurIntegrity(nouveauTravailleur, session);
            hasAnnonce(nouveauTravailleur);
            newTravailleur = new TravailleurEbuDomain(nouveauTravailleur);
            newTravailleur.setStatus(StatusAnnonceTravailleurEbu.EN_COURS);
            newTravailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().create(newTravailleur);
            setStatus(newTravailleur);
            ModificationTravailleurViewService.majStatusGlobal(newTravailleur.getId());
            if (newTravailleur.isTraite()) {
                newTravailleur.setStatus(StatusAnnonceTravailleurEbu.AUTO);
                VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(newTravailleur);
                try {
                    VulpeculaServiceLocator.getTravailleurService().notifierSynchroAnnonce(newTravailleur.getId(),
                            newTravailleur.getCorrelationId(), newTravailleur.getPosteCorrelationId());
                } catch (JadePersistenceException e) {
                    LOGGER.error(e.getMessage());
                }

            }

        } catch (SQLException e) {
            session.addError(e.getMessage());
            status.setReponse(StatusReponse.ERROR);
            message = e.getMessage();
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        status.setMessage(message);
        return status;
    }

    private void checkTravailleurIntegrity(TravailleurEbu nouveauTravailleur, BSession session) throws SQLException {
        Travailleur travailleurExistant = null;

        travailleurExistant = VulpeculaServiceLocator.getTravailleurService().findByNomPrenomDateNaissanceEmployeur(
                nouveauTravailleur.getNom(), nouveauTravailleur.getPrenom(), nouveauTravailleur.getDateNaissance(),
                nouveauTravailleur.getIdEmployeur());

        if (travailleurExistant != null) {
            throw new SQLException(session.getLabel("ERROR_TRAVAILLEUR_DEJA_EXISTANT"));
        }
    }

    private TravailleurEbu convertTravailleurToTravailleurEbu(Travailleur t, String idEmployeur, String synchroId,
            String idPosteTravail) {
        TravailleurEbuDomain travailleurEbuDomain = new TravailleurEbuDomain();
        TravailleurEbuDomain traEbuFromDB = null;
        PosteTravail poste = null;
        String posteCorrelationId = "";
        t = VulpeculaRepositoryLocator.getTravailleurRepository().findByIdTiers(t.getIdTiers());

        travailleurEbuDomain.setIdEmployeur(idEmployeur);
        travailleurEbuDomain.setSynchroId(synchroId);

        if (!JadeStringUtil.isEmpty(idPosteTravail) && !"0".equals(idPosteTravail)) {
            travailleurEbuDomain.setIdPosteTravail(idPosteTravail);
            poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdWithOccupations(idPosteTravail);
            if ((t.getCorrelationId() != null || !JadeStringUtil.isEmpty(t.getCorrelationId()))
                    && (poste.getPosteCorrelationId() != null || !JadeStringUtil.isEmpty(poste.getPosteCorrelationId()))) {
                traEbuFromDB = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                        .findByCorrelationIdAndPosteCorrelationId(t.getCorrelationId(), poste.getPosteCorrelationId());
                posteCorrelationId = poste.getPosteCorrelationId();
            }
        }

        if (traEbuFromDB == null && (t.getCorrelationId() != null || !JadeStringUtil.isEmpty(t.getCorrelationId()))) {
            traEbuFromDB = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findByCorrelationId(
                    t.getCorrelationId());
        }

        if (traEbuFromDB != null) {
            travailleurEbuDomain.setProfession(traEbuFromDB.getProfession());
            travailleurEbuDomain.setSalaire(traEbuFromDB.getSalaire());
            travailleurEbuDomain.setTypeSalaire(traEbuFromDB.getTypeSalaire());
            travailleurEbuDomain.setNombreEnfants(traEbuFromDB.getNombreEnfants());

        }
        if (!"0".equals(t.getId())) {
            travailleurEbuDomain.setIdTravailleur(t.getId());
        }
        travailleurEbuDomain.setCorrelationId(t.getCorrelationId());
        travailleurEbuDomain.setPosteCorrelationId(posteCorrelationId);
        travailleurEbuDomain.setNss(t.getNumAvsActuel());
        travailleurEbuDomain.setNom(t.getDesignation1());
        travailleurEbuDomain.setPrenom(t.getDesignation2());
        travailleurEbuDomain.setDateNaissance(t.getDateNaissance());
        travailleurEbuDomain.setSexe(t.getSexe());
        travailleurEbuDomain.setEtatCivil(EtatCivil.parse(t.getEtatCivil()));

        HashMap<TypeContact, MoyenContact> mapContact = null;
        try {
            mapContact = VulpeculaRepositoryLocator.getContactRepository().findMoyenContactForIdTiers(t.getIdTiers());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        String telephoneExistant = "";
        if (mapContact != null) {
            MoyenContact moyen = mapContact.get(TypeContact.PRIVE);

            if (moyen != null) {
                telephoneExistant = moyen.getValeur().replace(" ", "");
                if (!JadeStringUtil.isEmpty(telephoneExistant) && telephoneExistant.length() > 5) {
                    telephoneExistant = telephoneExistant.substring(0, 3) + "/"
                            + telephoneExistant.substring(3, telephoneExistant.length());
                }
            }
        }
        if (JadeStringUtil.isEmpty(travailleurEbuDomain.getTelephone())) {
            travailleurEbuDomain.setTelephone(telephoneExistant);
        }

        if (t.getPays() != null) {
            travailleurEbuDomain.setNationalite(t.getPays().getCodeIso());
        }

        Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                t.getIdTiers());

        if (adresse != null) {
            AdresseEbu adresseEbu = new AdresseEbu();

            adresseEbu.setRue(adresse.getRue());
            adresseEbu.setRueNumero(adresse.getRueNumero());
            adresseEbu.setNpa(adresse.getNpa());
            adresseEbu.setLocalite(adresse.getLocalite());
            adresseEbu.setIdLocalite(adresse.getIdLocalite());
            adresseEbu.setCasePostale(adresse.getCasePostale());

            if (adresse.getPays() != null) {
                adresseEbu.setPays(adresse.getIsoPays());
            }
            travailleurEbuDomain.setAdresse(adresseEbu);
        }

        if (!JadeStringUtil.isEmpty(t.getIdTiers())) {
            String iban = VulpeculaRepositoryLocator.getAdresseRepository().findIbanByIdTiers(t.getIdTiers(),
                    new Date());
            String nomBanque = VulpeculaRepositoryLocator.getAdresseRepository().findNomBanqueByIdTiers(t.getIdTiers(),
                    new Date());
            String localite = VulpeculaRepositoryLocator.getAdresseRepository().findLocaliteBanqueByIdTiers(
                    t.getIdTiers(), new Date());
            String idLocalite = "";
            if (!JadeStringUtil.isEmpty(nomBanque) && !JadeStringUtil.isEmpty(localite)) {
                idLocalite = VulpeculaRepositoryLocator.getAdresseRepository().findIdLocaliteBanqueByIdTiers(
                        t.getIdTiers(), new Date());
            }

            CompteBanquaireEbu compte = new CompteBanquaireEbu();
            compte.setIban(iban);
            compte.setNom(nomBanque);
            compte.setLocalite(localite);
            compte.setIdLocalite(idLocalite);
            travailleurEbuDomain.setCompteBancaire(compte);
        }

        if (poste != null) {
            travailleurEbuDomain.setCodeProfessionnel(poste.getQualification());
            travailleurEbuDomain.setDateDebutActivite(poste.getDebutActiviteAsSwissValue());
            travailleurEbuDomain.setTypeSalaire(poste.getTypeSalaire());
            travailleurEbuDomain.setTauxActivite(Double.parseDouble(poste.getOccupationActuel().getTaux().getValue()));
            travailleurEbuDomain.setDateTauxActivite(poste.getOccupationActuel().getDateValiditeAsValue());
            if (t.getPermisTravail() != null) {
                travailleurEbuDomain.setPermisSejour(new PermisTravailEbu(t.getPermisTravail().getValue(), t
                        .getReferencePermis()));
            }

            if (poste.getFinActiviteAsSwissValue() != null && !poste.getFinActiviteAsSwissValue().isEmpty()) {
                travailleurEbuDomain.setDateFinActivite(poste.getFinActiviteAsSwissValue());
                travailleurEbuDomain.setDureeContrat(new DureeContratEbu(true, poste.getFinActiviteAsSwissValue()));

            } else {
                travailleurEbuDomain.setDateFinActivite("");
                travailleurEbuDomain.setDureeContrat(new DureeContratEbu(false));
            }
            AffiliationCaisseMaladie affCaisse = VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository()
                    .findActifByIdPosteTravail(poste.getId());
            if (affCaisse != null) {
                AssuranceMaladieEbu assureur = AssuranceMaladieEbu.getFromMetierId(affCaisse.getIdCaisseMaladie());
                ContratAssuranceMaladieEbu contrat = new ContratAssuranceMaladieEbu();
                contrat.setAssureur(assureur);
                contrat.setAssuranceCombinee(false);
                travailleurEbuDomain.setContratCollectif(contrat);
            }
        }

        return new TravailleurEbu(travailleurEbuDomain);

    }

    private List<SynchronisationTravailleur> removeDuplicates(List<SynchronisationTravailleur> list) {
        List<String> allreadyIn = new ArrayList<String>();
        List<SynchronisationTravailleur> toReturn = new ArrayList<SynchronisationTravailleur>();
        for (SynchronisationTravailleur sync : list) {
            if (!allreadyIn.contains(sync.getIdAnnonce())) {
                toReturn.add(sync);
            }
            allreadyIn.add(sync.getIdAnnonce());
        }
        return toReturn;
    }

    @Override
    public List<TravailleurEbu> listTravailleur(String idEmployeur, boolean synchronize) {
        if (!synchronize) {
            Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
        }
        List<TravailleurEbu> travailleurs = new ArrayList<TravailleurEbu>();

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            if (synchronize) {
                Map<String, SynchronisationTravailleur> listeSynchro = VulpeculaRepositoryLocator
                        .getSynchronisationTravailleurEbuRepository().findTravailleurToSynchronize(idEmployeur);
                List<SynchronisationTravailleur> listeSynchroRefused = VulpeculaRepositoryLocator
                        .getSynchronisationTravailleurEbuRepository().findRefusedTravailleurToSynchronize(idEmployeur);
                listeSynchroRefused = removeDuplicates(listeSynchroRefused);

                for (Map.Entry<String, SynchronisationTravailleur> synchronisation : listeSynchro.entrySet()) {
                    List<PosteTravail> listPoste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                            .findListByTravailleurEtEmployeur(synchronisation.getValue().getTravailleur().getId(),
                                    idEmployeur);
                    for (PosteTravail poste : listPoste) {
                        Travailleur travailleurToAdd = synchronisation.getValue().getTravailleur();
                        travailleurToAdd.setCorrelationId(synchronisation.getValue().getCorrelationId());
                        TravailleurEbu travEbu = convertTravailleurToTravailleurEbu(travailleurToAdd, idEmployeur,
                                synchronisation.getValue().getId(), poste.getId());
                        if (!JadeStringUtil.isBlank(poste.getPosteCorrelationId())) {
                            travEbu.setPosteCorrelationId(poste.getPosteCorrelationId());
                        }
                        travEbu.setStatus(StatusAnnonceTravailleurEbu.TRAITE);
                        travailleurs.add(travEbu);
                    }
                }

                for (SynchronisationTravailleur syncroRefused : listeSynchroRefused) {
                    TravailleurEbuDomain trav = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                            syncroRefused.getIdAnnonce());
                    Travailleur travWM;
                    if (trav != null && !JadeStringUtil.isEmpty(trav.getIdTravailleur())) {
                        travWM = VulpeculaServiceLocator.getTravailleurService().findById(trav.getIdTravailleur());
                    } else {
                        travWM = VulpeculaServiceLocator.getTravailleurService().findByNomPrenomDateNaissanceEmployeur(
                                trav.getNom(), trav.getPrenom(), trav.getDateNaissance(), trav.getIdEmployeur());
                    }
                    TravailleurEbu containerTrav;
                    if (travWM != null) {
                        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                                .findByTravailleurEtEmployeurEtPosteCorrelationId(travWM.getId(),
                                        trav.getIdEmployeur(), trav.getPosteCorrelationId());
                        if (poste == null) {
                            List<Qualification> qualifications = new ArrayList<Qualification>();
                            qualifications.add(trav.getCodeProfessionnel());
                            poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                                    .findByTravailleurEmployeurEtQualification(travWM.getId(), trav.getIdEmployeur(),
                                            qualifications);
                        }

                        if (poste != null) {
                            containerTrav = convertTravailleurToTravailleurEbu(travWM, trav.getIdEmployeur(),
                                    syncroRefused.getId(), poste.getId());
                            containerTrav.setIdPosteTravail(poste.getId());
                        } else {
                            containerTrav = convertTravailleurToTravailleurEbu(travWM, trav.getIdEmployeur(),
                                    syncroRefused.getId(), null);
                            containerTrav.setIdPosteTravail("");
                        }
                        if (!JadeStringUtil.isBlank(syncroRefused.getCorrelationId())) {
                            containerTrav.setCorrelationId(syncroRefused.getCorrelationId());
                        }
                        if (!JadeStringUtil.isBlank(syncroRefused.getPosteCorrelationId())) {
                            containerTrav.setPosteCorrelationId(syncroRefused.getPosteCorrelationId());
                            // If postecorrelation id setted, get posteId if not is a refuse of new poste
                            poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findByPosteCorrelationId(
                                    syncroRefused.getPosteCorrelationId());
                            if (poste != null) {
                                containerTrav.setIdPosteTravail(poste.getId());
                            } else {
                                containerTrav.setIdPosteTravail("");
                            }
                        }
                    } else {
                        trav.setSynchroId(syncroRefused.getId());
                        trav = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(trav);
                        if (!JadeStringUtil.isBlank(syncroRefused.getCorrelationId())) {
                            trav.setCorrelationId(syncroRefused.getCorrelationId());
                        }
                        if (!JadeStringUtil.isBlank(syncroRefused.getPosteCorrelationId())) {
                            trav.setPosteCorrelationId(syncroRefused.getPosteCorrelationId());
                        }
                        containerTrav = new TravailleurEbu(trav);
                    }

                    if (trav.getStatus() == StatusAnnonceTravailleurEbu.AUTO) {
                        containerTrav.setStatus(StatusAnnonceTravailleurEbu.TRAITE);
                    } else {
                        containerTrav.setStatus(StatusAnnonceTravailleurEbu.REFUSE);
                    }
                    travailleurs.add(containerTrav);

                }

            } else {

                List<PosteTravail> listePostes = VulpeculaRepositoryLocator.getPosteTravailRepository()
                        .findByIdEmployeur(idEmployeur);

                for (PosteTravail posteTravail : listePostes) {
                    travailleurs.add(convertTravailleurToTravailleurEbu(posteTravail.getTravailleur(), idEmployeur,
                            null, posteTravail.getId()));
                }
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return travailleurs;
    }

    @Override
    public StatusAnnonceEbu modifierTravailleur(TravailleurEbu travailleur) {
        String message = "";
        StatusAnnonceEbu status = new StatusAnnonceEbu();
        status.setReponse(StatusReponse.OK);

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        TravailleurEbuDomain newTravailleur = null;

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            hasAnnonce(travailleur);
            newTravailleur = new TravailleurEbuDomain(travailleur);
            newTravailleur.setStatus(StatusAnnonceTravailleurEbu.EN_COURS);
            newTravailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().create(newTravailleur);

            setStatus(newTravailleur);
            updateExternalIds(travailleur, session, threadContext);

            // Retrieve pour vérifier si setStatus() n'a pas trouvé de modification, return status si aucune
            // modification détectée
            newTravailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(
                    newTravailleur.getId());
            ModificationTravailleurViewService.majStatusGlobal(newTravailleur.getId());
            if (newTravailleur.isTraite()) {
                newTravailleur.setStatus(StatusAnnonceTravailleurEbu.AUTO);
                VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(newTravailleur);
                try {
                    VulpeculaServiceLocator.getTravailleurService().notifierSynchroAnnonce(newTravailleur.getId(),
                            newTravailleur.getCorrelationId(), newTravailleur.getPosteCorrelationId());
                } catch (JadePersistenceException e) {
                    LOGGER.error(e.getMessage());
                }
            }

        } catch (SQLException e) {
            session.addError(e.getMessage());
            status.setReponse(StatusReponse.ERROR);
            message = e.getMessage();
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        status.setMessage(message);
        return status;
    }

    private void updateExternalIds(TravailleurEbu travailleur, BSession session, JadeThreadContext threadContext) {
        if (!JadeStringUtil.isBlankOrZero(travailleur.getIdPosteTravail())) {
            setPosteCorrelationId(travailleur.getIdPosteTravail(), travailleur.getPosteCorrelationId(), session,
                    threadContext);
        }
        if (!JadeStringUtil.isBlankOrZero(travailleur.getIdTravailleur())) {
            setCorrelationId(travailleur.getIdTravailleur(), travailleur.getCorrelationId(), session, threadContext);
        }

    }

    private void setPosteCorrelationId(String posteId, String posteCorrelationId, BSession session,
            JadeThreadContext threadContext) {
        String query = "UPDATE SCHEMA.PT_POSTES_TRAVAILS SET POSTE_CORRELATION_ID='" + posteCorrelationId
                + "' WHERE ID = " + posteId;
        excecuteQuery(query, session, threadContext);
    }

    private void setCorrelationId(String travailleurId, String correlationId, BSession session,
            JadeThreadContext threadContext) {
        String query = "UPDATE SCHEMA.PT_TRAVAILLEURS SET CORRELATION_ID='" + correlationId + "' WHERE ID = "
                + travailleurId;
        excecuteQuery(query, session, threadContext);
    }

    private void excecuteQuery(String query, BSession session, JadeThreadContext threadContext) {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            QueryUpdateExecutor.executeUpdate(query, this.getClass(), session);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        }
    }

    @Override
    public StatusAnnonceEbu ackSyncTravailleurs(List<String> idsTableSynchro) {
        String message = "";
        StatusAnnonceEbu status = new StatusAnnonceEbu();
        status.setReponse(StatusReponse.OK);
        List<String> idsWithoutDuplicate = new ArrayList<String>(new LinkedHashSet<String>(idsTableSynchro));

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            VulpeculaServiceLocator.getTravailleurService().ackSyncTravailleurs(idsWithoutDuplicate);
        } catch (SQLException e) {
            session.addError(e.getMessage());
            status.setReponse(StatusReponse.ERROR);
            message = e.getMessage();
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        status.setMessage(message);
        return status;
    }

    private String getQueryLocalite() {
        return "SELECT loc.HJILOC AS id, loc.HJNPA AS npa, loc.HJLOCC AS libelle_Court, loc.HJLOCA AS libelle_Long,"
                + " canton.PCOUID AS code_Canton, canton.PCOLUT AS libelle_Canton_Fr, cantond.PCOLUT AS libelle_Canton_All,"
                + "pays.HNIPAY AS code_Pays, pays.HNCISO AS code_Pays_Iso ,pays.HNLFR AS libelle_Pays_Fr, pays.HNLAL AS libelle_Pays_All "
                + "FROM SCHEMA.TILOCAP loc " + "INNER JOIN SCHEMA.TIPAYSP pays ON loc.HNIPAY=pays.HNIPAY "
                + "INNER JOIN SCHEMA.fwcoup canton ON canton.PCOSID=loc.HJICAN AND canton.plaide='F' "
                + "INNER JOIN SCHEMA.fwcoup cantond ON cantond.PCOSID=loc.HJICAN AND cantond.plaide='D'";
    }

    @Override
    public List<LocaliteEbu> getListLocalite() {

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        List<LocaliteEbu> list = new ArrayList<LocaliteEbu>();

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            list = QueryExecutor.execute(getQueryLocalite(), LocaliteEbu.class);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return list;
    }
}
