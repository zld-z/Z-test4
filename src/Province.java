import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Province {
    // �ļ�����
    public static List<Data> data = new ArrayList<>();
    // ʡ������
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


    // һ��һ������ַ������ļ�
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

    // һ��һ�ж�ȡTXT�ĵ���Ϣ
    public static void toArrayByFileReader(String name) {
        // ʹ��ArrayList���洢ÿ�ж�ȡ�����ַ���
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // ���ж�ȡ�ַ���
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

    // ��ȡʡ��������list
    public static Map<String,Integer> getSfMap(String name){
        Map<String,Integer> sfMap = new HashMap<>();
        Map<String,Integer> sfDataMap = new LinkedHashMap<>();
        // ��ȡʡ�ݵĳ���ֵֻ�����Map
        sf.forEach(e->{
            int num = data.stream().filter(data1 -> data1.getSf().equals(e)).mapToInt(Data::getNum).sum();
            sfMap.put(e,num);
        });
        if (null == name || "".equals(name)){

            // ��Map������ʡ�ݰ���ֵ��С���򣬷���������ַ���
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
            // �������ֲ�ѯָ�����
            sfMap.entrySet().stream().filter(e->e.getKey().equals(name)).forEachOrdered(x -> sfDataMap.put(x.getKey(), x.getValue()));
        }
        return sfDataMap;
    }

    // ��ȡ����ַ���List
    public static List<String> getData(Map<String,Integer> map){
        // �������Ҫ������ַ���List
        List<String> dataList = new ArrayList<>();
        // ����ʡ��List��ʡ�³��н�������
        map.forEach((key, value) -> {
            dataList.add(key + "\t" + value);
            // ����ʡ�ݻ�ȡ��ǰʡ�������г��е� ���ƺ�ֵ ���Map
            Map<String, Integer> csMap = data.stream().filter(data1 -> data1.getSf().equals(key)).collect(Collectors.toMap(Data::getCs, Data::getNum));
            // ��Map����ֵ��С����
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

    // ����ʵ����
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

