package globaz.osiris.print.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.interet.analytique.CAInteretAnalytique;
import globaz.osiris.db.interet.analytique.CAInteretAnalytiqueManager;
import globaz.pyxis.api.osiris.TITiersAdministrationOSI;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class CAListInteretAnalytique extends CAAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0223GCA";

    private static final String ZERO = "0.00";

    private CACompteAnnexe compteAnnexe = new CACompteAnnexe();

    private String forDateDebut;
    private String forDateFin;
    private BProcess process = null;

    private CARubrique rubrique = new CARubrique();
    private CASection section = new CASection();

    // créé la feuille xls
    public CAListInteretAnalytique(BSession session, BProcess process) {
        super(session, "liste_analytiques_interets", session.getLabel("IM_ANALYTIQUE_TITLE"));
        this.process = process;
    }

    /**
     * Ajoute les céllules informatives.
     * 
     * @param interetAnalytique
     */
    private void createCellsInformations(CAInteretAnalytique interetAnalytique) {
        this.createCell(
                compteAnnexe.getRole().getDescription(getSession().getIdLangueISO()) + " "
                        + compteAnnexe.getIdExterneRole(), getStyleListVerticalAlignTopLeft());
        this.createCell(section.getIdExterne() + " - " + section.getDescription(), getStyleListVerticalAlignTopLeft());
        this.createCell(rubrique.getIdExterne() + " - " + rubrique.getDescription(), getStyleListVerticalAlignTopLeft());
        this.createCell(getCaisseProfessionnelleLibelle(interetAnalytique.getIdCaisseProfessionnelle()),
                getStyleListLeft());
        this.createCell(JadeStringUtil.parseDouble(interetAnalytique.getMontant(), 0), getStyleMontant());
        this.createCell(JadeStringUtil.parseDouble(interetAnalytique.getMontantInteret(), 0), getStyleMontant());
    }

    /**
     * Return le libellé de la caisse prof..
     * 
     * @param idCaisseProf
     * @return
     */
    public String getCaisseProfessionnelleLibelle(String idCaisseProf) {
        if (!JadeStringUtil.isIntegerEmpty(idCaisseProf)) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationCodeEtLibelle(pyxisSession, idCaisseProf);
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * Return le montant proratisé arrondi à 5ct.
     * 
     * @param interetAnalytique
     * @param tmp
     * @return
     */
    private BigDecimal getMontantAuProrataArrondi5Ct(CAInteretAnalytique interetAnalytique, BigDecimal tmp) {
        tmp = tmp.multiply(new BigDecimal(interetAnalytique.getMontantInteret()));
        FWCurrency tmp2 = new FWCurrency(tmp.toString());
        tmp2.round(FWCurrency.ROUND_5CT);
        tmp = tmp2.getBigDecimalValue();

        return tmp;
    }

    /**
     * Return la prochaine entité.
     * 
     * @param manager
     * @param i
     * @return
     */
    private CAInteretAnalytique getNextEntity(CAInteretAnalytiqueManager manager, int i) {
        CAInteretAnalytique next = null;
        if (i < manager.size() - 1) {
            next = (CAInteretAnalytique) manager.get(i + 1);
        }
        return next;
    }

    @Override
    public String getNumeroInforom() {
        return CAListInteretAnalytique.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the process
     */
    public BProcess getProcess() {
        return process;
    }

    /**
     * Return le prorata du montant de l'écriture par rapport au montant soumis.
     * 
     * @param montantSoumisMap
     * @param interetAnalytique
     * @return
     */
    private BigDecimal getProRata(HashMap montantSoumisMap, CAInteretAnalytique interetAnalytique) {
        BigDecimal montantOperation = new BigDecimal(interetAnalytique.getMontant());
        BigDecimal montantSoumis = new BigDecimal((String) montantSoumisMap.get(interetAnalytique.getIdSection()));
        if ((montantOperation.signum() != 0) && (montantSoumis.signum() != 0)) {
            montantOperation = montantOperation.divide(
                    new BigDecimal((String) montantSoumisMap.get(interetAnalytique.getIdSection())), 5,
                    BigDecimal.ROUND_HALF_EVEN);
        }
        return montantOperation;
    }

    /**
     * Criteres de lancement
     */
    private void initCritere() {
        createRow();
        this.createCell(getSession().getLabel("IM_ANALYTIQUE_DES") + " : ", getStyleCritereTitle());
        this.createCell(getForDateDebut(), getStyleCritere());
        createRow();
        this.createCell(getSession().getLabel("IM_ANALYTIQUE_A") + " : ", getStyleCritereTitle());
        this.createCell(getForDateFin(), getStyleCritere());
    }

    /**
     * Création de la page
     * 
     * @return
     */
    public void initListe() {
        ArrayList<ParamTitle> colTitles = new ArrayList<ParamTitle>();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("SECTION"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("RUBRIQUE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("CAISSE_PROF"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("MONTANT"), getStyleListTitleCenter()));
        colTitles.add(new ParamTitle(getSession().getLabel("IM_MONTANT"), getStyleListTitleCenter()));
        colTitles.add(new ParamTitle(getSession().getLabel("PRORATA"), getStyleListTitleCenter()));
        colTitles.add(new ParamTitle(getSession().getLabel("MONTANT_CALCULE"), getStyleListTitleCenter()));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListInteretAnalytique.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 6000);
        currentSheet.setColumnWidth((short) numCol++, (short) 6000);
        currentSheet.setColumnWidth((short) numCol++, (short) 6000);
        currentSheet.setColumnWidth((short) numCol++, (short) 6000);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) numCol++, (short) 3500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * Créé le document excel en parcourant le manager.
     * 
     * @param manager
     * @param transaction
     * @param montantSoumis
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(CAInteretAnalytiqueManager manager, HashMap montantSoumis,
            BTransaction transaction) throws Exception {
        BigDecimal sumAttribution = new BigDecimal(CAListInteretAnalytique.ZERO);
        for (int i = 0; (i < manager.size()) && !getProcess().isAborted(); i++) {
            CAInteretAnalytique interetAnalytique = (CAInteretAnalytique) manager.get(i);

            retrieveSection(transaction, interetAnalytique.getIdSection());
            retrieveCompteAnnexe(transaction, section.getIdCompteAnnexe());
            retrieveRubrique(transaction, interetAnalytique.getIdRubrique());

            createRow();

            createCellsInformations(interetAnalytique);

            if (montantSoumis.containsKey(interetAnalytique.getIdSection())) {
                BigDecimal tmp = getProRata(montantSoumis, interetAnalytique);

                this.createCell(JadeStringUtil.parseDouble(tmp.toString(), 0), getStylePourcent());

                tmp = getMontantAuProrataArrondi5Ct(interetAnalytique, tmp);

                sumAttribution = sumAttribution.add(tmp);

                CAInteretAnalytique next = getNextEntity(manager, i);

                // Gère les derniers centimes non attibués. Si le programme
                // trouve qqch, il l'attribuera à la dernière ligne de la
                // section.
                if (((next != null) && (!next.getIdSection().equals(interetAnalytique.getIdSection())))
                        || (i == manager.size() - 1)) {
                    BigDecimal tmpMontantInteret = new BigDecimal(interetAnalytique.getMontantInteret());
                    if (sumAttribution.compareTo(tmpMontantInteret) != 0) {
                        tmpMontantInteret = tmpMontantInteret.subtract(sumAttribution);
                        tmp = tmp.add(tmpMontantInteret);
                    }

                    sumAttribution = new BigDecimal(CAListInteretAnalytique.ZERO);
                }

                this.createCell(JadeStringUtil.parseDouble(tmp.toString(), 0), getStyleMontant());
            } else {
                this.createCell("", getStyleListLeft());
                this.createCell("", getStyleListLeft());
            }

            getProcess().incProgressCounter();
        }

        return currentSheet;
    }

    /**
     * Return le compte annexe
     * 
     * @param transaction
     * @param idCompteAnnexe
     * @throws Exception
     */
    private void retrieveCompteAnnexe(BTransaction transaction, String idCompteAnnexe) throws Exception {
        if (!idCompteAnnexe.equals(compteAnnexe.getIdCompteAnnexe())) {
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setISession(getSession());
            compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);

            compteAnnexe.retrieve();

            if (compteAnnexe.hasErrors()) {
                throw new Exception(compteAnnexe.getErrors().toString());
            }

            if (compteAnnexe.isNew()) {
                throw new Exception(getSession().getLabel("5106"));
            }
        }
    }

    /**
     * Return la rubrique.
     * 
     * @param transaction
     * @param idCompteAnnexe
     * @throws Exception
     */
    private void retrieveRubrique(BTransaction transaction, String idRubrique) throws Exception {
        if (!idRubrique.equals(rubrique.getIdRubrique())) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(idRubrique);

            rubrique.retrieve();

            if (rubrique.hasErrors()) {
                throw new Exception(rubrique.getErrors().toString());
            }

            if (rubrique.isNew()) {
                throw new Exception(getSession().getLabel("5115"));
            }
        }
    }

    /**
     * Return la section.
     * 
     * @param transaction
     * @param idSection
     * @throws Exception
     */
    private void retrieveSection(BTransaction transaction, String idSection) throws Exception {
        if (!idSection.equals(section.getIdSection())) {
            section = new CASection();
            section.setSession(getSession());

            section.setIdSection(idSection);

            section.retrieve(transaction);

            if (section.hasErrors()) {
                throw new Exception(section.getErrors().toString());
            }

            if (section.isNew()) {
                throw new Exception(getSession().getLabel("5125"));
            }
        }
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param process
     *            the process to set
     */
    public void setProcess(BProcess process) {
        this.process = process;
    }
}
