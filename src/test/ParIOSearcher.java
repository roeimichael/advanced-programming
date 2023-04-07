package test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
