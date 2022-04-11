package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.process.ebill.EBillMail;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CACompteAnnexeViewBean extends CACompteAnnexe implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<String> collectionMotifMan = null;
    private Collection<String> commentaire = null;
    private Collection<String> dateDebutMotif = null;
    private Collection<String> dateFinMotif = null;
    private Collection<String> idCompteAnnexeMotif = null;
    private Collection<String> idMotifBlocage = null;
    private Collection<String> idMotifContentieux = null;
    private Collection<String> idSectionMotif = null;
    private CAMotifContentieuxManager mgr = null;

    /**
     * Constructor for CACompteAnnexeViewBean.
     */
    public CACompteAnnexeViewBean() {
        super();

        idMotifContentieux = new ArrayList<String>();
        idCompteAnnexeMotif = new ArrayList<String>();
        idSectionMotif = new ArrayList<String>();
        idMotifBlocage = new ArrayList<String>();
        dateDebutMotif = new ArrayList<String>();
        dateFinMotif = new ArrayList<String>();
        commentaire = new ArrayList<String>();
        collectionMotifMan = new ArrayList<String>();

        mgr = new CAMotifContentieuxManager();
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
                if (!(((ArrayList<String>) dateDebutMotif).get(i)).equals("")
                        && !collectionMotifMan.contains(((ArrayList<String>) idMotifContentieux).get(i))) {
                    motif.setSession(getSession());
                    motif.setIdCompteAnnexe(getIdCompteAnnexe());
                    motif.setIdMotifBlocage(((ArrayList<String>) idMotifBlocage).get(i));
                    motif.setDateDebut(((ArrayList<String>) dateDebutMotif).get(i));
                    motif.setDateFin(((ArrayList<String>) dateFinMotif).get(i));
                    motif.setCommentaire(((ArrayList<String>) commentaire).get(i));
                    motif.add(transaction);
                    // Update
                } else if (!(((ArrayList<String>) idMotifContentieux).get(i)).equals("")
                        && collectionMotifMan.contains(((ArrayList<String>) idMotifContentieux).get(i))) {
                    motif.setSession(getSession());
                    motif.setIdMotifContentieux(((ArrayList<String>) idMotifContentieux).get(i));
                    motif.retrieve(transaction);
                    if (!motif.isNew()) {
                        // motif.setIdMotifContentieux((String)((ArrayList)idMotifContentieux).get(i));
                        motif.setIdCompteAnnexe(getIdCompteAnnexe());
                        motif.setIdMotifBlocage(((ArrayList<String>) idMotifBlocage).get(i));
                        motif.setDateDebut(((ArrayList<String>) dateDebutMotif).get(i));
                        motif.setDateFin(((ArrayList<String>) dateFinMotif).get(i));
                        motif.setCommentaire(((ArrayList<String>) commentaire).get(i));
                        motif.update(transaction);
                    }
                } else if (getContEstBloque().booleanValue()) {
                    _addError(transaction, getSession().getLabel("COMPTE_ANNEXE_NE_DOIT_PAS_ETRE_BLOQUE"));
                }
            }
            // Suppression
            for (int i = 0; i < collectionMotifMan.size(); i++) {
                CAMotifContentieux motif = new CAMotifContentieux();
                if (!idMotifContentieux.contains(((ArrayList<String>) collectionMotifMan).get(i))) {
                    motif.setSession(getSession());
                    motif.setIdMotifContentieux(((ArrayList<String>) collectionMotifMan).get(i));
                    motif.retrieve(transaction);
                    if (!motif.isNew()) {
                        motif.setIdMotifContentieux(((ArrayList<String>) collectionMotifMan).get(i));
                        motif.delete(transaction);
                    }
                }
            }
        } else {
            mgr.setSession(getSession());
            mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                CAMotifContentieux motif = (CAMotifContentieux) mgr.getEntity(i);
                motif.delete();
            }
        }
    }

    /**
     * Initialisation des motif
     * 
     * @throws Exception
     */
    public void _initialise() throws Exception {
        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            idMotifContentieux = new ArrayList<String>();
            idCompteAnnexeMotif = new ArrayList<String>();
            idSectionMotif = new ArrayList<String>();
            idMotifBlocage = new ArrayList<String>();
            dateDebutMotif = new ArrayList<String>();
            dateFinMotif = new ArrayList<String>();
            commentaire = new ArrayList<String>();
            collectionMotifMan = new ArrayList<String>();

            mgr.setSession(getSession());
            mgr.setForIdCompteAnnexe(getIdCompteAnnexe());
            mgr.find();
            fillContainer();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.db.comptes.CACompteAnnexe#_validate(globaz.globall.db. BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            // OSIRIS
            if (JadeStringUtil.isEmpty(getIdContMotifBloque())) {
                setContEstBloque(Boolean.FALSE);
            } else {
                setContEstBloque(Boolean.TRUE);
            }
        } else {
            // AQUILA
            if ((idMotifBlocage == null) || idMotifBlocage.isEmpty() || "[]".equals(idMotifBlocage.toString())) {
                setContEstBloque(Boolean.FALSE);
            } else {
                setContEstBloque(Boolean.TRUE);
            }
        }
        validateEBill(statement);
        super._validate(statement);
    }

    /**
     * Contrôle : le champs eBill AccountId ne doit pas exister pour un autre affilié
     * @param statement
     */
    private void validateEBill(BStatement statement) {
        if(StringUtils.isNotEmpty(geteBillAccountID())) {
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            manager.setForEBillAccountID(geteBillAccountID());
            try {
                manager.find(BManager.SIZE_USEDEFAULT);
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
            }
            if(!manager.isEmpty()) {
                List<CACompteAnnexe> list = manager.getContainer();
                Optional<CACompteAnnexe> eBillIdAutreCompte = list.stream().filter(c -> !c.getIdTiers().equals(getIdTiers())).findFirst();
                if(eBillIdAutreCompte.isPresent()) {
                    _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("EBILL_COMPTE_ANNEXE_CONTROLE"), eBillIdAutreCompte.get().getIdExterneRole()));
                }
            }
        }
        if(StringUtils.isNotEmpty(geteBillMail()) && !EBillMail.isMailValid(geteBillMail())) {
            _addError(statement.getTransaction(), FWMessageFormat.format(getSession().getLabel("EBILL_MAIL_FORMAT"), geteBillMail()));
        }
    }

    /**
     * Rempli le container pour afficher le tableau à l'écran
     */
    private void fillContainer() {
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
     * @return
     */
    public Collection<String> getCommentaire() {
        return commentaire;
    }

    /**
     * @return
     */
    public Collection<String> getDateDebutMotif() {
        return dateDebutMotif;
    }

    /**
     * @return
     */
    public Collection<String> getDateFinMotif() {
        return dateFinMotif;
    }

    /**
     * Cette méthode retourne la date de début et de fin de blocage du contentieux
     * 
     * @return String date début - date fin
     */
    public String getEtatContentieux() {
        String etatContentieux = "";
        if (getContEstBloque().booleanValue()) {
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                etatContentieux = getContDateDebBloque() + " - " + getContDateFinBloque();
            } else {
                CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
                motifMgr.setSession(getSession());
                motifMgr.setForIdCompteAnnexe(getIdCompteAnnexe());
                motifMgr.setFromDateBetweenDebutFin(JACalendar.todayJJsMMsAAAA());
                try {
                    motifMgr.find();
                    if (!motifMgr.isEmpty()) {
                        CAMotifContentieux motif = (CAMotifContentieux) motifMgr.getEntity(0);
                        etatContentieux = motif.getDateDebut() + " - " + motif.getDateFin();
                    }
                } catch (Exception e) {
                }
            }
        }
        return etatContentieux;
    }

    /**
     * @return
     */
    public Collection<String> getIdCompteAnnexeMotif() {
        return idCompteAnnexeMotif;
    }

    /**
     * @return
     */
    public Collection<String> getIdMotifBlocage() {
        return idMotifBlocage;
    }

    /**
     * @return
     */
    public Collection<String> getIdMotifContentieux() {
        return idMotifContentieux;
    }

    /**
     * @return
     */
    public Collection<String> getIdSectionMotif() {
        return idSectionMotif;
    }

    /**
     * @param collection
     */
    public void setCommentaire(Collection<String> commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * @param collection
     */
    public void setDateDebutMotif(Collection<String> dateDebutMotif) {
        this.dateDebutMotif = dateDebutMotif;
    }

    /**
     * @param collection
     */
    public void setDateFinMotif(Collection<String> dateFinMotif) {
        this.dateFinMotif = dateFinMotif;
    }

    /**
     * @param collection
     */
    public void setIdCompteAnnexeMotif(Collection<String> idCompteAnnexeMotif) {
        this.idCompteAnnexeMotif = idCompteAnnexeMotif;
    }

    /**
     * @param collection
     */
    public void setIdMotifBlocage(Collection<String> idMotifBlocage) {
        this.idMotifBlocage = idMotifBlocage;
    }

    /**
     * @param collection
     */
    public void setIdMotifContentieux(Collection<String> idMotifContentieux) {
        this.idMotifContentieux = idMotifContentieux;
    }

    /**
     * @param collection
     */
    public void setIdSectionMotif(Collection<String> idSectionMotif) {
        this.idSectionMotif = idSectionMotif;
    }

}
