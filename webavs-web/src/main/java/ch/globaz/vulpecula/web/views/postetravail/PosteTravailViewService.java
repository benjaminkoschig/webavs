package ch.globaz.vulpecula.web.views.postetravail;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang.Validate;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.web.gson.PosteTravailGSON;
import com.google.gson.Gson;

/**
 * @author Arnaud Geiser (AGE) | Créé le 4 mars 2014
 * 
 */
public class PosteTravailViewService {
    public static List<ch.globaz.vulpecula.util.CodeSystem> getQualificationsParConvention(final String idConvention) {
        return VulpeculaServiceLocator.getPosteTravailService().getQualificationForConvention(idConvention);
    }

    public static Map<String, AdhesionCotisationViewContainer> getAdhesionsCotisationsActivesEtPossiblesGroupByPlanCaisse(
            final String idPosteTravail, final String idAffilie, final String dateDebut, final String dateFin,
            String dateNaissanceTravailleur, String sexeTravailleur) {
        return PosteTravailViewService.groupByPlanCaisseId(PosteTravailViewService
                .getAdhesionsCotisationsActivesEtPossibles(idPosteTravail, idAffilie, dateDebut, dateFin,
                        dateNaissanceTravailleur, sexeTravailleur));
    }

    public static Map<String, AdhesionCotisationViewContainer> getAdhesionsCotisationsActivesGroupByPlanCaisse(
            final String idPosteTravail, final String dateFin) {
        return PosteTravailViewService.groupByPlanCaisseId(PosteTravailViewService
                .getAdhesionsCotisationsActivesRevision(idPosteTravail, dateFin));
    }

    private static AdhesionCotisationViewContainer getAdhesionsCotisationsActivesRevision(final String idPosteTravail,
            final String dateFin) {
        AdhesionCotisationViewContainer cotisationsActives = new AdhesionCotisationViewContainer();
        cotisationsActives.addAll(PosteTravailViewService.getAdhesionsCotisationsActives(idPosteTravail, dateFin));
        return cotisationsActives;
    }

