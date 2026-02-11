package core;

import graphics3d.Mesh3D;
import graphics3d.Vertex3D;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ObjLoader {

    public static Mesh3D load(String path) {

        List<Vector3f> positions = new ArrayList<>();
        List<Vector3f> normals   = new ArrayList<>();

        List<Vertex3D> vertices  = new ArrayList<>();
        List<Integer> indices   = new ArrayList<>();

        Map<String, Integer> vertexMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(ResourceLoader.loadAsStream(path)))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank() || line.startsWith("#")) continue;

                String[] tokens = line.split("\\s+");

                switch (tokens[0]) {

                    case "v":
                        positions.add(new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        ));
                        break;

                    case "vn":
                        normals.add(new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        ));
                        break;

                    case "f":
                        parseFace(tokens, positions, normals,
                                vertices, indices, vertexMap);
                        break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load OBJ: " + path, e);
        }

        return buildMesh(vertices, indices);
    }

    // ===================== FACE PARSING + TRIANGULATION =====================

    private static void parseFace(
            String[] tokens,
            List<Vector3f> positions,
            List<Vector3f> normals,
            List<Vertex3D> vertices,
            List<Integer> indices,
            Map<String, Integer> vertexMap
    ) {
        List<Integer> faceIndices = new ArrayList<>();

        // tokens[0] == "f"
        for (int i = 1; i < tokens.length; i++) {

            String key = tokens[i]; // z.B. "47/1/1"

            Integer index = vertexMap.get(key);
            if (index == null) {

                String[] parts = key.split("/");

                int posIndex  = Integer.parseInt(parts[0]) - 1;
                int normIndex = Integer.parseInt(parts[2]) - 1;

                Vertex3D v = new Vertex3D(
                        positions.get(posIndex),
                        normals.get(normIndex)
                );

                vertices.add(v);
                index = vertices.size() - 1;
                vertexMap.put(key, index);
            }

            faceIndices.add(index);
        }

        // 🔺 Triangulation (Triangle Fan)
        for (int i = 1; i < faceIndices.size() - 1; i++) {
            indices.add(faceIndices.get(0));
            indices.add(faceIndices.get(i));
            indices.add(faceIndices.get(i + 1));
        }
    }

    // ===================== MESH BUILD =====================

    private static Mesh3D buildMesh(
            List<Vertex3D> vertices,
            List<Integer> indices
    ) {
        float[] vertexData = new float[vertices.size() * 6];

        for (int i = 0; i < vertices.size(); i++) {
            Vertex3D v = vertices.get(i);

            vertexData[i * 6 + 0] = v.position.x;
            vertexData[i * 6 + 1] = v.position.y;
            vertexData[i * 6 + 2] = v.position.z;

            vertexData[i * 6 + 3] = v.normal.x;
            vertexData[i * 6 + 4] = v.normal.y;
            vertexData[i * 6 + 5] = v.normal.z;
        }

        int[] indexArray = indices.stream().mapToInt(i -> i).toArray();

        return new Mesh3D(vertexData, indexArray);
    }
}
