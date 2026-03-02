package org.example.trade_bridge_26668.service;

import org.example.trade_bridge_26668.model.*;
import org.example.trade_bridge_26668.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RwandaDataInitializer implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (locationRepository.count() > 0) {
            System.out.println("Database already populated. Skipping initialization.");
            return;
        }

        System.out.println("Initializing complete Rwanda administrative structure...");

        // KIGALI CITY
        Location kigali = createLocation("KGL", "Kigali", LocationEnum.PROVINCE, null);
        
        // GASABO DISTRICT (15 sectors)
        Location gasabo = createLocation("GSB", "Gasabo", LocationEnum.DISTRICT, kigali);
        createSectorWithCellsVillages(gasabo, "BUM", "Bumbogo");
        createSectorWithCellsVillages(gasabo, "GAT", "Gatsata");
        createSectorWithCellsVillages(gasabo, "JAL", "Jali");
        createSectorWithCellsVillages(gasabo, "GIK", "Gikomero");
        createSectorWithCellsVillages(gasabo, "GIS", "Gisozi");
        createSectorWithCellsVillages(gasabo, "JAB", "Jabana");
        createSectorWithCellsVillages(gasabo, "KIN", "Kinyinya");
        createSectorWithCellsVillages(gasabo, "NDE", "Ndera");
        createSectorWithCellsVillages(gasabo, "NDU", "Nduba");
        createSectorWithCellsVillages(gasabo, "RUS", "Rusororo");
        createSectorWithCellsVillages(gasabo, "RUT", "Rutunga");
        createSectorWithCellsVillages(gasabo, "KAC", "Kacyiru");
        createSectorWithCellsVillages(gasabo, "KIM", "Kimihurura");
        createSectorWithCellsVillages(gasabo, "KMR", "Kimironko");
        createSectorWithCellsVillages(gasabo, "REM", "Remera");

        // KICUKIRO DISTRICT (10 sectors)
        Location kicukiro = createLocation("KCK", "Kicukiro", LocationEnum.DISTRICT, kigali);
        createSectorWithCellsVillages(kicukiro, "GAH", "Gahanga");
        createSectorWithCellsVillages(kicukiro, "GAE", "Gatenga");
        createSectorWithCellsVillages(kicukiro, "GKO", "Gikondo");
        createSectorWithCellsVillages(kicukiro, "KAG", "Kagarama");
        createSectorWithCellsVillages(kicukiro, "KAN", "Kanombe");
        createSectorWithCellsVillages(kicukiro, "KCU", "Kicukiro");
        createSectorWithCellsVillages(kicukiro, "KGA", "Kigarama");
        createSectorWithCellsVillages(kicukiro, "MAS", "Masaka");
        createSectorWithCellsVillages(kicukiro, "NIB", "Niboye");
        createSectorWithCellsVillages(kicukiro, "NYG", "Nyarugunga");

        // NYARUGENGE DISTRICT (10 sectors)
        Location nyarugenge = createLocation("NYR", "Nyarugenge", LocationEnum.DISTRICT, kigali);
        createSectorWithCellsVillages(nyarugenge, "GIT", "Gitega");
        createSectorWithCellsVillages(nyarugenge, "KNY", "Kanyinya");
        createSectorWithCellsVillages(nyarugenge, "KGL2", "Kigali");
        createSectorWithCellsVillages(nyarugenge, "KMS", "Kimisagara");
        createSectorWithCellsVillages(nyarugenge, "MAG", "Mageragere");
        createSectorWithCellsVillages(nyarugenge, "MUH", "Muhima");
        createSectorWithCellsVillages(nyarugenge, "NYK", "Nyakabanda");
        createSectorWithCellsVillages(nyarugenge, "NYM", "Nyamirambo");
        createSectorWithCellsVillages(nyarugenge, "RWE", "Rwezamenyo");
        createSectorWithCellsVillages(nyarugenge, "NYA", "Nyarugenge");

        // NORTHERN PROVINCE
        Location northern = createLocation("NTH", "Northern", LocationEnum.PROVINCE, null);
        
        Location burera = createLocation("BUR", "Burera", LocationEnum.DISTRICT, northern);
        createSectorsWithCells(burera, new String[]{"BUN", "BUT", "CYA", "CYE", "GAH", "GAT", "GTO", "KAG", "KIN", "KNY", "KIV", "NEM", "RUG", "RGD", "RHD", "RSB", "RWE"}, 
                                        new String[]{"Bungwe", "Butaro", "Cyanika", "Cyeru", "Gahunga", "Gatebe", "Gitovu", "Kagogo", "Kinoni", "Kinyababa", "Kivuye", "Nemba", "Rugarama", "Rugendabari", "Ruhunde", "Rusarabuge", "Rwerere"});

        Location gakenke = createLocation("GAK", "Gakenke", LocationEnum.DISTRICT, northern);
        createSectorsWithCells(gakenke, new String[]{"BUS", "COK", "CYB", "GAK", "GAS", "MUG", "JAN", "KAM", "KAR", "KVG", "MAT", "MIN", "MHD", "MYG", "MUZ", "NMB", "RUL", "RSS", "RSH"},
                                        new String[]{"Busengo", "Coko", "Cyabingo", "Gakenke", "Gashenyi", "Mugunga", "Janja", "Kamubuga", "Karambo", "Kivuruga", "Mataba", "Minazi", "Muhondo", "Muyongwe", "Muzo", "Nemba", "Ruli", "Rusasa", "Rushashi"});

        Location gicumbi = createLocation("GCM", "Gicumbi", LocationEnum.DISTRICT, northern);
        createSectorsWithCells(gicumbi, new String[]{"BUK", "BWI", "BYM", "CYU", "GIT", "KNG", "MNY", "MIY", "KGY", "MKR", "MUK", "MUT", "NYM", "NYK", "RBY", "RKM", "RSK", "RTA", "RVN", "RWM", "SHG"},
                                        new String[]{"Bukure", "Bwisige", "Byumba", "Cyumba", "Giti", "Kaniga", "Manyagiro", "Miyove", "Kageyo", "Mukarange", "Muko", "Mutete", "Nyamiyaga", "Nyankenke II", "Rubaya", "Rukomo", "Rushaki", "Rutare", "Ruvune", "Rwamiko", "Shangasha"});

        Location musanze = createLocation("MSZ", "Musanze", LocationEnum.DISTRICT, northern);
        createSectorsWithCells(musanze, new String[]{"BSG", "CYV", "GAC", "GSK", "GTR", "KMN", "KNG", "MHZ", "MKO", "MSZ", "NKT", "NYG", "RMR", "RWZ", "SHG"},
                                        new String[]{"Busogo", "Cyuve", "Gacaca", "Gashaki", "Gataraga", "Kimonyi", "Kinigi", "Muhoza", "Muko", "Musanze", "Nkotsi", "Nyange", "Remera", "Rwaza", "Shingiro"});

        Location rulindo = createLocation("RLD", "Rulindo", LocationEnum.DISTRICT, northern);
        createSectorsWithCells(rulindo, new String[]{"BAS", "BRG", "BSK", "BYG", "CYZ", "CYG", "KNH", "KSR", "MSR", "MBG", "MRM", "NGM", "NTR", "RKZ", "RSG", "SHY", "TMB"},
                                        new String[]{"Base", "Burega", "Bushoki", "Buyoga", "Cyinzuzi", "Cyungo", "Kinihira", "Kisaro", "Masoro", "Mbogo", "Murambi", "Ngoma", "Ntarabana", "Rukozo", "Rusiga", "Shyorongi", "Tumba"});

        // EASTERN PROVINCE
        Location eastern = createLocation("EST", "Eastern", LocationEnum.PROVINCE, null);
        
        Location bugesera = createLocation("BGS", "Bugesera", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(bugesera, new String[]{"GSH", "JUR", "KMB", "NTR", "MRB", "MYG", "MSN", "MWG", "NGR", "NYM", "NYR", "RLM", "RHH", "RWR", "SHY"},
                                        new String[]{"Gashora", "Juru", "Kamabuye", "Ntarama", "Mareba", "Mayange", "Musenyi", "Mwogo", "Ngeruka", "Nyamata", "Nyarugenge", "Rilima", "Ruhuha", "Rweru", "Shyara"});

        Location gatsibo = createLocation("GTS", "Gatsibo", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(gatsibo, new String[]{"GSG", "GTS", "GTK", "KBR", "KGY", "KRM", "KZG", "MHR", "MRM", "NGR", "NYG", "RMR", "RGR", "RWM"},
                                        new String[]{"Gasange", "Gatsibo", "Gitoki", "Kabarore", "Kageyo", "Kiramuruzi", "Kiziguro", "Muhura", "Murambi", "Ngarama", "Nyagihanga", "Remera", "Rugarama", "Rwimbogo"});

        Location kayonza = createLocation("KYZ", "Kayonza", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(kayonza, new String[]{"GHN", "KBR", "KBD", "MKR", "MRM", "MRD", "MWR", "NDG", "NYM", "RKR", "RRM", "RWK"},
                                        new String[]{"Gahini", "Kabare", "Kabarondo", "Mukarange", "Murama", "Murundi", "Mwiri", "Ndego", "Nyamirama", "Rukara", "Ruramira", "Rwinkwavu"});

        Location kirehe = createLocation("KRH", "Kirehe", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(kirehe, new String[]{"GHR", "GTR", "KGR", "KGN", "KRH", "MHM", "MPG", "MSZ", "MSH", "NSH", "NYM", "NYR"},
                                        new String[]{"Gahara", "Gatore", "Kigarama", "Kigina", "Kirehe", "Mahama", "Mpanga", "Musaza", "Mushikiri", "Nasho", "Nyamugari", "Nyarubuye"});

        Location ngoma = createLocation("NGM", "Ngoma", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(ngoma, new String[]{"GSH", "JRM", "KRM", "KZO", "KBG", "MGS", "MRM", "MTD", "RMR", "RKR", "RKM", "RRG", "SAK", "ZAZ"},
                                        new String[]{"Gashanda", "Jarama", "Karembo", "Kazo", "Kibungo", "Mugesera", "Murama", "Mutenderi", "Remera", "Rukira", "Rukumberi", "Rurenge", "Sake", "Zaza"});

        Location nyagatare = createLocation("NYG", "Nyagatare", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(nyagatare, new String[]{"GTD", "KYM", "KRM", "KRG", "KTB", "MTM", "MMU", "MKM", "MSH", "NYG", "RKM", "RWM", "RWM", "TBG"},
                                        new String[]{"Gatunda", "Kiyombe", "Karama", "Karangazi", "Katabagemu", "Matimba", "Mimuli", "Mukama", "Musheli", "Nyagatare", "Rukomo", "Rwempasha", "Rwimiyaga", "Tabagwe"});

        Location rwamagana = createLocation("RWM", "Rwamagana", LocationEnum.DISTRICT, eastern);
        createSectorsWithCells(rwamagana, new String[]{"FMB", "GHG", "GSH", "KRG", "KGB", "MHZ", "MNY", "MNG", "MSH", "MYM", "MWL", "NYK", "NZG", "RBN"},
                                        new String[]{"Fumbwe", "Gahengeri", "Gishari", "Karenge", "Kigabiro", "Muhazi", "Munyaga", "Munyiginya", "Musha", "Muyumbu", "Mwulire", "Nyakariro", "Nzige", "Rubona"});

        // SOUTHERN PROVINCE
        Location southern = createLocation("STH", "Southern", LocationEnum.PROVINCE, null);
        
        Location gisagara = createLocation("GSG", "Gisagara", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(gisagara, new String[]{"GKN", "GSH", "KNS", "KBR", "KGM", "MMB", "MGZ", "MGM", "MKD", "MSH", "NDR", "NYZ", "SAV"},
                                        new String[]{"Gikonko", "Gishubi", "Kansi", "Kibirizi", "Kigembe", "Mamba", "Muganza", "Mugombwa", "Mukindo", "Musha", "Ndora", "Nyanza", "Save"});

        Location huye = createLocation("HYE", "Huye", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(huye, new String[]{"GSH", "KRM", "KGM", "KNZ", "MRB", "MBZ", "MKR", "NGM", "RHS", "RST", "RWN", "SMB", "TMB", "HYE"},
                                        new String[]{"Gishamvu", "Karama", "Kigoma", "Kinazi", "Maraba", "Mbazi", "Mukura", "Ngoma", "Ruhashya", "Rusatira", "Rwaniro", "Simbi", "Tumba", "Huye"});

        Location kamonyi = createLocation("KMN", "Kamonyi", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(kamonyi, new String[]{"GCR", "KRM", "KYZ", "KYU", "MGN", "MSM", "NGB", "NYM", "NYR", "RGR", "RKM", "RND"},
                                        new String[]{"Gacurabwenge", "Karama", "Kayenzi", "Kayu", "Mugina", "Musambira", "Ngamba", "Nyamiyaga", "Nyarubaka", "Rugarika", "Rukoma", "Runda"});

        Location muhanga = createLocation("MHG", "Muhanga", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(muhanga, new String[]{"CYZ", "KBC", "KBG", "KYM", "MHG", "MSH", "NYB", "NYM", "NYR", "RNG", "RGD", "SHG"},
                                        new String[]{"Cyeza", "Kabacuzi", "Kibangu", "Kiyumba", "Muhanga", "Mushishiro", "Nyabinoni", "Nyamabuye", "Nyarusange", "Rongi", "Rugendabari", "Shyogwe"});

        Location nyamagabe = createLocation("NYM", "Nyamagabe", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(nyamagabe, new String[]{"BRH", "CYN", "GSK", "GTR", "KDH", "KMG", "KBR", "KBM", "KTB", "MBZ", "MGN", "MSG", "MSB", "MSH", "NKM", "GSK", "TAR"},
                                        new String[]{"Buruhukiro", "Cyanika", "Gasaka", "Gatare", "Kaduha", "Kamegeli", "Kibirizi", "Kibumbwe", "Kitabi", "Mbazi", "Mugano", "Musange", "Musebeya", "Mushubi", "Nkomane", "Gasaka", "Tare"});

        Location nyanza = createLocation("NYZ", "Nyanza", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(nyanza, new String[]{"BSS", "BSR", "CYB", "KBR", "KGM", "MKG", "MYR", "NTZ", "NYG", "RWB"},
                                        new String[]{"Busasamana", "Busoro", "Cyabakamyi", "Kibirizi", "Kigoma", "Mukingo", "Muyira", "Ntyazo", "Nyagisozi", "Rwabicuma"});

        Location nyaruguru = createLocation("NYR", "Nyaruguru", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(nyaruguru, new String[]{"CYH", "BSZ", "KBH", "MAT", "MGZ", "MNN", "KVU", "NGR", "NGM", "NYB", "NYG", "RHR", "RRM", "RSG"},
                                        new String[]{"Cyahinda", "Busanze", "Kibeho", "Mata", "Muganza", "Munini", "Kivu", "Ngera", "Ngoma", "Nyabimata", "Nyagisozi", "Ruheru", "Ruramba", "Rusenge"});

        Location ruhango = createLocation("RHG", "Ruhango", LocationEnum.DISTRICT, southern);
        createSectorsWithCells(ruhango, new String[]{"BWR", "BYM", "KBG", "KNZ", "KNH", "MBY", "MWD", "NTG", "RHG"},
                                        new String[]{"Bweramana", "Byimana", "Kabagari", "Kinazi", "Kinihira", "Mbuye", "Mwendo", "Ntongwe", "Ruhango"});

        // WESTERN PROVINCE
        Location western = createLocation("WST", "Western", LocationEnum.PROVINCE, null);
        
        Location karongi = createLocation("KRG", "Karongi", LocationEnum.DISTRICT, western);
        createSectorsWithCells(karongi, new String[]{"BWS", "GSH", "GSR", "GTS", "MBG", "MRM", "MRD", "MTT", "RBG", "RGB", "RGD", "RWK", "TWM"},
                                        new String[]{"Bwishyura", "Gishyita", "Gishari", "Gitesi", "Mubuga", "Murambi", "Murundi", "Mutuntu", "Rubengera", "Rugabano", "Ruganda", "Rwankuba", "Twumba"});

        Location ngororero = createLocation("NGR", "Ngororero", LocationEnum.DISTRICT, western);
        createSectorsWithCells(ngororero, new String[]{"BWR", "GTM", "HND", "KBY", "KGY", "KVM", "MTZ", "MHD", "MHR", "NDR", "NGR", "NYG", "SOV"},
                                        new String[]{"Bwira", "Gatumba", "Hindiro", "Kabaya", "Kageyo", "Kavumu", "Matyazo", "Muhanda", "Muhororo", "Ndaro", "Ngororero", "Nyange", "Sovu"});

        Location nyabihu = createLocation("NYB", "Nyabihu", LocationEnum.DISTRICT, western);
        createSectorsWithCells(nyabihu, new String[]{"BGG", "JND", "JMB", "KBT", "KRG", "KNT", "MKM", "MRG", "RMB", "RGR", "RRM", "SHR"},
                                        new String[]{"Bigogwe", "Jenda", "Jomba", "Kabatwa", "Karago", "Kintobo", "Mukamira", "Muringa", "Rambura", "Rugera", "Rurembo", "Shyira"});

        Location nyamasheke = createLocation("NYM", "Nyamasheke", LocationEnum.DISTRICT, western);
        createSectorsWithCells(nyamasheke, new String[]{"BSK", "BSG", "CYT", "GHM", "KGN", "KNJ", "KRM", "KRG", "KRM", "MCB", "MHM", "NYB", "RNG", "RHR", "SHG"},
                                        new String[]{"Bushekeri", "Bushenge", "Cyato", "Gihombo", "Kagano", "Kanjongo", "Karambi", "Karengera", "Kirimbi", "Macuba", "Mahembe", "Nyabitekeri", "Rangiro", "Ruharambuga", "Shangi"});

        Location rubavu = createLocation("RBV", "Rubavu", LocationEnum.DISTRICT, western);
        createSectorsWithCells(rubavu, new String[]{"BGS", "BSS", "CYZ", "GSN", "KNM", "KNZ", "MDD", "NYK", "NYM", "NYD", "RBV", "RGR"},
                                        new String[]{"Bugeshi", "Busasamana", "Cyanzarwe", "Gisenyi", "Kanama", "Kanzenze", "Mudende", "Nyakiriba", "Nyamyumba", "Nyundo", "Rubavu", "Rugerero"});

        Location rusizi = createLocation("RSZ", "Rusizi", LocationEnum.DISTRICT, western);
        createSectorsWithCells(rusizi, new String[]{"BGR", "BTR", "BWY", "GSH", "GHK", "GHD", "GKD", "GTM", "KMB", "MGZ", "MRR", "NKK", "NKM", "NKG", "NYK", "NYK", "NZH", "RWM"},
                                        new String[]{"Bugarama", "Butare", "Bweyeye", "Gashonga", "Giheke", "Gihundwe", "Gikundamvura", "Gitambi", "Kamembe", "Muganza", "Mururu", "Nkanka", "Nkombo", "Nkungu", "Nyakabuye", "Nyakarenzo", "Nzahaha", "Rwimbogo"});

        Location rutsiro = createLocation("RTS", "Rutsiro", LocationEnum.DISTRICT, western);
        createSectorsWithCells(rutsiro, new String[]{"BNZ", "GHG", "KGY", "KVM", "MNH", "MKR", "MRD", "MSS", "MSH", "MSB", "NYB", "RHG", "RSB"},
                                        new String[]{"Boneza", "Gihango", "Kigeyo", "Kivumu", "Manihira", "Mukura", "Murunda", "Musasa", "Mushonyi", "Mushubati", "Nyabirasi", "Ruhango", "Rusebeya"});

        System.out.println("Complete Rwanda administrative structure initialized!");
        
        // Create sample users
        createSampleUsers(kigali, gasabo, kicukiro, nyarugenge, northern, eastern, southern, western);
        
        System.out.println("Sample users created successfully!");
    }

    private Location createLocation(String code, String name, LocationEnum type, Location parent) {
        Location location = new Location();
        location.setStructureCode(code);
        location.setStructureName(name);
        location.setStructureType(type);
        location.setParent(parent);
        return locationRepository.save(location);
    }

    private void createSectorWithCellsVillages(Location district, String code, String name) {
        Location sector = createLocation(code, name, LocationEnum.SECTOR, district);
        Location cell1 = createLocation(code + "C1", name + " Cell 1", LocationEnum.CELL, sector);
        Location cell2 = createLocation(code + "C2", name + " Cell 2", LocationEnum.CELL, sector);
        createLocation(code + "V1", name + " Village 1", LocationEnum.VILLAGE, cell1);
        createLocation(code + "V2", name + " Village 2", LocationEnum.VILLAGE, cell1);
        createLocation(code + "V3", name + " Village 3", LocationEnum.VILLAGE, cell2);
        createLocation(code + "V4", name + " Village 4", LocationEnum.VILLAGE, cell2);
    }

    private void createSectorsWithCells(Location district, String[] codes, String[] names) {
        for (int i = 0; i < codes.length; i++) {
            Location sector = createLocation(codes[i], names[i], LocationEnum.SECTOR, district);
            Location cell1 = createLocation(codes[i] + "C1", names[i] + " Cell 1", LocationEnum.CELL, sector);
            Location cell2 = createLocation(codes[i] + "C2", names[i] + " Cell 2", LocationEnum.CELL, sector);
            createLocation(codes[i] + "V1", names[i] + " Village 1", LocationEnum.VILLAGE, cell1);
            createLocation(codes[i] + "V2", names[i] + " Village 2", LocationEnum.VILLAGE, cell2);
        }
    }

    private void createSampleUsers(Location... locations) {
        String[] firstNames = {"Uwase", "Mugisha", "Kalisa", "Niyonzima", "Habimana", 
                               "Mutoni", "Ishimwe", "Gasana", "Uwineza", "Nkusi",
                               "Imena", "Hirwa", "Umutoni", "Bizimana", "Nsengimana"};
        String[] lastNames = {"Jean", "Marie", "Claude", "Aline", "Patrick", 
                             "Grace", "Eric", "Diane", "Samuel", "Francine",
                             "David", "Alice", "Emmanuel", "Rose", "Joseph"};
        UserEnum[] userTypes = {UserEnum.CLIENT, UserEnum.SUPPLIER, UserEnum.INDUSTRY_WORKER};

        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setUserType(userTypes[i % 3]);
            user.setUsername(firstNames[i].toLowerCase() + "_" + lastNames[i].toLowerCase());
            user.setFirstName(firstNames[i]);
            user.setLastName(lastNames[i]);
            user.setEmail(firstNames[i].toLowerCase() + "." + lastNames[i].toLowerCase() + "@example.com");
            user.setPhoneNumber("078" + String.format("%07d", 1000000 + i));
            user.setLocation(locations[i % locations.length]);
            if (i % 3 == 1) {
                user.setCompanyName(firstNames[i] + " " + lastNames[i] + " Ltd");
            }
            userRepository.save(user);
        }
    }
}
