package com.spiralstudio.mod.pandora;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorBuilder;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;

/**
 * Enter "/pandora" to call the Pandora.
 * Enter "/clearpandora" to help GC.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.item.client.ArsenalPanel
 * @see com.threerings.projectx.shop.client.u VendorListPanel
 * @see com.threerings.projectx.shop.client.l ShopDialog
 * @see com.threerings.projectx.shop.data.ShopDialogInfo
 * @see com.threerings.projectx.shop.data.UniqueShopInfo
 * @see com.threerings.projectx.data.PlayerObject
 * @see com.threerings.crowd.client.d LocationDirector
 */
public class Bak {
    private static boolean mounted = false;

    static {
        Registers.add(Bak.class);
    }

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        addPreviewApplyListenerClass();
        redefineKnightSpriteToReplacePreviewItems();
        redefineGoodSlotToIgnoreException();
        redefineVendorListPanelToIgnoreException();
        redefineShopDialogToCacheAndRenameTitle();
        redefinePlayerObjectToPreview();
        addPandoraCommand();
    }

    static void addPreviewApplyListenerClass() throws Exception {
        ClassBuilder.makeClass("com.spiralstudio.mod.pandora.PreviewApplyListener")
                .interfaceClassName("com.threerings.opengl.gui.event.a")
                .addField(new FieldBuilder()
                        .fieldName("_ctx")
                        .typeName("com.threerings.projectx.util.A")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                .addConstructor(new ConstructorBuilder()
                        .parameters(new String[]{"com.threerings.projectx.util.A"})
                        .body("{this._ctx=$1; System.out.println(\"PreviewApplyListener New\");}")
                        .modifiers(Modifier.PUBLIC))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public void actionPerformed(com.threerings.opengl.gui.event.ActionEvent actionEvent) {\n" +
                                "            System.out.println(\"[Pandora] Apply actionPerformed:\" + actionEvent.toString());\n" +
                                "            com.threerings.opengl.gui.av _btn = (com.threerings.opengl.gui.av) actionEvent.getSource();\n" +
                                "            System.out.println(\"[Pandora] Apply\" + Boolean.toString(_btn.isSelected()));\n" +
                                "            if (_btn.isSelected()) {\n" +
                                "                System.out.println(\"Selected\");\n" +
                                "                com.threerings.projectx.client.dj whatever = this._ctx.xl();\n" +
                                "                java.lang.reflect.Field agdField = com.threerings.projectx.client.dj.class.getDeclaredField(\"agd\");\n" +
                                "                agdField.setAccessible(true);\n" +
                                "                com.threerings.projectx.data.PlayerEquipment playerEquipment = (com.threerings.projectx.data.PlayerEquipment) agdField.get(whatever);\n" +
                                "                java.lang.reflect.Field itemsField = com.threerings.projectx.data.PlayerEquipment.class.getDeclaredField(\"items\");\n" +
                                "                itemsField.setAccessible(true);\n" +
                                "                java.lang.Object itemsObject = itemsField.get(playerEquipment);\n" +
                                "                if (itemsObject == null) {\n" +
                                "                    return;\n" +
                                "                }\n" +
                                "                java.util.Map itemsMap = (java.util.Map) itemsObject;\n" +
                                "                com.threerings.projectx.item.data.LevelItem[] previewItems = new com.threerings.projectx.item.data.LevelItem[16];\n" +
                                "                for (int i = 0; i < previewItems.length; i++) {\n" +
                                "                    java.lang.Object itemObj = itemsMap.get(Integer.valueOf(i));\n" +
                                "                    if (itemObj != null) {\n" +
                                "                        previewItems[i] = (com.threerings.projectx.item.data.LevelItem) itemObj;\n" +
                                "                        System.out.println(\"Item key=\" + Integer.valueOf(i) + \", value=\" + itemObj.toString());\n" +
                                "                    }\n" +
                                "                }\n" +
                                "                com.threerings.projectx.data.PlayerObject playerObject = this._ctx.uk();\n" +
                                "\n" +
                                "                java.lang.reflect.Field aLsField = com.threerings.projectx.item.data.Item.class.getDeclaredField(\"aLs\");\n" +
                                "                java.lang.reflect.Field aLtField = com.threerings.projectx.item.data.Item.class.getDeclaredField(\"aLt\");\n" +
                                "                java.lang.reflect.Field aLuField = com.threerings.projectx.item.data.Item.class.getDeclaredField(\"aLu\");\n" +
                                "                aLsField.setAccessible(true);\n" +
                                "                aLtField.setAccessible(true);\n" +
                                "                aLuField.setAccessible(true);\n" +
                                "                java.lang.reflect.Field poEquipmentField = com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"equipment\");\n" +
                                "                java.lang.reflect.Field poItemsField = com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"items\");\n" +
                                "                poEquipmentField.setAccessible(true);\n" +
                                "                poItemsField.setAccessible(true);\n" +
                                "                long[] equipment = (long[]) poEquipmentField.get(playerObject);\n" +
                                "                com.threerings.presents.dobj.DSet items = (com.threerings.presents.dobj.DSet) poItemsField.get(playerObject);\n" +
                                "                for (int i = 0; i < equipment.length; i++) {\n" +
                                "                    if (previewItems[i] != null) {\n" +
                                "                        java.lang.Object item = items.f(Long.valueOf(equipment[i]));\n" +
                                "                        if (item != null) {\n" +
                                "                            aLsField.set(previewItems[i], aLsField.get(item));\n" +
                                "                            aLtField.set(previewItems[i], aLtField.get(item));\n" +
                                "                        }\n" +
                                "                        previewItems[i].setDirty(true);\n" +
                                "                    }\n" +
                                "                }\n" +
                                "\n" +
                                "                com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_equipmentPreview\").set(playerObject, previewItems);\n" +
                                "                com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_previewing\").set(playerObject, Boolean.valueOf(true));\n" +
                                "\n" +
                                "                System.out.println(\"Recreate model 1\");\n" +
                                "                com.threerings.crowd.client.d _d = this._ctx.rr();\n" +
                                "                java.lang.reflect.Field _FvField = com.threerings.crowd.client.d.class.getDeclaredField(\"Fv\");\n" +
                                "                _FvField.setAccessible(true);\n" +
                                "                java.lang.Object _FvObj = _FvField.get(_d);\n" +
                                "                if (_FvObj != null) {\n" +
                                "                    System.out.println(\"Recreate model 2\");\n" +
                                "                    com.threerings.crowd.client.t _t = (com.threerings.crowd.client.t) _FvObj;\n" +
                                "                    if (_t instanceof com.threerings.projectx.client.eL) {\n" +
                                "                        com.threerings.projectx.client.eL _el = (com.threerings.projectx.client.eL) _t;\n" +
                                "                        java.lang.reflect.Method _hgMethod = com.threerings.projectx.client.eL.class.getDeclaredMethod(\"hg\", new java.lang.Class[0]);\n" +
                                "                        _hgMethod.setAccessible(true);\n" +
                                "                        _hgMethod.invoke(_el, new Object[0]);\n" +
                                "                    } else {\n" +
                                "                        com.threerings.projectx.client.aC _hud = com.threerings.projectx.client.aC.h(this._ctx);\n" +
                                "                        com.threerings.tudey.a.b.a _actorSprite = _hud.vk().Eq();\n" +
                                "                        com.threerings.projectx.client.sprite.KnightSprite _knightSprite = (com.threerings.projectx.client.sprite.KnightSprite) _actorSprite.OJ();\n" +
                                "                        java.lang.reflect.Field _actorField = com.threerings.projectx.client.sprite.KnightSprite.class.getDeclaredField(\"_actor\");\n" +
                                "                        java.lang.reflect.Field _configPreviewField = com.threerings.projectx.client.sprite.KnightSprite.class.getDeclaredField(\"_configPreview\");\n" +
                                "                        _actorField.setAccessible(true);\n" +
                                "                        _configPreviewField.setAccessible(true);\n" +
                                "                        com.threerings.projectx.data.actor.Knight _knightActor = (com.threerings.projectx.data.actor.Knight) _actorField.get(_knightSprite);\n" +
                                "                        com.threerings.config.ConfigReference _previewConfigRef = com.threerings.projectx.data.PlayerObject.a(\"Character/PC/Default\", playerObject.a(this._ctx.getConfigManager(), true, true, -1), new Object[0]);\n" +
                                "                        _configPreviewField.set(_knightSprite, _previewConfigRef);\n" +
                                "\n" +
                                "                        java.lang.reflect.Field _configPreviewField222 = com.threerings.tudey.a.b.a.class.getDeclaredField(\"_configPreview\");\n" +
                                "                        _configPreviewField222.setAccessible(true);\n" +
                                "                        _configPreviewField222.set(_actorSprite, _previewConfigRef);\n" +
                                "\n" +
                                "                    }\n" +
                                "                    System.out.println(\"Recreate model 3\");\n" +
                                "                }\n" +
                                "            } else {\n" +
                                "                System.out.println(\"Not Selected\");\n" +
                                "                com.threerings.projectx.data.PlayerObject playerObject = this._ctx.uk();\n" +
                                "                com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_equipmentPreview\").set(playerObject, null);\n" +
                                "                com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_previewing\").set(playerObject, Boolean.valueOf(false));\n" +
                                "\n" +
                                "                com.threerings.projectx.client.aC _hud = com.threerings.projectx.client.aC.h(this._ctx);\n" +
                                "                com.threerings.tudey.a.b.a _actorSprite = _hud.vk().Eq();\n" +
                                "                com.threerings.projectx.client.sprite.KnightSprite _knightSprite = (com.threerings.projectx.client.sprite.KnightSprite) _actorSprite.OJ();\n" +
                                "                java.lang.reflect.Field _configPreviewField = com.threerings.projectx.client.sprite.KnightSprite.class.getDeclaredField(\"_configPreview\");\n" +
                                "                _configPreviewField.setAccessible(true);\n" +
                                "                _configPreviewField.set(_knightSprite, null);\n" +
                                "            }\n" +
                                "        }"))
                .build();


    }

    static void redefineKnightSpriteToReplacePreviewItems() throws Exception {
        ClassBuilder.fromClass("com.threerings.tudey.a.b.a")
                .addField(new FieldBuilder()
                        .fieldName("_configPreview")
                        .typeName("com.threerings.config.ConfigReference")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT | Modifier.VOLATILE))
                .modifyMethod(new MethodModifier()
                        .methodName("configUpdated")
                        .paramTypeNames(new String[]{"com.threerings.config.ConfigEvent"})
                        .insertBefore("     System.out.println(\"ActorSprite configUpdated\");\n"))
                .modifyMethod(new MethodModifier()
                        .methodName("updateFromConfig")
                        .insertBefore("Throwable throwable = new Throwable();\n" +
                                "        StackTraceElement[] stackElements = throwable.getStackTrace();\n" +
                                "        StringBuilder sb = new StringBuilder();\n" +
                                "        if (null != stackElements) {\n" +
                                "            for (int i = 0; i < stackElements.length; i++) {\n" +
                                "                sb.append(stackElements[i].getClassName());\n" +
                                "                sb.append(\".\").append(stackElements[i].getMethodName());\n" +
                                "                sb.append(\"(\").append(stackElements[i].getFileName()).append(\":\");\n" +
                                "                sb.append(stackElements[i].getLineNumber()).append(\")\").append(\"\\n\");\n" +
                                "            }\n" +
                                "        }\n" +
                                "        System.out.println(\"ActorSprite updateFromConfig\" + sb.toString() + String.valueOf(this._configPreview) + \"\\n\" + this.toString());\n"))
                /*.modifyMethod(new MethodModifier()
                        .methodName("update")
                        .insertBefore("" +
                                "if (this._configPreview == null) {\n" +
                                "    com.threerings.config.ConfigReference var2 = this._actor.ES();\n" +
                                "    com.threerings.tudey.config.ActorConfig var3 = (com.threerings.tudey.config.ActorConfig) this.aqE.getConfigManager().a(com.threerings.tudey.config.ActorConfig.class, var2);\n" +
                                "    if (this.aZo != var3) {\n" +
                                "        if (this.aZo != null) {\n" +
                                "            this.aZo.removeListener(this);\n" +
                                "        }\n" +
                                "        if ((this.aZo = var3) != null) {\n" +
                                "            this.aZo.addListener(this);\n" +
                                "        }\n" +
                                "        System.out.println(\"ActorSprite update _configPreview == null\");\n" +
                                "        this.updateFromConfig();\n" +
                                "    }\n" +
                                "    this.aZs.c(this._actor);\n" +
                                "} else {\n" +
                                "    System.out.println(\"ActorSprite update _configPreview != null\");\n" +
                                "    com.threerings.config.ConfigReference _old = this._actor.ES();\n" +
                                "    this._actor.setConfig(this._configPreview);\n" +
                                "    this.aZs.c(this._actor);\n" +
                                "    this._actor.setConfig(_old);\n" +
                                "}\n" +
                                "this.aZr.PV().b(this._actor.mH(), this._actor.mI(), 1.0F);\n" +
                                "this.aZr.a(this._actor.Pu().s(this.aqE.getConfigManager()));"))*/
                .modifyMethod(new MethodModifier()
                        .methodName("ca")
                        .paramTypeNames(new String[]{"int"})
                        .insertBefore("if (this._configPreview!=null){return this.previewTick();}"))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public boolean previewTick() {\n" +
                                "    boolean var3 = this.ON();\n" +
                                "    System.out.println(\"tick 1\");\n" +
                                "    if (this.aZs == null) {\n" +
                                "        System.out.println(\"tick 2\");\n" +
                                "        if (!this.isCreated()) {\n" +
                                "            System.out.println(\"tick 3\");\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "        System.out.println(\"tick 4\");\n" +
                                "        java.util.Iterator var4 = this._attachedModels.keySet().iterator();\n" +
                                "\n" +
                                "        boolean flag = true;\n" +
                                "        while (flag) {\n" +
                                "            com.threerings.opengl.model.h var2 = null;\n" +
                                "            do {\n" +
                                "                if (!var4.hasNext()) {\n" +
                                "                    System.out.println(\"tick 5\");\n" +
                                "                    this._view.Oj().f(this.aZr);\n" +
                                "                    this.aZs = this.aZt;\n" +
                                "                    this.update();\n" +
                                "                    this.aZs.EE();\n" +
                                "                    flag = false;\n" +
                                "                    break;\n" +
                                "                }\n" +
                                "                var2 = (com.threerings.opengl.model.h) var4.next();\n" +
                                "            } while (!this.aZq && var2 != this._model);\n" +
                                "            this._view.Oi().add(var2);\n" +
                                "        }\n" +
                                "        System.out.println(\"tick 6\");\n" +
                                "    } else if (this.aZs.a(var3, this._actor)) {\n" +
                                "        System.out.println(\"tick 7\");\n" +
                                "        this.update();\n" +
                                "    }\n" +
                                "    System.out.println(\"tick 8\");\n" +
                                "\n" +
                                "    if (this.aZn == null ? this.aZm.ey(this._view.Ok()) : this._view.On() >= this._actor.Pw()) {\n" +
                                "        System.out.println(\"tick 9\\n\");\n" +
                                "        this.aZs.Eo();\n" +
                                "        this.dispose();\n" +
                                "        return false;\n" +
                                "    } else if ((this.aZn == null ? this._view.Ok() : this._view.On()) >= this.aZp) {\n" +
                                "        System.out.println(\"tick 10\\n\");\n" +
                                "        this.dispose();\n" +
                                "        return false;\n" +
                                "    } else {\n" +
                                "        System.out.println(\"tick 11\\n\");\n" +
                                "        return true;\n" +
                                "    }\n" +
                                "}"))
                .build();


        /*{
            boolean var3 = this.ON();
            System.out.println("tick 1");
            if (this.aZs == null) {
                System.out.println("tick 2");
                if (!this.isCreated()) {
                    System.out.println("tick 3");
                    return true;
                }
                System.out.println("tick 4");
                java.util.Iterator var4 = this._attachedModels.keySet().iterator();
                label55:
                while (true) {
                    com.threerings.opengl.model.h var2;
                    do {
                        if (!var4.hasNext()) {
                            System.out.println("tick 5");
                            this._view.Oj().f(this.aZr);
                            this.aZs = aZt;
                            this.update();
                            this.aZs.EE();
                            break label55;
                        }
                        var2 = (h) var4.next();
                    } while (!this.aZq && var2 != this._model);
                    this._view.Oi().add(var2);
                }
                System.out.println("tick 6");
            } else if (this.aZs.a(var3, this._actor)) {
                System.out.println("tick 7");
                this.update();
            }
            System.out.println("tick 8");

            if (this.aZn == null ? this.aZm.ey(this._view.Ok()) : this._view.On() >= this._actor.Pw()) {
                System.out.println("tick 9");
                this.aZs.Eo();
                this.dispose();
                return false;
            } else if ((this.aZn == null ? this._view.Ok() : this._view.On()) >= this.aZp) {
                System.out.println("tick 10");
                this.dispose();
                return false;
            } else {
                System.out.println("tick 11");
                return true;
            }
        }*/

        ClassBuilder.fromClass("com.threerings.projectx.client.sprite.KnightSprite")
                .addField(new FieldBuilder()
                        .fieldName("_configPreview")
                        .typeName("com.threerings.config.ConfigReference")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT | Modifier.VOLATILE))
                .addField(new FieldBuilder()
                        .fieldName("_replaced")
                        .typeName("boolean")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT | Modifier.VOLATILE))
                /*.modifyMethod(new MethodModifier()
                        .methodName("c")
                        .paramTypeNames(new String[]{"com.threerings.tudey.data.actor.Actor"})
                        .insertBefore("if (this._configPreview != null) {$1.setConfig(this._configPreview);}"))*/
                .modifyMethod(new MethodModifier()
                        .methodName("c")
                        .paramTypeNames(new String[]{"com.threerings.tudey.data.actor.Actor"})
                        .insertBefore("" +
                                "if (this._configPreview != null) {\n" +
                                "            if (!this._configPreview.equals(this._actor.ES())) {\n" +
                                "                this._configPreview.copy(this._actor.ES());\n" +
                                "                System.out.println(\"copy copy copy\");\n" +
                                "            } else {\n" +
                                "                System.out.println(\"equal equal equal\");\n" +
                                "            }\n" +
                                "            super.c($1);\n" +
                                "        }"))
                /*.addMethod(new MethodBuilder()
                        .body("public void setPreviewConfig(com.threerings.config.ConfigReference config) {\n" +
                                "        Object cloned = this._actor.clone();\n" +
                                "        com.threerings.projectx.data.actor.Knight var2 = (com.threerings.projectx.data.actor.Knight) cloned;\n" +
                                "        var2.setConfig(config);\n" +
                                "        com.threerings.projectx.config.ActionDesc var3 = var2.Co();\n" +
                                "        boolean var4 = var2.Cr();\n" +
                                "        if (!com.samskivert.util.N.equals(var3, this._actionDesc) || var4 != this.apW) {\n" +
                                "            com.threerings.config.ConfigReference var5 = var3 == null ? null : var3.bn(!var4);\n" +
                                "            if (this.OP()) {\n" +
                                "                if (var5 != null) {\n" +
                                "                    if (this.apY == null) {\n" +
                                "                        this._model.attach(\"%ROOT%\", this.apY = new com.threerings.opengl.model.h(this.aqE, var5), false);\n" +
                                "                    } else {\n" +
                                "                        this.apY.setConfig(var5);\n" +
                                "                    }\n" +
                                "                } else if (this.apY != null) {\n" +
                                "                    this._model.detach(this.apY);\n" +
                                "                    this.apY = null;\n" +
                                "                }\n" +
                                "                if (var3 instanceof com.threerings.projectx.config.ActionDesc.Energy) {\n" +
                                "                    ((com.threerings.projectx.client.et) this._view).yO().b(this, 0L);\n" +
                                "                } else {\n" +
                                "                    ((com.threerings.projectx.client.et) this._view).yO().aR(this);\n" +
                                "                }\n" +
                                "            }\n" +
                                "            this._actionDesc = var3;\n" +
                                "            this.apW = var4;\n" +
                                "            if (this._actionDesc != null) {\n" +
                                "                int var6 = this._actionDesc.c(this.aqE.getConfigManager());\n" +
                                "                this.aZz.put(2, var6 == 2 ? this.aqg : (com.threerings.tudey.a.b.a.a.a) this.aZz.get(var6));\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }"))*/
                .build();

        /*if (this._configPreview != null) {
            if (!this._configPreview.equals($1.ES())) {
                this._configPreview.copy($1.ES());
                System.out.println("copy copy copy");
            } else {
                System.out.println("equal equal equal");
            }
            super.c($1);
        }*/

        /*{
            super.c($1);
            com.threerings.projectx.data.actor.Knight var2 = (com.threerings.projectx.data.actor.Knight) $1;
            if (this._configPreview != null) {
                var2 = var2.clone();
            }
            com.threerings.projectx.config.ActionDesc var3 = (var2).Co();
            boolean var4 = var2.Cr();
            com.threerings.config.ConfigReference var5 = null;
            if (var3 != null) {
                var5 = var3.bn(var4==false);
            }
            int var6 = 0;
            if (!com.samskivert.util.N.equals(var3, this._actionDesc) || var4 != this.apW) {
                if (this.OP()) {
                    if (var5 != null) {
                        if (this.apY == null) {
                            this._model.attach("%ROOT%", this.apY = new com.threerings.opengl.model.h(this.aqE, var5), false);
                        } else {
                            this.apY.setConfig(var5);
                        }
                    } else if (this.apY != null) {
                        this._model.detach(this.apY);
                        this.apY = null;
                    }

                    if (var3 instanceof com.threerings.projectx.config.ActionDesc.Energy) {
                        ((com.threerings.projectx.client.et) this._view).yO().b(this, 0L);
                    } else {
                        ((com.threerings.projectx.client.et) this._view).yO().aR(this);
                    }
                }

                this._actionDesc = var3;
                this.apW = var4;
                if (this._actionDesc != null) {
                    var6 = this._actionDesc.c(this.aqE.getConfigManager());
                    if (var6 == 2) {
                        this.aZz.put(2, this.aqg);
                    } else {
                        this.aZz.put(2, (com.threerings.tudey.a.b.a.a.a) this.aZz.get(var6));
                    }
                }
            }

            if (this.OP()) {
                if (this.apY != null) {
                    var5 = var2.Cq();
                }
                ((com.threerings.projectx.client.et) this._view).yO().h(var5);
            }

            boolean var12 = this.d(var2);
            if (this.aqb != var12) {
                if (this.aqb = var12) {
                    this._model.setRenderScheme("Translucent");
                    this.aqd.add(avk);
                } else {
                    this._model.setRenderScheme((String) null);
                    this.aqd.remove(avk);
                }

                this._colorState.getColor().set(this.Ak());
                this._colorState.setDirty(true);
            }

            this.Ag();
            this.vT();
            if ((var6 = var2.Cs()) != this.aqh) {
                if ((var4 = this.aqi != null) && this.aqh != 0) {
                    this.Ai();
                }

                this.aqh = var6;
                if (var4 && var6 != 0) {
                    this.a((com.threerings.projectx.data.actor.Lift) this._view.dV(var6).OK());
                }
            }

            if (var6 > 0) {
                com.threerings.math.Transform3D var7;
                com.threerings.tudey.a.b.a var9;
                if ((var9 = this._view.dV(var6)) != null && (var7 = this.b(var9)) != null) {
                    java.util.Iterator var13 = var9.OI().entrySet().iterator();

                    while (var13.hasNext()) {
                        java.util.Map.Entry var11;
                        if ((java.lang.Boolean) (var11 = (java.util.Map.Entry) var13.next()).getValue()) {
                            ((h) var11.getKey()).getLocalTransform().j(var7);
                            ((h) var11.getKey()).updateBounds();
                        }
                    }
                }

                if (this.aqi != null) {
                    boolean var8 = null == var2.Co();
                    this.bg(var8);
                    if (var8) {
                        this.o(this.p(com.threerings.math.Vector2f.OW));
                    }
                }
            }

            if (this.OP()) {
                this.a(var2);
            }

            var4 = var2.isSet(2);
            if (this.aqt == null || this.aqt != var4) {
                java.util.Iterator var10 = this.aqe.get(-1001).iterator();

                while (var10.hasNext()) {
                    com.threerings.opengl.model.a var14 = (com.threerings.opengl.model.a) var10.next();
                    if (var4) {
                        var14.start();
                    } else {
                        var14.stop();
                    }
                }

                this.aqt = var4;
            }

        }*/
    }

    static void redefineGoodSlotToIgnoreException() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.shop.client.GoodSlot")
                .modifyMethod(new MethodModifier()
                        .methodName("KC")
                        .body("{\n" +
                                "    boolean var1 = false;\n" +
                                "    boolean var2 = false;\n" +
                                "    com.threerings.projectx.util.A ctx = $0._ctx;\n" +
                                "    com.threerings.projectx.shop.config.GoodConfig aQs = $0.aQs;\n" +
                                "    com.threerings.projectx.shop.util.ItemPreparer aQu = $0.aQu;\n" +
                                "    if (aQs != null) {\n" +
                                "        try {\n" +
                                "            com.threerings.config.ConfigReference var3 = aQu.prepareItem(ctx, aQs);\n" +
                                "            com.threerings.projectx.item.config.ItemConfig.Original original = com.threerings.projectx.item.data.Item.j(ctx.getConfigManager(), var3);\n" +
                                "            com.threerings.projectx.item.data.Item var4 = original.b((com.threerings.projectx.item.b.a) ctx, var3);\n" +
                                "            var1 = var4.isLocked();\n" +
                                "            var2 = var4 instanceof com.threerings.projectx.item.data.LevelItem;\n" +
                                "        } catch (Throwable e) {\n" +
                                "        }\n" +
                                "    }\n" +
                                "    com.threerings.opengl.gui.q var5 = $0.getComponent(\"bound\");\n" +
                                "    var5.setEnabled(var1);\n" +
                                "    var5.setVisible(var1 || var2);\n" +
                                "}"))
                .build();
    }

    static void redefineVendorListPanelToIgnoreException() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.shop.client.u")
                .modifyMethod(new MethodModifier()
                        .methodName("j")
                        .body("{\n" +
                                "    java.lang.reflect.Field configMapField = com.threerings.projectx.shop.client.e.class.getDeclaredField(\"aQk\");\n" +
                                "    configMapField.setAccessible(true);\n" +
                                "    java.util.Map configMap = configMapField.get((com.threerings.projectx.shop.client.e) $0);\n" +
                                "    com.threerings.projectx.shop.data.UniqueShopInfo usi = $0.aQj.KD();\n" +
                                "    int i = 0;\n" +
                                "    int size = usi.goodCounts.size();\n" +
                                "    for (; i < size; ++i) {\n" +
                                "        com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount goodCount = (com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount) usi.goodCounts.get(i);\n" +
                                "        try {\n" +
                                "            com.threerings.config.ConfigReference itemConfigRef = goodCount.good.FX();\n" +
                                "            com.threerings.projectx.item.config.ItemConfig.Original itemConfigOriginal = com.threerings.projectx.item.data.Item.j($0._ctx.getConfigManager(), itemConfigRef);\n" +
                                "            com.threerings.projectx.client.d.c groupInfo = (com.threerings.projectx.client.d.c) $0.aEL.get(itemConfigOriginal.BA());\n" +
                                "            if (groupInfo != null && $0.bY(itemConfigOriginal.a((com.threerings.projectx.util.A) $0._ctx, itemConfigRef))) {\n" +
                                "                Object goodInfo = configMap.get(goodCount.good);\n" +
                                "                if (goodInfo == null) {\n" +
                                "                    goodInfo = new com.threerings.projectx.shop.client.e.a($0,goodCount.good, goodCount.count, i);\n" +
                                "                    configMap.put(goodCount.good, goodInfo);\n" +
                                "                }\n" +
                                "                groupInfo.abG.put(Integer.valueOf(i), goodInfo);\n" +
                                "            } else {\n" +
                                "                groupInfo.abG.remove(Integer.valueOf(i));\n" +
                                "            }\n" +
                                "        } catch (Exception e) {\n" +
                                "            System.out.println(\"[VendorListPanel] Failed to add item to pandora \" + goodCount.good.toString() + \"\\n\" + e.getMessage());\n" +
                                "        }\n" +
                                "    }\n" +
                                "}"))
                .build();
    }

    static void redefineShopDialogToCacheAndRenameTitle() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.shop.client.l")
                .modifyMethod(new MethodModifier()
                        .methodName("Bp")
                        .body("{\n" +
                                "    if (this.aQw.name == null || !this.aQw.name.toLowerCase().contains(\"pandora\")) {\n" +
                                "        this.atS = new com.threerings.projectx.shop.client.p(this, this._ctx, false);\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    if (this.atS != null) {\n" +
                                "        System.out.println(\"[Pandora] Use Cached VendorPanel\");\n" +
                                "        this._ctx.getRoot().addWindow(this.atS, true);\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    System.out.println(\"[Pandora] Create New VendorPanel\");\n" +
                                "    this.atS = new com.threerings.projectx.shop.client.p(this, this._ctx, false);\n" +
                                "    com.threerings.projectx.shop.client.e abM = (com.threerings.projectx.shop.client.e) this.atS.tH();\n" +
                                "    ((com.threerings.opengl.gui.Label) abM.getComponent(\"title\")).setText(this.aQw.title);\n" +
                                "    com.threerings.opengl.gui.av btnPreview = ((com.threerings.opengl.gui.av) abM.getComponent(\"preview\"));\n" +
                                "    java.lang.reflect.Method registerComponentMethod = com.threerings.opengl.gui.ay.class.getDeclaredMethod(\"registerComponent\",\n" +
                                "            new Class[]{String.class, com.threerings.opengl.gui.q.class});\n" +
                                "    registerComponentMethod.setAccessible(true);\n" +
                                "    com.threerings.opengl.gui.av btnApply = new com.threerings.opengl.gui.av(this._ctx, \"Apply\");\n" +
                                "    btnApply.setStyleConfigs(btnPreview.getStyleConfigs());\n" +
                                "    btnApply.setSize(btnPreview.getWidth(), btnPreview.getHeight());\n" +
                                "    registerComponentMethod.invoke(abM, new Object[]{\"apply\", btnApply});\n" +
                                "    com.threerings.opengl.gui.event.a btnApplyListener =\n" +
                                "            (com.threerings.opengl.gui.event.a) Class.forName(\"com.spiralstudio.mod.pandora.PreviewApplyListener\")\n" +
                                "                    .getConstructors()[0].newInstance(new Object[]{this._ctx});\n" +
                                "    btnApply.addListener$2eebd3b8(btnApplyListener);\n" +
                                "    abM.add(btnApply, (Object) new Integer(1));\n" +
                                "    System.out.println(\"[Pandora] Add Apply Button\");\n" +
                                "}"))
                .build();
    }

    static void redefinePlayerObjectToPreview() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.data.PlayerObject")
                .addField(new FieldBuilder()
                        .fieldName("_equipmentPreview")
                        .typeName("com.threerings.projectx.item.data.LevelItem[]")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                .addField(new FieldBuilder()
                        .fieldName("_previewing")
                        .typeName("boolean")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                .modifyMethod(new MethodModifier()
                        .methodName("cg")
                        .paramTypeNames(new String[]{"int"})
                        .body("{\n" +
                                "    if (this._previewing && this._equipmentPreview != null) {\n" +
                                "        com.threerings.projectx.item.data.LevelItem item = this._equipmentPreview[$1];\n" +
                                "        return item;\n" +
                                "    }\n" +
                                "    long id = this.equipment[$1];\n" +
                                "    if (id == 0L) {\n" +
                                "        return null;\n" +
                                "    }\n" +
                                "    com.threerings.projectx.item.data.Item item = (com.threerings.projectx.item.data.Item) this.items.f(Long.valueOf(id));\n" +
                                "    if (item instanceof com.threerings.projectx.item.data.LevelItem) {\n" +
                                "        return (com.threerings.projectx.item.data.LevelItem) item;\n" +
                                "    }\n" +
                                "    return null;\n" +
                                "}"))
                .build();
    }

    static void addPandoraCommand() {
        // Add a field for caching the window
        Commands.addField("_pandora", "com.threerings.opengl.gui.aE");
        // Add a command "/pandora"
        Commands.addCommand("pandora", "\n" +
                "        com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;\n" +
                "        if (this._pandora != null) {\n" +
                "            ctxxx.getRoot().addWindow(this._pandora);\n" +
                "        } else {\n" +
                "            com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);\n" +
                "            com.threerings.projectx.shop.data.ShopDialogInfo sdi = new com.threerings.projectx.shop.data.ShopDialogInfo();\n" +
                "            sdi.level = 0;\n" +
                "            sdi.type = com.threerings.projectx.shop.data.ShopDialogInfo.Type.CROWN;\n" +
                "            sdi.preparer = com.threerings.projectx.shop.util.ItemPreparer.BASIC;\n" +
                "            sdi.seedKnight = true;\n" +
                "            sdi.shopService = null;\n" +
                "            sdi.model = null;\n" +
                "            sdi.animation = null;\n" +
                "            sdi.name = \"Pandora\";\n" +
                "            sdi.title = \"Pandora\";\n" +
                "            sdi.sourceKey = new com.threerings.tudey.data.EntityKey.Actor(12);\n" +
                "            sdi.sourceTranslation = new com.threerings.math.Vector2f(11.377774F, 11.8713F);\n" +
                "            sdi.sourceRotation = -2.1118479F;\n" +
                "            sdi.sourceTransient = null;\n" +
                "            sdi.sourceCloseAnimation = null;\n" +
                "            com.threerings.projectx.shop.client.l shopDialog = new com.threerings.projectx.shop.client.l(ctxxx, hud.vk(), sdi);\n" +
                "            try {\n" +
                "                java.lang.reflect.Field cfgmgrField = com.threerings.opengl.e.class.getDeclaredField(\"_cfgmgr\");\n" +
                "                cfgmgrField.setAccessible(true);\n" +
                "                com.threerings.config.ConfigManager configManager = (com.threerings.config.ConfigManager) cfgmgrField.get(ctxxx);\n" +
                "\n" +
                "                java.lang.reflect.Field groupsField = com.threerings.config.ConfigManager.class.getDeclaredField(\"_groups\");\n" +
                "                groupsField.setAccessible(true);\n" +
                "                java.util.HashMap groups = (java.util.HashMap) groupsField.get(configManager);\n" +
                "                com.threerings.config.ConfigGroup group = (com.threerings.config.ConfigGroup) groups.get(com.threerings.projectx.item.config.ItemConfig.class);\n" +
                "\n" +
                "                java.lang.reflect.Field configsByNameField = com.threerings.config.ConfigGroup.class.getDeclaredField(\"_configsByName\");\n" +
                "                configsByNameField.setAccessible(true);\n" +
                "                java.util.HashMap configsByName = (java.util.HashMap) configsByNameField.get(group);\n" +
                "                java.lang.Iterable configs = configsByName.values();\n" +
                "                java.util.List goodCounts = new java.util.ArrayList();\n" +
                "                java.util.Iterator iterator = configs.iterator();\n" +
                "                while (iterator.hasNext()) {\n" +
                "                    com.threerings.projectx.item.config.ItemConfig o = (com.threerings.projectx.item.config.ItemConfig) iterator.next();\n" +
                "                    if (o.getName().startsWith(\"Weapon/PvP/\")) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    com.threerings.config.ConfigReference ref = o.getReference();\n" +
                "                    goodCounts.add(new com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount(\n" +
                "                            new com.threerings.projectx.shop.config.GoodConfig.Item(ref),\n" +
                "                            -1));\n" +
                "                }\n" +
                "                com.threerings.projectx.shop.data.UniqueShopInfo usi = new com.threerings.projectx.shop.data.UniqueShopInfo();\n" +
                "                java.lang.reflect.Field goodCountsField = com.threerings.projectx.shop.data.UniqueShopInfo.class.getDeclaredField(\"goodCounts\");\n" +
                "                goodCountsField.setAccessible(true);\n" +
                "                goodCountsField.set(usi, goodCounts);\n" +
                "                java.lang.reflect.Field aQxField = com.threerings.projectx.shop.client.l.class.getDeclaredField(\"aQx\");\n" +
                "                aQxField.setAccessible(true);\n" +
                "                aQxField.set(shopDialog, usi);\n" +
                "            } catch (Exception e) {\n" +
                "                throw new RuntimeException(e);\n" +
                "            }\n" +
                "            this._pandora = shopDialog;\n" +
                "            ctxxx.getRoot().addWindow(shopDialog);\n" +
                "        }");
        // Add a command "/clearpandora" to help GC
        Commands.addCommand("clearpandora", "this._pandora = null;");
    }

    public static void main(String[] args) {
    }
}
