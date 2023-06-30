import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DataTransferObject {
    private String name;
    private int age;
    private String phone;

    public DataTransferObject(String name, int age, String phone) {
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


class CRUDOperations {
    private List<DataTransferObject> data = new ArrayList<>();

    public void createData(DataTransferObject dto) {
        data.add(dto);
    }

    public List<DataTransferObject> readData() {
        return data;
    }

    public void updateData(int index, DataTransferObject dto) {
        if (index >= 0 && index < data.size()) {
            data.set(index, dto);
        }
    }

    public void deleteData(int index) {
        if (index >= 0 && index < data.size()) {
            data.remove(index);
        }
    }
}

public class Main {
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_SHOW_ALL = "show all";
    private static final String COMMAND_SAVE_ALL = "save all";
    private static final String COMMAND_CREATE = "create";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_SAVE = "save";
    private static final String JSON_FILE_PATH = "data.json";
    private static final Scanner scanner = new Scanner(System.in);
    private static final CRUDOperations crudOperations = new CRUDOperations();

    public static void main(String[] args) {
        boolean isRunning = true;
        while (isRunning) {
            printCommandPrompt();
            String command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case COMMAND_EXIT -> {
                    System.out.println("Додаток завершує роботу.");
                    isRunning = false;
                }
                case COMMAND_HELP -> printHelp();
                case COMMAND_SHOW_ALL -> showAllData();
                case COMMAND_SAVE_ALL -> saveAllData();
                case COMMAND_CREATE -> createData();
                case COMMAND_DELETE -> deleteData();
                case COMMAND_SAVE -> saveData();
                default -> System.out.println("Невідома команда. Введіть 'help', щоб переглянути список команд.");
            }
        }
    }
    private static void printCommandPrompt() {
        System.out.print("Введіть команду: ");
    }
    private static void printHelp() {
        System.out.println("Список команд:");
        System.out.println("  - Exit: Завершити роботу");
        System.out.println("  - Help: Показати список команд");
        System.out.println("  - Show all: Показати всі контакти");
        System.out.println("  - Save: Зберегти окремий контакт");
        System.out.println("  - Save all: Зберегти всі контакти");
        System.out.println("  - Create: Створити новий контакт");
        System.out.println("  - Delete: Видалити контакт");
    }
    private static void showAllData() {
        List<DataTransferObject> data = crudOperations.readData();
        if (data.isEmpty()) {
            System.out.println("Немає наявних даних.");
        } else {
            System.out.println("Список контактів:");
            for (int i = 0; i < data.size(); i++) {
                DataTransferObject dto = data.get(i);
                System.out.println((i + 1) + ". Ім'я: " + dto.getName() + ", Вік: " + dto.getAge());
            }
        }
    }
    private static void saveAllData() {
        List<DataTransferObject> data = crudOperations.readData();
        try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH)) {
            fileWriter.write("[\n");
            for (int i = 0; i < data.size(); i++) {
                DataTransferObject dto = data.get(i);
                String json = "{\"name\":\"" + dto.getName() + "\",\"age\":" + dto.getAge() + "}";
                fileWriter.write(json);
                if (i < data.size() - 1) {
                    fileWriter.write(",\n");
                } else {
                    fileWriter.write("\n");
                }
            }
            fileWriter.write("]\n");
            System.out.println("Контакти успішно збережені у файл " + JSON_FILE_PATH);
        } catch (IOException e) {
            System.out.println("Помилка при збереженні контактів у файл.");
            e.printStackTrace();
        }
    }
    private static void createData() {
        System.out.print("Введіть ім'я: ");
        String name = scanner.nextLine().trim();

        System.out.print("Введіть вік: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Введіть номер телефону: ");
        String phone = scanner.nextLine().trim();

        DataTransferObject dto = new DataTransferObject(name, age, phone);
        crudOperations.createData(dto);
        System.out.println("Контакт успішно створено.");
    }
    private static void deleteData() {
        System.out.print("Введіть індекс контакту, який потрібно видалити: ");
        int index = Integer.parseInt(scanner.nextLine());

        crudOperations.deleteData(index);
        System.out.println("Контакт успішно видалено.");
    }
    private static void saveData() {
        System.out.print("Введіть індекс контакту, який потрібно зберегти: ");
        int index = Integer.parseInt(scanner.nextLine());

        List<DataTransferObject> data = crudOperations.readData();
        if (index >= 1 && index <= data.size()) {
            DataTransferObject dto = data.get(index - 1);
            try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH)) {
                String json = "{\"name\":\"" + dto.getName() + "\",\"age\":" + dto.getAge() + "}";
                fileWriter.write(json);
                System.out.println("Контакт успішно збережений у файл " + JSON_FILE_PATH);
            } catch (IOException e) {
                System.out.println("Помилка при записі даних у файл.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Недійсний індекс контакту.");
        }
    }
}
