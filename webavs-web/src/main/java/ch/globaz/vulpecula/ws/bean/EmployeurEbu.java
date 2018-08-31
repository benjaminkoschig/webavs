package ch.globaz.vulpecula.ws.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

/**
 * Description de la classe
 * 
 * @since eBMS 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "employeur")
public class EmployeurEbu {

    private String idEmployeur;
    private String raisonSociale;

    @XmlElement(nillable = false, required = true)
    private String numeroAffiliation;

    @XmlElement(name = "adresseCourrier")
    private AdresseEbu adresseCourrier;

    @XmlElement(name = "adresseDomicile")
    private AdresseEbu adresseDomicile;

    @XmlElements({ @XmlElement(name = "cotisation") })
    private List<CotisationEbu> cotisations;

    private String periodicite;
    private String dateDebutActivite;
    private String dateFinActivite;

    private String codeConvention;
    private String libelleConvention;

    public EmployeurEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public EmployeurEbu(Employeur employeur) {
        idEmployeur = employeur.getId();
        raisonSociale = employeur.getRaisonSociale();
        numeroAffiliation = employeur.getAffilieNumero();
        periodicite = employeur.getPeriodicite();
        dateDebutActivite = employeur.getDateDebut();
        dateFinActivite = employeur.getDateFin();
        codeConvention = employeur.getConvention().getCode();
        libelleConvention = employeur.getConvention().getDesignation();
    }

    public void setCotisations(List<CotisationEbu> listCotisations) {
        cotisations = listCotisations;
    }

    public void setAdresseCourrier(AdresseEbu adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    public void setAdresseDomicile(AdresseEbu adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }
}
