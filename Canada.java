public final class Canada {

    public final static Province Alberta              = new Province("AB", "Alberta");
    public final static Province BritishColumbia      = new Province("BC", "BritishColumbia");
    public final static Province Manitoba             = new Province("MB", "Manitoba");
    public final static Province NewBrunswick         = new Province("NB", "NewBrunswick");
    public final static Province Newfoundland         = new Province("NL", "Newfoundland");
    public final static Province NorthwestTerritories = new Province("NT", "NorthwestTerritories");
    public final static Province NovaScotia           = new Province("NS", "NovaScotia");
    public final static Province Nunavut              = new Province("NU", "Nunavut");
    public final static Province Ontario              = new Province("ON", "Ontario");
    public final static Province PrinceEdwardIsland   = new Province("PE", "PrinceEdwardIsland");
    public final static Province Quebec               = new Province("QC", "Quebec");
    public final static Province Saskatchewan         = new Province("SK", "Saskatchewan");
    public final static Province Yukon                = new Province("YT", "Yukon");

    // List of all the provinces

    public final static Province[] provinces = {
            Alberta,
            BritishColumbia,
            Manitoba,
            NewBrunswick,
            Newfoundland,
            NorthwestTerritories,
            NovaScotia,
            Nunavut,
            Ontario,
            PrinceEdwardIsland,
            Quebec,
            Saskatchewan,
            Yukon
    };

    // Province abbreviations

    public final static Province AB = Alberta;
    public final static Province BC = BritishColumbia;
    public final static Province MB = Manitoba;
    public final static Province NB = NewBrunswick;
    public final static Province NL = Newfoundland;
    public final static Province NT = NorthwestTerritories;
    public final static Province NS = NovaScotia;
    public final static Province NU = Nunavut;
    public final static Province ON = Ontario;
    public final static Province PE = PrinceEdwardIsland;
    public final static Province QC = Quebec;
    public final static Province SK = Saskatchewan;
    public final static Province YT = Yukon;

    // Set the capitals

    private static boolean initialized = false;

    public static void initializeCapitals() {
        if (Canada.initialized) return;
        capital(AB, "Edmonton",       53.5461, -113.4938);
        capital(BC, "Victoria",       48.4284, -123.3656);
        capital(MB, "Winnipeg",       49.8951,  -97.1384);
        capital(NB, "Fredericton",    45.9636,  -66.6431);
        capital(NL, "St. John's",     47.5615,  -52.7126);
        capital(NT, "Yellowknife",    62.4540, -114.3718);
        capital(NS, "Halifax",        44.6488,  -63.5752);
        capital(NU, "Iqaluit",        63.7467,  -68.5170);
        capital(ON, "Toronto",        43.6532,  -79.3832);
        capital(PE, "Charlottetown",  46.2382,  -63.1311);
        capital(QC, "Quebec City",    46.8139,  -71.2080);
        capital(SK, "Regina",         50.4452, -104.6189);
        capital(YT, "Whitehorse",     60.7212, -135.0568);
    }

    static {
        initializeCapitals();
    }

    public static Province find(String name) {
        if ( ! Canada.initialized) initializeCapitals();
        for (Province province : provinces) {
            if (province.name().equals(name) || province.code().equals(name)) return province;
        }
        return null;
    }

    private static void capital(Province province, String name, double longitude, double latitude) {
        province.capital(new City(name, province, longitude, latitude));
    }
}
