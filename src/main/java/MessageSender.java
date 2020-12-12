import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageSender {

    private final static String QUEUE_NAME = "Prod_Queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();

        /*
         * Here we connect to a broker on the local machine - hence the localhost. If we wanted to
         * connect to a broker on a different machine we'd simply specify its name or IP address
         * here.
         */
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel())

        {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            Product product = new Product();
            product.setProdId(100);
            product.setProdName("Laptop");

            byte[] byteArray = getByteArray(product);

            channel.basicPublish("", QUEUE_NAME, null, byteArray);
            System.out.println(" [x] Sent '" + product + "'");
        }
        catch (Exception exe)
        {
            exe.printStackTrace();
        }
    }

    private static byte[] getByteArray(Product product) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(product);
        return out.toByteArray();
    }


}
