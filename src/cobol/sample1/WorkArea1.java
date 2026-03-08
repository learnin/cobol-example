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
        // 初期値はスペース
        java.util.Arrays.fill(buf, ' ');
    }

    // --- YYYYMMDD: フラットビュー（8文字全体）---

    /** YYYYMMDD の値を返す */
    public String getYYYYMMDD() {
        return new String(buf);
    }

    /** YYYYMMDD に値を設定する */
    public void setYYYYMMDD(String value) {
        copyInto(buf, 0, CobolUtil.move(value, 8));
    }

    // --- YYYYMMDD-R: 構造ビュー（同じ buf[] をスライス）---

    /** YYYYMMDD-R の YYYY（buf[0..3]）を返す */
    public String getYYYY() {
        return new String(buf, 0, 4);
    }

    /** YYYYMMDD-R の YYYY（buf[0..3]）に値を設定する */
    public void setYYYY(String value) {
        copyInto(buf, 0, CobolUtil.move(value, 4));
    }

    /** YYYYMMDD-R の MM（buf[4..5]）を返す */
    public String getMM() {
        return new String(buf, 4, 2);
    }

    /** YYYYMMDD-R の MM（buf[4..5]）に値を設定する */
    public void setMM(String value) {
        copyInto(buf, 4, CobolUtil.move(value, 2));
    }

    /** YYYYMMDD-R の DD（buf[6..7]）を返す */
    public String getDD() {
        return new String(buf, 6, 2);
    }

    /** YYYYMMDD-R の DD（buf[6..7]）に値を設定する */
    public void setDD(String value) {
        copyInto(buf, 6, CobolUtil.move(value, 2));
    }

    // --- グループ項目レベルの操作 ---

    /** グループ項目全体を文字列として返す（MOVE group-to-group の送り側） */
    public String toCobolString() {
        return new String(buf);
    }

    /** 8 文字の文字列からグループ項目全体を設定する（MOVE group-to-group の受け側） */
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
