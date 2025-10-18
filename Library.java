/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;
public class Library {

    private final String[] libraries = {"Main Library", "Engineering Library", "Science Library"};

    private final String[][] topics = {
        {"Computer Science", "Mathematics", "Physics", "History", "Languages"}, // Main Library
        {"Engineering", "Architecture", "Computer Science", "Math for Engineers", "Project Mgmt"}, // Engineering
        {"Biology", "Chemistry", "Physics", "Earth Science", "Math"} // Science
    };

    private final String[][][] books = {
        { // Main Library
            {"Intro to Java", "AI Basics", "Data Structures", "Algorithms", "Networking 101"},
            {"Linear Algebra", "Calculus I", "Statistics", "Discrete Math", "Probability"},
            {"Mechanics", "Thermodynamics", "Quantum Basics", "Electricity", "Waves"},
            {"World History I", "Arab History", "Modern Europe", "Ottoman Empire", "Ancient Civilizations"},
            {"English Grammar", "Arabic Morphology", "French A1", "Spanish Basics", "Academic Writing"}
        },
        { // Engineering Library
            {"Civil Basics", "Electrical Circuits", "Mechanical Design", "Materials", "Fluid Mechanics"},
            {"Urban Design", "Interior Design", "3D Modelling", "Building Codes", "Sustainable Design"},
            {"Software Eng", "Operating Systems", "Databases", "Computer Networks", "Cybersecurity"},
            {"Calculus II", "Differential Eq.", "Numerical Methods", "Transforms", "Optimization"},
            {"PMI Guide", "Agile Primer", "Risk Mgmt", "Scheduling", "Cost Control"}
        },
        { // Science Library
            {"Genetics", "Cell Biology", "Ecology", "Human Anatomy", "Evolution"},
            {"Organic Chem", "Inorganic Chem", "Physical Chem", "Lab Safety", "Reactions"},
            {"Modern Physics", "Optics", "Electromagnetism", "Relativity", "Astrophysics"},
            {"Geology", "Oceanography", "Meteorology", "Soil Science", "Climate Change"},
            {"Set Theory", "Number Theory", "Real Analysis", "Abstract Algebra", "Topology"}
        }
    };

    public String[] getLibraries() {
        return libraries;
    }

    public String[] getTopics(int libIndex) {
        if (libIndex < 0 || libIndex >= topics.length) return new String[0];
        return topics[libIndex];
    }

    public String[] getBooks(int libIndex, int topicIndex) {
        if (libIndex < 0 || libIndex >= books.length) return new String[0];
        if (topicIndex < 0 || topicIndex >= books[libIndex].length) return new String[0];
        return books[libIndex][topicIndex];
    }

    public String makeSlotId(int libIndex, int topicIndex, int bookIndex) {
        return libraries[libIndex] + " | " + topics[libIndex][topicIndex] + " | " + books[libIndex][topicIndex][bookIndex];
    }
}
