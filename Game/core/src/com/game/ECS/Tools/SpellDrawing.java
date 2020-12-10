package com.game.ECS.Tools;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sean on 1/04/2015.
 *
 * Handling of patterns of spells, SpellCasting handles the actual drawing of them.
 *
 */
public class SpellDrawing {
    public class Edge {
        public int p1;
        public int p2;

        public boolean compare(Edge other)
        {
            return (other.p1 == p1 && other.p2 == p2) || (other.p1 == p2 && other.p2 == p1);
        }
    }

    private LinkedList<Edge> edges;

    public SpellDrawing() {
        edges = new LinkedList<Edge>();
    }

    public void addEdge(int p1, int p2) {
        Edge edge = new Edge();
        edge.p1 = p1;
        edge.p2 = p2;
        edges.add(edge);
    }

    public List<Edge> getEdges(){
        return edges;
    }

    public void clearEdges(){
        edges.clear();
    }

    public boolean Compare(SpellDrawing other)
    {
        if(other.edges.size() != edges.size())
            return false;

        LinkedList<Edge> edgesLeft = (LinkedList<Edge>)edges.clone();

        for (int i = 0; i < other.edges.size(); i++)
        {
            boolean foundEdge = false;
            for (int j = 0; j < edgesLeft.size(); j++)
            {
                if(other.edges.get(i).compare(edgesLeft.get(j)))
                {
                    edgesLeft.remove(j);
                    foundEdge = true;
                    break;
                }
            }
            if (!foundEdge)
                return false;
        }
        return true;
    }
}
