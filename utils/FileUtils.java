package gestorInventario.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    public static List<String> readFileLinesToArray(String fileName) {
        File file = new File(fileName);
        List<String> lines = new ArrayList<>();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
                lines.add(st);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static Object loadBinFile(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            return objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToBinFile(String fileName, Object data) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(data);
            out.close();
            fos.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public static void appendToFile(String fileName, String stringToAppend) {
        try {
            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName, true));
            out.write(stringToAppend + "\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public static boolean removeFile(String fileName) {
        File file = new File(fileName);
        return file.delete();
    }

    public static List<String> getFilesInDir(String dir) {
        File file = new File(dir);

        return Arrays.asList(Objects.requireNonNull(file.list()));
    }
}
