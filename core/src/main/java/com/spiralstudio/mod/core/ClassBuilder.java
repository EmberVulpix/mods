package com.spiralstudio.mod.core;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * @author Leego Yih
 */
public class ClassBuilder {
    private static final int MODE_MAKE = 0;
    private static final int MODE_FROM = 1;
    private int mode;
    private ClassPool classPool;
    private String className;
    private String superClassName;
    private Collection<String> interfaceClassNames;
    private Collection<ConstructorBuilder> constructorBuilders;
    private Collection<ConstructorModifier> constructorModifiers;
    private StaticConstructorBuilder staticConstructor;
    private Collection<FieldBuilder> fieldBuilders;
    private Collection<FieldModifier> fieldModifiers;
    private Collection<MethodBuilder> methodBuilders;
    private Collection<MethodModifier> methodModifiers;
    private Consumer<Class<?>> actionOnComplete;
    private Class<?> clazz;

    protected ClassBuilder(String className, int mode) {
        this.className = className;
        this.mode = mode;
    }

    public static ClassBuilder makeClass(String className) {
        return new ClassBuilder(className, MODE_MAKE);
    }

    public static ClassBuilder fromClass(String className) {
        return new ClassBuilder(className, MODE_FROM);
    }

    public ClassBuilder classPool(ClassPool classPool) {
        this.classPool = classPool;
        return this;
    }

    public ClassBuilder superClassName(String superClassName) {
        this.superClassName = superClassName;
        return this;
    }

    public ClassBuilder interfaceClassName(String interfaceClassName) {
        if (this.interfaceClassNames == null) {
            this.interfaceClassNames = new ArrayList<>();
        }
        this.interfaceClassNames.add(interfaceClassName);
        return this;
    }

    public ClassBuilder interfaceClassNames(String... interfaceClassNames) {
        if (this.interfaceClassNames == null) {
            this.interfaceClassNames = new ArrayList<>();
        }
        Collections.addAll(this.interfaceClassNames, interfaceClassNames);
        return this;
    }

    public ClassBuilder interfaceClassNames(Collection<String> interfaceClassNames) {
        if (this.interfaceClassNames == null) {
            this.interfaceClassNames = new ArrayList<>();
        }
        this.interfaceClassNames.addAll(interfaceClassNames);
        return this;
    }

    public ClassBuilder addConstructor(ConstructorBuilder builder) {
        if (constructorBuilders == null) {
            constructorBuilders = new ArrayList<>();
        }
        constructorBuilders.add(builder);
        return this;
    }

    public ClassBuilder addConstructors(Collection<ConstructorBuilder> builders) {
        if (constructorBuilders == null) {
            constructorBuilders = new ArrayList<>();
        }
        constructorBuilders.addAll(builders);
        return this;
    }

    public ClassBuilder modifyConstructor(ConstructorModifier modifier) {
        if (constructorModifiers == null) {
            constructorModifiers = new ArrayList<>();
        }
        constructorModifiers.add(modifier);
        return this;
    }

    public ClassBuilder modifyConstructors(Collection<ConstructorModifier> modifiers) {
        if (constructorModifiers == null) {
            constructorModifiers = new ArrayList<>();
        }
        constructorModifiers.addAll(modifiers);
        return this;
    }

    public ClassBuilder setStaticConstructor(StaticConstructorBuilder builder) {
        staticConstructor = builder;
        return this;
    }

    public ClassBuilder addField(FieldBuilder builder) {
        if (fieldBuilders == null) {
            fieldBuilders = new ArrayList<>();
        }
        fieldBuilders.add(builder);
        return this;
    }

    public ClassBuilder addFields(Collection<FieldBuilder> builders) {
        if (fieldBuilders == null) {
            fieldBuilders = new ArrayList<>();
        }
        fieldBuilders.addAll(builders);
        return this;
    }

    public ClassBuilder modifyField(FieldModifier modifier) {
        if (fieldModifiers == null) {
            fieldModifiers = new ArrayList<>();
        }
        fieldModifiers.add(modifier);
        return this;
    }

    public ClassBuilder modifyFields(Collection<FieldModifier> modifiers) {
        if (fieldModifiers == null) {
            fieldModifiers = new ArrayList<>();
        }
        fieldModifiers.addAll(modifiers);
        return this;
    }

    public ClassBuilder addMethod(MethodBuilder builder) {
        if (methodBuilders == null) {
            methodBuilders = new ArrayList<>();
        }
        methodBuilders.add(builder);
        return this;
    }

    public ClassBuilder addMethods(Collection<MethodBuilder> builders) {
        if (methodBuilders == null) {
            methodBuilders = new ArrayList<>();
        }
        methodBuilders.addAll(builders);
        return this;
    }

    public ClassBuilder modifyMethod(MethodModifier modifier) {
        if (methodModifiers == null) {
            methodModifiers = new ArrayList<>();
        }
        methodModifiers.add(modifier);
        return this;
    }

    public ClassBuilder modifyMethods(Collection<MethodModifier> modifiers) {
        if (methodModifiers == null) {
            methodModifiers = new ArrayList<>();
        }
        methodModifiers.addAll(modifiers);
        return this;
    }

    public ClassBuilder actionOnComplete(Consumer<Class<?>> action) {
        this.actionOnComplete = action;
        return this;
    }

    public Class<?> build() throws NotFoundException, CannotCompileException {
        if (clazz != null) {
            return clazz;
        }
        if (classPool == null) {
            classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        }
        CtClass ctClass;
        if (mode == MODE_MAKE) {
            if (superClassName != null) {
                ctClass = classPool.makeClass(className, classPool.get(superClassName));
            } else {
                ctClass = classPool.makeClass(className);
            }
            if (interfaceClassNames != null && interfaceClassNames.size() > 0) {
                ctClass.setInterfaces(classPool.get(interfaceClassNames.toArray(new String[0])));
            }
        } else {
            ctClass = classPool.get(className);
            if (interfaceClassNames != null && interfaceClassNames.size() > 0) {
                for (CtClass i : classPool.get(interfaceClassNames.toArray(new String[0]))) {
                    ctClass.addInterface(i);
                }
            }
        }
        if (fieldBuilders != null) {
            for (FieldBuilder fb : fieldBuilders) {
                ctClass.addField(fb.declaring(ctClass).build(classPool));
            }
        }
        if (fieldModifiers != null) {
            for (FieldModifier fm : fieldModifiers) {
                fm.declaring(ctClass).build(classPool);
            }
        }
        if (constructorBuilders != null) {
            for (ConstructorBuilder cb : constructorBuilders) {
                ctClass.addConstructor(cb.declaring(ctClass).build(classPool));
            }
        }
        if (constructorModifiers != null) {
            for (ConstructorModifier cm : constructorModifiers) {
                cm.declaring(ctClass).build(classPool);
            }
        }
        if (methodBuilders != null) {
            for (MethodBuilder mb : methodBuilders) {
                ctClass.addMethod(mb.declaring(ctClass).build(classPool));
            }
        }
        if (methodModifiers != null) {
            for (MethodModifier mm : methodModifiers) {
                mm.declaring(ctClass).build(classPool);
            }
        }
        if (staticConstructor != null) {
            staticConstructor.declaring(ctClass).build(classPool);
        }
        clazz = ctClass.toClass();
        ctClass.detach();
        if (actionOnComplete != null) {
            actionOnComplete.accept(clazz);
        }
        return clazz;
    }
}
