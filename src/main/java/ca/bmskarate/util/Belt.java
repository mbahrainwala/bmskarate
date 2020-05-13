package ca.bmskarate.util;

public enum Belt {
    White(1),
    Yellow(2),
    Orange(3),
    Green(4),
    Blue(5),
    BlueAdvanced(6),
    Purple(7),
    PurpleAdvanced(8),
    Brown(9),
    BrownAdvanced(10),
    Shodan(11),
    Nidan(12),
    Sandan(13);

    int beltId;
    Belt(int beltId) {
        this.beltId = beltId;
    }

    public int getId(){
        return beltId;
    }
}
