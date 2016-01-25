package globaz.pavo.service.dto;

import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.hermes.service.dto.HEDataRemiseCA;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIExceptions;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;

/**
 * 
 * @author jeremy.wermeille
 * @since 13.02.14
 */
public class CIDataRemiseCA implements HEDataRemiseCA {

    private String adresse;
    private String anneeCot;
    private String dateNaiss;
    private String langueSingle;
    private String nAffilie;
    private String nnss;
    private String nom = "";
    private String politesse;
    private String prenom = "";
    private String single = "";

    @Override
    public String getAdresse() {
        return adresse;
    }

    @Override
    public String getAnneeCot() {
        return anneeCot;
    }

    @Override
    public String getDateNaiss() {
        return dateNaiss;
    }

    @Override
    public String getLangueSingle() {
        return langueSingle;
    }

    public String getnAffilie() {
        return nAffilie;
    }

    @Override
    public String getNAffilie() {
        return nAffilie;
    }

    @Override
    public String getNnss() {
        return nnss;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getPolitesse() {
        return politesse;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public String getSingle() {
        return single;
    }

    /**
     * Permet de peupler le DTO avec les données d'une exception CI
     * 
     * @param exceptions
     * @throws Exception
     */
    public void peupler(CIExceptions exceptions) throws Exception {

        setSingle("true");

        // Nom
        String[] nom;
        if (JadeStringUtil.isBlankOrZero(exceptions.getNomJsp())) {
            if (exceptions.getCompte() != null) {
                exceptions.setNomJsp(exceptions.getCompte().getNomPrenom());
            }
        }
        if (JadeStringUtil.indexOf(exceptions.getNomJsp(), ',') != -1) {
            nom = exceptions.getNomJsp().split(",");
        } else {
            nom = exceptions.getNomJsp().split(" ");
        }
        if (nom.length > 1) {
            setNom(nom[0]);
            setPrenom(nom[1]);
        }

        // Date de naissance
        setDateNaiss(exceptions.getDateNaissance());
        if (JadeStringUtil.isBlank(exceptions.getDateNaissance())) {
            setDateNaiss(exceptions.getCompte().getDateNaissance());
        }

        // NSS
        String nssTest = exceptions.getNumeroAvsFormateSansPrefixe().trim();
        if (nssTest.length() < 13) {
            setNnss(NSUtil.formatAVSUnknown("756" + nssTest));
        } else {
            setNnss(NSUtil.formatAVSUnknown(nssTest));
        }

        // Anne cotisation
        setAnneeCot(exceptions.getDateEnregistrement());
        if (JadeStringUtil.isBlank(exceptions.getDateEnregistrement())) {
            setAnneeCot(exceptions.getDateEngagement());
        }

        // Affilie
        setNAffilie(exceptions.getAffilie());
        AFAffiliationManager AFF_MNG = new AFAffiliationManager();
        AFAffiliation AFF;
        AFF_MNG.setSession(exceptions.getSession());
        AFF_MNG.setForAffilieNumero(exceptions.getAffilie());
        AFF_MNG.changeManagerSize(1);
        AFF_MNG.find();
        if ((AFF = (AFAffiliation) AFF_MNG.getFirstEntity()) != null) {
            // PO 9217 - Prendre date du jour au lieu de la date d'enregistrement pour l'adresse --REPORT point HNA
            setAdresse(AFF.getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    CIApplication.CS_DOMAINE_ADRESSE_CI_ARC, JACalendar.todayJJsMMsAAAA(), AFF.getAffilieNumero()));

            String titre = AFF.getTiers()
                    .getFormulePolitesse(TITiers.toLangueIso(exceptions.getLangueCorrespondance()));
            setPolitesse(titre);
            setLangueSingle(TITiers.toLangueIso(exceptions.getLangueCorrespondance()));
        }

    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnneeCot(String anneeCot) {
        this.anneeCot = anneeCot;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void setLangueSingle(String langueSingle) {
        this.langueSingle = langueSingle;
    }

    public void setNAffilie(String nAffilie) {
        this.nAffilie = nAffilie;
    }

    public void setNnss(String nnss) {
        this.nnss = nnss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPolitesse(String politesse) {
        this.politesse = politesse;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSingle(String single) {
        this.single = single;
    }

}
