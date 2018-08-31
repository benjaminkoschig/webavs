package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localite")
public class LocaliteEbu {

    private String id;
    private String npa;
    private String libelleCourt;
    private String libelleLong;
    private String codeCanton;
    private String libelleCantonFr;
    private String libelleCantonAll;
    private String codePays;
    private String codePaysIso;
    private String libellePaysFr;
    private String libellePaysAll;

    public LocaliteEbu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getCodeCanton() {
        return codeCanton;
    }

    public void setCodeCanton(String codeCanton) {
        this.codeCanton = codeCanton;
    }

    public String getLibelleCantonFr() {
        return libelleCantonFr;
    }

    public void setLibelleCantonFr(String libelleCantonFr) {
        this.libelleCantonFr = libelleCantonFr;
    }

    public String getLibelleCantonAll() {
        return libelleCantonAll;
    }

    public void setLibelleCantonAll(String libelleCantonAll) {
        this.libelleCantonAll = libelleCantonAll;
    }

    public String getCodePays() {
        return codePays;
    }

    public void setCodePays(String codePays) {
        this.codePays = codePays;
    }

    public String getCodePaysIso() {
        return codePaysIso;
    }

    public void setCodePaysIso(String codePaysIso) {
        this.codePaysIso = codePaysIso;
    }

    public String getLibellePaysFr() {
        return libellePaysFr;
    }

    public void setLibellePaysFr(String libellePaysFr) {
        this.libellePaysFr = libellePaysFr;
    }

    public String getLibellePaysAll() {
        return libellePaysAll;
    }

    public void setLibellePaysAll(String libellePaysAll) {
        this.libellePaysAll = libellePaysAll;
    }

}
