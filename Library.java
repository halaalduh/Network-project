/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Library {
    // library -> (topic -> list of books)
    private final Map<String, Map<String, List<String>>> data = new LinkedHashMap<>();
    // Track reserved books
    private final Set<String> reservedSlots = ConcurrentHashMap.newKeySet();

    public Library() {
        // ===== Main Library =====
        Map<String, List<String>> main = new LinkedHashMap<>();
        main.put("Computer Science", Arrays.asList("Intro to Java", "AI Basics", "Data Structures", "Algorithms", "Networking 101"));
        main.put("Mathematics", Arrays.asList("Linear Algebra", "Calculus I", "Statistics", "Discrete Math", "Probability"));
        main.put("Physics", Arrays.asList("Mechanics", "Thermodynamics", "Quantum Basics", "Electricity", "Waves"));
        main.put("History", Arrays.asList("World History I", "Arab History", "Modern Europe", "Ottoman Empire", "Ancient Civilizations"));
        main.put("Languages", Arrays.asList("English Grammar", "Arabic Morphology", "French A1", "Spanish Basics", "Academic Writing"));
        data.put("Main Library", main);

        // ===== Engineering Library =====
        Map<String, List<String>> eng = new LinkedHashMap<>();
        eng.put("Engineering", Arrays.asList("Civil Basics", "Electrical Circuits", "Mechanical Design", "Materials", "Fluid Mechanics"));
        eng.put("Architecture", Arrays.asList("Urban Design", "Interior Design", "3D Modelling", "Building Codes", "Sustainable Design"));
        eng.put("Computer Science", Arrays.asList("Software Eng", "Operating Systems", "Databases", "Computer Networks", "Cybersecurity"));
        eng.put("Math for Engineers", Arrays.asList("Calculus II", "Differential Eq.", "Numerical Methods", "Transforms", "Optimization"));
        eng.put("Project Mgmt", Arrays.asList("PMI Guide", "Agile Primer", "Risk Mgmt", "Scheduling", "Cost Control"));
        data.put("Engineering Library", eng);

        // ===== Science Library =====
        Map<String, List<String>> sci = new LinkedHashMap<>();
        sci.put("Biology", Arrays.asList("Genetics", "Cell Biology", "Ecology", "Human Anatomy", "Evolution"));
        sci.put("Chemistry", Arrays.asList("Organic Chem", "Inorganic Chem", "Physical Chem", "Lab Safety", "Reactions"));
        sci.put("Physics", Arrays.asList("Modern Physics", "Optics", "Electromagnetism", "Relativity", "Astrophysics"));
        sci.put("Earth Science", Arrays.asList("Geology", "Oceanography", "Meteorology", "Soil Science", "Climate Change"));
        sci.put("Math", Arrays.asList("Set Theory", "Number Theory", "Real Analysis", "Abstract Algebra", "Topology"));
        data.put("Science Library", sci);
    }

    // ===== Basic browsing =====
    public Set<String> getLibraries() {
        return data.keySet();
    }

    public Set<String> getTopics(String library) {
        Map<String, List<String>> topics = data.get(library);
        return topics != null ? topics.keySet() : Collections.emptySet();
    }

    public List<String> getBooks(String library, String topic) {
        Map<String, List<String>> topics = data.get(library);
        if (topics == null) return Collections.emptyList();

        // Return only available books (not reserved)
        List<String> allBooks = topics.getOrDefault(topic, Collections.emptyList());
        List<String> available = new ArrayList<>();

        for (String b : allBooks) {
            String slot = makeSlotId(library, topic, b);
            if (!reservedSlots.contains(slot)) {
                available.add(b);
            }
        }
        return available;
    }

    // ===== Slot helpers =====
    public String makeSlotId(String library, String topic, String book) {
        return library + " | " + topic + " | " + book;
    }

    // ===== Reservation management =====
    public synchronized boolean reserveBook(String library, String topic, String book) {
        String slot = makeSlotId(library, topic, book);
        if (reservedSlots.contains(slot)) return false;
        reservedSlots.add(slot);
        return true;
    }

    public synchronized void freeBook(String slot) {
        reservedSlots.remove(slot);
    }
}
