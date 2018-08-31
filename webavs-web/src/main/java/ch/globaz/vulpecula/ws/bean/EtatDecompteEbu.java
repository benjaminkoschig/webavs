package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;

@XmlType(name = "etatDecompte")
public enum EtatDecompteEbu {
    @XmlEnumValue("OUVERT")
    OUVERT,
    @XmlEnumValue("EN_COURS")
    EN_COURS,
    @XmlEnumValue("TERMINE")
    TERMINE,
    @XmlEnumValue("ANNULE")
    ANNULE,
    @XmlEnumValue("TAXATION_D_OFFICE")
    TAXATION_D_OFFICE;

    public static EtatDecompteEbu fromEtatDecompte(final EtatDecompte value) {
        if (EtatDecompte.GENERE.equals(value) || EtatDecompte.OUVERT.equals(value)
                || EtatDecompte.SOMMATION.equals(value)) {
            return EtatDecompteEbu.OUVERT;
        }
        if (EtatDecompte.A_TRAITER.equals(value) || EtatDecompte.ERREUR.equals(value)
                || EtatDecompte.RECEPTIONNE.equals(value)) {
            return EtatDecompteEbu.EN_COURS;
        }
        if (EtatDecompte.COMPTABILISE.equals(value) || EtatDecompte.FACTURATION.equals(value)
                || EtatDecompte.RECTIFIE.equals(value) || EtatDecompte.VALIDE.equals(value)) {
            return EtatDecompteEbu.TERMINE;
        }
        if (EtatDecompte.ANNULE.equals(value)) {
            return EtatDecompteEbu.ANNULE;
        }
        if (EtatDecompte.TAXATION_DOFFICE.equals(value)) {
            return EtatDecompteEbu.TAXATION_D_OFFICE;
        }

        return EtatDecompteEbu.EN_COURS;
    }
}
