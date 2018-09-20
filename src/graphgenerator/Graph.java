/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphgenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author bperadotto
 * @param <T>
 */
public class Graph<T> {

    private Map<Vertex, ArrayList<Edge>> vertices;
    private ArrayList<Edge> edges;
    private int numVertices;
    private int numEdges;

    public Graph() {
        edges = new ArrayList<>();
        vertices = new HashMap<>();
        numVertices = 0;
        numEdges = 0;
    }

    /**
     * addEdgeToGraph is a way to manually add one edge into the graph stucture.
     * It takes a source, a destination and a weight. In aadditon it also will
     * enter the destinatin as the source and the source as the destination to
     * cover all permuations.
     *
     * @param <T>
     * @param source
     * @param destination
     * @param weight
     */
    public <T> void addEdgeToGraph(T source, T destination, int weight) {
        // because of the way this graph class is set up sources and destinations can be discovered
        // to be vertices. because hashmap keys don't duplicate we wont duplicate vertex entries
        Vertex v1 = new Vertex(source);
        Vertex v2 = new Vertex(destination);
        ArrayList<Edge> temp1 = new ArrayList<>();
        ArrayList<Edge> temp2 = new ArrayList<>();
        Edge e = new Edge(v1, destination, weight);
        edges.add(e);                             // we are alrady adding the desitation as and edge so only dong v1 as vertex not v2
        numEdges++;
        if (!(vertices.containsKey(v1))) {
            numVertices++;
            temp1.add(e);
            vertices.put(v1, temp1);
        } else {
            temp1 = vertices.get(v1);             //get the current list 
            temp1.add(e);                           // add new edge to it
            vertices.put(v1, temp1);                /// place newwly added edge into hashmap

        }
        Edge f = new Edge(v2, source, weight);   // new edge with detination now as vertex adn source as next destination
        edges.add(f);
        numEdges++;
        if (!(vertices.containsKey(v2))) {
            numVertices++;
            temp2.add(f);
            vertices.put(v2, temp2);
        } else {

            temp2 = vertices.get(v2);
            temp2.add(f);
            vertices.put(v2, temp2);

        }

    }

    public String getFormattedGraph() {
        String returnString = "";
        for (Map.Entry<Vertex, ArrayList<Edge>> entry : vertices.entrySet()) {
            returnString += (entry.getKey() + " ");
            for (int i = 0; i < entry.getValue().size(); i++) {

                returnString += (entry.getValue().get(i).getDestination() + ":" + entry.getValue().get(i).getWeight() + " ");
            }
            returnString += "\n";  // one more hard return
        }
        return returnString;

    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Map<Vertex, ArrayList<Edge>> getEntireGraph() {
        return vertices;
    }

    public ArrayList<Vertex> getVertices() {
        ArrayList<Vertex> v = new ArrayList<>(vertices.keySet());
        return v;

    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public boolean isVertexInGraph(T vertex) {
        for (Map.Entry<Vertex, ArrayList<Edge>> entry : vertices.entrySet()) {
            if (vertex.toString().compareTo(entry.getKey().getName().toString()) == 0){
            return true;
            
            }
        }
        return false;
    }

    public boolean isEdgeInGraph(Edge edge) {
        for (Map.Entry<Vertex, ArrayList<Edge>> entry : vertices.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (edge.equals(entry.getValue().get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Quick method for sorting the graph edge nodes in order by weight.
     */
    public void sortEdgesByWeight() {

        edges.sort(Comparator.comparingInt(o -> o.weight));
    }

    protected class Edge<T> implements Comparable<Edge> {

        private final Vertex vertex;
        private final T destination;
        private final int weight;

        private Edge(Vertex vertex, T destination, int weight) {
            this.vertex = vertex;
            this.destination = destination;
            this.weight = weight;
        }

        public Vertex getVertex() {
            return vertex;
        }

        public T getDestination() {
            return destination;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.vertex);
            hash = 23 * hash + Objects.hashCode(this.destination);
            hash = 23 * hash + this.weight;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Edge<?> other = (Edge<?>) obj;
            if (this.weight != other.weight) {
                return false;
            }
            if (!Objects.equals(this.vertex, other.vertex)) {
                return false;
            }
            if (!Objects.equals(this.destination, other.destination)) {
                return false;
            }
            return true;
        }

        /**
         * To string override to display node data
         *
         * @return Returns string containing Rank, Data and Parents Data.
         */
        @Override
        public String toString() {
            return String.format("Source:" + getVertex() + " - Destination:" + getDestination() + " - Weight:" + getWeight());
        }

        @Override
        public int compareTo(Edge o) {
            return this.getWeight() - o.getWeight();

        }

    }

    protected class Vertex<T> implements Comparable<Vertex> {

        private final T name;
        private final int key;
        private final Vertex parent;

        public Vertex(T name) {
            this.name = name;
            this.key = Integer.MAX_VALUE;
            this.parent = null;
        }

        public Vertex(T name, int key, Vertex parent) {
            this.name = name;
            this.key = key;
            this.parent = parent;
        }

        public T getName() {
            return name;
        }

        public int getKey() {
            return key;
        }

        public Vertex getParent() {
            return parent;
        }

        /**
         * To string override to display node data
         *
         * @return Returns string containing Rank, Data and Parents Data.
         */
        @Override
        public String toString() {
            return String.format(" " + getName());
        }

        /**
         * Override with custom hashcodes to prevent duplicate entries when
         * comparing objects in hashmap
         *
         * @return
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.name);
            hash = 79 * hash + this.key;
            return hash;

        }

        /**
         * Override with custom hashcodes to prevent duplicate entries when
         * comparing objects in hashmap
         *
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Vertex<?> other = (Vertex<?>) obj;
            if (this.key != other.key) {
                return false;
            }
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

        /**
         * Override how to compare two objects
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Vertex o) {
            return this.getName().toString().compareTo(o.getName().toString());
        }
    }
}
