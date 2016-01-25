package ch.globaz.common.document.babel;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.babel.api.helper.ICTListeTextesImpl;
import globaz.babel.db.cat.CTDocumentAPIAdapter;
import globaz.babel.db.cat.CTDocumentAPIAdapterManager;
import globaz.babel.db.cat.CTTexte;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.exceptions.models.decision.BabelException;

public class CatalogueTexteLoader {
    private ConcurrentHashMap<String, Map<Langues, List<CTDocumentAPIAdapter>>> cache = new ConcurrentHashMap<String, Map<Langues, List<CTDocumentAPIAdapter>>>();
    private CTDocumentAPIAdapterManager adapterManager = new CTDocumentAPIAdapterManager();

    public CatalogueTexteLoader() {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            if (session == null) {
                throw new CommonTechnicalException("Impossible d'obtenir la session depuis le thread context !");
            }
            adapterManager.setSession(session);
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    public CatalogueTexteLoader(BSession session) {
        adapterManager.setSession(session);
    }

    private List<CTDocumentAPIAdapter> loadText(String domaine, String csTypeDoc, String nom) throws Exception {

        adapterManager.setForCsTypeDocument(csTypeDoc);
        adapterManager.setForNom(nom);
        adapterManager.setForCsDomaine(domaine);
        adapterManager.setForDefaut(null);
        adapterManager.setForActif(null);
        adapterManager.setForCodeIsoLangue(null);

        adapterManager.find(BManager.SIZE_NOLIMIT);

        List<CTDocumentAPIAdapter> textes = new ArrayList<CTDocumentAPIAdapter>();
        Iterator<CTDocumentAPIAdapter> iter = adapterManager.iterator();
        while (iter.hasNext()) {
            CTDocumentAPIAdapter texte = iter.next();
            textes.add(texte);
        }

        if (textes.isEmpty()) {
            throw new BabelException("Aucun texte n'a été trouvé! -> domaine:" + domaine + ", csTypeDoc:" + csTypeDoc
                    + ", nom: " + nom);
        }
        return textes;
    }

    private Map<Langues, List<CTDocumentAPIAdapter>> groupByLangue(List<CTDocumentAPIAdapter> textes) {

        Map<Langues, List<CTDocumentAPIAdapter>> mapLangues = new ConcurrentHashMap<Langues, List<CTDocumentAPIAdapter>>();
        for (Langues langue : Langues.values()) {
            mapLangues.put(langue, new ArrayList<CTDocumentAPIAdapter>());
        }
        for (CTDocumentAPIAdapter texte : textes) {
            Langues langue = Langues.getLangueDepuisCodeIso(texte.getCodeIsoLangue());
            mapLangues.get(langue).add(texte);
        }
        return mapLangues;
    }

    private Map<Langues, List<CTDocumentAPIAdapter>> loadAndGroupByLangue(String domaine, String csTypeDoc, String nom) {
        try {
            List<CTDocumentAPIAdapter> textes = loadText(domaine, csTypeDoc, nom);
            return groupByLangue(textes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Langues, List<CTDocumentAPIAdapter>> loadByDomaineTypeDocAndName(String domaine, String csTypeDoc,
            String nom) throws Exception {
        Checkers.checkNotNull(domaine, "domaine");
        Checkers.checkNotNull(csTypeDoc, "csTypeDoc");
        Checkers.checkNotNull(nom, "nom");
        String key = domaine + "_" + csTypeDoc + "_" + nom;
        if (!cache.contains(key)) {
            cache.put(key, loadAndGroupByLangue(domaine, csTypeDoc, nom));
        }
        return cache.get(key);
    }

    public Map<Langues, ICTDocument> loadICTDocumentByDomaineTypeDocAndName(String domaine, String csTypeDoc, String nom)
            throws Exception {
        Map<Langues, List<CTDocumentAPIAdapter>> map = loadByDomaineTypeDocAndName(domaine, csTypeDoc, nom);
        Map<Langues, ICTDocument> mapReturn = new HashMap<Langues, ICTDocument>();
        for (Langues langue : Langues.values()) {
            List<CTDocumentAPIAdapter> textes = map.get(langue);
            if (textes != null) {
                if (!textes.isEmpty()) {
                    mapReturn.put(langue, createDocuement(textes));
                }
            }
        }
        return mapReturn;
    }

    public Map<Langues, CTDocumentImpl> loadICTDocumentImpByDomaineTypeDocAndName(String domaine, String csTypeDoc,
            String nom) throws Exception {
        Map<Langues, List<CTDocumentAPIAdapter>> map = loadByDomaineTypeDocAndName(domaine, csTypeDoc, nom);
        Map<Langues, CTDocumentImpl> mapReturn = new HashMap<Langues, CTDocumentImpl>();
        for (Langues langue : Langues.values()) {
            List<CTDocumentAPIAdapter> textes = map.get(langue);
            if (textes != null) {
                if (!textes.isEmpty()) {
                    mapReturn.put(langue, createDocumentForCTDocumentImp(textes));
                }
            }
        }
        return mapReturn;
    }

    public void clearCache() {
        cache = new ConcurrentHashMap<String, Map<Langues, List<CTDocumentAPIAdapter>>>();
    }

    private ICTDocument createDocuement(final List<CTDocumentAPIAdapter> textes) {
        ICTDocument docuement = new ICTDocument() {
            private HashMap<Integer, ICTListeTextesImpl> niveaux = new HashMap<Integer, ICTListeTextesImpl>();
            private CTDocumentAPIAdapter documentText;

            @Override
            public void setISession(BISession newSession) {
            }

            @Override
            public boolean isNew() {
                return isNew();
            }

            @Override
            public String getLastModifiedUser() {
                return null;
            }

            @Override
            public String getLastModifiedTime() {
                return null;
            }

            @Override
            public String getLastModifiedDate() {
                return null;
            }

            @Override
            public String getId() {
                return documentText.getIdDocument();
            }

            @Override
            public BISession getISession() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public void setNomLike(String nom) {
            }

            @Override
            public void setNom(String nom) {
            }

            @Override
            public void setIdDocument(String idDocument) {
            }

            @Override
            public void setDefault(Boolean defaut) {
            }

            @Override
            public void setCsTypeDocument(String csTypeDocument) {
            }

            @Override
            public void setCsDomaine(String csDomaine) {
            }

            @Override
            public void setCsDestinataire(String csDestinataire) {
            }

            @Override
            public void setCodeIsoLangue(String codeIsoLangue) {
            }

            @Override
            public void setActif(Boolean actif) {
            }

            @Override
            public Map loadListeNoms() throws Exception {
                return null;
            }

            @Override
            public ICTDocument[] load() throws Exception {
                niveaux = new HashMap();
                // regrouper les vo par niveau
                for (int id = 0; id < textes.size(); ++id) {
                    CTTexte texte = textes.get(id);
                    Integer niveau = new Integer(textes.get(id).getNiveau());
                    ICTListeTextesImpl textes = niveaux.get(niveau);

                    if (textes == null) {
                        textes = new ICTListeTextesImpl();
                        niveaux.put(niveau, textes);
                    }
                    textes.addTexte(createTexte(texte));
                }
                documentText = textes.get(0);
                return null;
            }

            @Override
            public Boolean isStyledDocument() {
                return documentText.getIsStyledDocument();
            }

            @Override
            public Boolean isDefaut() {
                return documentText.getIsSelectedByDefault();
            }

            @Override
            public Boolean isActif() {
                return documentText.getActif();
            }

            @Override
            public ICTListeTextes getTextes(int idNiveau) {
                return niveaux.get(idNiveau);
            }

            @Override
            public String getNomLike() {
                return null;
            }

            @Override
            public String getNom() {
                return documentText.getNom();
            }

            @Override
            public String getIdDocument() {
                return documentText.getIdDocument();
            }

            @Override
            public String getDateDesactivation() {
                return documentText.getDateDesactivation();
            }

            @Override
            public String getCsTypeDocument() {
                return documentText.getCsTypeDocument();
            }

            @Override
            public String getCsEditable() {
                return null;
            }

            @Override
            public String getCsDomaine() {
                return documentText.getCsDomaine();
            }

            @Override
            public String getCsDestinataire() {
                return null;
            }

            @Override
            public String getCodeIsoLangue() {
                return null;
            }
        };
        try {
            docuement.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docuement;
    }

    /**
     * Permet de charger un Document
     * 
     * @param textes
     * @return
     */
    private CTDocumentImpl createDocumentForCTDocumentImp(final List<CTDocumentAPIAdapter> textes) {
        // On retourne une implémentation de Document
        CTDocumentImpl documentImp = new CTDocumentImpl(textes);

        return documentImp;
    }

    private ICTTexte createTexte(final CTTexte texte) {
        return new ICTTexte() {

            @Override
            public void setISession(BISession newSession) {
            }

            @Override
            public boolean isNew() {
                return texte.isNew();
            }

            @Override
            public String getLastModifiedUser() {
                return texte.getLastModifiedUser();
            }

            @Override
            public String getLastModifiedTime() {
                return texte.getLastModifiedTime();
            }

            @Override
            public String getLastModifiedDate() {
                return texte.getLastModifiedDate();
            }

            @Override
            public String getId() {
                return texte.getId();
            }

            @Override
            public BISession getISession() {
                return null;
            }

            @Override
            public String getPosition() {
                return texte.getPosition();
            }

            @Override
            public String getNiveau() {
                return texte.getNiveau();
            }

            @Override
            public String getDescriptionBrut() {
                return texte.getDescription();
            }

            @Override
            public String getDescription() {
                return texte.getDescription();
            }

            @Override
            public String getCodeIsoLangue() {
                return texte.getCodeIsoLangue();
            }
        };

    }

}
