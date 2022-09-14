package com.example.system.demo.calculate;


import java.io.*;
import java.util.*;

public class Detection {
    public static void main(String[] args) {
        //从txt文件中读取数据
        List<String> strList1 = getFileContent("E:\\1a黑马资料！！！！！！！\\python数据.txt");
        //网关集合
        HashMap<String, HashSet<String>> lampposts = new HashMap<>();
        //将String数据转换成数组
        int[][] ints = reverse(strToArray(strList1));
        //往网关集合中添加数据
        for (int i = 0; i <ints.length ; i++) {
            HashSet<String> hashSet1 = new HashSet<>();
            for (int j = 0; j < ints[0].length; j++) {
                if(ints[i][j]==1){
                    hashSet1.add(j + "");
                }
            }
            lampposts.put(i + "", hashSet1);
        }
        //sensors存放所有的传感器
        HashSet<String> sensors = new HashSet<>();

        //遍历hashmap，找出所有传感器放入sensors
        Iterator<Map.Entry<String, HashSet<String>>> iterator = lampposts.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, HashSet<String>> next = iterator.next();
            HashSet<String> val = next.getValue();

            val.stream().forEach(a -> sensors.add(a));
        }

        //查看传感器集合中的元素
        //sensors.stream().forEach(a -> System.out.print(a + " "));


        HashMap<String, HashSet<String>> lampposts2 = copyMap(lampposts);

        //近似贪心算法，找到近似最优解
        ArrayList<String> result = greedyAlgorithm(sensors, lampposts);

        //查看近似贪心算法的准确度   resultash.size();
        HashSet<String > resultash = new HashSet<>();
        for (int i = 0; i <result.size() ; i++) {
            HashSet<String> lamKey = lampposts2.get(result.get(i));
            Iterator<String> iterator1 = lamKey.iterator();
            while(iterator1.hasNext()) {
                resultash.add(iterator1.next());
            }
        }

        System.out.println("ssss");

// ------------------------------------------------------------------------------------------//
//        因为txt文件中的数据格式不对，需要转换成300*3000的，所以这里舍弃了
//        for (int i = 0; i < strList1.size(); i++) {
//            HashSet<String> hashSet1 = new HashSet<>();
//            int length = strList1.get(i).replace(" ", "").length();
//            String replace = strList1.get(i).replace(" ", "");
//            for (int j = 0; j < length; j++) {
//                if (replace.charAt(j) == '1') {
//                    hashSet1.add(j + "");
//                }
//            }
//            lampposts.put(i + "", hashSet1);
//
//        }
// ------------------------------------------------------------------------------------------//
        //网关集合
//            HashMap<String, HashSet<String>> lampposts = new HashMap<>();
//            //sensors存放所有的传感器
//            HashSet<String> sensors = new HashSet<>();
//
//            //添加传感器数据
//            add(lampposts);
//
//            //遍历hashmap，找出所有传感器放入sensors
//            Iterator<Map.Entry<String, HashSet<String>>> iterator = lampposts.entrySet().iterator();
//
//            while (iterator.hasNext()){
//                Map.Entry<String, HashSet<String>> next = iterator.next();
//                HashSet<String> val = next.getValue();
//
//                val.stream().forEach(a -> sensors.add(a));
//            }
//
//            sensors.stream().forEach(a -> System.out.print(a + " "));
//
//            //贪心算法，找到近似最优解
//            greedyAlgorithm(sensors,lampposts);
    }

    /**
     * 贪心算法
     *
     * @param sensors   所有传感器集合
     * @param lampposts 网关与网关所覆盖的传感器集合
     */
    public static ArrayList<String> greedyAlgorithm(HashSet<String> sensors, HashMap<String, HashSet<String>> lampposts) {
        //存放选择的网关集合
        ArrayList<String> select = new ArrayList<>();

        //定义一个临时集合，存放遍历过程中网关覆盖的传感器和当前没有覆盖传感器的交集
        HashSet<String> tempSet = new HashSet<>();

        //maxKey，保存一次遍历过程中，能够覆盖最大传感器的对应的网关
        //maxKey != null，加入select
        String maxKey = null;

        while (sensors.size() != 0) {//sensors不为0表示还没覆盖完全
            //遍历lampposts,取出对应的key
            for (String key : lampposts.keySet()) {
                //每进行一次循环，需要清空tempSet
                tempSet.clear();

                //当前key能覆盖的传感器
                HashSet<String> areas = lampposts.get(key);
                tempSet.addAll(areas);
                //求出tempSet与sensors的交集
                tempSet.retainAll(sensors);

                //如果当前集合包含未覆盖传感器的数量，比maxkey指向的集合传感器还多
                //就需要重置maxkey
                if (tempSet.size() > 0 &&
                        (maxKey == null || tempSet.size() > lampposts.get(maxKey).size())) {
                    maxKey = key;
                }
            }
            //maxKey != null，加入select
            if (maxKey != null) {
                select.add(maxKey);
                //将maxKey指向的网关覆盖的传感器从sensors中去除，把maxKey指向的网关去除
                sensors.removeAll(lampposts.get(maxKey));
                lampposts.remove(maxKey);
                //maxKey置空
                maxKey = null;
            }
        }

        //当while循环结束，得到选择结果
        //System.out.println("选择结果：" + select);
        //System.out.println(select.size());

        return select;
    }


    /**
     * 从目标文件中读取灯柱和节点坐标
     *
     * @param path
     * @return
     */
    public static List<String> getFileContent(String path) {

        List<String> strList2 = new ArrayList<>();
        try {
            File file = new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                strList2.add(line);
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strList2;

    }

    // 将矩阵转置
    public static int[][] reverse(int temp[][]) {
        int R = temp.length;
        int C = temp[0].length;
        //上面的两个步骤主要是为了下面这一步实现创建和实例化一个二维数组
        int[][] ans = new int[C][R];
        // 主要的思路就是遍历二维数组
        for(int r = 0; r < R; ++r ){
            for(int c = 0; c < C; ++c){
                ans[c][r] = temp[r][c];
            }
        }
        return ans;
    }

    //将字符串转数组换为矩阵
    public static int[][] strToArray(List<String> list) {
        int length = list.size();
        int width = list.get(0).replace(" ", "").length();
        int[][] a = new int[length][width];
        for (int i = 0; i <length ; i++) {
            for (int j = 0; j <width ; j++) {
                String replace = list.get(i).replace(" ", "");
                a[i][j]=Integer.parseInt(String.valueOf(replace.charAt(j)));
            }
        }
        return a;
    }

    /**
     * 复制map
     */
    public static HashMap<String, HashSet<String>> copyMap(HashMap<String, HashSet<String>> source){
        HashMap<String, HashSet<String>> resultMap=new  HashMap<>();
        //开始从paramMap中复制到resultMap中
        Iterator it=source.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry=(Map.Entry)it.next();
            Object key=entry.getKey();
            if(key!=null && source.get(key)!=null) {
                resultMap.put(key.toString(), source.get(key));
            }
        }

        return resultMap;
    }


    }
