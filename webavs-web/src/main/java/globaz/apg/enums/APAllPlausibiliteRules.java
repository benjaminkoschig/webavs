/**
 *
 */
package globaz.apg.enums;

/**
 * @author dde
 */
public enum APAllPlausibiliteRules {

    R_50(false, 50),
    R_51(false, 51),
    R_52(false, 52),
    R_53(false, 53),
    R_54(false, 54),
    R_55(false, 55),
    R_56(false, 56),
    R_57(false, 57),
    R_58(false, 58),
    R_59(false, 59),
    R_60(false, 60),
    R_61(false, 61),
    R_62(false, 62),
    R_63(false, 63),
    R_64(false, 64),
    R_65(false, 65),
    R_66(false, 66),
    R_67(false, 67),
    R_68(false, 68),
    R_104(false, 104),
    R_105(false, 105),
    R_201(false, 201),
    R_202(false, 202),
    R_203(false, 203),
    R_204(false, 204),
    R_205(false, 205),
    R_210(false, 210),
    R_211(false, 211),
    R_300(false, 300),
    R_301(false, 301),
    R_302(false, 302),
    R_303(false, 303),
    R_304(false, 304),
    R_307(false, 307),
    R_309(false, 309),
    R_310(false, 310),
    R_312(false, 312),
    R_314(false, 314),
    R_315(false, 315),
    R_316(false, 316),
    R_317(false, 317),
    R_318(false, 318),
    R_319(false, 319),
    R_321(false, 321),
    R_322(false, 322),
    R_323(false, 323),
    R_324(false, 324),
    R_325(false, 325),
    R_400(false, 400),
    R_401(false, 401),
    R_402(false, 402),
    R_403(false, 403),
    // R�gle plus utilis�e (on garde la classe Rule404 au cas o� elle serait utilis�e � nouveau)
    //R_404(false, 404),
    R_405(false, 405),
    R_406(false, 406),
    R_407(false, 407),
    R_408(false, 408),
    R_409(false, 409),
    R_410(false, 410),
    R_411(false, 411),
    R_412(false, 412),
    R_413(false, 413),
    R_414(false, 414),
    R_416(false, 416),
    R_417(false, 417),
    R_418(false, 418),
    R_419(false, 419),
    R_420(false, 420),
    R_421(false, 421),
    R_422(false, 422),
    R_424(false, 424),
    R_500(true, 500),
    R_501(true, 501),
    R_502(true, 502),
    R_503(true, 503),
    R_504(true, 504),
    R_505(true, 505),
    R_507(true, 507),
    R_508(true, 508),
    R_509(true, 509),
    R_510(true, 510),
    R_511(true, 511),
    R_512(true, 512),
    R_513(true, 513),
    R_1300(false, 1300),
    R_1419(true, 1419),
    R_1509(true, 1509),
    R_2000(true, 2000);

    public static APAllPlausibiliteRules valueOfCode(String code) {
        return APAllPlausibiliteRules.valueOf("R_" + code);
    }

    private final boolean breakable;

    private final int code;

    APAllPlausibiliteRules(boolean breakable, int code) {
        this.breakable = breakable;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getCodeAsString() {
        return String.valueOf(code);
    }

    public boolean isBreakable() {
        return breakable;
    }
}
