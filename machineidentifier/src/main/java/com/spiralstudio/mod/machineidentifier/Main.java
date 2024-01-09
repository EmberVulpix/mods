package com.spiralstudio.mod.machineidentifier;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Configs;
import com.spiralstudio.mod.core.MethodModifier;
import com.spiralstudio.mod.core.Registers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leego Yih
 * @see com.threerings.util.G IdentUtil
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineIdentUtil(getMachineIdentifier());
    }

    static void redefineIdentUtil(MachineIdentifier mi) {
        ClassPool.from("com.threerings.util.G") // com.threerings.util.IdentUtil
                .modifyMethod(new MethodModifier()
                        .methodName("QK") // getMachineIdentifier
                        .body("{\n" +
                                "    String appendIdent = \"" + mi.getValue() + "\";\n" +
                                "    try {\n" +
                                "        String[] macArray = com.samskivert.a.c.fj();\n" +
                                "        String macStr = com.samskivert.util.aq.a(macArray) + \",\" + appendIdent;\n" +
                                "        String ident = com.samskivert.util.aq.af(macStr);\n" +
                                "        StringBuilder encoded = new StringBuilder();\n" +
                                "        byte[] identArray = com.threerings.util.G.dD(ident);\n" +
                                "        byte[] checksum = com.threerings.util.G.j(identArray);\n" +
                                "        for (int ii = 0; ii < identArray.length; ++ii) {\n" +
                                "            byte value = (byte) ((identArray[ii] << 2) + checksum[ii]);\n" +
                                "            value = (byte) (value * 29 % 64);\n" +
                                "            encoded.append((char) com.threerings.util.G.bek[value]);\n" +
                                "        }\n" +
                                "        return encoded.toString();\n" +
                                "    } catch (Exception var5) {\n" +
                                "        com.threerings.b.log.f(\"Failed creating ident.\", new java.lang.Object[]{var5});\n" +
                                "        return \"ZHNmYWtsd2VxZmprbGpvZGxrc2pmb2lq\";\n" +
                                "    }\n" +
                                "}"));
    }

    static MachineIdentifier getMachineIdentifier() {
        try {
            return Configs.readYaml("machineidentifier.yml", MachineIdentifier.class);
        } catch (Exception e) {
            System.out.println("Failed to read config: " + e.getMessage());
            return new MachineIdentifier("fuckgreyhaven");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MachineIdentifier {
        private String value;
    }

    public static void main(String[] args) {
    }
}
