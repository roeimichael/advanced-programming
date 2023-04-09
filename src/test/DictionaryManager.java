package test;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class DictionaryManager {

    private static DictionaryManager instance;
    private Map<String, Dictionary> bookToDictionaryMap;

    private DictionaryManager() {
        bookToDictionaryMap = new HashMap<>();
    }

    // Singleton get() method
    public static DictionaryManager get() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public int getSize() {
        return bookToDictionaryMap.size();
    }

    public boolean query(String... args) {
        String word = args[args.length - 1];
        String[] books = Arrays.copyOfRange(args, 0, args.length - 1);
		

        Boolean res = false;
        for (String book : books) 
        {
        	if (bookToDictionaryMap.get(book) == null) 
        	{
        		Dictionary newdictionary = new Dictionary(books);
        		bookToDictionaryMap.put(book,newdictionary);
        	}
        	res = res || bookToDictionaryMap.get(book).query(word);
        }
        return res;
    }

    public boolean challenge(String... args) {
        String word = args[args.length - 1];
        String[] books = Arrays.copyOfRange(args, 0, args.length - 1);
        Boolean res = false;

        for (String book : books) {
            res = res || bookToDictionaryMap.get(book).challenge(word);
        }
        return res;
    }

}
