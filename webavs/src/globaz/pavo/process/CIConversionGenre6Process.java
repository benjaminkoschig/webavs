package globaz.pavo.process;

import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;

public class CIConversionGenre6Process extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String selectedIdValue = "";

    public CIConversionGenre6Process() {
        super();
    }

    public CIConversionGenre6Process(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        String idCompteIndividuel = "";

        try {
            // on récupère l'écriture
            CIEcriture ecriture = new CIEcriture();
            ecriture.setSession(getSession());
            ecriture.setEcritureId(getSelectedIdValue());
            ecriture.retrieve();

            idCompteIndividuel = ecriture.getCompteIndividuelId();
            // maj
            ecriture.setGenreEcriture(CIEcriture.CS_CIGENRE_6);
            ecriture.setIdTypeCompte(CIEcriture.CS_GENRE_6);

            // on récupère le CI en cours
            CICompteIndividuel ci = new CICompteIndividuel();
            ci.setSession(getSession());
            ci.setCompteIndividuelId(idCompteIndividuel);
            ci.retrieve();

            // si CI deja type 6, on update l'écriture
            if (CICompteIndividuel.CS_REGISTRE_GENRES_6.equals(ci.getRegistre())) {
                ecriture.setCompteIndividuelId(ci.getCompteIndividuelId());
                ecriture.setForAffilieParitaire(true);
                ecriture.update();
                majDs(ecriture.getEcritureId(), ci.getCompteIndividuelId());
            } else {// sinon on cherche si la personne possède deja un CI en
                // genre 6 (par navs)
                CICompteIndividuelManager mgr = new CICompteIndividuelManager();
                mgr.setSession(getSession());
                mgr.setForNumeroAvs(ecriture.getAvs());
                mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
                if (!JadeStringUtil.isBlank(ecriture.getAvs())) {
                    mgr.find();
                }

                if (mgr.size() > 0) {// si le CI genre 6 existe, on lui ajoute
                    // l'écriture
                    CICompteIndividuel g6 = (CICompteIndividuel) mgr.getFirstEntity();
                    ecriture.setCompteIndividuelId(g6.getCompteIndividuelId());
                    ecriture.update();
                    majDs(ecriture.getEcritureId(), g6.getCompteIndividuelId());
                } else {// sinon on effectue une recherche par nom prénom

                    CICompteIndividuelManager mgr2 = new CICompteIndividuelManager();
                    mgr2.setSession(getSession());
                    mgr2.setForNomPrenom(ecriture.getNomPrenom());
                    if (!JadeStringUtil.isBlank(ecriture.getAvs())) {
                        mgr2.setForNumeroAvs(ecriture.getAvs());
                    }
                    mgr2.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
                    if (!JadeStringUtil.isBlankOrZero(ecriture.getNomPrenom())) {
                        mgr2.find();
                    }

                    if (mgr2.size() > 0) {// si on retrouve le ci par le nom
                        // prénom, on lui assigne l'écriture
                        CICompteIndividuel g6 = (CICompteIndividuel) mgr2.getFirstEntity();
                        ecriture.setCompteIndividuelId(g6.getCompteIndividuelId());
                        ecriture.update();
                        majDs(ecriture.getEcritureId(), g6.getCompteIndividuelId());
                    } else {// Aucun CI existant de type 6, on en créé un
                        // nouveau

                        CICompteIndividuel crci = new CICompteIndividuel();
                        crci.setSession(getSession());
                        crci.setDateNaissance(ecriture.getDateDeNaissance());
                        crci.setNumeroAvs(ecriture.getAvs());
                        crci.setNomPrenom(ecriture.getNomPrenom());
                        crci.setPaysOrigineId(ecriture.getPaysCode());
                        crci.setDateCreation(JACalendar.todayJJsMMsAAAA());
                        crci.setRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
                        crci.add();
                        // on rattache l'écriture au nouveau CI
                        ecriture.setCompteIndividuelId(crci.getCompteIndividuelId());
                        ecriture.update();
                        majDs(ecriture.getEcritureId(), crci.getCompteIndividuelId());
                        // on recherche les écritures pour le CI de départ
                        /*
                         * CIEcritureManager emgr = new CIEcritureManager(); emgr.setSession(getSession());
                         * emgr.setForCompteIndividuelId(idCompteIndividuel); emgr.find();
                         * 
                         * if(emgr.size()<1){ //s'il n'existe pas d'autres écritures que celle traitée, on supprimer le
                         * CI de départ ci.delete(); }
                         */
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(false);
        setSendMailOnError(true);
        setControleTransaction(true);
    }

    @Override
    protected String getEMailObject() {
        return "test";
    }

    public String getSelectedIdValue() {
        return selectedIdValue;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void majDs(String ecritureId, String compteIndividuelId) throws Exception {
        DSInscriptionsIndividuellesManager mgr = new DSInscriptionsIndividuellesManager();
        mgr.setSession(getSession());
        mgr.setForIdEcrtirureCI(ecritureId);
        mgr.find(getTransaction());
        if (mgr.size() > 0) {
            DSInscriptionsIndividuelles insc = (DSInscriptionsIndividuelles) mgr.getFirstEntity();
            insc.wantCallValidate(false);
            insc.wantCallMethodBefore(false);
            insc.wantCallMethodAfter(false);
            insc.setCompteIndividuelId(compteIndividuelId);
            insc.update(getTransaction());
        }

    }

    public void setSelectedIdValue(String selectedIdValue) {
        this.selectedIdValue = selectedIdValue;
    }
}
