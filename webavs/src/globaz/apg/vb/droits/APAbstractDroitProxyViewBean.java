package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.util.TypePrestation;
import globaz.globall.api.BISession;
import globaz.globall.db.BSpy;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Vector;

/**
 * @author VRE
 */
public abstract class APAbstractDroitProxyViewBean extends PRAbstractViewBeanSupport implements INSSViewBean {

    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private transient APModeEditionDroit modeEditionDroit;
    private String csEtatCivil = "";
    private String dateNaissance = "";
    private APDroitLAPG droit;
    private String idAssure = "";
    private boolean noAVSmodifieDepuisDerniereRecherche = false;
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    private String provenance = "";
    private transient Vector responsables = null;
    private String rulesToBreak = "";
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;
    private String csSexe = "";

    protected APAbstractDroitProxyViewBean(APDroitLAPG droit) {
        this.droit = droit;
        modeEditionDroit = APModeEditionDroit.LECTURE;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut cs sexe
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsSexe()
     */
    @Override
    public String getCsSexe() {
        if (JadeStringUtil.isEmpty(csSexe)) {
            csSexe = getProprieteTiers(PRTiersWrapper.PROPERTY_SEXE);
        }
        return csSexe;
    }

    /**
     * @param string
     */
    @Override
    public void setCsSexe(String string) {
        csSexe = string;
    }

    public final APModeEditionDroit getModeEditionDroit() {
        return modeEditionDroit;
    }

    public final void setModeEditionDroit(APModeEditionDroit modeEditionDroit) {
        if (JadeStringUtil.isEmpty(getDroit().getEtat()) || ((modeEditionDroit != null) && isModifiable())) {
            this.modeEditionDroit = modeEditionDroit;
        } else {
            this.modeEditionDroit = APModeEditionDroit.LECTURE;
        }
    }

    public boolean extraireNomPrenomCI(String nomComplet) throws Exception {
        if (!JadeStringUtil.isEmpty(nomComplet)) {
            nomComplet = PRStringUtils.toWordsFirstLetterUppercase(nomComplet);

            int pos = nomComplet.indexOf(',');

            if (pos > 0) {
                // on tente de splitter le nom complet en se basant sur la virgule
                setNom(nomComplet.substring(0, pos).trim());
                setPrenom(nomComplet.substring(pos + 1).trim());
            } else {
                // si pas de virgule, on met tout dans le champ nom
                setNom(nomComplet);
            }

            trouveDansCI = true;
        } else {
            trouveDansCI = false;
        }

        return trouveDansCI;
    }

    @Override
    public String getCsCantonDomicile() {
        return null;
    }

    @Override
    public String getCsEtatCivil() {
        if (JadeStringUtil.isEmpty(csEtatCivil)) {
            csEtatCivil = getProprieteTiers(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL);
        }
        return csEtatCivil;
    }

    @Override
    public String getCsNationalite() {
        return "";
    }

    public String getCsProvenanceDroitAcquis() {
        return droit.getCsProvenanceDroitAcquis();
    }

    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    public String getDateDebutDroit() {
        return droit.getDateDebutDroit();
    }

    @Override
    public String getDateDeces() {
        return null;
    }

    public String getDateDepot() {
        return droit.getDateDepot();
    }

    public String getDateFinDroit() {
        return droit.getDateFinDroit();
    }

    @Override
    public String getDateNaissance() {
        if (JadeStringUtil.isEmpty(dateNaissance)) {
            dateNaissance = getProprieteTiers(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
        }
        return dateNaissance;
    }

    public String getDateReception() {
        return droit.getDateReception();
    }

    public APDroitLAPG getDroit() {
        return droit;
    }

    public String getEtat() {
        return droit.getEtat();
    }

    public String getGenreService() {
        return droit.getGenreService();
    }

    @Override
    public String getId() {
        return droit.getId();
    }

    @Override
    public String getIdAssure() {
        return idAssure;
    }

    public String getIdCaisse() {
        return droit.getIdCaisse();
    }

    public String getIdDemande() {
        return droit.getIdDemande();
    }

    public String getIdDroit() {
        return droit.getIdDroit();
    }

    public String getIdDroitParent() {
        return droit.getIdDroitParent();
    }

    public String getIdGestionnaire() {
        if (JadeStringUtil.isEmpty(droit.getIdGestionnaire())) {
            setIdGestionnaire(getCurrentUserId());
        }
        return droit.getIdGestionnaire();
    }

    public Boolean getIsSoumisCotisation() {
        return droit.getIsSoumisImpotSource();
    }

    public String getNoDroit() {
        return droit.getNoDroit();
    }

    @Override
    public String getNom() {
        if (JadeStringUtil.isEmpty(nom)) {
            nom = getProprieteTiers(PRTiersWrapper.PROPERTY_NOM);
        }
        return nom;
    }

    public String getNpa() {
        return droit.getNpa();
    }

    @Override
    public String getNss() {
        if (JadeStringUtil.isEmpty(nss)) {
            nss = getProprieteTiers(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        }
        return nss;
    }

    public String getPays() {
        return droit.getPays();
    }

    @Override
    public String getPrenom() {
        if (JadeStringUtil.isEmpty(prenom)) {
            prenom = getProprieteTiers(PRTiersWrapper.PROPERTY_PRENOM);
        }
        return prenom;
    }

    protected String getProprieteTiers(String nom) {
        String retValue = "";

        if (!JadeStringUtil.isIntegerEmpty(droit.getIdDemande())) {
            try {
                PRDemande demande = droit.loadDemande();

                if (!JadeStringUtil.isIntegerEmpty(demande.getIdTiers())) {
                    retValue = demande.loadTiers().getProperty(nom);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // pas pu charger la demande ou le tiers
            }
        }

        return retValue;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    public String getReference() {
        return droit.getReference();
    }

    public String getRemarque() {
        return droit.getRemarque();
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {userId, userFullName} des gestionnaires pour le type de prestation
     * défini pour ce view bean.
     * <p>
     * Le vecteur n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     * 
     * @return la valeur courante de l'attribut responsable data
     */
    public final Vector getResponsableData() {
        if (responsables == null) {
            try {
                if (droit instanceof APDroitMaternite) {
                    responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(TypePrestation.TYPE_MATERNITE
                            .toGroupeGestionnaire());
                } else {
                    responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(TypePrestation.TYPE_APG
                            .toGroupeGestionnaire());
                }
            } catch (Exception e) {
                droit.getSession().addError(
                        droit.getSession().getLabel(APAbstractDroitProxyViewBean.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }
        }
        // on veut une ligne vide
        responsables.insertElementAt(new String[] { "", "" }, 0);
        return responsables;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public final String isNNSS() {
        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }
        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getRulesToBreak() {
        return rulesToBreak;
    }

    @Override
    public BSpy getSpy() {
        return droit.getSpy();
    }

    public String getTauxImpotSource() {
        return droit.getTauxImpotSource();
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    /**
     * Désactive le pspy dans la table des droits LAPG.
     * <p>
     * Il FAUT réactiver le pspy dans les classes filles !!!
     * </p>
     * 
     * @return false
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    public boolean hasSpy() {
        return droit.hasSpy();
    }

    /**
     * @return vrai si l'etat du droit est égal à {@link IAPDroitLAPG#CS_ETAT_DROIT_ATTENTE}.
     */
    public boolean isModifiable() {
        return droit.isModifiable();
    }

    public boolean isNoAVSmodifieDepuisDerniereRecherche() {
        return noAVSmodifieDepuisDerniereRecherche;
    }

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    /**
     * Charge la demande avec laquelle ce droit est lié. Cette méthode recharge automatiquement la demande si (et
     * seulement si) la valeur de Id Demande de ce bean a été modifiée.
     * 
     * @return la demande liée à ce droit (jamais nul).
     * @throws Exception
     *             si la demande ne peut être chargée.
     */
    public PRDemande loadDemande() throws Exception {
        return droit.loadDemande();
    }

    /**
     * Charge le gestionnaire associé avec cette demande.
     * <p>
     * Le gestionnaire est automatiquement rechargé si le champ idGestionnaire de ce bean est modifié.
     * </p>
     * 
     * @return Une instance de JadeUser ou null si l'idGestionnaire est vide.
     * @throws Exception
     *             s'il n'existe pas de gestionnaire avec cet identifiant.
     */
    public JadeUser loadGestionnaire() throws Exception {
        return droit.loadGestionnaire();
    }

    public void resetFlags() {
        trouveDansCI = false;
        noAVSmodifieDepuisDerniereRecherche = false;
    }

    @Override
    public void setCsCantonDomicile(String csCantonDomicile) {
    }

    @Override
    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    @Override
    public void setCsNationalite(String csNationalite) {
    }

    public void setCsProvenanceDroitAcquis(String csProvenanceDroitAcquis) {
        droit.setCsProvenanceDroitAcquis(csProvenanceDroitAcquis);
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        droit.setDateDebutDroit(dateDebutDroit);
    }

    @Override
    public void setDateDeces(String dateDeces) {
    }

    public void setDateDepot(String dateDepot) {
        droit.setDateDepot(dateDepot);
    }

    public void setDateFinDroit(String dateFinDroit) {
        droit.setDateFinDroit(dateFinDroit);
    }

    @Override
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateReception(String dateReception) {
        droit.setDateReception(dateReception);
    }

    public void setEtat(String etat) {
        droit.setEtat(etat);
    }

    public void setGenreService(String genreService) {
        droit.setGenreService(genreService);
    }

    @Override
    public void setId(String id) {
        droit.setId(id);
    }

    @Override
    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    public void setIdCaisse(String idCaisse) {
        droit.setIdCaisse(idCaisse);
    }

    public void setIdDemande(String idDemande) {
        droit.setIdDemande(idDemande);
    }

    public void setIdDroit(String idDroit) {
        droit.setIdDroit(idDroit);
    }

    public void setIdDroitParent(String idDroitParent) {
        droit.setIdDroitParent(idDroitParent);
    }

    public void setIdGestionnaire(String idGestionnaire) {
        droit.setIdGestionnaire(idGestionnaire);
    }

    @Override
    public void setISession(BISession newSession) {
        super.setISession(newSession);
        droit.setISession(newSession);
    }

    public void setIsSoumisCotisation(Boolean isSoumisCotisation) {
        droit.setIsSoumisImpotSource(isSoumisCotisation);
    }

    public void setNoDroit(String noDroit) {
        droit.setNoDroit(noDroit);
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNpa(String npa) {
        droit.setNpa(npa);
    }

    @Override
    public void setNss(String nss) {
        noAVSmodifieDepuisDerniereRecherche = noAVSmodifieDepuisDerniereRecherche || !this.nss.equals(nss);
        this.nss = nss;
    }

    public void setPays(String pays) {
        droit.setPays(pays);
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void setProvenance(String provenance) {
        this.provenance = provenance;
        if (PRUtil.PROVENANCE_TIERS.equals(provenance)) {
            trouveDansCI = false;
            trouveDansTiers = true;
        } else if (PRUtil.PROVENANCE_CI.equals(provenance)) {
            trouveDansCI = true;
            trouveDansTiers = false;
        } else {
            trouveDansCI = false;
            trouveDansTiers = false;
        }
    }

    public void setReference(String reference) {
        droit.setReference(reference);
    }

    public void setRemarque(String remarque) {
        droit.setRemarque(remarque);
    }

    public void setRulesToBreak(String rulesToBreak) {
        this.rulesToBreak = rulesToBreak;
    }

    public void setTauxImpotSource(String tauxImpotSource) {
        droit.setTauxImpotSource(tauxImpotSource);
    }

    public void setTrouveDansTiers(boolean isTrouveDansTiers) {
        trouveDansTiers = isTrouveDansTiers;
    }

    protected void setDroit(APDroitLAPG droit) {
        this.droit = droit;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
