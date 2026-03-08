package cobol.sample1;

/**
 * COBOL の日付グループ項目を表す。
 *
 * SAMPLE1 の 03 HOGE-DATE（01 WORK-AREA2 内）と
 * SUB1 の 01 PARAM-DATE（LINKAGE SECTION）は同一構造のため共通クラスとして定義。
 *
 * COBOL 元定義:
 *   03 HOGE-DATE.
 *     05 YYYY PIC X(4).
 *     05 MM   PIC X(2).
 *     05 DD   PIC X(2).
 */
public class HogeDate {

    // 05 YYYY PIC X(4)
    private String yyyy = "    ";
    // 05 MM   PIC X(2)
    private String mm   = "  ";
    // 05 DD   PIC X(2)
    private String dd   = "  ";

    public String getYyyy() { return yyyy; }
    public void   setYyyy(String v) { yyyy = CobolUtil.move(v, 4); }

    public String getMm() { return mm; }
    public void   setMm(String v) { mm = CobolUtil.move(v, 2); }

    public String getDd() { return dd; }
    public void   setDd(String v) { dd = CobolUtil.move(v, 2); }

    /**
     * グループ項目全体を文字列として返す（DISPLAY や MOVE group-to-group で使用）。
     * 合計 8 文字: YYYY(4) + MM(2) + DD(2)
     */
    public String toCobolString() {
        return yyyy + mm + dd;
    }

    /**
     * 8 文字の文字列からグループ項目の各フィールドを設定する。
     * MOVE group-to-group の受け側として使用。
     */
    public void fromCobolString(String s) {
        s = CobolUtil.move(s, 8);
        yyyy = s.substring(0, 4);
        mm   = s.substring(4, 6);
        dd   = s.substring(6, 8);
    }
}
