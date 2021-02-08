package globaz.pavo.process.ree;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;

public class CIEcritureRee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_FEMME = "316001";
    public final static String CS_HOMME = "316000";

    private String affHist;
    private String annee;
    private String caisseChomage;
    private String codeSpecial;
    private String dateNaissance;
    private String employeur;
    private String extourne;
    private String genreEcriture;
    private String idAffilie;
    private String idTypeInscription;
    private String libelleAff;
    private String moisDebut;
    private String moisFin;
    private String nom;
    private String noNumEmployeur;
    private String numAvs;
    private String partenaireId;
    private String partenaireNomPrenom;
    private String partenaireNumAvs;
    private String pays;
    private String revenu;
    private String sexe;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        dateNaissance = CIUtil.formatDate(statement.dbReadNumeric("KADNAI"));
        sexe = statement.dbReadNumeric("KATSEX");
        pays = statement.dbReadNumeric("KAIPAY");
        numAvs = statement.dbReadString("KANAVS");
        nom = statement.dbReadString("KALNOM");
        moisFin = statement.dbReadNumeric("KBNMOF");
        moisDebut = statement.dbReadNumeric("KBNMOD");
        revenu = statement.dbReadNumeric("KBMMON", 2);
        extourne = statement.dbReadNumeric("KBTEXT");
        codeSpecial = statement.dbReadNumeric("KBTSPE");
        genreEcriture = statement.dbReadNumeric("KBTGEN");
        idTypeInscription = statement.dbReadNumeric("KCITIN");
        employeur = statement.dbReadNumeric("KBITIE");
        idAffilie = statement.dbReadString("MALNAF");
        libelleAff = statement.dbReadString("KBLIB");
        affHist = statement.dbReadString("KBIAFF");
        caisseChomage = statement.dbReadNumeric("KBICHO");
        partenaireId = statement.dbReadNumeric("KBIPAR");
        partenaireNumAvs = statement.dbReadString("PARTENAIRE_KANAVS");
        partenaireNomPrenom = statement.dbReadString("PARTENAIRE_KALNOM");
        annee = statement.dbReadNumeric("KBNANN");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAffHist() {
        return affHist;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCaisseChomage() {
        return caisseChomage;
    }

    public String getCaisseChomageFormattee() {
        return "999999" + JadeStringUtil.rightJustifyInteger(caisseChomage, 5);
    }

    public String getCodeSpecial() {
        return codeSpecial;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getEmployeur() {
        return employeur;
    }

    public String getExtourne() {
        return extourne;
    }

    public String getGenreEcriture() {
        return genreEcriture;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdTypeInscription() {
        return idTypeInscription;
    }

    public String getLibelleAff() {
        return libelleAff;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public String getNom() {
        return nom;
    }

    public String getNoNomEmployeurBis() {
        // if (!isNew()) {
        // Modif CCVS, si les champs ne sont pas vide, on les remplace par cela
        if (!JadeStringUtil.isBlankOrZero(libelleAff) && !JadeStringUtil.isBlank(affHist)) {
            return affHist;
        }
        if (!JAUtil.isIntegerEmpty(employeur)) {
            return idAffilie;
        }

        if (!JAUtil.isIntegerEmpty(partenaireId)) {
            // recherche du nom du partenaire
            if (!JAUtil.isStringEmpty(partenaireNumAvs)) {
                return NSUtil.formatAVSUnknown(partenaireNumAvs) + " " + partenaireNomPrenom;
            } else {
                try {
                    CICompteIndividuel ci = new CICompteIndividuel();
                    ci.setSession(getSession());
                    ci.setCompteIndividuelId(partenaireId);
                    if (isLoadedFromManager()) {
                        ci.setLoadedFromManager(true);
                    }
                    ci.retrieve();
                    if (!ci.isNew()) {
                        return NSUtil.formatAVSUnknown(ci.getNumeroAvs()) + " " + ci.getNomPrenom();
                    }
                } catch (Exception e) {
                    // erreur de db, on continue avec les autres test
                }
            }
        }
        if (!JAUtil.isIntegerEmpty(caisseChomage)) {
            // caisse de chômage
            return JAUtil.formatAvs(getCaisseChomageFormattee());
        }

        // aucune info, recherche du journal
        if (CIJournal.CS_APG.equals(idTypeInscription)) {
            // APG
            return "77777777777";
        }
        if (CIJournal.CS_PANDEMIE.equals(idTypeInscription)) {
            // PANDEMIE
            return "55555555555";
        }
        if (CIJournal.CS_IJAI.equals(idTypeInscription)) {
            // AI
            return "88888888888";
        }
        if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(idTypeInscription)) {
            // Militaire
            return "66666666666";
        }

        return " ";
    }

    public String getNoNumEmployeur() {
        return noNumEmployeur;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getPartenaireId() {
        return partenaireId;
    }

    public String getPartenaireNomPrenom() {
        return partenaireNomPrenom;
    }

    public String getPartenaireNumAvs() {
        return partenaireNumAvs;
    }

    public String getPays() {
        return pays;
    }

    public String getPaysCode() {
        try {
            return getSession().getCode(getPays());
        } catch (Exception e) {
            return "";
        }
    }

    public String getRevenu() {
        return revenu;
    }

    public String getSexe() {
        return sexe;
    }

    public String getSexeCode() {
        if (CIEcritureRee.CS_FEMME.equals(getSexe())) {
            return "F";
        } else {
            return "H";
        }
    }

    public void setAffHist(String affHist) {
        this.affHist = affHist;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCaisseChomage(String caisseChomage) {
        this.caisseChomage = caisseChomage;
    }

    public void setCodeSpecial(String codeSpecial) {
        this.codeSpecial = codeSpecial;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public void setExtourne(String extourne) {
        this.extourne = extourne;
    }

    public void setGenreEcriture(String genreEcriture) {
        this.genreEcriture = genreEcriture;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdTypeInscription(String idTypeInscription) {
        this.idTypeInscription = idTypeInscription;
    }

    public void setLibelleAff(String libelleAff) {
        this.libelleAff = libelleAff;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoNumEmployeur(String noNumEmployeur) {
        this.noNumEmployeur = noNumEmployeur;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setPartenaireId(String partenaireId) {
        this.partenaireId = partenaireId;
    }

    public void setPartenaireNomPrenom(String partenaireNomPrenom) {
        this.partenaireNomPrenom = partenaireNomPrenom;
    }

    public void setPartenaireNumAvs(String partenaireNumAvs) {
        this.partenaireNumAvs = partenaireNumAvs;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setRevenu(String revenu) {
        this.revenu = revenu;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
