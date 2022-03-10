package ch.globaz.eform.constant;

import globaz.globall.db.BSession;

public enum GFTypeEForm {
    F318_370("318.370"),
    F318_371("318.371"),
    F318_282("318.282"),
    F318_750("318.750"),
    F318_751("318.751"),
    F318_752("318.752"),
    F318_747("318.747"),
    F318_748("318.748"),
    F318_749("3181749"),
    F318_180("318.180"),
    F318_269("318.269"),
    F318_269_1("318.269.1"),
    F318_270("318.270"),
    F318_270_1("318.270.1"),
    F318_386("318.386"),
    F318_182("318.182"),
    F318_686("318.686");

    String code;

    GFTypeEForm(String code) {
        this.code = code;
    }

    public String getCodeEForm() {
        return code;
    }

    public String getDesignation(BSession session) {
        return session.getLabel("FORMULAIRE_" + code);
    }
}
