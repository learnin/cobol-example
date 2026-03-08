package cobol.sample1;

/**
 * COPY "COPY1" の展開: 01 COPY-WORK-AREA1。
 *
 * COBOL 元定義（copy1.cpy）:
 *   01 COPY-WORK-AREA1.
 *     03 COPY-WORK-DATA PIC X(10).
 */
public class CopyWorkArea1 {

    // 03 COPY-WORK-DATA PIC X(10)
    private String copyWorkData = "          ";

    public String getCopyWorkData() {
        return copyWorkData;
    }

    public void setCopyWorkData(String value) {
        copyWorkData = CobolUtil.move(value, 10);
    }
}
