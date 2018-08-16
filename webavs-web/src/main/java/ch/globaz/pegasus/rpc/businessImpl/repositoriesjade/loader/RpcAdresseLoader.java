package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.ITIAdresseDefTable;
import globaz.pyxis.db.adressecourrier.ITIAvoirAdresseDefTable;
import globaz.pyxis.db.adressecourrier.ITILocaliteDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.common.domaine.Canton;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.pegasus.businessimpl.services.adresse.AdresseLoader;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;
import ch.globaz.queryexc.converters.Mapper;
import ch.globaz.queryexec.bridge.jade.SCM;

class RpcAdresseLoader {

    private final Map<String, Canton> cantons = AdresseLoader.loadCanton();
    private final Map<String, List<RpcAddress>> cache = new ConcurrentHashMap<String, List<RpcAddress>>();
    private final HashMap<String, TreeMap<Integer, String>> cascadeDomaines = TIApplication
            .getCachedCascadesDomaine(BSessionUtil.getSessionFromThreadContext());
    private int partition = 2000;
    private Boolean parallel;
    private static final String ADRESSE_ETRANGER = "ET"; 

    private RpcAdresseLoader() {
    }

    public static RpcAdresseLoader build() {
        return new RpcAdresseLoader();
    }

    public RpcAdresseLoader parallel(Boolean parallel) {
        this.parallel = parallel;
        return this;
    }

    public RpcAdresseLoader partitionSize(int partition) {
        this.partition = partition;
        return this;
    }

    public RpcAdresseLoader load(Set<String> idsTiers) {
        cache.putAll(new ThreadLoaderRunner<List<String>, Map<String, List<RpcAddress>>>()
                .input(new ArrayList<String>(idsTiers))
                .transformer(new Transformer<List<String>, Map<String, List<RpcAddress>>>() {
                    @Override
                    public Map<String, List<RpcAddress>> transform(List<String> ids) {
                        return loadRpcAdresse(ids, cantons);
                    }
                }).partitionList(partition).parallel(parallel).loadAndJoinMap());
        return this;
    }

    public Map<String, RpcAddress> resolve(String domaine, String type) {
        return this.resolve(cache, domaine, type);
    }

    private Map<String, RpcAddress> resolve(Map<String, List<RpcAddress>> adresses, String domaine, String type) {
        Map<String, RpcAddress> map = new HashMap<String, RpcAddress>();

        for (Entry<String, List<RpcAddress>> entry : adresses.entrySet()) {
            String idExterne = "";
            RpcAddress adresse = resolveAdresse(entry.getValue(), domaine, type, idExterne);
            if (adresse != null) {
                map.put(entry.getKey(), adresse);
            } else {
                map.put(entry.getKey(), new RpcAddress());
            }
        }
        return map;
    }

