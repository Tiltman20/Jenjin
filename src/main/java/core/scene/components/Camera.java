package core.scene.components;

import core.scene.Component;
import core.scene.ComponentRegistry;
import graphics3d.Camera3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Camera extends Component {

    public Camera3D camera = new Camera3D();
    public boolean primary = true;
    @Override
    public Component cloneComponent() {
        Camera copy = new Camera();

        copy.primary = this.primary;

        copy.camera.setFov(camera.getFov());
        copy.camera.setNear(camera.getNear());
        copy.camera.setFar(camera.getFar());

        return copy;
    }

    @Override
    public String getType() {
        return "Camera";
    }

    @Override
    public Element writeXML(Document doc, Element componentElement) {
        componentElement.setAttribute("primary", String.valueOf(primary));
        componentElement.setAttribute("fov", String.valueOf(camera.getFov()));
        componentElement.setAttribute("near", String.valueOf(camera.getNear()));
        componentElement.setAttribute("far", String.valueOf(camera.getFar()));

        return componentElement;
    }

    @Override
    public Element readXML(Element componentElement) {
        primary = Boolean.parseBoolean(
                componentElement.getAttribute("primary"));

        float fov = Float.parseFloat(
                componentElement.getAttribute("fov"));

        float near = Float.parseFloat(
                componentElement.getAttribute("near"));

        float far = Float.parseFloat(
                componentElement.getAttribute("far"));

        camera.setFov(fov);
        camera.setNear(near);
        camera.setFar(far);

        return componentElement;
    }

    static {
        ComponentRegistry.register(
                "CameraComponent",
                Camera::new
        );
    }
}
