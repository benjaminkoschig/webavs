package ch.globaz.pegasus.businessimpl.services.adresse;

import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Canton;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.queryexc.converters.Mapper;
import ch.globaz.queryexec.bridge.jade.SCM;
import com.google.common.collect.Lists;

public class AdresseLoader {

    private Map<String, List<TIAbstractAdresseData>> cache = new ConcurrentHashMap<String, List<TIAbstractAdresseData>>();
    private Map<String, List<Adresse>> cacheAdresse = new ConcurrentHashMap<String, List<Adresse>>();

    private Map<String, Canton> cantons;

    public AdresseLoader() {
        cantons = loadCanton();
    }

    /**
     * Permet de charger les adresses valables pour un groupe de tiers. Fait la cascade.
     * 
     * 
     * @param idsTiers
     * @param domaine
     * @param type
     * @return
     */
    public Map<String, List<Adresse>> loadLastByIdsTiersAndGroupByIdTiers(Collection<String> idsTiers, String domaine,
            String type) {
        try {
            List<String> idsNotInCache = resolveIdsNotInCache(idsTiers);
            return searchByLot(idsNotInCache, domaine, type, 2000);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger les adresses avec le manager ->TIAdresseLoaderManager ",
                    e);
        }

    }

    private List<Adresse> search(Collection<String> idsTiers) {
        long start = System.currentTimeMillis();
        TIAdresseLoaderManager mgr = new TIAdresseLoaderManager();
        mgr.setForIdsTiers(idsTiers);
        mgr.setOrderBy("tiersHTLDU1, tiersHTLDU2");
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger les adresses avec le manager ->TIAdresseLoaderManager ",
                    e);
        }
        System.out.println("Manager time: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        List<Adresse> adresses = new ArrayList<Adresse>();
        for (int j = 0; j < mgr.size(); j++) {
            TIAbstractAdresseData entityData = (TIAbstractAdresseData) mgr.getEntity(j);
            adresses.add(convert(entityData));
        }
        return adresses;
    }

    /**
     * Permet de charger les adresses valables pour un groupe de tiers. Fait la cascade.
     * 
     * 
     * @param idsTiers
     * 
     */
    public void loadCurrentAdresseByIdsTiers(Collection<String> idsTiers, int partition) {
        try {
            List<String> idsNotInCache = resolveIdsNotInCache(idsTiers);
            List<List<String>> idsSplited = Lists.partition(new ArrayList<String>(idsNotInCache), partition);
            for (List<String> ids : idsSplited) {
                search(new ArrayList<String>(ids));
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger les adresses avec le manager ->TIAdresseLoaderManager ",
                    e);
        }
    }

    public Map<String, List<Adresse>> resolveAdresse(String domaine, String type) {
        return convertAll(domaine, type);
    }

    private List<String> resolveIdsNotInCache(Collection<String> idsTiers) {
        List<String> idsNotInCache = new ArrayList<String>();
        for (final Iterator<String> iterator = idsTiers.iterator(); iterator.hasNext();) {
            String id = iterator.next();
            if (!cache.containsKey(id)) {
                idsNotInCache.add(id);
            }
        }
        return idsNotInCache;
    }

    private Map<String, List<Adresse>> searchByLot(List<String> idsTiers, String domaine, String type, int limitSize)
            throws Exception {
        List<List<String>> idsSplited = Lists.partition(idsTiers, limitSize);
        Map<String, List<Adresse>> map = new HashMap<String, List<Adresse>>();
        for (List<String> ids : idsSplited) {
            map.putAll(searchAndConvert(domaine, type, ids));
        }
        return map;
    }

    private Map<String, List<Adresse>> searchAndConvert(String domaine, String type, List<String> idsTiers)
            throws Exception {
        search(idsTiers);
        return convertAll(domaine, type);
    }

    private void search(List<String> idsTiers) throws Exception {
        long start = System.currentTimeMillis();
        TIAdresseLoaderManager mgr = new TIAdresseLoaderManager();
        mgr.setForIdsTiers(idsTiers);
        mgr.setOrderBy("tiersHTLDU1, tiersHTLDU2");
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        mgr.find(BManager.SIZE_NOLIMIT);
        System.out.println("Manager time: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();

        for (int j = 0; j < mgr.size(); j++) {
            TIAbstractAdresseData entityData = (TIAbstractAdresseData) mgr.getEntity(j);
            String idTiers = entityData.getIdTiers();
            if (!cache.containsKey(idTiers)) {
                cache.put(idTiers, new ArrayList<TIAbstractAdresseData>());
            }
            cache.get(idTiers).add(entityData);
        }
        System.out.println("Cache: " + (System.currentTimeMillis() - start));

    }

    private void bySQL(List<String> idsTiers) {
        String toDay = JACalendar.today().toStrAMJ();
        SQLWriter writer = SQLWriter
                .write()
                .append("SELECT SCHEMA.TITIERP.HTITIE  tiersHTITIE,SCHEMA.TITIERP.HTINAC  tiersHTINAC,SCHEMA.TITIERP.HTTLAN  tiersHTTLAN,SCHEMA.TITIERP.HTPPHY  tiersHTPPHY,"
                        + "SCHEMA.TIAADRP.HEIAAU  avoirAdrHEIAAU,SCHEMA.TIAADRP.HFIAPP  avoirAdrHFIAPP,SCHEMA.TIAADRP.HETTAD  avoirAdrHETTAD,SCHEMA.TIAADRP.HEDDAD  avoirAdrHEDDAD,"
                        + "SCHEMA.TIAADRP.HEDFAD  avoirAdrHEDFAD,SCHEMA.TIAADRP.HEDCAD  avoirAdrHEDCAD,SCHEMA.TIAADRP.HEIDEX  avoirAdrHEIDEX,SCHEMA.TITIERP.HTTTTI  tiersHTTTTI,"
                        + "SCHEMA.TITIERP.HTLDU1  tiersHTLDU1,SCHEMA.TITIERP.HTLDU2  tiersHTLDU2,SCHEMA.TITIERP.HTLDE1  tiersHTLDE1,SCHEMA.TITIERP.HTLDE2  tiersHTLDE2,"
                        + "SCHEMA.TITIERP.HTLDE3  tiersHTLDE3,SCHEMA.TITIERP.HTLDE4  tiersHTLDE4,SCHEMA.TITIERP.HNIPAY  tiersHNIPAY,SCHEMA.TIADREP.HATTAD  adresseHATTAD,"
                        + "SCHEMA.TIADREP.HAATTE  adresseHAATTE,SCHEMA.TIADREP.HAADR1  adresseHAADR1,SCHEMA.TIADREP.HAADR2  adresseHAADR2,SCHEMA.TIADREP.HAADR3  adresseHAADR3,"
                        + "SCHEMA.TIADREP.HAADR4  adresseHAADR4,SCHEMA.TIADREP.HARUE   adresseHARUE,SCHEMA.TIADREP.HANRUE  adresseHANRUE,SCHEMA.TIADREP.HACPOS  adresseHACPOS,"
                        + "SCHEMA.TIADREP.HAIADU  adresseHAIADU,SCHEMA.TILOCAP.HJILOC  locHJILOC,SCHEMA.TILOCAP.HJNPA      locHJNPA,SCHEMA.TILOCAP.HJCNPA  locHJCNPA,"
                        + "SCHEMA.TILOCAP.HJICAN  locHJICAN,SCHEMA.TILOCAP.HJLOCA  locHJLOCA,SCHEMA.TIPAYSP.HNIPAY  paysHNIPAY,SCHEMA.TIPAYSP.HNCFCP  paysHNCFCP,"
                        + "SCHEMA.TIPAYSP.HNLFR   paysHNLFR,SCHEMA.TIPAYSP.HNLAL   paysHNLAL,SCHEMA.TIPAYSP.HNLIT   paysHNLIT,SCHEMA.TIPAYSP.HNCISO  paysHNCISO  "
                        + "FROM  SCHEMA.TIADREP INNER JOIN SCHEMA.TIAADRP ON (SCHEMA.TIAADRP.HAIADR=SCHEMA.TIADREP.HAIADR) "
                        + "INNER JOIN SCHEMA.TITIERP ON (SCHEMA.TIAADRP.HTITIE=SCHEMA.TITIERP.HTITIE AND HEIADR=HEIAAU) "
                        + "INNER JOIN SCHEMA.TILOCAP ON (SCHEMA.TIADREP.HJILOC=SCHEMA.TILOCAP.HJILOC) "
                        + "INNER JOIN SCHEMA.TIPAYSP ON (SCHEMA.TILOCAP.HNIPAY=SCHEMA.TIPAYSP.HNIPAY) "
                        + "WHERE (("
                        + toDay
                        + " BETWEEN HEDDAD AND HEDFAD) OR (HEDFAD=0 AND HEDDAD<="
                        + toDay
                        + ")) and  SCHEMA.TITIERP.HTITIE ").in(idsTiers);

        List<Adresse> adresses = SCM.newInstance(Adresse.class).query(writer.toSql()).mapper(new Mapper<Adresse>() {

            @Override
            public Adresse map(ResultSet rs, int index) {
                try {
                    String designation1_tiers = rs.getString(TIAbstractAdresseData.ALIAS_TIERS_DESIGNATION_1);
                    String designation2_tiers = rs.getString(TIAbstractAdresseData.ALIAS_TIERS_DESIGNATION_2);
                    String designation3_tiers = rs.getString(TIAbstractAdresseData.ALIAS_TIERS_DESIGNATION_3);
                    String designation4_tiers = rs.getString(TIAbstractAdresseData.ALIAS_TIERS_DESIGNATION_4);
                    String titre_tiers = rs.getString(TIAbstractAdresseData.ALIAS_TIERS_CS_TITRE_TIERS);

                    // AvoirAdresse
                    String idUnique = rs.getString(TIAbstractAdresseData.ALIAS_AVOIR_ADRESSE_ID_ADRESSE_INT_UNIQUE);

                    // Adresse
                    String attention = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_ATTENTION);
                    String designation1_adr = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_LIGNE_ADRESSE_1);
                    String designation2_adr = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_LIGNE_ADRESSE_2);
                    String designation3_adr = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_LIGNE_ADRESSE_3);
                    String designation4_adr = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_LIGNE_ADRESSE_4);
                    String titre_adr = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_CS_TITRE_ADRESSE);
                    String rue = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_RUE);
                    String casePostale = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_CASE_POSTALE);
                    String numero = rs.getString(TIAbstractAdresseData.ALIAS_ADRESSE_NUMERO_RUE);

                    String idCanton = rs.getString(TIAbstractAdresseData.ALIAS_LOCALITE_CS_CANTON);

                    // Localité
                    String npa = rs.getString(TIAbstractAdresseData.ALIAS_LOCALITE_NUMERO_POSTAL);

                    String localite = rs.getString(TIAbstractAdresseData.ALIAS_LOCALITE_NOM_LOCALITE);
                    String paysIso = rs.getString(TIAbstractAdresseData.ALIAS_PAYS_CODE_ISO);

                    Canton canton = cantons.get(idCanton);

                    return new Adresse(localite, casePostale, attention, npa, paysIso, canton, titre_adr, rue, numero,
                            designation1_adr, designation2_adr, designation3_adr, designation4_adr, idUnique,
                            designation1_tiers, designation2_tiers, designation3_tiers, designation4_tiers, titre_tiers);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).execute();
    }

    private Map<String, List<Adresse>> convertAll(String domaine, String type) {
        Map<String, List<Adresse>> map = new HashMap<String, List<Adresse>>();
        for (Entry<String, List<TIAbstractAdresseData>> entry : cache.entrySet()) {
            String idExterne = "";
            TIAbstractAdresseData data = _getAdresseData(entry.getValue(), domaine, type, idExterne);
            if (data != null) {
                if (!map.containsKey(data.getIdTiers())) {
                    map.put(data.getIdTiers(), new ArrayList<Adresse>());
                }
                try {
                    Adresse addresse = convert(data);
                    map.get(data.getIdTiers()).add(addresse);
                } catch (Exception e) {
                    new TechnicalExceptionWithTiers("Impossible de charger l'adresse id:" + data.getIdAdresseUnique(),
                            data.getIdTiers(), e);
                    map.get(data.getIdTiers()).add(new Adresse());
                }
            }
        }
        return map;
    }

    private Adresse convert(TIAbstractAdresseData data) {
        Canton canton = cantons.get(data.getIdCanton());
        return new Adresse(data.getLocalite(), data.getCasePostale(), data.getAttention(), data.getNpa(),
                data.getPaysIso(), canton, data.getTitre_adr(), data.getRue(), data.getNumero(),
                data.getDesignation1_adr(), data.getDesignation2_adr(), data.getDesignation3_adr(),
                data.getDesignation4_adr(), data.getIdAdresseUnique(), data.getDesignation1_tiers(),
                data.getDesignation2_tiers(), data.getDesignation3_tiers(), data.getDesignation4_tiers(),
                data.getTitre_tiers());
    }

    public static Map<String, Canton> loadCanton() {

        List<JadeCodeSysteme> codeSystemes;
        try {
            codeSystemes = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme("PYCANTON");
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        }
        Map<String, Canton> map = new HashMap<String, Canton>();
        for (JadeCodeSysteme jadeCodeSysteme : codeSystemes) {
            map.put(jadeCodeSysteme.getIdCodeSysteme(),
                    new Canton(jadeCodeSysteme.getCodeUtilisateur(Langues.Francais)));
        }

        return map;
    }

    /*
     * Effectue l'héritage...
     */
    private TIAbstractAdresseData _getAdresseData(List<TIAbstractAdresseData> adresses4Tiers, String domaine,
            String type, String idExterne) {
        TIAbstractAdresseData data = null;

        TreeMap<Integer, String> cascadeDomaine = TIApplication.getCachedCascadesDomaine(
                BSessionUtil.getSessionFromThreadContext()).get(domaine);
        if (cascadeDomaine != null) {
            for (Integer positionDomaine : cascadeDomaine.keySet()) {
                // 1) Domaine CascadeDomaineX, type Y, idExterne
                if (data == null) {
                    data = findAdresse(adresses4Tiers, cascadeDomaine.get(positionDomaine), type, idExterne);
                }
                if (data == null) {
                    // 2) Domaine CascadeDomaineX, type Y
                    data = findAdresse(adresses4Tiers, cascadeDomaine.get(positionDomaine), type);
                }
            }
        }

        if (data == null) {
            // 1) Domaine X, type Y, idExterne
            data = findAdresse(adresses4Tiers, domaine, type, idExterne);
            if (data == null) {
                // 2) Domaine X, type Y
                data = findAdresse(adresses4Tiers, domaine, type);
                if (data == null) {
                    // 3) Domaine X, type courrier,idExterne
                    data = findAdresse(adresses4Tiers, domaine, IConstantes.CS_AVOIR_ADRESSE_COURRIER, idExterne);
                    if (data == null) {
                        // 4) Domaine X, type courrier
                        data = findAdresse(adresses4Tiers, domaine, IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                        if (data == null) {
                            // 5) Domaine std, type Y, idExterne
                            data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT, type, idExterne);
                            if (data == null) {
                                // 6) Domaine std, type Y
                                data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT, type);
                                if (data == null) {
                                    // 7) Domaine std, type courrier, idExterne
                                    data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT,
                                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, idExterne);
                                    if (data == null) {
                                        // 8) Domaine std, type courrier
                                        data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT,
                                                IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                                        if (data == null) {
                                            // 9) domaine std, type domicile
                                            data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT,
                                                    IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return data;
    }

    /**
     * @param adresses4Tiers
     * @param domaine
     * @param type
     * @return TIAbstractAdresseData
     */
    private TIAbstractAdresseData findAdresse(Collection<TIAbstractAdresseData> adresses4Tiers, String domaine,
            String type) {
        TIAbstractAdresseData data;
        for (Iterator<TIAbstractAdresseData> it = adresses4Tiers.iterator(); it.hasNext();) {
            data = it.next();
            if ((data.getTypeAdresse().equals(type)) && ("".equals(data.getIdExterne()))
                    && (data.getIdApplication().equals(domaine))) {
                return data;
            }
        }
        return null;
    }

    private TIAbstractAdresseData findAdresse(Collection<TIAbstractAdresseData> adresses4Tiers, String domaine,
            String type, String idExterne) {
        TIAbstractAdresseData data;
        for (Iterator<TIAbstractAdresseData> it = adresses4Tiers.iterator(); it.hasNext();) {
            data = it.next();
            if ((data.getTypeAdresse().equals(type)) && (data.getIdExterne().equals(idExterne))
                    && (data.getIdApplication().equals(domaine))) {
                return data;
            }
        }
        return null;
    }

}
