
package younger.vrp.topological;

import java.io.*;
import java.util.*;

interface IOperation {
}

abstract class ALNSOperation implements IOperation {
    protected int uu = 0;

    protected ALNSOperation(int uu) {
        this.uu = uu;
    }
}

public class MyDestroy extends ALNSOperation {

    protected MyDestroy(int uu) {
        super(uu);
    }

    public static void main(String[] args) throws IOException {
        // String pro = System.getProperty("user.name");
        Properties pros = System.getProperties();
        System.out.println(pros);

    }
    // MyDestroy obj = new MyDestroy(10);
    // IDestroy[] des_ops = obj.getDestroy();
    // System.out.println(des_ops[0].destroy());
    // System.out.println(des_ops[1].destroy());
    // System.out.println(des_ops[2].destroy());

    public IDestroy[] getDestroy() {
        return new IDestroy[] { A.of(uu), B.of(uu), C.of(uu) };
    }

    public static <T> MyDestroy of(T elem) {
        if (elem instanceof MyDestroy) {
            MyDestroy ielem = (MyDestroy) elem;
            return ielem;
        } else {
            return null;
        }
    }
}

interface IDestroy extends IOperation {
    int destroy();
}

class A extends MyDestroy implements IDestroy {
    A(int uu) {
        super(uu);
    }

    public static A of(int uu) {
        return new A(uu);
    }

    @Override
    public int destroy() {
        return uu + 1;
    }
}

class B extends MyDestroy implements IDestroy {
    B(int uu) {
        super(uu);
    }

    public static B of(int uu) {
        return new B(uu);
    }

    @Override
    public int destroy() {
        return uu + 2;
    }
}

class C extends MyDestroy implements IDestroy {
    C(int uu) {
        super(uu);
    }

    public static C of(int uu) {
        return new C(uu);
    }

    @Override
    public int destroy() {
        return uu + 3;
    }

    public void a() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/default.properties")) {
            // TODO:
        }

        byte[] data;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write("Hello ".getBytes("UTF-8"));
            output.write("world!".getBytes("UTF-8"));
            data = output.toByteArray();
        }
        System.out.println(new String(data, "UTF-8"));
    }

}
