package cz.cvut.fit.ostrajava.Compiler;

import cz.cvut.fit.ostrajava.Interpreter.InterpretedMethod;
import cz.cvut.fit.ostrajava.Interpreter.LookupException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomaskohout on 11/12/15.
 */
public class Class {

    final String MAGIC_HEADER = "BANIK";

    protected List<String> flags;

    protected String className;
    protected String superName;

    protected List<Field> fields;
    protected List<Method> methods;

    protected ConstantPool constantPool;

    Class superClass;
    //Including super fields
    Set<Field> allFields;



    public Class(){
        flags = new ArrayList<String>();
        fields = new ArrayList<Field>();
        methods = new ArrayList<Method>();
    }

    public Class(String className, String superName){
        this.className = className;
        this.superName = superName;

        flags = new ArrayList<String>();
        fields = new ArrayList<Field>();
        methods = new ArrayList<Method>();
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void addFields(Field field) {
        this.fields.add(field);
    }

    public List<String> getFlags() {
        return flags;
    }

    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class superClass) {
        this.superClass = superClass;
    }

    public Set<Field> getAllFields(){
        if (allFields == null) {
            allFields = new LinkedHashSet<>();
            if (superClass != null) {
                allFields.addAll(getSuperClass().getAllFields());
            }
            allFields.addAll(getFields());
        }

        return allFields;
    }

    public InterpretedMethod lookupMethod(String descriptor) throws LookupException {
        for (Method method: methods){
            //TODO: Add not only exact match
            if (method.getDescriptor().equals(descriptor)){
                return (InterpretedMethod)method;
            }
        }

        if (superClass != null) {
            return superClass.lookupMethod(descriptor);
        }

        throw new LookupException("Method '" + descriptor + "' not found");
    }

    public int lookupField(String name) throws LookupException {
        int i = 0;

        for (Field field: getAllFields()){
            if (field.getName().equals(name)){
                return i;
            }
            i++;
        }

        throw new LookupException("Field '" + name + "' not found");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(MAGIC_HEADER + "\n");

        for (String flag : flags){
            sb.append(flag + "|");
        }
        sb.append("\n");

        sb.append(className + ">" + superName);
        sb.append("\n");


        return sb.toString();
    }
}
