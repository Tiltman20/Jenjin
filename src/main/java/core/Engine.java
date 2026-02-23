package core;

import core.scene.ComponentRegistry;
import core.scene.components.Camera;
import core.scene.components.MaterialRenderer;
import core.scene.components.MeshFilter;
import editor.EditorLayer;
import editor.EditorSelection;
import editor.GizmoRenderer;
import editor.LineRenderer;
import graphics.Renderer;
import graphics3d.Camera3D;
import graphics3d.Framebuffer;
import graphics3d.Material;
import input.Input;
import input.MouseInput;
import org.joml.Vector3f;

public class Engine {
    private boolean running;
    private final Game game;
    private final Timer timer;
    private final Window window;
    private final Renderer renderer;
    private final EditorLayer editor = new EditorLayer();
    private Framebuffer viewportFBO;

    private EngineMode mode = EngineMode.EDITOR;

    private Scene editorScene;
    private Scene runtimeScene;
    private Scene activeScene;

    private int viewportWidth  = 1;
    private int viewportHeight = 1;

    private Framebuffer sceneFBO;
    private Framebuffer gameFBO;

    private int sceneWidth = 1;
    private int sceneHeight = 1;

    private int gameWidth = 1;
    private int gameHeight = 1;

    private Camera3D editorCamera = new Camera3D();

    public Engine(Game game){
        this.game = game;
        this.timer = new Timer();
        this.window = new Window("Jenjin", 1280, 720);
        this.renderer = new Renderer();
    }

    public void start(){
        init();
        loop();
        cleanup();
    }

    public EngineMode getMode(){
        return mode;
    }

    public Camera3D getEditorCamera(){
        return editorCamera;
    }


    public void enterPlayMode(){
        mode = EngineMode.PLAY;

        runtimeScene = editorScene.deepCopy();
        activeScene = runtimeScene;

        //Window.setCursorEnabled(window.getHandle(), false);
    }

    public void exitPlayMode(){
        mode = EngineMode.EDITOR;
        activeScene = editorScene;
        Window.setCursorEnabled(window.getHandle(), true);
    }

    public Scene getActiveScene() {
        return activeScene;
    }

    public Scene getEditorScene() {
        return editorScene;
    }

    public Window getWindow() {
        return window;
    }

    public int getSceneTexture(){
        return sceneFBO.getTexture();
    }

    public int getGameTexture(){
        return gameFBO.getTexture();
    }

    public void setSceneViewportSize(int w, int h){
        sceneWidth = Math.max(w, 1);
        sceneHeight = Math.max(h, 1);
    }

    public void setGameViewportSize(int w, int h){
        gameWidth = Math.max(w, 1);
        gameHeight = Math.max(h, 1);
    }

    public void setEditorScene(Scene editorScene) {
        this.editorScene = editorScene;
        this.activeScene = editorScene;
    }

    private void createTestScene(){

        Node cube = new Node("Cube");
        Node car = new Node("Car");

        cube.transform.position.set(0, 0, -3);
        car.transform.position.set(0, 3, 0);

        // Mesh
        cube.addComponent(new MeshFilter("assets/cube.obj"));
        car.addComponent(new MeshFilter("assets/car.obj"));

        // Material
        Material mat = new Material("lit3d");
        mat.setAlbedo("assets/testTexture.png");
        Material carMat = new Material("lit3d");
        carMat.diffuse.set(new Vector3f(1f, 0f, 0f));

        cube.addComponent(new MaterialRenderer(mat));
        car.addComponent(new MaterialRenderer(carMat));
        cube.addChild(car);

        editorScene.getRoot().addChild(cube);
    }

    private void init(){
        ComponentRegistry.autoRegister("core.scene.components.MeshFilter");
        ComponentRegistry.autoRegister("core.scene.components.MaterialRenderer");
        ComponentRegistry.autoRegister("core.scene.components.Camera");

        window.init();
        renderer.init();

        sceneFBO = new Framebuffer(1280,720);
        gameFBO  = new Framebuffer(1280,720);

        timer.init();
        game.init();
        LineRenderer.init();

        editorScene = new Scene();
        activeScene = editorScene;

         createTestScene();

        viewportFBO = new Framebuffer(1280, 720);

        Window.setCursorEnabled(window.getHandle(), true);

        editor.init(window.getHandle());
        running = true;
    }

    private void loop(){
        while(running){
            float dt = timer.getDeltaTime();

            window.update();
            MouseInput.update();

            if(mode == EngineMode.PLAY)
                activeScene.update(dt);

            render();

            Input.endFrame();
            MouseInput.endFrame();
        }
    }

    private void render() {

        sceneFBO.resize(sceneWidth, sceneHeight);
        gameFBO.resize(gameWidth, gameHeight);

        Scene.worldCamera = editorCamera;
        editorCamera.setAspect((float)sceneWidth/sceneHeight);

        sceneFBO.bind();
        renderer.clear();
        activeScene.render();

        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_DEPTH_TEST);
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_CULL_FACE);

        GizmoRenderer.render(EditorSelection.selectedNode, editorCamera);

        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_DEPTH_TEST);
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_CULL_FACE);

        sceneFBO.unbind(window.getFramebufferWidth(),  window.getFramebufferHeight());


        Camera3D gameCamera = activeScene.findMainCamera();
        if (gameCamera != null){
            Scene.worldCamera = gameCamera;
            gameCamera.setAspect((float)gameWidth/gameHeight);

            gameFBO.bind();
            renderer.clear();
            activeScene.render();
            gameFBO.unbind(window.getFramebufferWidth(),  window.getFramebufferHeight());
        }


        renderer.clear();
        editor.begin();
        editor.renderGUI(this);
        editor.end();
    }

    private void update(float dt) {
        game.update(dt);
    }

    private void cleanup() {
        game.cleanup();
        window.cleanup();
    }

    public void stop(){
        running = false;
    }

    public int getViewportTexture() {
        return viewportFBO.getTexture();
    }

    public void setViewportSize(int w, int h) {
        viewportWidth = Math.max(w, 1);
        viewportHeight = Math.max(h, 1);
    }
}
