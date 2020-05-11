package ca.bmskarate.util;

public enum Belt {
    WHITE(0);

    int beltId;
    Belt(int beltId) {
        this.beltId = beltId;
    }

    public int getId(){
        return beltId;
    }
}