    public static void create(final String posteTravailString) throws ViewException, JadePersistenceException {
        Gson gson = new Gson();
        PosteTravailGSON posteTravailGson = gson.fromJson(posteTravailString, PosteTravailGSON.class);
        PosteTravail posteTravail = posteTravailGson.convertToDomain();

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                posteTravail.getEmployeur().getId());
        posteTravail.setEmployeur(employeur);
        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                posteTravail.getTravailleur().getId());
        posteTravail.setTravailleur(travailleur);
        for (AdhesionCotisationPosteTravail adh : posteTravail.getAdhesionsCotisations()) {
            adh.setCotisation(VulpeculaServiceLocator.getCotisationService().findById(adh.getIdCotisation()));
        }
        try {
            VulpeculaServiceLocator.getPosteTravailService().create(posteTravail);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
    }

    public static void update(final String posteTravailString) throws ViewException, JadePersistenceException,
            Exception {
        Gson gson = new Gson();
        PosteTravailGSON posteTravailGson = gson.fromJson(posteTravailString, PosteTravailGSON.class);
        PosteTravail posteTravail = posteTravailGson.convertToDomain();
        PosteTravail posteBeforeUpdate = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                posteTravail.getId());

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                posteTravail.getEmployeur().getId());
        posteTravail.setEmployeur(employeur);
        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                posteTravail.getTravailleur().getId());
        posteTravail.setTravailleur(travailleur);
        for (AdhesionCotisationPosteTravail adh : posteTravail.getAdhesionsCotisations()) {
            adh.setCotisation(VulpeculaServiceLocator.getCotisationService().findById(adh.getIdCotisation()));
        }
        try {
            VulpeculaServiceLocator.getPosteTravailService().update(posteTravail);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }

        VulpeculaServiceLocator.getPosteTravailService().checkForMailAF(posteBeforeUpdate, posteTravail);

    }

    public static Map<String, AdhesionCotisationViewContainer> groupByPlanCaisseId(
            AdhesionCotisationViewContainer adhesionCotisationViews) {
        Map<String, AdhesionCotisationViewContainer> map = new TreeMap<String, AdhesionCotisationViewContainer>();
        for (AdhesionCotisationView adhesionCotisationView : adhesionCotisationViews) {
            if (!map.containsKey(adhesionCotisationView.libellePlanCaisse)) {
                map.put(adhesionCotisationView.libellePlanCaisse, new AdhesionCotisationViewContainer());
            }
            map.get(adhesionCotisationView.libellePlanCaisse).add(adhesionCotisationView);
        }

        return map;
    }

    private static AdhesionCotisationViewContainer getAdhesionsCotisationsActivesEtPossibles(
            final String idPosteTravail, final String idAffilie, final String dateDebut, final String dateFin,
            String dateNaissanceTravailleur, String sexeTravailleur) {

        // Load actives cotisations => only existing and Active Poste de travail have cotisation
        AdhesionCotisationViewContainer cotisationsActivesEtPossibles = new AdhesionCotisationViewContainer();
        cotisationsActivesEtPossibles.addAll(PosteTravailViewService.getAdhesionsCotisationsActives(idPosteTravail,
                dateFin));

        // A new Poste de travail needs to have the possibility to choose any active cotisation
        AdhesionCotisationViewContainer tmpContainer = new AdhesionCotisationViewContainer();
        if (cotisationsActivesEtPossibles.isEmpty()) {
            tmpContainer.addAll(PosteTravailViewService.getAdhesionsCotisationsPossibles(idAffilie, dateDebut, dateFin,
                    isNouveauPoste(idPosteTravail), dateNaissanceTravailleur, sexeTravailleur));
        }

        if (isNouveauPosteEbu(idPosteTravail, tmpContainer)) {
            AdhesionCotisationViewContainer tmpEbuContainer = new AdhesionCotisationViewContainer();
            tmpEbuContainer.addAll(PosteTravailViewService.getAdhesionsCotisationsPossibles(idAffilie, dateDebut,
                    dateFin, true, dateNaissanceTravailleur, sexeTravailleur));
            for (AdhesionCotisationView cotisation : tmpEbuContainer) {
                if (cotisation.getDateDebut() != null && !cotisation.getDateDebut().isEmpty()) {
                    cotisation.checked = true;
                }
            }
            cotisationsActivesEtPossibles.addAll(tmpEbuContainer);
        } else {
            tmpContainer = new AdhesionCotisationViewContainer();
            tmpContainer.addAll(PosteTravailViewService.getAdhesionsCotisationsPossibles(idAffilie, dateDebut, dateFin,
                    isNouveauPoste(idPosteTravail), dateNaissanceTravailleur, sexeTravailleur));
            for (AdhesionCotisationView adhesionCotisation : tmpContainer) {
                if (JadeStringUtil.isBlank(adhesionCotisation.dateFinCotisation)) {
                    cotisationsActivesEtPossibles.add(adhesionCotisation);
                }
            }
        }
        return cotisationsActivesEtPossibles;
    }

    /**
     * @param idPosteTravail
     * @return
     */
    private static boolean isNouveauPoste(final String idPosteTravail) {
        return !((idPosteTravail != null) && (idPosteTravail.length() > 0));
    }

    /**
     * @param idPosteTravail
     * @param tmpContainer
     * @return
     */
    private static boolean isNouveauPosteEbu(final String idPosteTravail,
            final AdhesionCotisationViewContainer cotisationViewContainer) {
        // Check if poste was created by WebMetier
        boolean isNew = isNouveauPoste(idPosteTravail);

        // Only check if not a real new poste, when WebMetier creates a poste the poste id is empty by default
        if (!isNew) {
            for (AdhesionCotisationView cotisation : cotisationViewContainer) {
                // If any of the cotisation have checked value set to true that is means is a existing element
                if (cotisation.checked) {
                    isNew = false;
                    break;
                } else {
                    isNew = true;
                }
            }
        }
        return isNew;
    }

    /**
     * @param idPosteTravail
     * @return
     */
    public static Set<AdhesionCotisationView> getAdhesionsCotisationsActives(final String idPosteTravail,
            final String dateFin) {
        if (JadeStringUtil.isEmpty(idPosteTravail)) {
            return new HashSet<AdhesionCotisationView>();
        }

        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        Set<AdhesionCotisationView> adhesionsCotisationsActives = new TreeSet<AdhesionCotisationView>();

        List<AdhesionCotisationPosteTravail> cotisationPosteTravail = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findByIdPosteTravail(idPosteTravail);

        for (AdhesionCotisationPosteTravail adhesionCotisation : cotisationPosteTravail) {
            AdhesionCotisationView adhesionCotisationView = new AdhesionCotisationView();
            adhesionCotisationView.id = adhesionCotisation.getId();
            adhesionCotisationView.spy = adhesionCotisation.getSpy();
            adhesionCotisationView.idCotisation = adhesionCotisation.getCotisation().getId();
            adhesionCotisationView.dateDebut = adhesionCotisation.getPeriode().getDateDebutAsSwissValue();
            adhesionCotisationView.dateDebutCotisation = adhesionCotisation.getCotisation().getDateDebut()
                    .getSwissValue();
            if (adhesionCotisation.getCotisation().getDateFin() != null) {
                adhesionCotisationView.dateFinCotisation = adhesionCotisation.getCotisation().getDateFin()
                        .getSwissValue();
            }
            // Si le poste de travail et la cotisation possède la même date de fin et que la nouvelle période du
            // poste sera "vide", alors on met vide dans la nouvelle période des cotisations.
            if (JadeStringUtil.isEmpty(dateFin) && posteTravail.getFinActivite() != null
                    && posteTravail.getFinActivite().equals(adhesionCotisation.getPeriode().getDateFin())) {
                // On contrôle que la date de fin de cotisation ne dépasse pas celle du poste
                if (adhesionCotisation.getCotisation().getDateFin() == null) {
                    adhesionCotisationView.dateFin = null;
                } else {
                    adhesionCotisationView.dateFin = adhesionCotisation.getCotisation().getDateFin().getSwissValue();
                }
            } else {
                adhesionCotisationView.dateFin = adhesionCotisation.getPeriode().getDateFinAsSwissValue();
            }
            if (adhesionCotisation.getCotisation().getPlanCaisse() != null) {
                adhesionCotisationView.idPlanCaisse = adhesionCotisation.getCotisation().getPlanCaisse().getId();
            }
            adhesionCotisationView.checked = true;
            if (adhesionCotisation.getCotisation().getAssurance() != null) {
                adhesionCotisationView.nom = adhesionCotisation.getCotisation().getAssurance().getLibelleFr();
            }
            adhesionCotisationView.libellePlanCaisse = adhesionCotisation.getCotisation().getPlanCaisse().getLibelle();
            adhesionCotisationView.idAssurance = adhesionCotisation.getCotisation().getAssuranceId();
            adhesionsCotisationsActives.add(adhesionCotisationView);
        }
        return adhesionsCotisationsActives;
    }

    private static AdhesionCotisationViewContainer getAdhesionsCotisationsPossibles(final String idAffilie,
            final String dateDebutAsString, final String dateFinAsString, boolean isNouveauPoste,
            String dateNaissanceTravailleur, String sexeTravailleur) {
        Date dateNaissance = new Date(dateNaissanceTravailleur);
        Date dateDebutActivite = new Date(dateDebutAsString);
        Date dateFinActivite = !JadeStringUtil.isEmpty(dateFinAsString) ? new Date(dateFinAsString) : null;

        if (idAffilie == null) {
            return new AdhesionCotisationViewContainer();
        }

        Set<AdhesionCotisationView> adhesionsCotisationsPossibles = new TreeSet<AdhesionCotisationView>();
        List<Cotisation> cotisationsEmployeurs = null;
        cotisationsEmployeurs = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(idAffilie,
                dateDebutActivite, dateFinActivite);

        List<String> listIdCotisations = new ArrayList<String>();
        if (isNouveauPoste) {
            listIdCotisations = findCotisationsObligatoires(idAffilie, dateDebutAsString, dateFinAsString,
                    dateNaissanceTravailleur);
        }

        for (Cotisation cotisation : cotisationsEmployeurs) {
            AdhesionCotisationView adhesionCotisationView = new AdhesionCotisationView();
            adhesionCotisationView.idCotisation = cotisation.getId();
            adhesionCotisationView.idPlanCaisse = cotisation.getPlanCaisse().getId();
            adhesionCotisationView.checked = false;
            adhesionCotisationView.nom = cotisation.getAssurance().getLibelleFr();
            adhesionCotisationView.libellePlanCaisse = cotisation.getPlanCaisse().getLibelle();
            adhesionCotisationView.dateDebutCotisation = cotisation.getDateDebut().getSwissValue();
            if (cotisation.getDateFin() != null) {
                adhesionCotisationView.dateFinCotisation = cotisation.getDateFin().getSwissValue();
            } else {
                adhesionCotisationView.dateFinCotisation = "";
            }

            if (isNouveauPoste && listIdCotisations.contains(cotisation.getId())) {
                CotisationDatee cotisationDatee = CotisationDatee.calculer(cotisation, sexeTravailleur, dateNaissance,
                        dateDebutActivite, dateFinActivite);
                if (cotisationDatee.isValide()) {
                    adhesionCotisationView.dateDebut = cotisationDatee.getDateDebut().getSwissValue();
                    adhesionCotisationView.dateFin = cotisationDatee.getDateFin() != null ? cotisationDatee
                            .getDateFin().getSwissValue() : null;
                }
            }
            adhesionCotisationView.idAssurance = cotisation.getAssuranceId();
            adhesionsCotisationsPossibles.add(adhesionCotisationView);
        }
        return new AdhesionCotisationViewContainer(adhesionsCotisationsPossibles);
    }

    public double findNombreHeuresParMois(String idPosteTravail, String stringDate) {
        Date date = new Date(stringDate);
        return VulpeculaServiceLocator.getPosteTravailService().getNombreHeuresParMois(idPosteTravail, date);
    }

    public double findNombreHeuresParMois2(String idPosteTravail, String stringDateDebut, String stringDateFin) {
        Date dateDebut = new Date(stringDateDebut);
        Date dateFin = new Date(stringDateFin);
        return VulpeculaServiceLocator.getPosteTravailService().getNombreHeuresParMois(idPosteTravail, dateDebut,
                dateFin);
    }

    public double findNombreHeuresParJour(String idPosteTravail, String stringDate) {
        Date date = new Date(stringDate);
        return VulpeculaServiceLocator.getPosteTravailService().getNombreHeuresParJour(idPosteTravail, date);
    }

    public CotisationDateeView findCotisationDateeForTravailleur(String idTravailleur, String idCotisation,
            String dateDebutActivite, String dateFinActivite) {
        Date dateDebut = new Date(dateDebutActivite);
        Date dateFin = !"null".equals(dateFinActivite) ? new Date(dateFinActivite) : null;
        CotisationDatee cotisationDatee = VulpeculaServiceLocator.getCotisationService()
                .findCotisationDateeForTravailleur(idTravailleur, idCotisation, dateDebut, dateFin);
        return new CotisationDateeView(cotisationDatee);
    }

    /**
     * Retourne les idCotisations des cotisations AVS, AF, AC, AC2 et frais d'administration de l'employeur
     * 
     * @param idPosteTravail
     * @return List idCotisations AVS, AF, AC, AC2 et FA
     */
    public static List<String> findCotisationsObligatoires(String idEmployeur, String dateDebut, String dateFin,
            String dateNaissanceTravailleur) {

        List<String> listIdCotisations = new ArrayList<String>();
        List<Cotisation> listCotisation = new ArrayList<Cotisation>();
        if (!JadeStringUtil.isEmpty(dateFin.trim())) {
            listCotisation = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(idEmployeur,
                    new Date(dateDebut), new Date(dateFin));
        } else {
            listCotisation = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(idEmployeur,
                    new Date(dateDebut), null);
        }

        for (Cotisation cotisationEmployeur : listCotisation) {
            String typeAssurance = cotisationEmployeur.getAssurance().getTypeAssurance().getValue();
            if (typeAssurance.equals(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)
                    || typeAssurance.equals(CodeSystem.TYPE_ASS_COTISATION_AF)
                    || typeAssurance.equals(CodeSystem.TYPE_ASS_COTISATION_AC)
                    || typeAssurance.equals(CodeSystem.TYPE_ASS_COTISATION_AC2)
                    || typeAssurance.equals(CodeSystem.TYPE_ASS_FRAIS_ADMIN)
                    || typeAssurance.equals(CodeSystem.TYPE_ASS_FFPP)
                    || typeAssurance.equals(CodeSystem.TYPE_ASS_FFPP_MASSE)) {
                listIdCotisations.add(cotisationEmployeur.getId());
            }
        }

        return listIdCotisations;
    }

    public static boolean hasDecompteSalaireAfterDateFin(String idPosteTravail, String dateFinPoste) {
        Validate.notNull(idPosteTravail);
        Validate.notNull(dateFinPoste);

        Date dateFin = new Date(dateFinPoste);

        List<DecompteSalaire> decomptesSalaires = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdPosteTravailAfterDateFin(idPosteTravail, dateFin);
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            if (decompteSalaire.isComptabilise()
                    && TypeDecompte.PERIODIQUE.equals(decompteSalaire.getDecompte().getType())) {
                return true;
            }
        }
        return false;
    }
}
