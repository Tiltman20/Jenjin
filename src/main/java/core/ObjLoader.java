package core;

import graphics3d.Mesh3D;
import graphics3d.Vertex3D;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ObjLoader {
    public static Mesh3D load(String path){
        List<Vector3f> positions = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        List<Vertex3D> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        Map<String, Integer> vertexMap = new HashMap<>();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(ResourceLoader.loadAsStream(path)))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] tokens = line.split("\\s+");

                switch (tokens[0]){
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
                        indices.addAll(parseFace(tokens));
                        break;
                }
            }
        } catch (IOException e){
            throw new RuntimeException("Failed to load OBJ: " + path, e);
        }
        return buildMesh(positions, normals, indices);
    }

    private static List<Integer> parseFace(String[] tokens){
        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            String key = tokens[i];
            String[] indexArr = key.split("/");
            indices.add(Integer.parseInt(indexArr[0])-1);
//            indices.add(Integer.parseInt(indexArr[2])-1);
        }
        return indices;
    }
    private static Mesh3D buildMesh(
            List<Vector3f> positions,
            List<Vector3f> normals,
            List<Integer> indices
    ){
        float[] vertexData = new float[positions.size() * 6];

        for (int i = 0; i < positions.size(); i++) {
            vertexData[i*6 + 0] = positions.get(i).x;
            vertexData[i*6 + 1] = positions.get(i).y;
            vertexData[i*6 + 2] = positions.get(i).z;

            vertexData[i*6 + 3] = normals.get(i).x;
            vertexData[i*6 + 4] = normals.get(i).y;
            vertexData[i*6 + 5] = normals.get(i).z;
        }

        int[] indicesArray = indices.stream().mapToInt(i -> i).toArray();

        return new Mesh3D(vertexData, indicesArray);
    }
}
