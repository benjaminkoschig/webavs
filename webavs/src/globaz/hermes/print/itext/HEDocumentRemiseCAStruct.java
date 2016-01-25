package globaz.hermes.print.itext;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import ch.globaz.orion.business.models.acl.EBAttestationComparatorInterface;

public class HEDocumentRemiseCAStruct implements EBAttestationComparatorInterface {

    private String adresse = "";
    private AFAffiliation affiliation = null;
    private String anneeCot = "";
    private String dateEnregistrement = "";
    private String dateNaiss = "";
    private String Id = "";
    private boolean isEmployeur = false;
    private boolean isEmpty = false;
    private boolean isInde = false;
    private boolean isRentier = false;
    private String langue = "";
    private String motif = "";
    private String nAffilie = "";
    private String nnss = "";
    private String nom = "";

    private String numeroEmploye = "";
    private String numeroSuccursale = "";
    private String politesse = "";
    private String formulePolitesseSpecifique = "";
    private String prenom = "";
    private String reference = "";
    private String sexe = "";
    private String user = "";

    public HEDocumentRemiseCAStruct() {
    }

    public String getAdresse() {
        return adresse;
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public String getAnneeCot() {

        if (JadeStringUtil.isBlank(anneeCot)) {
            try {
                anneeCot = String.valueOf(JACalendar.getYear(JACalendar.today().toString()));
            } catch (JAException e) {
                return "0000";
            }
        } else if (JadeStringUtil.contains(anneeCot, "00.00")) {
            anneeCot = anneeCot.substring(6);
        }
        return anneeCot;
    }

    public String getCodeLangue() {
        if ("fr".equals(getLangue())) {
            return "F";
        }
        if ("de".equals(getLangue())) {
            return "D";
        }
        if ("it".equals(getLangue())) {
            return "I";
        }
        return "F";
    }

    public String getDateEnregistrement() {
        return dateEnregistrement;
    }

    public String getDateNaiss() {

        // Pour l'impression depuis PAVO, on passe la date en format dd.mm.yyyy,
        // car elle est complète
        if (dateNaiss.length() > 6) {
            return dateNaiss;
        }
        JADate date = null;
        try {
            // convertir la date en fonction de l'année courante
            int anneeCourante = (JACalendar.today().getYear() % 100);
            date = new JADate(dateNaiss, anneeCourante);
        } catch (JAException e) {
            return "00.00.0000";
        }
        return date.toStr(".");
    }

    public String getId() {
        return Id;
    }

    public String getLangue() {
        return langue;
    }

    public String getMotif() {
        return motif;
    }

    public String getNAffilie() {
        return nAffilie;
    }

    public String getNnss() {
        return nnss;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getNoSuccursale() {
        return getNumeroSuccursale();
    }

    @Override
    public String getNumeroAffilie() {
        return getNAffilie();
    }

    public String getNumeroAssure() {
        return getNumeroAssure();
    }

    public String getNumeroEmploye() {
        return numeroEmploye;
    }

    public String getNumeroSuccursale() {
        return numeroSuccursale;
    }

    public String getPolitesse() {
        return politesse;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getReference() {
        return reference;
    }

    public String getSexe() {
        return sexe;
    }

    @Override
    public String getUser() {
        return user;
    }

    public boolean isEmployeur() {
        return isEmployeur;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isInde() {
        return isInde;
    }

    public boolean isRentier() {
        return isRentier;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setAnneeCot(String anneeCot) {
        this.anneeCot = anneeCot;
    }

    public void setDateEnregistrement(String dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void setEmployeur(boolean isEmployeur) {
        this.isEmployeur = isEmployeur;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setInde(boolean isInde) {
        this.isInde = isInde;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNAffilie(String affilie) {
        nAffilie = affilie;
    }

    public void setNnss(String nnss) {
        this.nnss = nnss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumeroEmploye(String numeroEmploye) {
        this.numeroEmploye = numeroEmploye;
    }

    public void setNumeroSuccursale(String numeroSuccursale) {
        this.numeroSuccursale = numeroSuccursale;
    }

    public void setPolitesse(String politesse) {
        this.politesse = politesse;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setRentier(boolean isRentier) {
        this.isRentier = isRentier;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Getter de formulePolitesseSpecifique
     * 
     * @return the formulePolitesseSpecifique
     */
    public String getFormulePolitesseSpecifique() {
        return formulePolitesseSpecifique;
    }

    /**
     * Setter de formulePolitesseSpecifique
     * 
     * @param formulePolitesseSpecifique the formulePolitesseSpecifique to set
     */
    public void setFormulePolitesseSpecifique(String formulePolitesseSpecifique) {
        this.formulePolitesseSpecifique = formulePolitesseSpecifique;
    }

}
