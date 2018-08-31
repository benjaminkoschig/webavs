package ch.globaz.vulpecula.documents.listesinternes;

import static ch.globaz.vulpecula.documents.DocumentConstants.*;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.listesinternes.CaisseKey.Type;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListesInternesProcess extends BProcessWithContext {
    public static final String AVEC_TO = "_avecTO";
    public static final String SANS_TO = "_sansTO";
    public static final String AVEC_TO_TXT = "avec TO";
    public static final String SANS_TO_TXT = "sans TO";

    public static enum ExportMode {
        CONVENTION,
        CAISSE,
        CAISSE_CONVENTION;
    }

    private static final long serialVersionUID = -2242326023131077018L;

    public static class Caisse implements Comparable<Caisse> {
        private final String idExterne;
        private final String libelle;
        private final String type;

        public Caisse(String idExterne, String libelle) {
            this(idExterne, libelle, null);
        }

        public Caisse(String idExterne, String libelle, String type) {
            this.idExterne = idExterne;
            this.libelle = libelle;
            this.type = type;
        }

        public String getIdExterne() {
            return idExterne;
        }

        public String getType() {
            return type;
        }

        public String getLibelle() {
            return libelle;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((idExterne == null) ? 0 : idExterne.hashCode());
            result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Caisse other = (Caisse) obj;
            if (idExterne == null) {
                if (other.idExterne != null) {
                    return false;
                }
            } else if (!idExterne.equals(other.idExterne)) {
                return false;
            }
            if (libelle == null) {
                if (other.libelle != null) {
                    return false;
                }
            } else if (!libelle.equals(other.libelle)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Caisse o) {
            if (o == null) {
                return -1;
            }
            return getIdExterne().compareTo(o.getIdExterne());
        }
    }

    private String annee;
    private boolean omitTO = false;
    private ExportMode mode;
    private TaxationOfficeContainerService taxationOfficeContainerService;

    public void setMode(ExportMode mode) {
        this.mode = mode;
    }

    public ExportMode getMode() {
        return mode;
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        Annee annee = new Annee(this.annee);
        taxationOfficeContainerService = new TaxationOfficeContainerService();
        TaxationOfficeContainer toContainer = taxationOfficeContainerService.getTOContainer(annee, omitTO);
        List<Convention> conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
        String docName;
        String file = null;

        switch (mode) {
            case CAISSE:
                // mode Onglet 2
                try {
                    Map<Convention, Map<Date, List<RecapitulatifDTO>>> retrieveCotisations = retrieveCotisations(
                            conventions, annee, false);
                    docName = LISTES_INTERNES_RECAP_CAISSE_DOC_NAME + (omitTO ? SANS_TO : AVEC_TO);
                    Map<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> cotisationsByCaisse = preprocessListGroupByCaisse(retrieveCotisations);

                    RecapitulatifParCaisse recapitulatifParCaisse = new RecapitulatifParCaisse(cotisationsByCaisse,
                            toContainer, annee);
                    RecapitulatifParCaisseExcel excelByCaisse = new RecapitulatifParCaisseExcel(getSession(), docName,
                            DocumentConstants.LISTES_INTERNES_RECAP_CAISSE + " " + this.annee + " ("
                                    + (omitTO ? SANS_TO_TXT : AVEC_TO_TXT) + ")", annee, recapitulatifParCaisse);
                    excelByCaisse.create();
                    file = excelByCaisse.getOutputFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                break;
            case CAISSE_CONVENTION:
                // mode Onglet 4
                Map<Convention, Map<Date, List<RecapitulatifDTO>>> retrieveCotisationsWithTaux = retrieveCotisations(
                        conventions, annee, true);
                docName = LISTES_INTERNES_RECAP_CAISSE_CONVENTION_DOC_NAME + (omitTO ? SANS_TO : AVEC_TO);
                RecapitulatifParConventionParCaisse recapitulatifParConventionParCaisse = new RecapitulatifParConventionParCaisse(
                        groupForConventionCaisse(retrieveCotisationsWithTaux, toContainer));

                RecapitulatifParConventionParCaisseExcel excelByCaisseByConvention = new RecapitulatifParConventionParCaisseExcel(
                        getSession(), docName, DocumentConstants.LISTES_INTERNES_RECAP_CAISSE_CONVENTION + " "
                                + this.annee + " (" + (omitTO ? SANS_TO_TXT : AVEC_TO_TXT) + ")",
                        recapitulatifParConventionParCaisse);
                excelByCaisseByConvention.create();
                file = excelByCaisseByConvention.getOutputFile();
                break;
            case CONVENTION:
            default:
                // mode Onglet 1
                Map<Convention, Map<Date, List<RecapitulatifDTO>>> retrieveCotisations = retrieveCotisations(
                        conventions, annee, false);
                docName = LISTES_INTERNES_RECAP_CONVENTION_DOC_NAME + (omitTO ? SANS_TO : AVEC_TO);
                RecapitulatifParConvention recapitulatifParConvention = new RecapitulatifParConvention(
                        groupByConventionCaisse(retrieveCotisations), toContainer, annee);
                RecapitulatifParConventionExcel excel = new RecapitulatifParConventionExcel(getSession(), docName,
                        DocumentConstants.LISTES_INTERNES_RECAP_CONVENTION + " " + this.annee + " ("
                                + (omitTO ? SANS_TO_TXT : AVEC_TO_TXT) + ")", annee, recapitulatifParConvention);
                excel.create();
                file = excel.getOutputFile();
                break;
        }

        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), file);
        return true;
    }

    @Override
    protected String getEMailObject() {
        return "Listes internes";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private Map<Convention, Map<Date, List<RecapitulatifDTO>>> retrieveCotisations(List<Convention> conventions,
            Annee annee, boolean wantTaux) {
        Map<Convention, Map<Date, List<RecapitulatifDTO>>> data = new HashMap<Convention, Map<Date, List<RecapitulatifDTO>>>();
        for (Convention convention : conventions) {
            data.put(convention, retrieveCotisations(convention, annee, wantTaux));
        }
        return data;
    }

    private Map<Date, List<RecapitulatifDTO>> retrieveCotisations(Convention convention, Annee annee, boolean wantTaux) {
        Map<Date, List<RecapitulatifDTO>> dataParMois = new HashMap<Date, List<RecapitulatifDTO>>();
        List<Date> months = new AnneeComptable(annee).getMois();

        String strWithTauxSelect = "";
        String strWithTauxGroupBy = "";
        if (wantTaux) {
            strWithTauxSelect = ", b.TAUX as TAUX";
            strWithTauxGroupBy = ", b.TAUX";
        }

        for (Date month : months) {
            String query = "select "
                    + "c.IDEXTERNE AS ID_EXTERNE,"
                    + "f.MBLLIF AS LIBELLE, c.NATURERUBRIQUE, c.IDSECTEUR, sum(b.Masse) AS masse, sum(b.MONTANT) AS MONTANT,f.MBTTYP as TYPE"
                    + strWithTauxSelect + " from schema.casectp a "
                    + "inner join schema.caoperp b on a.IDSECTION=b.IDSECTION "
                    + "inner join schema.CARUBRP c on b.IDCOMPTE=c.IDRUBRIQUE "
                    + "inner join schema.TIADMIP d on a.IDCAISSEPRO=d.HTITIE "
                    + "inner join schema.CACPTAP e on a.IDCOMPTEANNEXE=e.IDCOMPTEANNEXE "
                    + "inner join schema.afassup f on f.MBIRUB=c.IDRUBRIQUE "
                    + "inner join schema.CAJOURP g on g.IDJOURNAL=b.IDJOURNAL " + "where g.DATEVALEURCG between "
                    + month.getFirstDayOfMonth().getValue() + " and " + month.getLastDayOfMonth().getValue()
                    + " and e.IDEXTERNEROLE like '%-" + convention.getCode()
                    + "%' and c.IDSECTEUR not in (2000,9000,7900) and b.ETAT=205002 "
                    + "group by c.IDEXTERNE, c.NATURERUBRIQUE, c.IDSECTEUR, f.MBLLIF, f.MBTTYP" + strWithTauxGroupBy
                    + " ORDER BY f.MBLLIF ASC";
            dataParMois.put(month, SCM.newInstance(RecapitulatifDTO.class).query(query).execute());
        }
        return dataParMois;
    }

    public Map<Convention, Map<CaisseKey, PeriodeRecapitulatifs>> groupByConventionCaisse(
            Map<Convention, Map<Date, List<RecapitulatifDTO>>> data) {
        Map<Convention, Map<CaisseKey, PeriodeRecapitulatifs>> map = new TreeMap<Convention, Map<CaisseKey, PeriodeRecapitulatifs>>();
        for (Map.Entry<Convention, Map<Date, List<RecapitulatifDTO>>> entry : data.entrySet()) {
            map.put(entry.getKey(), aggreateRecapsParCaisse(entry.getValue()));
        }
        return map;
    }

    public Map<Convention, Map<Caisse, CaisseRecapitulatifs>> groupForConventionCaisse(
            Map<Convention, Map<Date, List<RecapitulatifDTO>>> sourceData,
            TaxationOfficeContainer taxationOfficeContainer) {
        Map<Convention, Map<Caisse, CaisseRecapitulatifs>> result = new TreeMap<Convention, Map<Caisse, CaisseRecapitulatifs>>();

        for (Map.Entry<Convention, Map<Date, List<RecapitulatifDTO>>> sourceEntry : sourceData.entrySet()) {
            Convention sourceConvention = sourceEntry.getKey();
            Map<Date, List<RecapitulatifDTO>> sourceValue = sourceEntry.getValue();

            Map<Caisse, CaisseRecapitulatifs> caisseAndRecap = new TreeMap<Caisse, CaisseRecapitulatifs>();
            result.put(sourceConvention, caisseAndRecap);

            for (List<RecapitulatifDTO> dtos : sourceValue.values()) {
                for (RecapitulatifDTO dto : dtos) {
                    for (Caisse caisse : getCaisses(sourceValue)) {
                        if (dto.getIdExterne().equals(caisse.getIdExterne())) {
                            CaisseRecapitulatifs recap;

                            if (caisseAndRecap.containsKey(caisse)) {
                                recap = caisseAndRecap.get(caisse);
                            } else {
                                recap = new CaisseRecapitulatifs();

                                // TODO est-ce que setter le taux comme ça est vraiment une bonne idée?
                                if (dto.getTaux() == null) {
                                    dto.setTaux("0");
                                }

                                Taux taux = new Taux(dto.getTaux());
                                if (!taux.isZero()) {
                                    recap.setTaux(taux);
                                }

                                caisseAndRecap.put(caisse, recap);
                            }

                            recap.addContribution(Montant.valueOf(dto.getMontant())).substractContribution(
                                    taxationOfficeContainer.getMontant(sourceConvention, caisse.getIdExterne()));
                            recap.addMasse(Montant.valueOf(dto.getMasse())).substractMasse(
                                    taxationOfficeContainer.getMasse(sourceConvention, caisse.getIdExterne()));
                        }
                    }
                }
            }
        }

        return result;
    }

    public Map<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> preprocessListGroupByCaisse(
            Map<Convention, Map<Date, List<RecapitulatifDTO>>> source) {
        try {
            Map<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> result = new TreeMap<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>>();

            Set<String> listTypes = new TreeSet<String>();
            for (Map.Entry<Convention, Map<Date, List<RecapitulatifDTO>>> sourceEntry : source.entrySet()) {
                for (Entry<Date, List<RecapitulatifDTO>> sourceEntryEntry : sourceEntry.getValue().entrySet()) {
                    for (RecapitulatifDTO dto : sourceEntryEntry.getValue()) {
                        listTypes.add(dto.getType());
                    }
                }
            }

            Set<Caisse> caisses = new HashSet<Caisse>();
            for (Map.Entry<Convention, Map<Date, List<RecapitulatifDTO>>> data1 : source.entrySet()) {
                caisses.addAll(getCaisses(data1.getValue()));
            }

            // for each type
            for (String type : listTypes) {
                Map<Caisse, Map<Convention, PeriodeRecapitulatifs>> targetCaissesMap = new TreeMap<Caisse, Map<Convention, PeriodeRecapitulatifs>>();
                result.put(type, targetCaissesMap);

                // for each caisse
                for (Caisse sourceCaisse : caisses) {
                    if (!type.equals(sourceCaisse.getType())) {
                        continue;
                    }

                    SortedMap<Convention, PeriodeRecapitulatifs> targetConvPeriode = new TreeMap<Convention, PeriodeRecapitulatifs>();
                    targetCaissesMap.put(sourceCaisse, targetConvPeriode);

                    // for each convention
                    for (Map.Entry<Convention, Map<Date, List<RecapitulatifDTO>>> sourceByConv : source.entrySet()) {
                        Convention conv = sourceByConv.getKey();
                        targetConvPeriode.put(conv,
                                createPeriodeRecapitulatifs(sourceByConv.getValue(), sourceCaisse, type));
                    }
                }
            }

            return result;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Map<CaisseKey, PeriodeRecapitulatifs> aggreateRecapsParCaisse(Map<Date, List<RecapitulatifDTO>> o) {
        Map<CaisseKey, PeriodeRecapitulatifs> map = new TreeMap<CaisseKey, PeriodeRecapitulatifs>();
        Collection<Caisse> caisses = getCaisses(o);
        for (Caisse caisse : caisses) {
            PeriodeRecapitulatifs periodeRecapitulatifs = createPeriodeRecapitulatifs(o, caisse);
            map.put(new CaisseKey(caisse.getIdExterne(), caisse.getLibelle(), Type.COT), periodeRecapitulatifs);
            map.put(new CaisseKey(caisse.getIdExterne(), caisse.getLibelle(), Type.MS), periodeRecapitulatifs);
        }
        return map;
    }

    /**
     * Regroupement des recapitulatifs par chaque date de l'année en fonction la caisse en paramètre.
     * 
     * @param recapsParDate Recaps par date
     * @param caisse Caisse sociale
     * @return
     */
    private PeriodeRecapitulatifs createPeriodeRecapitulatifs(Map<Date, List<RecapitulatifDTO>> recapsParDate,
            Caisse caisse) {
        PeriodeRecapitulatifs recapsParCaisse = new PeriodeRecapitulatifs();
        for (Map.Entry<Date, List<RecapitulatifDTO>> entry : recapsParDate.entrySet()) {
            recapsParCaisse.put(entry.getKey(), getRecapForCaisse(entry.getValue(), caisse));
        }
        return recapsParCaisse;
    }

    private PeriodeRecapitulatifs createPeriodeRecapitulatifs(Map<Date, List<RecapitulatifDTO>> recapsParDate,
            Caisse caisse, String type) {
        PeriodeRecapitulatifs recapsParCaisse = new PeriodeRecapitulatifs();
        for (Map.Entry<Date, List<RecapitulatifDTO>> entry : recapsParDate.entrySet()) {
            recapsParCaisse.put(entry.getKey(), getRecapForCaisseAndType(entry.getValue(), caisse, type));
        }
        return recapsParCaisse;
    }

    /**
     * Recherche de la caisse sociale dans une liste de recapulatifs.
     * 
     * @param caisse Caisse sociale à rechercher
     * @param recaps Ensemble de recapitulatifs
     * @return Recapitulatif
     */
    private RecapitulatifDTO getRecapForCaisse(List<RecapitulatifDTO> recaps, Caisse caisse) {
        for (RecapitulatifDTO recap : recaps) {
            if (recap.getIdExterne().equals(caisse.getIdExterne())) {
                return recap;
            }
        }
        return null;
    }

    private RecapitulatifDTO getRecapForCaisseAndType(List<RecapitulatifDTO> recaps, Caisse caisse, String type) {
        for (RecapitulatifDTO recap : recaps) {
            if (type.equals(recap.getType()) && recap.getIdExterne().equals(caisse.getIdExterne())) {
                return recap;
            }
        }
        return null;
    }

    /**
     * Recherche de toutes les cotisations contenues dans la map.
     * 
     * @param recapsParDate Données agrégées par date
     * @return Liste des caisses sociales
     */
    private Collection<Caisse> getCaisses(Map<Date, List<RecapitulatifDTO>> recapsParDate) {
        Set<Caisse> caisses = new HashSet<Caisse>();
        for (Map.Entry<Date, List<RecapitulatifDTO>> entry : recapsParDate.entrySet()) {
            for (RecapitulatifDTO recap : entry.getValue()) {
                caisses.add(new Caisse(recap.getIdExterne(), recap.getLibelle(), recap.getType()));
            }
        }
        return caisses;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public boolean isOmitTO() {
        return omitTO;
    }

    public boolean getOmitTO() {
        return omitTO;
    }

    public void setOmitTO(boolean omitTO) {
        this.omitTO = omitTO;
    }
}
