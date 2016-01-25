package globaz.corvus.db.rentesverseesatort.wrapper;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

public class RERenteVerseeATortWrapper {

    private String dateDeces;
    private String dateNaissance;
    private Long idDemandeRente;
    private Long idRenteVerseeATort;
    private Long idTiers;
    private String nationalite;
    private String nom;
    private NumeroSecuriteSociale nss;
    private Periode periode;
    private String prenom;
    private RERentesPourCalculRenteVerseeATort renteAncienDroit;
    private RERentesPourCalculRenteVerseeATort renteNouveauDroit;
    private String sexe;
    private TypeRenteVerseeATort type;

    public RERenteVerseeATortWrapper(Long idRenteVerseeATort, Long idDemandeRente, Long idTiers, String nom,
            String prenom, NumeroSecuriteSociale nss, String dateNaissance, String dateDeces, String sexe,
            String nationalite, RERentesPourCalculRenteVerseeATort renteNouveauDroit,
            RERentesPourCalculRenteVerseeATort renteAncienDroit, Periode periode, TypeRenteVerseeATort type) {
        super();

        Checkers.checkNotNull(dateDeces, "dateDeces");
        Checkers.checkNotNull(dateNaissance, "dateNaissance");
        Checkers.checkNotNull(idRenteVerseeATort, "idRenteVerseeATort");
        Checkers.checkNotNull(idDemandeRente, "idDemandeRente");
        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(nom, "nom");
        Checkers.checkNotNull(prenom, "prenom");
        Checkers.checkNotNull(nationalite, "nationalite");
        Checkers.checkNotNull(nss, "nss");
        Checkers.checkNotNull(periode, "periode");
        Checkers.checkNotNull(sexe, "sexe");
        Checkers.checkNotNull(type, "type");

        this.dateDeces = dateDeces;
        this.dateNaissance = dateNaissance;
        this.idRenteVerseeATort = idRenteVerseeATort;
        this.idDemandeRente = idDemandeRente;
        this.idTiers = idTiers;
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
        this.nss = nss;
        this.periode = periode;
        this.renteNouveauDroit = renteNouveauDroit;
        this.renteAncienDroit = renteAncienDroit;
        this.sexe = sexe;
        this.type = type;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public Long getIdTiers() {
        return idTiers;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getNom() {
        return nom;
    }

    public NumeroSecuriteSociale getNss() {
        return nss;
    }

    public Periode getPeriode() {
        return periode;
    }

    public String getPrenom() {
        return prenom;
    }

    public RERentesPourCalculRenteVerseeATort getRenteAncienDroit() {
        return renteAncienDroit;
    }

    public RERentesPourCalculRenteVerseeATort getRenteNouveauDroit() {
        return renteNouveauDroit;
    }

    public String getSexe() {
        return sexe;
    }

    public TypeRenteVerseeATort getType() {
        return type;
    }
}
