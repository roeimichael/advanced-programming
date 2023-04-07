package test;

import java.util.HashSet;

public class CacheManager {
	
	
	HashSet<String> cache;
	int size;
	CacheReplacementPolicy crp;
	
	public CacheManager(int size,CacheReplacementPolicy crp) {
		cache=new HashSet<>(size);
		this.size=size;
		this.crp=crp;		
	}
	
	public boolean query(String word) {		
		return cache.contains(word);
	}
	
	public void add(String word) {
		crp.add(word);
		cache.add(word);
		if(cache.size()>size)
			cache.remove(crp.remove());
	}

}
