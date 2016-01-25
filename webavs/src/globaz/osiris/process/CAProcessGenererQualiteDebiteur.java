/*
 * Créé le 6 mars 06
 */
package globaz.osiris.process;

import globaz.aquila.api.ICOEtape;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.genererQualiteDebiteur.CAGenererQualiteDebiteur;
import globaz.osiris.db.genererQualiteDebiteur.CAGenererQualiteDebiteurManager;
import globaz.osiris.external.IntRole;

/**
 * @author dvh
 */
public class CAProcessGenererQualiteDebiteur extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CATEGORIE_INDEPENDANT_EMPLOYEUR = "804005";
    private static final String CODE_0 = "243001";
    private static final String CODE_1 = "243002";
    private static final String CODE_2 = "243003";
    private static final String CODE_3 = "243004";
    private static final String CODE_4 = "243005";
    private static final String ETAPE_JUSQUA_SOMMATION_SQL_FORMAT = "0, " + ICOEtape.CS_ARD_CREE + ", "
            + ICOEtape.CS_AUCUNE + ", " + ICOEtape.CS_CONTENTIEUX_CREE + ", " + ICOEtape.CS_PREMIER_RAPPEL_ENVOYE
            + ", " + ICOEtape.CS_DEUXIEME_RAPPEL_ENVOYE;
    public static final int NOMBRE_JOUR_ANNEE_NEGATIF = -364;

    private String dateExecution = null;

    /**
	 *
	 */
    public CAProcessGenererQualiteDebiteur() {
        super();
    }

    /**
     * @param parent
     */
    public CAProcessGenererQualiteDebiteur(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CAProcessGenererQualiteDebiteur(BSession session) {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        BTransaction transaction = null;

        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            JACalendarGregorian dateSection = new JACalendarGregorian();
            String dateSectionStr = dateSection.addDays(getDateExecution(),
                    CAProcessGenererQualiteDebiteur.NOMBRE_JOUR_ANNEE_NEGATIF);

            CAGenererQualiteDebiteurManager qualiteDebiteurManager = new CAGenererQualiteDebiteurManager();
            qualiteDebiteurManager.setSession(getSession());
            qualiteDebiteurManager.setForSelectionRole(IntRole.ROLE_AFFILIE + "," + IntRole.ROLE_AFFILIE_PARITAIRE
                    + "," + IntRole.ROLE_AFFILIE_PERSONNEL);
            qualiteDebiteurManager.setForIdCategorie("804002,804005,804001");
            qualiteDebiteurManager.setFromDate(dateSectionStr);
            qualiteDebiteurManager
                    .setLastEtatAquilaNotIn(CAProcessGenererQualiteDebiteur.ETAPE_JUSQUA_SOMMATION_SQL_FORMAT);
            qualiteDebiteurManager.setForSoldeGreater("0");
            qualiteDebiteurManager.setForIdGenreCompte(CACompteAnnexe.GENRE_COMPTE_STANDARD);
            qualiteDebiteurManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            qualiteDebiteurManager.find();

            if (isAborted()) {
                return false;
            }

            if (!qualiteDebiteurManager.isEmpty()) {

                setProgressScaleValue(qualiteDebiteurManager.size());
                setProgressCounter(0);

                CAGenererQualiteDebiteur qualiteDeb;
                String oldIdCompteAnnexe = new String();
                int compteur1 = 0;
                int compteur2 = 0;
                for (int i = 0; (i < qualiteDebiteurManager.size()) && !isAborted(); i++) {
                    qualiteDeb = (CAGenererQualiteDebiteur) qualiteDebiteurManager.getEntity(i);
                    if (!qualiteDeb.getIdCompteAnnexe().equals(oldIdCompteAnnexe)) {
                        if (!JadeStringUtil.isBlankOrZero(oldIdCompteAnnexe)) {
                            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                            compteAnnexe.setSession(getSession());
                            compteAnnexe.setIdCompteAnnexe(oldIdCompteAnnexe);
                            compteAnnexe.retrieve();
                            if (!compteAnnexe.isNew()) {
                                majCompteAnnexe(transaction, compteAnnexe, compteur1, compteur2);
                                if (transaction.hasErrors()) {
                                    throw new Exception("process non terminé à cause d'une erreur ("
                                            + compteAnnexe.getIdExterneRole() + ") : "
                                            + transaction.getErrors().toString());
                                }
                            }
                        }
                        oldIdCompteAnnexe = qualiteDeb.getIdCompteAnnexe();
                        compteur1 = 0;
                        compteur2 = 0;
                    }
                    // le cas est au contentieux, on incrémente
                    compteur1++;
                    compteur2++;

                    // Si taxation d'office, celle-ci vaut sommation directement
                    if (qualiteDeb.getCategorieSection().equals("227030")) {
                        compteur2++;
                    }
                    // si le delai a été muté une ou 2 fois
                    int nbDelaiMute = Integer.parseInt("".equals(qualiteDeb.getNombreDelaiMute()) ? "0" : qualiteDeb
                            .getNombreDelaiMute());

                    if (nbDelaiMute == 1) {
                        compteur1++;
                    } else if (nbDelaiMute > 1) {
                        compteur2++;
                    }
                    incProgressCounter();
                }
            }

            if (transaction.hasErrors() || isAborted()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
                transaction.rollback();
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            e.printStackTrace();
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                }
            }
        }

        return false;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isIntegerEmpty(dateExecution)) {
            this._addError(getTransaction(), getSession().getLabel("DATE_REQUISE"));
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_GENERATION_QUALITE_DEBITEUR_ERROR");
        } else {
            return getSession().getLabel("PROCESS_GENERATION_QUALITE_DEBITEUR_OK");
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param transaction
     * @param compteAnnexe
     * @param compteur1
     * @param compteur2
     * @param periodicite
     * @throws Exception
     */
    private void majCompteAnnexe(BTransaction transaction, CACompteAnnexe compteAnnexe, int compteur1, int compteur2)
            throws Exception {

        String periodicite = compteAnnexe.getRole().getPeriodicite();

        if (IntRole.ROLE_AFFILIE_PERSONNEL.equals(compteAnnexe.getIdRole())
                && CAProcessGenererQualiteDebiteur.CATEGORIE_INDEPENDANT_EMPLOYEUR
                        .equals(compteAnnexe.getIdCategorie())) {

            if (compteur2 > 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_4);
            } else if (compteur2 == 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_3);
            } else if (compteur1 > 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_2);
            } else if (compteur1 == 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_1);
            } else {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_0);
            }
        } else if (IntRole.PERIODICITE_MENSUELLE.equals(periodicite)) {

            if (compteur2 > 8) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_4);
            } else if (compteur2 >= 6) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_3);
            } else if (compteur1 > 5) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_2);
            } else if (compteur1 >= 4) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_1);
            } else {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_0);
            }
        } else if (IntRole.PERIODICITE_TRIMESTRIELLE.equals(periodicite)) {

            if (compteur2 > 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_4);
            } else if (compteur2 == 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_3);
            } else if (compteur1 > 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_2);
            } else if (compteur1 == 2) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_1);
            } else {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_0);
            }
        } else if (IntRole.PERIODICITE_SEMESTRIELLE.equals(periodicite)
                || IntRole.PERIODICITE_ANNUELLE.equals(periodicite)) {

            if (compteur2 > 1) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_4);
            } else if (compteur2 == 1) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_3);
            } else if (compteur1 > 1) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_2);
            } else if (compteur1 == 1) {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_1);
            } else {
                compteAnnexe.setQualiteDebiteur(CAProcessGenererQualiteDebiteur.CODE_0);
            }
        }

        compteAnnexe.update(transaction);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setDateExecution(String string) {
        dateExecution = string;
    }

}
