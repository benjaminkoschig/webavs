/*
 * Créé le 11 février 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.utils.RFUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

/**
 * 
 * @author jje
 */
public class RFSaisieQdViewBean extends RFQdAbstractViewBean {

    private String anneeQd = "";
    private String csDegreApi = "";
    private String csEtat = "";
    // invalidité, vieillesse, survivant
    private String csGenrePCAccordee = "";
    private String csGenreQd = "";
    private String csSource = "";

    private String csTypeBeneficiaire = "";
    // Domicile, pensionnaire attention ne correspond pas au cs PC
    private String csTypePCAccordee = "";

    private String csTypeQd = "";
    private String dateCreation = "";
    private String dateDebut = "";
    private String dateDebutPCAccordee = "";
    private String dateDebutQdAssure = "";
    private String dateFin = "";
    private String dateFinPCAccordee = "";
    private String dateFinQdAssure = "";
    private String excedentPCAccordee = "";
    private String idMontantConvention = "";
    private String idPCAccordee = "";
    private String idPotDroitPC = "";
    private String idPotSousTypeDeSoin = "";
    // qd base
    private String idQd = "";
    // petite Qd
    private String idQdAssure = "";
    // grande Qd
    private String idQdPrincipale = "";
    // private String idQdPrincipaleQsAssure = "";
    // private String idSfConjoint = "";
    private String idSousTypeDeSoin = "";
    private String idTiersPersonnesPriseDansCalcul = "";
    private String idVersionDroit = "";
    private Boolean isDroitPC = Boolean.FALSE;

    private boolean isFamilleOk = false;

    private boolean isLimiteAnnuelleOk = false;

    private Boolean isPlafonnee = Boolean.TRUE;
    private Boolean isRi = Boolean.FALSE;
    private String limiteAnnuelle = "";
    private Vector<String[]> membresFamille = null;
    private transient String message = "";
    private String montantChargeRfm = "";
    private transient String msgType = "";

    private Vector<String[]> periodesValidite = null;

    // Personnes dans le calcul String[idTiers,rôle famille]
    private List<PCAMembreFamilleVO> personnesDansPlanCalculList = null;

    private String soldeExcedentPCAccordee = "";

    private String TypeRemboursementConjoint = "";
    private String TypeRemboursementRequerant = "";

    public RFSaisieQdViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    public String formatDetailRequerantDetail(String nss, String nomPrenom, String dateNaissance, String sexe,
            String nationalite) {
        return "<b>" + nss + "</b> / " + nomPrenom + " / " + dateNaissance + " / " + sexe + " / " + nationalite;
    }

    public String getAnneeQd() {
        return anneeQd;
    }