    private static Map<String, List<RpcAddress>> loadRpcAdresse(List<String> idsTiers, final Map<String, Canton> cantons) {
        String toDay = JACalendar.today().toStrAMJ();
        SQLWriter writer = SQLWriter
                .write()
                .select()
                .fields("SCHEMA." + ITIAvoirAdresseDefTable.TABLE_NAME + "." + ITIAvoirAdresseDefTable.ID_ADRESSE,
                        "SCHEMA." + ITITiersDefTable.TABLE_NAME + "." + ITITiersDefTable.ID_TIERS,
                        ITILocaliteDefTable.CS_CANTON, ITIAvoirAdresseDefTable.CS_TYPE_ADRESSE,
                        ITIAvoirAdresseDefTable.ID_APPLICATION, ITIAvoirAdresseDefTable.ID_EXTERNE,
                        "SCHEMA." + ITIAdresseDefTable.TABLE_NAME + "." + ITIAdresseDefTable.ID_LOCALITE,
                        "SCHEMA." + ITILocaliteDefTable.TABLE_NAME + "." + ITILocaliteDefTable.NUMERO_COMMUNE_OFS)
                .from("SCHEMA.TIADREP")
                .join("SCHEMA.TIAADRP ON (SCHEMA.TIAADRP.HAIADR=SCHEMA.TIADREP.HAIADR)")
                .join("SCHEMA.TITIERP ON (SCHEMA.TIAADRP.HTITIE=SCHEMA.TITIERP.HTITIE AND HEIADR=HEIAAU)")
                .join("SCHEMA.TILOCAP ON (SCHEMA.TIADREP.HJILOC=SCHEMA.TILOCAP.HJILOC)")
                .where("((" + toDay + " BETWEEN HEDDAD AND HEDFAD) OR (HEDFAD=0 AND HEDDAD<=" + toDay
                        + ")) and SCHEMA.TITIERP.HTITIE").in(idsTiers);

        return SCM.newInstance(RpcAddress.class).query(writer.toSql()).mapper(new Mapper<RpcAddress>() {
            @Override
            public RpcAddress map(ResultSet rs, int index) {
                try {
                    String id = rs.getString(ITIAvoirAdresseDefTable.ID_ADRESSE);
                    String idCanton = rs.getString(ITILocaliteDefTable.CS_CANTON);
                    String type = rs.getString(ITIAvoirAdresseDefTable.CS_TYPE_ADRESSE);
                    String domaine = rs.getString(ITIAvoirAdresseDefTable.ID_APPLICATION);
                    String idExterne = rs.getString(ITIAvoirAdresseDefTable.ID_EXTERNE);

                    String nofs = rs.getString(ITILocaliteDefTable.NUMERO_COMMUNE_OFS);
                    if (nofs != null) {
                        nofs = nofs.trim();
                    }
                    Canton canton = cantons.get(idCanton);

                    return new RpcAddress(canton, nofs, id, type, domaine, idExterne);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }).executeAndGroupBy(ITITiersDefTable.ID_TIERS, String.class);
    }

    /*
     * Effectue l'héritage...
     */
    private RpcAddress resolveAdresse(List<RpcAddress> adresses, String domaine, String type, String idExterne) {
        RpcAddress adresse = null;

        TreeMap<Integer, String> cascadeDomaine = cascadeDomaines.get(domaine);

        if (cascadeDomaine != null) {
            for (Integer positionDomaine : cascadeDomaine.keySet()) {
                if (adresse == null) {
                    // 1) Domaine CascadeDomaineX, type Y, idExterne
                    adresse = findAdresse(adresses, cascadeDomaine.get(positionDomaine), type, idExterne);
                }
                if (adresse == null) {
                    // 2) Domaine CascadeDomaineX, type Y
                    adresse = findAdresse(adresses, cascadeDomaine.get(positionDomaine), type);
                }
            }
        }

        if (adresse == null) {
            // 1) Domaine X, type Y, idExterne
            adresse = findAdresse(adresses, domaine, type, idExterne);
            if (adresse == null) {
                // 2) Domaine X, type Y
                adresse = findAdresse(adresses, domaine, type);
                if (adresse == null) {
                    // 3) Domaine X, type courrier,idExterne
                    adresse = findAdresse(adresses, domaine, IConstantes.CS_AVOIR_ADRESSE_COURRIER, idExterne);
                    if (adresse == null) {
                        // 4) Domaine X, type courrier
                        adresse = findAdresse(adresses, domaine, IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                        if (adresse == null) {
                            // 5) Domaine std, type Y, idExterne
                            adresse = findAdresse(adresses, IConstantes.CS_APPLICATION_DEFAUT, type, idExterne);
                            if (adresse == null) {
                                // 6) Domaine std, type Y
                                adresse = findAdresse(adresses, IConstantes.CS_APPLICATION_DEFAUT, type);
                                if (adresse == null) {
                                    // 7) Domaine std, type courrier, idExterne
                                    adresse = findAdresse(adresses, IConstantes.CS_APPLICATION_DEFAUT,
                                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, idExterne);
                                    if (adresse == null) {
                                        // 8) Domaine std, type courrier
                                        adresse = findAdresse(adresses, IConstantes.CS_APPLICATION_DEFAUT,
                                                IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                                        if (adresse == null) {
                                            // 9) domaine std, type domicile
                                            adresse = findAdresse(adresses, IConstantes.CS_APPLICATION_DEFAUT,
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

        return adresse;
    }

    private RpcAddress findAdresse(Collection<RpcAddress> adresses, String domaine, String type) {
        for (RpcAddress adresse : adresses) {
            if (adresse.getType().equals(type) && adresse.getDomaine().equals(domaine)
                    && "".equals(adresse.getIdExterne().trim()) && !ADRESSE_ETRANGER.equals(adresse.getCanton().getAbreviation())) {
                return adresse;
            }
        }
        return null;
    }

    private RpcAddress findAdresse(Collection<RpcAddress> adresses, String domaine, String type, String idExterne) {
        for (RpcAddress adresse : adresses) {
            if (adresse.getType().equals(type) && adresse.getDomaine().equals(domaine)
                    && adresse.getIdExterne().equals(idExterne) && !ADRESSE_ETRANGER.equals(adresse.getCanton().getAbreviation())) {
                return adresse;
            }
        }
        return null;
    }

}
