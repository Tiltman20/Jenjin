package core.scene.components;

import core.ObjLoader;
import core.Scene;
import core.scene.Component;
import graphics.Shader;
import graphics3d.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MeshRenderer extends Component {
    public Mesh3D mesh;
    public Material material;

    public String meshPath;
    public String texturePath;

    public MeshRenderer(Mesh3D mesh, Material material,
                        String meshPath, String texturePath) {
        this.mesh = mesh;
        this.material = material;
        this.meshPath = meshPath;
        this.texturePath = texturePath;
    }

    public MeshRenderer() {
        System.out.println("manno");
    }

    @Override
    public void render(){
        Camera3D camera = Scene.worldCamera;

        material.bind(camera, node.transform);

        Shader shader = material.shader;

        shader.setInt("uLightCount", Scene.lights.size());
        for (int i = 0; i < Scene.lights.size(); i++) {
            Light l = Scene.lights.get(i);
            shader.setVec3("uLights["+i+"].position", l.position.x, l.position.y, l.position.z);
            shader.setVec3("uLights["+i+"].color", l.color.x, l.color.y, l.color.z);
        }

        shader.setVec3("uViewPos", camera.getPosition().x,  camera.getPosition().y, camera.getPosition().z);

        mesh.render();
        material.unbind();
    }

    @Override
    public String getType() {
        return "MeshRenderer";
    }

    @Override
    public Element writeXML(Document doc, Element e) {
        e.setAttribute("mesh",  meshPath);
        e.setAttribute("texture",  texturePath);
        e.setAttribute("material", material.shader.getName());

        System.out.println(texturePath);

        return e;
    }

    @Override
    public Element readXML(Element e) {
        meshPath = e.getAttribute("mesh");
        texturePath = e.getAttribute("texture");
        String shaderName = e.getAttribute("material");

        mesh = ObjLoader.load(meshPath);
        material = new Material(shaderName);
        if(texturePath != "")
            material.albedo = new Texture3D(texturePath);
        return e;
    }
}
