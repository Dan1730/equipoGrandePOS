enum Unit {
    items,
    kgs;

    public static String toString(Unit unit) {
        String unitInt = "0";
        if(unit == Unit.kgs) {
            unitInt = "1";
        }

        return unitInt;
    }
}