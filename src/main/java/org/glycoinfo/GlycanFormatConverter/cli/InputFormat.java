package org.glycoinfo.GlycanFormatConverter.cli;

public enum InputFormat {

    IUPAC_CONDENSED,
    IUPAC_EXTENDED,
    GLYCOCT,
    KCF,
    LINEARCODE,
    WURCS,
    JSON;

    public static InputFormat forInputFormat (String _input) throws Exception {
        switch (_input.toLowerCase()) {
            case "iupac-condensed":
                return IUPAC_CONDENSED;
            case "iupac-extended":
                return IUPAC_EXTENDED;
            case "kcf":
                return KCF;
            case "linearcode":
                return LINEARCODE;
            case "glycoct":
                return GLYCOCT;
            case "wurcs":
                return WURCS;
            case "json":
                return JSON;
            default:
                throw new Exception("This format is unknown format : " + _input);
        }
    }
}
