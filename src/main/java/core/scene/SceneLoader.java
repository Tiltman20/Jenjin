package core.scene;

import core.Node;
import core.Scene;
import graphics3d.Transform3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SceneLoader {
    public static Scene load(String path){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(path);
            document.getDocumentElement().normalize();

            Scene scene = new Scene();

            Element root = document.getDocumentElement();
            Element rootNodeXml = (Element) root.getElementsByTagName("node").item(0);
            Node rootNode = readNode(rootNodeXml);
            scene.getRoot().addChild(rootNode);

            return scene;
        }catch(Exception e){
            throw new RuntimeException("Could not load scene", e);
        }
    }

    private static Node readNode(Element nodeXml) {
        String name = nodeXml.getAttribute("name");
        Node node = new Node(name);

        readTransform(node, nodeXml);
        readComponents(node, nodeXml);
        readChildren(node, nodeXml);

        return node;
    }

    private static void readTransform(Node node, Element nodeXml) {
        NodeList list = nodeXml.getElementsByTagName("transform");
        if(list.getLength() == 0) return;
        Element element = (Element) list.item(0);
        Transform3D transform = node.transform;

        transform.position.set(
                Float.parseFloat(element.getAttribute("px")),
                Float.parseFloat(element.getAttribute("py")),
                Float.parseFloat(element.getAttribute("pz"))
        );
        transform.rotation.set(
                Float.parseFloat(element.getAttribute("rx")),
                Float.parseFloat(element.getAttribute("ry")),
                Float.parseFloat(element.getAttribute("rz"))
        );
        transform.scale.set(
                Float.parseFloat(element.getAttribute("sx")),
                Float.parseFloat(element.getAttribute("sy")),
                Float.parseFloat(element.getAttribute("sz"))
        );
    }

    private static void readComponents(Node node, Element nodeXml) {
        NodeList comContainers = nodeXml.getElementsByTagName("components");
        if(comContainers.getLength() == 0) return;
        Element comContainer = (Element) comContainers.item(0);
        NodeList compList = comContainer.getElementsByTagName("component");
        for (int i = 0; i < compList.getLength(); i++) {
            Element compXml = (Element) compList.item(i);
            String type = compXml.getAttribute("type");

            Component c = ComponentFactory.create(type);
            c.node = node;
            c.readXML(compXml);

            node.addComponent(c);
        }
    }

    private static void readChildren(Node node, Element nodeXml) {
        NodeList children = nodeXml.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if(!(children.item(i) instanceof Element)) continue;

            Element child = (Element) children.item(i);
            if(!child.getTagName().equals("node")) continue;

            Node childNode = readNode(child);
            node.addChild(childNode);
        }

    }
}
