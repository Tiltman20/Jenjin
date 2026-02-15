package core.scene;

import core.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Component {
    public Node node;

    public void update(float dt){}
    public void render(){}

    public abstract String getType();
    public abstract Element writeXML(Document doc, Element componentElement);
    public abstract Element readXML(Element componentElement);
}
