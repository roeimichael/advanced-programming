package test;

import java.io.File;
import java.util.Scanner;

public class IOSearcher implements FileSearcher{

	boolean stopMe;
	
	public IOSearcher() {
		stopMe=false;
	}
	
	public boolean search(String word, String...fileNames){
		boolean found=false;
		try {
			for(int i=0;!stopMe && i<fileNames.length && !found; i++) {
				Scanner s=new Scanner(new File(fileNames[i]));
				while(s.hasNext() && !found && !stopMe)
					if(s.next().equals(word))
						found=true;
				s.close();
			}
		}catch(Exception e) {}
		
		return found;
	}
	
	public void stop() {
		stopMe=true;
	}
}
