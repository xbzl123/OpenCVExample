package com.example.opencvexample.asm;


import org.junit.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



public class ASMTest {

    @Test
    public void test(){
        try {

            FileInputStream fio = new FileInputStream("D:\\OpenCV-android-sdk\\OpenCVExample\\app\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes\\com\\example\\opencvexample\\asm\\Test.class");
            //类读取器
            ClassReader classReader = new ClassReader(fio);
            //类存储器
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            //加载类访问器
            classReader.accept(new MyClassVisitor(Opcodes.ASM7,classWriter), ClassReader.EXPAND_FRAMES);
            //获取字节
            byte[] tmp = classWriter.toByteArray();
            FileOutputStream fos = new FileOutputStream("D:\\OpenCV-android-sdk\\OpenCVExample\\app\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes\\com\\example\\opencvexample\\asm\\Test1.class");
            fos.write(tmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //分析类
    static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor(int api) {
            super(api);
        }

        public MyClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        //访问类里面的方法
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
//            System.out.println("the method is : "+name+", and descriptor : "+descriptor+",signature ："+signature);
            return new MyMethodVisitor(api,methodVisitor,access,name,descriptor);
        }

    }

    //分析方法
    static class MyMethodVisitor extends AdviceAdapter {
        /**
         */
        long startTime = 0;
        String methodName = "";
        boolean isSelect;
        protected MyMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
            methodName = name;

        }

        //方法的开始入口
        @Override
        protected void onMethodEnter() {
            if(!isSelect)return;
            startTime = System.currentTimeMillis();
            super.onMethodEnter();
        }

        //方法的结束出口
        @Override
        protected void onMethodExit(int opcode) {
            if(isSelect)
            System.out.println( "The name of the method is :"+methodName+" , the spend time is :"+(System.currentTimeMillis()-startTime));
            super.onMethodExit(opcode);
        }

        //访问方法的注解属性
        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            System.out.println(" descriptor : "+descriptor);

            if(descriptor.contains("ASMTest")){
                isSelect = true;
            }
            return super.visitAnnotation(descriptor, visible);
        }
    }

}
