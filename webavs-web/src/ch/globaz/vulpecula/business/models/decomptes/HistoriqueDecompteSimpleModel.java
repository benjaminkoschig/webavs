package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class HistoriqueDecompteSimpleModel extends JadeSimpleModel {
    private String id;
    private String idDecompte;
    private String date;
    private String heure;
    private String etat;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdDecompte() {
        return idDecompte;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
