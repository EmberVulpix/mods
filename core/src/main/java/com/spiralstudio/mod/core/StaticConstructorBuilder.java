package com.spiralstudio.mod.core;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * @author Leego Yih
 */
public class StaticConstructorBuilder {
    private CtClass declaring;
    private String body;

    public StaticConstructorBuilder() {
    }

    public StaticConstructorBuilder declaring(CtClass declaring) {
        this.declaring = declaring;
        return this;
    }

    public StaticConstructorBuilder body(String body) {
        this.body = body;
        return this;
    }

    public CtConstructor build(ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtConstructor constructor = declaring.makeClassInitializer();
        if (this.body != null) {
            constructor.setBody(body);
        }
        return constructor;
    }
}
