package globaz.naos.db.listeAgenceCommunale;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.Vector;

public class AFListeAgenceCommunaleViewBean extends AFAbstractViewBean {

    private java.lang.String date = JACalendar.todayJJsMMsAAAA();
    private java.lang.String email = new String();
    private java.lang.String idTiersAgence = new String();
    private Boolean wantCsv = new Boolean(Boolean.FALSE);

    /**
     * Constructeur d'AFComparaisonAcompteMasseViewBean.
     */
    public AFListeAgenceCommunaleViewBean() {
    }

    public AFListeAgenceCommunaleViewBean(BSession session) {
    }

    public java.lang.String getDate() {
        return date;
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

    public java.lang.String getIdTiersAgence() {
        return idTiersAgence;
    }

    public Boolean getWantCsv() {
        return wantCsv;
    }

    public Vector returnAgenceCommunale() throws Exception {
        TIAdministrationManager agenceManager = new TIAdministrationManager();
        agenceManager.setSession(getSession());
        agenceManager.setForGenreAdministration(CodeSystem.GENRE_ADMIN_AGENCE_COMMUNALE);
        agenceManager.orderByCodeAdministration();
        agenceManager.find(BManager.SIZE_NOLIMIT);
        Vector v = new Vector();
        if (agenceManager.size() > 0) {
            String lineBlank[] = new String[2];
            lineBlank[0] = "";
            lineBlank[1] = "";
            v.add(lineBlank);
            for (int i = 0; i < agenceManager.size(); i++) {
                String line[] = new String[2];

                line[0] = ((TIAdministrationViewBean) agenceManager.getEntity(i)).getIdTiersAdministration();
                line[1] = ((TIAdministrationViewBean) agenceManager.getEntity(i)).getCodeAdministration() + " - "
                        + ((TIAdministrationViewBean) agenceManager.getEntity(i)).getNom();
                // this.setIdTypeAdresseDefaut("1");
                v.add(line);
            }
        }
        return v;
    }

    public void setDate(java.lang.String date) {
        this.date = date;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public void setIdTiersAgence(java.lang.String idTiersAgence) {
        this.idTiersAgence = idTiersAgence;
    }

    public void setWantCsv(Boolean wantCsv) {
        this.wantCsv = wantCsv;
    }
}
