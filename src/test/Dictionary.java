package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		searcher.finalize();
	}
 
	
	public class ParIOSearcher implements FileSearcher{

		ExecutorService es;	
		
		public ParIOSearcher() {		
			es=Executors.newCachedThreadPool();
		}
		
		public boolean search(String word, String...fileNames){
			ArrayList<IOSearcher> searchers=new ArrayList<>();
			ArrayList<Future<Boolean>> fs=new ArrayList<>();

			for(String fn : fileNames) {
				IOSearcher s = new IOSearcher();
				searchers.add(s);
				fs.add(es.submit(()->{				
					boolean found = s.search(word, fn);
					if(found) { 
						searchers.forEach(srch->srch.stop());
					}
					return found;
				}));
			}
			
			boolean found = false;
			for(Future<Boolean> f : fs) {
				try {
					found|=f.get();
				} catch (InterruptedException | ExecutionException e) {}
			}
			stop();
			es=Executors.newCachedThreadPool();
			return found;
		}
		
		public void stop() {
			es.shutdownNow();
		}
		
		@Override
		public void finalize() {
			es.shutdown();
		}
		
		
	}

}
