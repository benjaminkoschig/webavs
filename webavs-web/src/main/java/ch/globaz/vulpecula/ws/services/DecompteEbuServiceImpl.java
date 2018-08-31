package ch.globaz.vulpecula.ws.services;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.client.JadePublishDocument;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.jade.noteIt.NoteException;
import ch.globaz.jade.noteIt.SimpleNote;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.decompte.GenererDecompteProcessor;
import ch.globaz.vulpecula.documents.decompte.CotisationsInfo;
import ch.globaz.vulpecula.documents.decompte.DecompteContainer;
import ch.globaz.vulpecula.documents.decompte.DocumentDecompteBVRPrinter;
import ch.globaz.vulpecula.documents.decompte.DocumentDecompteVidePrinter;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAbsence;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.ebusiness.Synchronisation;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteInfo;
import ch.globaz.vulpecula.services.PTAFServices;
import ch.globaz.vulpecula.ws.bean.DecompteComplementaireEbu;
import ch.globaz.vulpecula.ws.bean.DecomptePeriodiqueEbu;
import ch.globaz.vulpecula.ws.bean.DecompteSpecialEbu;
import ch.globaz.vulpecula.ws.bean.LigneDecompteComplementaireEbu;
import ch.globaz.vulpecula.ws.bean.LigneDecomptePeriodiqueEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceEbu;
import ch.globaz.vulpecula.ws.bean.StatusLine;
import ch.globaz.vulpecula.ws.bean.StatusReponse;
import ch.globaz.vulpecula.ws.utils.UtilsService;

/**
 * @since eBMS 1.0
 */
