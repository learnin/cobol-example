package cobol.sample1;

/**
 * COBOL の 01 WORK-AREA1 を表す。
 *
 * YYYYMMDD（PIC X(8)）と YYYYMMDD-R（REDEFINES YYYYMMDD）が同じメモリを共有する
 * REDEFINES 構造を、8要素の char[] バッキングストアで再現する。
 * どちらのビューからの書き込みも同じ buf[] に反映される。
 *
 * COBOL 元定義:
 *   01 WORK-AREA1.
 *     03 YYYYMMDD PIC X(8).
 *     03 YYYYMMDD-R REDEFINES YYYYMMDD.
 *       05 YYYY PIC X(4).
 *       05 MM   PIC X(2).
 *       05 DD   PIC X(2).
 */
public class WorkArea1 {

    /** YYYYMMDD と YYYYMMDD-R が共有する 8 文字のバッキングストア */
    private final char[] buf = new char[8];

    public WorkArea1() {
        java.util.Arrays.fill(buf, ' ');
    }

    // --- YYYYMMDD: フラットビュー（8文字全体）---

    public String getYYYYMMDD() {
        return new String(buf);
    }

    public void setYYYYMMDD(String value) {
        copyInto(buf, 0, CobolUtil.move(value, 8));
    }

    // --- YYYYMMDD-R: 構造ビュー（同じ buf[] をスライス）---

    public String getYYYY() {
        return new String(buf, 0, 4);
    }

    public void setYYYY(String value) {
        copyInto(buf, 0, CobolUtil.move(value, 4));
    }

    public String getMM() {
        return new String(buf, 4, 2);
    }

    public void setMM(String value) {
        copyInto(buf, 4, CobolUtil.move(value, 2));
    }

    public String getDD() {
        return new String(buf, 6, 2);
    }

    public void setDD(String value) {
        copyInto(buf, 6, CobolUtil.move(value, 2));
    }

    // --- グループ項目レベルの操作 ---

    public String toCobolString() {
        return new String(buf);
    }

    public void fromCobolString(String s) {
        copyInto(buf, 0, CobolUtil.move(s, 8));
    }

    // --- 内部ヘルパー ---

    private static void copyInto(char[] dst, int offset, String s) {
        for (int i = 0; i < s.length(); i++) {
            dst[offset + i] = s.charAt(i);
        }
    }
}
