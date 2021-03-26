import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Province {
    // 文件数据
    public static List<Data> data = new ArrayList<>();
    // 省份数据
    public static Set<String> sf = new HashSet<>();

    public static void main(String[] args) throws IOException {
        List<String> sf1 = new ArrayList<>();
        Scanner s = new Scanner(System.in);
        String path = "C:\\Users\\DELL\\Desktop\\%s";
        String in = s.nextLine();
        String[] ss = in.split(" ");
        String name = "";
        if (ss.length == 3){
            name = ss[2];
        }
        toArrayByFileReader(String.format(path,ss[0]));
        List<String> dataList = getData(getSfMap(name));
        writeFile(dataList, String.format(path,ss[1]));
    }


    // 一行一行输出字符串到文件
    public static void writeFile(List<String> outList, String name) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(name));
        if (null != outList && outList.size() > 0){
            outList.forEach(e ->{
                pw.write(e);
                pw.write("\n");
            });

        }
        pw.close();
    }

    // 一行一行读取TXT文档信息
    public static void toArrayByFileReader(String name) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
                String[] ss = str.split("\t");
                data.add(new Data(ss[0],ss[1],Integer.parseInt(ss[2])));
                sf.add(ss[0]);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取省份排序后的list
    public static Map<String,Integer> getSfMap(String name){
        Map<String,Integer> sfMap = new HashMap<>();
        Map<String,Integer> sfDataMap = new LinkedHashMap<>();
        // 获取省份的城市值只和组成Map
        sf.forEach(e->{
            int num = data.stream().filter(data1 -> data1.getSf().equals(e)).mapToInt(Data::getNum).sum();
            sfMap.put(e,num);
        });
        if (null == name || "".equals(name)){

            // 对Map中所有省份按照值大小排序，返回排序后字符串
             sfMap.entrySet().stream()
                    .sorted((e1, e2) -> {
                        int i = e1.getValue() - e2.getValue();
                        if (i > 0){
                            return -1;
                        }else if (i == 0){
                            return e1.getKey().compareTo(e2.getKey());
                        }else {
                            return 1;
                        }
                    })
                    .forEachOrdered(x -> sfDataMap.put(x.getKey(), x.getValue()));
        } else {
            // 根据名字查询指定身份
            sfMap.entrySet().stream().filter(e->e.getKey().equals(name)).forEachOrdered(x -> sfDataMap.put(x.getKey(), x.getValue()));
        }
        return sfDataMap;
    }

    // 获取输出字符串List
    public static List<String> getData(Map<String,Integer> map){
        // 返回最后要输出的字符串List
        List<String> dataList = new ArrayList<>();
        // 根据省份List对省下城市进行排序
        map.forEach((key, value) -> {
            dataList.add(key + "\t" + value);
            // 根据省份获取当前省份下所有城市的 名称和值 组成Map
            Map<String, Integer> csMap = data.stream().filter(data1 -> data1.getSf().equals(key)).collect(Collectors.toMap(Data::getCs, Data::getNum));
            // 对Map按照值大小排序
            List<String> data = csMap.entrySet().stream().sorted((e1, e2) -> {
                int i = e1.getValue() - e2.getValue();
                if (i > 0) {
                    return -1;
                } else if (i == 0) {
                    return e1.getKey().compareTo(e2.getKey());
                } else {
                    return 1;
                }
            }).map(n -> "\t" + n.getKey() + "\t" + n.getValue()).collect(Collectors.toList());
            dataList.addAll(data);
        });
        return dataList;
    }

    // 数据实体类
    static class Data {
        private String sf;
        private String cs;
        private Integer num;

        public Data(String sf, String cs, Integer num) {
            this.sf = sf;
            this.cs = cs;
            this.num = num;
        }

        public String getSf() {
            return sf;
        }

        public void setSf(String sf) {
            this.sf = sf;
        }

        public String getCs() {
            return cs;
        }

        public void setCs(String cs) {
            this.cs = cs;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

}

