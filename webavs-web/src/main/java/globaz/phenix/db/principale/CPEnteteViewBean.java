package globaz.phenix.db.principale;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.translation.CACodeSystem;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (18.09.2002 11:42:39)
 * 
 * @author: Administrator
 */
public class CPEnteteViewBean extends TITiersViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AFAffiliation affiliation = new AFAffiliation();

    private Vector<String> docListe = new Vector<String>();
    private String idAffiliation = "";
    private Boolean irrecouvrable = Boolean.FALSE;

    /**
     * Commentaire relatif au constructeur CPEnteteViewBean.
     */
    public CPEnteteViewBean() {
        super();
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        try {
            // -------- Recherche des données de l'affilié --------
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(getIdAffiliation());
            affiliation.retrieve();
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    public void addDoc(String docName) {
        docListe.add(docName);
    }

    /**
     * @return
     */
    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    /*
     * Formatage pour l'affichage de la description du NNSS
     */
    public String getDescriptionNNSS() throws Exception {
        return this.getNumAvsActuel() + " - " + getDateNaissance() + " - "
                + CodeSystem.getCodeUtilisateur(getSession(), getSexe()) + " - " + getIdPays();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.06.2003 15:44:10)
     * 
     * @return java.util.Vector
     */
    public java.util.Vector<String> getDocListe() {
        return docListe;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /*
     * méthode pour récupérer l'id des comptes annexes avec un num d'aff et un role == 517002
     */
    public String getIdCompteAnnexe() throws Exception {
        // Extraction du compte annexe
        String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
        CACompteAnnexe compte = new CACompteAnnexe();
        compte.setSession(getSession());
        compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compte.setIdRole(role);
        compte.setIdExterneRole(getAffiliation().getAffilieNumero());
        compte.wantCallMethodBefore(false);
        compte.retrieve();
        if ((compte != null) && !compte.isNew()) {
            // Test si irrecouvrable
            // int anneeDeb =
            // JACalendar.getYear(getAffiliation().getDateDebut());
            int anneeFin = JACalendar.getYear(getAffiliation().getDateFin());
            if (anneeFin == 0) {
                anneeFin = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
            }
            setIrrecouvrable(Boolean.FALSE);
            for (int anneeDeb = JACalendar.getYear(getAffiliation().getDateDebut()); (anneeDeb < anneeFin)
                    && getIrrecouvrable().equals(Boolean.FALSE); anneeDeb++) {
                if (compte.hasMotifContentieuxForYear(CACodeSystem.CS_IRRECOUVRABLE, Integer.toString(anneeDeb))) {
                    setIrrecouvrable(Boolean.TRUE);
                }
            }
            return compte.getIdCompteAnnexe();
        } else {
            return null;
        }
    }

    public String getIdCompteIndividuel() throws Exception {
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.orderByAvs(false);
        CICompteIndividuel ci = ciMgr.getCIRegistreAssures(NSUtil.unFormatAVS(this.getNumAvsActuel()), null);
        // Si dernier num avs inexistant dans les cis (ex: Nss non ouvert)
        if (ci == null) {
            TIHistoriqueAvsManager histoMng = new TIHistoriqueAvsManager();
            histoMng.setSession(getSession());
            histoMng.setForIdTiers(getIdTiers());
            histoMng.find();
            for (int i = 0; (i < histoMng.size()) && (ci == null); i++) {
                TIHistoriqueAvs histo = ((TIHistoriqueAvs) histoMng.getEntity(i));
                // Ignorer les erreurs de saisie
                if (!"506006".equalsIgnoreCase(histo.getMotifHistorique())) {
                    String numAvs = histo.getNumAvs();
                    ci = ciMgr.getCIRegistreAssures(NSUtil.unFormatAVS(numAvs), null);
                }
            }
        }
        if ((ci != null) && !ci.isNew()) {
            return ci.getCompteIndividuelId();
        } else {
            return null;
        }

    }

    public Boolean getIrrecouvrable() {
        return irrecouvrable;
    }

    @Override
    public String getLocaliteLong() {
        try {
            TIAdresseDataSource ds = new TIAdresseDataSource();
            ds.setSession(affiliation.getSession());
            ds.load(AFAffiliationUtil.getAdresseExploitation(affiliation));
            return ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " - "
                    + ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param affiliation
     */
    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.06.2003 15:44:10)
     * 
     * @param newDocListe
     *            java.util.Vector
     */
    public void setDocListe(java.util.Vector<String> newDocListe) {
        docListe = newDocListe;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    public void setIrrecouvrable(Boolean irrecouvrable) {
        this.irrecouvrable = irrecouvrable;
    }

}
