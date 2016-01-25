package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.pyxis.api.osiris.TITiersAdministrationOSI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author user
 */
public class CASectionViewBean extends CASection implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_SECTION = "SECTION";
    private static final String TEXTAREA_RETURN = "\r\n";

    private Collection collectionMotifMan = null;
    private Collection commentaire = null;
    private Collection dateDebutMotif = null;
    private Collection dateFinMotif = null;
    private Collection idCompteAnnexeMotif = null;
    private Collection idMotifBlocage = null;
    private Collection idMotifContentieux = null;
    private Collection idSectionMotif = null;
    private CAMotifContentieuxManager mgr = new CAMotifContentieuxManager();

    /**
     * Constructor for CASectionViewBean.
     */
    public CASectionViewBean() {
        super();
        mgr.setSession(getSession());
        mgr.changeManagerSize(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        if (idMotifContentieux != null) {
            for (int i = 0; i < idMotifContentieux.size(); i++) {
                CAMotifContentieux motif = new CAMotifContentieux();
                // Ajout
                if (!((ArrayList) dateDebutMotif).get(i).equals("")
                        && !collectionMotifMan.contains(((ArrayList) idMotifContentieux).get(i))) {
                    motif.setSession(getSession());
                    motif.setIdSection(getIdSection());
                    motif.setIdMotifBlocage((String) ((ArrayList) idMotifBlocage).get(i));
                    motif.setDateDebut((String) ((ArrayList) dateDebutMotif).get(i));
                    motif.setDateFin((String) ((ArrayList) dateFinMotif).get(i));
                    motif.setCommentaire((String) ((ArrayList) commentaire).get(i));
                    motif.add(transaction);
                    // Update
                } else if (!((ArrayList) idMotifContentieux).get(i).equals("")
                        && collectionMotifMan.contains(((ArrayList) idMotifContentieux).get(i))) {
                    motif.setSession(getSession());
                    motif.setIdMotifContentieux((String) ((ArrayList) idMotifContentieux).get(i));
                    motif.retrieve(transaction);
                    if (!motif.isNew()) {
                        motif.setIdSection(getIdSection());
                        motif.setIdMotifBlocage((String) ((ArrayList) idMotifBlocage).get(i));
                        motif.setDateDebut((String) ((ArrayList) dateDebutMotif).get(i));
                        motif.setDateFin((String) ((ArrayList) dateFinMotif).get(i));
                        motif.setCommentaire((String) ((ArrayList) commentaire).get(i));
                        motif.update(transaction);
                    }
                } else if (getContentieuxEstSuspendu().booleanValue()) {
                    _addError(transaction, getSession().getLabel("SECTION_NE_DOIT_PAS_ETRE_BLOQUE"));
                }
            }
            // Suppression
            for (int i = 0; i < collectionMotifMan.size(); i++) {
                CAMotifContentieux motif = new CAMotifContentieux();
                if (!idMotifContentieux.contains(((ArrayList) collectionMotifMan).get(i))) {
                    motif.setSession(getSession());
                    motif.setIdMotifContentieux((String) ((ArrayList) collectionMotifMan).get(i));
                    motif.retrieve(transaction);
                    if (!motif.isNew()) {
                        motif.setIdMotifContentieux((String) ((ArrayList) collectionMotifMan).get(i));
                        motif.delete(transaction);
                    }
                }
            }
        } else {
            mgr.setSession(getSession());
            mgr.setForIdSection(getIdSection());
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                CAMotifContentieux motif = (CAMotifContentieux) mgr.getEntity(i);
                motif.delete();
            }
        }
        // Bug 6790
        if (!transaction.hasErrors()) {
            setTexteRemarque("");
        }
    }

    /**
     * Initialisation des motif
     * 
     * @throws Exception
     */
    public void _initialise() throws Exception {
        mgr.setSession(getSession());
        mgr.setForIdSection(getIdSection());
        mgr.find();
        fillContainer();
        // Bug 6790
        setTexteRemarque("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CASection#_validate(globaz.globall.db.BStatement )
     */
    @Override
    protected void _validate(BStatement statement) {
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            // OSIRIS
            if (JadeStringUtil.isEmpty(getIdMotifContentieuxSuspendu())) {
                setContentieuxEstSuspendu(Boolean.FALSE);
            } else {
                setContentieuxEstSuspendu(Boolean.TRUE);
            }
        } else {
            // AQUILA
            if ((idMotifBlocage == null) || idMotifBlocage.isEmpty() || "[]".equals(idMotifBlocage.toString())) {
                setContentieuxEstSuspendu(Boolean.FALSE);
            } else {
                setContentieuxEstSuspendu(Boolean.TRUE);
            }
        }

        super._validate(statement);
    }

    private void clearContainer() {
        collectionMotifMan = new ArrayList();
        idMotifContentieux = new ArrayList();
        idCompteAnnexeMotif = new ArrayList();
        idSectionMotif = new ArrayList();
        idMotifBlocage = new ArrayList();
        dateDebutMotif = new ArrayList();
        dateFinMotif = new ArrayList();
        commentaire = new ArrayList();

    }

    /**
     * Rempli le container pour afficher le tableau à l'écran
     */
    private void fillContainer() {
        clearContainer();
        for (int i = 0; i < mgr.size(); i++) {
            CAMotifContentieux motif = (CAMotifContentieux) mgr.getEntity(i);
            collectionMotifMan.add(motif.getIdMotifContentieux());
            idMotifContentieux.add(motif.getId());
            idCompteAnnexeMotif.add(motif.getIdCompteAnnexe());
            idSectionMotif.add(motif.getIdSection());
            idMotifBlocage.add(motif.getIdMotifBlocage());
            dateDebutMotif.add(motif.getDateDebut());
            dateFinMotif.add(JACalendar.format(motif.getDateFin()));
            commentaire.add(motif.getCommentaire());
        }
    }

    /**
     * Retourne le libellé de la caisse professionnelle.
     * 
     * @return Le libellé. Si id vide => return "".
     */
    public String getCaisseProfessionnelleLibelle() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationLibelle(pyxisSession, getIdCaisseProfessionnelle());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Retourne le numéro de la caisse professionnelle.
     * 
     * @return Le numéro. Si id vide => return "".
     */
    public String getCaisseProfessionnelleNumero() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationNumero(pyxisSession, getIdCaisseProfessionnelle());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public Collection getCommentaire() {
        return commentaire;
    }

    /**
     * @return
     */
    public Collection getDateDebutMotif() {
        return dateDebutMotif;
    }

    /**
     * @return
     */
    public Collection getDateFinMotif() {
        return dateFinMotif;
    }

    /**
     * @return
     */
    public Collection getIdCompteAnnexeMotif() {
        return idCompteAnnexeMotif;
    }

    /**
     * @return
     */
    public Collection getIdMotifBlocage() {
        return idMotifBlocage;
    }

    /**
     * @return
     */
    public Collection getIdMotifContentieux() {
        return idMotifContentieux;
    }

    /**
     * @return
     */
    public Collection getIdSectionMotif() {
        return idSectionMotif;
    }

    /**
     * Retourne le descriptif de la section principale liée.
     * 
     * @return
     */
    public String getSectionPrincipalInfos() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSectionPrincipal())) {
            CASection section = new CASection();
            section.setSession(getSession());
            section.setIdSection(getIdSectionPrincipal());

            try {
                section.retrieve();
            } catch (Exception e) {
                return "";
            }
            if (section.isNew()) {
                return "";
            }
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) section.getCompteAnnexe();
            if (compteAnnexe == null) {
                return "";
            }

            return getSession().getLabel(CASectionViewBean.LABEL_SECTION) + " " + section.getIdExterne()
                    + CASectionViewBean.TEXTAREA_RETURN + compteAnnexe.getTitulaireEntete();
        } else {
            return "";
        }
    }

    /**
     * Retourne la liste des sections auxiliaires liées à la section principale en cours.
     * 
     * @return
     */
    public String getSectionsAuxInfos() {
        String result = "";

        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setSession(getSession());
        sectionManager.setForIdSectionPrinc(getIdSection());

        try {
            sectionManager.find();
        } catch (Exception e) {
            return "";
        }

        for (int i = 0; i < sectionManager.size(); i++) {
            CASection section = (CASection) sectionManager.get(i);
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) section.getCompteAnnexe();

            if (compteAnnexe == null) {
                result += getSession().getLabel(CASectionViewBean.LABEL_SECTION) + " " + section.getIdExterne()
                        + CASectionViewBean.TEXTAREA_RETURN;
            } else {
                result += getSession().getLabel(CASectionViewBean.LABEL_SECTION) + " " + section.getIdExterne();
                result += ", " + compteAnnexe.getRole().getDescription(getSession().getIdLangueISO()) + " "
                        + compteAnnexe.getIdExterneRole() + CASectionViewBean.TEXTAREA_RETURN;
            }
        }

        return result;
    }

    /**
     * @param collection
     */
    public void setCommentaire(Collection commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * @param collection
     */
    public void setDateDebutMotif(Collection dateDebutMotif) {
        this.dateDebutMotif = dateDebutMotif;
    }

    /**
     * @param collection
     */
    public void setDateFinMotif(Collection dateFinMotif) {
        this.dateFinMotif = dateFinMotif;
    }

    /**
     * @param collection
     */
    public void setIdCompteAnnexeMotif(Collection idCompteAnnexeMotif) {
        this.idCompteAnnexeMotif = idCompteAnnexeMotif;
    }

    /**
     * @param collection
     */
    public void setIdMotifBlocage(Collection idMotifBlocage) {
        this.idMotifBlocage = idMotifBlocage;
    }

    /**
     * @param collection
     */
    public void setIdMotifContentieux(Collection idMotifContentieux) {
        this.idMotifContentieux = idMotifContentieux;
    }

    /**
     * @param collection
     */
    public void setIdSectionMotif(Collection idSectionMotif) {
        this.idSectionMotif = idSectionMotif;
    }

}
