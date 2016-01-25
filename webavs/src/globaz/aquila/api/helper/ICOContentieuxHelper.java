/*
 * Créé le 18 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api.helper;

import globaz.aquila.api.ICOContentieux;
import globaz.aquila.api.ICOEtape;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ICOContentieuxHelper extends GlobazHelper implements ICOContentieux {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static String IMPLEMENTATION_AC_METHOD_NAME = "annulerDerniereEtape";

    private static String IMPLEMENTATION_CLASS_NAME = "globaz.aquila.db.access.poursuite.COContentieux";
    private static String IMPLEMENTATION_CREER_METHOD_NAME = "creerContentieux";
    private static String IMPLEMENTATION_CREER_SURSIS_METHOD_NAME = "creerSommationSursisPaiement";
    private static String IMPLEMENTATION_HC_METHOD_NAME = "hasContentieux";
    private static String IMPLEMENTATION_ISETAPEEXECUTEE_METHODE_NAME = "isEtapeExecutee";
    private static String IMPLEMENTATION_LOAD_METHOD_NAME = "load";
    private static String MNAME_CS_DERNIERE_ETAPE_CONTENTIEUX = "libEtape";

    private static String MNAME_CS_SEQUENCE_CONTENTIEUX = "libSequence";
    private static String MNAME_DATE_DECLENCHEMENT = "dateDeclenchement";
    private static String MNAME_DATE_EXECUTION = "dateExecution";
    private static String MNAME_DATE_OUVERTURE = "dateOuverture";
    private static String MNAME_ID_CONTENTIEUX = "idContentieux";
    private static String MNAME_ID_DERNIERE_ETAPE = "idEtape";
    private static String MNAME_ID_SECTION = "idSection";
    private static String MNAME_ID_SEQUENCE_CONTENTIEUX = "idSequence";
    private static String MNAME_NB_DELAI_MUTE = "nbDelaiMute";
    private static String MNAME_PROCHAINE_DATE_DECLENCHEMENT = "prochaineDateDeclenchement";
    private static final long serialVersionUID = -1199386812162845289L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient ICOEtape etape;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe ICOContentieuxHelper.
     */
    public ICOContentieuxHelper() {
        super(ICOContentieuxHelper.IMPLEMENTATION_CLASS_NAME);
    }

    private ICOContentieuxHelper(GlobazValueObject vo) {
        super(vo);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.api.ICOContentieux#annulerDerniereEtape(java.util.Map)
     */
    @Override
    public void annulerDerniereEtape(HashMap criteres, BTransaction transaction) throws Exception {
        _getObject(ICOContentieuxHelper.IMPLEMENTATION_AC_METHOD_NAME, new Object[] { criteres, transaction });
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#creerContentieux(globaz.globall.db.BSession,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    public ICOContentieux creerContentieux(BSession session, BTransaction transaction, String idSection,
            String csSequence, String dateProchainDeclenchement, String remarque) throws Exception {
        // obtenir la bonne implementation de contentieux (AVS, PP, ...) avec
        // les champs renseignes
        GlobazValueObject vo = (GlobazValueObject) _getObject(ICOContentieuxHelper.IMPLEMENTATION_CREER_METHOD_NAME,
                new Object[] { idSection, csSequence, dateProchainDeclenchement, remarque });

        // sauver dans la base
        BEntity contentieux = BEntity.newInstance(vo);

        contentieux.setSession(session);
        contentieux.add(transaction);

        if (transaction.hasErrors()) {
            throw new Exception("Impossible d'ajouter le contentieux pour la section." + " ("
                    + transaction.getErrors().toString() + ")");
        }

        return new ICOContentieuxHelper(contentieux.toValueObject());
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param idSection
     *            DOCUMENT ME!
     * @param dateExecution
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ICOContentieux creerSommationSursisPaiement(BSession session, BTransaction transaction, String idSection,
            String dateExecution) throws Exception {
        // obtenir la bonne implementation de contentieux (AVS, PP, ...) avec
        // les champs renseignes
        GlobazValueObject contentieux = (GlobazValueObject) _getObject(
                ICOContentieuxHelper.IMPLEMENTATION_CREER_SURSIS_METHOD_NAME, new Object[] { session, transaction,
                        idSection, dateExecution });

        if (transaction.hasErrors()) {
            throw new Exception("Impossible d'ajouter le contentieux pour la section." + " ("
                    + transaction.getErrors().toString() + ")");
        }

        return new ICOContentieuxHelper(contentieux);
    }

    @Override
    public String findIdContentieuxForIdSection(String idSection) throws Exception {
        return (String) _getObject("findIdContentieuxForIdSection", new Object[] { idSection });

    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getCsDerniereEtapeContentieux()
     */
    @Override
    public String getCsDerniereEtapeContentieux() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_CS_DERNIERE_ETAPE_CONTENTIEUX);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getCsSequenceContentieux()
     */
    @Override
    public String getCsSequenceContentieux() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_CS_SEQUENCE_CONTENTIEUX);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getDateDeclenchement()
     */
    @Override
    public String getDateDeclenchement() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_DATE_DECLENCHEMENT);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getDateExecution()
     */
    @Override
    public String getDateExecution() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_DATE_EXECUTION);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getDateOuverture()
     */
    @Override
    public String getDateOuverture() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_DATE_OUVERTURE);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getIdContentieux()
     */
    @Override
    public String getIdContentieux() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_ID_CONTENTIEUX);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getIdDerniereEtapeContentieux()
     */
    @Override
    public String getIdDerniereEtapeContentieux() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_ID_DERNIERE_ETAPE);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getIdSection()
     */
    @Override
    public String getIdSection() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_ID_SECTION);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getIdSequenceContentieux()
     */
    @Override
    public String getIdSequenceContentieux() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_ID_SEQUENCE_CONTENTIEUX);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getNbDelaiMute()
     */
    @Override
    public String getNbDelaiMute() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_NB_DELAI_MUTE);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#getProchaineDateDeclenchement()
     */
    @Override
    public String getProchaineDateDeclenchement() {
        return (String) _getValueObject().getProperty(ICOContentieuxHelper.MNAME_PROCHAINE_DATE_DECLENCHEMENT);
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#hasContentieux(java.util.Map)
     */
    @Override
    public boolean hasContentieux(HashMap criteres) throws Exception {
        Boolean retValue = (Boolean) _getObject(ICOContentieuxHelper.IMPLEMENTATION_HC_METHOD_NAME,
                new Object[] { criteres });

        return (retValue != null) && retValue.booleanValue();
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#isEtapeExecutee(java.lang.String)
     */
    @Override
    public boolean isEtapeExecutee(String csEtape, String dateDe, String dateA, String forIdContentieux,
            String forIdSequence) throws Exception {
        Boolean retValue = (Boolean) _getObject(ICOContentieuxHelper.IMPLEMENTATION_ISETAPEEXECUTEE_METHODE_NAME,
                new Object[] { csEtape, dateDe, dateA, forIdContentieux, forIdSequence });

        return (retValue != null) && retValue.booleanValue();
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#load(java.util.Map)
     */
    @Override
    public Collection load(HashMap criteres) throws Exception {
        LinkedList retValue = new LinkedList();
        Object[] vos = _getArray(ICOContentieuxHelper.IMPLEMENTATION_LOAD_METHOD_NAME, new Object[] { criteres });

        if (vos != null) {
            for (int idVO = 0; idVO < vos.length; ++idVO) {
                retValue.add(new ICOContentieuxHelper((GlobazValueObject) vos[idVO]));
            }
        }

        return retValue;
    }

    /**
     * @see globaz.aquila.api.ICOContentieux#loadDerniereEtape()
     */
    @Override
    public ICOEtape loadDerniereEtape(BSession session) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdDerniereEtapeContentieux())) {
            throw new Exception(
                    "Le contentieux n'a pas été chargé ou alors l'identifiant de la dernière étape est vide");
        }

        if (etape == null) {
            // preparer la recherche
            ICOEtape loader = (ICOEtape) session.getAPIFor(ICOEtape.class);
            HashMap criteres = new HashMap();

            criteres.put(ICOEtape.FOR_ID_ETAPE, getIdDerniereEtapeContentieux());

            // charger l'etape
            Collection etapes = loader.load(criteres);

            if ((etapes == null) || etapes.isEmpty()) {
                throw new Exception("Impossible de charger l'étape");
            }

            etape = (ICOEtape) etapes.iterator().next();
        }

        return etape;
    }
}
