package ch.globaz.vulpecula.web.views.postetravail;

import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
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

    public static void create(final String posteTravailString) throws ViewException {
        Gson gson = new Gson();
        PosteTravailGSON posteTravailGson = gson.fromJson(posteTravailString, PosteTravailGSON.class);
        PosteTravail posteTravail = posteTravailGson.convertToDomain();

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                posteTravail.getEmployeur().getId());
        posteTravail.setEmployeur(employeur);
        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                posteTravail.getTravailleur().getId());
        posteTravail.setTravailleur(travailleur);
        try {
            VulpeculaServiceLocator.getPosteTravailService().create(posteTravail);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
    }

    public static void update(final String posteTravailString) throws ViewException {
        Gson gson = new Gson();
        PosteTravailGSON posteTravailGson = gson.fromJson(posteTravailString, PosteTravailGSON.class);
        PosteTravail posteTravail = posteTravailGson.convertToDomain();

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                posteTravail.getEmployeur().getId());
        posteTravail.setEmployeur(employeur);
        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                posteTravail.getTravailleur().getId());
        posteTravail.setTravailleur(travailleur);
        try {
            VulpeculaServiceLocator.getPosteTravailService().update(posteTravail);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
    }

    private static Map<String, AdhesionCotisationViewContainer> groupByPlanCaisseId(
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
        AdhesionCotisationViewContainer cotisationsActivesEtPossibles = new AdhesionCotisationViewContainer();
        cotisationsActivesEtPossibles.addAll(PosteTravailViewService.getAdhesionsCotisationsActives(idPosteTravail,
                dateFin));
        cotisationsActivesEtPossibles.addAll(PosteTravailViewService.getAdhesionsCotisationsPossibles(idAffilie,
                dateDebut, dateFin, isNouveauPoste(idPosteTravail), dateNaissanceTravailleur, sexeTravailleur));

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
     * @return
     */
    private static Set<AdhesionCotisationView> getAdhesionsCotisationsActives(final String idPosteTravail,
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
            adhesionsCotisationsActives.add(adhesionCotisationView);
        }
        return adhesionsCotisationsActives;
    }

    private static AdhesionCotisationViewContainer getAdhesionsCotisationsPossibles(final String idAffilie,
            final String dateDebutAsString, final String dateFinAsString, boolean isNouveauPoste,
            String dateNaissanceTravailleur, String sexeTravailleur) {
        if (idAffilie == null) {
            return new AdhesionCotisationViewContainer();
        }

        Set<AdhesionCotisationView> adhesionsCotisationsPossibles = new TreeSet<AdhesionCotisationView>();
        List<Cotisation> cotisationsEmployeurs = null;
        Date dateFin = null;
        if (!JadeStringUtil.isEmpty(dateFinAsString)) {
            dateFin = new Date(dateFinAsString);
        }
        cotisationsEmployeurs = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(idAffilie,
                new Date(dateDebutAsString), dateFin);

        List<String> listIdCotisations = null;
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
            }

            if (isNouveauPoste) {
                if (!isMineur(new Date(dateNaissanceTravailleur), dateDebutAsString)) {
                    if (listIdCotisations.contains(cotisation.getId())) {
                        Date dateDebutActivite = new Date(dateDebutAsString);
                        if (dateDebutActivite.after(cotisation.getDateDebut())) {
                            adhesionCotisationView.dateDebut = dateDebutAsString;
                        } else {
                            adhesionCotisationView.dateDebut = adhesionCotisationView.dateDebutCotisation;
                        }

                        if (cotisation.getDateFin() != null) {
                            adhesionCotisationView.dateFin = adhesionCotisationView.dateFinCotisation;
                        } else {
                            adhesionCotisationView.dateFin = dateFinAsString;
                        }
                    }
                } else {
                    // On test si le travailleur a 18ans et on lui rajoute l'AVS avec comme date de début le mois ou
                    // l'année ??? de ses
                    // 18 ans.
                    if (listIdCotisations.contains(cotisation.getId())) {
                        Date dateDebutActivite = new Date(dateDebutAsString);

                        if (dateDebutActivite.after(cotisation.getDateDebut())) {

                            Date dateAvs = getAnnee18Ans(dateNaissanceTravailleur).getDateOfFirstDayOfYear();
                            if (dateDebutActivite.after(dateAvs)) {
                                adhesionCotisationView.dateDebut = dateDebutAsString;
                            } else {
                                adhesionCotisationView.dateDebut = dateAvs.getSwissValue();
                            }

                        } else {
                            adhesionCotisationView.dateDebut = adhesionCotisationView.dateDebutCotisation;
                        }

                        if (cotisation.getDateFin() != null) {
                            adhesionCotisationView.dateFin = adhesionCotisationView.dateFinCotisation;
                        } else {
                            adhesionCotisationView.dateFin = dateFinAsString;
                        }
                    }
                }
                // L'AC et l'AC2 ont une date de fin le mois après l'âge de la retraite
                if (cotisation.getTypeAssurance().getValue().equals(CodeSystem.TYPE_ASS_COTISATION_AC)
                        || cotisation.getTypeAssurance().getValue().equals(CodeSystem.TYPE_ASS_COTISATION_AC2)) {
                    Date dateRetraite = null;
                    try {
                        dateRetraite = VulpeculaServiceLocator.getTravailleurService().giveDateRentier(
                                dateNaissanceTravailleur, sexeTravailleur);
                    } catch (JAException e) {
                    }
                    if (cotisation.getDateFin() != null) {
                        adhesionCotisationView.dateFin = adhesionCotisationView.dateFinCotisation;
                    } else {
                        if (dateRetraite.after(new Date(dateDebutAsString))) {
                            adhesionCotisationView.dateFin = dateRetraite.getSwissValue();
                        } else {
                            adhesionCotisationView.dateFin = dateDebutAsString;
                        }

                    }
                }
            }
            adhesionsCotisationsPossibles.add(adhesionCotisationView);
        }
        return new AdhesionCotisationViewContainer(adhesionsCotisationsPossibles);
    }

    private static Date getAnnee18Ans(String dateNaissanceTravailleur) {
        Date dateNaissance = new Date(dateNaissanceTravailleur);
        dateNaissance = dateNaissance.addYear(18);
        return dateNaissance;
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

    /**
     * @param dateNaissance
     * @return true si age >= 18 ans
     */
    private static boolean isMineur(Date dateNaissance, String dateDebutPoste) {
        Date now = new Date(dateDebutPoste);

        long ageInMillis = now.getTime() - dateNaissance.getTime();
        long age = ageInMillis / 1000 / 60 / 60 / 24 / 7 / 52;

        if (18 >= age) {
            return true;
        }

        return false;
    }
}
