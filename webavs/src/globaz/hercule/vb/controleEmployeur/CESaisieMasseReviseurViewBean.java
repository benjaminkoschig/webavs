package globaz.hercule.vb.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author JPA
 * @since 15 juil. 2010
 */
public class CESaisieMasseReviseurViewBean extends BJadePersistentObjectViewBean {

    private String dateDebutControle = "";
    private String dateFinControle = "";
    private String datePrevue = "";
    private String genreControle = "";
    private String idAffiliation = "";
    private String idReviseur = "";
    private String infoTiers = "";
    private String numAffilie = "";
    private String widgetReviseur = "";

    /**
     * Constructeur de CESaisieMasseReviseurViewBean
     */
    public CESaisieMasseReviseurViewBean() {
        super();
    }

    /**
     * Cette fonction transfère la date de réception d'un document dans LEO afin de clôturer un envoi
     */
    @Override
    public void add() throws Exception {
        BTransaction transaction = null;
        try {

            transaction = new BTransaction((BSession) getISession());
            transaction.openTransaction();
            if (validate(transaction)) {
                CEControleEmployeurManager manager = new CEControleEmployeurManager();
                manager.setSession((BSession) getISession());
                manager.setForAffiliationId(getIdAffiliation());
                manager.setForDateDebutControle(getDateDebutControle());
                manager.setForDateFinControle(getDateFinControle());
                manager.setForActif(true);
                manager.find();
                if (manager.getSize() > 0) {
                    CEControleEmployeur controleManager = (CEControleEmployeur) manager.getFirstEntity();
                    if (!controleManager.getGenreControle().equals(getGenreControle())) {
                        transaction.addErrors(((BSession) getISession())
                                .getLabel("SAISIE_MASSE_ERREUR_GENRE_DIFFERENT") + "\n");
                    }
                    if (!controleManager.getDatePrevue().equals(getDatePrevue())) {
                        transaction.addErrors(((BSession) getISession())
                                .getLabel("SAISIE_MASSE_ERREUR_DATE_PREVUE_DIFFERENTE") + "\n");
                    }
                    if (!JadeStringUtil.isEmpty(controleManager.getIdReviseur())) {
                        if (!controleManager.getIdReviseur().equals(getIdReviseur())) {
                            controleManager.setIdReviseur(getIdReviseur());
                            try {
                                controleManager.update(transaction);
                                transaction.commit();
                            } catch (Exception e) {
                                transaction.rollback();
                                throw e;
                            }
                        } else {
                            if (!transaction.hasErrors()) {
                                transaction.addErrors(((BSession) getISession())
                                        .getLabel("SAISIE_MASSE_ERREUR_CONTROLE_EXISTANT") + "\n");
                            }
                        }
                    } else {
                        controleManager.setIdReviseur(getIdReviseur());
                        try {
                            controleManager.update(transaction);
                            transaction.commit();
                        } catch (Exception e) {
                            transaction.rollback();
                            throw e;
                        }
                    }
                } else {

                    CEControleEmployeur controle = new CEControleEmployeur();
                    controle.setSession((BSession) getISession());
                    controle.setGenreControle(getGenreControle());
                    controle.setAffiliationId(getIdAffiliation());
                    controle.setNumAffilie(getNumAffilie());
                    controle.setIdReviseur(getIdReviseur());
                    controle.setDateDebutControle(getDateDebutControle());
                    controle.setDateFinControle(getDateFinControle());
                    controle.setDatePrevue(getDatePrevue());
                    try {
                        controle.add(transaction);
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                        throw e;
                    }
                }
            } else {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(transaction.getErrors().toString());
            }

        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
            return;
        } finally {
            if (transaction != null && transaction.isOpened()) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    public String getDatePrevue() {
        return datePrevue;
    }

    public String getGenreControle() {
        return genreControle;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdReviseur() {
        return idReviseur;
    }

    public String getInfoTiers() {
        return infoTiers;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getWidgetReviseur() {
        return widgetReviseur;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    public void setDateDebutControle(String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateFinControle(String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setDatePrevue(String datePrevue) {
        this.datePrevue = datePrevue;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setGenreControle(String genreControle) {
        this.genreControle = genreControle;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdReviseur(String idReviseur) {
        this.idReviseur = idReviseur;
    }

    public void setInfoTiers(String infoTiers) {
        this.infoTiers = infoTiers;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setWidgetReviseur(String widgetReviseur) {
        this.widgetReviseur = widgetReviseur;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }

    private boolean validate(BTransaction transaction) {
        if (JadeStringUtil.isEmpty(getNumAffilie())) {
            transaction.addErrors(((BSession) getISession()).getLabel("SAISIE_MASSE_ERREUR_NUM_AFFILIE") + "\n");
        }
        if (JadeStringUtil.isEmpty(getDatePrevue())) {
            transaction.addErrors(((BSession) getISession()).getLabel("SAISIE_MASSE_ERREUR_DATE_PREVUE") + "\n");
        }
        if (JadeStringUtil.isEmpty(getGenreControle())) {
            transaction.addErrors(((BSession) getISession()).getLabel("SAISIE_MASSE_ERREUR_GENRE_CONTROLE") + "\n");
        }
        if (JadeStringUtil.isEmpty(getIdReviseur())) {
            transaction.addErrors(((BSession) getISession()).getLabel("SAISIE_MASSE_ERREUR_REVISEUR") + "\n");
        }
        if (JadeStringUtil.isEmpty(getDateDebutControle())) {
            transaction.addErrors(((BSession) getISession()).getLabel("SAISIE_MASSE_ERREUR_DATE_DEBUT") + "\n");
        }
        if (JadeStringUtil.isEmpty(getDateFinControle())) {
            transaction.addErrors(((BSession) getISession()).getLabel("SAISIE_MASSE_ERREUR_DATE_FIN") + "\n");
        }
        if (transaction.hasErrors()) {
            return false;
        } else {
            return true;
        }
    }
}
