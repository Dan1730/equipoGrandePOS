enum Unit {
    items,
    kgs;

    public static String StringIntToString(String stringInt) {
        if(stringInt.equals("0")) {
            return "items";
        }
        else {
            return "kgs";
        }
    }

    public static Unit StringToUnit(String string) {
        if(string == "items") {
            return Unit.items;
        }
        else {
            return Unit.kgs;
        }
    }

    public String toString() {
        if(this == Unit.items) {
            return "items";
        }
        else {
            return "kgs";
        }
    }

    public String toStringInt() {
        String unitInt = "0";
        if(this == Unit.kgs) {
            unitInt = "1";
        }

        return unitInt;
    }
}