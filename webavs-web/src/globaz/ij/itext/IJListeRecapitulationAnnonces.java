/*
 * Crée le 4 septembre 2006
 */

package globaz.ij.itext;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.application.IJApplication;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.webavs.common.CommonProperties;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */
public class IJListeRecapitulationAnnonces extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String CHAMP_GENRE_SERVICE = "V_GENRE_SERVICE";

    private static final String CHAMP_MONTANT_QUESTIONNAIRES = "V_MONTANT_QUESTIONNAIRES";

    private static final String CHAMP_MONTANT_RESTITUTIONS = "V_MONTANT_RESTITUTIONS";
    private static final String CHAMP_MONTANT_RETROACTIFS = "V_MONTANT_RETROACTIFS";
    private static final String CHAMP_MONTANT_TOTAL_AC = "V_MONTANT_TOTAL_AC";
    private static final String CHAMP_NB_JOURS_QUESTIONNAIRES = "V_NB_JOURS_QUESTIONNAIRES";
    /**
     */
    public static final String FICHIER_MODELE = "IJ_RECAP_ANNONCE";
    /**
     */
    public static final String FICHIER_RESULTAT = "recap_annonce";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IJRecapitulationAnnonceAdapter adapter;
    private String forMoisAnneeComptable = "";
    private boolean hasNext = true;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJListeRecapitulationAnnonces.
     */
    public IJListeRecapitulationAnnonces() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJRecapitulationAnnonceDocument.
     * 
     * @param parent
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public IJListeRecapitulationAnnonces(BProcess parent) throws FWIException {
        super(parent, IJApplication.APPLICATION_IJ_REP, FICHIER_RESULTAT);
    }

    /**
     * Crée une nouvelle instance de la classe IJRecapitulationAnnonceDocument.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public IJListeRecapitulationAnnonces(BSession session) throws FWIException {
        super(session, IJApplication.APPLICATION_IJ_REP, FICHIER_RESULTAT);
    }

    @Override
    public void afterBuildReport() {
        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.RECAP_MENSUELLE_ANNONCES_IJ);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        try {

            adapter = new IJRecapitulationAnnonceAdapter(getSession(), getForMoisAnneeComptable());
            adapter.chargerParServices();

            setTemplateFile(FICHIER_MODELE);

            DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), "dd MMMM yyyy");
            Calendar cal = Calendar.getInstance();

            setParamIfNotNull("P_DATE", df.format(cal.getTime()));

            String numCaisse = null;

            numCaisse = getSession().getApplication().getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);

            setParamIfNotNull("P_NUM_CAISSE", "NO DE LA CAISSE " + numCaisse);
            setParamIfNotNull("P_NOMBRE_CARTES_QUESTIONNAIRES", adapter.getNbCartesQuestionnaires());
            setParamIfNotNull("P_NOMBRE_CARTES_RECTIFICATIVES", adapter.getNbCartesRectificatives());
            setParamIfNotNull("P_NOMBRE_CARTES", adapter.getNbCartesTotal());

            // internationalisation
            ICTDocument helper = PRBabelHelper.getDocumentHelper(getISession());

            helper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
            helper.setCsTypeDocument(IIJCatalogueTexte.CS_RECAPITULATION);
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
            setParamIfNotNull("P_PAIEMENTS_RETROACTIFS", document.getTextes(1).getTexte(6).getDescription());
            setParamIfNotNull("P_TOTAL_AB", document.getTextes(1).getTexte(7).getDescription());
            setParamIfNotNull("P_RESTITUTIONS", document.getTextes(1).getTexte(8).getDescription());
            setParamIfNotNull("P_GENRE_SERVICE", document.getTextes(1).getTexte(9).getDescription());
            setParamIfNotNull("P_JOURS", document.getTextes(1).getTexte(10).getDescription());
            setParamIfNotNull("P_FRANCS", document.getTextes(1).getTexte(11).getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES", document.getTextes(1).getTexte(12).getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES_QUESTIONNAIRES", document.getTextes(1).getTexte(15)
                    .getDescription());
            setParamIfNotNull("P_INTITULE_NOMBRE_CARTES_RECTIFICATIVES", document.getTextes(1).getTexte(17)
                    .getDescription());
            setParamIfNotNull("P_TOTAL", document.getTextes(1).getTexte(18).getDescription());

            setParamIfNotNull("P_COMPTE_TOTAL_AB", document.getTextes(2).getTexte(1).getDescription());
            setParamIfNotNull("P_COMPTE_RESTITUTIONS", document.getTextes(2).getTexte(2).getDescription());

        } catch (Exception e) {
            getMemoryLog()
                    .logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("RECAPITULATIF_ANNONCES"));
            throw new FWIException(e);
        }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        LinkedList lignes = new LinkedList();

        String cs = "IJAI";
        Map champs = new HashMap();

        // recuperer les infos sur la ligne courante
        IJRecapitulationAnnonceAdapter.IJLigneRecapitulationAnnonce ligne = adapter.getLigneParCU(cs);

        champs.put(CHAMP_GENRE_SERVICE, (cs));
        champs.put(CHAMP_MONTANT_TOTAL_AC, ligne.getMontantTotalAC());
        champs.put(CHAMP_MONTANT_QUESTIONNAIRES, ligne.getMontantQuestionnaires());
        champs.put(CHAMP_MONTANT_RESTITUTIONS, ligne.getMontantRestitutions());
        champs.put(CHAMP_MONTANT_RETROACTIFS, ligne.getMontantRetroActifs());
        champs.put(CHAMP_NB_JOURS_QUESTIONNAIRES, ligne.getNbJoursQuestionnaires());

        lignes.add(champs);

        setDataSource(lignes);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("IMPRESSION_DOCUMENT_ECHEC");
        } else {
            return getSession().getLabel("IMPRESSION_LISTE_RECAPITULATION_ANNONCES_OK");
        }
    }

    /**
     * getter pour l'attribut for mois annee comptable.
     * 
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {

        // Si formatté sans point MMAAAA, on le rajoute
        if (forMoisAnneeComptable != null && forMoisAnneeComptable.indexOf(".") == -1) {
            forMoisAnneeComptable = forMoisAnneeComptable.substring(0, 2) + "."
                    + forMoisAnneeComptable.substring(2, forMoisAnneeComptable.length());
        }
        return forMoisAnneeComptable;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (hasNext) {
            hasNext = false;

            return true;
        } else {
            return false;
        }
    }

    /**
     * setter pour l'attribut for mois annee comptable.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    private void setParamIfNotNull(String name, Object value) {
        if (value != null) {
            getImporter().setParametre(name, value);
        }
    }

}