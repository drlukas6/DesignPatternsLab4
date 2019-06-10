package graphics.drawing;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import graphics.Point;
import graphics.graphicalObjects.abstracts.AbstractGraphicalObject;
import graphics.graphicalObjects.abstracts.GraphicalObject;
import graphics.listeners.DocumentModelListener;
import graphics.listeners.GraphicalObjectListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10;

    // Kolekcija svih grafičkih objekata:
    private List<GraphicalObject> objects = new ArrayList<>();

    // Read-Only proxy oko kolekcije grafičkih objekata:
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);

    // Kolekcija prijavljenih promatrača:
    private List<DocumentModelListener> listeners = new ArrayList<>();

    // Kolekcija selektiranih objekata:
    private List<GraphicalObject> selectedObjects = new ArrayList<>();

    // Read-Only proxy oko kolekcije selektiranih objekata:
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    // Promatrač koji će biti registriran nad svim objektima crteža...
    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            System.err.println("GRAPHICAL OBJECT CHANGED NEEDS TO BE IMPLEMENTED");
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            objects.remove(go);
            selectedObjects.remove(go);

            if (go.isSelected()) {
                selectedObjects.add(go);
            } else {
                objects.add(go);
            }
        }
    };

    // Konstruktor...
    public DocumentModel() {

    }

    // Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
    // i potom obavijeste svi promatrači modela
    public void clear() {
        for(GraphicalObject object: objects) {
            object.removeGraphicalObjectListener(goListener);
        }
        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    // Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte model kao promatrača)
    public void addGraphicalObject(GraphicalObject obj) {
        obj.addGraphicalObjectListener(goListener);
        if(!obj.isSelected()) {
            objects.add(obj);
        } else {
            selectedObjects.add(obj);
        }
    }

    // Uklanjanje objekta iz dokumenta (pazite je li već selektiran; odregistrirajte model kao promatrača)
    public void removeGraphicalObject(GraphicalObject obj) {
        obj.removeGraphicalObjectListener(goListener);
        if(!obj.isSelected()) {
            objects.remove(obj);
        } else {
            selectedObjects.remove(obj);
        }
    }

    // Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo kroz metode modela)
    public List<GraphicalObject> list() {
        return roObjects;
    }

    // Prijava...
    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    // Odjava...
    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    // Obavještavanje...
    public void notifyListeners() {
        for(DocumentModelListener listener: listeners) {
            listener.documentChange();
        }
    }

    // Vrati nepromjenjivu listu selektiranih objekata
    public List getSelectedObjects() {
        return roSelectedObjects;
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
    // Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
    public void increaseZ(GraphicalObject go) {
        int currentIndex = objects.indexOf(go);

        if (currentIndex == -1) {
            return;
        }

        if (currentIndex == objects.size() - 1) {
            return;
        }

        GraphicalObject nextElement = objects.get(currentIndex + 1);
        objects.set(currentIndex, nextElement);
        objects.set(currentIndex + 1, go);
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto ranije...
    public void decreaseZ(GraphicalObject go) {
        int currentIndex = objects.indexOf(go);

        if (currentIndex == -1) {
            return;
        }

        if (currentIndex == 0) {
            return;
        }

        GraphicalObject previousElement = objects.get(currentIndex - 1);
        objects.set(currentIndex, previousElement);
        objects.set(currentIndex - 1, go);
    }

    // Pronađi postoji li u modelu neki objekt koji klik na točku koja je
    // predana kao argument selektira i vrati ga ili vrati null. Točka selektira
    // objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
    // SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        GraphicalObject selectedObject = null;
        double minDistance = SELECTION_PROXIMITY;

        for(GraphicalObject object: objects) {
            if (object.selectionDistance(mousePoint) <= minDistance) {
                selectedObject = object;
                minDistance = object.selectionDistance(mousePoint);
            }
        }

        return selectedObject;
    }

    // Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
    // Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
    // udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
    // kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije
    // se pri tome NE dira.
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int result = -1;
        double minDistance = SELECTION_PROXIMITY;

        for(int i = 0; i < object.getNumberOfHotPoints(); ++i) {
            double distance = object.getHotPointDistance(0, mousePoint);
            if (distance < minDistance) {
                minDistance = distance;
                result = i;
            }
        }

        return result;
    }

}
