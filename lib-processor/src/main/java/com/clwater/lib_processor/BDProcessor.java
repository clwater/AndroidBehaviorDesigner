package com.clwater.lib_processor;

import com.clwater.lib.BDTestAnnotation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * @date: 2022/12/23
 */
@AutoService(Processor.class)
public class BDProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Filer mFiler;//文件类

    private static final Map<TypeElement, HashMap<String, String>> testFields = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new HashSet<>();
        stringSet.add(BDTestAnnotation.class.getCanonicalName());//返回需要注解的类名
        return stringSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "SourceVersion.latestSupported(): "+ SourceVersion.latestSupported());
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();//初始化文件对象
        mMessager = processingEnvironment.getMessager();//初始化信息对象

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获取注解中的元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BDTestAnnotation.class);
        //遍历元素
        for (Element element : elements) {
            //因为注解的作用域是成员变量，所以这里可以直接强转成 VariableElement
            VariableElement variableElement = (VariableElement) element;
            //权限修饰符
            Set<Modifier> modifiers = variableElement.getModifiers();
            //类型检查
            if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "成员变量的类型不能是PRIVATE或者PROTECTED");
                return false;
            }
            //获得外部元素对象
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            genInfo(typeElement, variableElement);

        }
        writeToFile();

        return false;
    }

    private void writeToFile() {
        Set<TypeElement> typeElements = testFields.keySet();
        String paramName = "target";
        for (TypeElement typeElement : typeElements) {
            ClassName className = ClassName.get(typeElement);//获取参数类型
            PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();//获得外部对象
            String packageName = packageElement.getQualifiedName().toString();//获得包名
            HashMap<String, String> viewInfos = testFields.get(typeElement);
            CodeBlock.Builder builder = CodeBlock.builder();//代码块对象
//            builder.add("        Log.d(\"gzb\", \"6\");\n");
            for (Map.Entry<String, String> entry : viewInfos.entrySet()) {
                builder.add(paramName + "." + entry.getKey() + " = \"" + entry.getValue() + "\";\n");
            }

            ClassName list = ClassName.get("android.util", "Log");

            FieldSpec fieldSpec = FieldSpec.builder(String.class,"name",Modifier.PRIVATE).build();//成员变量

            MethodSpec methodSpec = MethodSpec.constructorBuilder()//生成的方法对象
                    .addModifiers(Modifier.PUBLIC)//方法的修饰符
                    .addParameter(className, paramName)//方法中的参数，第一个是参数类型，第二个是参数名
                    .addCode(builder.build())//方法体重的代码
                    .addStatement("$T.d(\"gzb\", \"6\")",list)
//                    .addStatement("$T result = new $T<>()", list)
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName().toString() + BDTestAnnotation.SUFFIX)//类对象，参数：类名
                    .addMethod(methodSpec)//添加方法
                    .addField(fieldSpec)//添加成员变量
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();//javaFile对象，最终用来写入的对象，参数1：包名；参数2：TypeSpec

            try {
                javaFile.writeTo(mFiler);//写入文件
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void genInfo(TypeElement typeElement, VariableElement variableElement){
        BDTestAnnotation annotation = variableElement.getAnnotation(BDTestAnnotation.class);
        HashMap<String, String> viewInfos;
        String viewName = variableElement.getSimpleName().toString();
        if (testFields.get(typeElement) != null) {
            viewInfos = testFields.get(typeElement);
        } else {
            viewInfos = new HashMap<>();
        }
        viewInfos.put(viewName, annotation.value());
        testFields.put(typeElement, viewInfos);
    }
}
