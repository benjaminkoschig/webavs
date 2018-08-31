package ch.globaz.vulpecula.process.revision;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.util.I18NUtil;

public class ListNonControleExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "Non Controle";

    private List<Employeur> employeurs;
    private ListNonControleExcelProcess process;
    private int progress = 0;

    private Date dateDebut;

    public ListNonControleExcel(BSession session, String filenameRoot, String documentTitle,
            ListNonControleExcelProcess listNonControleExcelProcess) {
        super(session, filenameRoot, documentTitle);
        createSheet(SHEET_TITLE);
        process = listNonControleExcelProcess;
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createTable();
    }

    private void createTable() {
        createCell("UWNUEM");
        createCell("UWLLIB");
        createCell("UWNOEM");
        createCell("UWADR1");
        createCell("UWADR2");
        createCell("UWADR3");
        createCell("UWADR4");
        createCell("UWNPA");
        createCell("UWLOC");
        createCell("UWTEL");
        createCell("UWFAX");
        createCell("UWNAT");
        createCell("UWEML");
        createCell("UWNCNV");
        createCell("UWLCNV");
        createCell("UWDDAC");
        createCell("UWNOME");
        createCell("UWDDCT");
        createCell("UWDCTL");
        createCell("UWMTCT");
        createCell("UWDIAG");
        createCell("UWUSER");
        createCell("UWDDSP");
        createCell("UWDFSP");
        createCell("UWCAVS");
        createCell("UWDDAV");
        createCell("UWDFAV");
        createCell("UWCAAF");
        createCell("UWDDAF");
        createCell("UWDFAF");
        createCell("UWNPOS");

        for (Employeur employeur : employeurs) {
            process.setProgressScaleValue(progress++);
            createRow();
            // num aff
            createCell(employeur.getAffilieNumero());
            // titre
            createCell(getSession().getCodeLibelle(employeur.getTitreTiers()));
            // Raison sociale
            createCell(employeur.getRaisonSociale());
            // Adresse courrier puis domicile si inexistante
            Adresse adresseCourrier = VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdresseCourrierByIdTiersForCT(employeur.getIdTiers());

            Adresse adresseDomicile = VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdresseDomicileByIdTiersForCT(employeur.getIdTiers());

            if (adresseCourrier != null) {
                // ligne adresse 1-4
                createCell(adresseCourrier.getDescription3());
                createCell(adresseCourrier.getDescription4());
                createCell(adresseCourrier.getRue() + " " + adresseCourrier.getRueNumero());
                createEmptyCell();
                // NPA
                createCell(adresseCourrier.getNpa());
                // Localité
                createCell(adresseCourrier.getLocalite());
            } else if (adresseDomicile != null) {
                // ligne adresse 1-4
                createCell(adresseDomicile.getDescription3());
                createCell(adresseDomicile.getDescription4());
                createCell(adresseDomicile.getRue() + " " + adresseDomicile.getRueNumero());
                createEmptyCell();
                // NPA
                createCell(adresseDomicile.getNpa());
                // Localité
                createCell(adresseDomicile.getLocalite());
            } else {
                // ligne adresse 1-4
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                // NPA
                createEmptyCell();
                // Localité
                createEmptyCell();
            }

            // Contacts
            HashMap<TypeContact, MoyenContact> contacts = VulpeculaRepositoryLocator.getContactRepository()
                    .findMoyenContactForIdTiers(employeur.getIdTiers());

            // tel. privé
            if (contacts.get(TypeContact.PRIVE) != null) {
                MoyenContact moyen = contacts.get(TypeContact.PRIVE);
                createCell(moyen.getValeur());
            } else
            // tel. professionnel
            if (contacts.get(TypeContact.PROFESSIONNEL) != null) {
                MoyenContact moyen = contacts.get(TypeContact.PROFESSIONNEL);
                createCell(moyen.getValeur());
            } else {
                createEmptyCell();
            }

            // fax
            if (contacts.get(TypeContact.FAX) != null) {
                MoyenContact moyen = contacts.get(TypeContact.FAX);
                createCell(moyen.getValeur());
            } else {
                createEmptyCell();
            }

            // tel. portable
            if (contacts.get(TypeContact.PORTABLE) != null) {
                MoyenContact moyen = contacts.get(TypeContact.PORTABLE);
                createCell(moyen.getValeur());
            } else {
                createEmptyCell();
            }

            // email
            if (contacts.get(TypeContact.EMAIL) != null) {
                MoyenContact moyen = contacts.get(TypeContact.EMAIL);
                createCell(moyen.getValeur());
            } else {
                createEmptyCell();
            }

            // convention n° et nom
            createCell(employeur.getConvention().getCode());
            createCell(employeur.getConvention().getDesignation());

            // Date de début d'activité
            createCell(JadeStringUtil.removeChar(employeur.getDateDebut(), '.'));

            // Date du dernier contrôle
            ControleEmployeur dernierControle = VulpeculaRepositoryLocator.getControleEmployeurRepository()
                    .findDernierControleByIdEmployeur(employeur.getId());

            if (dernierControle != null) {
                createCell(dernierControle.getNumeroMeroba());
                if (dernierControle.getDateAu() != null) {
                    createCell(JadeStringUtil.removeChar(dernierControle.getDateAu().toString(), '.'));
                }
                if (dernierControle.getDateControle() != null) {
                    createCell(JadeStringUtil.removeChar(dernierControle.getDateControle().toString(), '.'));
                }
                createCell(dernierControle.getMontant());

                if (dernierControle.isAutresMesures()) {
                    createCell("AM");
                } else {
                    createEmptyCell();
                }

                createCell(dernierControle.getIdUtilisateur());

            } else {
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
            }

            Particularite particularite = VulpeculaServiceLocator.getEmployeurService().findDerniereParticularite(
                    employeur.getId());
            if (particularite != null) {
                if (particularite.getDateDebut() != null) {
                    createCell(JadeStringUtil.removeChar(particularite.getDateDebut().toString(), '.'));
                } else {
                    createEmptyCell();
                }

                if (particularite.getDateFin() != null) {
                    createCell(JadeStringUtil.removeChar(particularite.getDateFin().toString(), '.'));
                } else {
                    createEmptyCell();
                }

            } else {
                createEmptyCell();
                createEmptyCell();
            }

            // Cotisations
            // Assurance
            List<Cotisation> cotis = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(
                    employeur.getId(), dateDebut);

            Cotisation cotisationAVS = null;
            for (Cotisation cotisation : cotis) {
                // On trie les cotisations par type
                if (cotisation.getTypeAssurance().getValue().equals(TypeAssurance.COTISATION_AVS_AI.getValue())) {
                    if (cotisationAVS == null || cotisationAVS.getDateDebut().before(cotisation.getDateDebut())) {
                        cotisationAVS = cotisation;
                    }
                }
            }
            if (cotisationAVS != null) {
                createCell(cotisationAVS.getPlanCaisseLibelle());
                if (cotisationAVS.getDateDebut() != null) {
                    createCell(JadeStringUtil.removeChar(cotisationAVS.getDateDebut().toString(), '.'));
                } else {
                    createEmptyCell();
                }
                if (cotisationAVS.getDateFin() != null) {
                    createCell(JadeStringUtil.removeChar(cotisationAVS.getDateFin().toString(), '.'));
                } else {
                    createEmptyCell();
                }
            } else {
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
            }

            Cotisation cotisationAF = null;
            for (Cotisation cotisation : cotis) {
                // On trie les cotisations par type
                if (cotisation.getTypeAssurance().getValue().equals(TypeAssurance.COTISATION_AF.getValue())) {
                    if (cotisationAF == null || cotisationAF.getDateDebut().before(cotisation.getDateDebut())) {
                        cotisationAF = cotisation;
                    }
                }
            }
            if (cotisationAF != null) {
                createCell(cotisationAF.getAssuranceLibelle(I18NUtil.getUserLocale()));
                if (cotisationAF.getDateDebut() != null) {
                    createCell(JadeStringUtil.removeChar(cotisationAF.getDateDebut().toString(), '.'));
                } else {
                    createEmptyCell();
                }
                if (cotisationAF.getDateFin() != null) {
                    createCell(JadeStringUtil.removeChar(cotisationAF.getDateFin().toString(), '.'));
                } else {
                    createEmptyCell();
                }

            } else {
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
            }

            // Nombre travailleur le plus grand sur les 12 derniers mois
            if (dernierControle != null && dernierControle.getDateControle() != null) {
                int nombreTravailleur = VulpeculaServiceLocator.getPosteTravailService()
                        .findPostesActifsByIdAffiliePourControle(employeur.getId(), dernierControle.getDateControle());
                createCell(nombreTravailleur);
            } else {
                createEmptyCell();
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
