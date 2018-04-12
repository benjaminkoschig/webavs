package globaz.orion.vb.adi;

import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.orion.vb.EBAbstractViewBean;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.Date;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.db.EBDemandeModifAcompteJoinDecisionEntity;
import ch.globaz.orion.db.EBDemandeModifAcompteMessageEntity;
import ch.globaz.orion.db.EBDemandeModifAcompteMessageManager;
import ch.globaz.orion.ws.cotisation.InfosDerniereDecisionActive;
import ch.globaz.orion.ws.cotisation.WebAvsCotisationsServiceImpl;
import ch.globaz.orion.ws.exceptions.WebAvsException;

public class EBDemandesTransmisesViewBean extends EBAbstractViewBean {
    private EBDemandeModifAcompteJoinDecisionEntity demandeTransmise;
    // private EBDemandeModifAcompteJoinDecisionEntity nextDemandeTransmise;
    private AFAffiliation affiliation;
    private CPDecision decision;
    private String revenuDeterminant;
    private String id;
    private BigDecimal capitalDecisionActuelle;
    private BigDecimal beneficeDecisionActuelle;
    private List<EBDemandeModifAcompteMessageEntity> listeErreurs = null;
    private String selectedIds = null;
    private boolean valideTheNext = false;
    private List<String> listOfSelectedIds = new LinkedList<String>();
    private boolean isAffiliationExistante = false;
    private String adresse = null;
    private String dateNaissanceTiers = null;
    private String dateDecesTiers = null;
    private String sexeTiers = null;
    private String etatCivilTiers = null;
    private List<String> listeErreursTranslated = null;
    private String duplicataDoc = null;

    public EBDemandesTransmisesViewBean() {
        super();
    }

    public EBDemandesTransmisesViewBean(EBDemandeModifAcompteJoinDecisionEntity demandeEntity) {
        super();
        demandeTransmise = demandeEntity;
        id = demandeEntity.getId();

        retrieveDatas();
    }

    private void retrieveDatas() {
        affiliation = retrieveAffiliation();
        retrieveInfoLastDecision();
        retrieveDecision();
        getErreurs();
    }

