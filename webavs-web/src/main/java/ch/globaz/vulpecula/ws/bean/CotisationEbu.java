package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cotisation")
public class CotisationEbu {

    private String idCotisation;

    private String dateDebutCotisation;
    private String dateFinCotisation;

    private String libelleFrCourtAssurance;
    private String libelleFrLongAssurance;
    private String libelleDeCourtAssurance;
    private String libelleDeLongAssurance;
    private String libelleItCourtAssurance;
    private String libelleItLongAssurance;
    private String typeAssurance;
    private String GenreCoti;
    private String numCaisse;

    public CotisationEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public CotisationEbu(Cotisation cotisation) {
        idCotisation = cotisation.getId();
        dateDebutCotisation = cotisation.getDateDebut().getSwissValue();
        if (cotisation.getDateFin() != null) {
            dateFinCotisation = cotisation.getDateFin().getSwissValue();
        } else {
            dateFinCotisation = "";
        }
        // libelleFrCourtAssurance = cotisation.getAssurance().getLibelleCourtFr();
        libelleFrLongAssurance = cotisation.getAssurance().getLibelleFr();
        // libelleDeCourtAssurance = cotisation.getAssurance().getLibelleCourtAl();
        libelleDeLongAssurance = cotisation.getAssurance().getLibelleAl();
        // libelleItCourtAssurance = cotisation.getAssurance().getLibelleCourtIt();
        libelleItLongAssurance = cotisation.getAssurance().getLibelleIt();
        typeAssurance = TypeAssurance.fromValue(cotisation.getAssurance().getTypeAssurance().getValue()).name();
        // GenreCoti = cotisation.getAssurance().getAssuranceGenre();
        numCaisse = cotisation.getPlanCaisse().getCodeAdministration();
    }
}
