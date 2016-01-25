/*
 * Créé le 24 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jts 24 mars 05 12:34:07
 */
public class CAListComptesAnnexesMotifBlocageContentieuxAquila extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0183GCA";
    private Boolean blocageInactif = new Boolean(false);
    private String forIdGenreCompte = new String();

    private String forSelectionCompte = new String();

    private String forSelectionRole = new String();
    private String forSelectionTri = new String();
    private String idContMotifBloque = new String();
    private CAMotifContentieux motifContentieux = null;
    private BProcess processAppelant = null;
    private FWParametersUserCode ucMotifContentieuxSuspendu = null;
    private String user = new String();

    // créé la feuille xls
    public CAListComptesAnnexesMotifBlocageContentieuxAquila(BSession session) {
        super(session, session.getLabel("CAMBC_TITRE"), session.getLabel("CAMBC_TITRE"));
    }

    /**
     * @return
     */
    public Boolean getBlocageInactif() {
        return blocageInactif;
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
        return CAListComptesAnnexesMotifBlocageContentieuxAquila.NUMERO_REFERENCE_INFOROM;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public String getUser() {
        return user;
    }

    /**
     * méthode pour la création du style de la feuille pour l'onglet "Liste des décisions d'intérêts" (entêtes, des
     * bordures...)
     */
    private void initCritere() {
        /*
         * Critères
         */
        // Genre
        if (!JadeStringUtil.isBlank(getForIdGenreCompte()) && !getForIdGenreCompte().equals("0")) {
            createRow();
            this.createCell(getSession().getLabel("GENRE"), getStyleCritereTitle());
            try {
                this.createCell(CACodeSystem.getLibelle(getSession(), getForIdGenreCompte()), getStyleCritere());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Rôle
        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            createRow();
            this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
            try {
                StringBuffer ssTitle = new StringBuffer();
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
                this.createCell(ssTitle.toString(), getStyleCritere());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // motif
        if (!JadeStringUtil.isBlank(getIdContMotifBloque())) {
            createRow();
            this.createCell(getSession().getLabel("MOTIF"), getStyleCritereTitle());
            try {
                ucMotifContentieuxSuspendu = new FWParametersUserCode();
                ucMotifContentieuxSuspendu.setSession(getSession());
                ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdContMotifBloque());
                ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
                ucMotifContentieuxSuspendu.retrieve();
                this.createCell(ucMotifContentieuxSuspendu.getCodeUtiLib(), getStyleCritere());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // solde
        if (!JadeStringUtil.isBlank(getForSelectionCompte())) {
            createRow();
            this.createCell(getSession().getLabel("SOLDE"), getStyleCritereTitle());
            try {
                if (getForSelectionCompte().equals("1")) {
                    this.createCell(getSession().getLabel("OUVERT"), getStyleCritere());
                } else if (getForSelectionCompte().equals("2")) {
                    this.createCell(getSession().getLabel("COMPTE_SOLDE"), getStyleCritere());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // avec blocage inactif
        createRow();
        this.createCell(getSession().getLabel("CAMBC_BLOCAGE"), getStyleCritereTitle());
        this.createCell(getBlocageInactif().booleanValue(), getStyleCritere());
        // create Title Row
        createRow();
        createRow();
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
        colTitles.add(new ParamTitle(getSession().getLabel("COMMENTAIRE")));

        createSheet(getSession().getLabel("LISTE"));
        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListComptesAnnexesMotifBlocageContentieuxAquila.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_REMARQUE);
        currentSheet.setColumnWidth((short) 2, CAAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) 3, CAAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) 4, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) 5, CAAbstractListExcel.COLUMN_WIDTH_REMARQUE);
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

                CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
                motifMgr.setSession(getSession());
                motifMgr.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
                motifMgr.setForIdMotifBlocage(getIdContMotifBloque());
                motifMgr.changeManagerSize(0);
                if (!getBlocageInactif().booleanValue()) {
                    motifMgr.setFromDateBetweenDebutFin(JACalendar.todayJJsMMsAAAA());
                }
                motifMgr.find(transaction);
                if (!motifMgr.isEmpty()) {
                    for (int i = 0; i < motifMgr.size(); i++) {
                        motifContentieux = (CAMotifContentieux) motifMgr.getEntity(i);
                        createRow();
                        // Compte annexe
                        this.createCell(
                                compteAnnexe.getCARole().getDescription() + " " + compteAnnexe.getIdExterneRole() + " "
                                        + compteAnnexe.getDescription(), getStyleListLeft());
                        // Motif
                        this.createCell(motifContentieux.getUcMotifContentieuxSuspendu().getCodeUtiLib(),
                                getStyleListLeft());
                        // Date début
                        this.createCell(motifContentieux.getDateDebut(), getStyleListCenter());
                        // Date début
                        this.createCell(motifContentieux.getDateFin(), getStyleListCenter());
                        if (i == motifMgr.size() - 1) {
                            // Solde
                            this.createCell(JadeStringUtil.parseDouble(compteAnnexe.getSolde(), 0), false);
                        } else {
                            this.createCell(0, false);
                        }
                        // Commentaire
                        this.createCell(motifContentieux.getCommentaire(), getStyleListLeft());
                    }
                }
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
        this.createCell("", getStyleListTitleLeft());
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

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setUser(String string) {
        user = string;
    }
}
