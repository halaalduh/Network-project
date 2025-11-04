/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package networkhala;

/**
 *
 * @author renad
 */
import java.util.*;

public class Library {
    
    private final Map<String, Map<String, Map<String, Boolean>>> data = new HashMap<>();

    public Library() { bootstrap(); }

    private void bootstrap() {
        String[] libraries = {"Central Library", "Science Library", "Digital Library"};
        String[][] topics = {
            {"Computer Science","Mathematics","Physics","Biology","Chemistry"},
            {"Engineering","Medicine","Law","Business","Arts"},
            {"Technology","Research","Education","History","Literature"}
        };
        String[][][] books = { 
            {
                {"Java Programming","Data Structures","Algorithms","Database Systems","Networking"},
                {"Calculus","Linear Algebra","Statistics","Discrete Math","Number Theory"},
                {"Quantum Physics","Classical Mechanics","Thermodynamics","Electromagnetism","Optics"},
                {"Genetics","Microbiology","Ecology","Anatomy","Botany"},
                {"Organic Chemistry","Inorganic Chemistry","Biochemistry","Analytical Chemistry","Physical Chemistry"}
            },
            {
                {"Electrical Engineering","Mechanical Engineering","Civil Engineering","Chemical Engineering","Software Engineering"},
                {"Human Anatomy","Pharmacology","Pathology","Surgery","Internal Medicine"},
                {"Constitutional Law","Criminal Law","Corporate Law","International Law","Family Law"},
                {"Marketing","Finance","Management","Accounting","Economics"},
                {"Painting","Sculpture","Music","Dance","Theater"}
            },
            {
                {"Artificial Intelligence","Machine Learning","Web Development","Mobile Apps","Cybersecurity"},
                {"Academic Research","Scientific Method","Thesis Writing","Data Analysis","Publication"},
                {"Teaching Methods","Curriculum Design","Educational Psychology","Classroom Management","E-Learning"},
                {"Ancient History","Medieval History","Modern History","World Wars","Cultural History"},
                {"Fiction","Poetry","Drama","Biography","Criticism"}
            }
        };

        for (int i = 0; i < libraries.length; i++) { // اعبي البيانات في ال data   
            Map<String, Map<String, Boolean>> lib = new HashMap<>();
            for (int t = 0; t < topics[i].length; t++) {
                Map<String, Boolean> topicBooks = new HashMap<>();
                for (int b = 0; b < books[i][t].length; b++) {
                    topicBooks.put(books[i][t][b], Boolean.TRUE);
                }
                lib.put(topics[i][t], topicBooks);
            }
            data.put(libraries[i], lib);
        }
    }

    public Set<String> getLibraries() { return data.keySet(); } // اسماء الايبرريز 

    public Set<String> getTopics(String library) { // التوبكس 
        Map<String, Map<String, Boolean>> lib = data.get(library);
        if (lib == null) return Collections.emptySet();
        return lib.keySet();
    }

    public List<String> getAvailableBooks(String library, String topic) { // عرض الكتب المتاحه 
        Map<String, Map<String, Boolean>> lib = data.get(library);
        if (lib == null) return Collections.emptyList();
        Map<String, Boolean> books = lib.get(topic);
        if (books == null) return Collections.emptyList();
        List<String> out = new ArrayList<String>();
        for (Map.Entry<String, Boolean> e : books.entrySet()) {
            if (Boolean.TRUE.equals(e.getValue())) out.add(e.getKey());
        }
        return out;
    }

    Map<String, Map<String, Map<String, Boolean>>> getRawData() { return data; } // هذي الي تحدث حاله الكتاب 
}
    

