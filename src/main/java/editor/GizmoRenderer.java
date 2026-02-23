package editor;

import core.Node;
import editor.GizmoState.Axis;
import graphics3d.Camera3D;
import org.joml.Vector3f;

public class GizmoRenderer {
    public static void render(Node selected, Camera3D cam){
        if(selected == null) return;

        Vector3f objPos = selected.transform.getWorldPosition();
        float gizmoScale = GizmoMath.getGizmoScale(cam, objPos);

        Vector3f red   = new Vector3f(1,0,0);
        Vector3f green = new Vector3f(0,1,0);
        Vector3f blue  = new Vector3f(0,0,1);
        Vector3f yellow = new Vector3f(1,1,0);

        Axis hover  = GizmoState.hoverAxis;
        Axis active = GizmoState.activeAxis;

        Vector3f colorX = (hover == Axis.X || active == Axis.X) ? yellow : red;
        Vector3f colorY = (hover == Axis.Y || active == Axis.Y) ? yellow : green;
        Vector3f colorZ = (hover == Axis.Z || active == Axis.Z) ? yellow : blue;

        Vector3f p = objPos;

        Vector3f xEnd = new Vector3f(p).add(gizmoScale, 0, 0);
        Vector3f yEnd = new Vector3f(p).add(0, gizmoScale, 0);
        Vector3f zEnd = new Vector3f(p).add(0, 0, gizmoScale);

        float size = 2.5f;

        LineRenderer.drawLine(
                p,
                xEnd,
                colorX,
                cam
        );

        LineRenderer.drawLine(
                p,
                yEnd,
                colorY,
                cam
        );

        LineRenderer.drawLine(
                p,
                zEnd,
                colorZ,
                cam
        );
    }
}
