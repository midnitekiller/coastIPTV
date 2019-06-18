package com.direct2guests.d2g_tv.NonActivity;


public class Constant {
    //public static final String OneSignalGCMSenderId = "873347594067";
    //public static final String OneSignalRESTApiBaseUri = "https://onesignal.com/api/v1/";
    //public static final String OneSignalApplicationId = "9c1798f0-4dbd-48e5-9170-15c4395cdc4f";
    //public static final String OneSignalRESTApiKey = "ZTEyN2RiMGItMGFjZS00MmNkLWJiNzEtMGRiMzUwZmVlZDUz";
    //public static final String ServerAdminUrl = "http://direct2guests.tv/admin/";
    public static final String ServerUrl = "http://178.128.26.46/";
    public static final String ServerUrlLocal = "http://192.168.0.199/";
    public static final String ApiUrl = "http://178.128.26.46/api/";
    public static final String ApiUrlLocal = "http://192.168.0.199/api/";
    public static final String ApiBasePath = "api/";
    public static final String ImgPath = "uploads/";
    public static final String apkPath = "uploads/apk/";
    public static final String ImgPlaces = "media/images/Superadmin/places/";

    public static final String[] airport_names = {"Alabat", "Allah Valley", "Antique", "Bacolod", "Bacon",
            "Bagabag", "Baguio", "Baler", "Bantayan", "Basco", "Biliran", "Bislig", "Borongan", "Bulan",
            "Butuan Bancasi", "Cagayan De Oro", "Cagayan De Sulu", "Calapan","Calbayog", "Camiguin", "Camp Capinpin",
            "Catarman", "Catbalogan", "Caticlan", "Cauayan", "Cotabato", "Cuyo", "Daet", "Daniel z Romualdez",
            "Diosdado Macapagal", "Dipolog", "Dumaguete", "Fernando Airport", "Fort Magsaysay", "Francisco b Reyes",
            "Francisco Banghoy", "Guiuan","Hilongos", "Iba", "Iligan", "Iloilo", "Ipil", "Itbayat", "Jolo", "Jomalig",
            "Kalibo", "Laguindingan", "Laoag", "Legazpi", "Liloy", "Lingayen", "Lubang", "Maasin", "Mactan Cebu",
            "Malabang", "Mamburao", "Marinduque", "Masbate", "Mati", "Naga", "Ninoy Aquino","Ormoc", "Ozamis",
            "Pagadian", "Palanan", "Pinamalayan", "Plaridel", "Puerto Princesa Int'l", "Rajah Buayan Air Base",
            "Romblon", "Rosales", "Roxas", "San Fernando", "San Jose", "Sanga Sanga", "Siargao", "Siocon", "Siquijor",
            "Subic Bay", "Surigao", "Tagbilaran", "Tambler", "Tandag", "Tuguegarao", "Ubay", "Vigan", "Virac", "Wasig",
            "Zamboanga", "Hong Kong Intl", "Shek Kong Airfield", "Changi Rsaf", "Paya Lebar", "Sembawang", "Singapore Changi Intl",
            "Singapore Seletar", "Tengah", "Busan Gimhae Intl", "Cheongju Intl", "Daegu Intl", "Gangneung Airport", "Gunsan",
            "Gwangju", "Jeju Intl", "Jeju Jeongseok", "Jinhae", "Jun Ju Airport", "Jungwon", "Mokpo Airport", "Muan Intl",
            "Osan Ab", "Pohang", "Poseung Helipad", "Secheon", "Seoul Gimpo Intl", "Seoul Incheon Intl", "Seoul Seoul Airbase",
            "Suwan", "Uljin", "Ulsan", "Wonju", "Yangyang Intl", "Yecheon", "Yeosu", "Aguni", "Akeno", "Akita", "Amakusa",
            "Amami", "Aomori", "Asahikawa Civil", "Asahikawa Jsdf g Mil", "Ashiya", "Atsugi", "Chitose", "Chofu",
            "Chubu Centrair Intl", "Fukue", "Fukui", "Fukuoka", "Fukushima", "Futenma", "Gifu", "Hachi Jojima",
            "Hachinohe", "Hakodate", "Hamamatsu", "Hanamaki", "Hateruma", "Hiroshima", "Hiroshimanishi", "Hofu",
            "Hyakuri", "Iejima", "Iki", "Iruma", "Ishigaki", "Iwakuni", "Iwami", "Iwoto", "Izumo", "Kadena", "Kagoshima",
            "Kamigoto", "Kanoya", "Kansai Intl", "Kasumigaura","Kasuminome", "Kerama", "Kikai", "Kisarazu", "Kitadaito",
            "Kitakyushu", "Kobe", "Kochi", "Kohnan", "Komatsu", "Kozushima", "Kumamoto", "Kumejima", "Kushiro",
            "Le Shima Aux Ab Airport", "Matsumoto", "Matsushima", "Matsuyama", "Memanbetsu", "Metabaru", "Miho",
            "Minami Daito", "Misawa", "Miyakejima", "Miyako", "Miyazaki", "Monbetsu", "Nagasaki", "Nagoya", "Naha",
            "Nakashibetsu", "Nanki Shirahama", "Narita Intl", "Niigata", "Niijima", "Noto", "Nyutabaru", "Obihiro",
            "Odate Noshiro", "Oita", "Ojika", "Okayama", "Oki", "Okierabu", "Okushiri", "Ominato", "Omura", "Osaka Intl",
            "Oshima", "Ozuki", "Rebun", "Rishiri", "Sado", "Saga", "Sapporo", "Sapporo", "Sapporo New Chitose", "Sendai",
            "Shimofusa", "Shimojishima", "Shizuhama", "Shizuoka", "Shaonai", "Tachikawa", "Tajima", "Takamatsu", "Tanegashima",
            "Tarama", "Tateyama", "Tokachi", "Tokunoshima", "Tokushima", "Tokyo Intl", "Tottori", "Toyama", "Tsuiki",
            "Tsushima", "Utsunomiya", "Wakkanai", "Yakushima", "Yamagata", "Yamaguchi Ube", "Yao", "Yakota", "Yonaguni", "Yoron", "Zama Kastner"};
    public static final String[] airport_codes = {"RPLY", "RPMA", "RPVS", "RPVB", "RPLZ", "RPUZ", "RPUB", "RPUR", "RPSB", "RPUO", "RPVQ",
            "RPMF", "RPVW", "RPUU", "RPME", "RPML", "RPMU", "RPUK", "RPVC", "RPMH", "RPLM", "RPVF", "RPVY", "RPVE", "RPUY", "RPMC",
            "RPLO", "RPUD", "RPVA", "RPLC", "RPMG", "RPVD", "RPUL", "RPLV", "RPVV", "RPMD", "RPVG", "RPVH", "RPUI", "RPMI", "RPVI",
            "RPMV", "RPLT", "RPMJ", "RPLJ", "RPVK", "RPMY", "RPLI", "RPLP", "RPMX", "RPUG", "RPLU", "RPSM", "RPVM", "RPMM",
            "RPUM", "RPUW", "RPVJ", "RPMQ", "RPUN", "RPLL", "RPVO", "RPMO", "RPMP", "RPLN", "RPLA", "RPUX", "RPVP", "RPMB", "RPVU",
            "RPLR", "RPVR", "RPUS", "RPUH", "RPMN", "RPNS", "RPNO", "RPVZ", "RPLB", "RPMS", "RPVT", "RPMR", "RPMW", "RPUT", "RPSN",
            "RPUQ", "RPUV", "RPLG", "RPMZ", "VHHH", "VHSK", "WSAC", "WSAP", "WSSS", "WSSL", "WSAT", "RKPK", "RKTU", "RKTN", "RKNN",
            "RKJK", "RKJJ", "RKPC", "RKPD", "RKPE", "RKJU", "RKTI", "RKJM", "RKJB", "RKSO", "RKTH", "RKBN", "RKPS", "RKSS", "RKSI",
            "RKSM", "RKSW", "RKTL", "RKPU", "RKNW", "RKNY", "RKTY", "RKJY", "RORA", "RJOE", "RJSK", "RJDA", "RJKA", "RJSA", "RJEC",
            "RJCA", "RJFA", "RJTA", "RJCJ", "RJTF", "RJGG", "RJFE", "RJNF", "RJFF", "RJSF", "ROTM", "RJNG", "RJTH", "RJSH", "RJCH",
            "RJNH", "RJSI", "RORH", "RJOA", "RJBH", "RJOF", "RJAH", "RORE", "RJDB", "RJTJ", "ROIG", "RJOI", "RJOW", "RJAW", "RJOC",
            "RODN", "RJFK", "RJDK", "RJFY", "RJBB", "RJAK", "RJSU", "ROKR", "RJKI", "RJTK", "RORK", "RJFR", "RJBE", "RJOK", "RJBK",
            "RJNK", "RJAZ", "RJFT", "ROKJ", "RJCK", "RODE", "RJAF", "RJST", "RJOM", "RJOM", "RJCM", "RJDM", "RJOH", "ROMD", "RJSM",
            "RJTQ", "ROMY", "RJFM", "RJEB", "RJFU", "RJNA", "ROAH", "RJCN", "RJBD", "RJAA", "RJSN", "RJAN", "RJNW", "RJFN", "RJCB",
            "RJSR", "RJFO", "RJDO", "RJOB", "RJNO", "RJKB", "RJEO", "RJSO", "RJDU", "RJOO", "RJTO", "RJOZ", "RJCR", "RJER", "RJSD",
            "RJFS", "RJCO", "RJCC", "RJSS", "RJTL", "RORS", "RJNY", "RJNS", "RJSY", "RJTC", "RJBT", "RJOT", "RJFG", "RORT", "RJTE",
            "RJCT", "RJKN", "RJOS", "RJTI", "RJTT", "RJOR", "RJNT", "RJFZ", "RJDT", "RJTU", "RJCW", "RJFC", "RJSC", "RJDC", "RJOY",
            "RJTY", "ROYN", "RORY", "RJTR"};
}
