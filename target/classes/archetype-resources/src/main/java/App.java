package $org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Hello world!
 */
public class App {
    public class Main {
        LocalDateTime dateTime;

        public static void main(String[] args) {
            HashMap<Integer, String> gpsList = new HashMap();
            try {
                // Создаем серверный сокет на порту 8080
                ServerSocket serverSocket = new ServerSocket(8080);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
                while (true) {
                    // Ожидаем подключения клиента
                    Socket clientSocket = serverSocket.accept();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
                    // Получаем входной и выходной потоки для обмена данными с клиентом
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Читаем данные из входного потока
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String inputData = reader.readLine();

                    // Обрабатываем запрос и формируем ответ
                    // Макет запроса id:name:lat:long:time
                    String[] strings = inputData.split("/");
                    // Кладем данные в хешмап
                    String formattedDateTime = dateTime.format(formatter);
                    gpsList.put(Integer.getInteger(strings[0]), strings[1] + "/" + strings[2] + "/" + strings[3] + "/" + formattedDateTime);
                    for (String s : gpsList.values())  {
                        String[] data = s.split("/");
                        String dateString = data[4];
                        LocalDateTime date = LocalDateTime.parse(dateString, formatter);
                        Duration duration = Duration.between(dateTime, date);
                        // Проверяем разницу между датами
                        if (duration.toHours() > 2) {
                            // Удаляем дату
                            date2 = null;
                        }
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(gpsList);

                    // Отправляем ответ клиенту
                    PrintWriter writer = new PrintWriter(outputStream);
                    writer.println(json);
                    writer.flush();

                    // Закрываем соединение с клиентом
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
