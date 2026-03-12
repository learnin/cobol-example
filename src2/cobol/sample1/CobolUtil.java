package cobol.sample1;

/**
 * COBOL ユーティリティ。
 * PIC X(n) の固定幅文字列操作と DISPLAY 命令を提供する。
 */
public final class CobolUtil {

    private CobolUtil() {}

    /**
     * COBOL の MOVE value TO PIC X(width) に相当する。
     * 左詰め・右スペースパッド。width を超える場合は先頭から width 文字を返す。
     */
    public static String move(String value, int width) {
        if (value == null) value = "";
        if (value.length() >= width) return value.substring(0, width);
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }

    /**
     * COBOL の DISPLAY items... に相当する。
     * 全引数を結合して標準出力へ1行出力する。
     */
    public static void display(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String p : parts) sb.append(p);
        System.out.println(sb);
    }
}
