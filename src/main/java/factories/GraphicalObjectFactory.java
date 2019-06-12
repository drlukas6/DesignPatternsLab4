package factories;

import graphics.graphicalObjects.CompositeObject;
import graphics.graphicalObjects.LineSegment;
import graphics.graphicalObjects.Oval;
import graphics.graphicalObjects.abstracts.GraphicalObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class GraphicalObjectFactory {
    private  Map<String, GraphicalObject> graphicalObjectPrototypes = new HashMap<>();
    public static GraphicalObjectFactory instance = new GraphicalObjectFactory();

    private GraphicalObjectFactory() {
        graphicalObjectPrototypes.put("Line", new LineSegment());
        graphicalObjectPrototypes.put("Oval", new Oval());
        graphicalObjectPrototypes.put("Composite", new CompositeObject(new LinkedList<>()));
    }


    public GraphicalObject graphicalObjectWithDescription(Stack<GraphicalObject> stack,  String description) {
        String[] splitDescription = description.split(" ");
        StringBuilder data = new StringBuilder();
        for (int i = 1; i < splitDescription.length; ++i) {
            data.append(splitDescription[i]).append(" ");
        }

        String points = data.substring(0, data.length() - 1);

        String objectType = splitDescription[0];

        return graphicalObjectPrototypes.get(objectType).load(stack, points);
    }
}
