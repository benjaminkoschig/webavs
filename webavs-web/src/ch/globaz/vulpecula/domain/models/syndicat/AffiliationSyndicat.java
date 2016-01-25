package ch.globaz.vulpecula.domain.models.syndicat;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class AffiliationSyndicat implements DomainEntity {
    private String id;
    private Travailleur travailleur;
    private Administration syndicat;
    private String spy;
    private Periode periode;
    private Montant cumulSalaires;

    public AffiliationSyndicat() {

    }

    public AffiliationSyndicat(AffiliationSyndicat affiliationSyndicat) {
        id = affiliationSyndicat.id;
        travailleur = affiliationSyndicat.travailleur;
        syndicat = affiliationSyndicat.syndicat;
        spy = affiliationSyndicat.spy;
        periode = affiliationSyndicat.periode;
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public Administration getSyndicat() {
        return syndicat;
    }

    public void setSyndicat(Administration syndicat) {
        this.syndicat = syndicat;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getDateDebutAsSwissValue() {
        if (periode == null) {
            return null;
        }
        return periode.getDateDebutAsSwissValue();
    }

    public String getDateFinAsSwissValue() {
        if (periode == null) {
            return null;
        }
        return periode.getDateFinAsSwissValue();
    }

    public String getIdTravailleur() {
        return travailleur.getId();
    }

    public String getIdSyndicat() {
        if (syndicat == null) {
            return null;
        }
        return syndicat.getId();
    }

    public String getLibelleSyndicat() {
        if (syndicat == null) {
            return null;
        }
        return syndicat.getDesignation1() + " " + syndicat.getDesignation2();
    }

    public String getCodeAdministration() {
        return syndicat.getCodeAdministration();
    }

    public static Map<Administration, Collection<AffiliationSyndicat>> groupBySyndicat(
            List<AffiliationSyndicat> affiliationsSyndicats) {
        return Multimaps.index(affiliationsSyndicats, new Function<AffiliationSyndicat, Administration>() {
            @Override
            public Administration apply(AffiliationSyndicat affiliationSyndicat) {
                return affiliationSyndicat.syndicat;
            }
        }).asMap();
    }

    public String getNoAVSTravailleur() {
        return travailleur.getNumAvsActuel();
    }

    public String getNomTravailleur() {
        return travailleur.getDesignation1();
    }

    public String getPrenomTravailleur() {
        return travailleur.getDesignation2();
    }

    public Montant getCumulSalaires() {
        if (cumulSalaires == null) {
            throw new IllegalStateException(
                    "Le cumul salaire n'est pas disponible ! La valeur doit être renseigné au travers d'un service (calculé dynamiquement)");
        }
        return cumulSalaires;
    }

    public void setCumulSalaires(Montant cumulSalaires) {
        this.cumulSalaires = cumulSalaires;
    }

    public String getDateNaissanceTravailleur() {
        return travailleur.getDateNaissance();
    }

    // Selon le livre Effective Java, il ne faut pas utiliser la méthode clone mais privilégier une copie par
    // constructeur : http://www.artima.com/intv/bloch13.html
    @Override
    public AffiliationSyndicat clone() {
        return new AffiliationSyndicat(this);
    }
}
