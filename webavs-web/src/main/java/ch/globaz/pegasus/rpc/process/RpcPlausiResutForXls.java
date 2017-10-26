package ch.globaz.pegasus.rpc.process;

import java.lang.reflect.Type;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.PlausiResult;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class RpcPlausiResutForXls {
    private static final Gson gson = createGson();

    private final PlausiResult result;
    private final String description;

    public RpcPlausiResutForXls(PlausiResult result, String desciption) {
        this.result = result;
        this.description = desciption;
    }

    @ColumnStyle(width = "30%", align = Align.LEFT)
    @Column(order = 1, name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    @Column(order = 2, name = "TYPE")
    public RpcPlausiType getType() {
        return result.getPlausi().getType();
    }

    @Column(order = 3, name = "ID")
    public String getID() {
        return result.getPlausi().getID();
    }

    @Column(order = 4, name = "REFERANCE")
    public String getReferance() {
        return result.getPlausi().getReferance();
    }

    @Column(order = 5, name = "CATEGORY")
    public RpcPlausiCategory getCategory() {
        return result.getPlausi().getCategory();
    }

    @Column(order = 6, name = "DATAS")
    public String getData() {
        return gson.toJson(result);
    }

    private static Gson createGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Montant.class, new com.google.gson.JsonSerializer<Montant>() {

                    @Override
                    public JsonElement serialize(Montant src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.getValueDouble());
                    }
                }).create();
    }
}
