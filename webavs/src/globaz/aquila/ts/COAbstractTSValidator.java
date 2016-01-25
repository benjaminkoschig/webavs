package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Validation/éxécution d'une suite de "rules" pour un élément contentieux.<br/>
 * Exemple : Pour FER la validation va testé la longueur et caractères contenu dans la nom d'un tiers.
 * 
 * @author DDA
 */
public abstract class COAbstractTSValidator implements COTSValidator {

    private List rules;

    /**
     * Ajoute une régle de validation à celles gérées par ce validateur.
     * 
     * @param validationRule
     */
    protected void addValidationRule(COTSValidationRule validationRule) {
        if (rules == null) {
            rules = new LinkedList();
        }

        rules.add(validationRule);
    }

    /**
     * Remplace la liste des règles de validation par celles transmises dans la liste.
     * 
     * @param rules
     *            une liste de {@link COTSValidationRule} non nulle
     */
    protected void setRules(List rules) {
        this.rules = rules;
    }

    /**
     * @see COTSValidator#validate(COContentieux, COTransition)
     */
    @Override
    public COElementJournalBatch validate(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransition transition) {
        COElementJournalBatch elementJournalBatch = new COElementJournalBatch(contentieux, transition);

        for (Iterator ruleIter = rules.iterator(); ruleIter.hasNext();) {
            ((COTSValidationRule) ruleIter.next()).validate(session, transaction, contentieux, transition,
                    elementJournalBatch);
        }

        return elementJournalBatch;
    }

    /**
     * @see COTSValidator#validationRules()
     */
    @Override
    public List validationRules() {
        if ((rules == null) || rules.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return Collections.unmodifiableList(rules);
    }
}
