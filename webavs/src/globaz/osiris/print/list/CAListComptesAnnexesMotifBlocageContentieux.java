/*
 * Créé le 24 mars 05
 */
package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jts 24 mars 05 12:34:07
 */
public class CAListComptesAnnexesMotifBlocageContentieux extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0047GCA";
    private Boolean blocageInactif = new Boolean(false);
    private String forIdGenreCompte = new String();

    private String forSelectionCompte = new String();

    private String forSelectionRole = new String();
    private String forSelectionTri = new String();
    private String idContMotifBloque = new String();
    private BProcess processAppelant = null;
    private FWParametersUserCode ucMotifContentieuxSuspendu = null;
    private String user = new String();

    // créé la feuille xls
    public CAListComptesAnnexesMotifBlocageContentieux(BSession session) {
        super(session, session.getLabel("CAMBC_TITRE"), session.getLabel("CAMBC_TITRE"));
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * @return
     */
    public String getIdContMotifBloque() {
        return idContMotifBloque;
    }

    @Override
    public String getNumeroInforom() {
        return CAListComptesAnnexesMotifBlocageContentieux.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return
     */
    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    /**
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * Initialisation des critères choisis
     */
    private void initCritere() {
        // styles
        HSSFCellStyle styleTitreCritere = getStyleCritereTitle();

        /*
         * Critères
         */
        // Genre
        if (!JadeStringUtil.isBlank(getForIdGenreCompte()) && !getForIdGenreCompte().equals("0")) {
            String value = "";
            try {
                value = CACodeSystem.getLibelle(getSession(), getForIdGenreCompte());
            } catch (Exception e) {
                e.printStackTrace();
            }
            createRow();
            this.createCell(getSession().getLabel("GENRE"), styleTitreCritere);
            this.createCell(value, getStyleCritere());
        }

        // Rôle
        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            StringBuffer ssTitle = new StringBuffer("");
            try {
                if (getForSelectionRole().indexOf(',') != -1) {
                    String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);
                    for (int id = 0; id < roles.length; ++id) {
                        if (id > 0) {
                            ssTitle.append(',');
                        }
                        ssTitle.append(CACodeSystem.getLibelle(getSession(), roles[id]));
                    }
                } else {
                    ssTitle.append(CACodeSystem.getLibelle(getSession(), getForSelectionRole()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            createRow();
            this.createCell(getSession().getLabel("ROLE"), styleTitreCritere);
            this.createCell(ssTitle.toString(), getStyleCritere());
        }

        // motif
        if (!JadeStringUtil.isBlank(getIdContMotifBloque())) {
            String value = "";
            try {
                ucMotifContentieuxSuspendu = new FWParametersUserCode();
                ucMotifContentieuxSuspendu.setSession(getSession());
                ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdContMotifBloque());
                ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
                ucMotifContentieuxSuspendu.retrieve();
                value = ucMotifContentieuxSuspendu.getCodeUtiLib();
            } catch (Exception e) {
                e.printStackTrace();
            }
            createRow();
            this.createCell(getSession().getLabel("MOTIF"), styleTitreCritere);
            this.createCell(value, getStyleCritere());
        }

        // solde
        if (!JadeStringUtil.isBlank(getForSelectionCompte())) {
            String value = "";
            try {
                if (getForSelectionCompte().equals("1")) {
                    value = getSession().getLabel("OUVERT");
                } else if (getForSelectionCompte().equals("2")) {
                    value = getSession().getLabel("COMPTE_SOLDE");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            createRow();
            this.createCell(getSession().getLabel("SOLDE"), styleTitreCritere);
            this.createCell(value, getStyleCritere());
        }
        // avec blocage inactif
        createRow();
        this.createCell(getSession().getLabel("CAMBC_BLOCAGE"), styleTitreCritere);
        this.createCell(isBlocageInactif().booleanValue(), getStyleCritere());
    }

    /**
     * Création de la page ("Liste des décisions d'intérêts")
     * 
     * @return
     */
    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("MOTIF")));
        colTitles.add(new ParamTitle(getSession().getLabel("DATE_DEBUT")));
        colTitles.add(new ParamTitle(getSession().getLabel("DATE_FIN")));
        colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));

        createSheet(getSession().getLabel("LISTE"));
        initCritere();
        // create Title Row
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListComptesAnnexesMotifBlocageContentieux.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_REMARQUE);
        currentSheet.setColumnWidth((short) 2, CAAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) 3, CAAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) 4, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * @return
     */
    public Boolean isBlocageInactif() {
        return blocageInactif;
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(CACompteAnnexeManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CACompteAnnexe compteAnnexe = null;
        int numPremiereLigne = 0;

        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());

        // création de la page
        initListe();
        // récupération du numéro de la première ligne
        numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((compteAnnexe = (CACompteAnnexe) manager.cursorReadNext(statement)) != null) && (!compteAnnexe.isNew())) {
            if (compteAnnexe != null) {
                createRow();
                // Compte annexe
                this.createCell(compteAnnexe.getCARole().getDescription() + " " + compteAnnexe.getIdExterneRole() + " "
                        + compteAnnexe.getDescription(), getStyleListLeft());
                // Motif
                this.createCell(compteAnnexe.getUcMotifContentieuxSuspendu().getCodeUtiLib(), getStyleListCenter());
                // Date début
                this.createCell(compteAnnexe.getContDateDebBloque(), getStyleListCenter());
                // Date début
                this.createCell(compteAnnexe.getContDateFinBloque(), getStyleListCenter());
                // Solde
                this.createCell(JadeStringUtil.parseDouble(compteAnnexe.getSolde(), 0), false);

                processAppelant.incProgressCounter();
            }
        }
        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        // solde total
        createCellFormula("SUM(E" + numPremiereLigne + ":E" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());

        return currentSheet;
    }

    /**
     * @param boolean1
     */
    public void setBlocageInactif(Boolean boolean1) {
        blocageInactif = boolean1;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionCompte(String string) {
        forSelectionCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

    /**
     * @param string
     */
    public void setIdContMotifBloque(String string) {
        idContMotifBloque = string;
    }

    /**
     * @param process
     */
    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    /**
     * @param string
     */
    public void setUser(String string) {
        user = string;
    }
}
