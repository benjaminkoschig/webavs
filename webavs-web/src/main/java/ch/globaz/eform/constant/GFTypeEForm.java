package ch.globaz.eform.constant;

import ch.globaz.common.exceptions.NotFoundException;
import globaz.globall.db.BSession;
import javassist.NotFoundException;

import java.util.Arrays;

import java.util.Arrays;

public enum GFTypeEForm {
    F318_370("318.370", "2504", "003700"),
    F318_371("318.371", "2504", "003710"),
    F318_282("318.282", "2504", "002820"),
    F318_750("318.750", "2509", "007500"),
    F318_751("318.751", "2509", "007510"),
    F318_752("318.752", "2509", "007520"),
    F318_747("318.747", "2514", "007470"),
    F318_748("318.748", "2514", "007480"),
    F318_749("318.749", "2514", "007490"),
    F318_180("318.180", "2501", "001800"),
    F318_269("318.269", "2501", "002690"),
    F318_269_1("318.269.1", "2501", "002691"),
    F318_270("318.270", "2513", "002700"),
    F318_270_1("318.270.1", "2513", "002701"),
    F318_744("318.744", "", ""),
    F318_745("318.745", "", ""),
    F318_746("318.746", "", ""),
    F318_386("318.386", "2504", "003860"),
    F318_688("318.688", "", "006880"),
    F318_182("318.182", "2501", "001820"),
    F318_686("318.686", "2506", "006860"),
    F318_260("318.260", "", "");

    String code;
    String messageTypeId;
    String messageSubTypeId;

    GFTypeEForm(String code, String messageTypeId, String messageSubTypeId) {
        this.code = code;
        this.messageTypeId = messageTypeId;
        this.messageSubTypeId = messageSubTypeId;
    }

    public String getCodeEForm() {
        return code;
    }

    public String getMessageTypeId() {
        return messageTypeId;
    }

    public String getMessageSubTypeId() {
        return messageSubTypeId;
    }

    public String getDesignation(BSession session) {
        return session.getLabel("FORMULAIRE_" + code);
    }

    public static GFTypeEForm getGFTypeEForm(String code) throws NotFoundException {
        return Arrays.stream(GFTypeEForm.values())
                .filter(type -> type.getCodeEForm().equals(code))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("formulaire non géré! " + code));
    }

    public static GFTypeEForm getGFTypeEForm(String messageSubTypeId, String messageTypeId) throws NotFoundException {
        return Arrays.stream(GFTypeEForm.values())
                .filter(type -> type.getMessageTypeId().equals(messageTypeId) && type.getMessageSubTypeId().equals(messageSubTypeId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Type et sous type de formulaire non géré! Type : " + messageTypeId + " sous-type : " + messageSubTypeId));
    }
}
