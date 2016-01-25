package globaz.hera.vb.famille;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SCR
 * 
 *         Value object pour stocker les infos de chaque membre de la famille.
 */

public class SFMembreVO {

    private String csDomaine = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String idMembreFamille = "";
    private String idTiers = "";
    private String nom = "";
    private String nssFormatte = "";
    // Les périodes sont renseignée pour le requérant et les enfants uniquement.
    // Les péridoes des conjoints sont dans SFConjointVO
    private List periodes = null;

    private String prenom = "";

    public void addPeriode(SFPeriodeVO per) {
        if (periodes == null) {
            periodes = new ArrayList();
        }
        periodes.add(per);
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getImgName() {
        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            if (JadeStringUtil.isBlankOrZero(dateDeces)) {
                return "father.png";
            } else {
                return "fatherDead.png";
            }

        } else {
            if (JadeStringUtil.isBlankOrZero(dateDeces)) {
                return "mother.png";
            } else {
                return "motherDead.png";
            }
        }
    }

    public String getLibelleDomaine(BSession session) {
        return session.getCodeLibelle(csDomaine);
    }

    public String getLibelleNationalite(BSession session) {
        return session.getCodeLibelle(session.getSystemCode("CIPAYORI", csNationalite));
    }

    public String getLibelleSexe(BSession session) {
        return session.getCodeLibelle(csSexe);
    }

    public String getNom() {
        return nom;
    }

    public String getNssFormatte() {
        return nssFormatte;
    }

    public List getPeriodes() {
        return periodes;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsNationalite(String elm) {
        csNationalite = elm;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNssFormatte(String nssFormatte) {
        this.nssFormatte = nssFormatte;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
