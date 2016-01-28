package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;

public class PosteTravailGSON implements Serializable {
    private static final long serialVersionUID = 8624088861442699832L;

    private String idEmployeur;
    private String idTravailleur;

    public List<OccupationGSON> occupations;
    public List<AdhesionCotisationPosteTravailGSON> cotisations;

    public String spy;
    public String id;
    public String periodeDebut;
    public String periodeFin;
    public String qualification;
    public String conventionId;
    public String genreSalaire;
    public String etat;
    public boolean posteFranchiseAVS;
    public String idTiersCM;

    public String getIdTiersCM() {
        return idTiersCM;
    }

    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    public PosteTravail convertToDomain() {
        Date dateDebut = null;
        if (Date.isValid(periodeDebut)) {
            dateDebut = new Date(periodeDebut);
        }

        Date dateFin = null;
        if (Date.isValid(periodeFin)) {
            dateFin = new Date(periodeFin);
        }

        Qualification qualification = null;
        if (Qualification.isValid(this.qualification)) {
            qualification = Qualification.fromValue(this.qualification);
        }

        TypeSalaire genreSalaire = null;
        if (TypeSalaire.isValid(this.genreSalaire)) {
            genreSalaire = TypeSalaire.fromValue(this.genreSalaire);
        }

        List<Occupation> occup = new ArrayList<Occupation>();
        for (OccupationGSON occupationGSON : occupations) {
            occup.add(occupationGSON.convertToDomain());
        }

        List<AdhesionCotisationPosteTravail> adhesionCotisation = new ArrayList<AdhesionCotisationPosteTravail>();
        for (AdhesionCotisationPosteTravailGSON adhesionGSON : cotisations) {
            adhesionCotisation.add(adhesionGSON.convertToDomain());
        }

        PosteTravail poste = new PosteTravail();
        poste.setId(id);
        poste.setPeriodeActivite(new Periode(dateDebut, dateFin));
        poste.setQualification(qualification);
        poste.setTypeSalaire(genreSalaire);
        Travailleur travailleur = new Travailleur();
        travailleur.setId(idTravailleur);
        poste.setTravailleur(travailleur);
        Employeur employeur = new Employeur();
        employeur.setId(idEmployeur);
        poste.setEmployeur(employeur);
        poste.setFranchiseAVS(posteFranchiseAVS);
        poste.setOccupations(occup);
        poste.setAdhesionsCotisations(adhesionCotisation);
        poste.setSpy(spy);
        poste.setIdTiersCM(idTiersCM);

        return poste;
    }
}
