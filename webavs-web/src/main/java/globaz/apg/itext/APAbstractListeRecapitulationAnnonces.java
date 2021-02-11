package globaz.apg.itext;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.utils.APGUtils;
import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.webavs.common.CommonProperties;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;

/**
 * Générateur de liste récapitulative des annonces APG. Regroupe les fonctionnalités communes aux annonces APG et RAPG.
 *
 * @author PBA
 */
public abstract class APAbstractListeRecapitulationAnnonces extends FWIDocumentManager {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String CHAMP_CU_GENRE_SERVICE = "V_CU_GENRE_SERVICE";
    private static final String CHAMP_GENRE_SERVICE = "V_GENRE_SERVICE";
    private static final String CHAMP_MONTANT_DUPLICATA = "V_MONTANT_DUPLICATA";
    private static final String CHAMP_MONTANT_QUESTIONNAIRES = "V_MONTANT_QUESTIONNAIRES";
    private static final String CHAMP_MONTANT_RESTITUTIONS = "V_MONTANT_RESTITUTIONS";
    private static final String CHAMP_MONTANT_RETROACTIFS = "V_MONTANT_RETROACTIFS";
    private static final String CHAMP_MONTANT_TOTAL_AC = "V_MONTANT_TOTAL_AC";
    private static final String CHAMP_NB_JOUR_RETROACTIF = "V_NB_JOURS_RETROACTIF";
    private static final String CHAMP_NB_JOURS_DUPLICATA = "V_NB_JOURS_DUPLICATA";
    private static final String CHAMP_NB_JOURS_QUESTIONNAIRES = "V_NB_JOURS_QUESTIONNAIRES";

    public static final String FICHIER_MODELE = "AP_RECAP_ANNONCE";
    public static final String FICHIER_RESULTAT = "recap_annonce";

    private APAbstractRecapitulationAnnonceAdapter adapter;
    private String forMoisAnneeComptable = "";
    private String forTypeAPG = "";
    private boolean hasNext = true;
    private List<JadeCodeSysteme> services;

    public APAbstractListeRecapitulationAnnonces() {
        super();
    }

    public APAbstractListeRecapitulationAnnonces(BProcess parent) throws FWIException {
        super(parent, APApplication.APPLICATION_APG_REP, APAbstractListeRecapitulationAnnonces.FICHIER_RESULTAT);
    }

    public APAbstractListeRecapitulationAnnonces(BSession session) throws FWIException {
        super(session, APApplication.APPLICATION_APG_REP, APAbstractListeRecapitulationAnnonces.FICHIER_RESULTAT);
    }

    @Override
    public void afterBuildReport() {
        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.RECAP_MENSUELLE_ANNONCES_APG);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        // TODO: mettre un titre au document
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            BSessionUtil.initContext(getSession(), this);

            adapter = getAdapter(getSession(), getForMoisAnneeComptable());
            adapter.setForTypeAPG(forTypeAPG);
            adapter.chargerParServices();

            JadeCodeSystemeService codeSystemService = JadeBusinessServiceLocator.getCodeSystemeService();
            services = codeSystemService.getFamilleCodeSysteme(IAPDroitLAPG.CS_GROUPE_GENRE_SERVICE_APG);
            if(!JadeStringUtil.isBlankOrZero(forTypeAPG)){
                services = triParTypeAPG(services,forTypeAPG);
            }
            services = triNotPandemie(services);
            final Langues langue = Langues.getLangueDepuisCodeIso(getSession().getIdLangueISO());
            Collections.sort(services, new Comparator<JadeCodeSysteme>() {
                @Override
                public int compare(JadeCodeSysteme codeSysteme1, JadeCodeSysteme codeSysteme2) {
                    return codeSysteme1.getCodeUtilisateur(langue).compareTo(codeSysteme2.getCodeUtilisateur(langue));
                }
            });

            APDonneesRecapitulationAnnonce donnees = adapter.getDonnees();

            setTemplateFile(APAbstractListeRecapitulationAnnonces.FICHIER_MODELE);

            DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), "dd MMMM yyyy");
            Calendar cal = Calendar.getInstance();

            setParamIfNotNull("P_DATE", df.format(cal.getTime()));

            String numCaisse = null;

            numCaisse = getSession().getApplication().getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);

            setParamIfNotNull("P_NUM_CAISSE", getSession().getLabel("JSP_CAISSE") + " " + numCaisse);
            setParamIfNotNull("P_NOMBRE_CARTES_QUESTIONNAIRES", donnees.getNombreCartesQuestionnaires());
            setParamIfNotNull("P_NOMBRE_CARTES_DUPLICATA", donnees.getNombreCartesDuplicata());
            setParamIfNotNull("P_NOMBRE_CARTES_RECTIFICATIVES", donnees.getNombreCartesRectificatives());
            setParamIfNotNull("P_NOMBRE_CARTES", donnees.getNombreTotalCartes());
            setParamIfNotNull("P_MONTANT_TOTAL_GENERAL_AC", donnees.getMontantTotalAC());
            setParamIfNotNull("P_MONTANT_TOTAL_RESTITUTIONS", donnees.getMontantTotalRestitutions());
            setParamIfNotNull("P_FRAIS_GARDE", donnees.getMontantTotalFraisGarde());

            // internationalisation
            ICTDocument helper = PRBabelHelper.getDocumentHelper(getISession());

            helper.setCsDomaine(IAPCatalogueTexte.CS_APG);
            helper.setCsTypeDocument(IAPCatalogueTexte.CS_RECAPITULATION_APG);
            helper.setDefault(Boolean.TRUE);
            helper.setActif(Boolean.TRUE);
            helper.setCodeIsoLangue(getSession().getIdLangueISO());

            ICTDocument[] candidats = helper.load();

            if ((candidats == null) || (candidats.length == 0)) {
                throw new Exception("impossible de trouver le catalogue de texte");
            }

            // entree dans le pdf
            ICTDocument document = candidats[0];

            JADate asJADate = new JADate(getForMoisAnneeComptable());

            cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);
            df = PRDateFormater.getDateFormatInstance(getSession(), " MMMM yyyy");

            setParamIfNotNull("P_TITRE_PAGE", document.getTextes(1).getTexte(1).getDescription());
            setParamIfNotNull("P_MOIS_ANNEE",
                    document.getTextes(1).getTexte(2).getDescription() + df.format(cal.getTime()));
            setParamIfNotNull("P_GENRE_CARTE", document.getTextes(1).getTexte(3).getDescription());
            setParamIfNotNull("P_QUESTIONNAIRES", document.getTextes(1).getTexte(4).getDescription());
            setParamIfNotNull("P_DUPLICATA", document.getTextes(1).getTexte(5).getDescription());
            setParamIfNotNull("P_PAIEMENTS_RETROACTIFS", document.getTextes(1).getTexte(6).getDescription());
            setParamIfNotNull("P_TOTAL_AC", document.getTextes(1).getTexte(7).getDescription());
            setParamIfNotNull("P_RESTITUTIONS", document.getTextes(1).getTexte(8).getDescription());
            setParamIfNotNull("P_GENRE_SERVICE", document.getTextes(1).getTexte(9).getDescription());
            setParamIfNotNull("P_JOURS", document.getTextes(1).getTexte(10).getDescription());
            setParamIfNotNull("P_FRANCS", document.getTextes(1).getTexte(11).getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES", document.getTextes(1).getTexte(12).getDescription());
            setParamIfNotNull("P_INTITULE_FRAIS_DE_GARDE", document.getTextes(1).getTexte(13).getDescription());
            setParamIfNotNull("P_TOTAL_GENERAL", document.getTextes(1).getTexte(14).getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES_QUESTIONNAIRES", document.getTextes(1).getTexte(15)
                    .getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES_DUPLICATA", document.getTextes(1).getTexte(16).getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES_RECTIFICATIVES", document.getTextes(1).getTexte(17)
                    .getDescription());
            setParamIfNotNull("P_TOTAL", document.getTextes(1).getTexte(18).getDescription());

            setParamIfNotNull("P_COMPTE_550", document.getTextes(2).getTexte(1).getDescription());
            setParamIfNotNull("P_COMPTE_556", document.getTextes(2).getTexte(2).getDescription());
        } catch (Exception e) {
            getMemoryLog()
                    .logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("RECAPITULATIF_ANNONCES"));
            throw new FWIException(e);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }
    }

    private  List<JadeCodeSysteme> triNotPandemie(List<JadeCodeSysteme> services){
        List<JadeCodeSysteme> newList = new ArrayList<>();

        for (JadeCodeSysteme cs : services) {
            if(! APGUtils.isTypeAllocationPandemie(cs.getIdCodeSysteme())){
                newList.add(cs);
            }
        }
        return newList;

    };

    private List<JadeCodeSysteme> triParTypeAPG(List<JadeCodeSysteme> services, String forTypeAPG) {
        List<JadeCodeSysteme> newList = new ArrayList<>();
        for (JadeCodeSysteme cs : services) {
            if (forTypeAPG.equals(IAPAnnonce.CS_PATERNITE)) {
                if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(cs.getIdCodeSysteme())) {
                    newList.add(cs);
                }
            }
        }
        return newList;

    }

    @Override
    public final void createDataSource() throws Exception {

        APDonneesRecapitulationAnnonce donnees = adapter.getDonnees();
        Langues langue = Langues.getLangueDepuisCodeIso(getSession().getIdLangueISO());

        Collection<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();

        for (JadeCodeSysteme unCodeGenreService : services) {

            Map<String, Object> champs = new HashMap<String, Object>();

            // recuperer les infos sur la ligne courante
            APLigneRecapitulationAnnonce uneLigne = donnees.getLignePourGenreService(unCodeGenreService
                    .getCodeUtilisateur(langue));

            champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_GENRE_SERVICE,
                    unCodeGenreService.getTraduction(langue));
            champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_CU_GENRE_SERVICE,
                    unCodeGenreService.getCodeUtilisateur(langue));

            if (uneLigne != null) {

                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_NB_JOURS_DUPLICATA,
                        uneLigne.getNombreJoursDuplicata());
                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_MONTANT_DUPLICATA,
                        uneLigne.getMontantDuplicata());

                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_NB_JOURS_QUESTIONNAIRES,
                        uneLigne.getNombreJoursQuestionnaires());
                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_MONTANT_QUESTIONNAIRES,
                        uneLigne.getMontantQuestionnaires());

                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_NB_JOUR_RETROACTIF,
                        uneLigne.getNombreJoursPaiementRetroactifs());
                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_MONTANT_RETROACTIFS,
                        uneLigne.getMontantPaiementRetroactifs());

                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_MONTANT_TOTAL_AC, uneLigne.getMontantAC());

                champs.put(APAbstractListeRecapitulationAnnonces.CHAMP_MONTANT_RESTITUTIONS,
                        uneLigne.getMontantRestitutions());
            }

            dataSource.add(champs);
        }

        this.setDataSource(dataSource);
    }

    protected abstract APAbstractRecapitulationAnnonceAdapter getAdapter(BSession session, String forMoisAnneeCommptable);

    @Override
    protected final String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("IMPRESSION_DOCUMENT_ECHEC");
        } else {
            return getSession().getLabel("IMPRESSION_LISTE_RECAPITULATION_ANNONCES_OK");
        }
    }

    public final String getForMoisAnneeComptable() {

        // Si formatté sans point MMAAAA, on le rajoute
        if ((forMoisAnneeComptable != null) && (forMoisAnneeComptable.indexOf(".") == -1)) {
            forMoisAnneeComptable = forMoisAnneeComptable.substring(0, 2) + "."
                    + forMoisAnneeComptable.substring(2, forMoisAnneeComptable.length());
        }
        return forMoisAnneeComptable;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public final boolean next() throws FWIException {
        if (hasNext) {
            hasNext = false;

            return true;
        } else {
            return false;
        }
    }

    public final void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    private void setParamIfNotNull(String name, Object value) {
        if (value != null) {
            getImporter().setParametre(name, value);
        }
    }

    public String getForTypeAPG() {
        return forTypeAPG;
    }

    public void setForTypeAPG(String forTypeAPG) {
        this.forTypeAPG = forTypeAPG;
    }

}
