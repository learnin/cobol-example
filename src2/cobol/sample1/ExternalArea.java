package cobol.sample1;

/**
 * COBOL の IS EXTERNAL データ領域を表す静的シングルトン。
 *
 * SAMPLE1 と SUB1 の両プログラムが同一インスタンスを参照することで、
 * COBOL の IS EXTERNAL による共有セマンティクスを再現する。
 *
 * COBOL 元定義:
 *   01 EXT-WORK-AREA IS EXTERNAL.
 *     03 EXT-WORK-DATA PIC X(10).
 */
public class ExternalArea {

    /** SAMPLE1 / SUB1 が共有する唯一のインスタンス */
    public static final ExternalArea INSTANCE = new ExternalArea();

    private ExternalArea() {}

    // 03 EXT-WORK-DATA PIC X(10) — 初期値はスペース
    private String extWorkData = "          ";

    public String getExtWorkData() {
        return extWorkData;
    }

    public void setExtWorkData(String value) {
        extWorkData = CobolUtil.move(value, 10);
    }
}
