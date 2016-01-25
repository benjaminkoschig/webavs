package ch.globaz.common.document.babel;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.jade.business.models.Langues;

public class TextGiverBabel implements TextGiver<BabelTextDefinition> {
    private ICTDocument document;
    private BSession session;
    private Map<Langues, ICTDocument> mapDocuement = new HashMap<Langues, ICTDocument>();
    private Langues langue;

    public TextGiverBabel(ICTDocument document) {
        Checkers.checkNotNull(document, "document");
        session = BSessionUtil.getSessionFromThreadContext();
        Checkers.checkNotNull(session, "la session n'a pas été trouvé dans le thread context");
        this.document = document;
    }

    public TextGiverBabel(ICTDocument document, BSession session) {
        Checkers.checkNotNull(document, "document");
        Checkers.checkNotNull(session, "session");
        this.document = document;
        this.session = session;
        langue = Langues.getLangueDepuisCodeIso(session.getIdLangueISO());
    }

    public TextGiverBabel(Map<Langues, ICTDocument> mapDocuement, BSession session) {
        Checkers.checkNotNull(mapDocuement, "mapDocuement");
        Checkers.checkNotNull(session, "session");
        this.mapDocuement = mapDocuement;
        this.session = session;
        langue = Langues.getLangueDepuisCodeIso(session.getIdLangueISO());
    }

    public String resolveText(BabelTextDefinition definition, ICTDocument document) {
        Checkers.checkNotNull(definition, "definition");
        try {
            // si on a définit un valeur forcée on ne passe pas par le mécanisme de recherche dans le catalogue de texte
            // et on retourne la valeur forcée
            if (definition.isForcedValue()) {
                return definition.getValue();
            }
            ICTTexte template = document.getTextes(definition.getNiveau()).getTexte(definition.getPosition());
            return template.getDescription();
        } catch (IndexOutOfBoundsException e) {
            String domaine = session.getCodeLibelle(document.getCsDomaine());
            String typeDocument = session.getCodeLibelle(document.getCsTypeDocument());

            throw new CommonTechnicalException("La description pour la clé " + definition.getKey()
                    + " n'est pas définit dans le catalogue de texte. Domaine:" + domaine + ", Type de document:"
                    + typeDocument + ", Nom:" + document.getNom() + ", Niveau:" + definition.getNiveau()
                    + ", Position:" + definition.getPosition() + " -> Description: " + definition.getDescription());
        }
    }

    @Override
    public String resolveText(BabelTextDefinition definition) {
        Checkers.checkNotNull(definition, "definition");
        if (mapDocuement.isEmpty()) {
            return this.resolveText(definition, document);
        } else {
            return this.resolveText(definition, langue);
        }
    }

    @Override
    public String resolveText(BabelTextDefinition definition, String langue) {

        Langues langues = resolveLangue(langue);
        ICTDocument document1 = mapDocuement.get(langues);

        if (document1 == null) {
            String domaine = session.getCodeLibelle(document.getCsDomaine());
            String typeDocument = session.getCodeLibelle(document.getCsTypeDocument());
            throw new CommonTechnicalException("La langue " + langues + " n'a été chargée. Domaine:" + domaine
                    + ", Type de document:" + typeDocument + ", Nom:" + document.getNom() + ", Niveau:"
                    + definition.getNiveau() + ", Position:" + definition.getPosition() + " -> Description: "
                    + definition.getDescription());
        }
        return this.resolveText(definition, document1);
    }

    @Override
    public String resolveText(BabelTextDefinition definition, Langues langues) {
        Checkers.checkNotNull(langues, "langues");
        ICTDocument document1 = mapDocuement.get(langues);
        if (document1 == null) {
            String domaine = session.getCodeLibelle(document.getCsDomaine());
            String typeDocument = session.getCodeLibelle(document.getCsTypeDocument());
            throw new CommonTechnicalException("La lanugue " + langues + " n'a été chargée. Domaine:" + domaine
                    + ", Type de document:" + typeDocument + ", Nom:" + document.getNom() + ", Niveau:"
                    + definition.getNiveau() + ", Position:" + definition.getPosition() + " -> Description: "
                    + definition.getDescription());
        }
        return this.resolveText(definition, document1);
    }

    @Override
    public void setLangue(String langue) {
        Langues langues = resolveLangue(langue);
        this.langue = langues;
    }

    private Langues resolveLangue(String langue) {
        Langues langues = null;
        Checkers.checkNotNull(langue, "langues");
        if ("503001".equals(langue)) {
            langues = Langues.getLangueDepuisCodeIso("fr");
        } else if ("503002".equals(langue)) {
            langues = Langues.getLangueDepuisCodeIso("de");
        } else if ("503004".equals(langue)) {
            langues = Langues.getLangueDepuisCodeIso("it");
        } else {
            langues = Langues.getLangueDepuisCodeIso(langue);
            if (langues == null) {
                throw new CommonTechnicalException("Imposible à détérminer la langue pour cette valeur:" + langue);
            }
        }
        return langues;
    }

}
