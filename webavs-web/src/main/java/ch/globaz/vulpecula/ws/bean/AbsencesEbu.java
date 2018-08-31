package ch.globaz.vulpecula.ws.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import ch.globaz.vulpecula.domain.models.decompte.TypeAbsence;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="absences")
public class AbsencesEbu {
    private List<TypeAbsence> absences = new ArrayList<TypeAbsence>();

    public AbsencesEbu() {
    	// Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public void addAbsence(TypeAbsence absence) {
        absences.add(absence);
    }

	/**
	 * @return the absences
	 */
	public List<TypeAbsence> getAbsences() {
		return absences;
	}

	/**
	 * @param absences the absences to set
	 */
	public void setAbsences(List<TypeAbsence> absences) {
		this.absences = absences;
	}
	
	public boolean hasAbsence() {
		return absences.size() > 0;
	}
}
