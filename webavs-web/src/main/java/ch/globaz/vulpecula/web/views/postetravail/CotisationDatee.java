package ch.globaz.vulpecula.web.views.postetravail;

import globaz.jade.client.util.JadeDateUtil;
import java.util.Arrays;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.PersonneSimple;

public class CotisationDatee {
    private String id;
    private Date dateDebut;
    private Date dateFin;

    private static final List<TypeAssurance> SOUMIS_REGLES_AVS = Arrays.asList(TypeAssurance.COTISATION_AVS_AI,
            TypeAssurance.COTISATION_LPP, TypeAssurance.ASSURANCE_CHOMAGE, TypeAssurance.COTISATION_AC2,
            TypeAssurance.COTISATION_AF, TypeAssurance.COTISATION_FFPP_MASSE, TypeAssurance.COTISATION_FFPP_CAPITATION,
            TypeAssurance.FRAIS_ADMINISTRATION);
    private static final List<TypeAssurance> SOUMIS_REGLES_AC = Arrays.asList(TypeAssurance.ASSURANCE_CHOMAGE,
            TypeAssurance.COTISATION_AC2);

    public static CotisationDatee calculer(Cotisation cotisation, String sexe, Date dateNaissance,
            Date dateDebutActivite, Date dateFinActivite) {
        CotisationDatee cotisationDatee = new CotisationDatee();
        if (!PersonneSimple.isMineur(dateNaissance, dateDebutActivite)) {
            if (dateDebutActivite.after(cotisation.getDateDebut())) {
                cotisationDatee.setDateDebut(dateDebutActivite);
            } else {
                cotisationDatee.setDateDebut(cotisation.getDateDebut());
            }
            if (cotisation.hasDateFin()) {
                cotisationDatee.setDateFin(cotisation.getDateFin());
            } else {
                cotisationDatee.setDateFin(dateFinActivite);
            }
        } else {
            Date dateAvs = PersonneSimple.getAnnee18ans(dateNaissance).getDateOfFirstDayOfYear();
            if (dateDebutActivite.after(cotisation.getDateDebut())) {
                if (!isSoumiseReglesAVS(cotisation.getTypeAssurance()) || dateDebutActivite.after(dateAvs)) {
                    cotisationDatee.setDateDebut(dateDebutActivite);
                } else {
                    cotisationDatee.setDateDebut(dateAvs);
                }
            } else {
                if (!isSoumiseReglesAVS(cotisation.getTypeAssurance()) || cotisation.getDateDebut().after(dateAvs)) {
                    cotisationDatee.setDateDebut(cotisation.getDateDebut());
                } else {
                    cotisationDatee.setDateDebut(dateAvs);
                }
            }

            if (cotisation.hasDateFin()) {
                cotisationDatee.setDateFin(cotisation.getDateFin());
            } else {
                cotisationDatee.setDateFin(dateFinActivite);
            }
        }

        // L'AC et l'AC2 ont une date de fin le mois après l'âge de la retraite
        if (isSoumiseReglesAC(cotisation.getTypeAssurance())) {
            Date dateRetraite = VulpeculaServiceLocator.getTravailleurService().giveDateRentier(
                    dateNaissance.getSwissValue(), sexe);
            if (cotisation.hasDateFin()) {
                cotisationDatee.setDateFin(cotisation.getDateFin());
            } else if (dateRetraite.after(dateDebutActivite)) {
                if (dateFinActivite == null || dateFinActivite.after(dateRetraite)) {
                    Date lastDateCotisation = new Date(JadeDateUtil.addDays(dateRetraite.toString(), -1));
                    cotisationDatee.setDateFin(lastDateCotisation);
                } else {
                    cotisationDatee.setDateFin(dateFinActivite);
                }
            } else {
                cotisationDatee.setDateFin(dateDebutActivite);
            }
        }

        return cotisationDatee;
    }

    public static boolean isSoumiseReglesAVS(TypeAssurance typeAssurance) {
        return SOUMIS_REGLES_AVS.contains(typeAssurance);
    }

    public static boolean isSoumiseReglesAC(TypeAssurance typeAssurance) {
        return SOUMIS_REGLES_AC.contains(typeAssurance);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isValide() {
        return dateFin == null || dateFin.afterOrEquals(dateDebut);
    }
}
