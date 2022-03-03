package utils.exception;

import lombok.Data;

@Data
public class KijiKokoroException extends Throwable {
    private Object object;

    public KijiKokoroException() {
    }

    public KijiKokoroException(String s) {
        super(s);
    }

    public KijiKokoroException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public KijiKokoroException(Throwable throwable) {
        super(throwable);
    }

    public KijiKokoroException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

    public KijiKokoroException(String s, Object object, Throwable throwable) {
        super(s, throwable);
        this.object = object;
    }
}
