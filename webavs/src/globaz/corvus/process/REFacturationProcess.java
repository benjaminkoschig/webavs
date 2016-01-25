package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREFactureARestituer;
import globaz.corvus.db.rentesaccordees.REFactureARestituer;
import globaz.corvus.db.rentesaccordees.REFacturesARestituerManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.module.compta.REModuleComptableFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.util.FAUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;

/**
 * Process de facturation, permettant l'édition des bvr par MUSCA
 * 
 * @author SCR
 */
public class REFacturationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String dateComptable = null;
    protected String emailObject = "";
    private String idModuleFacturation = "";
    String noSection = null;
    // passage
    String numeroPassage = null;

    @Override
    protected void _executeCleanUp() {
        // Auto-generated method stub
    }

    /**
     * Exécution du process
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        boolean success = true;
        BTransaction transaction = getTransaction();
        BStatement statement = null;

        BSession sessionOsiris = (BSession) PRSession.connectSession(getSession(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
        // Initialisation des rubriques
        REModuleComptableFactory.getInstance().initIdsRubriques(sessionOsiris);

        try {
            REFacturesARestituerManager mgr = new REFacturesARestituerManager();
            mgr.setSession(getSession());
            mgr.setForCsEtat(IREFactureARestituer.CS_A_FACTURER);
            statement = mgr.cursorOpen(transaction);
            REFactureARestituer far = null;

            while ((far = (REFactureARestituer) mgr.cursorReadNext(statement)) != null) {

                String catSection = "";

                if (APISection.ID_CATEGORIE_SECTION_RESTITUTIONS.equals(far.getCsCatSection())) {
                    catSection = APISection.CATEGORIE_SECTION_RESTITUTIONS;

                } else {
                    throw new Exception("Unsupporter idCategorieSection : " + far.getCsCatSection());
                }

                // on demande l'en-tête de facture correspondante
                FAEnteteFacture enteteFacture = FAUtil.getEnteteFacture(
                        getNumeroPassage(),
                        far.getIdTiersBenefPrincipal(),
                        far.getCsRole(),
                        far.getIdExterne(),
                        catSection,
                        getNoSection(sessionOsiris, transaction, far.getIdExterne(), getDateComptable(),
                                APISection.ID_CATEGORIE_SECTION_RESTITUTIONS), getSession(), transaction);

                // on rajoute l'affact dans l'en-tête de facture
                createLigneFacture(transaction, far, enteteFacture);
                // MAJ de la facture a restituer...
                far.setCsEtat(IREFactureARestituer.CS_FACTURE);
                far.setIdPassage(enteteFacture.getIdPassage());
                far.update(transaction);
            }
            transaction.commit();
        } catch (Exception e) {
            success = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {
            if (success) {
                emailObject = sessionOsiris.getLabel("EMAIL_OBJECT_RESTITUTION_FACTURATION_SUCCESS");
            } else {
                emailObject = sessionOsiris.getLabel("EMAIL_OBJECT_RESTITUTION_FACTURATION_ERREUR");
            }
            try {
                // On ne ferme pas la transaction,
                // car elle est utilisée par les modules de facturation suivants
                // !!!
                statement.closeStatement();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Exécute le processus de facturation en appelant la méthode protected
     */
    public boolean _executeProcessFacturation() throws Exception {
        return _executeProcess();
    }

    /**
     * Crée une ligne de facture
     */
    private void createLigneFacture(BTransaction transaction, REFactureARestituer far, FAEnteteFacture enteteFacture)
            throws Exception {

        FAAfact aFact = new FAAfact();
        aFact.setISession(getSession());
        aFact.setIdEnteteFacture(enteteFacture.getIdEntete());
        aFact.setIdPassage(getNumeroPassage());
        aFact.setIdModuleFacturation(getIdModuleFacturation());

        aFact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        aFact.setNonImprimable(Boolean.FALSE);
        aFact.setNonComptabilisable(Boolean.FALSE);
        aFact.setAQuittancer(Boolean.FALSE);
        aFact.setAnneeCotisation("");

        // On récupère la rente accordée...
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(far.getIdRenteAccordee());
        ra.retrieve(transaction);
        aFact.setIdExterneRubrique(getRubriqueRestitution(ra).getIdExterne());

        JADate dd = new JADate(getDateComptable());
        dd.setDay(1);
        aFact.setDebutPeriode(PRDateFormater.convertDate_AAAAMMJJ_to_JJMMAAAA(dd.toStrAMJ()));
        aFact.setFinPeriode(PRDateFormater.convertDate_AAAAMMJJ_to_JJMMAAAA(formatToLastDayInMonth(getDateComptable())));
        aFact.setMontantFacture(String.valueOf(Double.parseDouble(far.getMontantFactARestituer())));

        try {
            aFact.add(transaction);

            // erreur dans la transaction?
            if (transaction.hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

        } catch (Exception e) {
            throw new Exception("Erreur à l'enregistrement de l'AFact: " + e.getMessage());
        }
    }

    /**
     * 
     * @param date
     * @return last day in month for date in parameter. Format : AAAAMMJJ
     * @throws Exception
     */
    private String formatToLastDayInMonth(String date) throws Exception {
        // calendrier pour trouver le dernier jour du mois
        JACalendarGregorian calendar = new JACalendarGregorian();

        JADate jdate = new JADate(date);

        int day = calendar.daysInMonth(jdate.getMonth(), jdate.getYear());
        jdate = new JADate(day, jdate.getMonth(), jdate.getYear());

        return jdate.toStrAMJ();
    }

    /**
     * @return
     */
    public String getDateComptable() {
        return dateComptable;
    }

    /**
     * ?
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getNoSection(BSession sessionOsiris, BTransaction transaction, String idExterneRole,
            String dateComptable, String idCategorieSection) throws Exception {

        String typeSection = null;
        if (APISection.ID_CATEGORIE_SECTION_RETOUR.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_RETOUR;
        } else if (APISection.ID_CATEGORIE_SECTION_RESTITUTIONS.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_RESTITUTION;

        } else if (APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_BLOCAGE;
        } else if (APISection.ID_CATEGORIE_SECTION_DECISION.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_RENTE_AVS_AI;
        } else if (APISection.ID_CATEGORIE_SECTION_AVANCE.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_AVANCES;
        }

        else {
            throw new Exception("Unsupporter idCategorieSection : " + idCategorieSection);
        }

        // on créé un numero de facture unique qui servira a creer la section
        return CAUtil.creerNumeroSectionUnique(sessionOsiris, transaction, IntRole.ROLE_RENTIER, idExterneRole,
                typeSection, String.valueOf(new JADate(dateComptable).getYear()), idCategorieSection);

    }

    /**
     * @return
     */
    public String getNumeroPassage() {
        return numeroPassage;
    }

    protected APIRubrique getRubriqueRestitution(RERenteAccordee ra) throws Exception {

        switch (ra.getGroupeGenreRente()) {
            case REPrestationsAccordees.GROUPE_API_AI:
                return REModuleComptableFactory.getInstance().PRST_API_AI_RESTITUER;

            case REPrestationsAccordees.GROUPE_API_AVS:
                return REModuleComptableFactory.getInstance().PRST_API_AVS_RESTITUER;

            case REPrestationsAccordees.GROUPE_REO_AI:
                return REModuleComptableFactory.getInstance().PRST_AI_RESTITUER;

            case REPrestationsAccordees.GROUPE_REO_AVS:
                return REModuleComptableFactory.getInstance().PRST_AVS_RESTITUER;

            case REPrestationsAccordees.GROUPE_RO_AI:
                return REModuleComptableFactory.getInstance().PRST_AI_RESTITUER;

            case REPrestationsAccordees.GROUPE_RO_AVS:
                return REModuleComptableFactory.getInstance().PRST_AVS_RESTITUER;

            default:
                return null;
        }

    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process (constantes dans <code>GlobazJobQueue</code>).
     * 
     * @return la Job Queue à utiliser pour soumettre le process
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    /**
     * @param string
     */
    public void setDateComptable(String string) {
        dateComptable = string;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    /**
     * @param passage
     */
    public void setNumeroPassage(String newIdPassage) {
        numeroPassage = newIdPassage;
    }

}
