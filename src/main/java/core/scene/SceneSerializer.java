package core.scene;

import core.Node;
import core.Scene;

import graphics3d.Transform3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;

public class SceneSerializer {
    public static void save(Scene scene, String path){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("scene");
            doc.appendChild(rootElement);

            writeNode(doc, rootElement, scene.getRoot());

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(new DOMSource(doc),
                    new StreamResult(new File(path)));

        } catch (Exception e){
            throw new RuntimeException("Failed to save scene", e);
        }
    }

    private static void writeNode(Document doc, Element parentXml, Node node){
        Element nodeXml = doc.createElement("node");
        nodeXml.setAttribute("name", node.name);
        parentXml.appendChild(nodeXml);

        writeTransform(doc, nodeXml, node.transform);
        writeComponents(doc, nodeXml, node);

        for(Node child : node.getChildren()){
            writeNode(doc, nodeXml, child);
        }
    }

    private static void writeComponents(Document doc, Element nodeXml, Node node) {

        Element componentsXml = doc.createElement("components");
        nodeXml.appendChild(componentsXml);

        for (Component c : node.getComponents()) {
            Element componentXml = doc.createElement("component");
            componentXml.setAttribute("type", c.getType());
            componentXml = c.writeXML(doc, componentXml);
            componentsXml.appendChild(componentXml);
        }
    }

    private static void writeTransform(Document doc, Element nodeXml, Transform3D t) {
        Element tr =  doc.createElement("transform");

        tr.setAttribute("px", String.valueOf(t.position.x));
        tr.setAttribute("py", String.valueOf(t.position.y));
        tr.setAttribute("pz", String.valueOf(t.position.z));

        tr.setAttribute("rx", String.valueOf(t.rotation.x));
        tr.setAttribute("ry", String.valueOf(t.rotation.y));
        tr.setAttribute("rz", String.valueOf(t.rotation.z));

        tr.setAttribute("sx", String.valueOf(t.scale.x));
        tr.setAttribute("sy", String.valueOf(t.scale.y));
        tr.setAttribute("sz", String.valueOf(t.scale.z));

        nodeXml.appendChild(tr);
    }

//    private static void readComponents(Node node, Element nodeXml){
//        NodeList compList = nodeXml.getElementsByTagName("component");
//
//        for (int i = 0; i < compList.getLength(); i++) {
//            Element compXml = (Element) compList.item(i);
//            String type = compXml.getAttribute("type");
//
//            Component c = ComponentFactory.create(type);
//            c.node = node;
//            c.readXML(compXml);
//            node.addComponent(c);
//        }
//    }
}
