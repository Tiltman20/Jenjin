package core.scene.components;

import core.scene.Component;
import graphics3d.Material;
import graphics3d.Texture3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class MaterialRenderer extends Component{
    public Material material;

    public MaterialRenderer(){}

    public MaterialRenderer(Material material){
        this.material = material;
    }

    @Override
    public String getType() {
        return "MaterialRenderer";
    }

    @Override
    public Element writeXML(Document doc, Element e) {
        e.setAttribute("shader", material.shaderType);
        if(material.albedoPath != null){
            e.setAttribute("albedoPath", material.albedoPath);
        }
        return e;
    }

    @Override
    public Element readXML(Element e) {
        String shaderType =  e.getAttribute("shader");
        String albedoPath =  e.getAttribute("albedoPath");
        System.out.println("shaderType: " + shaderType);
        System.out.println("albedoPath: " + albedoPath);

        material = new Material(shaderType);
        if(!albedoPath.isEmpty()){
            material.albedo = new Texture3D(albedoPath);
            material.albedoPath = albedoPath;
        }
        return e;
    }
}
