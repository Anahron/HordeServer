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


        public static void main(String[] args) {
            HashMap<Integer, String> gpsList = new HashMap();
            try {
                // Создаем серверный сокет на порту 8080
                ServerSocket serverSocket = new ServerSocket(8080);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
                while (true) {
                    // Ожидаем подключения клиента
                    Socket clientSocket = serverSocket.accept();
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
                    LocalDateTime dateTime = LocalDateTime.now();
                    String formattedDateTime = dateTime.format(formatter);
                    gpsList.put(Integer.getInteger(strings[0]), strings[1] + "/" + strings[2] + "/" + strings[3] + "/" + formattedDateTime);
                    // Проходимя по мапе и проверяем на разницу в датах, больше 2х часов удаляем
                    // создание списка ключей для удаления
                    List<Integer> keysToDelete = new ArrayList<>();

                    // проход по всем элементам HashMap
                    for (Map.Entry<Integer, String> entry : gpsList.entrySet()) {
                        Integer key = entry.getKey();
                        String value = entry.getValue();
                        // преобразование значения в LocalDateTime
                        String[] split = inputData.split("/");
                        LocalDateTime dateOld = LocalDateTime.parse(split[4]);
                        // вычисление разницы между текущим временем и временем в HashMap
                        Duration duration = Duration.between(dateOld, dateTime);
                        // проверка на условие больше чем 2 часа
                        if (duration.toHours() > 1) {
                            keysToDelete.add(key);
                        }
                    }
                    // удаление элементов по ключам из списка
                    for (Integer key : keysToDelete) {
                        gpsList.remove(key);
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
