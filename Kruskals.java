import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.StringTokenizer;


public class Kruskals {
	private final static String path = "assn9_data.csv";
	
	private ArrayList<ArrayList<String>> adjacencyList;
	private HashSet<Edge> edges;
	private ArrayList<String> vertex;
	private int numVertices;

	public static void main(String[] args) {
		Kruskals kruskals = new Kruskals(Kruskals.path);
		kruskals.kruskal();
	}

	public Kruskals(String path2) {
		adjacencyList = readExcelFromPath(path);
		edges = new HashSet<>();
		vertex = new ArrayList<>();
		
		for(ArrayList<String> list : adjacencyList){
			String u = list.get(0);
			int u1 = vertex.indexOf(u);
			if(u1==-1){
				u1=vertex.size();
				vertex.add(u);
			}
			for(int i=1;i<list.size();i+=2){
				String v = list.get(i);
				int d = Integer.parseInt(list.get(i+1));
				int v1 = vertex.indexOf(v);
				if(v1==-1){
					v1=vertex.size();
					vertex.add(v);
				}
				Edge e = new Edge(u1,v1,d);
				edges.add(e);
			}
		}
		
		numVertices = vertex.size();
	}

	public ArrayList<ArrayList<String>> readExcelFromPath(String path){
		FileReader fr;
		BufferedReader br;
		ArrayList<ArrayList<String>> list = new ArrayList<>();
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);  
			String s;

			while((s = br.readLine()) != null) {
				StringTokenizer input = new StringTokenizer(s,",");
				ArrayList<String> temp = new ArrayList<>();
				while(input.hasMoreTokens()){
					temp.add(input.nextToken());
				}
				if(temp.size()==0){
					continue;
				}
				list.add(temp);
			}

			fr.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return list;
	}

	public void kruskal() {
		int edgesAccepted = 0;
		int totalDistance = 0;

		DisjSets ds = new DisjSets(numVertices);
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>(getEdges());
		Edge e;

		while (edgesAccepted < numVertices-1){
			e = pq.remove();
			int uset = ds.find( e.u );
			int vset = ds.find( e.v );
			if (uset != vset){
				System.out.printf("%-20s", vertex.get(e.u));
				System.out.printf("%-20s", vertex.get(e.v));
				System.out.println(e.distance);
				totalDistance += e.distance;
				edgesAccepted++;
				ds.union(uset, vset);
			}
		}
		
		System.out.println();
		System.out.print("Total Distance : ");
		System.out.println(totalDistance);
	} 

	private HashSet<Edge> getEdges() {
		return edges;
	}

	private static class Edge implements Comparable<Edge>{
		public int u;
		public int v;
		public int distance;

		public Edge(int u1, int v1, int d) {
			u=u1;
			v=v1;
			distance=d;
		}

		@Override
		public boolean equals(Object other) {
		    if (!(other instanceof Edge)) {
		        return false;
		    }
		    
		    Edge other1 = (Edge) other;
		    
		    if(this.u == other1.u && this.v == other1.v && this.distance == other1.distance){
		    	return true;
		    }
		    if(this.u == other1.v && this.v == other1.u && this.distance == other1.distance){
		    	return true;
		    }
		    
		    return false;
		}
		
		@Override
		public int hashCode(){
			return this.distance;
		}
		
		@Override
		public int compareTo(Edge o) {
			if(this.distance>o.distance){
				return 1;
			} else if(this.distance<o.distance){
				return -1;
			}
			return 0;
		}
	}
}