@WebService(endpointInterface = "ch.globaz.vulpecula.ws.services.DecompteEbuService")
public class DecompteEbuServiceImpl extends VulpeculaAbstractService implements DecompteEbuService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    // TODO ne gère pas bien quand il y a plusieurs codes erreurs
    EtatDecompte etatDecompte = EtatDecompte.VALIDE;
    static final int MAX_SIZE_REMARQUE = 250;

    /**
     * liste les décomptes
     */
    @Override
    public List<DecomptePeriodiqueEbu> listDecomptes(String idEmployeur, String yearsMonthFrom, String yearsMonthTo,
            String status, boolean synchronize) {
        if (synchronize) {
            List<DecomptePeriodiqueEbu> listeDecompteEbu = new ArrayList<DecomptePeriodiqueEbu>();
            // Récupération d'une session
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);

            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                Map<String, Synchronisation> listeSynchro = VulpeculaRepositoryLocator.getSynchronisationRepository()
                        .findDecompteToSynchronize(idEmployeur, yearsMonthFrom, yearsMonthTo, status);

                for (Map.Entry<String, Synchronisation> synchronisation : listeSynchro.entrySet()) {
                    Decompte decompteToAdd = synchronisation.getValue().getDecompte();
                    decompteToAdd.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                            .findByIdDecompteWithDependencies(decompteToAdd.getId()));
                    boolean isToComptaOuValide = true;
                    if (decompteToAdd.isTaxationOffice()) {
                        isToComptaOuValide = isTOComptabiliseOuValide(decompteToAdd);
                    }
                    if (decompteToAdd.getType() == TypeDecompte.PERIODIQUE && isToComptaOuValide) {
                        listeDecompteEbu.add(convertDecompteForEbu(decompteToAdd, synchronisation.getValue().getId()));
                    }
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                session.addError(e.getMessage());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                session.addError(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

            return listeDecompteEbu;
        } else {
            // Validation input data
            Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
            EtatDecompte etat = null;

            // Ce test est-il utile? Les etatsEbu retournant plusieurs etatDecompte ne sont pas vu comme valide
            // if (!status.isEmpty()) {
            // Validate.isTrue(EtatDecompte.isValid(EtatDecompte.valueOf(status).getValue()), "Status is invalid !");
            // etat = EtatDecompte.valueOf(status);
            // }

            List<DecomptePeriodiqueEbu> listeDecompteEbu = new ArrayList<DecomptePeriodiqueEbu>();
            // Récupération d'une session
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);
            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

                // load decompte
                List<Decompte> listeDecompte = getDecompteFromEbuStatus(status, threadContext, idEmployeur,
                        yearsMonthFrom, yearsMonthTo);

                // if (listeDecompte == null || listeDecompte.isEmpty()) {
                // // Créer le décompte
                // listeDecompte = createDecompte(idEmployeur, yearsMonthFrom, yearsMonthTo);
                // }

                for (Decompte decompte : listeDecompte) {
                    boolean isToComptaOuValide = true;
                    if (decompte.isTaxationOffice()) {
                        isToComptaOuValide = isTOComptabiliseOuValide(decompte);
                    }
                    if (decompte.getType() == TypeDecompte.PERIODIQUE && isToComptaOuValide) {
                        listeDecompteEbu.add(convertDecompteForEbu(decompte, ""));
                    }
                }
            } catch (SQLException e) {
                session.addError(e.getMessage());
                LOGGER.error(e.getMessage());
            } catch (Exception e) {
                session.addError(e.getMessage());
                LOGGER.error(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

            return listeDecompteEbu;
        }
    }

    @Override
    public List<DecompteSpecialEbu> listDecomptesSpecial(String idEmployeur, String year, String status,
            boolean synchronize) {
        // VALIDATE PARAM
        Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
        Validate.notEmpty(year, "Year must not be empty !");
        Validate.isTrue(Integer.parseInt(year) > 0, "Year must be an integer greater than zero.");
        Validate.isTrue(year.trim().length() == 4, "Year must be in format YYYY !");

        Date yearsMonthFromDate = Date.getFirstDayOfYear(Integer.parseInt(year));
        Date yearsMonthToDate = Date.getLastDayOfYear(Integer.parseInt(year));
        String yearsMonthFrom = yearsMonthFromDate.getAnneeMois();
        String yearsMonthTo = yearsMonthToDate.getAnneeMois();
        if (synchronize) {
            List<DecompteSpecialEbu> listeDecompteSpecialEbu = new ArrayList<DecompteSpecialEbu>();
            // Récupération d'une session
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);

            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);
                if (new Date(employeur.getDateDebut()).after(yearsMonthFromDate)) {
                    yearsMonthFromDate = new Date(employeur.getDateDebut());
                    yearsMonthFrom = yearsMonthFromDate.getAnneeMois();
                }
                Map<String, Synchronisation> listeSynchro = VulpeculaRepositoryLocator.getSynchronisationRepository()
                        .findDecompteToSynchronize(idEmployeur, yearsMonthFrom, yearsMonthTo, status);

                for (Map.Entry<String, Synchronisation> synchronisation : listeSynchro.entrySet()) {
                    Decompte decompteToAdd = synchronisation.getValue().getDecompte();
                    boolean isToComptaOuValide = true;
                    if (decompteToAdd.isTaxationOffice()) {
                        isToComptaOuValide = isTOComptabiliseOuValide(decompteToAdd);
                    }
                    if (decompteToAdd.getType().isTraiterAsSpecial()
                            && (isToComptaOuValide && decompteToAdd.getEtat() == EtatDecompte.COMPTABILISE)) {
                        listeDecompteSpecialEbu.add(convertDecompteSpecialForEbu(decompteToAdd, synchronisation
                                .getValue().getId()));
                    }
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                session.addError(e.getMessage());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                session.addError(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

            return listeDecompteSpecialEbu;
        } else {
            // Validation input data
            Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");

            List<DecompteSpecialEbu> listeDecompteSpecialEbu = new ArrayList<DecompteSpecialEbu>();
            // Récupération d'une session
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);
            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

                // load decompte
                List<Decompte> listeDecompte = getDecompteFromEbuStatus(status, threadContext, idEmployeur,
                        yearsMonthFrom, yearsMonthTo);

                for (Decompte decompte : listeDecompte) {
                    boolean isToComptaOuValide = true;
                    if (decompte.isTaxationOffice()) {
                        isToComptaOuValide = isTOComptabiliseOuValide(decompte);
                    }
                    if (decompte.getType().isTraiterAsSpecial()
                            && (isToComptaOuValide && decompte.getEtat() == EtatDecompte.COMPTABILISE)) {
                        listeDecompteSpecialEbu.add(convertDecompteSpecialForEbu(decompte, ""));
                    }
                }
            } catch (SQLException e) {
                session.addError(e.getMessage());
                LOGGER.error(e.getMessage());
            } catch (Exception e) {
                session.addError(e.getMessage());
                LOGGER.error(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

            return listeDecompteSpecialEbu;
        }
    }

    private List<Decompte> getDecompteFromEbuStatus(String statusEbu, JadeThreadContext threadContext,
            String idEmployeur, String yearsMonthFrom, String yearsMonthTo) {
        List<Decompte> listeDecompte = new ArrayList<Decompte>();
        if (statusEbu == null || statusEbu.isEmpty()) {
            listeDecompte = VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo);
        }
        if ("OUVERT".equals(statusEbu)) {
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.GENERE));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.OUVERT));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.SOMMATION));
        }
        if ("EN_COURS".equals(statusEbu)) {
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.A_TRAITER));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.ERREUR));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.RECEPTIONNE));
        }
        if ("TERMINE".equals(statusEbu)) {
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.COMPTABILISE));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.FACTURATION));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.RECTIFIE));
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.VALIDE));
        }
        if ("ANNULE".equals(statusEbu)) {
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.ANNULE));
        }
        if ("TAXATION_D_OFFICE".equals(statusEbu)) {
            listeDecompte.addAll(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdEmployeurAndPeriodeWithDependencies(idEmployeur, yearsMonthFrom, yearsMonthTo,
                            EtatDecompte.TAXATION_DOFFICE));
        }
        return listeDecompte;
    }

    private boolean isTOComptabiliseOuValide(Decompte decompte) {
        TaxationOffice to = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findByIdDecompte(decompte.getId());
        return EtatTaxation.COMPTABILISE.equals(to.getEtat()) || EtatTaxation.VALIDE.equals(to.getEtat());
    }

    @Override
    public DecomptePeriodiqueEbu getDecompte(String idDecompte) {
        Decompte decompte = null;
        DecomptePeriodiqueEbu decompteToReturn = new DecomptePeriodiqueEbu();
        // Validation input data
        Validate.notEmpty(idDecompte.trim(), "idDecompte must not be empty !");

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // load decompte
            decompte = VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(idDecompte);

            if (decompte != null) {
                decompteToReturn = convertDecompteForEbu(decompte, "");
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        Validate.isTrue(decompte != null && TypeDecompte.PERIODIQUE.equals(decompte.getType()),
                "Le décompte est null ou n'est pas un périodique !");

        return decompteToReturn;

    }

    /**
     * Met à jour les décomptes
     * 
     * @throws Exception
     */
    @Override
    public DecomptePeriodiqueEbu updateDecompte(DecomptePeriodiqueEbu decompteEbu) {
        Validate.notNull(decompteEbu, "Decompte is null !");
        Validate.notEmpty(decompteEbu.getIdDecompte(), "Id decompte is empty !");

        double MNT_10 = 10.00;
        double MNT_1500 = 1500.00;

        DecomptePeriodiqueEbu decompteEbuRetour = null;
        etatDecompte = EtatDecompte.VALIDE;
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository()
                    .findById(decompteEbu.getIdDecompte());
            String idEmployeur = decompte.getEmployeur().getId();

            Validate.isTrue(decompte.isEditable() && !EtatDecompte.A_TRAITER.equals(decompte.getEtat()),
                    "Le décompte dois être éditable! Statut décompte: " + decompte.getEtat());
            if (decompteEbu.getDecompteLines() == null) {
                List<LigneDecomptePeriodiqueEbu> listeVide = new ArrayList<LigneDecomptePeriodiqueEbu>();
                decompteEbu.setDecompteLines(listeVide);
            }
            for (LigneDecomptePeriodiqueEbu line : decompteEbu.getDecompteLines()) {
                boolean creation = false;
                if (StatusLine.UNCHANGED.equals(line.getStatus())) {
                    continue;
                }

                String lineCorrelationId = line.getLineCorrelationId();

                StatusLine statusLine = StatusLine.UNCHANGED;
                DecompteSalaire decompteSalaire = null;

                if (StatusLine.TO_ADD.equals(line.getStatus())) {
                    etatDecompte = EtatDecompte.A_TRAITER;

                    // Verifie qu'on a le minimum des infos nécessaire
                    if (line.getPosteTravailEbu() == null) {
                        line.setStatus(StatusLine.ERROR);
                        continue;
                    }
                    PosteTravail pt = null;
                    TravailleurEbuDomain travEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                            .findByCorrelationIdAndPosteCorrelationId(line.getPosteTravailEbu().getCorrelationId(),
                                    line.getPosteTravailEbu().getPosteCorrelationId());

                    if (travEbu != null) {
                        List<Qualification> qualifications = new ArrayList<Qualification>();
                        qualifications.add(travEbu.getCodeProfessionnel());
                        pt = VulpeculaRepositoryLocator.getPosteTravailRepository()
                                .findByTravailleurEtEmployeurEtPosteCorrelationId(
                                        line.getPosteTravailEbu().getIdTravailleur(), idEmployeur,
                                        line.getPosteTravailEbu().getPosteCorrelationId());
                    } else {
                        if (!JadeStringUtil.isEmpty(line.getPosteTravailEbu().getIdPosteTravail())) {
                            pt = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                                    line.getPosteTravailEbu().getIdPosteTravail());
                        }
                    }

                    if (pt != null && !pt.isActif(new Periode(line.getPeriodeDebut(), line.getPeriodeFin()))) {
                        // Reset le poste to null si inactif => signifiant reactivation non-quittancée
                        pt = null;
                    }

                    if (pt == null || JadeStringUtil.isEmpty(line.getPosteTravailEbu().getIdTravailleur())) {
                        if (line.getPosteTravailEbu().getCorrelationId() == null
                                || line.getPosteTravailEbu().getCorrelationId().isEmpty()) {
                            continue;
                        } else {
                            // Nouveau travailleur
                            decompteSalaire = addNewDecompteSalaireNewTravailleur(line, decompte, lineCorrelationId);
                        }
                    } else {
                        // travailleur existant
                        creation = true;
                        decompteSalaire = addNewDecompteSalaire(line, decompte, pt, lineCorrelationId);
                    }

                    statusLine = StatusLine.ADDED;

                } else if (StatusLine.TO_DELETE_PENDING.equals(line.getStatus())) {
                    etatDecompte = EtatDecompte.A_TRAITER;
                    decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                            line.getIdDecompteLine());
                    statusLine = StatusLine.TO_DELETE_PENDING;

                } else if (StatusLine.TO_MODIFY.equals(line.getStatus())) {
                    if (line.getIdDecompteLine() == null || line.getIdDecompteLine().isEmpty()) {
                        line.setStatus(StatusLine.ERROR);
                        continue;
                    }

                    // Reporte données calculées
                    decompteSalaire = checkForCodesErreur(line);
                    statusLine = StatusLine.MODIFIED;

                } else if (StatusLine.TO_DELETE.equals(line.getStatus())) {
                    decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                            line.getIdDecompteLine());
                    decompte.getLignes().remove(decompteSalaire);
                    VulpeculaServiceLocator.getDecompteSalaireService().delete(decompteSalaire);
                    continue;
                }

                // Vérifie si le travailleur est retraiter
                if (decompteSalaire != null) {

                    if (decompteSalaire.getIdPosteTravail() != null
                            && decompteSalaire.isTravailleurRetraiter(decompteSalaire.getDecompte().getPeriodeFin())) {
                        decompteSalaire.addCodeErreur(CodeErreur.RETRAITE);
                        etatDecompte = EtatDecompte.A_TRAITER;
                    }
                    if (line.getAbsences().hasAbsence()) {
                        decompteSalaire.addCodeErreur(CodeErreur.CODE_ABSENCE);
                        etatDecompte = EtatDecompte.A_TRAITER;
                        if (decompteSalaire.getPosteTravail() != null && decompteSalaire.getPosteTravail().isMensuel()) {
                            decompteSalaire.setHeures(new Double("0.00"));
                        }
                    }

                    // On vérifie que le poste de travail est dans la période du décompte salaire
                    if (decompteSalaire.getIdPosteTravail() != null
                            && !JadeStringUtil.isEmpty(decompteSalaire.getIdPosteTravail())) {
                        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                                decompteSalaire.getIdPosteTravail());

                        // La ligne suivant à supprimer si le contrôle des période fonctionne
                        decompteSalaire.setPeriode(new Periode(decompteSalaire.getPeriodeDebut(), decompteSalaire
                                .getPeriodeFin()));

                        Periode periodePoste;
                        if (poste.getFinActivite() != null
                                && JadeStringUtil.isBlankOrZero(poste.getFinActiviteAsSwissValue())) {
                            periodePoste = new Periode(poste.getDebutActivite().getFirstDayOfMonth(),
                                    poste.getFinActivite());
                        } else {
                            periodePoste = new Periode(poste.getDebutActivite().getFirstDayOfMonth());
                        }

                        if (!periodePoste.contains(new Periode(decompteSalaire.getPeriodeDebutDecompte(),
                                decompteSalaire.getPeriodeFinDecompte()))) {
                            decompteSalaire.addCodeErreur(CodeErreur.HORS_PERIODE);
                            etatDecompte = EtatDecompte.A_TRAITER;
                        }

                        // On regarde si le travailleur possède un post-it
                        // "56788
                        if (decompteSalaire.getIdTravailleur() != null) {
                            if (VulpeculaServiceLocator.getPosteTravailService().travailleurHasPostIt(
                                    decompteSalaire.getIdTravailleur())) {
                                List<SimpleNote> postits = VulpeculaServiceLocator.getPosteTravailService().getPostIt(
                                        decompteSalaire.getIdTravailleur());
                                int countFound = 0;
                                int countElse = 0;
                                for (SimpleNote postit : postits) {
                                    if (!postit.getDescription().startsWith("*")) {
                                        countElse++;
                                    } else {
                                        countFound++;
                                    }
                                }
                                if (!(countFound > 0 && countElse == 0)) {
                                    decompteSalaire.addCodeErreur(CodeErreur.POST_IT);
                                    etatDecompte = EtatDecompte.A_TRAITER;
                                }
                            }
                        }

                        // on regarde si le taux ebusiness correspond au taux BMS
                        if (decompteSalaire.getIdPosteTravail() != null) {
                            if (!decompteSalaire.getTauxContribuableForCaissesSociales(true).equals(
                                    new Taux(line.getTauxContribution()))) {
                                decompteSalaire.addCodeErreur(CodeErreur.TAUX_DIFFERENT);
                                etatDecompte = EtatDecompte.A_TRAITER;
                            }
                        }

                        PosteTravail pt = decompteSalaire.getPosteTravail();
                        DecompteSalaire decomptePrecedent = null;

                        // Supression du test sur les postes soumis CTT, confirmation téléphonique (BMS-Delphine)
                        // BMS-3669
                        if (!ligneSupprimeExists(decompteSalaire.getListeCodeErreur())) {
                            decomptePrecedent = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                                    .findPrecedentComptabilise(pt.getId(),
                                            decompteSalaire.getDecompte().getPeriodeDebut());

                            if (decomptePrecedent != null && !StatusLine.TO_DELETE.equals(line.getStatus())) {
                                if (pt.getTypeSalaire().equals(TypeSalaire.HEURES)
                                        || pt.getTypeSalaire().equals(TypeSalaire.CONSTANT)) {

                                    double salaireHorairePrecedent = Double.parseDouble(decomptePrecedent
                                            .getSalaireHoraireAsValue());
                                    double salaireHoraireActuel = Double.parseDouble(decompteSalaire
                                            .getSalaireHoraireAsValue());

                                    if (salaireHoraireActuel >= (salaireHorairePrecedent + MNT_10)
                                            || salaireHoraireActuel <= (salaireHorairePrecedent - MNT_10)) {
                                        decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_CHANGE);
                                        etatDecompte = EtatDecompte.A_TRAITER;
                                    }

                                } else {

                                    decomptePrecedent.getPeriode();
                                    double nbMonthPeriod = (Periode.getNombreMois(decomptePrecedent.getPeriodeDebut(),
                                            decomptePrecedent.getPeriodeFin())) + 1.00;
                                    double salaireTotalPrecedent = Double.parseDouble(decomptePrecedent
                                            .getSalaireTotalAsValue());
                                    if (nbMonthPeriod > 1) {
                                        salaireTotalPrecedent = salaireTotalPrecedent / nbMonthPeriod;
                                    }

                                    double salaireTotalActuel = Double.parseDouble(decompteSalaire
                                            .getSalaireTotalAsValue());

                                    if (salaireTotalActuel >= (salaireTotalPrecedent + MNT_1500)
                                            || salaireTotalActuel <= (salaireTotalPrecedent - MNT_1500)) {
                                        decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_CHANGE);
                                        etatDecompte = EtatDecompte.A_TRAITER;
                                    }
                                }
                            }
                        }
                    }

                    if (decompteSalaire.isaTraiter() && decompteSalaire.getListeCodeErreur().isEmpty()) {
                        decompteSalaire.setaTraiter(false);
                    }

                    if (decompteSalaire.getPosteTravail() != null && decompteSalaire.getPosteTravail().isMensuel()) {
                        decompteSalaire.setHeures(VulpeculaServiceLocator.getPosteTravailService()
                                .getNombreHeuresParMois(decompteSalaire.getPosteTravail().getId(), new Date()));
                    }

                    // Met à jour la ligne
                    if (decompteSalaire.getPosteTravail() != null || decompteSalaire.getIdPosteTravail() != null) {
                        if (creation) {
                            decompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().create(
                                    decompteSalaire);
                        } else {
                            decompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().update(
                                    decompteSalaire);
                        }
                    }
                    line.updateNoSynchro(decompteSalaire, statusLine);
                }
            }

            decompte.setTypeProvenance(TypeProvenance.EBUSINESS);
            decompte.setDateReception(Date.now());
            VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

            updateDecompteToReceptionne(decompteEbu.getIdDecompte(), etatDecompte);
            decompteEbuRetour = convertDecompteForEbu(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdWithDependencies(decompteEbu.getIdDecompte()), "");

            if (etatDecompte.equals(EtatDecompte.VALIDE)) {
                PTAFServices.genererPrestationsAFPeriodique(decompte.getEmployeur(), decompte.getPeriode(),
                        decompte.getType());
            }

        } catch (UnsatisfiedSpecificationException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (SQLException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (NoteException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return decompteEbuRetour;
    }

    private boolean checkIf2Lines(LigneDecompteComplementaireEbu line) {

        boolean gotAPG = line.getApgComplementaireSM() != null
                && !JadeStringUtil.isBlankOrZero(line.getApgComplementaireSM());
        boolean gotAbsJust = line.getAbsencesJustifiees() != null
                && !JadeStringUtil.isBlankOrZero(line.getAbsencesJustifiees());
        boolean gotVacances = line.getVacances() != null && !JadeStringUtil.isBlankOrZero(line.getVacances());
        boolean gotGrats = line.getGratification() != null && !JadeStringUtil.isBlankOrZero(line.getGratification());

        return gotAPG && (gotAbsJust || gotVacances || gotGrats);
    }

    private LigneDecompteComplementaireEbu createLineForAPG(final LigneDecompteComplementaireEbu line, Decompte decompte)
            throws UnsatisfiedSpecificationException, JadePersistenceException {
        LigneDecompteComplementaireEbu newLine = line.clone();

        PosteTravail pt = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                line.getPosteTravail().getIdPosteTravail());

        // Reset IDS and values except APG
        newLine.setAbsencesJustifiees("");
        newLine.setVacances("");
        newLine.setGratification("");
        newLine.setLineCorrelationId("");
        newLine.setIdDecompteLine("");
        // TODO à voir que faire de la remarque si ligne ADDED automatiquement
        // newLine.setRemarque("Ligne APG ADDED");
        newLine.setStatus(StatusLine.ADDED);
        DecompteSalaire decompteSalaire = addNewDecompteSalaireComplementaire(newLine, decompte, pt, "", true);
        decompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().create(decompteSalaire);
        newLine.setIdDecompteLine(decompteSalaire.getId());
        decompteSalaire = checkForCodeErreurLigneComplementaire(newLine, true);
        VulpeculaServiceLocator.getDecompteSalaireService().update(decompteSalaire);
        decompte.getLignes().add(decompteSalaire);

        return newLine;
    }

    /**
     * Met à jour les décomptes
     * 
     * @throws JadePersistenceException
     */
    @Override
    public DecompteComplementaireEbu updateDecompteComplementaire(DecompteComplementaireEbu decompteEbu)
            throws JadePersistenceException {

        Validate.notNull(decompteEbu, "Decompte is null !");
        Validate.notEmpty(decompteEbu.getIdDecompte(), "Id decompte is empty !");

        DecompteComplementaireEbu decompteEbuRetour = null;
        etatDecompte = EtatDecompte.VALIDE;
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository()
                    .findById(decompteEbu.getIdDecompte());
            Validate.isTrue(decompte.isEditable() && !EtatDecompte.A_TRAITER.equals(decompte.getEtat()),
                    "Le décompte dois être éditable! Statut décompte " + decompte.getEtat());
            if (decompteEbu.getDecompteLines() == null) {
                List<LigneDecompteComplementaireEbu> listeVide = new ArrayList<LigneDecompteComplementaireEbu>();
                decompteEbu.setDecompteLines(listeVide);
            }

            for (LigneDecompteComplementaireEbu line : decompteEbu.getDecompteLines()) {
                boolean creation = false;
                if (StatusLine.UNCHANGED.equals(line.getStatus())) {
                    continue;
                }

                if (checkIf2Lines(line)) {
                    createLineForAPG(line, decompte);
                    line.setApgComplementaireSM("");
                }

                StatusLine statusLine = StatusLine.UNCHANGED;
                DecompteSalaire decompteSalaire = null;

                if (StatusLine.TO_ADD.equals(line.getStatus())) {
                    etatDecompte = EtatDecompte.A_TRAITER;

                    // Verifie qu'on a le minimum des infos nécessaire
                    if (line.getPosteTravail() == null || line.getPosteTravail().getIdPosteTravail() == null
                            || line.getPosteTravail().getIdPosteTravail().isEmpty()) {
                        line.setStatus(StatusLine.ERROR);
                        continue;
                    }
                    PosteTravail pt = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                            line.getPosteTravail().getIdPosteTravail());
                    decompteSalaire = addNewDecompteSalaireComplementaire(line, decompte, pt,
                            line.getLineCorrelationId(), false);
                    creation = true;
                    statusLine = StatusLine.ADDED;
                    decompteSalaire.setStatus(statusLine);

                } else if (StatusLine.TO_DELETE.equals(line.getStatus())) {
                    decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                            line.getIdDecompteLine());
                    decompte.getLignes().remove(decompteSalaire);
                    VulpeculaServiceLocator.getDecompteSalaireService().delete(decompteSalaire);
                    continue;

                } else if (StatusLine.TO_MODIFY.equals(line.getStatus())) {
                    if (line.getIdDecompteLine() == null || line.getIdDecompteLine().isEmpty()) {
                        line.setStatus(StatusLine.ERROR);
                        continue;
                    }

                    decompteSalaire = checkForCodeErreurLigneComplementaire(line, false);
                    statusLine = StatusLine.MODIFIED;
                    decompteSalaire.setStatus(statusLine);

                }

                // on regarde si le taux ebusiness correspond au taux BMS
                if (decompteSalaire.getIdPosteTravail() != null
                        && (decompteSalaire.getApgComplementaireSM() == null
                                || JadeStringUtil.isBlankOrZero(decompteSalaire.getApgComplementaireSM().getValue()) || Montant.ZERO
                                    .equals(decompteSalaire.getApgComplementaireSM()))) {
                    if (!decompteSalaire.getTauxContribuableForCaissesSociales(true).equals(
                            new Taux(line.getTauxContribution()))) {
                        decompteSalaire.addCodeErreur(CodeErreur.TAUX_DIFFERENT);
                        etatDecompte = EtatDecompte.A_TRAITER;
                    }
                }

                if (creation) {
                    decompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().create(decompteSalaire);
                } else {
                    decompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().update(decompteSalaire);
                }
                line.update(decompteSalaire, statusLine);

            }

            decompte.setTypeProvenance(TypeProvenance.EBUSINESS);

            if (decompte.getDateReception() == null
                    || JadeStringUtil.isBlankOrZero(decompte.getDateReceptionAsSwissValue())) {
                decompte.setDateReception(new Date());
            }

            decompte.setEtat(etatDecompte);
            VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

            decompteEbuRetour = convertDecompteComplementaireForEbu(VulpeculaRepositoryLocator.getDecompteRepository()
                    .findByIdWithDependencies(decompteEbu.getIdDecompte()), "");

            updateDecompteToReceptionne(decompteEbu.getIdDecompte(), etatDecompte);

        } catch (UnsatisfiedSpecificationException e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return decompteEbuRetour;
    }

    private boolean ligneSupprimeExists(List<CodeErreurDecompteSalaire> list) {
        for (CodeErreurDecompteSalaire codeErreur : list) {
            if (codeErreur.getCodeErreur().equals(CodeErreur.LIGNE_SUPPRIMEE)) {
                return true;
            }
        }
        return false;
    }

    private DecompteSalaire addNewDecompteSalaireNewTravailleur(LigneDecomptePeriodiqueEbu line, Decompte decompte,
            String lineCorrelationId) throws UnsatisfiedSpecificationException {

        String correlationId = line.getPosteTravailEbu().getCorrelationId();
        String posteCorrelationId = line.getPosteTravailEbu().getPosteCorrelationId();

        // TravailleurEbuDomain travailleurEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
        // .findByCorrelationId(posteCorrelationId);

        DecompteSalaire newDecompteSalaire = line.getDecompteSalaire();
        newDecompteSalaire.setCorrelationId(correlationId);
        newDecompteSalaire.setLineCorrelationId(lineCorrelationId);
        newDecompteSalaire.setPosteCorrelationId(posteCorrelationId);
        newDecompteSalaire.setDecompte(decompte);
        newDecompteSalaire.setSequence(getLastSequence(decompte.getId()));
        newDecompteSalaire.setPeriode(new Periode(line.getPeriodeDebut(), line.getPeriodeFin()));
        newDecompteSalaire.setTauxAfficheErreur(String.valueOf(line.getTauxContribution()));

        // On récupère le taux de contribution
        newDecompteSalaire.setTauxForEbu(new Taux(line.getTauxContribution()));
        newDecompteSalaire.setTauxSaisieEbu(Double.toString(line.getTauxContribution()));

        if (line.getAbsences().hasAbsence()) {
            newDecompteSalaire.addCodeErreur(CodeErreur.CODE_ABSENCE);
            for (TypeAbsence absence : line.getAbsences().getAbsences()) {
                Absence abs = new Absence();
                abs.setType(absence);
                newDecompteSalaire.addAbsence(abs);
            }
        }
        if (!line.getRemarque().isEmpty() && !line.getRemarque().trim().equals(newDecompteSalaire.getRemarque())) {
            newDecompteSalaire.addCodeErreur(CodeErreur.REMARQUE);
            if (line.getRemarque().length() > MAX_SIZE_REMARQUE) {
                String remarqueSub = line.getRemarque().substring(0, MAX_SIZE_REMARQUE);
                newDecompteSalaire.setRemarque(remarqueSub);
            } else {
                newDecompteSalaire.setRemarque(line.getRemarque());
            }
            etatDecompte = EtatDecompte.A_TRAITER;
        }

        // TODO A vérifier : ne pas ajouter le code NOUVELLE_LIGNE pour les NOUVEAU_TRAVAILLEUR ?
        // newDecompteSalaire.addCodeErreur(CodeErreur.NOUVELLE_LIGNE);
        newDecompteSalaire.addCodeErreur(CodeErreur.NOUVEAU_TRAVAILLEUR);

        if (newDecompteSalaire.getPosteTravail() != null) {
            VulpeculaServiceLocator.getDecompteService().ajoutCotisationsPourPoste(newDecompteSalaire);
            newDecompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().create(newDecompteSalaire);
        } else {
            newDecompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService().createWithoutPosteTravail(
                    newDecompteSalaire);
        }

        return newDecompteSalaire;
    }

    /**
	 * 
	 */
    @Override
    public byte[] downloadDecompteBvrPDF(String idDecompte) {
        Validate.notEmpty(idDecompte, "Un id doit être renseigné !");

        BSession session = UtilsService.initSession("VULPECULA");
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // On regarde que l'utilisateur possède les droits d'impression
            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(idDecompte);

            List<DecompteContainer> decompteContainers = new ArrayList<DecompteContainer>();
            VulpeculaServiceLocator.getDecompteService().retrieveDecompteInfos(decompte);

            CotisationsInfo cotisationInfo = VulpeculaServiceLocator.getDecompteService().retrieveCotisationsInfo(
                    decompte.getIdEmployeur(), decompte.getPeriodeDebut());
            DecompteContainer decompteContainer = new DecompteContainer(decompte.getType());
            decompteContainer.setDecompte(decompte);
            decompteContainer.setCotisationsInfo(cotisationInfo);
            decompteContainers.add(decompteContainer);

            // Impression BVR
            DocumentDecompteBVRPrinter documentDecompteBVRPrinter = DocumentDecompteBVRPrinter
                    .createWithDecompteContainer(decompteContainers);
            documentDecompteBVRPrinter.setSession(session);
            documentDecompteBVRPrinter.setSendCompletionMail(false);
            documentDecompteBVRPrinter.setSendMailOnError(false);
            documentDecompteBVRPrinter.executeProcess();

            JadePublishDocument doc = (JadePublishDocument) documentDecompteBVRPrinter.getAttachedDocuments().get(0);

            return pdfToByte(doc.getDocumentLocation());

        } catch (SQLException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return null;
    }

    @Override
    public byte[] downloadDecomptePDF(String idDecompte) {
        Validate.notEmpty(idDecompte, "Un id doit être renseigné !");

        BSession session = UtilsService.initSession("VULPECULA");
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // On regarde que l'utilisateur possède les droits d'impression
            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(idDecompte);

            List<DecompteContainer> decompteContainers = new ArrayList<DecompteContainer>();
            VulpeculaServiceLocator.getDecompteService().retrieveDecompteInfos(decompte);

            CotisationsInfo cotisationInfo = VulpeculaServiceLocator.getDecompteService().retrieveCotisationsInfo(
                    decompte.getIdEmployeur(), decompte.getPeriodeDebut());
            DecompteContainer decompteContainer = new DecompteContainer(decompte.getType());
            decompteContainer.setDecompte(decompte);
            decompteContainer.setCotisationsInfo(cotisationInfo);
            decompteContainers.add(decompteContainer);

            // Impression Decompte
            DocumentDecompteVidePrinter documentDecompteVidePrinter = DocumentDecompteVidePrinter
                    .createWithDecompteContainer(decompteContainers, true);
            documentDecompteVidePrinter.setSession(session);
            documentDecompteVidePrinter.setSendCompletionMail(false);
            documentDecompteVidePrinter.setSendMailOnError(false);
            documentDecompteVidePrinter.setPrintingFromEbu(true);
            documentDecompteVidePrinter.executeProcess();

            JadePublishDocument doc = (JadePublishDocument) documentDecompteVidePrinter.getAttachedDocuments().get(0);

            return pdfToByte(doc.getDocumentLocation());

        } catch (SQLException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return null;
    }

    public static void convertByteArrayToDoc(byte[] b) {
        OutputStream out;
        try {
            out = new FileOutputStream("C:/temp/temp.pdf");
            out.write(b, 0, b.length);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static byte[] pdfToByte(String filePath) throws JRException {
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        byte[] data = null;
        byte[] finalData = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            data = new byte[(int) file.length()];
            finalData = new byte[(int) file.length()];
            byteArrayOutputStream = new ByteArrayOutputStream();

            fileInputStream.read(data);
            byteArrayOutputStream.write(data);
            finalData = byteArrayOutputStream.toByteArray();

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException e) {
            System.out.println("IO exception" + e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
            }
        }

        return finalData;
    }

    /**
     * @param line
     * @return
     * @throws UnsatisfiedSpecificationException
     */
    private DecompteSalaire checkForCodesErreur(LigneDecomptePeriodiqueEbu line)
            throws UnsatisfiedSpecificationException {

        DecompteSalaire decompteSalaire = null;

        Montant MNT_578 = new Montant(578);
        Montant MNT_1763 = new Montant(1763);
        Montant MNT_12350 = new Montant(12350);

        try {
            decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                    line.getIdDecompteLine());

            List<CodeErreurDecompteSalaire> listeVide = new ArrayList<CodeErreurDecompteSalaire>();
            decompteSalaire.setListeCodeErreur(listeVide);

            if (JadeStringUtil.isEmpty(decompteSalaire.getIdPosteTravail())) {
                TravailleurEbuDomain travailleurEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                        .findByCorrelationId(decompteSalaire.getCorrelationId());
                decompteSalaire.setPosteTravail(travailleurEbu.getPosteTravailFictif());
                decompteSalaire.addCodeErreur(CodeErreur.NOUVEAU_TRAVAILLEUR);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            // Update decompteSalaire
            Montant montantTotalSalaire = Montant.ZERO;
            montantTotalSalaire = new Montant(line.getTotalSalaire());

            decompteSalaire.setTauxSaisieEbu(Double.toString(line.getTauxContribution()));
            decompteSalaire.setHeures(line.getNombreHeure());
            decompteSalaire.setSalaireHoraire(new Montant(line.getSalaireHoraire()));
            decompteSalaire.setSalaireTotal(montantTotalSalaire);
            decompteSalaire.setPeriode(new Periode(line.getPeriodeDebut(), line.getPeriodeFin()));

            if (!line.getRemarque().isEmpty()
                    && !line.getRemarque().trim().equals(decompteSalaire.getRemarque().trim())) {
                if (line.getRemarque().length() > MAX_SIZE_REMARQUE) {
                    String remarqueSub = line.getRemarque().substring(0, MAX_SIZE_REMARQUE);
                    decompteSalaire.setRemarque(remarqueSub);
                } else {
                    decompteSalaire.setRemarque(line.getRemarque());
                }
                decompteSalaire.addCodeErreur(CodeErreur.REMARQUE);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            // TODO comparer les absences existantes avec celles à rajouter !
            for (TypeAbsence absence : line.getAbsences().getAbsences()) {
                if (!hasThisAbsence(decompteSalaire, absence)) {
                    Absence abs = new Absence();
                    abs.setType(absence);
                    decompteSalaire.addAbsence(abs);
                }
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            montantTotalSalaire = decompteSalaire.getSalaireTotal();
            if (montantTotalSalaire.less(MNT_578)
                    && !Qualification.APPRENTI.equals(decompteSalaire.getQualificationPoste())) {
                if (decompteSalaire.getPosteTravail().getTravailleur().getIdTiers() != null) {
                    if (PTAFServices.hasDroitsActifs(decompteSalaire.getPeriodeFinAsSwissValue(), decompteSalaire
                            .getPosteTravail().getTravailleur().getIdTiers())) {
                        decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_INFERIEUR_578);
                        etatDecompte = EtatDecompte.A_TRAITER;
                    }
                }
            }
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                    decompteSalaire.getEmployeur().getId());
            List<Cotisation> listeCoti = VulpeculaServiceLocator.getCotisationService().findByIdAffilie(
                    employeur.getId());
            employeur.setCotisations(listeCoti);
            if (montantTotalSalaire.greater(MNT_12350) && employeur.isSoumisAVS(decompteSalaire.getPeriodeDebut())) {
                decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_SUPERIEUR_12350);
                etatDecompte = EtatDecompte.A_TRAITER;
            } else if (montantTotalSalaire.greater(MNT_1763)
                    && Qualification.APPRENTI.equals(decompteSalaire.getQualificationPoste())) {
                decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_SUPERIEUR_1763);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            if (montantTotalSalaire.isZero() && decompteSalaire.getAbsences().isEmpty()) {
                decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_ZERO);
                etatDecompte = EtatDecompte.A_TRAITER;
            }
            if (montantTotalSalaire.isNegative()) {
                decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_NEGATIF);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

        } catch (Exception e) {
            LOGGER.warn("Erreur de mise à jour du décompte salaire avec id : " + line.getIdDecompteLine(), e);
        }

        return decompteSalaire;
    }

    private DecompteSalaire checkForCodeErreurLigneComplementaire(LigneDecompteComplementaireEbu line,
            boolean cotisApgAllreadyExist) {

        DecompteSalaire decompteSalaire = null;
        double totalSalaire = 0.00;

        try {
            decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                    line.getIdDecompteLine());
            Montant MNT_MAX_AC = new Montant(148200);
            if (decompteSalaire.getCotisationAC() != null) {
                Assurance assuranceAC = decompteSalaire.getCotisationAC().getAssurance();
                MNT_MAX_AC = VulpeculaServiceLocator.getDecompteService().getPlafond(assuranceAC,
                        decompteSalaire.getPeriodeFin());
            }

            boolean isAPG;

            if (!cotisApgAllreadyExist) {
                isAPG = line.getApgComplementaireSM() != null
                        && !JadeStringUtil.isBlankOrZero(line.getApgComplementaireSM());
            } else {
                isAPG = false;
            }

            List<CodeErreurDecompteSalaire> listeVide = new ArrayList<CodeErreurDecompteSalaire>();
            decompteSalaire.setListeCodeErreur(listeVide);

            if (JadeStringUtil.isEmpty(decompteSalaire.getIdPosteTravail())) {
                TravailleurEbuDomain travailleurEbu = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                        .findByCorrelationId(decompteSalaire.getCorrelationId());
                decompteSalaire.setPosteTravail(travailleurEbu.getPosteTravailFictif());
                decompteSalaire.addCodeErreur(CodeErreur.NOUVEAU_TRAVAILLEUR);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            decompteSalaire.setTauxSaisieEbu(line.getTauxContribution());
            decompteSalaire.setPeriode(new Periode(line.getPeriodeDebut(), line.getPeriodeFin()));
            if (!JadeStringUtil.isEmpty(line.getApgComplementaireSM())) {
                decompteSalaire.setApgComplementaireSM(new Montant(line.getApgComplementaireSM()));
                totalSalaire = decompteSalaire.getApgComplementaireSM().doubleValue();
            }
            if (!JadeStringUtil.isEmpty(line.getAbsencesJustifiees())) {
                decompteSalaire.setAbsencesJustifiees(new Montant(line.getAbsencesJustifiees()));
                totalSalaire += decompteSalaire.getAbsencesJustifiees().doubleValue();
            }
            if (!JadeStringUtil.isEmpty(line.getGratification())) {
                decompteSalaire.setGratifications(new Montant(line.getGratification()));
                totalSalaire += decompteSalaire.getGratifications().doubleValue();
            }
            if (!JadeStringUtil.isEmpty(line.getVacances())) {
                decompteSalaire.setVacancesFeries(new Montant(line.getVacances()));
                totalSalaire += decompteSalaire.getVacancesFeries().doubleValue();
            }
            decompteSalaire.setSalaireTotal(new Montant(totalSalaire));

            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                    decompteSalaire.getEmployeur().getId());
            List<Cotisation> listeCoti = VulpeculaServiceLocator.getCotisationService().findByIdAffilie(
                    employeur.getId());
            employeur.setCotisations(listeCoti);
            decompteSalaire.getPeriode();
            double nbMonthPeriod = (Periode.getNombreMois(decompteSalaire.getPeriodeDebut(),
                    decompteSalaire.getPeriodeFin())) + 1.00;
            double salaireMensuel = Double.parseDouble(decompteSalaire.getSalaireTotalAsValue());

            salaireMensuel = salaireMensuel / nbMonthPeriod;

            Montant montantSalaireAnnuel = new Montant(salaireMensuel);
            montantSalaireAnnuel.add(findMasseACFor(decompteSalaire.getPosteTravail().getId(), decompteSalaire
                    .getDecompte().getAnnee()));
            if (employeur.isSoumisAVS(decompteSalaire.getPeriodeDebut()) && montantSalaireAnnuel.greater(MNT_MAX_AC)) {
                decompteSalaire.addCodeErreur(CodeErreur.SALAIRE_SUPERIEUR_148200);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            if (!line.getRemarque().isEmpty()
                    && !line.getRemarque().trim().equals(decompteSalaire.getRemarque().trim())) {
                if (line.getRemarque().length() > MAX_SIZE_REMARQUE) {
                    String remarqueSub = line.getRemarque().substring(0, MAX_SIZE_REMARQUE);
                    decompteSalaire.setRemarque(remarqueSub);
                } else {
                    decompteSalaire.setRemarque(line.getRemarque());
                }
                decompteSalaire.addCodeErreur(CodeErreur.REMARQUE);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            if (decompteSalaire.getIdTravailleur() != null) {
                if (VulpeculaServiceLocator.getPosteTravailService().travailleurHasPostIt(
                        decompteSalaire.getIdTravailleur())) {
                    List<SimpleNote> postits = VulpeculaServiceLocator.getPosteTravailService().getPostIt(
                            decompteSalaire.getIdTravailleur());
                    int countFound = 0;
                    int countElse = 0;
                    for (SimpleNote postit : postits) {
                        if (!postit.getDescription().startsWith("*")) {
                            countElse++;
                        } else {
                            countFound++;
                        }
                    }
                    if (!(countFound > 0 && countElse == 0)) {
                        decompteSalaire.addCodeErreur(CodeErreur.POST_IT);
                        etatDecompte = EtatDecompte.A_TRAITER;
                    }
                }
            }
            if (decompteSalaire.getIdPosteTravail() != null
                    && decompteSalaire.isTravailleurRetraiter(decompteSalaire.getDecompte().getPeriodeFin())) {
                decompteSalaire.addCodeErreur(CodeErreur.RETRAITE);
                etatDecompte = EtatDecompte.A_TRAITER;
            }

            if (!cotisApgAllreadyExist) {
                if (isAPG) {
                    VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                            .deleteCotisationsDecompte(decompteSalaire);
                    decompteSalaire.getCotisationsDecompte().clear();
                    VulpeculaServiceLocator.getDecompteService().ajoutCotisationPourLigneAPG(decompteSalaire);
                }
            }

            decompteSalaire.setStatus(line.getStatus());

        } catch (Exception e) {
            LOGGER.warn("Erreur de mise à jour du décompte salaire avec id : " + line.getIdDecompteLine(), e);
        }
        return decompteSalaire;
    }

    private boolean hasThisAbsence(DecompteSalaire ligneExistante, TypeAbsence typeAbsence) {
        for (Absence absence : ligneExistante.getAbsences()) {
            if (absence.getType() == typeAbsence) {
                return true;
            }
        }
        return false;
    }

    private Montant findMasseACFor(String idPosteTravail, Annee anneeDecompte) {
        List<DecompteSalaire> decomptesSalaires = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findForYear(idPosteTravail, anneeDecompte);
        Montant masseAC = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            CotisationDecompte cotisationAC = decompteSalaire.getCotisationAC();
            if (cotisationAC != null && decompteSalaire.isComptabilise()) {
                if (cotisationAC.getMasse().isZero()) {
                    masseAC = masseAC.add(decompteSalaire.getSalaireTotal());
                } else {
                    masseAC = masseAC.add(cotisationAC.getMasse());
                }
            }
        }
        return masseAC;
    }

    /**
     * @param line
     * @param decompte
     * @return
     * @throws UnsatisfiedSpecificationException
     */
    private DecompteSalaire addNewDecompteSalaire(LigneDecomptePeriodiqueEbu line, Decompte decompte, PosteTravail pt,
            String lineCorrelationId) throws UnsatisfiedSpecificationException {
        // String idPosteTravail = line.getPosteTravailEbu().getIdPosteTravail();
        // VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        DecompteSalaire newDecompteSalaire = line.getDecompteSalaire();
        newDecompteSalaire.setLineCorrelationId(lineCorrelationId);
        newDecompteSalaire.setPosteCorrelationId(pt.getPosteCorrelationId());
        newDecompteSalaire.setPosteTravail(pt);
        newDecompteSalaire.setDecompte(decompte);
        newDecompteSalaire.setSequence(getLastSequence(decompte.getId()));
        newDecompteSalaire.setPeriode(new Periode(line.getPeriodeDebut(), line.getPeriodeFin()));
        if (line.getRemarque().length() > MAX_SIZE_REMARQUE) {
            String remarqueSub = line.getRemarque().substring(0, MAX_SIZE_REMARQUE);
            newDecompteSalaire.setRemarque(remarqueSub);
        } else {
            newDecompteSalaire.setRemarque(line.getRemarque());
        }
        newDecompteSalaire.setTauxAfficheErreur(String.valueOf(line.getTauxContribution()));

        // On récupère le taux de contribution
        newDecompteSalaire.setTauxForEbu(new Taux(line.getTauxContribution()));
        newDecompteSalaire.setTauxSaisieEbu(Double.toString(line.getTauxContribution()));

        for (TypeAbsence absence : line.getAbsences().getAbsences()) {
            Absence abs = new Absence();
            abs.setType(absence);
            newDecompteSalaire.addAbsence(abs);
        }

        newDecompteSalaire.addCodeErreur(CodeErreur.NOUVELLE_LIGNE);
        VulpeculaServiceLocator.getDecompteService().ajoutCotisationsPourPoste(newDecompteSalaire);

        return newDecompteSalaire;
    }

    private DecompteSalaire addNewDecompteSalaireComplementaire(LigneDecompteComplementaireEbu line, Decompte decompte,
            PosteTravail pt, String lineCorrelationId, boolean addedForAPG) throws UnsatisfiedSpecificationException {
        // isApg : la ligne est elle uniquement APG ? / addedForAPG : ligne APG créée par le traitement d'un autre ligne
        boolean isApg = false;
        if (!addedForAPG) {
            isApg = line.getApgComplementaireSM() != null
                    && !JadeStringUtil.isBlankOrZero(line.getApgComplementaireSM());
        } else {
            isApg = true;
        }
        double totalSalaire = 0.00;
        DecompteSalaire newDecompteSalaire = line.getDecompteSalaire();
        newDecompteSalaire.setLineCorrelationId(lineCorrelationId);
        newDecompteSalaire.setPosteCorrelationId(pt.getPosteCorrelationId());
        newDecompteSalaire.setPosteTravail(pt);
        newDecompteSalaire.setDecompte(decompte);
        newDecompteSalaire.setSequence(getLastSequence(decompte.getId()));
        newDecompteSalaire.setPeriode(new Periode(line.getPeriodeDebut(), line.getPeriodeFin()));
        newDecompteSalaire.setTauxAfficheErreur(String.valueOf(line.getTauxContribution()));
        if (!JadeStringUtil.isEmpty(line.getApgComplementaireSM())) {
            newDecompteSalaire.setApgComplementaireSM(new Montant(line.getApgComplementaireSM()));
            totalSalaire = Double.valueOf(line.getApgComplementaireSM());
        }
        if (!JadeStringUtil.isEmpty(line.getAbsencesJustifiees())) {
            newDecompteSalaire.setAbsencesJustifiees(new Montant(line.getAbsencesJustifiees()));
            totalSalaire += newDecompteSalaire.getAbsencesJustifiees().doubleValue();
        }
        if (!JadeStringUtil.isEmpty(line.getGratification())) {
            newDecompteSalaire.setGratifications(new Montant(line.getGratification()));
            totalSalaire += newDecompteSalaire.getGratifications().doubleValue();
        }
        if (!JadeStringUtil.isEmpty(line.getVacances())) {
            newDecompteSalaire.setVacancesFeries(new Montant(line.getVacances()));
            totalSalaire += newDecompteSalaire.getVacancesFeries().doubleValue();
        }
        newDecompteSalaire.setSalaireTotal(new Montant(totalSalaire));

        if (line.getRemarque() != null && !JadeStringUtil.isEmpty(line.getRemarque())) {
            newDecompteSalaire.setRemarque(line.getRemarque());
            newDecompteSalaire.addCodeErreur(CodeErreur.REMARQUE);
        }

        // On récupère le taux de contribution
        if (!JadeStringUtil.isBlankOrZero(line.getTauxContribution())) {
            newDecompteSalaire.setTauxForEbu(new Taux(line.getTauxContribution()));
            newDecompteSalaire.setTauxSaisieEbu(line.getTauxContribution());
        } else {
            newDecompteSalaire.setTauxForEbu(new Taux(0.00));
            newDecompteSalaire.setTauxSaisieEbu("0.00");
        }

        if (!addedForAPG) {
            newDecompteSalaire.addCodeErreur(CodeErreur.NOUVELLE_LIGNE);
        }

        newDecompteSalaire.setStatus(line.getStatus());

        if (!isApg) {
            VulpeculaServiceLocator.getDecompteService().ajoutCotisationsPourPoste(newDecompteSalaire);
        } else {
            VulpeculaServiceLocator.getDecompteService().ajoutCotisationPourLigneAPG(newDecompteSalaire);
        }

        return newDecompteSalaire;
    }

    /**
     * @param idDecompte
     * @return
     */
    private String getLastSequence(String idDecompte) {
        List<DecompteSalaire> liste = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdDecompte(
                idDecompte);
        int sequenceMax = 0;
        for (DecompteSalaire decompteSalaire : liste) {
            int currentSeq = Integer.parseInt(decompteSalaire.getSequence());
            if (sequenceMax < currentSeq) {
                sequenceMax = currentSeq;
            }
        }
        return String.valueOf(++sequenceMax);
    }

    /**
     * @param decompte
     * @throws UnsatisfiedSpecificationException
     */
    private void updateDecompteToReceptionne(String idDecompte, EtatDecompte etat)
            throws UnsatisfiedSpecificationException {
        VulpeculaServiceLocator.getDecompteService().updateEtat(idDecompte, etat);
    }

    /**
     * @param listeDecompteEbu
     * @param decompte
     */
    private DecomptePeriodiqueEbu convertDecompteForEbu(Decompte decompte, String synchronize_id) {
        // convert to decompte EBU
        DecomptePeriodiqueEbu decompteEbu = new DecomptePeriodiqueEbu(decompte, synchronize_id);

        // load lines decompte
        List<LigneDecomptePeriodiqueEbu> listLines = new ArrayList<LigneDecomptePeriodiqueEbu>();

        if ("".equals(synchronize_id)) {
            // si update ou liste non synchro
            for (DecompteSalaire ligne : decompte.getLignes()) {
                listLines.add(new LigneDecomptePeriodiqueEbu(ligne, false));
            }
        } else {
            // si liste synchro
            for (DecompteSalaire ligne : decompte.getLignes()) {
                listLines.add(new LigneDecomptePeriodiqueEbu(ligne, true));
            }
        }

        decompteEbu.setDecompteLines(listLines);
        return decompteEbu;
    }

    private DecompteComplementaireEbu convertDecompteComplementaireForEbu(Decompte decompte, String syncroId) {
        // convert to decompte EBU
        DecompteComplementaireEbu decompteEbu = new DecompteComplementaireEbu(decompte, syncroId);
        // load lines decompte
        List<LigneDecompteComplementaireEbu> listLines = new ArrayList<LigneDecompteComplementaireEbu>();
        for (DecompteSalaire ligne : decompte.getLignes()) {
            listLines.add(new LigneDecompteComplementaireEbu(ligne));
        }
        decompteEbu.setDecompteLines(listLines);

        return decompteEbu;
    }

    private DecompteSpecialEbu convertDecompteSpecialForEbu(Decompte decompte, String syncroId) {
        // convert to decompte EBU
        DecompteSpecialEbu decompteEbu = new DecompteSpecialEbu(decompte, syncroId);
        // load lines decompte

        return decompteEbu;
    }

    /**
     * @param idEmployeur
     * @param yearsMonthFrom
     * @param yearsMonthTo
     * @return
     * @throws JadePersistenceException
     */
    @Deprecated
    private List<Decompte> createDecompte(String idEmployeur, String yearsMonthFrom, String yearsMonthTo)
            throws JadePersistenceException {
        List<Decompte> listeDecompte;
        List<Decompte> listeDecompteRetour = new ArrayList<Decompte>();
        PeriodeMensuelle periode = new PeriodeMensuelle(yearsMonthFrom, yearsMonthTo);
        Date dateEtablissement = Date.now();

        GenererDecompteProcessor genererDecompteProcess = new GenererDecompteProcessor();

        PTProcessDecompteInfo decompteInfo = new PTProcessDecompteInfo(TypeDecompte.PERIODIQUE, periode,
                dateEtablissement, 0, "");
        Employeur empl = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);
        listeDecompte = genererDecompteProcess.genererDecompte(empl, decompteInfo);

        // On indique la provenance du décompte. Il a été créé par eBusiness
        for (Decompte decompte : listeDecompte) {
            decompte.setTypeProvenance(TypeProvenance.EBUSINESS);
            VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

            listeDecompteRetour.add(VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(
                    decompte.getId()));
        }

        return listeDecompteRetour;
    }

    @Override
    public List<DecompteComplementaireEbu> createDecompteComplementaire(String idEmployeur, String year) {
        // TODO Faut-il faire un service qui liste les complémentaire ou crée un ??

        // Validation input data
        Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
        Validate.notEmpty(year, "Year must not be empty !");
        Validate.isTrue(Integer.parseInt(year) > 0, "Year must be an integer greater than zero.");
        Validate.isTrue(year.trim().length() == 4, "Year must be in format YYYY !");

        List<Decompte> listeDecompte;
        List<Decompte> listeDecompteRetour = new ArrayList<Decompte>();
        int annee = Integer.parseInt(year);
        PeriodeMensuelle periode = new PeriodeMensuelle(Date.getFirstDayOfYear(annee), Date.getLastDayOfYear(annee));
        Date dateEtablissement = Date.now();

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            GenererDecompteProcessor genererDecompteProcess = new GenererDecompteProcessor();

            PTProcessDecompteInfo decompteInfo = new PTProcessDecompteInfo(TypeDecompte.COMPLEMENTAIRE, periode,
                    dateEtablissement, 0, "");
            Employeur empl = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);
            listeDecompte = genererDecompteProcess.genererDecompte(empl, decompteInfo);

            // On indique la provenance du décompte. Il a été créé par eBusiness
            for (Decompte decompte : listeDecompte) {
                decompte.setTypeProvenance(TypeProvenance.EBUSINESS);
                VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

                listeDecompteRetour.add(VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(
                        decompte.getId()));
            }

        } catch (SQLException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (listeDecompteRetour.size() > 1) {
            // TODO Erreur
            LOGGER.error("Aucun decompte complementaire n'a pu etre cree !!");
        }
        Decompte dec = listeDecompteRetour.get(0);

        List<DecompteComplementaireEbu> listeDecompteComplemEbu = new ArrayList<DecompteComplementaireEbu>();

        listeDecompteComplemEbu.add(convertDecompteComplementaireForEbu(dec, ""));

        return listeDecompteComplemEbu;
    }

    @Override
    public StatusAnnonceEbu ackSyncDecomptes(List<String> synchronizeIdList) {
        String message = "";
        StatusAnnonceEbu status = new StatusAnnonceEbu();
        status.setReponse(StatusReponse.OK);

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            for (String synchronizeId : synchronizeIdList) {
                Synchronisation synch = VulpeculaRepositoryLocator.getSynchronisationRepository().findBySynchronizeId(
                        synchronizeId);
                if (synch != null) {
                    synch.setDateSynchronisation(Date.now());
                    VulpeculaRepositoryLocator.getSynchronisationRepository().update(synch);
                    String idDecompte = synch.getDecompte().getId();
                    List<Synchronisation> listeSynch = VulpeculaRepositoryLocator.getSynchronisationRepository()
                            .findDecompteToAck(synchronizeId, idDecompte);
                    for (Synchronisation synchronisation : listeSynch) {
                        synchronisation.setDateSynchronisation(Date.now());
                        VulpeculaRepositoryLocator.getSynchronisationRepository().update(synchronisation);
                    }
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

    @Override
    public List<DecompteComplementaireEbu> listDecomptesComnplementaire(String idEmployeur, String year, String status,
            boolean synchronize) {
        // VALIDATE PARAM
        Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
        Validate.notEmpty(year, "Year must not be empty !");
        Validate.isTrue(Integer.parseInt(year) > 0, "Year must be an integer greater than zero.");
        Validate.isTrue(year.trim().length() == 4, "Year must be in format YYYY !");

        Date yearsMonthFromDate = Date.getFirstDayOfYear(Integer.parseInt(year));
        Date yearsMonthToDate = Date.getLastDayOfYear(Integer.parseInt(year));
        String yearsMonthFrom = yearsMonthFromDate.getAnneeMois();
        String yearsMonthTo = yearsMonthToDate.getAnneeMois();
        if (synchronize) {
            List<DecompteComplementaireEbu> listeDecompteComplemEbu = new ArrayList<DecompteComplementaireEbu>();
            // Récupération d'une session
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);

            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);
                if (new Date(employeur.getDateDebut()).after(yearsMonthFromDate)) {
                    yearsMonthFromDate = new Date(employeur.getDateDebut());
                    yearsMonthFrom = yearsMonthFromDate.getAnneeMois();
                }
                Map<String, Synchronisation> listeSynchro = VulpeculaRepositoryLocator.getSynchronisationRepository()
                        .findDecompteToSynchronize(idEmployeur, yearsMonthFrom, yearsMonthTo, status);

                for (Map.Entry<String, Synchronisation> synchronisation : listeSynchro.entrySet()) {
                    Decompte decompteToAdd = synchronisation.getValue().getDecompte();

                    decompteToAdd.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                            .findByIdDecompteWithDependencies(decompteToAdd.getId()));
                    boolean isToComptaOuValide = true;
                    if (decompteToAdd.isTaxationOffice()) {
                        isToComptaOuValide = isTOComptabiliseOuValide(decompteToAdd);
                    }
                    if (decompteToAdd.getType().equals(TypeDecompte.COMPLEMENTAIRE) && isToComptaOuValide) {
                        listeDecompteComplemEbu.add(convertDecompteComplementaireForEbu(decompteToAdd, synchronisation
                                .getValue().getId()));
                    }
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                session.addError(e.getMessage());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                session.addError(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

            return listeDecompteComplemEbu;
        } else {
            // Validation input data
            Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");

            List<DecompteComplementaireEbu> listeDecompteComplemEbu = new ArrayList<DecompteComplementaireEbu>();
            // Récupération d'une session
            BSession session = UtilsService.initSession();
            JadeThreadContext threadContext = initThreadContext(session);
            try {
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

                // load decompte
                List<Decompte> listeDecompte = VulpeculaRepositoryLocator.getDecompteRepository()
                        .findComplementaireByIdEmployeurAndPeriodeWithDependencies(idEmployeur, new Annee(year));

                for (Decompte decompte : listeDecompte) {
                    boolean isToComptaOuValide = true;
                    if (decompte.isTaxationOffice()) {
                        isToComptaOuValide = isTOComptabiliseOuValide(decompte);
                    }
                    if (isToComptaOuValide) {
                        listeDecompteComplemEbu.add(convertDecompteComplementaireForEbu(decompte, ""));
                    }
                }
            } catch (SQLException e) {
                session.addError(e.getMessage());
                LOGGER.error(e.getMessage());
            } catch (Exception e) {
                session.addError(e.getMessage());
                LOGGER.error(e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

            return listeDecompteComplemEbu;
        }
    }

    @Deprecated
    private boolean listeDecompteContainsComplementaire(List<Decompte> listeDecompte) {
        for (Decompte dec : listeDecompte) {
            if (dec.getType().equals(TypeDecompte.COMPLEMENTAIRE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DecompteComplementaireEbu getDecompteComplementaire(String idDecompte) {
        Validate.notEmpty(idDecompte.trim(), "idDecompte must not be empty !");
        Decompte dec = null;
        DecompteComplementaireEbu decCompRetour = null;
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            dec = VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(idDecompte);
            if (dec != null && TypeDecompte.COMPLEMENTAIRE.equals(dec.getType())) {
                decCompRetour = convertDecompteComplementaireForEbu(dec, "");
            }

        } catch (SQLException e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            session.addError(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        Validate.isTrue(dec != null && TypeDecompte.COMPLEMENTAIRE.equals(dec.getType()),
                "The decompte est null ou n'est pas un complémentaire !");
        return decCompRetour;

    }

}
