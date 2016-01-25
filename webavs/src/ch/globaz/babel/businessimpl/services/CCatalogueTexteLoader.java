package ch.globaz.babel.businessimpl.services;

import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSessionUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.exceptions.models.decision.BabelException;

public class CCatalogueTexteLoader {
    private ConcurrentHashMap<String, Map<Langues, List<ICTDocument>>> cache = new ConcurrentHashMap<String, Map<Langues, List<ICTDocument>>>();
    private ICTDocument iTCDocument;

    public CCatalogueTexteLoader() {
        try {
            iTCDocument = PRBabelHelper.getDocumentHelper(BSessionUtil.getSessionFromThreadContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CCatalogueTexteLoader(ICTDocument iTCDocument) throws Exception {
        this.iTCDocument = iTCDocument;
    }

    private ICTDocument[] loadText(String domaine, String csTypeDoc, String nom) throws Exception {
        iTCDocument.setCsTypeDocument(csTypeDoc);
        iTCDocument.setNom(nom);
        iTCDocument.setDefault(Boolean.FALSE);
        iTCDocument.setActif(Boolean.TRUE);
        ICTDocument[] documents;

        try {
            documents = iTCDocument.load();
        } catch (CatalogueTexteException e) {
            throw new CatalogueTexteException("Unable to load the document (nom: " + nom + ",type: "
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csTypeDoc) + " - " + csTypeDoc, e);
        }

        if ((documents == null) || (documents.length == 0)) {
            throw new BabelException("Probleme durant le chargement du  catalogue de texte pour le type type: "
                    + csTypeDoc);
        }
        return documents;
    }

    private Map<Langues, List<ICTDocument>> groupByLangue(ICTDocument[] docuements) {

        Map<Langues, List<ICTDocument>> mapLangues = new ConcurrentHashMap<Langues, List<ICTDocument>>();
        for (Langues langue : Langues.values()) {
            mapLangues.put(langue, new ArrayList<ICTDocument>());
        }
        for (ICTDocument document : docuements) {
            Langues langue = Langues.getLangueDepuisCodeIso(document.getCodeIsoLangue());
            mapLangues.get(langue).add(document);
        }
        return mapLangues;
    }

    private Map<Langues, List<ICTDocument>> loadAndGroupByLangue(String domaine, String csTypeDoc, String nom) {
        try {
            ICTDocument[] docuements = loadText(domaine, csTypeDoc, nom);
            return groupByLangue(docuements);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Langues, List<ICTDocument>> load(String domaine, String csTypeDoc, String nom) throws Exception {
        Checkers.checkNotNull(domaine, "domaine");
        Checkers.checkNotNull(csTypeDoc, "csTypeDoc");
        Checkers.checkNotNull(nom, "nom");
        String key = domaine + "_" + csTypeDoc + "_" + nom;
        if (!cache.contains(key)) {
            cache.put(key, loadAndGroupByLangue(domaine, csTypeDoc, nom));
        }
        return cache.get(key);
    }

    public List<ICTDocument> load(Langues langues, String domaine, String csTypeDoc, String nom) throws Exception {
        Checkers.checkNotNull(langues, "langues");
        return this.load(domaine, csTypeDoc, nom).get(langues);
    }

    public void cleareCache() {
        cache = new ConcurrentHashMap<String, Map<Langues, List<ICTDocument>>>();
    }

}
