package core.scene.components;

import core.scene.Component;
import graphics3d.Mesh3D;
import core.ObjLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MeshFilter extends Component {
    public Mesh3D mesh;
    public String meshPath;

    public MeshFilter(){}

    public MeshFilter(String meshPath){
        this.meshPath = meshPath;
        this.mesh = ObjLoader.load(meshPath);
    }

    @Override
    public String getType() {
        return "MeshFilter";
    }

    @Override
    public Element writeXML(Document doc, Element e) {
        e.setAttribute("mesh", meshPath);
        return e;
    }

    @Override
    public Element readXML(Element e) {
        meshPath = e.getAttribute("mesh");
        mesh = ObjLoader.load(meshPath);
        return e;
    }
}
