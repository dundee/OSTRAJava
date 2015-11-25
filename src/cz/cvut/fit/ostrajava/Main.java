
package cz.cvut.fit.ostrajava;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import cz.cvut.fit.ostrajava.Compiler.*;
import cz.cvut.fit.ostrajava.Compiler.Class;
import cz.cvut.fit.ostrajava.Interpreter.ClassPool;
import cz.cvut.fit.ostrajava.Interpreter.OSTRAJavaInterpreter;
import cz.cvut.fit.ostrajava.Parser.*;


public class Main {

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0) {
            System.out.println("Include filename in the arguments");
            return;
        }

        List<Node> rootNodeList = parse(args);


        List<Class> classList = new ArrayList<>();
        OSTRAJavaCompiler compiler = new OSTRAJavaCompiler();

        //First stage - precompilation
        for (Node node: rootNodeList){
            classList.addAll(compiler.precompile(node));
        }

        //Second stage - compilation
        ClassPool classPool = new ClassPool(classList);

        classList.clear();

        for (Node node: rootNodeList){
            classList.addAll(compiler.compile(node, classPool));
        }

        OSTRAJavaInterpreter interpreter = new OSTRAJavaInterpreter(classList);
        interpreter.run();

    }

    protected static List<Node> parse(String[] filenames) throws FileNotFoundException, ParseException {
        Reader fr = null;
        OSTRAJavaParser jp = null;

        List<Node> rootNodeList = new ArrayList<>();

        for (String fileName: filenames) {

            fr = new InputStreamReader(new FileInputStream(new File(fileName)));

            if (jp == null){
                jp = new OSTRAJavaParser(fr);
            }else{
                jp.ReInit(fr);
            }

            try {
                //Parse
                jp.CompilationUnit();
                ASTCompilationUnit node = (ASTCompilationUnit) jp.rootNode();

                rootNodeList.add(node);
            } catch (ParseException e) {
                System.out.println("Parsing exception in file " + fileName);
                throw e;
            }
        }

        return rootNodeList;
    }


}
