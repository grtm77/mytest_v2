package top.bultrail.markroad.util;

import java.util.ArrayList;

public class Demo01 {

    public static void main(String[] args) {
        String s = "abcacb";
        String p = "ab";
        int[] removable = {3,4,2,5};

        System.out.println(maximumRemovals(s,p,removable));

    }
    public static int maximumRemovals(String s, String p, int[] removable) {
        char[] sChar = s.toCharArray();
        char[] pChar = p.toCharArray();
        ArrayList<Character> ls = new ArrayList<>();
        ArrayList<Character> lp = new ArrayList<>();
        for(char c : sChar){
            ls.add(c);
        }
        for(char c : pChar){
            lp.add(c);
        }
        int count=removable.length;
        for(int i=0;i<removable.length;i++){
            ArrayList<Character> tmp=new ArrayList<>(ls);
            for(int j=0;j<=i;j++){
                tmp.set(removable[j],'A');
            }
            for(int j=0;j<tmp.size();j++){
                if(tmp.get(j)=='A')tmp.remove(j);
            }

            if(!helper(tmp,lp)){
                count = i;
                break;
            }
        }
        return count;
    }

    public static boolean helper(ArrayList<Character> ls,ArrayList<Character> lp){
        ArrayList<Character> lss = new ArrayList<>(ls);
        ArrayList<Character> lsp = new ArrayList<>(lp);

        for(int i=0;i<lss.size();i++){
            if(lsp.size()!=0 && lss.get(i) == lsp.get(0)){
                lsp.remove(0);
            }
        }
        return lsp.size()==0 ? true : false;
    }


}