    public String getCsDegreApi() {
        return csDegreApi;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGenrePCAccordee() {
        return csGenrePCAccordee;
    }

    public String getCsGenreQd() {
        return csGenreQd;
    }

    public String getCsSource() {
        return csSource;
    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public String getCsTypePCAccordee() {
        return csTypePCAccordee;
    }

    public String getCsTypeQd() {
        return csTypeQd;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDebutPCAccordee() {
        return dateDebutPCAccordee;
    }

    public String getDateDebutQdAssure() {
        return dateDebutQdAssure;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateFinPCAccordee() {
        return dateFinPCAccordee;
    }

    public String getDateFinQdAssure() {
        return dateFinQdAssure;
    }

    public String getDetailFamille() {
        StringBuffer familleStrBuf = new StringBuffer();
        if (null != getMembresFamille()) {
            int i = 0;
            for (String[] membreCourant : getMembresFamille()) {
                if (membreCourant[8].equals(Boolean.TRUE.toString())) {
                    if (!membreCourant[0].equals(getIdTiers())) {
                        i++;
                        if (isAfficherDetail()) {
                            familleStrBuf.append("<TR><TD>"
                                    + "<em>"
                                    + getSession().getCodeLibelle(membreCourant[1])
                                    + "</em>"
                                    + " : "
                                    + formatDetailRequerantDetail(membreCourant[2], membreCourant[3] + " "
                                            + membreCourant[4], membreCourant[5],
                                            RFUtils.getLibelleCourtSexe(membreCourant[6]),
                                            RFUtils.getLibellePays(membreCourant[7], getSession())) + "</TD></TR>");

                        } else {
                            familleStrBuf.append("<TR><TD><INPUT type=\"checkbox\" name=\"membreFamille_"
                                    + i
                                    + "\" value=\""
                                    + membreCourant[0]
                                    + "\" checked=\"checked\" /></TD><TD>"
                                    + "<em>"
                                    + getSession().getCodeLibelle(membreCourant[1])
                                    + "</em>"
                                    + " : "
                                    + formatDetailRequerantDetail(membreCourant[2], membreCourant[3] + " "
                                            + membreCourant[4], membreCourant[5],
                                            RFUtils.getLibelleCourtSexe(membreCourant[6]),
                                            RFUtils.getLibellePays(membreCourant[7], getSession())) + "</TD></TR>");
                        }
                    }
                }
            }
        }

        return familleStrBuf.toString();
    }

    public String getExcedentPCAccordee() {
        return excedentPCAccordee;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdMontantConvention() {
        return idMontantConvention;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdPotDroitPC() {
        return idPotDroitPC;
    }

    public String getIdPotSousTypeDeSoin() {
        return idPotSousTypeDeSoin;
    }

    public String getIdQd() {
        return idQd;
    }

    public String getIdQdAssure() {
        return idQdAssure;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public String getIdTiersPersonnesPriseDansCalcul() {
        return idTiersPersonnesPriseDansCalcul;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getImageDroitPC() {
        if (isDroitPC.booleanValue()) {
            return getImageSuccess();
        } else {
            return getImageError();
        }
    }

    @Override
    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageLimiteAnnuelle() {
        if (isLimiteAnnuelleOk) {
            return getImageSuccess();
        } else {
            return getImageError();
        }
    }

    @Override
    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    public Boolean getIsDroitPC() {
        return isDroitPC;
    }

    public boolean getIsFamilleOk() {
        return isFamilleOk;
    }

    public boolean getIsLimiteAnnuelleOk() {
        return isLimiteAnnuelleOk;
    }

    public Boolean getIsPlafonnee() {
        return isPlafonnee;
    }

    public Boolean getIsRi() {
        return isRi;
    }

    public String getLimiteAnnuelle() {
        return limiteAnnuelle;
    }

    /**
     * 
     * @return String[IdTiers, RelationAuRequerant, Nss, Nom, Prénom, DateNaissance, CsSexe, CsNationalite,
     *         isComprisDansCalcul]
     */
    public Vector<String[]> getMembresFamille() {
        return membresFamille;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMontantChargeRfm() {
        return montantChargeRfm;
    }

    /*
     * public String getIdQdPrincipaleQsAssure() { return this.idQdPrincipaleQsAssure; }
     */

    @Override
    public String getMsgType() {
        return msgType;
    }

    public Vector<String[]> getPeriodesValidite() {
        return periodesValidite;
    }

    public List<PCAMembreFamilleVO> getPersonnesDansPlanCalculList() {
        return personnesDansPlanCalculList;
    }

    public String getSoldeExcedentPCAccordee() {
        return soldeExcedentPCAccordee;
    }

    public String getTypeRemboursementConjoint() {
        return TypeRemboursementConjoint;
    }

    public String getTypeRemboursementRequerant() {
        return TypeRemboursementRequerant;
    }

    public boolean hasMembreFamille() {
        return (getMembresFamille() != null) && (getMembresFamille().size() > 1);
    }

    /**
     * Méthode remontant le détail du conjoint et du requerant
     * 
     * @param FWViewBeanInterface
     *            , BSession
     * @throws Exception
     */
    public void rechercheDetailRequerantFamille() throws Exception {

        try {
            RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = new RFAssQdDossierJointDossierJointTiersManager();
            rfAssQdDossierJointDossierJointTiersMgr.setSession(getSession());
            rfAssQdDossierJointDossierJointTiersMgr.setForIdQd(idQd);
            // rfAssQdDossierJointDossierJointTiersMgr.setForCsTypeRelation(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT);
            rfAssQdDossierJointDossierJointTiersMgr.changeManagerSize(0);
            rfAssQdDossierJointDossierJointTiersMgr.find();

            if (rfAssQdDossierJointDossierJointTiersMgr.size() > 0) {

                Iterator<RFAssQdDossierJointDossierJointTiers> rfAssQdDossierJointDossierJointTiersIter = rfAssQdDossierJointDossierJointTiersMgr
                        .iterator();

                Vector<String[]> membresFamilleVec = new Vector<String[]>();

                while (rfAssQdDossierJointDossierJointTiersIter.hasNext()) {

                    RFAssQdDossierJointDossierJointTiers rfAssQdDossierJointDossierJointTiers = rfAssQdDossierJointDossierJointTiersIter
                            .next();

                    if (null != rfAssQdDossierJointDossierJointTiers) {
                        if (rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul().booleanValue()) {
                            membresFamilleVec.add(RFUtils.getMembreFamilleTabString(
                                    rfAssQdDossierJointDossierJointTiers.getIdTiers(),
                                    rfAssQdDossierJointDossierJointTiers.getTypeRelation(),
                                    rfAssQdDossierJointDossierJointTiers.getNss(),
                                    rfAssQdDossierJointDossierJointTiers.getNom(),
                                    rfAssQdDossierJointDossierJointTiers.getPrenom(),
                                    rfAssQdDossierJointDossierJointTiers.getDateNaissance(),
                                    rfAssQdDossierJointDossierJointTiers.getCsSexe(),
                                    rfAssQdDossierJointDossierJointTiers.getCsNationalite(),
                                    rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul()));

                            if (rfAssQdDossierJointDossierJointTiers.getTypeRelation().equals(
                                    IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {

                                setIdTiers(rfAssQdDossierJointDossierJointTiers.getIdTiers());
                                setNss(rfAssQdDossierJointDossierJointTiers.getNss());
                                setNom(rfAssQdDossierJointDossierJointTiers.getNom());
                                setPrenom(rfAssQdDossierJointDossierJointTiers.getPrenom());
                                setDateNaissance(rfAssQdDossierJointDossierJointTiers.getDateNaissance());
                                setCsSexe(rfAssQdDossierJointDossierJointTiers.getCsSexe());
                                setCsNationalite(rfAssQdDossierJointDossierJointTiers.getCsNationalite());

                            }
                        }
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieQdViewBean");
                    }

                }

                setMembresFamille(membresFamilleVec);

            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieQdViewBean");
            }
        } catch (Exception e) {
            RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieQdViewBean");
        }
    }

    public void recherchePeriodesValidite() throws Exception {

        periodesValidite = new Vector<String[]>();

        RFPeriodeValiditeQdPrincipaleManager rfPeriodeValiditeQdPrincipaleMgr = new RFPeriodeValiditeQdPrincipaleManager();
        rfPeriodeValiditeQdPrincipaleMgr.setSession(getSession());
        rfPeriodeValiditeQdPrincipaleMgr.setForIdQd(idQd);
        rfPeriodeValiditeQdPrincipaleMgr.setForDerniereVersion(true);
        rfPeriodeValiditeQdPrincipaleMgr.setOrderByDateDebutAsc(true);
        rfPeriodeValiditeQdPrincipaleMgr.changeManagerSize(0);
        rfPeriodeValiditeQdPrincipaleMgr.find();

        Iterator<RFPeriodeValiditeQdPrincipale> rfPeriodeValiditeQdPrincipaleIter = rfPeriodeValiditeQdPrincipaleMgr
                .iterator();

        while (rfPeriodeValiditeQdPrincipaleIter.hasNext()) {
            String[] periodeValidite = new String[3];

            RFPeriodeValiditeQdPrincipale rfPeriodeValiditeQdPrincipale = rfPeriodeValiditeQdPrincipaleIter.next();

            periodeValidite[0] = rfPeriodeValiditeQdPrincipale.getIdPeriodeValidite();
            periodeValidite[1] = rfPeriodeValiditeQdPrincipale.getDateDebut();
            periodeValidite[2] = rfPeriodeValiditeQdPrincipale.getDateFin();

            periodesValidite.add(periodeValidite);
        }

    }

    @Override
    public void retrieve() throws Exception {

        // Recherche de la Qd
        if (IRFQd.CS_GRANDE_QD.equals(getCsGenreQd())) {

            RFQdPrincipale rfQdPrincipal = new RFQdPrincipale();
            rfQdPrincipal.setSession(getSession());
            rfQdPrincipal.setHasCreationSpy(true);
            rfQdPrincipal.setHasSpy(true);
            rfQdPrincipal.setIdQdPrincipale(getIdQd());

            rfQdPrincipal.retrieve();

            if (!rfQdPrincipal.isNew()) {
                setCsEtat(rfQdPrincipal.getCsEtat());
                setCsGenrePCAccordee(rfQdPrincipal.getCsGenrePCAccordee());
                setCsGenreQd(rfQdPrincipal.getCsGenreQd());
                setCsSource(rfQdPrincipal.getCsSource());
                setCsTypePCAccordee(rfQdPrincipal.getCsTypePCAccordee());
                setCsTypeBeneficiaire(rfQdPrincipal.getCsTypeBeneficiaire());
                setTypeRemboursementRequerant(rfQdPrincipal.getRemboursementRequerant());
                setTypeRemboursementConjoint(rfQdPrincipal.getRemboursementConjoint());
                setDateCreation(rfQdPrincipal.getDateCreation());
                setDateDebutPCAccordee(rfQdPrincipal.getDateDebutPCAccordee());
                setDateFinPCAccordee(rfQdPrincipal.getDateFinPCAccordee());
                setAnneeQd(rfQdPrincipal.getAnneeQd());
                setId(rfQdPrincipal.getId());
                setIdGestionnaire(rfQdPrincipal.getIdGestionnaire());
                setIdPCAccordee(rfQdPrincipal.getIdPCAccordee());
                setIdQd(rfQdPrincipal.getIdQd());
                setIdQdPrincipale(rfQdPrincipal.getIdQdPrincipale());
                setIsPlafonnee(rfQdPrincipal.getIsPlafonnee());
                setLimiteAnnuelle(rfQdPrincipal.getLimiteAnnuelle());
                setMontantChargeRfm(rfQdPrincipal.getMontantChargeRfm());
                setExcedentPCAccordee(rfQdPrincipal.getExcedentPCAccordee());
                setCsDegreApi(rfQdPrincipal.getCsDegreApi());
                setSpy(rfQdPrincipal.getSpy());
                setCreationSpy(rfQdPrincipal.getCreationSpy());
                setIsDroitPC(JadeStringUtil.isBlankOrZero(rfQdPrincipal.getDateDebutPCAccordee()) ? Boolean.FALSE
                        : Boolean.TRUE);
                setIsRi(rfQdPrincipal.getIsRI());
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieQdViewBean");
            }

        } else if (IRFQd.CS_PETITE_QD.equals(getCsGenreQd())) {

            RFQdAssure rfQdAssure = new RFQdAssure();
            rfQdAssure.setSession(getSession());
            rfQdAssure.setHasCreationSpy(true);
            rfQdAssure.setHasSpy(true);
            rfQdAssure.setIdQdAssure(getIdQd());

            rfQdAssure.retrieve();

            if (!rfQdAssure.isNew()) {
                setCsEtat(rfQdAssure.getCsEtat());
                setCsGenreQd(rfQdAssure.getCsGenreQd());
                setCsSource(rfQdAssure.getCsSource());
                // this.setCsTypeQd(rfQdAssure.getCsTypeQd());
                setDateCreation(rfQdAssure.getDateCreation());
                setDateDebut(rfQdAssure.getDateDebut());
                setDateFin(rfQdAssure.getDateFin());
                setAnneeQd(rfQdAssure.getAnneeQd());
                setId(rfQdAssure.getId());
                setIdGestionnaire(rfQdAssure.getIdGestionnaire());
                // this.setIdMontantConvention(rfQdAssure.getIdMontantConvention());
                setIdQd(rfQdAssure.getIdQd());
                setIdQdAssure(rfQdAssure.getIdQdAssure());
                // this.setIdQdPrincipale(rfQdAssure.getIdQdPrincipale());
                setIdPotSousTypeDeSoin(rfQdAssure.getIdPotSousTypeDeSoin());
                setIsPlafonnee(rfQdAssure.getIsPlafonnee());
                setLimiteAnnuelle(rfQdAssure.getLimiteAnnuelle());
                setMontantChargeRfm(rfQdAssure.getMontantChargeRfm());
                setSpy(rfQdAssure.getSpy());
                setCreationSpy(rfQdAssure.getCreationSpy());
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieQdViewBean");
            }
        }

    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    /*
     * public String getIdQdPrincipalePetiteQd() { return idQdPrincipalePetiteQd; }
     * 
     * public void setIdQdPrincipalePetiteQd(String idQdPrincipalePetiteQd) { this.idQdPrincipalePetiteQd =
     * idQdPrincipalePetiteQd; }
     */

    public void setCsDegreApi(String csDegreApi) {
        this.csDegreApi = csDegreApi;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGenrePCAccordee(String csGenrePCAccordee) {
        this.csGenrePCAccordee = csGenrePCAccordee;
    }

    public void setCsGenreQd(String csGenreQd) {
        this.csGenreQd = csGenreQd;
    }

    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setCsTypePCAccordee(String csTypePCAccordee) {
        this.csTypePCAccordee = csTypePCAccordee;
    }

    public void setCsTypeQd(String csTypeQd) {
        this.csTypeQd = csTypeQd;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDebutPCAccordee(String dateDebutPCAccordee) {
        this.dateDebutPCAccordee = dateDebutPCAccordee;
    }

    public void setDateDebutQdAssure(String dateDebutQdAssure) {
        this.dateDebutQdAssure = dateDebutQdAssure;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateFinPCAccordee(String dateFinPCAccordee) {
        this.dateFinPCAccordee = dateFinPCAccordee;
    }

    public void setDateFinQdAssure(String dateFinQdAssure) {
        this.dateFinQdAssure = dateFinQdAssure;
    }

    public void setExcedentPCAccordee(String excedentPCAccordee) {
        this.excedentPCAccordee = excedentPCAccordee;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    public void setIdMontantConvention(String idMontantConvention) {
        this.idMontantConvention = idMontantConvention;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdPotDroitPC(String idPotDroitPC) {
        this.idPotDroitPC = idPotDroitPC;
    }

    public void setIdPotSousTypeDeSoin(String idPotSousTypeDeSoin) {
        this.idPotSousTypeDeSoin = idPotSousTypeDeSoin;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIdQdAssure(String idQdAssure) {
        this.idQdAssure = idQdAssure;
    }

    /*
     * public void setIdQdPrincipaleQsAssure(String idQdPrincipaleQsAssure) { this.idQdPrincipaleQsAssure =
     * idQdPrincipaleQsAssure; }
     */

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdTiersPersonnesPriseDansCalcul(String idTiersPersonnesPriseDansCalcul) {
        this.idTiersPersonnesPriseDansCalcul = idTiersPersonnesPriseDansCalcul;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsDroitPC(Boolean isDroitPC) {
        this.isDroitPC = isDroitPC;
    }

    public void setIsFamilleOk(boolean isFamilleOk) {
        this.isFamilleOk = isFamilleOk;
    }

    public void setIsLimiteAnnuelleOk(boolean isLimiteAnnuelleOk) {
        this.isLimiteAnnuelleOk = isLimiteAnnuelleOk;
    }

    public void setIsPlafonnee(Boolean isPlafonnee) {
        this.isPlafonnee = isPlafonnee;
    }

    public void setIsRi(Boolean isRi) {
        this.isRi = isRi;
    }

    public void setLimiteAnnuelle(String limiteAnnuelle) {
        this.limiteAnnuelle = limiteAnnuelle;
    }

    public void setLimiteAnnuelleOk(boolean isLimiteAnnuelleOk) {
        this.isLimiteAnnuelleOk = isLimiteAnnuelleOk;
    }

    public void setMembresFamille(Vector<String[]> membresFamille) {
        this.membresFamille = membresFamille;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setMontantChargeRfm(String montantChargeRfm) {
        this.montantChargeRfm = montantChargeRfm;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setPeriodesValidite(Vector<String[]> periodesValidite) {
        this.periodesValidite = periodesValidite;
    }

    public void setPersonnesDansPlanCalculList(List<PCAMembreFamilleVO> personnesDansPlanCalculList) {
        this.personnesDansPlanCalculList = personnesDansPlanCalculList;
    }

    public void setSoldeExcedentPCAccordee(String soldeExcedentPCAccordee) {
        this.soldeExcedentPCAccordee = soldeExcedentPCAccordee;
    }

    public void setTypeRemboursementConjoint(String typeRemboursementConjoint) {
        TypeRemboursementConjoint = typeRemboursementConjoint;
    }

    public void setTypeRemboursementRequerant(String typeRemboursementRequerant) {
        TypeRemboursementRequerant = typeRemboursementRequerant;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}