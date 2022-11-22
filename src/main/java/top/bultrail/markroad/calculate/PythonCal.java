package top.bultrail.markroad.calculate;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class PythonCal {
    static HashMap<String, ArrayList<String>> hg;
    static HashMap<String, Integer> hl;
    static HashMap<String, String> hs;
    static HashMap<String, String> hg_r;
    static String names;

    /**
     * 写入文本的脚本
     * 测试，后面直接写到list更好
     *
     * @param
     */
    public void writeFile() throws IOException {
        File file = new File("C:\\Users\\THTF\\Desktop\\result.txt");
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);
    }
//    public static void main(String[] args) {
//        PythonCal pythonCal = new PythonCal();
//        List<String> result = pythonCal.calByPython();
//        result.forEach(System.out::println);
//        String property = System.getProperty("user.dir");
//        System.out.println(property+"\\src\\main\\java\\com\\example\\system\\demo\\calculate");
//    }

    /**
     * 使用python脚本计算
     *
     * @return
     */
    public List<String> calByPython() {

        List<String> result = new LinkedList<>();
        String property = System.getProperty("user.dir");
        // 写入文件
//        try {
//            new PythonCal().writeFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            String[] args1 = new String[]{"python", property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\tets01_Copy.py",
                    property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\gateWayData.txt",
                    property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\sensorData.txt"};
            Process pr = Runtime.getRuntime().exec(args1);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line;
            boolean flag = false;
            while ((line = in.readLine()) != null) {
                if (flag == true) {
                    result.add(line.replace("[", "")
                            .replace("]", "")
                            .replace("'", "")
                    );
                }
                if ("12345".equals(line)) {
                    flag = true;
                }
            }
            in.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //result.forEach(System.out::println); //输出结果看一看
        return result;
    }



    // 从txt文件中读取数据到list集合
    public static List<String> getFileContent(String path) {
        List<String> strList = new ArrayList<String>();
        try {
            File file = new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                strList.add(line);
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strList;
    }

}
