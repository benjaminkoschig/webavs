package globaz.corvus.itext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.webavs.common.CommonProperties;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 
 * @author JJE
 */
public class RERecapitulationRentes extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FICHIER_RESULTAT = "recapitulationRentes";
    private static final String RE_RECAP_RENTES = "RE_RECAP_RENTES";
    private boolean hasNext = true;
    private REDetailRecapMensuelleViewBean reDetRecMenViewBean = null;

    public RERecapitulationRentes() {
        super();
    }

    public RERecapitulationRentes(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, REApplication.APPLICATION_CORVUS_REP, RERecapitulationRentes.FICHIER_RESULTAT);
    }

    public RERecapitulationRentes(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, REApplication.APPLICATION_CORVUS_REP, RERecapitulationRentes.FICHIER_RESULTAT);
    }

    private String afficheMnt(String mnt) {

        if (JadeStringUtil.isBlankOrZero(removeCot(mnt))) {
            return "0.00";
        } else {
            return JANumberFormatter.format(removeCot(mnt), 0.01, 2, JANumberFormatter.NEAR);
        }

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {

            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RECAPITULATION_RENTES);
            // ajout de la période pour la mise en GED
            getDocumentInfo().setDocumentProperty("corvus.recap.periode", reDetRecMenViewBean.getDateRapport());

            // le document est effacé à la fin du process
            setDeleteOnExit(true);

            // on set le modele
            setTemplateFile(RERecapitulationRentes.RE_RECAP_RENTES);

            // on set les paramétres du rapport
            getImporter().setParametre("P_TITRE", getSession().getLabel("DOCUMENT_RECAP_RENTES_TITRE"));

            String pMois = getSession().getLabel("DOCUMENT_RECAP_RENTES_MOIS") + " "
                    + reDetRecMenViewBean.getDateRapport().substring(0, 2);
            getImporter().setParametre("P_MOIS", pMois);
            String pAnnee = /*
                             * getSession().getLabel("DOCUMENT_RECAP_RENTES_NO")+ " "+
                             */reDetRecMenViewBean.getDateRapport().substring(3, 7);
            getImporter().setParametre("P_NO", pAnnee);

            String noCaisse = PRAbstractApplication.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)
                    .getProperty(CommonProperties.KEY_NO_CAISSE);
            String noAgence = PRAbstractApplication.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)
                    .getProperty(CommonProperties.KEY_NO_AGENCE);

            TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
            tiAdministrationMgr.setSession(getSession());
            tiAdministrationMgr.setForCodeAdministration(noCaisse);
            tiAdministrationMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);

            tiAdministrationMgr.find();

            TIAdministrationViewBean tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();

            if (null != tiAdministration) {
                TIAdresseDataManager tiAdresseDatamgr = new TIAdresseDataManager();
                tiAdresseDatamgr.setSession(getSession());
                tiAdresseDatamgr.setForIdTiers(tiAdministration.getIdTiers());
                tiAdresseDatamgr.changeManagerSize(0);
                tiAdresseDatamgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
                tiAdresseDatamgr.find();

                TIAdresseDataSource dataSource = new TIAdresseDataSource();
                dataSource.load((TIAbstractAdresseData) tiAdresseDatamgr.getFirstEntity(), "");

                getImporter().setParametre("P_NOM_CAISSE", dataSource.ligne1);
            }

            // Correction du bug BZ-4825
            // équivalent à un StringBuffer, mais pas synchronizé
            StringBuilder pNoCaisseBuilder = new StringBuilder();
            pNoCaisseBuilder.append(getSession().getLabel("DOCUMENT_RECAP_RENTES_NOCAISSE"));
            pNoCaisseBuilder.append(" ");
            pNoCaisseBuilder.append(noCaisse).append(".").append(noAgence);

            getImporter().setParametre("P_NO_CAISSE", pNoCaisseBuilder.toString());
            getImporter().setParametre("P_AVS_RO", getSession().getLabel("DOCUMENT_RECAP_RENTES_RO"));
            getImporter().setParametre("P_AVS_REO", getSession().getLabel("DOCUMENT_RECAP_RENTES_REO"));
            getImporter().setParametre("P_AVS_API", getSession().getLabel("DOCUMENT_RECAP_RENTES_API"));
            getImporter().setParametre("P_AI_RO", getSession().getLabel("DOCUMENT_RECAP_RENTES_AIRO"));
            getImporter().setParametre("P_AI_REO", getSession().getLabel("DOCUMENT_RECAP_RENTES_AIREO"));
            getImporter().setParametre("P_AI_API", getSession().getLabel("DOCUMENT_RECAP_RENTES_AIAPI"));

            getImporter().setParametre("P_LIBELLE_1", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE1"));
            getImporter().setParametre("P_LIBELLE_2", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE2"));
            getImporter().setParametre("P_LIBELLE_3", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE3"));
            getImporter().setParametre("P_LIBELLE_4", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE4"));
            getImporter().setParametre("P_LIBELLE_5", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE5"));
            getImporter().setParametre("P_LIBELLE_6", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE6"));
            getImporter().setParametre("P_LIBELLE_7", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE7"));
            getImporter().setParametre("P_LIBELLE_8", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE8"));
            getImporter().setParametre("P_LIBELLE_9", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE9"));
            getImporter().setParametre("P_LIBELLE_10", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE10"));
            getImporter().setParametre("P_LIBELLE_11", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE11"));
            getImporter().setParametre("P_LIBELLE_12", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIBELLE12"));
            getImporter().setParametre("P_REMARQUES", getSession().getLabel("DOCUMENT_RECAP_RENTES_REMARQUES"));
            getImporter().setParametre("P_LIEUETDATE", getSession().getLabel("DOCUMENT_RECAP_RENTES_LIEUETDATE"));
            getImporter().setParametre("P_CAISSE", getSession().getLabel("DOCUMENT_RECAP_RENTES_CAISSE"));
            // getImporter().setParametre("P_DROIT",getSession().getLabel("DOCUMENT_RECAP_RENTES_DROIT"));

            getImporter().setParametre("P_ELEM_500001", afficheMnt(reDetRecMenViewBean.getElem500001().getMontant()));
            getImporter().setParametre("P_ELEM_500002", afficheMnt(reDetRecMenViewBean.getElem500002().getMontant()));
            getImporter().setParametre("P_ELEM_500003", afficheMnt(reDetRecMenViewBean.getElem500003().getMontant()));
            getImporter().setParametre("P_ELEM_500004", afficheMnt(reDetRecMenViewBean.getElem500004().getMontant()));
            getImporter().setParametre("P_ELEM_500005", afficheMnt(reDetRecMenViewBean.getElem500005().getMontant()));
            getImporter().setParametre("P_ELEM_500006", afficheMnt(reDetRecMenViewBean.getElem500006().getMontant()));
            getImporter().setParametre("P_ELEM_500007", afficheMnt(reDetRecMenViewBean.getElem500007().getMontant()));
            getImporter().setParametre("P_TOTAL_AVSRO", getSession().getLabel("DOCUMENT_RECAP_RENTES_2123000"));

            getImporter().setParametre("P_ELEM_501001", afficheMnt(reDetRecMenViewBean.getElem501001().getMontant()));
            getImporter().setParametre("P_ELEM_501002", afficheMnt(reDetRecMenViewBean.getElem501002().getMontant()));
            getImporter().setParametre("P_ELEM_501003", afficheMnt(reDetRecMenViewBean.getElem501003().getMontant()));
            getImporter().setParametre("P_ELEM_501004", afficheMnt(reDetRecMenViewBean.getElem501004().getMontant()));
            getImporter().setParametre("P_ELEM_501005", afficheMnt(reDetRecMenViewBean.getElem501005().getMontant()));
            getImporter().setParametre("P_ELEM_501006", afficheMnt(reDetRecMenViewBean.getElem501006().getMontant()));
            getImporter().setParametre("P_ELEM_501007", afficheMnt(reDetRecMenViewBean.getElem501007().getMontant()));
            getImporter().setParametre("P_TOTAL_AVSREO", getSession().getLabel("DOCUMENT_RECAP_RENTES_2123010"));

            getImporter().setParametre("P_ELEM_503001", afficheMnt(reDetRecMenViewBean.getElem503001().getMontant()));
            getImporter().setParametre("P_ELEM_503002", afficheMnt(reDetRecMenViewBean.getElem503002().getMontant()));
            getImporter().setParametre("P_ELEM_503003", afficheMnt(reDetRecMenViewBean.getElem503003().getMontant()));
            getImporter().setParametre("P_ELEM_503004", afficheMnt(reDetRecMenViewBean.getElem503004().getMontant()));
            getImporter().setParametre("P_ELEM_503005", afficheMnt(reDetRecMenViewBean.getElem503005().getMontant()));
            getImporter().setParametre("P_ELEM_503006", afficheMnt(reDetRecMenViewBean.getElem503006().getMontant()));
            getImporter().setParametre("P_ELEM_503007", afficheMnt(reDetRecMenViewBean.getElem503007().getMontant()));
            getImporter().setParametre("P_TOTAL_AVSAPI", getSession().getLabel("DOCUMENT_RECAP_RENTES_2123030"));

            getImporter().setParametre("P_ELEM_510001", afficheMnt(reDetRecMenViewBean.getElem510001().getMontant()));
            getImporter().setParametre("P_ELEM_510002", afficheMnt(reDetRecMenViewBean.getElem510002().getMontant()));
            getImporter().setParametre("P_ELEM_510003", afficheMnt(reDetRecMenViewBean.getElem510003().getMontant()));
            getImporter().setParametre("P_ELEM_510004", afficheMnt(reDetRecMenViewBean.getElem510004().getMontant()));
            getImporter().setParametre("P_ELEM_510005", afficheMnt(reDetRecMenViewBean.getElem510005().getMontant()));
            getImporter().setParametre("P_ELEM_510006", afficheMnt(reDetRecMenViewBean.getElem510006().getMontant()));
            getImporter().setParametre("P_ELEM_510007", afficheMnt(reDetRecMenViewBean.getElem510007().getMontant()));
            getImporter().setParametre("P_TOTAL_AIRO", getSession().getLabel("DOCUMENT_RECAP_RENTES_2133000"));

            getImporter().setParametre("P_ELEM_511001", afficheMnt(reDetRecMenViewBean.getElem511001().getMontant()));
            getImporter().setParametre("P_ELEM_511002", afficheMnt(reDetRecMenViewBean.getElem511002().getMontant()));
            getImporter().setParametre("P_ELEM_511003", afficheMnt(reDetRecMenViewBean.getElem511003().getMontant()));
            getImporter().setParametre("P_ELEM_511004", afficheMnt(reDetRecMenViewBean.getElem511004().getMontant()));
            getImporter().setParametre("P_ELEM_511005", afficheMnt(reDetRecMenViewBean.getElem511005().getMontant()));
            getImporter().setParametre("P_ELEM_511006", afficheMnt(reDetRecMenViewBean.getElem511006().getMontant()));
            getImporter().setParametre("P_ELEM_511007", afficheMnt(reDetRecMenViewBean.getElem511007().getMontant()));
            getImporter().setParametre("P_TOTAL_AIREO", getSession().getLabel("DOCUMENT_RECAP_RENTES_2133010"));

            getImporter().setParametre("P_ELEM_513001", afficheMnt(reDetRecMenViewBean.getElem513001().getMontant()));
            getImporter().setParametre("P_ELEM_513002", afficheMnt(reDetRecMenViewBean.getElem513002().getMontant()));
            getImporter().setParametre("P_ELEM_513003", afficheMnt(reDetRecMenViewBean.getElem513003().getMontant()));
            getImporter().setParametre("P_ELEM_513004", afficheMnt(reDetRecMenViewBean.getElem513004().getMontant()));
            getImporter().setParametre("P_ELEM_513005", afficheMnt(reDetRecMenViewBean.getElem513005().getMontant()));
            getImporter().setParametre("P_ELEM_513006", afficheMnt(reDetRecMenViewBean.getElem513006().getMontant()));
            getImporter().setParametre("P_ELEM_513007", afficheMnt(reDetRecMenViewBean.getElem513007().getMontant()));
            getImporter().setParametre("P_TOTAL_AIAPI", getSession().getLabel("DOCUMENT_RECAP_RENTES_2133030"));

            getImporter().setParametre("P_OP_1", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPPLUS"));
            getImporter().setParametre("P_OP_2", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPEGALE"));
            getImporter().setParametre("P_OP_3", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPMOINS"));
            getImporter().setParametre("P_OP_4", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPEGALE"));
            getImporter().setParametre("P_OP_5", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPPLUS"));
            getImporter().setParametre("P_OP_6", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPPLUS"));
            getImporter().setParametre("P_OP_7", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPEGALE"));
            getImporter().setParametre("P_OP_8", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPMOINS"));
            getImporter().setParametre("P_OP_9", getSession().getLabel("DOCUMENT_RECAP_RENTES_OPEGALE"));

            // On calcul les totaux intermédiaire
            String tot1 = sousTotal4(reDetRecMenViewBean.getElem500001().getMontant(), reDetRecMenViewBean
                    .getElem500002().getMontant(), reDetRecMenViewBean.getElem500003().getMontant());

            getImporter().setParametre("P_TOT_1", afficheMnt(tot1));

            String tot2 = sousTotal4(reDetRecMenViewBean.getElem501001().getMontant(), reDetRecMenViewBean
                    .getElem501002().getMontant(), reDetRecMenViewBean.getElem501003().getMontant());

            getImporter().setParametre("P_TOT_2", afficheMnt(tot2));

            String tot3 = sousTotal4(reDetRecMenViewBean.getElem503001().getMontant(), reDetRecMenViewBean
                    .getElem503002().getMontant(), reDetRecMenViewBean.getElem503003().getMontant());

            getImporter().setParametre("P_TOT_3", afficheMnt(tot3));

            String tot4 = sousTotal4(reDetRecMenViewBean.getElem510001().getMontant(), reDetRecMenViewBean
                    .getElem510002().getMontant(), reDetRecMenViewBean.getElem510003().getMontant());

            getImporter().setParametre("P_TOT_4", afficheMnt(tot4));

            String tot5 = sousTotal4(reDetRecMenViewBean.getElem511001().getMontant(), reDetRecMenViewBean
                    .getElem511002().getMontant(), reDetRecMenViewBean.getElem511003().getMontant());

            getImporter().setParametre("P_TOT_5", afficheMnt(tot5));

            String tot6 = sousTotal4(reDetRecMenViewBean.getElem513001().getMontant(), reDetRecMenViewBean
                    .getElem513002().getMontant(), reDetRecMenViewBean.getElem513003().getMontant());

            getImporter().setParametre("P_TOT_6", afficheMnt(tot6));

            String tot7 = sousTotal6(tot1, reDetRecMenViewBean.getElem500004().getMontant());

            getImporter().setParametre("P_TOT_7", afficheMnt(tot7));

            String tot8 = sousTotal6(tot2, reDetRecMenViewBean.getElem501004().getMontant());

            getImporter().setParametre("P_TOT_8", afficheMnt(tot8));

            String tot9 = sousTotal6(tot3, reDetRecMenViewBean.getElem503004().getMontant());

            getImporter().setParametre("P_TOT_9", afficheMnt(tot9));

            String tot10 = sousTotal6(tot4, reDetRecMenViewBean.getElem510004().getMontant());

            getImporter().setParametre("P_TOT_10", afficheMnt(tot10));

            String tot11 = sousTotal6(tot5, reDetRecMenViewBean.getElem511004().getMontant());

            getImporter().setParametre("P_TOT_11", afficheMnt(tot11));

            String tot12 = sousTotal6(tot6, reDetRecMenViewBean.getElem513004().getMontant());

            getImporter().setParametre("P_TOT_12", afficheMnt(tot12));

            String tot13 = sousTotal4(tot7, reDetRecMenViewBean.getElem500005().getMontant(), reDetRecMenViewBean
                    .getElem500006().getMontant());

            getImporter().setParametre("P_TOT_13", afficheMnt(tot13));

            String tot14 = sousTotal4(tot8, reDetRecMenViewBean.getElem501005().getMontant(), reDetRecMenViewBean
                    .getElem501006().getMontant());

            getImporter().setParametre("P_TOT_14", afficheMnt(tot14));

            String tot15 = sousTotal4(tot9, reDetRecMenViewBean.getElem503005().getMontant(), reDetRecMenViewBean
                    .getElem503006().getMontant());

            getImporter().setParametre("P_TOT_15", afficheMnt(tot15));

            String tot16 = sousTotal4(tot10, reDetRecMenViewBean.getElem510005().getMontant(), reDetRecMenViewBean
                    .getElem510006().getMontant());

            getImporter().setParametre("P_TOT_16", afficheMnt(tot16));

            String tot17 = sousTotal4(tot11, reDetRecMenViewBean.getElem511005().getMontant(), reDetRecMenViewBean
                    .getElem511006().getMontant());

            getImporter().setParametre("P_TOT_17", afficheMnt(tot17));

            String tot18 = sousTotal4(tot12, reDetRecMenViewBean.getElem513005().getMontant(), reDetRecMenViewBean
                    .getElem513006().getMontant());

            getImporter().setParametre("P_TOT_18", afficheMnt(tot18));

            String tot19 = sousTotal6(tot13, reDetRecMenViewBean.getElem500007().getMontant());

            getImporter().setParametre("P_TOT_19", afficheMnt(tot19));

            String tot20 = sousTotal6(tot14, reDetRecMenViewBean.getElem501007().getMontant());

            getImporter().setParametre("P_TOT_20", afficheMnt(tot20));

            String tot21 = sousTotal6(tot15, reDetRecMenViewBean.getElem503007().getMontant());

            getImporter().setParametre("P_TOT_21", afficheMnt(tot21));

            String tot22 = sousTotal6(tot16, reDetRecMenViewBean.getElem510007().getMontant());

            getImporter().setParametre("P_TOT_22", afficheMnt(tot22));

            String tot23 = sousTotal6(tot17, reDetRecMenViewBean.getElem511007().getMontant());

            getImporter().setParametre("P_TOT_23", afficheMnt(tot23));

            String tot24 = sousTotal6(tot18, reDetRecMenViewBean.getElem513007().getMontant());

            getImporter().setParametre("P_TOT_24", afficheMnt(tot24));

            // Ajout de la date et l'heure de la génération du document

            Calendar mainteneant = Calendar.getInstance();
            Integer hh = new Integer(mainteneant.get(Calendar.HOUR_OF_DAY));
            Integer mm = new Integer(mainteneant.get(Calendar.MINUTE));
            Integer ss = new Integer(mainteneant.get(Calendar.SECOND));
            getImporter().setParametre("P_DATE_HEURE",
                    JACalendar.todayJJsMMsAAAA() + " " + hh.toString() + ":" + mm.toString() + ":" + ss.toString());

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("LISTE_ETR_MAIL"));
            throw new FWIException(e);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Effacer les pdf a la fin
        setDeleteOnExit(true);
    }

    @Override
    public void createDataSource() throws Exception {

    }

    public REDetailRecapMensuelleViewBean getReDetRecMenViewBean() {
        return reDetRecMenViewBean;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public boolean next() throws FWIException {
        // Génération d'un unique rapport
        if (hasNext) {
            hasNext = false;
            return true;
        } else {
            return false;
        }
    }

    private String removeCot(String mnt) {

        String mntf = "";
        mntf = mnt.replaceAll("'", "");

        return mntf;

    }

    public void setReDetRecMenViewBean(REDetailRecapMensuelleViewBean reDetRecMenViewBean) {
        this.reDetRecMenViewBean = reDetRecMenViewBean;
    }

    private String sousTotal4(String mnt1, String mnt2, String mnt3) {

        if (JadeStringUtil.isBlankOrZero(mnt1)) {
            mnt1 = "0";
        }
        if (JadeStringUtil.isBlankOrZero(mnt2)) {
            mnt2 = "0";
        }
        if (JadeStringUtil.isBlankOrZero(mnt3)) {
            mnt3 = "0";
        }

        BigDecimal bmnt1 = new BigDecimal(removeCot(mnt1));
        BigDecimal bmnt2 = new BigDecimal(removeCot(mnt2));
        BigDecimal bmnt3 = new BigDecimal(removeCot(mnt3));

        return new FWCurrency(JANumberFormatter.format((bmnt1.add(bmnt2)).add(bmnt3).toString(), 0.05, 2,
                JANumberFormatter.NEAR)).toString();
    }

    private String sousTotal6(String mnt1, String mnt2) {

        if (JadeStringUtil.isBlankOrZero(mnt1)) {
            mnt1 = "0";
        }
        if (JadeStringUtil.isBlankOrZero(mnt2)) {
            mnt2 = "0";
        }

        BigDecimal bmnt1 = new BigDecimal(removeCot(mnt1));
        BigDecimal bmnt2 = new BigDecimal(removeCot(mnt2));

        return new FWCurrency(JANumberFormatter.format(bmnt1.subtract(bmnt2).toString(), 0.05, 2,
                JANumberFormatter.NEAR)).toString();
    }

}
