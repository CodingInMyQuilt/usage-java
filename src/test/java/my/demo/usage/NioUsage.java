package my.demo.usage;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NioUsage {

    @Test
    void testByteBuffer() {
        try (FileChannel channel = new RandomAccessFile("src/test/resources/data.txt", "rw").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(30); // get ByteBuffer obj
            while (true) {
                int len = channel.read(buffer); // write data to ByteBuffer obj
                System.out.println("读取到的字节数：" + len);
                if (len == -1) {
                    break;
                }
            }
            buffer.put("789".getBytes()); // write data to ByteBuffer obj

            buffer.flip();
            while (buffer.hasRemaining()) {
                byte b = buffer.get(); // read data from ByteBuffer obj
                System.out.println("实际字节：{}" + (char) b);
            }
            buffer.rewind();
            channel.write(buffer); // read data from ByteBuffer obj
            channel.position(0);
            buffer.clear();
            while (true) {
                int len = channel.read(buffer); // write data to ByteBuffer obj

                System.out.println("读取到的字节数：" + len);
                if (len == -1) {
                    break;
                }
            }
            buffer.flip();
            byte[] data = new byte[10];
            // length of array can not be longer than the capacity of bytebuffer
            buffer.get(data); // read data from ByteBuffer obj
            System.out.println(new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testByteBuffer2String() {
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());
        buffer.flip();
        String str1 = StandardCharsets.UTF_8.decode(buffer).toString();

        String str2 = new String(buffer.array());
    }

    @Test
    void testString2ByteBuffer() {

        ByteBuffer buffer1 = ByteBuffer.allocate(30);
        buffer1.put("hello world".getBytes());

        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello world");

        ByteBuffer buffer3 = ByteBuffer.wrap("hello world".getBytes());
    }

    @Test
    void testScatteringRead() {
        try (FileChannel channel = new RandomAccessFile("src/test/resources/data.txt", "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(2);
            ByteBuffer b2 = ByteBuffer.allocate(2);
            ByteBuffer b3 = ByteBuffer.allocate(2);
            channel.read(new ByteBuffer[]{b1,b2,b3});
            b1.flip();
            b2.flip();
            b3.flip();
            System.out.println(new String(b1.array()));
            System.out.println(new String(b2.array()));
            System.out.println(new String(b3.array()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGatheringWrite() {
        try (FileChannel channel = new RandomAccessFile("src/test/resources/data.txt", "rw").getChannel()) {
            ByteBuffer b1 = ByteBuffer.wrap("hello world111".getBytes());
            ByteBuffer b2 = ByteBuffer.wrap("hello world222".getBytes());
            ByteBuffer b3 = ByteBuffer.wrap("hello world333".getBytes());
            channel.write(new ByteBuffer[]{b1,b2,b3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
