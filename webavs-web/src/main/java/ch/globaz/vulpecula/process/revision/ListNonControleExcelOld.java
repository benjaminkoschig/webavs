package ch.globaz.vulpecula.process.revision;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.jade.noteIt.NoteException;
import ch.globaz.jade.noteIt.SimpleNote;
import ch.globaz.jade.noteIt.SimpleNoteSearch;
import ch.globaz.jade.noteIt.business.service.JadeNoteService;
import ch.globaz.jade.noteIt.businessimpl.service.JadeNoteServiceImpl;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.util.I18NUtil;

public class ListNonControleExcelOld extends AbstractListExcel {
    private static final String SHEET_TITLE = "Non Controle";

    private List<Employeur> employeurs;

    private Date dateDebut;

    public ListNonControleExcelOld(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) 0, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 1, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) 2, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) 3, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) 4, AbstractListExcel.COLUMN_WIDTH_ADRESS);
        sheet.setColumnWidth((short) 5, AbstractListExcel.COLUMN_WIDTH_ADRESS);

        sheet.setColumnWidth((short) 6, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 7, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 8, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 9, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 10, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 11, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) 12, AbstractListExcel.COLUMN_WIDTH_AFILIE);

        sheet.setColumnWidth((short) 13, AbstractListExcel.COLUMN_WIDTH_2000);
        sheet.setColumnWidth((short) 14, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 15, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) 16, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 17, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) 18, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 19, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) 20, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);

        sheet.setColumnWidth((short) 21, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 22, AbstractListExcel.COLUMN_WIDTH_3500);
        // Cotisations
        sheet.setColumnWidth((short) 23, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 24, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 25, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 26, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 27, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 28, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 29, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 30, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 31, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 32, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 33, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 34, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 35, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 36, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 37, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 38, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 39, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 39, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 40, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 41, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 42, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 43, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 44, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 45, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 46, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 47, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 48, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 49, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 50, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 51, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 52, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 53, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 54, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 55, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 56, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 57, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 58, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 59, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 60, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 61, AbstractListExcel.COLUMN_WIDTH_3500);
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createCell(getLabel("JSP_PERIODE"), getStyleCritereTitle());
        createCell(dateDebut.getSwissValue(), getStyleCritere());
        createRow();
        createRow(2);
        createTable();
    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_NUM_AFFILIE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_ENTREPRISE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DATE_DEBUT_AFFILIATION"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DATE_FIN_AFFILIATION"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_ADRESSE_COURRIER"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_ADRESSE_DOMICILE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_TEL_PRIVE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_TEL_PROFESSIONNEL"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_TEL_PORTABLE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FAX"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_EMAIL"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_CONTACT"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SITE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_CONVENTION_NUM"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_CONVENTION"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DERNIER_CONTROLE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_NUMERO_RAPPORT"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_MONTANT"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_TYPE_CONTROLE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_VISA_REVISEUR"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_REMARQUE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_NOMBRE_TRAVAILLEUR"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_COTISATIONS"), getStyleListGris25PourcentGras());

        for (Employeur employeur : employeurs) {
            createRow();
            createCell(employeur.getAffilieNumero(), getStyleListLeft());
            createCell(employeur.getRaisonSociale(), getStyleListLeft());
            createCell(employeur.getDateDebut(), getStyleListLeft());
            if (employeur.getDateFin() == null) {
                createCell("-", getStyleListLeft());
            } else {
                createEmptyCell();
            }

            Adresse adresseCourrier = VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdresseCourrierByIdTiersForCT(employeur.getIdTiers());
            if (adresseCourrier != null) {
                createCell(adresseCourrier.getAdresseFormatte(), getStyleListLeft());
            } else {
                createEmptyCell();
            }

            Adresse adresseDomicile = VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdresseDomicileByIdTiersForCT(employeur.getIdTiers());
            if (adresseDomicile != null) {
                createCell(adresseDomicile.getAdresseFormatte(), getStyleListLeft());
            } else {
                createEmptyCell();
            }

            // Contacts
            HashMap<TypeContact, MoyenContact> contacts = VulpeculaRepositoryLocator.getContactRepository()
                    .findMoyenContactForIdTiers(employeur.getIdTiers());

            // tel. privé
            if (contacts.get(TypeContact.PRIVE) != null) {
                MoyenContact moyen = contacts.get(TypeContact.PRIVE);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // tel. professionnel
            if (contacts.get(TypeContact.PROFESSIONNEL) != null) {
                MoyenContact moyen = contacts.get(TypeContact.PROFESSIONNEL);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // tel. portable
            if (contacts.get(TypeContact.PORTABLE) != null) {
                MoyenContact moyen = contacts.get(TypeContact.PORTABLE);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // fax
            if (contacts.get(TypeContact.FAX) != null) {
                MoyenContact moyen = contacts.get(TypeContact.FAX);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // email
            if (contacts.get(TypeContact.EMAIL) != null) {
                MoyenContact moyen = contacts.get(TypeContact.EMAIL);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // contact
            if (contacts.get(TypeContact.CONTACT) != null) {
                MoyenContact moyen = contacts.get(TypeContact.CONTACT);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // site web
            if (contacts.get(TypeContact.SITE) != null) {
                MoyenContact moyen = contacts.get(TypeContact.SITE);
                createCell(moyen.getValeur(), getStyleListLeft());
            } else {
                createEmptyCell();
            }

            // convention n° et nom
            createCell(employeur.getConvention().getCode(), getStyleListLeft());
            createCell(employeur.getConvention().getDesignation(), getStyleListLeft());
            // Date du dernier contrôle
            ControleEmployeur dernierControle = VulpeculaRepositoryLocator.getControleEmployeurRepository()
                    .findDernierControleByIdEmployeur(employeur.getId());
            if (dernierControle != null) {
                createCell(dernierControle.getDateAuAsSwissValue(), getStyleListLeft());
                createCell(dernierControle.getNumeroMeroba(), getStyleListLeft());
                createCell(dernierControle.getMontant(), getStyleMontant());
                createCell(getSession().getCodeLibelle(dernierControle.getType().getValue()), getStyleListLeft());
                createCell(dernierControle.getIdUtilisateur(), getStyleListLeft());
                // post it
                try {
                    SimpleNoteSearch search = new SimpleNoteSearch();
                    search.setForTableSource("PT_CONTROLES_EMPLOYEURS");
                    search.setForSourceId(dernierControle.getId());
                    JadeNoteService noteService = new JadeNoteServiceImpl();
                    List<SimpleNote> notes = noteService.search(search);
                    String description = "";
                    for (SimpleNote note : notes) {
                        description = description + " " + note.description + " : " + note.memo + "\n";
                    }
                    if (!JadeStringUtil.isEmpty(description)) {
                        createCell(description, getStyleListLeft());
                    } else {
                        createEmptyCell();
                    }
                } catch (NoteException e) {
                    createEmptyCell();
                } catch (JadePersistenceException e) {
                    createEmptyCell();
                }
            } else {
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
            }
            // Nombre travailleur le plus grand sur les 12 derniers mois
            if (dernierControle != null) {
                int nombreTravailleur = VulpeculaServiceLocator.getPosteTravailService()
                        .findPostesActifsByIdAffiliePourControle(employeur.getId(), dernierControle.getDateControle());
                createCell(nombreTravailleur, getStyleListLeft());
            } else {
                createEmptyCell();
            }
            // Cotisations
            // Assurance
            List<Cotisation> cotis = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(
                    employeur.getId(), dateDebut);
            TreeMap<String, Cotisation> mapCotisations = new TreeMap<String, Cotisation>();
            for (Cotisation cotisation : cotis) {
                // On trie les cotisations par type
                if (cotisation.getTypeAssurance().getValue().startsWith("6")) {
                    mapCotisations.put("999" + cotisation.getTypeAssurance().getValue() + "-" + cotisation.getId(),
                            cotisation);
                } else {
                    mapCotisations.put(cotisation.getTypeAssurance().getValue() + "-" + cotisation.getId(), cotisation);
                }
            }

            for (Map.Entry<String, Cotisation> entry : mapCotisations.entrySet()) {
                Cotisation cotisation = entry.getValue();
                createCell(cotisation.getDateDebut().getSwissValue(), getStyleListLeft());
                if (cotisation.getDateFin() != null) {
                    createCell(cotisation.getDateFin().getSwissValue(), getStyleListLeft());
                } else {
                    createEmptyCell();
                }
                createCell(cotisation.getAssuranceLibelle(I18NUtil.getUserLocale()), getStyleListLeft());
            }
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_NON_CONTROLE_TYPE_NUMBER;
    }

    public List<Employeur> getEmployeurs() {
        return employeurs;
    }

    public void setEmployeurs(List<Employeur> employeurs) {
        this.employeurs = employeurs;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
}
