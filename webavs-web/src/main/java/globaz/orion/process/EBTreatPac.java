package globaz.orion.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.process.AFCalculRetroactifProcess;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.businessimpl.services.pac.PacServiceImpl;
import ch.globaz.xmlns.eb.pac.MassePourAVS;
import ch.globaz.xmlns.eb.pac.MassesMensuellesPourAVS;
import ch.globaz.xmlns.eb.pac.StatusSaisiePAC;

/**
 * Calcule les cotisation rétroactives
 * 
 * @author hna
 */
public class EBTreatPac extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = 1L;
    private String dateChangementAvs = "";
    private IFAPassage dernierPassagePeriodiqueParitaire = null;
    private List<Map<String, String>> listRecapPourImpression = new ArrayList<Map<String, String>>();
    private String periodicite = "";
    boolean premiereMasse = true;

    @Override
    protected void _executeCleanUp() {
        // do nothing
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // Récupération des masses saisies dans eBusiness
            List<MassesMensuellesPourAVS> saisiesDeMasses = PacServiceImpl.listPacSaisies(getSession());

            // Pas de saisie, on fait rien
            if (saisiesDeMasses.isEmpty()) {
                return true;
            }

            // Recherche masse annuelle max pour test changement de prériodicité mensuelle
            BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle = ((EBApplication) getSession().getApplication())
                    .getMasseAnnuelleMaxPourPeriodiciteAnnuelle();
            // Recherche de la dernière périodique
            setDernierPassagePeriodiqueParitaire(ServicesFacturation.getDernierPassageFacturation(getSession(),
                    getTransaction(), FAModuleFacturation.CS_MODULE_PERIODIQUE_PARITAIRE));
            // Lecture des cotisations à mettre à jour venant de eBusiness
            for (int i = 0; (i < saisiesDeMasses.size()) && (isAborted() == false); i++) {
                Map<String, String> recapPrevision = new HashMap<String, String>();
                MassesMensuellesPourAVS prevision = saisiesDeMasses.get(i);
                boolean changementPeriodicite = false;
                // Mise à jour du status traitement dans eBusiness
                PacServiceImpl.changeStatus(prevision.getIdSaisieDeMassesEBu(), StatusSaisiePAC.EN_COURS, getSession());
                // Tester les erreurs jusqu'à la prochaine affiliation. Evite ainsi d'avoir x fois le même message
                // pour le même cas. Les erreurs sont remise à zéro lorsque l'on change de numréro d'affilié
                recapPrevision.put(EBImprimerPrevisionAcompte.NUM_AFFILIE, prevision.getNumeroAffilieFormatte());
                recapPrevision.put(EBImprimerPrevisionAcompte.NOM, prevision.getNomAffilie());
                premiereMasse = true;
                try {
                    boolean masseDifferenteDeZero = false;
                    for (MassePourAVS uneMasse : prevision.getMassesAVS()) {
                        // Indiquer dans la liste récapitulative si la nouvelle masse AVS est supèrieure à la masse pour
                        // périodicité mensuelle (properties)
                        // Test si la cotisation est toujours valide depuis la saisie dans eBusiness
                        String nouvelleDate = JadeDateUtil.getGlobazFormattedDate(uneMasse.getDateNouvelleMasse()
                                .toGregorianCalendar().getTime());
                        AFCotisation cotisation = controleDonneeLueAvecCotisation(
                                uneMasse.getIdCotisation().toString(), nouvelleDate);

                        // sauvegarder une seule fois l'info des masses... normalement l'avs car elle est traitée en
                        // premier
                        // Evite ainsi d'avoir la masse AF au lieu de l'AVS dans la liste récapitulative
                        saveInfoPremiereMasse(recapPrevision, uneMasse, getSession(), cotisation, nouvelleDate);

                        changementPeriodicite = indiquerChangementEnMasseMensuelle(
                                masseAnnuelleMaxPourPeriodiciteAnnuelle, uneMasse, recapPrevision,
                                changementPeriodicite, cotisation);
                        // Tester si une masse est > 0 pour mettre motif de fin sans personnel, sinon AC2 à toujours ce
                        // motif en
                        // cas e changement de masse
                        if (masseDifferenteDeZero == false
                                && !JadeStringUtil.isBlankOrZero(uneMasse.getNouvelleMasse().toString())) {
                            masseDifferenteDeZero = true;
                        }
                        // Mise à jour de la masse
                        if (!getTransaction().hasErrors()) {
                            updateMasseAnnuelle(changementPeriodicite, nouvelleDate, uneMasse, cotisation, prevision,
                                    masseDifferenteDeZero);
                        }
                    }
                } catch (Exception e) {
                    this._addError(getTransaction(), e.getMessage());
                }
                // Commit par id affiliation ou mise à jour du status Erreur pour eBusiness
                traiterAffilie(prevision.getIdAffiliation().toString(), recapPrevision, listRecapPourImpression,
                        prevision.getIdSaisieDeMassesEBu(), changementPeriodicite);
            }
            // Liste récapitulative des prévisions d'acomptes traitées
            listeExcelPrevisionAcompte(listRecapPourImpression, masseAnnuelleMaxPourPeriodiciteAnnuelle);

            return true;
        } catch (Error e) {
            e.printStackTrace();
            this._addError(getTransaction(), e.toString());
            // this.getMemoryLog().logMessage(e.getMessage().toString(), FWMessage.ERREUR, this.toString());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            this._addError(getTransaction(), e.toString());
            // this.getMemoryLog().logMessage(e.getMessage().toString(), FWMessage.ERREUR, this.toString());
            return false;
        }
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (getSession().hasErrors()) {
            abort();
        }
    }

    protected AFCotisation controleDonneeLueAvecCotisation(String idCoti, String dateNouvelleMasse) throws Exception {
        AFCotisation cotisation = new AFCotisation();
        cotisation.setCotisationId(idCoti);
        cotisation.setSession(getSession());
        cotisation.retrieve();
        if (!cotisation.isNew()) {
            // Test si date de modification souhaitée toujours valide par rapport à la cotisation
            if ((!JadeStringUtil.isBlankOrZero(cotisation.getDateFin()) && BSessionUtil.compareDateFirstGreaterOrEqual(
                    getSession(), dateNouvelleMasse, cotisation.getDateFin()))
                    || BSessionUtil.compareDateFirstLower(getSession(), dateNouvelleMasse, cotisation.getDateDebut())) {
                this._addError(getTransaction(), getSession().getLabel("PREVISIONACOMPTE_HORSPERIODE"));
            }
        } else {
            this._addError(getTransaction(), getSession().getLabel("EVENTAIL_REGIME_AUCUNE_COTI"));
        }
        return cotisation;
    }

    private int determinerMoislancementPourCalculRetro(boolean changementPeriodicite, int moisPassage) {
        int moisLancementCalculRetro = 0;
        if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(getPeriodicite()) || changementPeriodicite) {
            moisLancementCalculRetro = moisPassage;
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(getPeriodicite())) {
            if (moisPassage < 3) {
                moisLancementCalculRetro = 0;
            } else if (moisPassage < 6) {
                moisLancementCalculRetro = 3;
            } else if (moisPassage < 9) {
                moisLancementCalculRetro = 6;
            } else if (moisPassage < 12) {
                moisLancementCalculRetro = 9;
            } else {
                moisLancementCalculRetro = 12;
            }
        }
        return moisLancementCalculRetro;
    }

    /**
     * Permet la génération d'un relevé rétro actif
     * 
     * @param idAffiliation Id de l'affiliation
     * @param debutCalculRevele La date de début du calcul (format : mm.aaaa)
     * @param periodeCalcul La date de fin de calcul (format : mm.aaaa)
     * @throws Exception
     */
    protected void genererRelevePourRetroActif(String idAffiliation, String debutCalculRevele, String periodeCalcul)
            throws Exception {
        // lancement du processus de création des relevés pour les cas rétro actifs.
        AFCalculRetroactifProcess calculRetro = new AFCalculRetroactifProcess();
        calculRetro.setParentWithCopy(this);
        calculRetro.setIdAffiliation(idAffiliation);
        calculRetro.setDateCalcul(periodeCalcul);
        calculRetro.setDateDebutCalculRevele(debutCalculRevele);
        calculRetro.setCompenserMontantNegatif(Boolean.FALSE);
        calculRetro.setCompenserMontantPositif(Boolean.FALSE);
        calculRetro.setPrevisionAcompteEbu(Boolean.TRUE);
        calculRetro.setProcessExterne(true);
        calculRetro.executeProcess();
    }

    public String getDateChangementAvs() {
        return dateChangementAvs;
    }

    public IFAPassage getDernierPassagePeriodiqueParitaire() {
        return dernierPassagePeriodiqueParitaire;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("PREVISION_ACOMPTE_KO");
        } else {
            return getSession().getLabel("PREVISION_ACOMPTE_OK");
        }
    }

    public String getPeriodicite() {
        return periodicite;
    }

    protected boolean indiquerChangementEnMasseMensuelle(BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle,
            MassePourAVS uneMasse, Map<String, String> recapPrevision, boolean changementPeriodicite,
            AFCotisation cotisation) {
        if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(uneMasse.getTypeCotisation().toString())
                && (uneMasse.getNouvelleMasse().compareTo(masseAnnuelleMaxPourPeriodiciteAnnuelle) == 1)) {
            recapPrevision.put(EBImprimerPrevisionAcompte.SUP_MASSE_MAX_AUTORISEE, "X");
            if (cotisation.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_MENSUELLE) == false) {
                changementPeriodicite = true;
            }
        } else {
            recapPrevision.put(EBImprimerPrevisionAcompte.SUP_MASSE_MAX_AUTORISEE, "");
        }
        return changementPeriodicite;
    }

    protected boolean indiquerReleveAcontroler(Map<String, String> recapPrevision) throws Exception {
        AFApercuReleveManager releveManager = new AFApercuReleveManager();
        releveManager.setSession(getSession());
        releveManager.setForAffilieNumero(recapPrevision.get(EBImprimerPrevisionAcompte.NUM_AFFILIE));
        releveManager.setForEtat(CodeSystem.ETATS_RELEVE_SAISIE);
        releveManager.find();
        if (releveManager.getSize() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    protected void listeExcelPrevisionAcompte(List<Map<String, String>> listPrevisionFromEbu,
            BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle) throws Exception {
        EBImprimerPrevisionAcompte listeRecap = new EBImprimerPrevisionAcompte();
        listeRecap.setParentWithCopy(this);
        listeRecap.setListPrevisionAcompte(listPrevisionFromEbu);
        listeRecap.setEMailAddress(getEMailAddress());
        listeRecap.setMasseAnnuelleMaxPourPeriodiciteAnnuelle(masseAnnuelleMaxPourPeriodiciteAnnuelle);
        listeRecap.executeProcess();
    }

    protected void manageErrorOrCommit(Map<String, String> recapImpression, int idPacEbu) throws Exception {
        StatusSaisiePAC statutEbu = StatusSaisiePAC.TERMINE;
        if (getTransaction().hasErrors()) {
            statutEbu = StatusSaisiePAC.PROBLEME;
            getMemoryLog().logMessage(
                    recapImpression.get(EBImprimerPrevisionAcompte.NUM_AFFILIE) + ": "
                            + getTransaction().getErrors().toString(), FWMessage.ERREUR, toString());
            getTransaction().rollback();
            getTransaction().clearErrorBuffer();
        } else {
            getTransaction().commit();
        }
        // Sauvegarde du statut pour la liste récapitulative
        recapImpression.put(EBImprimerPrevisionAcompte.STATUT, statutEbu.toString());
        // Mise à jour status dans eBusiness
        PacServiceImpl.changeStatus(idPacEbu, statutEbu, getSession());

    }

    protected void miseAjourPeriodiciteMensuelleAffiliation(MassesMensuellesPourAVS prevision) throws Exception {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(getSession());
        aff.setAffiliationId(prevision.getIdAffiliation().toString());
        aff.retrieve();
        if ((aff.isNew() == false) && (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE) == false)) {
            aff.setPeriodicite(CodeSystem.PERIODICITE_MENSUELLE);
            aff.wantCallValidate(false);
            aff.wantCallMethodBefore(false);
            aff.wantCallMethodAfter(false);
            aff.update(getTransaction());
        }
    }

    protected void saveInfoPremiereMasse(Map<String, String> recapPrevision, MassePourAVS uneMasse, BSession session,
            AFCotisation cotisation, String dateChangement) throws Exception {
        if (premiereMasse) {
            try {
                setPeriodicite(cotisation.getPeriodicite());
                setDateChangementAvs(dateChangement);
                recapPrevision.put(EBImprimerPrevisionAcompte.ANCIENNE_MASSE, uneMasse.getAncienneMasse().toString());
                recapPrevision.put(EBImprimerPrevisionAcompte.NOUVELLE_MASSE, uneMasse.getNouvelleMasse().toString());
                premiereMasse = false;
            } catch (Exception e) {
                throw new Exception(session.getLabel("PREVISION_MASSE_ERRONNEE") + " : " + e.toString(), e);
            }
        }
    }

    public void setDateChangementAvs(String dateChangementAvs) {
        this.dateChangementAvs = dateChangementAvs;
    }

    public void setDernierPassagePeriodiqueParitaire(IFAPassage dernierPassagePeriodiqueParitaire) {
        this.dernierPassagePeriodiqueParitaire = dernierPassagePeriodiqueParitaire;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    protected void traiterAffilie(String idAffiliation, Map<String, String> recapPrevision,
            List<Map<String, String>> listRecapPrevision, int idPacEbu, boolean changementPeriodicite) throws Exception {
        boolean releveAcontroler = false;
        if (getTransaction().hasErrors() == false) {
            try {
                int anneePassage = JACalendar.getYear(getDernierPassagePeriodiqueParitaire().getDatePeriode());
                int anneeChangement = JACalendar.getYear(getDateChangementAvs());
                if (anneePassage >= anneeChangement) {
                    // Recalcul de la date de fin (se baser sur la périodicité de l'affiliation et la dernière
                    // périodique)
                    // Si affiliation annuelle ou date de changement dans le futur => ne rien faire.
                    int moisPassage = JACalendar.getMonth(getDernierPassagePeriodiqueParitaire().getDatePeriode());
                    int moisLancementCalculRetro = determinerMoislancementPourCalculRetro(changementPeriodicite,
                            moisPassage);
                    // Si date de changement > calcul retro => ne rien faire
                    int moisChangement = JACalendar.getMonth(getDateChangementAvs());
                    if (moisLancementCalculRetro >= moisChangement) {
                        String periodeLancementCalculRetro = "";
                        if (moisLancementCalculRetro < 10) {
                            periodeLancementCalculRetro = "0";
                        }
                        periodeLancementCalculRetro = periodeLancementCalculRetro
                                + Integer.toString(moisLancementCalculRetro) + "." + Integer.toString(anneePassage);
                        // Lancement du calcul rétroactif
                        String dateDebutDeCaclul = getDateChangementAvs().substring(3);
                        genererRelevePourRetroActif(idAffiliation, dateDebutDeCaclul, periodeLancementCalculRetro);
                        // Tester et indiquer dans la liste récapitulaltive si des relevés ont été créés
                        releveAcontroler = indiquerReleveAcontroler(recapPrevision);
                    }
                }
            } catch (Exception e) {
                // Intercepter car dans NAOS certains processus remontent les erreurs comme exception (et
                // non addError) - Ceci permet de ne pas sortir de la boucle du processus et de traiter tous les cas.
                this._addError(getTransaction(), e.getMessage());
            }
        }
        if (releveAcontroler) {
            recapPrevision.put(EBImprimerPrevisionAcompte.RELEVE_A_CONTROLER, "X");
        } else {
            recapPrevision.put(EBImprimerPrevisionAcompte.RELEVE_A_CONTROLER, " ");
        }
        manageErrorOrCommit(recapPrevision, idPacEbu);
        // miseAjourEBusiness;
        listRecapPrevision.add(recapPrevision);
    }

    protected void updateMasseAnnuelle(boolean changementPeriodicite, String dateNouvelleMasse, MassePourAVS uneMasse,
            AFCotisation cotisation, MassesMensuellesPourAVS prevision, boolean masseDiffrenteDeZero) throws Exception {
        // Si nouvellemasse > parametre masse max défini (par défaut 200000) => mettre la périodicité à
        // mensuelle
        try {
            if (changementPeriodicite) {
                if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(uneMasse.getTypeCotisation().toString())) {
                    // MAJ l'affiliation si periodocité plus petite - Evite d'avoir le message, la périodicité ne
                    // peut pas être plus petite que celle de l'affiliation. A faire une seule fois (pour l'AVS)
                    miseAjourPeriodiciteMensuelleAffiliation(prevision);
                }
                cotisation.updateMasseAnnuelle(CodeSystem.PERIODICITE_MENSUELLE,
                        uneMasse.getNouvelleMasse().toString(), CodeSystem.PERIODICITE_MENSUELLE, dateNouvelleMasse,
                        masseDiffrenteDeZero);
            } else {
                cotisation.updateMasseAnnuelle(CodeSystem.PERIODICITE_MENSUELLE,
                        uneMasse.getNouvelleMasse().toString(), null, dateNouvelleMasse, masseDiffrenteDeZero);
            }
        } catch (Exception e) {
            // Intercepter exception car dans NAOS certains processus remontent les erreurs comme
            // exception (et non addError) - Ceci permet de ne pas sortir de la boucle du processus
            // et de traiter tous les cas.
            this._addError(getTransaction(), e.getMessage());
        }
    }
}
