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
 *     05 DD   PIC 9(2) COMP-3.
 *
 * DD の型変更について:
 *   DD は PIC 9(2) COMP-3（packed decimal）に変更された。
 *   COMP-3 は1バイトに2桁を詰め込むため、実際の COBOL バイト長は YYYY(4)+MM(2)+DD(1) = 7 バイト。
 *   Java シミュレーションでは dd を int で保持し、表示・集団項目操作には
 *   2桁ゼロパディング文字列（getDd()）を使う。
 *   これにより toCobolString() は論理的な8文字表現を維持するが、
 *   実際の COBOL バイナリ表現と完全には一致しない点に注意。
 */
public class HogeDate {

    // 05 YYYY PIC X(4)
    private String yyyy = "    ";
    // 05 MM   PIC X(2)
    private String mm   = "  ";
    // 05 DD   PIC 9(2) COMP-3 — 数値として保持
    private int dd = 0;

    public String getYyyy() { return yyyy; }
    public void   setYyyy(String v) { yyyy = CobolUtil.move(v, 4); }

    public String getMm() { return mm; }
    public void   setMm(String v) { mm = CobolUtil.move(v, 2); }

    /**
     * DD の値を2桁ゼロパディング文字列で返す。
     * DISPLAY "DD: " DD のように使用する。
     */
    public String getDd() { return String.format("%02d", dd); }

    /**
     * 文字列から DD を設定する。
     * MOVE "01" TO DD のような英数字からの代入を数値変換して受け取る。
     */
    public void setDd(String v) {
        try {
            dd = Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            dd = 0;
        }
    }

    /**
     * グループ項目全体を文字列として返す（DISPLAY や MOVE group-to-group で使用）。
     * 論理的な8文字: YYYY(4) + MM(2) + DD表示(2)
     * 実 COBOL では DD が COMP-3(1バイト)なので物理バイト長は7だが、
     * Java シミュレーションでは8文字表現で統一する。
     */
    public String toCobolString() {
        return yyyy + mm + getDd();
    }

    /**
     * 文字列からグループ項目の各フィールドを設定する。
     * MOVE group-to-group の受け側として使用。
     * 8文字文字列を想定: YYYY[0:4] / MM[4:6] / DD表示[6:8]
     */
    public void fromCobolString(String s) {
        s = CobolUtil.move(s, 8);
        yyyy = s.substring(0, 4);
        mm   = s.substring(4, 6);
        setDd(s.substring(6, 8));
    }
}
