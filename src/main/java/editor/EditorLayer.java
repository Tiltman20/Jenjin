package editor;

import core.*;
import core.scene.Component;
import core.scene.ComponentRegistry;
import core.scene.SceneLoader;
import core.scene.SceneSerializer;
import core.scene.components.Camera;
import core.scene.components.MaterialRenderer;
import core.scene.components.MeshFilter;
import editor.GizmoState.Axis;
import graphics3d.Camera3D;
import graphics3d.Material;
import graphics3d.Transform3D;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import input.MouseInput;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EditorLayer {

    private final ImGuiImplGlfw glfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 gl3 = new ImGuiImplGl3();

    private Vector2f viewportPos = new Vector2f();
    private Vector2f viewportSize = new Vector2f();

    private boolean viewportHovered = false;

    public void init(long windowHandle){
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
//        io.setIniFilename(null);

        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

        glfw.init(windowHandle, true);
        gl3.init("#version 330");
    }

    public void begin(){
        glfw.newFrame();
        gl3.newFrame();
        ImGui.newFrame();
    }

    public void end(){
        ImGui.render();
        gl3.renderDrawData(ImGui.getDrawData());
    }

    public void renderDemo(){
        ImGui.showDemoWindow();
    }


    public void dispose(){
        gl3.shutdown();
        glfw.shutdown();
        ImGui.destroyContext();
    }

    public void renderGUI(Engine engine) {
        drawToolbar(engine);
        drawHierarchy(engine);
        drawInspector(engine);
        drawSceneViewport(engine);
        drawGameViewport(engine);
        handleGizmoDrag(engine);

        processDeletions();
    }

    private void drawSceneViewport(Engine engine) {
        int flags =
                ImGuiWindowFlags.NoScrollbar |
                        ImGuiWindowFlags.NoScrollWithMouse |
                        ImGuiWindowFlags.NoCollapse |
                        ImGuiWindowFlags.NoBringToFrontOnFocus;

        ImGui.begin("Viewport", flags);

        viewportPos.set(
                ImGui.getCursorScreenPosX(),
                ImGui.getCursorScreenPosY()
        );

        viewportSize.set(
                ImGui.getContentRegionAvailX(),
                ImGui.getContentRegionAvailY()
        );

        if(viewportSize.x < 1 || viewportSize.y < 1){
            ImGui.end();
            return;
        }

        engine.setSceneViewportSize((int)viewportSize.x,(int)viewportSize.y);

        ImGui.invisibleButton("viewport_input",
                viewportSize.x,
                viewportSize.y);

        boolean viewportActive = ImGui.isItemActive();
        boolean viewportHoveredNow = ImGui.isItemHovered();

        viewportHovered = viewportHoveredNow || viewportActive;


        ImGui.setCursorScreenPos(viewportPos.x, viewportPos.y);

        int texId = engine.getSceneTexture();

        ImGui.image(
                texId,
                viewportSize.x,
                viewportSize.y,
                0,1, 1,0
        );

        ImGui.end();
    }

    private void drawGameViewport(Engine engine) {
        ImGui.begin("Game");

        Vector2f size = new Vector2f(
                ImGui.getContentRegionAvailX(),
                ImGui.getContentRegionAvailY()
        );

        if(size.x<1 || size.y<1){
            ImGui.end();
            return;
        }

        engine.setGameViewportSize((int)size.x,(int)size.y);

        int texId = engine.getGameTexture();

        ImGui.image(texId, size.x, size.y, 0, 1, 1, 0);

        ImGui.end();
    }


    private void handleGizmoDrag(Engine engine) {
        ImGuiIO io = ImGui.getIO();
        if(io.getWantCaptureMouse() && !viewportHovered)
            return;

        GizmoState.hoverAxis = Axis.NONE;

        Node node = EditorSelection.selectedNode;
        if(node == null) return;

        Vector3f gizmoPos = node.transform.getWorldPosition();
        float threshold = GizmoMath.getPickThreshold(engine.getEditorCamera(), gizmoPos);

        Camera3D cam = Scene.worldCamera;

        int windowWidth  = (int)viewportSize.x;
        int windowHeight = (int)viewportSize.y;

        float mouseX = MouseInput.getMouseX() - viewportPos.x;
        float mouseY = MouseInput.getMouseY() - viewportPos.y;

        if(mouseX < 0 || mouseY < 0 || mouseX > viewportSize.x || mouseY > viewportSize.y)
            return;

        Vector3f rayDir = EditorCameraUtil.screenToWorldRay(
                mouseX, mouseY,
                windowWidth, windowHeight,
                cam
        );

        Vector3f rayOrigin = cam.getPosition();

        float distX = EditorCameraUtil.distanceRayToLine(
                rayOrigin, rayDir,
                gizmoPos, new Vector3f(1,0,0));
        float distY = EditorCameraUtil.distanceRayToLine(
                rayOrigin, rayDir,
                gizmoPos, new Vector3f(0,1,0));
        float distZ = EditorCameraUtil.distanceRayToLine(
                rayOrigin, rayDir,
                gizmoPos, new Vector3f(0,0,1));

        float min = Math.min(distX, Math.min(distY, distZ));

        if(min < threshold){
                if(min == distX) GizmoState.hoverAxis = Axis.X;
                else if(min == distY) GizmoState.hoverAxis = Axis.Y;
                else GizmoState.hoverAxis = Axis.Z;
        }

        if(MouseInput.isButtonPressed(0) && !GizmoState.dragging && GizmoState.hoverAxis != Axis.NONE){
            GizmoState.activeAxis = GizmoState.hoverAxis;

            Vector3f axisDir = switch (GizmoState.activeAxis){
                case X -> new Vector3f(1,0,0);
                case Y -> new Vector3f(0,1,0);
                case Z -> new Vector3f(0,0,1);
                default -> new Vector3f(1,0,0);
            };
            Vector3f camForward = new Vector3f(0,0,-1);
            Vector3f camRight   = new Vector3f(1,0,0);

            Matrix4f invView = new Matrix4f(cam.getViewMatrix()).invert();
            invView.transformDirection(camForward);
            invView.transformDirection(camRight);

            Vector3f planeNormal = axisDir.cross(camForward);

            if(planeNormal.lengthSquared() < 0.0001f){
                planeNormal = axisDir.cross(camRight);
            }

            if(planeNormal.lengthSquared() < 0.0001f){
                planeNormal.set(0,1,0); // absoluter fallback
            }

            GizmoState.dragPlaneNormal.set(planeNormal.normalize());
            GizmoState.dragPlanePoint.set(gizmoPos);

            Vector3f hit = EditorCameraUtil.rayPlaneIntersection(
                    rayOrigin, rayDir,
                    GizmoState.dragPlanePoint,
                    GizmoState.dragPlaneNormal);
            if(hit == null) return;

            GizmoState.dragOffset.set(node.transform.getWorldPosition()).sub(hit);
            GizmoState.dragging = true;
        }
        if(MouseInput.isButtonDown(0) && GizmoState.dragging){
            Vector3f hit = EditorCameraUtil.rayPlaneIntersection(
                    rayOrigin, rayDir,
                    GizmoState.dragPlanePoint,
                    GizmoState.dragPlaneNormal);
            if(hit == null) return;

            Vector3f newPos = new Vector3f(hit).add(GizmoState.dragOffset);
            Vector3f localPos = node.transform.worldToLocalPosition(newPos);
            switch(GizmoState.activeAxis){
                case X -> node.transform.position.x = localPos.x;
                case Y -> node.transform.position.y = localPos.y;
                case Z -> node.transform.position.z = localPos.z;
            }
        }
        if(MouseInput.isButtonReleased(0)){
            GizmoState.dragging = false;
            GizmoState.activeAxis = Axis.NONE;
        }
    }

    private void processDeletions() {
        if(EditorSelection.nodesToDelete.isEmpty()){
            return;
        }
        for(Node node : EditorSelection.nodesToDelete){
            node.removeFromParent();
        }
        EditorSelection.nodesToDelete.clear();
    }

    private void drawHierarchy(Engine engine) {
        ImGui.begin("Hierarchy");

        if(ImGui.button("Create Empty")){
            Node newNode = new Node("Empty");
            engine.getActiveScene().getRoot().addChild(newNode);
        }

        ImGui.separator();

        Node root = engine.getActiveScene().getRoot();

        for (Node child : root.getChildren()) {
            drawNodeTree(child);
        }

        ImGui.end();
    }

    private void drawInspector(Engine engine){
        ImGui.begin("Inspector");

        if(EditorSelection.selectedNode == null){
            ImGui.text("Noting selected");
            ImGui.end();
            return;
        }

        Node node = EditorSelection.selectedNode;
        ImGui.text("Node: " + node.name);
        ImGui.separator();

        drawTransform(node);
        drawComponents(node);
        drawAddComponentButton(node);

        ImGui.end();
    }

    private void drawAddComponentButton(Node node) {
        if(ImGui.button("Add Component")){
            ImGui.openPopup("add_component_popup");
        }
        if(ImGui.beginPopup("add_component_popup")){
            for(String name : ComponentRegistry.getComponentNames()){
                if(ImGui.menuItem(name)){
                    Component c = ComponentRegistry.create(name);
                    if(node.getComponent(c.getClass()) == null)
                        node.addComponent(c);
                    ImGui.closeCurrentPopup();
                }
            }
            ImGui.endPopup();
        }
    }

    private void drawComponents(Node node) {
        ImGui.text("Components");
        for(Component c :  node.getComponents()){
            ImGui.separator();
            ImGui.text(c.getType());

            if(c instanceof MeshFilter meshFilter){
                ImGui.text("Mesh: " + meshFilter.meshPath);
            }
            if(c instanceof MaterialRenderer materialRenderer){
                drawMaterialInspector(materialRenderer);
            }
            if(c instanceof Camera cam){
                ImGui.text("Camera");
                boolean primary = cam.primary;
                if(ImGui.checkbox("Primary Camera", primary)){
                    cam.primary = primary;
                }
            }
        }
    }

    private void drawMaterialInspector(MaterialRenderer materialRenderer) {
        Material mat =  materialRenderer.material;

        float[] color = {mat.diffuse.x,  mat.diffuse.y, mat.diffuse.z};

        if(ImGui.colorEdit3("Color", color)){
            mat.diffuse.set(color[0], color[1], color[2]);
        }
        if(mat.albedoPath != null){
            ImGui.text("Texture: " + mat.albedoPath);
        }
    }

    private void drawTransform(Node node) {
        Transform3D tr = node.transform;
        ImGui.text("Transform");
        float[] pos = {tr.position.x, tr.position.y, tr.position.z};
        if(ImGui.dragFloat3("Position", pos))
            tr.position.set(pos[0],  pos[1], pos[2]);

        float[] rot = {tr.rotation.x, tr.rotation.y, tr.rotation.z};
        if(ImGui.dragFloat3("Rotation", rot))
            tr.rotation.set(rot[0],  rot[1], rot[2]);

        float[] scale = {tr.scale.x, tr.scale.y, tr.scale.z};
        if(ImGui.dragFloat3("Scale", scale))
            tr.scale.set(scale[0],  scale[1], scale[2]);

        ImGui.separator();
    }

    private void drawNodeTree(Node node) {
        boolean selected = node == EditorSelection.selectedNode;
        String label = node.name + "##" + node.id;
        boolean opened = ImGui.treeNodeEx(
                label,
                selected ? ImGuiTreeNodeFlags.Selected : 0
                );

        if(ImGui.isItemClicked()){
            EditorSelection.selectedNode = node;
        }

        boolean deleteThisNode = false;

        if(ImGui.beginPopupContextItem()){
            if(ImGui.menuItem("Create Child")){
                Node child = new Node("Empty Child");
                node.addChild(child);
                EditorSelection.selectedNode = child;
            }
            if(node.getParent() != null){
                if(ImGui.menuItem("Delete")){
                    if(EditorSelection.selectedNode == node){
                        EditorSelection.selectedNode = null;
                    }
                    deleteThisNode = true;
                }
            }
            ImGui.endPopup();
        }
        if(opened){
            for(Node child : node.getChildren()){
                drawNodeTree(child);
            }
            ImGui.treePop();
        }
        if(deleteThisNode){
            if(EditorSelection.selectedNode == node){
                EditorSelection.selectedNode = null;
            }
            EditorSelection.nodesToDelete.add(node);
        }
    }

    private void drawToolbar(Engine engine) {
        ImGui.begin("Toolbar");

        if(ImGui.button("New")){
            engine.setEditorScene(new Scene());
            EditorSelection.selectedNode = null;
        }

        ImGui.sameLine();

        if(ImGui.button("Save")){
            SceneSerializer.save(engine.getEditorScene(), "scene.xml");
        }

        ImGui.sameLine();

        if(ImGui.button("Load")){
            Scene loaded = SceneLoader.load("scene.xml");
            engine.setEditorScene(loaded);
            EditorSelection.selectedNode = null;
        }

        ImGui.sameLine();

        if(engine.getMode() == EngineMode.EDITOR){
            if(ImGui.button("Play")){
                engine.enterPlayMode();
            }
        }else{
            if(ImGui.button("STOP")){
                engine.exitPlayMode();
            }
        }
        ImGui.end();
    }
}
