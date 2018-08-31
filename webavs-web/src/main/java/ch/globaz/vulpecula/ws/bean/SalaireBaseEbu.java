package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="salaireBase")
public class SalaireBaseEbu {
    private TypeSalaire typeSalaire;
    private Montant salaireBase;
}
