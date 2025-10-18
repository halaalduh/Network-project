/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;
import java.util.ArrayList;
import java.util.Arrays;

public class Library {

    // Lists to store libraries, topics, and books
    private final ArrayList<String> libraries = new ArrayList<>();
    private final ArrayList<ArrayList<String>> topics = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<String>>> books = new ArrayList<>();

    public Library() {
        // ==== Main Library ====
        libraries.add("Main Library");

        ArrayList<String> mainTopics = new ArrayList<>(Arrays.asList(
            "Computer Science", "Mathematics", "Physics", "History", "Languages"
        ));

        ArrayList<ArrayList<String>> mainBooks = new ArrayList<>();
        mainBooks.add(new ArrayList<>(Arrays.asList("Intro to Java", "AI Basics", "Data Structures", "Algorithms", "Networking 101")));
        mainBooks.add(new ArrayList<>(Arrays.asList("Linear Algebra", "Calculus I", "Statistics", "Discrete Math", "Probability")));
        mainBooks.add(new ArrayList<>(Arrays.asList("Mechanics", "Thermodynamics", "Quantum Basics", "Electricity", "Waves")));
        mainBooks.add(new ArrayList<>(Arrays.asList("World History I", "Arab History", "Modern Europe", "Ottoman Empire", "Ancient Civilizations")));
        mainBooks.add(new ArrayList<>(Arrays.asList("English Grammar", "Arabic Morphology", "French A1", "Spanish Basics", "Academic Writing")));

        topics.add(mainTopics);
        books.add(mainBooks);

        // ==== Engineering Library ====
        libraries.add("Engineering Library");

        ArrayList<String> engTopics = new ArrayList<>(Arrays.asList(
            "Engineering", "Architecture", "Computer Science", "Math for Engineers", "Project Mgmt"
        ));

        ArrayList<ArrayList<String>> engBooks = new ArrayList<>();
        engBooks.add(new ArrayList<>(Arrays.asList("Civil Basics", "Electrical Circuits", "Mechanical Design", "Materials", "Fluid Mechanics")));
        engBooks.add(new ArrayList<>(Arrays.asList("Urban Design", "Interior Design", "3D Modelling", "Building Codes", "Sustainable Design")));
        engBooks.add(new ArrayList<>(Arrays.asList("Software Eng", "Operating Systems", "Databases", "Computer Networks", "Cybersecurity")));
        engBooks.add(new ArrayList<>(Arrays.asList("Calculus II", "Differential Eq.", "Numerical Methods", "Transforms", "Optimization")));
        engBooks.add(new ArrayList<>(Arrays.asList("PMI Guide", "Agile Primer", "Risk Mgmt", "Scheduling", "Cost Control")));

        topics.add(engTopics);
        books.add(engBooks);

        // ==== Science Library ====
        libraries.add("Science Library");

        ArrayList<String> sciTopics = new ArrayList<>(Arrays.asList(
            "Biology", "Chemistry", "Physics", "Earth Science", "Math"
        ));

        ArrayList<ArrayList<String>> sciBooks = new ArrayList<>();
        sciBooks.add(new ArrayList<>(Arrays.asList("Genetics", "Cell Biology", "Ecology", "Human Anatomy", "Evolution")));
        sciBooks.add(new ArrayList<>(Arrays.asList("Organic Chem", "Inorganic Chem", "Physical Chem", "Lab Safety", "Reactions")));
        sciBooks.add(new ArrayList<>(Arrays.asList("Modern Physics", "Optics", "Electromagnetism", "Relativity", "Astrophysics")));
        sciBooks.add(new ArrayList<>(Arrays.asList("Geology", "Oceanography", "Meteorology", "Soil Science", "Climate Change")));
        sciBooks.add(new ArrayList<>(Arrays.asList("Set Theory", "Number Theory", "Real Analysis", "Abstract Algebra", "Topology")));

        topics.add(sciTopics);
        books.add(sciBooks);
    }

    // Get all libraries
    public ArrayList<String> getLibraries() {
        return libraries;
    }

    // Get all topics of a specific library
    public ArrayList<String> getTopics(int libraryIndex) {
        if (libraryIndex < 0 || libraryIndex >= topics.size()) return new ArrayList<>();
        return topics.get(libraryIndex);
    }

    // Get all books under a topic in a library
    public ArrayList<String> getBooks(int libraryIndex, int topicIndex) {
        if (libraryIndex < 0 || libraryIndex >= books.size()) return new ArrayList<>();
        ArrayList<ArrayList<String>> libBooks = books.get(libraryIndex);
        if (topicIndex < 0 || topicIndex >= libBooks.size()) return new ArrayList<>();
        return libBooks.get(topicIndex);
    }

    // Create a unique slot name for each book
    public String makeSlotId(String library, String topic, String book) {
        return library + " | " + topic + " | " + book;
    }
}
