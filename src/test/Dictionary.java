package test;

import java.io.File;
import java.util.Scanner;

public class Dictionary {
	
	CacheManager exists,notExists;
	BloomFilter bf;
	private String[] fileNames;
	ParIOSearcher searcher;
	
	public Dictionary(String...fileNames) {
		this.fileNames=fileNames;
		exists=new CacheManager(400, new LRU());
		notExists=new CacheManager(100, new LFU());
		bf = new BloomFilter(256, "MD5","SHA1");
		
		for(String fn : fileNames) {
			try {
				Scanner s=new Scanner(new File(fn));
				while(s.hasNext())
					bf.add(s.next());
				s.close();
			}catch(Exception e) {}
		}		
		searcher=new ParIOSearcher();
	}
	
	public boolean query(String word) {
		if(exists.query(word))
			return true;
		if(notExists.query(word))
			return false;
		
		boolean doesExist = bf.contains(word);
		if(doesExist)
			exists.add(word);
		else
			notExists.add(word);
		
		return doesExist;
	}
	
	public boolean challenge(String word) {
		boolean doesExist = searcher.search(word, fileNames);
		if(doesExist)
			exists.add(word);
		else
			notExists.add(word);
		
		return doesExist;
	}
	
	public void close() {
		searcher.stop();
	}

}
