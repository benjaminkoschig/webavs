package globaz.aquila.print.list;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Crée la liste des rentiers et irrécouvrables
 * 
 * @author SEL
 */
public class COListDossiersEtape extends COAbstractListExcel {
    public static final String NUMERO_REFERENCE_INFOROM = "0136GCO";

    private String aNumAffilie = "";
    private Boolean blocageInactif = new Boolean(false);
    private String categorie = "";
    private String csEtape = "";
    private String csSequence = "";
    private String deNumAffilie = "";
    private String forTypeExecution = "";
    private String role = "";
    private String solde = "";
    private String symboleSolde = "";

    // créé la feuille xls
    public COListDossiersEtape(BSession session) {
        super(session, session.getLabel("DOSSIER_ETAPE_TITLE_CONCAT"), session.getLabel("DOSSIER_ETAPE_LIST_TITLE"));
    }

    public String getaNumAffilie() {
        return aNumAffilie;
    }

    public Boolean getBlocageInactif() {
        return blocageInactif;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getCsEtape() {
        return csEtape;
    }

    public String getCsSequence() {
        return csSequence;
    }

    public String getDeNumAffilie() {
        return deNumAffilie;
    }

    public String getForTypeExecution() {
        return forTypeExecution;
    }

    @Override
    public String getNumeroInforom() {
        return COListDossiersEtape.NUMERO_REFERENCE_INFOROM;
    }

    public String getRole() {
        return role;
    }

    public String getSolde() {
        return solde;
    }

    public String getSymboleSolde() {
        return symboleSolde;
    }

    /**
     * Critères
     * 
     * @author: sel Créé le : 20 oct. 06
     * @param sheet
     */
    private void initCritere() {

        if (!JadeStringUtil.isBlank(getRole())) {
            createRow();
            this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
            try {
                if (!JadeStringUtil.contains(getRole(), ",")) {
                    this.createCell(CACodeSystem.getLibelle(getSession(), getRole()), getStyleCritere());
                } else {
                    this.createCell("Tous", getStyleCritere());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JadeStringUtil.isBlank(getCategorie())) {
            createRow();
            this.createCell(getSession().getLabel("AQUILA_CATEGORIE"), getStyleCritereTitle());
            try {
                if (!JadeStringUtil.contains(getCategorie(), ",")) {
                    this.createCell(CACodeSystem.getLibelle(getSession(), getCategorie()), getStyleCritere());
                } else {
                    this.createCell("Tous", getStyleCritere());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JadeStringUtil.isBlank(getCsSequence())) {
            createRow();
            this.createCell(getSession().getLabel("SEQUENCE"), getStyleCritereTitle());
            try {
                this.createCell(CACodeSystem.getLibelle(getSession(), getCsSequence()), getStyleCritere());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JadeStringUtil.isBlank(getCsEtape())) {
            createRow();
            this.createCell(getSession().getLabel("ETAPE"), getStyleCritereTitle());
            try {
                this.createCell(CACodeSystem.getLibelle(getSession(), getCsEtape()), getStyleCritere());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JadeStringUtil.isBlank(getDeNumAffilie())) {
            createRow();
            this.createCell(getSession().getLabel("AQUILA_DEB_NUM_AFF"), getStyleCritereTitle());
            try {
                this.createCell(getDeNumAffilie());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JadeStringUtil.isBlank(getaNumAffilie())) {
            createRow();
            this.createCell(getSession().getLabel("AQUILA_FIN_NUM_AFF"), getStyleCritereTitle());
            try {
                this.createCell(getaNumAffilie());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!JadeStringUtil.isBlank(getSolde())) {
            createRow();
            this.createCell(getSession().getLabel("SOLDE"), getStyleCritereTitle());
            try {
                this.createCell(getSymboleSolde() + getSolde());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JadeStringUtil.isBlank(getForTypeExecution())) {
            createRow();
            this.createCell(getSession().getLabel("AQUILA_TYPE_EXECUTION"), getStyleCritereTitle());
            try {
                if ("EP".equals(getForTypeExecution())) {
                    this.createCell(getSession().getLabel("AQUILA_PROCH_ETAPE_POTENTIELLE"), getStyleCritere());
                }
                if ("EE".equals(getForTypeExecution())) {
                    this.createCell(getSession().getLabel("AQUILA_LAST_ETAPE_EXEC"), getStyleCritere());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Création de la page
     */
    private void initListe() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(getSession().getLabel("ROLE"));
        colTitles.add(getSession().getLabel("AQUILA_NUMERO_COMPTE_ANNEXE"));
        colTitles.add(getSession().getLabel("AQUILA_DESCRIPTION_COMPTE_ANNEXE"));
        colTitles.add(getSession().getLabel("AQUILA_CATEGORIE"));
        colTitles.add(getSession().getLabel("AQUILA_SECTION"));
        colTitles.add(getSession().getLabel("AQUILA_DESCRIPTION_SECTION"));
        colTitles.add(getSession().getLabel("SOLDE"));
        colTitles.add(getSession().getLabel("AQUILA_SEQUENCE"));
        colTitles.add(getSession().getLabel("AQUILA_LAST_ETAPE"));
        colTitles.add(getSession().getLabel("AQUILA_DATE_DECLENCHEMENT"));
        colTitles.add(getSession().getLabel("AQUILA_DATE_EXECUTION"));
        colTitles.add(getSession().getLabel("AQUILA_NUM_POURSUITE"));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COListDossiersEtape.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Role
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // id Externe Role
        currentSheet.setColumnWidth((short) numCol++, (short) 14000); // Compte Annexe
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Catégorie
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Section
        currentSheet.setColumnWidth((short) numCol++, (short) 14000); // description section
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Solde
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // Sequence
        currentSheet.setColumnWidth((short) numCol++, (short) 12000); // Etape
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // Date decl
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date exe
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // N° poursuite
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(COContentieuxManager manager, BTransaction transaction) throws Exception {
        COContentieux contentieux = null;

        // création de la page
        initListe();

        // parcours du manager et remplissage des cell
        Iterator it = manager.iterator();
        while (it.hasNext()) {
            contentieux = (COContentieux) it.next();
            if (contentieux != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                CACompteAnnexe compte = contentieux.getCompteAnnexe();

                if (!getBlocageInactif().booleanValue()) {
                    if (contentieux.suspendreContentieuxEnFonctionDuMotif(JACalendar.todayJJsMMsAAAA(), getCsEtape())) {
                        continue;
                    }
                }

                createRow();
                this.createCell(compte.getRole().getDescription(), getStyleListLeft()); // Role
                this.createCell(compte.getIdExterneRole(), getStyleListLeft()); // id Externe Role
                this.createCell(compte.getDescription(), getStyleListLeft()); // CA
                this.createCell(getSession().getCodeLibelle(compte.getIdCategorie()), getStyleListLeft()); // Catégorie
                this.createCell(contentieux.getNumSection(), getStyleListLeft()); // num Section
                this.createCell(contentieux.getSection().getDescription(getSession().getIdLangueISO()),
                        getStyleListLeft()); // description Section
                this.createCell(JadeStringUtil.parseDouble(contentieux.getSolde(), 0), false); // Solde
                this.createCell(contentieux.getSequence().getLibSequenceLibelle(), getStyleListLeft()); // Sequence
                this.createCell(getSession().getCodeLibelle(contentieux.getLibEtape()), getStyleListLeft()); // Etape
                this.createCell(contentieux.getDateDeclenchement(), getStyleListLeft()); // Date decl
                this.createCell(contentieux.getDateExecution(), getStyleListLeft()); // Date exe
                this.createCell(contentieux.getNumPoursuite(), getStyleListLeft()); // N° poursuite
            }
        }
        return currentSheet;
    }

    public void setaNumAffilie(String aNumAffilie) {
        this.aNumAffilie = aNumAffilie;
    }

    public void setBlocageInactif(Boolean blocageInactif) {
        this.blocageInactif = blocageInactif;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setCsEtape(String csEtape) {
        this.csEtape = csEtape;
    }

    public void setCsSequence(String csSequence) {
        this.csSequence = csSequence;
    }

    public void setDeNumAffilie(String deNumAffilie) {
        this.deNumAffilie = deNumAffilie;
    }

    public void setForTypeExecution(String forTypeExecution) {
        this.forTypeExecution = forTypeExecution;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    public void setSymboleSolde(String symboleSolde) {
        this.symboleSolde = symboleSolde;
    }

}
