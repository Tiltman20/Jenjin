package core;

import graphics3d.Transform3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import core.scene.Component;

public class Node {
    public String name;
    public Transform3D transform = new Transform3D();

    private Node parent;
    private final List<Node> children = new ArrayList<>();
    private final List<Component> components = new ArrayList<>();

    public Node(String name){
        this.name = name;
    }

    public void addChild(Node child){
        child.parent = this;
        child.transform.parent = this.transform;
        children.add(child);
    }

    public List<Node> getChildren(){
        return children;
    }

    public void addComponent(Component component){
        component.node = this;
        components.add(component);
    }

    public <T extends Component> T getComponent(Class<T> type){
        for(Component c : components)
            if(type.isInstance(c))
                return type.cast(c);
        return null;
    }

    public List<Component> getComponents() {
        return components;
    }
}
