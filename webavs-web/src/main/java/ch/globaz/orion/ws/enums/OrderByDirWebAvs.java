package ch.globaz.orion.ws.enums;

public enum OrderByDirWebAvs {
    ASC("Asc"),
    DESC("Desc");

    private String text;

    private OrderByDirWebAvs(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
