package com.example.opencvexample.asm;

import com.example.opencvexample.annotation.ASMTest;


public class Test {
    @ASMTest
    public static void main(String[] args){
        System.out.println("this is a test!");
//        String s = "{\"id_no\":\"342422198012001122\",\"mobile_no\":\"13666667789\",\"name\":\"徐丽\"}";
//        try {
//            JSONObject jsonObject = new JSONObject(s);
//            System.out.println(jsonObject.get("id_no"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @ASMTest
    public void test(){
        for (int i = 0; i < 10000; i++) {
            String string = ""+i;
            for (int j = 0; j < 1000; j++) {
                String s = ""+j;
                string = s+string;
            }
        }



    }

}