    public EBDemandeModifAcompteJoinDecisionEntity getDemandeTransmise() {
        return demandeTransmise;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void retrieve() throws Exception {
        if (!JadeStringUtil.isEmpty(selectedIds)) {
            listOfSelectedIds = new ArrayList<String>(Arrays.asList(selectedIds.split(",")));
        }

        String currentId = getCurrentId();
        demandeTransmise = retrieveDemande(currentId);
        // Suppression du premier index (courant)
        listOfSelectedIds.remove(currentId);
        selectedIds = StringUtils.join(listOfSelectedIds, ',');

        // if (getNextId() != null && !getNextId().isEmpty()) {
        // nextDemandeTransmise = retrieveDemande(getNextId());
        // } else {
        // nextDemandeTransmise = null;
        // }

        if (!demandeTransmise.isNew()) {
            retrieveDatas();
            translateListeErreurs();
        }

        if (!affiliation.isNew()) {
            isAffiliationExistante = true;

            // Recherche adresse
            TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), affiliation.getIdTiers());

            adresse = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), new TIAdresseFormater(), true,
                    getSession().getIdLangue());

            if (!tiers.getDateNaissance().isEmpty()) {
                dateNaissanceTiers = new Date(tiers.getDateNaissance()).getSwissValue();
            }

            if (!tiers.getDateDeces().isEmpty()) {
                dateDecesTiers = new Date(tiers.getDateDeces()).getSwissValue();
            } else {
                dateDecesTiers = "-";
            }
            sexeTiers = getSession().getCodeLibelle(tiers.getSexe());
            etatCivilTiers = getSession().getCodeLibelle(tiers.getEtatCivil());
        }
    }

    // public boolean hasNext() {
    // return nextDemandeTransmise != null;
    // }

    private EBDemandeModifAcompteJoinDecisionEntity retrieveDemande(String id) throws Exception {
        EBDemandeModifAcompteJoinDecisionEntity decisionEntity = new EBDemandeModifAcompteJoinDecisionEntity();
        decisionEntity.setSession(getSession());
        decisionEntity.setIdAndJustDoIt(id);
        decisionEntity.retrieve();
        return decisionEntity;
    }

    /**
     * Retourne le 1er id de la liste si présent, sinon retourne l'id
     * 
     * @return
     */
    public String getCurrentId() {
        if (!listOfSelectedIds.isEmpty()) {
            return listOfSelectedIds.get(0);
        } else {
            return id;
        }
    }

    public String getNextId() {
        if (!listOfSelectedIds.isEmpty()) {
            int index = listOfSelectedIds.indexOf(getCurrentId());
            int nextId = index + 1;
            if (nextId < listOfSelectedIds.size()) {
                return listOfSelectedIds.get(nextId);
            }
        }
        return null;
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    private AFAffiliation retrieveAffiliation() {
        try {
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setSession(getSession());
            affiliationManager.setForAffilieNumero(demandeTransmise.getNumAffilie());
            affiliationManager.find(BManager.SIZE_USEDEFAULT);

            if (affiliationManager.size() > 0) {
                return (AFAffiliation) affiliationManager.getFirstEntity();
            } else {
                throw new IllegalArgumentException("Aucune affiliation trouvée : " + demandeTransmise.getNumAffilie());
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Erreur recherche affiliation liée ", ex);
        }
    }

    private void retrieveInfoLastDecision() {
        try {
            WebAvsCotisationsServiceImpl webAvsCotisationsServiceImpl = new WebAvsCotisationsServiceImpl();
            InfosDerniereDecisionActive infosDerniereDecisionActive = webAvsCotisationsServiceImpl
                    .findInfosDerniereDecisionActive(demandeTransmise.getNumAffilie(), demandeTransmise.getAnnee());
            if (infosDerniereDecisionActive != null) {
                capitalDecisionActuelle = infosDerniereDecisionActive.getCapitalInvesti();
                beneficeDecisionActuelle = infosDerniereDecisionActive.getResultatNet();
            } else {
                capitalDecisionActuelle = new BigDecimal(0);
                beneficeDecisionActuelle = new BigDecimal(0);
            }
        } catch (WebAvsException wex) {
            throw new IllegalStateException("Erreur récupération des infos de la dernière décision active", wex);
        }
    }

    private void retrieveDecision() {
        try {
            CPDecisionManager decisionManager = new CPDecisionManager();

            decisionManager.setSession(getSession());
            decisionManager.setForIdAffiliation(demandeTransmise.getIdAffiliation());
            decisionManager.setForIsActive(true);
            decisionManager.setForAnneeDecision(demandeTransmise.getAnnee().toString());
            decisionManager.setOrder("IAANNE DESC, IAIDEC DESC");
            decisionManager.find(BManager.SIZE_USEDEFAULT);

            if (!decisionManager.isEmpty()) {
                decision = (CPDecision) decisionManager.getFirstEntity();

                String montantRevDet = getMontant(decision.getId(), ICPDonneesCalcul.CS_REV_NET);
                if (!montantRevDet.isEmpty()) {
                    revenuDeterminant = montantRevDet;
                } else {
                    revenuDeterminant = "0";
                }
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erreur récupération de la dernière décision active", ex);
        }
    }

    public String getMontant(String idDecision, String idDonnee) throws Exception {
        CPDonneesCalcul donnee = new CPDonneesCalcul();
        donnee.setSession(getSession());
        donnee.setIdDecision(idDecision);
        donnee.setIdDonneesCalcul(idDonnee);
        donnee.retrieve();
        return donnee.getMontant();
    }

    public void getErreurs() {
        EBDemandeModifAcompteMessageManager modifAcompteMessageManager = new EBDemandeModifAcompteMessageManager();
        modifAcompteMessageManager.setSession(getSession());
        modifAcompteMessageManager.setForIdDemande(Integer.valueOf(demandeTransmise.getId()));
        listeErreurs = modifAcompteMessageManager.search();
    }

    public String getDateReceptionFormate() {
        Date dateReceptionFmt = new Date(demandeTransmise.getDateReception());

        return dateReceptionFmt.getSwissValue();
    }

    public String getRecepeteurDemande() {
        return affiliation.getAffilieNumero() + " - " + affiliation.getTiers().getDesignation1() + " "
                + affiliation.getTiers().getDesignation2();
    }

    public String getRevenuNetCommuniqueFmt() {
        return formatMontantSansCentimes(demandeTransmise.getRevenu());
    }

    public String getCapitalCommuniqueFmt() {
        return formatMontantSansCentimes(demandeTransmise.getCapital());
    }

    public String getRevenuNetActuelFmt() {
        return formatMontantSansCentimes(getBeneficeDecisionActuelle());
    }

    public String getCapitalActuelFmt() {
        return formatMontantSansCentimes(getCapitalDecisionActuelle());
    }

    private String formatMontantSansCentimes(BigDecimal montant) {
        String pattern = "###,##0";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator('\'');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        return decimalFormat.format(montant.setScale(2, RoundingMode.HALF_UP));
    }

    public Boolean isRemarque() {
        return !demandeTransmise.getRemarque().isEmpty();
    }

    public Boolean isAvertissement() {
        return !listeErreurs.isEmpty();
    }

    public List<EBDemandeModifAcompteMessageEntity> getListeErreurs() {
        return listeErreurs;
    }

    public void translateListeErreurs() {
        listeErreursTranslated = new ArrayList<String>();
        for (EBDemandeModifAcompteMessageEntity err : listeErreurs) {
            listeErreursTranslated.add(getSession().getLabel(err.getMessageErreur()));
        }
    }

    public List<String> getListeErreursTranslated() {
        return listeErreursTranslated;
    }

    public String getStatut() {
        DemandeModifAcompteStatut demandeModifAcompteStatut = DemandeModifAcompteStatut.fromValue(demandeTransmise
                .getCsStatut());
        return demandeModifAcompteStatut.getLabel();

    }

    public DemandeModifAcompteStatut getStatutEnum() {
        return DemandeModifAcompteStatut.fromValue(demandeTransmise.getCsStatut());
    }

    public BigDecimal getCapitalDecisionActuelle() {
        return capitalDecisionActuelle;
    }

    public void setCapitalDecisionActuelle(BigDecimal capitalDecisionActuelle) {
        this.capitalDecisionActuelle = capitalDecisionActuelle;
    }

    public BigDecimal getBeneficeDecisionActuelle() {
        return beneficeDecisionActuelle;
    }

    public void setBeneficeDecisionActuelle(BigDecimal beneficeDecisionActuelle) {
        this.beneficeDecisionActuelle = beneficeDecisionActuelle;
    }

    public String getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }

    public boolean isValideTheNext() {
        return valideTheNext;
    }

    public void setValideTheNext(boolean valideTheNext) {
        this.valideTheNext = valideTheNext;
    }

    // public EBDemandeModifAcompteJoinDecisionEntity getNextDemandeTransmise() {
    // return nextDemandeTransmise;
    // }
    //
    // public void setNextDemandeTransmise(EBDemandeModifAcompteJoinDecisionEntity nextDemandeTransmise) {
    // this.nextDemandeTransmise = nextDemandeTransmise;
    // }

    public String getIdAffiliation() {
        return affiliation.getId();
    }

    public String getNumeroIde() {
        return affiliation.getNumeroIDE();
    }

    public String getPeriodeAffiliation() {
        String dateFin = "*";

        if (!affiliation.getDateFin().isEmpty()) {
            dateFin = affiliation.getDateFin();
        }

        return affiliation.getDateDebut() + " - " + dateFin;
    }

    public boolean isAffiliationExistante() {
        return isAffiliationExistante;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDateNaissanceTiers() {
        return dateNaissanceTiers;
    }

    public String getDateDecesTiers() {
        return dateDecesTiers;
    }

    public String getSexeTiers() {
        return sexeTiers;
    }

    public String getEtatCivilTiers() {
        return etatCivilTiers;
    }

    public CPDecision getDecision() {
        return decision;
    }

    public String getTypeDecision() {
        if (decision != null) {
            String csTypeDecision = getDecision().getTypeDecision();
            return getSession().getCodeLibelle(csTypeDecision);
        }
        return "";
    }

    public String getGenreDecision() {
        if (decision != null) {
            String csGenreDecision = getDecision().getGenreAffilie();
            return getSession().getCodeLibelle(csGenreDecision);
        }
        return "";
    }

    public String getRevenuDeterminant() {
        return revenuDeterminant;
    }

    public String getIdDecision() {
        return decision.getIdDecision();
    }

    public String getDuplicataDoc() {
        return duplicataDoc;
    }

    public void setDuplicataDoc(String duplicataDoc) {
        this.duplicataDoc = duplicataDoc;
    }

}
