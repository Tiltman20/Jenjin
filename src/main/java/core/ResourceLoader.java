package core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.lwjgl.BufferUtils;


public class ResourceLoader {
    public static String load(String path){
        InputStream is = ResourceLoader.class
            .getClassLoader()
            .getResourceAsStream(path);
        if (is == null){
            throw new RuntimeException("Resource not found: " + path);
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(is, StandardCharsets.UTF_8)))
        {
            return reader.lines().collect(Collectors.joining("\n"));
        }catch(Exception e){
            throw new RuntimeException("Failed to load resource: " + path, e);
        }
    }
    public static ByteBuffer loadAsByteBuffer(String path) {
        try (
                InputStream is = ResourceLoader.class
                        .getClassLoader()
                        .getResourceAsStream(path)
        ) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + path);
            }

            ReadableByteChannel channel = Channels.newChannel(is);
            ByteBuffer buffer = BufferUtils.createByteBuffer(8 * 1024);

            while (true) {
                int bytes = channel.read(buffer);
                if (bytes == -1) break;

                if (buffer.remaining() == 0) {
                    buffer = resize(buffer, buffer.capacity() * 2);
                }
            }

            buffer.flip();
            return buffer;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + path, e);
        }
    }

    private static ByteBuffer resize(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static InputStream loadAsStream(String path) {
        InputStream is = ResourceLoader.class
                .getClassLoader()
                .getResourceAsStream(path);

        if (is == null) {
            throw new RuntimeException("Resource not found: " + path);
        }

        return is;
    }
}
