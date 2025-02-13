package my.demo.usage;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioUsage {

    @Test
    void testByteBuffer() {
        try (FileChannel channel = new FileInputStream(new File("src/test/resources/data.txt")).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = channel.read(buffer);
                System.out.println("读取到的字节数：" + len);
                if (len == -1) {
                    break;
                }
            }

            buffer.flip(); // switch to read mode
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                System.out.println("实际字节：{}" + (char) b);
            }
            buffer.clear(); // switch to write mode
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
