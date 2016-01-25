package db;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseListViewBean;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class LAInsertionFichierViewBean extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String adresseClocalite = "";
    public String adresseCNumero = "";
    public String adresseCRue = "";
    public String adresseDlocalite = "";
    public String adresseDNumero = "";
    public String adresseDRue = "";
    public String alias = "";
    public String caisseAFDateDebut = "";
    public String caisseAFDateFin = "";
    public String caisseAFNum = "";
    public String caisseAVSDateDebut = "";
    public String caisseAVSDateFin = "";
    public String caisseAVSNum = "";
    public String caisseParent = "";
    public String formeJuridique = "";
    private String idAffiliation = "";
    public String idCLocalite = "";
    public String idDLocalite = "";
    public String idLocaliteParent = "";
    public String idParent = "";
    private String idTiers = "";
    private String langue = "";
    public String lienParent = "";
    public String localiteC = "";
    public String localiteCCode = "";
    public String localiteCodeParent = "";
    public String localiteD = "";
    public String localiteDCode = "";
    public String localiteParent = "";
    public String motif = "";
    private Boolean nnss = Boolean.FALSE;
    public String nom = "";
    public String nomParent1 = "";
    public String nomParent2 = "";
    public String nomParent3 = "";
    public String nomSuite = "";
    public String numAffilie = "";
    private String numAffilieAF = "";
    private String numAffilieAVS = "";
    public String numAffilieParent = "";
    public String numAvs = "";
    public String personnelMaisonDateDebut = "";
    public String personnelMaisonDateFin = "";
    public String prenom = "";
    public String siege = "";
    public String titre = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        try {
            BSessionUtil.checkDateGregorian(getSession(), getCaisseAVSDateDebut());
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("DATE_DEBUT_AVS_NON_VALIDE") + " : " + getCaisseAVSDateDebut());
        }
        try {
            BSessionUtil.checkDateGregorian(getSession(), getCaisseAVSDateFin());
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("DATE_FIN_AVS_NON_VALIDE") + " : " + getCaisseAVSDateFin());
        }
        try {
            BSessionUtil.checkDateGregorian(getSession(), getCaisseAFDateDebut());
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("DATE_DEBUT_AF_NON_VALIDE") + " : " + getCaisseAFDateDebut());
        }
        try {
            BSessionUtil.checkDateGregorian(getSession(), getCaisseAFDateFin());
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("DATE_FIN_AF_NON_VALIDE") + " : " + getCaisseAFDateFin());
        }
        try {
            BSessionUtil.checkDateGregorian(getSession(), getPersonnelMaisonDateDebut());
        } catch (Exception e) {
            getSession().addError(
                    getSession().getLabel("DATE_DEBUT_PERSONNEL_MAISON_NON_VALIDE") + " : "
                            + getPersonnelMaisonDateDebut());
        }
        try {
            BSessionUtil.checkDateGregorian(getSession(), getPersonnelMaisonDateFin());
        } catch (Exception e) {
            getSession()
                    .addError(
                            getSession().getLabel("DATE_FIN_PERSONNEL_MAISON_NON_VALIDE") + " : "
                                    + getPersonnelMaisonDateFin());
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAdresseClocalite() {
        return adresseClocalite;
    }

    public String getAdresseCNumero() {
        return adresseCNumero;
    }

    public String getAdresseCRue() {
        return adresseCRue;
    }

    public String getAdresseDlocalite() {
        return adresseDlocalite;
    }

    public String getAdresseDNumero() {
        return adresseDNumero;
    }

    public String getAdresseDRue() {
        return adresseDRue;
    }

    public String getAdressesLibelle(BSession objSession, int position) {
        String ad = "";
        try {
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(objSession);
            affiliation.setAffiliationId(getIdAffiliation());
            affiliation.retrieve();
            if (!affiliation.isNew()) {
                TIAvoirAdresseListViewBean listViewBean = new TIAvoirAdresseListViewBean();
                listViewBean.setSession(objSession);
                listViewBean.setForIdTiers(affiliation.getIdTiers());
                listViewBean.find();
                TIAvoirAdresse adresse = (TIAvoirAdresse) listViewBean.getEntity(position);
                TIAvoirAdresseViewBean adresseViewBean = new TIAvoirAdresseViewBean();
                adresseViewBean.setSession(objSession);
                adresseViewBean.setIdAdresse(adresse.getIdAdresse());
                adresseViewBean.retrieve();
                if (adresseViewBean.isNew()) {
                    ad += "<tr><td><strong>" + objSession.getCodeLibelle(adresse.getTypeAdresse())
                            + "</strong></td></tr>";
                    if (!JadeStringUtil.isEmpty(adresse.getDateFinRelation())) {
                        ad += "<tr><td>" + adresse.getDateDebutRelation() + " -> " + adresse.getDateFinRelation()
                                + "</td></tr>";
                    } else {
                        ad += "<tr><td>" + adresse.getDateDebutRelation() + " -> " + "..." + "</td></tr>";
                    }
                    if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse1())) {
                        ad += "<tr><td>" + adresse.getLigneAdresse1() + "</td></tr>";
                    }
                    if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse2())) {
                        ad += "<tr><td>" + adresse.getLigneAdresse2() + "</td></tr>";
                    }
                    if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse3())) {
                        ad += "<tr><td>" + adresse.getLigneAdresse3() + "</td></tr>";
                    }
                    if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse4())) {
                        ad += "<tr><td>" + adresse.getLigneAdresse4() + "</td></tr>";
                    }
                    if (!JadeStringUtil.isEmpty(adresse.getRue())) {
                        ad += "<tr><td>" + adresse.getRue() + " " + adresse.getNumeroRue() + "</td></tr>";
                    }
                    if (!JadeStringUtil.isEmpty(adresse.getIdLocalite())) {
                        TILocalite localite = new TILocalite();
                        localite.setSession(objSession);
                        localite.setIdLocalite(adresse.getIdLocalite());
                        localite.retrieve();
                        if (!localite.isNew()) {
                            ad += "<tr><td>" + localite.getNumPostal() + " " + localite.getLocaliteCourt()
                                    + "</td></tr>";
                        }
                    }
                }
            }
            return ad;
        } catch (Exception e) {
            return "";
        }

    }

    public String getAlias() {
        return alias;
    }

    public String getCaisseAFDateDebut() {
        return caisseAFDateDebut;
    }

    public String getCaisseAFDateFin() {
        return caisseAFDateFin;
    }

    public String getCaisseAFNum() {
        return caisseAFNum;
    }

    public String getCaisseAVSDateDebut() {
        return caisseAVSDateDebut;
    }

    public String getCaisseAVSDateFin() {
        return caisseAVSDateFin;
    }

    public String getCaisseAVSNum() {
        return caisseAVSNum;
    }

    public String getCaisseParent() {
        return caisseParent;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdCLocalite() {
        return idCLocalite;
    }

    public String getIdDLocalite() {
        return idDLocalite;
    }

    public String getIdLocaliteParent() {
        return idLocaliteParent;
    }

    public String getIdParent() {
        return idParent;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLangue() {
        return langue;
    }

    public String getLienParent() {
        return lienParent;
    }

    public String getLocaliteC() {
        return localiteC;
    }

    public String getLocaliteCCode() {
        return localiteCCode;
    }

    public String getLocaliteCodeParent() {
        return localiteCodeParent;
    }

    public String getLocaliteD() {
        return localiteD;
    }

    public String getLocaliteDCode() {
        return localiteDCode;
    }

    public String getLocaliteParent() {
        return localiteParent;
    }

    public String getMotif() {
        return motif;
    }

    public int getNbreAdresses(BSession objSession) {
        try {
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(objSession);
            affiliation.setAffiliationId(getIdAffiliation());
            affiliation.retrieve();
            if (!affiliation.isNew()) {
                TIAvoirAdresseListViewBean listViewBean = new TIAvoirAdresseListViewBean();
                listViewBean.setSession(objSession);
                listViewBean.setForIdTiers(affiliation.getIdTiers());
                listViewBean.find();
                return listViewBean.size();
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public Boolean getNnss() {
        return nnss;
    }

    public String getNom() {
        return nom;
    }

    public String getNomParent1() {
        return nomParent1;
    }

    public String getNomParent2() {
        return nomParent2;
    }

    public String getNomParent3() {
        return nomParent3;
    }

    public String getNomSuite() {
        return nomSuite;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAffilieAF() {
        return numAffilieAF;
    }

    public String getNumAffilieAVS() {
        return numAffilieAVS;
    }

    public String getNumAffilieParent() {
        return numAffilieParent;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumeroavsNNSS() {
        if (getNumAvs().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getParentDescription(BSession session) {
        try {
            String adresse = "";
            if (!JadeStringUtil.isEmpty(getIdParent())) {
                AFAffiliationViewBean affiliation = new AFAffiliationViewBean();
                affiliation.setSession(session);
                affiliation.setAffiliationId(getIdParent());
                affiliation.retrieve();
                if (!affiliation.isNew()) {
                    TIPersonneAvsAdresseViewBean tiersAdresse = new TIPersonneAvsAdresseViewBean();
                    tiersAdresse.setSession(session);
                    tiersAdresse.setIdTiers(affiliation.getIdTiers());
                    tiersAdresse.retrieve();
                    if (!tiersAdresse.isNew()) {
                        String rue = tiersAdresse.getRue();
                        String num = tiersAdresse.getNumero();
                        String cp = tiersAdresse.getCasePostaleComp();
                        String npa = tiersAdresse.getNpa();
                        String localite = tiersAdresse.getLocalite();
                        TITiersViewBean tiers = new TITiersViewBean();
                        tiers.setSession(session);
                        tiers.setIdTiers(affiliation.getIdTiers());
                        tiers.retrieve();
                        String designation = "";
                        if (!JadeStringUtil.isEmpty(tiers.getPrenomNom())) {
                            designation = tiers.getPrenomNom();
                        } else {
                            designation = tiers.getDesignation1();
                        }
                        adresse = designation + "\n" + rue + " " + num + "\n" + cp + "\n" + npa + " " + localite;
                    }
                }
            }
            return adresse;
        } catch (Exception e) {
            return "";
        }
    }

    public String getPartialNss() {
        return NSUtil.formatWithoutPrefixe(getNumAvs(), nnss.booleanValue());
    }

    public String getPersonnelMaisonDateDebut() {
        return personnelMaisonDateDebut;
    }

    public String getPersonnelMaisonDateFin() {
        return personnelMaisonDateFin;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSiege() {
        return siege;
    }

    public String getTitre() {
        return titre;
    }

    public void setAdresseClocalite(String adresseClocalite) {
        this.adresseClocalite = adresseClocalite;
    }

    public void setAdresseCNumero(String adresseCNumero) {
        this.adresseCNumero = adresseCNumero;
    }

    public void setAdresseCRue(String adresseCRue) {
        this.adresseCRue = adresseCRue;
    }

    public void setAdresseDlocalite(String adresseDlocalite) {
        this.adresseDlocalite = adresseDlocalite;
    }

    public void setAdresseDNumero(String adresseDNumero) {
        this.adresseDNumero = adresseDNumero;
    }

    public void setAdresseDRue(String adresseDRue) {
        this.adresseDRue = adresseDRue;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCaisseAFDateDebut(String caisseAFDateDebut) {
        this.caisseAFDateDebut = caisseAFDateDebut;
    }

    public void setCaisseAFDateFin(String caisseAFDateFin) {
        this.caisseAFDateFin = caisseAFDateFin;
    }

    public void setCaisseAFNum(String caisseAFNum) {
        this.caisseAFNum = caisseAFNum;
    }

    public void setCaisseAVSDateDebut(String caisseAVSDateDebut) {
        this.caisseAVSDateDebut = caisseAVSDateDebut;
    }

    public void setCaisseAVSDateFin(String caisseAVSDateFin) {
        this.caisseAVSDateFin = caisseAVSDateFin;
    }

    public void setCaisseAVSNum(String caisseAVSNum) {
        this.caisseAVSNum = caisseAVSNum;
    }

    public void setCaisseParent(String caisseParent) {
        this.caisseParent = caisseParent;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdCLocalite(String idCLocalite) {
        this.idCLocalite = idCLocalite;
    }

    public void setIdDLocalite(String idDLocalite) {
        this.idDLocalite = idDLocalite;
    }

    public void setIdLocaliteParent(String idLocaliteParent) {
        this.idLocaliteParent = idLocaliteParent;
    }

    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setLienParent(String lienParent) {
        this.lienParent = lienParent;
    }

    public void setLocaliteC(String localiteC) {
        this.localiteC = localiteC;
    }

    public void setLocaliteCCode(String localiteCCode) {
        this.localiteCCode = localiteCCode;
    }

    public void setLocaliteCodeParent(String localiteCodeParent) {
        this.localiteCodeParent = localiteCodeParent;
    }

    public void setLocaliteD(String localiteD) {
        this.localiteD = localiteD;
    }

    public void setLocaliteDCode(String localiteDCode) {
        this.localiteDCode = localiteDCode;
    }

    public void setLocaliteParent(String localiteParent) {
        this.localiteParent = localiteParent;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNnss(Boolean nnss) {
        this.nnss = nnss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomParent1(String nomParent1) {
        this.nomParent1 = nomParent1;
    }

    public void setNomParent2(String nomParent2) {
        this.nomParent2 = nomParent2;
    }

    public void setNomParent3(String nomParent3) {
        this.nomParent3 = nomParent3;
    }

    public void setNomSuite(String nomSuite) {
        this.nomSuite = nomSuite;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumAffilieAF(String numAffilieAF) {
        this.numAffilieAF = numAffilieAF;
    }

    public void setNumAffilieAVS(String numAffilieAVS) {
        this.numAffilieAVS = numAffilieAVS;
    }

    public void setNumAffilieParent(String numAffilieParent) {
        this.numAffilieParent = numAffilieParent;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setPersonnelMaisonDateDebut(String personnelMaisonDateDebut) {
        this.personnelMaisonDateDebut = personnelMaisonDateDebut;
    }

    public void setPersonnelMaisonDateFin(String personnelMaisonDateFin) {
        this.personnelMaisonDateFin = personnelMaisonDateFin;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
