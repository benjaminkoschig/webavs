/**
 *
 */
package globaz.osiris.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.api.ICOSequence;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.cumulcotisations.CACumulCotisationsParAnnee;
import globaz.osiris.db.cumulcotisations.CACumulCotisationsParAnneeManager;
import globaz.osiris.db.statofas.CAStatOfasArdManager;
import globaz.osiris.db.statofas.CAStatOfasPoursuiteAquilaManager;
import globaz.osiris.db.statofas.CAStatOfasPoursuiteOsirisCountManager;
import globaz.osiris.db.statofas.CAStatOfasPoursuiteOsirisSumManager;
import globaz.osiris.db.statofas.CAStatOfasSursisPmtManager;
import globaz.osiris.print.list.CAListStatOfas;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SEL
 */
public class CAProcessListStatOfas extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DATE_DEBUT_JANVIER = "01.01.";
    private static final String DATE_FIN_JANVIER = "31.01.";
    public static final String ID_EXTERNE_BEGIN_WITH_2160_4030 = APIRubrique.ID_EXTERNE_BEGIN_WITH_2160_4030.substring(
            0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_2160_4030.substring(5, 9) + ")(.)*";
    private static final String MONTANT = "MONTANT";
    private String forAnnee = "";

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Not used
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        CAListStatOfas liste = new CAListStatOfas(getSession());

        calculStatForCumulCoti(liste);

        calculStatForSursis(liste);

        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            calculStatForPoursuiteAquila(liste);
        } else {
            calculStatForPoursuiteOsiris(liste);
        }

        calculStatForArd(liste);

        liste.setDocumentInfo(createDocumentInfo());
        liste.populateSheetListe();
        this.registerAttachedDocument(liste.getDocumentInfo(), liste.getOutputFile());

        return true;
    }

    @Override
    protected void _validate() throws Exception {
        // validation année
        if (JadeStringUtil.isBlankOrZero(getForAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("STATOFAS_CRITERE_ANNEE_MANQUANT"));
        }
    }

    /**
     * @param liste
     * @throws Exception
     */
    private void calculStatForArd(CAListStatOfas liste) throws Exception {
        CAStatOfasArdManager ardManager = new CAStatOfasArdManager();
        ardManager.setSession(getSession());
        ardManager.setForAnnee(Integer.parseInt(getForAnnee()));

        liste.setArdMontant(ardManager.getSum(CAStatOfasArdManager.ALIAS_MONTANTTOT, getTransaction()));
        liste.setArdNombre(ardManager.getCount());
    }

    /**
     * @param liste
     * @throws Exception
     */
    private void calculStatForCumulCoti(CAListStatOfas liste) throws Exception {
        BigDecimal cotiPers = new BigDecimal("0");
        BigDecimal cotiPar = new BigDecimal("0");
        BigDecimal cotiAC = new BigDecimal("0");

        CACumulCotisationsParAnneeManager manager = new CACumulCotisationsParAnneeManager();
        manager.setSession(getSession());

        manager.setFromIdExterne("2110.4000.0000");
        manager.setToIdExterne("2169.4030.9999");

        manager.setFromDateValeur(JACalendar.format(
                CAProcessListStatOfas.DATE_DEBUT_JANVIER + (Integer.parseInt(getForAnnee()) + 1),
                JACalendar.FORMAT_YYYYMMDD));
        manager.setToDateValeur(JACalendar.format(
                CAProcessListStatOfas.DATE_FIN_JANVIER + (Integer.parseInt(getForAnnee()) + 1),
                JACalendar.FORMAT_YYYYMMDD));
        manager.setForAnneeCotisation(getForAnnee());

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        Iterator<CACumulCotisationsParAnnee> it = manager.iterator();
        while (it.hasNext()) {
            CACumulCotisationsParAnnee coti = it.next();
            String key = coti.getIdExterne().split("[.]")[1];
            BigDecimal sumMontant = new BigDecimal(coti.getSumMontant());

            if (coti.getIdExterne().startsWith("211")) {
                if (key.equals("4000")) {
                    cotiPers = cotiPers.add(sumMontant);
                } else if (key.equals("4010")) {
                    cotiPar = cotiPar.add(sumMontant);
                }
            } else if (coti.getIdExterne().startsWith("216")) {
                Pattern pat = null;
                Matcher mat = null;
                pat = Pattern.compile(CAProcessListStatOfas.ID_EXTERNE_BEGIN_WITH_2160_4030);
                mat = pat.matcher(coti.getIdExterne());
                if (mat.matches()) {
                    cotiAC = cotiAC.add(sumMontant);
                }
            }
        }

        liste.setAnnee("" + getForAnnee());
        liste.setCotiAc(cotiAC);
        liste.setCotiPar(cotiPar);
        liste.setCotiPers(cotiPers);
    }

    /**
     * @param liste
     * @throws Exception
     */
    private void calculStatForPoursuiteAquila(CAListStatOfas liste) throws Exception {
        CAStatOfasPoursuiteAquilaManager poursuiteManager = new CAStatOfasPoursuiteAquilaManager();
        poursuiteManager.setSession(getSession());
        poursuiteManager.setForAnnee(Integer.parseInt(getForAnnee()));

        poursuiteManager.setForCsEtape(ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);
        poursuiteManager.setForIdSequence(ICOSequence.CS_SEQUENCE_AVS);
        liste.setRpAvsMontant(poursuiteManager.getSum(ICOHistoriqueConstante.FNAME_SOLDE, getTransaction()));
        liste.setRpAvsNombre(poursuiteManager.getCount());

        poursuiteManager.setForIdSequence(ICOSequence.CS_SEQUENCE_ARD);
        liste.setRpArdMontant(poursuiteManager.getSum(ICOHistoriqueConstante.FNAME_SOLDE, getTransaction()));
        liste.setRpArdNombre(poursuiteManager.getCount());

        poursuiteManager.setForCsEtape(ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE);
        poursuiteManager.setForIdSequence(ICOSequence.CS_SEQUENCE_AVS);
        liste.setRcAvsMontant(poursuiteManager.getSum(ICOHistoriqueConstante.FNAME_SOLDE, getTransaction()));
        liste.setRcAvsNombre(poursuiteManager.getCount());

        poursuiteManager.setForIdSequence(ICOSequence.CS_SEQUENCE_ARD);
        liste.setRcArdMontant(poursuiteManager.getSum(ICOHistoriqueConstante.FNAME_SOLDE, getTransaction()));
        liste.setRcArdNombre(poursuiteManager.getCount());
    }

    /**
     * @param liste
     * @throws Exception
     */
    private void calculStatForPoursuiteOsiris(CAListStatOfas liste) throws Exception {
        CAStatOfasPoursuiteOsirisCountManager poursuiteManagerCount = new CAStatOfasPoursuiteOsirisCountManager();
        CAStatOfasPoursuiteOsirisSumManager poursuiteManagerSum = new CAStatOfasPoursuiteOsirisSumManager();
        poursuiteManagerCount.setSession(getSession());
        poursuiteManagerCount.setForAnnee(Integer.parseInt(getForAnnee()));
        poursuiteManagerSum.setSession(getSession());
        poursuiteManagerSum.setForAnnee(Integer.parseInt(getForAnnee()));

        poursuiteManagerSum.setForCsTypeEtape(APIEtape.POURSUITE);
        poursuiteManagerCount.setForCsTypeEtape(APIEtape.POURSUITE);
        liste.setRpAvsMontant(poursuiteManagerSum.getSum(CAProcessListStatOfas.MONTANT, getTransaction()));
        liste.setRpAvsNombre(poursuiteManagerCount.getCount());

        poursuiteManagerSum.setForCsTypeEtape(APIEtape.CONTINUER);
        poursuiteManagerCount.setForCsTypeEtape(APIEtape.CONTINUER);
        liste.setRcAvsMontant(poursuiteManagerSum.getSum(CAProcessListStatOfas.MONTANT, getTransaction()));
        liste.setRcAvsNombre(poursuiteManagerCount.getCount());
    }

    /**
     * @param liste
     * @throws Exception
     */
    private void calculStatForSursis(CAListStatOfas liste) throws Exception {
        CAStatOfasSursisPmtManager sursisManager = new CAStatOfasSursisPmtManager();
        sursisManager.setSession(getSession());
        sursisManager.setForAnnee(Integer.parseInt(getForAnnee()));
        liste.setSursisNombre(sursisManager.getCount(getTransaction()));
        sursisManager.setForSum(true);
        liste.setSursisMontant(sursisManager.getSum(CAEcheancePlan.FIELD_MONTANT, getTransaction()));
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("STATOFAS_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("STATOFAS_SUJETMAIL_OK");
        }
    }

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }
}
