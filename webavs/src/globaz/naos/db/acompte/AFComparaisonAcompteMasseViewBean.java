package globaz.naos.db.acompte;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

public class AFComparaisonAcompteMasseViewBean extends AFAbstractViewBean {

    private java.lang.String anneeAcompte = new String();
    private java.lang.String anneeMasse = new String();
    private java.lang.String differenceTolereeFranc = new String();
    private java.lang.String differenceTolereeTaux = new String();
    private java.lang.String email = new String();

    /**
     * Constructeur d'AFComparaisonAcompteMasseViewBean.
     */
    public AFComparaisonAcompteMasseViewBean() {
    }

    public AFComparaisonAcompteMasseViewBean(BSession session) {
    }

    public java.lang.String getAnneeAcompte() {
        return anneeAcompte;
    }

    public java.lang.String getAnneeMasse() {
        return anneeMasse;
    }

    public java.lang.String getDifferenceTolereeFranc() {
        return differenceTolereeFranc;
    }

    public java.lang.String getDifferenceTolereeTaux() {
        return differenceTolereeTaux;
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public void init(BSession session) {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            setAnneeAcompte(Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
            setAnneeMasse(Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) - 1));
            // --- Chargement paramètre par défaut
            setDifferenceTolereeFranc(FWFindParameter.findParameter(transaction, "1", "AFDIFFCHF",
                    JACalendar.todayJJsMMsAAAA(), "", 2));
            setDifferenceTolereeTaux(FWFindParameter.findParameter(transaction, "1", "AFDIFFTAUX",
                    JACalendar.todayJJsMMsAAAA(), "", 2));
        } catch (Exception e) {
            setAnneeAcompte("");
            setAnneeMasse("");
            setDifferenceTolereeFranc("");
            setDifferenceTolereeTaux("");
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e) {
                // Pas besoin de gérer
            }
        }
    }

    public void setAnneeAcompte(java.lang.String date) {
        anneeAcompte = date;
    }

    public void setAnneeMasse(java.lang.String idTiersAgence) {
        anneeMasse = idTiersAgence;
    }

    public void setDifferenceTolereeFranc(java.lang.String differenceTolereeFranc) {
        this.differenceTolereeFranc = differenceTolereeFranc;
    }

    public void setDifferenceTolereeTaux(java.lang.String differenceTolereeTaux) {
        this.differenceTolereeTaux = differenceTolereeTaux;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }
}
