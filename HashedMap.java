import java.util.*;

public class HashedMap<Key,Value> { 
	private final int INITIAL_CAPACITY=100;
	private int size,capacity;
	private Entry<Key,Value>[] map;
	
	private static class Entry<Key,Value> {
		private Key key;
		private Value value;
		
		private Entry(Key k,Value v) {key=k;value=v;}

		private void setValue(Value v) {value=v;}
		private Value getValue() {return value;}
		private Key getKey() {return key;}
		public boolean equals(Object k) {return key.equals(k);}
		public int hashCode() {return key.hashCode();}
		public String toString() {return "["+key+" = "+value+"]";}
	}

	public HashedMap() {size=0;capacity=INITIAL_CAPACITY;map=new Entry[capacity];}

	private int hash(int h) {return Math.abs(h)%(capacity);}
	private void rehash(int nCap) { 
		Entry<Key,Value>[] nMap = new Entry[nCap];
		for (int i=0,count=0;i<map.length&&count!=size;i++) { //Iterate until every Entry is moved
			if (map[i]!=null) {
				nMap[locateEntry(map[i].getKey(),nMap)]=map[i];
				map[i]=null;count++;
			}
		}
		map = nMap;
	} 
	private void adjustCluster(int i) {
		ArrayList<Entry> cluster = new ArrayList<Entry>();
		for (int x=hash(i+1);map[x]!=null;x=hash(x+1)) {
			cluster.add(map[x]); //Add to Cluster
			map[x]=null; //Remove from Array
		}
		for (Entry e:cluster) {addEntry(e);} //Reintroduce to Array
	}
	private void addEntry(Entry e) {map[locateEntry((Key)e.getKey())]=e;} //Reintroduces an Entry to the Array
	private void addEntry(Key k,Value v) { 
		int i = locateEntry(k);
		if (map[i]!=null) {map[i].setValue(v);} //Update Entry
		else { //New Entry
			map[i] = new Entry<Key,Value>(k,v);
			if ((double)++size/capacity>0.75) {rehash(capacity*=2);} //Rehash if usage over 75% (Doubles Capacity)
		}
	}
	private boolean removeEntry(Key k) {
		int i = locateEntry(k);
		if (map[i]!=null) {
			map[i]=null;
			if ((double)--size/capacity<0.20&&capacity>INITIAL_CAPACITY) {rehash(capacity/=2);} //Rehash if usage below 20% (Halves Capacity)
			else {adjustCluster(i);}
			return true; 
		}
		return false;
	}
	private Entry<Key,Value> getEntry(Key k) {return getEntry(locateEntry(k));} //Returns an Entry or Null
	private Entry<Key,Value> getEntry(int i) {return map[i];} //Returns specific Entry or Null (Already Located)
	private int locateEntry(Key k) {return locateEntry(k,map);} //Default to "map" array
	private int locateEntry(Key k,Entry[] eA) { //Finds Entry's Index or the first Null Index in specified array
		int h = hash(k.hashCode());
		while (eA[h]!=null&&!eA[h].equals(k)) {h=hash(h+1);} //Iterate hash (Clusters)
		return h;
	}
	public int size() {return size;}
	public int capacity() {return capacity;}
	public boolean isEmpty() {return size==0;}
	public Set keySet() {
		Set keys = new HashSet<Key>();
		for (Entry e:map) {if(e!=null) {keys.add(e.getKey());}}
		return keys;
	}
	public void put(Key k,Value v) {addEntry(k,v);}
	public void clear() {for (Entry<Key,Value> e:map) {e=null;}}
	public boolean remove(Key k) {return removeEntry(k);}
	public boolean contains(Key k) {return (map[locateEntry(k)]!=null);}
	public Object get(Key k) {
		Entry e=getEntry(k);
		if (e!=null) {return e.getValue();}
		return null;
	}
	public String toString() {
		String rs = "";
		for (Entry<Key,Value> e:map) {rs+=e+"\n";}
		return rs;
	}
}