package top.bultrail.markroad.calculate;


import java.io.*;
import java.util.*;

public class Detection {
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
//        System.out.println("选择结果：" + select);
//        System.out.println(select.size());

        return select;
    }

    /**
     * 使用有向贪心计算
     *
     * @return
     */
    public static int[] calByLinner_new(List<String> sensors, List<String> gateways, HashMap<Integer, HashSet<Integer>> hash_int2ints) {
        //存放所有sensor节点id
        LinkedList<Integer> sortList = new LinkedList<>();
        for(int i=0;i<sensors.size();i++){
            String[] tmpStrings= sensors.get(i).split(",");
            int tmpIds = Integer.parseInt(tmpStrings[0]);
            sortList.add(tmpIds);
        }

        // 存放所有网关id
        LinkedList<Integer> sortGateway = new LinkedList<>();
//        HashMap<String, Integer> sortHelp = new HashMap<>();
        for(int i=0;i<gateways.size();i++){
            String[] tmpStrings= gateways.get(i).split(",");
            int tmpGatewayIds = Integer.parseInt(tmpStrings[0]);
            sortGateway.add(tmpGatewayIds);
//            sortHelp.put(tmpStrings[0],i);
        }

        // 与hash_string2strings一样，但此处为LinkedHashMap，表示有序吧
        LinkedHashMap<Integer, HashSet<Integer>> sortMap = new LinkedHashMap<>();
        for(int i = 0;i<sortGateway.size();i++){
            Integer gateway_id = sortGateway.get(i);
            sortMap.put(gateway_id,hash_int2ints.get(gateway_id));
        }

        // 已删除的传感器节点列表
        ArrayList<Integer> del = new ArrayList<>();
        // 存储选中的网关id
        ArrayList<Integer> result = new ArrayList<>();
        for(int i=0;i<sortList.size();i++){
            //遍历至当前sensor节点
            Integer cur_Sensor = sortList.get(i);

            Integer tmpSelected = null;
            int maxL = 0;

            // 遍历网关节点，选择与当前传感器节点连接的传感器节点数量最多的网关节点
            for(Map.Entry<Integer, HashSet<Integer>> entry:sortMap.entrySet()){

                HashSet<Integer> values = entry.getValue();
                // 判断当前网关节点是否连接当前传感器
                if(values.contains(cur_Sensor)){
                    // 计算当前网关节点连接的传感器节点数量（除去已删除的传感器节点）
                    int length = Detection.helpCal_new(del,values);
                    if(length > maxL){
                        tmpSelected = entry.getKey();
                        maxL = length;
                    }
//                    if(value.size() < maxL && !"".equals(tmpSelected)
//                            && entry.getKey().substring(0, 3).equals(tmpSortSensor.substring(0, 3))
//                            && sortHelp.get(entry.getKey()) < sortHelp.get(tmpSelected)){
//                        sortMap.put(entry.getKey(),new HashSet<>());
//                    }
                }
            }
            result.add(tmpSelected);

            // 将选定的网关节点连接的传感器节点从列表中删除
            for(Integer tmpSet : sortMap.get(tmpSelected)){
                if(sortList.contains(tmpSet)){
                    del.add(tmpSet);
                    sortList.remove(tmpSet);
                }
            }

            // 清空选定的网关节点连接的传感器节点列表，避免重复选择
            sortMap.put(tmpSelected,new HashSet<>());
            i=-1;
        }

        // 0、1数组，1代表选中
        int[] sol = new int[gateways.size()];
        for (int i = 0; i < result.size(); i++) {
            sol[result.get(i)] = 1;
        }
        return sol;
    }


    /**
     * 使用有向贪心计算
     *
     * @return
     */
    public static ArrayList<String> calByLinner(List<String> sensors, List<String> gateways, HashMap<String, HashSet<String>> lampposts) {

        //存放所有sensor节点名
        LinkedList<String> sortList = new LinkedList<>();
        for(int i=0;i<sensors.size();i++){
            String[] tmpStrings= sensors.get(i).split(",");
            sortList.add(tmpStrings[0]);
        }

        // 存放所有网关名
        LinkedList<String> sortGateway = new LinkedList<>();
//        HashMap<String, Integer> sortHelp = new HashMap<>();

        for(int i=0;i<gateways.size();i++){
            String[] tmpStrings= gateways.get(i).split(",");
            sortGateway.add(tmpStrings[0]);
//            sortHelp.put(tmpStrings[0],i);
        }

        // 与hash_string2strings一样，但此处为LinkedHashMap，表示有序吧
        LinkedHashMap<String, HashSet<String>> sortMap = new LinkedHashMap<>();
        for(int i = 0;i<sortGateway.size();i++){
            String poll = sortGateway.get(i);
            sortMap.put(poll,lampposts.get(poll));
        }

        // 已删除的传感器节点列表
        ArrayList<String> del = new ArrayList<>();

        ArrayList<String> result = new ArrayList<>();
        for(int i=0;i<sortList.size();i++){
            String tmpSortSensor = sortList.get(i);
            String tmpSortSensor2 = "";
            if(sortList.size() > 1){
                tmpSortSensor2 = sortList.get(i+1);
            }

            String tmpSelected = "";
            int maxL = 0;

            // 遍历网关节点，选择与当前传感器节点连接的传感器节点数量最多的网关节点
            for(Map.Entry<String, HashSet<String>> entry:sortMap.entrySet()){

                HashSet<String> value = entry.getValue();
                // 判断当前网关节点是否连接当前传感器节点或下一个传感器节点
                if(value.contains(tmpSortSensor)){
                    // 计算当前网关节点连接的传感器节点数量（除去已删除的传感器节点）
                    int length = Detection.helpCal(del,value);
                    if(length > maxL){
                        tmpSelected = entry.getKey();
                        maxL = length;
                    }
//                    if(value.size() < maxL && !"".equals(tmpSelected)
//                            && entry.getKey().substring(0, 3).equals(tmpSortSensor.substring(0, 3))
//                            && sortHelp.get(entry.getKey()) < sortHelp.get(tmpSelected)){
//                        sortMap.put(entry.getKey(),new HashSet<>());
//                    }
                }
            }
            result.add(tmpSelected);

            // 将选定的网关节点连接的传感器节点从列表中删除
            for(String tmpSet : sortMap.get(tmpSelected)){
                if(sortList.contains(tmpSet)){
                    del.add(tmpSet);
                    sortList.remove(tmpSet);
                }
            }

            // 清空选定的网关节点连接的传感器节点列表，避免重复选择
            sortMap.put(tmpSelected,new HashSet<>());
            i=-1;
        }

        return result;
    }

    /**
     * 辅助方法，计算传感器节点与网关节点连接的传感器节点数量（除去已删除的传感器节点）
     *
     * @param del 已删除的传感器节点列表
     * @param hs  网关节点连接的传感器节点集合
     * @return 返回传感器节点与网关节点连接的传感器节点数量
     */
    public static int helpCal(ArrayList<String> del, HashSet<String> hs) {
        int count = 0;
        for (String s : hs) {
            if (!del.contains(s)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 辅助方法，计算传感器节点与网关节点连接的传感器节点数量（除去已删除的传感器节点）
     *
     * @param del 已删除的传感器节点列表
     * @param hs  网关节点连接的传感器节点集合
     * @return 返回传感器节点与网关节点连接的传感器节点数量
     */
    public static int helpCal_new(ArrayList<Integer> del, HashSet<Integer> hs) {
        int count = 0;
        for (Integer s : hs) {
            if (!del.contains(s)) {
                count++;
            }
        }
        return count;
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
