package cobol.sample1;

/**
 * COBOL の 01 WORK-AREA2 を表す。
 *
 * COBOL 元定義:
 *   01 WORK-AREA2.
 *     03 HOGE-DATE.
 *       05 YYYY PIC X(4).
 *       05 MM   PIC X(2).
 *       05 DD   PIC X(2).
 */
public class WorkArea2 {

    /** 03 HOGE-DATE — 多階層アクセス: DD OF HOGE-DATE OF WORK-AREA2 → hogeDate.getDd() */
    public final HogeDate hogeDate = new HogeDate();

    /** グループ項目全体を文字列として返す（DISPLAY や MOVE group-to-group で使用）*/
    public String toCobolString() {
        return hogeDate.toCobolString();
    }

    /** 文字列からグループ項目全体を設定する（MOVE group-to-group の受け側） */
    public void fromCobolString(String s) {
        hogeDate.fromCobolString(s);
    }
}
